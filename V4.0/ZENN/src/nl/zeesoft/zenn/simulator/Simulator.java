package nl.zeesoft.zenn.simulator;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.functions.ZRandomize;
import nl.zeesoft.zdk.genetic.EvolverUnit;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.neural.Prediction;
import nl.zeesoft.zdk.thread.LockedCode;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zenn.animal.AnimalConstants;
import nl.zeesoft.zenn.animal.AnimalMutator;
import nl.zeesoft.zenn.animal.CarnivoreEvolver;
import nl.zeesoft.zenn.animal.CarnivoreMutator;
import nl.zeesoft.zenn.animal.HerbivoreEvolver;
import nl.zeesoft.zenn.animal.HerbivoreMutator;
import nl.zeesoft.zenn.environment.Animal;
import nl.zeesoft.zenn.environment.EnvironmentConfig;
import nl.zeesoft.zenn.environment.EnvironmentState;
import nl.zeesoft.zenn.environment.Organism;
import nl.zeesoft.zenn.environment.Plant;
import nl.zeesoft.zodb.Config;

public class Simulator extends Locker {
	private WorkerUnion							union					= null;
	private EnvironmentInitializer				envInitializer			= null;
	private SimulatorAnimalInitializer			simAniInitializer		= null;
	private HerbivoreEvolver					herbEvolver				= null;
	private CarnivoreEvolver					carnEvolver				= null;
	private HerbivoreMutator					herbMutator				= null;
	private CarnivoreMutator					carnMutator				= null;
	private SimulatorWorker						worker					= null;

	private EnvironmentConfig					environmentConfig		= null;
	private EnvironmentState					environmentState		= null;

	private List<SimulatorAnimalWorker>			animalWorkers			= new ArrayList<SimulatorAnimalWorker>();
	private boolean								working					= false;
	
	private long								energyUpdated			= 0;
	private long								stateUpdated			= 0;
	
	public Simulator(Config config,EnvironmentInitializer envInit,SimulatorAnimalInitializer simAniInit,HerbivoreEvolver herbEvo,CarnivoreEvolver carnEvo,HerbivoreMutator herbMut, CarnivoreMutator carnMut) {
		super(config.getMessenger());
		union = config.getUnion();
		envInitializer = envInit;
		simAniInitializer = simAniInit;
		herbEvolver = herbEvo;
		carnEvolver = carnEvo;
		herbMutator = herbMut;
		carnMutator = carnMut;
		worker = new SimulatorWorker(config.getMessenger(),config.getUnion(),this);
	}
	
	public void setEnvironment(EnvironmentConfig environmentConfig,EnvironmentState environmentState) {
		lockMe(this);
		this.environmentConfig = environmentConfig;
		this.environmentState = environmentState;
		worker.setEnvironmentConfig(environmentConfig);
		unlockMe(this);
	}
	
	public void start() {
		lockMe(this);
		if (environmentConfig!=null && environmentConfig.active && environmentState!=null && !working && !worker.isWorking()) {
			if (environmentState.prepareForStart()) {
				if (envInitializer!=null) {
					envInitializer.updatedState();
				}
			}
			long now = System.currentTimeMillis();
			energyUpdated = now;
			
			animalWorkers.clear();
			for (Animal ani: environmentState.getAnimals()) {
				SimulatorAnimalWorker animalWorker = new SimulatorAnimalWorker(getMessenger(),union,this,ani,environmentConfig);
				animalWorker.setSimulatorAnimal(simAniInitializer.getSimulatorAnimalByName(animalWorker.getAnimalName()));
				animalWorkers.add(animalWorker);
			}
			
			worker.start();
			working = true;
			
			for (SimulatorAnimalWorker animalWorker: animalWorkers) {
				animalWorker.start();
			}
			
			if (getMessenger()!=null) {
				getMessenger().debug(this,"Started simulator");
			}
		}
		unlockMe(this);
	}
	
