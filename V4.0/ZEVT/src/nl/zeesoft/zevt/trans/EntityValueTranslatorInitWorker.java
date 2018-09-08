package nl.zeesoft.zevt.trans;

import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zodb.Config;

public class EntityValueTranslatorInitWorker extends Worker {
	private EntityValueTranslator	translator					= null;
	
	public EntityValueTranslatorInitWorker(Config config,EntityValueTranslator t) {
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
