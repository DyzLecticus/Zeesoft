package nl.zeesoft.zadf.model.impl;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zadf.model.ZADFModel;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.model.MdlDataObject;
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
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtIdRefList;
import nl.zeesoft.zodb.model.datatypes.DtInteger;
import nl.zeesoft.zodb.model.impl.DbUser;

@PersistCollection(entity=false,module=ZADFModel.MODULE_ZADF,nameSingle="Fetch",nameMulti="Fetches")
@CacheableCollection
@ConstrainObjectUnique(properties={"collection;name"})
@ConstrainObjectAccess(userLevelUpdate=DbUser.USER_LEVEL_MIN,userLevelRemove=DbUser.USER_LEVEL_MIN,userLevelAdd=DbUser.USER_LEVEL_MIN)
@DescribeCollection(
description=
	"Fetches define database data retrieval requests.\n" +
	"When one or more entities are selected an entity request is performed.\n" +
	"")
public class DbFetch extends MdlDataObject {
	private DtIdRef 		collection 			= new DtIdRef();
	private DtIdRefList		entities 			= new DtIdRefList();
	private DtIdRef			orderBy 			= new DtIdRef();
	private DtBoolean 		orderAscending		= new DtBoolean(true);
	private DtInteger 		limit				= new DtInteger(100);
	
	/**
	 * @return the collection
	 */
	@ConstrainPropertyMandatory
	@PersistReference(property="collection",label="Collection",className="nl.zeesoft.zadf.model.impl.DbCollection",entityLabel="Fetches")
	public DtIdRef getCollection() {
		return collection;
	}

	/**
	 * @return the entities
	 */
	@PersistReference(property="entities",label="Entities",className="nl.zeesoft.zadf.model.impl.DbCollection",entity=false,entityLabel="Fetches")
	public DtIdRefList getEntities() {
		return entities;
	}

	/**
	 * @return the orderBy
	 */
	@PersistReference(property="orderBy",label="Order by",className="nl.zeesoft.zadf.model.impl.DbCollectionProperty",entity=false,entityLabel="Fetches")
	public DtIdRef getOrderBy() {
		return orderBy;
	}

	/**
	 * @return the orderAscending
	 */
	@PersistProperty(property = "orderAscending",label="Ascending")
	public DtBoolean getOrderAscending() {
		return orderAscending;
	}

	/**
	 * @return the limit
	 */
	@ConstrainPropertyRange(minValue=1,maxValue=1000)
	@PersistProperty(property = "limit",label="Limit")
	public DtInteger getLimit() {
		return limit;
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
	 * @return the entities
	 */
	public List<DbCollection> getEntiyColls() {
		List<DbCollection> list = new ArrayList<DbCollection>();
		for (MdlDataObject obj: getReferencedObjects("entities")) {
			list.add((DbCollection) obj);
		}
		return list;
	}

	/**
	 * @return the oby
	 */
	public DbCollectionProperty getOBy() {
		DbCollectionProperty prop = null;
		for (MdlDataObject obj: getReferencedObjects("orderBy")) {
			prop = (DbCollectionProperty) obj;
			break;
		}
		return prop;
	}

	/**
	 * @return the filters
	 */
	public List<DbFetchFilter> getFilters() {
		List<DbFetchFilter> list = new ArrayList<DbFetchFilter>();
		String childCollectionAndPropertyName = DbFetchFilter.class.getName() + Generic.SEP_STR + "fetch";
		for (MdlDataObject obj: getChildObjects(childCollectionAndPropertyName)) {
			list.add((DbFetchFilter) obj);
		}
		return list;
	}
}
