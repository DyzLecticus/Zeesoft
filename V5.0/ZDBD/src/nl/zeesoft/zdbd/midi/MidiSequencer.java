package nl.zeesoft.zdbd.midi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;
import nl.zeesoft.zdk.thread.Waitable;
import nl.zeesoft.zdk.thread.Waiter;

public class MidiSequencer implements Sequencer, Waitable {
	protected static int						INITIAL_BUFFER_SIZE	= 123456;
	protected static int						CURR				= 0;
	protected static int						NEXT				= 1;
	
	protected Lock								lock				= new Lock();

	protected float								beatsPerMinute		= 120;
	protected int								nsPerTick			= 520833;

	protected List<MidiSequencerEventListener>	listeners			= new ArrayList<MidiSequencerEventListener>();
	
	protected SynthConfig						synthConfig			= null;

	protected MidiSequence[]					sequence			= new MidiSequence[2];
	protected MidiSequence[]					lfoSequence			= new MidiSequence[2];
	protected MixState[]						mixState			= new MixState[2];
	
	protected boolean							paused				= false;
	protected long								startedNs			= 0;
	protected long								prevTick			= 0;

	protected boolean							recording			= false;
	protected long								recordedTicks		= 0;
	protected MidiSequence						recordBuffer		= null;
	
	protected Lock								recordLock			= new Lock();
	protected MidiSequence						recordedSequence	= null;
	protected long								recordedSeqTicks	= 0;
	
	protected CodeRunner						sender				= null;
	protected CodeRunner						recorder			= null;
	protected CodeRunner						eventPublisher		= null;
	
	public MidiSequencer() {
		sender = new CodeRunner(getSendEventsRunCode()) {
			@Override
			protected void caughtException(Exception exception) {
				exception.printStackTrace();
			}
			@Override
			protected void doneCallback() {
				flushRecordBuffer();
				MidiSys.allSoundOff();
			}
		};
		sender.setPriority(Thread.MAX_PRIORITY);
		recorder = new CodeRunner(getRecorderRunCode());
		recorder.setSleepMs(10);
		recorder.setPriority(Thread.MIN_PRIORITY);
		eventPublisher = new CodeRunner(getPublishSequenceSwitchRunCode());
		eventPublisher.setPriority(Thread.MIN_PRIORITY);
		mixState[CURR] = new MixState();
		mixState[NEXT] = new MixState();
		calculateNsPerTickNoLock();
	}

	@Override
	public void open() throws MidiUnavailableException {
		// Always open
	}

	@Override
	public void close() {
		stop();
		stopRecording();
		sender.stop();
		recorder.stop();
		eventPublisher.stop();

		recordLock.lock(this);
		recordedSequence = null;
		recordedSeqTicks = 0;
		recordLock.unlock(this);
	}

	protected void waitForClose(int waitMs) {
		Waiter.waitFor(sender, waitMs);
		Waiter.waitFor(recorder, waitMs);
		Waiter.waitFor(eventPublisher, waitMs);
	}
	
	@Override
	public boolean isOpen() {
		// Always open
		return true;
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
		boolean recording = this.recording;
		long recordedTicks = this.recordedTicks;
		lock.unlock(this);
		if (recording) {
			recordLock.lock(this);
			if (recordedSequence!=null && recordedSequence.sequence!=null) {
				MidiSequenceUtil.addTempoMetaEventToSequence(recordedSequence.sequence, 0, beatsPerMinute, recordedTicks);
			}
			recordLock.unlock(this);
		}
	}

	@Override
	public float getTempoInBPM() {
		lock.lock(this);
		float r = beatsPerMinute;
		lock.unlock(this);
		return r;
	}
	
	public void setSynthConfig(SynthConfig synthConfig) {
		lock.lock(this);
		this.synthConfig = synthConfig;
		lock.unlock(this);
	}
	
