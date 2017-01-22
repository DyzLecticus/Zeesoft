package nl.zeesoft.zadf.model.impl;

import nl.zeesoft.zadf.model.ZADFModel;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.annotations.CacheableCollection;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.impl.DbUser;

@PersistCollection(entity=false,module=ZADFModel.MODULE_ZADF,nameSingle="Filter operator",nameMulti="Filter operators")
@CacheableCollection
@ConstrainObjectUnique
@ConstrainObjectAccess(userLevelUpdate=DbUser.USER_LEVEL_OFF,userLevelRemove=DbUser.USER_LEVEL_OFF,userLevelAdd=DbUser.USER_LEVEL_MIN)
@DescribeCollection(
description=
	"Filter operators define how object property values are compared to filter values.\n" +
	"")
public class DbFilterOperator extends MdlDataObject {
}
