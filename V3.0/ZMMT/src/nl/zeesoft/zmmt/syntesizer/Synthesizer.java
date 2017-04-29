package nl.zeesoft.zmmt.syntesizer;

import java.util.ArrayList;
import java.util.List;

public class Synthesizer {
	private List<SynthesizerInstrument>	instruments	= new ArrayList<SynthesizerInstrument>();
	private List<SynthesizerDrum>		drums 		= new ArrayList<SynthesizerDrum>();

	public void initialize() {
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			SynthesizerInstrument inst = new SynthesizerInstrument();
			inst.setChannelNum(i);
			inst.setInstrument(Instrument.INSTRUMENTS[i]);
			initializeInstrument(inst);
			instruments.add(inst);
		}
		for (int i = 0; i < Drum.DRUMS.length; i++) {
			SynthesizerDrum drum = new SynthesizerDrum();
			drum.setDrum(Drum.DRUMS[i]);
			initializeDrum(drum);
			drums.add(drum);
		}
	}

	protected void initializeInstrument(SynthesizerInstrument instrument) {
		if (instrument.getInstrument().equals(Instrument.SYNTH_BASS1)) {
			instrument.setMidiNum(38);
			instrument.setBaseOctave(2);
			instrument.setBaseVelocity(120);
			instrument.setAccentVelocity(127);
		} else if (instrument.getInstrument().equals(Instrument.SYNTH_BASS2)) {
			instrument.setMidiNum(39);
			instrument.setBaseOctave(2);
			instrument.setBaseVelocity(120);
			instrument.setAccentVelocity(127);
		} else if (instrument.getInstrument().equals(Instrument.SYNTH_BASS3)) {
			instrument.setMidiNum(80);
			instrument.setBaseOctave(2);
			instrument.setBaseVelocity(120);
			instrument.setAccentVelocity(127);
		} else if (instrument.getInstrument().equals(Instrument.SYNTH1)) {
			instrument.setMidiNum(90);
			instrument.setBaseOctave(4);
		} else if (instrument.getInstrument().equals(Instrument.SYNTH2)) {
			instrument.setMidiNum(81);
			instrument.setBaseOctave(4);
		} else if (instrument.getInstrument().equals(Instrument.SYNTH3)) {
			instrument.setMidiNum(54);
			instrument.setBaseOctave(4);
		} else if (instrument.getInstrument().equals(Instrument.BASS)) {
			instrument.setMidiNum(32);
			instrument.setBaseVelocity(120);
			instrument.setAccentVelocity(127);
		} else if (instrument.getInstrument().equals(Instrument.PIANO)) {
			instrument.setMidiNum(0);
			instrument.setBaseOctave(4);
			instrument.setBaseVelocity(80);
			instrument.setAccentVelocity(90);
		} else if (instrument.getInstrument().equals(Instrument.HARP)) {
			instrument.setMidiNum(46);
			instrument.setBaseOctave(5);
		} else if (instrument.getInstrument().equals(Instrument.DRUMS)) {
			instrument.setMidiNum(118);
		} else if (instrument.getInstrument().equals(Instrument.STRINGS1)) {
			instrument.setMidiNum(48);
		} else if (instrument.getInstrument().equals(Instrument.STRINGS2)) {
			instrument.setMidiNum(49);
			instrument.setBaseOctave(4);
			instrument.setBaseVelocity(80);
			instrument.setAccentVelocity(90);
		}
	}

	protected void initializeDrum(SynthesizerDrum drum) {
		if (drum.getDrum().equals(Drum.BASEBEAT)) {
			drum.setNoteNum(36);
			drum.setBaseVelocity(120);
			drum.setAccentVelocity(127);
		} else if (drum.getDrum().equals(Drum.CLAP)) {
			drum.setNoteNum(39);
		} else if (drum.getDrum().equals(Drum.SNARE)) {
			drum.setNoteNum(38);
		} else if (drum.getDrum().equals(Drum.HIHAT1)) {
			drum.setNoteNum(42);
		} else if (drum.getDrum().equals(Drum.HIHAT2)) {
			drum.setNoteNum(69);
		} else if (drum.getDrum().equals(Drum.TOM1)) {
			drum.setNoteNum(41);
		} else if (drum.getDrum().equals(Drum.TOM2)) {
			drum.setNoteNum(43);
		} else if (drum.getDrum().equals(Drum.RIDE)) {
			drum.setNoteNum(59);
			drum.setBaseVelocity(80);
			drum.setAccentVelocity(90);
		} else if (drum.getDrum().equals(Drum.CYMBAL)) {
			drum.setNoteNum(57);
			drum.setBaseVelocity(120);
			drum.setAccentVelocity(127);
		}
	}

	public List<SynthesizerInstrument> getInstruments() {
		return instruments;
	}

	public List<SynthesizerDrum> getDrums() {
		return drums;
	}

	public SynthesizerInstrument getInstrument(String instrument) {
		SynthesizerInstrument r = null;
		for (SynthesizerInstrument inst: instruments) {
			if (inst.getInstrument().equals(instrument)) {
				r = inst;
			}
		}
		return r;
	}

	public SynthesizerDrum getDrum(String drum) {
		SynthesizerDrum r = null;
		for (SynthesizerDrum inst: drums) {
			if (inst.getDrum().equals(drum)) {
				r = inst;
			}
		}
		return r;
	}

	public void configureMidiSynthesizer(javax.sound.midi.Synthesizer synth) {
		for (SynthesizerInstrument inst: instruments) {
			synth.getChannels()[inst.getChannelNum()].programChange(inst.getMidiNum());
		}
	}

	public int getMidiNoteNumberForNote(String instrument,int note) {
		int r = -1;
		SynthesizerInstrument inst = getInstrument(instrument);
		if (instrument.equals(Instrument.DRUMS)) {
			SynthesizerDrum drum = null;
			if (note==0) {
				drum = getDrum(Drum.BASEBEAT);
			} else if (note==1) {
				drum = getDrum(Drum.CLAP);
			} else if (note==2) {
				drum = getDrum(Drum.SNARE);
			} else if (note==3) {
				drum = getDrum(Drum.HIHAT1);
			} else if (note==4) {
				drum = getDrum(Drum.HIHAT2);
			} else if (note==5) {
				drum = getDrum(Drum.TOM1);
			} else if (note==6) {
				drum = getDrum(Drum.TOM2);
			} else if (note==7) {
				drum = getDrum(Drum.RIDE);
			} else if (note==8) {
				drum = getDrum(Drum.CYMBAL);
			}
			if (drum!=null) {
				r = drum.getNoteNum();
			}
		} else {
			r = (inst.getBaseOctave() * 12) - 1 + note;
		}
		return r;
	}

	public int getVelocityForNote(String instrument,int note,boolean accent) {
		int r = -1;
		SynthesizerInstrument inst = getInstrument(instrument);
		if (instrument.equals(Instrument.DRUMS)) {
			SynthesizerDrum drum = null;
			if (note==0) {
				drum = getDrum(Drum.BASEBEAT);
			} else if (note==1) {
				drum = getDrum(Drum.CLAP);
			} else if (note==2) {
				drum = getDrum(Drum.SNARE);
			} else if (note==3) {
				drum = getDrum(Drum.HIHAT1);
			} else if (note==4) {
				drum = getDrum(Drum.HIHAT2);
			} else if (note==5) {
				drum = getDrum(Drum.TOM1);
			} else if (note==6) {
				drum = getDrum(Drum.TOM2);
			} else if (note==7) {
				drum = getDrum(Drum.RIDE);
			} else if (note==8) {
				drum = getDrum(Drum.CYMBAL);
			}
			if (drum!=null) {
				if (accent) {
					r = drum.getAccentVelocity();
				} else {
					r = drum.getBaseVelocity();
				}
			}
		} else {
			if (accent) {
				r = inst.getAccentVelocity();
			} else {
				r = inst.getBaseVelocity();
			}
		}
		return r;
	}
}
