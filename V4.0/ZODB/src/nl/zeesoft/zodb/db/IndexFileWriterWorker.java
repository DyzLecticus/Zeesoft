package nl.zeesoft.zodb.db;

import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class IndexFileWriterWorker extends FileWriterWorkerObject {
	private static final int	MAX			= 10;
	
	protected IndexFileWriterWorker(Messenger msgr, WorkerUnion union, Index index) {
		super(msgr, union, index);
	}
	
	@Override
	protected int getMax() {
		return MAX;
	}

	@Override
	protected String getDirectory() {
		return getIndex().getFileDirectory();
	}
	
	@Override
	protected FileWriteWorkerObject getNewFileWriteWorker(Messenger msgr, WorkerUnion union,FileWriterWorkerObject writer, String fileName, List<IndexElement> elements, StringBuilder key) {
		return new IndexFileWriteWorker(msgr,union,this,fileName,elements,key);
	}

	@Override
	protected SortedMap<Integer, List<IndexElement>> getChangedFiles(int max) {
		return getIndex().getChangedIndexFiles(max);
	}
}
