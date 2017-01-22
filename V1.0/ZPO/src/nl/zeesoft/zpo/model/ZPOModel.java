package nl.zeesoft.zpo.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zadf.gui.property.PrpTextAreaStringBuffer;
import nl.zeesoft.zadf.model.DbModel;
import nl.zeesoft.zadf.model.ZADFModel;
import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionFilter;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zadf.model.impl.DbFetch;
import nl.zeesoft.zadf.model.impl.DbFetchFilter;
import nl.zeesoft.zadf.model.impl.DbModule;
import nl.zeesoft.zadf.model.impl.DbReport;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.model.MdlCollectionProperty;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;
import nl.zeesoft.zodb.model.impl.BtcProgram;
import nl.zeesoft.zodb.model.impl.BtcRepeat;
import nl.zeesoft.zodb.model.impl.DbUser;
import nl.zeesoft.zpo.batch.impl.BtcGenerateDemo;
import nl.zeesoft.zpo.format.impl.FmtUsrTaskLog;
import nl.zeesoft.zpo.model.impl.UsrNote;
import nl.zeesoft.zpo.model.impl.UsrTask;
import nl.zeesoft.zpo.model.impl.UsrTaskLog;
import nl.zeesoft.zpo.model.impl.UsrTaskRepeat;

public class ZPOModel extends ZADFModel {
	public final static String 						MODULE_ZPO 					= "ZPO";
	
	public final static String 						MODULE_ZPO_LABEL			= "Personal";
	public final static String 						MODULE_ZPO_DESC				= 
		"This module provides access to user specific tasks and notes.\n" +
		"The collections in this module are provided by the Zeesoft Personal Organizer.\n" +
		"The Zeesoft Personal Organizer extends the Zeesoft Application Development Framework.\n" +
		"\n" +
		"Features:\n" +
		"- Personal task administration and reporting.\n" +
		"- Personal note administration.\n" +
		"";

	@Override
	protected List<String> getPersistedClassNames() {
		List<String> l = super.getPersistedClassNames();
		l.add(UsrTaskRepeat.class.getName());
		l.add(UsrNote.class.getName());
		l.add(UsrTask.class.getName());
		l.add(UsrTaskLog.class.getName());
		return l;
	}
	
	@Override
	public DbUser generateInitialData(Object source) {
		DbUser admin = super.generateInitialData(source);
		
		// Task repeats
		QryTransaction t = new QryTransaction(admin);
		UsrTaskRepeat repeat = new UsrTaskRepeat();
		repeat.getName().setValue(UsrTaskRepeat.SUNDAY);
		repeat.getActive().setValue(false);
		t.addQuery(new QryAdd(repeat));
		repeat = new UsrTaskRepeat();
		repeat.getName().setValue(UsrTaskRepeat.MONDAY);
		t.addQuery(new QryAdd(repeat));
		repeat = new UsrTaskRepeat();
		repeat.getName().setValue(UsrTaskRepeat.TUESDAY);
		t.addQuery(new QryAdd(repeat));
		repeat = new UsrTaskRepeat();
		repeat.getName().setValue(UsrTaskRepeat.WEDNESDAY);
		t.addQuery(new QryAdd(repeat));
		repeat = new UsrTaskRepeat();
		repeat.getName().setValue(UsrTaskRepeat.THURSDAY);
		t.addQuery(new QryAdd(repeat));
		repeat = new UsrTaskRepeat();
		repeat.getName().setValue(UsrTaskRepeat.FRIDAY);
		t.addQuery(new QryAdd(repeat));
		repeat = new UsrTaskRepeat();
		repeat.getName().setValue(UsrTaskRepeat.SATURDAY);
		repeat.getActive().setValue(false);
		t.addQuery(new QryAdd(repeat));
		DbIndex.getInstance().executeTransaction(t, source);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		// Task log report batch procedure
		t = new QryTransaction(admin);
		cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		// Demo Data Generation batch procedure
		BtcProgram program = new BtcProgram();
		program.getName().setValue(BtcGenerateDemo.class.getName());
		program.getDescription().setValue(
			"This program will generate ZPO demo data.\n" +
			"It will generate a user named 'demo' with password '1demodemo!'.\n" +
			"Log in using this generated user to see the results.\n"
			);
		program.getActive().setValue(false);
		program.getStart().setValue(cal.getTime());
		program.getExecuteAsUser().setValue(admin.getId().getValue());
		program.getRepeatAmount().setValue(1000);
		program.getRepeat().setValue(repeats.get(BtcRepeat.YEAR).getId().getValue());
		t.addQuery(new QryAdd(program));
		DbIndex.getInstance().executeTransaction(t, source);
		
		// Report formats
		t = new QryTransaction(admin);
		t.addQuery(new QryAdd(getNewReportFormat(FmtUsrTaskLog.class.getName(),"csv")));
		DbIndex.getInstance().executeTransaction(t, source);

		// Reports
		DbCollection usrTaskLogCollection = collections.get(UsrTaskLog.class.getName());
		t = new QryTransaction(admin);
		DbReport report = new DbReport();
		report.getName().setValue("Personal " + usrTaskLogCollection.getNameSingle().getValue().toLowerCase());
		report.getDescription().setValue("Task logs for a certain period for a certain user in CSV format.\n");
		report.getFetches().getValue().add(collectionFetches.get(UsrTaskLog.class.getName()).getId().getValue());
		report.getFormat().setValue(formats.get(FmtUsrTaskLog.class.getName()));
		report.getUserLevelVisible().setValue(DbUser.USER_LEVEL_MAX);
		t.addQuery(new QryAdd(report));
		DbIndex.getInstance().executeTransaction(t, source);
		
		return admin;
	}

