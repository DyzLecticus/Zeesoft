package nl.zeesoft.zals.simulator;

import nl.zeesoft.zac.database.model.Module;
import nl.zeesoft.zac.database.model.SymbolSequenceTraining;
import nl.zeesoft.zac.module.ModTrainer;
import nl.zeesoft.zac.module.confabulate.ConInputOutput;
import nl.zeesoft.zac.module.confabulate.ConModuleInstance;
import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.database.DbRequestQueue;

public class SimAnimalModule extends Locker {
	private Module				module				= null;

	private ConModuleInstance 	instance			= null;
	private ModTrainer			trainer				= null;
	private boolean				trained				= false;
	
	protected SimAnimalModule(Module mod) {
		this.module = mod;
	}

	protected void initialize() {
		lockMe(this);
		trainer = new ModTrainer(module);
		instance = trainer.getNewConfabulatorInstance();
		unlockMe(this);
	}

	protected final void updateMaxSequenceDistance(int maxSequenceDistance) {
		lockMe(this);
		if (module.getMaxSequenceDistance()!=maxSequenceDistance) {
			module.setMaxSequenceDistance(maxSequenceDistance);
			DbRequestQueue.getInstance().addRequest(module.getNewUpdateRequest(null),this);
		}
		unlockMe(this);
	}

	protected final void update() {
		lockMe(this);
		if (trained) {
			trainer.pruneLinks();
			trainer.updateLinks();
			trained = false;
		}
		unlockMe(this);
	}
	
	protected final void reinitialize() {
		lockMe(this);
		trainer.reinitialize();
		instance = trainer.getNewConfabulatorInstance();
		unlockMe(this);
	}

	protected final void remove() {
		lockMe(this);
		trainer.removeLinks();
		unlockMe(this);
	}

	public void confabulateExtend(ConInputOutput io){
		lockMe(this);
		instance.confabulateExtend(io);
		unlockMe(this);
	}
	
	public void trainSequence(SymbolSequenceTraining sequence,boolean update,int weight) {
		lockMe(this);
		trainer.trainSequence(sequence, update, weight);
		trained = true;
		unlockMe(this);
	}
}
