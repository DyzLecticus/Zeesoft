package nl.zeesoft.zdbd.midi.convertors;

import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdbd.midi.SynthConfig;

public class InstrumentConvertors {
	public static final int							KICK				= 0;
	public static final int							SNARE				= 1;
	public static final int							CLOSED_HIHAT		= 2;
	public static final int							OPEN_HIHAT			= 3;
	public static final int							RIDE				= 4;
	public static final int							CRASH				= 5;
	public static final int							PERCUSSION1			= 6;
	public static final int							PERCUSSION2			= 7;
	public static final int							BASS				= 8;
	
	public static final String[]					INSTRUMENT_NAMES	= {
		"Kick", "Snare", "ClosedHihat", "OpenHihat", "Ride", "Crash", "Percussion1", "Percussion2", "Bass"
	};
	
	private SortedMap<String,InstrumentConvertor>	convertors			= new TreeMap<String,InstrumentConvertor>();

	public InstrumentConvertors() {
		initializeDefaults();
	}
	
	public void initializeDefaults() {
		convertors.clear();
		for (int i = 0; i < INSTRUMENT_NAMES.length; i++) {
			String name = INSTRUMENT_NAMES[i];
			if (i==BASS) {
				BassConvertor bass = new BassConvertor();
				bass.name = name;
				BassLayerConvertor layer1 = new BassLayerConvertor();
				layer1.channel = SynthConfig.BASS_CHANNEL_1;
				bass.layers.add(layer1);
				BassLayerConvertor layer2 = new BassLayerConvertor();
				layer2.channel = SynthConfig.BASS_CHANNEL_2;
				layer2.velocity = 40;
				layer2.accentVelocity = 50;
				bass.layers.add(layer2);
				convertors.put(name, bass);
			} else {
				DrumConvertor drum = new DrumConvertor();
				drum.name = name;
				DrumSampleConvertor sample = new DrumSampleConvertor();
				if (i==KICK) {
					sample.midiNote = 35;
					sample.accentVelocity = 110;
					sample.accentVelocity = 127;
				} else if (i==SNARE) {
					sample.midiNote = 50;
					sample.velocity = 80;
					sample.accentVelocity = 100;
					sample.hold = 0.8F;
					sample.accentHold = 1.0F;
				} else if (i==CLOSED_HIHAT) {
					sample.midiNote = 44;
					sample.velocity = 70;
					sample.accentVelocity = 80;
					sample.hold = 0.2F;
					sample.accentHold = 0.3F;
				} else if (i==OPEN_HIHAT) {
					sample.midiNote = 45;
					sample.velocity = 80;
					sample.accentVelocity = 90;
					sample.hold = 0.5F;
					sample.accentHold = 0.8F;
				} else if (i==RIDE) {
					sample.midiNote = 69;
					sample.velocity = 80;
					sample.accentVelocity = 90;
					sample.hold = 0.9F;
					sample.accentHold = 1.9F;
				} else if (i==CRASH) {
					sample.midiNote = 70;
					sample.velocity = 90;
					sample.accentVelocity = 100;
					sample.hold = 1.9F;
					sample.accentHold = 3.9F;
				} else if (i==PERCUSSION1) {
					sample.midiNote = 76;
					sample.velocity = 80;
					sample.accentVelocity = 90;
					sample.hold = 0.1F;
					sample.accentHold = 0.2F;
				} else if (i==PERCUSSION2) {
					sample.midiNote = 77;
					sample.velocity = 70;
					sample.accentVelocity = 80;
					sample.hold = 0.1F;
					sample.accentHold = 0.2F;
				}
				drum.samples.add(sample);
				convertors.put(name, drum);
			}
		}
	}
	
	public InstrumentConvertor get(String name) {
		return convertors.get(name);
	}
	
	public static String getInstrumentName(int index) {
		return INSTRUMENT_NAMES[index];
	}
}
