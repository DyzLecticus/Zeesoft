package nl.zeesoft.zcrm.model.impl;

import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyRange;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtDecimal;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;

public abstract class CrmContractDetailObject extends MdlDataObject {
	private DtIdRef 					contract							= new DtIdRef();
	private DtIdRef 					serviceProduct						= new DtIdRef();
	private DtDecimal					pricePerUnit						= new DtDecimal();

	@ConstrainPropertyMandatory
	@PersistReference(property="contract",label="Contract",className="nl.zeesoft.zcrm.model.impl.CrmContractObject",entityLabel="Contract details")
	public DtIdRef getContract() {
		return contract;
	}

	@ConstrainPropertyMandatory
	@PersistReference(property="serviceProduct",label="Service product",className="nl.zeesoft.zcrm.model.impl.CrmServiceProduct",entityLabel="Contract details")
	public DtIdRef getServiceProduct() {
		return serviceProduct;
	}

	@ConstrainPropertyRange(minValue=0,maxValue=Long.MAX_VALUE)
	@PersistProperty(property="pricePerUnit",label="Price per unit")
	public DtDecimal getPricePerUnit() {
		return pricePerUnit;
	}
}
