package nl.zeesoft.zodb.database;

import java.util.Date;

import javax.xml.parsers.DocumentBuilder;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.file.XMLConfig;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.MdlObjectRefList;

public final class DbIndexLoadObjectWorker extends DbIndexLoadWorkerObject {
	public static final int					MAX_FETCH_WORKERS	= 10;
	
	private MdlObjectRefList				loadObjectList		= new MdlObjectRefList();
	private Date							started				= null;
	
	private	DocumentBuilder 				builder 			= null;
	
	protected DbIndexLoadObjectWorker() {
		setSleep(1);
	}

	@Override
	public void start() {
		setDone(false);
		started	= new Date();
		if (loadObjectList.getReferences().size()<=0) {
			setDone(true);
			return;
		}
		builder = XMLConfig.getInstance().getBuilderForSource(this);
		super.start();
    }
	
	@Override
    public void stop() {
		XMLConfig.getInstance().removeBuilderForSource(this);
		super.stop();
		Messenger.getInstance().debug(this, "Loaded " + loadObjectList.getReferences().size() + " objects in " + (new Date().getTime() - started.getTime()) + " ms");
    }
	
	@Override
	public void whileWorking() {
		for (MdlObjectRef ref: loadObjectList.getReferences()) {
			DbIndexPreloadWorker.unserializeObject(ref,this,builder);
		}
		stop();
		setDone(true);
	}

	/**
	 * @return the loadObjectList
	 */
	protected MdlObjectRefList getLoadObjectList() {
		return loadObjectList;
	}
}	
