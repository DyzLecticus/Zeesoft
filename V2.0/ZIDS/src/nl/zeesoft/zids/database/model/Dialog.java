package nl.zeesoft.zids.database.model;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class Dialog extends HlpObject {
	private String					name					= "";
	private String					contextSymbol			= "";
	private String					controllerClassName		= "";
	
	private List<DialogExample>		examples				= new ArrayList<DialogExample>();
	private List<DialogVariable>	variables				= new ArrayList<DialogVariable>();
	
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("name")) {
			setName(obj.getPropertyValue("name").toString());
		}
		if (obj.hasPropertyValue("contextSymbol")) {
			setContextSymbol(obj.getPropertyValue("contextSymbol").toString());
		}
		if (obj.hasPropertyValue("controllerClassName")) {
			setControllerClassName(obj.getPropertyValue("controllerClassName").toString());
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("name",new StringBuilder(getName()));
		r.setPropertyValue("contextSymbol",new StringBuilder(getContextSymbol()));
		r.setPropertyValue("controllerClassName",new StringBuilder(getControllerClassName()));
		return r;
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
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the contextSymbol
	 */
	public String getContextSymbol() {
		return contextSymbol;
	}

	/**
	 * @param contextSymbol the contextSymbol to set
	 */
	public void setContextSymbol(String contextSymbol) {
		this.contextSymbol = contextSymbol;
	}

	/**
	 * @return the examples
	 */
	public List<DialogExample> getExamples() {
		return examples;
	}

	/**
	 * @return the variables
	 */
	public List<DialogVariable> getVariables() {
		return variables;
	}

	/**
	 * @return the controllerClassName
	 */
	public String getControllerClassName() {
		return controllerClassName;
	}

	/**
	 * @param controllerClassName the controllerClassName to set
	 */
	public void setControllerClassName(String controllerClassName) {
		this.controllerClassName = controllerClassName;
	}

}
