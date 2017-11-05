package nl.zeesoft.zmmt.sequencer;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.composition.Control;
import nl.zeesoft.zmmt.composition.Note;
import nl.zeesoft.zmmt.composition.Pattern;
import nl.zeesoft.zmmt.synthesizer.Drum;
import nl.zeesoft.zmmt.synthesizer.DrumConfiguration;
import nl.zeesoft.zmmt.synthesizer.EchoConfiguration;
import nl.zeesoft.zmmt.synthesizer.Instrument;
import nl.zeesoft.zmmt.synthesizer.InstrumentConfiguration;
import nl.zeesoft.zmmt.synthesizer.InstrumentLayerConfiguration;
import nl.zeesoft.zmmt.synthesizer.MidiNote;
import nl.zeesoft.zmmt.synthesizer.MidiNoteDelayed;
import nl.zeesoft.zmmt.synthesizer.SynthesizerConfiguration;

public class CompositionToSequenceConvertor {
	public static final int								TEMPO					= 0x51;
	public static final int								TEXT					= 0x01;
	public static final int								MARKER					= 0x06;
	
	public static final String							SEQUENCE_MARKER			= "SEQ:";
	public static final String							PATTERN_STEP_MARKER		= "PS:";
	public static final String							VELOCITY_MARKER			= "VEL:";

	private static final int							START_TICK				= 0;
	
	private static final int[]							DYNAMIC_CONTROLS		= {
		Control.EXPRESSION,
		Control.MODULATION,
		Control.FILTER,
		Control.CHORUS,
		Control.RESONANCE,
		Control.VIB_DEPTH
		};
	private static final int[]							STATIC_CONTROLS			= {
		Control.ATTACK,
		Control.DECAY,
		Control.RELEASE,
		Control.VIB_RATE,
		Control.VIB_DELAY
		};
	private static final int[]							ALL_CONTROLS			= {
		Control.EXPRESSION,
		Control.MODULATION,
		Control.FILTER,
		Control.CHORUS,
		Control.RESONANCE,
		Control.ATTACK,
		Control.DECAY,
		Control.RELEASE,
		Control.VIB_RATE,
		Control.VIB_DEPTH,
		Control.VIB_DELAY
		};
	
	private Messenger									messenger					= null;
	private Composition									composition					= null;
	
	private Sequence									sequence					= null;
	private long										sequenceEndTick				= 0;
	
	private boolean										externalize					= false;
	private boolean										addMarkers					= false;
	private boolean										addSideChainCompression		= false;
	
	private SortedMap<String,InstrumentConfiguration>	instruments					= new TreeMap<String,InstrumentConfiguration>();
	
	private boolean[]									channelHasNotes				= new boolean[16];
	private List<Pattern>								patterns					= null;
	private int[][]										velocityPerStepChannel		= null;
	
	private int[]										sideChainVelocityPerStep	= null;
	private long[]										channelSideChainTickFrom	= null;
	private long[]										channelSideChainTickTo		= null;
	
	public CompositionToSequenceConvertor(Composition composition) {
		this.composition = composition;
	}
	
	public CompositionToSequenceConvertor(Messenger messenger, Composition composition) {
		this.messenger = messenger;
		this.composition = composition;
	}

	public Sequence getSequence() {
		return sequence;
	}

	public long getSequenceEndTick() {
		return sequenceEndTick;
	}

	public void convertPatternInternal(int patternNumber,boolean addSideChainCompression) {
		convertPattern(patternNumber,false,true,addSideChainCompression);
	}

	public void convertPattern(int patternNumber,boolean externalize,boolean addMarkers,boolean addSideChainCompression) {
		initialize(patternNumber,externalize,addMarkers,addSideChainCompression);
		if (sequence!=null) {
			addEvents(false);
		}
	}
	
	public void convertSequenceInternal(boolean addSideChainCompression) {
		convertSequence(false,true,addSideChainCompression);
	}

	public void convertSequence(boolean externalize,boolean addMarkers,boolean addSideChainCompression) {
		initialize(-1,externalize,addMarkers,addSideChainCompression);
		if (sequence!=null) {
			addEvents(true);
		}
	}

	protected void initialize(int patternNumber, boolean externalize,boolean addMarkers,boolean addSideChainCompression) {
		this.sequence = createSequence();
		if (sequence!=null) {
			addCompositionInfoToSequenceTick(START_TICK);
			addSynthesizerConfigurationToSequenceTick(START_TICK);
			this.sequenceEndTick = 0;
			this.externalize = externalize;
			this.addMarkers = addMarkers;
			this.addSideChainCompression = addSideChainCompression;
			for (InstrumentConfiguration inst: composition.getSynthesizerConfiguration().getInstruments()) {
				instruments.put(inst.getName(),inst);
			}
			for (int c = 0; c < channelHasNotes.length; c++) {
				channelHasNotes[c] = false;
			}
			patterns = getPatternList(patternNumber);
			setSequenceEndTick(patterns);
			initializeVelocityArrays(patterns);
		}
	}
	
	protected void addEvents(boolean addSequence) {
		List<SeqNote> notes = getPatternNotes(START_TICK);
		if (notes.size()>0) {
			addNotes(notes);
			List<SeqControl> controls = getPatternControls(START_TICK);
			addControls(controls);
		}
		if (addMarkers) {
			addMarkers(START_TICK,addSequence);
		}
		// Align track endings
		for (int t = 0; t < sequence.getTracks().length; t++) {
			createEventOnTrack(sequence.getTracks()[t],ShortMessage.NOTE_OFF,0,0,0,sequenceEndTick);
		}
	}
	
