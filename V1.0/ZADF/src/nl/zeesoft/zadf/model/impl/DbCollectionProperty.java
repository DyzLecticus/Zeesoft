package nl.zeesoft.zadf.model.impl;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zadf.model.ZADFModel;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.MdlObject;
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
import nl.zeesoft.zodb.model.datatypes.DtIdRefList;
import nl.zeesoft.zodb.model.datatypes.DtInteger;
import nl.zeesoft.zodb.model.datatypes.DtLong;
import nl.zeesoft.zodb.model.datatypes.DtObject;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.impl.DbUser;

@PersistCollection(entity=false,module=ZADFModel.MODULE_ZADF,nameSingle="Property",nameMulti="Properties")
@CacheableCollection
@ConstrainObjectUnique(properties={"collection;name"})
@ConstrainObjectAccess(userLevelUpdate=10,userLevelRemove=DbUser.USER_LEVEL_OFF,userLevelAdd=DbUser.USER_LEVEL_MIN)
@DescribeCollection(
description=
	"Properties define GUI acces to collection object properties.\n" +
	"Entity reference properties define lower level entity object collections.\n" +
	"")
public class DbCollectionProperty extends MdlDataObject {
	private DtString 		label				= new DtString();
	private DtBoolean 		entity				= new DtBoolean(false);
	private DtString 		entityLabel			= new DtString();
	private DtIdRef 		collection 			= new DtIdRef();
	private DtInteger 		userLevelVisible 	= new DtInteger(DbUser.USER_LEVEL_MAX);
	private DtInteger 		userLevelEnabled 	= new DtInteger(DbUser.USER_LEVEL_MAX);
	private DtInteger 		orderInFilter		= new DtInteger(0);
	private DtInteger 		orderInDetail		= new DtInteger(0);
	private DtInteger 		orderInGrid			= new DtInteger(0);
	private DtInteger 		orderInSelect		= new DtInteger(0);
	private DtString 		propertyClassName	= new DtString();
	private DtIdRef 		referenceCollection	= new DtIdRef();
	private DtIdRefList 	constraints			= new DtIdRefList();
	private DtLong 			minValue 			= new DtLong(Long.MIN_VALUE);
	private DtLong 			maxValue 			= new DtLong(Long.MIN_VALUE);

	@Override
	public boolean checkObjectConstraintsForUserQuery(DbUser user, QryObject q) {
		boolean ok = super.checkObjectConstraintsForUserQuery(user, q);
		if (ok) {
			DbCollectionProperty changedObject = null;
			if (q instanceof QryAdd) {
				changedObject = (DbCollectionProperty) ((QryAdd) q).getDataObject();
			} else if (q instanceof QryUpdate) {
				changedObject = (DbCollectionProperty) ((QryUpdate) q).getDataObject();
			}
			if (changedObject!=null) {
				if (
					(getName().getValue().equals(MdlObject.PROPERTY_ID)) ||
					(getName().getValue().equals(MdlObject.PROPERTY_CLASSNAME)) ||
					(getName().getValue().equals(MdlObject.PROPERTY_CREATEDBY)) ||
					(getName().getValue().equals(MdlObject.PROPERTY_CREATEDON)) ||
					(getName().getValue().equals(MdlObject.PROPERTY_CHANGEDBY)) ||
					(getName().getValue().equals(MdlObject.PROPERTY_CHANGEDON))
					) {
					changedObject.getUserLevelEnabled().setValue(DbUser.USER_LEVEL_OFF);
				}
				if (
					(getName().getValue().equals(MdlObject.PROPERTY_CLASSNAME))
					) {
					changedObject.getOrderInDetail().setValue(-1);
					changedObject.getOrderInFilter().setValue(-1);
					changedObject.getOrderInGrid().setValue(-1);
					changedObject.getOrderInSelect().setValue(-1);
					changedObject.getUserLevelVisible().setValue(DbUser.USER_LEVEL_MIN);
				}

				if (!user.getAdmin().getValue()) {
					if (changedObject.getUserLevelEnabled().getValue()<user.getLevel().getValue()) {
						changedObject.getUserLevelEnabled().setValue(getUserLevelEnabled().getValue());
					}
					if (changedObject.getUserLevelVisible().getValue()<user.getLevel().getValue()) {
						changedObject.getUserLevelVisible().setValue(getUserLevelVisible().getValue());
					}
				}

			}
		}
		return ok;
	}
	
	/**
	 * @return the label
	 */
	@ConstrainPropertyLength(minValue=0,maxValue=64)
	@ConstrainPropertyMandatory
	@PersistProperty(property = "label",label="Label")
	public DtString getLabel() {
		return label;
	}

	/**
	 * @return the entity
	 */
	@PersistProperty(property = "entity",label="Entity")
	public DtBoolean getEntity() {
		return entity;
	}

	/**
	 * @return the entityLabel
	 */
	@ConstrainPropertyLength(minValue=0,maxValue=64)
	@PersistProperty(property = "entityLabel",label="Entity label")
	public DtString getEntityLabel() {
		return entityLabel;
	}

	/**
	 * @return the userLevelVisible
	 */
	@ConstrainPropertyRange(minValue=DbUser.USER_LEVEL_MIN,maxValue=DbUser.USER_LEVEL_MAX)
	@PersistProperty(property = "userLevelVisible",label="User level visible")
	public DtInteger getUserLevelVisible() {
		return userLevelVisible;
	}

