package nl.zeesoft.zodb.db;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class WhiteList implements JsAble{
	private List<String> list = new ArrayList<String>();
	
	public List<String> getList() {
		return list;
	}
	
	public boolean isAllowed(String addr) {
		return (list.size()==0 || list.indexOf(addr)>=0);
	}
	
	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem("whiteList",true);
		for (String w: list) {
			json.rootElement.children.add(new JsElem(null,w,true));
		}
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		list.clear();
		JsElem wElem = json.rootElement.getChildByName("whiteList");
		if (wElem!=null && wElem.array) {
			for (JsElem w: wElem.children) {
				if (w.value!=null && w.value.length()>0) {
					list.add(w.value.toString());
				}
			}
		}
	}
	
}
