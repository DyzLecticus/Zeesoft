package nl.zeesoft.zodb.database.model;

import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

public abstract class MdlObject {
	private MdlModel	model		= null;

	protected MdlObject(MdlModel mdl) {
		model = mdl;
	}
	
	/**
	 * @return the model
	 */
	protected MdlModel getModel() {
		return model;
	}

	protected abstract XMLFile toXML();

	protected abstract void fromXML(XMLElem rootElem);

	protected abstract String getFullName();

	protected abstract void cleanUp();
}
