package nl.zeesoft.zadf.model.impl;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zadf.model.ZADFModel;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.model.MdlCollection;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.MdlObjectRefList;
import nl.zeesoft.zodb.model.annotations.CacheableCollection;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyLength;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyRange;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtInteger;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;
import nl.zeesoft.zodb.model.impl.DbUser;

@PersistCollection(module=ZADFModel.MODULE_ZADF,nameSingle="Collection",nameMulti="Collections")
@CacheableCollection
@ConstrainObjectUnique
@ConstrainObjectAccess(userLevelUpdate=10,userLevelRemove=DbUser.USER_LEVEL_OFF,userLevelAdd=DbUser.USER_LEVEL_MIN)
@DescribeCollection(
description=
	"Collections define GUI specific user acces to database collections.\n" +
	"They are also used to expose the database model for the GUI.\n" +
	"Users need a user level that is less or equal to the user level fetch of the collection in order to be able to fetch data from it.\n" +
	"Entity collections are top level entity object collections.\n" +
	"")
public class DbCollection extends MdlDataObject {
	private DtIdRef 					module 						= new DtIdRef();
	private DtBoolean 					entity 						= new DtBoolean(false);
	private DtString 					nameMulti 					= new DtString("");
	private DtString 					nameSingle 					= new DtString("");
	private DtInteger 					userLevelFetch 				= new DtInteger(DbUser.USER_LEVEL_MAX);
	private DtInteger 					userLevelUpdate 			= new DtInteger(DbUser.USER_LEVEL_MAX);
	private DtInteger 					userLevelRemove 			= new DtInteger(DbUser.USER_LEVEL_MAX);
	private DtInteger 					userLevelAdd 				= new DtInteger(DbUser.USER_LEVEL_MAX);
	private DtStringBuffer				description					= new DtStringBuffer();
	
	private List<DbCollectionProperty>	detailProperties			= new ArrayList<DbCollectionProperty>(); 
	private List<DbCollectionProperty>	enabledProperties			= new ArrayList<DbCollectionProperty>(); 
	private List<DbCollectionProperty>	filterProperties			= new ArrayList<DbCollectionProperty>(); 
	private List<DbCollectionProperty>	gridProperties				= new ArrayList<DbCollectionProperty>(); 
	private List<DbCollectionProperty>	selectProperties			= new ArrayList<DbCollectionProperty>();
	
	@Override
	public boolean checkObjectConstraintsForUserQuery(DbUser user, QryObject q) {
		boolean ok = super.checkObjectConstraintsForUserQuery(user, q);
		if (ok) {
			DbCollection changedObject = null;
			if (q instanceof QryAdd) {
				changedObject = (DbCollection) ((QryAdd) q).getDataObject();
			} else if (q instanceof QryUpdate) {
				changedObject = (DbCollection) ((QryUpdate) q).getDataObject();
			}
			if (changedObject!=null) {
				if (!user.getAdmin().getValue()) {
					if (
						(getUserLevelFetch().getValue()<user.getLevel().getValue()) ||
						(changedObject.getUserLevelFetch().getValue()<user.getLevel().getValue())
						) {
						changedObject.getUserLevelFetch().setValue(getUserLevelFetch().getValue());
					}
					if (
						(getUserLevelUpdate().getValue()<user.getLevel().getValue()) ||
						(changedObject.getUserLevelUpdate().getValue()<user.getLevel().getValue())
						) {
						changedObject.getUserLevelUpdate().setValue(getUserLevelUpdate().getValue());
					}
					if ((getUserLevelRemove().getValue()<user.getLevel().getValue()) || 
						(changedObject.getUserLevelRemove().getValue()<user.getLevel().getValue())
						) {
						changedObject.getUserLevelRemove().setValue(getUserLevelRemove().getValue());
					}
					if (
						(getUserLevelAdd().getValue()<user.getLevel().getValue()) ||
						(changedObject.getUserLevelAdd().getValue()<user.getLevel().getValue())
						) {
						changedObject.getUserLevelAdd().setValue(getUserLevelAdd().getValue());
					}
				}
				
				MdlCollection col = DbConfig.getInstance().getModel().getCollectionByName(getName().getValue());
				String propName = "";
				int propValue = 0;
				if (changedObject.getUserLevelFetch().getValue()>col.getUserLevelFetch()) {
					ok = false;
					propName = "userLevelFetch";
					propValue = col.getUserLevelFetch();
				} else if (changedObject.getUserLevelUpdate().getValue()>col.getUserLevelUpdate()) {
					ok = false;
					propName = "userLevelUpdate";
					propValue = col.getUserLevelUpdate();
				} else if (changedObject.getUserLevelRemove().getValue()>col.getUserLevelRemove()) {
					ok = false;
					propName = "userLevelRemove";
					propValue = col.getUserLevelRemove();
				} else if (changedObject.getUserLevelAdd().getValue()>col.getUserLevelAdd()) {
					ok = false;
					propName = "userLevelAdd";
					propValue = col.getUserLevelAdd();
				}
				if (!ok) {
					q.addError("2005",DbConfig.getInstance().getModel().getCollectionPropertyByName(getClassName().getValue(),propName).getName(),"Value is greater than model value: @1","" + propValue);
				}
			}
		}
		return ok;
	}

