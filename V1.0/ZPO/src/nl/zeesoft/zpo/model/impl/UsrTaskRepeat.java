package nl.zeesoft.zpo.model.impl;

import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.annotations.CacheableCollection;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.impl.DbUser;
import nl.zeesoft.zpo.model.ZPOModel;

@PersistCollection(module=ZPOModel.MODULE_ZPO,nameSingle="Task repeat",nameMulti="Task repeats")
@CacheableCollection
@ConstrainObjectUnique(properties={"name"})
@ConstrainObjectAccess(userLevelUpdate=DbUser.USER_LEVEL_MIN,userLevelRemove=DbUser.USER_LEVEL_MIN,userLevelAdd=DbUser.USER_LEVEL_MIN)
@DescribeCollection(
description=
	"Task repeats can be used to indicate weekly repeating tasks.\n" +
	"")
public class UsrTaskRepeat extends MdlDataObject {
	public static final String			SUNDAY							= "Sunday";
	public static final String			MONDAY							= "Monday";
	public static final String			TUESDAY							= "Tuesday";
	public static final String			WEDNESDAY						= "Wednesday";
	public static final String			THURSDAY						= "Thursday";
	public static final String			FRIDAY							= "Friday";
	public static final String			SATURDAY						= "Saturday";

	private DtBoolean					active							= new DtBoolean(true);

	@PersistProperty(property="active",label="Active")
	public DtBoolean getActive() {
		return active;
	}
}
