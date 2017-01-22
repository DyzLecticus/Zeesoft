package nl.zeesoft.zodb.database;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Worker;
import nl.zeesoft.zodb.database.gui.GuiController;
import nl.zeesoft.zodb.database.index.IdxObject;
import nl.zeesoft.zodb.file.FileIO;

public final class DbIndexReadWorker extends Worker {
	private IdxObject		indexObject		= null;
	private boolean			done			= false;
	private List<String>	fileNums		= new ArrayList<String>();
	
	protected DbIndexReadWorker(IdxObject index) {
		setSleep(0);
		indexObject = index;
	}
	
	@Override
	public void start() {
		super.start();
    }
	
	@Override
    public void stop() {
		super.stop();
		done = true;
    }
	
	@Override
	public void whileWorking() {
		if (fileNums.size()==0) {
			fileNums = Generic.getDirNumberedFileNames(indexObject.getDirName());
		}
		if (fileNums.size()>0) {
			String fileNum = fileNums.remove(0);
			FileIO file = new FileIO();
			StringBuilder content = file.readFile(indexObject.getDirName() + fileNum);
			indexObject.unseralizeFileContent(Integer.parseInt(fileNum), content);
			GuiController.getInstance().incrementProgressFrameDone();
		}
		if (fileNums.size()==0) {
			stop();
		}
	}

	/**
	 * @return the done
	 */
	protected boolean isDone() {
		return done;
	}
	
}	
