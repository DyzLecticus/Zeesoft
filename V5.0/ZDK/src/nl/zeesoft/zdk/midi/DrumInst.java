package nl.zeesoft.zdk.midi;

import java.util.ArrayList;
import java.util.List;

public class DrumInst extends Inst {
	public static final int		BASEBEAT		= 0;
	public static final int		SNARE			= 1;
	public static final int		HIHAT_CLOSED	= 2;
	public static final int		HIHAT_OPEN		= 3;
	public static final	int		CLAP			= 4;
	public static final	int		TOM1			= 5;
	public static final	int		TOM2			= 6;
	public static final	int		RIDE			= 7;
	public static final	int		CYMBAL			= 8;
	public static final	int		FX1				= 9;
	public static final	int		FX2				= 10;
	public static final	int		FX3				= 11;
	
	public Drum[]				drums			= new Drum[12];
	
	public DrumInst() {
		instrument = 118;
	}
	
	public void initialize() {
		for (int i = 0; i < drums.length; i++) {
			drums[i] = new Drum();
			DrumLayer layer = new DrumLayer();
			if (i == BASEBEAT) {
				layer.midiNoteNum = 35;
			} else if (i == SNARE) {
				layer.midiNoteNum = 50;
				layer.velocity = 100;
			} else if (i == HIHAT_CLOSED) {
				layer.midiNoteNum = 44;
				layer.velocity = 100;
			} else if (i == HIHAT_OPEN) {
				layer.midiNoteNum = 45;
				layer.velocity = 100;
			} else if (i == CLAP) {
				layer.midiNoteNum = 74;
				layer.velocity = 100;
			} else if (i == TOM1) {
				layer.midiNoteNum = 55;
				layer.velocity = 100;
			} else if (i == TOM2) {
				layer.midiNoteNum = 59;
				layer.velocity = 100;
			} else if (i == RIDE) {
				layer.midiNoteNum = 69;
				layer.velocity = 100;
			} else if (i == CYMBAL) {
				layer.midiNoteNum = 70;
				layer.velocity = 100;
			} else if (i == FX1) {
				layer.midiNoteNum = 80;
				layer.velocity = 100;
			} else if (i == FX2) {
				layer.midiNoteNum = 81;
				layer.velocity = 100;
			} else if (i == FX3) {
				layer.midiNoteNum = 76;
				layer.velocity = 100;
			}
			drums[i].layers.add(layer);
		}
	}
	
	@Override
	public Inst copy() {
		DrumInst r = (DrumInst) super.copy();
		for (int i = 0; i < drums.length; i++) {
			r.drums[i] = drums[i].copy();
		}
		return r;
	}
	
	@Override
	protected List<MidiNote> getNotes(String... notes) {
		List<MidiNote> r = new ArrayList<MidiNote>();
		List<MidiNote> list = super.getNotes(notes);
		for (MidiNote note: list) {
			if (note.octave==3) {
				Drum drum = drums[note.octaveNote];
				for (DrumLayer layer: drum.layers) {
					MidiNote ln = note.copy();
					ln.octave = layer.getOctave();
					ln.octaveNote = layer.getOctaveNote();
					ln.stepPercentage = layer.stepPercentage;
					if (ln.velocity>0) {
						ln.velocity = (int)(((float)ln.velocity / 127F) * (float)layer.velocity);
					}
					r.add(ln);
				}
			}
		}
		return r;
	}
}
