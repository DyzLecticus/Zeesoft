package nl.zeesoft.zmmt.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zmmt.synthesizer.Instrument;
import nl.zeesoft.zmmt.synthesizer.SynthesizerConfiguration;

public class TestSynthesizerConfiguration extends TestObject {
	public TestSynthesizerConfiguration(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSynthesizerConfiguration(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create a *SynthesizerConfiguration* instance and convert it to and from JSON.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create configuration");
		System.out.println("SynthesizerConfiguration conf = new SynthesizerConfiguration();");
		System.out.println("// Convert to JSON");
		System.out.println("JsFile json = conf.toJson();");
		System.out.println("// Convert from JSON");
		System.out.println("conf.fromJson(json);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestSynthesizerConfiguration.class));
		System.out.println(" * " + getTester().getLinkForClass(SynthesizerConfiguration.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the JSON structure of the *SynthesizerConfiguration*.  ");
	}

	@Override
	protected void test(String[] args) {
		SynthesizerConfiguration conf = new SynthesizerConfiguration();
		conf.getInstrument(Instrument.SYNTH_BASS1).setVolume(123);
		conf.getInstrument(Instrument.SYNTH_BASS1).setPan(123);
		
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer1().setMidiNum(123);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer1().setPressure(123);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer1().setModulation(123);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer1().setReverb(123);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer1().setChorus(123);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer1().setFilter(123);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer1().setResonance(123);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer1().setAttack(123);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer1().setDecay(123);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer1().setRelease(123);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer1().setVibRate(123);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer1().setVibDepth(123);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer1().setVibDelay(123);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer1().setBaseOctave(5);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer1().setBaseVelocity(123);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer1().setAccentVelocity(123);

		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer2().setMidiNum(111);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer2().setPressure(111);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer2().setModulation(111);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer2().setReverb(111);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer2().setChorus(111);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer2().setFilter(111);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer2().setResonance(111);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer2().setAttack(111);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer2().setDecay(111);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer2().setRelease(111);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer2().setVibRate(111);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer2().setVibDepth(111);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer2().setVibDelay(111);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer2().setBaseOctave(5);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer2().setBaseVelocity(111);
		conf.getInstrument(Instrument.SYNTH_BASS1).getLayer2().setAccentVelocity(111);
		
		JsFile json = conf.toJson();
		ZStringBuilder before = json.toStringBuilderReadFormat();
		conf.fromJson(json);
		ZStringBuilder after = conf.toJson().toStringBuilderReadFormat();
		assertEqual(after,before,"The after conversion JSON does not match expectation");
		System.out.println(after);
	}
}