	@Override
	public void setSequence(Sequence sequence) {
		if (sequence!=null &&
			sequence.getDivisionType() == Sequence.PPQ &&
			sequence.getResolution()==MidiSequenceUtil.RESOLUTION
			) {
			MidiSequence seq = new MidiSequence(sequence);
			int generateLFO = -1;
			lock.lock(this);
			if (this.sequence[CURR]!=null && sender.isBusy()) {
				if (lfoSequence[NEXT]==null) {
					generateLFO = NEXT;
				}
				setSequenceNoLock(NEXT,seq);
			} else {
				if (lfoSequence[CURR]==null) {
					generateLFO = CURR;
				}
				setSequenceNoLock(CURR,seq);
			}
			SynthConfig config = synthConfig;
			lock.unlock(this);
			if (config!=null && generateLFO>=0) {
				generateLfo(generateLFO,config,sequence.getTickLength());
			}
		}
	}
	
	public void setNextSequence(Sequence sequence) {
		if (sequence!=null &&
			sequence.getDivisionType() == Sequence.PPQ &&
			sequence.getResolution()==MidiSequenceUtil.RESOLUTION
			) {
			MidiSequence seq = new MidiSequence(sequence);
			boolean generateLFO = false;
			lock.lock(this);
			if (lfoSequence[NEXT]==null) {
				generateLFO = true;
			}
			setSequenceNoLock(NEXT,seq);
			SynthConfig config = synthConfig;
			lock.unlock(this);
			if (config!=null && generateLFO) {
				generateLfo(NEXT,config,sequence.getTickLength());
			}
		}
	}
	
	public void setMixState(MixState state) {
		if (state!=null) {
			state = state.copy();
			lock.lock(this);
			mixState[CURR] = state;
			lock.unlock(this);
		}
	}
	
