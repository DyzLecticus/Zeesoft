package nl.zeesoft.zodb.db;

import java.io.File;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public abstract class FileWriteWorkerObject extends Worker {
	private FileWriterWorkerObject		writer			= null;
	private int							fileNum			= 0;
	private String						directory		= "";
	private List<IndexElement>			elements		= null;
	private	StringBuilder				key				= null;
	
	protected FileWriteWorkerObject(Messenger msgr, WorkerUnion union,FileWriterWorkerObject writer,int fileNum,String directory, List<IndexElement> elements, StringBuilder key) {
		super(msgr, union);
		this.writer = writer;
		this.fileNum = fileNum;
		this.directory = directory;
		this.elements = elements;
		this.key = key;
		setSleep(0);
	}
	
	@Override
	protected void whileWorking() {
		String fileName = directory + fileNum + ".txt";
		if (elements.size()==0) {
			File file = new File(fileName);
			if (file.exists() && !file.delete()) {
				getMessenger().error(this,"Failed to delete file: " + fileName);
			}
		} else {
			ZStringBuilder error = getData().toFile(fileName);
			if (error.length()>0) {
				getMessenger().error(this,"Failed to write file: " + error);
			}
		}
		stop();
		writer.writtenFile(fileNum);
		writer = null;
	}
	
	@Override
	protected void setCaughtException(Exception caughtException) {
		super.setCaughtException(caughtException);
		writer.writtenFile(fileNum);
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