	@Override
	protected void setModuleProperties(DbModule dbMod) {
		if (dbMod.getName().equals(MODULE_ZPO)) {
			dbMod.getLabel().setValue(MODULE_ZPO_LABEL);
			dbMod.getDescription().setValue(MODULE_ZPO_DESC);
		} else {
			super.setModuleProperties(dbMod);
		}
	}
	
	@Override
	protected void setCollectionPropertyAccess(DbCollection dbCol,DbCollectionProperty dbProp) {
		super.setCollectionPropertyAccess(dbCol, dbProp);
		if (dbCol.getName().equals(UsrNote.class.getName())) {
			if (dbProp.getName().getValue().equals("user")) {
				dbProp.getOrderInDetail().setValue(-1);
				dbProp.getOrderInFilter().setValue(-1);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
		}
		if (dbCol.getName().equals(UsrTask.class.getName())) {
			if (dbProp.getName().getValue().equals("user")) {
				dbProp.getOrderInDetail().setValue(-1);
				dbProp.getOrderInFilter().setValue(-1);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
		}
		if (dbCol.getName().equals(UsrTaskLog.class.getName())) {
			if ((dbProp.getName().getValue().equals("task")) ||
				(dbProp.getName().getValue().equals("user"))
				) {
				dbProp.getOrderInDetail().setValue(-1);
				dbProp.getOrderInFilter().setValue(-1);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("dateTime")) {
				dbProp.getOrderInDetail().setValue(1);
				dbProp.getOrderInFilter().setValue(1);
				dbProp.getOrderInGrid().setValue(1);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("duration")) {
				dbProp.getOrderInDetail().setValue(2);
				dbProp.getOrderInFilter().setValue(2);
				dbProp.getOrderInGrid().setValue(2);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("name")) {
				dbProp.getOrderInDetail().setValue(3);
				dbProp.getOrderInFilter().setValue(3);
				dbProp.getOrderInGrid().setValue(3);
				dbProp.getOrderInSelect().setValue(1);
			}
		}
	}

	@Override
	protected String getGuiPropertyClassNameForMdlCollectionProperty(MdlCollectionProperty prop) {
		String r = super.getGuiPropertyClassNameForMdlCollectionProperty(prop); 
		if (prop.getDataTypeClassName().equals(DtStringBuffer.class.getName())) {
			if (prop.getName().equals("output")) {
				r = PrpTextAreaStringBuffer.class.getName();
			}
		}
		return r;
	}

	@Override
	protected List<DbCollectionFilter> getCollectionFiltersForCollectionProperty(DbCollection dbCol, DbCollectionProperty dbProp) {
		List<DbCollectionFilter> filters = super.getCollectionFiltersForCollectionProperty(dbCol, dbProp);
		if (
			((dbCol.getName().getValue().equals(UsrNote.class.getName())) && (dbProp.getName().getValue().equals("user"))) ||
			((dbCol.getName().getValue().equals(UsrTask.class.getName())) && (dbProp.getName().getValue().equals("user"))) ||
			((dbCol.getName().getValue().equals(UsrTaskLog.class.getName())) && (dbProp.getName().getValue().equals("user")))
			) {
			DbCollectionFilter filter = new DbCollectionFilter();
			filter.getCollection().setValue(dbCol);
			filter.getName().setValue(dbCol.getName().getValue() + "." + dbProp.getName().getValue());
			filter.getProperty().setValue(dbProp);
			filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_CONTAINS));
			filter.getMandatory().setValue(true);
			filter.getSoftware().setValue(false);
			filter.getValue().setValue(DbModel.SESSION_USER_ID);
			filters.add(filter);
		}
		return filters;
	}

