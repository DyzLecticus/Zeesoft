package nl.zeesoft.zmmt.test;

import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.composition.Note;
import nl.zeesoft.zmmt.composition.Pattern;
import nl.zeesoft.zmmt.synthesizer.Instrument;

public class MockComposition extends Composition {
	public MockComposition() {
		setComposer("Dyz Lecticus");
		setName("ZeeTracker mock composition");
		getSynthesizerConfiguration().getInstrument(Instrument.SYNTH_BASS1).setLayer1MidiNum(123);
		getSynthesizerConfiguration().getInstrument(Instrument.SYNTH_BASS1).setLayer1BaseOctave(5);
		getSynthesizerConfiguration().getInstrument(Instrument.SYNTH_BASS1).setLayer1Pressure(123);
		getSynthesizerConfiguration().getInstrument(Instrument.SYNTH_BASS1).setLayer1Reverb(123);
		getSynthesizerConfiguration().getInstrument(Instrument.SYNTH_BASS1).setLayer1BaseVelocity(123);
		getSynthesizerConfiguration().getInstrument(Instrument.SYNTH_BASS1).setLayer1AccentVelocity(123);
		getPatterns().add(generateDrumPattern(getBarsPerPattern()));
		getSequence().add(1);
		getSequence().add(0);
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