	protected List<Pattern> getPatternList(int patternNumber) {
		List<Pattern> r = new ArrayList<Pattern>();
		if (patternNumber>=0) {
			Pattern p = composition.getPattern(patternNumber);
			if (p==null) {
				p = new Pattern();
				p.setNumber(patternNumber);
			}
			r.add(p);
		} else {
			for (Integer number: composition.getSequence()) {
				Pattern p = composition.getPattern(number);
				if (p==null) {
					p = new Pattern();
					p.setNumber(patternNumber);
				}
				r.add(p);
			}
		}
		return r;
	}
	
	protected void initializeVelocityArrays(List<Pattern> patterns) {
		if (patterns.size()>0) {
			int steps = 0;
			for (Pattern p: patterns) {
				steps = steps + composition.getStepsForPattern(p);
			}
			velocityPerStepChannel = new int[steps][16];
			if (addSideChainCompression) {
				sideChainVelocityPerStep = new int[steps];
				channelSideChainTickFrom = new long[16];
				channelSideChainTickTo = new long[16];
				for (int c = 0; c < 16; c++) {
					channelSideChainTickFrom[c] = sequenceEndTick;
					channelSideChainTickTo[c] = 0;
				}
			}
			for (int s = 0; s < steps; s++) {
				for (int c = 0; c < 16; c++) {
					velocityPerStepChannel[s][c] = 0;
				}
				if (addSideChainCompression) {
					sideChainVelocityPerStep[s] = 0;
					if (composition.getSynthesizerConfiguration().getSideChainSource().equals(SynthesizerConfiguration.SOURCE_MIDI) && (s % composition.getStepsPerBeat())==0) {
						sideChainVelocityPerStep[s] = 127;
					}
				}
			}
		}
	}
	
	public void setSequenceEndTick(List<Pattern> patterns) {
		sequenceEndTick = 0;
		for (Pattern p: patterns) {
			sequenceEndTick = sequenceEndTick + (composition.getStepsForPattern(p) * composition.getTicksPerStep());
		}
		if (sequenceEndTick>0) {
			sequenceEndTick = sequenceEndTick - 1;
		}
	}

	protected Sequence createSequence() {
		Sequence r = null;
		try {
			r = new Sequence(Sequence.PPQ,Composition.RESOLUTION,(Instrument.INSTRUMENTS.length + 1));
		} catch (InvalidMidiDataException e) {
			if (messenger!=null) {
				messenger.error(this,"Invalid MIDI data",e);
			} else {
				e.printStackTrace();
			}
		}
		return r;
	}

	protected void addCompositionInfoToSequenceTick(int tick) {
		Track track = sequence.getTracks()[0];
		int tempo = (60000000 / composition.getBeatsPerMinute());
		byte[] b = new byte[3];
		int tmp = tempo >> 16;
		b[0] = (byte) tmp;
		tmp = tempo >> 8;
		b[1] = (byte) tmp;
		b[2] = (byte) tempo;
		createMetaEventOnTrack(track,TEMPO,b,b.length,tick);

		String txt = "Name: " + composition.getName();
		byte[] tb = txt.getBytes();
		createMetaEventOnTrack(track,TEXT,tb,tb.length,tick);
		
		txt = "Composer: " + composition.getComposer();
		tb = txt.getBytes();
		createMetaEventOnTrack(track,TEXT,tb,tb.length,tick);
	}

