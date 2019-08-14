package nl.zeesoft.zenn.environment;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.functions.ZRandomize;
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

	public void initialize() {
		organisms.clear();
		
		for (int i = 0; i < PLANTS; i++) {
			Plant plt = new Plant();
			plt.name = "Plant" + (i + 1);
			organisms.add(plt);
		}
		for (int i = 0; i < herbivores; i++) {
			Animal ani = new Animal();
			ani.name = "Herbivore" + String.format("%02d",(i + 1)) ;
			ani.herbivore = true;
			organisms.add(ani);
		}
		for (int i = 0; i < carnivores; i++) {
			Animal ani = new Animal();
			ani.name = "Carnivore" + String.format("%02d",(i + 1)) ;
			ani.herbivore = false;
			organisms.add(ani);
		}
		
		for (Organism organism: organisms) {
			if (organism instanceof Plant) {
				repositionPlant((Plant)organism);
			} else if (organism instanceof Animal) {
				initializeAnimal((Animal)organism);
			}
		}
	}
	
	public boolean prepareForStart() {
 		boolean r = false;
 		List<Plant> plants = getPlants();
 		if (plants.size()!=PLANTS) {
 			if (plants.size()>PLANTS) {
 				for (int i = plants.size() - 1; i >= PLANTS; i--) {
 					organisms.remove(plants.get(i));
 				}
 			} else {
 				for (int i = plants.size() - 1; i < PLANTS; i++) {
 					Plant plt = new Plant();
 					plt.name = "Plant" + (i + 1);
 					organisms.add(plt);
 	 				repositionPlant(plt);
 				}
 			}
 			r = true;
 		}
 		List<Animal> herbis = getAnimals(true);
 		if (herbis.size()!=herbivores) {
 			if (herbis.size()>herbivores) {
 				for (int i = herbis.size() - 1; i >= herbivores; i--) {
 					organisms.remove(herbis.get(i));
 				}
 			} else {
 				int start = herbis.size() - 1;
 				if (start < 0) {
 					start = 0;
 				}
 				for (int i = start; i < herbivores; i++) {
 					Animal ani = new Animal();
 					ani.name = "Herbivore" + String.format("%02d",(i + 1)) ;
 					ani.herbivore = true;
 					organisms.add(ani);
 					initializeAnimal(ani);
 				}
 			}
 			r = true;
 		}
 		List<Animal> carnis = getAnimals(false);
 		if (carnis.size()!=carnivores) {
 			if (carnis.size()>carnivores) {
 				for (int i = carnis.size() - 1; i >= carnivores; i--) {
 					organisms.remove(carnis.get(i));
 				}
 			} else {
 				int start = carnis.size() - 1;
 				if (start < 0) {
 					start = 0;
 				}
 				for (int i = start; i < carnivores; i++) {
 					Animal ani = new Animal();
 					ani.name = "Carnivore" + String.format("%02d",(i + 1)) ;
 					ani.herbivore = false;
 					organisms.add(ani);
 					initializeAnimal(ani);
 				}
 			}
 			r = true;
 		}
 		return r;
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
		long now = System.currentTimeMillis();
		for (Plant plt: getPlants()) {
			if (plt.dateTimeDied < (now - (deathDurationSeconds * 1000))) {
				r.add(plt);
			}
		}
		return r;
	}
 	
	public void initializeAnimal(Animal animal) {
		animal.score = 0;
		if (animal.herbivore) {
			animal.energy = maxEnergyHerbivore / 2;
		} else {
			animal.energy = maxEnergyCarnivore / 2;
		}
		repositionOrganism(animal,0,SIZE_X - 1,0,SIZE_Y - 1);
		animal.setRotation(ZRandomize.getRandomInt(0,3) * 90);
	}
 	
	public void repositionPlant(Plant plt) {
		int minX = 0;
		int maxX = 0;
		int minY = 0;
		int maxY = 0;
		int index = getPlants().indexOf(plt);
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
	
	public void repositionOrganism(Organism o,int minX,int maxX,int minY,int maxY) {
		int x = ZRandomize.getRandomInt(minX, maxX);
		int y = ZRandomize.getRandomInt(minY, maxY);
		boolean free = false;
		while (!free) {
			if (getOrganismByPos(x,y)==null) {
				free = true;
			} else {
				x = ZRandomize.getRandomInt(minX, maxX);
				y = ZRandomize.getRandomInt(minY, maxY);
			}
		}
		o.posX = x;
		o.posY = y;
	}

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
	
	public List<Animal> getAnimals(boolean herbivore) {
		List<Animal> r = new ArrayList<Animal>();
		for (Organism organism: organisms) {
			if (organism instanceof Animal) {
				Animal ani = (Animal) organism;
				if (ani.herbivore==herbivore) {
					r.add(ani);
				}
			}
		}
		return r;
	}
	
	public Organism getOrganismByPos(int posX, int posY) {
		Organism r = null;
		for (Organism organism: organisms) {
			if (organism.posX == posX && organism.posY == posY) {
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
			if (orgsElem!=null && orgsElem.children.size()>0) {
				organisms.clear();
				for (JsElem orgElem: orgsElem.children) {
					JsElem herbElem = orgElem.getChildByName("herbivore");
					JsFile js = new JsFile();
					js.rootElement = orgElem;
					if (herbElem!=null) {
						Animal ani = new Animal();
						ani.fromJson(js);
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
