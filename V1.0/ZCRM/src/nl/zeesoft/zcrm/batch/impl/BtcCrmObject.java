package nl.zeesoft.zcrm.batch.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zcrm.model.impl.CrmDeliveryOrganization;
import nl.zeesoft.zcrm.model.impl.CrmServiceProduct;
import nl.zeesoft.zcrm.model.impl.CrmServiceProductPrice;
import nl.zeesoft.zcrm.model.impl.CrmServiceProductValueAddedTax;
import nl.zeesoft.zodb.batch.BtcProgramObject;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.database.query.QryFetchList;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtLong;
import nl.zeesoft.zodb.model.impl.BtcLog;
import nl.zeesoft.zodb.model.impl.DbUser;

public abstract class BtcCrmObject extends BtcProgramObject {
	protected static final int						MAX_RESULT_SIZE		= 100;
	
	protected List<CrmDeliveryOrganization> 		organizations		= null;
	protected SortedMap<Long,CrmServiceProduct> 	serviceProducts 	= new TreeMap<Long,CrmServiceProduct>(); 
	
	@Override
	public String execute(BtcLog log) {
		organizations = getDeliveryOrganizations(log.getExecutingAsUser());
		if (organizations.size()==0) {
			log.addLogLine("No active delivery organizations found");
			return "";
		}
		String err = "";
		
		return err;
	}

	private List<CrmDeliveryOrganization> getDeliveryOrganizations(DbUser executeAsUser) {
		List<CrmDeliveryOrganization> organizations = new ArrayList<CrmDeliveryOrganization>();

		QryFetchList fl = new QryFetchList(executeAsUser);
		QryFetch f = new QryFetch(CrmDeliveryOrganization.class.getName());

		f.getConditions().add(new QryFetchCondition("active",QryFetchCondition.OPERATOR_EQUALS,new DtBoolean(true)));
		fl.addQuery(f);
		
		DbIndex.getInstance().executeFetchList(fl,this);
		
		for (QryObject qo: fl.getQueries()) {
			f = (QryFetch) qo;
			if (f.getMainResults().getReferences().size()>0) {
				for (MdlObjectRef ref: f.getMainResults().getReferences()) {
					organizations.add((CrmDeliveryOrganization) ref.getDataObject());
				}
			}
		}
		
		return organizations;
	}

	protected CrmServiceProduct getServiceProductById(long id,DbUser executeAsUser) {
		CrmServiceProduct serviceProduct = serviceProducts.get(id);
		if (serviceProduct==null) {
			QryFetch f = new QryFetch(CrmServiceProduct.class.getName(),id);
			DbIndex.getInstance().executeFetch(f,executeAsUser,this);
			if (f.getMainResults().getReferences().size()>0) {
				serviceProduct = (CrmServiceProduct) f.getMainResults().getReferences().get(0).getDataObject();
				serviceProducts.put(id,serviceProduct);
			}
			if (serviceProduct!=null) {
				f = new QryFetch(CrmServiceProductPrice.class.getName());
				f.getConditions().add(new QryFetchCondition("serviceProduct",QryFetchCondition.OPERATOR_CONTAINS,new DtLong(serviceProduct.getId().getValue())));
				f.setOrderBy("start",false);
				DbIndex.getInstance().executeFetch(f,executeAsUser,this);
				for (MdlObjectRef ref: f.getMainResults().getReferences()) {
					serviceProduct.addChildObject("prices", ref.getDataObject());
				}
				f = new QryFetch(CrmServiceProductValueAddedTax.class.getName());
				f.getConditions().add(new QryFetchCondition("serviceProduct",QryFetchCondition.OPERATOR_CONTAINS,new DtLong(serviceProduct.getId().getValue())));
				f.setOrderBy("start",false);
				DbIndex.getInstance().executeFetch(f,executeAsUser,this);
				for (MdlObjectRef ref: f.getMainResults().getReferences()) {
					serviceProduct.addChildObject("valueAddedTaxes", ref.getDataObject());
				}
			}
		}
		return serviceProduct;
	}
	
}
