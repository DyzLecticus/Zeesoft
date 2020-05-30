package nl.zeesoft.zdk.database;

import java.io.File;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.collection.PersistableObject;
import nl.zeesoft.zdk.collection.PersistableProperty;

@PersistableObject
public class DatabaseConfiguration {
	@PersistableProperty
	private Logger		logger						= new Logger();
	
	@PersistableProperty
	private String 		baseDir						= "";
	@PersistableProperty
	private String 		indexDir					= "database/index/";
	@PersistableProperty
	private String 		dataDir						= "database/data/";

	@PersistableProperty
	private int 		indexPartitionSize			= 100;
	@PersistableProperty
	private int 		numberOfDataBlocks			= 1000;
	
	@PersistableProperty
	private int 		loadIndexTimeoutMs			= 3000;
	@PersistableProperty
	private int 		loadAllBlocksTimeoutMs		= 3000;
	@PersistableProperty
	private int 		loadBlockTimeoutMs			= 200;

	public DatabaseConfiguration copy() {
		DatabaseConfiguration r = new DatabaseConfiguration();
		r.logger = new Logger(logger.isDebug());
		r.baseDir = baseDir;
		r.indexDir = indexDir;
		r.dataDir = dataDir;
		r.indexPartitionSize = indexPartitionSize;
		r.numberOfDataBlocks = numberOfDataBlocks;
		r.loadIndexTimeoutMs = loadIndexTimeoutMs;
		r.loadAllBlocksTimeoutMs = loadAllBlocksTimeoutMs;
		r.loadBlockTimeoutMs = loadBlockTimeoutMs;
		return r;
	}

	public void mkDirs() {
		if (mkDir(baseDir)) {
			mkDir(baseDir + indexDir);
			mkDir(baseDir + dataDir);
		}
	}
	
	public void rmDirs() {
		File dir = new File(baseDir);
		if (dir.exists()) {
			rmDir(baseDir + indexDir);
			rmDir(baseDir + dataDir);
		}
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	public String getIndexPath() {
		return baseDir + indexDir;
	}
	
	public String getDataBlockFilePath(int blockNum) {
		return baseDir + dataDir + blockNum + ".txt";
	}

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public String getIndexDir() {
		return indexDir;
	}

	public void setIndexDir(String indexDir) {
		this.indexDir = indexDir;
	}

	public String getDataDir() {
		return dataDir;
	}

	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}

	public int getIndexPartitionSize() {
		return indexPartitionSize;
	}

	public void setIndexPartitionSize(int indexPartitionSize) {
		this.indexPartitionSize = indexPartitionSize;
	}

	public int getNumberOfDataBlocks() {
		return numberOfDataBlocks;
	}

	public void setNumberOfDataBlocks(int numberOfDataBlocks) {
		this.numberOfDataBlocks = numberOfDataBlocks;
	}

	public int getLoadIndexTimeoutMs() {
		return loadIndexTimeoutMs;
	}

	public void setLoadIndexTimeoutMs(int loadIndexTimeoutMs) {
		this.loadIndexTimeoutMs = loadIndexTimeoutMs;
	}
	
	public int getLoadAllBlocksTimeoutMs() {
		return loadAllBlocksTimeoutMs;
	}

	public void setLoadAllBlocksTimeoutMs(int loadDataTimeoutMs) {
		this.loadAllBlocksTimeoutMs = loadDataTimeoutMs;
	}

	public int getLoadBlockTimeoutMs() {
		return loadBlockTimeoutMs;
	}

	public void setLoadBlockTimeoutMs(int loadBlockTimeoutMs) {
		this.loadBlockTimeoutMs = loadBlockTimeoutMs;
	}

	public boolean isDebug() {
		return logger.isDebug();
	}
	
	public void debug(Object source, Str message) {
		logger.debug(source, message);
	}
	
	public void error(Object source, Str message) {
		logger.error(this, message);
	}
	
	private boolean mkDir(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			Str msg = new Str("Creating directory: ");
			msg.sb().append(dir.getAbsolutePath());
			logger.debug(this,msg);
			dir.mkdirs();
		}
		return dir.exists();
	}
	
	private boolean rmDir(String path) {
		File dir = new File(path);
		if (dir.exists()) {
			Str msg = new Str("Deleting directory: ");
			msg.sb().append(dir.getAbsolutePath());
			logger.debug(this,msg);
			for (File file: dir.listFiles()) {
				file.delete();
			}
			dir.delete();
		}
		return dir.exists();
	}
}
