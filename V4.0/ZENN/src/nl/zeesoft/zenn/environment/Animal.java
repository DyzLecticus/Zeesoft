package nl.zeesoft.zenn.environment;

import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class Animal extends Organism implements JsAble {
	public boolean				herbivore		= true;
	public int					rotation		= 0;
	public int					score			= 0;
	
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
		json.rootElement.children.add(new JsElem("herbivore","" + herbivore));
		json.rootElement.children.add(new JsElem("rotation","" + rotation));
		json.rootElement.children.add(new JsElem("score","" + score));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		super.fromJson(json);
		if (json.rootElement!=null) {
			herbivore = json.rootElement.getChildBoolean("herbivore",herbivore);
			rotation = json.rootElement.getChildInt("rotation",rotation);
			score = json.rootElement.getChildInt("score",score);
		}
	}
	

}
