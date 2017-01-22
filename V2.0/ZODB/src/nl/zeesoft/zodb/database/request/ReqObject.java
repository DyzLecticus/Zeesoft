package nl.zeesoft.zodb.database.request;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventPublisher;
import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

public abstract class ReqObject extends EvtEventPublisher {
	public static final String				GET										= "get";
	public static final String				ADD										= "add";
	public static final String				UPDATE									= "update";
	public static final String				REMOVE									= "remove";
	
	public static final String				REQUEST_DONE							= "REQUEST_DONE";
	
	public static final String 				ERROR_CODE_CLASS_NOT_FOUND				= "CLASS_NOT_FOUND";
	public static final String 				ERROR_CODE_CLASS_IS_ABSTRACT			= "CLASS_IS_ABSTRACT";
	public static final String 				ERROR_CODE_CLASS_PROPERTY_NOT_FOUND		= "CLASS_PROPERTY_NOT_FOUND";
	
	public static final String 				ERROR_CODE_VALUE_TOO_LONG				= "VALUE_TOO_LONG";
	public static final String 				ERROR_CODE_VALUE_NOT_A_NUMBER			= "VALUE_NOT_A_NUMBER";
	public static final String 				ERROR_CODE_LINK_VALUE_TOO_LARGE			= "LINK_VALUE_TOO_LARGE";
	public static final String 				ERROR_CODE_OBJECT_ID_NOT_FOUND			= "OBJECT_ID_NOT_FOUND";
	public static final String 				ERROR_CODE_UNIQUE_INDEX_VIOLATION		= "UNIQUE_INDEX_VIOLATION";
	public static final String 				ERROR_CODE_VALUE_IS_MANDATORY			= "VALUE_IS_MANDATORY";
	public static final String 				ERROR_CODE_INVALID_PROPERTY_VALUE		= "INVALID_PROPERTY_VALUE";
	public static final String 				ERROR_CODE_INVALID_FILTER_PROPERTY		= "INVALID_FILTER_PROPERTY";
	
	public static final String 				ERROR_CODE_DATATYPE_OPERATOR_MISMATCH	= "OPERATOR_DATATYPE_MISMATCH";

	public static final String 				ERROR_CODE_GET_REQUEST_NOT_SET			= "GET_REQUEST_NOT_SET";

	public static final String 				ERROR_CODE_NO_OBJECTS_TO_ADD			= "NO_OBJECTS_TO_ADD";

	public static final String 				ERROR_CODE_UPDATE_OBJECT_NOT_SET		= "UPDATE_OBJECT_NOT_SET";
	public static final String 				ERROR_CODE_UPDATE_OBJECT_EMPTY			= "UPDATE_OBJECT_EMPTY";

	public static final String 				ERROR_CODE_UNABLE_TO_REMOVE_OBJECT		= "UNABLE_TO_LOCK_OBJECT";
	public static final String 				ERROR_CODE_UNABLE_TO_LOCK_OBJECTS		= "UNABLE_TO_LOCK_OBJECTS";

	public static final String 				ERROR_CODE_DATABASE_IS_READ_ONLY		= "DATABASE_IS_READ_ONLY";
	public static final String 				ERROR_CODE_DATABASE_STOPPED				= "DATABASE_STOPPED";

	private String							className	= "";
	private StringBuilder 					log 		= new StringBuilder();
	private List<ReqError>					errors		= new ArrayList<ReqError>();
	private List<ReqDataObject> 			objects		= new ArrayList<ReqDataObject>();

	private ReqObject						original	= null;

	public ReqObject(String className) {
		this.className = className;
	}
	
	public void addLogLine(String line) {
		addLogLine(line,false);
	}

	private void addLogLine(String line, boolean err) {
		log.append(line);
		log.append("\n");
		if (DbConfig.getInstance().isDebugRequestLogging()) {
			if (err) {
				Messenger.getInstance().error(this,line);
			} else {
				Messenger.getInstance().debug(this,line);
			}
		}
	}
	
	public ReqError addError(String code, String message) {
		addLogLine(message,true);
		ReqError err = new ReqError(code,message);
		errors.add(err);
		return err;
	}

	public ReqError addObjectError(ReqDataObject object, String code, String message) {
		addLogLine(message,true);
		ReqError err = object.addError(code,message);
		errors.add(err);
		return err;
	}
	
	public boolean hasError() {
		return errors.size()>0;
	}

	public void prepare() {
		
	}
	
	protected abstract String getXmlRootElementName();
	
	public static ReqObject copy(ReqObject original) {
		ReqObject copy = null;
		if (original instanceof ReqGet) {
			ReqGet c = new ReqGet(original.getClassName());
			c.copy((ReqGet) original, c);
			copy = c;
		} else if (original instanceof ReqAdd) {
			ReqAdd c = new ReqAdd(original.getClassName());
			c.copy((ReqAdd) original, c);
			copy = c;
		} else if (original instanceof ReqUpdate) {
			ReqUpdate c = new ReqUpdate(original.getClassName());
			c.copy((ReqUpdate) original, c);
			copy = c;
		} else if (original instanceof ReqRemove) {
			ReqRemove c = new ReqRemove(original.getClassName());
			c.copy((ReqRemove) original, c);
			copy = c;
		}
		return copy;
	}
	
