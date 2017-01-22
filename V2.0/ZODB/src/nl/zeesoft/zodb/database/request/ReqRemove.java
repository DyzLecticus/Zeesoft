package nl.zeesoft.zodb.database.request;

import java.util.List;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlLink;
import nl.zeesoft.zodb.database.model.MdlProperty;

public class ReqRemove extends ReqObjectGetAndChange {
	public ReqRemove(String className) {
		super(className);
	}

	public ReqRemove(String className,long id) {
		super(className,id);
	}

	public ReqRemove(ReqGet getRequest) {
		super(getRequest);
	}

	@Override
	protected String getXmlRootElementName() {
		return REMOVE;
	}
	
	@Override
	public void prepare() {
		getObjects().clear();
		if (getGet().getIndex()[0]==null) {
			getGet().getProperties().clear();
			getGet().getProperties().add(MdlProperty.ID);
			getGet().getChildIndexes().clear();
			getGet().getChildIndexes().add(ReqGet.ALL_CHILD_INDEXES);
			super.prepare();
		} else  {
			for (ReqDataObject reqObj: getGet().getObjects()) {
				getObjects().add(reqObj.copy());
			}
		}
	}

	@Override
	public List<String> getClassNames() {
		List<String> classNames = super.getClassNames();
		MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(getClassName());
		for (MdlLink cLink: cls.getChildLinks()) {
			if (!classNames.contains(cLink.getCls().getName())) {
				classNames.add(cLink.getCls().getName());
			}
		}
		return classNames;
	}
	
}
 