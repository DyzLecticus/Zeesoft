package nl.zeesoft.zpo.model.impl;

import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.annotations.CacheableCollection;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;
import nl.zeesoft.zpo.model.ZPOModel;

@PersistCollection(module=ZPOModel.MODULE_ZPO,nameSingle="Note",nameMulti="Notes")
@CacheableCollection
@ConstrainObjectUnique(properties={"user;name"})
@DescribeCollection(
description=
	"Notes can be used to store personal reminders and general information.\n" +
	"")
public class UsrNote extends MdlDataObject {
	private DtIdRef						user							= new DtIdRef();
	private DtStringBuffer				description						= new DtStringBuffer();

	@ConstrainPropertyMandatory
	@PersistReference(property="user",label="User",className="nl.zeesoft.zodb.model.impl.DbUser",entityLabel="Notes",entity=false)
	public DtIdRef getUser() {
		return user;
	}

	@ConstrainPropertyMandatory
	@PersistProperty(property="description",label="Content")
	public DtStringBuffer getDescription() {
		return description;
	}
}
