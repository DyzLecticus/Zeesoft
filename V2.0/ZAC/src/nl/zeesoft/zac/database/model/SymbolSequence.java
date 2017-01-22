package nl.zeesoft.zac.database.model;

import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public abstract class SymbolSequence extends HlpObject {
	private StringBuilder		sequence				= new StringBuilder();
	private String 				contextSymbol1			= "";
	private String 				contextSymbol2			= "";
	private String 				contextSymbol3			= "";
	private String 				contextSymbol4			= "";
	private String 				contextSymbol5			= "";
	private String 				contextSymbol6			= "";
	private String 				contextSymbol7			= "";
	private String 				contextSymbol8			= "";

	private long				moduleId				= 0;
	private Module				module					= null;
	
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("sequence")) {
			setSequence(obj.getPropertyValue("sequence"));
		}
		if (obj.hasPropertyValue("contextSymbol1")) {
			setContextSymbol1(obj.getPropertyValue("contextSymbol1").toString());
		}
		if (obj.hasPropertyValue("contextSymbol2")) {
			setContextSymbol2(obj.getPropertyValue("contextSymbol2").toString());
		}
		if (obj.hasPropertyValue("contextSymbol3")) {
			setContextSymbol3(obj.getPropertyValue("contextSymbol3").toString());
		}
		if (obj.hasPropertyValue("contextSymbol4")) {
			setContextSymbol4(obj.getPropertyValue("contextSymbol4").toString());
		}
		if (obj.hasPropertyValue("contextSymbol5")) {
			setContextSymbol5(obj.getPropertyValue("contextSymbol5").toString());
		}
		if (obj.hasPropertyValue("contextSymbol6")) {
			setContextSymbol6(obj.getPropertyValue("contextSymbol6").toString());
		}
		if (obj.hasPropertyValue("contextSymbol7")) {
			setContextSymbol7(obj.getPropertyValue("contextSymbol7").toString());
		}
		if (obj.hasPropertyValue("contextSymbol8")) {
			setContextSymbol8(obj.getPropertyValue("contextSymbol8").toString());
		}
		if (obj.hasPropertyValue("module") && obj.getLinkValue("module").size()>0) {
			setModuleId(obj.getLinkValue("module").get(0));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("sequence",new StringBuilder(getSequence()));
		r.setPropertyValue("contextSymbol1",new StringBuilder(getContextSymbol1()));
		r.setPropertyValue("contextSymbol2",new StringBuilder(getContextSymbol2()));
		r.setPropertyValue("contextSymbol3",new StringBuilder(getContextSymbol3()));
		r.setPropertyValue("contextSymbol4",new StringBuilder(getContextSymbol4()));
		r.setPropertyValue("contextSymbol5",new StringBuilder(getContextSymbol5()));
		r.setPropertyValue("contextSymbol6",new StringBuilder(getContextSymbol6()));
		r.setPropertyValue("contextSymbol7",new StringBuilder(getContextSymbol7()));
		r.setPropertyValue("contextSymbol8",new StringBuilder(getContextSymbol8()));
		r.setLinkValue("module",getModuleId());
		return r;
	}	

	/**
	 * @return the sequence
	 */
	public StringBuilder getSequence() {
		return sequence;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(StringBuilder sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the contextSymbol1
	 */
	public String getContextSymbol1() {
		return contextSymbol1;
	}

	/**
	 * @param contextSymbol1 the contextSymbol1 to set
	 */
	public void setContextSymbol1(String contextSymbol1) {
		this.contextSymbol1 = contextSymbol1;
	}

	/**
	 * @return the contextSymbol2
	 */
	public String getContextSymbol2() {
		return contextSymbol2;
	}

	/**
	 * @param contextSymbol2 the contextSymbol2 to set
	 */
	public void setContextSymbol2(String contextSymbol2) {
		this.contextSymbol2 = contextSymbol2;
	}

	/**
	 * @return the contextSymbol3
	 */
	public String getContextSymbol3() {
		return contextSymbol3;
	}

	/**
	 * @param contextSymbol3 the contextSymbol3 to set
	 */
	public void setContextSymbol3(String contextSymbol3) {
		this.contextSymbol3 = contextSymbol3;
	}

	/**
	 * @return the contextSymbol4
	 */
	public String getContextSymbol4() {
		return contextSymbol4;
	}

	/**
	 * @param contextSymbol4 the contextSymbol4 to set
	 */
	public void setContextSymbol4(String contextSymbol4) {
		this.contextSymbol4 = contextSymbol4;
	}

	/**
	 * @return the contextSymbol5
	 */
	public String getContextSymbol5() {
		return contextSymbol5;
	}

	/**
	 * @param contextSymbol5 the contextSymbol5 to set
	 */
	public void setContextSymbol5(String contextSymbol5) {
		this.contextSymbol5 = contextSymbol5;
	}

	/**
	 * @return the contextSymbol6
	 */
	public String getContextSymbol6() {
		return contextSymbol6;
	}

	/**
	 * @param contextSymbol6 the contextSymbol6 to set
	 */
	public void setContextSymbol6(String contextSymbol6) {
		this.contextSymbol6 = contextSymbol6;
	}

	/**
	 * @return the contextSymbol7
	 */
	public String getContextSymbol7() {
		return contextSymbol7;
	}

	/**
	 * @param contextSymbol7 the contextSymbol7 to set
	 */
	public void setContextSymbol7(String contextSymbol7) {
		this.contextSymbol7 = contextSymbol7;
	}

	/**
	 * @return the contextSymbol8
	 */
	public String getContextSymbol8() {
		return contextSymbol8;
	}

	/**
	 * @param contextSymbol8 the contextSymbol8 to set
	 */
	public void setContextSymbol8(String contextSymbol8) {
		this.contextSymbol8 = contextSymbol8;
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
}
