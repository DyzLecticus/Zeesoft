package nl.zeesoft.zids.server.requesthandler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventExceptionSubscriber;

public abstract class RhRequestHandlerObject extends Locker implements EvtEventExceptionSubscriber {
	public final static String 	GET				= "GET";
	public final static String 	POST			= "POST";
	
	private boolean 			done			= false;
	private String				title			= "";
	private String				backgroundColor	= "";

	@Override
	public void handleEventException(EvtEvent evt, Exception ex) {
		setDone(true);
	}

	@Override
	public void handleEvent(EvtEvent e) {
		// Override to implement 
	}
	
	public String getMethod() {
		return GET;
	}

	public String getPath() {
		return "";
	}

	public String getFile() {
		return "index.html";
	}
	
	public final String getPathAlias() {
		return getPath() + "/";
	}

	public final String getFileAlias() {
		return getPathAlias() + getFile();
	}

	public abstract void handleRequest(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException;

	private boolean isDone() {
		lockMe(this);
		boolean r = done;
		unlockMe(this);
		return r;
	}

	protected void setDone(boolean done) {
		lockMe(this);
		this.done = done;
		unlockMe(this);
	}

	protected long getSleep() {
		return 5;
	}
	
	protected void waitTillDone() {
		while(!isDone()) {
			whileWaiting();
			try {
				Thread.sleep(getSleep());
			} catch (InterruptedException e) {
				Messenger.getInstance().debug(this,"Request handler (" + this.getClass().getName() + ") was interrupted: " + e);
			}
		}
	}

	protected void whileWaiting() {
		// Override to implement
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the backgroundColor
	 */
	public String getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * @param backgroundColor the backgroundColor to set
	 */
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}
