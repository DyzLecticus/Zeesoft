package nl.zeesoft.zac.module;

import nl.zeesoft.zac.database.model.Module;

public class ModTrainingWorker extends ModWorker {
	private ModTrainer						trainer					= null;
	private int								refresh					= 0;

	protected ModTrainingWorker(Module module) {
		super(module);
		setSleep(1000);
	}
	
	@Override
	public void start() {
		if (trainer==null) {
			trainer = new ModTrainer(getModule());
			trainer.initialize();
		}
		trainer.refreshTrainingSequences();
		super.start();
		//Messenger.getInstance().debug(this,"Started training worker");
	}

	@Override
	public void stop() {
		super.stop();
		waitForStop(10,false);
		//Messenger.getInstance().debug(this,"Stopped training worker");
	}

	@Override
	public void whileWorking() {
		trainer.train();
		refresh++;
		if (refresh>60) {
			trainer.pruneLinks();
			trainer.updateLinks();
			trainer.refreshTrainingSequences();
			refresh = 0;
		}
	}
}
