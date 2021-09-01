package nl.zeesoft.zdk.test.midi;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.midi.MidiSys;
import nl.zeesoft.zdk.midi.instrument.Instrument;
import nl.zeesoft.zdk.midi.instrument.Instruments;
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

		PatternGenerator pg = MidiSys.instruments.drum.addGenerator("Kick");
		pg.midiNoteNum = 36;
		pg.hold = 0.75F;
		pg.velocity = 110;
		pg.accentHold = 1.5F;
		pg.accentVelocity = 127;
		pg.addStep(0, true);
		
		assert pg.getStep(0) != null;
		assert pg.getStep(1) == null;
		assert pg.getStep(8) != null;
		
		assert MidiSys.instruments.drum.addGenerator("Kick") == null;
		
		pg = MidiSys.instruments.drum.addGenerator("Snare");
		pg.midiNoteNum = 37;
		pg.hold = 0.75F;
		pg.velocity = 100;
		pg.accentHold = 1.5F;
		pg.accentVelocity = 110;
		pg.addStep(4, true);
		
		pg = MidiSys.instruments.drum.addGenerator("ClosedHihat");
		pg.length = 4;
		pg.midiNoteNum = 38;
		pg.hold = 0.25F;
		pg.velocity = 70;
		pg.accentHold = 0.5F;
		pg.accentVelocity = 70;
		pg.addStep(0, true);
		pg.addStep(1, false);
		pg.addStep(3, false);
		
		pg = MidiSys.instruments.drum.addGenerator("OpenHihat");
		pg.length = 4;
		pg.midiNoteNum = 39;
		pg.hold = 0.5F;
		pg.velocity = 80;
		pg.accentHold = 0.75F;
		pg.accentVelocity = 80;
		pg.addStep(2, true);
	}
}
