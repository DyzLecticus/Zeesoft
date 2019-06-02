package nl.zeesoft.zodb.db;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class IndexObjectWriteWorker extends Worker {
	private IndexObjectWriterWorker		objectWriter	= null;
	private String						fileName		= "";
	private List<IndexElement>			elements		= null;
	private	StringBuilder				key				= null;
	
	protected IndexObjectWriteWorker(Messenger msgr, WorkerUnion union,IndexObjectWriterWorker objectWriter,String fileName, List<IndexElement> elements, StringBuilder key) {
		super(msgr, union);
		this.objectWriter = objectWriter;
		this.fileName = fileName;
		this.elements = elements;
		this.key = key;
		setSleep(0);
	}
	
	@Override
	public void whileWorking() {
		ZStringBuilder data = new ZStringBuilder();
		for (IndexElement element: elements) {
			ZStringEncoder encoder = new ZStringEncoder(element.obj.toStringBuilder());
			encoder.encodeKey(key,0);
			if (data.length()>0) {
				data.append("\n");
			}
			data.append("" + element.id);
			data.append("\t");
			data.append(encoder);
		}
		ZStringBuilder error = data.toFile(fileName);
		if (error.length()>0) {
			getMessenger().error(this,"Failed to write file: " + error);
		}
		stop();
		objectWriter.writtenObject();
		objectWriter = null;
	}
}
