package nl.zeesoft.zadf.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zadf.batch.impl.BtcHideCreatedAndChangedProperties;
import nl.zeesoft.zadf.batch.impl.BtcHideIDProperties;
import nl.zeesoft.zadf.batch.impl.BtcShowCreatedAndChangedProperties;
import nl.zeesoft.zadf.batch.impl.BtcShowIDProperties;
import nl.zeesoft.zadf.format.impl.FmtCSVHeader;
import nl.zeesoft.zadf.format.impl.FmtCSVNoHeader;
import nl.zeesoft.zadf.format.impl.FmtXMLRaw;
import nl.zeesoft.zadf.format.impl.FmtXMLRead;
import nl.zeesoft.zadf.gui.property.PrpCheckBox;
import nl.zeesoft.zadf.gui.property.PrpDateTime;
import nl.zeesoft.zadf.gui.property.PrpDecimal;
import nl.zeesoft.zadf.gui.property.PrpFloat;
import nl.zeesoft.zadf.gui.property.PrpIdRef;
import nl.zeesoft.zadf.gui.property.PrpIdRefList;
import nl.zeesoft.zadf.gui.property.PrpInteger;
import nl.zeesoft.zadf.gui.property.PrpLong;
import nl.zeesoft.zadf.gui.property.PrpPassword;
import nl.zeesoft.zadf.gui.property.PrpString;
import nl.zeesoft.zadf.gui.property.PrpStringBuffer;
import nl.zeesoft.zadf.gui.property.PrpTextAreaString;
import nl.zeesoft.zadf.gui.property.PrpTextAreaStringBuffer;
import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionFilter;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zadf.model.impl.DbFetch;
import nl.zeesoft.zadf.model.impl.DbFetchFilter;
import nl.zeesoft.zadf.model.impl.DbFilterOperator;
import nl.zeesoft.zadf.model.impl.DbModule;
import nl.zeesoft.zadf.model.impl.DbPropertyConstraint;
import nl.zeesoft.zadf.model.impl.DbReferenceFilter;
import nl.zeesoft.zadf.model.impl.DbReport;
import nl.zeesoft.zadf.model.impl.DbReportFormat;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.model.MdlCollection;
import nl.zeesoft.zodb.model.MdlCollectionProperty;
import nl.zeesoft.zodb.model.MdlCollectionReference;
import nl.zeesoft.zodb.model.MdlObject;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.ZODBModel;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;
import nl.zeesoft.zodb.model.datatypes.DtDecimal;
import nl.zeesoft.zodb.model.datatypes.DtFloat;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtIdRefList;
import nl.zeesoft.zodb.model.datatypes.DtInteger;
import nl.zeesoft.zodb.model.datatypes.DtLong;
import nl.zeesoft.zodb.model.datatypes.DtObject;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;
import nl.zeesoft.zodb.model.impl.BtcLog;
import nl.zeesoft.zodb.model.impl.BtcProgram;
import nl.zeesoft.zodb.model.impl.BtcRepeat;
import nl.zeesoft.zodb.model.impl.DbSession;
import nl.zeesoft.zodb.model.impl.DbUser;
import nl.zeesoft.zodb.model.impl.DbWhiteListItem;

public class ZADFModel extends ZODBModel {
	public final static String 							MODULE_ZADF 				= "ZADF";
	
	public final static String 							MODULE_ZADF_LABEL			= "Interface";
	public final static String 							MODULE_ZADF_DESC			= 
		"This module provides access to all application interface information.\n" + 
		"The collections in this module are provided by the Zeesoft Application Development Framework.\n" +
		"The Zeesoft Application Development Framework extends the Zeesoft Object Database.\n" +
		"Together, they provide a fast, secure and reliable framework for application development and management.\n" +
		"\n" +
		"Features:\n" +
		"- Remote server control GUI\n" +
		"- Database content management GUI\n" +
		"- Collection model visualization\n" +
		"- Debugging GUI\n" +
		"- Collection and property access management\n" +
		"- Configurable data report generation\n" +
		"";
	
	public final static String 							MODULE_ZODB_LABEL			= "Database";
	public final static String 							MODULE_ZODB_DESC			= 
		"This module provides access to all database management information.\n" +
		"The collections in this module are provided by the Zeesoft Object Database.\n" +
		"The Zeesoft Object Database is a pure Java object persistence database.\n" +
		"It was created to provide an easy to install, client server data persistence framework for Java applications.\n" +
		"Its collection model is static because it is entirely derived from the custom annotations of the objects it persists.\n" +
		"Object IDs, unique property combinations and links between objects are indexed and loaded in memory when the database server starts.\n" +
		"The actual objects themselves are actively loaded in a separate background process or lazy loaded when needed.\n" +
		"When objects are added, updated or removed a separate background process saves all the changes.\n" +
		"Database configuration changes made using the client control protocol are applied immediately.\n" +
		"\n" +
		"Features:\n" +
		"- User management\n" +
		"- Session logging\n" +
		"- White list access control\n" +
		"- Batch programs\n" +
		"- Static data request response cashing\n" +
		"- Separate server control port and protocol\n" +
		"- Encrypted communication protocols\n" +
		"- Client protocol software support\n" +
		"- Database backup (batch program) and restore\n" +
		"- Generic data update functionality\n" +
		"";
	
	protected SortedMap<String,DbFilterOperator> 		operators					= new TreeMap<String,DbFilterOperator>();
	protected SortedMap<String,DbReportFormat> 			formats						= new TreeMap<String,DbReportFormat>();
	protected SortedMap<String,DbModule> 				modules						= new TreeMap<String,DbModule>();
	protected SortedMap<String,DbCollection> 			collections		 			= new TreeMap<String,DbCollection>();
	protected SortedMap<String,DbPropertyConstraint> 	constraints					= new TreeMap<String,DbPropertyConstraint>();
	protected SortedMap<String,DbFetch> 				collectionFetches 			= new TreeMap<String,DbFetch>();
	
	@Override
	protected List<String> getPersistedClassNames() {
		List<String> l = super.getPersistedClassNames();
		l.add(DbPropertyConstraint.class.getName());
		l.add(DbFilterOperator.class.getName());
		l.add(DbModule.class.getName());
		l.add(DbCollection.class.getName());
		l.add(DbCollectionFilter.class.getName());
		l.add(DbCollectionProperty.class.getName());
		l.add(DbReferenceFilter.class.getName());
		l.add(DbFetch.class.getName());
		l.add(DbFetchFilter.class.getName());
		l.add(DbReport.class.getName());
		l.add(DbReportFormat.class.getName());
		return l;
	}

