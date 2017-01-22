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

@PersistCollection(entity=false,module=ZCRMModel.MODULE_ZCRM,nameSingle="Service or product price",nameMulti="Service or product prices")
@CacheableCollection
@ConstrainObjectUnique(properties={"serviceProduct;start"})
@ConstrainObjectAccess(userLevelUpdate=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelRemove=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelAdd=ZCRMModel.USER_LEVEL_CRM_ADMIN)
@DescribeCollection(
description=
	"Service or product prices define historic and future prices for services and/or products.\n" +
	"")
public class CrmServiceProductPrice extends MdlDataObject {
	private DtIdRef 					serviceProduct				= new DtIdRef();
	private DtDateTime					start						= new DtDateTime();
	private DtDecimal					pricePerUnit				= new DtDecimal();
	private DtDecimal					costPerUnit					= new DtDecimal();

	public CrmServiceProductPrice() {
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
		CrmServiceProductPrice changedObject = null;
		if (q instanceof QryAdd) {
			changedObject = (CrmServiceProductPrice) ((QryAdd) q).getDataObject();
		} else if (q instanceof QryUpdate) {
			changedObject = (CrmServiceProductPrice) ((QryUpdate) q).getDataObject();
		}
		if (changedObject!=null) {
			if (changedObject.getStart().getValue()!=null) {
				changedObject.getName().setValue( 
					Generic.getDateTimeString(changedObject.getStart().getValue(),true,false) + " - " + 
					changedObject.getPricePerUnit().getValue()
					);
			}
		}
		boolean ok = super.checkObjectConstraintsForUserQuery(user, q);
		return ok;
	}

	
	@ConstrainPropertyMandatory
	@PersistReference(property="serviceProduct",label="Service or product",className="nl.zeesoft.zcrm.model.impl.CrmServiceProduct",entityLabel="Prices")
	public DtIdRef getServiceProduct() {
		return serviceProduct;
	}

	@ConstrainPropertyMandatory
	@PersistProperty(property="start",label="Start")
	public DtDateTime getStart() {
		return start;
	}
	
	@ConstrainPropertyRange(minValue=0,maxValue=Long.MAX_VALUE)
	@PersistProperty(property="pricePerUnit",label="Price per unit")
	public DtDecimal getPricePerUnit() {
		return pricePerUnit;
	}

	@ConstrainPropertyRange(minValue=0,maxValue=Long.MAX_VALUE)
	@PersistProperty(property="costPerUnit",label="Cost per unit")
	public DtDecimal getCostPerUnit() {
		return costPerUnit;
	}
}
