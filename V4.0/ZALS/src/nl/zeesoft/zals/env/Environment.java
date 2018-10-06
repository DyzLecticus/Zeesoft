package nl.zeesoft.zals.env;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zals.mod.ModZALS;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.db.InitializerDatabaseObject;

public class Environment implements InitializerDatabaseObject {
	public static final int				SIZE_X					= 40;
	public static final int				SIZE_Y					= 40;
	public static final int				PLANTS					= 4;
	public static final long			MINIMUM_TRAINING_STATES	= 5000;

	public static final String			COLOR_NOTHING			= "black";
	public static final String			COLOR_WALL				= "white";
	public static final String			COLOR_PLANT				= "green";
	public static final String			COLOR_HERBIVORE			= "blue";
	public static final String			COLOR_CARNIVORE			= "red";
	public static final String			COLOR_DEAD_OBJECT		= "purple";

	public static final String			INTENSITY_NOTHING		= "nothing";
	public static final String			INTENSITY_FAINT			= "faint";
	public static final String			INTENSITY_WEAK			= "weak";
	public static final String			INTENSITY_STRONG		= "strong";
	public static final String			INTENSITY_BRIGHT		= "bright";

	public static final String[]		INTENSITIES				= {INTENSITY_BRIGHT,INTENSITY_STRONG,INTENSITY_WEAK,INTENSITY_FAINT,INTENSITY_NOTHING};
	
	public List<Plant>					plants					= new ArrayList<Plant>();
	public List<Animal>					animals					= new ArrayList<Animal>();
	
	private int							herbivores				= 8;
	private int							carnivores				= 2;

	private int							deathDurationSeconds	= 1;

	private int							energyActionLook		= 100;
	private int							energyActionMove		= 1000;
	private int							energyActionTurn		= 300;
	private int							energyActionBite		= 500;
	
	private int							energyInputPerSecond	= 500000;
	private int							maxEnergyPlant			= 500000;
 	private int							maxEnergyHerbivore		= 500000;
 	private int							maxEnergyHerbivoreBite	= 50000;
	private int							maxEnergyCarnivore		= 500000;
 	private int							maxEnergyCarnivoreBite	= 100000;

	private int							statesPerSecond			= 25;
	private int							keepStateHistorySeconds	= 10;
	
	@Override
	public String getObjectName() {
		return ModZALS.NAME + "/Environment";
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		
		json.rootElement.children.add(new JsElem("herbivores","" + herbivores));
		json.rootElement.children.add(new JsElem("carnivores","" + carnivores));

		json.rootElement.children.add(new JsElem("deathDurationSeconds","" + deathDurationSeconds));

		json.rootElement.children.add(new JsElem("energyActionLook","" + energyActionLook));
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
		
		JsElem pltsElem = new JsElem("plants",true);
		json.rootElement.children.add(pltsElem);
		for (Plant plt: plants) {
			JsFile pltJs = plt.toJson();
			pltsElem.children.add(pltJs.rootElement);
		}
		
		JsElem anisElem = new JsElem("animals",true);
		json.rootElement.children.add(anisElem);
		for (Animal ani: animals) {
			JsFile aniJs = ani.toJson();
			anisElem.children.add(aniJs.rootElement);
		}
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			plants.clear();
			animals.clear();
			
			herbivores = json.rootElement.getChildInt("herbivores",herbivores);
			carnivores = json.rootElement.getChildInt("carnivores",carnivores);

			deathDurationSeconds = json.rootElement.getChildInt("deathDurationSeconds",deathDurationSeconds);

			energyActionLook = json.rootElement.getChildInt("energyActionLook",energyActionLook);
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
			
			JsElem pltsElem = json.rootElement.getChildByName("plants");
			for (JsElem pltElem: pltsElem.children) {
				Plant plt = new Plant();
				JsFile pltJs = new JsFile();
				pltJs.rootElement = pltElem;
				plt.fromJson(pltJs);
				plants.add(plt);
			}
			
			JsElem anisElem = json.rootElement.getChildByName("animals");
			for (JsElem aniElem: anisElem.children) {
				Animal ani = new Animal();
				JsFile aniJs = new JsFile();
				aniJs.rootElement = aniElem;
				ani.fromJson(aniJs);
				animals.add(ani);
			}
		}
	}
}
