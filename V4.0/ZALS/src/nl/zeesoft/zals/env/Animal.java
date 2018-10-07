package nl.zeesoft.zals.env;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class Animal extends Organism {
	public static final String	HERBIVORE		= "HERBIVORE";
	public static final String	CARNIVORE		= "CARNIVORE";
	
	public String				type			=  "";
	public int					rotation		= 0;
	public int					score			= 0;
	public int					topScore		= 0;
	public long					trainedStates	= 0;
	public long					generation		= 0;
	
	public boolean isHerbivore() {
		return type.equals(HERBIVORE);
	}
	
	public boolean isCarnivore() {
		return type.equals(CARNIVORE);
	}
	public int getForwardPosX() {
		int r = posX;
		if (rotation==90) {
			r++;
		} else if (rotation==270) {
			r--;
		}
		return r;
	}
	
	public int getForwardPosY() {
		int r = posY;
		if (rotation==0) {
			r--;
		} else if (rotation==180) {
			r++;
		}
		return r;
	}

	public void incrementScore() {
		score++;
		if (score>topScore) {
			topScore = score;
		}
	}

	public void setRotation(int rotation) {
		rotation = rotation % 360;
		if (rotation % 90 != 0) {
			rotation = rotation - (rotation % 90); 
		}
		this.rotation = rotation;
	}
	
	@Override
	public JsFile toJson() {
		JsFile json = super.toJson();
		json.rootElement.children.add(new JsElem("type",type,true));
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
			type = json.rootElement.getChildString("type",type);
			rotation = json.rootElement.getChildInt("rotation",rotation);
			score = json.rootElement.getChildInt("score",score);
			topScore = json.rootElement.getChildInt("topScore",topScore);
			trainedStates = json.rootElement.getChildLong("trainedStates",trainedStates);
			generation = json.rootElement.getChildLong("generation",generation);
		}
	}
	
}
