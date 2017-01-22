package nl.zeesoft.zcrm.model.impl;

import nl.zeesoft.zcrm.model.ZCRMModel;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.model.annotations.CacheableCollection;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyDateTimePast;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyLength;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.impl.DbUser;

@PersistCollection(module=ZCRMModel.MODULE_ZCRM,nameSingle="Person",nameMulti="People")
@CacheableCollection
@ConstrainObjectUnique
@ConstrainObjectAccess(userLevelFetch=ZCRMModel.USER_LEVEL_FETCH_PEOPLE,userLevelUpdate=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelRemove=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelAdd=ZCRMModel.USER_LEVEL_CRM_ADMIN)
@DescribeCollection(
description=
	"People define humans.\n" +
	"")
public class CrmPerson extends CrmContactObject {
	private DtString		firstName		= new DtString();
	private DtString		middleName		= new DtString();
	private DtString		preposition		= new DtString();
	private DtString		lastName		= new DtString();
	private DtDateTime		birthDate		= new DtDateTime();
	private DtIdRef			invoiceAddress	= new DtIdRef();
	
	@Override
	public boolean checkObjectConstraintsForUserQuery(DbUser user, QryObject q) {
		CrmPerson changedObject = null;
		if (q instanceof QryAdd) {
			changedObject = (CrmPerson) ((QryAdd) q).getDataObject();
		} else if (q instanceof QryUpdate) {
			changedObject = (CrmPerson) ((QryUpdate) q).getDataObject();
		}
		if (changedObject!=null) {
			changedObject.getFirstName().setValue(changedObject.getFirstName().getValue().trim());
			changedObject.getMiddleName().setValue(changedObject.getMiddleName().getValue().trim());
			changedObject.getPreposition().setValue(changedObject.getPreposition().getValue().trim());
			changedObject.getLastName().setValue(changedObject.getLastName().getValue().trim());
			String name = "";
			if (!changedObject.getFirstName().getValue().equals("")) {
				if (!name.equals("")) {
					name = name + " ";
				}
				name = name + changedObject.getFirstName().getValue();
			}
			if (!changedObject.getMiddleName().getValue().equals("")) {
				if (!name.equals("")) {
					name = name + " ";
				}
				name = name + changedObject.getMiddleName().getValue();
			}
			if (!changedObject.getPreposition().getValue().equals("")) {
				if (!name.equals("")) {
					name = name + " ";
				}
				name = name + changedObject.getPreposition().getValue();
			}
			if (!changedObject.getLastName().getValue().equals("")) {
				if (!name.equals("")) {
					name = name + " ";
				}
				name = name + changedObject.getLastName().getValue();
			}
			changedObject.getName().setValue(name);
		}
		return super.checkObjectConstraintsForUserQuery(user, q);
	}

	@ConstrainPropertyLength(minValue=0,maxValue=64)
	@ConstrainPropertyMandatory
	@PersistProperty(property="firstName",label="First name")
	public DtString getFirstName() {
		return firstName;
	}

	@ConstrainPropertyLength(minValue=0,maxValue=64)
	@PersistProperty(property="middleName",label="Middle name")
	public DtString getMiddleName() {
		return middleName;
	}

	@ConstrainPropertyLength(minValue=0,maxValue=12)
	@PersistProperty(property="preposition",label="Preposition")
	public DtString getPreposition() {
		return preposition;
	}

	@ConstrainPropertyLength(minValue=0,maxValue=128)
	@PersistProperty(property="lastName",label="Last name")
	public DtString getLastName() {
		return lastName;
	}
	
	@ConstrainPropertyDateTimePast
	@PersistProperty(property="birthDate",label="Birth date")
	public DtDateTime getBirthDate() {
		return birthDate;
	}

	@PersistReference(property="invoiceAddress",label="Invoice address",className="nl.zeesoft.zcrm.model.impl.CrmPersonAddress",entityLabel="People",entity=false,removeMe=false)
	public DtIdRef getInvoiceAddress() {
		return invoiceAddress;
	}
}
