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

@PersistCollection(entity=false,module=ZADFModel.MODULE_ZADF,nameSingle="Reference filter",nameMulti="Reference filters")
@CacheableCollection
@ConstrainObjectUnique(properties={"reference;name"})
@ConstrainObjectAccess(userLevelUpdate=10,userLevelRemove=10,userLevelAdd=10)
@DescribeCollection(
description=
	"Reference filters define filters to limit GUI data access.\n" +
	"Mandatory filters cannot be deactivated within the GUI.\n" +
	"Software filters are not visible within the GUI.\n" +
	"\n" +
	"The following value constructs will be replaced with context specific values;\n" +
	DbModel.SESSION_DESCRIPTION +
	DbModel.DATE_DESCRIPTION +
	"\n" +
	DbModel.CONTEXT_OBJECT_DESCRIPTION +
	"")
public class DbReferenceFilter extends DbFilterSoftwareObject {
	private DtIdRef 				reference 			= new DtIdRef();

	/**
	 * @return the reference
	 */
	@ConstrainPropertyMandatory
	@PersistReference(property="reference",label="Reference",className="nl.zeesoft.zadf.model.impl.DbCollectionProperty",entityLabel="Reference filters")
	public DtIdRef getReference() {
		return reference;
	}

	/**
	 * @return the ref
	 */
	public DbCollectionProperty getRef() {
		DbCollectionProperty ref = null;
		for (MdlDataObject obj: getReferencedObjects("reference")) {
			ref = (DbCollectionProperty) obj;
			break;
		}
		return ref;
	}
	
}
