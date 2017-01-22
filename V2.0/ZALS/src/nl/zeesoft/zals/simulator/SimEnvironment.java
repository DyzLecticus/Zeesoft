package nl.zeesoft.zals.simulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zals.database.model.Animal;
import nl.zeesoft.zals.database.model.Carnivore;
import nl.zeesoft.zals.database.model.Environment;
import nl.zeesoft.zals.database.model.EnvironmentObject;
import nl.zeesoft.zals.database.model.Herbivore;
import nl.zeesoft.zals.database.model.Organism;
import nl.zeesoft.zals.database.model.Plant;
import nl.zeesoft.zals.database.model.StateHistory;
import nl.zeesoft.zals.database.model.ZALSModel;
import nl.zeesoft.zals.simulator.animals.BasicAnimal;
import nl.zeesoft.zals.simulator.object.ObjCarnivore;
import nl.zeesoft.zals.simulator.object.ObjEnvironment;
import nl.zeesoft.zals.simulator.object.ObjHerbivore;
import nl.zeesoft.zals.simulator.object.ObjPlant;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.gui.GuiController;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqGetFilter;
import nl.zeesoft.zodb.database.request.ReqRemove;
import nl.zeesoft.zodb.database.request.ReqUpdate;

public class SimEnvironment extends Locker {
	private ObjEnvironment			environments				= new ObjEnvironment();
	private ObjPlant 				plants						= null;
	private ObjHerbivore 			herbivores					= null;
	private ObjCarnivore 			carnivores					= null;
	
	private Date					energyUpdated				= new Date();
	private Date					historyRemoved				= new Date();
	
	private Environment				environment					= null;
	private List<SimAnimalWorker>	animalWorkers				= new ArrayList<SimAnimalWorker>();
	
	private String					workingClassNameHerbivore	= "";
	private String					workingClassNameCarnivore	= "";
	
	private boolean					showProgress				= true;
	
	/************************ Methods called by worker ******************************/
	protected void initialize() {
		GuiController.getInstance().setProgressFrameTitle("Initializing simulation ...");
		lockMe(this);
		environments.initialize();
		environment = environments.getEnvironment();
		GuiController.getInstance().setProgressFrameTodo((environment.getHerbivores() + environment.getCarnivores()) + 1);
		workingClassNameHerbivore = environment.getClassNameHerbivore();
		workingClassNameCarnivore = environment.getClassNameCarnivore();
		plants = new ObjPlant(environment.getId());
		plants.initialize();
		herbivores = new ObjHerbivore(environment.getId());
		herbivores.initialize();
		carnivores = new ObjCarnivore(environment.getId());
		carnivores.initialize();
		List<Animal> removeAnimals = updateAnimals(environment);
		GuiController.getInstance().incrementProgressFrameDone();
		List<SimAnimalWorker> stopWorkers = updateAnimalWorkers(environment);
		Environment env = environment;
		List<SimAnimalWorker> workers = new ArrayList<SimAnimalWorker>(animalWorkers); 
		unlockMe(this);
		for (SimAnimalWorker worker: stopWorkers) {
			worker.stop();
		}
		for (SimAnimalWorker worker: workers) {
			worker.updateEnvironment(env);
		}
		for (Animal animal: removeAnimals) {
			SimAnimalWorker worker = getWorkerForAnimal(animal);
			worker.getAnimal().removeModules();
		}
		lockMe(this);
		showProgress = false;
		unlockMe(this);
	}
	
	protected Environment getEnvironment() {
		lockMe(this);
		Environment r = environment;
		unlockMe(this);
		return r;
	}
	