	public String getToolTipText() {
		String r = null;
		if (!getDescription().getValue().equals("")) {
			StringBuffer t = new StringBuffer();
			t.append("<html>");
			t.append(getDescription().getValue().toString().replaceAll("\n", "<br/>"));
			t.append("</html>");
			r = t.toString();
		}
		return r;
	}
	
	public DbCollectionProperty getPropertyByName(String propName) {
		DbCollectionProperty dbProp = null;
		for (DbCollectionProperty p: getProperties()) {
			if (p.getName().equals(propName)) {
				dbProp = p;
				break;
			}
		}
		return dbProp;
	}
	
	public void initializeOrderedPropertyLists(int userLevel) {
		detailProperties.clear();
		MdlObjectRefList props = new MdlObjectRefList();
		for (DbCollectionProperty prop: getProperties()) {
			if ((prop.getUserLevelVisible().getValue()>=userLevel) &&
				(prop.getOrderInDetail().getValue()>=0) &&
				((prop.getRefColl()==null) || (prop.getRefColl().getUserLevelFetch().getValue()>=userLevel))
				) {
				props.getReferences().add(new MdlObjectRef(prop));
			}
		}
		props.sortObjects("orderInDetail");
		for (MdlObjectRef ref: props.getReferences()) {
			DbCollectionProperty prop = (DbCollectionProperty) ref.getDataObject();
			detailProperties.add(prop);
			if (prop.getUserLevelEnabled().getValue()>=userLevel) {
				enabledProperties.add(prop);
			}
		}

		filterProperties.clear();
		props = new MdlObjectRefList();
		for (DbCollectionProperty prop: getProperties()) {
			if ((prop.getUserLevelVisible().getValue()>=userLevel) && 
				(prop.getOrderInFilter().getValue()>=0) &&
				((prop.getRefColl()==null) || (prop.getRefColl().getUserLevelFetch().getValue()>=userLevel))
				) {
				props.getReferences().add(new MdlObjectRef(prop));
			}
		}
		props.sortObjects("orderInFilter");
		for (MdlObjectRef ref: props.getReferences()) {
			DbCollectionProperty prop = (DbCollectionProperty) ref.getDataObject();
			filterProperties.add(prop);
		}
		
		gridProperties.clear();
		props = new MdlObjectRefList();
		for (DbCollectionProperty prop: getProperties()) {
			if ((prop.getUserLevelVisible().getValue()>=userLevel) && 
				(prop.getOrderInGrid().getValue()>=0) &&
				((prop.getRefColl()==null) || (prop.getRefColl().getUserLevelFetch().getValue()>=userLevel))
				) {
				props.getReferences().add(new MdlObjectRef(prop));
			}
		}
		props.sortObjects("orderInGrid");
		for (MdlObjectRef ref: props.getReferences()) {
			DbCollectionProperty prop = (DbCollectionProperty) ref.getDataObject();
			gridProperties.add(prop);
		}

		selectProperties.clear();
		props = new MdlObjectRefList();
		for (DbCollectionProperty prop: getProperties()) {
			if ((prop.getUserLevelVisible().getValue()>=userLevel) && 
				(prop.getOrderInSelect().getValue()>=0) &&
				((prop.getRefColl()==null) || (prop.getRefColl().getUserLevelFetch().getValue()>=userLevel))
				) {
				props.getReferences().add(new MdlObjectRef(prop));
			}
		}
		props.sortObjects("orderInSelect");
		for (MdlObjectRef ref: props.getReferences()) {
			DbCollectionProperty prop = (DbCollectionProperty) ref.getDataObject();
			selectProperties.add(prop);
		}
	}

	/**
	 * @return the module
	 */
	@ConstrainPropertyMandatory
	@PersistReference(property="module",label="Module",className="nl.zeesoft.zadf.model.impl.DbModule",entity=true,entityLabel="Collections")
	public DtIdRef getModule() {
		return module;
	}

