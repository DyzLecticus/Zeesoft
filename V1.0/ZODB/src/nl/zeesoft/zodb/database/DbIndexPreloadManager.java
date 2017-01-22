package nl.zeesoft.zodb.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;
import nl.zeesoft.zodb.file.FileObject;
import nl.zeesoft.zodb.file.XMLElem;
import nl.zeesoft.zodb.file.XMLFile;
import nl.zeesoft.zodb.model.MdlCollection;

public final class DbIndexPreloadManager extends Worker {
	private static final int				NUM_PRELOADERS			= 16;
	
	private static DbIndexPreloadManager 	preloadManager			= null;
	
	private List<MdlCollection>				preloadCollections		= new ArrayList<MdlCollection>();
	private List<MdlCollection>				todoCollections			= new ArrayList<MdlCollection>();
	private List<MdlCollection>				doneCollections			= new ArrayList<MdlCollection>();
	
	private List<DbIndexPreloadWorker>		workers					= new ArrayList<DbIndexPreloadWorker>();
	
	private	int								checkCollectionList		= 0;
	
	private DbIndexPreloadManager() {
		setSleep(100);
		for (int i = 0; i < NUM_PRELOADERS; i++) {
			DbIndexPreloadWorker worker = new DbIndexPreloadWorker(); 
			workers.add(worker);
		}
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}
	public static DbIndexPreloadManager getInstance() {
		if (preloadManager==null) {
			preloadManager = new DbIndexPreloadManager();
		}
		return preloadManager;
	}

	@Override
	public void start() {
		Messenger.getInstance().debug(this, "Starting index preload manager ...");
		super.start();
		Messenger.getInstance().debug(this, "Started index preload manager");
    }
	
	@Override
    public void stop() {
		Messenger.getInstance().debug(this, "Stopping index preload manager ...");
		super.stop();
		for (DbIndexPreloadWorker worker: workers) {
			if (worker.isWorking()) {
				worker.stop();
				while (worker.isWorking()) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// Ignore
					}
				}
			}
		}
		Messenger.getInstance().debug(this, "Stopped index preload manager");
    }
	
	@Override
	public void whileWorking() {
		checkCollectionList++;
		if (checkCollectionList>=100) {
			checkCollectionList = 0;
			checkSavePreloadCollections();
		}
		if (setWorkerCollectionsAndStartReturnDone()) {
			checkSavePreloadCollections();
			stop();
		}
	}

	private void checkSavePreloadCollections() {
		List<MdlCollection> newList = getPreloadCollectionList(); 
		if (newList!=null) {
			preloadCollections = newList;
			todoCollections.clear();
			for (MdlCollection col: preloadCollections) {
				if (!doneCollections.contains(col)) {
					todoCollections.add(col);
				}
			}
			serializePreloadCollections();
		}
	}

	private boolean setWorkerCollectionsAndStartReturnDone() {
		boolean done = true;

		List<DbIndexPreloadWorker> freeWorkers = new ArrayList<DbIndexPreloadWorker>();
		for (DbIndexPreloadWorker worker: workers) {
			if (!worker.isWorking()) {
				freeWorkers.add(worker);
			} else {
				done = false;
			}
		}

		if ((freeWorkers.size()>=2) && (todoCollections.size()>0)) {
			MdlCollection col = todoCollections.remove(0);
			freeWorkers.get(0).setCollection(col);
			freeWorkers.get(0).setBackward(false);
			freeWorkers.get(0).start();
			freeWorkers.get(1).setCollection(col);
			freeWorkers.get(1).setBackward(true);
			freeWorkers.get(1).start();
			doneCollections.add(col);
			done = false;
		}

		return done;
	}
	
	private List<MdlCollection> getPreloadCollectionList() {
		Date start = new Date();
		List<MdlCollection> newList = DbIndex.getInstance().getPreloadCollectionList(this); 
		if (newList!=null) {
			int size = newList.size();
	        if (size>0) {
	        	Messenger.getInstance().debug(this, "Retrieved preload collections: " + size + " in " + (new Date().getTime() - start.getTime()) + " ms");
	        }
		}
        return newList;
	}

	protected void unserializePreloadCollections() {
		preloadCollections.clear();
		todoCollections.clear();
		if (FileObject.fileExists(getFileName())) {
			XMLFile f = new XMLFile();
			f.parseFile(getFileName());
			for (XMLElem colElem: f.getRootElement().getChildren()) {
				MdlCollection col = DbConfig.getInstance().getModel().getCollectionByName(colElem.getValue().toString());
				if (col!=null) {
					preloadCollections.add(col);
					todoCollections.add(col);
				}
			}
			f.cleanUp();
		} else {
			serializePreloadCollections();
		}
	}

	private void serializePreloadCollections() {
		if (preloadCollections.size()<=0) {
			for (MdlCollection col: DbConfig.getInstance().getModel().getCollections()) {
				if (!col.isCacheable()) {
					preloadCollections.add(col);
					todoCollections.add(col);
				}
			}
			for (MdlCollection col: DbConfig.getInstance().getModel().getCollections()) {
				if (col.isCacheable()) {
					preloadCollections.add(col);
					todoCollections.add(col);
				}
			}
		}
		XMLFile f = new XMLFile();
		f.setRootElement(new XMLElem("collections",null, null));
		for (MdlCollection col: preloadCollections) {
			new XMLElem("name",new StringBuffer(col.getName()),f.getRootElement());
		}
		f.writeFile(getFileName(),f.toStringReadFormat());
		f.cleanUp();
	}
	
	private String getFileName() {
		return DbConfig.getInstance().getFullIndexDir() + "DbPreloadOrder.xml";
	}
	
	/**
	 * @return the preloadCollections
	 */
	protected List<MdlCollection> getPreloadCollections() {
		return new ArrayList<MdlCollection>(preloadCollections);
	}
}	
