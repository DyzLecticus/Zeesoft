package nl.zeesoft.zids.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zids.resource.IndexHtml;

public class IndexHandler extends HandlerObject {
	public IndexHandler(Messenger msgr) {
		super(msgr,"/index.html");
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(getCachedResponse());
		} catch (IOException e) {
			getMessenger().error(this,"I/O exception",e);
		}
	}

	@Override
	protected ZStringBuilder buildResponse() {
		IndexHtml index = new IndexHtml();
		return index.toStringBuilder();
	}
}
