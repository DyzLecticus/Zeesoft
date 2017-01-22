package nl.zeesoft.zodb.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.index.IdxClass;
import nl.zeesoft.zodb.file.xml.XMLFile;

public final class DbDataObjectWriteWorker extends DbWriteWorkerObject {
	private static final int	GET_MAX_FILES	= 1000;

	private IdxClass			classIndex		= null;
	
	private List<Long>			writtenIdList	= new ArrayList<Long>();
	private List<Long>			deletedIdList	= new ArrayList<Long>();
	
	protected DbDataObjectWriteWorker(IdxClass idx) {
		setSleep(100);
		classIndex = idx;
	}
	
	@Override
    public void stop() {
		super.stop();
		while(isWorking()) {
			sleep(1);
		}
		getChangedObjects(Integer.MAX_VALUE);
    }
	
	@Override
	public void whileWorking() {
		getChangedObjects(GET_MAX_FILES);
    	writeFiles(false);
	}
	
	@Override
	protected void writingFilesDone() {
		classIndex.removeWrittenDataObjects(writtenIdList,this);
		classIndex.removeRemovedIds(deletedIdList,this);
		if (DbConfig.getInstance().isDebugPerformance()) {
			if (writtenIdList.size()>0 && deletedIdList.size()>0) {
				Messenger.getInstance().debug(this, "Writing " + writtenIdList.size() + " and deleting " + deletedIdList.size() + " " + classIndex.getCls().getFullName() + " objects took: " + (new Date().getTime() - getStart().getTime()) + " ms");
			} else if (writtenIdList.size()>0) {
				Messenger.getInstance().debug(this, "Writing " + writtenIdList.size() + " " + classIndex.getCls().getFullName() + " objects took: " + (new Date().getTime() - getStart().getTime()) + " ms");
			} else if (deletedIdList.size()>0) {
				Messenger.getInstance().debug(this, "Deleting " + deletedIdList.size() + " " + classIndex.getCls().getFullName() + " objects took: " + (new Date().getTime() - getStart().getTime()) + " ms");
			}
		}
	}

	private void getChangedObjects(int max) {
		Date start = null;
		if (DbConfig.getInstance().isDebugPerformance()) {
			start = new Date();
		}
		writtenIdList.clear();
		deletedIdList.clear();
		SortedMap<Long,DbDataObject> changedObjects = classIndex.getChangedDataObjects(max,this);
		List<Long> removedIdList = classIndex.getRemovedIdList(max,this);
        if (changedObjects.size()>0 || removedIdList.size()>0) {
    		if (DbConfig.getInstance().isDebugPerformance()) {
    			Messenger.getInstance().debug(this, "Retrieving changed and deleted " + classIndex.getCls().getFullName() + " objects took: " + (new Date().getTime() - start.getTime()) + " ms");
    		}
        	for (Entry<Long,DbDataObject> entry: changedObjects.entrySet()) {
        		writtenIdList.add(entry.getKey());
        		String fileName = DbConfig.getInstance().getDataDir() + classIndex.getCls().getFullName() + "/" + entry.getKey() + ".xml";
        		XMLFile xml = entry.getValue().toXML(classIndex.getCls(),true);
        		getWriteFiles().put(fileName,xml.toStringBuilder());
        		xml.cleanUp();
        	}
        	for (long id: removedIdList) {
        		deletedIdList.add(id);
        		String fileName = DbConfig.getInstance().getDataDir() + classIndex.getCls().getFullName() + "/" + id + ".xml";
        		getWriteFiles().put(fileName,new StringBuilder());
        	}
        }
	}
}	
