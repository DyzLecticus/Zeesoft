package nl.zeesoft.zjmo.orchestra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import nl.zeesoft.zdk.ZStringBuilder;

/**
 * Handles socket input and output. 
 */
public class SocketHandler {
	private static final String END_OF_PROTOCOL_OUTPUT	= "END_OF_PROTOCOL_OUTPUT";
	private Socket 				socket					= null;
	private PrintWriter			out						= null;
	private BufferedReader 		in						= null;

	protected void setSocket(Socket s) {
		socket = s;
	}

	protected String getSocketIpAddress() {
		String r = "";
		if (socket!=null && socket.getInetAddress()!=null) {
			r = socket.getInetAddress().getHostAddress();
		}
		return r;
	}

	protected int getSocketPort() {
		int r = 0;
		if (socket!=null && socket.getInetAddress()!=null) {
			r = socket.getPort();
		}
		return r;
	}

	protected String getSocketIpAddressAndPort() {
		String r = "";
		if (socket!=null && socket.getInetAddress()!=null) {
			r = getSocketIpAddress() + ":" + getSocketPort();
		}
		return r;
	}

	protected boolean isOpen() {
		boolean r = false;
		if (socket!=null && !socket.isClosed() && !socket.isInputShutdown() && !socket.isOutputShutdown() && socket.isConnected()) {
			r = true;
		}
		return r;
	}

	protected boolean open(String ipAddressOrHostName, int port,boolean timeOut) {
		try {
			socket = new Socket(ipAddressOrHostName,port);
		} catch (UnknownHostException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		if (socket!=null && timeOut) {
			try {
				socket.setSoTimeout(1000);
			} catch (SocketException e) {
				//e.printStackTrace();
			}
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
		if (socket!=null) {
			try {
				socket.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
			socket = null;
		}
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
	}
	
	protected ZStringBuilder readInput() {
		ZStringBuilder input = new ZStringBuilder();
		boolean error = false;
		boolean stop = false;
		BufferedReader r = in;
		while (!stop && in!=null) {
			String line = null;
			boolean timeout = false;
			try {
				line = r.readLine();
			} catch (SocketTimeoutException e) {
				timeout = true;
			} catch (IOException e) {
				error = true;
			}
			if (!timeout) {
				if (!error && line!=null && !line.equals(END_OF_PROTOCOL_OUTPUT)) {
					if (input.length()>0) {
						input.append("\n");
					}
					input.append(line);
				} else {
					stop = true;
				}
			}
		}
		if (error) {
			input = null;
		}
		return input;
	}
	
	protected void writeOutput(ZStringBuilder output) {
		ZStringBuilder put = new ZStringBuilder(output);
		put.append("\n");
		put.append(END_OF_PROTOCOL_OUTPUT);
		put.append("\n");
		PrintWriter w = out;
		if (w!=null) {
			w.print(put);
			w.flush();
		}
	}
}
