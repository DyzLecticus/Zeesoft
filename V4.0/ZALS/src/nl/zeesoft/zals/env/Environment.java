package nl.zeesoft.zals.env;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZIntegerGenerator;
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
	public List<History>				histories				= new ArrayList<History>();
	
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
	
	@Override
	public String getObjectName() {
		return "State";
	}

	public void initialize() {
		plants.clear();
		animals.clear();
		histories.clear();
		
		for (int i = 0; i < PLANTS; i++) {
			Plant plt = new Plant();
			plt.name = "Plant" + (i + 1);
			plants.add(plt);
		}
		for (int i = 0; i < herbivores; i++) {
			Animal ani = new Animal();
			ani.name = "Herbivore" + String.format("%02d",(i + 1)) ;
			ani.type = Animal.HERBIVORE;
			animals.add(ani);
		}
		for (int i = 0; i < carnivores; i++) {
			Animal ani = new Animal();
			ani.name = "Carnivore" + String.format("%02d",(i + 1)) ;
			ani.type = Animal.CARNIVORE;
			animals.add(ani);
		}
		
		for (Plant plt: plants) {
			repositionPlant(plt);
		}
		for (Animal ani: animals) {
			initializeAnimal(ani);
		}
	}
	
	public void destroy() {
		plants.clear();
		animals.clear();
		histories.clear();
	}
	
	public List<Organism> getOrganisms() {
		List<Organism> r = new ArrayList<Organism>();
		for (Plant plt: plants) {
			r.add(plt);
		}
		for (Animal ani: animals) {
			r.add(ani);
		}
		return r;
	}
	
	public void repositionPlant(Plant plt) {
		int minX = 0;
		int maxX = 0;
		int minY = 0;
		int maxY = 0;
		int index = plants.indexOf(plt);
		if (index==0) {
			minX=0;
			maxX=10;
			minY=0;
			maxY=10;
		} else if (index==1) {
			minX=(SIZE_X - 11);
			maxX=(SIZE_X - 1);
			minY=0;
			maxY=10;
		} else if (index==2) {
			minX=0;
			maxX=10;
			minY=(SIZE_Y - 11);
			maxY=(SIZE_Y - 1);
		} else if (index==3) {
			minX=(SIZE_X - 11);
			maxX=(SIZE_X - 1);
			minY=(SIZE_Y - 11);
			maxY=(SIZE_Y - 1);
		}
		repositionOrganism(plt,minX,maxX,minY,maxY);
	}
	
	public void initializeAnimal(Animal animal) {
		animal.score = 0;
		animal.generation++;
		if (animal.isHerbivore()) {
			animal.energy = maxEnergyHerbivore / 2;
		} else if (animal.isHerbivore()) {
			animal.energy = maxEnergyCarnivore / 2;
		}
		repositionOrganism(animal,0,SIZE_X - 1,0,SIZE_Y - 1);
		ZIntegerGenerator gen = new ZIntegerGenerator(0,359);
		animal.setRotation(gen.getNewInteger());
	}
	
	public void repositionOrganism(Organism o,int minX,int maxX,int minY,int maxY) {
		ZIntegerGenerator xGen = new ZIntegerGenerator(minX,maxX);
		ZIntegerGenerator yGen = new ZIntegerGenerator(minY,maxY);
		int x = xGen.getNewInteger();
		int y = yGen.getNewInteger();
		List<Organism> orgs = getOrganisms();
		boolean free = false;
		while (!free) {
			boolean found = false;
			for (Organism org: orgs) {
				if (org.posX==x && org.posY==y) {
					found = true;
					break;
				}
			}
			if (!found) {
				free = true;
			} else {
				x = xGen.getNewInteger();
				y = yGen.getNewInteger();
			}
		}
		o.posX = x;
		o.posY = y;
	}
	
	public void updatePlants() {
		List<Plant> livingPlants = getLivingPlants();
		int div = livingPlants.size();
		for (Plant plant: livingPlants) {
			if (plant.energy==maxEnergyPlant) {
				div--;
			}
		}
		if (div>0) {
			for (Plant plt: livingPlants) {
				if (plt.energy<maxEnergyPlant) {
					if (plt.energy==0) {
						repositionPlant(plt);
					}
					plt.energy += (energyInputPerSecond / div);
					if (plt.energy > maxEnergyPlant) {
						plt.energy = maxEnergyPlant;
					}
				}
			}
		}
	}

	public List<Plant> getLivingPlants() {
		List<Plant> r = new ArrayList<Plant>();
		Date now = new Date();
		for (Plant plt: plants) {
			if (plt.dateTimeDied < (now.getTime() - (deathDurationSeconds * 1000))) {
				r.add(plt);
			}
		}
		return r;
	}
	
	public void addHistory() {
		History his = new History();
		his.timeStamp = (new Date()).getTime();
		his.addOrganismData(getOrganisms());
		histories.add(his);
		int max = (keepStateHistorySeconds * statesPerSecond);
		if (histories.size()>max) {
			int remove = (histories.size() - max);
			for (int i = 0; i < remove; i++) {
				histories.remove(0);
			}
		}
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
		
		JsElem histsElem = new JsElem("histories",true);
		json.rootElement.children.add(histsElem);
		for (History his: histories) {
			JsFile hisJs = his.toJson();
			histsElem.children.add(hisJs.rootElement);
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
			
			JsElem histsElem = json.rootElement.getChildByName("histories");
			for (JsElem histElem: histsElem.children) {
				History his = new History();
				JsFile hisJs = new JsFile();
				hisJs.rootElement = histElem;
				his.fromJson(hisJs);
				histories.add(his);
			}
		}
	}
}
