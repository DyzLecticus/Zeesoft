package nl.zeesoft.zdk;

public class Logger {
	public static Logger	logger		= null;
	
	private boolean			debug		= false;
	
	public Logger() {
		
	}
	
	public Logger(boolean debug) {
		this.debug = debug;
	}
	
	public void setDebug(boolean debug) {
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
	
	public static void initializeLogger() {
		initializeLogger("", false);
	}
	
	public static void initializeLogger(String className, boolean debug) {
		if (className.length()==0) {
			className = Logger.class.getName();
		}
		logger = (Logger) Instantiator.getNewClassInstanceForName(className);
		logger.setDebug(debug);
	}
	
	public static void setLoggerDebug(boolean debug) {
		getLogger().setDebug(debug);
	}
	
	public static void dbg(Object source, Str message) {
		getLogger().debug(source, message);
	}
	
	public static void err(Object source, Str message) {
		getLogger().error(source, message);
	}
	
	public static void err(Object source, Str message, Exception ex) {
		getLogger().error(source, message, ex);
	}
	
	private static Logger getLogger() {
		if (logger==null) {
			initializeLogger();
		}
		return logger;
	}
}
