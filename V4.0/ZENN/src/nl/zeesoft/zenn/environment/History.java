package nl.zeesoft.zenn.environment;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class History implements JsAble {
	public long				timeStamp		= 0;
	public ZStringBuilder	organismData	= new ZStringBuilder();

	public void addOrganismData(List<Organism> organisms) {
		for (Organism org: organisms) {
			Animal ani = null;
			String color = EnvironmentConfig.COLOR_PLANT;
			if (org instanceof Animal) {
				ani = (Animal) org;
				if (ani.herbivore) {
					color = EnvironmentConfig.COLOR_HERBIVORE;
				} else {
					color = EnvironmentConfig.COLOR_CARNIVORE;
				}
			}
			
			if (organismData.length()>0) {
				organismData.append("|");
			}
			organismData.append(color);
			organismData.append(";");
			organismData.append(org.name);
			organismData.append(";");
			organismData.append("" + org.posX);
			organismData.append(";");
			organismData.append("" + org.posY);
			organismData.append(";");
			organismData.append("" + org.energy);
			organismData.append(";");
			if (ani!=null) {
				organismData.append("" + ani.rotation);
			}
			organismData.append(";");
			if (ani!=null) {
				organismData.append("" + ani.score);
			}
		}
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("timeStamp","" + timeStamp));
		json.rootElement.children.add(new JsElem("organismData",organismData,true));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			timeStamp = json.rootElement.getChildLong("timeStamp",timeStamp);
			organismData = json.rootElement.getChildZStringBuilder("organismData",organismData);
		}
	}
}
