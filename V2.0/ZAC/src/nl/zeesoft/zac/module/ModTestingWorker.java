package nl.zeesoft.zac.module;

import nl.zeesoft.zac.database.model.Module;

public class ModTestingWorker extends ModWorker {
	private ModTester						tester					= null;
	private int								refresh					= 0;

	protected ModTestingWorker(Module module) {
		super(module);
		setSleep(10000);
	}
	
	@Override
	public void start() {
		if (tester==null) {
			tester = new ModTester(getModule());
		}
		tester.refreshTestSequences();
		super.start();
		//Messenger.getInstance().debug(this,"Started testing worker");
	}

	@Override
	public void stop() {
		super.stop();
		//Messenger.getInstance().debug(this,"Stopped testing worker");
	}

	@Override
	public void whileWorking() {
		tester.test();
		refresh++;
		if (refresh>6) {
			tester.refreshTestSequences();
			refresh = 0;
		}
	}
}