	protected void refreshEnvironment() {
		lockMe(this);
		environments.reinitialize();
		environment = environments.getEnvironment();
		List<Animal> removeAnimals = updateAnimals(environment);

		boolean changedClassName = false;
		if (!workingClassNameHerbivore.equals(environment.getClassNameHerbivore())) {
			workingClassNameHerbivore = environment.getClassNameHerbivore();
			changedClassName = true;
		}
		if (!workingClassNameCarnivore.equals(environment.getClassNameCarnivore())) {
			workingClassNameCarnivore = environment.getClassNameCarnivore();
			changedClassName = true;
		}
		Environment env = environment;
		List<SimAnimalWorker> workers = null;
		unlockMe(this);
		if (!changedClassName) {
			lockMe(this);
			List<SimAnimalWorker> stopWorkers = updateAnimalWorkers(environment);
			workers = new ArrayList<SimAnimalWorker>(animalWorkers); 
			unlockMe(this);
			for (SimAnimalWorker worker: stopWorkers) {
				worker.stop();
			}
		} else {
			lockMe(this);
			List<SimAnimalWorker> stopWorkers = new ArrayList<SimAnimalWorker>(animalWorkers);
			unlockMe(this);
			for (SimAnimalWorker worker: stopWorkers) {
				worker.stop();
			}
			lockMe(this);
			animalWorkers.clear();
			updateAnimalWorkers(environment);
			workers = new ArrayList<SimAnimalWorker>(animalWorkers); 
			unlockMe(this);
		}
		for (SimAnimalWorker worker: workers) {
			worker.updateEnvironment(env);
		}
		for (Animal animal: removeAnimals) {
			SimAnimalWorker worker = getWorkerForAnimal(animal);
			worker.getAnimal().removeModules();
		}
	}
	
	protected void simulate() {
		Environment env = getEnvironment();
		updatePlantEnergy(env);
		updateStateHistory(env);
	}
	
	protected void updateObjects() {
		lockMe(this);
		for (EnvironmentObject obj: getEnvironmentObjects()) {
			ReqUpdate request = obj.getNewUpdateRequest(null);
			request.getUpdateObject().removePropertyValue("environment");
			DbRequestQueue.getInstance().addRequest(obj.getNewUpdateRequest(null),this);
		}
		unlockMe(this);
	}
	
	protected void stopSimulating() {
		updateObjects();
		lockMe(this);
		List<SimAnimalWorker> workers = new ArrayList<SimAnimalWorker>(animalWorkers);
		unlockMe(this);
		for (SimAnimalWorker worker: workers) {
			worker.stop();
		}
		animalWorkers.clear();
	}

