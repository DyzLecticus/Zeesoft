package nl.zeesoft.zdk.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.str.StrUtil;

public class HttpConnectionWriter {
	protected Socket			socket		= null;
	protected PrintWriter		writer		= null;
	
	public HttpConnectionWriter(Socket socket, PrintWriter writer) {
		this.socket = socket;
		this.writer = writer;
	}
	
	public void writeHead(StringBuilder str) {
		while (!StrUtil.endsWith(str, "\r\n\r\n")) {
			str.append("\r\n");
		}
		writer.print(str);
		writer.flush();
	}

	public void writeBody(byte[] body, boolean mockException) {
		try {
			if (mockException) {
				throw new IOException();
			}
			socket.getOutputStream().write(body);
		} catch (IOException ex) {
			Logger.error(this, "IO exception",ex);
		}
	}
	
	protected void destroy() {
		writer.flush();
		writer.close();
		writer = null;
	}
}
