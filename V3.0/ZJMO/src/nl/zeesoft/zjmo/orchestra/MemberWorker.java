package nl.zeesoft.zjmo.orchestra;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class MemberWorker extends Worker {
	private MemberObject		member		= null;
	private SocketHandler		socket		= null;
	private Protocol			protocol	= null;

	public MemberWorker(MemberObject member,SocketHandler socket, Protocol protocol) {
		super(null,null);
		setSleep(0);
		this.member = member;
		this.socket = socket;
		this.protocol = protocol;
	}

	public MemberWorker(Messenger msgr, WorkerUnion union,MemberObject member,SocketHandler socket, Protocol protocol) {
		super(msgr,union);
		setSleep(0);
		this.member = member;
		this.socket = socket;
		this.protocol = protocol;
	}
	
	@Override
	public void start() {
		boolean start = false;
		lockMe(this);
		start = socket.open();
		unlockMe(this);
		if (start) {
			super.start();
		}
	}

	@Override
	public void stop() {
		lockMe(this);
		socket.close();
		unlockMe(this);
		super.stop();
		waitForStop(1,false);
	}
	
	@Override
	public void whileWorking() {
		boolean close = false;
		boolean stop = false;
		lockMe(this);
		ZStringBuilder input = socket.readInput();
		ZStringBuilder output = null;
		if (input==null) {
			close = true;
		} else {
			protocol.setStop(false);
			protocol.setClose(false);
			output = protocol.handleInput(member,input);
			close = output==null || protocol.isClose();
			stop = protocol.isStop();
		}
		unlockMe(this);
		if (stop) {
			member.stopWorker(this);
			member.stopProgram();
		} else if (close) {
			member.stopWorker(this);
		} else {
			lockMe(this);
			socket.writeOutput(output);
			unlockMe(this);
		}
	}
}