	/************************ Methods called by animals ******************************/
	protected String[] takeLook(SimAnimal animal) {
		boolean update = false;
		boolean reinitialize = false;

		String[] inputs = new String[6];

		inputs[0] = Environment.COLOR_NOTHING;
		inputs[1] = Environment.INTENSITY_NOTHING;
		inputs[2] = Environment.COLOR_NOTHING;
		inputs[3] = Environment.INTENSITY_NOTHING;
		inputs[4] = Environment.COLOR_NOTHING;
		inputs[5] = Environment.INTENSITY_NOTHING;
		
		Environment env = getEnvironment();
		
		lockMe(this);
		if (animal.getAnimal().getEnergy()>0) {
			for (int direction = 0; direction < 3; direction++) {
				String scanAxis = "";
				if (animal.getAnimal().getRotation()==0) {
					if (direction==0) {
						scanAxis = "x-";
					} else if (direction==1) {
						scanAxis = "y-";
					} else if (direction==2) {
						scanAxis = "x+";
					}
				} else if (animal.getAnimal().getRotation()==90) {
					if (direction==0) {
						scanAxis = "y-";
					} else if (direction==1) {
						scanAxis = "x+";
					} else if (direction==2) {
						scanAxis = "y+";
					}
				} else if (animal.getAnimal().getRotation()==180) {
					if (direction==0) {
						scanAxis = "x+";
					} else if (direction==1) {
						scanAxis = "y+";
					} else if (direction==2) {
						scanAxis = "x-";
					}
				} else if (animal.getAnimal().getRotation()==270) {
					if (direction==0) {
						scanAxis = "y+";
					} else if (direction==1) {
						scanAxis = "x-";
					} else if (direction==2) {
						scanAxis = "y-";
					}
				}
				int x = animal.getAnimal().getPosX();
				int y = animal.getAnimal().getPosY();
				String color = Environment.COLOR_NOTHING;
				String intensity = Environment.INTENSITY_NOTHING;
				for (int i = 0; i < (Environment.INTENSITIES.length - 1); i++) {
					if (scanAxis=="x-") {
						x--;
					} else if (scanAxis=="y-") {
						y--;
					} else if (scanAxis=="x+") {
						x++;
					} else if (scanAxis=="y+") {
						y++;
					}
					if (
						x>=0 && x<Environment.SIZE_X && 
						y>=0 && y<Environment.SIZE_Y
						) {
						EnvironmentObject dirObj = getEnvironmentObjectByPosition(x,y);
						if (dirObj!=null) {
							if (dirObj instanceof Organism && ((Organism)dirObj).getEnergy()==0) {
								color = Environment.COLOR_DEAD_OBJECT;
							} else {
								if (dirObj instanceof Plant) {
									color = Environment.COLOR_PLANT;
								} else if (dirObj instanceof Herbivore) {
									color = Environment.COLOR_HERBIVORE;
								} else if (dirObj instanceof Carnivore) {
									color = Environment.COLOR_CARNIVORE;
								}
							}
							intensity = Environment.INTENSITIES[i];
							break;
						}
					} else {
						color = Environment.COLOR_WALL;
						intensity = Environment.INTENSITIES[i];
						break;
					}
				}
				inputs[(direction * 2)] = color;
				inputs[(direction * 2) + 1] = intensity;
			}
		} else {
			inputs = null;
			Date now = new Date();
			if (animal.getAnimal().getDateTimeDied() < (now.getTime() - (env.getDeathDurationSeconds() * 1000))) {
				reinitialize = true;
				update = (animal.getAnimal().getTrainedStates()<(Environment.MINIMUM_TRAINING_STATES * 5));
			}
		}
		unlockMe(this);
		
		if (reinitialize) {
			if (update) {
				animal.updateModules();
			}
			animal.reinitializeModules();
			lockMe(this);
			List<EnvironmentObject> objs = getEnvironmentObjects();
			env.initializeAnimal(animal.getAnimal(),objs);
			unlockMe(this);
		}

		return inputs;
	}

