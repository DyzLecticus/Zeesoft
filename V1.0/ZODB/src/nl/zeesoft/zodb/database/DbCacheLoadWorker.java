package nl.zeesoft.zodb.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.file.XMLConfig;
import nl.zeesoft.zodb.file.XMLElem;
import nl.zeesoft.zodb.file.XMLFile;

public final class DbCacheLoadWorker extends DbIndexLoadWorkerObject {
	private List<Integer>							fileNums			= new ArrayList<Integer>();

	private SortedMap<String,QryFetch>				keyFetchMap			= new TreeMap<String,QryFetch>();
	private SortedMap<String,Long>					keyWeightMap		= new TreeMap<String,Long>();
	
	private Date									started				= null;
	private int										loaded				= 0;

	private	DocumentBuilder 						builder 			= null;

	protected DbCacheLoadWorker() {
		setSleep(1);
	}

	@Override
	public void start() {
		keyFetchMap.clear();
		keyWeightMap.clear();
		started = new Date();
		if ((fileNums==null) || (fileNums.size()<=0)) {
			Messenger.getInstance().error(this, "Unable to start load worker: file numbers have not been set or list is empty");
			setDone(true);
			return;
		}
		builder = XMLConfig.getInstance().getBuilderForSource(this);
		super.start();
    }
	
	@Override
    public void stop() {
		XMLConfig.getInstance().removeBuilderForSource(this);
		super.stop();
    }
	
	@Override
	public void whileWorking() {
		if (fileNums.size()>0) {
			Integer fileNum = fileNums.get(0);
			unserializeCacheFileNoLock("" + fileNum,this);
			fileNums.remove(fileNum);
			loaded++;
		} else {
			Messenger.getInstance().debug(this, "Loaded " + loaded + " cache files in " + (new Date().getTime() - started.getTime()) + " ms");
			stop();
			setDone(true);
		}
	}
	
	private void unserializeCacheFileNoLock(String fileName,Object source) {
		XMLFile file = new XMLFile(builder);
		file.parseFile(DbConfig.getInstance().getFullCacheDir() + fileName);
		String key = file.getRootElement().getChildByName("key").getValue().toString();
		long weight = Long.parseLong(file.getRootElement().getChildByName("weight").getValue().toString());
		QryFetch fetch = null; 
		XMLElem fElem = file.getRootElement().getChildByName("fetch");
		if (fElem!=null) {
			XMLFile f = new XMLFile();
			f.setRootElement(fElem);
			fetch = (QryFetch) QryObject.fromXml(f);
			f.cleanUp();
		}
		if (fetch!=null) {
			keyFetchMap.put(key, fetch);
			keyWeightMap.put(key, weight);
		}
		file.cleanUp();
	}

	/**
	 * @return the fileNums
	 */
	protected List<Integer> getFileNums() {
		return fileNums;
	}

	/**
	 * @return the keyFetchMap
	 */
	public SortedMap<String, QryFetch> getKeyFetchMap() {
		return keyFetchMap;
	}

	/**
	 * @return the keyWeightMap
	 */
	public SortedMap<String, Long> getKeyWeightMap() {
		return keyWeightMap;
	}
}	
