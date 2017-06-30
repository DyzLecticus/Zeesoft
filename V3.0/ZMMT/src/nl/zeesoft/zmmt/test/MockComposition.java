package nl.zeesoft.zmmt.test;

import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.composition.Control;
import nl.zeesoft.zmmt.composition.Note;
import nl.zeesoft.zmmt.composition.Pattern;
import nl.zeesoft.zmmt.synthesizer.Instrument;

public class MockComposition extends Composition {
	public MockComposition() {
		setComposer("Dyz Lecticus");
		setName("ZeeTracker mock composition");

		getSynthesizerConfiguration().getInstrument(Instrument.BASS1).setSideChainPercentage(10);

		getSynthesizerConfiguration().getInstrument(Instrument.BASS1).setVolume(123);
		getSynthesizerConfiguration().getInstrument(Instrument.BASS1).setPan(123);
		getSynthesizerConfiguration().getInstrument(Instrument.BASS1).getLayer1().setMidiNum(123);
		getSynthesizerConfiguration().getInstrument(Instrument.BASS1).getLayer1().setBaseOctave(5);
		getSynthesizerConfiguration().getInstrument(Instrument.BASS1).getLayer1().setFilter(123);
		getSynthesizerConfiguration().getInstrument(Instrument.BASS1).getLayer1().setChorus(123);
		getSynthesizerConfiguration().getInstrument(Instrument.BASS1).getLayer1().setModulation(123);
		getSynthesizerConfiguration().getInstrument(Instrument.BASS1).getLayer1().setPressure(123);
		getSynthesizerConfiguration().getInstrument(Instrument.BASS1).getLayer1().setReverb(123);
		getSynthesizerConfiguration().getInstrument(Instrument.BASS1).getLayer1().setBaseVelocity(123);
		getSynthesizerConfiguration().getInstrument(Instrument.BASS1).getLayer1().setAccentVelocity(123);
		
		getPatterns().add(generateDrumPattern(0,getBarsPerPattern()));
		addPatternControls(getPatterns().get(0),getBarsPerPattern());
		
		getPatterns().add(generateDrumPattern(1,getBarsPerPattern()));
		addPatternControls(getPatterns().get(1),getBarsPerPattern());

		getSequence().add(1);
		getSequence().add(0);
	}
	
	protected Pattern generateDrumPattern(int num,int bars) {
		Pattern pattern = new Pattern();
		pattern.setNumber(num);
		Note note = null;
		for (int s = 1; s <= (bars * getStepsPerBar()); s++) {
			if (s % 8 == 1) {
				note = new Note();
				note.instrument = Instrument.DRUMS;
				note.track = 1;
				note.step = s;
				note.note = 36;
				note.accent = true;
				pattern.getNotes().add(note);
				
				note = new Note();
				note.instrument = Instrument.DRUMS;
				note.track = 2;
				note.step = s;
				note.note = 39;
				note.accent = true;
				pattern.getNotes().add(note);
			} if (s % 8 == 5) {
				note = new Note();
				note.instrument = Instrument.DRUMS;
				note.track = 3;
				note.step = s;
				note.note = 40;
				pattern.getNotes().add(note);

				note = new Note();
				note.instrument = Instrument.BASS1;
				note.track = 4;
				note.step = s;
				note.note = 36;
				note.duration = 4;
				pattern.getNotes().add(note);
			}
		}
		return pattern;
	}
	
	protected void addPatternControls(Pattern pattern,int bars) {
		Control ctrl = new Control();
		ctrl.instrument = Instrument.DRUMS;
		ctrl.step = 1;
		ctrl.control = Control.EXPRESSION;
		ctrl.percentage = 0;
		pattern.getControls().add(ctrl);

		ctrl = new Control();
		ctrl.instrument = Instrument.DRUMS;
		ctrl.step = ((bars / 2) * getStepsPerBar());
		ctrl.control = Control.EXPRESSION;
		ctrl.percentage = 100;
		pattern.getControls().add(ctrl);

		ctrl = new Control();
		ctrl.instrument = Instrument.DRUMS;
		ctrl.step = (bars * getStepsPerBar());
		ctrl.control = Control.EXPRESSION;
		ctrl.percentage = 0;
		pattern.getControls().add(ctrl);

		if (pattern.getNumber()==0) {
			ctrl = new Control();
			ctrl.instrument = Instrument.BASS1;
			ctrl.step = 1;
			ctrl.control = Control.EXPRESSION;
			ctrl.percentage = 50;
			pattern.getControls().add(ctrl);
	
			ctrl = new Control();
			ctrl.instrument = Instrument.BASS1;
			ctrl.step = ((bars / 2) * getStepsPerBar());
			ctrl.control = Control.EXPRESSION;
			ctrl.percentage = 100;
			pattern.getControls().add(ctrl);
	
			ctrl = new Control();
			ctrl.instrument = Instrument.BASS1;
			ctrl.step = (bars * getStepsPerBar());
			ctrl.control = Control.EXPRESSION;
			ctrl.percentage = 0;
			pattern.getControls().add(ctrl);
		}
	}
}
