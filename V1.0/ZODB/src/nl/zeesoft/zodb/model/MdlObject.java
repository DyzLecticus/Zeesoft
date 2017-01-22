package nl.zeesoft.zodb.model;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyLength;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtLong;
import nl.zeesoft.zodb.model.datatypes.DtObject;
import nl.zeesoft.zodb.model.datatypes.DtString;

public abstract class MdlObject {
	public final static String 	PROPERTY_CLASSNAME		= "className";
	public final static String 	PROPERTY_ID 			= "id";
	public final static String 	PROPERTY_NAME			= "name";
	public final static String 	PROPERTY_CREATEDBY		= "createdBy";
	public final static String 	PROPERTY_CHANGEDBY		= "changedBy";
	public final static String 	PROPERTY_CREATEDON		= "createdOn";
	public final static String 	PROPERTY_CHANGEDON		= "changedOn";

	private DtString			className 				= new DtString();
	private DtLong				id						= new DtLong();
	private DtString			name 					= new DtString();
	private DtIdRef				createdBy				= new DtIdRef();
	private DtIdRef				changedBy				= new DtIdRef();
	private DtDateTime			createdOn				= new DtDateTime();
	private DtDateTime			changedOn				= new DtDateTime();

	public MdlObject() {
		className.setValue(this.getClass().getName());
	}

	public final String getFullFileName() {
		return (Generic.dirName(DbConfig.getInstance().getFullDataDir() + getClassName().getValue()) + getId().getValue() + ".xml");
	}

	public final DtObject getPropertyValue(String property) {
		DtObject v = null;
		MdlCollectionProperty p = DbConfig.getInstance().getModel().getCollectionPropertyByName(this.getClass().getName(),property);
		if (p!=null) {
			v = (DtObject) Generic.executeObjectClassMethod(this, p.getMethod(), null);
		}
		return v;
	}

	public final void setPropertyValue(String property,DtObject value) {
		DtObject v = null;
		MdlCollectionProperty p = DbConfig.getInstance().getModel().getCollectionPropertyByName(this.getClass().getName(),property);
		if (p!=null) {
			v = (DtObject) Generic.executeObjectClassMethod(this, p.getMethod(), null);
			v.setValue(value.getValue());
		}
	}
	
	/**
	 * @return the className
	 */
	@PersistProperty(property = PROPERTY_CLASSNAME,label="Class name")
	public DtString getClassName() {
		return className;
	}

	/**
	 * @return the id
	 */
	@PersistProperty(property = PROPERTY_ID,label="ID")
	public DtLong getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	@ConstrainPropertyLength(minValue=0,maxValue=64)
	@ConstrainPropertyMandatory
	@PersistProperty(property = PROPERTY_NAME,label="Name")
	public DtString getName() {
		return name;
	}

	/**
	 * @return the createdOn
	 */
	@ConstrainPropertyMandatory
	@PersistProperty(property = PROPERTY_CREATEDON,label="Created on")
	public DtDateTime getCreatedOn() {
		return createdOn;
	}

	/**
	 * @return the createdBy
	 */
	@ConstrainPropertyMandatory
	@PersistReference(property = PROPERTY_CREATEDBY,className="nl.zeesoft.zodb.model.impl.DbUser",entity=false,removeMe=false,label="Created by",entityLabel="Created objects")
	public DtIdRef getCreatedBy() {
		return createdBy;
	}

	/**
	 * @return the changedOn
	 */
	@ConstrainPropertyMandatory
	@PersistProperty(property = PROPERTY_CHANGEDON,label="Changed on")
	public DtDateTime getChangedOn() {
		return changedOn;
	}

	/**
	 * @return the changedBy
	 */
	@ConstrainPropertyMandatory
	@PersistReference(property = PROPERTY_CHANGEDBY,className="nl.zeesoft.zodb.model.impl.DbUser",entity=false,removeMe=false,label="Changed by",entityLabel="Changed objects")
	public DtIdRef getChangedBy() {
		return changedBy;
	}

}
