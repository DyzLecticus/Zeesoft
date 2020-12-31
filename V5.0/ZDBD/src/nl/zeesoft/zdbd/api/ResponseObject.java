package nl.zeesoft.zdbd.api;

import nl.zeesoft.zdk.Str;

public abstract class ResponseObject {
	public abstract Str render();
	
	public static void append(Str str, String line) {
		append(str,new Str(line));
	}
	
	public static void append(Str str, Str line) {
		if (str.length()>0) {
			str.sb().append("\n");
		}
		str.sb().append(line.sb());
	}
}
