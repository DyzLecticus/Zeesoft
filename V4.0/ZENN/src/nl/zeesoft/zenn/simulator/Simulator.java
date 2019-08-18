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
import nl.zeesoft.zenn.animal.CarnivoreEvolver;
import nl.zeesoft.zenn.animal.HerbivoreEvolver;
import nl.zeesoft.zenn.environment.Animal;
import nl.zeesoft.zenn.environment.EnvironmentConfig;
import nl.zeesoft.zenn.environment.EnvironmentState;
import nl.zeesoft.zenn.environment.Organism;
import nl.zeesoft.zenn.environment.Plant;
import nl.zeesoft.zodb.Config;

public class Simulator extends Locker {
	private WorkerUnion					union				= null;
	private EnvironmentInitializer		envInitializer		= null;
	private SimulatorAnimalInitializer	simAniInitializer	= null;
	private HerbivoreEvolver			herbEvolver			= null;
	private CarnivoreEvolver			carnEvolver			= null;
	private SimulatorWorker				worker				= null;

	private EnvironmentConfig			environmentConfig	= null;
	private EnvironmentState			environmentState	= null;

	private List<SimulatorAnimalWorker>	animalWorkers		= new ArrayList<SimulatorAnimalWorker>();
	private boolean						working				= false;
	
	private long						energyUpdated		= 0;
	private long						stateUpdated		= 0;
	
	public Simulator(Config config,EnvironmentInitializer envInit,SimulatorAnimalInitializer simAniInit,HerbivoreEvolver herbEvo,CarnivoreEvolver carnEvo) {
		super(config.getMessenger());
		union = config.getUnion();
		envInitializer = envInit;
		simAniInitializer = simAniInit;
		herbEvolver = herbEvo;
		carnEvolver = carnEvo;
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
		if (environmentConfig!=null && environmentState!=null && !working && !worker.isWorking()) {
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
						SimulatorAnimal simAni = new SimulatorAnimal();
						simAni.name = ani.name;
						simAni.code = bestSoFar.code;
						simAni.neuralNet = bestSoFar.neuralNet;
						simAniInitializer.addOrReplaceObject(simAni);
						//System.out.println("Set new simulation animal: " + ani.name + " " + simAni);
						lockMe(this);
						if (ani.energy==0) {
							environmentState.initializeAnimal(ani);
						}
						unlockMe(this);
						animalWorker.setSimulatorAnimal(simAni);
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
	
	protected void setPredictionInputForAnimal(Animal ani,Prediction p) {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				if (working && organismIsAliveNoLock(ani)) {
					p.inputs[AnimalConstants.IN_RANDOM] = Math.round(ZRandomize.getRandomFloat(0,1));
					for (int i = 0; i < 3; i++) {
						//System.out.println("Animal: " + ani.name + " take a look, step " + (i + 1));
						int posX = ani.posX;
						int posY = ani.posY;
						for (int d = 0; d <= 3; d++) {
							if (ani.rotation==0) {
								if (i==0) {
									posX--;
								} else if (i==1) {
									posY++;
								} else if (i==2) {
									posX++;
								}
							} else if (ani.rotation==90) {
								if (i==0) {
									posY++;
								} else if (i==1) {
									posX++;
								} else if (i==2) {
									posY--;
								}
							} else if (ani.rotation==180) {
								if (i==0) {
									posX++;
								} else if (i==1) {
									posY--;
								} else if (i==2) {
									posX--;
								}
							} else if (ani.rotation==270) {
								if (i==0) {
									posY--;
								} else if (i==1) {
									posX--;
								} else if (i==2) {
									posY++;
								}
							}
							float[] color = null;
							if (posX<0||posX>=EnvironmentConfig.SIZE_X ||
								posY<0||posX>=EnvironmentConfig.SIZE_Y
								) {
								color = AnimalConstants.COLOR_GREY;
							} else {
								Organism org = environmentState.getOrganismByPos(posX, posY);
								if (org!=null && !organismIsAliveNoLock(org)) {
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
								//System.out.println(i + " " + color[0] + " " +  + color[1] + " " + color[2]);
								for (int c = 0; c < color.length; c++) {
									p.inputs[1 + (i * 3) + c] = color[c] * AnimalConstants.INTENSITIES[d];
								}
								break;
							}
						}
					}
					ani.energy = ani.energy - environmentConfig.energyActionLook;
					checkOrganismEnergyNoLock(ani);
					//System.out.println("Animal: " + ani.name + " took a look: " + ani.energy);
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
					for (int i = 0; i < p.outputs.length; i++) {
						int rounded = Math.round(p.outputs[i]);
						if (rounded==1) {
							if (i==AnimalConstants.OUT_BACK_ACTUATOR) {
								//System.out.println("Animal: " + ani.name + " take action: MOVE FORWARD " + ani.posX + "," + ani.posY);
								int newX = ani.getForwardPosX();
								int newY = ani.getForwardPosY();
								if (newX>=0 && newX<EnvironmentConfig.SIZE_X &&
									newY>=0 && newY<EnvironmentConfig.SIZE_Y
									) {
									Organism org = environmentState.getOrganismByPos(newX,newY);
									//System.out.println("Animal: " + ani.name + " take action: MOVE FORWARD " + ani.posX + "," + ani.posY + " -> " + newX + "," + newY + "? org: " + org);
									if (org==null) {
										ani.posX = newX;
										ani.posY = newY;
									}
								}
								ani.energy = ani.energy - environmentConfig.energyActionMove;
								checkOrganismEnergyNoLock(ani);
								//System.out.println("Animal: " + ani.name + " took action: MOVE FORWARD " + ani.posX + "," + ani.posY);
							} else if (i==AnimalConstants.OUT_LEFT_ACTUATOR) {
								//System.out.println("Animal: " + ani.name + " take action: TURN RIGHT");
								ani.rotation = (ani.rotation + 90) % 360;
								ani.energy = ani.energy - environmentConfig.energyActionTurn;
								checkOrganismEnergyNoLock(ani);
							} else if (i==AnimalConstants.OUT_RIGHT_ACTUATOR) {
								//System.out.println("Animal: " + ani.name + " take action: TURN LEFT");
								if (ani.rotation==0) {
									ani.rotation = 270;
								} else {
									ani.rotation = (ani.rotation - 90);
								}
								ani.energy = ani.energy - environmentConfig.energyActionTurn;
								checkOrganismEnergyNoLock(ani);
							} else if (i==AnimalConstants.OUT_FRONT_MOUTH) {
								//System.out.println("Animal: " + ani.name + " take action: BITE");
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
										//System.out.println("Animal: " + ani.name + " took action: BITE");
										org.energy = org.energy - energy;
										checkOrganismEnergyNoLock(org);
										ani.energy = ani.energy + energy;
										ani.score = ani.score + 1;
									}
								}
								ani.energy = ani.energy - environmentConfig.energyActionBite;
								checkOrganismEnergyNoLock(ani);
							}
							break;
						}
					}
					//System.out.println("Animal: " + ani.name + " took action: " + ani.energy);
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
					animalWorker.setSimulatorAnimal(null);
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
}
