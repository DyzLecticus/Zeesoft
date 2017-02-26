package nl.zeesoft.zjmo.orchestra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import nl.zeesoft.zdk.ZStringBuilder;

public class SocketHandler {
	private static final String END_OF_PROTOCOL_OUTPUT	= "END_OF_PROTOCOL_OUTPUT";
	private Socket 				socket					= null;
	private PrintWriter			out						= null;
	private BufferedReader 		in						= null;

	protected void setSocket(Socket s) {
		socket = s;
	}

	protected boolean open(String ipAddressOrHostName, int port,boolean timeOut) {
		try {
			socket = new Socket(ipAddressOrHostName,port);
			if (timeOut) {
				socket.setSoTimeout(1000);
			}
		} catch (SocketException e) {
			//e.printStackTrace();
		} catch (UnknownHostException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return open();
	}

	protected boolean open() {
		boolean open = false;
		if (socket!=null) {
			try {
				out = new PrintWriter(socket.getOutputStream(), true);
			} catch (IOException e) {
				//e.printStackTrace();
			}
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				//e.printStackTrace();
			}
			if (out!=null && in!=null) {
				open = true;
			}
		}
		if (!open) {
			close();
		}
		return open;
	}
	
	protected void close() {
		if (out!=null) {
			out.close();
			out = null;
		}
		if (in!=null) {
			try {
				in.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
			in = null;
		}
		if (socket!=null) {
			try {
				socket.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
			socket = null;
		}
	}
	
	protected ZStringBuilder readInput() {
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
			if (!error && line!=null && !line.equals(END_OF_PROTOCOL_OUTPUT)) {
				if (input.length()>0) {
					input.append("\n");
				}
				input.append(line);
			} else {
				stop = true;
			}
		}
		if (error) {
			input = null;
		}
		return input;
	}
	
	protected void writeOutput(ZStringBuilder output) {
		if (out!=null) {
			out.print(output.toString());
			out.print("\n");
			out.print(END_OF_PROTOCOL_OUTPUT);
			out.print("\n");
			out.flush();
		}
	}
}
