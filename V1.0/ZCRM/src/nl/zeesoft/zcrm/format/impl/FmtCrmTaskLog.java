package nl.zeesoft.zcrm.format.impl;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zcrm.model.impl.CrmOrganizationTaskLog;
import nl.zeesoft.zcrm.model.impl.CrmPersonTaskLog;
import nl.zeesoft.zpo.format.impl.FmtUsrTaskLog;
import nl.zeesoft.zpo.model.impl.UsrTaskLog;

public class FmtCrmTaskLog extends FmtUsrTaskLog {
	@Override
	protected List<DbCollectionProperty> getReportCollectionProperties(DbCollection dbCol, List<DbCollectionProperty> dbProps) {
		List<DbCollectionProperty> lst = new ArrayList<DbCollectionProperty>();
		if (
			(dbCol.getName().getValue().equals(UsrTaskLog.class.getName())) ||
			(dbCol.getName().getValue().equals(CrmOrganizationTaskLog.class.getName())) ||
			(dbCol.getName().getValue().equals(CrmPersonTaskLog.class.getName()))
			) {
			lst.add(getCollectionPropertyByName(UsrTaskLog.class.getName(),"dateTime"));
			lst.add(getCollectionPropertyByName(UsrTaskLog.class.getName(),"duration"));
			lst.add(getCollectionPropertyByName(UsrTaskLog.class.getName(),"task"));
			lst.add(getCollectionPropertyByName(UsrTaskLog.class.getName(),"name"));
			DbCollectionProperty user = getCollectionPropertyByName(UsrTaskLog.class.getName(),"user");
			if (user!=null) {
				lst.add(user);
			}
		}
		if (
			(dbCol.getName().getValue().equals(CrmOrganizationTaskLog.class.getName())) ||
			(dbCol.getName().getValue().equals(CrmPersonTaskLog.class.getName()))
			) {
			DbCollectionProperty cust = getCollectionPropertyByName(dbCol.getName().getValue(),"customer");
			if (cust!=null) {
				lst.add(cust);
			}
			DbCollectionProperty servProd = getCollectionPropertyByName(dbCol.getName().getValue(),"serviceProduct");
			if (servProd!=null) {
				lst.add(servProd);
			}
		}
		return lst;
	}
}
