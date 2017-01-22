package nl.zeesoft.zodb.database;

import java.util.Date;
import java.util.Map.Entry;
import java.util.SortedMap;

import nl.zeesoft.zodb.Messenger;

public final class DbIndexWriteWorker extends DbWriteWorkerObject {
	private static final int				GET_MAX_FILES	= 100;
	
	private static DbIndexWriteWorker 		indexWorker		= null;

	private int								write			= 0;
	
	private DbIndexWriteWorker() {
		setSleep(100);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}
	
	protected static DbIndexWriteWorker getInstance() {
		if (indexWorker==null) {
			indexWorker = new DbIndexWriteWorker();
		}
		return indexWorker;
	}
	
	@Override
    public void stop() {
		super.stop();
		waitForStop(10,false);
		getChangedIndexFiles(Integer.MAX_VALUE);
    }
	
	@Override
	public void whileWorking() {
		getChangedIndexFiles(GET_MAX_FILES);
        write++;
        if ((write>=10) || (getWriteFiles().size()>=(GET_MAX_FILES * 5))) {
        	writeFiles(false);
        	write = 0;
        }
	}
	
	@Override
	protected void writingFilesDone() {
		if (DbConfig.getInstance().isDebugPerformance()) {
			Messenger.getInstance().debug(this, "Writing index files took: " + (new Date().getTime() - getStart().getTime()) + " ms");
		}
	}

	private void getChangedIndexFiles(int maxFiles) {
		Date start = null;
		if (DbConfig.getInstance().isDebugPerformance()) {
			start = new Date();
		}
		SortedMap<String,StringBuilder> addFiles = DbIndex.getInstance().getChangedIndexFiles(maxFiles,this);
        if (addFiles.size()>0) {
    		if (DbConfig.getInstance().isDebugPerformance()) {
    			Messenger.getInstance().debug(this, "Retrieving changed index files took: " + (new Date().getTime() - start.getTime()) + " ms");
    		}
        	for (Entry<String,StringBuilder> entry: addFiles.entrySet()) {
        		getWriteFiles().put(entry.getKey(),entry.getValue());
        	}
        }
	}
}	
