package nl.zeesoft.zadf.format;

import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.file.XMLElem;
import nl.zeesoft.zodb.file.XMLFile;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.MdlObjectRefList;
import nl.zeesoft.zodb.model.MdlObjectRefListMap;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtIdRefList;
import nl.zeesoft.zodb.model.datatypes.DtObject;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;

public abstract class FmtXML extends FmtObject {
	private boolean		readFormat	= true;
	
	@Override
	public StringBuffer format() {
		StringBuffer result = new StringBuffer();
	
		XMLFile file = new XMLFile();
		file.setRootElement(new XMLElem("data",null,null));

		addEntityXML(file.getRootElement(),null);
		
		if (readFormat) {
			result = file.toStringReadFormat();
		} else {
			result = file.toStringBuffer();
		}

		file.cleanUp();
		
		return result;
	}

	private void addEntityXML(XMLElem parentElem,MdlObjectRefList parentResults) {
		boolean topLevel = false;
		if (parentResults==null) {
			topLevel = true;
			parentResults = getResults().getReferenceListForCollection(QryFetch.MAIN_RESULTS);
		}
		for (MdlObjectRef objRef: parentResults.getReferences()) {
			XMLElem objectElem = new XMLElem(objRef.getClassName().getValue(),null,parentElem);
			MdlDataObject obj = objRef.getDataObject();
			addObjectPropertiesToObjectElement(objectElem,obj);
			if (topLevel) {
				formatted();
			}
			MdlObjectRefListMap childObjectMap = getChildObjects(obj);
			for (String childLabel: childObjectMap.getCollectionList()) {
				MdlObjectRefList children = childObjectMap.getReferenceListForCollection(childLabel);
				String elemName = Generic.removeNonAllowedCharactersFromString(childLabel,Generic.ALPHABETIC).toLowerCase();
				XMLElem childElem = new XMLElem(elemName,null,objectElem);
				if (children.getReferences().size()>0) {
					addEntityXML(childElem,children);
				}
			}
		}
	}
	
	private MdlObjectRefListMap getChildObjects(MdlDataObject parentObject) {
		MdlObjectRefListMap childObjectMap = new MdlObjectRefListMap();
		DbCollection pDbCol = getCollectionByName(parentObject.getClassName().getValue());
		for (DbCollectionProperty cDbProp: this.getReferencesByCollectionId(pDbCol.getId().getValue(),true)) {
			MdlObjectRefList children = new MdlObjectRefList();
			String cColName = getCollectionById(cDbProp.getCollection().getValue()).getName().getValue();
			MdlObjectRefList childResults = getResults().getReferenceListForCollection(cColName);
			for (MdlObjectRef ref: childResults.getReferences()) {
				DtObject cValObj = ref.getDataObject().getPropertyValue(cDbProp.getName().getValue());
				if (cValObj instanceof DtIdRef) {
					DtIdRef idRef = (DtIdRef) cValObj;
					long id = idRef.getValue();
					if ((parentObject.getId().equals(id)) && (children.getMdlObjectRefById(ref.getId().getValue())==null)) {
						children.getReferences().add(ref);
					}
				} else if (cValObj instanceof DtIdRefList) {
					DtIdRefList idRefList = (DtIdRefList) cValObj;
					for (long id: idRefList.getValue()) {
						if ((parentObject.getId().equals(id)) && (children.getMdlObjectRefById(ref.getId().getValue())==null)) {
							children.getReferences().add(ref);
							break;
						}
					}
				}
			}
			childObjectMap.addReferenceList(cDbProp.getEntityLabel().getValue(),children);
		}
		return childObjectMap;
	}
	
	protected void addObjectPropertiesToObjectElement(XMLElem objectElem, MdlDataObject obj) {
		for (DbCollectionProperty dbProp: getCollectionProperties().get(obj.getClassName().getValue())) {
			DtObject valObj = getConvertedValue(obj.getPropertyValue(dbProp.getName().getValue()));
			StringBuffer value = getSafeValue(valObj.toStringBuffer());
			XMLElem valElem = new XMLElem(dbProp.getName().getValue(),value,objectElem);
			if ((valObj instanceof DtString) || (valObj instanceof DtStringBuffer)) {
				valElem.setCData(true);
			}
		}
	}
	
	protected DtObject getConvertedValue(DtObject valObj) {
		return valObj;
	}

	protected StringBuffer getSafeValue(StringBuffer value) {
		return value;
	}

	/**
	 * @param readFormat the readFormat to set
	 */
	protected void setReadFormat(boolean readFormat) {
		this.readFormat = readFormat;
	}
}
