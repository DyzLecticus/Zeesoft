package nl.zeesoft.zodb.db;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
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

	protected List<String> getFileNames() {
		return fileNames;
	}
	
	@Override
	public void whileWorking() {
		if (done<fileNames.size()) {
			String fileName = fileNames.get(done);
			ZStringBuilder content = new ZStringBuilder();
			ZStringBuilder err = content.fromFile(fileName);
			if (err.length()>0) {
				getMessenger().error(this,err.toString());
			} else {
				String[] split = fileName.split("/");
				split = split[(split.length - 1)].split("\\.");
				int fileNum = Integer.parseInt(split[0]);
				List<IndexElement> elements = new ArrayList<IndexElement>();
				List<ZStringBuilder> lines = content.split("\n");
				for (ZStringBuilder line: lines) {
					IndexElement elem = new IndexElement();
					elem.fromStringBuilder(line);
					elem.fileNum = fileNum;
					elements.add(elem);
				}
				if (elements.size()>0) {
					index.addFileElements(fileNum,elements);
				}
			}
		} else {
			reader.workerIsDone();
			stop();
		}
	}
}