	protected String takeAction(SimAnimal animal,String action,int firedLinks,long trainedStates,StringBuilder plannedActions,StringBuilder trainingModules,StringBuilder confabulatingModules) {
		String feedback = "";
		Environment env = getEnvironment();
		
		boolean reinitializeModules = false;

		// Sanity check
		if (!action.equals(Animal.ACTION_MOVE_FORWARD) && 
			!action.equals(Animal.ACTION_TURN_LEFT) && 
			!action.equals(Animal.ACTION_TURN_RIGHT)  && 
			!action.equals(Animal.ACTION_TAKE_BITE)
			) {
			Messenger.getInstance().error(this,animal.getAnimal().getClass().getName() + ":" + animal.getAnimal().getId() + ": action: " + action + ", firedLinks: " + firedLinks);
		}
		
		lockMe(this);
		if (
			!Generic.stringBuilderEquals(animal.getAnimal().getTrainingModules(),trainingModules) ||
			!Generic.stringBuilderEquals(animal.getAnimal().getConfabulatingModules(),confabulatingModules)
			) {
			animal.getAnimal().setScore(0);
			animal.getAnimal().setTopScore(0);
		}
		animal.getAnimal().setTrainingModules(trainingModules);
		animal.getAnimal().setConfabulatingModules(confabulatingModules);
		if (animal.getAnimal().getEnergy()>0) {
			Date now = new Date();
			animal.getAnimal().setPlannedActions(plannedActions);
			int actionEnergy = 0 ;
			if (action.equals(Animal.ACTION_TURN_LEFT)) {
				actionEnergy = env.getEnergyActionTurn();
				if (animal.getAnimal().getRotation()==0) {
					animal.getAnimal().setRotation(270);
				} else {
					animal.getAnimal().setRotation(animal.getAnimal().getRotation() - 90);
				}
				feedback = Animal.FEEDBACK_TURNED_LEFT;
			} else if (action.equals(Animal.ACTION_TURN_RIGHT)) {
				actionEnergy = env.getEnergyActionTurn();
				animal.getAnimal().setRotation(animal.getAnimal().getRotation() + 90);
				feedback = Animal.FEEDBACK_TURNED_RIGHT;
			} else if (action.equals(Animal.ACTION_TAKE_BITE)) {
				actionEnergy = env.getEnergyActionBite();
				EnvironmentObject obj = getEnvironmentObjectByPosition(animal.getAnimal().getForwardPosX(),animal.getAnimal().getForwardPosY());
				Organism org = null;
				if (obj!=null && obj instanceof Organism) {
					org = (Organism) obj;
				}
				if (org!=null && org.getEnergy()>0 && (
					(animal.getAnimal() instanceof Herbivore && org instanceof Plant) ||
					(animal.getAnimal() instanceof Carnivore && org instanceof Herbivore)
					)) {
					int maxBite = 0;
					int max = 0;
					if (animal.getAnimal() instanceof Herbivore) {
						maxBite = env.getMaxEnergyHerbivoreBite();
						max = env.getMaxEnergyHerbivore();
					} else if (animal.getAnimal() instanceof Carnivore) {
						maxBite = env.getMaxEnergyCarnivoreBite();
						max = env.getMaxEnergyCarnivore();
					}
					int addEnergy = maxBite;
					if (addEnergy > org.getEnergy()) {
						addEnergy = org.getEnergy();
					}
					if (addEnergy>0) {
						org.setEnergy(org.getEnergy() - addEnergy);
						if (org.getEnergy()==0) {
							//Messenger.getInstance().debug(this,obj.getClass().getName() + ":" + obj.getId() + " was eaten by " + animal.getAnimal().getClass().getName() + ":" + animal.getAnimal().getId());
							org.setDateTimeDied(now.getTime());
						}
						if (animal.getAnimal().getEnergy()+addEnergy>max) {
							animal.getAnimal().setEnergy(max);
						} else {
							animal.getAnimal().setEnergy(animal.getAnimal().getEnergy() + addEnergy);
						}
						animal.getAnimal().incrementScore();
						if (animal.getAnimal().getScore()>=animal.getAnimal().getTopScore() &&
							(
								(animal.getAnimal().getScore() <= 100 && animal.getAnimal().getScore() % 20 == 0) || 
								(animal.getAnimal().getScore() > 100 && animal.getAnimal().getScore() % 50 == 0)
							)
							) {
							reinitializeModules = true;
						}
						feedback = Animal.FEEDBACK_TOOK_BITE;
					}
				}
			} else if (action.equals(Animal.ACTION_MOVE_FORWARD)) {
				actionEnergy = env.getEnergyActionMove();
				int fx = animal.getAnimal().getForwardPosX();
				int fy = animal.getAnimal().getForwardPosY();
				boolean canMove = true;
				if (fx>=0 && fx<Environment.SIZE_X && fy>=0 && fy<Environment.SIZE_Y) {
					EnvironmentObject obj = getEnvironmentObjectByPosition(animal.getAnimal().getForwardPosX(),animal.getAnimal().getForwardPosY());
					if (obj!=null) {
						canMove = false;
					}
				} else {
					canMove = false;
				}
				if (canMove) {
					animal.getAnimal().setPosY(fy);
					animal.getAnimal().setPosX(fx);
					feedback = Animal.FEEDBACK_MOVED_FORWARD;
				}
			}
			actionEnergy+=(env.getEnergyActionLook() + firedLinks);
			animal.getAnimal().setEnergy(animal.getAnimal().getEnergy() - actionEnergy);
			if (animal.getAnimal().getEnergy()==0) {
				//Messenger.getInstance().debug(this,animal.getAnimal().getClass().getName() + ":" + animal.getAnimal().getId() + " died of starvation");
				animal.getAnimal().setDateTimeDied(now.getTime());
			}
		} else {
			animal.getAnimal().setPlannedActions(new StringBuilder());
		}
		animal.getAnimal().setTrainedStates(trainedStates);
		unlockMe(this);

		if (reinitializeModules) {
			animal.updateModules();
			animal.reinitializeModules();
		}
		return feedback;
	}
	
