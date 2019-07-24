package nl.zeesoft.zenn.animal;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zenn.network.Evolver;

public class AnimalEvolver extends Evolver {
	private boolean		herbivore	= true;
	
	public AnimalEvolver(Messenger msgr,WorkerUnion uni,boolean herbivore) {
		super(msgr,uni);
		this.herbivore = herbivore;
		AnimalNN nn = new AnimalNN();
		nn.initialize();
		AnimalTestCycleSet tcs = new AnimalTestCycleSet();
		tcs.initialize(nn, herbivore);
		initialize(nn,tcs,3);
	}
	
	@Override
	protected String getType() {
		String r = "herbivore";
		if (!herbivore) {
			r = "carnivore";
		}
		return r;
	}
}
