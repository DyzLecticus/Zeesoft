package nl.zeesoft.zdk;

public class Logger {
	private static Logger	logger		= null;
	
	private Lock			lock		= new Lock(this);
	private Console			console		= new Console();
	private boolean			debug		= false;
	
	private Logger() {
		// Singleton
	}
	
	private static synchronized Logger getInstance() {
		if (logger==null) {
			logger = new Logger();
		}
		return logger;
	}
	
	public static void setLoggerDebug(boolean debug) {
		getInstance().setDebug(debug);
	}
	
	public static boolean isLoggerDebug() {
		return getInstance().isDebug();
	}
	
	public static void setLoggerConsole(Console console) {
		getInstance().setConsole(console);
	}
	
	public static void debug(Object source, Object message) {
		if (getInstance().isDebug()) {
			getInstance().getConsole().logObject(prependSource(source,message));
		}
	}
	
	public static void error(Object source, Object message) {
		error(source,message,null);
	}
	
	public static void error(Object source, Object message, Exception ex) {
		getInstance().getConsole().errObject(prependSource(source,message),ex);
	}
	
	private boolean isDebug() {
		lock.lock();
		boolean r = debug;
		lock.unlock();
		return r;
	}
	
	private void setDebug(boolean debug) {
		lock.lock();
		this.debug = debug;
		lock.unlock();
	}
	
	private Console getConsole() {
		lock.lock();
		Console r = console;
		lock.unlock();
		return r;
	}
	
	private void setConsole(Console console) {
		lock.lock();
		this.console = console;
		lock.unlock();
	}
	
	private static String prependSource(Object source, Object message) {
		return source.getClass().getName() + ": " + message;
	}
}
