package nl.zeesoft.zdbd.midi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;

import nl.zeesoft.zdbd.midi.convertors.PatternSequenceConvertor;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;
import nl.zeesoft.zdk.thread.Waiter;

public class MidiSequencer implements Sequencer {
	private static int							CURR			= 0;
	private static int							NEXT			= 1;
	
	protected Lock								lock			= new Lock();
	protected Synthesizer						synthesizer		= null;
	
	protected float								beatsPerMinute	= 120;
	protected int								nsPerTick		= 520833;
	protected List<MidiSequencerEventListener>	listeners		= new ArrayList<MidiSequencerEventListener>();
	
	protected MidiSequence[]					sequence		= new MidiSequence[2];
	
	protected boolean							paused			= false;
	protected long								startedNs		= 0;
	protected long								prevTick		= 0;
	
	protected CodeRunner						sender			= null;
	protected CodeRunner						eventPublisher	= null;
	
	public MidiSequencer() {
		sender = new CodeRunner(getSendEventsRunCode()) {
			@Override
			protected void caughtException(Exception exception) {
				exception.printStackTrace();
			}
			@Override
			protected void doneCallback() {
				lock.lock(this);
				allSoundOff(synthesizer);
				lock.unlock(this);
			}
		};
		sender.setPriority(Thread.MAX_PRIORITY);
		eventPublisher = new CodeRunner(getPublishSequenceSwitchRunCode());
		calculateNsPerTickNoLock();
	}
	
	public void setSynthesizer(Synthesizer synthesizer) {
		lock.lock(this);
		this.synthesizer = synthesizer;
		lock.unlock(this);
	}
	
	public void addListener(MidiSequencerEventListener listener) {
		lock.lock(this);
		listeners.add(listener);
		lock.unlock(this);
	}
	
	@Override
	public void setTempoInBPM(float beatsPerMinute) {
		lock.lock(this);
		this.beatsPerMinute = beatsPerMinute;
		calculateNsPerTickNoLock();
		lock.unlock(this);
	}


	@Override
	public float getTempoInBPM() {
		lock.lock(this);
		float r = beatsPerMinute;
		lock.unlock(this);
		return r;
	}
	
	@Override
	public void setSequence(Sequence sequence) {
		if (sequence!=null &&
			sequence.getDivisionType() == Sequence.PPQ &&
			sequence.getResolution()==PatternSequenceConvertor.RESOLUTION
			) {
			MidiSequence seq = new MidiSequence(sequence);
			boolean restart = false;
			lock.lock(this);
			if (this.sequence[CURR]!=null && sender.isBusy()) {
				if (seq.getTickLength() < this.sequence[CURR].getTickLength() && getCurrentTick(seq) >= seq.getTickLength()) {
					restart = true;
				}
			}
			setSequenceNoLock(CURR,seq);
			lock.unlock(this);
			if (restart) {
				restart();
			}
		}
	}
	
	public void setNextSequence(Sequence sequence) {
		if (sequence!=null &&
			sequence.getDivisionType() == Sequence.PPQ &&
			sequence.getResolution()==PatternSequenceConvertor.RESOLUTION
			) {
			MidiSequence seq = new MidiSequence(sequence);
			lock.lock(this);
			setSequenceNoLock(NEXT,seq);
			lock.unlock(this);
		}
	}
	
	@Override
	public void setSequence(InputStream stream) throws IOException, InvalidMidiDataException {
		setSequence(MidiSystem.getSequence(stream));
	}

	@Override
	public Sequence getSequence() {
		Sequence r = null;
		lock.lock(this);
		if (sequence[CURR]!=null) {
			r = sequence[CURR].sequence;
		}
		lock.unlock(this);
		return r;
	}
	
	public void restart() {
		if (sender.isBusy()) {
			sender.stop();
		}
		lock.lock(this);
		paused = false;
		lock.unlock(this);
		if (Waiter.waitFor(sender,1000)) {
			start();
		}
	}

	@Override
	public void start() {
		if (!sender.isBusy()) {
			Str err = new Str();
			lock.lock(this);
			if (synthesizer==null) {
				err.sb().append("Synthesizer has not been set");
			} else if (sequence[CURR]==null) {
				err.sb().append("Sequence has not been set");
			}
			if (err.length()==0) {
				if (!paused) {
					startedNs = System.nanoTime();
					prevTick = -1;
				}
				paused = false;
			}
			lock.unlock(this);
			
			if (err.length()>0) {
				Logger.err(this, err);
			} else {
				sender.start();
			}
		}
	}
	
