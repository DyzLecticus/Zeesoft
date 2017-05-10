package nl.zeesoft.zmmt.syntesizer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sound.midi.Synthesizer;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class SynthesizerConfiguration {
	private List<InstrumentConfiguration>	instruments	= new ArrayList<InstrumentConfiguration>();
	private EchoConfiguration				echo		= new EchoConfiguration();
	private List<DrumConfiguration>			drums 		= new ArrayList<DrumConfiguration>();
	
	public SynthesizerConfiguration() {
		initialize();
	}
	
	public SynthesizerConfiguration copy() {
		SynthesizerConfiguration copy = new SynthesizerConfiguration();
		copy.fromJson(toJson());
		return copy;
	}
	
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		for (InstrumentConfiguration inst: instruments) {
			JsElem instElem = new JsElem("instrument");
			json.rootElement.children.add(instElem);
			instElem.children.add(new JsElem("name",inst.getName(),true));
			instElem.children.add(new JsElem("layer1MidiNum","" + inst.getLayer1MidiNum()));
			instElem.children.add(new JsElem("layer1Pressure","" + inst.getLayer1Pressure()));
			instElem.children.add(new JsElem("layer1Reverb","" + inst.getLayer1Reverb()));
			if (!inst.getName().equals(Instrument.DRUMS)) {
				instElem.children.add(new JsElem("layer1BaseOctave","" + inst.getLayer1BaseOctave()));
				instElem.children.add(new JsElem("layer1BaseVelocity","" + inst.getLayer1BaseVelocity()));
				instElem.children.add(new JsElem("layer1AccentVelocity","" + inst.getLayer1AccentVelocity()));
			}
			if (inst.getName().equals(Instrument.SYNTH_BASS1) ||
				inst.getName().equals(Instrument.SYNTH1) || 
				inst.getName().equals(Instrument.LEAD) ||
				inst.getName().equals(Instrument.STRINGS)
				) { 
				instElem.children.add(new JsElem("layer2MidiNum","" + inst.getLayer2MidiNum()));
				instElem.children.add(new JsElem("layer2Pressure","" + inst.getLayer1Pressure()));
				instElem.children.add(new JsElem("layer2Reverb","" + inst.getLayer1Reverb()));
				instElem.children.add(new JsElem("layer2BaseOctave","" + inst.getLayer2BaseOctave()));
				instElem.children.add(new JsElem("layer2BaseVelocity","" + inst.getLayer2BaseVelocity()));
				instElem.children.add(new JsElem("layer2AccentVelocity","" + inst.getLayer2AccentVelocity()));
			}
		}
		for (DrumConfiguration drum: drums) {
			JsElem drumElem = new JsElem("drum");
			json.rootElement.children.add(drumElem);
			drumElem.children.add(new JsElem("name",drum.getName(),true));
			drumElem.children.add(new JsElem("layer1MidiNote","" + drum.getLayer1MidiNote()));
			drumElem.children.add(new JsElem("layer1BaseVelocity","" + drum.getLayer1BaseVelocity()));
			drumElem.children.add(new JsElem("layer1AccentVelocity","" + drum.getLayer1AccentVelocity()));
			drumElem.children.add(new JsElem("layer2MidiNote","" + drum.getLayer2MidiNote()));
			drumElem.children.add(new JsElem("layer2BaseVelocity","" + drum.getLayer2BaseVelocity()));
			drumElem.children.add(new JsElem("layer2AccentVelocity","" + drum.getLayer2AccentVelocity()));
		}
		JsElem echoElem = new JsElem("echo");
		json.rootElement.children.add(echoElem);
		echoElem.children.add(new JsElem("instrument",echo.getInstrument(),true));
		echoElem.children.add(new JsElem("layer","" + echo.getLayer()));
		echoElem.children.add(new JsElem("steps","" + echo.getSteps()));
		echoElem.children.add(new JsElem("velocityPercentage1","" + echo.getVelocityPercentage1()));
		echoElem.children.add(new JsElem("velocityPercentage2","" + echo.getVelocityPercentage2()));
		echoElem.children.add(new JsElem("velocityPercentage3","" + echo.getVelocityPercentage3()));
		echoElem.children.add(new JsElem("reverb1","" + echo.getReverb1()));
		echoElem.children.add(new JsElem("reverb2","" + echo.getReverb2()));
		echoElem.children.add(new JsElem("reverb3","" + echo.getReverb3()));
		return json;
	}
	
	public void fromJson(JsFile json) {
		initialize();
		if (json.rootElement!=null) {
			for (JsElem elem: json.rootElement.children) {
				if (elem.name.equals("instrument")) {
					InstrumentConfiguration inst = new InstrumentConfiguration();
					for (JsElem val: elem.children) {
						if (val.name.equals("name")) {
							inst.setName(val.value.toString());
						} else if (val.name.equals("layer1MidiNum")) {
							inst.setLayer1MidiNum(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer1Reverb")) {
							inst.setLayer1Reverb(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer1Pressure")) {
							inst.setLayer1Pressure(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer1BaseOctave")) {
							inst.setLayer1BaseOctave(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer1BaseVelocity")) {
							inst.setLayer1BaseVelocity(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer1AccentVelocity")) {
							inst.setLayer1AccentVelocity(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer2MidiNum")) {
							inst.setLayer2MidiNum(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer2Reverb")) {
							inst.setLayer2Reverb(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer2Pressure")) {
							inst.setLayer2Pressure(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer2BaseOctave")) {
							inst.setLayer2BaseOctave(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer2BaseVelocity")) {
							inst.setLayer2BaseVelocity(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer2AccentVelocity")) {
							inst.setLayer2AccentVelocity(Integer.parseInt(val.value.toString()));
						}
					}
					if (inst.getName().length()>0) {
						InstrumentConfiguration current = getInstrument(inst.getName());
						if (current!=null) {
							int index = instruments.indexOf(current);
							instruments.remove(index);
							instruments.add(index,inst);
						}
					}
				} else if (elem.name.equals("drum")) {
					DrumConfiguration drum = new DrumConfiguration();
					for (JsElem val: elem.children) {
						if (val.name.equals("name")) {
							drum.setName(val.value.toString());
						} else if (val.name.equals("layer1MidiNote")) {
							drum.setLayer1MidiNote(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer1BaseVelocity")) {
							drum.setLayer1BaseVelocity(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer1AccentVelocity")) {
							drum.setLayer1AccentVelocity(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer2MidiNote")) {
							drum.setLayer2MidiNote(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer2BaseVelocity")) {
							drum.setLayer2BaseVelocity(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer2AccentVelocity")) {
							drum.setLayer2AccentVelocity(Integer.parseInt(val.value.toString()));
						}
					}
					if (drum.getName().length()>0) {
						DrumConfiguration current = getDrum(drum.getName());
						if (current!=null) {
							int index = drums.indexOf(current);
							drums.remove(index);
							drums.add(index,drum);
						}
					}
				} else if (elem.name.equals("echo")) {
					for (JsElem val: elem.children) {
						if (val.name.equals("instrument")) {
							echo.setInstrument(val.value.toString());
						} else if (val.name.equals("layer")) {
							echo.setLayer(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("steps")) {
							echo.setSteps(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("velocityPercentage1")) {
							echo.setVelocityPercentage1(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("velocityPercentage2")) {
							echo.setVelocityPercentage2(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("velocityPercentage3")) {
							echo.setVelocityPercentage3(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("reverb1")) {
							echo.setReverb1(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("reverb2")) {
							echo.setReverb2(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("reverb3")) {
							echo.setReverb3(Integer.parseInt(val.value.toString()));
						}
					}
				}
			}
		}
	}

	public List<InstrumentConfiguration> getInstruments() {
		return instruments;
	}

	public EchoConfiguration getEcho() {
		return echo;
	}

	public List<DrumConfiguration> getDrums() {
		return drums;
	}

	public InstrumentConfiguration getInstrument(String instrument) {
		InstrumentConfiguration r = null;
		for (InstrumentConfiguration inst: instruments) {
			if (inst.getName().equals(instrument)) {
				r = inst;
			}
		}
		return r;
	}

	public DrumConfiguration getDrum(String drum) {
		DrumConfiguration r = null;
		for (DrumConfiguration inst: drums) {
			if (inst.getName().equals(drum)) {
				r = inst;
			}
		}
		return r;
	}

	public void configureMidiSynthesizer(Synthesizer synth) {
		InstrumentConfiguration echoInst = getInstrument(echo.getInstrument());
		if (echoInst!=null) {
			int layerMidiNum = echoInst.getLayer1MidiNum();
			int layerPressure = echoInst.getLayer1Pressure();
			if (echo.getLayer()==2) {
				layerMidiNum = echoInst.getLayer2MidiNum();
				layerPressure = echoInst.getLayer2Pressure();
			}
			if (layerMidiNum>=0) {
				for (int e = 0; e < 3; e++) {
					int channel = Instrument.getMidiChannelForInstrument(Instrument.ECHO,e);
					synth.getChannels()[channel].programChange(layerMidiNum);
					synth.getChannels()[channel].setChannelPressure(layerPressure);
					if (e==0) {
						synth.getChannels()[channel].controlChange(91,echo.getReverb1());
					} else if (e==1) {
						synth.getChannels()[channel].controlChange(91,echo.getReverb2());
					} else if (e==2) {
						synth.getChannels()[channel].controlChange(91,echo.getReverb3());
					}
				}
			}
		}
		for (InstrumentConfiguration inst: instruments) {
			if (!inst.getName().equals(Instrument.ECHO)) {
				int channel = Instrument.getMidiChannelForInstrument(inst.getName(),0);
				synth.getChannels()[channel].programChange(inst.getLayer1MidiNum());
				synth.getChannels()[channel].setChannelPressure(inst.getLayer1Pressure());
				synth.getChannels()[channel].controlChange(91,inst.getLayer1Reverb());
				int layerChannel = Instrument.getMidiChannelForInstrument(inst.getName(),1);
				if (layerChannel>=0 && inst.getLayer2MidiNum()>=0) {
					synth.getChannels()[layerChannel].programChange(inst.getLayer2MidiNum());
					synth.getChannels()[layerChannel].setChannelPressure(inst.getLayer2Pressure());
					synth.getChannels()[layerChannel].controlChange(91,inst.getLayer2Reverb());
				}
			}
		}
	}

	public List<MidiNote> getMidiNotesForNote(String instrument, int note,boolean accent,long msPerStep) {
		List<MidiNote> notes = new ArrayList<MidiNote>();
		if (instrument.equals(Instrument.ECHO)) {
			instrument = echo.getInstrument();
		}
		if (instrument.length()>0) {
			int playNote = getMidiNoteNumberForNote(instrument,note,false);
			if (playNote>=0) {
				int velocity = getVelocityForNote(instrument,note,accent,false);
				if (velocity>=0) {
					MidiNote mn = new MidiNote();
					mn.instrument = instrument;
					mn.note = note;
					mn.channel = Instrument.getMidiChannelForInstrument(instrument,0);
					mn.midiNote = playNote;
					mn.velocity = velocity;
					notes.add(mn);
					int layerNote = getMidiNoteNumberForNote(instrument,note,true);
					if (layerNote>=0) {
						velocity = getVelocityForNote(instrument,note,accent,true);
						mn = new MidiNote();
						mn.instrument = instrument;
						mn.note = note;
						mn.channel = Instrument.getMidiChannelForInstrument(instrument,1);
						mn.midiNote = layerNote;
						mn.velocity = velocity;
						notes.add(mn);
					}
					if (msPerStep>0 && instrument.equals(echo.getInstrument()) && notes.size()>=echo.getLayer()) {
						long now = (new Date()).getTime();
						MidiNote base = notes.get((echo.getLayer()-1));
						for (int e = 0; e < 3; e++) {
							int perc = 0;
							if (e==0) {
								perc = echo.getVelocityPercentage1();
							} else if (e==1) {
								perc = echo.getVelocityPercentage2();
							} else if (e==2) {
								perc = echo.getVelocityPercentage3();
							}
							if (base.velocity>0) {
								velocity = (base.velocity * perc) / 100;
							} else {
								velocity = 0;
							}
							MidiNoteDelayed mnd = new MidiNoteDelayed();
							mnd.instrument = Instrument.ECHO;
							mnd.note = base.note;
							mnd.channel = Instrument.getMidiChannelForInstrument(Instrument.ECHO,e);
							mnd.midiNote = base.midiNote;
							mnd.velocity = velocity;
							mnd.delaySteps = ((e + 1) * echo.getSteps());
							mnd.playDateTime = now + (mnd.delaySteps * msPerStep);
							notes.add(mnd);
						}
					}
				}
			}
		}
		return notes;
	}

	protected int getMidiNoteNumberForNote(String instrument,int note,boolean layer) {
		int r = -1;
		InstrumentConfiguration inst = getInstrument(instrument);
		if (instrument.equals(Instrument.DRUMS)) {
			DrumConfiguration drum = null;
			if (note==36) {
				drum = getDrum(Drum.BASEBEAT);
			} else if (note==37) {
				drum = getDrum(Drum.CLAP);
			} else if (note==38) {
				drum = getDrum(Drum.SNARE);
			} else if (note==39) {
				drum = getDrum(Drum.HIHAT1);
			} else if (note==40) {
				drum = getDrum(Drum.HIHAT2);
			} else if (note==41) {
				drum = getDrum(Drum.TOM1);
			} else if (note==42) {
				drum = getDrum(Drum.TOM2);
			} else if (note==43) {
				drum = getDrum(Drum.RIDE);
			} else if (note==44) {
				drum = getDrum(Drum.CYMBAL);
			} else if (note==45) {
				drum = getDrum(Drum.FX1);
			} else if (note==46) {
				drum = getDrum(Drum.FX2);
			} else if (note==47) {
				drum = getDrum(Drum.FX3);
			}
			if (drum!=null) {
				if (layer) { 
					if (drum.getLayer2MidiNote()>=35 && drum.getLayer2MidiNote()!=drum.getLayer1MidiNote()) {
						r = drum.getLayer2MidiNote();
					}
				} else {
					r = drum.getLayer1MidiNote();
				}
			}
		} else {
			if (layer) { 
				if (inst.getLayer2MidiNum()>=0 && inst.getLayer2MidiNum()!=inst.getLayer1MidiNum()) {
					r = (inst.getLayer2BaseOctave() * 12) + (note - 36);
				}
			} else {
				r = (inst.getLayer1BaseOctave() * 12) + (note - 36);
			}
		}
		return r;
	}

	protected int getVelocityForNote(String instrument,int note,boolean accent,boolean layer) {
		int r = -1;
		InstrumentConfiguration inst = getInstrument(instrument);
		if (instrument.equals(Instrument.DRUMS)) {
			DrumConfiguration drum = null;
			if (note==36) {
				drum = getDrum(Drum.BASEBEAT);
			} else if (note==37) {
				drum = getDrum(Drum.CLAP);
			} else if (note==38) {
				drum = getDrum(Drum.SNARE);
			} else if (note==39) {
				drum = getDrum(Drum.HIHAT1);
			} else if (note==40) {
				drum = getDrum(Drum.HIHAT2);
			} else if (note==41) {
				drum = getDrum(Drum.TOM1);
			} else if (note==42) {
				drum = getDrum(Drum.TOM2);
			} else if (note==43) {
				drum = getDrum(Drum.RIDE);
			} else if (note==44) {
				drum = getDrum(Drum.CYMBAL);
			} else if (note==45) {
				drum = getDrum(Drum.FX1);
			} else if (note==46) {
				drum = getDrum(Drum.FX2);
			} else if (note==47) {
				drum = getDrum(Drum.FX3);
			}
			if (drum!=null) {
				if (layer) {
					if (drum.getLayer2MidiNote()>=35 && drum.getLayer2MidiNote()!=drum.getLayer1MidiNote()) {
						if (accent && drum.getLayer2MidiNote()>=0) {
							r = drum.getLayer2AccentVelocity();
						} else {
							r = drum.getLayer2BaseVelocity();
						}
					}
				} else {
					if (accent) {
						r = drum.getLayer1AccentVelocity();
					} else {
						r = drum.getLayer1BaseVelocity();
					}
				}
			}
		} else {
			if (layer) {
				if (inst.getLayer2MidiNum()>=0 && inst.getLayer2MidiNum()!=inst.getLayer1MidiNum()) {
					if (accent) {
						r = inst.getLayer2AccentVelocity();
					} else {
						r = inst.getLayer2BaseVelocity();
					}
				}
			} else {
				if (accent) {
					r = inst.getLayer1AccentVelocity();
				} else {
					r = inst.getLayer1BaseVelocity();
				}
			}
		}
		return r;
	}

	protected void initialize() {
		instruments.clear();
		drums.clear();
		for (int i = 0; i < (Instrument.INSTRUMENTS.length - 1); i++) {
			InstrumentConfiguration inst = new InstrumentConfiguration();
			inst.setName(Instrument.INSTRUMENTS[i]);
			initializeInstrument(inst);
			instruments.add(inst);
		}
		for (int i = 0; i < Drum.DRUMS.length; i++) {
			DrumConfiguration drum = new DrumConfiguration();
			drum.setName(Drum.DRUMS[i]);
			initializeDrum(drum);
			drums.add(drum);
		}
	}

	protected void initializeInstrument(InstrumentConfiguration instrument) {
		if (instrument.getName().equals(Instrument.SYNTH_BASS1)) {
			instrument.setLayer1MidiNum(34);
			instrument.setLayer1BaseOctave(2);
			instrument.setLayer1BaseVelocity(120);
			instrument.setLayer1AccentVelocity(127);
			instrument.setLayer1Reverb(0);
			instrument.setLayer2MidiNum(38);
			instrument.setLayer2BaseOctave(2);
			instrument.setLayer2BaseVelocity(100);
			instrument.setLayer2AccentVelocity(120);
			instrument.setLayer2Reverb(0);
		} else if (instrument.getName().equals(Instrument.SYNTH_BASS2)) {
			instrument.setLayer1MidiNum(39);
			instrument.setLayer1BaseOctave(2);
			instrument.setLayer1BaseVelocity(120);
			instrument.setLayer1AccentVelocity(127);
			instrument.setLayer1Reverb(0);
		} else if (instrument.getName().equals(Instrument.SYNTH_BASS3)) {
			instrument.setLayer1MidiNum(80);
			instrument.setLayer1BaseOctave(2);
			instrument.setLayer1BaseVelocity(120);
			instrument.setLayer1AccentVelocity(127);
			instrument.setLayer1Reverb(0);
		} else if (instrument.getName().equals(Instrument.SYNTH1)) {
			instrument.setLayer1MidiNum(90);
			instrument.setLayer1BaseOctave(4);
			instrument.setLayer2BaseOctave(4);
		} else if (instrument.getName().equals(Instrument.SYNTH2)) {
			instrument.setLayer1MidiNum(81);
			instrument.setLayer1BaseOctave(4);
		} else if (instrument.getName().equals(Instrument.SYNTH3)) {
			instrument.setLayer1MidiNum(54);
			instrument.setLayer1BaseOctave(4);
		} else if (instrument.getName().equals(Instrument.LEAD)) {
			instrument.setLayer1MidiNum(0);
			instrument.setLayer1BaseOctave(4);
			instrument.setLayer1BaseVelocity(80);
			instrument.setLayer1AccentVelocity(90);
			instrument.setLayer1Reverb(91);
			instrument.setLayer2MidiNum(50);
			instrument.setLayer2BaseOctave(4);
			instrument.setLayer2BaseVelocity(80);
			instrument.setLayer2AccentVelocity(90);
			instrument.setLayer2Reverb(127);
		} else if (instrument.getName().equals(Instrument.STRINGS)) {
			instrument.setLayer1MidiNum(48);
			instrument.setLayer1BaseVelocity(80);
			instrument.setLayer1AccentVelocity(90);
			instrument.setLayer1Reverb(127);
			instrument.setLayer2MidiNum(49);
			instrument.setLayer2BaseOctave(4);
			instrument.setLayer2BaseVelocity(80);
			instrument.setLayer2AccentVelocity(90);
			instrument.setLayer2Reverb(127);
		} else if (instrument.getName().equals(Instrument.DRUMS)) {
			instrument.setLayer1MidiNum(118);
			instrument.setLayer1Reverb(0);
		}
	}

	protected void initializeDrum(DrumConfiguration drum) {
		if (drum.getName().equals(Drum.BASEBEAT)) {
			drum.setLayer1MidiNote(36);
			drum.setLayer1BaseVelocity(120);
			drum.setLayer1AccentVelocity(127);
			drum.setLayer2MidiNote(41);
			drum.setLayer1BaseVelocity(120);
			drum.setLayer1AccentVelocity(127);
		} else if (drum.getName().equals(Drum.CLAP)) {
			drum.setLayer1MidiNote(39);
		} else if (drum.getName().equals(Drum.SNARE)) {
			drum.setLayer1MidiNote(38);
			drum.setLayer2MidiNote(66);
			drum.setLayer2BaseVelocity(60);
			drum.setLayer2AccentVelocity(70);
		} else if (drum.getName().equals(Drum.HIHAT1)) {
			drum.setLayer1MidiNote(42);
		} else if (drum.getName().equals(Drum.HIHAT2)) {
			drum.setLayer1MidiNote(69);
			drum.setLayer2MidiNote(42);
		} else if (drum.getName().equals(Drum.TOM1)) {
			drum.setLayer1MidiNote(43);
		} else if (drum.getName().equals(Drum.TOM2)) {
			drum.setLayer1MidiNote(45);
		} else if (drum.getName().equals(Drum.RIDE)) {
			drum.setLayer1MidiNote(59);
			drum.setLayer1BaseVelocity(80);
			drum.setLayer1AccentVelocity(90);
		} else if (drum.getName().equals(Drum.CYMBAL)) {
			drum.setLayer1MidiNote(57);
		} else if (drum.getName().equals(Drum.FX1)) {
			drum.setLayer1MidiNote(37);
		} else if (drum.getName().equals(Drum.FX2)) {
			drum.setLayer1MidiNote(56);
		} else if (drum.getName().equals(Drum.FX3)) {
			drum.setLayer1MidiNote(76);
		}
	}
}
