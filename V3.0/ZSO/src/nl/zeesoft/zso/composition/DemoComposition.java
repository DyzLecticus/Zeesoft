package nl.zeesoft.zso.composition;

import nl.zeesoft.zso.orchestra.SampleOrchestra;

public class DemoComposition extends Composition {
	public DemoComposition() {
		setName("Demo");
		
		int bar = 1;
		getSteps().add(new Step(SampleOrchestra.BASEBEAT,bar,1));
		getSteps().add(new Step(SampleOrchestra.BASEBEAT,bar,11));
		getSteps().add(new Step(SampleOrchestra.SNARE,bar,5));
		getSteps().add(new Step(SampleOrchestra.SNARE,bar,13));
		generateHihatsForBar(bar);

		bar ++;
		getSteps().add(new Step(SampleOrchestra.BASEBEAT,bar,1));
		getSteps().add(new Step(SampleOrchestra.BASEBEAT,bar,7));
		getSteps().add(new Step(SampleOrchestra.BASEBEAT,bar,11));
		getSteps().add(new Step(SampleOrchestra.SNARE,bar,5));
		getSteps().add(new Step(SampleOrchestra.SNARE,bar,13));
		getSteps().add(new Step(SampleOrchestra.SNARE,bar,16,0,100));
		generateHihatsForBar(bar);

		bar ++;
		getSteps().add(new Step(SampleOrchestra.BASEBEAT,bar,1));
		getSteps().add(new Step(SampleOrchestra.BASEBEAT,bar,11));
		getSteps().add(new Step(SampleOrchestra.SNARE,bar,5));
		getSteps().add(new Step(SampleOrchestra.SNARE,bar,13));
		generateHihatsForBar(bar);

		bar ++;
		getSteps().add(new Step(SampleOrchestra.BASEBEAT,bar,1));
		getSteps().add(new Step(SampleOrchestra.BASEBEAT,bar,3));
		getSteps().add(new Step(SampleOrchestra.BASEBEAT,bar,7));
		getSteps().add(new Step(SampleOrchestra.BASEBEAT,bar,11));
		getSteps().add(new Step(SampleOrchestra.SNARE,bar,5));
		getSteps().add(new Step(SampleOrchestra.SNARE,bar,13));
		getSteps().add(new Step(SampleOrchestra.SNARE,bar,15,0,100));
		getSteps().add(new Step(SampleOrchestra.SNARE,bar,16,0,100));
		generateHihatsForBar(bar);
	}
	
	private void generateHihatsForBar(int bar) {
		getSteps().add(new Step(SampleOrchestra.HIHAT,bar,1,150,10));
		getSteps().add(new Step(SampleOrchestra.HIHAT,bar,3,100,50));
		getSteps().add(new Step(SampleOrchestra.HIHAT,bar,5,150,10));
		getSteps().add(new Step(SampleOrchestra.HIHAT,bar,7,100,50));
		getSteps().add(new Step(SampleOrchestra.HIHAT,bar,8,100,50));
		getSteps().add(new Step(SampleOrchestra.HIHAT,bar,9,150,10));
		getSteps().add(new Step(SampleOrchestra.HIHAT,bar,10,150,10));
		getSteps().add(new Step(SampleOrchestra.HIHAT,bar,11,100,50));
		getSteps().add(new Step(SampleOrchestra.HIHAT,bar,13,150,10));
		getSteps().add(new Step(SampleOrchestra.HIHAT,bar,15,0,150));
	}
}
