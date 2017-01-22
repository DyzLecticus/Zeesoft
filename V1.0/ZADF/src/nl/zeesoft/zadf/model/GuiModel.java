package nl.zeesoft.zadf.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zadf.gui.GuiConfig;
import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionFilter;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.file.FileObject;
import nl.zeesoft.zodb.file.XMLElem;
import nl.zeesoft.zodb.file.XMLFile;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.datatypes.DtObject;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;

public final class GuiModel {
	private static GuiModel								model					= null;

	private SortedMap<String,MdlDataObject>					contextObjectMap		= new TreeMap<String,MdlDataObject>();
	
	private SortedMap<String,Long>							contextIdMap			= new TreeMap<String,Long>();
	private SortedMap<String,Integer>						contextStartMap			= new TreeMap<String,Integer>();
	private SortedMap<String,Integer>						contextLimitMap			= new TreeMap<String,Integer>();
	private SortedMap<String,DbCollectionProperty>			contextOrderByMap		= new TreeMap<String,DbCollectionProperty>();
	private SortedMap<String,Boolean>						contextAscendingMap		= new TreeMap<String,Boolean>();

	private SortedMap<String,List<GuiModelContextFilter>>	contextFiltersMap		= new TreeMap<String,List<GuiModelContextFilter>>();

	private SortedMap<String,Boolean>						contextActiveMap		= new TreeMap<String,Boolean>();

	private boolean											unserialized			= false;
	
	private GuiModel() {
		// Singleton
	}

	public static GuiModel getInstance() {
		if (model==null) {
			model = new GuiModel();
		}
		return model;
	}
	
	public void setContextObject(String context, MdlDataObject object) {
		if ((context.equals("")) || (object==null)) {
			return;
		}
		if (contextObjectMap.containsKey(context)) {
			contextObjectMap.remove(context);
		}
		contextObjectMap.put(context, object);
	}
	
	public MdlDataObject getObjectForContext(String context) {
		MdlDataObject object = null;
		if (contextObjectMap.containsKey(context)) {
			object = contextObjectMap.get(context);
		}
		return object;
	}
	
	public void setContextId(String context, Long id) {
		if ((context.equals("")) || (id == 0)) {
			return;
		}
		if (contextIdMap.containsKey(context)) {
			contextIdMap.remove(context);
		}
		contextIdMap.put(context, id);
	}
	
	public long getIdForContext(String context) {
		long id = 0;
		if (contextIdMap.containsKey(context)) {
			id = contextIdMap.get(context);
		}
		return id;
	}

	public void setContextStart(String context, Integer start) {
		if (context.equals("")) {
			return;
		}
		if (contextStartMap.containsKey(context)) {
			contextStartMap.remove(context);
		}
		contextStartMap.put(context, start);
	}
	
	public int getStartForContext(String context) {
		int start = 0;
		if (contextStartMap.containsKey(context)) {
			start = contextStartMap.get(context);
		}
		return start;
	}

	public void setContextLimit(String context, Integer limit) {
		if (context.equals("")) {
			return;
		}
		if (contextLimitMap.containsKey(context)) {
			contextLimitMap.remove(context);
		}
		contextLimitMap.put(context, limit);
	}
	
	public int getLimitForContext(String context) {
		int limit = 100;
		if (contextLimitMap.containsKey(context)) {
			limit = contextLimitMap.get(context);
		}
		return limit;
	}

	public void setContextOrderBy(String context, DbCollectionProperty orderBy) {
		if (context.equals("")) {
			return;
		}
		if (contextOrderByMap.containsKey(context)) {
			contextOrderByMap.remove(context);
		}
		contextOrderByMap.put(context, orderBy);
	}
	
	public DbCollectionProperty getOrderByForContext(String context) {
		DbCollectionProperty orderBy = null;
		if (contextOrderByMap.containsKey(context)) {
			orderBy = contextOrderByMap.get(context);
		}
		return orderBy;
	}

	public void setContextAscending(String context, Boolean ascending) {
		if (context.equals("")) {
			return;
		}
		if (contextAscendingMap.containsKey(context)) {
			contextAscendingMap.remove(context);
		}
		contextAscendingMap.put(context, ascending);
	}
	
	public boolean getAscendingForContext(String context) {
		boolean ascending = true;
		if (contextAscendingMap.containsKey(context)) {
			ascending = contextAscendingMap.get(context);
		}
		return ascending;
	}
	
	public void setContextFilters(String context, List<GuiModelContextFilter> filters) {
		if (context.equals("")) {
			return;
		}
		if (contextFiltersMap.containsKey(context)) {
			contextFiltersMap.remove(context);
		}
		contextFiltersMap.put(context, filters);
	}
	
	public List<GuiModelContextFilter> getFiltersForContext(String context) {
		List<GuiModelContextFilter> filters = new ArrayList<GuiModelContextFilter>();
		if (contextFiltersMap.containsKey(context)) {
			filters = contextFiltersMap.get(context);
		}
		return filters;
	}