	/************************ PRIVATE METHODS ******************************/
	private void updatePlantEnergy(Environment env) {
		Date now = new Date();
		if (now.getTime()>(energyUpdated.getTime() + 1000)) {
			lockMe(this);
			List<Plant> livingPlants = plants.getLivingPlantsAsList(env);
			int div = livingPlants.size();
			for (Plant plant: livingPlants) {
				if (plant.getEnergy()==env.getMaxEnergyPlant()) {
					div--;
				}
			}
			if (div>0) {
				for (Plant plant: livingPlants) {
					if (plant.getEnergy()<env.getMaxEnergyPlant()) {
						if (plant.getEnergy()==0) {
							List<EnvironmentObject> objs = getEnvironmentObjects();
							env.repositionPlant(plant, objs);
						}
						int energy = plant.getEnergy() + (env.getEnergyInputPerSecond() / div);
						if (energy>env.getMaxEnergyPlant()) {
							energy = env.getMaxEnergyPlant();
						}
						plant.setEnergy(energy);
					}
				}
			}
			unlockMe(this);
			energyUpdated = now;
		}
	}

	private void updateStateHistory(Environment env) {
		Date now = new Date();
		if (now.getTime()>(historyRemoved.getTime() + 1000)) {
			lockMe(this);
			ReqRemove request = new ReqRemove(ZALSModel.STATE_HISTORY_CLASS_FULL_NAME);
			request.getGet().addFilter("dateTime",ReqGetFilter.LESS_OR_EQUALS,"" + (now.getTime() - (env.getKeepStateHistorySeconds() * 1000)));
			DbRequestQueue.getInstance().addRequest(request,this);
			unlockMe(this);
			historyRemoved = now;
		}
		addStateHistory(env.getId());
	}
	
	private void addStateHistory(long environmentId) {
		lockMe(this);
		StateHistory hist = new StateHistory();
		hist.setEnvironmentId(environmentId);
		hist.setDateTime((new Date()).getTime());
		for (EnvironmentObject obj: getEnvironmentObjects()) {
			hist.addEnvironmentObjectToObjectData(obj); 
		}
		ReqAdd request = new ReqAdd(ZALSModel.STATE_HISTORY_CLASS_FULL_NAME);
		request.getObjects().add(new ReqDataObject(hist.toDataObject()));
		DbRequestQueue.getInstance().addRequest(request,this);
		unlockMe(this);
	}

