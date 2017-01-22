package nl.zeesoft.zals.simulator;

import nl.zeesoft.zals.database.model.Environment;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;

public class SimAnimalWorker extends Worker {
	private SimAnimal			animal					= null;

	protected SimAnimalWorker(SimAnimal animal,Environment env) {
		this.animal = animal;
		animal.updateEnvironment(env);
		resetSleep(env.getStatesPerSecond());
	}

	@Override
	public void start() {
		super.start();
		Messenger.getInstance().debug(this,"Started worker for animal: " + animal.getAnimal().getClass().getName() + ":" + animal.getAnimal().getId() + " (" + animal.getClass().getName()  + ")");
	 }

	@Override
	public void stop() {
		super.stop();
		waitForStop(10,false);
		Messenger.getInstance().debug(this,"Stopped worker for animal: " + animal.getAnimal().getClass().getName() + ":" + animal.getAnimal().getId());
	}
	
	@Override
	public void whileWorking() {
		animal.animate();
	}

	/**
	 * @return the animal
	 */
	public SimAnimal getAnimal() {
		return animal;
	}

	protected final void updateEnvironment(Environment env) {
		animal.updateEnvironment(env);
		resetSleep(env.getStatesPerSecond());
	}
	
	private void resetSleep(int statesPerSecond) {
		setSleep((1000 / statesPerSecond));
	}
}
