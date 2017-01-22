package nl.zeesoft.zodb.model.impl;

import java.util.Calendar;
import java.util.Date;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.ZODBController;
import nl.zeesoft.zodb.batch.BtcProgramObject;
import nl.zeesoft.zodb.batch.BtcProgramWorker;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbFactory;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.MdlObject;
import nl.zeesoft.zodb.model.ZODBModel;
import nl.zeesoft.zodb.model.annotations.CacheableCollection;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyRange;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtInteger;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;

@PersistCollection(entity=true,module=ZODBModel.MODULE_ZODB,nameSingle="Batch program",nameMulti="Batch programs")
@CacheableCollection
@ConstrainObjectUnique
@ConstrainObjectAccess(userLevelFetch=20,userLevelUpdate=20,userLevelRemove=DbUser.USER_LEVEL_MIN,userLevelAdd=DbUser.USER_LEVEL_MIN)
@DescribeCollection(
description=
	"Batch programs define programs that will be executed at a certain date/time as a certain user.\n" +
	"The repeat and repeat amount allows them to be rescheduled automatically.\n" +
	"Rescheduling a program to run every week would require repeat '" + BtcRepeat.DAY + "' with the repeat amount set to 7.\n" +
	"")
public class BtcProgram extends MdlDataObject {
	private DtStringBuffer	description		= new DtStringBuffer();
	private DtDateTime		start			= new DtDateTime();
	private DtBoolean		active			= new DtBoolean(true);
	private DtIdRef			executeAsUser	= new DtIdRef();
	private DtBoolean		executing		= new DtBoolean(false);
	private DtInteger		repeatAmount	= new DtInteger(1);
	private DtIdRef			repeat			= new DtIdRef();
	private DtInteger		maxLogs			= new DtInteger(10);
	
	@Override
	public boolean checkObjectConstraintsForUserQuery(DbUser user, QryObject q) {
		boolean ok = super.checkObjectConstraintsForUserQuery(user, q);
		if (ok) {
			BtcProgram changedObject = null;
			if (q instanceof QryAdd) {
				changedObject = (BtcProgram) ((QryAdd) q).getDataObject();
			} else if (q instanceof QryUpdate) {
				changedObject = (BtcProgram) ((QryUpdate) q).getDataObject();
			}
			if (changedObject!=null) {
				if (!changedObject.getExecuting().getValue().equals(getExecuting().getValue())) {
					if (
						(!(q.getSource() instanceof BtcProgramWorker)) &&
						(!(q.getSource() instanceof DbFactory)) &&
						(!(q.getSource() instanceof ZODBController))
						) {
						q.addError("2001",getModelCollectionProperty("executing").getName(),"Only an instance of @1 may execute a program",BtcProgramWorker.class.getName());
						ok = false;
					}
				}
				if (ok) {
					if (changedObject.getRepeatAmount().getValue()<1) {
						changedObject.getRepeatAmount().setValue(1);
					}
					Object test = Generic.testInstanceForName(changedObject.getName().getValue());
					if (test==null) {
						q.addError("2002",MdlObject.PROPERTY_NAME,"Unable to instantiate batch program class with this name");
						ok = false;
					} else if (!(test instanceof BtcProgramObject)) {
						q.addError("2003",MdlObject.PROPERTY_NAME,"Batch programs must extend: " + BtcProgramObject.class.getName());
						ok = false;
					}
				}
			}
		}
		return ok;
	}
	
	public DbUser getExecuteAsUser(Object source) {
		DbUser user = null;
		if (executeAsUser.getValue()>0) {
			QryFetch q = new QryFetch(new DbUser().getClassName().getValue(),executeAsUser.getValue());
			DbIndex.getInstance().executeFetch(q, null, this);
			if (q.getMainResults().getReferences().size()>0) {
				user = (DbUser) q.getMainResults().getReferences().get(0).getDataObject();
			}
		} 
		if (user==null) {
			user = DbConfig.getInstance().getModel().getAdminUser(source);
			if ((executeAsUser.getValue()>0) && (executeAsUser.getValue()!=user.getId().getValue())) {
				executeAsUser.setValue(user.getId().getValue());
			}
		}
		return user;
	}
	
