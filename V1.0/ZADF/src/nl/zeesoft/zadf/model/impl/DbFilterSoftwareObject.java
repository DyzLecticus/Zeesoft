package nl.zeesoft.zadf.model.impl;

import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.impl.DbUser;

public abstract class DbFilterSoftwareObject extends DbFilterObject {
	private DtBoolean 		software			= new DtBoolean(false);
	private DtBoolean 		mandatory 			= new DtBoolean(false);
	
	@Override
	public boolean checkObjectConstraintsForUserQuery(DbUser user, QryObject q) {
		boolean ok = super.checkObjectConstraintsForUserQuery(user, q);
		if (ok) {
			DbFilterSoftwareObject changedObject = null;
			if (q instanceof QryAdd) {
				changedObject = (DbFilterSoftwareObject) ((QryAdd) q).getDataObject();
			} else if (q instanceof QryUpdate) {
				changedObject = (DbFilterSoftwareObject) ((QryUpdate) q).getDataObject();
			}
			if (changedObject!=null) {
				if (changedObject.getSoftware().getValue()) {
					changedObject.getMandatory().setValue(true);
				}
			}
		}
		return ok;
	}

	/**
	 * @return the mandatory
	 */
	@PersistProperty(property="mandatory",label="Mandatory")
	public DtBoolean getMandatory() {
		return mandatory;
	}

	/**
	 * @return the software
	 */
	@PersistProperty(property="software",label="Software")
	public DtBoolean getSoftware() {
		return software;
	}
}
