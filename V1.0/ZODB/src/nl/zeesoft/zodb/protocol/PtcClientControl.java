package nl.zeesoft.zodb.protocol;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.client.ClSessionManager;
import nl.zeesoft.zodb.event.EvtEvent;

public class PtcClientControl extends PtcClient {
	
	@Override
	public StringBuffer processInputAndReturnOutput(StringBuffer input) {
		StringBuffer output = new StringBuffer();

		if ((input!=null) && (input.length()>0)) {
			output = super.processInputAndReturnOutput(input);
			if ((output==null) || (output.length()==0)) {
				if (Generic.stringBufferStartsWith(input,GET_SERVER_IS_WORKING)) {
					String[] val = Generic.getValuesFromString(Generic.stringBufferReplace(input,"\n","").toString());
					if ((val.length>1) && (!val[1].trim().equals(""))) {
						ClSessionManager.getInstance().publishEvent(new EvtEvent(ClSessionManager.SERVER_IS_WORKING, getSession(), Boolean.parseBoolean(val[1].trim())));
						output = null;
					}
				} else if (Generic.stringBufferStartsWith(input,GET_BATCH_IS_WORKING)) {
					String[] val = Generic.getValuesFromString(Generic.stringBufferReplace(input,"\n","").toString());
					if ((val.length>1) && (!val[1].trim().equals(""))) {
						ClSessionManager.getInstance().publishEvent(new EvtEvent(ClSessionManager.BATCH_IS_WORKING, getSession(), Boolean.parseBoolean(val[1].trim())));
						output = null;
					}
				} else if (Generic.stringBufferStartsWith(input,GET_SERVER_CACHE)) {
					String[] val = Generic.getValuesFromString(Generic.stringBufferReplace(input,"\n","").toString());
					if ((val.length>1) && (!val[1].trim().equals(""))) {
						ClSessionManager.getInstance().publishEvent(new EvtEvent(ClSessionManager.SERVER_CACHE, getSession(), val[1].trim()));
						output = null;
					}
				} else if (
					(Generic.stringBufferStartsWith(input,GET_SERVER_PROPERTIES)) || 
					(Generic.stringBufferStartsWith(input,SET_SERVER_PROPERTIES))
					) {
					ClSessionManager.getInstance().publishEvent(new EvtEvent(ClSessionManager.SERVER_PROPERTIES, getSession(),Generic.stringBufferReplace(input,"\n","")));
					output = null;
				}
			}
		}

		return output;
	}
}
