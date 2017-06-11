package nl.zeesoft.zmmt.synthesizer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sound.midi.Synthesizer;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zmmt.composition.Control;

public class SynthesizerConfiguration {
	private List<InstrumentConfiguration>	instruments	= new ArrayList<InstrumentConfiguration>();
	private EchoConfiguration				echo		= new EchoConfiguration();
	private List<DrumConfiguration>			drums 		= new ArrayList<DrumConfiguration>();
	
	public SynthesizerConfiguration() {
		initialize();
	}
	
	public SynthesizerConfiguration copy() {
		SynthesizerConfiguration copy = new SynthesizerConfiguration();
		copy.getInstruments().clear();
		for (InstrumentConfiguration inst: instruments) {
			copy.getInstruments().add(inst.copy());
		}
		copy.setEcho(echo.copy());
		copy.getDrums().clear();
		for (DrumConfiguration drum: drums) {
			copy.getDrums().add(drum.copy());
		}
		return copy;
	}
	
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		for (InstrumentConfiguration inst: instruments) {
			JsElem instElem = new JsElem("instrument");
			json.rootElement.children.add(instElem);
			instElem.children.add(new JsElem("name",inst.getName(),true));
			instElem.children.add(new JsElem("layer1MidiNum","" + inst.getLayer1().getMidiNum()));
			instElem.children.add(new JsElem("layer1Volume","" + inst.getLayer1().getVolume()));
			instElem.children.add(new JsElem("layer1Filter","" + inst.getLayer1().getFilter()));
			instElem.children.add(new JsElem("layer1Chorus","" + inst.getLayer1().getChorus()));
			instElem.children.add(new JsElem("layer1Pressure","" + inst.getLayer1().getPressure()));
			instElem.children.add(new JsElem("layer1Pan","" + inst.getLayer1().getPan()));
			instElem.children.add(new JsElem("layer1Reverb","" + inst.getLayer1().getReverb()));
			instElem.children.add(new JsElem("layer1Modulation","" + inst.getLayer1().getModulation()));
			if (!inst.getName().equals(Instrument.DRUMS)) {
				instElem.children.add(new JsElem("layer1BaseOctave","" + inst.getLayer1().getBaseOctave()));
				instElem.children.add(new JsElem("layer1BaseVelocity","" + inst.getLayer1().getBaseVelocity()));
				instElem.children.add(new JsElem("layer1AccentVelocity","" + inst.getLayer1().getAccentVelocity()));
			}
			if (inst.getName().equals(Instrument.SYNTH_BASS1) ||
				inst.getName().equals(Instrument.SYNTH1) || 
				inst.getName().equals(Instrument.LEAD) ||
				inst.getName().equals(Instrument.STRINGS)
				) { 
				instElem.children.add(new JsElem("layer2MidiNum","" + inst.getLayer2().getMidiNum()));
				instElem.children.add(new JsElem("layer2Volume","" + inst.getLayer2().getVolume()));
				instElem.children.add(new JsElem("layer2Filter","" + inst.getLayer2().getFilter()));
				instElem.children.add(new JsElem("layer2Chorus","" + inst.getLayer2().getChorus()));
				instElem.children.add(new JsElem("layer2Pressure","" + inst.getLayer2().getPressure()));
				instElem.children.add(new JsElem("layer2Pan","" + inst.getLayer2().getPan()));
				instElem.children.add(new JsElem("layer2Reverb","" + inst.getLayer2().getReverb()));
				instElem.children.add(new JsElem("layer2BaseOctave","" + inst.getLayer2().getBaseOctave()));
				instElem.children.add(new JsElem("layer2BaseVelocity","" + inst.getLayer2().getBaseVelocity()));
				instElem.children.add(new JsElem("layer2AccentVelocity","" + inst.getLayer2().getAccentVelocity()));
				instElem.children.add(new JsElem("layer2Modulation","" + inst.getLayer2().getModulation()));
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
		echoElem.children.add(new JsElem("pan1","" + echo.getPan1()));
		echoElem.children.add(new JsElem("pan2","" + echo.getPan2()));
		echoElem.children.add(new JsElem("pan3","" + echo.getPan3()));
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
						// Layer 1
						} else if (val.name.equals("layer1MidiNum")) {
							inst.getLayer1().setMidiNum(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer1Volume")) {
							inst.getLayer1().setVolume(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer1Filter")) {
							inst.getLayer1().setFilter(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer1Chorus")) {
							inst.getLayer1().setChorus(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer1Pressure")) {
							inst.getLayer1().setPressure(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer1Pan")) {
							inst.getLayer1().setPan(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer1Reverb")) {
							inst.getLayer1().setReverb(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer1Modulation")) {
							inst.getLayer1().setModulation(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer1BaseOctave")) {
							inst.getLayer1().setBaseOctave(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer1BaseVelocity")) {
							inst.getLayer1().setBaseVelocity(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer1AccentVelocity")) {
							inst.getLayer1().setAccentVelocity(Integer.parseInt(val.value.toString()));
						// Layer 2
						} else if (val.name.equals("layer2MidiNum")) {
							inst.getLayer2().setMidiNum(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer2Volume")) {
							inst.getLayer2().setVolume(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer2Filter")) {
							inst.getLayer2().setFilter(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer2Chorus")) {
							inst.getLayer2().setChorus(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer2Pressure")) {
							inst.getLayer2().setPressure(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer2Pan")) {
							inst.getLayer2().setPan(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer2Reverb")) {
							inst.getLayer2().setReverb(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer2Modulation")) {
							inst.getLayer2().setModulation(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer2BaseOctave")) {
							inst.getLayer2().setBaseOctave(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer2BaseVelocity")) {
							inst.getLayer2().setBaseVelocity(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layer2AccentVelocity")) {
							inst.getLayer2().setAccentVelocity(Integer.parseInt(val.value.toString()));
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
						} else if (val.name.equals("pan1")) {
							echo.setPan1(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("pan2")) {
							echo.setPan2(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("pan3")) {
							echo.setPan3(Integer.parseInt(val.value.toString()));
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

	public void setEcho(EchoConfiguration echo) {
		this.echo = echo;
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
			int layerMidiNum = echoInst.getLayer1().getMidiNum();
			int layerPressure = echoInst.getLayer1().getPressure();
			int layerModulation = echoInst.getLayer1().getModulation();
			int layerVolume = echoInst.getLayer1().getVolume();
			int layerFilter = echoInst.getLayer1().getFilter();
			int layerChorus = echoInst.getLayer1().getChorus();
			if (echo.getLayer()==2) {
				layerMidiNum = echoInst.getLayer2().getMidiNum();
				layerPressure = echoInst.getLayer2().getPressure();
				layerModulation = echoInst.getLayer2().getModulation();
				layerVolume = echoInst.getLayer2().getVolume();
				layerFilter = echoInst.getLayer2().getFilter();
				layerChorus = echoInst.getLayer2().getChorus();
			}
			if (layerMidiNum>=0) {
				for (int e = 0; e < 3; e++) {
					int channel = Instrument.getMidiChannelForInstrument(Instrument.ECHO,e);
					synth.getChannels()[channel].programChange(layerMidiNum);
					synth.getChannels()[channel].setChannelPressure(layerPressure);
					synth.getChannels()[channel].controlChange(Control.MODULATION,layerModulation);
					synth.getChannels()[channel].controlChange(Control.VOLUME,layerVolume);
					synth.getChannels()[channel].controlChange(Control.FILTER,layerFilter);
					synth.getChannels()[channel].controlChange(Control.CHORUS,layerChorus);
					if (e==0) {
						synth.getChannels()[channel].controlChange(Control.PAN,echo.getPan1());
						synth.getChannels()[channel].controlChange(Control.REVERB,echo.getReverb1());
					} else if (e==1) {
						synth.getChannels()[channel].controlChange(Control.PAN,echo.getPan2());
						synth.getChannels()[channel].controlChange(Control.REVERB,echo.getReverb2());
					} else if (e==2) {
						synth.getChannels()[channel].controlChange(Control.PAN,echo.getPan3());
						synth.getChannels()[channel].controlChange(Control.REVERB,echo.getReverb3());
					}
				}
			}
		}
		for (InstrumentConfiguration inst: instruments) {
			if (!inst.getName().equals(Instrument.ECHO)) {
				int channel = Instrument.getMidiChannelForInstrument(inst.getName(),0);
				synth.getChannels()[channel].programChange(inst.getLayer1().getMidiNum());
				synth.getChannels()[channel].setChannelPressure(inst.getLayer1().getPressure());
				synth.getChannels()[channel].controlChange(Control.PAN,inst.getLayer1().getPan());
				synth.getChannels()[channel].controlChange(Control.REVERB,inst.getLayer1().getReverb());
				synth.getChannels()[channel].controlChange(Control.MODULATION,inst.getLayer1().getModulation());
				synth.getChannels()[channel].controlChange(Control.VOLUME,inst.getLayer1().getVolume());
				synth.getChannels()[channel].controlChange(Control.FILTER,inst.getLayer1().getFilter());
				synth.getChannels()[channel].controlChange(Control.CHORUS,inst.getLayer1().getChorus());
				if (inst.getLayer2().getMidiNum()>=0) {
					channel = Instrument.getMidiChannelForInstrument(inst.getName(),1);
					if (channel>=0) {
						synth.getChannels()[channel].programChange(inst.getLayer2().getMidiNum());
						synth.getChannels()[channel].setChannelPressure(inst.getLayer2().getPressure());
						synth.getChannels()[channel].controlChange(Control.PAN,inst.getLayer2().getPan());
						synth.getChannels()[channel].controlChange(Control.REVERB,inst.getLayer2().getReverb());
						synth.getChannels()[channel].controlChange(Control.MODULATION,inst.getLayer2().getModulation());
						synth.getChannels()[channel].controlChange(Control.VOLUME,inst.getLayer2().getVolume());
						synth.getChannels()[channel].controlChange(Control.FILTER,inst.getLayer2().getFilter());
						synth.getChannels()[channel].controlChange(Control.CHORUS,inst.getLayer2().getChorus());
					}
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
				drum = getDrum(Drum.KICK);
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
				if (inst.getLayer2().getMidiNum()>=0 && inst.getLayer2().getMidiNum()!=inst.getLayer1().getMidiNum()) {
					r = (inst.getLayer2().getBaseOctave() * 12) + (note - 36);
				}
			} else {
				r = (inst.getLayer1().getBaseOctave() * 12) + (note - 36);
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
				drum = getDrum(Drum.KICK);
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
				if (inst.getLayer2().getMidiNum()>=0 && inst.getLayer2().getMidiNum()!=inst.getLayer1().getMidiNum()) {
					if (accent) {
						r = inst.getLayer2().getAccentVelocity();
					} else {
						r = inst.getLayer2().getBaseVelocity();
					}
				}
			} else {
				if (accent) {
					r = inst.getLayer1().getAccentVelocity();
				} else {
					r = inst.getLayer1().getBaseVelocity();
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
			instrument.getLayer1().setMidiNum(34);
			instrument.getLayer1().setBaseOctave(3);
			instrument.getLayer2().setMidiNum(38);
			instrument.getLayer2().setBaseOctave(3);
		} else if (instrument.getName().equals(Instrument.SYNTH_BASS2)) {
			instrument.getLayer1().setMidiNum(7);
			instrument.getLayer1().setBaseOctave(3);
		} else if (instrument.getName().equals(Instrument.SYNTH_BASS3)) {
			instrument.getLayer1().setMidiNum(25);
			instrument.getLayer1().setBaseOctave(3);
		} else if (instrument.getName().equals(Instrument.SYNTH1)) {
			instrument.getLayer1().setMidiNum(85);
			instrument.getLayer1().setBaseOctave(5);
			instrument.getLayer1().setBaseVelocity(60);
			instrument.getLayer1().setAccentVelocity(70);
			instrument.getLayer1().setPan(80);
			instrument.getLayer1().setReverb(16);
			instrument.getLayer2().setMidiNum(80);
			instrument.getLayer2().setBaseOctave(6);
			instrument.getLayer2().setBaseVelocity(50);
			instrument.getLayer2().setAccentVelocity(64);
			instrument.getLayer2().setPan(48);
			instrument.getLayer2().setReverb(127);
		} else if (instrument.getName().equals(Instrument.SYNTH2)) {
			instrument.getLayer1().setMidiNum(81);
			instrument.getLayer1().setBaseOctave(4);
			instrument.getLayer1().setBaseVelocity(60);
			instrument.getLayer1().setAccentVelocity(70);
			instrument.getLayer1().setPan(48);
			instrument.getLayer1().setReverb(127);
		} else if (instrument.getName().equals(Instrument.SYNTH3)) {
			instrument.getLayer1().setMidiNum(62);
			instrument.getLayer1().setBaseOctave(3);
			instrument.getLayer1().setBaseVelocity(84);
			instrument.getLayer1().setAccentVelocity(96);
			instrument.getLayer1().setPan(40);
			instrument.getLayer1().setReverb(48);
		} else if (instrument.getName().equals(Instrument.LEAD)) {
			instrument.getLayer1().setMidiNum(87);
			instrument.getLayer1().setBaseOctave(6);
			instrument.getLayer1().setBaseVelocity(70);
			instrument.getLayer1().setAccentVelocity(80);
			instrument.getLayer1().setPan(80);
			instrument.getLayer1().setReverb(64);
			instrument.getLayer2().setMidiNum(102);
			instrument.getLayer2().setBaseOctave(6);
			instrument.getLayer2().setBaseVelocity(60);
			instrument.getLayer2().setAccentVelocity(70);
			instrument.getLayer2().setPan(48);
			instrument.getLayer2().setReverb(127);
		} else if (instrument.getName().equals(Instrument.STRINGS)) {
			instrument.getLayer1().setMidiNum(50);
			instrument.getLayer1().setBaseOctave(4);
			instrument.getLayer1().setBaseVelocity(40);
			instrument.getLayer1().setAccentVelocity(50);
			instrument.getLayer1().setPan(48);
			instrument.getLayer1().setReverb(127);
			instrument.getLayer2().setMidiNum(49);
			instrument.getLayer2().setBaseOctave(5);
			instrument.getLayer2().setBaseVelocity(36);
			instrument.getLayer2().setAccentVelocity(44);
			instrument.getLayer2().setPan(80);
			instrument.getLayer2().setReverb(127);
		} else if (instrument.getName().equals(Instrument.DRUMS)) {
			instrument.getLayer1().setMidiNum(118);
			instrument.getLayer1().setReverb(0);
		}
	}

	protected void initializeDrum(DrumConfiguration drum) {
		if (drum.getName().equals(Drum.KICK)) {
			drum.setLayer1MidiNote(36);
			drum.setLayer1BaseVelocity(120);
			drum.setLayer1AccentVelocity(127);
			drum.setLayer2MidiNote(41);
			drum.setLayer2BaseVelocity(80);
			drum.setLayer2AccentVelocity(100);
		} else if (drum.getName().equals(Drum.CLAP)) {
			drum.setLayer1MidiNote(39);
			drum.setLayer1BaseVelocity(70);
			drum.setLayer1AccentVelocity(80);
		} else if (drum.getName().equals(Drum.SNARE)) {
			drum.setLayer1MidiNote(38);
			drum.setLayer1BaseVelocity(100);
			drum.setLayer1AccentVelocity(110);
			drum.setLayer2MidiNote(66);
			drum.setLayer2BaseVelocity(50);
			drum.setLayer2AccentVelocity(60);
		} else if (drum.getName().equals(Drum.HIHAT1)) {
			drum.setLayer1MidiNote(44);
			drum.setLayer1BaseVelocity(60);
			drum.setLayer1AccentVelocity(70);
		} else if (drum.getName().equals(Drum.HIHAT2)) {
			drum.setLayer1MidiNote(69);
			drum.setLayer1BaseVelocity(70);
			drum.setLayer1AccentVelocity(80);
			drum.setLayer2MidiNote(42);
			drum.setLayer2BaseVelocity(80);
			drum.setLayer2AccentVelocity(90);
		} else if (drum.getName().equals(Drum.TOM1)) {
			drum.setLayer1MidiNote(43);
		} else if (drum.getName().equals(Drum.TOM2)) {
			drum.setLayer1MidiNote(45);
		} else if (drum.getName().equals(Drum.RIDE)) {
			drum.setLayer1MidiNote(59);
			drum.setLayer1BaseVelocity(70);
			drum.setLayer1AccentVelocity(80);
			drum.setLayer2MidiNote(51);
			drum.setLayer2BaseVelocity(60);
			drum.setLayer2AccentVelocity(70);
		} else if (drum.getName().equals(Drum.CYMBAL)) {
			drum.setLayer1MidiNote(57);
			drum.setLayer1BaseVelocity(72);
			drum.setLayer1AccentVelocity(80);
			drum.setLayer2MidiNote(55);
			drum.setLayer2BaseVelocity(72);
			drum.setLayer2AccentVelocity(80);
		} else if (drum.getName().equals(Drum.FX1)) {
			drum.setLayer1MidiNote(37);
			drum.setLayer1BaseVelocity(80);
			drum.setLayer1AccentVelocity(90);
		} else if (drum.getName().equals(Drum.FX2)) {
			drum.setLayer1MidiNote(56);
			drum.setLayer1BaseVelocity(70);
			drum.setLayer1AccentVelocity(80);
		} else if (drum.getName().equals(Drum.FX3)) {
			drum.setLayer1MidiNote(76);
			drum.setLayer1BaseVelocity(80);
			drum.setLayer1AccentVelocity(90);
		}
	}
}
