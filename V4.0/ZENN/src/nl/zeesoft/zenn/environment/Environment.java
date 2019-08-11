package nl.zeesoft.zenn.environment;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.db.init.Persistable;

public class Environment implements Persistable {
	public static final int				SIZE_X					= 40;
	public static final int				SIZE_Y					= 40;
	public static final int				PLANTS					= 4;

	public static final String			COLOR_NOTHING			= "black";
	public static final String			COLOR_WALL				= "white";
	public static final String			COLOR_PLANT				= "green";
	public static final String			COLOR_HERBIVORE			= "blue";
	public static final String			COLOR_CARNIVORE			= "red";
	public static final String			COLOR_DEAD_OBJECT		= "purple";
	
	public List<Organism>				organisms				= new ArrayList<Organism>();

	public int							herbivores				= 8;
	public int							carnivores				= 2;

	public int							deathDurationSeconds	= 1;

	public int							energyActionLook		= 100;
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

	public List<Plant> getPlants() {
		List<Plant> r = new ArrayList<Plant>();
		for (Organism organism: organisms) {
			if (organism instanceof Plant) {
				r.add((Plant)organism);
			}
		}
		return r;
	}
	
	public List<Animal> getAnimals() {
		List<Animal> r = new ArrayList<Animal>();
		for (Organism organism: organisms) {
			if (organism instanceof Animal) {
				r.add((Animal)organism);
			}
		}
		return r;
	}
	
	public Organism getOrganismByPos(int posX, int postY) {
		Organism r = null;
		for (Organism organism: organisms) {
			if (organism.posX == posX && organism.posY == postY) {
				r = organism;
				break;
			}
		}
		return r;
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
		
		if (organisms.size()>0) {
			JsElem orgsElem = new JsElem("organisms",true);
			json.rootElement.children.add(orgsElem);
			for (Organism organism: organisms) {
				JsFile orgJs = organism.toJson();
				orgsElem.children.add(orgJs.rootElement);
			}
		}
		return json;
	}
	
	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			organisms.clear();
			
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
			
			JsElem orgsElem = json.rootElement.getChildByName("organisms");
			if (orgsElem!=null) {
				for (JsElem orgElem: orgsElem.children) {
					JsElem herbElem = orgElem.getChildByName("herbivore");
					JsFile js = new JsFile();
					js.rootElement = orgElem;
					if (herbElem!=null) {
						Animal ani = new Animal();
						ani.fromJson(json);
						organisms.add(ani);
					} else {
						Plant plt = new Plant();
						plt.fromJson(js);
						organisms.add(plt);
					}
				}
			}
		}
	}

	@Override
	public ZStringBuilder getObjectName() {
		return new ZStringBuilder("State");
	}
}