	/**
	 * @return the nameMulti
	 */
	@ConstrainPropertyLength(minValue=0,maxValue=64)
	@ConstrainPropertyMandatory
	@PersistProperty(property = "nameMulti",label="Multiple")
	public DtString getNameMulti() {
		return nameMulti;
	}

	/**
	 * @return the nameSingle
	 */
	@ConstrainPropertyLength(minValue=0,maxValue=64)
	@ConstrainPropertyMandatory
	@PersistProperty(property = "nameSingle",label="Single")
	public DtString getNameSingle() {
		return nameSingle;
	}

	/**
	 * @return the entity
	 */
	@PersistProperty(property = "entity",label="Entity")
	public DtBoolean getEntity() {
		return entity;
	}
	
	/**
	 * @return the userLevelFetch
	 */
	@ConstrainPropertyRange(minValue=DbUser.USER_LEVEL_MIN,maxValue=DbUser.USER_LEVEL_MAX)
	@PersistProperty(property = "userLevelFetch",label="User level fetch")
	public DtInteger getUserLevelFetch() {
		return userLevelFetch;
	}

	/**
	 * @return the userLevelUpdate
	 */
	@ConstrainPropertyRange(minValue=DbUser.USER_LEVEL_OFF,maxValue=DbUser.USER_LEVEL_MAX)
	@PersistProperty(property = "userLevelUpdate",label="User level update")
	public DtInteger getUserLevelUpdate() {
		return userLevelUpdate;
	}

	/**
	 * @return the userLevelRemove
	 */
	@ConstrainPropertyRange(minValue=DbUser.USER_LEVEL_OFF,maxValue=DbUser.USER_LEVEL_MAX)
	@PersistProperty(property = "userLevelRemove",label="User level remove")
	public DtInteger getUserLevelRemove() {
		return userLevelRemove;
	}

	/**
	 * @return the userLevelAdd
	 */
	@ConstrainPropertyRange(minValue=DbUser.USER_LEVEL_OFF,maxValue=DbUser.USER_LEVEL_MAX)
	@PersistProperty(property = "userLevelAdd",label="User level add")
	public DtInteger getUserLevelAdd() {
		return userLevelAdd;
	}

	/**
	 * @return the description
	 */
	@ConstrainPropertyLength(minValue=0,maxValue=2048)
	@PersistProperty(property = "description",label="Description")
	public DtStringBuffer getDescription() {
		return description;
	}

	/**
	 * @return the properties
	 */
	public List<DbCollectionProperty> getProperties() {
		List<DbCollectionProperty> list = new ArrayList<DbCollectionProperty>();
		String childCollectionAndPropertyName = DbCollectionProperty.class.getName() + Generic.SEP_STR + "collection";
		for (MdlDataObject obj: getChildObjects(childCollectionAndPropertyName)) {
			list.add((DbCollectionProperty) obj);
		}
		return list;
	}

	/**
	 * @return the filters
	 */
	public List<DbCollectionFilter> getFilters() {
		List<DbCollectionFilter> list = new ArrayList<DbCollectionFilter>();
		String childCollectionAndPropertyName = DbCollectionFilter.class.getName() + Generic.SEP_STR + "collection";
		for (MdlDataObject obj: getChildObjects(childCollectionAndPropertyName)) {
			list.add((DbCollectionFilter) obj);
		}
		return list;
	}

	/**
	 * @return the referencedProperties
	 */
	public List<DbCollectionProperty> getReferencedProperties() {
		List<DbCollectionProperty> list = new ArrayList<DbCollectionProperty>();
		String childCollectionAndPropertyName = DbCollectionProperty.class.getName() + Generic.SEP_STR + "referenceCollection";
		for (MdlDataObject obj: getChildObjects(childCollectionAndPropertyName)) {
			list.add((DbCollectionProperty) obj);
		}
		return list;
	}

	/**
	 * @return the detailProperties
	 */
	public List<DbCollectionProperty> getDetailProperties() {
		return detailProperties;
	}

	/**
	 * @return the enabledProperties
	 */
	public List<DbCollectionProperty> getEnabledProperties() {
		return enabledProperties;
	}

	/**
	 * @return the gridProperties
	 */
	public List<DbCollectionProperty> getGridProperties() {
		return gridProperties;
	}

	/**
	 * @return the selectProperties
	 */
	public List<DbCollectionProperty> getSelectProperties() {
		return selectProperties;
	}

	/**
	 * @return the filterProperties
	 */
	public List<DbCollectionProperty> getFilterProperties() {
		return filterProperties;
	}
}
