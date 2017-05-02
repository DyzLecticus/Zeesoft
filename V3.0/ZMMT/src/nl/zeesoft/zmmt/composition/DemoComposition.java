package nl.zeesoft.zmmt.composition;

import nl.zeesoft.zmmt.syntesizer.Instrument;

public class DemoComposition extends Composition {
	public DemoComposition() {
		setComposer("Dyz Lecticus");
		setName("ZeeTracker demo composition");
		getPatterns().add(generateDrumPattern());
	}
	
	protected Pattern generateDrumPattern() {
		Pattern pattern = new Pattern();
		Step step = null;
		for (int s = 1; s <=this.getStepsForBars(pattern.getBars()); s++) {
			if (s==1 || s==9 || s==17 || s==25 || s==33 || s==41 || s==49 || s==57) {
				step = new Step();
				step.instrument = Instrument.DRUMS;
				step.track = 1;
				step.note = 36;
				step.accent = true;
				pattern.getSteps().add(step);
			} if (s==5 || s==13 || s==21 || s==29 || s==37 || s==45 || s==53 || s==61) {
				step = new Step();
				step.instrument = Instrument.DRUMS;
				step.track = 2;
				step.note = 39;
				pattern.getSteps().add(step);
			}
		}
		return pattern;
	}
}
