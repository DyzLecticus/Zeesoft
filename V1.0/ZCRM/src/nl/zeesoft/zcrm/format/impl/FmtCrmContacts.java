package nl.zeesoft.zcrm.format.impl;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zadf.format.FmtSeparatedValues;
import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zcrm.model.impl.CrmOrganization;
import nl.zeesoft.zcrm.model.impl.CrmPerson;
import nl.zeesoft.zodb.model.MdlObject;

public class FmtCrmContacts extends FmtSeparatedValues {
	@Override
	protected List<DbCollectionProperty> getReportCollectionProperties(DbCollection dbCol, List<DbCollectionProperty> dbProps) {
		List<DbCollectionProperty> lst = new ArrayList<DbCollectionProperty>();
		if (
			(dbCol.getName().getValue().equals(CrmOrganization.class.getName())) ||
			(dbCol.getName().getValue().equals(CrmPerson.class.getName()))
			) {
			lst.add(getCollectionPropertyByName(CrmOrganization.class.getName(),MdlObject.PROPERTY_NAME));
			lst.add(getCollectionPropertyByName(CrmOrganization.class.getName(),"telephone"));
			lst.add(getCollectionPropertyByName(CrmOrganization.class.getName(),"email"));
		}
		if (
			(dbCol.getName().getValue().equals(CrmOrganization.class.getName()))
			) {
			lst.add(getCollectionPropertyByName(CrmOrganization.class.getName(),"website"));
			lst.add(getCollectionPropertyByName(CrmOrganization.class.getName(),"accountNumber"));
			lst.add(getCollectionPropertyByName(CrmOrganization.class.getName(),"chamberOfCommerceCode"));
		}
		if (
			(dbCol.getName().getValue().equals(CrmPerson.class.getName()))
			) {
			lst.add(getCollectionPropertyByName(CrmPerson.class.getName(),"firstName"));
			lst.add(getCollectionPropertyByName(CrmPerson.class.getName(),"middleName"));
			lst.add(getCollectionPropertyByName(CrmPerson.class.getName(),"preposition"));
			lst.add(getCollectionPropertyByName(CrmPerson.class.getName(),"lastName"));
			lst.add(getCollectionPropertyByName(CrmPerson.class.getName(),"birthDate"));
			lst.add(getCollectionPropertyByName(CrmPerson.class.getName(),"invoiceAddress"));
		}
		return lst;
	}
}