	@Override
	public DbUser generateInitialData(Object source) {
		DbUser admin = super.generateInitialData(source);

		QryTransaction t = null;
		QryAdd add = null;
		QryFetch fetch = null;

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		// Batch programs
		t = new QryTransaction(admin);
		BtcProgram program = null;
		program = new BtcProgram();
		program.getName().setValue(BtcHideIDProperties.class.getName());
		program.getDescription().setValue(
			"Hides the ID properties of all collections in the GUI.\n" +
			"The GUI must be restarted to show the results of this program.\n" +
			"Use: " + BtcShowIDProperties.class.getName() + " to undo the results of this program.\n"
			);
		program.getStart().setValue(cal.getTime());
		program.getExecuteAsUser().setValue(admin.getId().getValue());
		program.getRepeatAmount().setValue(1000);
		program.getRepeat().setValue(repeats.get(BtcRepeat.YEAR).getId().getValue());
		program.getActive().setValue(false);
		t.addQuery(new QryAdd(program));

		program = new BtcProgram();
		program.getName().setValue(BtcShowIDProperties.class.getName());
		program.getDescription().setValue(
			"Shows the ID properties of all collections in the GUI.\n" +
			"The GUI must be restarted to show the results of this program.\n" +
			"Use: " + BtcHideIDProperties.class.getName() + " to undo the results of this program.\n"
			);
		program.getStart().setValue(cal.getTime());
		program.getExecuteAsUser().setValue(admin.getId().getValue());
		program.getRepeatAmount().setValue(1000);
		program.getRepeat().setValue(repeats.get(BtcRepeat.YEAR).getId().getValue());
		program.getActive().setValue(false);
		t.addQuery(new QryAdd(program));
		
		program = new BtcProgram();
		program.getName().setValue(BtcHideCreatedAndChangedProperties.class.getName());
		program.getDescription().setValue(
			"Hides the created and changed properties of all collections in the GUI.\n" +
			"The GUI must be restarted to show the results of this program.\n" +
			"Use: " + BtcShowCreatedAndChangedProperties.class.getName() + " to undo the results of this program.\n"
			);
		program.getStart().setValue(cal.getTime());
		program.getExecuteAsUser().setValue(admin.getId().getValue());
		program.getRepeatAmount().setValue(1000);
		program.getRepeat().setValue(repeats.get(BtcRepeat.YEAR).getId().getValue());
		program.getActive().setValue(false);
		t.addQuery(new QryAdd(program));

		program = new BtcProgram();
		program.getName().setValue(BtcShowCreatedAndChangedProperties.class.getName());
		program.getDescription().setValue(
			"Shows the created and changed properties of all collections in the GUI.\n" +
			"The GUI must be restarted to show the results of this program.\n" +
			"Use: " + BtcHideCreatedAndChangedProperties.class.getName() + " to undo the results of this program.\n"
			);
		program.getStart().setValue(cal.getTime());
		program.getExecuteAsUser().setValue(admin.getId().getValue());
		program.getRepeatAmount().setValue(1000);
		program.getRepeat().setValue(repeats.get(BtcRepeat.YEAR).getId().getValue());
		program.getActive().setValue(false);
		t.addQuery(new QryAdd(program));
		DbIndex.getInstance().executeTransaction(t, source);
		
		// Filter operators
		t = new QryTransaction(admin);
		for (String name: Generic.getValuesFromString(QryFetchCondition.OPERATORS)) {
			t.addQuery(new QryAdd(getNewFilterOperator(name)));
		}
		DbIndex.getInstance().executeTransaction(t, source);

		// Report formats
		t = new QryTransaction(admin);
		t.addQuery(new QryAdd(getNewReportFormat(FmtCSVHeader.class.getName(),"csv")));
		t.addQuery(new QryAdd(getNewReportFormat(FmtCSVNoHeader.class.getName(),"csv")));
		t.addQuery(new QryAdd(getNewReportFormat(FmtXMLRaw.class.getName(),"xml")));
		t.addQuery(new QryAdd(getNewReportFormat(FmtXMLRead.class.getName(),"xml")));
		DbIndex.getInstance().executeTransaction(t, source);
		
		// Constraints
		t = new QryTransaction(admin);
		for (String name: DtObject.CONSTRAINTS) {
			t.addQuery(new QryAdd(getNewPropertyConstraint(name)));
		}
		DbIndex.getInstance().executeTransaction(t, source);

		// Modules
		t = new QryTransaction(admin);
		for (MdlCollection col: getCollections()) {
			if (!modules.containsKey(col.getModule())) {
				t.addQuery(new QryAdd(getNewModule(col.getModule())));
			}
		}
		DbIndex.getInstance().executeTransaction(t, source);

		List<DbCollection> collectionList = new ArrayList<DbCollection>();
		SortedMap<String,List<DbCollectionProperty>> collectionProperties = new TreeMap<String,List<DbCollectionProperty>>();
		
		// Collections
		t = new QryTransaction(admin);
		for (MdlCollection col: getCollections()) {
			DbCollection dbCol = new DbCollection();
			dbCol.getName().setValue(col.getName());
			dbCol.getModule().setValue(modules.get(col.getModule()));
			dbCol.getNameMulti().setValue(col.getNameMulti());
			dbCol.getNameSingle().setValue(col.getNameSingle());
			dbCol.getEntity().setValue(col.isEntity());
			dbCol.getUserLevelFetch().setValue(col.getUserLevelFetch());
			dbCol.getUserLevelUpdate().setValue(col.getUserLevelUpdate());
			dbCol.getUserLevelRemove().setValue(col.getUserLevelRemove());
			dbCol.getUserLevelAdd().setValue(col.getUserLevelAdd());
			dbCol.getDescription().setValue(col.getDescription());
			setCollectionAccess(dbCol);
			add = new QryAdd(dbCol);
			t.addQuery(add);
			collectionList.add(dbCol);
			collections.put(dbCol.getName().getValue(),dbCol);
		}
		DbIndex.getInstance().executeTransaction(t, source);
		
		// Properties
		fetch = new QryFetch(DbCollection.class.getName());
		DbIndex.getInstance().executeFetch(fetch, admin, source);
		t = new QryTransaction(admin);
		for (MdlObjectRef ref: fetch.getMainResults().getReferences()) {
			DbCollection dbCol = (DbCollection) ref.getDataObject();
			List<DbCollectionProperty> properties = new ArrayList<DbCollectionProperty>();
			collectionProperties.put(dbCol.getName().getValue(), properties);
			int detailOrder = 2;
			int filterOrder = 2;
			int gridOrder = 2;
			boolean select = false;
			for (MdlCollectionProperty prop: getCollectionProperties(dbCol.getName().getValue())) {
				DbCollectionProperty dbProp = new DbCollectionProperty();
				dbProp.getName().setValue(prop.getName());
				dbProp.getLabel().setValue(prop.getLabel());
				dbProp.getCollection().setValue(dbCol);
				dbProp.getMinValue().setValue(prop.getMinValue());
				dbProp.getMaxValue().setValue(prop.getMaxValue());
				if (prop instanceof MdlCollectionReference) {
					MdlCollectionReference propRef = (MdlCollectionReference) prop;
					MdlObjectRef colRef = fetch.getMainResults().getMdlObjectRefByName(propRef.getReference().getName());
					if (colRef!=null) {
						dbProp.getEntity().setValue(propRef.isEntity());
						dbProp.getEntityLabel().setValue(propRef.getEntityLabel());
						DbCollection refDbCol = (DbCollection) colRef.getDataObject();
						dbProp.getReferenceCollection().setValue(refDbCol);
					}
				}
				dbProp.getPropertyClassName().setValue(getGuiPropertyClassNameForMdlCollectionProperty(prop));
				dbProp.getOrderInSelect().setValue(-1);

				List<Long> idList = new ArrayList<Long>();
				for (String name: prop.getConstraints()) {
					idList.add(constraints.get(name).getId().getValue());
				}
				dbProp.getConstraints().setValue(idList);

				int initialIdOrder = -1;
				int initialCreatedByOrder = -1;
				int initialCreatedOnOrder = -1;
				int initialChangedByOrder = -1;
				int initialChangedOnOrder = -1;
				
				if (
					(dbProp.getName().equals(MdlObject.PROPERTY_CLASSNAME)) ||
					(dbProp.getName().equals(MdlObject.PROPERTY_CREATEDBY)) ||
					(dbProp.getName().equals(MdlObject.PROPERTY_CREATEDON)) ||
					(dbProp.getName().equals(MdlObject.PROPERTY_CHANGEDBY)) ||
					(dbProp.getName().equals(MdlObject.PROPERTY_CHANGEDON))
					) {
					dbProp.getUserLevelEnabled().setValue(DbUser.USER_LEVEL_OFF);
					if (dbProp.getName().equals(MdlObject.PROPERTY_CLASSNAME)) {
						dbProp.getUserLevelVisible().setValue(DbUser.USER_LEVEL_MIN);
						dbProp.getOrderInDetail().setValue(-1);
						dbProp.getOrderInFilter().setValue(-1);
						dbProp.getOrderInSelect().setValue(-1);
					} else if (dbProp.getName().equals(MdlObject.PROPERTY_CREATEDBY)) {
						dbProp.getOrderInDetail().setValue(initialCreatedByOrder);
						dbProp.getOrderInFilter().setValue(initialCreatedByOrder);
					} else if (dbProp.getName().equals(MdlObject.PROPERTY_CREATEDON)) {
						dbProp.getOrderInDetail().setValue(initialCreatedOnOrder);
						dbProp.getOrderInFilter().setValue(initialCreatedOnOrder);
					} else if (dbProp.getName().equals(MdlObject.PROPERTY_CHANGEDBY)) {
						dbProp.getOrderInDetail().setValue(initialChangedByOrder);
						dbProp.getOrderInFilter().setValue(initialChangedByOrder);
					} else if (dbProp.getName().equals(MdlObject.PROPERTY_CHANGEDON)) {
						dbProp.getOrderInDetail().setValue(initialChangedOnOrder);
						dbProp.getOrderInFilter().setValue(initialChangedOnOrder);
					}
					dbProp.getOrderInGrid().setValue(-1);
				} else if (dbProp.getName().equals(MdlObject.PROPERTY_ID)) {
					dbProp.getUserLevelEnabled().setValue(DbUser.USER_LEVEL_OFF);
					dbProp.getOrderInDetail().setValue(initialIdOrder);
					dbProp.getOrderInFilter().setValue(initialIdOrder);
					dbProp.getOrderInGrid().setValue(initialIdOrder);
					dbProp.getOrderInSelect().setValue(initialIdOrder);
				} else if (dbProp.getName().equals(MdlObject.PROPERTY_NAME)) {
					dbProp.getOrderInDetail().setValue(1);
					dbProp.getOrderInFilter().setValue(1);
					dbProp.getOrderInGrid().setValue(1);
					dbProp.getOrderInSelect().setValue(1);
				} else if (dbProp.getPropertyClassName().getValue().equals(PrpPassword.class.getName())) {
					dbProp.getOrderInDetail().setValue(detailOrder);
					dbProp.getOrderInFilter().setValue(-1);
					dbProp.getOrderInGrid().setValue(-1);
					detailOrder++;
				} else if (dbProp.getPropertyClassName().getValue().equals(PrpTextAreaStringBuffer.class.getName())) {
					dbProp.getOrderInDetail().setValue((detailOrder + 800));
					dbProp.getOrderInFilter().setValue(-1);
					dbProp.getOrderInGrid().setValue(-1);
					detailOrder++;
				} else if (dbProp.getPropertyClassName().getValue().equals(PrpTextAreaString.class.getName())) {
					dbProp.getOrderInDetail().setValue((detailOrder + 900));
					dbProp.getOrderInFilter().setValue(-1);
					dbProp.getOrderInGrid().setValue(-1);
					detailOrder++;
				} else {
					dbProp.getOrderInDetail().setValue(detailOrder);
					dbProp.getOrderInFilter().setValue(filterOrder);
					dbProp.getOrderInGrid().setValue(gridOrder);
					detailOrder++;
					filterOrder++;
					gridOrder++;
					if (!select) {
						dbProp.getOrderInSelect().setValue(2);
						select = true;
					}
				}
				setCollectionPropertyAccess(dbCol,dbProp);
				add = new QryAdd(dbProp);
				t.addQuery(add);
				properties.add(dbProp);
			}
		}
		DbIndex.getInstance().executeTransaction(t, source);

		// Collection filters and reference filters
		t = new QryTransaction(admin);
		for (DbCollection dbCol: collectionList) {
			for (DbCollectionProperty dbProp: collectionProperties.get(dbCol.getName().getValue())) {
				for (DbCollectionFilter filter: getCollectionFiltersForCollectionProperty(dbCol,dbProp)) {
					add = new QryAdd(filter);
					t.addQuery(add);
				}
				if (dbProp.getReferenceCollection().getValue()>0) {
					QryFetch fetchRefProp = new QryFetch(DbCollectionProperty.class.getName());
					fetchRefProp.addCondition(new QryFetchCondition("collection",QryFetchCondition.OPERATOR_CONTAINS,dbProp.getReferenceCollection()));
					DbIndex.getInstance().executeFetch(fetchRefProp, admin, source);
					for (MdlObjectRef refRefProp: fetchRefProp.getMainResults().getReferences()) {
						DbCollectionProperty dbRefProp = (DbCollectionProperty) refRefProp.getDataObject();
						for (DbReferenceFilter filter: getReferenceFiltersForCollectionProperty(dbCol,dbProp,dbRefProp)) {
							add = new QryAdd(filter);
							t.addQuery(add);
						}
					}
				}
			}
		}
		DbIndex.getInstance().executeTransaction(t, source);
		
		// Fetches
		t = new QryTransaction(admin);
		for (DbCollection dbCol: collectionList) {
			DbFetch dbFetch = new DbFetch();
			dbFetch.getName().setValue(dbCol.getNameMulti().getValue());
			dbFetch.getCollection().setValue(dbCol);
			for (DbCollectionProperty dbProp: collectionProperties.get(dbCol.getName().getValue())) {
				setFetchOrderByForFetchCollectionProperty(dbFetch,dbCol,dbProp);
			}
			addFetchEntities(dbCol,dbFetch,admin,source);
			setFetchDetails(dbFetch,dbCol);
			t.addQuery(new QryAdd(dbFetch));
			collectionFetches.put(dbCol.getName().getValue(), dbFetch);
		}
		DbIndex.getInstance().executeTransaction(t, source);
		
		// Fetch filters
		t = new QryTransaction(admin);
		for (DbCollection dbCol: collectionList) {
			DbFetch dbFetch = collectionFetches.get(dbCol.getName().getValue());
			for (DbCollectionProperty dbProp: collectionProperties.get(dbCol.getName().getValue())) {
				List<DbFetchFilter> filters = getFetchFiltersForFetchCollectionProperty(dbFetch,dbCol,dbProp);
				for (DbFetchFilter filter: filters) {
					t.addQuery(new QryAdd(filter));
				}
			}
		}
		DbIndex.getInstance().executeTransaction(t, source);

		DbCollection moduleCollection = collections.get(DbModule.class.getName());
		DbCollection collectionCollection = collections.get(DbCollection.class.getName());
		
		// Reports
		t = new QryTransaction(admin);
		DbFetch moduleAndCollectionFetch = new DbFetch();
		moduleAndCollectionFetch.getName().setValue("Database " + moduleCollection.getNameMulti().getValue().toLowerCase() + " and " + collectionCollection.getNameMulti().getValue().toLowerCase());
		moduleAndCollectionFetch.getCollection().setValue(moduleCollection);
		for (DbCollectionProperty dbProp: collectionProperties.get(moduleCollection.getName().getValue())) {
			setFetchOrderByForFetchCollectionProperty(moduleAndCollectionFetch,moduleCollection,dbProp);
		}
		moduleAndCollectionFetch.getEntities().getValue().add(collectionCollection.getId().getValue());
		t.addQuery(new QryAdd(moduleAndCollectionFetch));
		DbIndex.getInstance().executeTransaction(t, source);
		
		t = new QryTransaction(admin);
		DbReport report = new DbReport();
		report.getName().setValue(moduleAndCollectionFetch.getName().getValue());
		report.getDescription().setValue(moduleAndCollectionFetch.getName().getValue() + " in XML format.\n");
		report.getFetches().getValue().add(moduleAndCollectionFetch.getId().getValue());
		report.getFormat().setValue(formats.get(FmtXMLRead.class.getName()));
		t.addQuery(new QryAdd(report));

		report = new DbReport();
		report.getName().setValue("Database session log");
		report.getDescription().setValue("Database sessions over a certain period for a certain user in CSV format.\n");
		report.getFetches().getValue().add(collectionFetches.get(DbSession.class.getName()).getId().getValue());
		report.getFormat().setValue(formats.get(FmtCSVHeader.class.getName()));
		t.addQuery(new QryAdd(report));
		DbIndex.getInstance().executeTransaction(t, source);

		return admin;
	}
	
