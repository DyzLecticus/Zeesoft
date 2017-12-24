package nl.zeesoft.zids.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zids.dialog.Dialogs;

public class DialogHandler extends HandlerObject {
	private Dialogs 		dialogs		= null;
	private ZStringBuilder	json		= null;
	
	public DialogHandler(Messenger msgr,Dialogs d) {
		super(msgr,"/dialogs.json");
		this.dialogs = d;
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		try {
			PrintWriter out = response.getWriter();
			out.print(getJson());
		} catch (IOException e) {
			getMessenger().error(this,"I/O exception",e);
		}
	}
	
	private ZStringBuilder getJson() {
		if (json==null) {
			json = dialogs.getDialogsJson().toStringBuilderReadFormat();
		}
		return json;
	}
}
