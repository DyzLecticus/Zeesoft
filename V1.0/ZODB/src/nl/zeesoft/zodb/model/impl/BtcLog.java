package nl.zeesoft.zodb.model.impl;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.ZODBModel;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;

@PersistCollection(entity=false,module=ZODBModel.MODULE_ZODB,nameSingle="Batch log",nameMulti="Batch logs")
@ConstrainObjectAccess(userLevelFetch=20,userLevelUpdate=DbUser.USER_LEVEL_MIN,userLevelRemove=DbUser.USER_LEVEL_MIN,userLevelAdd=DbUser.USER_LEVEL_MIN)
@DescribeCollection(
description=
	"Batch logs are used to log details about batch program execution.\n" +
	"")
public class BtcLog extends MdlDataObject {
	private DtIdRef			program			= new DtIdRef();
	private DtDateTime		started			= new DtDateTime();
	private DtDateTime		stopped			= new DtDateTime();
	private DtIdRef			executedAsUser	= new DtIdRef();
	private DtStringBuffer	log 			= new DtStringBuffer();
	
	private DbUser			executingAsUser	= null;

	public void addLogLine(String line) {
		log.getValue().append(line);
		log.getValue().append("\n");
		Messenger.getInstance().debug(this, line);
	}

	public void addError(String err) {
		addLogLine("ERROR: " + err);
	}
	
	public DbUser getExecutingAsUser() {
		return executingAsUser;
	}

	public void setExecutingAsUser(DbUser executingAsUser) {
		this.executingAsUser = executingAsUser;
	}
	
	/**
	 * @return the started
	 */
	@ConstrainPropertyMandatory
	@PersistProperty(property="started",label="Started")
	public DtDateTime getStarted() {
		return started;
	}

	/**
	 * @return the stopped
	 */
	@PersistProperty(property="stopped",label="Stopped")
	public DtDateTime getStopped() {
		return stopped;
	}

	/**
	 * @return the log
	 */
	@PersistProperty(property="log",label="Log")
	public DtStringBuffer getLog() {
		return log;
	}

	/**
	 * @return the program
	 */
	@ConstrainPropertyMandatory
	@PersistReference(property="program",className="nl.zeesoft.zodb.model.impl.BtcProgram",removeMe=true,entity=true,label="Program",entityLabel="Program logs")
	public DtIdRef getProgram() {
		return program;
	}

	/**
	 * @return the executedAsUser
	 */
	@ConstrainPropertyMandatory
	@PersistReference(property="executedAsUser",className="nl.zeesoft.zodb.model.impl.DbUser",removeMe=false,entity=true,label="Executed as",entityLabel="Executed batch logs")
	public DtIdRef getExecutedAsUser() {
		return executedAsUser;
	}
}
