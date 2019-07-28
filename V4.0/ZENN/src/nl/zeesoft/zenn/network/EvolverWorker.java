package nl.zeesoft.zenn.network;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class EvolverWorker extends Worker {
	private Evolver 			evolver 		= null;
	private int 				trainRepeat		= 20;
	private int					trainCycles		= 50;
	
	private int					repeat			= 0;

	private TrainingProgram		tp				= null;

	protected EvolverWorker(Messenger msgr, WorkerUnion union, Evolver evolver) {
		super(msgr, union);
		this.evolver = evolver;
	}
	
	@Override
	public void start() {
		if (!isWorking()) {
			reset();
			super.start();
		}
	}
	
	@Override
	protected void whileWorking() {
		if (tp==null) {
			tp = evolver.getNewTrainingProgram(this);
			tp.setTrainCycles(trainCycles);
		}
		NN nn = tp.runProgram();
		repeat++;
		if (repeat>=trainRepeat) {
			if (nn!=null) {
				evolver.trainedNN(nn,tp.getFinalResults(),tp.isSuccess());
			}
			reset();
		}
	}

	private void reset() {
		trainCycles = evolver.getTrainCycles();
		trainRepeat = evolver.getTrainRepeat();
		setSleep(evolver.getTrainSleepMs());
		repeat = 0;
		tp = null;
	}
}
