package nl.zeesoft.zodb.database.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Date;

import nl.zeesoft.zodb.Worker;

public class SvrSocketWorker extends Worker {
	private	SvrProtocolObject	protocol				= null;
	private Socket				socket					= null;

	private PrintWriter			out						= null;
	private BufferedReader 		in						= null;
	
	protected SvrSocketWorker(Socket socket, SvrProtocolObject protocol) {
		setSleep(0);
		this.socket = socket;
		this.protocol = protocol;
	}
	
	@Override
	public void start() {
		if (open()) {
			super.start();
		} else {
			lockMe(this);
			cleanUpProtocol();
			unlockMe(this);
		}
	}

	@Override
	public void stop() {
		super.stop();
		lockMe(this);
		close();
		cleanUpProtocol();
		unlockMe(this);
	}
	
	@Override
	public void whileWorking() {
		StringBuilder input = readInput();
		if (input.length()==0) {
			stop();
		} else {
			boolean stop = false;
			lockMe(this);
			if (protocol!=null && out!=null) {
				Object output = protocol.processIO(input);
				writeOutput(output);
				if (protocol.getSession()!=null) {			
					protocol.getSession().setLatestActivity(new Date());
				}
				String connection = protocol.getParameters().get("Connection");
				if ((connection!=null && connection.equals("close"))) {
					out.flush();
					stop = true;
				} else {
					out.flush();
					if (protocol.getImage()!=null) {
						writeOutput(protocol.getImage());
						protocol.setImage(null);
						out.flush();
					}
				}
			}
			unlockMe(this);
			if (stop) {
				stop();
			}
		}
	}

	protected Socket getSocket() {
		return socket;
	}
	
	/******************************** PRIVATE METHODS ********************************/ 
	private void cleanUpProtocol() {
		SvrProtocolObject ptc = protocol;
		if (ptc!=null) {
			ptc.getServer().removeSocketWorker(this);
			if (protocol!=null) {
				protocol.cleanUp();
				protocol = null;
			}
		}
	}
	
	private StringBuilder readInput() {
		StringBuilder input = new StringBuilder();
		String line = "";
		boolean isPost = false;
		int contentLength = 0;
		try {
			line = in.readLine();
			if (line!=null) {
				input.append("" + line);
				isPost = line.startsWith("POST");
				contentLength = 0;
				boolean stop = false;
				boolean gotNull = false;
				while (!stop) {
					line = in.readLine();
					if (line!=null) {
						if (line.length()>0) {
							input.append("\n");
							input.append(line);
							if (isPost) {
								if (line.startsWith("Content-Length: ")) {
									contentLength = Integer.parseInt(line.substring("Content-Length: ".length()));
								}
							}
						} else {
							stop = true;
						}
					} else if (gotNull) {
						stop = true;
					} else {
						gotNull = true;
					}
					if (!in.ready()) {
						stop = true;
					}
				}
				StringBuilder body = new StringBuilder();
				if (isPost) {
					int c = 0;
					for (int i = 0; i < contentLength; i++) {
						c = in.read();
						body.append((char) c);
					}
				}
				if (body.length()>0) {
					input.append("\n");
					input.append("\n");
					input.append(body.toString());
				}
			}
		} catch (SocketTimeoutException e) {
			// Ignore
		} catch (IOException e) {
			if (!socket.isClosed()) {
				SvrSocketLogger.getInstance().readException(socket,e);
			}
		}
		return input;
	}	

	private void writeOutput(Object output) {
		//Messenger.getInstance().debug(this,"Output: " + output);
		try {
			if (out!=null) {
				out.print(output);
			}
		} catch (Exception e) {
			if (!socket.isClosed()) {
				SvrSocketLogger.getInstance().writeException(socket,e);
			}
		}
	}

	private void writeOutput(byte[] image) {
		//Messenger.getInstance().debug(this,"Output image length: " + image.length);
		try {
			socket.getOutputStream().write(image);
		} catch (IOException e) {
			if (!socket.isClosed()) {
				SvrSocketLogger.getInstance().writeException(socket,e);
			}
		}
	}
	

	private boolean open() {
		boolean opened = false;
		if (socket!=null) {
			try {
				out = new PrintWriter(socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				opened = true;
			} catch (IOException e) {
				SvrSocketLogger.getInstance().openException(socket,e);
			}
		}
		return opened;
	}

	private boolean close() {
		boolean closed = false;
		try {
			if (out!=null) {
				out.flush();
				out.close();
				out = null;
			}
			if (in!=null) {
				in.close();
				in = null;
			}
			if (socket!=null) {
				socket.close();
			}
			closed = true;
		} catch (IOException e) {
			SvrSocketLogger.getInstance().closeException(socket,e);
		}
		return closed;
	}
}
