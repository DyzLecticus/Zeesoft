package nl.zeesoft.zdk.genetic;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class EvolverWorker extends Worker {
	private Evolver		evolver		= null;

	private EvolverUnit unit		= null;
	
	public EvolverWorker(Messenger msgr, WorkerUnion union, Evolver evolver) {
		super(msgr, union);
		this.evolver = evolver;
		setSleep(evolver.getEvolverWorkerSleepMs());
	}

	@Override
	protected void whileWorking() {
		setSleep(evolver.getEvolverWorkerSleepMs());
		if (unit==null) {
			unit = evolver.getNewEvolverUnit(this);
		}
		if (unit!=null) {
			int trainEpochBatches = evolver.getTrainEpochBatches();
			int trainEpochBatchSize = evolver.getTrainEpochBatchSize();
			unit.trainingProgram.train(trainEpochBatchSize);
			if ((unit.trainingProgram.stopOnSuccess && unit.trainingProgram.latestResults.success) || 
				(unit.trainingProgram.trainedEpochs >= trainEpochBatches * trainEpochBatchSize) ||
				!lossCheckPointQuarter(trainEpochBatches,trainEpochBatchSize,evolver.getCheckFactorQuarter()) ||
				!lossCheckPointHalf(trainEpochBatches,trainEpochBatchSize,evolver.getCheckFactorHalf())
				) {
				evolver.finishedTrainigProgram(this,unit);
				unit = null;
			}
		}
	}
	
	private boolean lossCheckPointQuarter(int trainEpochBatches, int trainEpochBatchSize, float factor) {
		return lossCheckPoint(trainEpochBatches,trainEpochBatchSize,4,factor);
	}
	
	private boolean lossCheckPointHalf(int trainEpochBatches, int trainEpochBatchSize,float factor) {
		return lossCheckPoint(trainEpochBatches,trainEpochBatchSize,2,factor);
	}
	
	private boolean lossCheckPoint(int trainEpochBatches, int trainEpochBatchSize,int div,float factor) {
		boolean r = true;
		if (unit.trainingProgram.trainedEpochs == Math.round(trainEpochBatches / div) * trainEpochBatchSize) {
			r = unit.trainingProgram.lossCheckPoint(factor);
		}
		return r;
	}
}
