package nl.zeesoft.zdbd.api.html;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class Bye extends ResponseObject {
	@Override
	public Str render() {
		Str r = new Str();
		append(r,"<div>");
		append(r,"<h1>Bye</h1>");
		append(r,"Hope you had fun");
		append(r,"</div>");
		return r;
	}
}
