package nl.zeesoft.zcrm.model.impl;

import java.util.Calendar;
import java.util.Date;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyRange;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtInteger;
import nl.zeesoft.zodb.model.impl.DbUser;

public abstract class CrmDeliveryObject extends MdlDataObject {
	private DtDateTime					orderDateTime						= new DtDateTime();
	private DtDateTime					deliveryDateTime					= new DtDateTime();
	private DtIdRef 					serviceProduct						= new DtIdRef();
	private DtInteger					units								= new DtInteger(1);

	public CrmDeliveryObject() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		Date today = cal.getTime();
		
		getDeliveryDateTime().setValue(today);
	}
	
	@Override
	public boolean checkObjectConstraintsForUserQuery(DbUser user, QryObject q) {
		CrmDeliveryObject changedObject = null;
		if (q instanceof QryAdd) {
			changedObject = (CrmDeliveryObject) ((QryAdd) q).getDataObject();
		} else if (q instanceof QryUpdate) {
			changedObject = (CrmDeliveryObject) ((QryUpdate) q).getDataObject();
		}
		if (changedObject!=null) {
			if (changedObject.getName().getValue().trim().equals("")) {
				changedObject.getName().setValue(Generic.getDateTimeString(changedObject.getDeliveryDateTime().getValue(),true,false) + "-" + changedObject.getUnits().getValue() + "-" + changedObject.getServiceProduct().getValue());
			}
			if (changedObject.getOrderDateTime().getValue()==null) {
				changedObject.getOrderDateTime().setValue(changedObject.getDeliveryDateTime().getValue());
			}
		}
		return super.checkObjectConstraintsForUserQuery(user, q);
	}

	@PersistProperty(property="orderDateTime",label="Order date and time")
	public DtDateTime getOrderDateTime() {
		return orderDateTime;
	}

	@ConstrainPropertyMandatory
	@PersistProperty(property="deliveryDateTime",label="Delivery date and time")
	public DtDateTime getDeliveryDateTime() {
		return deliveryDateTime;
	}
	
	@ConstrainPropertyMandatory
	@PersistReference(property="serviceProduct",label="Service or product",className="nl.zeesoft.zcrm.model.impl.CrmServiceProduct",entityLabel="Deliveries")
	public DtIdRef getServiceProduct() {
		return serviceProduct;
	}

	@ConstrainPropertyRange(minValue=1,maxValue=Integer.MAX_VALUE)
	@PersistProperty(property="units",label="Units")
	public DtInteger getUnits() {
		return units;
	}
}
