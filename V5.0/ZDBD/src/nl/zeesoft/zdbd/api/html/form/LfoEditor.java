package nl.zeesoft.zdbd.api.html.form;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.midi.SoundPatch;
import nl.zeesoft.zdbd.midi.SynthConfig;
import nl.zeesoft.zdbd.midi.lfo.ChannelLFO;
import nl.zeesoft.zdbd.midi.lfo.LFO;

public class LfoEditor extends AbstractEditor {
	public LfoEditor(String name, List<ChannelLFO> lfos, String prevName, String nextName) {
		cancelLabel = "Done";
		onCancelClick = "soundpatch.editDone();";
		
		addProperty("instrumentName", "Instrument", name, FormProperty.TEXT);
		
		int i = 0;
		for (ChannelLFO lfo: lfos) {
			addChannelLFOProperties(lfo,i);
			i++;
		}
		
		for (FormProperty property: properties) {
			property.onChange = "soundpatch.propertyChange(this);";
		}
		
		this.prevName = prevName;
		this.nextName = nextName;
		this.function = "soundpatch.edit";
	}
	
	protected void addChannelLFOProperties(ChannelLFO lfo, int index) {
		String suffix = "-" + index;
		addProperty("lfo-number" + suffix, "Number", "" + (index + 1), FormProperty.TEXT);
		addProperty("lfo-active" + suffix, "Active", lfo.isActive(), FormProperty.CHECKBOX_INPUT);
		addProperty("lfo-channel" + suffix, "Instrument/layer", getLfoChannels(), FormProperty.SELECT, getNameForChannel(lfo.getChannel()));
		addProperty("lfo-control" + suffix, "Control", getLfoControls(), FormProperty.SELECT, getNameForControl(lfo.getControl()));
		addProperty("lfo-type" + suffix, "Type", getLfoTypes(), FormProperty.SELECT, lfo.getType());
		addProperty("lfo-cycleSteps" + suffix, "Cycle steps", lfo.getCycleSteps(), FormProperty.NUMBER_INPUT);
		addProperty("lfo-change" + suffix, "Change", lfo.getChange(), FormProperty.ANY_INPUT);
	}
	
	public static List<String> getLfoTypes() {
		List<String> r = new ArrayList<String>();
		r.add(LFO.SINE);
		r.add(LFO.LINEAR);
		return r;
	}
	
	public static List<String> getLfoChannels() {
		List<String> r = new ArrayList<String>();
		for (String name: SoundPatch.getInstrumentNames()) {
			if (!name.equals(SoundPatch.LFOS)) {
				r.add(name + "/1");
				if (name.equals(SoundPatch.BASS) || name.equals(SoundPatch.ARPEGGIATOR)) {
					r.add(name + "/2");
				}
			}
		}
		return r;
	}
	
	public static String getNameForChannel(int channel) {
		String r = "";
		if (channel==SynthConfig.DRUM_CHANNEL) {
			r = SoundPatch.DRUMS + "/1";
		} else if (channel==SynthConfig.BASS_CHANNEL_1) {
			r = SoundPatch.BASS + "/1";
		} else if (channel==SynthConfig.BASS_CHANNEL_2) {
			r = SoundPatch.BASS + "/2";
		} else if (channel==SynthConfig.STAB_CHANNEL) {
			r = SoundPatch.STAB + "/1";
		} else if (channel==SynthConfig.ARP_CHANNEL_1) {
			r = SoundPatch.ARPEGGIATOR + "/1";
		} else if (channel==SynthConfig.ARP_CHANNEL_2) {
			r = SoundPatch.ARPEGGIATOR + "/2";
		}
		return r;
	}
	
	public static int getChannelForName(String name) {
		int r = -1;
		if (name.equals(SoundPatch.DRUMS + "/1")) {
			r = SynthConfig.DRUM_CHANNEL;
		} else if (name.equals(SoundPatch.BASS + "/1")) {
			r = SynthConfig.BASS_CHANNEL_1;
		} else if (name.equals(SoundPatch.BASS + "/2")) {
			r = SynthConfig.BASS_CHANNEL_2;
		} else if (name.equals(SoundPatch.STAB + "/1")) {
			r = SynthConfig.STAB_CHANNEL;
		} else if (name.equals(SoundPatch.ARPEGGIATOR + "/1")) {
			r = SynthConfig.ARP_CHANNEL_1;
		} else if (name.equals(SoundPatch.ARPEGGIATOR + "/2")) {
			r = SynthConfig.ARP_CHANNEL_2;
		}
		return r;
	}
	
	public static List<String> getLfoControls() {
		List<String> r = new ArrayList<String>();
		for (int i = 0; i < SynthConfig.CONTROL_NAMES.length; i++) {
			r.add(SynthConfig.CONTROL_NAMES[i]);
		}
		return r;
	}

	public static String getNameForControl(int control) {
		String r = "";
		for (int i = 0; i < SynthConfig.CONTROLS.length; i++) {
			if (SynthConfig.CONTROLS[i]==control) {
				r = SynthConfig.CONTROL_NAMES[i];
				break;
			}
		}
		return r;
	}
	
	public static int getControlForName(String name) {
		int r = -1;
		for (int i = 0; i < SynthConfig.CONTROL_NAMES.length; i++) {
			if (SynthConfig.CONTROL_NAMES[i].equals(name)) {
				r = SynthConfig.CONTROLS[i];
				break;
			}
		}
		return r;
	}
}
