package nl.zeesoft.zmmt.composition;

import nl.zeesoft.zmmt.syntesizer.Instrument;

public class DemoComposition extends Composition {
	public DemoComposition() {
		setComposer("Dyz Lecticus");
		setName("ZeeTracker demo composition");
		getPatterns().add(generateDrumPattern(getBarsPerPattern()));
	}
	
	protected Pattern generateDrumPattern(int bars) {
		Pattern pattern = new Pattern();
		Note note = null;
		for (int s = 1; s <= (bars * getStepsPerBar()); s++) {
			if (s==1 || s==9 || s==17 || s==25 || s==33 || s==41 || s==49 || s==57) {
				note = new Note();
				note.instrument = Instrument.DRUMS;
				note.track = 1;
				note.note = 36;
				note.accent = true;
				pattern.getNotes().add(note);
			} if (s==5 || s==13 || s==21 || s==29 || s==37 || s==45 || s==53 || s==61) {
				note = new Note();
				note.instrument = Instrument.DRUMS;
				note.track = 2;
				note.note = 39;
				pattern.getNotes().add(note);
			}
		}
		return pattern;
	}
}