	protected void addSynthesizerConfigurationToSequenceTick(int tick) {
		int track = (Instrument.getIndexForInstrument(Instrument.ECHO) + 1);
		Track echotrack = sequence.getTracks()[track];
		int channels = 3;
		EchoConfiguration echo = composition.getSynthesizerConfiguration().getEcho();
		InstrumentConfiguration echoInst = composition.getSynthesizerConfiguration().getInstrument(echo.getInstrument());
		if (echoInst==null) {
			echoInst = composition.getSynthesizerConfiguration().getInstrument(Instrument.ECHO);
			channels = 2;
		}
		int volume = (echoInst.getVolume() * composition.getSynthesizerConfiguration().getMasterVolume()) / 127;
		int layerMidiNum = echoInst.getLayer((echo.getLayer() - 1)).getMidiNum();
		int layerPressure = echoInst.getLayer((echo.getLayer() - 1)).getPressure();
		if (echo.getLayer()==2) {
			layerMidiNum = echoInst.getLayer2().getMidiNum();
			layerPressure = echoInst.getLayer2().getPressure();
		}
		if (layerMidiNum>=0) {
			for (int e = 0; e < channels; e++) {
				int channel = Instrument.getMidiChannelForInstrument(Instrument.ECHO,e);
				createEventOnTrack(echotrack,ShortMessage.PROGRAM_CHANGE,channel,layerMidiNum,0,tick);
				createEventOnTrack(echotrack,ShortMessage.CHANNEL_PRESSURE,channel,layerPressure,0,tick);
				createEventOnTrack(echotrack,ShortMessage.CONTROL_CHANGE,channel,Control.VOLUME,volume,tick);
				if (e==0) {
					createEventOnTrack(echotrack,ShortMessage.CONTROL_CHANGE,channel,Control.PAN,echo.getPan1(),tick);
					createEventOnTrack(echotrack,ShortMessage.CONTROL_CHANGE,channel,Control.REVERB,echo.getReverb1(),tick);
				} else if (e==1) {
					createEventOnTrack(echotrack,ShortMessage.CONTROL_CHANGE,channel,Control.PAN,echo.getPan2(),tick);
					createEventOnTrack(echotrack,ShortMessage.CONTROL_CHANGE,channel,Control.REVERB,echo.getReverb2(),tick);
				} else if (e==2) {
					createEventOnTrack(echotrack,ShortMessage.CONTROL_CHANGE,channel,Control.PAN,echo.getPan3(),tick);
					createEventOnTrack(echotrack,ShortMessage.CONTROL_CHANGE,channel,Control.REVERB,echo.getReverb3(),tick);
				}
			}
		}
		for (InstrumentConfiguration inst: composition.getSynthesizerConfiguration().getInstruments()) {
			track = (Instrument.getIndexForInstrument(inst.getName()) + 1);
			Track instTrack = sequence.getTracks()[track];
			if (!inst.getName().equals(Instrument.ECHO) || echo.getInstrument().length()==0) {
				int layer = 0;
				if (inst.getName().equals(Instrument.ECHO)) {
					layer = 2;
				}
				int channel = Instrument.getMidiChannelForInstrument(inst.getName(),layer);
				createEventOnTrack(instTrack,ShortMessage.PROGRAM_CHANGE,channel,inst.getLayer1().getMidiNum(),0,tick);
				createEventOnTrack(instTrack,ShortMessage.CHANNEL_PRESSURE,channel,inst.getLayer1().getPressure(),0,tick);
				createEventOnTrack(instTrack,ShortMessage.CONTROL_CHANGE,channel,Control.VOLUME,
					((inst.getVolume() * composition.getSynthesizerConfiguration().getMasterVolume()) / 127),tick);
				createEventOnTrack(instTrack,ShortMessage.CONTROL_CHANGE,channel,Control.PAN,inst.getPan(),tick);
				createEventOnTrack(instTrack,ShortMessage.CONTROL_CHANGE,channel,Control.REVERB,inst.getLayer1().getReverb(),tick);
				if (inst.getLayer2().getMidiNum()>=0) {
					channel = Instrument.getMidiChannelForInstrument(inst.getName(),1);
					if (channel>=0) {
						createEventOnTrack(instTrack,ShortMessage.PROGRAM_CHANGE,channel,inst.getLayer2().getMidiNum(),0,tick);
						createEventOnTrack(instTrack,ShortMessage.CHANNEL_PRESSURE,channel,inst.getLayer2().getPressure(),0,tick);
						createEventOnTrack(instTrack,ShortMessage.CONTROL_CHANGE,channel,Control.VOLUME,
							((inst.getVolume() * composition.getSynthesizerConfiguration().getMasterVolume()) / 127),tick);
						createEventOnTrack(instTrack,ShortMessage.CONTROL_CHANGE,channel,Control.PAN,inst.getPan(),tick);
						createEventOnTrack(instTrack,ShortMessage.CONTROL_CHANGE,channel,Control.REVERB,inst.getLayer2().getReverb(),tick);
					}
				}
			}
		}
	}
	
	protected void addMarkers(int startTick, boolean addSequence) {
		if (patterns.size()==0) {
			String seq = SEQUENCE_MARKER + "-1";
			byte[] data = seq.getBytes();
			createMetaEventOnTrack(sequence.getTracks()[0],MARKER,data,data.length,startTick);
			String ps = PATTERN_STEP_MARKER + "-1:-1";
			data = ps.getBytes();
			createMetaEventOnTrack(sequence.getTracks()[0],MARKER,data,data.length,startTick);
			String vel = VELOCITY_MARKER;
			for (int c = 0; c < 16; c++) {
				if (c>0) {
					vel = vel + ":";
				}
				vel = vel + "0";
			}
			data = vel.getBytes();
			createMetaEventOnTrack(sequence.getTracks()[0],MARKER,data,data.length,startTick);
		} else {
			int elem = 0;
			int ticksPerStep = composition.getTicksPerStep();
			int tick = startTick;
			int step = 0;
			for (Pattern p: patterns) {
				if (addSequence) {
					String seq = SEQUENCE_MARKER + elem;
					byte[] data = seq.getBytes();
					createMetaEventOnTrack(sequence.getTracks()[0],MARKER,data,data.length,tick);
				}
				int patternSteps = composition.getStepsForPattern(p);
				for (int s = 1; s<=patternSteps; s++) {
					if (!addSequence && (s % composition.getStepsPerBeat()) == 1) {
						String seq = SEQUENCE_MARKER + "-1";
						byte[] data = seq.getBytes();
						createMetaEventOnTrack(sequence.getTracks()[0],MARKER,data,data.length,tick);
					}
					String ps = PATTERN_STEP_MARKER + p.getNumber() + ":" + s;
					byte[] data = ps.getBytes();
					createMetaEventOnTrack(sequence.getTracks()[0],MARKER,data,data.length,tick);
					String vel = VELOCITY_MARKER;
					for (int c = 0; c < 16; c++) {
						if (c>0) {
							vel = vel + ":";
						}
						vel = vel + velocityPerStepChannel[step][c];
					}
					data = vel.getBytes();
					createMetaEventOnTrack(sequence.getTracks()[0],MARKER,data,data.length,tick);
					tick = tick + ticksPerStep;
					step = step + 1;
				}
				elem++;
			}
		}
	}
	
