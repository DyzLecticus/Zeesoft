package nl.zeesoft.zenn.network;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class Evolver extends Locker {
	private List<EvolverWorker>		workers					= new ArrayList<EvolverWorker>();
	private NN						baseNN					= null;
	private	TestCycleSet			baseTestCycleSet		= null;
	private	TrainingProgram			baseTrainingProgram		= null;
	
	private boolean					debug					= false;
	private float 					mutationPercentage		= 0.01F;
	private int 					trainRepeat				= 20;
	private int						trainCycles				= 50;
	private int						trainSleepMs			= 1000;
	
	private NN						bestSoFar				= null;
	private TestCycleSet			bestResults				= null;

	public Evolver(Messenger msgr,WorkerUnion uni,NN baseNN, TestCycleSet baseTestCycleSet,int slots) {
		super(msgr);
		init(uni,baseNN,baseTestCycleSet,new TrainingProgram(baseNN,baseTestCycleSet),slots);
	}

	public Evolver(Messenger msgr,WorkerUnion uni,NN baseNN, TestCycleSet baseTestCycleSet,TrainingProgram baseTrainingProgram,int slots) {
		super(msgr);
		init(uni,baseNN,baseTestCycleSet,baseTrainingProgram,slots);
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

	public void start() {
		for (EvolverWorker worker: workers) {
			worker.start();
		}
	}

	public void stop() {
		for (EvolverWorker worker: workers) {
			worker.stop();
		}
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
	}

	public NN getBestSoFar() {
		NN r = null;
		lockMe(this);
		r = bestSoFar;
		unlockMe(this);
		return r;
	}

	public TestCycleSet getBestResults() {
		TestCycleSet r = null;
		lockMe(this);
		r = bestResults;
		unlockMe(this);
		return r;
	}
	
	protected void trainedNN(NN nn,TestCycleSet finalResult) {
		lockMe(this);
		if (bestSoFar==null || finalResult.successes>bestResults.successes) {
			bestSoFar = nn;
			bestResults = finalResult;
		}
		if (debug && getMessenger()!=null) {
			String improved = "";
			if (nn==bestSoFar) {
				improved = "!";
			}
			getMessenger().debug(this,"Trained neural net: " + finalResult.successes + "/" + finalResult.cycles.size() + improved);
		}
		unlockMe(this);
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
		if (bestSoFar!=null && workers.indexOf(worker)<workers.size()/2) {
			r = bestSoFar.copy(mutationPercentage);
		}
		unlockMe(this);
		if (r==null) {
			r = baseNN.copy(1.0F);
		}
		return r;
	}
	
	protected TestCycleSet getNewTestCycleSet() {
		return baseTestCycleSet.copy();
	}

	private void init(WorkerUnion uni,NN baseNN, TestCycleSet baseTestCycleSet,TrainingProgram baseTrainingProgram,int slots) {
		if (slots<3) {
			slots = 3;
		}
		this.baseNN = baseNN;
		this.baseTestCycleSet = baseTestCycleSet;
		this.baseTrainingProgram = baseTrainingProgram;
		for (int i = 0; i < slots; i++) {
			workers.add(new EvolverWorker(getMessenger(),uni,this));
		}
	}
}
