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
	public static final int			STAB_CHANNEL		= 2;
	public static final int			ARP_CHANNEL_1		= 7;
	public static final int			ARP_CHANNEL_2		= 8;
	public static final int			ECHO_CHANNEL_1		= 10;
	public static final int			ECHO_CHANNEL_2		= 11;
	public static final int			ECHO_CHANNEL_3		= 12;
	public static final int			ECHO_CHANNEL_4		= 13;
	
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
	
	public static final String[]	CONTROL_NAMES		= {
		"Volume",
		"Attack",
		"Decay",
		"Release",
		"Pan",
		"Modulation",
		"Chorus",
		"Filter",
		"Resonance",
		"Reverb",
		"Vibrato rate",
		"Vibrato depth",
		"Vibrato delay",
	};
	
	private Lock					lock				= new Lock();
	
	private SynthChannelConfig[]	channels 			= new SynthChannelConfig[16];
	private List<ChannelLFO>		lfos				= new ArrayList<ChannelLFO>();
	private List<EchoConfig>		echos				= new ArrayList<EchoConfig>();
	
	public SynthConfig() {
		initializeDefaults();
	}
	
	public SynthChannelConfig getChannelConfig(int channel) {
		SynthChannelConfig r = null;
		lock.lock(this);
		if (channel >= 0 && channel < 16) {
			r = new SynthChannelConfig();
			r.copyFrom(channels[channel]);
		}
		lock.unlock(this);
		return r;
	}
	
	public void setChannelInstrument(int channel, int instrument) {
		lock.lock(this);
		if (instrument >= 0 && instrument < 128) {
			channels[channel].instrument = instrument;
		}
		applyEchoConfigNoLock();
		lock.unlock(this);
	}
	
	public void setChannelControl(int channel, int control, int value) {
		lock.lock(this);
		if (value >= 0 && value < 128) {
			channels[channel].setControlValue(control, value);
		}
		applyEchoConfigNoLock();
		lock.unlock(this);
	}
	
	public List<ChannelLFO> getLFOs() {
		List<ChannelLFO> r = new ArrayList<ChannelLFO>();
		lock.lock(this);
		for (ChannelLFO lfo: lfos) {
			r.add(lfo.copy());
		}
		lock.unlock(this);
		return r;
	}
	
	public void setLFOProperty(int index, String property, Object value) {
		lock.lock(this);
		if (index>=0 && index<lfos.size()) {
			ChannelLFO lfo = lfos.get(index);
			if (property.equals("active")) {
				lfo.setActive((Boolean)value);
			} else if (property.equals("channel")) {
				lfo.setChannel((int)value);
			} else if (property.equals("control")) {
				lfo.setControl((int)value);
			} else if (property.equals("type")) {
				lfo.setType((String)value);
			} else if (property.equals("cycleSteps")) {
				lfo.setCycleSteps((int)value);
			} else if (property.equals("change")) {
				lfo.setChange((float)value);
			}
		}
		lock.unlock(this);
	}
	
	public List<EchoConfig> getEchos() {
		List<EchoConfig> r = new ArrayList<EchoConfig>();
		lock.lock(this);
		for (EchoConfig echo: echos) {
			r.add(echo.copy());
		}
		lock.unlock(this);
		return r;
	}
	
	public void setEchoProperty(int index, String property, Object value) {
		lock.lock(this);
		if (index>=0 && index<echos.size()) {
			EchoConfig echo = echos.get(index);
			if (property.equals("sourceChannel")) {
				echo.sourceChannel = (int)value;
			} else if (property.equals("delay")) {
				echo.chorus = (int)value;
			} else if (property.equals("pan")) {
				echo.pan = (int)value;
			} else if (property.equals("velocity")) {
				echo.velocity = (float)value;
			} else if (property.equals("filter")) {
				echo.filter = (float)value;
			} else if (property.equals("reverb")) {
				echo.reverb = (float)value;
			} else if (property.equals("chorus")) {
				echo.chorus = (float)value;
			}
			applyEchoConfigNoLock();
		}
		lock.unlock(this);
	}
	
	public void addEchoChanges(List<int[]> changes) {
		lock.lock(this);
		int[] change = changes.get(0);
		for (EchoConfig echo: echos) {
			if (change[0] == echo.sourceChannel) {
				int[] add = new int[3];
				add[0] = echo.targetChannel;
				add[1] = change[1];
				add[2] = channels[echo.targetChannel].getControlValue(change[1]);
				changes.add(add);
			}
		}
		lock.unlock(this);
	}
	
	public void applyInstrumentPropertyChange(Synthesizer synthesizer, int[] change) {
		int channel = change[0];
		int control = change[1];
		int value = change[2];
		MidiChannel chan = synthesizer.getChannels()[channel];
		if (control==0) {
			chan.programChange(value);
		} else {
			chan.controlChange(control,value);
		}
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
				if (lfo.isActive()) {
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
			}
			lock.unlock(this);
		}
		return r;
	}
	
	public void commitChannelLFOs(long ticks) {
		lock.lock(this);
		for (ChannelLFO lfo: lfos) {
			if (lfo.isActive()) {
				lfo.commitTicks(ticks);
			}
		}
		lock.unlock(this);
	}
	
	public void initializeDefaults() {
		lock.lock(this);
		lfos.clear();
		echos.clear();
		
		for (int c = 0; c < channels.length; c++) {
			channels[c] = new SynthChannelConfig();
			channels[c].channel = c;
		}
		
		EchoConfig echo = null;
		
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
		lfos.add(new ChannelLFO(BASS_CHANNEL_2,PAN,LFO.LINEAR,6,1));

		SynthChannelConfig stabConfig = channels[STAB_CHANNEL];
		stabConfig.instrument = 81;
		stabConfig.reverb = 64;
		stabConfig.chorus = 40;

		SynthChannelConfig arp1Config = channels[ARP_CHANNEL_1];
		arp1Config.instrument = 80;
		arp1Config.reverb = 48;
		arp1Config.filter = 16;
		arp1Config.pan = 32;
		SynthChannelConfig arp2Config = channels[ARP_CHANNEL_2];
		arp2Config.instrument = 83;
		arp2Config.reverb = 48;

		lfos.add(new ChannelLFO(ARP_CHANNEL_1,FILTER,LFO.SINE,32,0.75F));
		lfos.add(new ChannelLFO(ARP_CHANNEL_2,FILTER,LFO.SINE,32,-0.4F));
		lfos.add(new ChannelLFO(ARP_CHANNEL_1,PAN,LFO.LINEAR,12,0.5F));
		lfos.add(new ChannelLFO(ARP_CHANNEL_2,PAN,LFO.LINEAR,12,-0.5F));

		echo = new EchoConfig();
		echo.sourceChannel = ARP_CHANNEL_1;
		echo.targetChannel = ECHO_CHANNEL_1;
		echo.pan = 16;
		echos.add(echo);
		
		echo = new EchoConfig();
		echo.sourceChannel = ARP_CHANNEL_2;
		echo.targetChannel = ECHO_CHANNEL_2;
		echo.pan = 112;
		echos.add(echo);
		
		echo = new EchoConfig();
		echo.sourceChannel = ARP_CHANNEL_1;
		echo.targetChannel = ECHO_CHANNEL_3;
		echo.pan = 112;
		echo.delay = 6;
		echo.velocity = 0.40F;
		echo.filter = 0.75F;
		echo.reverb = 1.5F;
		echo.chorus = 1.5F;
		echos.add(echo);
		
		echo = new EchoConfig();
		echo.sourceChannel = ARP_CHANNEL_2;
		echo.targetChannel = ECHO_CHANNEL_4;
		echo.pan = 16;
		echo.delay = 6;
		echo.velocity = 0.40F;
		echo.filter = 0.75F;
		echo.reverb = 1.5F;
		echo.chorus = 1.5F;
		echos.add(echo);
		
		applyEchoConfigNoLock();
		
		for (int i = lfos.size(); i < 10; i++) {
			ChannelLFO lfo = new ChannelLFO();
			lfo.setActive(false);
			lfos.add(lfo);
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
		} else if (channel==STAB_CHANNEL) {
			r = 3;
		} else if (channel==ARP_CHANNEL_1) {
			r = 4;
		} else if (channel==ARP_CHANNEL_2) {
			r = 5;
		} else if (channel==ECHO_CHANNEL_1) {
			r = 6;
		} else if (channel==ECHO_CHANNEL_2) {
			r = 7;
		} else if (channel==ECHO_CHANNEL_3) {
			r = 8;
		} else if (channel==ECHO_CHANNEL_4) {
			r = 9;
		}
		return r;
	}
	
	public static int getTotalTracks() {
		return 10;
	}
	
	protected void applyEchoConfigNoLock() {
		for (EchoConfig echo: echos) {
			if (echo.sourceChannel>=0) {
				SynthChannelConfig source = channels[echo.sourceChannel];
				SynthChannelConfig target = channels[echo.targetChannel];
				target.copyFrom(source);
				target.pan = echo.pan;
				target.filter = (int)(target.filter * echo.filter);
				target.reverb = (int)(target.reverb * echo.reverb);
				target.chorus = (int)(target.chorus * echo.chorus);
				if (target.filter < 0) {
					target.filter = 0;
				} else if (target.filter > 127) {
					target.filter = 127;
				}
				if (target.reverb < 0) {
					target.reverb = 0;
				} else if (target.reverb > 127) {
					target.reverb = 127;
				}
				if (target.chorus < 0) {
					target.chorus = 0;
				} else if (target.chorus > 127) {
					target.chorus = 127;
				}
			}
		}
	}
}
