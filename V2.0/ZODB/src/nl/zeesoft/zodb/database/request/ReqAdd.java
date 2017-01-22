package nl.zeesoft.zodb.database.request;

public class ReqAdd extends ReqObjectChange {
	public ReqAdd(String className) {
		super(className);
	}

	@Override
	protected String getXmlRootElementName() {
		return ADD;
	}

	@Override
	public boolean check(Object source) {
		boolean ok = super.check(source);
		if (ok) {
			if (getObjects().size()==0) {
				addError(ERROR_CODE_NO_OBJECTS_TO_ADD,"No objects to add for add request");
				ok = false;
			}
		}
		return ok;
	}
}
