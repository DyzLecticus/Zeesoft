package nl.zeesoft.zpo.model.impl;

import java.math.BigDecimal;
import java.util.Calendar;

import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyLength;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyRange;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;
import nl.zeesoft.zodb.model.datatypes.DtDecimal;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zpo.model.ZPOModel;

@PersistCollection(entity=false,module=ZPOModel.MODULE_ZPO,nameSingle="Task log",nameMulti="Task logs")
@ConstrainObjectUnique(properties={"task;dateTime"})
@DescribeCollection(
description=
	"Task logs record activities that have been done.\n" +
	"")
public class UsrTaskLog extends MdlDataObject {
	public static final String			DONE							= "Done";
	
	private DtIdRef						user							= new DtIdRef(); // Explicit redundancy
	private DtIdRef						task							= new DtIdRef();
	private DtDateTime					dateTime						= new DtDateTime();
	private DtDecimal					duration						= new DtDecimal(new BigDecimal("0.50"));

	public UsrTaskLog() {
		getName().setValue(DONE);
		
		Calendar cal = Calendar.getInstance();
		if (cal.get(Calendar.MINUTE)>=30) {
			cal.set(Calendar.MINUTE, 30);
		} else {
			cal.set(Calendar.MINUTE, 0);
		}
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		getDateTime().setValue(cal.getTime());
		getDuration().setValue(30);
	}

	@Override
	@ConstrainPropertyLength(minValue=0,maxValue=128)
	@ConstrainPropertyMandatory
	@PersistProperty(property = PROPERTY_NAME,label="Log")
	public DtString getName() {
		return super.getName();
	}
	
	@ConstrainPropertyMandatory
	@PersistReference(property="user",label="User",className="nl.zeesoft.zodb.model.impl.DbUser",entityLabel="Task logs")
	public DtIdRef getUser() {
		return user;
	}

	@ConstrainPropertyMandatory
	@PersistReference(property="task",label="Task",className="nl.zeesoft.zpo.model.impl.UsrTask",entityLabel="Logs",removeMe=false)
	public DtIdRef getTask() {
		return task;
	}

	@ConstrainPropertyMandatory
	@PersistProperty(property="dateTime",label="Date and time")
	public DtDateTime getDateTime() {
		return dateTime;
	}

	@ConstrainPropertyRange(minValue=0,maxValue=24)
	@PersistProperty(property="duration",label="Duration in hours")
	public DtDecimal getDuration() {
		return duration;
	}
}
