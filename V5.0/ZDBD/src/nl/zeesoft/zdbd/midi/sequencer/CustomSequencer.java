package nl.zeesoft.zdbd.midi.sequencer;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;

public class CustomSequencer {
	private static int		END_OF_SEQUENCE	= 47;
	private static int		CURR			= 0;
	private static int		NEXT			= 1;
	
	protected Lock			lock			= new Lock();
	protected Synthesizer	synthesizer		= null;
	
	protected float			beatsPerMinute	= 120;
	
	protected Sequence[]	sequence		= new Sequence[2];
	protected int[]			nsPerTick		= new int[2];
	protected long[]		lastTick		= new long[2];
	
	protected long			prevNs			= 0;
	protected long			prevTick		= -1;
	protected long			currTick		= 0;
	
	protected CodeRunner	runner			= null;
	
	public CustomSequencer() {
		runner = new CodeRunner(getSendEventsRunCode()) {
			@Override
			protected void caughtException(Exception exception) {
				exception.printStackTrace();
			}
		};
	}
	
	public void setSynthesizer(Synthesizer synthesizer) {
		lock.lock(this);
		this.synthesizer = synthesizer;
		lock.unlock(this);
	}
	
	public void setBeatsPerMinute(int beatsPerMinute) {
		lock.lock(this);
		this.beatsPerMinute = beatsPerMinute;
		calculateNsPerTickNoLock(CURR);
		if (sequence[CURR]!=null) {
			runner.setSleepNs(nsPerTick[CURR]);
		}
		calculateNsPerTickNoLock(NEXT);
		lock.unlock(this);
	}
	
	public void setSequence(Sequence sequence) {
		if (sequence.getDivisionType() == Sequence.PPQ) {
			lock.lock(this);
			setSequenceNoLock(CURR,sequence);
			lock.unlock(this);
		}
	}
	
	public void setNextSequence(Sequence sequence) {
		if (sequence.getDivisionType() == Sequence.PPQ) {
			lock.lock(this);
			setSequenceNoLock(NEXT,sequence);
			lock.unlock(this);
		}
	}
	
	public void start() {
		Str err = new Str();
		lock.lock(this);
		if (synthesizer==null) {
			err.sb().append("Sequence synthesizer has not been set");
		}
		prevNs = System.nanoTime();
		lock.unlock(this);
		if (err.length()>0) {
			Logger.err(this, err);
		} else {
			runner.setSleepNs(nsPerTick[CURR] / 100);
			runner.start();
		}	
	}
	
	public void stop() {
		runner.stop();
	}
	
	public boolean isRunning() {
		return runner.isBusy();
	}
	
	protected void setSequenceNoLock(int seq, Sequence sequence) {
		this.sequence[seq] = sequence;
		calculateNsPerTickNoLock(seq);
		calculateLastTickNoLock(seq);
	}
	
	protected void calculateNsPerTickNoLock(int seq) {
		if (sequence[seq]!=null) {
			long msPerBeat = (int)(60000000F / beatsPerMinute);
			System.out.println("msPerBeat: " + msPerBeat);
			nsPerTick[seq] = (int)((msPerBeat * 1000) / sequence[seq].getResolution());
			System.out.println("nsPerTick: " + nsPerTick[seq]);
		}
	}
	
	protected void calculateLastTickNoLock(int seq) {
		if (sequence[seq]!=null) {
			lastTick[seq] = 0;
			Track[] tracks = sequence[seq].getTracks();
			for (int t = 0; t < tracks.length; t++) {
				for (int i = 0; i < tracks[t].size(); i++) {
					MidiEvent event = tracks[t].get(i);
					if (event.getMessage()!=null && event.getMessage() instanceof MetaMessage) {
						if (((MetaMessage)event.getMessage()).getType()==END_OF_SEQUENCE) {
							if (event.getTick()>lastTick[seq]) {
								lastTick[seq] = event.getTick();
								System.out.println("ticks: " + lastTick[seq]);
							}
						}
					}
				}
			}
		}
	}
	
	protected RunCode getSendEventsRunCode() {
		return new RunCode() {
			@Override
			protected boolean run() {
				lock.lock(this);
				Sequence seq = sequence[CURR];
				if (currTick>=lastTick[CURR]) {
					prevNs = System.nanoTime();
					currTick = 0;
					prevTick = -1;
				} else if (prevTick>=0) {
					long passed = System.nanoTime() - prevNs;
					currTick = prevTick + (int)(passed / nsPerTick[CURR]);
				}
				long pTick = prevTick;
				long cTick = currTick;
				Synthesizer synth = synthesizer;
				lock.unlock(this);
				
				boolean stop = false;
				if (seq!=null) {
					for (long tick = (pTick+1); tick <= cTick; tick++) {
						Track[] tracks = seq.getTracks();
						for (int t = 0; t < tracks.length; t++) {
							for (int i = 0; i < tracks[t].size(); i++) {
								MidiEvent event = tracks[t].get(i);
								if (event.getTick()==tick) {
									try {
										synth.getReceiver().send(event.getMessage(),0);
									} catch (MidiUnavailableException e) {
										stop = true;
									}
								}
							}
						}
					}
					lock.lock(this);
					prevNs = System.nanoTime();
					prevTick = currTick;
					lock.unlock(this);
				} else {
					stop = true;
				}
				return stop;
			}
		};
	}
}