	@Override
	public String getUpdateMethodForCollection(MdlCollection collection) {
		String method = super.getUpdateMethodForCollection(collection);
		if (collection.getName().equals(DbCollection.class.getName())) {
			method = METHOD_UPDATE_ONLY;
		} else if (collection.getName().equals(DbCollectionFilter.class.getName())) {
			method = METHOD_ADD_ONLY;
		} else if (collection.getName().equals(DbCollectionProperty.class.getName())) {
			method = METHOD_UPDATE_ONLY;
		} else if (collection.getName().equals(DbFilterOperator.class.getName())) {
			method = METHOD_SKIP;
		} else if (collection.getName().equals(DbModule.class.getName())) {
			method = METHOD_SKIP;
		} else if (collection.getName().equals(DbPropertyConstraint.class.getName())) {
			method = METHOD_SKIP;
		} else if (collection.getName().equals(DbReferenceFilter.class.getName())) {
			method = METHOD_ADD_ONLY;
		} else if (collection.getName().equals(DbReportFormat.class.getName())) {
			method = METHOD_SKIP;
		}
		return method;
	}

	@Override
	public boolean canUpdateCollectionProperty(MdlCollection collection, MdlCollectionProperty property) {
		boolean update = super.canUpdateCollectionProperty(collection, property);
		if (update) {
			if (collection.getName().equals(DbCollection.class.getName())) {
				if (
					(!property.getName().equals("userLevelFetch")) &&
					(!property.getName().equals("userLevelAdd")) &&
					(!property.getName().equals("userLevelUpdate")) &&
					(!property.getName().equals("userLevelRemove"))
					) {
					update = false;
				}
			} else if (collection.getName().equals(DbCollectionProperty.class.getName())) {
				if (
					(!property.getName().equals("userLevelVisible")) &&
					(!property.getName().equals("userLevelEnabled")) &&
					(!property.getName().equals("orderInDetail")) &&
					(!property.getName().equals("orderInFilter")) &&
					(!property.getName().equals("orderInGrid")) &&
					(!property.getName().equals("orderInSelect"))
					) {
					update = false;
				}
			}
		}
		return update;
	}
	
