package nl.zeesoft.zodb.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.batch.impl.BtcDatabaseBackup;
import nl.zeesoft.zodb.batch.impl.BtcDatabaseBackupRemote;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.model.annotations.CacheableCollection;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyAlphabetic;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyAlphabeticUnderscore;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyAlphanumeric;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyAlphanumericUnderscore;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyDateTimeFuture;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyDateTimePast;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyLength;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyLowerCase;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyNoSpace;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyPassword;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyRange;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyUpperCase;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtObject;
import nl.zeesoft.zodb.model.impl.BtcLog;
import nl.zeesoft.zodb.model.impl.BtcProgram;
import nl.zeesoft.zodb.model.impl.BtcRepeat;
import nl.zeesoft.zodb.model.impl.DbSession;
import nl.zeesoft.zodb.model.impl.DbUser;
import nl.zeesoft.zodb.model.impl.DbWhiteListItem;

/**
 * This class is designed to be extended for specific implementations.
 * 
 * Override getPersistedClassNames to extend the model classes.
 * Override generateInitialData to extend the initial data generation process (called during installation).
 */
public class ZODBModel {
	public final static String 				INITIAL_ADMIN_USERNAME		= "admin";
	public final static String 				INITIAL_ADMIN_PASSWORD		= "1superAdmin!";

	public final static String 				MODULE_ZODB 				= "ZODB";

	public static final String 				METHOD_SKIP					= "METHOD_SKIP";
	public static final String 				METHOD_ADD_ONLY				= "METHOD_ADD_ONLY";
	public static final String				METHOD_ADD_OR_UPDATE		= "METHOD_UPDATE_OR_ADD";
	public static final String				METHOD_UPDATE_ONLY			= "METHOD_UPDATE_ONLY";
	
	private final static String 			CLASS_NAME_MODEL_OBJECT 	= MdlDataObject.class.getName();
	private final static String 			CLASS_NAME_DATATYPE_OBJECT 	= DtObject.class.getName();

	private List<String> 					modules						= new ArrayList<String>();
	private List<MdlCollection> 			collections					= new ArrayList<MdlCollection>();
	private long							crc							= 0;
	
	private DbUser							adminUser					= null;

	protected SortedMap<String,BtcRepeat> 	repeats						= new TreeMap<String,BtcRepeat>();
	
	/**
	 * Override for custom persisted classes; must call super first!
	 * 
	 * All model classes must extend the MdlDataObject class
	 */
	protected List<String> getPersistedClassNames() {
		List<String> classNames = new ArrayList<String>();
		classNames.add(DbUser.class.getName());
		classNames.add(DbWhiteListItem.class.getName());
		classNames.add(DbSession.class.getName());
		classNames.add(BtcRepeat.class.getName());
		classNames.add(BtcProgram.class.getName());
		classNames.add(BtcLog.class.getName());
		return classNames;
	}
	
