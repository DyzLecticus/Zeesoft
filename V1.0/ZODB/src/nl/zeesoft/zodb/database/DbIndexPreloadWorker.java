package nl.zeesoft.zodb.database;

import java.util.Date;

import javax.xml.parsers.DocumentBuilder;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;
import nl.zeesoft.zodb.file.XMLConfig;
import nl.zeesoft.zodb.file.XMLFile;
import nl.zeesoft.zodb.model.MdlCollection;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.MdlObjectRefList;

public final class DbIndexPreloadWorker extends Worker {
	private static final int				MAX_PRELOAD			= 100;
	
	private MdlObjectRefList				preloadObjectList	= null;

	private MdlCollection					collection			= null;
	private boolean							backward			= false;
	private Date							started				= null;
	private int								preloaded			= 0;
	
	private	DocumentBuilder 				builder 			= null;
	
	protected DbIndexPreloadWorker() {
		builder = XMLConfig.getInstance().getBuilderForSource(this);
		setSleepBasedOnPreloadSpeed();
	}

	@Override
	public void start() {
		started	= new Date();
		preloaded = 0;
		if (collection==null) {
			Messenger.getInstance().error(this, "Unable to start preload worker: collection has not been set");
			return;
		}
		super.start();
    }
	
	@Override
    public void stop() {
		XMLConfig.getInstance().removeBuilderForSource(this);
		super.stop();
    }
	
	@Override
	public void whileWorking() {
		setSleepBasedOnPreloadSpeed();
		int size = getPreloadObjectList();
		if (size==0) {
			Messenger.getInstance().debug(this, "Preloaded " + preloaded + " " + collection.getName() + " objects in " + (new Date().getTime() - started.getTime()) + " ms");
			stop();
		} else {
			for (MdlObjectRef ref: preloadObjectList.getReferences()) {
				unserializeObject(ref,this,builder);
			}
			setPreloadObjectList(); 
		}
	}
	
	private int getPreloadObjectList() {
		//Date start = new Date();
		preloadObjectList = DbIndex.getInstance().getPreloadObjectList(collection,backward,MAX_PRELOAD,this);
		int size = preloadObjectList.getReferences().size();
        return size;
	}

	private void setPreloadObjectList() {
		//Date start = new Date();
		int done = DbIndex.getInstance().setPreloadObjectList(preloadObjectList, this);
        if (done>0) {
    		preloaded = preloaded + done;
        }
	}

	public static void unserializeObject(MdlObjectRef ref, Object source) {
		unserializeObject(ref,source,null);
	}

	public static void unserializeObject(MdlObjectRef ref, Object source, DocumentBuilder builder) {
		if (ref.getDataObject()==null) {
			XMLFile f = null;
			if (builder!=null) {
				f = new XMLFile(builder);
			} else {
				f = new XMLFile();
			}
			f.parseFile(ref.getFullFileName());
			if (f.getRootElement()!=null) {
				MdlDataObject o = MdlDataObject.fromXml(f);
				if (o!=null) {
					ref.setDataObject(o);
				}
			}
			f.cleanUp();
		}
	}

	/**
	 * @return the collection
	 */
	protected MdlCollection getCollection() {
		return collection;
	}

	/**
	 * @param collection the collection to set
	 */
	protected void setCollection(MdlCollection collection) {
		this.collection = collection;
	}

	/**
	 * @return the backward
	 */
	protected boolean isBackward() {
		return backward;
	}

	/**
	 * @param backward the backward to set
	 */
	protected void setBackward(boolean backward) {
		this.backward = backward;
	}

	private void setSleepBasedOnPreloadSpeed() {
		int sleep = 1000 - (DbConfig.getInstance().getPreloadSpeed() * 10);
		if (sleep<1) {
			sleep = 1;
		}
		setSleep(sleep);
	}
	
}	