	/**
	 * Override to implement; must call super first!
	 * 
	 * This method is called for each module
	 * 
	 * @param dbMod The module
	 */
	protected void setModuleProperties(DbModule dbMod) {
		if (dbMod.getName().equals(MODULE_ZODB)) {
			dbMod.getLabel().setValue(MODULE_ZODB_LABEL);
			dbMod.getDescription().setValue(MODULE_ZODB_DESC);
		} else if (dbMod.getName().equals(MODULE_ZADF)) {
			dbMod.getLabel().setValue(MODULE_ZADF_LABEL);
			dbMod.getDescription().setValue(MODULE_ZADF_DESC);
		}
	}

	/**
	 * Override to implement; must call super first!
	 * 
	 * @param dbCol The collection of which the user access levels are to be set
	 */
	protected void setCollectionAccess(DbCollection dbCol) {
		if (
			(dbCol.getName().getValue().equals(DbUser.class.getName())) ||
			(dbCol.getName().getValue().equals(DbFilterOperator.class.getName())) ||
			(dbCol.getName().getValue().equals(DbModule.class.getName())) ||
			(dbCol.getName().getValue().equals(DbCollection.class.getName())) ||
			(dbCol.getName().getValue().equals(DbCollectionProperty.class.getName())) ||
			(dbCol.getName().getValue().equals(DbCollectionFilter.class.getName())) ||
			(dbCol.getName().getValue().equals(DbReferenceFilter.class.getName())) ||
			(dbCol.getName().getValue().equals(DbPropertyConstraint.class.getName())) ||
			(dbCol.getName().getValue().equals(DbFetch.class.getName())) ||
			(dbCol.getName().getValue().equals(DbFetchFilter.class.getName())) ||
			(dbCol.getName().getValue().equals(DbReportFormat.class.getName()))
			) {
			dbCol.getUserLevelFetch().setValue(10);
		}
		if (
			(dbCol.getName().getValue().equals(DbSession.class.getName())) ||
			(dbCol.getName().getValue().equals(BtcLog.class.getName())) ||
			(dbCol.getName().getValue().equals(BtcRepeat.class.getName())) ||
			(dbCol.getName().getValue().equals(DbModule.class.getName())) ||
			(dbCol.getName().getValue().equals(DbFilterOperator.class.getName())) ||
			(dbCol.getName().getValue().equals(DbCollection.class.getName())) ||
			(dbCol.getName().getValue().equals(DbCollectionProperty.class.getName())) ||
			(dbCol.getName().getValue().equals(DbPropertyConstraint.class.getName()))
			) {
			dbCol.getUserLevelAdd().setValue(0);
		}
		if (
			(dbCol.getName().getValue().equals(DbSession.class.getName())) ||
			(dbCol.getName().getValue().equals(BtcLog.class.getName()))
			) {
			dbCol.getUserLevelUpdate().setValue(0);
		}
	}

