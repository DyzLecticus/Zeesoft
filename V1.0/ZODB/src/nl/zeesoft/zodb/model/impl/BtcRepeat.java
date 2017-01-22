package nl.zeesoft.zodb.model.impl;

import nl.zeesoft.zodb.model.ZODBModel;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.annotations.CacheableCollection;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;

@PersistCollection(entity=false,module=ZODBModel.MODULE_ZODB,nameSingle="Batch repeat",nameMulti="Batch repeats")
@CacheableCollection
@ConstrainObjectUnique
@ConstrainObjectAccess(userLevelFetch=20,userLevelUpdate=DbUser.USER_LEVEL_OFF,userLevelRemove=DbUser.USER_LEVEL_OFF,userLevelAdd=DbUser.USER_LEVEL_MIN)
@DescribeCollection(
description=
	"Batch repeats are used to reschedule batch programs.\n" +
	"")
public class BtcRepeat extends MdlDataObject {
	public static final String MINUTE 		= "Minute";
	public static final String HOUR 		= "Hour";
	public static final String DAY 			= "Day";
	public static final String MONTH		= "Month";
	public static final String YEAR			= "Year";
}
