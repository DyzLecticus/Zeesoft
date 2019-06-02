package nl.zeesoft.zodb.db;

import java.io.File;
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
		for (int num: files.keySet()) {
			String fileName = index.getFileDirectory() + num + ".txt";
			ZStringBuilder content = new ZStringBuilder();
			List<IndexElement> elements = files.get(num);
			if (elements.size()==0) {
				File file = new File(fileName);
				if (file.exists() && !file.delete()) {
					getMessenger().error(this,"Failed to delete file: " + fileName);
				}
			} else {
				for (IndexElement element: elements) {
					if (content.length()>0) {
						content.append("\n");
					}
					content.append(element.toStringBuilder(index.getKey()));
				}
				content.toFile(fileName);
			}
		}
	}
}