	protected void addNotes(List<SeqNote> notes) {
		for (SeqNote sn: notes) {
			int track = (Instrument.getIndexForInstrument(sn.instrument) + 1);
			createEventOnTrack(sequence.getTracks()[track],ShortMessage.NOTE_ON,sn.channel,sn.midiNote,sn.velocity,sn.tickStart);
			createEventOnTrack(sequence.getTracks()[track],ShortMessage.NOTE_OFF,sn.channel,sn.midiNote,0,sn.tickEnd);
		}
	}

	protected void addControls(List<SeqControl> controls) {
		for (SeqControl sc: controls) {
			int track = (Instrument.getIndexForInstrument(sc.instrument) + 1);
			createEventOnTrack(sequence.getTracks()[track],ShortMessage.CONTROL_CHANGE,sc.channel,sc.control,sc.value,sc.tick);
		}
	}

	protected List<SeqNote> getPatternNotes(int startTick) {
		List<SeqNote> r = new ArrayList<SeqNote>();
		
		EchoConfiguration echo = composition.getSynthesizerConfiguration().getEcho();

		int nextPatternStartTick = 0;
		int ticksPerStep = composition.getTicksPerStep();
		int patternStartStep = 0;
		
		for (Pattern p: patterns) {
			int patternSteps = composition.getStepsForPattern(p);
			nextPatternStartTick = nextPatternStartTick + (patternSteps * ticksPerStep);
			for (Note n: p.getNotes()) {
				if (n.step<=patternSteps && n.track<=Composition.TRACKS && !instruments.get(n.instrument).isMuted() &&
					(!n.instrument.equals(Instrument.ECHO) || echo.getInstrument().length()==0)	
					) {
					boolean muted = false;
					boolean isKick = false;
					if (n.instrument.equals(Instrument.DRUMS)) {
						String name = Drum.getDrumNameForNote(n.note);
						if (name.length()>0) {
							DrumConfiguration drum = composition.getSynthesizerConfiguration().getDrum(name);
							if (drum!=null) {
								muted = drum.isMuted();
							}
							if (name.equals(Drum.KICK)) {
								isKick = true;
							}
						}
					}
					if (!muted) {
						List<MidiNote> midiNotes = composition.getSynthesizerConfiguration().getMidiNotesForNote(n.instrument,n.note,n.accent,1);
						int noteLayerNum = 0;
						for (MidiNote mn: midiNotes) {
							if (externalize) {
								externalizeMidiNote(n,noteLayerNum,mn);
							}
							if (mn.midiNote>=0) {
								SeqNote sn = new SeqNote();
								sn.instrument = mn.instrument;
								sn.midiNote = mn.midiNote;
								sn.channel = mn.channel;
								sn.velocity = (mn.velocity * n.velocityPercentage) / 100;
								long tick = startTick + ((n.step - 1) * ticksPerStep);
								long tickEnd = tick + ((n.duration * ticksPerStep) - 1);
								MidiNoteDelayed mnd = null;
								if (mn instanceof MidiNoteDelayed) {
									mnd = (MidiNoteDelayed) mn;
									tick = tick + (mnd.delaySteps * ticksPerStep);
									tickEnd = tickEnd + (mnd.delaySteps * ticksPerStep);
								} else if (tickEnd>=nextPatternStartTick) {
									tickEnd = nextPatternStartTick - 1;
								}
								if (instruments.get(sn.instrument).getHoldPercentage()<100) {
									int hold = (ticksPerStep * instruments.get(sn.instrument).getHoldPercentage()) / 100;
									int subtract = (ticksPerStep - hold);
									tickEnd = (tickEnd - subtract);
									if (tickEnd<=tick) {
										tickEnd = tick + 1;
									}
								}
								if (tick>sequenceEndTick) {
									tick = (tick - sequenceEndTick);
								}
								if (tickEnd>sequenceEndTick) {
									tickEnd = (tickEnd - sequenceEndTick);
								}
								sn.tickStart = tick;
								sn.tickEnd = tickEnd;
								r.add(sn);
								channelHasNotes[sn.channel] = true;

								if (addSideChainCompression) {
									if (sn.tickStart<channelSideChainTickFrom[sn.channel]) {
										channelSideChainTickFrom[sn.channel] = sn.tickStart;
									}
									long scTickEnd = tickEnd + (composition.getBarsPerPattern() * ticksPerStep);
									if (scTickEnd>channelSideChainTickTo[sn.channel]) {
										channelSideChainTickTo[sn.channel]=scTickEnd;
									}
								}
								
								int currentStep = (patternStartStep + (n.step - 1));
								if (mnd!=null) {
									currentStep = currentStep + (mnd.delaySteps);
								}
								for (int s = currentStep; s < (currentStep + n.duration); s++) {
									int setStep = s;
									if (setStep>=velocityPerStepChannel.length) {
										setStep = (setStep % velocityPerStepChannel.length);
									}
									if (mn.velocity>velocityPerStepChannel[setStep][sn.channel]) {
										velocityPerStepChannel[setStep][sn.channel] = mn.velocity;
									}
								}
								if (addSideChainCompression && mnd==null && isKick && 
									composition.getSynthesizerConfiguration().getSideChainSource().equals(SynthesizerConfiguration.SOURCE_KICK) && 
									mn.velocity > sideChainVelocityPerStep[currentStep]
									) {
									sideChainVelocityPerStep[currentStep] = mn.velocity;
								}
							}
							noteLayerNum++;
						}
					}
				}
			}
			startTick = nextPatternStartTick;
			patternStartStep = patternStartStep + patternSteps;
		}
		
		return r;
	}

