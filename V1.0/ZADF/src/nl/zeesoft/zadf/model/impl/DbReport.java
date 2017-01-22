package nl.zeesoft.zadf.model.impl;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zadf.model.ZADFModel;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.model.MdlDataObject;
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
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtIdRefList;
import nl.zeesoft.zodb.model.datatypes.DtInteger;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;
import nl.zeesoft.zodb.model.impl.DbUser;

@PersistCollection(module=ZADFModel.MODULE_ZADF,nameSingle="Report",nameMulti="Reports")
@CacheableCollection
@ConstrainObjectUnique(properties={"name"})
@ConstrainObjectAccess(userLevelUpdate=10,userLevelRemove=10,userLevelAdd=10)
@DescribeCollection(
description=
	"Reports define database data reports.\n" +
	"Reports use one or more fetches to retrieve database data and specify how the results are to be formatted.\n" +
	"")
public class DbReport extends MdlDataObject {
	private DtStringBuffer	description			= new DtStringBuffer();
	private DtIdRefList		fetches				= new DtIdRefList();
	private DtIdRef 		format 				= new DtIdRef();
	private DtInteger 		userLevelVisible 	= new DtInteger(10);

	@Override
	public boolean checkObjectConstraintsForUserQuery(DbUser user, QryObject q) {
		boolean ok = super.checkObjectConstraintsForUserQuery(user, q);
		if (ok) {
			DbReport changedObject = null;
			if (q instanceof QryAdd) {
				changedObject = (DbReport) ((QryAdd) q).getDataObject();
			} else if (q instanceof QryUpdate) {
				changedObject = (DbReport) ((QryUpdate) q).getDataObject();
			}
			if (changedObject!=null) {
				if (changedObject.getUserLevelVisible().getValue()<user.getLevel().getValue()) {
					changedObject.getUserLevelVisible().setValue(getUserLevelVisible().getValue());
				}
			}
		}
		return ok;
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
	 * @return the fetches
	 */
	@ConstrainPropertyMandatory
	@PersistReference(property="fetches",label="Fetches",className="nl.zeesoft.zadf.model.impl.DbFetch",entityLabel="Reports",removeMe=false)
	public DtIdRefList getFetches() {
		return fetches;
	}

	/**
	 * @return the format
	 */
	@ConstrainPropertyMandatory
	@PersistReference(property="format",label="Format",className="nl.zeesoft.zadf.model.impl.DbReportFormat",entityLabel="Reports",entity=false,removeMe=false)
	public DtIdRef getFormat() {
		return format;
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
	 * @return the ftcs
	 */
	public List<DbFetch> getFtcs() {
		List<DbFetch> ftcs = new ArrayList<DbFetch>();
		for (MdlDataObject obj: getReferencedObjects("fetches")) {
			ftcs.add((DbFetch) obj);
		}
		return ftcs;
	}

	/**
	 * @return the fmt
	 */
	public DbReportFormat getFmt() {
		DbReportFormat fmt = null;
		for (MdlDataObject obj: getReferencedObjects("format")) {
			fmt = (DbReportFormat) obj;
			break;
		}
		return fmt;
	}
}
