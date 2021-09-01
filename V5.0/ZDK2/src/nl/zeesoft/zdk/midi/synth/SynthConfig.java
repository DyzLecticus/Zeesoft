package nl.zeesoft.zdk.midi.synth;

import java.util.ArrayList;
import java.util.List;

public class SynthConfig {
	public static final int		BASS_CHANNEL_1		= 0;
	public static final int		BASS_CHANNEL_2		= 1;
	public static final int		STAB_CHANNEL		= 2;
	public static final int		ARP_CHANNEL_1		= 3;
	public static final int		ARP_CHANNEL_2		= 4;
	public static final int		DRUM_CHANNEL		= 9;
	
	public List<ChannelConfig>	channels			= new ArrayList<ChannelConfig>();
	
	public SynthConfig() {
		for (int i = 0; i < 16; i++) {
			channels.add(new ChannelConfig());
		}
	}
	
	public void copyFrom(SynthConfig other) {
		int i = 0;
		for (ChannelConfig channel: channels) {
			channel.copyFrom(other.channels.get(i));
			i++;
		}
	}
	
	public ChannelConfig getBassChannel(int num) {
		ChannelConfig r = channels.get(BASS_CHANNEL_1);
		if (num==1) {
			r = channels.get(BASS_CHANNEL_2);
		}
		return r;
	}
	
	public ChannelConfig getStabChannel() {
		return channels.get(STAB_CHANNEL);
	}
	
	public ChannelConfig getArpChannel(int num) {
		ChannelConfig r = channels.get(ARP_CHANNEL_1);
		if (num==1) {
			r = channels.get(ARP_CHANNEL_2);
		}
		return r;
	}
	
	public ChannelConfig getDrumChannel() {
		return channels.get(DRUM_CHANNEL);
	}
	
	public int getInstrument(int channel) {
		return channels.get(channel).instrument;
	}
	
	public void setInstrument(int channel, int instrument) {
		if (instrument >= 0 && instrument <= 127) {
			channels.get(channel).instrument = instrument;
		}
	}
	
	public int getControlValue(int channel, int control) {
		return channels.get(channel).getControlValue(control);
	}
	
	public void setControlValue(int channel, int control, int value) {
		channels.get(channel).setControlValue(control, value);
	}
}
