package nl.zeesoft.zpo.model.impl;

import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.annotations.CacheableCollection;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;
import nl.zeesoft.zpo.model.ZPOModel;

@PersistCollection(module=ZPOModel.MODULE_ZPO,nameSingle="Task",nameMulti="Tasks")
@CacheableCollection
@ConstrainObjectUnique(properties={"user;name"})
@DescribeCollection(
description=
	"Tasks represent activities that need to be done.\n" +
	"Task repeats can be used to separate weekly tasks from irregular tasks.\n" +
	"")
public class UsrTask extends MdlDataObject {
	private DtIdRef						user							= new DtIdRef();
	private DtStringBuffer				description						= new DtStringBuffer();
	private DtBoolean					active							= new DtBoolean(true);
	private DtIdRef						repeat							= new DtIdRef();

	@ConstrainPropertyMandatory
	@PersistReference(property="user",label="User",className="nl.zeesoft.zodb.model.impl.DbUser",entityLabel="Tasks",entity=false)
	public DtIdRef getUser() {
		return user;
	}

	@ConstrainPropertyMandatory
	@PersistProperty(property="description",label="Description")
	public DtStringBuffer getDescription() {
		return description;
	}

	@PersistProperty(property="active",label="Active")
	public DtBoolean getActive() {
		return active;
	}

	@PersistReference(property="repeat",label="Repeat",className="nl.zeesoft.zpo.model.impl.UsrTaskRepeat",entityLabel="Tasks",entity=false)
	public DtIdRef getRepeat() {
		return repeat;
	}
}

