package nl.zeesoft.zdbd.midi;

import java.util.Set;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;

import nl.zeesoft.zdk.thread.Lock;

public class VolumeControl {
	private Lock	lock		= new Lock();
	
	private float	global		= 1.0F;
	private float	drums		= 1.0F;
	private float	bass		= 1.0F;
	private float	stab		= 1.0F;
	private float	arpeggiator	= 1.0F;

	public void copyFrom(VolumeControl vc) {
		lock.lock(this);
		this.global = vc.getGlobal();
		this.drums = vc.getDrums();
		this.bass = vc.getBass();
		this.stab = vc.getStab();
		this.arpeggiator = vc.getArpeggiator();
		lock.unlock(this);
	}
	
	public float getGlobal() {
		lock.lock(this);
		float r = global;
		lock.unlock(this);
		return r;
	}

	public void setGlobal(float global) {
		lock.lock(this);
		this.global = global;
		lock.unlock(this);
	}

	public float getDrums() {
		lock.lock(this);
		float r = drums;
		lock.unlock(this);
		return r;
	}

	public void setDrums(float drums) {
		lock.lock(this);
		this.drums = drums;
		lock.unlock(this);
	}

	public float getBass() {
		lock.lock(this);
		float r = bass;
		lock.unlock(this);
		return r;
	}

	public void setBass(float bass) {
		lock.lock(this);
		this.bass = bass;
		lock.unlock(this);
	}

	public float getStab() {
		lock.lock(this);
		float r = stab;
		lock.unlock(this);
		return r;
	}

	public void setStab(float stab) {
		lock.lock(this);
		this.stab = stab;
		lock.unlock(this);
	}

	public float getArpeggiator() {
		lock.lock(this);
		float r = arpeggiator;
		lock.unlock(this);
		return r;
	}

	public void setArpeggiator(float arpeggiator) {
		lock.lock(this);
		this.arpeggiator = arpeggiator;
		lock.unlock(this);
	}
	
	public void applyToEvents(Set<MidiEvent> events) {
		lock.lock(this);
		for (MidiEvent event: events) {
			if (event.getMessage() instanceof ShortMessage) {
				ShortMessage msg = (ShortMessage) event.getMessage();
				if (msg.getCommand()==ShortMessage.NOTE_ON) {
					int data2 = msg.getData2();
					if (global < 1.0F) {
						data2 = (int)((float)data2 * global);
					}
					if (drums < 1.0F && msg.getChannel()==SynthConfig.DRUM_CHANNEL) {
						data2 = (int)((float)data2 * drums);
					}
					if (bass < 1.0F && 
						(msg.getChannel()==SynthConfig.BASS_CHANNEL_1 || msg.getChannel()==SynthConfig.BASS_CHANNEL_2)
						) {
						data2 = (int)((float)data2 * bass);
					}
					if (stab < 1.0F && msg.getChannel()==SynthConfig.STAB_CHANNEL) {
						data2 = (int)((float)data2 * stab);
					}
					if (arpeggiator < 1.0F && 
						(msg.getChannel()==SynthConfig.ARP_CHANNEL_1 || msg.getChannel()==SynthConfig.ARP_CHANNEL_2)
						) {
						data2 = (int)((float)data2 * arpeggiator);
					}
					try {
						msg.setMessage(ShortMessage.NOTE_ON, msg.getChannel(), msg.getData1(), data2);
					} catch (InvalidMidiDataException e) {
						e.printStackTrace();
					}
				}
			}
		}
		lock.unlock(this);
	}
}
