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

public class CompositionToSequenceConvertor {
	public static final int								TEMPO					= 0x51;
	public static final int								TEXT					= 0x01;
	public static final int								MARKER					= 0x06;
	
	public static final String							SEQUENCE_MARKER			= "SEQ:";
	public static final String							PATTERN_STEP_MARKER		= "PS:";
	
	private static final int[]							CONTROLS				= {Control.EXPRESSION,Control.MODULATION,Control.FILTER};
	private static final int[]							ALL_CONTROLS			= {Control.EXPRESSION,Control.MODULATION,Control.FILTER,
		Control.CHORUS,
		Control.RESONANCE,
		Control.ATTACK,
		Control.DECAY,
		Control.RELEASE,
		Control.VIB_RATE,
		Control.VIB_DEPTH,
		Control.VIB_DELAY
		};
	
	private Messenger									messenger				= null;
	private Composition									composition				= null;
	
	private Sequence									sequence				= null;
	private int											sequenceEndTick			= 0;
	
	private boolean										addMarkers				= false;
	private boolean										externalize				= false;
	private SortedMap<String,InstrumentConfiguration>	instruments				= new TreeMap<String,InstrumentConfiguration>();
	private boolean[]									channelHasNotes			= new boolean[16];
	private List<Pattern>								patterns				= null;
	
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

	public int getSequenceEndTick() {
		return sequenceEndTick;
	}

	public void convertPatternInternal(int patternNumber) {
		convertPattern(patternNumber,false,true);
	}

	public void convertPattern(int patternNumber,boolean externalize,boolean addMarkers) {
		initialize(patternNumber,externalize,addMarkers);
		if (sequence!=null) {
			addEvents(false);
		}
	}
	
	public void convertSequenceInternal() {
		convertSequence(false,true);
	}

	public void convertSequence(boolean externalize,boolean addMarkers) {
		initialize(-1,externalize,addMarkers);
		if (sequence!=null) {
			addEvents(true);
		}
	}

	protected void initialize(int patternNumber, boolean externalize,boolean addMarkers) {
		this.sequence = createSequence();
		if (sequence!=null) {
			addCompositionInfoToSequenceTick(0);
			addSynthesizerConfigurationToSequenceTick(0);
			this.sequenceEndTick = 0;
			this.externalize = externalize;
			this.addMarkers = addMarkers;
			for (InstrumentConfiguration inst: composition.getSynthesizerConfiguration().getInstruments()) {
				instruments.put(inst.getName(),inst);
			}
			for (int c = 0; c < channelHasNotes.length; c++) {
				channelHasNotes[c] = false;
			}
			patterns = getPatternList(patternNumber);
			setSequenceEndTick(patterns);
		}
	}
	
