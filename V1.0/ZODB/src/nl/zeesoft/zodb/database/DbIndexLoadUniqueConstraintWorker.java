package nl.zeesoft.zodb.database;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.model.MdlCollectionUniqueConstraint;

public final class DbIndexLoadUniqueConstraintWorker extends DbIndexLoadWorkerObject {
	private List<MdlCollectionUniqueConstraint>		uniqueConstraints	= null;

	private Date									started				= null;
	
	protected DbIndexLoadUniqueConstraintWorker() {
		setSleep(1);
	}
	
	@Override
	public void start() {
		if (uniqueConstraints==null) {
			Messenger.getInstance().error(this, "Unable to start load worker: unique constraints have not been set");
			return;
		}
		started = new Date();
		super.start();
    }
	
	@Override
	public void whileWorking() {
		for (MdlCollectionUniqueConstraint uc: uniqueConstraints) {
			uc.unserialize();
		}
		Messenger.getInstance().debug(this, "Unserialized " + uniqueConstraints.size() + " unique constraint indexes in " + (new Date().getTime() - started.getTime()) + " ms");
		stop();
		setDone(true);
	}

	/**
	 * @param uniqueConstraints the uniqueConstraints to set
	 */
	protected void setUniqueConstraints(List<MdlCollectionUniqueConstraint> uniqueConstraints) {
		this.uniqueConstraints = uniqueConstraints;
	}

}	
