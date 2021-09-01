package nl.zeesoft.zdk.test.midi;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.midi.MidiSys;
import nl.zeesoft.zdk.midi.synth.ChannelConfig;
import nl.zeesoft.zdk.midi.synth.SynthConfig;

public class TestSynth {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		SynthConfig sc = new SynthConfig();
		assert sc.channels.size() == 16;
		assert sc.getBassChannel(0) == sc.channels.get(SynthConfig.BASS_CHANNEL_1);
		assert sc.getBassChannel(1) == sc.channels.get(SynthConfig.BASS_CHANNEL_2);
		assert sc.getStabChannel() == sc.channels.get(SynthConfig.STAB_CHANNEL);
		assert sc.getArpChannel(0) == sc.channels.get(SynthConfig.ARP_CHANNEL_1);
		assert sc.getArpChannel(1) == sc.channels.get(SynthConfig.ARP_CHANNEL_2);
		assert sc.getDrumChannel() == sc.channels.get(SynthConfig.DRUM_CHANNEL);
		
		for (int i = 0; i < ChannelConfig.CONTROLS.length; i++) {
			sc.getDrumChannel().setControlValue(ChannelConfig.CONTROLS[i], i * 2);
			assert sc.getDrumChannel().getControlValue(ChannelConfig.CONTROLS[i]) == (i * 2);
			assert ChannelConfig.CONTROL_NAMES[i].length() > 2;
		}
		sc.setInstrument(SynthConfig.DRUM_CHANNEL, 123);
		assert sc.getInstrument(SynthConfig.DRUM_CHANNEL) == 123;
		sc.setInstrument(SynthConfig.DRUM_CHANNEL, -1);
		assert sc.getInstrument(SynthConfig.DRUM_CHANNEL) == 123;
		sc.setInstrument(SynthConfig.DRUM_CHANNEL, 128);
		assert sc.getInstrument(SynthConfig.DRUM_CHANNEL) == 123;
		
		SynthConfig sc2 = new SynthConfig();
		sc2.copyFrom(sc);
		for (int i = 0; i < ChannelConfig.CONTROLS.length; i++) {
			assert sc2.getDrumChannel().getControlValue(ChannelConfig.CONTROLS[i]) == (i * 2);
		}
		assert sc2.getInstrument(SynthConfig.DRUM_CHANNEL) == 123;
		
		sc.getDrumChannel().setControlValue(999, 100);
		assert sc.getDrumChannel().getControlValue(999) == ChannelConfig.DEFAULT;
		
		sc.setControlValue(SynthConfig.DRUM_CHANNEL,ChannelConfig.FILTER,-1);
		assert sc.getControlValue(SynthConfig.DRUM_CHANNEL,ChannelConfig.FILTER) == 0;
		sc.setControlValue(SynthConfig.DRUM_CHANNEL,ChannelConfig.FILTER,128);
		assert sc.getControlValue(SynthConfig.DRUM_CHANNEL,ChannelConfig.FILTER) == 127;
		
		sc.getStabChannel().solo = true;
		sc.getStabChannel().mute = true;
		sc.getStabChannel().pressure = 111;
		
		assert new MidiSys() != null;
		
		assert !MidiSys.isInitialized();
		MidiSys.destroy();
		MidiSys.initializeSynth(true, false);
		assert !MidiSys.isInitialized();
		MidiSys.initializeSynth(false, true);
		assert !MidiSys.isInitialized();
		MidiSys.initialize();
		assert MidiSys.isInitialized();
		
		MidiSys.synth.setConfig(sc);
		assert MidiSys.synth.getConfig() != sc;
		assert MidiSys.synth.getConfig().getInstrument(SynthConfig.DRUM_CHANNEL) == sc.getInstrument(SynthConfig.DRUM_CHANNEL);
		assert MidiSys.synth.getSynthesizer().getChannels()[SynthConfig.DRUM_CHANNEL].getProgram() == 123;
		assert MidiSys.synth.getSynthesizer().getChannels()[SynthConfig.STAB_CHANNEL].getSolo();
		assert MidiSys.synth.getSynthesizer().getChannels()[SynthConfig.STAB_CHANNEL].getMute();
		assert MidiSys.synth.getSynthesizer().getChannels()[SynthConfig.STAB_CHANNEL].getChannelPressure() == 111;

		MidiSys.synth.setInstrument(SynthConfig.STAB_CHANNEL, 111);
		assert MidiSys.synth.getInstrument(SynthConfig.STAB_CHANNEL) == 111;
		MidiSys.synth.setControlValue(SynthConfig.STAB_CHANNEL, ChannelConfig.CHORUS, 111);
		assert MidiSys.synth.getControlValue(SynthConfig.STAB_CHANNEL, ChannelConfig.CHORUS) == 111;
				
		MidiSys.destroy();
		assert !MidiSys.isInitialized();
	}
}