	/**
	 * Override for custom initial data; must call super first!
	 */
	public DbUser generateInitialData(Object source) {
		DbUser admin = getAdminUser(source); 

		QryTransaction t = null;
		
		DbUser user = null;
		t = new QryTransaction(admin);
		user = new DbUser();
		user.getName().setValue("userAdmin");
		user.getPassword().setValue(DbConfig.getInstance().encodePassword(new StringBuffer("1userAdmin!")));
		user.getLevel().setValue(10);
		t.addQuery(new QryAdd(user));
		user = new DbUser();
		user.getName().setValue("batchAdmin");
		user.getPassword().setValue(DbConfig.getInstance().encodePassword(new StringBuffer("1batchAdmin!")));
		user.getLevel().setValue(20);
		t.addQuery(new QryAdd(user));
		DbIndex.getInstance().executeTransaction(t, source);

		t = new QryTransaction(admin);
		DbWhiteListItem whiteList = new DbWhiteListItem();
		whiteList.getName().setValue("localhost");
		whiteList.getControl().setValue(true);
		t.addQuery(new QryAdd(whiteList));
		DbIndex.getInstance().executeTransaction(t, source);
		
		// Create batch repeats
		BtcRepeat repeat = null;
		t = new QryTransaction(admin);
		repeat = getNewBatchRepeat(BtcRepeat.MINUTE);
		t.addQuery(new QryAdd(repeat));
		repeat = getNewBatchRepeat(BtcRepeat.HOUR);
		t.addQuery(new QryAdd(repeat));
		repeat = getNewBatchRepeat(BtcRepeat.DAY);
		t.addQuery(new QryAdd(repeat));
		repeat = getNewBatchRepeat(BtcRepeat.MONTH);
		t.addQuery(new QryAdd(repeat));
		repeat = getNewBatchRepeat(BtcRepeat.YEAR);
		t.addQuery(new QryAdd(repeat));
		DbIndex.getInstance().executeTransaction(t, source);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		t = new QryTransaction(admin);
		BtcProgram program = null;
		program = new BtcProgram();
		program.getName().setValue(BtcDatabaseBackup.class.getName());
		program.getDescription().setValue(
			"This program will generate a backup of all the data in the database.\n" +
			"The backup files will be created in: " + DbConfig.getInstance().getFullBackupDir() + "\n" +
			"Use the restore script in the bin folder to restore it.\n" +
			"During a restore, the admin account will be reset to its original state and batch programs will be rescheduled.\n"
			);
		program.getStart().setValue(cal.getTime());
		program.getExecuteAsUser().setValue(admin.getId().getValue());
		program.getRepeatAmount().setValue(7);
		program.getRepeat().setValue(repeats.get(BtcRepeat.DAY).getId().getValue());
		t.addQuery(new QryAdd(program));

		program = new BtcProgram();
		program.getName().setValue(BtcDatabaseBackupRemote.class.getName());
		program.getDescription().setValue(
			"This program will generate a backup of all the data in the database.\n" +
			"The backup files will be created in the remote backup directory if it is connected to the server.\n"
			);
		program.getStart().setValue(cal.getTime());
		program.getExecuteAsUser().setValue(admin.getId().getValue());
		program.getRepeatAmount().setValue(1);
		program.getRepeat().setValue(repeats.get(BtcRepeat.DAY).getId().getValue());
		program.getActive().setValue(false);
		t.addQuery(new QryAdd(program));
		DbIndex.getInstance().executeTransaction(t, source);
				
		return admin;
	}

	/**
	 * Override for custom collection update method
	 * 
	 * @param collection The collection for which the update method is to be returned
	 */ 
	public String getUpdateMethodForCollection(MdlCollection collection) {
		String method = METHOD_ADD_OR_UPDATE;
		if (collection.getName().equals(DbSession.class.getName())) {
			method = METHOD_SKIP;
		} else if (collection.getName().equals(BtcLog.class.getName())) {
			method = METHOD_SKIP;
		} else if (collection.getName().equals(BtcRepeat.class.getName())) {
			method = METHOD_SKIP;
		}
		return method;
	}
	
	/**
	 * Override for custom update object property
	 * 
	 * @param collection The property collection
	 * @param property The collection property
	 * @return true if the collection property can be updated during update
	 */
	public boolean canUpdateCollectionProperty(MdlCollection collection, MdlCollectionProperty property) {
		boolean update = true;
		String method = getUpdateMethodForCollection(collection);
		if (
			(method.equals(METHOD_SKIP)) ||
			(method.equals(METHOD_ADD_ONLY))
			) {
			update = false;
		}
		return update;
	}
	
	/**
	 * Override for custom update object
	 * 
	 * @param updateObject A key value pair combination representing the import object
	 * @return A valid object to be added or updated
	 */
	public MdlDataObject getObjectForUpdateObject(SortedMap<String,StringBuffer> updateObject) {
		MdlDataObject obj = null;
		StringBuffer className = updateObject.get(MdlObject.PROPERTY_CLASSNAME);
		if ((className!=null) && (className.length()>0)){
			MdlCollection col = getCollectionByName(className.toString());
			if (col!=null) {
				obj = (MdlDataObject) Generic.instanceForName(className.toString());
				for (MdlCollectionProperty prop: getCollectionProperties(obj.getClassName().getValue())) {
					StringBuffer strVal = updateObject.get(prop.getName());
					if ((strVal!=null) && (strVal.length()>0)) {
						DtObject value = (DtObject) Generic.executeObjectClassMethod(obj,prop.getMethod(),null);
						value.fromString(strVal);
					}
				}
			}
		}
		return obj;
	}

	public List<String> getModules() {
		return new ArrayList<String>(modules);
	}

	public final List<MdlCollection> getCollections() {
		return new ArrayList<MdlCollection>(collections);
	}
	
	public MdlCollection getCollectionByName(String name) {
		MdlCollection r = null;
		for (MdlCollection c: collections) {
			if (c.getName().equals(name)) {
				r = c;
				break;
			}
		}
		if (r==null) {
			Messenger.getInstance().warn(this, "Class not persisted: " + name);
		}
		return r;
	}

