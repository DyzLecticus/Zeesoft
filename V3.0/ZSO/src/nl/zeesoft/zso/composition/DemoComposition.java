package nl.zeesoft.zso.composition;

import nl.zeesoft.zso.orchestra.SampleOrchestra;

public class DemoComposition extends Composition {
	public DemoComposition() {
		setName("Demo");
		getSteps().add(new Step(SampleOrchestra.BASEBEAT,1,1));
		getSteps().add(new Step(SampleOrchestra.BASEBEAT,1,9));

		getSteps().add(new Step(SampleOrchestra.SNARE,1,5));
		getSteps().add(new Step(SampleOrchestra.SNARE,1,13));

		getSteps().add(new Step(SampleOrchestra.HIHAT,1,1,100,20));
		getSteps().add(new Step(SampleOrchestra.HIHAT,1,3));
		getSteps().add(new Step(SampleOrchestra.HIHAT,1,5,100,20));
		getSteps().add(new Step(SampleOrchestra.HIHAT,1,7));
		getSteps().add(new Step(SampleOrchestra.HIHAT,1,9,100,20));
		getSteps().add(new Step(SampleOrchestra.HIHAT,1,11));
		getSteps().add(new Step(SampleOrchestra.HIHAT,1,13,100,20));
		getSteps().add(new Step(SampleOrchestra.HIHAT,1,15));
	}
}
