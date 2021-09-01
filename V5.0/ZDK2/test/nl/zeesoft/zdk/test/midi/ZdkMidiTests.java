package nl.zeesoft.zdk.test.midi;

import nl.zeesoft.zdk.Console;

public class ZdkMidiTests {
	public static void main(String[] args) {
		Console.log("Test Synth ...");
		TestSynth.main(args);
		Console.log("Test Groove ...");
		TestGroove.main(args);
		Console.log("Test Instruments ...");
		TestInstruments.main(args);
	}
}