	public void stop() {
		boolean stop = false;
		List<SimulatorAnimalWorker>	stopWorkers = null;
		lockMe(this);
		if (working) {
			working = false;
			stop = true;
			for (SimulatorAnimalWorker animalWorker: animalWorkers) {
				animalWorker.stop();
			}
			stopWorkers = new ArrayList<SimulatorAnimalWorker>(animalWorkers);
			animalWorkers.clear();
			if (getMessenger()!=null) {
				getMessenger().debug(this,"Stopped simulator");
			}
		}
		unlockMe(this);
		if (stop) {
			worker.stop();
			for (SimulatorAnimalWorker animalWorker: stopWorkers) {
				animalWorker.waitForStopAndDestroy();
			}
		}
	}
	
	public boolean isWorking() {
		boolean r = false;
		lockMe(this);
		r = working;
		unlockMe(this);
		return r;
	}
	
	public void destroy() {
		lockMe(this);
		if (!working) {
			worker.destroy();
			worker = null;
		}
		unlockMe(this);
	}

	public JsFile getEnvironmentState() {
		JsFile json = null; 
		lockMe(this);
		if (environmentState!=null && environmentConfig!=null && working) {
			json = environmentState.toJson();
			json.rootElement.children.add(new JsElem("statesPerSecond","" + environmentConfig.statesPerSecond));
			json.rootElement.children.add(new JsElem("keepStateHistorySeconds","" + environmentConfig.keepStateHistorySeconds));
			json.rootElement.children.add(new JsElem("maxEnergyPlant","" + environmentConfig.maxEnergyPlant));
		}
		unlockMe(this);
		return json;
	}

	protected SimulatorAnimalWorker getAnimalWorkerByAnimalName(String name) {
		SimulatorAnimalWorker r = null;
		for (SimulatorAnimalWorker animalWorker: animalWorkers) {
			if (animalWorker.getAnimalName().equals(name)) {
				r = animalWorker;
				break;
			}
		}
		return r;
	}
	
	protected void simulateNextState() {
		boolean simulate = false;
		lockMe(this);
		List<Animal> livingAnimals = new ArrayList<Animal>();
		if (working && environmentState!=null) {
			simulate = true;
			for (Animal ani: environmentState.getAnimals()) {
				if (organismIsAliveNoLock(ani)) {
					livingAnimals.add(ani);
				}
			}
		}
		unlockMe(this);
		if (simulate) {
			for (Animal ani: livingAnimals) {
				SimulatorAnimalWorker animalWorker = getAnimalWorkerByAnimalName(ani.name);
				if (animalWorker!=null && !animalWorker.hasSimulatorAnimal()) {
					EvolverUnit bestSoFar = null;
					if (ani.herbivore) {
						bestSoFar = herbEvolver.getBestSoFar();
					} else {
						bestSoFar = carnEvolver.getBestSoFar();
					}
					if (bestSoFar!=null) {
						LockedCode code = new LockedCode() {
							@Override
							public Object doLocked() {
								if (environmentState.canInitializeAnimal(ani.herbivore)) {
									EvolverUnit bestSoFar = (EvolverUnit) param1;
									SimulatorAnimal simAni = null;
									AnimalMutator mutator = null;
									if (ani.herbivore) {
										mutator = herbMutator;
									} else {
										mutator = carnMutator;
									}
									simAni = mutator.getTopScoringAnimal();
									if (simAni==null || ZRandomize.getRandomInt(0,1)==1) {
										EvolverUnit bestVariation = mutator.getBestSoFar();
										if (bestVariation!=null && ZRandomize.getRandomInt(0,1)==1) {
											bestSoFar = bestVariation;
										}
										simAni = new SimulatorAnimal();
										simAni.name = ani.name;
										simAni.unit = bestSoFar;
									}
									simAniInitializer.addOrReplaceObject(simAni);
									if (ani.energy==0) {
										environmentState.initializeAnimal(ani,true);
									}
									animalWorker.setSimulatorAnimal(simAni);
								}
								return null;
							}
						};
						code.param1 = bestSoFar;
						doLocked(this,code);
					}
				}
			}
			lockMe(this);
			long now = System.currentTimeMillis();
			if (now>(energyUpdated + 1000)) {
				environmentState.updatePlants();
				energyUpdated = now;
			}
			environmentState.updateHistory();
			if (working && envInitializer!=null && now> stateUpdated + 1000) {
				stateUpdated = now;
				envInitializer.updatedState();
			}
			unlockMe(this);
		}
	}
	
