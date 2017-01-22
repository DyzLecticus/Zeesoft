package nl.zeesoft.zodb.database.index;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.database.DbIndex;

public abstract class IdxObject extends Locker {
	private SortedMap<Integer,List<String>>	fileNumIndexKeysMap	= new TreeMap<Integer,List<String>>();
	private SortedMap<String,Integer>		indexKeyFileNumMap	= new TreeMap<String,Integer>();
	private List<Integer>					changedFileNums		= new ArrayList<Integer>();
	private String							dirName				= "";

	protected void addedIndexKey(String key) {
		if (!indexKeyFileNumMap.containsKey(key)) {
			int fileNum = getFileNum();
			indexKeyFileNumMap.put(key, fileNum);
			List<String> indexKeys = fileNumIndexKeysMap.get(fileNum);
			if (indexKeys==null) {
				indexKeys = new ArrayList<String>();
			}
			indexKeys.add(key);
			fileNumIndexKeysMap.put(fileNum,indexKeys);
			if (!changedFileNums.contains(fileNum)) {
				changedFileNums.add(fileNum);
			}
		}
	}

	protected void updatedIndexKey(String key) {
		if (indexKeyFileNumMap.containsKey(key)) {
			int fileNum = indexKeyFileNumMap.get(key);
			if (!changedFileNums.contains(fileNum)) {
				changedFileNums.add(fileNum);
			}
		}
	}
	
	protected void removedIndexKey(String key) {
		if (indexKeyFileNumMap.containsKey(key)) {
			int fileNum = indexKeyFileNumMap.get(key);
			indexKeyFileNumMap.remove(key);
			List<String> indexKeys = fileNumIndexKeysMap.get(fileNum);
			if (indexKeys!=null) {
				indexKeys.remove(key);
				if (indexKeys.size()==0) {
					fileNumIndexKeysMap.remove(fileNum);
				}
			}
			if (!changedFileNums.contains(fileNum)) {
				changedFileNums.add(fileNum);
			}
		}
	}
	
	protected boolean isChanged() {
		return changedFileNums.size()>0;
	}
	
	public SortedMap<Integer,StringBuilder> getChangedFiles(int max) {
		SortedMap<Integer,StringBuilder> fileNumFileContentMap = new TreeMap<Integer,StringBuilder>();
		if (isChanged()) {
			List<Integer> fileNums = new ArrayList<Integer>(changedFileNums);
			for (int fileNum: fileNums) {
				fileNumFileContentMap.put(fileNum, getFileContentForKeys(fileNumIndexKeysMap.get(new Integer(fileNum))));
				changedFileNums.remove(new Integer(fileNum));
				if (max>0 && fileNumFileContentMap.size()>=max) {
					break;
				}
			}
		}
		return fileNumFileContentMap;
	}

	public void unseralizeFileContent(int fileNum,StringBuilder content) {
		List<String> keys = getKeysFromFileContent(content);
		fileNumIndexKeysMap.put(fileNum,keys);
		for (String key: keys) {
			indexKeyFileNumMap.put(key,fileNum);
		}
	}

	/**
	 * @return the dirName
	 */
	public String getDirName() {
		return dirName;
	}

	/**
	 * @param dirName the dirName to set
	 */
	protected void setDirName(String dirName) {
		this.dirName = dirName;
	}
	
	protected abstract StringBuilder getFileContentForKeys(List<String> keys);
	
	protected abstract List<String> getKeysFromFileContent(StringBuilder content);

	private int getFileNum() {
		int r = 1;
		for (int i = 1; i < 999999; i++) {
			r = i;
			List<String> keys = fileNumIndexKeysMap.get(i);
			if (keys==null || keys.size()<=DbIndex.BLOCK_SIZE) {
				break;
			}
		}
		return r;
	}	
}
