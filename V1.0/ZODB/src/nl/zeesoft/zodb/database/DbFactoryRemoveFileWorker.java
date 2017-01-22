package nl.zeesoft.zodb.database;

import java.io.File;
import java.io.IOException;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;
import nl.zeesoft.zodb.file.FileObject;

public final class DbFactoryRemoveFileWorker extends Worker {
	private File							dir				= null;
	private String[]						files			= null;
	private int								index			= 0;
	private int								removed			= 0;
	
	protected DbFactoryRemoveFileWorker(File dir) {
		setSleep(1);
		this.dir = dir;
	}
	
	@Override
	public void stop() {
		super.stop();
		Messenger.getInstance().debug(this, "Stopped remove file worker: " + dir.getAbsolutePath() + ", removed: " + removed);
	}

	@Override
	public void whileWorking() {
		if (files==null) {
			files = dir.list();
		}
		if (index>=files.length) {
			stop();
		} else {
			int si = index;
			for (int i = index; i < files.length; i++) {
				si++;
				String fileName = Generic.dirName(dir.getAbsolutePath()) + files[i];
				try {
					if (FileObject.fileExists(fileName)) {
						FileObject.deleteFile(fileName);
						removed++;
					}
				} catch (IOException e) {
					Messenger.getInstance().error(this, "Unable to remove file: " + fileName + ", error: " + e.getMessage());
				}
				if ((removed % 100) == 0) {
					break;
				}
			}
			index = si;
		}
	}
}	
