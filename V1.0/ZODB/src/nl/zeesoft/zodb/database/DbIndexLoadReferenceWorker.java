package nl.zeesoft.zodb.database;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.model.MdlCollectionReference;

public final class DbIndexLoadReferenceWorker extends DbIndexLoadWorkerObject {
	private List<MdlCollectionReference>			references			= null;

	private Date									started				= null;
	
	protected DbIndexLoadReferenceWorker() {
		setSleep(1);
	}
	
	@Override
	public void start() {
		if (references==null) {
			Messenger.getInstance().error(this, "Unable to start load worker: references have not been set");
			return;
		}
		started = new Date();
		super.start();
    }
	
	@Override
    public void stop() {
		super.stop();
    }
	
	@Override
	public void whileWorking() {
		for (MdlCollectionReference r: references) {
			r.unserialize();
		}
		Messenger.getInstance().debug(this, "Unserialized " + references.size() + " reference indexes in " + (new Date().getTime() - started.getTime()) + " ms");
		stop();
		setDone(true);
	}

	/**
	 * @param references the references to set
	 */
	protected void setReferences(List<MdlCollectionReference> references) {
		this.references = references;
	}
}	
