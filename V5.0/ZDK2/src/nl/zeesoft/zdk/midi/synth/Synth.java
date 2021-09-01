package nl.zeesoft.zdk.midi.synth;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.Synthesizer;

public class Synth {
	protected Synthesizer	synthesizer = null;
	protected SynthConfig	config		= new SynthConfig();
	
	public Synth(Synthesizer synthesizer) {
		this.synthesizer = synthesizer;
	}
	
	public Synthesizer getSynthesizer() {
		return synthesizer;
	}
	
	public synchronized void setConfig(SynthConfig config) {
		this.config = config;
		configureSynthesizer();
	}
	
	public synchronized SynthConfig getConfig() {
		SynthConfig r = new SynthConfig();
		r.copyFrom(config);
		return r;
	}
	
	public synchronized int getInstrument(int channel) {
		return config.getInstrument(channel);
	}
	
	public synchronized void setInstrument(int channel, int instrument) {
		config.setInstrument(channel, instrument);
		instrument = config.getInstrument(channel);
		MidiChannel chan = synthesizer.getChannels()[channel];
		if (chan.getProgram()!=instrument) {
			chan.programChange(instrument);
		}
	}
		
	public synchronized int getControlValue(int channel, int control) {
		return config.getControlValue(channel, control);
	}
	
	public synchronized void setControlValue(int channel, int control, int value) {
		config.setControlValue(channel, control, value);
		synthesizer.getChannels()[channel].controlChange(control, config.getControlValue(channel, control));
	}
	
	public synchronized void configureSynthesizer() {
		int i = 0;
		for (ChannelConfig channel: config.channels) {
			MidiChannel chan = synthesizer.getChannels()[i];
			if (chan.getProgram()!=channel.instrument) {
				chan.programChange(channel.instrument);
			}
			for (int c = 0; c < ChannelConfig.CONTROLS.length; c++) {
				chan.controlChange(ChannelConfig.CONTROLS[c],channel.getControlValue(ChannelConfig.CONTROLS[c]));
			}
			configureRemainingChannelProperties(channel, chan);
			i++;
		}
	}
	
	public void allSoundOff() {
		MidiChannel[] channels = synthesizer.getChannels();
		for (int c = 0; c < channels.length; c++) {
			MidiChannel channel = channels[c];
			channel.allSoundOff();
		}
	}
	
	protected void configureRemainingChannelProperties(ChannelConfig channel, MidiChannel chan) {
		if (chan.getChannelPressure()!=channel.pressure) {
			chan.setChannelPressure(channel.pressure);
		}
		if (chan.getMute()!=channel.mute) {
			chan.setMute(channel.mute);
		}
		if (chan.getSolo()!=channel.solo) {
			chan.setSolo(channel.solo);
		}
	}
}