	public void setNextMixState(MixState state) {
		if (state!=null) {
			state = state.copy();
			lock.lock(this);
			mixState[NEXT] = state;
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
			if (MidiSys.synthesizer==null) {
				err.sb().append("Midi system has not been initialized");
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

	@Override
	public boolean isBusy() {
		return isRunning();
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

	@Override
	public void startRecording() {
		boolean start = false;
		lock.lock(this);
		if (!recording) {
			start = true;
			recording = true;
			recordedTicks = 0;
			recordBuffer = new MidiSequence();
		}
		lock.unlock(this);
		if (start) {
			recordLock.lock(this);
			recordedSequence = new MidiSequence();
			recordedSequence.sequence = MidiSequenceUtil.createSequence(SynthConfig.getTotalTracks());
			MidiSequenceUtil.addTempoMetaEventToSequence(recordedSequence.sequence, 0, beatsPerMinute, 0);
			if (synthConfig!=null) {
				synthConfig.addInitialSynthConfig(recordedSequence.sequence);
			}
			// Initialize buffer to prevent dynamic memory scaling from interrupting sender
			for (long t = 0; t < INITIAL_BUFFER_SIZE; t++) {
				recordedSequence.eventsPerTick.put(t, new HashSet<MidiEvent>());
			}
			recordedSeqTicks = 0;
			recordLock.unlock(this);
			recorder.start();
		}
	}

	@Override
	public void stopRecording() {
		lock.lock(this);
		if (recording) {
			recording = false;
			recorder.stop();
		}
		lock.unlock(this);
	}

	@Override
	public boolean isRecording() {
		lock.lock(this);
		boolean r = recording;
		lock.unlock(this);
		return r;
	}

	public long getRecordedTicks() {
		long r = 0;
		recordLock.lock(this);
		if (recordedSequence!=null && recordedSequence.sequence!=null) {
			r = recordedSeqTicks;
		}
		recordLock.unlock(this);
		return r;
	}

	public Sequence getRecordedSequence() {
		Sequence r = null;
		recordLock.lock(this);
		if (recordedSequence!=null && recordedSequence.sequence!=null) {
			r = MidiSequenceUtil.copySequence(recordedSequence.sequence);
			for (Entry<Long,Set<MidiEvent>> entry: recordedSequence.eventsPerTick.entrySet()) {
				if (entry.getKey()<=recordedSeqTicks) {
					for (MidiEvent event: entry.getValue()) {
						int trackNum = getTrackNumForMidiEvent(event);
						if (trackNum>=0) {
							event.setTick(entry.getKey());
							r.getTracks()[trackNum].add(event);
						}
					}
				}
			}
			MidiSequenceUtil.alignTrackEndings(r,recordedSeqTicks);
		}
		recordLock.unlock(this);
		return r;
	}
	
	protected int getTrackNumForMidiEvent(MidiEvent event) {
		int r = -1;
		if (event.getMessage() instanceof ShortMessage) {
			ShortMessage msg = (ShortMessage) event.getMessage();
			r = SynthConfig.getTrackNumForChannel(msg.getChannel());
		}
		return r;
	}
	
	protected void generateLfo(int seq, SynthConfig config, long ticks) {
		MidiSequence lfo = new MidiSequence(config.generateSequenceForChannelLFOs(ticks));
		if (seq==CURR) {
			config.commitChannelLFOs(ticks);
		}
		lock.lock(this);
		lfoSequence[seq] = lfo;
		lock.unlock(this);
	}
	
	protected void setSequenceNoLock(int seq, MidiSequence sequence) {
		this.sequence[seq] = sequence;
	}
	
	protected void calculateNsPerTickNoLock() {
		long cTick = -1;
		if (sender.isBusy()) {
			cTick = getCurrentTickNoLock(sequence[CURR]);
		}
		long msPerBeat = (int)(60000000F / beatsPerMinute);
		nsPerTick = (int)((msPerBeat * 1000) / MidiSequenceUtil.RESOLUTION);
		if (cTick>=0) {
			startedNs = System.nanoTime() - (cTick * nsPerTick);
		}
		sender.setSleepNs(nsPerTick / 2);
	}

	protected RunCode getSendEventsRunCode() {
		return new RunCode() {
			//private MixState prevMixState = new MixState();
			@Override
			protected boolean run() {
				lock.lock(this);
				MidiSequence pSeq = sequence[CURR];
				MidiSequence cSeq = sequence[CURR];
				MidiSequence lpSeq = lfoSequence[CURR];
				MidiSequence lcSeq = lfoSequence[CURR];
				MixState pMix = mixState[CURR].copy();
				MixState cMix = pMix;
				Synthesizer synth = MidiSys.synthesizer;
				long pTick = 0;
				long cTick = 0;
				long sTick = -1;
				long eTick = -1;
				boolean switched = false;
				if (synth!=null && cSeq!=null) {
					// Determine current ticks to play
					pTick = prevTick;
					cTick = getCurrentTickNoLock(pSeq);
					
					// Handle sequence restart
					sTick = -1;
					eTick = -1;
					if (cTick<pTick && pTick < pSeq.getTickLength()) {
						sTick = pTick;
						eTick = pSeq.getTickLength();
						pTick = -1;
						
						if (sequence[NEXT]!=null) {
							cSeq = sequence[NEXT];
							lcSeq = lfoSequence[NEXT];
							sequence[CURR] = cSeq;
							lfoSequence[CURR] = lcSeq;
							sequence[NEXT] = null;
							lfoSequence[NEXT] = null;
							if (mixState[NEXT]!=null) {
								cMix = mixState[NEXT];
								mixState[CURR] = cMix;
								mixState[NEXT] = cMix.copy();
							}
							switched = true;
						}
					}
					prevTick = cTick;
				}
				boolean record = recording;
				lock.unlock(this);
				
				MidiSequence rSequence = new MidiSequence();
				long rt = 0;
				
				boolean stop = (synth==null || cSeq==null || pSeq==null);
				if (!stop) {
					List<MidiEvent> events = new ArrayList<MidiEvent>();
					if (sTick>=0 && eTick>=0) {
						for (long t = sTick; t < eTick; t++) {
							Set<MidiEvent> tickEvents = new HashSet<MidiEvent>();
							if (lpSeq!=null) {
								Set<MidiEvent> evts = lpSeq.getEventsForTick(t,pMix);
								events.addAll(evts);
								tickEvents.addAll(evts);
							}
							Set<MidiEvent> evts = pSeq.getEventsForTick(t,pMix);
							events.addAll(evts);
							tickEvents.addAll(evts);
							rSequence.eventsPerTick.put(rt, tickEvents);
							rt++;
						}
					}
					for (long t = (pTick + 1); t < (cTick + 1); t++) {
						Set<MidiEvent> tickEvents = new HashSet<MidiEvent>();
						if (lcSeq!=null) {
							Set<MidiEvent> evts = lcSeq.getEventsForTick(t,cMix);
							events.addAll(evts);
							tickEvents.addAll(evts);
						}
						Set<MidiEvent> evts = cSeq.getEventsForTick(t,cMix);
						events.addAll(evts);
						tickEvents.addAll(evts);
						rSequence.eventsPerTick.put(rt, tickEvents);
						rt++;
					}
					for (MidiEvent event:events) {
						try {
							synth.getReceiver().send(event.getMessage(),-1);
						} catch (MidiUnavailableException e) {
							stop = true;
							break;
						}
					}
				}
				
				if (record && rt>0) {
					lock.lock(this);
					for (long t = 0; t < rt; t++) {
						recordBuffer.eventsPerTick.put(recordedTicks, rSequence.eventsPerTick.get(t));
						recordedTicks++;
					}
					lock.unlock(this);
				}

				if (switched) {
					eventPublisher.start();
				}
				
				return stop;
			}
		};
	}
	
	protected long getCurrentTickNoLock(MidiSequence seq) {
		return ((System.nanoTime() - startedNs) / nsPerTick) % seq.getTickLength();
	}
	
	protected RunCode getRecorderRunCode() {
		return new RunCode() {
			@Override
			protected boolean run() {
				flushRecordBuffer();
				return false;
			}
		};
	}
	
	protected void flushRecordBuffer() {
		HashMap<Long,Set<MidiEvent>> events = new HashMap<Long,Set<MidiEvent>>(); 
		lock.lock(this);
		if (recordBuffer!=null) {
			events = new HashMap<Long,Set<MidiEvent>>(recordBuffer.eventsPerTick);
			recordBuffer.eventsPerTick.clear();
		}
		lock.unlock(this);
		if (events!=null && events.size()>0) {
			for (Entry<Long,Set<MidiEvent>> entry: events.entrySet()) {
				Set<MidiEvent> list = new HashSet<MidiEvent>(entry.getValue());
				entry.setValue(new HashSet<MidiEvent>());
				for (MidiEvent event: list) {
					MidiEvent recEvent = new MidiEvent(event.getMessage(),event.getTick());
					entry.getValue().add(recEvent);
				}
			}
			recordLock.lock(this);
			recordedSequence.eventsPerTick.putAll(events);
			long prev = recordedSeqTicks;
			for (Long tick: events.keySet()) {
				if (tick > recordedSeqTicks) {
					recordedSeqTicks = tick;
				}
			}
			for (long t = prev; t < recordedSeqTicks; t++) {
				if (recordedSequence.eventsPerTick.get(t).size()==0) {
					recordedSequence.eventsPerTick.remove(t);
				}
			}
			recordLock.unlock(this);
		}
	}
	
	protected RunCode getPublishSequenceSwitchRunCode() {
		return new RunCode() {
			@Override
			protected boolean run() {
				lock.lock(this);
				if (synthConfig!=null) {
					synthConfig.commitChannelLFOs(sequence[CURR].getTickLength());
				}
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

	@Override
	public Info getDeviceInfo() {
		// Not implemented
		return null;
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
