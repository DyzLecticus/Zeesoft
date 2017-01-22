package nl.zeesoft.zodb.model.impl;

import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.ZODBModel;
import nl.zeesoft.zodb.model.annotations.CacheableCollection;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyLength;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyNoSpace;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtString;

@PersistCollection(module=ZODBModel.MODULE_ZODB,nameSingle="Whitelist item",nameMulti="Whitelist items")
@ConstrainObjectAccess(userLevelFetch=10,userLevelUpdate=DbUser.USER_LEVEL_MIN,userLevelRemove=DbUser.USER_LEVEL_MIN,userLevelAdd=DbUser.USER_LEVEL_MIN)
@ConstrainObjectUnique(properties={"name","startsWith"})
@CacheableCollection
@DescribeCollection(
description=
	"Whitelist items define remote address access.\n" +
	"When no active whitelist items are available the database is open to all remote addresses.\n" +
	"Control items indicate the address can access the database control protocol.\n" +
	"")
public class DbWhiteListItem extends MdlDataObject {
	private DtBoolean 				active			= new DtBoolean(true);
	private DtString				startsWith		= new DtString("/127.0.0.1:");
	private DtBoolean 				control			= new DtBoolean(false);

	/**
	 * @return the active
	 */
	@PersistProperty(property="active",label="Active")
	public DtBoolean getActive() {
		return active;
	}

	/**
	 * @return the control
	 */
	@PersistProperty(property="control",label="Server control")
	public DtBoolean getControl() {
		return control;
	}

	/**
	 * @return the startsWith
	 */
	@ConstrainPropertyLength(minValue=0,maxValue=64)
	@ConstrainPropertyNoSpace
	@ConstrainPropertyMandatory
	@PersistProperty(property="startsWith",label="Starts with")
	public DtString getStartsWith() {
		return startsWith;
	}
}
