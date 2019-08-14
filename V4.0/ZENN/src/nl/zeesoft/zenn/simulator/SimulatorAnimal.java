package nl.zeesoft.zenn.simulator;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.genetic.GeneticCode;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.neural.NeuralNet;
import nl.zeesoft.zodb.db.init.Persistable;

public class SimulatorAnimal implements Persistable {
	public ZStringBuilder	name		= new ZStringBuilder();
	public GeneticCode		code		= null;
	public NeuralNet		neuralNet	= null;
	
	@Override
	public JsFile toJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fromJson(JsFile json) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ZStringBuilder getObjectName() {
		return name;
	}

}
