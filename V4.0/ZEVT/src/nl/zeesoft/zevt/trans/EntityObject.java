package nl.zeesoft.zevt.trans;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zevt.type.Types;
import nl.zeesoft.znlb.lang.Languages;

public abstract class EntityObject {
	private Translator							translator					= null;
	
	private boolean								initialized					= false;
	private String								internalValuePrefix			= "";
	private SortedMap<String,EntityValue>		externalValues				= new TreeMap<String,EntityValue>();
	private SortedMap<String,List<EntityValue>>	internalValues				= new TreeMap<String,List<EntityValue>>();
	
	public EntityObject(Translator t) {
		translator = t;
		internalValuePrefix = getLanguage() + "_" + getType() + Translator.VALUE_CONCATENATOR;
	}
	
	public String getLanguage() {
		return Languages.UNI;
	}

	public String getType() {
		return Types.ALPHABETIC;
	}

	public String getName() {
		String[] split = (getClass().getName()).split("\\.");
		return split[(split.length - 1)];
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
	
	public void initialize() {
		initializeEntityValues();
		initialized();
	}
	
	public abstract void initializeEntityValues();

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
	
	public static String upperCaseFirst(String str) {
		String r = str.substring(0,1).toUpperCase();
		if (str.length()>1) {
			r += str.substring(1).toLowerCase();
		}
		return r;
	}
	
	protected Translator getTranslator() {
		return translator;
	}
	
	/**
	 * Must be called after initialization
	 */
	protected void initialized() {
		initialized = true;
		translator.initializedEntity(this);
	}
	
	protected void clear() {
		externalValues.clear();
		internalValues.clear();
		initialized = false;
	}

	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("language",getLanguage(),true));
		JsElem evsElem = new JsElem("entityValues",true);
		json.rootElement.children.add(evsElem);
		for (EntityValue ev: getExternalValues().values()) {
			evsElem.children.add(new JsElem(null,ev.externalValue,true));
		}
		return json;
	}
}