	public final List<MdlCollection> getCollectionsByModuleName(String name) {
		List<MdlCollection> r = new ArrayList<MdlCollection>();
		for (MdlCollection c: collections) {
			if (c.getModule().equals(name)) {
				r.add(c);
			}
		}
		return r;
	}

	public List<MdlCollectionUniqueConstraint> getCollectionUniqueConstraints(String className) {
		List<MdlCollectionUniqueConstraint> r = null;
		MdlCollection c = getCollectionByName(className);
		if (c!=null) {
			r = c.getUniqueConstraints();
		}
		return r;
	}

	public List<MdlCollectionProperty> getCollectionProperties(String className) {
		List<MdlCollectionProperty> r = null;
		MdlCollection c = getCollectionByName(className);
		if (c!=null) {
			r = c.getProperties();
		}
		return r;
	}
	
	public MdlCollectionProperty getCollectionPropertyByName(String className, String name) {
		MdlCollectionProperty r = null;
		MdlCollection c = getCollectionByName(className);
		if (c!=null) {
			for (MdlCollectionProperty p: c.getProperties()) {
				if (p.getName().equals(name)) { 
					r = p;
					break;
				}
			}
		}
		return r;
	}

	public List<MdlCollectionReference> getCollectionReferences(String className) {
		List<MdlCollectionReference> r = new ArrayList<MdlCollectionReference>();
		MdlCollection c = getCollectionByName(className);
		if (c!=null) {
			for (MdlCollectionProperty p: c.getProperties()) {
				if (p instanceof MdlCollectionReference) {
					MdlCollectionReference ref = (MdlCollectionReference) p;
					r.add(ref);
				}
			}
		}
		return r;
	}

	public List<MdlCollectionReference> getCollectionReferencesByReferenceClass(String className) {
		List<MdlCollectionReference> r = new ArrayList<MdlCollectionReference>();
		for (MdlCollection c: collections) {
			for (MdlCollectionProperty p: c.getProperties()) {
				if (p instanceof MdlCollectionReference) {
					MdlCollectionReference ref = (MdlCollectionReference) p;
					if (ref.getReference().getName().equals(className)) {
						r.add(ref);
					}
				}
			}
		}
		return r;
	}
	
	private final List<Class<?>> getPersistedCollectionClasses() {
		List<Class<?>> l = new ArrayList<Class<?>>();
		for (String className: getPersistedClassNames()) {
			Class<?> cls = Generic.forName(className);
			if ((cls!=null) && (Generic.instanceOf(cls, CLASS_NAME_MODEL_OBJECT))) {
				if (cls.isAnnotationPresent(PersistCollection.class)) {
					l.add(cls);
				}
			}
		}
		return l;
	}
	
	private static final List<Method> getPersistedCollectionClassMethods(Class<?> cls) {
		List<Method> l = new ArrayList<Method>();
		
		if (Generic.instanceOf(cls, CLASS_NAME_MODEL_OBJECT)) {
			for (Method mtd: cls.getMethods()) {
				if (Generic.instanceOf(mtd.getReturnType(), CLASS_NAME_DATATYPE_OBJECT)) {
					if (
						(mtd.isAnnotationPresent(PersistProperty.class)) ||
						(mtd.isAnnotationPresent(PersistReference.class))
						) {
						l.add(mtd);
					}
				}
			}
		}
		return l;
	}

	private final static void setPersistedCollectionProperties(Class<?> cls, MdlCollection c) {
		PersistCollection pc = cls.getAnnotation(PersistCollection.class);
		if (pc!=null) {
			c.setEntity(pc.entity());
			c.setModule(pc.module());
			c.setNameSingle(pc.nameSingle());
			c.setNameMulti(pc.nameMulti());
		}
		ConstrainObjectAccess coa = cls.getAnnotation(ConstrainObjectAccess.class);
		if (coa!=null) {
			c.setUserLevelFetch(coa.userLevelFetch());
			c.setUserLevelUpdate(coa.userLevelUpdate());
			c.setUserLevelRemove(coa.userLevelRemove());
			c.setUserLevelAdd(coa.userLevelAdd());
		}
		CacheableCollection ca = cls.getAnnotation(CacheableCollection.class);
		if (ca!=null) {
			c.setCacheable(true);
		}
		DescribeCollection dc = cls.getAnnotation(DescribeCollection.class);
		if (dc!=null) {
			c.setDescription(dc.description());
		}
	}
	
