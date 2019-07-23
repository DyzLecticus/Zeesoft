package nl.zeesoft.zenn.network;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public abstract class Breeder extends Locker {
	private List<BreederWorker>		workers				= new ArrayList<BreederWorker>();
	
	private NN						bestSoFar			= null;
	private TestCycleSet			bestResults			= null;
	
	public Breeder(Messenger msgr,WorkerUnion uni,int width) {
		super(msgr);
		for (int i = 0; i < width; i++) {
			workers.add(new BreederWorker(msgr,uni,this));
		}
	}
	
	public void start() {
		for (BreederWorker worker: workers) {
			worker.start();
		}
	}

	public void stop() {
		for (BreederWorker worker: workers) {
			worker.stop();
		}
		for (BreederWorker worker: workers) {
			while(worker.isWorking()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					if (getMessenger()!=null) {
						getMessenger().error(this,"Waiting for breeder worker to finish was interrupted",e);
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
	
	protected void bredNN(NN nn,TestCycleSet finalResult) {
		lockMe(this);
		if (bestSoFar==null || finalResult.successes>bestResults.successes) {
			bestSoFar = nn;
			bestResults = finalResult;
		}
		unlockMe(this);
	}
	
	protected abstract NN getNewNN();
	protected abstract TestCycleSet getNewTestCycleSet();
	protected abstract TrainingProgram getNewTrainingProgram(NN nn,TestCycleSet tc);
}
