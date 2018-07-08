package nl.zeesoft.zsd.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;

public abstract class EntityObject {
	private boolean								initialized			= false;
	private String								internalValuePrefix	= "";
	private SortedMap<String,EntityValue>		externalValues		= new TreeMap<String,EntityValue>();
	private SortedMap<String,List<EntityValue>>	internalValues		= new TreeMap<String,List<EntityValue>>();
	
	public String getLanguage() {
		return BaseConfiguration.LANG_UNI;
	}

	public String getType() {
		return BaseConfiguration.TYPE_ALPHABETIC;
	}

	/**
	 * Returns the maximum symbol sequence length this entity contains.
	 * 
	 * @return The maximum symbol sequence length
	 */
	public int getMaximumSymbols() {
		return 1;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void initialize(EntityValueTranslator translator) {
		initialized = true;
		internalValuePrefix = getLanguage() + "_" + getType() + translator.getValueConcatenator();
	}

	public final String getInternalValuePrefix() {
		return internalValuePrefix;
	}

	public String getInternalValueForExternalValue(String str) {
		String r = "";
		EntityValue ev = externalValues.get(str);
		if (ev!=null) {
			r = ev.internalValue;
		}
		return r;
	}

	public String getExternalValueForInternalValue(String str) {
		String r = "";
		if (!str.startsWith(internalValuePrefix)) {
			str = internalValuePrefix + str;
		}
		List<EntityValue> evl = internalValues.get(str);
		if (evl!=null) {
			r = evl.get(0).externalValue;
		}
		return r;
	}

	public Object getTypeValueForInternalValue(String str) {
		Object r = "";
		List<EntityValue> evl = internalValues.get(str);
		if (evl!=null) {
			r = evl.get(0).typeValue;
		}
		return r;
	}

	public EntityValue getEntityValue(String externalValue, String internalValue, Object typeValue) {
		EntityValue r = new EntityValue();
		r.externalValue = externalValue;
		r.internalValue = getInternalValuePrefix() + internalValue;
		r.typeValue = typeValue;
		return r;
	}

	public void addEntityValue(EntityValue value) {
		externalValues.put(value.externalValue,value);
		List<EntityValue> evl = internalValues.get(value.internalValue);
		if (evl==null) {
			evl = new ArrayList<EntityValue>();
			internalValues.put(value.internalValue,evl);
		}
		evl.add(value);
	}

	public void addEntityValue(String externalValue, String internalValue, Object typeValue) {
		addEntityValue(getEntityValue(externalValue,internalValue,typeValue));
	}

	public void addEntityValue(String externalValue, Object typeValue) {
		addEntityValue(getEntityValue(externalValue,"" + typeValue,typeValue));
	}

	public void addEntityValue(String externalValue) {
		Integer idx = new Integer(externalValues.size());
		addEntityValue(getEntityValue(externalValue,"" + idx,idx));
	}

	public SortedMap<String, EntityValue> getExternalValues() {
		return externalValues;
	}
}
