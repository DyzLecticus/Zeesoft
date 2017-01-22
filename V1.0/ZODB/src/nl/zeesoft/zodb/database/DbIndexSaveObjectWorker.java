package nl.zeesoft.zodb.database;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.MdlObjectRefList;

public final class DbIndexSaveObjectWorker extends DbIndexSaveWorkerObject {
	private MdlObjectRefList				references		= null;
	
	@Override
	public void start() {
		if ((references!=null) && (references.getReferences().size()>0)) {
			super.start();
		} else {
			Messenger.getInstance().error(this, "Unable to start save object worker because object reference list is not set or size = 0");
			setDone(true);
		}
    }
	
	@Override
	public void whileWorking() {
		for (MdlObjectRef reference: references.getReferences()) {
			DbIndexSaveWorker.serializeObject(reference, this);
		}
		stop();
		setDone(true);
	}

	/**
	 * @param references the references to set
	 */
	protected void setReferences(MdlObjectRefList references) {
		this.references = references;
	}
	
}	
