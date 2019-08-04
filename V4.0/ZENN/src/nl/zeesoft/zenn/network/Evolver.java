package nl.zeesoft.zenn.network;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class Evolver extends Locker {
	private WorkerUnion					union					= null;
	
	private List<EvolverWorker>			workers					= new ArrayList<EvolverWorker>();
	private boolean						working					= false;

	private NN							baseNN					= null;
	private	TestCycleSet				baseTestCycleSet		= null;
	private	TrainingProgram				baseTrainingProgram		= null;
	
	private boolean						debug					= false;
	private float 						mutationPercentage		= 0.01F;
	private int 						trainRepeat				= 20;
	private int							trainCycles				= 50;
	private int							trainSleepMs			= 1000;
	
	private NN							bestSoFar				= null;
	private TestCycleSet				bestResults				= null;
	private SortedMap<TestCycleSet,NN>	viableNNs				= new TreeMap<TestCycleSet,NN>();

	public Evolver(Messenger msgr,WorkerUnion uni) {
		super(msgr);
		this.union = uni;
	}
	
	public void initialize(NN baseNN, TestCycleSet baseTestCycleSet) {
		initialize(baseNN,baseTestCycleSet,5);
	}
	
	public void initialize(NN baseNN, TestCycleSet baseTestCycleSet,int slots) {
		initialize(baseNN,baseTestCycleSet,new TrainingProgram(baseNN,baseTestCycleSet),slots);
	}

	public void initialize(NN baseNN, TestCycleSet baseTestCycleSet,TrainingProgram baseTrainingProgram,int slots) {
		if (slots<3) {
			slots = 3;
		}
		this.baseNN = baseNN;
		this.baseTestCycleSet = baseTestCycleSet;
		this.baseTrainingProgram = baseTrainingProgram;
		for (int i = 0; i < slots; i++) {
			workers.add(new EvolverWorker(getMessenger(),union,this));
		}
	}
	
	public void setDebug(boolean debug) {
		lockMe(this);
		this.debug = debug;
		unlockMe(this);
	}
	
	public void setMutationPercentage(float mutationPercentage) {
		lockMe(this);
		this.mutationPercentage = mutationPercentage;
		unlockMe(this);
	}

	public void setRepeatCycles(int trainRepeat, int trainCycles) {
		lockMe(this);
		this.trainRepeat = trainRepeat;
		this.trainCycles = trainCycles;
		unlockMe(this);
	}

	public void setSleepMs(int trainSleepMs) {
		lockMe(this);
		this.trainSleepMs = trainSleepMs;
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
		r = !working;
		unlockMe(this);
		if (r) {
			debug("Starting" + getTypeSafe() + " evolver ...");
			for (EvolverWorker worker: workers) {
				worker.start();
			}
			debug("Started" + getTypeSafe() + " evolver");
		}
		lockMe(this);
		working = r;
		unlockMe(this);
	}

	public void stop() {
		boolean r = false;
		lockMe(this);
		r = working;
		unlockMe(this);
		if (r) {
			for (EvolverWorker worker: workers) {
				worker.stop();
			}
			debug("Stopping" + getTypeSafe() + " evolver ...");
			for (EvolverWorker worker: workers) {
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
				}
			}
			debug("Stopped" + getTypeSafe() + " evolver");
		}
		lockMe(this);
		working = !r;
		unlockMe(this);
	}

	public NN getBestSoFar() {
		NN r = null;
		lockMe(this);
		r = getBestSoFarNoLock();
		unlockMe(this);
		return r;
	}

	public TestCycleSet getBestResults() {
		TestCycleSet r = null;
		lockMe(this);
		r = getBestResultsNoLock();
		unlockMe(this);
		return r;
	}
	
	protected NN getBestSoFarNoLock() {
		NN r = null;
		if (bestSoFar!=null) {
			r = bestSoFar.copy();
		}
		return r;
	}

	public TestCycleSet getBestResultsNoLock() {
		TestCycleSet r = null;
		if (bestResults!=null) {
			r = bestResults.copy();
		}
		return r;
	}
	
	protected void setBest(NN bestSoFar,TestCycleSet bestResults) {
		lockMe(this);
		this.bestSoFar = bestSoFar;
		this.bestResults = bestResults;
		debugBest(bestSoFar,bestResults,false);
		unlockMe(this);
	}

	protected String getTypeSafe() {
		String r = getType();
		if (r.length()>0) {
			r = " " + r;
		}
		return r;
	}

	protected String getType() {
		return "";
	}
	
	protected void trainedNN(NN nn,TestCycleSet finalResult,boolean success) {
		boolean selected = false;
		lockMe(this);
		if (bestSoFar==null || finalResult.compareTo(bestResults)>0) {
			if (success) {
				viableNNs.put(finalResult,nn);
			}
			if (bestSoFar!=null) {
				debugBest(nn,finalResult,success);
			}
			bestSoFar = nn;
			bestResults = finalResult;
			selected = true;
		} else if (bestSoFar!=nn && !viableNNs.containsValue(nn)) {
			nn.destroy();
		}
		unlockMe(this);
		if (selected) {
			selectedNN();
		}
	}
	
	protected void selectedNN() {
		// Override to implement
	}
	
	protected int getTrainRepeat() {
		int r = 0;
		lockMe(this);
		r = trainRepeat;
		unlockMe(this);
		return r;
	}
	
	protected int getTrainCycles() {
		int r = 0;
		lockMe(this);
		r = trainCycles;
		unlockMe(this);
		return r;
	}
	
	protected int getTrainSleepMs() {
		int r = 0;
		lockMe(this);
		r = trainSleepMs;
		unlockMe(this);
		return r;
	}
	
	protected TrainingProgram getNewTrainingProgram(EvolverWorker worker) {
		return baseTrainingProgram.copy(getNewNN(worker),getNewTestCycleSet());
	}
	
	protected NN getNewNN(EvolverWorker worker) {
		NN r = null;
		lockMe(this);
		if (bestSoFar!=null && workers.indexOf(worker)<((workers.size() / 3) * 2)) { 
			if (workers.indexOf(worker)==0) {
				r = bestSoFar.copy();
			} else {
				r = bestSoFar.copy(mutationPercentage);
			}
		}
		unlockMe(this);
		if (r==null) {
			r = getNewNN();
		}
		return r;
	}

	protected NN getNewNN() {
		return baseNN.copy(1.0F);
	}

	protected TestCycleSet getNewTestCycleSet() {
		return baseTestCycleSet.copy();
	}

	protected void debug(String msg) {
		if (debug && getMessenger()!=null) {
			getMessenger().debug(this,msg);
		}
	}
	
	protected void debugBest(NN nn,TestCycleSet best, boolean success) {
		String viable = "";
		if (success) {
			viable = " viable";
		}
		debug("Best" + viable + getTypeSafe() + " neural net so far: " + best.successes + "/" + best.cycles.size() + " " + best.averageError + " " + nn.getTrainedCycles());
	}
}
