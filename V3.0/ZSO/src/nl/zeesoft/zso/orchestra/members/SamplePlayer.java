package nl.zeesoft.zso.orchestra.members;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.members.Player;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolWork;

public class SamplePlayer extends Player {
	private String				error	= "";
	private	SamplePlayerWorker	worker	= null;
	private Clip				clip	= null;
	
	public SamplePlayer(Messenger msgr, Orchestra orch, String positionName, int positionBackupNumber) {
		super(msgr, orch, positionName, positionBackupNumber);
		loadClip(getSampleFileNameForPosition());
		if (clip!=null) {
			worker = new SamplePlayerWorker(msgr,getUnion(),this);
		} else {
			error = "Failed to load sample";
		}
	}

	@Override
	public void stop(Worker ignoreWorker) {
		worker.stop();
		super.stop(ignoreWorker);
	}

	@Override
	protected ProtocolWork getNewWorkProtocol() {
		return new SamplePlayerProtocol();
	}
	
	protected String getSampleFileNameForPosition() {
		return "/samples/" + getPosition().getName() + ".wav";
	}
	
    protected void loadClip(String fileName) {
    	InputStream is = getClass().getResourceAsStream(fileName);
        AudioInputStream stream = null;
		try {
			stream = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
		} catch (UnsupportedAudioFileException e) {
        	getMessenger().error(this,"File not supported: " + fileName + ", error: " + e);
		} catch (IOException e) {
        	getMessenger().error(this,"Exception loading file: " + fileName + ", error: " + e);
		}
		if (stream!=null) {
            try {
				clip = AudioSystem.getClip();
			} catch (LineUnavailableException e) {
	        	getMessenger().error(this,"No line available: " + e);
			}
            try {
				clip.open(stream);
			} catch (LineUnavailableException | IOException e) {
	        	getMessenger().error(this,"Failed to open clip: " + e);
			}
		}
    }
    
    public String playClip(long startMs,long durationMs) {
    	if (worker!=null) {
    		worker.play(startMs, durationMs);
    	}
    	return error;
    }
    
	protected void setStartMs(long ms) {
		clip.setMicrosecondPosition(ms);
	}
	
	protected void startClip(){
		clip.start();
	}
	
	protected void stopClip(){
		clip.stop();
	}	

	protected boolean clipHasStopped(){
		return !clip.isActive();
	}	
}