	private List<Animal> updateAnimals(Environment env) {
		List<Animal> removeAnimals = new ArrayList<Animal>();
		List<EnvironmentObject> objs = getEnvironmentObjects();
		List<Herbivore> addHerbivores = new ArrayList<Herbivore>();
		List<Herbivore> removeHerbivores = new ArrayList<Herbivore>();
		if (herbivores.getObjectsAsList().size()<env.getHerbivores()) {
			int add = env.getHerbivores() - herbivores.getObjectsAsList().size();
			for (int i = 0; i < add; i++) {
				Herbivore herb = env.getNewHerbivore(objs);
				objs.add(herb);
				addHerbivores.add(herb);
			}
		} else if (herbivores.getObjectsAsList().size()>env.getHerbivores()) {
			int i = 0;
			for (Herbivore herb: herbivores.getHerbivoresAsList()) {
				if (i>=env.getHerbivores()) {
					removeHerbivores.add(herb);
				}
				i++;
			}
		}
		List<Carnivore> addCarnivores = new ArrayList<Carnivore>();
		List<Carnivore> removeCarnivores = new ArrayList<Carnivore>();
		if (carnivores.getObjectsAsList().size()<env.getCarnivores()) {
			int add = env.getCarnivores() - carnivores.getObjectsAsList().size();
			for (int i = 0; i < add; i++) {
				Carnivore carn = env.getNewCarnivore(objs);
				objs.add(carn);
				addCarnivores.add(carn);
			}
		} else if (carnivores.getObjectsAsList().size()>env.getCarnivores()) {
			int i = 0;
			for (Carnivore carn: carnivores.getCarnivoresAsList()) {
				if (i>=env.getCarnivores()) {
					removeCarnivores.add(carn);
				}
				i++;
			}
		}
		if (addHerbivores.size()>0) {
			herbivores.addHerbivores(addHerbivores);
			Messenger.getInstance().debug(this,"Added herbivores: " + addHerbivores.size());
		}
		if (removeHerbivores.size()>0) {
			for (Herbivore herb: removeHerbivores) {
				DbRequestQueue.getInstance().addRequest(new ReqRemove(ZALSModel.HERBIVORE_CLASS_FULL_NAME,herb.getId()),this);
				herbivores.removeObject(herb);
			}
			Messenger.getInstance().debug(this,"Removed herbivores: " + removeHerbivores.size());
		}
		if (addCarnivores.size()>0) {
			carnivores.addCarnivores(addCarnivores);
			Messenger.getInstance().debug(this,"Added carnivores: " + addCarnivores.size());
		}
		if (removeCarnivores.size()>0) {
			for (Carnivore carn: removeCarnivores) {
				DbRequestQueue.getInstance().addRequest(new ReqRemove(ZALSModel.CARNIVORE_CLASS_FULL_NAME,carn.getId()),this);
				carnivores.removeObject(carn);
			}
			Messenger.getInstance().debug(this,"Removed carnivores: " + removeCarnivores.size());
		}
		removeAnimals.addAll(removeHerbivores);
		removeAnimals.addAll(removeCarnivores);
		return removeAnimals;
	}

	private List<SimAnimalWorker> updateAnimalWorkers(Environment env) {
		List<SimAnimalWorker> startWorkers = new ArrayList<SimAnimalWorker>();
		List<SimAnimalWorker> stopWorkers = new ArrayList<SimAnimalWorker>();
		List<SimAnimalWorker> herbivoreWorkers = getHerbivoreWorkers();
		if (herbivoreWorkers.size()<herbivores.getObjectsAsList().size()) {
			for (Herbivore herb: herbivores.getHerbivoresAsList()) {
				SimAnimalWorker worker = getWorkerForAnimal(herb);
				if (worker==null) {
					worker = new SimAnimalWorker(getNewSimAnimalForAnimal(herb),env);
					animalWorkers.add(worker);
					startWorkers.add(worker);
					if (showProgress) {
						GuiController.getInstance().incrementProgressFrameDone();
					}
				}
			}
		} else if (herbivoreWorkers.size()>herbivores.getObjectsAsList().size()) {
			for (SimAnimalWorker worker: herbivoreWorkers) {
				if (herbivores.getObjectById(worker.getAnimal().getAnimal().getId())==null) {
					stopWorkers.add(worker);
					animalWorkers.remove(worker);
				}
			}
		}
		List<SimAnimalWorker> carnivoreWorkers = getCarnivoreWorkers();
		if (carnivoreWorkers.size()<carnivores.getObjectsAsList().size()) {
			for (Carnivore carn: carnivores.getCarnivoresAsList()) {
				SimAnimalWorker worker = getWorkerForAnimal(carn);
				if (worker==null) {
					worker = new SimAnimalWorker(getNewSimAnimalForAnimal(carn),env);
					animalWorkers.add(worker);
					startWorkers.add(worker);
					if (showProgress) {
						GuiController.getInstance().incrementProgressFrameDone();
					}
				}
			}
		} else if (carnivoreWorkers.size()<carnivores.getObjectsAsList().size()) {
			for (SimAnimalWorker worker: carnivoreWorkers) {
				if (carnivores.getObjectById(worker.getAnimal().getAnimal().getId())==null) {
					stopWorkers.add(worker);
					animalWorkers.remove(worker);
				}
			}
		}
		for (SimAnimalWorker worker: startWorkers) {
			worker.start();
		}
		return stopWorkers;
	}

