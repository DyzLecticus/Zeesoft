package nl.zeesoft.zodb.db;

import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class IndexFileWriteWorker extends Worker {
	private static final int	MAX			= 10;
	
	private	Index				index		= null;

	protected IndexFileWriteWorker(Messenger msgr, WorkerUnion union, Index index) {
		super(msgr, union);
		this.index = index;
		setSleep(100);
	}
	
	@Override
	public void stop() {
		super.stop();
		waitForStop(10,false);
		SortedMap<Integer,List<IndexElement>> files = index.getChangedIndexFiles(0);
		if (files.size()>0) {
			getMessenger().debug(this,"Remaining index files: " + files.size());
			writeChangedFiles(files);
			getMessenger().debug(this,"Done");
		}
	}
	
	@Override
	public void whileWorking() {
		SortedMap<Integer,List<IndexElement>> files = index.getChangedIndexFiles(MAX);
		if (files.size()>0) {
			writeChangedFiles(files);
			if (files.size()==MAX) {
				setSleep(1);
			} else {
				setSleep(10);
			}
		} else {
			setSleep(100);
		}
	}
	
	protected void destroy() {
		if (isWorking()) {
			stop();
		}
		index = null;
	}
	
	private void writeChangedFiles(SortedMap<Integer,List<IndexElement>> files) {
		// TODO: Remove empty files
		for (int num: files.keySet()) {
			String fileName = index.getFileDirectory() + num + ".txt";
			ZStringBuilder content = new ZStringBuilder();
			for (IndexElement elem: files.get(num)) {
				if (content.length()>0) {
					content.append("\n");
				}
				content.append(elem.toStringBuilder(index.getKey()));
			}
			content.toFile(fileName);
		}
	}
}
