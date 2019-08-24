package nl.zeesoft.zenn.simulator;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.genetic.EvolverUnit;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.db.init.Persistable;

public class SimulatorAnimal implements Persistable {
	public String			name	= "";
	public EvolverUnit		unit	= null;
	
	public SimulatorAnimal copy() {
		SimulatorAnimal r = new SimulatorAnimal();
		r.name = name;
		r.unit = unit.copy();
		return r;
	}
	
	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("name",name,true));
		JsElem unitElem = new JsElem("unit",true);
		json.rootElement.children.add(unitElem);
		unitElem.children.add(unit.toJson().rootElement);
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			name = json.rootElement.getChildString("name",name);
			JsElem unitElem = json.rootElement.getChildByName("unit");
			if (unitElem!=null && unitElem.children.size()>0) {
				JsFile js = new JsFile();
				js.rootElement = unitElem.children.get(0);
				unit = new EvolverUnit();
				unit.fromJson(js);
			}
		}
	}

	@Override
	public ZStringBuilder getObjectName() {
		return new ZStringBuilder(name);
	}

}
