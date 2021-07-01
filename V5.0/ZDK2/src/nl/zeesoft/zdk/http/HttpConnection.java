package nl.zeesoft.zdk.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.str.StrUtil;

public abstract class HttpConnection {
	protected boolean				open	= false;
	protected Socket				socket	= null;
	protected BufferedReader 		reader	= null;
	protected HttpConnectionWriter	writer	= null;
	
	protected synchronized boolean open(Socket	socket) {
		this.socket = socket;
		boolean r = createIO(false);
		open = r;
		return r;
	}
	
	protected synchronized boolean close() {
		boolean r = destroyIO();
		open = !r;
		return r;
	}
	
	protected boolean createIO(boolean mockException) {
		boolean r = false;
		try {
			if (mockException) {
				throw new IOException();
			}
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new HttpConnectionWriter(socket, new PrintWriter(socket.getOutputStream(), true));
			r = true;
		} catch (IOException e) {
			Logger.error(this,"Failed to open connection",e);
		}
		return r;
	}
	
	protected boolean destroyIO() {
		boolean r = false;
		writer.destroy();
		r = destroyReaderAndSocket(false);
		return r;
	}
	
	protected boolean destroyReaderAndSocket(boolean mockException) {
		boolean r = false;
		try {
			if (mockException) {
				throw new IOException();
			}
			reader.close();
			reader = null;
			socket.close();
			socket = null;
			r = true;
		} catch (IOException e) {
			Logger.error(this,"Failed to close connection",e);
		}
		return r;
	}
	
	protected boolean isOpen() {
		return open;
	}
	
	protected StringBuilder readHead() {
		StringBuilder r = new StringBuilder();
		boolean done = false;
		while(!done) {
			done = readLine(r, false);
		}
		return r;
	}
	
	protected boolean readLine(StringBuilder input, boolean mockException) {
		boolean r = false;
		try {
			if (mockException) {
				throw new IOException();
			}
			r = readLineThrowException(input);
		} catch (IOException e) {
			if (isOpen()) {
				Logger.error(this, "IO exception", e);
			}
			r = true;
		}
		return r;
	}
	
	protected boolean readLineThrowException(StringBuilder input) throws IOException {
		boolean r = false;
		String line = reader.readLine();
		if (line==null || line.length()==0) {
			r = true;
		} else {
			input.append(line);
			input.append(StrUtil.CRLF);
		}
		return r;
	}
	
	protected byte[] readBody(int contentLength, boolean mockException) {
		byte[] bytes = new byte[contentLength];
		int c = 0;
		for (int i = 0; i < contentLength; i++) {
			try {
				if (mockException) {
					throw new IOException();
				}
				c = reader.read();
				bytes[i] = (byte) c;
			} catch (IOException e) {
				Logger.error(this, "IO exception", e);
			}
		}
		return bytes;
	}
}