	protected void copy(ReqObject original, ReqObject copy) {
		copy.setOriginal(original);
		copy.setClassName(new String(original.getClassName()));
		copy.setLog(new StringBuilder(original.getLog()));
		copy.getErrors().clear();
		for (ReqError error: original.getErrors()) {
			copy.getErrors().add(error.copy());
		}
		copy.getObjects().clear();
		for (ReqDataObject object: original.getObjects()) {
			copy.getObjects().add(object.copy());
		}
	}
	
	public XMLFile toXML() {
		XMLFile file = new XMLFile();
		file.setRootElement(new XMLElem(getXmlRootElementName(),null,null));
		new XMLElem("className",new StringBuilder(className),file.getRootElement());
		if (log.length()>0) {
			XMLElem logElem = new XMLElem("log",new StringBuilder(log),file.getRootElement());
			logElem.setCData(true);
		}
		if (errors.size()>0) {
			XMLElem errElem = new XMLElem("errors",null,file.getRootElement());
			for (ReqError error: errors) {
				error.toXML().getRootElement().setParent(errElem);
			}
		}
		if (objects.size()>0) {
			MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(className);
			XMLElem objElem = new XMLElem("objects",null,file.getRootElement());
			for (ReqDataObject object: objects) {
				object.toXML(cls).getRootElement().setParent(objElem);
			}
		}
		return file;
	}

	public static ReqObject abstractfromXML(XMLElem rootElem) {
		ReqObject r = null;
		if (rootElem.getName().equals(GET)) {
			ReqGet req = new ReqGet("");
			req.fromXML(rootElem);
			r = req;
		} else if (rootElem.getName().equals(ADD)) {
			ReqAdd req = new ReqAdd("");
			req.fromXML(rootElem);
			r = req;
		} else if (rootElem.getName().equals(UPDATE)) {
			ReqUpdate req = new ReqUpdate("");
			req.fromXML(rootElem);
			r = req;
		} else if (rootElem.getName().equals(REMOVE)) {
			ReqRemove req = new ReqRemove("");
			req.fromXML(rootElem);
			r = req;
		}
		return r;
	}
	
	public void fromXML(XMLElem rootElem) {
		if (rootElem.getName().equals(getXmlRootElementName())) {
			for (XMLElem cElem: rootElem.getChildren()) {
				if (cElem.getName().equals("className") && cElem.getValue()!=null) {
					className = cElem.getValue().toString();
				}
				if (cElem.getName().equals("log") && cElem.getValue()!=null) {
					log = cElem.getValue();
				}
				if (cElem.getName().equals("errors")) {
					for (XMLElem errElem: cElem.getChildren()) {
						ReqError error = new ReqError();
						error.fromXML(errElem);
						errors.add(error);
					}
				}
				if (cElem.getName().equals("objects")) {
					MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(className);
					for (XMLElem objElem: cElem.getChildren()) {
						ReqDataObject object = new ReqDataObject();
						object.fromXML(objElem,cls);
						objects.add(object);
					}
				}
			}
		}
	}

	/**
	 * Default checks using the model
	 * @param source The caller
	 * @return true if the request is valid
	 */
	public boolean check(Object source) {
		boolean ok = true;
		MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(getClassName());
		if (cls==null) {
			addError(ERROR_CODE_CLASS_NOT_FOUND,"Class not found: " + getClassName());
			ok = false;
		} else if (cls.isAbstr()) {
			addError(ERROR_CODE_CLASS_IS_ABSTRACT,"Class is abstract: " + getClassName());
			ok = false;
		}
		return ok;
	}
	
	/**
	 * @return the log
	 */
	public StringBuilder getLog() {
		return log;
	}

	/**
	 * @param log the log to set
	 */
	public void setLog(StringBuilder log) {
		this.log = log;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	
	/**
	 * @return the objects
	 */
	public List<ReqDataObject> getObjects() {
		return objects;
	}

	/**
	 * @param objects the objects to set
	 */
	public void setObjects(List<ReqDataObject> objects) {
		this.objects = objects;
	}

	public List<String> getClassNames() {
		List<String> classNames = new ArrayList<String>();
		classNames.add(className);
		return classNames;
	}
	
	public void publishRequestDoneEvent(Object source) {
		if (original!=null) {
			original.publishRequestDoneEvent(source);
		}
		publishEvent(new EvtEvent(REQUEST_DONE, source, this));
	}

	/**
	 * @return the errors
	 */
	public List<ReqError> getErrors() {
		return errors;
	}

	/**
	 * @return the original
	 */
	public ReqObject getOriginal() {
		return original;
	}

	/**
	 * @param original the original to set
	 */
	public void setOriginal(ReqObject original) {
		this.original = original;
	}
}
