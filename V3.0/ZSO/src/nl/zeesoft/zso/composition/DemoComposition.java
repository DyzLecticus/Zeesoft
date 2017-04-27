package nl.zeesoft.zso.composition;

import java.util.ArrayList;
import java.util.List;

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
		
		//generateBassForBar(bar);
		//generatePianoForBar(bar);
		
		bar ++;
		getSteps().add(new Step(SampleOrchestra.BASEBEAT,bar,1));
		getSteps().add(new Step(SampleOrchestra.BASEBEAT,bar,7));
		getSteps().add(new Step(SampleOrchestra.BASEBEAT,bar,11));
		getSteps().add(new Step(SampleOrchestra.SNARE,bar,5));
		getSteps().add(new Step(SampleOrchestra.SNARE,bar,13));
		getSteps().add(new Step(SampleOrchestra.SNARE,bar,16,0,100));
		generateHihatsForBar(bar);

		//generateBassForBar(bar);

		bar ++;
		getSteps().add(new Step(SampleOrchestra.BASEBEAT,bar,1));
		getSteps().add(new Step(SampleOrchestra.BASEBEAT,bar,11));
		getSteps().add(new Step(SampleOrchestra.SNARE,bar,5));
		getSteps().add(new Step(SampleOrchestra.SNARE,bar,13));
		generateHihatsForBar(bar);

		//generateBassForBar(bar);

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

		//generateBassForBar(bar);
	}
	
	protected void generateHihatsForBar(int bar) {
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
	
	protected void generateBassForBar(int bar) {
		getSteps().add(new MidiStep(SampleOrchestra.SYNTH_BASS,bar,1,24,127,200));
		getSteps().add(new MidiStep(SampleOrchestra.SYNTH_BASS,bar,3,24,127,200));
		getSteps().add(new MidiStep(SampleOrchestra.SYNTH_BASS,bar,5,24,127,200));
		getSteps().add(new MidiStep(SampleOrchestra.SYNTH_BASS,bar,7,24,127,200));
		getSteps().add(new MidiStep(SampleOrchestra.SYNTH_BASS,bar,9,24,127,200));
		getSteps().add(new MidiStep(SampleOrchestra.SYNTH_BASS,bar,11,36,127,200));
		getSteps().add(new MidiStep(SampleOrchestra.SYNTH_BASS,bar,13,34,127,200));
		getSteps().add(new MidiStep(SampleOrchestra.SYNTH_BASS,bar,15,29,127,200));
	}
	
	protected void generatePianoForBar(int bar) {
		getSteps().add(new MidiStep(SampleOrchestra.SYNTH_PIANO,bar,1,getChord(60,63,67),64,500));
	}

	protected List<Integer> getChord(int note1,int note2,int note3) {
		List<Integer> notes = new ArrayList<Integer>();
		notes.add(note1);
		notes.add(note2);
		notes.add(note3);
		return notes;
	}
}