	protected void setPredictionInputForAnimal(Animal ani, int size,Prediction p) {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				if (working && organismIsAliveNoLock(ani)) {
					p.inputs[AnimalConstants.IN_RANDOM] = Math.round(ZRandomize.getRandomFloat(0,1));
					for (int i = 0; i < 3; i++) {
						int posX = ani.posX;
						int posY = ani.posY;
						for (int d = 0; d <= 3; d++) {
							if (ani.rotation==0) {
								if (i==0) {
									posX--;
								} else if (i==1) {
									posY--;
								} else if (i==2) {
									posX++;
								}
							} else if (ani.rotation==90) {
								if (i==0) {
									posY--;
								} else if (i==1) {
									posX++;
								} else if (i==2) {
									posY++;
								}
							} else if (ani.rotation==180) {
								if (i==0) {
									posX++;
								} else if (i==1) {
									posY++;
								} else if (i==2) {
									posX--;
								}
							} else if (ani.rotation==270) {
								if (i==0) {
									posY++;
								} else if (i==1) {
									posX--;
								} else if (i==2) {
									posY--;
								}
							}
							float[] color = null;
							if (posX<0||posX>=EnvironmentConfig.SIZE_X ||
								posY<0||posY>=EnvironmentConfig.SIZE_Y
								) {
								color = AnimalConstants.COLOR_GREY;
							} else {
								Organism org = environmentState.getOrganismByPos(posX, posY);
								if (org!=null && (org.energy==0 || !organismIsAliveNoLock(org))) {
									org = null;
									color = AnimalConstants.COLOR_GREY;
								}
								if (org!=null) {
									if (org instanceof Plant) {
										color = AnimalConstants.COLOR_GREEN;
									} else if (org instanceof Animal) {
										Animal ani2 = (Animal) org;
										if (ani2.herbivore) {
											color = AnimalConstants.COLOR_BLUE;
										} else {
											color = AnimalConstants.COLOR_RED;
										}
									}
								}
							}
							if (color!=null) {
								for (int c = 0; c < color.length; c++) {
									p.inputs[1 + (i * 3) + c] = color[c] * AnimalConstants.INTENSITIES[d];
								}
								break;
							}
						}
					}
					ani.energy = ani.energy - (environmentConfig.energyActionLookFactor * size);
					checkOrganismEnergyNoLock(ani);
				}
				return null;
			}
		};
		doLocked(this, code);
	}
	
	protected void handlePredictionOutputForAnimal(Animal ani,Prediction p) {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				if (working && organismIsAliveNoLock(ani)) {
					ani.lastAction = "";
					List<Integer> activeOutputs = new ArrayList<Integer>();
					for (int i = 0; i < p.outputs.length; i++) {
						int rounded = Math.round(p.outputs[i]);
						if (rounded>=1) {
							activeOutputs.add(i);
						}
					}
					if (activeOutputs.size()==1) {
						ani.lastAction = getActionForActiveOutput(activeOutputs.get(0));
					} else if (activeOutputs.size()>0) {
						int i = ZRandomize.getRandomInt(0,activeOutputs.size() - 1);
						ani.lastAction = getActionForActiveOutput(activeOutputs.get(i));
					}
					if (ani.lastAction.length()==0) {
						int i = ZRandomize.getRandomInt(0,AnimalConstants.OUTPUTS.length - 1);
						ani.lastAction = getActionForActiveOutput(i);
					}
					
					if (ani.lastAction.equals(EnvironmentConfig.ACTION_MOVE_FORWARD)) {
						int newX = ani.getForwardPosX();
						int newY = ani.getForwardPosY();
						if (newX>=0 && newX<EnvironmentConfig.SIZE_X &&
							newY>=0 && newY<EnvironmentConfig.SIZE_Y
							) {
							Organism org = environmentState.getOrganismByPos(newX,newY);
							if (org==null) {
								ani.posX = newX;
								ani.posY = newY;
							}
						}
						ani.energy = ani.energy - environmentConfig.energyActionMove;
						checkOrganismEnergyNoLock(ani);
					} else if (ani.lastAction.equals(EnvironmentConfig.ACTION_TURN_RIGHT)) {
						ani.rotation = (ani.rotation + 90) % 360;
						ani.energy = ani.energy - environmentConfig.energyActionTurn;
						checkOrganismEnergyNoLock(ani);
					} else if (ani.lastAction.equals(EnvironmentConfig.ACTION_TURN_LEFT)) {
						if (ani.rotation==0) {
							ani.rotation = 270;
						} else {
							ani.rotation = (ani.rotation - 90);
						}
						ani.energy = ani.energy - environmentConfig.energyActionTurn;
						checkOrganismEnergyNoLock(ani);
					} else if (ani.lastAction.equals(EnvironmentConfig.ACTION_BITE)) {
						Organism org = environmentState.getOrganismByPos(ani.getForwardPosX(),ani.getForwardPosY());
						if (org!=null) {
							int energy = 0;
							if (ani.herbivore && org instanceof Plant) {
								energy = environmentConfig.maxEnergyHerbivoreBite;
							} else if (!ani.herbivore && org instanceof Animal){
								Animal ani2 = (Animal) org;
								if (ani2.herbivore) {
									energy = environmentConfig.maxEnergyCarnivoreBite;
								}
							}
							if (energy>0) {
								org.energy = org.energy - energy;
								checkOrganismEnergyNoLock(org);
								ani.energy = ani.energy + energy;
								ani.score = ani.score + 1;
							}
						}
						ani.energy = ani.energy - environmentConfig.energyActionBite;
						checkOrganismEnergyNoLock(ani);
					}
				}
				return null;
			}
		};
		doLocked(this, code);
	}
	
	protected void checkOrganismEnergyNoLock(Organism org) {
		if (org.energy<=0) {
			org.dateTimeDied = System.currentTimeMillis();
			if (org instanceof Animal) {
				SimulatorAnimalWorker animalWorker = getAnimalWorkerByAnimalName(org.name);
				if (animalWorker!=null) {
					SimulatorAnimal simAni = animalWorker.getSimulatorAnimal();
					if (simAni!=null) {
						Animal ani = (Animal) org;
						if (ani.herbivore) {
							herbMutator.checkBest(ani,simAni);
						} else {
							boolean resetHerb = false;
							if (carnMutator.getTopScoringAnimal()==null) {
								resetHerb = true;
							}
							carnMutator.checkBest(ani,simAni);
							if (carnMutator.checkBest(ani,simAni) && resetHerb) {
								for (Animal herb: environmentState.getAnimals(true)) {
									herb.score = 0;
								}
								herbMutator.resetTopScoringAnimal();
							}
						}
						animalWorker.setSimulatorAnimal(null);
					}
				}
			}
			org.energy = 0;
		} else if (org instanceof Plant && org.energy>environmentConfig.maxEnergyPlant) {
			org.energy = environmentConfig.maxEnergyPlant;
		} else if (org instanceof Animal) {
			Animal ani = (Animal) org;
			if (ani.herbivore && org.energy>environmentConfig.maxEnergyHerbivore) {
				org.energy = environmentConfig.maxEnergyHerbivore;
			} else if (!ani.herbivore && org.energy>environmentConfig.maxEnergyCarnivore) {
				org.energy = environmentConfig.maxEnergyCarnivore;
			}
		}
	}

	protected boolean organismIsAliveNoLock(Organism org) {
		boolean r = true;
		if (org.dateTimeDied + (environmentConfig.deathDurationSeconds * 1000) > System.currentTimeMillis()) {
			r = false;
		}
		return r;
	}
	
	protected String getActionForActiveOutput(int i) {
		String r = "";
		if (i==AnimalConstants.OUT_BACK_ACTUATOR) {
			r = EnvironmentConfig.ACTION_MOVE_FORWARD;
		} else if (i==AnimalConstants.OUT_LEFT_ACTUATOR) {
			r = EnvironmentConfig.ACTION_TURN_RIGHT;
		} else if (i==AnimalConstants.OUT_RIGHT_ACTUATOR) {
			r = EnvironmentConfig.ACTION_TURN_LEFT;
		} else if (i==AnimalConstants.OUT_FRONT_MOUTH) {
			r = EnvironmentConfig.ACTION_BITE;
		}
		return r;
	}
}
