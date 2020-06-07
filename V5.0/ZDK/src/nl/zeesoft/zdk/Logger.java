package nl.zeesoft.zdk;

public class Logger {
	private boolean		debug = false;
	
	public Logger() {
		
	}
	
	public Logger(boolean debug) {
		this.debug = debug;
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	public void debug(Object source, Str message) {
		if (debug) {
			prependSource(source,message);
			System.out.println(message.sb());
		}
	}
	
	public void error(Object source, Str message) {
		error(source,message,null);
	}
	
	public void error(Object source, Str message, Exception ex) {
		prependSource(source,message);
		System.err.println(message.sb());
		if (ex!=null) {
			ex.printStackTrace();
		}
	}
	
	private void prependSource(Object source, Str message) {
		message.sb().insert(0, ": ");
		message.sb().insert(0, source.getClass().getName());
		message.sb().insert(0, " ");
		message.sb().insert(0, TimeStamp.getDateTimeString());
	}
}