	@Override
	protected void setFetchOrderByForFetchCollectionProperty(DbFetch dbFetch, DbCollection dbCol, DbCollectionProperty dbProp) {
		if (dbCol.getName().getValue().equals(UsrTaskLog.class.getName())) {
			if (dbProp.getName().getValue().equals("dateTime")) {
				dbFetch.getOrderBy().setValue(dbProp);
			}
		} else {
			super.setFetchOrderByForFetchCollectionProperty(dbFetch, dbCol, dbProp);
		}
	}

	@Override
	protected List<DbFetchFilter> getFetchFiltersForFetchCollectionProperty(DbFetch dbFetch, DbCollection dbCol, DbCollectionProperty dbProp) {
		List<DbFetchFilter> filters = super.getFetchFiltersForFetchCollectionProperty(dbFetch, dbCol, dbProp);
		if (
			((dbCol.getName().getValue().equals(UsrTaskLog.class.getName())) && (dbProp.getName().getValue().equals("user")))
			) {
			DbFetchFilter filter = new DbFetchFilter();
			filter.getFetch().setValue(dbFetch);
			filter.getName().setValue("User");
			filter.getProperty().setValue(dbProp);
			filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_CONTAINS));
			filter.getValue().setValue(DbModel.SESSION_USER_ID);
			filters.add(filter);
		}
		if (
			((dbCol.getName().getValue().equals(UsrTaskLog.class.getName())) && (dbProp.getName().getValue().equals("dateTime")))
			) {
			DbFetchFilter filter = new DbFetchFilter();
			filter.getFetch().setValue(dbFetch);
			filter.getName().setValue("From");
			filter.getProperty().setValue(dbProp);
			filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_GREATER_OR_EQUALS));
			filter.getValue().setValue(DbModel.DATE_THIS_YEAR);
			filters.add(filter);

			filter = new DbFetchFilter();
			filter.getFetch().setValue(dbFetch);
			filter.getName().setValue("Until");
			filter.getProperty().setValue(dbProp);
			filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_LESS));
			filter.getValue().setValue(DbModel.DATE_NEXT_YEAR);
			filters.add(filter);
		}
		return filters;
	}

	@Override
	public MdlDataObject getObjectForUpdateObject(SortedMap<String, StringBuffer> updateObject) {
		MdlDataObject obj = super.getObjectForUpdateObject(updateObject);
		if ((obj!=null) && (obj.getClassName().equals(UsrTaskLog.class.getName()))) {
			BigDecimal duration = new BigDecimal(updateObject.get("duration").toString());
			if (duration.compareTo(new BigDecimal("24.00"))>0){
				UsrTaskLog taskLog = (UsrTaskLog) obj;
				duration = duration.divide(new BigDecimal("60.00"));
				duration = duration.setScale(2,BigDecimal.ROUND_HALF_UP);
				taskLog.getDuration().setValue(duration);
			}
		}
		return obj;
	}
	
	
}
