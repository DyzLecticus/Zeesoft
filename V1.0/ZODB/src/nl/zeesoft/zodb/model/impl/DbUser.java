package nl.zeesoft.zodb.model.impl;

import nl.zeesoft.zodb.client.ClConfig;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.ZODBModel;
import nl.zeesoft.zodb.model.annotations.CacheableCollection;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyAlphanumericUnderscore;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyLength;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyPassword;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyRange;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtInteger;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;

@PersistCollection(module=ZODBModel.MODULE_ZODB,nameSingle="User",nameMulti="Users")
@CacheableCollection
@ConstrainObjectUnique
@ConstrainObjectAccess(userLevelFetch=10,userLevelUpdate=10,userLevelRemove=DbUser.USER_LEVEL_OFF,userLevelAdd=10)
@DescribeCollection(
description=
	"Users define the password and access level for specific individuals.\n" + 
	"The highest user level is " + DbUser.USER_LEVEL_MIN + " and the lowest is " + DbUser.USER_LEVEL_MAX + ".\n" + 
	"Users with administrator privilege can access the database control protocol.\n" + 
	"Users cannot be removed but they can be deactivated.\n" + 
	"")
public class DbUser extends MdlDataObject {
	public static final int			USER_LEVEL_OFF		= 0;
	public static final int			USER_LEVEL_MIN		= 1; // Highest level
	public static final int			USER_LEVEL_MAX		= 999999; // Lowest level
	
	private DtStringBuffer			password			= new DtStringBuffer();
	private DtBoolean 				admin 				= new DtBoolean();
	private DtBoolean 				active 				= new DtBoolean(true);
	private DtInteger 				level 				= new DtInteger(USER_LEVEL_MAX);

	@Override
	public boolean checkObjectConstraintsForUserQuery(DbUser user, QryObject q) {
		boolean ok = super.checkObjectConstraintsForUserQuery(user, q);
		if (ok) {
			DbUser changedObject = null;
			if (q instanceof QryAdd) {
				changedObject = (DbUser) ((QryAdd) q).getDataObject();
			} else if (q instanceof QryUpdate) {
				if (getLevel().getValue()<user.getLevel().getValue()) {
					q.addError("2004", "Updating a higher level user is not allowed");
					ok = false;
				} else {
					changedObject = (DbUser) ((QryUpdate) q).getDataObject();
				}
			}
			if (changedObject!=null) {
				if (changedObject.getLevel().getValue()>USER_LEVEL_MAX) {
					changedObject.getLevel().setValue(USER_LEVEL_MAX);
				} else if (changedObject.getLevel().getValue()<USER_LEVEL_MIN) {
					changedObject.getLevel().setValue(USER_LEVEL_MIN);
				} else if (changedObject.getLevel().getValue()<user.getLevel().getValue()) {
					changedObject.getLevel().setValue(user.getLevel().getValue());
				}
				if (q instanceof QryUpdate) {
					if (getId().getValue()==1) {
						// Updating admin user
						if (!changedObject.getAdmin().getValue()) { 
							changedObject.getAdmin().setValue(true);
						}
						if (changedObject.getLevel().getValue()!=1) { 
							changedObject.getLevel().setValue(1);
						}
						ClConfig.getInstance().setUserName(changedObject.getName().getValue());
						ClConfig.getInstance().setUserPassword(new StringBuffer(changedObject.getPassword().getValue()));
						ClConfig.getInstance().serialize();
					}
				}
			}
		}
		return ok;
	}
	
	/**
	 * @return the admin
	 */
	@PersistProperty(property = "admin",label="Administrator")
	public DtBoolean getAdmin() {
		return admin;
	}

	/**
	 * @return the active
	 */
	@PersistProperty(property = "active",label="Active")
	public DtBoolean getActive() {
		return active;
	}

	/**
	 * @return the password
	 */
	@ConstrainPropertyLength(minValue=8,maxValue=64)
	@ConstrainPropertyMandatory
	@ConstrainPropertyPassword
	@PersistProperty(property = "password",label="Password")
	public DtStringBuffer getPassword() {
		return password;
	}

	/**
	 * @return the level
	 */
	@ConstrainPropertyRange(minValue=USER_LEVEL_MIN,maxValue=USER_LEVEL_MAX)
	@PersistProperty(property = "level",label="Level")
	public DtInteger getLevel() {
		return level;
	}

	/**
	 * @return the name
	 */
	@ConstrainPropertyLength(minValue=0,maxValue=64)
	@ConstrainPropertyMandatory
	@ConstrainPropertyAlphanumericUnderscore
	@PersistProperty(property = PROPERTY_NAME,label="Name")
	@Override
	public DtString getName() {
		return super.getName();
	}
}
