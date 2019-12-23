package nl.zeesoft.zsda.grid;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.grid.ZGridFactory;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zodb.db.init.Persistable;

public class PersistableZGridFactory extends ZGridFactory implements Persistable {
	public PersistableZGridFactory(Messenger msgr, WorkerUnion uni) {
		super(msgr,uni);
	}
	
	@Override
	public ZStringBuilder getObjectName() {
		return new ZStringBuilder("ZGridFactory");
	}
}