	/**
	 * @return the userLevelEnabled
	 */
	@ConstrainPropertyRange(minValue=DbUser.USER_LEVEL_OFF,maxValue=DbUser.USER_LEVEL_MAX)
	@PersistProperty(property = "userLevelEnabled",label="User level enabled")
	public DtInteger getUserLevelEnabled() {
		return userLevelEnabled;
	}

	/**
	 * @return the minValue
	 */
	@PersistProperty(property = "minValue",label="Minimum value")
	public DtLong getMinValue() {
		return minValue;
	}
	
	/**
	 * @return the maxValue
	 */
	@PersistProperty(property = "maxValue",label="Maximum value")
	public DtLong getMaxValue() {
		return maxValue;
	}
	
	/**
	 * @return the propertyClassName
	 */
	@ConstrainPropertyMandatory
	@PersistProperty(property = "propertyClassName",label="Property class name")
	public DtString getPropertyClassName() {
		return propertyClassName;
	}

	/**
	 * @return the collection
	 */
	@ConstrainPropertyMandatory
	@PersistReference(property="collection",label="Collection",className="nl.zeesoft.zadf.model.impl.DbCollection",entityLabel="Properties")
	public DtIdRef getCollection() {
		return collection;
	}

	/**
	 * @return the referenceCollection
	 */
	@PersistReference(property="referenceCollection",label="Reference collection",className="nl.zeesoft.zadf.model.impl.DbCollection",entityLabel="Reference properties",entity=false)
	public DtIdRef getReferenceCollection() {
		return referenceCollection;
	}

	/**
	 * @return the constraints
	 */
	@PersistReference(property="constraints",label="Constraints",className="nl.zeesoft.zadf.model.impl.DbPropertyConstraint",removeMe=false,entityLabel="Properties")
	public DtIdRefList getConstraints() {
		return constraints;
	}
	
	/**
	 * @return the orderInGrid
	 */
	@ConstrainPropertyRange(minValue=-1,maxValue=Integer.MAX_VALUE)
	@PersistProperty(property = "orderInGrid",label="Order in grid")
	public DtInteger getOrderInGrid() {
		return orderInGrid;
	}

	/**
	 * @return the orderInSelect
	 */
	@ConstrainPropertyRange(minValue=-1,maxValue=Integer.MAX_VALUE)
	@PersistProperty(property = "orderInSelect",label="Order in select")
	public DtInteger getOrderInSelect() {
		return orderInSelect;
	}

	/**
	 * @return the orderInDetail
	 */
	@ConstrainPropertyRange(minValue=-1,maxValue=Integer.MAX_VALUE)
	@PersistProperty(property = "orderInDetail",label="Order in details")
	public DtInteger getOrderInDetail() {
		return orderInDetail;
	}

	/**
	 * @return the orderInFilter
	 */
	@ConstrainPropertyRange(minValue=-1,maxValue=Integer.MAX_VALUE)
	@PersistProperty(property = "orderInFilter",label="Order in filter")
	public DtInteger getOrderInFilter() {
		return orderInFilter;
	}

	/**
	 * @return the coll
	 */
	public DbCollection getColl() {
		DbCollection col = null;
		for (MdlDataObject obj: getReferencedObjects("collection")) {
			col = (DbCollection) obj;
			break;
		}
		return col;
	}

	/**
	 * @return the refColl
	 */
	public DbCollection getRefColl() {
		DbCollection col = null;
		for (MdlDataObject obj: getReferencedObjects("referenceCollection")) {
			col = (DbCollection) obj;
			break;
		}
		return col;
	}

	/**
	 * @return the reference filters
	 */
	public List<DbReferenceFilter> getFilters() {
		List<DbReferenceFilter> list = new ArrayList<DbReferenceFilter>();
		String childCollectionAndPropertyName = DbReferenceFilter.class.getName() + Generic.SEP_STR + "reference";
		for (MdlDataObject obj: getChildObjects(childCollectionAndPropertyName)) {
			list.add((DbReferenceFilter) obj);
		}
		return list;
	}

	/**
	 * @return the constraints
	 */
	public List<DbPropertyConstraint> getConstrs() {
		List<DbPropertyConstraint> list = new ArrayList<DbPropertyConstraint>();
		for (MdlDataObject obj: getReferencedObjects("constraints")) {
			list.add((DbPropertyConstraint) obj);
		}
		return list;
	}
	
	public String getToolTipText(DtObject valueObject) {
		String toolTip = "";
		if (valueObject!=null) {
			String[] elem = valueObject.getClass().getName().split("\\.");
			toolTip = elem[(elem.length-1)];
			if (getReferenceCollection().getValue()>0) {
				toolTip = toolTip + " -> " + getRefColl().getNameMulti();
			}
			List<DbPropertyConstraint> constraints = getConstrs();
			String constr = "";
			if (constraints.size()>0) {
				for (DbPropertyConstraint constraint: constraints) {
					if (!constraint.getName().getValue().equals(DtObject.CONSTRAIN_PROPERTY_MANDATORY)) {
						if (!constr.equals("")) {
							constr = constr + ", ";
						}
						constr = constr + constraint.getName().getValue();
					}
				}
			}
			String minMax = "";
			if ((getMinValue().getValue()>Long.MIN_VALUE) || (getMaxValue().getValue()<Long.MAX_VALUE)) {
				minMax = getMinValue().getValue() + "/" + getMaxValue().getValue();
				if (!constr.equals("")) {
					constr = constr + ", ";
				}
			}
			if ((!constr.equals("")) || (!minMax.equals(""))) {
				toolTip = toolTip + " (" + constr + minMax + ")";
			}
		}
		if (toolTip.equals("")) {
			toolTip = null;
		}
		return toolTip;
	}
}
