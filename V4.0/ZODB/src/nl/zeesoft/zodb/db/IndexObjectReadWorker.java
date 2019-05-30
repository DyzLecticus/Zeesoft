package nl.zeesoft.zodb.db;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class IndexObjectReadWorker extends Worker {
	private IndexObjectReaderWorker		objectReader	= null;
	private long						id				= 0;
	private String						fileName		= "";
	private	StringBuilder				key				= null;
	
	protected IndexObjectReadWorker(Messenger msgr, WorkerUnion union,IndexObjectReaderWorker objectReader,long id,String fileName,StringBuilder key) {
		super(msgr, union);
		this.objectReader = objectReader;
		this.id = id;
		this.fileName = fileName;
		this.key = key;
		setSleep(0);
	}
	
	@Override
	public void whileWorking() {
		JsFile obj = new JsFile();
		ZStringEncoder decoder = new ZStringEncoder();
		ZStringBuilder err = decoder.fromFile(fileName);
		if (err.length()>0) {
			getMessenger().error(this,"Failed to read object " + id + ": " + err);
		} else {
			decoder.decodeKey(key,0);
			obj = new JsFile();
			obj.fromStringBuilder(decoder);
			if (obj.rootElement==null) {
				getMessenger().error(this,"Object " + id + " has been corrupted");
				obj.rootElement = new JsElem();
			}
		}
		stop();
		objectReader.readObject(id,obj);
		objectReader = null;
	}
}
