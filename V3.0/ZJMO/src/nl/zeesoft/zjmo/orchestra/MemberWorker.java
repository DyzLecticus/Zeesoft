package nl.zeesoft.zjmo.orchestra;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControl;

/**
 * Handles sessions for the MemberObject.
 */
public class MemberWorker extends Worker {
	private MemberObject		member		= null;
	private SocketHandler		socket		= null;
	private ProtocolObject		protocol	= null;

	public MemberWorker(Messenger msgr, WorkerUnion union,MemberObject member,SocketHandler socket, ProtocolObject protocol) {
		super(msgr,union);
		setSleep(0);
		this.member = member;
		this.socket = socket;
		this.protocol = protocol;
	}

	public boolean isControl() {
		return protocol instanceof ProtocolControl;
	}
	
	@Override
	public void start() {
		boolean start = false;
		start = socket.open();
		if (start) {
			super.start();
		}
	}

	@Override
	public void stop() {
		super.stop();
		socket.writeOutput(protocol.getCommandJson(ProtocolObject.CLOSE_SESSION,null));
		socket.close();
	}
	
	@Override
	public void whileWorking() {
		boolean close = false;
		boolean stop = false;
		ZStringBuilder input = socket.readInput();
		ZStringBuilder output = null;
		if (!socket.isOpen()) {
			close = true;
		} else {
			if (input==null) {
				close = true;
			} else {
				protocol.setStop(false);
				protocol.setClose(false);
				output = protocol.handleInput(member,input);
				close = output==null || protocol.isClose();
				stop = protocol.isStop();
			}
		}
		if (stop) {
			member.stopWorker(this);
			member.stopProgram(this);
		} else if (close) {
			member.stopWorker(this);
		} else {
			socket.writeOutput(output);
		}
	}
}
