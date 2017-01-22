package nl.zeesoft.zcrm.model.impl;

import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;
import nl.zeesoft.zodb.model.datatypes.DtDecimal;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;

public abstract class CrmInvoiceDetailObject extends CrmInvoiceSummaryObject {
	private DtDateTime					orderDateTime						= new DtDateTime();
	private DtDateTime					deliveryDateTime					= new DtDateTime();
	private DtDecimal					deliveryUnits						= new DtDecimal();
	private DtIdRef 					serviceProduct						= new DtIdRef();
	private DtStringBuffer				serviceProductName					= new DtStringBuffer();
	private DtStringBuffer				serviceProductDescription			= new DtStringBuffer();
	private DtBoolean					serviceProductService				= new DtBoolean(true);
	private DtDecimal					serviceProductPricePerUnit			= new DtDecimal();
	private DtDecimal					serviceProductCostPerUnit			= new DtDecimal();
	private DtDecimal 					serviceProductVATPercentage			= new DtDecimal();
	private DtDecimal					contractDetailPricePerUnit			= new DtDecimal();

	@PersistProperty(property="orderDateTime",label="Order date time")
	public DtDateTime getOrderDateTime() {
		return orderDateTime;
	}

	@PersistProperty(property="deliveryDateTime",label="Delivery date time")
	public DtDateTime getDeliveryDateTime() {
		return deliveryDateTime;
	}

	@PersistProperty(property="deliveryUnits",label="Delivery units")
	public DtDecimal getDeliveryUnits() {
		return deliveryUnits;
	}

	@ConstrainPropertyMandatory
	@PersistReference(property="serviceProduct",label="Service or product",className="nl.zeesoft.zcrm.model.impl.CrmServiceProduct",entityLabel="Invoice details",removeMe=false)
	public DtIdRef getServiceProduct() {
		return serviceProduct;
	}

	@PersistProperty(property="serviceProductName",label="Service or product name")
	public DtStringBuffer getServiceProductName() {
		return serviceProductName;
	}

	@PersistProperty(property="serviceProductDescription",label="Service or product description")
	public DtStringBuffer getServiceProductDescription() {
		return serviceProductDescription;
	}

	@PersistProperty(property="serviceProductService",label="Service")
	public DtBoolean getServiceProductService() {
		return serviceProductService;
	}

	@PersistProperty(property="serviceProductPricePerUnit",label="Service or product price per unit")
	public DtDecimal getServiceProductPricePerUnit() {
		return serviceProductPricePerUnit;
	}

	@PersistProperty(property="serviceProductCostPerUnit",label="Service or product cost per unit")
	public DtDecimal getServiceProductCostPerUnit() {
		return serviceProductCostPerUnit;
	}

	@PersistProperty(property="serviceProductVATPercentage",label="Service or product VAT Percentage")
	public DtDecimal getServiceProductVATPercentage() {
		return serviceProductVATPercentage;
	}

	@PersistProperty(property="contractDetailPricePerUnit",label="Contract detail price per unit")
	public DtDecimal getContractDetailPricePerUnit() {
		return contractDetailPricePerUnit;
	}

}
