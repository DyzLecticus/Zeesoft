package nl.zeesoft.zcrm.model.impl;

import nl.zeesoft.zcrm.model.ZCRMModel;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.annotations.CacheableCollection;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.impl.DbUser;

@PersistCollection(module=ZCRMModel.MODULE_ZCRM,nameSingle="Country",nameMulti="Countries")
@CacheableCollection
@ConstrainObjectUnique(properties={"name","code"})
@ConstrainObjectAccess(userLevelUpdate=DbUser.USER_LEVEL_MIN,userLevelRemove=DbUser.USER_LEVEL_MIN,userLevelAdd=DbUser.USER_LEVEL_MIN)
@DescribeCollection(
description=
	"Countries define large geographical collections of addresses with specific value added tax laws.\n" +
	"")
public class CrmCountry extends MdlDataObject {
	private DtString					code						= new DtString();

	@ConstrainPropertyMandatory
	@PersistProperty(property="code",label="Code")
	public DtString getCode() {
		return code;
	}
}
