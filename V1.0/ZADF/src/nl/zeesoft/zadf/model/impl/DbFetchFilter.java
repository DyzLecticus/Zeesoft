package nl.zeesoft.zadf.model.impl;

import nl.zeesoft.zadf.model.DbModel;
import nl.zeesoft.zadf.model.ZADFModel;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.annotations.CacheableCollection;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.impl.DbUser;

@PersistCollection(entity=false,module=ZADFModel.MODULE_ZADF,nameSingle="Fetch filter",nameMulti="Fetch filters")
@CacheableCollection
@ConstrainObjectUnique(properties={"fetch;name","fetch;property;operator"})
@ConstrainObjectAccess(userLevelUpdate=DbUser.USER_LEVEL_MIN,userLevelRemove=DbUser.USER_LEVEL_MIN,userLevelAdd=DbUser.USER_LEVEL_MIN)
@DescribeCollection(
description=
	"Fetch filters define filters to limit fetch results.\n" +
	"\n" +
	"The following value constructs will be replaced with context specific values;\n" +
	DbModel.SESSION_DESCRIPTION +
	DbModel.DATE_DESCRIPTION +
	"")
public class DbFetchFilter extends DbFilterObject {
	private DtIdRef 				fetch 			= new DtIdRef();
	
	/**
	 * @return the fetch
	 */
	@ConstrainPropertyMandatory
	@PersistReference(property="fetch",label="Fetch",className="nl.zeesoft.zadf.model.impl.DbFetch",entityLabel="Filters")
	public DtIdRef getFetch() {
		return fetch;
	}
	
	/**
	 * @return the ftc
	 */
	public DbFetch getFtc() {
		DbFetch ftc = null;
		for (MdlDataObject obj: getReferencedObjects("fetch")) {
			ftc = (DbFetch) obj;
			break;
		}
		return ftc;
	}

}