	public BtcRepeat getRepeat(Object source) {
		BtcRepeat rep = null;
		if (repeat.getValue()>0) {
			QryFetch q = new QryFetch(new BtcRepeat().getClassName().getValue(),repeat.getValue());
			DbIndex.getInstance().executeFetch(q, null, this);
			if (q.getMainResults().getReferences().size()>0) {
				rep = (BtcRepeat) q.getMainResults().getReferences().get(0).getDataObject();
			}
		}
		return rep;
	}

	public String execute(BtcLog log) {
		String err = "";
		BtcProgramObject program = null;
		try {
			program = (BtcProgramObject) Generic.instanceForName(getName().getValue());
		} catch (Exception e) {
			err = "Unable to instantiate batch program: " + e.getMessage() + "\n" + Generic.getCallStackString(e.getStackTrace(), "");
			program = null;
		}
		if (program!=null) {
			try {
				err = program.execute(log);
			} catch (Exception e) {
				err = "Error executing program: " + e.getMessage() + "\n" + Generic.getCallStackString(e.getStackTrace(), "");
			}
		}
		return err;
	}

	public void reschedule(boolean error) {
		Calendar cal = Calendar.getInstance();
		BtcRepeat repeat = getRepeat(this);
		Date d = new Date();

		Date now = new Date();
		
		if ((!error) && (repeat!=null) && (getRepeatAmount().getValue()>0)) {
			cal.setTime(getStart().getValue());
			while (cal.getTimeInMillis()<now.getTime()) {
				if (repeat.getName().equals(BtcRepeat.MINUTE)) {
					cal.add(Calendar.MINUTE, getRepeatAmount().getValue());
				} else if (repeat.getName().equals(BtcRepeat.HOUR)) {
					cal.add(Calendar.HOUR, getRepeatAmount().getValue());
				} else if (repeat.getName().equals(BtcRepeat.DAY)) {
					cal.add(Calendar.DATE, getRepeatAmount().getValue());
				} else if (repeat.getName().equals(BtcRepeat.MONTH)) {
					cal.add(Calendar.MONTH, getRepeatAmount().getValue());
				} else if (repeat.getName().equals(BtcRepeat.YEAR)) {
					cal.add(Calendar.YEAR, getRepeatAmount().getValue());
				}
			}
			d.setTime(cal.getTimeInMillis());
		} else {
			cal.set(9999, 11, 31, 0, 0, 0);
			d.setTime(cal.getTimeInMillis());
		}
		getStart().setValue(d);
	}
	
	/**
	 * @return the description
	 */
	@ConstrainPropertyMandatory
	@PersistProperty(property="description",label="description")
	public DtStringBuffer getDescription() {
		return description;
	}

	/**
	 * @return the start
	 */
	@ConstrainPropertyMandatory
	@PersistProperty(property="start",label="Start")
	public DtDateTime getStart() {
		return start;
	}
	
	/**
	 * @return the executeAsUser
	 */
	@ConstrainPropertyMandatory
	@PersistReference(property="executeAsUser",className="nl.zeesoft.zodb.model.impl.DbUser",removeMe=false,entity=false,label="Execute as",entityLabel="Batch programs")
	public DtIdRef getExecuteAsUser() {
		return executeAsUser;
	}
	
	/**
	 * @return the repeatAmount
	 */
	@ConstrainPropertyRange(minValue=0,maxValue=Integer.MAX_VALUE)
	@PersistProperty(property="repeatAmount",label="Repeat amount")
	public DtInteger getRepeatAmount() {
		return repeatAmount;
	}
	
	/**
	 * @return the repeat
	 */
	@ConstrainPropertyMandatory
	@PersistReference(property="repeat",className="nl.zeesoft.zodb.model.impl.BtcRepeat",removeMe=false,entity=false,label="Repeat",entityLabel="Programs")
	public DtIdRef getRepeat() {
		return repeat;
	}

	/**
	 * @return the active
	 */
	@PersistProperty(property="active",label="Active")
	public DtBoolean getActive() {
		return active;
	}

	/**
	 * @return the executing
	 */
	@PersistProperty(property="executing",label="Executing")
	public DtBoolean getExecuting() {
		return executing;
	}

	/**
	 * @return the maxLogs
	 */
	@ConstrainPropertyRange(minValue=1,maxValue=1000)
	@PersistProperty(property="maxLogs",label="Max. # logs")
	public DtInteger getMaxLogs() {
		return maxLogs;
	}
}
