package nl.zeesoft.zdk.genetic;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
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
	private boolean					appendLog			= true;
	private float					mutationRate		= 0.05F;
	private int						trainEpochBatches	= 5000;
	private int						trainEpochBatchSize	= 10;
	private int						sleepMs				= 10;
	private int						sleepMsFoundBest	= 50;
	private boolean					stopIfFoundBest		= false;
	private boolean					working				= false;
	
	private List<ZStringBuilder>	logLines			= new ArrayList<ZStringBuilder>();
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
	
	public void setAppendLog(boolean appendLog) {
		lockMe(this);
		this.appendLog = appendLog;
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
	
	public void setSleepMs(int sleepMs) {
		lockMe(this);
		this.sleepMs = sleepMs;
		unlockMe(this);
	}

	public void setSleepMsFoundBest(int sleepMsFoundBest) {
		lockMe(this);
		this.sleepMsFoundBest = sleepMsFoundBest;
		unlockMe(this);
	}

	public void setStopIfFoundBest(boolean stopIfFoundBest) {
		lockMe(this);
		this.stopIfFoundBest = stopIfFoundBest;
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
		boolean dbg = false;
		lockMe(this);
		if (!working) {
			working = true;
			r = true;
			dbg = debug;
		}
		unlockMe(this);
		if (r) {
			for (EvolverWorker evolver: evolvers) {
				evolver.start();
			}
			if (dbg && getMessenger()!=null) {
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
		boolean dbg = false;
		lockMe(this);
		dbg = debug;
		unlockMe(this);
		if (dbg && getMessenger()!=null) {
			getMessenger().debug(this,"Stopped evolver");
		}
	}
	
	public void setBase(ZStringBuilder code,int codePropertyStart,NeuralNet nn) {
		lockMe(this);
		geneticNN.code.setCode(code);
		geneticNN.codePropertyStart = codePropertyStart;
		geneticNN.neuralNet = nn;
		
		EvolverUnit unit = new EvolverUnit();
		unit.code = geneticNN.code;
		unit.codePropertyStart = geneticNN.codePropertyStart;
		unit.neuralNet = geneticNN.neuralNet;
		unit.trainingProgram = getNewTrainingProgram(unit.neuralNet,baseTestSet.copy());
		bestSoFar = unit;
		boolean start = !working && stopIfFoundBest;
		unlockMe(this);
		if (start) {
			start();
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
				json.rootElement.children.add(new JsElem("sleepMs","" + sleepMs));
				json.rootElement.children.add(new JsElem("sleepMsFoundBest","" + sleepMsFoundBest));

				JsElem logElem = new JsElem("log",true);
				json.rootElement.children.add(logElem);
				for (ZStringBuilder line: logLines) {
					logElem.children.add(new JsElem(null,line,true));
				}
				
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
					sleepMs = json.rootElement.getChildInt("sleepMs",sleepMs);
					sleepMsFoundBest = json.rootElement.getChildInt("sleepMsFoundBest",sleepMsFoundBest);
					
					logLines.clear();
					JsElem logElem = json.rootElement.getChildByName("log");
					if (logElem!=null) {
						for (JsElem lineElem: logElem.children) {
							logLines.add(lineElem.value);
						}
					}
					
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
		if (evolvers.size()==1 || evolvers.indexOf(evolver)<=(evolvers.size() / 2)) {
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
				r.codePropertyStart = geneticNN.codePropertyStart;
				r.neuralNet = geneticNN.neuralNet;
				r.trainingProgram = getNewTrainingProgram(r.neuralNet,baseTestSet.copy());
				// Skip if initial results are not satisfactory
				if (bestSoFar!=null && r.compareTo(bestSoFar) < 0) {
					r = null;
				}
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
		TrainingProgram r =  new TrainingProgram(nn);
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
	
	protected int getEvolverWorkerSleepMs() {
		int r = 0;
		lockMe(this);
		if (bestSoFar==null) {
			r = sleepMs;
		} else {
			r = sleepMsFoundBest;
		}
		unlockMe(this);
		return r;
	}
	
	protected void finishedTrainigProgram(EvolverWorker worker,EvolverUnit unit) {
		boolean selected = false;
		boolean stop = false;
		if (unit.trainingProgram.latestResults.success) {
			lockMe(this);
			if (bestSoFar==null || unit.compareTo(bestSoFar)>0) {
				bestSoFar = unit;
				if (debug && getMessenger()!=null) {
					getMessenger().debug(this,"Selected new best genetic neural net;\n" + unit.toStringBuilder());
				}
				if (appendLog) {
					logLines.add(0,bestSoFar.toLogLine());
				}
				selected = true;
				stop = stopIfFoundBest;
			}
			unlockMe(this);
		}
		if (selected) {
			if (stop) {
				stop();
			}
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
		if (evolvers < 1) {
			evolvers = 1;
		}
		this.baseTestSet = baseTestSet;
		for (int i = 0; i < evolvers; i++) {
			this.evolvers.add(new EvolverWorker(getMessenger(),union,this));
		}
		geneticNN = new GeneticNN(baseTestSet.inputNeurons,maxHiddenLayers,maxHiddenNeurons,baseTestSet.outputNeurons,codePropertyStart);
	}
}
