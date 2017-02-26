package nl.zeesoft.zjmo.orchestra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class MemberWorker extends Worker {
	private MemberObject		member		= null;
	private Socket 				socket		= null;
	private Protocol			protocol	= null;

	private PrintWriter			out			= null;
	private BufferedReader 		in			= null;

	public MemberWorker(MemberObject member,Socket socket, Protocol protocol) {
		super(null,null);
		this.member = member;
		this.socket = socket;
		this.protocol = protocol;
	}

	public MemberWorker(Messenger msgr, WorkerUnion union,MemberObject member,Socket socket, Protocol protocol) {
		super(msgr,union);
		this.member = member;
		this.socket = socket;
		this.protocol = protocol;
	}
	
	@Override
	public void start() {
		boolean start = false;
		lockMe(this);
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (out!=null && in!=null) {
			start = true;
		}
		unlockMe(this);
		if (start) {
			super.start();
		}
	}

	@Override
	public void stop() {
		lockMe(this);
		if (out!=null) {
			out.close();
		}
		if (in!=null) {
			try {
				in.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		if (socket!=null) {
			try {
				socket.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		unlockMe(this);
		waitForStop(1,false);
		super.stop();
	}
	
	@Override
	public void whileWorking() {
		boolean stop = false;
		lockMe(this);
		ZStringBuilder input = readInput();
		ZStringBuilder output = null;
		if (input==null) {
			stop = true;
		} else {
			protocol.setStop(false);
			output = protocol.handleInput(input);
			stop = output==null || protocol.isStop();
		}
		unlockMe(this);
		if (stop) {
			member.stopWorker(this);
		} else {
			lockMe(this);
			writeOutput(output);
			unlockMe(this);
		}
	}
	
	private ZStringBuilder readInput() {
		ZStringBuilder input = new ZStringBuilder();
		boolean error = false;
		boolean stop = false;
		while (!stop) {
			String line = null;
			try {
				line = in.readLine();
			} catch (IOException e) {
				error = true;
			}
			if (line!=null) {
				input.append(line);
				input.append("\n");
			} else {
				stop = true;
			}
		}
		if (error) {
			input = null;
		}
		return input;
	}
	
	private void writeOutput(ZStringBuilder output) {
		if (out!=null) {
			out.print(output.toString());
		}
	}
}
