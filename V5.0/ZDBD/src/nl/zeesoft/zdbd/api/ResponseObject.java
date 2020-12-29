package nl.zeesoft.zdbd.api;

import nl.zeesoft.zdk.Str;

public abstract class ResponseObject {
	public abstract Str render();
	
	public void append(Str str, String line) {
		if (str.length()>0) {
			str.sb().append("\n");
		}
		str.sb().append(line);
	}
}
