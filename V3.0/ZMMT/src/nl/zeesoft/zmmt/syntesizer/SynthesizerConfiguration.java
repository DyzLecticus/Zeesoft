package nl.zeesoft.zmmt.syntesizer;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class SynthesizerConfiguration {
	private List<InstrumentConfiguration>	instruments	= new ArrayList<InstrumentConfiguration>();
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
			instElem.children.add(new JsElem("midiNum","" + inst.getMidiNum()));
			instElem.children.add(new JsElem("polyphony","" + inst.getPolyphony()));
			if (!inst.getName().equals(Instrument.DRUMS)) {
				instElem.children.add(new JsElem("baseOctave","" + inst.getBaseOctave()));
				instElem.children.add(new JsElem("baseVelocity","" + inst.getBaseVelocity()));
				instElem.children.add(new JsElem("accentVelocity","" + inst.getAccentVelocity()));
			}
			if (inst.getName().equals(Instrument.SYNTH1) || 
				inst.getName().equals(Instrument.SYNTH_BASS1) ||
				inst.getName().equals(Instrument.PIANO) ||
				inst.getName().equals(Instrument.STRINGS1)
				) { 
				instElem.children.add(new JsElem("layerMidiNum","" + inst.getLayerMidiNum()));
				instElem.children.add(new JsElem("layerBaseOctave","" + inst.getLayerBaseOctave()));
				instElem.children.add(new JsElem("layerBaseVelocity","" + inst.getLayerBaseVelocity()));
				instElem.children.add(new JsElem("layerAccentVelocity","" + inst.getLayerAccentVelocity()));
			}
		}
		for (DrumConfiguration drum: drums) {
			JsElem drumElem = new JsElem("drum");
			json.rootElement.children.add(drumElem);
			drumElem.children.add(new JsElem("name",drum.getName(),true));
			drumElem.children.add(new JsElem("noteNum","" + drum.getNoteNum()));
			drumElem.children.add(new JsElem("baseVelocity","" + drum.getBaseVelocity()));
			drumElem.children.add(new JsElem("accentVelocity","" + drum.getAccentVelocity()));
			drumElem.children.add(new JsElem("layerNoteNum","" + drum.getLayerNoteNum()));
			drumElem.children.add(new JsElem("layerBaseVelocity","" + drum.getLayerBaseVelocity()));
			drumElem.children.add(new JsElem("layerAccentVelocity","" + drum.getLayerAccentVelocity()));
		}
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
						} else if (val.name.equals("midiNum")) {
							inst.setMidiNum(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("baseOctave")) {
							inst.setBaseOctave(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("baseVelocity")) {
							inst.setBaseVelocity(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("accentVelocity")) {
							inst.setAccentVelocity(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("polyphony")) {
							inst.setPolyphony(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layerMidiNum")) {
							inst.setLayerMidiNum(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layerBaseOctave")) {
							inst.setLayerBaseOctave(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layerBaseVelocity")) {
							inst.setLayerBaseVelocity(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layerAccentVelocity")) {
							inst.setLayerAccentVelocity(Integer.parseInt(val.value.toString()));
						}
					}
					if (inst.getName().length()>0) {
						InstrumentConfiguration current = getInstrument(inst.getName());
						if (current!=null) {
							int index = instruments.indexOf(current);
							inst.setChannelNum(current.getChannelNum());
							instruments.remove(index);
							instruments.add(index,inst);
						}
					}
				} else if (elem.name.equals("drum")) {
					DrumConfiguration drum = new DrumConfiguration();
					for (JsElem val: elem.children) {
						if (val.name.equals("name")) {
							drum.setName(val.value.toString());
						} else if (val.name.equals("noteNum")) {
							drum.setNoteNum(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("baseVelocity")) {
							drum.setBaseVelocity(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("accentVelocity")) {
							drum.setAccentVelocity(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layerNoteNum")) {
							drum.setLayerNoteNum(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layerBaseVelocity")) {
							drum.setLayerBaseVelocity(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("layerAccentVelocity")) {
							drum.setLayerAccentVelocity(Integer.parseInt(val.value.toString()));
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
				}
			}
		}
	}

	public List<InstrumentConfiguration> getInstruments() {
		return instruments;
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

	public void configureMidiSynthesizer(javax.sound.midi.Synthesizer synth) {
		for (InstrumentConfiguration inst: instruments) {
			synth.getChannels()[Instrument.getMidiChannelForInstrument(inst.getName(),false)].programChange(inst.getMidiNum());
			int layerChannel = Instrument.getMidiChannelForInstrument(inst.getName(),true);
			if (layerChannel>=0 && inst.getLayerMidiNum()>=0) {
				synth.getChannels()[layerChannel].programChange(inst.getLayerMidiNum());
			}
		}
	}

	public int getMidiNoteNumberForNote(String instrument,int note,boolean layer) {
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
			}
			if (drum!=null) {
				if (layer) { 
					if (drum.getLayerNoteNum()>=35 && drum.getLayerNoteNum()!=drum.getNoteNum()) {
						r = drum.getLayerNoteNum();
					}
				} else {
					r = drum.getNoteNum();
				}
			}
		} else {
			if (layer) { 
				if (inst.getLayerMidiNum()>=0 && inst.getLayerMidiNum()!=inst.getMidiNum()) {
					r = (inst.getLayerBaseOctave() * 12) + (note - 36);
				}
			} else {
				r = (inst.getBaseOctave() * 12) + (note - 36);
			}
		}
		return r;
	}

	public int getVelocityForNote(String instrument,int note,boolean accent,boolean layer) {
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
			}
			if (drum!=null) {
				if (layer) {
					if (drum.getLayerNoteNum()>=35 && drum.getLayerNoteNum()!=drum.getNoteNum()) {
						if (accent && drum.getLayerNoteNum()>=0) {
							r = drum.getLayerAccentVelocity();
						} else {
							r = drum.getLayerBaseVelocity();
						}
					}
				} else {
					if (accent) {
						r = drum.getAccentVelocity();
					} else {
						r = drum.getBaseVelocity();
					}
				}
			}
		} else {
			if (layer) {
				if (inst.getLayerMidiNum()>=0 && inst.getLayerMidiNum()!=inst.getMidiNum()) {
					if (accent) {
						r = inst.getLayerAccentVelocity();
					} else {
						r = inst.getLayerBaseVelocity();
					}
				}
			} else {
				if (accent) {
					r = inst.getAccentVelocity();
				} else {
					r = inst.getBaseVelocity();
				}
			}
		}
		return r;
	}

	protected void initialize() {
		instruments.clear();
		drums.clear();
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			InstrumentConfiguration inst = new InstrumentConfiguration();
			inst.setChannelNum(i);
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
			instrument.setMidiNum(38);
			instrument.setBaseOctave(2);
			instrument.setBaseVelocity(120);
			instrument.setAccentVelocity(127);
			instrument.setLayerBaseOctave(2);
			instrument.setLayerBaseVelocity(120);
			instrument.setLayerAccentVelocity(127);
		} else if (instrument.getName().equals(Instrument.SYNTH_BASS2)) {
			instrument.setMidiNum(39);
			instrument.setBaseOctave(2);
			instrument.setBaseVelocity(120);
			instrument.setAccentVelocity(127);
		} else if (instrument.getName().equals(Instrument.SYNTH_BASS3)) {
			instrument.setMidiNum(80);
			instrument.setBaseOctave(2);
			instrument.setBaseVelocity(120);
			instrument.setAccentVelocity(127);
		} else if (instrument.getName().equals(Instrument.SYNTH1)) {
			instrument.setMidiNum(90);
			instrument.setBaseOctave(4);
			instrument.setLayerBaseOctave(4);
		} else if (instrument.getName().equals(Instrument.SYNTH2)) {
			instrument.setMidiNum(81);
			instrument.setBaseOctave(4);
		} else if (instrument.getName().equals(Instrument.SYNTH3)) {
			instrument.setMidiNum(54);
			instrument.setBaseOctave(4);
		} else if (instrument.getName().equals(Instrument.BASS)) {
			instrument.setMidiNum(32);
			instrument.setBaseVelocity(120);
			instrument.setAccentVelocity(127);
		} else if (instrument.getName().equals(Instrument.PIANO)) {
			instrument.setMidiNum(0);
			instrument.setBaseOctave(4);
			instrument.setBaseVelocity(80);
			instrument.setAccentVelocity(90);
			instrument.setLayerMidiNum(50);
			instrument.setLayerBaseOctave(4);
			instrument.setLayerBaseVelocity(80);
			instrument.setLayerAccentVelocity(90);
		} else if (instrument.getName().equals(Instrument.HARP)) {
			instrument.setMidiNum(46);
			instrument.setBaseOctave(5);
		} else if (instrument.getName().equals(Instrument.DRUMS)) {
			instrument.setMidiNum(118);
			instrument.setPolyphony(8);
		} else if (instrument.getName().equals(Instrument.STRINGS1)) {
			instrument.setMidiNum(48);
		} else if (instrument.getName().equals(Instrument.STRINGS2)) {
			instrument.setMidiNum(49);
			instrument.setBaseOctave(4);
			instrument.setBaseVelocity(80);
			instrument.setAccentVelocity(90);
		}
	}

	protected void initializeDrum(DrumConfiguration drum) {
		if (drum.getName().equals(Drum.BASEBEAT)) {
			drum.setNoteNum(36);
			drum.setBaseVelocity(120);
			drum.setAccentVelocity(127);
			drum.setLayerNoteNum(41);
			drum.setBaseVelocity(120);
			drum.setAccentVelocity(127);
		} else if (drum.getName().equals(Drum.CLAP)) {
			drum.setNoteNum(39);
		} else if (drum.getName().equals(Drum.SNARE)) {
			drum.setNoteNum(38);
			drum.setLayerNoteNum(66);
			drum.setLayerBaseVelocity(60);
			drum.setLayerAccentVelocity(70);
		} else if (drum.getName().equals(Drum.HIHAT1)) {
			drum.setNoteNum(42);
		} else if (drum.getName().equals(Drum.HIHAT2)) {
			drum.setNoteNum(69);
			drum.setLayerNoteNum(42);
		} else if (drum.getName().equals(Drum.TOM1)) {
			drum.setNoteNum(43);
		} else if (drum.getName().equals(Drum.TOM2)) {
			drum.setNoteNum(45);
		} else if (drum.getName().equals(Drum.RIDE)) {
			drum.setNoteNum(59);
			drum.setBaseVelocity(80);
			drum.setAccentVelocity(90);
		} else if (drum.getName().equals(Drum.CYMBAL)) {
			drum.setNoteNum(57);
		}
	}
}
