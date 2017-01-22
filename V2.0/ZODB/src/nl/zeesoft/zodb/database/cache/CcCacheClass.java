package nl.zeesoft.zodb.database.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbDataObject;


public final class CcCacheClass {
	private static final long					MAX_HITS		= 1000;
	
	private boolean								debug			= false;
	private int									maxSize			= 100;
	private SortedMap<Long,DbDataObject> 		idObjectMap 	= new TreeMap<Long,DbDataObject>();
	private SortedMap<Long,Long> 				idHitsMap 		= new TreeMap<Long,Long>();
	
	protected CcCacheClass(boolean debug,int maxSize) {
		this.debug = debug;
		this.maxSize = maxSize;
	}

	protected void clear() {
		idObjectMap.clear();
		idHitsMap.clear();
	}
	
	protected void addObject(DbDataObject object) {
		if (object.getId()>0 && !idObjectMap.containsKey(object.getId())) {
			popBeforeAdd();
			idObjectMap.put(object.getId(),object);
			hitObject(object.getId());
		}
	}

	protected void updateObject(DbDataObject object) {
		if (object.getId()>0 && idObjectMap.containsKey(object.getId())) {
			idObjectMap.put(object.getId(),object);
		}
	}

	protected DbDataObject getObjectById(long id) {
		DbDataObject r = null;
		if (idObjectMap.containsKey(id)) {
			r = idObjectMap.get(id);
			hitObject(id);
		}
		return r;
	}

	protected void removeObjectById(long id) {
		if (idObjectMap.containsKey(id)) {
			idObjectMap.remove(id);
			idHitsMap.remove(id);
		}
	}
	
	protected void applyHalfLife() {
		Date started = null;
		if (debug && DbConfig.getInstance().isDebugPerformance()) {
			started = new Date();
		}
		boolean applied = false;
		for (long id: idHitsMap.keySet()) {
			Long hits = idHitsMap.get(id);
			if (hits>1) {
				hits = (hits / 2);
				idHitsMap.put(id,hits);
				applied = true;
			}
		}
		if (applied && debug && DbConfig.getInstance().isDebugPerformance()) {
			Messenger.getInstance().debug(this,"Applying half life to cache hits took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		}
	}
	
	private void hitObject(long id) {
		if (idObjectMap.containsKey(id)) {
			Long hits = idHitsMap.get(id);
			if (hits==null) {
				hits = 0L;
			}
			hits++;
			idHitsMap.put(id,hits);
			if (hits>=MAX_HITS) {
				applyHalfLife();
			}
		}
	}
	
	private void popBeforeAdd() {
		if (idObjectMap.size()>=maxSize) {
			Date started = null;
			if (debug && DbConfig.getInstance().isDebugPerformance()) {
				started = new Date();
			}
			int margin = maxSize / 10;
			List<Long> orderedIdList = getIdListOrderedByHits();
			while (idObjectMap.size()>=(maxSize - margin)) {
				long removeId = orderedIdList.remove(0);
				idObjectMap.remove(removeId);
				idHitsMap.remove(removeId);
			}
			if (debug && DbConfig.getInstance().isDebugPerformance()) {
				Messenger.getInstance().debug(this,"Popping cache before add took: " + ((new Date()).getTime() - started.getTime()) + " ms");
			}
		}
	}
	
	private List<Long> getIdListOrderedByHits() {
		List<Long> r = new ArrayList<Long>();
		SortedMap<Long,List<Long>> hitsIdListMap = new TreeMap<Long,List<Long>>();
		for (Long id: idHitsMap.keySet()) {
			long hits = idHitsMap.get(id);
			List<Long> idList = hitsIdListMap.get(hits);
			if (idList==null) {
				idList = new ArrayList<Long>();
				hitsIdListMap.put(hits,idList);
			}
			idList.add(id);
		}
		for (Long hits: hitsIdListMap.keySet()) {
			List<Long> addIds = hitsIdListMap.get(hits);
			for (Long id: addIds) {
				r.add(id);
			}
		}
		return r;
	}
}
