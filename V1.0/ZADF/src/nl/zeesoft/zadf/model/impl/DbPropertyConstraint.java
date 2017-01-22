package nl.zeesoft.zadf.model.impl;

import nl.zeesoft.zadf.model.ZADFModel;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.annotations.CacheableCollection;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.impl.DbUser;

@PersistCollection(entity=false,module=ZADFModel.MODULE_ZADF,nameSingle="Property constraint",nameMulti="Property constraints")
@CacheableCollection
@ConstrainObjectUnique
@ConstrainObjectAccess(userLevelUpdate=DbUser.USER_LEVEL_OFF,userLevelRemove=DbUser.USER_LEVEL_OFF,userLevelAdd=DbUser.USER_LEVEL_MIN)
@DescribeCollection(
description=
	"Property constraints limit and or format object property values.\n" +
	"")
public class DbPropertyConstraint extends MdlDataObject {
}
