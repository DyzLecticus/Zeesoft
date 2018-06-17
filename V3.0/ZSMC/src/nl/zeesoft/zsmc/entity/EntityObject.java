package nl.zeesoft.zsmc.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zsmc.EntityValueTranslator;

public abstract class EntityObject {
	public static final String					LANG_UNI			= "UNI";
	public static final String					LANG_ENG			= "ENG";
	public static final String					LANG_NLD			= "NLD";

	public static final String					TYPE_ALPHABETIC		= "ABC";
	public static final String					TYPE_NUMERIC		= "NUM";
	public static final String					TYPE_TIME			= "TIM";
	public static final String					TYPE_ORDER			= "ORD";
	public static final String					TYPE_ORDER2			= "OR2";
	public static final String					TYPE_MONTH			= "MNT";
	public static final String					TYPE_DURATION		= "DUR";
	public static final String					TYPE_DATE			= "DAT";
	
	private boolean								initialized			= false;
	private String								internalValuePrefix	= "";
	private SortedMap<String,EntityValue>		externalValues		= new TreeMap<String,EntityValue>();
	private SortedMap<String,List<EntityValue>>	internalValues		= new TreeMap<String,List<EntityValue>>();
	
	public String getLanguage() {
		return LANG_UNI;
	}

	public String getType() {
		return TYPE_ALPHABETIC;
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
	
	public boolean externalValuesContains(String str) {
		return externalValues.containsKey(str);
	}

	public boolean internalValuesContains(String str) {
		return externalValues.containsKey(str);
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
}
