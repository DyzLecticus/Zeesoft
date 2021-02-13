package nl.zeesoft.zdbd.midi;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Sequence;

import nl.zeesoft.zdbd.midi.convertors.PatternSequenceConvertor;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdk.collection.PersistableCollection;
import nl.zeesoft.zdk.thread.RunCode;

public class SoundPatch {
	public static String				DRUMS				= "Drums";
	public static String				BASS				= "Bass";
	public static String				STAB				= "Stab";
	public static String				ARPEGGIATOR			= "Arpeggiator";
	
	public static String[]				INSTRUMENT_NAMES	= {
		DRUMS,BASS,STAB,ARPEGGIATOR
	};
	
	public SynthConfig					synthConfig			= new SynthConfig();
	public PatternSequenceConvertor		convertor			= new PatternSequenceConvertor();
	
	public void copyFrom(SoundPatch patch) {
		this.synthConfig = patch.synthConfig;
		this.convertor = patch.convertor;
	}
	
	public Sequence generateMidiSequence(PatternSequence sequence, Arpeggiator arp) {
		return convertor.generateSequenceForPatternSequence(sequence,arp);
	}
	
	public SynthChannelConfig getChannelConfig(String name, int layer) {
		SynthChannelConfig r = null;
		int channel = getChannelForInstrumentLayer(name,layer);
		if (channel>=0) {
			r = synthConfig.getChannelConfig(channel);
		}
		return r;
	}
	
	public List<int[]> setInstrumentProperty(String name, int layer, String propertyName, int value) {
		List<int[]> r = new ArrayList<int[]>();
		int[] changes = null;
		int channel = getChannelForInstrumentLayer(name,layer);
		if (channel>=0) {
			changes = new int[3];
			changes[0] = channel;
			changes[1] = 0;
			changes[2] = value;
			if (propertyName.equals("instrument")) {
				synthConfig.setChannelInstrument(channel, value);
			} else {
				int control = -1;
				if (propertyName.equals("volume")) {
					control = SynthConfig.VOLUME;
				} else if (propertyName.equals("attack")) {
					control = SynthConfig.ATTACK;
				} else if (propertyName.equals("decay")) {
					control = SynthConfig.DECAY;
				} else if (propertyName.equals("release")) {
					control = SynthConfig.RELEASE;
				} else if (propertyName.equals("pan")) {
					control = SynthConfig.PAN;
				} else if (propertyName.equals("modulation")) {
					control = SynthConfig.MODULATION;
				} else if (propertyName.equals("chorus")) {
					control = SynthConfig.CHORUS;
				} else if (propertyName.equals("filter")) {
					control = SynthConfig.FILTER;
				} else if (propertyName.equals("resonance")) {
					control = SynthConfig.RESONANCE;
				} else if (propertyName.equals("reverb")) {
					control = SynthConfig.REVERB;
				} else if (propertyName.equals("vib_rate")) {
					control = SynthConfig.VIB_RATE;
				} else if (propertyName.equals("vib_depth")) {
					control = SynthConfig.VIB_DEPTH;
				} else if (propertyName.equals("vib_delay")) {
					control = SynthConfig.VIB_DELAY;
				}
				if (control>=0) {
					synthConfig.setChannelControl(channel, control, value);
					changes[1] = control;
				} else {
					changes = null;
				}
			}
		}
		if (changes!=null) {
			r.add(changes);
			synthConfig.addEchoChanges(r);
		}
		return r;
	}
	
	public RunCode getFromFileRunCode(String path) {
		return new RunCode() {
			@Override
			protected boolean run() {
				fromFile(path);
				return true;
			}
		};
	}
	
	public void fromFile(String path) {
		SoundPatch patch = (SoundPatch) PersistableCollection.fromFile(path);
		if (patch!=null) {
			copyFrom(patch);
		}
	}
	
	public RunCode getToFileRunCode(String path) {
		return new RunCode() {
			@Override
			protected boolean run() {
				toFile(path);
				return true;
			}
		};
	}
	
	public void toFile(String path) {
		PersistableCollection.toFile(this, path);
	}
	
	public static List<String> getInstrumentNames() {
		List<String> r = new ArrayList<String>();
		for (int i = 0; i < INSTRUMENT_NAMES.length; i++) {
			r.add(INSTRUMENT_NAMES[i]);
		}
		return r;
	}
	
	public static int getChannelForInstrumentLayer(String name, int layer) {
		int r = -1;
		if (name.equals(DRUMS)) {
			if (layer==0) {
				r = SynthConfig.DRUM_CHANNEL;
			}
		} else if (name.equals(BASS)) {
			if (layer==0) {
				r = SynthConfig.BASS_CHANNEL_1;
			} else if (layer==1) {
				r = SynthConfig.BASS_CHANNEL_2;
			}
		} else if (name.equals(STAB)) {
			if (layer==0) {
				r = SynthConfig.STAB_CHANNEL;
			}
		} else if (name.equals(ARPEGGIATOR)) {
			if (layer==0) {
				r = SynthConfig.ARP_CHANNEL_1;
			} else if (layer==1) {
				r = SynthConfig.ARP_CHANNEL_2;
			}
		}
		return r;
	}
}
