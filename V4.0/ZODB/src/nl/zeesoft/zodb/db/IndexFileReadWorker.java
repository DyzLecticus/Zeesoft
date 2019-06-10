package nl.zeesoft.zodb.db;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class IndexFileReadWorker extends Worker {
	private	Index			index		= null;
	private	IndexFileReader	reader		= null;
	private List<String>	fileNames	= new ArrayList<String>();
	private int				done		= 0;

	protected IndexFileReadWorker(Messenger msgr, WorkerUnion union,Index index,IndexFileReader reader) {
		super(msgr, union);
		this.index = index;
		this.reader = reader;
		setSleep(0);
	}

	@Override
	protected void whileWorking() {
		if (done<fileNames.size()) {
			String fileName = fileNames.get(done);
			ZStringBuilder content = new ZStringBuilder();
			ZStringBuilder err = content.fromFile(index.getFileDirectory() + fileName);
			if (err.length()>0) {
				getMessenger().error(this,err.toString());
			} else {
				String[] split = fileName.split("/");
				split = split[(split.length - 1)].split("\\.");
				if (ZStringEncoder.isNumberNotNegative(split[0])) {
					int fileNum = Integer.parseInt(split[0]);
					List<IndexElement> elements = new ArrayList<IndexElement>();
					List<ZStringBuilder> lines = content.split("\n");
					for (ZStringBuilder line: lines) {
						IndexElement elem = new IndexElement();
						elem.fromStringBuilder(line,index.getKey());
						if (elem.name.length()>0 && elem.modified>0) {
							elem.indexFileNum = fileNum;
							elements.add(elem);
						}
					}
					if (elements.size()>0) {
						index.addFileElements(fileNum,elements);
					}
				}
			}
			done++;
		} else {
			reader.workerIsDone();
			reader = null;
			index = null;
			stop();
		}
	}
	
	@Override
	protected void setCaughtException(Exception caughtException) {
		super.setCaughtException(caughtException);
		reader.workerIsDone();
		reader = null;
		index = null;
	}

	protected List<String> getFileNames() {
		return fileNames;
	}
}