	private final static String getPersistedCollectionReference(Class<?> cls, Method mtd) {
		String r = "";
		PersistReference pr = mtd.getAnnotation(PersistReference.class);
		if (pr!=null) {
			r = pr.className();
		}
		return r;
	}

	private final static void setPersistedCollectionPropertyProperties(Class<?> cls, Method mtd, MdlCollectionProperty p) {
		p.setMethod(mtd.getName());
		p.setDataTypeClassName(mtd.getReturnType().getName());
		PersistProperty pp = mtd.getAnnotation(PersistProperty.class);
		if (pp!=null) {
			p.setName(pp.property());
			p.setLabel(pp.label());
		} else {
			PersistReference pr = mtd.getAnnotation(PersistReference.class);
			if (pr!=null) {
				p.setName(pr.property());
				p.setLabel(pr.label());
			}
		}
	}

	private final static void setPersistedCollectionReferenceProperties(Class<?> cls, Method mtd, MdlCollectionReference r) {
		PersistReference pr = mtd.getAnnotation(PersistReference.class);
		if (pr!=null) {
			r.setRemoveMe(pr.removeMe());
			r.setEntity(pr.entity());
			r.setEntityLabel(pr.entityLabel());
		}
	}
	
	private final static void setPersistedCollectionPropertyConstraints(Class<?> cls, Method mtd, MdlCollectionProperty m) {
		if (m instanceof MdlCollectionReference) {
			if (mtd.getAnnotation(ConstrainPropertyMandatory.class)!=null) {
				m.getConstraints().add(DtObject.CONSTRAIN_PROPERTY_MANDATORY);
			}
		} else {
			ConstrainPropertyRange range = mtd.getAnnotation(ConstrainPropertyRange.class);
			if (range!=null) {
				m.setMinValue(range.minValue());
				m.setMaxValue(range.maxValue());
			} else if (mtd.getAnnotation(ConstrainPropertyPassword.class)!=null) {
				m.getConstraints().add(DtObject.CONSTRAIN_PASSWORD);
				if (mtd.getAnnotation(ConstrainPropertyMandatory.class)!=null) {
					m.getConstraints().add(DtObject.CONSTRAIN_PROPERTY_MANDATORY);
				}
			} else {
				if (mtd.getAnnotation(ConstrainPropertyDateTimeFuture.class)!=null) {
					m.getConstraints().add(DtObject.CONSTRAIN_DATETIME_FUTURE);
				} else if (mtd.getAnnotation(ConstrainPropertyDateTimePast.class)!=null) {
					m.getConstraints().add(DtObject.CONSTRAIN_DATETIME_PAST);
				} else {
					if (mtd.getAnnotation(ConstrainPropertyAlphabetic.class)!=null) {
						m.getConstraints().add(DtObject.CONSTRAIN_STRING_ALPHABETIC);
					} else if (mtd.getAnnotation(ConstrainPropertyAlphabeticUnderscore.class)!=null) {
						m.getConstraints().add(DtObject.CONSTRAIN_STRING_ALPHABETIC_UNDERSCORE);
					} else if (mtd.getAnnotation(ConstrainPropertyAlphanumeric.class)!=null) {
						m.getConstraints().add(DtObject.CONSTRAIN_STRING_ALPHANUMERIC);
					} else if (mtd.getAnnotation(ConstrainPropertyAlphanumericUnderscore.class)!=null) {
						m.getConstraints().add(DtObject.CONSTRAIN_STRING_ALPHANUMERIC_UNDERSCORE);
					} else if (mtd.getAnnotation(ConstrainPropertyNoSpace.class)!=null) {
						m.getConstraints().add(DtObject.CONSTRAIN_STRING_NO_SPACE);
					} 
					
					if (mtd.getAnnotation(ConstrainPropertyLength.class)!=null) {
						ConstrainPropertyLength length = mtd.getAnnotation(ConstrainPropertyLength.class);
						if (length!=null) {
							m.setMinValue(length.minValue());
							m.setMaxValue(length.maxValue());
						}
					}
		
					if (mtd.getAnnotation(ConstrainPropertyUpperCase.class)!=null) {
						m.getConstraints().add(DtObject.CONSTRAIN_STRING_UPPER_CASE);
					} else if (mtd.getAnnotation(ConstrainPropertyLowerCase.class)!=null) {
						m.getConstraints().add(DtObject.CONSTRAIN_STRING_LOWER_CASE);
					}
				}
				if (mtd.getAnnotation(ConstrainPropertyMandatory.class)!=null) {
					m.getConstraints().add(DtObject.CONSTRAIN_PROPERTY_MANDATORY);
				}
			}
		}
	}

