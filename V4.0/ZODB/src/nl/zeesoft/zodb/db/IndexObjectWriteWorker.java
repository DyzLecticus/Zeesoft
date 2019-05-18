package nl.zeesoft.zodb.db;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class IndexObjectWriteWorker extends Worker {
	private IndexObjectWriterWorker		objectWriter	= null;
	private String						fileName		= "";
	private JsFile						obj				= null;
	private	StringBuilder				key				= null;
	
	protected IndexObjectWriteWorker(Messenger msgr, WorkerUnion union,IndexObjectWriterWorker objectWriter,String fileName, JsFile obj, StringBuilder key) {
		super(msgr, union);
		this.objectWriter = objectWriter;
		this.fileName = fileName;
		this.obj = obj;
		this.key = key;
		setSleep(0);
	}
	
	@Override
	public void whileWorking() {
		ZStringEncoder encoder = new ZStringEncoder(obj.toStringBuilder());
		encoder.encodeKey(key,0);
		ZStringBuilder error = encoder.toFile(fileName);
		if (error.length()>0) {
			getMessenger().error(this,"Failed to write file: " + error);
		}
		stop();
		objectWriter.writtenObject();
		objectWriter = null;
	}
}
