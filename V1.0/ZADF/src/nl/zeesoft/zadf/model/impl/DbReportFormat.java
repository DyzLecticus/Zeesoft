package nl.zeesoft.zadf.model.impl;

import nl.zeesoft.zadf.format.FmtObject;
import nl.zeesoft.zadf.model.ZADFModel;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.MdlObject;
import nl.zeesoft.zodb.model.annotations.CacheableCollection;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.impl.DbUser;

@PersistCollection(entity=false,module=ZADFModel.MODULE_ZADF,nameSingle="Report format",nameMulti="Report formats")
@CacheableCollection
@ConstrainObjectUnique(properties={"name"})
@ConstrainObjectAccess(userLevelUpdate=DbUser.USER_LEVEL_MIN,userLevelRemove=DbUser.USER_LEVEL_OFF,userLevelAdd=DbUser.USER_LEVEL_MIN)
@DescribeCollection(
description=
	"Reports formats specify how fetch results are to be formatted.\n" +
	"")
public class DbReportFormat extends MdlDataObject {
	private DtString extension = new DtString();

	@Override
	public boolean checkObjectConstraintsForUserQuery(DbUser user, QryObject q) {
		boolean ok = super.checkObjectConstraintsForUserQuery(user, q);
		if (ok) {
			DbReportFormat changedObject = null;
			if (q instanceof QryAdd) {
				changedObject = (DbReportFormat) ((QryAdd) q).getDataObject();
			} else if (q instanceof QryUpdate) {
				changedObject = (DbReportFormat) ((QryUpdate) q).getDataObject();
			}
			if (changedObject!=null) {
				Object test = Generic.testInstanceForName(changedObject.getName().getValue());
				if (test==null) {
					q.addError("2006",MdlObject.PROPERTY_NAME,"Unable to instantiate report format class with this name");
					ok = false;
				} else if (!(test instanceof FmtObject)) {
					q.addError("2007",MdlObject.PROPERTY_NAME,"Batch programs must extend: " + FmtObject.class.getName());
					ok = false;
				}
			}
		}
		return ok;
	}
	
	/**
	 * @return the extension
	 */
	@PersistProperty(property="extension",label="File extension")
	public DtString getExtension() {
		return extension;
	}
}
