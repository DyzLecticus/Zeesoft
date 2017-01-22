package nl.zeesoft.zodb.batch;

import nl.zeesoft.zodb.database.query.QryError;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.model.impl.BtcLog;

public abstract class BtcProgramObject {
	/**
	 * Override to implement
	 * 
	 * @param log The log data object for this execution containing the user
	 * @return A String error message or "";
	 */
	public abstract String execute(BtcLog log);
	
	protected static boolean getTransactionErrors(QryTransaction t,BtcLog log) {
		boolean hasError = false;
		for (QryObject qry: t.getQueries()) {
			for (QryError err: qry.getErrors()) {
				StringBuffer properties = new StringBuffer();
				for (String prop: err.getProperties()) {
					if (properties.length()>0) {
						properties.append(", ");
					}
					properties.append(prop);
				}
				if (properties.length()>0) {
					log.addError(qry.getClass().getName() + ": " + err.toString() + " (" + properties + ")");
				} else {
					log.addError(qry.getClass().getName() + ": " + err.toString());
				}
				hasError = true;
			}
		}
		return hasError;
	}
}
