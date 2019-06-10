package nl.zeesoft.zodb.db;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public abstract class FileWriteWorkerObject extends Worker {
	private FileWriterWorkerObject		writer			= null;
	private String						fileName		= "";
	private List<IndexElement>			elements		= null;
	private	StringBuilder				key				= null;
	
	protected FileWriteWorkerObject(Messenger msgr, WorkerUnion union,FileWriterWorkerObject writer,String fileName, List<IndexElement> elements, StringBuilder key) {
		super(msgr, union);
		this.writer = writer;
		this.fileName = fileName;
		this.elements = elements;
		this.key = key;
		setSleep(0);
	}
	
	@Override
	protected void whileWorking() {
		ZStringBuilder error = getData().toFile(fileName);
		if (error.length()>0) {
			getMessenger().error(this,"Failed to write file: " + error);
		}
		stop();
		writer.writtenFile();
		writer = null;
	}
	
	@Override
	protected void setCaughtException(Exception caughtException) {
		super.setCaughtException(caughtException);
		writer.writtenFile();
		writer = null;
	}

	protected List<IndexElement> getElements() {
		return elements;
	}
	
	protected StringBuilder getKey() {
		return key;
	}
	
	protected abstract ZStringBuilder getData();
}
