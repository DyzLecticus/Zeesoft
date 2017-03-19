package nl.zeesoft.zso.composition;

import nl.zeesoft.zso.orchestra.SampleOrchestra;

public class DemoComposition extends Composition {
	public DemoComposition() {
		setName("Demo");
		getSteps().add(new Step(SampleOrchestra.BASEBEAT,1,1));
		getSteps().add(new Step(SampleOrchestra.BASEBEAT,1,11));

		getSteps().add(new Step(SampleOrchestra.SNARE,1,5));
		getSteps().add(new Step(SampleOrchestra.SNARE,1,13));

		getSteps().add(new Step(SampleOrchestra.HIHAT,1,1,200,10));
		getSteps().add(new Step(SampleOrchestra.HIHAT,1,3,100,100));
		getSteps().add(new Step(SampleOrchestra.HIHAT,1,5,200,10));
		getSteps().add(new Step(SampleOrchestra.HIHAT,1,7,100,100));
		getSteps().add(new Step(SampleOrchestra.HIHAT,1,9,200,10));
		getSteps().add(new Step(SampleOrchestra.HIHAT,1,11,100,100));
		getSteps().add(new Step(SampleOrchestra.HIHAT,1,13,200,10));
		getSteps().add(new Step(SampleOrchestra.HIHAT,1,15,100,100));
	}
}
