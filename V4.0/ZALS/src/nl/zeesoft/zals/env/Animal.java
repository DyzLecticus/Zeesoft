package nl.zeesoft.zals.env;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class Animal extends Organism {
	public static final String	HERBIVORE		= "HERBIVORE";
	public static final String	CARNIVORE		= "CARNIVORE";
	
	private int					rotation		= 0;
	private int					score			= 0;
	private int					topScore		= 0;
	private long				trainedStates	= 0;
	private long				generation		= 0;
	
	@Override
	public JsFile toJson() {
		JsFile json = super.toJson();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("rotation","" + rotation));
		json.rootElement.children.add(new JsElem("score","" + score));
		json.rootElement.children.add(new JsElem("topScore","" + topScore));
		json.rootElement.children.add(new JsElem("trainedStates","" + trainedStates));
		json.rootElement.children.add(new JsElem("generation","" + generation));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		super.fromJson(json);
		if (json.rootElement!=null) {
			rotation = json.rootElement.getChildInt("rotation",rotation);
			score = json.rootElement.getChildInt("score",score);
			topScore = json.rootElement.getChildInt("topScore",topScore);
			trainedStates = json.rootElement.getChildLong("trainedStates",trainedStates);
			generation = json.rootElement.getChildLong("generation",generation);
		}
	}
	
}
