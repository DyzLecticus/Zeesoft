package nl.zeesoft.zodb.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;
import nl.zeesoft.zodb.client.ClConfig;
import nl.zeesoft.zodb.file.FileObject;
import nl.zeesoft.zodb.file.XMLFile;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.MdlObjectRefList;
import nl.zeesoft.zodb.model.impl.DbUser;

public final class DbIndexSaveWorker extends Worker {
	private static final int				MAX_FILES		= 10;
	private static final int				MAX_OBJECTS		= (5 * DbIndex.BLOCK_SIZE);
	
	private static DbIndexSaveWorker 		indexWorker		= null;

	private SortedMap<String,StringBuffer> 	writeFiles 		= new TreeMap<String,StringBuffer>();
	private SortedMap<Long,MdlObjectRef> 	writeObjects 	= new TreeMap<Long,MdlObjectRef>();
	private SortedMap<Long,MdlObjectRef> 	removeObjects 	= new TreeMap<Long,MdlObjectRef>();
	private int								write			= 0;
	private int								files			= 0;
	
	private DbIndexSaveWorker() {
		setSleep(100);
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}
	public static DbIndexSaveWorker getInstance() {
		if (indexWorker==null) {
			indexWorker = new DbIndexSaveWorker();
		}
		return indexWorker;
	}
	
	@Override
	public void start() {
		Messenger.getInstance().debug(this, "Starting index save worker ...");
		super.start();
		Messenger.getInstance().debug(this, "Started index save worker");
    }
	
	@Override
    public void stop() {
		Messenger.getInstance().debug(this, "Stopping index save worker ...");
		super.stop();
		while(isWorking()) {
			sleep(10);
		}
		getIndexChanges(Integer.MAX_VALUE,Integer.MAX_VALUE);
        writeFiles();
		Messenger.getInstance().debug(this, "Stopped index save worker");
    }
	
	@Override
	public void whileWorking() {
		files++;
		if (files>=100) {
			files = 0;
			getIndexChanges(MAX_FILES,MAX_OBJECTS);
		} else { 
			getIndexChanges(1,MAX_OBJECTS);
		}
        write++;
        if ((write>=10) || (writeFiles.size()>=10) || (writeObjects.size()>=DbIndex.BLOCK_SIZE) || (removeObjects.size()>=DbIndex.BLOCK_SIZE)) {
        	writeFiles();
        	write = 0;
        }
	}
	
	private void getIndexChanges(int maxFiles, int maxObjects) {
		Date start = new Date();
        if (DbIndex.getInstance().addChangesToFileWorker(maxFiles,maxObjects,this)) {
        	Messenger.getInstance().debug(this, "Retrieving index changes took: " + (new Date().getTime() - start.getTime()) + " ms");
        }
	}

	public void addWriteFile(String fileName,StringBuffer content) {
		writeFiles.put(fileName, content);
	}

	protected void addWriteObject(MdlObjectRef ref) {
		writeObjects.put(ref.getId().getValue(),ref);
	}
	
	protected void addRemoveObject(MdlObjectRef ref) {
		removeObjects.put(ref.getId().getValue(),ref);
		writeObjects.remove(ref.getId().getValue());
	}

	private void writeFiles() {
		Date start = new Date();
		List<DbIndexSaveWorkerObject> saveWorkers = new ArrayList<DbIndexSaveWorkerObject>();
        if (writeFiles.size()>0) {
	        for (Entry<String,StringBuffer> entry: writeFiles.entrySet()) {
	        	DbIndexSaveFileWorker worker = new DbIndexSaveFileWorker();
	        	worker.setFileName(entry.getKey());
	        	worker.setContent(entry.getValue());
	        	worker.start();
	        	saveWorkers.add(worker);
	        }
        }
        if (writeObjects.size()>0) {
        	MdlObjectRefList refs = new MdlObjectRefList();
        	int added = 0;
	        for (Entry<Long,MdlObjectRef> entry: writeObjects.entrySet()) {
	        	refs.getReferences().add(entry.getValue());
	        	added++;
	        	if ((added % DbIndex.BLOCK_SIZE) == 0) {
	            	DbIndexSaveObjectWorker worker = new DbIndexSaveObjectWorker();
	            	worker.setReferences(refs);
	            	worker.start();
	            	saveWorkers.add(worker);
	            	refs = new MdlObjectRefList();
	            	added = 0;
	        	}
			}
	        if (added>0) {
	        	DbIndexSaveObjectWorker worker = new DbIndexSaveObjectWorker();
	        	worker.setReferences(refs);
	        	worker.start();
	        	saveWorkers.add(worker);
	        }
        }
        if (removeObjects.size()>0) {
			Date rStart = new Date();
			String fileName = "";
			for (Entry<Long,MdlObjectRef> entry: removeObjects.entrySet()) {
				fileName = entry.getValue().getFullFileName();
				try {
					if (FileObject.fileExists(fileName)) {
						FileObject.deleteFile(fileName);
					}
				} catch (IOException e) {
					Messenger.getInstance().error(this, "Unable to remove file: " + fileName + ", error: " + e.getMessage());
				}
			}
        	Messenger.getInstance().debug(this, "Removing objects files took: " + (new Date().getTime() - rStart.getTime()) + " ms");
        	removeObjects.clear();
        }
        if (saveWorkers.size()>0) {
	        boolean interrupted = false;
	        for (DbIndexSaveWorkerObject worker: saveWorkers) {
	        	while (!worker.isDone()) {
	        		try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						interrupted = true;
						Messenger.getInstance().error(this, "A save worker was interrupted");
					}
	        	}
	        }
	        if (!interrupted) {
	        	writeFiles.clear();
	        	writeObjects.clear();
	        	Messenger.getInstance().debug(this, "Writing index changes took: " + (new Date().getTime() - start.getTime()) + " ms");
	        }
        }
	}

	protected static void serializeObject(MdlObjectRef ref, Object source) {
		MdlDataObject obj = ref.getDataObject();
		if (obj!=null) {
			XMLFile f = MdlDataObject.toXml(obj);
			if (f!=null) {
				StringBuffer xml = null;
				if (DbConfig.getInstance().isDebug()) {
					xml = f.toStringReadFormat();
				} else {
					xml = f.toStringBuffer();
				}
				f.writeFile(obj.getFullFileName(), xml);
				if (
					(obj.getId().getValue()==1) &&
					(obj instanceof DbUser)
					) {
					DbUser admin = (DbUser) obj;
					ClConfig.getInstance().setUserName(admin.getName().getValue());
					ClConfig.getInstance().setUserPassword(new StringBuffer(admin.getPassword().getValue()));
					ClConfig.getInstance().serialize();
				}
			}
			f.cleanUp();
		}
	}

}	
