package nl.zeesoft.zdk.genetic;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class EvolverWorker extends Worker {
	private Evolver		evolver				= null;

	private EvolverUnit unit				= null;
	private int			trainEpochBatches	= 0;
	private int			trainEpochBatchSize	= 0;
	private float		checkFactorQuarter	= 0F;
	private float		checkFactorHalf		= 0F;
	
	protected EvolverWorker(Messenger msgr, WorkerUnion union, Evolver evolver) {
		super(msgr, union);
		this.evolver = evolver;
	}
	
	protected void start(int sleepMs,int trainEpochBatches,int trainEpochBatchSize,float checkFactorQuarter,float checkFactorHalf) {
		setSleep(sleepMs);
		lockMe(this);
		this.trainEpochBatches = trainEpochBatches;
		this.trainEpochBatchSize = trainEpochBatchSize;
		this.checkFactorQuarter = checkFactorQuarter;
		this.checkFactorHalf = checkFactorHalf;
		unlockMe(this);
		start();
	}
	
	protected void setSleepMs(int sleepMs) {
		setSleep(sleepMs);
	}

	@Override
	protected void whileWorking() {
		if (unit==null) {
			unit = evolver.getNewEvolverUnit(this);
		}
		if (unit!=null) {
			unit.trainingProgram.train(trainEpochBatchSize);
			if ((unit.trainingProgram.stopOnSuccess && unit.trainingProgram.latestResults.success) || 
				(unit.trainingProgram.trainedEpochs >= trainEpochBatches * trainEpochBatchSize) ||
				!lossCheckQuarter() ||
				!lossCheckHalf()
				) {
				evolver.finishedTrainigProgram(this,unit);
				unit = null;
			}
		}
	}
	
	private boolean lossCheckQuarter() {
		return lossCheck(4,checkFactorQuarter);
	}
	
	private boolean lossCheckHalf() {
		return lossCheck(2,checkFactorHalf);
	}
	
	private boolean lossCheck(int div,float factor) {
		boolean r = true;
		if (unit.trainingProgram.trainedEpochs >= Math.round(trainEpochBatches / div) * trainEpochBatchSize) {
			r = unit.trainingProgram.lossCheck(factor);
		}
		return r;
	}
}
