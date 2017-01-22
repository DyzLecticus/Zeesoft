package nl.zeesoft.zodb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

public class MdlObjectRefListMap {
	private SortedMap<String,MdlObjectRefList> referenceLists =  new TreeMap<String,MdlObjectRefList>(); 

	public void clear() {
		referenceLists.clear();
	}
	
	public void addReferenceList(String collectionName, MdlObjectRefList referenceList) {
		for (MdlObjectRef ref: referenceList.getReferences()) {
			addReference(collectionName,ref);	
		}
	}

	public void addReference(String collectionName, MdlObjectRef reference) {
		MdlObjectRefList refList = getReferenceListForCollection(collectionName);
		if (refList.getMdlObjectRefById(reference.getId().getValue())==null) {
			refList.getReferences().add(MdlObjectRef.copy(reference));
		}
	}

	public MdlObjectRefList getReferenceListForCollection(String collectionName) {
		MdlObjectRefList refList = referenceLists.get(collectionName);
		if (refList==null) {
			refList = new MdlObjectRefList();
			referenceLists.put(collectionName, refList);
		}
		return refList;
	}

	public MdlObjectRef getMdlObjectRefById(long id) {
		MdlObjectRef o = null;
		for (Entry<String,MdlObjectRefList> entry: referenceLists.entrySet()) {
			MdlObjectRef obj = entry.getValue().getMdlObjectRefById(id);
			if (obj!=null) {
				o = obj;
				break;
			}
		}
		return o;
	}

	public List<Long> getIdList() {
		List<Long> r = new ArrayList<Long>();
		for (Entry<String,MdlObjectRefList> entry: referenceLists.entrySet()) {
			for (MdlObjectRef ref: entry.getValue().getReferences()) {
				r.add(ref.getId().getValue());
			}
		}
		return r;
	}

	public int getSize() {
		int size = 0;
		for (Entry<String,MdlObjectRefList> entry: referenceLists.entrySet()) {
			size = size + entry.getValue().getReferences().size();
		}
		return size;
	}

	public int getUnloadedSize() {
		int size = 0;
		for (Entry<String,MdlObjectRefList> entry: referenceLists.entrySet()) {
			size = size + entry.getValue().getUnloadedRefs().size();
		}
		return size;
	}
	
	public List<String> getCollectionList() {
		List<String> r = new ArrayList<String>();
		for (Entry<String,MdlObjectRefList> entry: referenceLists.entrySet()) {
			r.add(entry.getKey());
		}
		return r;
	}

}
