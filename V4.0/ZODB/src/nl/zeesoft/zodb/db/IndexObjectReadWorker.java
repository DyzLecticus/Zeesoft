package nl.zeesoft.zodb.db;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class IndexObjectReadWorker extends Worker {
	private IndexObjectReaderWorker		objectReader	= null;
	private int							fileNum			= 0;
	private String						fileName		= "";
	private	StringBuilder				key				= null;
	
	protected IndexObjectReadWorker(Messenger msgr, WorkerUnion union,IndexObjectReaderWorker objectReader,int fileNum,String fileName,StringBuilder key) {
		super(msgr, union);
		this.objectReader = objectReader;
		this.fileName = fileName;
		this.key = key;
		setSleep(0);
	}
	
	@Override
	public void whileWorking() {
		SortedMap<Long,JsFile> idObjMap = new TreeMap<Long,JsFile>();
		ZStringBuilder data = new ZStringBuilder();
		ZStringBuilder err = data.fromFile(fileName);
		if (err.length()>0) {
			getMessenger().error(this,"Failed to read object file " + fileName + ": " + err);
		} else {
			List<ZStringBuilder> lines = data.split("\n");
			for (ZStringBuilder line: lines) {
				long id = 0;
				List<ZStringBuilder> idObj = line.split("\t");
				try {
					id = Long.parseLong(idObj.get(0).toString());
				} catch (NumberFormatException e) {
					getMessenger().error(this,"Object file has been corrupted: " + fileName,e);
				}
				if (id!=0) {
					ZStringEncoder decoder = new ZStringEncoder(idObj.get(1));
					decoder.decodeKey(key,0);
					JsFile obj = new JsFile();
					obj.fromStringBuilder(decoder);
					if (obj.rootElement==null) {
						getMessenger().error(this,"Object " + id + " has been corrupted");
						obj.rootElement = new JsElem();
					} else {
						idObjMap.put(id,obj);
					}
				}
			}
		}
		stop();
		if (idObjMap.size()>0) {
			objectReader.readObjects(fileNum,idObjMap);
		}
		objectReader = null;
	}
}
