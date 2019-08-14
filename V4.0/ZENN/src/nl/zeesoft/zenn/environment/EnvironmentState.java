package nl.zeesoft.zenn.environment;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.functions.ZRandomize;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.db.init.Persistable;

public class EnvironmentState implements Persistable {
	public EnvironmentConfig 			config					= null;
	
	public List<Organism>				organisms				= new ArrayList<Organism>();
	public List<History>				histories				= new ArrayList<History>();

	public void initialize(EnvironmentConfig config) {
		this.config = config;
		
		organisms.clear();
		
		for (int i = 0; i < EnvironmentConfig.PLANTS; i++) {
			Plant plt = new Plant();
			plt.name = "Plant" + (i + 1);
			organisms.add(plt);
		}
		for (int i = 0; i < config.herbivores; i++) {
			Animal ani = new Animal();
			ani.name = "Herbivore" + String.format("%02d",(i + 1)) ;
			ani.herbivore = true;
			organisms.add(ani);
		}
		for (int i = 0; i < config.carnivores; i++) {
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
 		if (plants.size()!=EnvironmentConfig.PLANTS) {
 			if (plants.size()>EnvironmentConfig.PLANTS) {
 				for (int i = plants.size() - 1; i >= EnvironmentConfig.PLANTS; i--) {
 					organisms.remove(plants.get(i));
 				}
 			} else {
 				for (int i = plants.size() - 1; i < EnvironmentConfig.PLANTS; i++) {
 					Plant plt = new Plant();
 					plt.name = "Plant" + (i + 1);
 					organisms.add(plt);
 	 				repositionPlant(plt);
 				}
 			}
 			r = true;
 		}
 		List<Animal> herbis = getAnimals(true);
 		if (herbis.size()!=config.herbivores) {
 			if (herbis.size()>config.herbivores) {
 				for (int i = herbis.size() - 1; i >= config.herbivores; i--) {
 					organisms.remove(herbis.get(i));
 				}
 			} else {
 				int start = herbis.size() - 1;
 				if (start < 0) {
 					start = 0;
 				}
 				for (int i = start; i < config.herbivores; i++) {
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
 		if (carnis.size()!=config.carnivores) {
 			if (carnis.size()>config.carnivores) {
 				for (int i = carnis.size() - 1; i >= config.carnivores; i--) {
 					organisms.remove(carnis.get(i));
 				}
 			} else {
 				int start = carnis.size() - 1;
 				if (start < 0) {
 					start = 0;
 				}
 				for (int i = start; i < config.carnivores; i++) {
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
			if (plant.energy==config.maxEnergyPlant) {
				div--;
			}
		}
		if (div>0) {
			for (Plant plt: livingPlants) {
				if (plt.energy<config.maxEnergyPlant) {
					if (plt.energy==0) {
						repositionPlant(plt);
					}
					plt.energy += (config.energyInputPerSecond / div);
					if (plt.energy > config.maxEnergyPlant) {
						plt.energy = config.maxEnergyPlant;
					}
				}
			}
		}
	}

	public List<Plant> getLivingPlants() {
		List<Plant> r = new ArrayList<Plant>();
		long now = System.currentTimeMillis();
		for (Plant plt: getPlants()) {
			if (plt.dateTimeDied < (now - (config.deathDurationSeconds * 1000))) {
				r.add(plt);
			}
		}
		return r;
	}
 	
	public void updateHistory() {
		List<History> list = new ArrayList<History>(histories);
		long timeStamp = System.currentTimeMillis() - (config.keepStateHistorySeconds * 1000);
		for (History hist: list) {
			if (hist.timeStamp < timeStamp) {
				histories.remove(hist);
			}
		}
		History hist = new History();
		hist.timeStamp = System.currentTimeMillis();
		hist.addOrganismData(organisms);
		histories.add(hist);
	}
	
	public void initializeAnimal(Animal animal) {
		animal.score = 0;
		if (animal.herbivore) {
			animal.energy = config.maxEnergyHerbivore / 2;
		} else {
			animal.energy = config.maxEnergyCarnivore / 2;
		}
		repositionOrganism(animal,0,EnvironmentConfig.SIZE_X - 1,0,EnvironmentConfig.SIZE_Y - 1);
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
			minX=(EnvironmentConfig.SIZE_X - 11);
			maxX=(EnvironmentConfig.SIZE_X - 1);
			minY=0;
			maxY=10;
		} else if (index==2) {
			minX=0;
			maxX=10;
			minY=(EnvironmentConfig.SIZE_Y - 11);
			maxY=(EnvironmentConfig.SIZE_Y - 1);
		} else if (index==3) {
			minX=(EnvironmentConfig.SIZE_X - 11);
			maxX=(EnvironmentConfig.SIZE_X - 1);
			minY=(EnvironmentConfig.SIZE_Y - 11);
			maxY=(EnvironmentConfig.SIZE_Y - 1);
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
		if (organisms.size()>0) {
			JsElem orgsElem = new JsElem("organisms",true);
			json.rootElement.children.add(orgsElem);
			for (Organism organism: organisms) {
				JsFile orgJs = organism.toJson();
				orgsElem.children.add(orgJs.rootElement);
			}
		}
		if (histories.size()>0) {
			JsElem histsElem = new JsElem("histories",true);
			json.rootElement.children.add(histsElem);
			for (History history: histories) {
				JsFile histJs = history.toJson();
				histsElem.children.add(histJs.rootElement);
			}
		}
		return json;
	}
	
	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
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
			JsElem histsElem = json.rootElement.getChildByName("histories");
			if (histsElem!=null && histsElem.children.size()>0) {
				histories.clear();
				for (JsElem histElem: histsElem.children) {
					JsFile js = new JsFile();
					js.rootElement = histElem;
					History hist = new History();
					hist.fromJson(js);
					histories.add(hist);
				}
			}
		}
	}

	@Override
	public ZStringBuilder getObjectName() {
		return new ZStringBuilder("State");
	}
}
