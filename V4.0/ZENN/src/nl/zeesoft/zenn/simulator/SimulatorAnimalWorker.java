package nl.zeesoft.zenn.simulator;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zenn.environment.Animal;

public class SimulatorAnimalWorker extends Worker {
	private Animal				animal		= null;
	private Simulator			simulator	= null;
	
	private SimulatorAnimal		simAnimal	= null;

	protected SimulatorAnimalWorker(Messenger msgr, WorkerUnion union, Simulator sim,Animal ani) {
		super(msgr, union);
		this.simulator = sim;
		this.animal = ani;
	}
	
	public void setSimulatorAnimal(SimulatorAnimal simAni) {
		lockMe(this);
		this.simAnimal = simAni;
		unlockMe(this);
	}
	

	protected void destroy() {
		simulator = null;
		animal = null;
	}
	
	@Override
	public void stop() {
		super.stop();
		waitForStop(10,false);
		destroy();
	}
	
	@Override
	protected void whileWorking() {
		// TODO Auto-generated method stub
	}
}
