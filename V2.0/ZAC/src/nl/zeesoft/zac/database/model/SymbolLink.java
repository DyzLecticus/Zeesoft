package nl.zeesoft.zac.database.model;

import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public abstract class SymbolLink extends HlpObject {
	private String 				symbolFrom				= "";
	private String 				symbolTo				= "";
	private long				count					= 0;

	private long				moduleId				= 0;
	private Module				module					= null;
	
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("symbolFrom")) {
			setSymbolFrom(obj.getPropertyValue("symbolFrom").toString());
		}
		if (obj.hasPropertyValue("symbolTo")) {
			setSymbolTo(obj.getPropertyValue("symbolTo").toString());
		}
		if (obj.hasPropertyValue("count")) {
			setCount(Long.parseLong(obj.getPropertyValue("count").toString()));
		}
		if (obj.hasPropertyValue("module") && obj.getLinkValue("module").size()>0) {
			setModuleId(obj.getLinkValue("module").get(0));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("symbolFrom",new StringBuilder(getSymbolFrom()));
		r.setPropertyValue("symbolTo",new StringBuilder(getSymbolTo()));
		r.setPropertyValue("count",new StringBuilder("" + getCount()));
		r.setLinkValue("module",getModuleId());
		return r;
	}	

	/**
	 * @return the moduleId
	 */
	public long getModuleId() {
		return moduleId;
	}

	/**
	 * @param moduleId the moduleId to set
	 */
	public void setModuleId(long moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * @return the module
	 */
	public Module getModule() {
		return module;
	}

	/**
	 * @param module the module to set
	 */
	public void setModule(Module module) {
		this.module = module;
		if (module!=null) {
			moduleId = module.getId();
		} else {
			moduleId = 0;
		}
	}

	/**
	 * @return the symbolFrom
	 */
	public String getSymbolFrom() {
		return symbolFrom;
	}

	/**
	 * @param symbolFrom the symbolFrom to set
	 */
	public void setSymbolFrom(String symbolFrom) {
		this.symbolFrom = symbolFrom;
	}

	/**
	 * @return the symbolTo
	 */
	public String getSymbolTo() {
		return symbolTo;
	}

	/**
	 * @param symbolTo the symbolTo to set
	 */
	public void setSymbolTo(String symbolTo) {
		this.symbolTo = symbolTo;
	}

	/**
	 * @return the count
	 */
	public long getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(long count) {
		this.count = count;
	}
}
