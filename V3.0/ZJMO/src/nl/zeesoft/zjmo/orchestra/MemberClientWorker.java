package nl.zeesoft.zjmo.orchestra;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

/**
 * Used to time out socket reading.
 */
public class MemberClientWorker extends Worker {
	private ZStringBuilder		input		= null;
	private SocketHandler		socket		= null;
	private boolean				reading		= false;
	private boolean				read		= false;

	public MemberClientWorker(Messenger msgr, WorkerUnion union,SocketHandler socket,int timeout) {
		super(msgr,union);
		setSleep(timeout);
		this.socket = socket;
	}

	public ZStringBuilder getInput() {
		return input;
	}
	
	private void setReading(boolean reading) {
		lockMe(this);
		this.reading = reading;
		unlockMe(this);
	}

	public boolean isReading() {
		boolean r = false;
		lockMe(this);
		r = reading;
		unlockMe(this);
		return r;
	}
	
	
	@Override
	public void start() {
		if (socket.isOpen()) {
			setReading(true);
			read = false;
			input = null;
			super.start();
		}
	}
	
	@Override
	public void whileWorking() {
		if (!read) {
			input = socket.readInput();
			read = true;
			setReading(false);
			stop();
		}
	}
}