	public void setContextActive(String context, Boolean active) {
		if (context.equals("")) {
			return;
		}
		if (contextActiveMap.containsKey(context)) {
			contextActiveMap.remove(context);
		}
		contextActiveMap.put(context, active);
	}
	
	public boolean getActiveForContext(String context) {
		boolean active = true;
		if (contextActiveMap.containsKey(context)) {
			active = contextActiveMap.get(context);
		}
		return active;
	}
	
	public void serialize() {
		if (unserialized) {
			Messenger.getInstance().debug(this,"Serializing GUI model ...");
			XMLFile f = toXml();
			f.writeFile(getFileName(), f.toStringCompressed());
			f.cleanUp();
		}
	}
	
	public void unserialize() {
		if (GuiConfig.getInstance().isLoadGuiModelOnStart()) {
			if (FileObject.fileExists(getFileName())) { 
				Messenger.getInstance().debug(this, "Unserializing GUI model ... ");
				XMLFile f = new XMLFile();
				f.parseFile(getFileName());
				fromXml(f);
				f.cleanUp();
			}
		}
		unserialized = true;
	}

	private String getFileName() {
		return DbConfig.getInstance().getFullDataDir() + "GuiModel.xml";
	}
	
	private XMLFile toXml() {
		XMLFile f = new XMLFile();
		f.setRootElement(new XMLElem("model",null,null));
		
		XMLElem objsElem = new XMLElem("contextObjectMap",null,f.getRootElement());
		for (Entry<String,MdlDataObject> entry: contextObjectMap.entrySet()) {
			XMLElem objElem = new XMLElem("contextObject",null,objsElem);
			new XMLElem("context",new StringBuffer(entry.getKey()),objElem);
			new XMLElem("className",new StringBuffer(entry.getValue().getClassName().getValue()),objElem);
			StringBuffer sb = new StringBuffer();
			sb.append(entry.getValue().getId().getValue());
			new XMLElem("id",sb,objElem);
		}

		XMLElem idsElem = new XMLElem("contextIdMap",null,f.getRootElement());
		for (Entry<String,Long> entry: contextIdMap.entrySet()) {
			XMLElem idElem = new XMLElem("contextId",null,idsElem);
			new XMLElem("context",new StringBuffer(entry.getKey()),idElem);
			StringBuffer sb = new StringBuffer();
			sb.append(entry.getValue());
			new XMLElem("id",sb,idElem);
		}

		XMLElem slsElem = new XMLElem("contextStartLimitMap",null,f.getRootElement());
		for (Entry<String,Integer> entry: contextStartMap.entrySet()) {
			XMLElem slElem = new XMLElem("contextStartLimit",null,slsElem);
			int limit = getLimitForContext(entry.getKey());
			new XMLElem("context",new StringBuffer(entry.getKey()),slElem);
			StringBuffer sb = new StringBuffer();
			sb.append(entry.getValue());
			new XMLElem("start",sb,slElem);
			sb = new StringBuffer();
			sb.append(limit);
			new XMLElem("limit",sb,slElem);
		}

		XMLElem obsElem = new XMLElem("contextOrderByMap",null,f.getRootElement());
		for (Entry<String,DbCollectionProperty> entry: contextOrderByMap.entrySet()) {
			XMLElem obElem = new XMLElem("contextOrderBy",null,obsElem);
			boolean asc = getAscendingForContext(entry.getKey());
			new XMLElem("context",new StringBuffer(entry.getKey()),obElem);
			StringBuffer sb = new StringBuffer();
			sb.append(entry.getValue().getId());
			new XMLElem("orderBy",sb,obElem);
			sb = new StringBuffer();
			sb.append(asc);
			new XMLElem("ascending",sb,obElem);
		}

		XMLElem actsElem = new XMLElem("contextActiveMap",null,f.getRootElement());
		for (Entry<String,Boolean> entry: contextActiveMap.entrySet()) {
			XMLElem idElem = new XMLElem("contextActive",null,actsElem);
			new XMLElem("context",new StringBuffer(entry.getKey()),idElem);
			StringBuffer sb = new StringBuffer();
			sb.append(entry.getValue());
			new XMLElem("active",sb,idElem);
		}
		
		XMLElem filtsElem = new XMLElem("contextFiltersMap",null,f.getRootElement());
		for (Entry<String,List<GuiModelContextFilter>> entry: contextFiltersMap.entrySet()) {
			String[] vals = Generic.getValuesFromString(entry.getKey()); 
			DbCollection col = (DbCollection) DbModel.getInstance().getCollectionByName(vals[0]);
			if (col!=null) {
				XMLElem filtElem = new XMLElem("contextFilter",null,filtsElem);
				new XMLElem("context",new StringBuffer(entry.getKey()),filtElem);
				XMLElem cfElem = new XMLElem("filters",null,filtElem);
				for (GuiModelContextFilter filter: entry.getValue()) {
					boolean add = true;
					for (DbCollectionFilter colFilt: col.getFilters()) {
						if (colFilt.getProp().getName().getValue().equals(filter.getProperty())) {
							add = false;
							break;
						}
					}
					if (add) {
						XMLElem fElem = new XMLElem("filter",new StringBuffer(entry.getKey()),cfElem);
						StringBuffer sb = new StringBuffer();
						sb.append(filter.isActive());
						new XMLElem("active",sb,fElem);
						new XMLElem("property",new StringBuffer(filter.getProperty()),fElem);
						sb = new StringBuffer();
						sb.append(filter.isInvert());
						new XMLElem("invert",sb,fElem);
						new XMLElem("operator",new StringBuffer(filter.getOperator()),fElem);
						new XMLElem("value",filter.getValue().toStringBuffer(),fElem);
						new XMLElem("valueClassName",new StringBuffer(filter.getValue().getClass().getName()),fElem);
						new XMLElem("stringValue",new StringBuffer(filter.getStringValue()),fElem);
					}
				}
			}
		}
		return f;
	}

