package nl.zeesoft.zids.server.requesthandler;

import java.util.List;

import nl.zeesoft.zids.json.JsElem;
import nl.zeesoft.zids.json.JsFile;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlLink;
import nl.zeesoft.zodb.database.model.MdlNumber;
import nl.zeesoft.zodb.database.model.MdlProperty;
import nl.zeesoft.zodb.database.model.MdlString;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqError;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqGetFilter;
import nl.zeesoft.zodb.database.request.ReqObject;
import nl.zeesoft.zodb.database.request.ReqObjectChange;
import nl.zeesoft.zodb.database.request.ReqRemove;
import nl.zeesoft.zodb.database.request.ReqUpdate;

public final class RhReqObjectToJSON {
	public static JsFile requestToJSON(ReqObject req) {
		JsFile jsf = new JsFile();
		jsf.rootElement = new JsElem();

		JsElem cElem = null;
		cElem = new JsElem();
		jsf.rootElement.children.add(cElem);
		cElem.name = "type";
		if (req instanceof ReqGet) {
			cElem.value = new StringBuilder(ReqObject.GET);
		} else if (req instanceof ReqAdd) {
			cElem.value = new StringBuilder(ReqObject.ADD);
		} else if (req instanceof ReqUpdate) {
			cElem.value = new StringBuilder(ReqObject.UPDATE);
		} else if (req instanceof ReqRemove) {
			cElem.value = new StringBuilder(ReqObject.REMOVE);
		}
		cElem.cData = true;

		cElem = new JsElem();
		jsf.rootElement.children.add(cElem);
		cElem.name = "className";
		cElem.value = new StringBuilder(req.getClassName());
		cElem.cData = true;

		cElem = new JsElem();
		jsf.rootElement.children.add(cElem);
		cElem.name = "log";
		cElem.value = Generic.stringBuilderReplace(req.getLog(),"\n",". ");
		cElem.cData = true;

		if (req.hasError()) {
			JsElem eElem = new JsElem();
			jsf.rootElement.children.add(eElem);
			eElem.name = "errors";
			eElem.array = true;
			for (ReqError err: req.getErrors()) {
				JsFile errJsf = reqErrorToJSON(err);
				eElem.children.add(errJsf.rootElement);
			}
		}

		if (req instanceof ReqGet) {
			JsFile getJsf = getRequestToJSON((ReqGet) req);
			for (JsElem child: getJsf.rootElement.children) {
				jsf.rootElement.children.add(child);
			}
		} else if (req instanceof ReqObjectChange) {
			ReqObjectChange reqChange = (ReqObjectChange) req;
			JsFile impactedIdsJsf = getImpactedIdsToJSON(reqChange.getImpactedIds());
			jsf.rootElement.children.add(impactedIdsJsf.rootElement);
		}
		
		return jsf;
	}
	
	private static JsFile getRequestToJSON(ReqGet get) {
		JsFile jsf = new JsFile();
		jsf.rootElement = new JsElem();
		
		JsElem cElem = null;

		cElem = new JsElem();
		jsf.rootElement.children.add(cElem);
		cElem.name = "start";
		cElem.value = new StringBuilder("" + get.getStart());

		cElem = new JsElem();
		jsf.rootElement.children.add(cElem);
		cElem.name = "limit";
		cElem.value = new StringBuilder("" + get.getLimit());

		if (get.getOrderBy().length()>0) {
			cElem = new JsElem();
			jsf.rootElement.children.add(cElem);
			cElem.name = "orderBy";
			cElem.value = new StringBuilder(get.getOrderBy());
			
			cElem = new JsElem();
			jsf.rootElement.children.add(cElem);
			cElem.name = "orderAscending";
			cElem.value = new StringBuilder("" + get.isOrderAscending());
		}

		if (get.getProperties().size()>0) {
			JsElem pElem = new JsElem();
			jsf.rootElement.children.add(pElem);
			pElem.name = "properties";
			pElem.array = true;
			for (String propName: get.getProperties()) {
				JsElem propElem = new JsElem();
				pElem.children.add(propElem);
				propElem.value = new StringBuilder(propName);
				propElem.cData = true;
			}
		}
		
		if (get.getFilters().size()>0) {
			JsElem fElem = new JsElem();
			jsf.rootElement.children.add(fElem);
			fElem.name = "filters";
			fElem.array = true;
			for (ReqGetFilter filter: get.getFilters()) {
				JsFile filtJsf = reqGetFilterToJSON(filter);
				fElem.children.add(filtJsf.rootElement);
			}
		}

		cElem = new JsElem();
		jsf.rootElement.children.add(cElem);
		cElem.name = "count";
		cElem.value = new StringBuilder("" + get.getCount());
		
		if (get.getObjects().size()>0) {
			cElem = new JsElem();
			jsf.rootElement.children.add(cElem);
			cElem.name = "objects";
			cElem.array = true;
			MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(get.getClassName());
			for (ReqDataObject object: get.getObjects()) {
				JsFile objJsf = reqDataObjectToJSON(object,cls);
				cElem.children.add(objJsf.rootElement);
			}
		}
		
		return jsf;
	}

