package nl.zeesoft.zso.orchestra.members;

import java.util.Date;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class SamplePlayerWorker extends Worker {
	private SamplePlayer	player		= null;
	private long			end			= 0;
	
	public SamplePlayerWorker(Messenger msgr, WorkerUnion union,SamplePlayer player) {
		super(msgr,union);
		setSleep(1);
		this.player = player;
	}
	
	public void play(long startMs,long durationMs) {
		if (isWorking()) {
			stop();
		}
		player.setStartMs(startMs);
		if (durationMs>0) {
			end = (new Date()).getTime() + durationMs;
			start();
		} else {
			end = 0;
		}
		player.startClip();
	}
	
	@Override
	public void whileWorking() {
		if (end>0 && (new Date()).getTime()>=end) {
			stop();
			player.stopClip();
			player.setStartMs(0);
		} else if (player.clipHasStopped()) {
			stop();
			player.setStartMs(0);
		}
	}
}
