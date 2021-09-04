package nl.zeesoft.zdk.test.midi;

import java.io.File;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Transmitter;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.midi.MidiSequenceUtil;
import nl.zeesoft.zdk.midi.MidiSys;
import nl.zeesoft.zdk.midi.instrument.Bass;
import nl.zeesoft.zdk.midi.instrument.Drum;
import nl.zeesoft.zdk.midi.instrument.Instrument;
import nl.zeesoft.zdk.midi.instrument.Instruments;
import nl.zeesoft.zdk.midi.pattern.InstrumentPattern;
import nl.zeesoft.zdk.midi.pattern.Pattern;
import nl.zeesoft.zdk.midi.pattern.PatternGenerator;
import nl.zeesoft.zdk.midi.synth.SoundbankLoader;

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

		assert MidiSys.chordPattern.getStep(-1) == null;
		assert MidiSys.chordPattern.getStep(0) != null;
		assert MidiSys.chordPattern.getStep(1) == MidiSys.chordPattern.getStep(0);
		assert MidiSys.chordPattern.getStep(32) != MidiSys.chordPattern.getStep(0);
		assert MidiSys.chordPattern.getStep(128) == MidiSys.chordPattern.getStep(0);
		
		assert MidiSys.instruments.drum.addGenerator(0,"Pizza") == null;
		
		// Drum patterns
		PatternGenerator pg = MidiSys.instruments.drum.addGenerator(0,Drum.KICK);
		assert pg.getStep(0) == null;
		pg.addStep(0, true);
		
		assert pg.getStep(0) != null;
		assert pg.getStep(1) == null;
		assert pg.getStep(8) != null;
		
		assert MidiSys.instruments.drum.addGenerator(0,Drum.KICK) == null;

		Pattern p = pg.generatePattern(0, MidiSys.groove.getTotalSteps());
		assert p.name.equals(Drum.KICK);
		assert p.stepStart == 0;
		assert p.stepEnd == MidiSys.groove.getTotalSteps();
		assert p.steps.size() == 8;
		assert p.getStep(0) != null;
		assert p.getStep(8) != null;
		assert p.getStep(16) != null;
		assert p.getStep(24) != null;
		assert p.getStep(32) != null;
		
		pg = MidiSys.instruments.drum.addGenerator(0,Drum.SNARE);
		pg.addStep(4, true);
		
		pg = MidiSys.instruments.drum.addGenerator(0,Drum.CLOSED_HIHAT);
		pg.length = 4;
		pg.addStep(0, true);
		pg.addStep(1, false);
		pg.addStep(3, false);
		
		pg = MidiSys.instruments.drum.addGenerator(0,Drum.OPEN_HIHAT);
		pg.length = 4;
		pg.addStep(2, true);
		
		// Drum patterns variation
		MidiSys.instruments.drum.patternVariations.add(MidiSys.instruments.drum.patternVariations.get(0).copy());
		pg = MidiSys.instruments.drum.patternVariations.get(1).getGenerator(Drum.KICK);
		pg.addStep(2, false);
		pg.addStep(3, false);
		
		pg = MidiSys.instruments.drum.patternVariations.get(1).getGenerator(Drum.SNARE);
		pg.addStep(7, false);
		
		// Bass pattern
		pg = MidiSys.instruments.bass.addGenerator(0,Bass.BASS);
		pg.addStep(2, false);
		pg.addStep(3, false);
		pg.addStep(6, true);
		
		//Console.log(JsonConstructor.fromObject(MidiSys.instruments).toStringBuilderReadFormat());
		
		InstrumentPattern drumPattern1 = MidiSys.instruments.drum.generatePattern(0);
		assert drumPattern1.name.equals(Instrument.DRUM);
		assert drumPattern1.patterns.get(0).name.equals(Drum.KICK);
		InstrumentPattern drumPattern2 = MidiSys.instruments.drum.generatePattern(1);
		assert drumPattern2.name.equals(Instrument.DRUM);
		assert drumPattern2.patterns.get(0).name.equals(Drum.KICK);
		InstrumentPattern bassPattern = MidiSys.instruments.bass.generatePattern(0);
		assert bassPattern.name.equals(Instrument.BASS);
		assert bassPattern.patterns.get(0).name.equals(Drum.BASS);
		
		//Console.log(JsonConstructor.fromObject(drumPattern).toStringBuilderReadFormat());
		//Console.log(JsonConstructor.fromObject(bassPattern).toStringBuilderReadFormat());
		
		Sequence drumSeq1 = MidiSys.instruments.drum.generateSequence(drumPattern1);
		Sequence drumSeq2 = MidiSys.instruments.drum.generateSequence(drumPattern2);
		Sequence bassSeq = MidiSys.instruments.bass.generateSequence(bassPattern);
		
		Sequence seq1 = MidiSequenceUtil.mergeTracks(drumSeq1, bassSeq);
		Sequence seq2 = MidiSequenceUtil.mergeTracks(drumSeq2, bassSeq);
		
		playSequence(seq1,5000);
		Util.sleep(1000);
		playSequence(seq2,5000);		
	}
	
	private static void playSequence(Sequence sequence, int ms) {
		File file = new File("../../V3.0/ZeeTracker/resources/ZeeTrackerDrumKit.sf2");
		if (file.exists()) {
			MidiSys.initialize();
			assert SoundbankLoader.load("../../V3.0/ZeeTracker/resources/ZeeTrackerDrumKit.sf2") != null;
			assert SoundbankLoader.load("../../V3.0/ZeeTracker/resources/ZeeTrackerSynthesizers.sf2") != null;

			Sequencer sequencer = null;
			try {
				sequencer = MidiSystem.getSequencer(false);
				sequencer.open();
				if (sequencer.getTransmitters().size()>0) {
					for (Transmitter trm: sequencer.getTransmitters()) {
						trm.setReceiver(MidiSys.synth.getSynthesizer().getReceiver());
					}
				} else {
					sequencer.getTransmitter().setReceiver(MidiSys.synth.getSynthesizer().getReceiver());
				}			
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
			if (sequencer!=null) {
				try {
					sequencer.setSequence(sequence);
				} catch (InvalidMidiDataException e) {
					e.printStackTrace();
				}
				sequencer.setTickPosition(0);
				sequencer.start();
				Util.sleep(ms);
				sequencer.stop();
				sequencer.close();
			}
			
			MidiSys.destroy();
		}
	}
}
