package nl.zeesoft.zodb.db;

import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class IndexFileWriteWorker extends Worker {
	private	Index			index		= null;

	protected IndexFileWriteWorker(Messenger msgr, WorkerUnion union, Index index) {
		super(msgr, union);
		this.index = index;
		setSleep(100);
	}
	
	@Override
	public void whileWorking() {
		SortedMap<Integer,List<IndexElement>> files = index.getChangedFiles();
		if (files.size()>0) {
			for (int num: files.keySet()) {
				String fileName = index.getDirectory() + num + ".txt";
				ZStringBuilder content = new ZStringBuilder();
				for (IndexElement elem: files.get(num)) {
					if (content.length()>0) {
						content.append("\n");
					}
					content.append(elem.toStringBuilder());
				}
				content.toFile(fileName);
			}
			setSleep(1);
		} else {
			setSleep(100);
		}
	}
}
