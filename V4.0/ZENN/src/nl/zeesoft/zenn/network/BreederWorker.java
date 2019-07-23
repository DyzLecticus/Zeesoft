package nl.zeesoft.zenn.network;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class BreederWorker extends Worker {
	private Breeder 			breeder 		= null;
	private int					trainCycles		= 50;
	private int 				trainRepeat		= 20;
	
	private int					repeat			= 0;

	private	NN					nn				= null;
	private TestCycleSet 		tcs				= null;
	private TrainingProgram		tp				= null;

	public BreederWorker(Messenger msgr, WorkerUnion union, Breeder breeder) {
		super(msgr, union);
		this.breeder = breeder;
		setSleep(0);
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
		if (nn==null) {
			nn = breeder.getNewNN();
			tcs = breeder.getNewTestCycleSet();
			tp = breeder.getNewTrainingProgram(nn,tcs);
			tp.setTrainCycles(trainCycles);
		}
		nn = tp.runProgram();
		repeat++;
		if (repeat>=trainRepeat) {
			breeder.bredNN(nn,tp.getFinalResults());
			reset();
		}
	}

	private void reset() {
		repeat = 0;
		nn = null;
		nn = null;
		tcs = null;
	}
}
