package nl.zeesoft.zodb.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.file.FileObject;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.MdlObjectRefList;

public final class DbIndexLoadFileWorker extends DbIndexLoadWorkerObject {
	private List<Integer>							fileNums			= new ArrayList<Integer>();
	private SortedMap<Integer,MdlObjectRefList>		fileNumObjectMap	= new TreeMap<Integer,MdlObjectRefList>();
	private SortedMap<Long,Integer>					idFileNumMap		= new TreeMap<Long,Integer>();

	private Date									started				= null;
	private int										loaded				= 0;
	
	protected DbIndexLoadFileWorker() {
		setSleep(1);
	}

	@Override
	public void start() {
		if ((fileNums==null) || (fileNums.size()<=0)) {
			Messenger.getInstance().error(this, "Unable to start load worker: file numbers have not been set or list is empty");
			return;
		}
		started = new Date();
		super.start();
    }
	
	@Override
	public void whileWorking() {
		if (fileNums.size()>0) {
			Integer fileNum = fileNums.get(0);
			unserializeIndexFileNoLock("" + fileNum,this);
			fileNums.remove(fileNum);
			DbIndex.getInstance().addLoadedReferences(fileNumObjectMap, idFileNumMap, this);
			fileNumObjectMap.clear();
			idFileNumMap.clear();
			loaded++;
		} else {
			Messenger.getInstance().debug(this, "Loaded " + loaded + " index files in " + (new Date().getTime() - started.getTime()) + " ms");
			stop();
			setDone(true);
		}
	}
	
	private void unserializeIndexFileNoLock(String fileName,Object source) {
		FileObject f = new FileObject();

		int fileNum = Integer.parseInt(fileName);
		MdlObjectRefList refs = fileNumObjectMap.get(fileNum);
		if (refs==null) {
			refs = new MdlObjectRefList();
			fileNumObjectMap.put(fileNum, refs);
		}

		String[] vals = f.readFile(DbConfig.getInstance().getFullIndexDir() + fileName).toString().split("\n");
		for (String val: vals) {
			MdlObjectRef ref = MdlObjectRef.parseMdlObjectRef(val);
			if (ref!=null) {
				MdlDataObject obj = DbCache.getInstance().getPreloadedObjects().get(ref.getId().getValue());
				if (obj!=null) {
					ref.setDataObject(obj);
				}
				refs.getReferences().add(ref);
				idFileNumMap.put(ref.getId().getValue(),fileNum);
			}
		}
	}

	/**
	 * @return the fileNums
	 */
	protected List<Integer> getFileNums() {
		return fileNums;
	}
}	
