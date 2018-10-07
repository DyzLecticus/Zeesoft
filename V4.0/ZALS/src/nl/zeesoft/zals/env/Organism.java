package nl.zeesoft.zals.env;

import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public abstract class Organism implements JsAble {
	public String	name			= "";
	public int		posX			= -1;
	public int		posY			= -1;
	public int		energy			= 0;
	public long		dateTimeDied	= 0;

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("name",name,true));
		json.rootElement.children.add(new JsElem("posX","" + posX));
		json.rootElement.children.add(new JsElem("posY","" + posY));
		json.rootElement.children.add(new JsElem("energy","" + energy));
		json.rootElement.children.add(new JsElem("dateTimeDied","" + dateTimeDied));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			name = json.rootElement.getChildString("name",name);
			posX = json.rootElement.getChildInt("posX",posX);
			posY = json.rootElement.getChildInt("posY",posY);
			energy = json.rootElement.getChildInt("energy",energy);
			dateTimeDied = json.rootElement.getChildLong("dateTimeDied",dateTimeDied);
		}
	}
}
