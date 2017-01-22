package nl.zeesoft.zodb.database;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.file.FileObject;

public final class DbIndexSaveFileWorker extends DbIndexSaveWorkerObject {
	private String							fileName		= "";
	private StringBuffer					content			= new StringBuffer();
	
	public DbIndexSaveFileWorker() {
		setSleep(1);
	}
	
	@Override
	public void start() {
		if (fileName.length()>0) {
			super.start();
		} else {
			Messenger.getInstance().error(this, "Unable to start save file worker because file name is empty");
			setDone(true);
		}
    }
	
	@Override
	public void whileWorking() {
        FileObject f = new FileObject();
        if (!f.writeFile(fileName,content)) {
    		Messenger.getInstance().error(this, "Error while writing file: " + fileName);
    	} else {
    		stop();
    		setDone(true);
    	}
	}

	/**
	 * @param fileName the fileName to set
	 */
	protected void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @param content the content to set
	 */
	protected void setContent(StringBuffer content) {
		this.content = content;
	}
	
}	
