package nl.zeesoft.zodb.db;

import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class IndexObjectWriterWorker extends FileWriterWorkerObject {
	private static final int	MAX			= 1000;
	
	protected IndexObjectWriterWorker(Messenger msgr, WorkerUnion union,Index index) {
		super(msgr, union, index);
	}
	
	@Override
	protected int getMax() {
		return MAX;
	}

	@Override
	protected String getDirectory() {
		return getIndex().getObjectDirectory();
	}

	@Override
	protected FileWriteWorkerObject getNewFileWriteWorker(Messenger msgr,WorkerUnion union,FileWriterWorkerObject writer,int fileNum,String directory,List<IndexElement> elements,StringBuilder key) {
		return new IndexObjectWriteWorker(msgr,union,this,fileNum,directory,elements,key);
	}

	@Override
	protected SortedMap<Integer, List<IndexElement>> getChangedFiles(int max) {
		return getIndex().getChangedDataFiles(max,getWriting());
	}
}
