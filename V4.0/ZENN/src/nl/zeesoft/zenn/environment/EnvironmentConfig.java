package nl.zeesoft.zenn.environment;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.db.init.Persistable;

public class EnvironmentConfig implements Persistable {
	public static final int				SIZE_X					= 40;
	public static final int				SIZE_Y					= 40;
	public static final int				PLANTS					= 4;

	public static final String			COLOR_PLANT				= "green";
	public static final String			COLOR_HERBIVORE			= "blue";
	public static final String			COLOR_CARNIVORE			= "red";
	public static final String			COLOR_DEAD_OBJECT		= "purple";

	public static final String			ACTION_MOVE_FORWARD		= "moveForward";
	public static final String			ACTION_BITE				= "bite";
	public static final String			ACTION_TURN_LEFT		= "turnLeft";
	public static final String			ACTION_TURN_RIGHT		= "turnRight";

	public boolean						active					= true;
	
	public int							herbivores				= 8;
	public int							carnivores				= 2;

	public int							deathDurationSeconds	= 2;

	public int							energyActionLookFactor	= 2;
	public int							energyActionMove		= 1000;
	public int							energyActionTurn		= 300;
	public int							energyActionBite		= 500;
	
	public int							energyInputPerSecond	= 500000;
	public int							maxEnergyPlant			= 500000;
	public int							maxEnergyHerbivore		= 500000;
	public int							maxEnergyHerbivoreBite	= 50000;
 	public int							maxEnergyCarnivore		= 500000;
 	public int							maxEnergyCarnivoreBite	= 100000;

 	public int							statesPerSecond			= 25;
 	public int							keepStateHistorySeconds	= 10;
 	
	public float						randomToOne				= 0.10F;
 	public int							minTopScore				= 400;
	
	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();

		json.rootElement.children.add(new JsElem("active","" + active));

		json.rootElement.children.add(new JsElem("herbivores","" + herbivores));
		json.rootElement.children.add(new JsElem("carnivores","" + carnivores));

		json.rootElement.children.add(new JsElem("deathDurationSeconds","" + deathDurationSeconds));

		json.rootElement.children.add(new JsElem("energyActionLookFactor","" + energyActionLookFactor));
		json.rootElement.children.add(new JsElem("energyActionMove","" + energyActionMove));
		json.rootElement.children.add(new JsElem("energyActionTurn","" + energyActionTurn));
		json.rootElement.children.add(new JsElem("energyActionBite","" + energyActionBite));

		json.rootElement.children.add(new JsElem("energyInputPerSecond","" + energyInputPerSecond));
		json.rootElement.children.add(new JsElem("maxEnergyPlant","" + maxEnergyPlant));
		json.rootElement.children.add(new JsElem("maxEnergyHerbivore","" + maxEnergyHerbivore));
		json.rootElement.children.add(new JsElem("maxEnergyHerbivoreBite","" + maxEnergyHerbivoreBite));
		json.rootElement.children.add(new JsElem("maxEnergyCarnivore","" + maxEnergyCarnivore));
		json.rootElement.children.add(new JsElem("maxEnergyCarnivoreBite","" + maxEnergyCarnivoreBite));
		
		json.rootElement.children.add(new JsElem("statesPerSecond","" + statesPerSecond));
		json.rootElement.children.add(new JsElem("keepStateHistorySeconds","" + keepStateHistorySeconds));
		
		json.rootElement.children.add(new JsElem("randomToOne","" + randomToOne));
		json.rootElement.children.add(new JsElem("minTopScore","" + minTopScore));
		return json;
	}
	
	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			active = json.rootElement.getChildBoolean("active",active);
			
			herbivores = json.rootElement.getChildInt("herbivores",herbivores);
			carnivores = json.rootElement.getChildInt("carnivores",carnivores);

			deathDurationSeconds = json.rootElement.getChildInt("deathDurationSeconds",deathDurationSeconds);

			energyActionLookFactor = json.rootElement.getChildInt("energyActionLookFactor",energyActionLookFactor);
			energyActionMove = json.rootElement.getChildInt("energyActionMove",energyActionMove);
			energyActionTurn = json.rootElement.getChildInt("energyActionTurn",energyActionTurn);
			energyActionBite = json.rootElement.getChildInt("energyActionBite",energyActionBite);

			energyInputPerSecond = json.rootElement.getChildInt("energyInputPerSecond",energyInputPerSecond);
			maxEnergyPlant = json.rootElement.getChildInt("maxEnergyPlant",maxEnergyPlant);
			maxEnergyHerbivore = json.rootElement.getChildInt("maxEnergyHerbivore",maxEnergyHerbivore);
			maxEnergyHerbivoreBite = json.rootElement.getChildInt("maxEnergyHerbivoreBite",maxEnergyHerbivoreBite);
			maxEnergyCarnivore = json.rootElement.getChildInt("maxEnergyCarnivore",maxEnergyCarnivore);
			maxEnergyCarnivoreBite = json.rootElement.getChildInt("maxEnergyCarnivoreBite",maxEnergyCarnivoreBite);

			statesPerSecond = json.rootElement.getChildInt("statesPerSecond",statesPerSecond);
			keepStateHistorySeconds = json.rootElement.getChildInt("keepStateHistorySeconds",keepStateHistorySeconds);
			
			randomToOne = json.rootElement.getChildFloat("randomToOne",randomToOne);
			minTopScore = json.rootElement.getChildInt("minTopScore",minTopScore);
		}
	}

	@Override
	public ZStringBuilder getObjectName() {
		return new ZStringBuilder("Config");
	}
}
