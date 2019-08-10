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
	private int						trainEpochBatches	= 5000;
	private int						trainEpochBatchSize	= 10;
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
	
	public void setTrainEpochBatches(int trainEpochBatches) {
		lockMe(this);
		this.trainEpochBatches = trainEpochBatches;
		unlockMe(this);
	}
	
	public void setTrainEpochBatchSize(int trainEpochBatchSize) {
		lockMe(this);
		this.trainEpochBatchSize = trainEpochBatchSize;
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
			if (debug && getMessenger()!=null) {
				getMessenger().debug(this,"Started evolver");
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
		if (debug && getMessenger()!=null) {
			getMessenger().debug(this,"Stopped evolver");
		}
	}
	
	public EvolverUnit getBestSoFar() {
		EvolverUnit r = null;
		lockMe(this);
		if (bestSoFar!=null) {
			r = bestSoFar.copy();
		}
		unlockMe(this);
		return r;
	}

	@Override
	public JsFile toJson() {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				JsFile json = new JsFile();
				json.rootElement = new JsElem();
				json.rootElement.children.add(new JsElem("mutationRate","" + mutationRate));
				json.rootElement.children.add(new JsElem("trainEpochBatches","" + trainEpochBatches));
				json.rootElement.children.add(new JsElem("trainEpochBatchSize","" + trainEpochBatchSize));
				json.rootElement.children.add(new JsElem("evolverSleepMs","" + evolverSleepMs));

				if (bestSoFar!=null) {
					JsElem bestElem = new JsElem("bestSoFar",true);
					json.rootElement.children.add(bestElem);
					bestElem.children.add(bestSoFar.toJson().rootElement);
				}
				return json;
			}
		};
		JsFile json = (JsFile) doLocked(this,code);
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
					trainEpochBatches = json.rootElement.getChildInt("trainEpochBatches",trainEpochBatches);
					trainEpochBatchSize = json.rootElement.getChildInt("trainEpochBatchSize",trainEpochBatchSize);
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
	
	protected void selectedBest() {
		// Override to implement
	}
	
	protected EvolverUnit getNewEvolverUnit(EvolverWorker evolver) {
		GeneticCode gCode = null;
		if (evolvers.indexOf(evolver)<=(evolvers.size() / 2)) {
			lockMe(this);
			if (bestSoFar!=null) {
				gCode = new GeneticCode();
				gCode.setCode(bestSoFar.code.getCode());
				gCode.mutate(mutationRate);
			}
			unlockMe(this);
		}
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				GeneticCode code = (GeneticCode) param1;
				EvolverUnit r = new EvolverUnit();
				geneticNN.generateNewNN(code);
				r.code = geneticNN.code;
				r.neuralNet = geneticNN.neuralNet;
				r.trainingProgram = getNewTrainingProgram(r.neuralNet,baseTestSet.copy());
				return r;
			}
		};
		code.param1 = gCode;
		EvolverUnit r = (EvolverUnit) doLocked(this,code);
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
	
	protected int getTrainEpochBatches() {
		int r = 0;
		lockMe(this);
		r = trainEpochBatches;
		unlockMe(this);
		return r;
	}
	
	protected int getTrainEpochBatchSize() {
		int r = 0;
		lockMe(this);
		r = trainEpochBatchSize;
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
		boolean selected = false;
		if (unit.trainingProgram.latestResults.success) {
			lockMe(this);
			if (bestSoFar==null || unit.compareTo(bestSoFar)>0) {
				bestSoFar = unit;
				if (debug && getMessenger()!=null) {
					getMessenger().debug(this,"Selected new best genetic neural net;\n" + unit.toStringBuilder());
				}
				selected = true;
			}
			unlockMe(this);
		}
		if (selected) {
			selectedBest();
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
