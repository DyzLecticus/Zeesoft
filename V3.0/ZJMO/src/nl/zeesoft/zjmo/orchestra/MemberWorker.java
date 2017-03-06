package nl.zeesoft.zjmo.orchestra;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControl;

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
		//if (control && member.getPosition().getName().equals(Orchestra.CONDUCTOR)) {
		//	System.out.println(member.getPosition().getName() + " accepted control from " + socket.getSocketIpAddressAndPort());
		//}
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
		//if (control && member.getPosition().getName().equals(Orchestra.CONDUCTOR)) {
		//	System.out.println("Closing socket: " + socket.getSocketIpAddressAndPort() + " ...");
		//}
		socket.close();
		//if (control && member.getPosition().getName().equals(Orchestra.CONDUCTOR)) {
		//	System.out.println("Stopping member worker ...");
		//}
		super.stop();
		//waitForStop(1,false);
		//if (control && member.getPosition().getName().equals(Orchestra.CONDUCTOR)) {
		//	System.out.println("Stopped member worker");
		//}
	}
	
	@Override
	public void whileWorking() {
		boolean close = false;
		boolean stop = false;
		ZStringBuilder input = socket.readInput();
		//System.out.println(this + ": Input: " + input);
		ZStringBuilder output = null;
		if (!socket.isOpen()) {
			close = true;
		} else {
			lockMe(this);
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
		}
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
