package nl.zeesoft.zodb.db;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class IndexObjectWriteWorker extends FileWriteWorkerObject {
	protected IndexObjectWriteWorker(Messenger msgr,WorkerUnion union,FileWriterWorkerObject writer,int fileNum,String directory,List<IndexElement> elements,StringBuilder key) {
		super(msgr,union,writer,fileNum,directory,elements,key);
	}

	@Override
	protected ZStringBuilder getData() {
		ZStringBuilder data = new ZStringBuilder();
		for (IndexElement element: getElements()) {
			ZStringEncoder encoder = new ZStringEncoder(element.obj.toStringBuilder());
			encoder.encodeKey(getKey(),0);
			if (data.length()>0) {
				data.append("\n");
			}
			data.append("" + element.id);
			data.append("\t");
			data.append(encoder);
		}
		return data;
	}
}