	protected String getId(SeqControl sc) {
		return getId(sc.channel,sc.control);
	}

	protected String getId(int channel,int control) {
		return String.format("%03d",channel) + ":" + control;
	}
	
	protected List<SeqControl> getPatternControls(int startTick) {
		List<SeqControl> r = new ArrayList<SeqControl>();

		EchoConfiguration echo = composition.getSynthesizerConfiguration().getEcho();
		SortedMap<String,List<SeqControl>> channelControlMap = new TreeMap<String,List<SeqControl>>();

		int nextPatternStartTick = 0;
		int ticksPerStep = composition.getTicksPerStep();
		int currentTick = startTick;
		
		// Build channel control map for DYNAMIC_CONTROLS
		for (Pattern p: patterns) {
			int patternSteps = composition.getStepsForPattern(p);
			nextPatternStartTick = nextPatternStartTick + (patternSteps * ticksPerStep);
			for (Control c: p.getControls()) {
				if (c.step<=patternSteps && !instruments.get(c.instrument).isMuted() &&
					(!c.instrument.equals(Instrument.ECHO) || echo.getInstrument().length()==0)	
					) {
					int layers = 1;
					if (c.instrument.equals(Instrument.BASS1) ||
						c.instrument.equals(Instrument.SYNTH1) ||
						c.instrument.equals(Instrument.LEAD) ||
						c.instrument.equals(Instrument.STRINGS)
						) {
						layers = 2;
					}
					for (int layer = 0; layer < layers; layer++) {
						if (c.instrument.equals(Instrument.ECHO)) {
							layer = 2;
						}
						int channel = Instrument.getMidiChannelForInstrument(c.instrument,layer);
						InstrumentLayerConfiguration conf = instruments.get(c.instrument).getLayer(layer % 2);
						if (channel>=0 && channelHasNotes[channel] && 
							(c.control!=Control.MODULATION || conf.isControlModulation()) &&
							(c.control!=Control.FILTER || conf.isControlFilter())
							) {
							SeqControl sc = new SeqControl();
							sc.instrument = c.instrument;
							sc.channel = channel;
							sc.control = c.control;
							sc.tick = currentTick + ((c.step - 1) * ticksPerStep);
							int base = 0;
							int add = 0;
							if (c.control==Control.EXPRESSION) {
								base = 127;
							} else if (c.control==Control.MODULATION) {
								add = instruments.get(c.instrument).getLayer(layer % 2).getModulation();
								base = 127 - add;
							} else if (c.control==Control.FILTER) {
								base = instruments.get(c.instrument).getLayer(layer % 2).getFilter();
							}
							sc.value = add + ((base * c.percentage) / 100);
							List<SeqControl> list = channelControlMap.get(getId(sc));
							int addIndex = 0;
							if (list==null) {
								list = new ArrayList<SeqControl>();
								channelControlMap.put(getId(sc),list);
							} else {
								int i = 0;
								for (SeqControl tsc: list) {
									if (sc.tick<tsc.tick) {
										break;
									}
									i++;
									addIndex = i;
								}
							}
							list.add(addIndex,sc);
							if (c.control==Control.MODULATION) {
								if (conf.isModToChorus()) {
									SeqControl asc = new SeqControl();
									asc.instrument = c.instrument;
									asc.channel = channel;
									asc.control = Control.CHORUS;
									asc.tick = sc.tick;
									add = instruments.get(c.instrument).getLayer(layer % 2).getChorus();
									base = 127 - add;
									asc.value = add + ((base * c.percentage) / 100);
									list = channelControlMap.get(getId(asc));
									if (list==null) {
										list = new ArrayList<SeqControl>();
										channelControlMap.put(getId(asc),list);
									}
									list.add(addIndex,asc);
								}
								if (conf.isModToResonance()) {
									SeqControl asc = new SeqControl();
									asc.instrument = c.instrument;
									asc.channel = channel;
									asc.control = Control.RESONANCE;
									asc.tick = sc.tick;
									add = instruments.get(c.instrument).getLayer(layer % 2).getResonance();
									base = 127 - add;
									asc.value = add + ((base * c.percentage) / 100);
									list = channelControlMap.get(getId(asc));
									if (list==null) {
										list = new ArrayList<SeqControl>();
										channelControlMap.put(getId(asc),list);
									}
									list.add(addIndex,asc);
								}
								if (conf.isModToVibDepth()) {
									SeqControl asc = new SeqControl();
									asc.instrument = c.instrument;
									asc.channel = channel;
									asc.control = Control.VIB_DEPTH;
									asc.tick = sc.tick;
									add = instruments.get(c.instrument).getLayer(layer % 2).getVibDepth();
									base = 127 - add;
									asc.value = add + ((base * c.percentage) / 100);
									list = channelControlMap.get(getId(asc));
									if (list==null) {
										list = new ArrayList<SeqControl>();
										channelControlMap.put(getId(asc),list);
									}
									list.add(addIndex,asc);
								}
							}
						}
					}
				}
			}
			currentTick = nextPatternStartTick;
		}

		List<InstrumentLayerChannel> instrumentLayers = new ArrayList<InstrumentLayerChannel>();
		
		// Build InstrumentLayerChannel list
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			if (!instruments.get(Instrument.INSTRUMENTS[i]).isMuted() &&
				(!Instrument.INSTRUMENTS[i].equals(Instrument.ECHO) || echo.getInstrument().length()==0)
				) {
				int layers = 1;
				if (Instrument.INSTRUMENTS[i].equals(Instrument.BASS1) ||
					Instrument.INSTRUMENTS[i].equals(Instrument.SYNTH1) ||
					Instrument.INSTRUMENTS[i].equals(Instrument.LEAD) ||
					Instrument.INSTRUMENTS[i].equals(Instrument.STRINGS)
					) {
					layers = 2;
				}
				for (int layer = 0; layer < layers; layer++) {
					if (Instrument.INSTRUMENTS[i].equals(Instrument.ECHO)) {
						layer = 2;
					}
					int channel = Instrument.getMidiChannelForInstrument(Instrument.INSTRUMENTS[i],layer);
					if (channel>=0) {
						InstrumentLayerConfiguration conf = instruments.get(Instrument.INSTRUMENTS[i]).getLayer(layer % 2);
						InstrumentLayerChannel il = new InstrumentLayerChannel();
						il.instrument = Instrument.INSTRUMENTS[i];
						il.index = i;
						il.layer = layer;
						il.channel = channel;
						il.configuration = conf;
						instrumentLayers.add(il);
					}
				}
			}
		}
		
