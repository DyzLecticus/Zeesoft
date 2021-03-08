package nl.zeesoft.zdk;

public class Console {
	public void logObject(Object obj) {
		log(obj);
	}
	public void errObject(Object obj, Exception ex) {
		err(obj, ex);
	}
	public static void log(Object obj) {
		System.out.println(obj);
	}
	public static void err(Object obj) {
		err(obj,null);
	}
	public static void err(Object obj, Exception ex) {
		System.err.println(obj);
		if (ex!=null) {
			ex.printStackTrace();
		}
	}
}