	@Override
	public boolean isRunning() {
		return sender.isBusy();
	}
	
	public void pause() {
		if (sender.isBusy()) {
			sender.stop();
			lock.lock(this);
			paused = true;
			lock.unlock(this);
		}
	}
	
	public void resume() {
		if (!sender.isBusy()) {
			boolean start = false;
			lock.lock(this);
			if (paused) {
				start = true;
			}
			lock.unlock(this);
			if (start) {
				start();
			}
		}
	}
	
	@Override
	public void stop() {
		if (sender.isBusy()) {
			sender.stop();
			lock.lock(this);
			paused = false;
			lock.unlock(this);
		}
	}
	
	protected void setSequenceNoLock(int seq, MidiSequence sequence) {
		this.sequence[seq] = sequence;
	}
	
	protected void calculateNsPerTickNoLock() {
		long cTick = -1;
		if (sender.isBusy()) {
			cTick = getCurrentTick(sequence[CURR]);
		}
		long msPerBeat = (int)(60000000F / beatsPerMinute);
		nsPerTick = (int)((msPerBeat * 1000) / PatternSequenceConvertor.RESOLUTION);
		if (cTick>=0) {
			startedNs = System.nanoTime() - (cTick * nsPerTick);
		}
		sender.setSleepNs(nsPerTick / 2);
	}
		
	protected RunCode getSendEventsRunCode() {
		return new RunCode() {
			@Override
			protected boolean run() {
				lock.lock(this);
				MidiSequence pSeq = sequence[CURR];
				MidiSequence cSeq = sequence[CURR];
				Synthesizer synth = synthesizer;
				long pTick = 0;
				long cTick = 0;
				long sTick = -1;
				long eTick = -1;
				boolean switched = false;
				if (synth!=null && cSeq!=null) {
					// Determine current ticks to play
					pTick = prevTick;
					cTick = getCurrentTick(pSeq);
					
					// Handle sequence restart
					sTick = -1;
					eTick = -1;
					if (cTick<pTick && pTick < pSeq.getTickLength()) {
						sTick = pTick;
						eTick = pSeq.getTickLength();
						pTick = -1;
						
						if (sequence[NEXT]!=null) {
							cSeq = sequence[NEXT];
							sequence[CURR] = cSeq;
							sequence[NEXT] = null;
							switched = true;
						}
					}
					prevTick = cTick;
				}
				lock.unlock(this);
				
				boolean stop = (synth==null || cSeq==null || pSeq==null);
				if (!stop) {
					List<MidiEvent> events = new ArrayList<MidiEvent>();
					if (sTick>=0 && eTick>=0) {
						events.addAll(pSeq.getMidiEventsForTicks(sTick, eTick));
					}
					events.addAll(cSeq.getMidiEventsForTicks(pTick + 1, cTick + 1));
					for (MidiEvent event:events) {
						try {
							synth.getReceiver().send(event.getMessage(),-1);
						} catch (MidiUnavailableException e) {
							stop = true;
							break;
						}
					}
				}
				
				if (switched) {
					eventPublisher.start();
				}
				
				return stop;
			}
		};
	}
	
	protected long getCurrentTick(MidiSequence seq) {
		return ((System.nanoTime() - startedNs) / nsPerTick) % seq.getTickLength();
	}
	
	protected RunCode getPublishSequenceSwitchRunCode() {
		return new RunCode() {
			@Override
			protected boolean run() {
				lock.lock(this);
				List<MidiSequencerEventListener> list = new ArrayList<MidiSequencerEventListener>(listeners);
				lock.unlock(this);
				for (MidiSequencerEventListener listener: list) {
					try {
						listener.switchedSequence();
					} catch (Exception e) {
						Logger.err(this, new Str("Sequencer caught exception while publishing switched sequence event"),e);
					}
				}
				return true;
			}
		};
	}
	
	public static void allSoundOff(Synthesizer synthesizer) {
		MidiChannel[] channels = synthesizer.getChannels();
		for (int c = 0; c < channels.length; c++) {
			MidiChannel channel = channels[c];
			channel.allSoundOff();
		}
	}

