package nl.zeesoft.zids.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.messenger.Messenger;

public class IndexHandler extends HandlerObject {
	
	public IndexHandler(Messenger msgr) {
		super(msgr,"/index.html");
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		/*
		response.setContentType("application/json");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(getJson());
		} catch (IOException e) {
			getMessenger().error(this,"I/O exception",e);
		}
		*/
	}
}