	private void fromXml(XMLFile f) {
		if ((f.getRootElement()!=null) && (f.getRootElement().getName().equals("model"))) {
			for (XMLElem mapElem: f.getRootElement().getChildren()) {
				if (mapElem.getName().equals("contextObjectMap")) {
					contextObjectMap.clear();
					for (XMLElem objElem: mapElem.getChildren()) {
						String context = objElem.getChildByName("context").getValue().toString();
						String className = objElem.getChildByName("className").getValue().toString();
						long id = Long.parseLong(objElem.getChildByName("id").getValue().toString());
						MdlDataObject obj = DbModel.getInstance().getCollectionObjectById(className,id);
						contextObjectMap.put(context, obj);
					}
				}
				if (mapElem.getName().equals("contextIdMap")) {
					contextIdMap.clear();
					for (XMLElem objElem: mapElem.getChildren()) {
						String context = objElem.getChildByName("context").getValue().toString();
						long id = Long.parseLong(objElem.getChildByName("id").getValue().toString());
						contextIdMap.put(context, id);
					}
				}
				if (mapElem.getName().equals("contextStartLimitMap")) {
					contextStartMap.clear();
					contextLimitMap.clear();
					for (XMLElem objElem: mapElem.getChildren()) {
						String context = objElem.getChildByName("context").getValue().toString();
						int start = Integer.parseInt(objElem.getChildByName("start").getValue().toString());
						int limit = Integer.parseInt(objElem.getChildByName("limit").getValue().toString());
						contextStartMap.put(context, start);
						contextLimitMap.put(context, limit);
					}
				}
				if (mapElem.getName().equals("contextOrderByMap")) {
					contextOrderByMap.clear();
					contextAscendingMap.clear();
					for (XMLElem objElem: mapElem.getChildren()) {
						String context = objElem.getChildByName("context").getValue().toString();
						long id = Long.parseLong(objElem.getChildByName("orderBy").getValue().toString());
						boolean asc = Boolean.parseBoolean(objElem.getChildByName("ascending").getValue().toString());
						DbCollectionProperty orderBy = (DbCollectionProperty) DbModel.getInstance().getCollectionObjectById(DbCollectionProperty.class.getName(),id);
						contextOrderByMap.put(context, orderBy);
						contextAscendingMap.put(context, asc);
					}
				}
				if (mapElem.getName().equals("contextActiveMap")) {
					contextActiveMap.clear();
					for (XMLElem objElem: mapElem.getChildren()) {
						String context = objElem.getChildByName("context").getValue().toString();
						boolean act = Boolean.parseBoolean(objElem.getChildByName("active").getValue().toString());
						contextActiveMap.put(context, act);
					}
				}
				if (mapElem.getName().equals("contextFiltersMap")) {
					contextFiltersMap.clear();
					for (XMLElem objElem: mapElem.getChildren()) {
						String context = objElem.getChildByName("context").getValue().toString();
						List<GuiModelContextFilter> filters = new ArrayList<GuiModelContextFilter>();
						for (XMLElem fElem: objElem.getChildByName("filters").getChildren()) {
							boolean active = Boolean.parseBoolean(fElem.getChildByName("active").getValue().toString());
							String property = fElem.getChildByName("property").getValue().toString();
							boolean invert = Boolean.parseBoolean(fElem.getChildByName("invert").getValue().toString());
							String operator = fElem.getChildByName("operator").getValue().toString();
							DtObject valueObj = (DtObject) Generic.instanceForName(fElem.getChildByName("valueClassName").getValue().toString());
							if (
								(fElem.getChildByName("value").getValue().length()>0) ||
								(valueObj instanceof DtString) ||
								(valueObj instanceof DtStringBuffer)
								){
								valueObj.fromString(new StringBuffer(fElem.getChildByName("value").getValue()));
							}
							String stringValue = fElem.getChildByName("stringValue").getValue().toString();
							filters.add(new GuiModelContextFilter(active, property, invert, operator, valueObj, stringValue));
						}
						contextFiltersMap.put(context, filters);
					}
				}
			}
		}
	}
	
}
