package nl.zeesoft.zdk.midi.synth;

public class DefaultSynthConfig extends SynthConfig {
	public DefaultSynthConfig() {
		configureBass();
		configureStab();
		configureArp();
		configureDrum();
	}
	
	protected void configureBass() {
		ChannelConfig channel = getBassChannel(0);
		channel.instrument = 87;
		channel.reverb = 0;
		channel.decay = 32;
		channel.filter = 16;
		channel = getBassChannel(1);
		channel.instrument = 84;
		channel.reverb = 0;
		channel.decay = 40;
		channel.resonance = 80;
	}
	
	protected void configureStab() {
		ChannelConfig channel = getStabChannel();
		channel.instrument = 81;
		channel.reverb = 64;
		channel.chorus = 40;
	}
	
	protected void configureArp() {
		ChannelConfig channel = getArpChannel(0);
		channel.instrument = 80;
		channel.reverb = 48;
		channel = getArpChannel(1);
		channel.instrument = 83;
		channel.reverb = 48;
	}
	
	protected void configureDrum() {
		ChannelConfig channel = getDrumChannel();
		channel.instrument = 118;
	}
}