	private final static String[] getPersistedCollectionUniqeConstraints(Class<?> cls) {
		String[] m = null;
		ConstrainObjectUnique pc = cls.getAnnotation(ConstrainObjectUnique.class);
		if (pc!=null) {
			m = pc.properties();
		}
		return m;
	}
	
	private BtcRepeat getNewBatchRepeat(String name) {
		BtcRepeat repeat = null;
		repeat = new BtcRepeat();
		repeat.getName().setValue(name);
		repeats.put(name, repeat);
		return repeat;
	}

	public final void buildModel() {
		String s = getClass().getName() + "\n";
		
		List<Class<?>> clsList = getPersistedCollectionClasses();
		for (Class<?> cls: clsList) {
			MdlCollection c = new MdlCollection();
			c.setName(cls.getName());
			setPersistedCollectionProperties(cls,c);
			collections.add(c);
			if (!modules.contains(c.getModule())) {
				modules.add(c.getModule());
			}
		}

		for (Class<?> cls: clsList) {
			MdlCollection c = getCollectionByName(cls.getName());
			SortedMap<String,MdlCollectionProperty>	orderedProps = new TreeMap<String,MdlCollectionProperty>();
			for (Method mtd: getPersistedCollectionClassMethods(cls)) {
				String reference = getPersistedCollectionReference(cls, mtd);
				MdlCollection ref = null;
				if (reference.length()>0) {
					ref = getCollectionByName(reference);
				}
				if (ref!=null) {
					MdlCollectionReference m = new MdlCollectionReference();
					m.setModelCollection(c);
					m.setReference(ref);
					setPersistedCollectionPropertyProperties(cls,mtd,m);
					setPersistedCollectionPropertyConstraints(cls,mtd,m);
					setPersistedCollectionReferenceProperties(cls,mtd,m);
					orderedProps.put(m.getName(), m);
				} else {
					MdlCollectionProperty m = new MdlCollectionProperty();
					m.setModelCollection(c);
					setPersistedCollectionPropertyProperties(cls,mtd,m);
					setPersistedCollectionPropertyConstraints(cls,mtd,m);
					orderedProps.put(m.getName(), m);
				}
			}
			for (Entry<String, MdlCollectionProperty> e: orderedProps.entrySet()) {
				c.getProperties().add(e.getValue());
			}
		}

		for (Class<?> cls: clsList) {
			MdlCollection c = getCollectionByName(cls.getName());
			String[] ucs = getPersistedCollectionUniqeConstraints(cls);
			if (ucs!=null) {
				for (String str: getPersistedCollectionUniqeConstraints(cls)) {
					MdlCollectionUniqueConstraint uc = new MdlCollectionUniqueConstraint();
					uc.setModelClass(c);
					for (String p: str.split(";")) {
						if (
							(!p.equals(MdlObject.PROPERTY_CLASSNAME)) &&
							(!p.equals(MdlObject.PROPERTY_ID)) &&
							(!p.equals(MdlObject.PROPERTY_CREATEDBY)) && 
							(!p.equals(MdlObject.PROPERTY_CREATEDON)) && 
							(!p.equals(MdlObject.PROPERTY_CHANGEDBY)) && 
							(!p.equals(MdlObject.PROPERTY_CHANGEDON))
							) {
							MdlCollectionProperty prop = getCollectionPropertyByName(c.getName(),p);
							if (prop!=null) {
								uc.getProperties().add(prop);
							} else {
								Messenger.getInstance().error(this, "Unique index property not found: " + c.getName() + "." + p);
							}
						}
					}
					if (uc.getProperties().size()>0) {
						c.getUniqueConstraints().add(uc);
					}
				}
			}
			s = s + c.getCrc() + "\n";
		}
		crc = Generic.calculateCRC(s);
	}

