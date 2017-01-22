package nl.zeesoft.zodb.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;
import nl.zeesoft.zodb.database.gui.GuiController;
import nl.zeesoft.zodb.file.FileIO;

public final class DbWriteFileWorker extends Worker {
	private boolean							showProgress	= false;
	private List<String>					fileNames		= new ArrayList<String>();
	private List<StringBuilder>				fileContents	= new ArrayList<StringBuilder>();
	private int								attempts		= 0;
		
	protected DbWriteFileWorker() {
		setSleep(1);
	}

	@Override
	public void start() {
		attempts = 0;
		if (getSize()>0) {
			super.start();
		}
	}

	protected void addFile(String fileName, StringBuilder fileContent, boolean showProgress) {
		lockMe(this);
		this.showProgress = showProgress;
		fileNames.add(fileName);
		fileContents.add(fileContent);
		unlockMe(this);
	}

	protected int getSize() {
		int r = 0;
		lockMe(this);
		r = fileNames.size();
		unlockMe(this);
		return r;
	}

	@Override
	public void whileWorking() {
		int size = 0;
		boolean show = false;
		String fileName = "";
		StringBuilder content = null;

		lockMe(this);
		size = fileNames.size();
		if (size>0) {
			show = showProgress;
			fileName = fileNames.get(0);
			content = fileContents.get(0);
		}
		unlockMe(this);
		
		if (fileName.length()>0 && content!=null) {
	        boolean error = false;
	        
	        FileIO f = new FileIO();
	        if (content.length()>0) {
	        	String err = f.writeFileReturnError(fileName,content);
	        	if (err.length()>0) {
	        		if (attempts>0) {
	        			Messenger.getInstance().error(this, "Error while writing file: " + fileName + ", error: " + err);
	        		}
	        		error = true;
	        	}
	        } else {
	        	try {
					FileIO.deleteFile(fileName);
				} catch (IOException e) {
	        		if (attempts>0) {
	        			Messenger.getInstance().error(this, "Error while removing file: " + fileName + " " + Generic.getCallStackString(e.getStackTrace(),""));
	        		}
	        		error = true;
				}
	        }

	        if (!error) {
	        	attempts = 0;
	    		lockMe(this);
	    		if (fileNames.size()>0) {
	    			fileNames.remove(0);
	    		}
	    		if (fileContents.size()>0) {
	    			fileContents.remove(0);
	    		}
	    		unlockMe(this);
	    		size--;
	    		if (show) {
	    			GuiController.getInstance().incrementProgressFrameDone();
	    		}
	        } else {
	        	attempts++;
	        }
		} else {
    		lockMe(this);
    		if (fileNames.size()>0) {
    			fileNames.remove(0);
    		}
    		if (fileContents.size()>0) {
    			fileContents.remove(0);
    		}
    		unlockMe(this);
		}
		
        if (size==0) {
    		lockMe(this);
			fileNames.clear();
    		fileContents.clear();
    		unlockMe(this);
        	stop();
        }
	}
}	
