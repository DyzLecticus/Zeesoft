package nl.zeesoft.zdbd.midi;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;

import nl.zeesoft.zdbd.midi.lfo.ChannelLFO;
import nl.zeesoft.zdbd.midi.lfo.LFO;
import nl.zeesoft.zdbd.pattern.Rythm;
import nl.zeesoft.zdk.thread.Lock;

public class SynthConfig {
	public static final int			DRUM_CHANNEL		= 9;
	public static final int			BASS_CHANNEL_1		= 0;
	public static final int			BASS_CHANNEL_2		= 1;
	
	public static final int			VOLUME				= 7;
	public static final int			ATTACK				= 73;
	public static final int			DECAY				= 75;
	public static final int			RELEASE				= 72;
	public static final int			PAN					= 10;
	public static final int			MODULATION			= 1;
	public static final int			CHORUS				= 93;
	public static final int			FILTER				= 74;
	public static final int			RESONANCE			= 71;
	public static final int			REVERB				= 91;
	public static final int			VIB_RATE			= 76;
	public static final int			VIB_DEPTH			= 77;
	public static final int			VIB_DELAY			= 78;

	public static final int[]		CONTROLS			= {
		VOLUME,
		ATTACK,
		DECAY,
		RELEASE,
		PAN,
		MODULATION,
		CHORUS,
		FILTER,
		RESONANCE,
		REVERB,
		VIB_RATE,
		VIB_DEPTH,
		VIB_DELAY,
	};
	
	private Lock					lock				= new Lock();
	
	private SynthChannelConfig[]	channels 			= new SynthChannelConfig[16];
	private List<ChannelLFO>		lfos				= new ArrayList<ChannelLFO>();
	
	public SynthConfig() {
		initializeDefaults();
	}
	
	public void initializeDefaults() {
		lock.lock(this);
		lfos.clear();
		for (int c = 0; c < channels.length; c++) {
			channels[c] = new SynthChannelConfig();
			channels[c].channel = c;
		}
		SynthChannelConfig drumConfig = channels[DRUM_CHANNEL];
		drumConfig.instrument = 118;
		SynthChannelConfig bass1Config = channels[BASS_CHANNEL_1];
		bass1Config.instrument = 87;
		bass1Config.reverb = 0;
		bass1Config.decay = 32;
		bass1Config.filter = 16;
		SynthChannelConfig bass2Config = channels[BASS_CHANNEL_2];
		bass2Config.instrument = 84;
		bass2Config.reverb = 0;
		bass2Config.chorus = 40;
		bass2Config.pan = 0;
		bass2Config.resonance = 80;
		lfos.add(new ChannelLFO(BASS_CHANNEL_2));
		lfos.add(new ChannelLFO(BASS_CHANNEL_2,PAN,LFO.TRIANGLE,6,1));
		lock.unlock(this);
	}
	
	public void configureSynthesizer(Synthesizer synthesizer) {
		lock.lock(this);
		for (int c = 0; c < channels.length; c++) {
			SynthChannelConfig config = channels[c];
			MidiChannel chan = synthesizer.getChannels()[c];
			if (chan.getProgram()!=config.instrument) {
				chan.programChange(config.instrument);
			}
			chan.controlChange(VOLUME,config.volume);
			chan.controlChange(ATTACK,config.attack);
			chan.controlChange(DECAY,config.decay);
			chan.controlChange(RELEASE,config.release);
			chan.controlChange(PAN,config.pan);
			chan.controlChange(MODULATION,config.modulation);
			chan.controlChange(CHORUS,config.chorus);
			chan.controlChange(FILTER,config.filter);
			chan.controlChange(RESONANCE,config.resonance);
			chan.controlChange(REVERB,config.reverb);
			chan.controlChange(VIB_RATE,config.vib_rate);
			chan.controlChange(VIB_DEPTH,config.vib_depth);
			chan.controlChange(VIB_DELAY,config.vib_delay);
			if (chan.getChannelPressure()!=config.pressure) {
				chan.setChannelPressure(config.pressure);
			}
			if (chan.getMute()!=config.mute) {
				chan.setMute(config.mute);
			}
			if (chan.getSolo()!=config.solo) {
				chan.setSolo(config.solo);
			}
		}
		lock.unlock(this);
	}

	// TODO: Add to recorded/exported sequences
	public void addInitialSynthConfig(Sequence sequence) {
		lock.lock(this);
		for (SynthChannelConfig channelConfig: channels) {
			int trackNum = getTrackNumForChannel(channelConfig.channel);
			if (trackNum>=0) {
				MidiSequenceUtil.createEventOnTrack(
					sequence.getTracks()[trackNum],ShortMessage.PROGRAM_CHANGE,channelConfig.channel,channelConfig.instrument,0,0
				);
				for (Integer control: SynthConfig.CONTROLS) {
					int value = channelConfig.getControlValue(control);
					MidiSequenceUtil.createEventOnTrack(
						sequence.getTracks()[trackNum],ShortMessage.CONTROL_CHANGE,channelConfig.channel,control,value,0
					);
				}
			}
		}
		lock.unlock(this);
	}
	
	public void setRythm(Rythm rythm) {
		lock.lock(this);
		for (ChannelLFO lfo: lfos) {
			lfo.setRythm(rythm);
		}
		lock.unlock(this);
	}
	
	public Sequence generateSequenceForChannelLFOs(long ticks) {
		Sequence r = MidiSequenceUtil.createSequence(1);
		if (r!=null) {
			lock.lock(this);
			Track track = r.getTracks()[0]; 
			for (ChannelLFO lfo: lfos) {
				int channel = lfo.getChannel();
				int control = lfo.getControl();
				List<Float> changes = lfo.getChangesForTicks(ticks);
				int value = channels[channel].getControlValue(control);
				long tick = 0;
				int pVal = value;
				for (Float change: changes) {
					int val = value;
					if (change>0F) {
						val = value + (int)(127F * change);
						if (val>127F) {
							val = 127;
						}
					} else if (change<0F) {
						val = value - (int)(127F * (change * -1F));
						if (val<0F) {
							val = 0;
						}
					}
					if (val!=pVal) {
						MidiSequenceUtil.createEventOnTrack(
							track, ShortMessage.CONTROL_CHANGE, channel, control, val, tick
						);
					}
					pVal = val;
					tick++;
				}
			}
			lock.unlock(this);
		}
		return r;
	}
	
	public void commitChannelLFOs(long ticks) {
		lock.lock(this);
		for (ChannelLFO lfo: lfos) {
			lfo.commitTicks(ticks);
		}
		lock.unlock(this);
	}
	
	public static int getTrackNumForChannel(int channel) {
		int r = -1;
		if (channel==DRUM_CHANNEL) {
			r = 0;
		} else if (channel==BASS_CHANNEL_1) {
			r = 1;
		} else if (channel==BASS_CHANNEL_2) {
			r = 2;
		}
		return r;
	}
}
