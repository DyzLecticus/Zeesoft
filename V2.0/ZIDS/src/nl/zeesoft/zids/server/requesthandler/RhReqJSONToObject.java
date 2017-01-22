package nl.zeesoft.zids.server.requesthandler;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zids.json.JsElem;
import nl.zeesoft.zids.json.JsFile;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqObject;
import nl.zeesoft.zodb.database.request.ReqRemove;
import nl.zeesoft.zodb.database.request.ReqUpdate;

public final class RhReqJSONToObject {
	public static ReqObject jsonToRequest(JsFile jsf) {
		ReqObject req = null;
		if (jsf.rootElement!=null) {
			StringBuilder type = jsf.rootElement.getChildValueByName("type");
			StringBuilder className = jsf.rootElement.getChildValueByName("className");
			if (type!=null && className!=null) {
				MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(className.toString());
				if (cls!=null) {
					if (type.toString().equals(ReqObject.GET)) {
						req = jsonToGetRequest(jsf.rootElement,cls);
					} else if (type.toString().equals(ReqObject.ADD)) {
						req = jsonToAddRequest(jsf.rootElement,cls);
					} else if (type.toString().equals(ReqObject.UPDATE)) {
						req = jsonToUpdateRequest(jsf.rootElement,cls);
					} else if (type.toString().equals(ReqObject.REMOVE)) {
						req = jsonToRemoveRequest(jsf.rootElement,cls);
					}
				}
			}
		}
		return req;
	}	

	private static ReqGet jsonToGetRequest(JsElem rootElement,MdlClass cls) {
		ReqGet get = new ReqGet(cls.getFullName());
		StringBuilder start = rootElement.getChildValueByName("start");
		StringBuilder limit = rootElement.getChildValueByName("limit");
		if (start!=null && limit!=null) {
			int s = 0;
			int l = 0;
			try {
				s = Integer.parseInt(start.toString());
				l = Integer.parseInt(limit.toString());
			} catch (NumberFormatException e) {
				// Ignore
			}
			get.setStart(s);
			get.setLimit(l);
		}
		StringBuilder orderBy = rootElement.getChildValueByName("orderBy");
		StringBuilder orderAscending = rootElement.getChildValueByName("orderAscending");
		if (orderBy!=null && orderBy.length()>0 && orderAscending!=null) {
			get.setOrderBy(orderBy.toString());
			get.setOrderAscending(Boolean.parseBoolean(orderAscending.toString()));
		}
		JsElem properties = rootElement.getChildByName("properties");
		if (properties!=null && properties.children.size()>0) {
			for (JsElem property: properties.children) {
				if (property.value!=null) {
					get.getProperties().add("" + property.value);
				}
			}
		}
		JsElem filters = rootElement.getChildByName("filters");
		if (filters!=null && filters.children.size()>0) {
			for (JsElem filter: filters.children) {
				StringBuilder property = filter.getChildValueByName("property");
				StringBuilder operator = filter.getChildValueByName("operator");
				StringBuilder value = filter.getChildValueByName("value");
				StringBuilder invert = filter.getChildValueByName("invert");
				if (property!=null && operator!=null && value!=null) {
					if (invert==null) {
						invert = new StringBuilder("false");
					}
					get.addFilter(property.toString(),Boolean.parseBoolean(invert.toString()),operator.toString(),value.toString());
				}
			}
		}
		return get;
	}

	private static ReqAdd jsonToAddRequest(JsElem rootElement,MdlClass cls) {
		ReqAdd add = new ReqAdd(cls.getFullName());
		jsonToRequestObjects(rootElement,cls,add);
		return add;
	}

	private static ReqUpdate jsonToUpdateRequest(JsElem rootElement,MdlClass cls) {
		ReqUpdate update = new ReqUpdate(cls.getFullName());
		JsElem getRequest = rootElement.getChildByName("getRequest");
		if (getRequest!=null && getRequest.children.size()>0) {
			update.setGet(jsonToGetRequest(getRequest,cls));
		}
		jsonToRequestObjects(rootElement,cls,update);
		return update;
	}

	private static ReqRemove jsonToRemoveRequest(JsElem rootElement,MdlClass cls) {
		ReqRemove remove = new ReqRemove(cls.getFullName());
		JsElem getRequest = rootElement.getChildByName("getRequest");
		if (getRequest!=null && getRequest.children.size()>0) {
			remove.setGet(jsonToGetRequest(getRequest,cls));
		}
		return remove;
	}
	
	private static void jsonToRequestObjects(JsElem rootElement,MdlClass cls,ReqObject req) {
		JsElem objects = rootElement.getChildByName("objects");
		if (objects!=null && objects.children.size()>0) {
			for (JsElem reqObj: objects.children) {
				JsElem dataObj = reqObj.getChildByName("object");
				if (dataObj!=null) {
					DbDataObject obj = new DbDataObject();
					for (JsElem propElem: dataObj.children) {
						if (propElem.array) {
							List<Long> value = new ArrayList<Long>();
							for (JsElem idElem: propElem.children) {
								if (idElem.value!=null) {
									try {
										value.add(Long.parseLong(idElem.value.toString()));
									} catch (NumberFormatException e) {
										// Ignore
									}
								}
							}
						} else {
							obj.setPropertyValue(propElem.name,propElem.value);
						}
					}
					req.getObjects().add(new ReqDataObject(obj));
				}
			}
		}
	}
	
}
