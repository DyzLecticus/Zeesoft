package nl.zeesoft.zodb.database.request;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlProperty;


public class ReqUpdate extends ReqObjectGetAndChange {
	public ReqUpdate(String className) {
		super(className);
	}

	public ReqUpdate(String className,long id) {
		super(className,id);
	}

	public ReqUpdate(ReqGet getRequest) {
		super(getRequest);
	}

	@Override
	protected String getXmlRootElementName() {
		return UPDATE;
	}

	public DbDataObject getUpdateObject() {
		DbDataObject r = null;
		if (getObjects().size()==0) {
			r = new DbDataObject();
			getObjects().add(new ReqDataObject(r));
		} else {
			r = getObjects().get(0).getDataObject();
		}
		return r;
	}

	@Override
	public boolean check(Object source) {
		boolean ok = super.check(source);
		if (ok) {
			if (getObjects().size()==0) {
				addError(ERROR_CODE_UPDATE_OBJECT_NOT_SET,"Update object not set for update request");
				ok = false;
			} else {
				MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(getClassName());
				DbDataObject updateObject = getUpdateObject();
				boolean updatesProp = false;
				for (MdlProperty prop: cls.getPropertiesExtended()) {
					if (!prop.getName().equals(MdlProperty.ID)) {
						if (updateObject.hasPropertyValue(prop.getName())) {
							updatesProp = true;
							break;
						}
					}
				}
				if (!updatesProp) {
					addError(ERROR_CODE_UPDATE_OBJECT_EMPTY,"Update object for update request has no property values to update");
					ok = false;
				}
			}
		}
		return ok;
	}

	@Override
	public void prepare() {
		if (getGet().getIndex()[0]==null) {
			getGet().getProperties().clear();
			getGet().getProperties().add(ReqGet.ALL_PROPERTIES);
			super.prepare();
		} else  {
			DbDataObject updateObject = getUpdateObject(); 
			getObjects().clear();
			for (ReqDataObject reqObj: getGet().getObjects()) {
				DbDataObject mergedObject = reqObj.getDataObject().copy(updateObject);
				mergedObject.setId(reqObj.getDataObject().getId());
				ReqDataObject copy = reqObj.copy();
				copy.setDataObject(mergedObject);
				getObjects().add(copy);
			}
		}
	}
}
