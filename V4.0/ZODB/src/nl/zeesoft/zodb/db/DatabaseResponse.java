package nl.zeesoft.zodb.db;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class DatabaseResponse implements JsAble {
	public DatabaseRequest			request			= null;
	
	public int						statusCode		= 200;
	public List<ZStringBuilder>		errors			= new ArrayList<ZStringBuilder>();
	public List<DatabaseResult>		results			= new ArrayList<DatabaseResult>();
	public int						size			= 0;
	
	public void decodeObjects(StringBuilder key) {
		if (request.encoding.equals(DatabaseRequest.ENC_KEY)) {
			for (DatabaseResult result: results) {
				result.decodeObject(key);
			}
		} else if (request.encoding.equals(DatabaseRequest.ENC_ASCII)) {
			for (DatabaseResult result: results) {
				result.decodeObject();
			}
		}
	}
	
	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("statusCode","" + statusCode));
		if (errors.size()>0) {
			JsElem errsElem = new JsElem("errors",true);
			json.rootElement.children.add(errsElem);
			for (ZStringBuilder err: errors) {
				errsElem.children.add(new JsElem(null,err,true));
			}
		}
		if (results.size()>0) {
			JsElem ressElem = new JsElem("results",true);
			json.rootElement.children.add(ressElem);
			for (DatabaseResult result: results) {
				JsFile resJson = result.toJson();
				ressElem.children.add(resJson.rootElement);
			}
		}
		if (size>0) {
			json.rootElement.children.add(new JsElem("size","" + size));
		}
		return json;
	}
	
	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			statusCode = json.rootElement.getChildInt("statusCode",statusCode);
			JsElem errsElem = json.rootElement.getChildByName("errors");
			if (errsElem!=null && errsElem.children.size()>0) {
				for (JsElem errElem: errsElem.children) {
					if (errElem.value!=null && errElem.value.length()>0) {
						errors.add(errElem.value);
					}
				}
			}
			JsElem ressElem = json.rootElement.getChildByName("results");
			if (ressElem!=null && ressElem.children.size()>0) {
				for (JsElem resElem: ressElem.children) {
					JsFile resJson = new JsFile();
					resJson.rootElement = resElem;
					DatabaseResult res = new DatabaseResult();
					res.fromJson(resJson);
					results.add(res);
				}
			}
			size = json.rootElement.getChildInt("size",size);
		}
	}
	
	public void resultsFromElements(List<IndexElement> elements) {
		for (IndexElement element: elements) {
			results.add(new DatabaseResult(element));
		}
	}
}
