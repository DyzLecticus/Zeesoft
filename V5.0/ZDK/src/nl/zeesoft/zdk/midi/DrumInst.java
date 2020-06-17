package nl.zeesoft.zdk.midi;

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
	
	// TODO: Finish
	public void initialize() {
		for (int i = 0; i < drums.length; i++) {
			drums[i] = new Drum();
			DrumLayer layer = new DrumLayer();
			if (i == 0) {
				layer.midiNoteNum = 35;
			} else if (i == 1) {
				layer.midiNoteNum = 38;
			}
		}
	}
	
	// TODO: Finish
	protected List<MidiNote> getNotes(String... notes) {
		return super.getNotes(notes);
	}
}
