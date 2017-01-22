package nl.zeesoft.zodb.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;
import nl.zeesoft.zodb.database.gui.GuiController;

public abstract class DbWriteWorkerObject extends Worker {
	private static final int 					MAX_THREADS		= 20;
	
	private SortedMap<String,StringBuilder> 	writeFiles 		= new TreeMap<String,StringBuilder>();
	private Date 								start			= null;

	private List<DbWriteFileWorker>				writeWorkers	= new ArrayList<DbWriteFileWorker>();
	
	protected DbWriteWorkerObject() {
		for (int i = 0; i < MAX_THREADS; i++) {
			writeWorkers.add(new DbWriteFileWorker());
		}
	}

	protected void writeFiles(boolean showProgress) {
		if (DbConfig.getInstance().isDebugPerformance()) {
			start = new Date();
		}
				
		boolean write = false;
		while (writeFiles.size()>0) {
			write = true;
			
			List<String> writeKeys	= new ArrayList<String>();

			int workerIndex = 0;
	        for (Entry<String,StringBuilder> entry: writeFiles.entrySet()) {
	        	DbWriteFileWorker writeWorker = writeWorkers.get(workerIndex);
	        	writeKeys.add(entry.getKey());
				writeWorker.addFile(entry.getKey(),entry.getValue(),showProgress);
	        	workerIndex++;
	        	if (workerIndex>=writeWorkers.size()) {
	        		workerIndex = 0;
	        	}
	        }
	        
			for (DbWriteFileWorker writeWorker: writeWorkers) {
				writeWorker.start();
			}
	        
	        boolean interrupted = false;
	        for (DbWriteFileWorker writeWorker: writeWorkers) {
	        	while (writeWorker.getSize()>0) {
	        		try {
	        			if (showProgress) {
		            		GuiController.getInstance().refreshProgressFrame();
	        			}
						Thread.sleep(10);
					} catch (InterruptedException e) {
						interrupted = true;
						Messenger.getInstance().error(this, "A write worker was interrupted");
					}
	        	}
	        }

	        for (DbWriteFileWorker writeWorker: writeWorkers) {
				writeWorker.stop();
			}

	        if (!interrupted) {
	        	if (writeKeys.size()==writeFiles.size()) {
	        		writeFiles.clear();
	        	} else {
		        	for (String key: writeKeys) {
		        		writeFiles.remove(key);
		        	}
	        	}
	        }
		}

		if (write) {
			writingFilesDone();
		}
	}

	protected void writingFilesDone() {
		if (DbConfig.getInstance().isDebugPerformance()) {
			Messenger.getInstance().debug(this, "Writing files took: " + (new Date().getTime() - start.getTime()) + " ms");
		}
	}
	
	/**
	 * @return the writeFiles
	 */
	protected SortedMap<String, StringBuilder> getWriteFiles() {
		return writeFiles;
	}

	/**
	 * @param writeFiles the writeFiles to set
	 */
	protected void setWriteFiles(SortedMap<String, StringBuilder> writeFiles) {
		this.writeFiles = writeFiles;
	}

	/**
	 * @return the start
	 */
	protected Date getStart() {
		return start;
	}
}	
