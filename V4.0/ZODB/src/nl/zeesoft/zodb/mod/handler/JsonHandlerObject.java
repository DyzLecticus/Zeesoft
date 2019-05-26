package nl.zeesoft.zodb.mod.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;

public abstract class JsonHandlerObject extends HandlerObject {
	public JsonHandlerObject(Config config, ModObject mod, String path) {
		super(config, mod, path);
		setContentType("application/json");
	}
	
	protected JsFile getPostBodyJson(HttpServletRequest request,HttpServletResponse response) {
		JsFile json = new JsFile();
		
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			getConfiguration().getMessenger().error(this,"Unsupported encoding",e);
		}
	
		ZStringBuilder js = new ZStringBuilder();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				js.append(line);
			}
		} catch (IOException e) {
			getConfiguration().getMessenger().error(this,"I/O exception while reading JSON POST request",e);
		}
		if (js.length()>0) {
			try {
				json.fromStringBuilder(js);
			} catch(Exception e) {
				getConfiguration().getMessenger().error(this,"Exception while parsing JSON POST request",e);
				json.rootElement = null;
			}
		}
		
		return json;
	}
	
	protected ZStringBuilder stringifyJson(JsFile json) {
		ZStringBuilder r = null;
		if (getConfiguration().isDebug()) {
			r = json.toStringBuilderReadFormat();
		} else {
			r = json.toStringBuilder();
		}
		return r;
	}
}
