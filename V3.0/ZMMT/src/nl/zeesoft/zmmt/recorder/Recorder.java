package nl.zeesoft.zmmt.recorder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class Recorder extends Locker {
	private RecorderWorker			worker			= null;
	
	private List<String>			mixerNames		= null;
	
	private Mixer					mixer			= null;
	private AudioFormat				audioFormat		= null;
	private AudioFileFormat.Type	fileFormat		= null;
	private TargetDataLine			targetDataLine	= null;
	private AudioInputStream		inputStream		= null;
	
	private File					outputFile		= null;
	
	public Recorder(Messenger msgr, WorkerUnion uni) {
		super(msgr);
		worker = new RecorderWorker(msgr,uni,this);
	}

	public void intialize(String mixerName) {
		intialize(mixerName,null,null);
	}

	public void setOutputFile(File outputFile) {
		lockMe(this);
		this.outputFile = outputFile;
		unlockMe(this);
	}
	
	public void start() {
		lockMe(this);
		if (targetDataLine!=null && outputFile!=null) {
			targetDataLine.start();
			worker.start();
		}
		unlockMe(this);
	}
	
	protected void record() {
		try {
			AudioSystem.write(inputStream,fileFormat,outputFile);
		} catch (IOException e) {
			getMessenger().error(this,"Error while recording",e);
		}
	}
	
	public void stop() {
		lockMe(this);
		if (worker.isWorking()) {
			worker.stop();
			targetDataLine.close();
		}
		unlockMe(this);
	}
	
	public void close() {
		lockMe(this);
		mixer.close();
		unlockMe(this);
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

	
	public void intialize(String mixerName, AudioFormat audioFmt, AudioFileFormat.Type fileFmt) {
		if (!getMixerNames().contains(mixerName)) {
			getMessenger().error(this,"Mixer not found: " + mixerName);
		} else {
			lockMe(this);
			
			audioFormat = null;
			fileFormat = null;
			mixer = null;
			targetDataLine = null;
			inputStream = null;
			
			if (audioFmt!=null) {
				audioFormat = audioFmt;
			} else {
				audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,44100.0F,16,2,4,44100.0F,false);
			}
			
			if (fileFmt!=null) {
				fileFormat = fileFmt;
			} else {
				fileFormat = AudioFileFormat.Type.WAVE;
			}

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
			if (mixer.isOpen()) {
				DataLine.Info info = new DataLine.Info(TargetDataLine.class,audioFormat);
				try {
					targetDataLine = (TargetDataLine) mixer.getLine(info);
					targetDataLine.open(audioFormat);
					if (!targetDataLine.isOpen()) {
						targetDataLine = null;
					}
				} catch (IllegalArgumentException e) {
					getMessenger().error(this,"Unable to obtain a recording line from mixer: " + mixerName,e);
				} catch (LineUnavailableException e) {
					getMessenger().error(this,"Unable to obtain a recording line from mixer: " + mixerName,e);
				}
				
			}

			if (targetDataLine!=null && targetDataLine.isOpen()) {
				inputStream = new AudioInputStream(targetDataLine);
			}
			
			unlockMe(this);
		}
	}
}