	private SimAnimal getNewSimAnimalForAnimal(Animal animal) {
		SimAnimal r = null;
		Object obj = null;
		String className = "";
		if (animal instanceof Herbivore) {
			className = workingClassNameHerbivore;
		} else if (animal instanceof Carnivore) {
			className = workingClassNameCarnivore;
		}
		obj = Generic.testInstanceForName(className);
		if (obj==null || (!(obj instanceof SimAnimal))) {
			Messenger.getInstance().debug(this,"Invalid animal simulator class name: " + className + " (object: " + obj + ")");
			obj = new BasicAnimal();
		}
		r = (SimAnimal) obj;
		r.setAnimalAndEnvironment(animal,this,environment);
		return r;
	}
	
	private List<SimAnimalWorker> getHerbivoreWorkers() {
		List<SimAnimalWorker> r = new ArrayList<SimAnimalWorker>();
		for (SimAnimalWorker worker: animalWorkers) {
			if (worker.getAnimal().getAnimal() instanceof Herbivore) {
				r.add(worker);
			}
		}
		return r;
	}

	private List<SimAnimalWorker> getCarnivoreWorkers() {
		List<SimAnimalWorker> r = new ArrayList<SimAnimalWorker>();
		for (SimAnimalWorker worker: animalWorkers) {
			if (worker.getAnimal().getAnimal() instanceof Carnivore) {
				r.add(worker);
			}
		}
		return r;
	}

	private SimAnimalWorker getWorkerForAnimal(Animal animal) {
		SimAnimalWorker r = null;
		if (animal instanceof Herbivore) {
			for (SimAnimalWorker worker: getHerbivoreWorkers()) {
				if (worker.getAnimal().getAnimal().getId()==animal.getId()) {
					r = worker;
					break;
				}
			}
		} else if (animal instanceof Carnivore) {
			for (SimAnimalWorker worker: getCarnivoreWorkers()) {
				if (worker.getAnimal().getAnimal().getId()==animal.getId()) {
					r = worker;
					break;
				}
			}
		}
		return r;
	}
	
	private List<EnvironmentObject> getEnvironmentObjects() {
		List<EnvironmentObject> r = new ArrayList<EnvironmentObject>();
		for (Plant plant: plants.getPlantsAsList()) {
			r.add(plant);
		}
		for (Herbivore herb: herbivores.getHerbivoresAsList()) {
			r.add(herb);
		}
		for (Carnivore carn: carnivores.getCarnivoresAsList()) {
			r.add(carn);
		}
		return r;
	}
	
	private EnvironmentObject getEnvironmentObjectByPosition(int x,int y) {
		EnvironmentObject r = null;
		if (x>=0 && x<Environment.SIZE_X && y>=0 && y<Environment.SIZE_Y) {
			for (EnvironmentObject obj:getEnvironmentObjects()) {
				if (obj.getPosX()==x && obj.getPosY()==y) {
					r = obj;
					break;
				}
			}
		}
		return r;
	}
}
