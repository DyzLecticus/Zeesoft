package nl.zeesoft.zenn.simulator;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.neural.Prediction;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zenn.environment.Animal;
import nl.zeesoft.zenn.environment.EnvironmentConfig;

public class SimulatorAnimalWorker extends Worker {
	private Animal				animal		= null;
	private Simulator			simulator	= null;
	
	private SimulatorAnimal		simAnimal	= null;

	protected SimulatorAnimalWorker(Messenger msgr, WorkerUnion union, Simulator sim,Animal ani,EnvironmentConfig environment) {
		super(msgr, union);
		this.simulator = sim;
		this.animal = ani;
		setSleep(1000 / environment.statesPerSecond);
	}

	protected String getAnimalName() {
		return animal.name;
	}

	protected void setSimulatorAnimal(SimulatorAnimal simAni) {
		lockMe(this);
		this.simAnimal = simAni;
		unlockMe(this);
	}

	protected boolean hasSimulatorAnimal() {
		boolean r = false;
		lockMe(this);
		r = this.simAnimal != null;
		unlockMe(this);
		return r;
	}

	protected void destroy() {
		lockMe(this);
		simulator = null;
		animal = null;
		unlockMe(this);
	}
	
	@Override
	public void stop() {
		super.stop();
	}
	
	public void waitForStopAndDestroy() {
		waitForStop(10,false);
		destroy();
	}
	
	@Override
	protected void whileWorking() {
		SimulatorAnimal simAni = null;
		lockMe(this);
		if (simAnimal!=null) {
			simAni = simAnimal;
		}
		unlockMe(this);
		if (simAni!=null) {
			Prediction p = simAni.neuralNet.getNewPrediction();
			simulator.setPredictionInputForAnimal(animal, p);
			simAni.neuralNet.predict(p);
			simulator.handlePredictionOutputForAnimal(animal, p);
		}
	}
}