		// Force start control events for DYNAMIC_CONTROLS and STATIC_CONTROLS on all instruments
		for (InstrumentLayerChannel il: instrumentLayers) {
			for (int co = 0; co < DYNAMIC_CONTROLS.length; co++) {
				List<SeqControl> list = channelControlMap.get(getId(il.channel,DYNAMIC_CONTROLS[co]));
				if (list==null || list.get(0).tick>startTick) {
					int value = 0;
					if (DYNAMIC_CONTROLS[co]==Control.EXPRESSION) {
						value = 127;
					} else if (DYNAMIC_CONTROLS[co]==Control.MODULATION) {
						value = il.configuration.getModulation();
					} else if (DYNAMIC_CONTROLS[co]==Control.FILTER) {
						value = il.configuration.getFilter();
					} else if (DYNAMIC_CONTROLS[co]==Control.CHORUS) {
						value = il.configuration.getChorus();
					} else if (DYNAMIC_CONTROLS[co]==Control.RESONANCE) {
						value = il.configuration.getResonance();
					} else if (DYNAMIC_CONTROLS[co]==Control.VIB_DEPTH) {
						value = il.configuration.getVibDepth();
					}
					SeqControl sc = new SeqControl();
					sc.instrument = il.instrument;
					sc.channel = il.channel;
					sc.control = DYNAMIC_CONTROLS[co];
					sc.tick = startTick;
					sc.value = value;
					if (list==null) {
						list = new ArrayList<SeqControl>();
						channelControlMap.put(getId(sc),list);
					}
					list.add(0,sc);
				}
			}
			for (int co = 0; co < STATIC_CONTROLS.length; co++) {
				int value = 64;
				if (STATIC_CONTROLS[co]==Control.ATTACK) {
					value = il.configuration.getAttack();
				} else if (STATIC_CONTROLS[co]==Control.DECAY) {
					value = il.configuration.getDecay();
				} else if (STATIC_CONTROLS[co]==Control.RELEASE) {
					value = il.configuration.getRelease();
				} else if (STATIC_CONTROLS[co]==Control.VIB_RATE) {
					value = il.configuration.getVibRate();
				} else if (STATIC_CONTROLS[co]==Control.VIB_DELAY) {
					value = il.configuration.getVibDelay();
				}
				SeqControl sc = new SeqControl();
				sc.instrument = il.instrument;
				sc.channel = il.channel;
				sc.control = STATIC_CONTROLS[co];
				sc.tick = startTick;
				sc.value = value;
				List<SeqControl> list = new ArrayList<SeqControl>();
				list.add(sc);
				channelControlMap.put(getId(sc),list);
			}
		}

		List<SeqControl> echoControls = new ArrayList<SeqControl>();
		SortedMap<String,List<SeqControl>> channelExpressionControlMap = new TreeMap<String,List<SeqControl>>();
		
		// Calculate control changes between ALL_CONTROLS
		for (InstrumentLayerChannel il: instrumentLayers) {
			if (channelHasNotes[il.channel]) {
				List<SeqControl> expressionControls = new ArrayList<SeqControl>();
				channelExpressionControlMap.put("" + il.channel,expressionControls);
				for (int co = 0; co < ALL_CONTROLS.length; co++) {
					List<SeqControl> list = channelControlMap.get(getId(il.channel,ALL_CONTROLS[co]));
					int c = 0;
					for (SeqControl sc: list) {
						r.add(sc);
						if ((echo.getInstrument().equals(sc.instrument) && (echo.getLayer()-1)==il.layer) || 
							(sc.instrument.equals(Instrument.ECHO) && echo.getInstrument().length()==0)
							) {
							echoControls.add(sc);
						}
						if (addSideChainCompression && ALL_CONTROLS[co]==Control.EXPRESSION) {
							expressionControls.add(sc);
						}
						if ((c+1) < list.size()) {
							SeqControl nsc = list.get((c+1));
							List<SeqControl> slideControls = getSlideControls(sc,nsc);
							for (SeqControl asc: slideControls) {
								r.add(asc);
								if ((echo.getInstrument().equals(sc.instrument) && (echo.getLayer()-1)==il.layer) || 
									(sc.instrument.equals(Instrument.ECHO) && echo.getInstrument().length()==0)
									) {
									echoControls.add(asc);
								}
								if (addSideChainCompression && ALL_CONTROLS[co]==Control.EXPRESSION) {
									expressionControls.add(asc);
								}
							}
						}
						c++;
					}
				}
			}
		}

