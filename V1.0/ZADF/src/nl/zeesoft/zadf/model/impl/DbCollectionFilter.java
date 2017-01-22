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

@PersistCollection(entity=false,module=ZADFModel.MODULE_ZADF,nameSingle="Collection filter",nameMulti="Collection filters")
@CacheableCollection
@ConstrainObjectUnique(properties={"collection;name"})
@ConstrainObjectAccess(userLevelUpdate=10,userLevelRemove=10,userLevelAdd=10)
@DescribeCollection(
description=
	"Collections filters define filters to limit GUI data access.\n" +
	"Mandatory filters cannot be deactivated within the GUI.\n" +
	"Software filters are not visible within the GUI.\n" +
	"\n" +
	"The following value constructs will be replaced with context specific values;\n" +
	DbModel.SESSION_DESCRIPTION +
	DbModel.DATE_DESCRIPTION +
	"")
public class DbCollectionFilter extends DbFilterSoftwareObject {
	private DtIdRef 				collection 			= new DtIdRef();

	/**
	 * @return the collection
	 */
	@ConstrainPropertyMandatory
	@PersistReference(property="collection",label="Collection",className="nl.zeesoft.zadf.model.impl.DbCollection",entityLabel="Filters")
	public DtIdRef getCollection() {
		return collection;
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

}