	@Override
	public Info getDeviceInfo() {
		// Not implemented
		return null;
	}

	@Override
	public void open() throws MidiUnavailableException {
		// Not implemented
	}

	@Override
	public void close() {
		// Not implemented
	}

	@Override
	public boolean isOpen() {
		// Not implemented
		return true;
	}

	@Override
	public int getMaxReceivers() {
		// Not implemented
		return 0;
	}

	@Override
	public int getMaxTransmitters() {
		// Not implemented
		return 0;
	}

	@Override
	public Receiver getReceiver() throws MidiUnavailableException {
		// Not implemented
		return null;
	}

	@Override
	public List<Receiver> getReceivers() {
		// Not implemented
		return null;
	}

	@Override
	public Transmitter getTransmitter() throws MidiUnavailableException {
		// Not implemented
		return null;
	}

	@Override
	public List<Transmitter> getTransmitters() {
		// Not implemented
		return null;
	}

	@Override
	public void startRecording() {
		// TODO Implement
	}

	@Override
	public void stopRecording() {
		// TODO Implement
	}

	@Override
	public boolean isRecording() {
		// TODO Implement
		return false;
	}

	@Override
	public void recordEnable(Track track, int channel) {
		// Not implemented
	}

	@Override
	public void recordDisable(Track track) {
		// Not implemented
	}

	@Override
	public float getTempoInMPQ() {
		// Not implemented
		return 0;
	}

	@Override
	public void setTempoInMPQ(float mpq) {
		// Not implemented
	}

	@Override
	public void setTempoFactor(float factor) {
		// Not implemented
	}

	@Override
	public float getTempoFactor() {
		// Not implemented
		return 0;
	}

	@Override
	public long getTickLength() {
		// Not implemented
		return 0;
	}

	@Override
	public long getTickPosition() {
		// TODO Implement
		return 0;
	}

	@Override
	public void setTickPosition(long tick) {
		// TODO Implement
	}

	@Override
	public long getMicrosecondLength() {
		// Not implemented
		return 0;
	}

	@Override
	public long getMicrosecondPosition() {
		// Not implemented
		return 0;
	}

	@Override
	public void setMicrosecondPosition(long microseconds) {
		// Not implemented
	}

	@Override
	public void setMasterSyncMode(SyncMode sync) {
		// Not implemented
	}

	@Override
	public SyncMode getMasterSyncMode() {
		// Not implemented
		return null;
	}

	@Override
	public SyncMode[] getMasterSyncModes() {
		// Not implemented
		return null;
	}

	@Override
	public void setSlaveSyncMode(SyncMode sync) {
		// Not implemented
	}

	@Override
	public SyncMode getSlaveSyncMode() {
		// Not implemented
		return null;
	}

	@Override
	public SyncMode[] getSlaveSyncModes() {
		// Not implemented
		return null;
	}

	@Override
	public void setTrackMute(int track, boolean mute) {
		// TODO Implement
	}

	@Override
	public boolean getTrackMute(int track) {
		// TODO Implement
		return false;
	}

	@Override
	public void setTrackSolo(int track, boolean solo) {
		// TODO Implement
	}

	@Override
	public boolean getTrackSolo(int track) {
		// TODO Implement
		return false;
	}

	@Override
	public boolean addMetaEventListener(MetaEventListener listener) {
		// TODO Implement
		return false;
	}

	@Override
	public void removeMetaEventListener(MetaEventListener listener) {
		// TODO Implement
	}

	@Override
	public int[] addControllerEventListener(ControllerEventListener listener, int[] controllers) {
		// Not implemented
		return null;
	}

	@Override
	public int[] removeControllerEventListener(ControllerEventListener listener, int[] controllers) {
		// Not implemented
		return null;
	}

	@Override
	public void setLoopStartPoint(long tick) {
		// Not implemented
	}

	@Override
	public long getLoopStartPoint() {
		// Not implemented
		return 0;
	}

	@Override
	public void setLoopEndPoint(long tick) {
		// Not implemented
	}

	@Override
	public long getLoopEndPoint() {
		// Not implemented
		return getTickLength();
	}

	@Override
	public void setLoopCount(int count) {
		// Not implemented
	}

	@Override
	public int getLoopCount() {
		return Sequencer.LOOP_CONTINUOUSLY;
	}
}