		if (addSideChainCompression) {
			// Apply side chain compression to volume controls
			for (InstrumentLayerChannel il: instrumentLayers) {
				if (channelHasNotes[il.channel] && instruments.get(il.instrument).getSideChainPercentage()>0) {
					List<SeqControl> expressionControls = channelExpressionControlMap.get("" + il.channel);
					boolean toEchoControls = false;
					for (int s = 0; s < sideChainVelocityPerStep.length; s++) {
						if (sideChainVelocityPerStep[s]>0) {
							long maxTick = 0;
							for (int sn = (s + 1); sn < sideChainVelocityPerStep.length; sn++) {
								if (sideChainVelocityPerStep[sn]>0 || (sn - s) > 24) {
									maxTick = (sn * ticksPerStep - 1);
									break;
								}
							}
							if (maxTick==0) {
								maxTick = sequenceEndTick;
							}
							long fromTick = s * ticksPerStep;
							long toTick = fromTick + (long) ((
								composition.getSynthesizerConfiguration().getSideChainAttack() + 
								composition.getSynthesizerConfiguration().getSideChainSustain() + 
								composition.getSynthesizerConfiguration().getSideChainRelease()
								) * ticksPerStep);
							if (toTick>maxTick) {
								toTick=maxTick;
							}
							if (toTick>=channelSideChainTickFrom[il.channel] &&
								fromTick<=channelSideChainTickTo[il.channel]
								) {
								int fromExpression = 127;
								int toExpression = 127;
								List<SeqControl> removeControls = new ArrayList<SeqControl>();
								for (SeqControl ctrl: expressionControls) {
									if (ctrl.tick<=fromTick) {
										fromExpression = ctrl.value;
									}
									if (ctrl.tick<=toTick) {
										toExpression = ctrl.value;
									}
									if (ctrl.tick>=fromTick && ctrl.tick<=toTick) {
										removeControls.add(ctrl);
									}
									if (ctrl.tick>toTick) {
										break;
									}
								}
								if (fromExpression>0 || toExpression>0) {
									for (SeqControl ctrl: removeControls) {
										r.remove(ctrl);
										if (echoControls.contains(ctrl)) {
											echoControls.remove(ctrl);
											toEchoControls = true;
										}
									}
									
									int percentage = (instruments.get(il.instrument).getSideChainPercentage() * sideChainVelocityPerStep[s]) / 127;
									
									// Start
									SeqControl sc = new SeqControl();
									sc.instrument = il.instrument;
									sc.channel = il.channel;
									sc.control = Control.EXPRESSION;
									sc.tick = fromTick;
									sc.value = fromExpression;
									r.add(sc);
									if (toEchoControls) {
										echoControls.add(sc);
									}
				
									SeqControl nsc = null;
									long nextTick = 0; 
											
									int value = (fromExpression * (100 - percentage)) / 100;
									if (value<0) {
										value = 0;
									}
									if (value!=fromExpression) {
										// Attack
										nextTick = fromTick + (long) (composition.getSynthesizerConfiguration().getSideChainAttack() * ticksPerStep);
										nsc = new SeqControl();
										nsc.instrument = il.instrument;
										nsc.channel = il.channel;
										nsc.control = Control.EXPRESSION;
										nsc.tick = nextTick;
										nsc.value = value;
										List<SeqControl> slideControls = getSlideControls(sc,nsc);
										for (SeqControl asc: slideControls) {
											if (asc.tick<toTick) {
												r.add(asc);
												if (toEchoControls) {
													echoControls.add(asc);
												}
											}
										}
										if (nsc.tick<=toTick) {
											r.add(nsc);
											if (toEchoControls) {
												echoControls.add(nsc);
											}
										}
										sc = nsc;
									}
									
									if (value!=toExpression) {
										// Sustain
										nextTick = fromTick + (long) ((
											composition.getSynthesizerConfiguration().getSideChainAttack() + 
											composition.getSynthesizerConfiguration().getSideChainSustain()
											) * ticksPerStep);
										nsc = new SeqControl();
										nsc.instrument = il.instrument;
										nsc.channel = il.channel;
										nsc.control = Control.EXPRESSION;
										nsc.tick = nextTick;
										nsc.value = value;
										if (nsc.tick<=toTick) {
											r.add(nsc);
											if (toEchoControls) {
												echoControls.add(nsc);
											}
										}
										sc = nsc;
									}
				
									// Release
									nsc = new SeqControl();
									nsc.instrument = il.instrument;
									nsc.channel = il.channel;
									nsc.control = Control.EXPRESSION;
									nsc.tick = toTick;
									nsc.value = toExpression;
									if (value!=toExpression) {
										List<SeqControl> slideControls = getSlideControls(sc,nsc);
										for (SeqControl asc: slideControls) {
											if (asc.tick<toTick) {
												r.add(asc);
												if (toEchoControls) {
													echoControls.add(asc);
												}
											}
										}
									}
									if (nsc.tick<=toTick) {
										r.add(nsc);
										if (toEchoControls) {
											echoControls.add(nsc);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		// Copy echo controls to echo channels 
		if (echoControls.size()>0) {
			for (SeqControl sc: echoControls) {
				int layers = 3;
				if (echo.getInstrument().length()==0) {
					layers = 2;
				}
				for (int l = 0; l < layers; l++) {
					long tick = sc.tick + ((l * echo.getSteps()) * ticksPerStep);
					if (tick>sequenceEndTick) {
						tick = (tick - sequenceEndTick);
					}
					SeqControl asc = new SeqControl();
					asc.instrument = sc.instrument;
					asc.channel = Instrument.getMidiChannelForInstrument(Instrument.ECHO,l);
					asc.control = sc.control;
					asc.tick = tick;
					asc.value = sc.value;
					r.add(asc);
				}
			}
		}

		return r;
	}
	
	protected List<SeqControl> getSlideControls(SeqControl sc, SeqControl nsc) {
		List<SeqControl> r = new ArrayList<SeqControl>();
		long tickDiff = nsc.tick - sc.tick;
		int valueStart = sc.value;
		int valueEnd = nsc.value;
		int valueDiff = 0;
		boolean add = true;
		if (valueStart<valueEnd) {
			valueDiff = (valueEnd - valueStart);
			add = true;
		} else if (valueStart>valueEnd) {
			valueDiff = (valueStart - valueEnd);
			add = false;
		}
		valueDiff = (valueDiff - 1);
		if (valueDiff>1) {
			if (tickDiff>valueDiff) {
				long ticksPerValueChange = (tickDiff / valueDiff);
				for (int vc = 1; vc < valueDiff; vc++) {
					int value = sc.value;
					if (add) {
						value = value + vc;
					} else {
						value = value - vc;
					}
					SeqControl asc = new SeqControl();
					asc.instrument = sc.instrument;
					asc.channel = sc.channel;
					asc.control = sc.control;
					asc.tick = sc.tick + (vc * ticksPerValueChange);
					asc.value = value;
					r.add(asc);
				}
			} else {
				int valueChangePerTick = (valueDiff / (int) tickDiff);
				if (valueChangePerTick>0) {
					for (int tick = 1; tick < tickDiff; tick++) {
						int value = sc.value;
						if (add) {
							value = value + (tick * valueChangePerTick);
						} else {
							value = value - (tick * valueChangePerTick);
						}
						SeqControl asc = new SeqControl();
						asc.instrument = sc.instrument;
						asc.channel = sc.channel;
						asc.control = sc.control;
						asc.tick = sc.tick + tick;
						asc.value = value;
						r.add(asc);
					}
				}
			}
		}
		return r;
	}
	
	protected void createEventOnTrack(Track track, int type, int channel, int num, int val, long tick) {
		ShortMessage message = new ShortMessage();
		try {
			message.setMessage(type,channel,num,val); 
			MidiEvent event = new MidiEvent(message,tick);
			track.add(event);
		} catch (InvalidMidiDataException e) {
			if (messenger!=null) {
				messenger.error(this,"Invalid MIDI data",e);
			} else {
				e.printStackTrace();
			}
		}
	}
	
	protected void createMetaEventOnTrack(Track track, int type, byte[] data, int length, long tick) {
		MetaMessage message = new MetaMessage();
		try {
			message.setMessage(type,data,length);
			MidiEvent event = new MidiEvent(message,tick);
			track.add(event);
		} catch (InvalidMidiDataException e) {
			if (messenger!=null) {
				messenger.error(this,"Invalid MIDI data",e);
			} else {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Used to externalize MIDI notes.
	 * Set the midiNote.midiNote to -1 to exclude the note from the sequence.
	 * 
	 * @param note The pattern note
	 * @param noteLayerNum For non MidiNoteDelayed objects, this will correspond with the technical origin layer number (0 - 1)
	 * @param midiNote The MIDI note corresponding to the pattern
	 */
	protected void externalizeMidiNote(Note note,int noteLayerNum,MidiNote midiNote) {
		if (note.instrument.equals(Instrument.DRUMS)) {
			if (noteLayerNum==0) {
				midiNote.midiNote = getExternalDrumMidiNoteForNote(note.note);
			} else {
				midiNote.midiNote = -1;
			}
		}
	}
	
	protected int getExternalDrumMidiNoteForNote(int patternNote) {
		int r = 36;
		String name = Drum.getDrumNameForNote(patternNote);
		if (name.equals(Drum.KICK)) {
			r = 36;
		} else if (name.equals(Drum.SNARE)) {
			r = 40; 
		} else if (name.equals(Drum.HIHAT1)) {
			r = 42;
		} else if (name.equals(Drum.HIHAT2)) {
			r = 46; 
		} else if (name.equals(Drum.CLAP)) {
			r = 39; 
		} else if (name.equals(Drum.TOM1)) {
			r = 41; 
		} else if (name.equals(Drum.TOM2)) {
			r = 43; 
		} else if (name.equals(Drum.RIDE)) {
			r = 51; 
		} else if (name.equals(Drum.CYMBAL)) {
			r = 49; 
		} else if (name.equals(Drum.FX1)) {
			r = 76; 
		} else if (name.equals(Drum.FX2)) {
			r = 80; 
		} else if (name.equals(Drum.FX3)) {
			r = 56; 
		}
		return r;
	}
}
