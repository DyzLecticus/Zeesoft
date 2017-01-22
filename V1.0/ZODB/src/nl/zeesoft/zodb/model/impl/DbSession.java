package nl.zeesoft.zodb.model.impl;

import nl.zeesoft.zodb.model.ZODBModel;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtString;

@PersistCollection(module=ZODBModel.MODULE_ZODB,nameSingle="Session",nameMulti="Sessions")
@ConstrainObjectAccess(userLevelFetch=10,userLevelUpdate=DbUser.USER_LEVEL_MIN,userLevelRemove=DbUser.USER_LEVEL_OFF,userLevelAdd=DbUser.USER_LEVEL_MIN)
@DescribeCollection(
description=
	"Sessions log remote address access.\n" +
	"Sessions cannot be removed.\n" +
	"")
public class DbSession extends MdlDataObject {
	public final static String		OPEN			= "OPEN";
	public final static String		CLOSED			= "CLOSED";
	
	private	DbUser					dbUser			= null;
	
	private DtIdRef 				user			= new DtIdRef();
	private DtString				ipAndPort		= new DtString();
	private DtDateTime 				started			= new DtDateTime();
	private DtDateTime 				ended			= new DtDateTime();

	/**
	 * @return the dbUser
	 */
	public DbUser getDbUser() {
		return dbUser;
	}

	/**
	 * @param dbUser the dbUser to set
	 */
	public void setDbUser(DbUser dbUser) {
		this.dbUser = dbUser;
		getUser().setValue(dbUser);
	}

	/**
	 * @return the user
	 */
	@PersistReference(property="user",className="nl.zeesoft.zodb.model.impl.DbUser",removeMe=false,entity=true,label="User",entityLabel="Sessions")
	public DtIdRef getUser() {
		return user;
	}

	/**
	 * @return the ipAndPort
	 */
	@PersistProperty(property="ipAndPort",label="IP and port")
	public DtString getIpAndPort() {
		return ipAndPort;
	}

	/**
	 * @return the started
	 */
	@PersistProperty(property = "started",label="Started")
	public DtDateTime getStarted() {
		return started;
	}

	/**
	 * @return the ended
	 */
	@PersistProperty(property = "ended",label="Ended")
	public DtDateTime getEnded() {
		return ended;
	}

}