	protected void addEvents(boolean addSequence) {
		List<SeqNote> notes = getPatternNotes(0);
		if (notes.size()>0) {
			addNotes(notes);
			addControls(getPatternControls(0));
		}
		if (addMarkers) {
			addMarkers(0,addSequence);
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
		int volume = echoInst.getVolume();
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
				createEventOnTrack(instTrack,ShortMessage.CONTROL_CHANGE,channel,Control.VOLUME,inst.getVolume(),tick);
				createEventOnTrack(instTrack,ShortMessage.CONTROL_CHANGE,channel,Control.PAN,inst.getPan(),tick);
				createEventOnTrack(instTrack,ShortMessage.CONTROL_CHANGE,channel,Control.REVERB,inst.getLayer1().getReverb(),tick);
				if (inst.getLayer2().getMidiNum()>=0) {
					channel = Instrument.getMidiChannelForInstrument(inst.getName(),1);
					if (channel>=0) {
						createEventOnTrack(instTrack,ShortMessage.PROGRAM_CHANGE,channel,inst.getLayer2().getMidiNum(),0,tick);
						createEventOnTrack(instTrack,ShortMessage.CHANNEL_PRESSURE,channel,inst.getLayer2().getPressure(),0,tick);
						createEventOnTrack(instTrack,ShortMessage.CONTROL_CHANGE,channel,Control.VOLUME,inst.getVolume(),tick);
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
		} else {
			int elem = 0;
			int ticksPerStep = composition.getTicksPerStep();
			int tick = startTick;
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
					tick = tick + ticksPerStep;
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

		for (Pattern p: patterns) {
			int patternSteps = composition.getStepsForPattern(p);
			nextPatternStartTick = nextPatternStartTick + (patternSteps * ticksPerStep);
			for (Note n: p.getNotes()) {
				if (n.step<=patternSteps && !instruments.get(n.instrument).isMuted() &&
					(!n.instrument.equals(Instrument.ECHO) || echo.getInstrument().length()==0)	
					) {
					boolean muted = false;
					if (n.instrument.equals(Instrument.DRUMS)) {
						String name = Drum.getDrumNameForNote(n.note);
						if (name.length()>0) {
							DrumConfiguration drum = composition.getSynthesizerConfiguration().getDrum(name);
							if (drum!=null) {
								muted = drum.isMuted();
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
								int tick = startTick + ((n.step - 1) * ticksPerStep);
								int tickEnd = tick + ((n.duration * ticksPerStep) - 1);
								if (mn instanceof MidiNoteDelayed) {
									MidiNoteDelayed mnd = (MidiNoteDelayed) mn;
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
							}
							noteLayerNum++;
						}
					}
				}
			}
			startTick = nextPatternStartTick;
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
		
		// Build channel control map for CONTROLS
		for (Pattern p: patterns) {
			int patternSteps = composition.getStepsForPattern(p);
			nextPatternStartTick = nextPatternStartTick + (patternSteps * ticksPerStep);
			for (Control c: p.getControls()) {
				if (c.step<=patternSteps && !instruments.get(c.instrument).isMuted() &&
					(!c.instrument.equals(Instrument.ECHO) || echo.getInstrument().length()==0)	
					) {
					int layers = 1;
					if (c.instrument.equals(Instrument.SYNTH_BASS1) ||
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
						if (channel>=0 && channelHasNotes[channel]) {
							SeqControl sc = new SeqControl();
							sc.instrument = c.instrument;
							sc.channel = channel;
							sc.control = c.control;
							sc.tick = startTick + ((c.step - 1) * ticksPerStep);
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
							sc.value = add + (base * c.percentage) / 100;
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
						}
					}
				}
			}
			startTick = nextPatternStartTick;
		}
		
		// Force start control events for ALL_CONTROLS on all instruments
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			if (!instruments.get(Instrument.INSTRUMENTS[i]).isMuted() &&
				(!Instrument.INSTRUMENTS[i].equals(Instrument.ECHO) || echo.getInstrument().length()==0)
				) {
				int layers = 1;
				if (Instrument.INSTRUMENTS[i].equals(Instrument.SYNTH_BASS1) ||
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
					InstrumentLayerConfiguration conf = instruments.get(Instrument.INSTRUMENTS[i]).getLayer(layer % 2);
					int channel = Instrument.getMidiChannelForInstrument(Instrument.INSTRUMENTS[i],layer);
					if (channel>=0) {
						for (int co = 0; co < ALL_CONTROLS.length; co++) {
							if (co<CONTROLS.length) {
								List<SeqControl> list = channelControlMap.get(getId(channel,CONTROLS[co]));
								if (list==null) {
									int value = 0;
									if (CONTROLS[co]==Control.EXPRESSION) {
										value = 127;
									} else if (CONTROLS[co]==Control.MODULATION) {
										value = conf.getModulation();
									} else if (CONTROLS[co]==Control.FILTER) {
										value = conf.getFilter();
									}
									SeqControl sc = new SeqControl();
									sc.instrument = Instrument.INSTRUMENTS[i];
									sc.channel = channel;
									sc.control = CONTROLS[co];
									sc.tick = 0;
									sc.value = value;
									list = new ArrayList<SeqControl>();
									list.add(sc);
									channelControlMap.put(getId(sc),list);
								}
							} else {
								int value = 64;
								if (ALL_CONTROLS[co]==Control.CHORUS) {
									value = conf.getChorus();
								} else if (ALL_CONTROLS[co]==Control.RESONANCE) {
									value = conf.getResonance();
								} else if (ALL_CONTROLS[co]==Control.ATTACK) {
									value = conf.getAttack();
								} else if (ALL_CONTROLS[co]==Control.DECAY) {
									value = conf.getDecay();
								} else if (ALL_CONTROLS[co]==Control.RELEASE) {
									value = conf.getRelease();
								} else if (ALL_CONTROLS[co]==Control.VIB_RATE) {
									value = conf.getVibRate();
								} else if (ALL_CONTROLS[co]==Control.VIB_DEPTH) {
									value = conf.getVibDepth();
								} else if (ALL_CONTROLS[co]==Control.VIB_DELAY) {
									value = conf.getVibDelay();
								}
								SeqControl sc = new SeqControl();
								sc.instrument = Instrument.INSTRUMENTS[i];
								sc.channel = channel;
								sc.control = ALL_CONTROLS[co];
								sc.tick = 0;
								sc.value = value;
								List<SeqControl> list = new ArrayList<SeqControl>();
								list.add(sc);
								channelControlMap.put(getId(sc),list);
							}
						}
					}
				}
			}
		}

		List<SeqControl> echoControls = new ArrayList<SeqControl>();

		// Calculate control changes between ALL_CONTROLS
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			if (!instruments.get(Instrument.INSTRUMENTS[i]).isMuted() &&
				(!Instrument.INSTRUMENTS[i].equals(Instrument.ECHO) || echo.getInstrument().length()==0)
				) {
				int layers = 1;
				if (Instrument.INSTRUMENTS[i].equals(Instrument.SYNTH_BASS1) ||
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
						for (int co = 0; co < ALL_CONTROLS.length; co++) {
							List<SeqControl> list = channelControlMap.get(getId(channel,ALL_CONTROLS[co]));
							int c = 0;
							for (SeqControl sc: list) {
								r.add(sc);
								if ((echo.getInstrument().equals(sc.instrument) && echo.getLayer()==layer) || 
									(sc.instrument.equals(Instrument.ECHO) && echo.getInstrument().length()==0)
									) {
									echoControls.add(sc);
								}
								if ((c+1) < list.size()) {
									SeqControl nsc = list.get((c+1));
									List<SeqControl> slideControls = getSlideControls(sc,nsc);
									for (SeqControl asc: slideControls) {
										r.add(asc);
										if ((echo.getInstrument().equals(sc.instrument) && echo.getLayer()==layer) || 
											(sc.instrument.equals(Instrument.ECHO) && echo.getInstrument().length()==0)
											) {
											echoControls.add(asc);
										}
									}
								}
								c++;
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
