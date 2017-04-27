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
			inst.setMidiNum(getInitialMidiNumForInstrument(inst.getInstrument()));
			instruments.add(inst);
		}
		for (int i = 0; i < Drum.DRUMS.length; i++) {
			SynthesizerDrum drum = new SynthesizerDrum();
			drum.setDrum(Drum.DRUMS[i]);
			drum.setNoteNum(getInitialNoteNumForDrum(drum.getDrum()));
			drums.add(drum);
		}
	}

	protected int getInitialMidiNumForInstrument(String instrument) {
		int r = 0;
		if (instrument.equals(Instrument.SYNTH_BASS1)) {
			r = 38;
		} else if (instrument.equals(Instrument.SYNTH_BASS2)) {
			r = 39;
		} else if (instrument.equals(Instrument.SYNTH_BASS3)) {
			r = 80;
		} else if (instrument.equals(Instrument.SYNTH1)) {
			r = 90;
		} else if (instrument.equals(Instrument.SYNTH2)) {
			r = 81;
		} else if (instrument.equals(Instrument.SYNTH3)) {
			r = 54;
		} else if (instrument.equals(Instrument.BASS)) {
			r = 32;
		} else if (instrument.equals(Instrument.PIANO)) {
			r = 0;
		} else if (instrument.equals(Instrument.HARP)) {
			r = 46;
		} else if (instrument.equals(Instrument.DRUMS)) {
			r = 118;
		} else if (instrument.equals(Instrument.STRINGS1)) {
			r = 48;
		} else if (instrument.equals(Instrument.STRINGS2)) {
			r = 49;
		}
		return r;
	}

	protected int getInitialNoteNumForDrum(String drum) {
		int r = 60;
		if (drum.equals(Drum.BASEBEAT)) {
			r = 36;
		} else if (drum.equals(Drum.HIHAT1)) {
			r = 42;
		} else if (drum.equals(Drum.HIHAT2)) {
			r = 69;
		} else if (drum.equals(Drum.SNARE)) {
			r = 38;
		} else if (drum.equals(Drum.CLAP)) {
			r = 39;
		} else if (drum.equals(Drum.TOM1)) {
			r = 40;
		} else if (drum.equals(Drum.TOM2)) {
			r = 41;
		} else if (drum.equals(Drum.RIDE)) {
			r = 59;
		} else if (drum.equals(Drum.CYMBAL)) {
			r = 57;
		}
		return r;
	}
	
	public List<SynthesizerInstrument> getChannels() {
		return instruments;
	}
}
