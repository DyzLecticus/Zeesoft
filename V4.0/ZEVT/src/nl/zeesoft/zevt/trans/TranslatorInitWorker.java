package nl.zeesoft.zevt.trans;

import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zodb.Config;

public class TranslatorInitWorker extends Worker {
	private Translator	translator	= null;
	
	public TranslatorInitWorker(Config config,Translator t) {
		super(config.getMessenger(),config.getUnion());
		translator = t;
	}

	@Override
	public void whileWorking() {
		translator.initializeEntities();
		translator = null;
		stop();
	}
}
