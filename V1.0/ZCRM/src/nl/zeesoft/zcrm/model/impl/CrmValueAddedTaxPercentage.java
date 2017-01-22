package nl.zeesoft.zcrm.model.impl;

import java.util.Calendar;

import nl.zeesoft.zcrm.model.ZCRMModel;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.annotations.CacheableCollection;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyRange;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;
import nl.zeesoft.zodb.model.datatypes.DtDecimal;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.impl.DbUser;

@PersistCollection(entity=false,module=ZCRMModel.MODULE_ZCRM,nameSingle="Value added tax percentage",nameMulti="Value added tax percentages")
@CacheableCollection
@ConstrainObjectUnique(properties={"valueAddedTax;start"})
@ConstrainObjectAccess(userLevelUpdate=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelRemove=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelAdd=ZCRMModel.USER_LEVEL_CRM_ADMIN)
@DescribeCollection(
description=
	"Value added tax percentages define historic and future value added tax percentages.\n" +
	"")
public class CrmValueAddedTaxPercentage extends MdlDataObject {
	private DtIdRef 					valueAddedTax				= new DtIdRef();
	private DtDateTime					start						= new DtDateTime();
	private DtDecimal 					percentage					= new DtDecimal();

	public CrmValueAddedTaxPercentage() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH,0);
		cal.set(Calendar.DATE,1);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);

		getStart().setValue(cal.getTime());
	}
	
	@Override
	public boolean checkObjectConstraintsForUserQuery(DbUser user, QryObject q) {
		CrmValueAddedTaxPercentage changedObject = null;
		if (q instanceof QryAdd) {
			changedObject = (CrmValueAddedTaxPercentage) ((QryAdd) q).getDataObject();
		} else if (q instanceof QryUpdate) {
			changedObject = (CrmValueAddedTaxPercentage) ((QryUpdate) q).getDataObject();
		}
		if (changedObject!=null) {
			if (changedObject.getStart().getValue()!=null) {
				changedObject.getName().setValue( 
					Generic.getDateTimeString(changedObject.getStart().getValue(),true,false) + " - " + 
					changedObject.getPercentage().getValue() + "%"
					);
			}
		}
		boolean ok = super.checkObjectConstraintsForUserQuery(user, q);
		return ok;
	}

	@ConstrainPropertyMandatory
	@PersistReference(property="valueAddedTax",label="Value added tax",className="nl.zeesoft.zcrm.model.impl.CrmValueAddedTax",entityLabel="Percentages")
	public DtIdRef getValueAddedTax() {
		return valueAddedTax;
	}
	
	@ConstrainPropertyMandatory
	@PersistProperty(property="start",label="Start")
	public DtDateTime getStart() {
		return start;
	}

	@ConstrainPropertyRange(minValue=0,maxValue=100)
	@PersistProperty(property="percentage",label="Percentage")
	public DtDecimal getPercentage() {
		return percentage;
	}
}
