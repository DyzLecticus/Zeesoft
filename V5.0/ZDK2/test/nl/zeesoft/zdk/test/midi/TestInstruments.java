package nl.zeesoft.zdk.test.midi;

import java.io.File;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.midi.MidiSequenceUtil;
import nl.zeesoft.zdk.midi.MidiSys;
import nl.zeesoft.zdk.midi.instrument.Bass;
import nl.zeesoft.zdk.midi.instrument.Drum;
import nl.zeesoft.zdk.midi.instrument.Instrument;
import nl.zeesoft.zdk.midi.instrument.InstrumentChannelSound;
import nl.zeesoft.zdk.midi.instrument.Instruments;
import nl.zeesoft.zdk.midi.pattern.InstrumentPattern;
import nl.zeesoft.zdk.midi.pattern.Pattern;
import nl.zeesoft.zdk.midi.pattern.PatternGenerator;
import nl.zeesoft.zdk.midi.synth.SoundbankLoader;

public class TestInstruments {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		assert new InstrumentChannelSound() != null;

		assert new Instruments() != null;
		
		assert MidiSys.instruments.bass.name.equals(Instrument.BASS);
		assert MidiSys.instruments.stab.name.equals(Instrument.STAB);
		assert MidiSys.instruments.arp.name.equals(Instrument.ARP);
		assert MidiSys.instruments.drum.name.equals(Instrument.DRUM);
		
		assert MidiSys.instruments.get(Instrument.BASS)!=null;
		assert MidiSys.instruments.get(Instrument.STAB)!=null;
		assert MidiSys.instruments.get(Instrument.ARP)!=null;
		assert MidiSys.instruments.get(Instrument.DRUM)!=null;

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
		
		assert MidiSys.instruments.drum.addGenerator(0,Instrument.DRUM,"Pizza") == null;
		
		// Drum patterns
		PatternGenerator pg = MidiSys.instruments.drum.addGenerator(0,Instrument.DRUM,Drum.KICK);
		assert pg.getStep(0) == null;
		pg.addStep(0, true);
		
		assert pg.getStep(0) != null;
		assert pg.getStep(1) == null;
		assert pg.getStep(8) != null;
		
		assert MidiSys.instruments.drum.addGenerator(0,Instrument.DRUM,Drum.KICK) == null;

		Pattern p = pg.generatePattern(0, MidiSys.groove.getTotalSteps());
		assert p.elementName.equals(Drum.KICK);
		assert p.stepStart == 0;
		assert p.stepEnd == MidiSys.groove.getTotalSteps();
		assert p.steps.size() == 8;
		assert p.getStep(0) != null;
		assert p.getStep(8) != null;
		assert p.getStep(16) != null;
		assert p.getStep(24) != null;
		assert p.getStep(32) != null;
		
		pg = MidiSys.instruments.drum.addGenerator(0,Instrument.DRUM,Drum.SNARE);
		pg.addStep(4, true);
		
		pg = MidiSys.instruments.drum.addGenerator(0,Instrument.DRUM,Drum.CLOSED_HIHAT);
		pg.length = 4;
		pg.addStep(0, true);
		pg.addStep(1, false);
		pg.addStep(3, false);
		
		pg = MidiSys.instruments.drum.addGenerator(0,Instrument.DRUM,Drum.OPEN_HIHAT);
		pg.length = 4;
		pg.addStep(2, true);
		
		// Drum patterns variation
		MidiSys.instruments.drum.patternVariations.add(MidiSys.instruments.drum.patternVariations.get(0).copy());
		pg = MidiSys.instruments.drum.patternVariations.get(1).getGenerator(Instrument.DRUM,Drum.KICK);
		pg.addStep(2, false);
		pg.addStep(3, false);
		
		pg = MidiSys.instruments.drum.patternVariations.get(1).getGenerator(Instrument.DRUM,Drum.SNARE);
		pg.addStep(7, false);
		
		// Bass pattern
		pg = MidiSys.instruments.bass.addGenerator(0,Instrument.BASS,Bass.BASS);
		pg.addStep(2, false);
		pg.addStep(3, false);
		pg.addStep(6, true);
		
		//Console.log(JsonConstructor.fromObject(MidiSys.instruments).toStringBuilderReadFormat());
		
		InstrumentPattern drumPattern1 = MidiSys.instruments.drum.generatePattern(0);
		assert drumPattern1.name.equals(Instrument.DRUM);
		assert drumPattern1.patterns.get(0).elementName.equals(Drum.KICK);
		InstrumentPattern drumPattern2 = MidiSys.instruments.drum.generatePattern(1);
		assert drumPattern2.name.equals(Instrument.DRUM);
		assert drumPattern2.patterns.get(0).elementName.equals(Drum.KICK);
		InstrumentPattern bassPattern = MidiSys.instruments.bass.generatePattern(0);
		assert bassPattern.name.equals(Instrument.BASS);
		assert bassPattern.patterns.get(0).elementName.equals(Bass.BASS);
		
		//Console.log(JsonConstructor.fromObject(drumPattern).toStringBuilderReadFormat());
		//Console.log(JsonConstructor.fromObject(bassPattern).toStringBuilderReadFormat());
		
		Sequence test = MidiSequenceUtil.createSequence(1, true);
		assert test == null;
		test = MidiSequenceUtil.createSequence(1, false);
		assert test != null;
		assert test.getTracks()[0].size() == 1;
		MidiSequenceUtil.createEventOnTrack(test.getTracks()[0], 1, 1, 1, 1, 1, true);
		assert test.getTracks()[0].size() == 1;
		MidiSequenceUtil.createEventOnTrack(test.getTracks()[0], ShortMessage.NOTE_ON, 1, 1, 1, 1, false);
		assert test.getTracks()[0].size() == 2;
		
		Sequence drumSeq1 = MidiSys.instruments.drum.generateSequence(drumPattern1);
		Sequence drumSeq2 = MidiSys.instruments.drum.generateSequence(drumPattern2);
		Sequence bassSeq = MidiSys.instruments.bass.generateSequence(bassPattern);

		assert bassSeq.getTracks()[0].get(0).getTick() == 480;
		assert bassSeq.getTracks()[0].get(1).getTick() == 660;
		assert bassSeq.getTracks()[0].get(2).getTick() == 756;
		
		Sequence seq1 = MidiSequenceUtil.mergeTracks(drumSeq1, bassSeq);
		assert seq1.getTracks()[0].size() == 161;
		Sequence seq2 = MidiSequenceUtil.mergeTracks(drumSeq2, bassSeq);
		assert seq2.getTracks()[0].size() == 209;

		//playSequence(seq1,5000);
		//Util.sleep(1000);
		//playSequence(seq2,5000);		

		bassPattern.patterns.get(0).steps.get(0).accent = true;
		Sequence bassSeq2 = MidiSys.instruments.bass.generateSequence(bassPattern);
		assert bassSeq2.getTracks()[0].get(0).getTick() == 480;
		assert bassSeq2.getTracks()[0].get(1).getTick() == 755;
		assert bassSeq2.getTracks()[0].get(2).getTick() == 756;
		//playSequence(bassSeq2,5000);				
	}
	
	protected static void playSequence(Sequence sequence, int ms) {
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
