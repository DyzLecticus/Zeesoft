package nl.zeesoft.zodb.database.request;

import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

public abstract class ReqObjectGetAndChange extends ReqObjectChange {
	private ReqGet get = null;

	public ReqObjectGetAndChange(String className) {
		super(className);
		get = new ReqGet(className);
	}

	public ReqObjectGetAndChange(String className,long id) {
		super(className);
		get = new ReqGet(className,id);
	}

	public ReqObjectGetAndChange(ReqGet getRequest) {
		super(getRequest.getClassName());
		get = getRequest;
	}

	protected void copy(ReqObjectGetAndChange original, ReqObjectGetAndChange copy) {
		super.copy(original, copy);
		ReqGet g = new ReqGet(original.getGet().getClassName());
		g.copy(original.getGet(),g);
		copy.setGet(g);
	}
	
	@Override
	public XMLFile toXML() {
		XMLFile file = super.toXML();
		if (get!=null) {
			get.toXML().getRootElement().setParent(file.getRootElement());
		}
		return file;
	}

	@Override
	public void fromXML(XMLElem rootElem) {
		super.fromXML(rootElem);
		if (rootElem.getName().equals(getXmlRootElementName())) {
			get = new ReqGet(getClassName());
			for (XMLElem cElem: rootElem.getChildren()) {
				if (cElem.getName().equals(get.getXmlRootElementName())) {
					get.fromXML(cElem);
					break;
				} 
			}
		}
	}
	
	@Override
	public boolean check(Object source) {
		boolean ok = super.check(source);
		if (ok) {
			if (get==null) {
				addError(ERROR_CODE_GET_REQUEST_NOT_SET,"Get request not found for: " + this.getClass().getName());
			} else {
				ok = get.check(source);
			}
		}
		return ok;
	}

	@Override
	public void prepare() {
		super.prepare();
		if (get.getIndex()[0]==null) {
			get.setFilterStrict(true);
			get.prepare();
		}
	}

	/**
	 * @return the get
	 */
	public ReqGet getGet() {
		return get;
	}

	/**
	 * @param get the get to set
	 */
	public void setGet(ReqGet get) {
		this.get = get;
	}
}
