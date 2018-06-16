package nl.zeesoft.zsmc.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public abstract class EntityObject {
	public static final String					LANG_UNI			= "UNI";
	public static final String					LANG_ENG			= "ENG";
	public static final String					LANG_NED			= "NED";

	public static final String					TYPE_ALPHABETIC		= "ABC";
	public static final String					TYPE_NUMERIC		= "NUM";
	public static final String					TYPE_TIME			= "TIM";
	
	private String								internalValuePrefix	= "";
	private SortedMap<String,EntityValue>		externalValues		= new TreeMap<String,EntityValue>();
	private SortedMap<String,List<EntityValue>>	internalValues		= new TreeMap<String,List<EntityValue>>();
	
	public String getLanguage() {
		return LANG_UNI;
	}

	public String getType() {
		return TYPE_ALPHABETIC;
	}

	public void initialize(String valueConcatenator) {
		internalValuePrefix = getLanguage() + "_" + getType() + valueConcatenator;
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
}
