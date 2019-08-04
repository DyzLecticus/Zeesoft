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
		setSleep(evolver.getEvolverSleepMs());
	}

	@Override
	protected void whileWorking() {
		setSleep(evolver.getEvolverSleepMs());
		if (unit==null) {
			unit = evolver.getNewEvolverUnit(this);
		}
		unit.trainingProgram.train(evolver.getTrainEpochSize());
		if ((unit.trainingProgram.stopOnSuccess && unit.trainingProgram.latestResults.success) || 
			(unit.trainingProgram.trainedEpochs >= evolver.getTrainEpochs() * evolver.getTrainEpochSize())
			) {
			evolver.finishedTrainigProgram(this,unit);
			unit = null;
		}
	}
}
