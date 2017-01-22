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
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.impl.DbUser;

@PersistCollection(entity=false,module=ZCRMModel.MODULE_ZCRM,nameSingle="Service or product value added tax",nameMulti="Service or product value added taxes")
@CacheableCollection
@ConstrainObjectUnique(properties={"serviceProduct;start"})
@ConstrainObjectAccess(userLevelUpdate=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelRemove=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelAdd=ZCRMModel.USER_LEVEL_CRM_ADMIN)
@DescribeCollection(
description=
	"Service or product value added taxes define historic and future value added taxes for services and/or products.\n" +
	"")
public class CrmServiceProductValueAddedTax extends MdlDataObject {
	private DtIdRef 					serviceProduct				= new DtIdRef();
	private DtIdRef 					country						= new DtIdRef();
	private DtIdRef 					valueAddedTax				= new DtIdRef();
	private DtDateTime					start						= new DtDateTime();

	public CrmServiceProductValueAddedTax() {
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
		CrmServiceProductValueAddedTax changedObject = null;
		if (q instanceof QryAdd) {
			changedObject = (CrmServiceProductValueAddedTax) ((QryAdd) q).getDataObject();
		} else if (q instanceof QryUpdate) {
			changedObject = (CrmServiceProductValueAddedTax) ((QryUpdate) q).getDataObject();
		}
		if (changedObject!=null) {
			if (changedObject.getStart().getValue()!=null) {
				changedObject.getName().setValue( 
					Generic.getDateTimeString(changedObject.getStart().getValue(),true,false) 
					);
			}
		}
		boolean ok = super.checkObjectConstraintsForUserQuery(user, q);
		return ok;
	}

	
	@ConstrainPropertyMandatory
	@PersistReference(property="serviceProduct",label="Service or product",className="nl.zeesoft.zcrm.model.impl.CrmServiceProduct",entityLabel="Value added taxes")
	public DtIdRef getServiceProduct() {
		return serviceProduct;
	}

	@ConstrainPropertyMandatory
	@PersistReference(property="country",label="Country",className="nl.zeesoft.zcrm.model.impl.CrmCountry",entityLabel="Service or product value added taxes",entity=false)
	public DtIdRef getCountry() {
		return country;
	}

	@ConstrainPropertyMandatory
	@PersistReference(property="valueAddedTax",label="Value added tax",className="nl.zeesoft.zcrm.model.impl.CrmValueAddedTax",entityLabel="Service or product value added taxes",entity=false)
	public DtIdRef getValueAddedTax() {
		return valueAddedTax;
	}
	
	@ConstrainPropertyMandatory
	@PersistProperty(property="start",label="Start")
	public DtDateTime getStart() {
		return start;
	}
}
