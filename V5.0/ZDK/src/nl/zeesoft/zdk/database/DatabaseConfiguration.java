package nl.zeesoft.zdk.database;

import java.io.File;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.TimeStamp;
import nl.zeesoft.zdk.collection.PersistableObject;
import nl.zeesoft.zdk.collection.PersistableProperty;

@PersistableObject
public class DatabaseConfiguration {
	@PersistableProperty
	private boolean		debug						= false;
	
	@PersistableProperty
	private int 		numBlocks					= 100;
	@PersistableProperty
	private String 		baseDir						= "";
	@PersistableProperty
	private String 		indexDir					= "database/";
	@PersistableProperty
	private String 		dataDir						= "database/data/";

	@PersistableProperty
	private boolean		compressIndex				= false;
	@PersistableProperty
	private boolean		compressData				= false;
	
	@PersistableProperty
	private int 		loadAllBlocksTimeoutMs		= 3000;
	@PersistableProperty
	private int 		loadBlockTimeoutMs			= 200;

	public DatabaseConfiguration copy() {
		DatabaseConfiguration r = new DatabaseConfiguration();
		r.debug = debug;
		r.numBlocks = numBlocks;
		r.baseDir = baseDir;
		r.indexDir = indexDir;
		r.dataDir = dataDir;
		r.loadAllBlocksTimeoutMs = loadAllBlocksTimeoutMs;
		r.loadBlockTimeoutMs = loadBlockTimeoutMs;
		return r;
	}

	public void mkDirs() {
		File bDir = new File(baseDir);
		if (!bDir.exists()) {
			Str msg = new Str("Creating directory: ");
			msg.sb().append(bDir.getAbsolutePath());
			bDir.mkdirs();
		}
		File iDir = new File(baseDir + indexDir);
		if (!iDir.exists()) {
			Str msg = new Str("Creating directory: ");
			msg.sb().append(iDir.getAbsolutePath());
			iDir.mkdirs();
		}
		File dDir = new File(baseDir + dataDir);
		if (!dDir.exists()) {
			Str msg = new Str("Creating directory: ");
			msg.sb().append(dDir.getAbsolutePath());
			dDir.mkdirs();
		}
	}
	
	public String getIndexFilePath() {
		return baseDir + indexDir + "index.txt";
	}
	
	public String getDataBlockFilePath(int blockNum) {
		return baseDir + dataDir + blockNum + ".txt";
	}
	
	public void debug(Object source, Str message) {
		if (debug) {
			prependSource(source,message);
			System.out.println(message.sb());
		}
	}
	
	public void error(Object source, Str message) {
		prependSource(source,message);
		System.err.println(message.sb());
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public int getNumBlocks() {
		return numBlocks;
	}

	public void setNumBlocks(int numBlocks) {
		this.numBlocks = numBlocks;
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

	public boolean isCompressIndex() {
		return compressIndex;
	}

	public void setCompressIndex(boolean compressIndex) {
		this.compressIndex = compressIndex;
	}

	public boolean isCompressData() {
		return compressData;
	}

	public void setCompressData(boolean compressData) {
		this.compressData = compressData;
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
	
	private void prependSource(Object source, Str message) {
		message.sb().insert(0, ": ");
		message.sb().insert(0, source.getClass().getName());
		message.sb().insert(0, " ");
		message.sb().insert(0, TimeStamp.getDateTimeString());
	}
}
