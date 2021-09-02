package nl.zeesoft.zdk.test.midi;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.midi.Groove;
import nl.zeesoft.zdk.midi.MidiSys;

public class TestGroove {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		assert new Groove() != null;
		
		MidiSys.groove.setBeatsPerMinute(135);
		MidiSys.groove.setStepsPerBeat(6);
		MidiSys.groove.setBeatsPerBar(3);
		MidiSys.groove.setBars(5);
		MidiSys.groove.setShuffle(0.1F);
		
		Groove g = new Groove();
		g.copyFrom(MidiSys.groove);
		assert g.getBeatsPerMinute() == 135F;
		assert g.getStepsPerBeat() == 6;
		assert g.getBeatsPerBar() == 3;
		assert g.getBars() == 5;
		assert g.getShuffle() == 0.1F;
		
		assert g.getStepsPerBar() == 18;
		assert g.getTotalSteps() == 90;
		
		assert g.getShuffleForBeatStep(0) == 0.0F;
		assert g.getShuffleForBeatStep(1) == 0.1F;
		
		g.setBeatsPerMinute(10);
		assert g.getBeatsPerMinute() == 40F;
		g.setBeatsPerMinute(300);
		assert g.getBeatsPerMinute() == 240F;

		g.setStepsPerBeat(0);
		assert g.getStepsPerBeat() == 2;
		g.setStepsPerBeat(10);
		assert g.getStepsPerBeat() == 8;

		g.setBeatsPerBar(0);
		assert g.getBeatsPerBar() == 2;
		g.setBeatsPerBar(10);
		assert g.getBeatsPerBar() == 8;
		
		g.setBars(0);
		assert g.getBars() == 1;
		g.setBars(10);
		assert g.getBars() == 8;
		
		g.setShuffle(-0.1F);
		assert g.getShuffle() == 0.0F;
		g.setShuffle(1F);
		assert g.getShuffle() == 0.75F;
		
		MidiSys.groove.copyFrom(new Groove());
		assert MidiSys.groove.getBeatsPerMinute() == 120F;
	}
}
