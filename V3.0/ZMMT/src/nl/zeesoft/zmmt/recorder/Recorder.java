package nl.zeesoft.zmmt.recorder;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class Recorder extends Locker {
	private List<String>			mixerNames		= null;
	
	private Mixer					mixer			= null;
	private AudioFormat				audioFormat		= null;
	private AudioFileFormat.Type	fileFormat		= null;
	private TargetDataLine			targetDataLine	= null;
	
	public Recorder(Messenger msgr) {
		super(msgr);
	}
	
	public void intialize(String mixerName) {
		if (!getMixerNames().contains(mixerName)) {
			getMessenger().error(this,"Mixer not found: " + mixerName);
		} else {
			lockMe(this);
			
			audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,44100.0F,16,2,4,44100.0F,false);
			fileFormat = AudioFileFormat.Type.WAVE;

			Mixer.Info[] infos = AudioSystem.getMixerInfo();
			for (Mixer.Info info: infos) {
				if (info.getName().equals(mixerName)) {
					mixer = AudioSystem.getMixer(info);
					break;
				}
			}
			
			try {
				mixer.open();
			} catch (LineUnavailableException e) {
				getMessenger().error(this,"Unable to open mixer: " + mixerName,e);
			}
			
			DataLine.Info info = new DataLine.Info(TargetDataLine.class,audioFormat);
			try {
				targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
				targetDataLine.open(audioFormat);
			} catch (LineUnavailableException e) {
				getMessenger().error(this,"Unable to obtain a recording line from mixer: " + mixerName,e);
			}
			
			unlockMe(this);
		}
	}
	
	public List<String> getMixerNames() {
		List<String> r = new ArrayList<String>();
		lockMe(this);
		if (mixerNames==null) {
			mixerNames = new ArrayList<String>();
			Mixer.Info[] infos = AudioSystem.getMixerInfo();
			for (Mixer.Info info: infos) {
				mixerNames.add(info.getName());
			}
		}
		r = new ArrayList<String>(mixerNames);
		unlockMe(this);
		return r;
	}
}