	public StringBuffer getDescription() {
		StringBuffer desc = new StringBuffer();
		
		String LF = "\r\n";
		String TAB = "\t";
		
		desc.append("Model class: ");
		desc.append(getClass().getName());
		desc.append(LF);
		desc.append("Model CRC: ");
		desc.append(getCrc());
		desc.append(LF);
		for (MdlCollection col: getCollections()) {
			desc.append(LF);
			if (col.isEntity()) {
				desc.append("Entity: ");
			} else {
				desc.append("Collection: ");
			}
			desc.append(col.getName());
			
			desc.append(" (");
			desc.append(col.getNameSingle());
			desc.append("/");
			desc.append(col.getNameMulti());
			desc.append(")");
			desc.append(LF);

			desc.append("User level fetch: ");
			desc.append(col.getUserLevelFetch());
			desc.append(", update: ");
			desc.append(col.getUserLevelUpdate());
			desc.append(", remove: ");
			desc.append(col.getUserLevelRemove());
			desc.append(", add: ");
			desc.append(col.getUserLevelAdd());
			desc.append(LF);

			desc.append("Cacheable: ");
			desc.append(col.isCacheable());
			desc.append(LF);
			
			desc.append("Description:");
			desc.append(LF);
			desc.append(col.getDescription().replaceAll("\n",LF));
			desc.append(LF);
			
			desc.append("Properties:");
			desc.append(LF);

			for (MdlCollectionProperty prop: col.getProperties()) {
				
				desc.append(TAB);
				if (prop instanceof MdlCollectionReference) {
					desc.append("Reference: ");
				} else {
					desc.append("Property: ");
				}
				
				desc.append(prop.getName());
				desc.append(" (");
				desc.append(prop.getLabel());
				desc.append(")");
				desc.append(LF);
				desc.append(TAB);
				desc.append(TAB);
				desc.append("Data type:");
				desc.append(prop.getDataTypeClassName());
				desc.append(LF);
				
				if (prop instanceof MdlCollectionReference) {
					MdlCollectionReference ref = (MdlCollectionReference) prop;
					desc.append(TAB);
					desc.append(TAB);
					desc.append("References: ");
					desc.append(ref.getReference().getName());
					desc.append(" (");
					desc.append(ref.getEntityLabel());
					desc.append(")");
					desc.append(LF);
					desc.append(TAB);
					desc.append(TAB);
					desc.append(TAB);
					desc.append("Entity: ");
					desc.append(ref.isEntity());
					desc.append(LF);
					desc.append(TAB);
					desc.append(TAB);
					desc.append(TAB);
					desc.append("Remove me: ");
					desc.append(ref.isRemoveMe());
					desc.append(LF);
				} else if ((prop.getMinValue()>Long.MIN_VALUE) || (prop.getMaxValue()<Long.MAX_VALUE)) {
					desc.append(TAB);
					desc.append(TAB);
					desc.append("Minimum/maximum: ");
					desc.append(prop.getMinValue());
					desc.append("/");
					desc.append(prop.getMaxValue());
					desc.append(LF);
				}
				if (prop.getConstraints().size()>0) {
					desc.append(TAB);
					desc.append(TAB);
					int c = 0;
					for (String constraint: prop.getConstraints()) {
						if (c>0) {
							desc.append(", ");
						}
						desc.append(constraint);
						c++;
					}
					desc.append(LF);
				}
			}
		}
		return desc;
	}

	/**
	 * @return the crc
	 */
	public long getCrc() {
		return crc;
	}

	/**
	 * @return the adminUser
	 */
	public DbUser getAdminUser(Object source) {
		if (adminUser==null) {
			QryFetch q = new QryFetch(new DbUser().getClassName().getValue(),new Long(1));
			DbIndex.getInstance().executeFetch(q, null, this);
			if (q.getMainResults().getReferences().size()>0) {
				adminUser = (DbUser) q.getMainResults().getReferences().get(0).getDataObject();
				if (
					(!adminUser.getAdmin().getValue()) ||
					(adminUser.getLevel().getValue()!=DbUser.USER_LEVEL_MIN)
					) {
					adminUser.getAdmin().setValue(true);
					adminUser.getLevel().setValue(DbUser.USER_LEVEL_MIN);
					QryTransaction t = new QryTransaction(adminUser);
					t.addQuery(new QryUpdate(adminUser));
					DbIndex.getInstance().executeTransaction(t, source);
				}
			} else {
				DbUser admin = new DbUser();
				admin.getId().setValue(new Long(1)); // Required for createdBy and changedBy
				admin.getName().setValue(INITIAL_ADMIN_USERNAME);
				admin.getPassword().setValue(DbConfig.getInstance().encodePassword(new StringBuffer(INITIAL_ADMIN_PASSWORD)));
				admin.getLevel().setValue(DbUser.USER_LEVEL_MIN);
				admin.getAdmin().setValue(true);
				adminUser = admin;
				QryTransaction t = new QryTransaction(admin);
				t.addQuery(new QryAdd(admin));
				DbIndex.getInstance().executeTransaction(t, source);
			}
		}
		return adminUser;
	}

}
