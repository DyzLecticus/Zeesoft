package nl.zeesoft.zmmt.composition;

import nl.zeesoft.zmmt.syntesizer.Instrument;

public class DemoComposition extends Composition {
	public DemoComposition() {
		setComposer("Dyz Lecticus");
		setName("ZeeTracker demonstration composition");
		getPatterns().add(generateDrumPattern());
	}
	
	protected InstrumentPattern generateDrumPattern() {
		InstrumentPattern pattern = new InstrumentPattern();
		pattern.setInstrument(Instrument.DRUMS);
		Step step = null;
		for (int s = 1; s <=this.getStepsForBars(pattern.getBars()); s++) {
			if (s==1 || s==9 || s==17 || s==25) {
				step = new Step();
				step.track = 1;
				step.note = 36;
				step.accent = true;
				pattern.getSteps().add(step);
			} if (s==5 || s==13 || s==21 || s==29) {
				step = new Step();
				step.track = 2;
				step.note = 39;
				pattern.getSteps().add(step);
			}
		}
		return pattern;
	}
}
