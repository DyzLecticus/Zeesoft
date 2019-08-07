package nl.zeesoft.zdk.genetic;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.neural.NeuralNet;
import nl.zeesoft.zdk.neural.TestSet;
import nl.zeesoft.zdk.neural.TrainingProgram;
import nl.zeesoft.zdk.thread.LockedCode;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class Evolver extends Locker implements JsAble {
	private WorkerUnion				union				= null;
	
	private TestSet					baseTestSet			= null;
	private List<EvolverWorker>		evolvers			= new ArrayList<EvolverWorker>();
	private GeneticNN				geneticNN			= null;
	
	private boolean					debug				= false;
	private float					mutationRate		= 0.05F;
	private int						trainEpochs			= 5000;
	private int						trainEpochSize		= 10;
	private int						evolverSleepMs		= 10;
	private boolean					working				= false;
	
	private EvolverUnit				bestSoFar			= null;
	
	public Evolver(Messenger msgr, WorkerUnion uni, int maxHiddenLayers, int maxHiddenNeurons, int codePropertyStart, TestSet baseTestSet,int evolvers) {
		super(msgr);
		union = uni;
		initialize(maxHiddenLayers,maxHiddenNeurons,codePropertyStart,baseTestSet,evolvers);
	}
	
	public void setDebug(boolean debug) {
		lockMe(this);
		this.debug = debug;
		unlockMe(this);
	}
	
	public void setMutationRate(float mutationRate) {
		lockMe(this);
		this.mutationRate = mutationRate;
		unlockMe(this);
	}
	
	public void setTrainEpochs(int trainEpochs) {
		lockMe(this);
		this.trainEpochs = trainEpochs;
		unlockMe(this);
	}
	
	public void setTrainEpochSize(int trainEpochSize) {
		lockMe(this);
		this.trainEpochSize = trainEpochSize;
		unlockMe(this);
	}
	
	public void setEvolverSleepMs(int evolverSleepMs) {
		lockMe(this);
		this.evolverSleepMs = evolverSleepMs;
		unlockMe(this);
	}

	public boolean isWorking() {
		boolean r = false;
		lockMe(this);
		r = working;
		unlockMe(this);
		return r;
	}

	public void start() {
		boolean r = false;
		lockMe(this);
		if (!working) {
			working = true;
			r = true;
		}
		unlockMe(this);
		if (r) {
			for (EvolverWorker evolver: evolvers) {
				evolver.start();
			}
		}
	}
	
	public void stop() {
		boolean r = false;
		lockMe(this);
		if (working) {
			working = false;
			r = true;
		}
		unlockMe(this);
		if (r) {
			for (EvolverWorker evolver: evolvers) {
				evolver.stop();
			}
		}
	}

	public void whileStopping() {
		whileStopping(10);
	}

	public void whileStopping(int timeoutSeconds) {
		long started = System.currentTimeMillis();
		for (EvolverWorker worker: evolvers) {
			while(worker.isWorking()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					if (getMessenger()!=null) {
						getMessenger().error(this,"Waiting for evolver worker to finish was interrupted",e);
					} else {
						e.printStackTrace();
					}
				}
				long now = System.currentTimeMillis();
				if (now - started > timeoutSeconds * 1000) {
					break;
				}
			}
			long now = System.currentTimeMillis();
			if (now - started > timeoutSeconds * 1000) {
				break;
			}
		}
	}
	
	public EvolverUnit getBestSoFar() {
		EvolverUnit r = null;
		lockMe(this);
		r = bestSoFar.copy();
		unlockMe(this);
		return r;
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		lockMe(this);
		json.rootElement.children.add(new JsElem("mutationRate","" + mutationRate));
		json.rootElement.children.add(new JsElem("trainEpochs","" + trainEpochs));
		json.rootElement.children.add(new JsElem("trainEpochSize","" + trainEpochSize));
		json.rootElement.children.add(new JsElem("evolverSleepMs","" + evolverSleepMs));

		if (bestSoFar!=null) {
			JsElem bestElem = new JsElem("bestSoFar",true);
			json.rootElement.children.add(bestElem);
			bestElem.children.add(bestSoFar.toJson().rootElement);
		}
		unlockMe(this);
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			LockedCode code = new LockedCode() {
				@Override
				public Object doLocked() {
					Evolver evolver = (Evolver) this.param1;
					JsFile json = (JsFile) this.param2;
					mutationRate = json.rootElement.getChildFloat("mutationRate",mutationRate);
					trainEpochs = json.rootElement.getChildInt("trainEpochs",trainEpochs);
					trainEpochSize = json.rootElement.getChildInt("trainEpochSize",trainEpochSize);
					evolverSleepMs = json.rootElement.getChildInt("evolverSleepMs",evolverSleepMs);
					
					JsElem bestSoFarElem = json.rootElement.getChildByName("bestSoFar");
					if (bestSoFarElem!=null && bestSoFarElem.children.size()>0) {
						bestSoFar = new EvolverUnit();
						JsFile js = new JsFile();
						js.rootElement = bestSoFarElem.children.get(0);
						bestSoFar.fromJson(js,evolver);
						if (bestSoFar.code==null || bestSoFar.neuralNet==null || bestSoFar.trainingProgram==null) {
							bestSoFar = null;
						}
					}
					return null;
				}
			};
			code.param1 = this;
			code.param2 = json;
			doLocked(this,code);
		}
	}
	
	protected EvolverUnit getNewEvolverUnit(EvolverWorker evolver) {
		EvolverUnit r = new EvolverUnit();
		GeneticCode code = null;
		if (evolvers.indexOf(evolver)<=(evolvers.size() / 2)) {
			lockMe(this);
			if (bestSoFar!=null) {
				code = new GeneticCode();
				code.setCode(bestSoFar.code.getCode());
				code.mutate(mutationRate);
			}
			unlockMe(this);
		}
		lockMe(this);
		geneticNN.generateNewNN(code);
		r.code = geneticNN.code;
		r.neuralNet = geneticNN.neuralNet;
		r.trainingProgram = getNewTrainingProgram(r.neuralNet,baseTestSet.copy());
		unlockMe(this);
		return r;
	}

	protected TrainingProgram getNewTrainingProgram(NeuralNet nn, TestSet baseTs) {
		return new TrainingProgram(nn,baseTs);
	}
	
	protected TrainingProgram getNewTrainingProgram(NeuralNet nn) {
		TrainingProgram r =  new TrainingProgram(nn,null);
		r.baseTestSet = baseTestSet.copy();
		return r;
	}
	
	protected int getTrainEpochs() {
		int r = 0;
		lockMe(this);
		r = trainEpochs;
		unlockMe(this);
		return r;
	}
	
	protected int getTrainEpochSize() {
		int r = 0;
		lockMe(this);
		r = trainEpochSize;
		unlockMe(this);
		return r;
	}
	
	protected int getEvolverSleepMs() {
		int r = 0;
		lockMe(this);
		r = evolverSleepMs;
		unlockMe(this);
		return r;
	}
	
	protected void finishedTrainigProgram(EvolverWorker worker,EvolverUnit unit) {
		if (unit.trainingProgram.latestResults.success) {
			lockMe(this);
			if (bestSoFar==null || unit.compareTo(bestSoFar)>0) {
				bestSoFar = unit;
				if (debug && getMessenger()!=null) {
					getMessenger().debug(this,"Selected new best genetic neural net;\n" + unit.toStringBuilder());
				}
			}
			unlockMe(this);
		}
	}
	
	protected void initialize(int maxHiddenLayers, int maxHiddenNeurons,int codePropertyStart,TestSet baseTestSet,int evolvers) {
		if (maxHiddenLayers<1) {
			maxHiddenLayers = 1;
		}
		if (maxHiddenNeurons<2) {
			maxHiddenLayers = 2;
		}
		if (evolvers < 2) {
			evolvers = 2;
		}
		this.baseTestSet = baseTestSet;
		for (int i = 0; i < evolvers; i++) {
			this.evolvers.add(new EvolverWorker(getMessenger(),union,this));
		}
		geneticNN = new GeneticNN(baseTestSet.inputNeurons,maxHiddenLayers,maxHiddenNeurons,baseTestSet.outputNeurons,codePropertyStart);
	}
}
