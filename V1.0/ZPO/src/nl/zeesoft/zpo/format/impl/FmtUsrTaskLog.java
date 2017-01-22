package nl.zeesoft.zpo.format.impl;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zadf.format.FmtSeparatedValues;
import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zpo.model.impl.UsrTaskLog;

public class FmtUsrTaskLog extends FmtSeparatedValues {
	@Override
	protected List<DbCollectionProperty> getReportCollectionProperties(DbCollection dbCol, List<DbCollectionProperty> dbProps) {
		List<DbCollectionProperty> lst = new ArrayList<DbCollectionProperty>();
		if (dbCol.getName().getValue().equals(UsrTaskLog.class.getName())) {
			lst.add(getCollectionPropertyByName(UsrTaskLog.class.getName(),"dateTime"));
			lst.add(getCollectionPropertyByName(UsrTaskLog.class.getName(),"duration"));
			lst.add(getCollectionPropertyByName(UsrTaskLog.class.getName(),"task"));
			lst.add(getCollectionPropertyByName(UsrTaskLog.class.getName(),"name"));
			DbCollectionProperty user = getCollectionPropertyByName(UsrTaskLog.class.getName(),"user");
			if (user!=null) {
				lst.add(user);
			}
		}
		return lst;
	}
	
	@Override
	protected boolean getTotalizeProperty(DbCollectionProperty dbProp) {
		boolean totalize = false;
		if (dbProp.getName().equals("duration")) {
			totalize = true;
		}
		return totalize;
	}
}
