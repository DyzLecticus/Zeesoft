package nl.zeesoft.zodb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.model.impl.DbUser;

public class MdlCollection {
	private String 								name 				= "";
	private String 								module 				= "";
	private boolean 							entity 				= false;

	private String 								nameSingle			= "";
	private String 								nameMulti			= "";
	private boolean 							cacheable			= false;
	private String								description			= "";
	
	private	int									userLevelFetch		= DbUser.USER_LEVEL_MAX;
	private	int									userLevelUpdate		= DbUser.USER_LEVEL_MAX;
	private	int									userLevelRemove		= DbUser.USER_LEVEL_MAX;
	private	int									userLevelAdd		= DbUser.USER_LEVEL_MAX;

	private List<MdlCollectionUniqueConstraint>	uniqueConstraints	= new ArrayList<MdlCollectionUniqueConstraint>();
	private List<MdlCollectionProperty>			properties			= new ArrayList<MdlCollectionProperty>();

	// Used by DbIndex
	private SortedMap<Long,MdlObjectRef>		idRefMap			= new TreeMap<Long,MdlObjectRef>();
	private SortedMap<String,MdlObjectRefList>	nameRefListMap		= new TreeMap<String,MdlObjectRefList>();
	// Ordered cache for DbIndex
	private MdlObjectRefList					orderedRefList		= new MdlObjectRefList();
	private String								orderedBy			= "";
	private boolean								orderedAscending	= true;
	
	protected MdlCollection() {
		
	}
	
	public long getCrc() {
		String text = 
			name + Generic.SEP_STR + module + Generic.SEP_STR + entity + Generic.SEP_STR + 
			/*nameSingle + Generic.SEP_STR + nameMulti + Generic.SEP_STR +*/ 
			cacheable + Generic.SEP_STR + 
			/*description + Generic.SEP_STR +*/
			userLevelFetch + Generic.SEP_STR + userLevelUpdate + Generic.SEP_STR + userLevelRemove + Generic.SEP_STR + userLevelAdd + Generic.SEP_STR;
		for (MdlCollectionUniqueConstraint uc: uniqueConstraints) {
			text = text + "\n" + uc.getCrcText();
		}
		for (MdlCollectionProperty p: properties) {
			text = text + "\n" + p.getCrcText();
		}
		return Generic.calculateCRC(text);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	protected void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the module
	 */
	public String getModule() {
		return module;
	}
	
	/**
	 * @param module the module to set
	 */
	protected void setModule(String module) {
		this.module = module;
	}
	
	/**
	 * @return the entity
	 */
	public boolean isEntity() {
		return entity;
	}
	
	/**
	 * @param entity the entity to set
	 */
	protected void setEntity(boolean entity) {
		this.entity = entity;
	}
	
	/**
	 * @return the properties
	 */
	public List<MdlCollectionProperty> getProperties() {
		return properties;
	}
	
	/**
	 * @return the uniqueConstraints
	 */
	public List<MdlCollectionUniqueConstraint> getUniqueConstraints() {
		return uniqueConstraints;
	}

	/**
	 * @return the nameSingle
	 */
	public String getNameSingle() {
		return nameSingle;
	}

	/**
	 * @param nameSingle the nameSingle to set
	 */
	public void setNameSingle(String nameSingle) {
		this.nameSingle = nameSingle;
	}

	/**
	 * @return the nameMulti
	 */
	public String getNameMulti() {
		return nameMulti;
	}

	/**
	 * @param nameMulti the nameMulti to set
	 */
	public void setNameMulti(String nameMulti) {
		this.nameMulti = nameMulti;
	}

	/**
	 * @return the userLevelFetch
	 */
	public int getUserLevelFetch() {
		return userLevelFetch;
	}

	/**
	 * @param userLevelFetch the userLevelFetch to set
	 */
	public void setUserLevelFetch(int userLevelFetch) {
		if (userLevelFetch<1) {
			userLevelFetch=1;
		}
		this.userLevelFetch = userLevelFetch;
	}

	/**
	 * @return the userLevelUpdate
	 */
	public int getUserLevelUpdate() {
		return userLevelUpdate;
	}

	/**
	 * @param userLevelUpdate the userLevelUpdate to set
	 */
	public void setUserLevelUpdate(int userLevelUpdate) {
		if (userLevelUpdate<0) {
			userLevelUpdate=0;
		}
		this.userLevelUpdate = userLevelUpdate;
	}

	/**
	 * @return the userLevelRemove
	 */
	public int getUserLevelRemove() {
		return userLevelRemove;
	}

	/**
	 * @param userLevelRemove the userLevelRemove to set
	 */
	public void setUserLevelRemove(int userLevelRemove) {
		if (userLevelRemove<0) {
			userLevelRemove=0;
		}
		this.userLevelRemove = userLevelRemove;
	}

	/**
	 * @return the userLevelAdd
	 */
	public int getUserLevelAdd() {
		return userLevelAdd;
	}

	/**
	 * @param userLevelAdd the userLevelAdd to set
	 */
	public void setUserLevelAdd(int userLevelAdd) {
		if (userLevelAdd<1) {
			userLevelAdd=1;
		}
		this.userLevelAdd = userLevelAdd;
	}

	/**
	 * @return the cacheable
	 */
	public boolean isCacheable() {
		return cacheable;
	}

	/**
	 * @param cacheable the cacheable to set
	 */
	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the idRefMap
	 */
	public SortedMap<Long, MdlObjectRef> getIdRefMap() {
		return idRefMap;
	}

	/**
	 * @return the nameRefListMap
	 */
	public SortedMap<String, MdlObjectRefList> getNameRefListMap() {
		return nameRefListMap;
	}

	/**
	 * @return the refList
	 */
	public MdlObjectRefList getRefList() {
		MdlObjectRefList refList = new MdlObjectRefList();
		for (Entry<Long, MdlObjectRef> entry: idRefMap.entrySet()) {
			refList.getReferences().add(entry.getValue());
		}
		return refList;
	}
	
	public void clearOrderedRefList() {
		orderedRefList.getReferences().clear();
		orderedBy = "";
		orderedAscending = true;
	}

	/**
	 * @return the orderedBy
	 */
	public String getOrderedBy() {
		return orderedBy;
	}

	/**
	 * @param orderedBy the orderedBy to set
	 */
	public void setOrderedBy(String orderedBy) {
		this.orderedBy = orderedBy;
	}

	/**
	 * @return the orderedAscending
	 */
	public boolean isOrderedAscending() {
		return orderedAscending;
	}

	/**
	 * @param orderedAscending the orderedAscending to set
	 */
	public void setOrderedAscending(boolean orderedAscending) {
		this.orderedAscending = orderedAscending;
	}

	/**
	 * @return the orderedRefList
	 */
	public MdlObjectRefList getOrderedRefList() {
		return orderedRefList;
	}
}
