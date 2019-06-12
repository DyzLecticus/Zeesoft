package nl.zeesoft.zodb.db;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class IndexFileWriteWorker extends FileWriteWorkerObject {
	protected IndexFileWriteWorker(Messenger msgr,WorkerUnion union,FileWriterWorkerObject writer,int fileNum,String directory,List<IndexElement> elements,StringBuilder key) {
		super(msgr,union,writer,fileNum,directory,elements,key);
	}

	@Override
	protected ZStringBuilder getData() {
		ZStringBuilder data = new ZStringBuilder();
		for (IndexElement element: getElements()) {
			if (data.length()>0) {
				data.append("\n");
			}
			data.append(element.toStringBuilder(getKey()));
		}
		return data;
	}
}
