package nl.zeesoft.zids.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zids.resource.ZIDSStyleSheet;

public class ZIDSStyleSheetHandler extends HandlerObject {
	public ZIDSStyleSheetHandler(Messenger msgr) {
		super(msgr,"/ZIDS.css");
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/css");
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
		ZIDSStyleSheet ZIDS = new ZIDSStyleSheet();
		return ZIDS.toStringBuilder();
	}
}
