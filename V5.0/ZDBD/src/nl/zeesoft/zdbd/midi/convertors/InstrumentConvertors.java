package nl.zeesoft.zdbd.midi.convertors;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.midi.Arpeggiator;
import nl.zeesoft.zdbd.midi.SynthConfig;
import nl.zeesoft.zdk.thread.Lock;

public class InstrumentConvertors {
	public static final int				KICK				= 0;
	public static final int				SNARE				= 1;
	public static final int				CLOSED_HIHAT		= 2;
	public static final int				OPEN_HIHAT			= 3;
	public static final int				RIDE				= 4;
	public static final int				CRASH				= 5;
	public static final int				PERCUSSION1			= 6;
	public static final int				PERCUSSION2			= 7;
	public static final int				BASS				= 8;
	public static final int				STAB				= 9;
	public static final int				ARPEGGIATOR			= 10;
	
	public static final String[]		INSTRUMENT_NAMES	= {
		"Kick", "Snare", "ClosedHihat", "OpenHihat", "Ride", "Crash", "Percussion1", "Percussion2", "Bass", "Stab",
		Arpeggiator.class.getSimpleName()
	};
	
	private Lock						lock				= new Lock();
	
	private List<InstrumentConvertor>	convertors			= new ArrayList<InstrumentConvertor>();

	public InstrumentConvertors() {
		initializeDefaults();
	}
	
	public static String getInstrumentName(int index) {
		return INSTRUMENT_NAMES[index];
	}
	
	public InstrumentConvertor get(String name) {
		lock.lock(this);
		InstrumentConvertor r = getNoLock(name);
		if (r!=null) {
			r = r.copy();
		}
		lock.unlock(this);
		return r;
	}
	
	public void setConvertorLayerProperty(int convertor, int layer, String property, Object value) {
		lock.lock(this);
		InstrumentConvertor conv = getNoLock(INSTRUMENT_NAMES[convertor]);
		if (conv instanceof DrumConvertor) {
			DrumConvertor dc = (DrumConvertor) conv;
			if (dc.samples.size()>layer) {
				DrumSampleConvertor sample = dc.samples.get(layer);
				if (property.equals("midiNote")) {
					sample.midiNote = (int) value;
				} else if (property.equals("velocity")) {
					sample.velocity = (int) value;
				} else if (property.equals("accentVelocity")) {
					sample.accentVelocity = (int) value;
				} else if (property.equals("hold")) {
					sample.hold = (float) value;
				} else if (property.equals("accentHold")) {
					sample.accentHold = (float) value;
				}
			}
		} else if (conv instanceof BassConvertor) {
			BassConvertor bc = (BassConvertor) conv;
			if (property.equals("hold")) {
				bc.hold = (float) value;
			} else {
				if (bc.layers.size()>layer) {
					SynthLayerConvertor sl = bc.layers.get(layer);
					if (property.equals("baseOctave")) {
						sl.baseOctave = (int) value;
					} else if (property.equals("velocity")) {
						sl.velocity = (int) value;
					} else if (property.equals("accentVelocity")) {
						sl.accentVelocity = (int) value;
					}
				}
			}
		}
		lock.unlock(this);
	}
	
	protected InstrumentConvertor getNoLock(String name) {
		InstrumentConvertor r = null;
		for (InstrumentConvertor convertor: convertors) {
			if (convertor.name.equals(name)) {
				r = convertor;
				break;
			}
		}
		return r;
	}
	
	private void initializeDefaults() {
		convertors.clear();
		for (int i = 0; i < INSTRUMENT_NAMES.length; i++) {
			String name = INSTRUMENT_NAMES[i];
			if (i==BASS) {
				BassConvertor conv = new BassConvertor();
				conv.name = name;
				SynthLayerConvertor layer1 = new SynthLayerConvertor();
				layer1.channel = SynthConfig.BASS_CHANNEL_1;
				conv.layers.add(layer1);
				SynthLayerConvertor layer2 = new SynthLayerConvertor();
				layer2.channel = SynthConfig.BASS_CHANNEL_2;
				layer2.velocity = 40;
				layer2.accentVelocity = 50;
				conv.layers.add(layer2);
				convertors.add(conv);
			} else if (i==STAB) {
				StabConvertor conv = new StabConvertor();
				conv.name = name;
				conv.hold = 0.7F;
				SynthLayerConvertor layer = new SynthLayerConvertor();
				layer.channel = SynthConfig.STAB_CHANNEL;
				layer.baseOctave = 5;
				layer.velocity = 40;
				layer.accentVelocity = 48;
				conv.layers.add(layer);
				convertors.add(conv);
			} else if (i==ARPEGGIATOR) {
				SynthConvertor conv = new SynthConvertor();
				conv.name = name;
				SynthLayerConvertor layer1 = new SynthLayerConvertor();
				layer1.channel = SynthConfig.ARP_CHANNEL_1;
				layer1.baseOctave = 4;
				layer1.velocity = 48;
				layer1.accentVelocity = 52;
				conv.layers.add(layer1);
				SynthLayerConvertor layer2 = new SynthLayerConvertor();
				layer2.channel = SynthConfig.ARP_CHANNEL_2;
				layer2.baseOctave = 4;
				layer2.velocity = 48;
				layer2.accentVelocity = 52;
				conv.layers.add(layer2);
				convertors.add(conv);
			} else {
				DrumConvertor drum = new DrumConvertor();
				drum.name = name;
				DrumSampleConvertor sample = new DrumSampleConvertor();
				if (i==KICK) {
					sample.midiNote = 35;
					sample.velocity = 110;
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
					sample.velocity = 76;
					sample.accentVelocity = 84;
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
					sample.velocity = 60;
					sample.accentVelocity = 70;
					sample.hold = 0.1F;
					sample.accentHold = 0.2F;
				} else if (i==PERCUSSION2) {
					sample.midiNote = 77;
					sample.velocity = 50;
					sample.accentVelocity = 60;
					sample.hold = 0.1F;
					sample.accentHold = 0.2F;
				}
				drum.samples.add(sample);
				convertors.add(drum);
			}
		}
	}
}