	private static JsFile getImpactedIdsToJSON(List<Long> impactedIds) {
		JsFile jsf = new JsFile();
		jsf.rootElement = new JsElem();
		jsf.rootElement.name = "impactedIds";
		jsf.rootElement.array = true;
		if (impactedIds.size()>0) {
			for (long id: impactedIds) {
				JsElem cElem = new JsElem();
				jsf.rootElement.children.add(cElem);
				cElem.value = new StringBuilder("" + id);
			}
		} else {
			jsf.rootElement.children.add(new JsElem());
		}
		return jsf;
	}
	
	private static JsFile reqErrorToJSON(ReqError err) {
		JsFile jsf = new JsFile();
		jsf.rootElement = new JsElem();

		JsElem cElem = null;
		
		cElem = new JsElem();
		jsf.rootElement.children.add(cElem);
		cElem.name = "code";
		cElem.value = new StringBuilder(err.getCode());
		cElem.cData = true;
		
		cElem = new JsElem();
		jsf.rootElement.children.add(cElem);
		cElem.name = "message";
		cElem.value = new StringBuilder(err.getMessage());
		cElem.cData = true;

		if (err.getProperties().size()>0) {
			JsElem pElem = new JsElem();
			jsf.rootElement.children.add(pElem);
			pElem.name = "properties";
			pElem.array = true;
			for (String propName: err.getProperties()) {
				JsElem propElem = new JsElem();
				pElem.children.add(propElem);
				propElem.value = new StringBuilder(propName);
				propElem.cData = true;
			}
		}
		
		return jsf;
	}

	private static JsFile reqGetFilterToJSON(ReqGetFilter filter) {
		JsFile jsf = new JsFile();
		jsf.rootElement = new JsElem();

		JsElem cElem = null;
		
		cElem = new JsElem();
		jsf.rootElement.children.add(cElem);
		cElem.name = "value";
		cElem.value = new StringBuilder(filter.getProperty());
		cElem.cData = true;
		
		cElem = new JsElem();
		jsf.rootElement.children.add(cElem);
		cElem.name = "operator";
		cElem.value = new StringBuilder(filter.getOperator());
		cElem.cData = true;

		cElem = new JsElem();
		jsf.rootElement.children.add(cElem);
		cElem.name = "value";
		cElem.value = new StringBuilder(filter.getValue());
		cElem.cData = true;

		if (filter.isInvert()) {
			cElem = new JsElem();
			jsf.rootElement.children.add(cElem);
			cElem.name = "invert";
			cElem.value = new StringBuilder("" + filter.isInvert());
		}
		
		return jsf;
	}
	
	private static JsFile reqDataObjectToJSON(ReqDataObject obj,MdlClass cls) {
		JsFile jsf = new JsFile();
		jsf.rootElement = new JsElem();
		JsFile objJsf = dbDataObjectToJSON(obj.getDataObject(),cls);
		jsf.rootElement.children.add(objJsf.rootElement);
		if (obj.getErrors().size()>0) {
			JsElem eElem = new JsElem();
			jsf.rootElement.children.add(eElem);
			eElem.name = "errors";
			eElem.array = true;
			for (ReqError err: obj.getErrors()) {
				JsFile errJsf = reqErrorToJSON(err);
				eElem.children.add(errJsf.rootElement);
			}
		}
		return jsf;
	}
	
	private static JsFile dbDataObjectToJSON(DbDataObject obj,MdlClass cls) {
		JsFile jsf = new JsFile();
		jsf.rootElement = new JsElem();
		jsf.rootElement.name = "object";
		
		JsElem cElem = null;

		for (MdlProperty prop: cls.getPropertiesExtended()) {
			if (obj.hasPropertyValue(prop.getName())) {
				cElem = new JsElem();
				jsf.rootElement.children.add(cElem);
				cElem.name = prop.getName();
				if (prop instanceof MdlString || prop instanceof MdlNumber) {
					cElem.value = obj.getPropertyValue(prop.getName());
					if (prop instanceof MdlString && cElem.value!=null) {
						cElem.cData = true;
					}
				} else if (prop instanceof MdlLink) {
					List<Long> value = obj.getLinkValue(prop.getName());
					if (value!=null) {
						for (long id: value) {
							JsElem idElem = new JsElem();
							cElem.children.add(idElem);
							cElem.array = true;
							idElem.value = new StringBuilder("" + id);
						}
					} else {
						cElem.value = null;
					}
				}
			}
		}
		
		return jsf;
	}
}
