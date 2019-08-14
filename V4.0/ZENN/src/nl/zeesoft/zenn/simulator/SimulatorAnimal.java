package nl.zeesoft.zenn.simulator;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.genetic.GeneticCode;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.neural.NeuralNet;
import nl.zeesoft.zodb.db.init.Persistable;

public class SimulatorAnimal implements Persistable {
	public String			name		= "";
	public GeneticCode		code		= null;
	public NeuralNet		neuralNet	= null;
	
	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("name",name,true));
		json.rootElement.children.add(new JsElem("code",code.toCompressedCode(),true));
		JsElem netElem = new JsElem("neuralNet",true);
		json.rootElement.children.add(netElem);
		netElem.children.add(neuralNet.toJson().rootElement);
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			name = json.rootElement.getChildString("name",name);
			ZStringBuilder c = json.rootElement.getChildZStringBuilder("code");
			if (c!=null && c.length()>0) {
				code = new GeneticCode();
				code.fromCompressedCode(c);
			}
			JsElem netElem = json.rootElement.getChildByName("neuralNet");
			if (netElem!=null && netElem.children.size()>0) {
				JsFile js = new JsFile();
				js.rootElement = netElem.children.get(0);
				neuralNet = new NeuralNet(js);
			}
		}
	}

	@Override
	public ZStringBuilder getObjectName() {
		return new ZStringBuilder(name);
	}

}