	/**
	 * Override to implement; must call super first!
	 * 
	 * @param dbCol The collection to which the property belongs
	 * @param dbProp The collection property of which the user access levels are to be set
	 */
	protected void setCollectionPropertyAccess(DbCollection dbCol, DbCollectionProperty dbProp) {
		if (dbProp.getName().getValue().equals("active")) {
			dbProp.getOrderInGrid().setValue(-1);
			dbProp.getOrderInSelect().setValue(-1);
		}
		if (dbCol.getName().getValue().equals(DbFilterOperator.class.getName())) {
			dbProp.getUserLevelEnabled().setValue(DbUser.USER_LEVEL_OFF);
		} else if (dbCol.getName().getValue().equals(DbModule.class.getName())) {
			dbProp.getUserLevelEnabled().setValue(DbUser.USER_LEVEL_OFF);
		} else if (dbCol.getName().getValue().equals(DbPropertyConstraint.class.getName())) {
			dbProp.getUserLevelEnabled().setValue(DbUser.USER_LEVEL_OFF);
		} else if (dbCol.getName().getValue().equals(DbCollection.class.getName())) {
			if (
				(dbProp.getName().getValue().equals("nameSingle")) ||
				(dbProp.getName().getValue().equals("entity"))
				) {
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (!dbProp.getName().getValue().startsWith("userLevel")) {
				dbProp.getUserLevelEnabled().setValue(DbUser.USER_LEVEL_OFF);
			}
		} else if (dbCol.getName().getValue().equals(DbCollectionProperty.class.getName())) {
			if (
				(dbProp.getName().getValue().equals("collection")) ||
				(dbProp.getName().getValue().equals("referenceCollection")) ||
				(dbProp.getName().getValue().equals("constraints")) ||
				(dbProp.getName().getValue().equals("entity")) ||
				(dbProp.getName().getValue().equals("entityLabel")) ||
				(dbProp.getName().getValue().equals("propertyClassName")) ||
				(dbProp.getName().getValue().equals("minValue")) ||
				(dbProp.getName().getValue().equals("maxValue"))
				) {
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("label")) {
				dbProp.getOrderInSelect().setValue(2);
			}
			if (
				(!dbProp.getName().getValue().startsWith("enabled")) &&
				(!dbProp.getName().getValue().startsWith("orderIn")) &&
				(!dbProp.getName().getValue().startsWith("userLevel"))
				) {
				dbProp.getUserLevelEnabled().setValue(DbUser.USER_LEVEL_OFF);
			}
			if (dbProp.getName().getValue().equals("minValue")) {
				dbProp.getOrderInDetail().setValue(7);
				dbProp.getOrderInFilter().setValue(7);
			}
			if (dbProp.getName().getValue().equals("maxValue")) {
				dbProp.getOrderInDetail().setValue(8);
				dbProp.getOrderInFilter().setValue(8);
			}
		} else if (dbCol.getName().getValue().equals(DbCollectionFilter.class.getName())) {
			if (dbProp.getName().getValue().equals("property")) {
				dbProp.getOrderInGrid().setValue(2);
				dbProp.getOrderInDetail().setValue(3);
			} else if (dbProp.getName().getValue().equals("invert")) {
				dbProp.getOrderInGrid().setValue(3);
				dbProp.getOrderInDetail().setValue(4);
			} else if (dbProp.getName().getValue().equals("operator")) {
				dbProp.getOrderInGrid().setValue(4);
				dbProp.getOrderInDetail().setValue(5);
			} else if (dbProp.getName().getValue().equals("value")) {
				dbProp.getOrderInGrid().setValue(5);
				dbProp.getOrderInDetail().setValue(6);
			} else if (dbProp.getName().getValue().equals("mandatory")) {
				dbProp.getOrderInGrid().setValue(6);
				dbProp.getOrderInDetail().setValue(7);
			}
			if (
				(dbProp.getName().getValue().equals("collection")) ||
				(dbProp.getName().getValue().equals("stringValue")) ||
				(dbProp.getName().getValue().equals("valueClassName"))
				) {
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (
				(dbProp.getName().getValue().equals("software"))
				) {
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
				dbProp.getOrderInFilter().setValue(-1);
				dbProp.getOrderInDetail().setValue(-1);
				dbProp.getUserLevelEnabled().setValue(DbUser.USER_LEVEL_OFF);
			}
		} else if (dbCol.getName().getValue().equals(DbReferenceFilter.class.getName())) {
			if (dbProp.getName().getValue().equals("reference")) {
				dbProp.getOrderInDetail().setValue(2);
			} else if (dbProp.getName().getValue().equals("property")) {
				dbProp.getOrderInGrid().setValue(2);
				dbProp.getOrderInDetail().setValue(3);
			} else if (dbProp.getName().getValue().equals("invert")) {
				dbProp.getOrderInGrid().setValue(3);
				dbProp.getOrderInDetail().setValue(4);
			} else if (dbProp.getName().getValue().equals("operator")) {
				dbProp.getOrderInGrid().setValue(4);
				dbProp.getOrderInDetail().setValue(5);
			} else if (dbProp.getName().getValue().equals("value")) {
				dbProp.getOrderInGrid().setValue(5);
				dbProp.getOrderInDetail().setValue(6);
			} else if (dbProp.getName().getValue().equals("mandatory")) {
				dbProp.getOrderInGrid().setValue(6);
				dbProp.getOrderInDetail().setValue(7);
			}
			if (
				(dbProp.getName().getValue().equals("reference")) ||
				(dbProp.getName().getValue().equals("stringValue")) ||
				(dbProp.getName().getValue().equals("valueClassName"))
				) {
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (
				(dbProp.getName().getValue().equals("software"))
				) {
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
				dbProp.getOrderInFilter().setValue(-1);
				dbProp.getOrderInDetail().setValue(-1);
				dbProp.getUserLevelEnabled().setValue(DbUser.USER_LEVEL_OFF);
			}
		} else if (dbCol.getName().getValue().equals(DbUser.class.getName())) {
			if (dbProp.getName().getValue().equals("password")) {
				dbProp.getOrderInDetail().setValue(2);
				dbProp.getOrderInFilter().setValue(2);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("level")) {
				dbProp.getOrderInDetail().setValue(3);
				dbProp.getOrderInFilter().setValue(3);
				dbProp.getOrderInGrid().setValue(2);
				dbProp.getOrderInSelect().setValue(2);
			}
			if (dbProp.getName().getValue().equals("admin")) {
				dbProp.getOrderInDetail().setValue(4);
				dbProp.getOrderInFilter().setValue(4);
				dbProp.getOrderInGrid().setValue(3);
				dbProp.getOrderInSelect().setValue(-1);
				dbProp.getUserLevelEnabled().setValue(DbUser.USER_LEVEL_MIN);
				dbProp.getUserLevelVisible().setValue(DbUser.USER_LEVEL_MIN);
			}
			if (dbProp.getName().getValue().equals("active")) {
				dbProp.getOrderInDetail().setValue(5);
				dbProp.getOrderInFilter().setValue(5);
			}
		} else if (dbCol.getName().getValue().equals(BtcProgram.class.getName())) {
			if (dbProp.getName().getValue().equals("executeAs")) {
				dbProp.getOrderInDetail().setValue(2);
				dbProp.getOrderInFilter().setValue(2);
				dbProp.getOrderInGrid().setValue(2);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("start")) {
				dbProp.getOrderInDetail().setValue(3);
				dbProp.getOrderInFilter().setValue(3);
				dbProp.getOrderInGrid().setValue(3);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("repeat")) {
				dbProp.getOrderInDetail().setValue(4);
				dbProp.getOrderInFilter().setValue(4);
				dbProp.getOrderInGrid().setValue(4);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("repeatAmount")) {
				dbProp.getOrderInDetail().setValue(5);
				dbProp.getOrderInFilter().setValue(5);
				dbProp.getOrderInGrid().setValue(5);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("maxLogs")) {
				dbProp.getOrderInDetail().setValue(6);
				dbProp.getOrderInFilter().setValue(6);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("active")) {
				dbProp.getOrderInDetail().setValue(7);
				dbProp.getOrderInFilter().setValue(7);
			}
			if (dbProp.getName().getValue().equals("executing")) {
				dbProp.getOrderInDetail().setValue(8);
				dbProp.getOrderInFilter().setValue(8);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
				dbProp.getUserLevelEnabled().setValue(DbUser.USER_LEVEL_OFF);
			}
		} else if (dbCol.getName().getValue().equals(BtcLog.class.getName())) {
			dbProp.getUserLevelEnabled().setValue(DbUser.USER_LEVEL_OFF);
			if (dbProp.getName().getValue().equals("program")) {
				dbProp.getOrderInDetail().setValue(2);
				dbProp.getOrderInFilter().setValue(2);
				dbProp.getOrderInGrid().setValue(2);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("executedAs")) {
				dbProp.getOrderInDetail().setValue(3);
				dbProp.getOrderInFilter().setValue(3);
				dbProp.getOrderInGrid().setValue(3);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("started")) {
				dbProp.getOrderInDetail().setValue(4);
				dbProp.getOrderInFilter().setValue(4);
				dbProp.getOrderInGrid().setValue(4);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("stopped")) {
				dbProp.getOrderInDetail().setValue(5);
				dbProp.getOrderInFilter().setValue(5);
				dbProp.getOrderInGrid().setValue(5);
				dbProp.getOrderInSelect().setValue(-1);
			}
		} else if (dbCol.getName().getValue().equals(BtcRepeat.class.getName())) {
			dbProp.getUserLevelEnabled().setValue(DbUser.USER_LEVEL_OFF);
		} else if (dbCol.getName().getValue().equals(DbSession.class.getName())) {
			dbProp.getUserLevelEnabled().setValue(DbUser.USER_LEVEL_OFF);
			if (dbProp.getName().getValue().equals("started")) {
				dbProp.getOrderInDetail().setValue(2);
				dbProp.getOrderInFilter().setValue(2);
				dbProp.getOrderInGrid().setValue(2);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("ended")) {
				dbProp.getOrderInDetail().setValue(3);
				dbProp.getOrderInFilter().setValue(3);
				dbProp.getOrderInGrid().setValue(3);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("user")) {
				dbProp.getOrderInDetail().setValue(4);
				dbProp.getOrderInFilter().setValue(4);
				dbProp.getOrderInGrid().setValue(4);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("ipAndPort")) {
				dbProp.getOrderInDetail().setValue(5);
				dbProp.getOrderInFilter().setValue(5);
				dbProp.getOrderInGrid().setValue(5);
				dbProp.getOrderInSelect().setValue(-1);
			}
		} else if (dbCol.getName().getValue().equals(DbWhiteListItem.class.getName())) {
			if (dbProp.getName().getValue().equals("startsWith")) {
				dbProp.getOrderInDetail().setValue(2);
				dbProp.getOrderInFilter().setValue(2);
				dbProp.getOrderInGrid().setValue(2);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("control")) {
				dbProp.getOrderInDetail().setValue(3);
				dbProp.getOrderInFilter().setValue(3);
				dbProp.getOrderInGrid().setValue(3);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("active")) {
				dbProp.getOrderInDetail().setValue(4);
				dbProp.getOrderInFilter().setValue(4);
			}
		} else if (dbCol.getName().getValue().equals(DbFetch.class.getName())) {
			if (dbProp.getName().getValue().equals("collection")) {
				dbProp.getOrderInDetail().setValue(2);
				dbProp.getOrderInFilter().setValue(2);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("entities")) {
				dbProp.getOrderInDetail().setValue(3);
				dbProp.getOrderInFilter().setValue(3);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("limit")) {
				dbProp.getOrderInDetail().setValue(4);
				dbProp.getOrderInFilter().setValue(4);
				dbProp.getOrderInGrid().setValue(2);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("orderBy")) {
				dbProp.getOrderInDetail().setValue(5);
				dbProp.getOrderInFilter().setValue(5);
				dbProp.getOrderInGrid().setValue(3);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("orderAscending")) {
				dbProp.getOrderInDetail().setValue(6);
				dbProp.getOrderInFilter().setValue(6);
				dbProp.getOrderInGrid().setValue(4);
				dbProp.getOrderInSelect().setValue(-1);
			}
		} else if (dbCol.getName().getValue().equals(DbFetchFilter.class.getName())) {
			if (dbProp.getName().getValue().equals("fetch")) {
				dbProp.getOrderInDetail().setValue(2);
				dbProp.getOrderInFilter().setValue(2);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("property")) {
				dbProp.getOrderInDetail().setValue(3);
				dbProp.getOrderInFilter().setValue(3);
				dbProp.getOrderInGrid().setValue(2);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("invert")) {
				dbProp.getOrderInDetail().setValue(4);
				dbProp.getOrderInFilter().setValue(4);
				dbProp.getOrderInGrid().setValue(3);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("operator")) {
				dbProp.getOrderInDetail().setValue(5);
				dbProp.getOrderInFilter().setValue(5);
				dbProp.getOrderInGrid().setValue(4);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("value")) {
				dbProp.getOrderInDetail().setValue(6);
				dbProp.getOrderInFilter().setValue(6);
				dbProp.getOrderInGrid().setValue(5);
				dbProp.getOrderInSelect().setValue(-1);
			}
		} else if (dbCol.getName().getValue().equals(DbReport.class.getName())) {
			if (dbProp.getName().getValue().equals("fetches")) {
				dbProp.getOrderInDetail().setValue(2);
				dbProp.getOrderInFilter().setValue(2);
				dbProp.getOrderInGrid().setValue(2);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("format")) {
				dbProp.getOrderInDetail().setValue(3);
				dbProp.getOrderInFilter().setValue(3);
				dbProp.getOrderInGrid().setValue(3);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("userLevelVisible")) {
				dbProp.getOrderInDetail().setValue(4);
				dbProp.getOrderInFilter().setValue(4);
				dbProp.getOrderInGrid().setValue(4);
				dbProp.getOrderInSelect().setValue(-1);
			}
		} else if (dbCol.getName().getValue().equals(DbReportFormat.class.getName())) {
			if (dbProp.getName().getValue().equals("extension")) {
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
		}
	}
	
	/**
	 * Override to implement; must call super first!
	 * 
	 * @param prop The collection property of which the GuiProperty implementation class name is to be determined
	 */
	protected String getGuiPropertyClassNameForMdlCollectionProperty(MdlCollectionProperty prop) {
		
		String r = "";
		if (prop.getDataTypeClassName().equals(DtBoolean.class.getName())) {
			r = PrpCheckBox.class.getName();
		} else if (prop.getDataTypeClassName().equals(DtDateTime.class.getName())) {
			r = PrpDateTime.class.getName();
		} else if (prop.getDataTypeClassName().equals(DtDecimal.class.getName())) {
			r = PrpDecimal.class.getName();
		} else if (prop.getDataTypeClassName().equals(DtFloat.class.getName())) {
			r = PrpFloat.class.getName();
		} else if (prop.getDataTypeClassName().equals(DtInteger.class.getName())) {
			r = PrpInteger.class.getName();
		} else if (prop.getDataTypeClassName().equals(DtLong.class.getName())) {
			r = PrpLong.class.getName();
		} else if (prop.getDataTypeClassName().equals(DtIdRef.class.getName())) {
			r = PrpIdRef.class.getName();
		} else if (prop.getDataTypeClassName().equals(DtIdRefList.class.getName())) {
			r = PrpIdRefList.class.getName();
		} else if (prop.getDataTypeClassName().equals(DtString.class.getName())) {
			r = PrpString.class.getName();
		} else if (prop.getDataTypeClassName().equals(DtStringBuffer.class.getName())) {
			if (prop.getConstraints().contains(DtObject.CONSTRAIN_PASSWORD)) {
				r = PrpPassword.class.getName();
			} else {
				r = PrpStringBuffer.class.getName();
			}
		} else if (prop.getDataTypeClassName().equals(DtStringBuffer.class.getName())) {
			r = PrpStringBuffer.class.getName();
		} else {
			r = "";
		}
		
		if (prop.getDataTypeClassName().equals(DtString.class.getName())) {
			if (prop.getName().equals("description")) {
				r = PrpTextAreaString.class.getName();
			} else if (prop.getName().equals("log")) {
				r = PrpTextAreaString.class.getName();
			}
		} else if (prop.getDataTypeClassName().equals(DtStringBuffer.class.getName())) {
			if (prop.getName().equals("description")) {
				r = PrpTextAreaStringBuffer.class.getName();
			} else if (prop.getName().equals("log")) {
				r = PrpTextAreaStringBuffer.class.getName();
			}
		}
		
		return r;
	}
	
	/**
	 * Override to implement; must call super first!
	 * 
	 * This method is called for each property of each collection
	 * 
	 * @param dbCol The collection
	 * @param dbProp The collection property
	 * @return The list of collection filters for the specified collection
	 */
	protected List<DbCollectionFilter> getCollectionFiltersForCollectionProperty(DbCollection dbCol, DbCollectionProperty dbProp) {
		List<DbCollectionFilter> filters = new ArrayList<DbCollectionFilter>();
		if (dbProp.getName().getValue().equals("active")) {
			DbCollectionFilter filter = new DbCollectionFilter();
			filter.getCollection().setValue(dbCol);
			filter.getName().setValue(dbCol.getName().getValue() + "." + dbProp.getName().getValue());
			filter.getProperty().setValue(dbProp);
			filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_EQUALS));
			filter.getMandatory().setValue(false);
			filter.getSoftware().setValue(false);
			filter.getValue().setValue("true");
			filters.add(filter);
		}
		if (
			((dbCol.getName().getValue().equals(DbCollectionFilter.class.getName())) && (dbProp.getName().getValue().equals("software"))) ||
			((dbCol.getName().getValue().equals(DbReferenceFilter.class.getName())) && (dbProp.getName().getValue().equals("software")))
			) {
			DbCollectionFilter filter = new DbCollectionFilter();
			filter.getCollection().setValue(dbCol);
			filter.getName().setValue(dbCol.getName().getValue() + "." + dbProp.getName().getValue());
			filter.getProperty().setValue(dbProp);
			filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_EQUALS));
			filter.getMandatory().setValue(true);
			filter.getSoftware().setValue(true);
			filter.getValue().setValue("false");
			filters.add(filter);
		}
		if (
			((dbCol.getName().getValue().equals(DbUser.class.getName())) && (dbProp.getName().getValue().equals("level"))) ||
			((dbCol.getName().getValue().equals(DbCollection.class.getName())) && (dbProp.getName().getValue().equals("userLevelFetch"))) ||
			((dbCol.getName().getValue().equals(DbCollectionProperty.class.getName())) && (dbProp.getName().getValue().equals("userLevelVisible"))) ||
			((dbCol.getName().getValue().equals(DbReport.class.getName())) && (dbProp.getName().getValue().equals("userLevelVisible")))
			) {
			DbCollectionFilter filter = new DbCollectionFilter();
			filter.getCollection().setValue(dbCol);
			filter.getName().setValue(dbCol.getName().getValue() + "." + dbProp.getName().getValue());
			filter.getProperty().setValue(dbProp);
			filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_GREATER_OR_EQUALS));
			filter.getMandatory().setValue(true);
			filter.getSoftware().setValue(true);
			filter.getValue().setValue(DbModel.SESSION_USER_LEVEL);
			filters.add(filter);
		}
		if (
			((dbCol.getName().getValue().equals(DbSession.class.getName())) && (dbProp.getName().getValue().equals(MdlObject.PROPERTY_ID))) ||
			((dbCol.getName().getValue().equals(DbSession.class.getName())) && (dbProp.getName().getValue().equals(MdlObject.PROPERTY_NAME))) ||
			((dbCol.getName().getValue().equals(DbSession.class.getName())) && (dbProp.getName().getValue().equals("started")))
			) {
			DbCollectionFilter filter = new DbCollectionFilter();
			filter.getCollection().setValue(dbCol);
			filter.getName().setValue(dbCol.getName().getValue() + "." + dbProp.getName().getValue());
			filter.getProperty().setValue(dbProp);
			filter.getMandatory().setValue(false);
			filter.getSoftware().setValue(false);
			if (dbProp.getName().getValue().equals(MdlObject.PROPERTY_ID)) {
				filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_EQUALS));
				filter.getValue().setValue(DbModel.SESSION_ID);
			} else if (dbProp.getName().getValue().equals(MdlObject.PROPERTY_NAME)) {
				filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_EQUALS));
				filter.getValue().setValue(DbSession.OPEN);
			} else if (dbProp.getName().getValue().equals("started")) {
				filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_GREATER_OR_EQUALS));
				filter.getValue().setValue(DbModel.DATE_TODAY);
			}
			filters.add(filter);
		}
		return filters;
	}
	
	/**
	 * Override to implement; must call super first!
	 * 
	 * This method is called for each reference property and each property of the collection referenced by that property
	 * 
	 * @param dbCol The collection
	 * @param dbProp The collection property
	 * @param dbRefProp The referenced collection property
	 * @return The list of collection reference filters for the specified reference property
	 */
	protected List<DbReferenceFilter> getReferenceFiltersForCollectionProperty(DbCollection dbCol, DbCollectionProperty dbProp, DbCollectionProperty dbRefProp) {
		List<DbReferenceFilter> filters = new ArrayList<DbReferenceFilter>();
		if (
			(dbCol.getName().getValue().equals(DbCollectionFilter.class.getName())) && 
			(dbProp.getName().getValue().equals("property")) &&
			(dbRefProp.getName().getValue().equals("collection"))
			) {
			DbReferenceFilter filter = new DbReferenceFilter();
			filter.getReference().setValue(dbProp);
			filter.getName().setValue(dbCol.getName().getValue() + "." + dbProp.getName().getValue() + " -> " + dbRefProp.getName().getValue());
			filter.getProperty().setValue(dbRefProp);
			filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_CONTAINS));
			filter.getMandatory().setValue(true);
			filter.getSoftware().setValue(true);
			filter.getValue().setValue(DbModel.CONTEXT_OBJECT_PREFIX + ".collection");
			filters.add(filter);
		}
		if (
			(dbCol.getName().getValue().equals(DbReferenceFilter.class.getName())) && 
			(dbProp.getName().getValue().equals("reference")) &&
			(dbRefProp.getName().getValue().equals("referenceCollection"))
			) {
			DbReferenceFilter filter = new DbReferenceFilter();
			filter.getReference().setValue(dbProp);
			filter.getName().setValue(dbCol.getName().getValue() + "." + dbProp.getName().getValue() + " -> " + dbRefProp.getName().getValue());
			filter.getProperty().setValue(dbRefProp);
			filter.getInvert().setValue(true);
			filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_EQUALS));
			filter.getMandatory().setValue(true);
			filter.getSoftware().setValue(true);
			filter.getValue().setValue(new DtLong().toString());
			filters.add(filter);
		}
		if (
			(dbCol.getName().getValue().equals(DbReferenceFilter.class.getName())) && 
			(dbProp.getName().getValue().equals("property")) &&
			(dbRefProp.getName().getValue().equals("collection"))
			) {
			DbReferenceFilter filter = new DbReferenceFilter();
			filter.getReference().setValue(dbProp);
			filter.getName().setValue(dbCol.getName().getValue() + "." + dbProp.getName().getValue() + " -> " + dbRefProp.getName().getValue());
			filter.getProperty().setValue(dbRefProp);
			filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_CONTAINS));
			filter.getMandatory().setValue(true);
			filter.getSoftware().setValue(true);
			filter.getValue().setValue(DbModel.CONTEXT_OBJECT_PREFIX + ".reference.referenceCollection");
			filters.add(filter);
		}
		if (
			(dbCol.getName().getValue().equals(DbFetchFilter.class.getName())) && 
			(dbProp.getName().getValue().equals("property")) &&
			(dbRefProp.getName().getValue().equals("collection"))
			) {
			DbReferenceFilter filter = new DbReferenceFilter();
			filter.getReference().setValue(dbProp);
			filter.getName().setValue(dbCol.getName().getValue() + "." + dbProp.getName().getValue() + " -> " + dbRefProp.getName().getValue());
			filter.getProperty().setValue(dbRefProp);
			filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_CONTAINS));
			filter.getMandatory().setValue(true);
			filter.getSoftware().setValue(true);
			filter.getValue().setValue(DbModel.CONTEXT_OBJECT_PREFIX + ".fetch.collection");
			filters.add(filter);
		}
		return filters;
	}

	/**
	 * Override to implement
	 * 
	 * This method is called for each fetch collection property
	 * 
	 * @param dbFetch The fetch
	 * @param dbCol The fetch collection
	 * @param dbProp The fetch collection property
	 */
	protected void setFetchOrderByForFetchCollectionProperty(DbFetch dbFetch, DbCollection dbCol, DbCollectionProperty dbProp) {
		if (dbProp.getName().getValue().equals(MdlObject.PROPERTY_NAME)) {
			dbFetch.getOrderBy().setValue(dbProp);
		}
	}

	/**
	 * Override to implement
	 * 
	 * This method is called for each fetch
	 * 
	 * @param dbFetch The fetch
	 * @param dbCol The fetch collection
	 */
	protected void setFetchDetails(DbFetch dbFetch, DbCollection dbCol) {
		if (dbCol.getEntity().getValue()) {
			dbFetch.getLimit().setValue(1);
		}
	}

	/**
	 * Override to implement; must call super first!
	 * 
	 * This method is called for each fetch collection property
	 * 
	 * @param dbFetch The fetch
	 * @param dbCol The fetch collection
	 * @param dbProp The fetch collection property
	 */
	protected List<DbFetchFilter> getFetchFiltersForFetchCollectionProperty(DbFetch dbFetch, DbCollection dbCol, DbCollectionProperty dbProp) {
		List<DbFetchFilter> filters = new ArrayList<DbFetchFilter>();

		if (dbProp.getName().getValue().equals("active")) {
			DbFetchFilter filter = new DbFetchFilter();
			filter.getFetch().setValue(dbFetch);
			filter.getName().setValue(dbProp.getName().getValue());
			filter.getProperty().setValue(dbProp);
			filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_EQUALS));
			filter.getValue().setValue("true");
			filters.add(filter);
		}
		if (
			((dbCol.getName().getValue().equals(DbSession.class.getName())) && (dbProp.getName().getValue().equals("user")))
			) {
			DbFetchFilter filter = new DbFetchFilter();
			filter.getFetch().setValue(dbFetch);
			filter.getName().setValue(dbProp.getName().getValue());
			filter.getProperty().setValue(dbProp);
			filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_CONTAINS));
			filter.getValue().setValue(DbModel.SESSION_USER_ID);
			filters.add(filter);
		}
		if (
			((dbCol.getName().getValue().equals(DbSession.class.getName())) && (dbProp.getName().getValue().equals("started")))
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
	
	private void addFetchEntities(DbCollection parentCol, DbFetch dbFetch, DbUser admin, Object source) {
		if (dbFetch.getEntities().getValue().size()>=3) {
			return;
		}
		
		// Add direct entity references
		QryFetch propFetch = new QryFetch(DbCollectionProperty.class.getName());
		propFetch.addCondition(new QryFetchCondition("collection",QryFetchCondition.OPERATOR_CONTAINS,parentCol.getId()));
		propFetch.addCondition(new QryFetchCondition("referenceCollection",true,QryFetchCondition.OPERATOR_CONTAINS,new DtIdRef()));
		propFetch.addCondition(new QryFetchCondition("entity",QryFetchCondition.OPERATOR_EQUALS,new DtBoolean(true)));
		propFetch.addCondition(new QryFetchCondition(MdlObject.PROPERTY_NAME,true,QryFetchCondition.OPERATOR_EQUALS,new DtString(MdlObject.PROPERTY_CREATEDBY)));
		propFetch.addCondition(new QryFetchCondition(MdlObject.PROPERTY_NAME,true,QryFetchCondition.OPERATOR_EQUALS,new DtString(MdlObject.PROPERTY_CHANGEDBY)));
		DbIndex.getInstance().executeFetch(propFetch, admin, source);
		for (MdlObjectRef propRef: propFetch.getMainResults().getReferences()) {
			DbCollectionProperty dbProp = (DbCollectionProperty) propRef.getDataObject(); 
			if ((!dbFetch.getCollection().getValue().equals(dbProp.getReferenceCollection().getValue())) &&
				(!dbFetch.getEntities().getValue().contains(dbProp.getReferenceCollection().getValue()))
				) {
				dbFetch.getEntities().getValue().add(dbProp.getReferenceCollection().getValue());
				if (dbFetch.getEntities().getValue().size()>=3) {
					return;
				}
			}
		}

		// Add child entity references
		List<DbCollection> addedChildren = new ArrayList<DbCollection>();
		propFetch = new QryFetch(DbCollectionProperty.class.getName());
		propFetch.addCondition(new QryFetchCondition("referenceCollection",QryFetchCondition.OPERATOR_CONTAINS,parentCol.getId()));
		propFetch.addCondition(new QryFetchCondition("entity",QryFetchCondition.OPERATOR_EQUALS,new DtBoolean(true)));
		propFetch.addCondition(new QryFetchCondition(MdlObject.PROPERTY_NAME,true,QryFetchCondition.OPERATOR_EQUALS,new DtString(MdlObject.PROPERTY_CREATEDBY)));
		propFetch.addCondition(new QryFetchCondition(MdlObject.PROPERTY_NAME,true,QryFetchCondition.OPERATOR_EQUALS,new DtString(MdlObject.PROPERTY_CHANGEDBY)));
		DbIndex.getInstance().executeFetch(propFetch, admin, source);
		for (MdlObjectRef propRef: propFetch.getMainResults().getReferences()) {
			DbCollectionProperty dbProp = (DbCollectionProperty) propRef.getDataObject(); 
			if ((!dbFetch.getCollection().getValue().equals(dbProp.getCollection().getValue())) &&
				(!dbFetch.getEntities().getValue().contains(dbProp.getCollection().getValue()))
				) {
				dbFetch.getName().setValue(dbFetch.getName().getValue() + ", " + dbProp.getEntityLabel().getValue().toLowerCase());
				dbFetch.getEntities().getValue().add(dbProp.getCollection().getValue());
				if (dbFetch.getEntities().getValue().size()>=3) {
					return;
				}
				QryFetch colFetch = new QryFetch(DbCollection.class.getName(),dbProp.getCollection().getValue());
				DbIndex.getInstance().executeFetch(colFetch, admin, source);
				DbCollection childCol = (DbCollection) colFetch.getMainResults().getReferences().get(0).getDataObject();
				addedChildren.add(childCol);
			}
		}
		for (DbCollection childCol: addedChildren) {
			addFetchEntities(childCol,dbFetch,admin,source);
		}
	}
	
	private DbFilterOperator getNewFilterOperator(String name) {
		DbFilterOperator operator = null;
		operator = new DbFilterOperator();
		operator.getName().setValue(name);
		operators.put(name, operator);
		return operator;
	}

	protected DbReportFormat getNewReportFormat(String name,String extension) {
		DbReportFormat format = null;
		format = new DbReportFormat();
		format.getName().setValue(name);
		format.getExtension().setValue(extension);
		formats.put(name, format);
		return format;
	}

	private DbPropertyConstraint getNewPropertyConstraint(String name) {
		DbPropertyConstraint constraint = null;
		constraint = new DbPropertyConstraint();
		constraint.getName().setValue(name);
		constraints.put(name, constraint);
		return constraint;
	}

	private DbModule getNewModule(String name) {
		DbModule module = null;
		module = new DbModule();
		module.getName().setValue(name);
		setModuleProperties(module);
		modules.put(name, module);
		return module;
	}
}
