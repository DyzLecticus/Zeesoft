package nl.zeesoft.zevt.trans;

import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zodb.Config;

public class TranslatorRefreshWorker extends Worker {
	private Translator	translator	= null;
	
	public TranslatorRefreshWorker(Config config,Translator t) {
		super(config.getMessenger(),config.getUnion());
		super.setSleep(3600000);
		translator = t;
	}

	@Override
	public void stop() {
		super.stop();
		waitForStop(10,false);
		translator = null;
	}
	
	@Override
	public void whileWorking() {
		translator.refreshEntities();
	}
}
