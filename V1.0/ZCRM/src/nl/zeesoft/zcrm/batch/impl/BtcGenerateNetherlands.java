package nl.zeesoft.zcrm.batch.impl;

import java.math.BigDecimal;

import nl.zeesoft.zcrm.model.impl.CrmCountry;
import nl.zeesoft.zodb.model.impl.BtcLog;

public class BtcGenerateNetherlands extends BtcGenerateObject {
	@Override
	public String execute(BtcLog log) {
		String err = "";
		
		if (getCrmEntityObjectCount(log.getExecutingAsUser())==0) {
			CrmCountry nl = generateCountry(log.getExecutingAsUser(),"Netherlands","NL",new BigDecimal("21.00"),new BigDecimal("6.00"));
			
			log.addLogLine("Added country " + nl.getName().getValue());
		}
		return err;
	}
	
}
