package nl.zeesoft.zdk.test.midi;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.midi.MidiSys;
import nl.zeesoft.zdk.midi.instrument.Instrument;
import nl.zeesoft.zdk.midi.instrument.Instruments;
import nl.zeesoft.zdk.midi.pattern.InstrumentPattern;
import nl.zeesoft.zdk.midi.pattern.Pattern;
import nl.zeesoft.zdk.midi.pattern.PatternGenerator;

public class TestInstruments {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		assert new Instruments() != null;
		
		assert MidiSys.instruments.bass.name.equals(Instrument.BASS);
		assert MidiSys.instruments.stab.name.equals(Instrument.STAB);
		assert MidiSys.instruments.arp.name.equals(Instrument.ARP);
		assert MidiSys.instruments.drum.name.equals(Instrument.DRUM);

		assert MidiSys.instruments.bass.getChannels().size() == 2;
		assert MidiSys.instruments.stab.getChannels().size() == 1;
		assert MidiSys.instruments.arp.getChannels().size() == 2;
		assert MidiSys.instruments.drum.getChannels().size() == 1;
		
		assert MidiSys.instruments.list().size() == 4;

		assert MidiSys.chordPattern.getStep(0) != null;
		assert MidiSys.chordPattern.getStep(1) == MidiSys.chordPattern.getStep(0);
		assert MidiSys.chordPattern.getStep(32) != MidiSys.chordPattern.getStep(0);
		assert MidiSys.chordPattern.getStep(128) == MidiSys.chordPattern.getStep(0);
		
		// Drum patterns
		PatternGenerator pg = MidiSys.instruments.drum.addGenerator("Kick");
		pg.chordNote = 0;
		pg.hold = 0.75F;
		pg.velocity = 110;
		pg.accentHold = 1.5F;
		pg.accentVelocity = 127;
		pg.addStep(0, true);
		
		assert pg.getStep(0) != null;
		assert pg.getStep(1) == null;
		assert pg.getStep(8) != null;
		
		assert MidiSys.instruments.drum.addGenerator("Kick") == null;

		Pattern p = pg.generatePattern(0, MidiSys.groove.getTotalSteps());
		assert p.name.equals("Kick");
		assert p.stepStart == 0;
		assert p.stepEnd == MidiSys.groove.getTotalSteps();
		assert p.steps.size() == 8;
		assert p.getStep(0) != null;
		assert p.getStep(8) != null;
		assert p.getStep(16) != null;
		assert p.getStep(24) != null;
		assert p.getStep(32) != null;
		
		pg = MidiSys.instruments.drum.addGenerator("Snare");
		pg.chordNote = 1;
		pg.hold = 0.75F;
		pg.velocity = 100;
		pg.accentHold = 1.5F;
		pg.accentVelocity = 110;
		pg.addStep(4, true);
		
		pg = MidiSys.instruments.drum.addGenerator("ClosedHihat");
		pg.length = 4;
		pg.chordNote = 2;
		pg.hold = 0.25F;
		pg.velocity = 70;
		pg.accentHold = 0.5F;
		pg.accentVelocity = 70;
		pg.addStep(0, true);
		pg.addStep(1, false);
		pg.addStep(3, false);
		
		pg = MidiSys.instruments.drum.addGenerator("OpenHihat");
		pg.length = 4;
		pg.chordNote = 3;
		pg.hold = 0.5F;
		pg.velocity = 80;
		pg.accentHold = 0.75F;
		pg.accentVelocity = 80;
		pg.addStep(2, true);
		
		// Bass pattern
		pg = MidiSys.instruments.bass.addGenerator("Bass");
		pg.chordNote = 0;
		pg.hold = 0.75F;
		pg.velocity = 120;
		pg.accentHold = 1.75F;
		pg.accentVelocity = 120;
		pg.addStep(2, false);
		pg.addStep(3, false);
		pg.addStep(6, true);
		
		//Console.log(JsonConstructor.fromObject(MidiSys.instruments).toStringBuilderReadFormat());
		
		InstrumentPattern drumPattern = MidiSys.instruments.drum.generatePattern();
		assert drumPattern.name.equals(Instrument.DRUM);
		assert drumPattern.patterns.get(0).name.equals("Kick");
		InstrumentPattern bassPattern = MidiSys.instruments.bass.generatePattern();
		assert bassPattern.name.equals(Instrument.BASS);
		assert bassPattern.patterns.get(0).name.equals("Bass");
		
		//Console.log(JsonConstructor.fromObject(drumPattern).toStringBuilderReadFormat());
		//Console.log(JsonConstructor.fromObject(bassPattern).toStringBuilderReadFormat());
	}
}
