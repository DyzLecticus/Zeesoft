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

/**
 * An evolver can be used to generate neural nets according to a certain specification.
 * It uses multi threading to use processing power effectively.
 * When specifying multiple evolvers, half of them are used to generate completely new neural nets.
 * The other half are used to generate mutations of the best-so-far generated neural net.  
 */
public class Evolver extends Locker implements JsAble {
	private WorkerUnion				union				= null;
	
	private TestSet					baseTestSet			= null;
	private List<EvolverWorker>		evolvers			= new ArrayList<EvolverWorker>();
	private GeneticNN				geneticNN			= null;
	
	private boolean					debug				= false;
	private int						maxLogLines			= 20;
	private float					mutationRate		= 0.05F;
	private int						trainEpochBatches	= 5000;
	private int						trainEpochBatchSize	= 10;
	private float					checkFactorQuarter	= 0.66F;
	private float					checkFactorHalf		= 0.33F;
	private int						sleepMs				= 10;
	private int						sleepMsFoundBest	= 50;
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
	
	public void setMaxLogLines(int maxLogLines) {
		lockMe(this);
		this.maxLogLines = maxLogLines;
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
	
	public void setCheckFactorQuarter(float checkFactorQuarter) {
		lockMe(this);
		this.checkFactorQuarter = checkFactorQuarter;
		unlockMe(this);
	}
	
	public void setCheckFactorHalf(float checkFactorHalf) {
		lockMe(this);
		this.checkFactorHalf = checkFactorHalf;
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

	public boolean isWorking() {
		boolean r = false;
		lockMe(this);
		r = working;
		unlockMe(this);
		return r;
	}

	public void start() {
		if (!baseTestSet.isConsistent()) {
			getMessenger().error(this,"Unable to start evolver; test set is inconsistent");
		} else {
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
					getMessenger().debug(this,"Started");
				}
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
			getMessenger().debug(this,"Stopped");
		}
	}
	
	public void setGeneticNN(GeneticNN geneticNN) {
		lockMe(this);
		this.geneticNN = geneticNN.copy();
		unlockMe(this);
	}
	
	public GeneticNN getGeneticNN() {
		GeneticNN r = null;
		lockMe(this);
		r = geneticNN.copy();
		unlockMe(this);
		return r;
	}

	public void setBestSoFar(EvolverUnit unit) {
		setBestSoFar(unit,false);
	}

	public boolean setBestSoFarIfBetter(EvolverUnit unit) {
		return setBestSoFar(unit,unit!=null);
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
				json.rootElement.children.add(new JsElem("checkFactorQuarter","" + checkFactorQuarter));
				json.rootElement.children.add(new JsElem("checkFactorHalf","" + checkFactorHalf));
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
					JsFile json = (JsFile) this.param1;
					mutationRate = json.rootElement.getChildFloat("mutationRate",mutationRate);
					trainEpochBatches = json.rootElement.getChildInt("trainEpochBatches",trainEpochBatches);
					trainEpochBatchSize = json.rootElement.getChildInt("trainEpochBatchSize",trainEpochBatchSize);
					checkFactorQuarter = json.rootElement.getChildFloat("checkFactorQuarter",checkFactorQuarter);
					checkFactorHalf = json.rootElement.getChildFloat("checkFactorHalf",checkFactorHalf);
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
						bestSoFar.fromJson(js);
					}
					return null;
				}
			};
			code.param1 = json;
			doLocked(this,code);
		}
	}
	
	protected void selectedBest() {
		// Override to implement
	}

	protected boolean setBestSoFar(EvolverUnit unit,boolean ifBetter) {
		boolean r = false;
		lockMe(this);
		if (unit==null || bestSoFar==null || (!ifBetter || unit.compareTo(bestSoFar)>0)) {
			bestSoFar = unit;
			r = true;
			if (bestSoFar!=null) {
				this.geneticNN = unit.geneticNN.copy();
				if (debug && getMessenger()!=null) {
					getMessenger().debug(this,"Set new best genetic neural net;\n" + unit.toStringBuilder());
				}
				addLogLine(bestSoFar,"SET");
			}
		}
		unlockMe(this);
		return r;
	}
	
	protected EvolverUnit getNewEvolverUnit(EvolverWorker evolver) {
		GeneticCode gCode = null;
		if (evolvers.size()==1 || evolvers.indexOf(evolver)<=(evolvers.size() / 2)) {
			lockMe(this);
			if (bestSoFar!=null) {
				gCode = new GeneticCode();
				gCode.setCode(bestSoFar.geneticNN.code.getCode());
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
				r.geneticNN = geneticNN.copy();
				r.trainingProgram = getNewTrainingProgram(r.geneticNN.neuralNet,baseTestSet.copy());
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
	
	protected float getCheckFactorQuarter() {
		float r = 0;
		lockMe(this);
		r = checkFactorQuarter;
		unlockMe(this);
		return r;
	}
	
	protected float getCheckFactorHalf() {
		float r = 0;
		lockMe(this);
		r = checkFactorHalf;
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
				addLogLine(bestSoFar,"SEL");
				selected = true;
				stop = unit.trainingProgram.trainedEpochs == 0;
			}
			unlockMe(this);
		}
		if (selected) {
			selectedBest();
		}
		if (stop) {
			stop();
			whileStopping();
		}
	}
	
	protected void addLogLine(EvolverUnit unit,String action) {
		if (maxLogLines>0) {
			logLines.add(unit.toLogLine(action));
			while(logLines.size()>maxLogLines) {
				logLines.remove(0);
			}
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
		geneticNN = new GeneticNN();
		geneticNN.initialize(baseTestSet.inputNeurons,maxHiddenLayers,maxHiddenNeurons,baseTestSet.outputNeurons,codePropertyStart);
	}
}
