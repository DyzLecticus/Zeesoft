package nl.zeesoft.zdsm.process;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdsm.database.model.Domain;
import nl.zeesoft.zdsm.database.model.ProcessLog;
import nl.zeesoft.zdsm.database.model.Response;
import nl.zeesoft.zdsm.database.model.Service;
import nl.zeesoft.zdsm.database.model.ZDSMModel;
import nl.zeesoft.zdsm.monitor.MonResponse;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.model.helpers.HlpControllerObject;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.file.xml.XMLFile;

public abstract class PrHandler extends HlpControllerObject {
	private ReqAdd									addProcessLogRequest	= new ReqAdd(ZDSMModel.PROCESS_LOG_CLASS_FULL_NAME);

	private Domain									domain					= null;
	private SortedMap<String,Service>				servicesByName			= new TreeMap<String,Service>();
	private nl.zeesoft.zdsm.database.model.Process	process					= null;
	private ProcessLog								log						= new ProcessLog();
	private List<XMLFile> 							bodyXMLFiles			= new ArrayList<XMLFile>(); 
	
	public void work() {
		Date now = new Date();
		log.setDomain(domain);
		log.setProcess(process);		
		addLogLine("Process started");
		handleProcess();
		addLogLine("Process done");
		log.setDateTime((new Date()).getTime());
		log.setDuration(log.getDateTime() - now.getTime());
		addProcessLogRequest.getObjects().add(new ReqDataObject(log.toDataObject()));
		DbRequestQueue.getInstance().addRequest(addProcessLogRequest,this);
		for (XMLFile file: bodyXMLFiles) {
			file.cleanUp();
		}
	}
	
	// Override to implement
	protected abstract void handleProcess();
	
	protected final Response getNewServiceResponse(String name,String expectedCode) {
		Response response = null;
		Service service = getServiceByName(name);
		if (service!=null) {
			response = getNewServiceResponse(service,expectedCode);
		}
		return response;
	}
	
	protected final Response getNewServiceResponse(Service service,String expectedCode) {
		MonResponse serviceResponse = new MonResponse(getDomain(),service);
		if (expectedCode!=null) {
			service.setExpectedCode(expectedCode);
		} 
		serviceResponse.work();
		if (!serviceResponse.getResponse().isSuccess()) {
			addLogLine("ERROR: Service: " + service.getName() + " returned: " + serviceResponse.getResponse().getCode() + " (expected: " + expectedCode + ")");
		}
		return serviceResponse.getResponse();
	}
	
	protected final void addLogLine(String line) {
		log.getLog().append(Generic.getDateTimeString(new Date()));
		log.getLog().append(": ");
		log.getLog().append(line);
		log.getLog().append("\n");
	}
	
	protected final XMLFile getResponseBodyAsXMLFile(StringBuilder body) {
		StringBuilder xml = new StringBuilder(body);
		xml = Generic.stringBuilderReplace(xml,"&lt;","<");
		xml = Generic.stringBuilderReplace(xml,"&gt;",">");
		XMLFile file = new XMLFile();
		String err = file.parseXML(xml);
		if (err.length()>0) {
			addLogLine("ERROR: " + err);
		}
		bodyXMLFiles.add(file);
		return file;
	}
	
	@Override
	protected final void initialize() {
		// Not implemented
	}

	@Override
	public void handleEvent(EvtEvent e) {
		// Override to implement
	}

	private final Domain getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public final void setDomain(Domain domain) {
		this.domain = domain;
	}

	/**
	 * @return the process
	 */
	public final nl.zeesoft.zdsm.database.model.Process getProcess() {
		return process;
	}

	/**
	 * @param process the process to set
	 */
	public final void setProcess(nl.zeesoft.zdsm.database.model.Process process) {
		this.process = process;
	}

	/**
	 * @return the log
	 */
	public final ProcessLog getLog() {
		return log;
	}

	/**
	 * @param servicesByName the servicesByName to set
	 */
	public final void setServicesByName(SortedMap<String, Service> servicesByName) {
		this.servicesByName = servicesByName;
	}

	protected final Service getServiceByName(String name) {
		Service r = null;
		r = servicesByName.get(name);
		if (r==null) {
			addLogLine("ERROR: Service not found with name: " + name);
		} else {
			Service copy = new Service();
			copy.fromDataObject(r.toDataObject());
			r = copy;
		}
		return r;
	}
	
	protected SortedMap<String,String> parseHeaders(StringBuilder header) {
		SortedMap<String,String> headers = new TreeMap<String,String>();
		List<StringBuilder> lines = Generic.stringBuilderSplit(header,"\n");
		for (StringBuilder line: lines) {
			String[] keyVal = line.toString().split(": ");
			String val = headers.get(keyVal[0]);
			if (keyVal.length>1) {
				if (val!=null) {
					val += "," + keyVal[1];
				} else {
					val = keyVal[1];
				}
				headers.put(keyVal[0],val);
			} else {
				headers.put(keyVal[0],"");
			}
		}
		return headers;
	}

	protected SortedMap<String,String> parseCookieValues(StringBuilder header) {
		return parseCookieValues(parseHeaders(header));
	}

	private SortedMap<String,String> parseCookieValues(SortedMap<String,String> headers) {
		SortedMap<String,String> keyValues = new TreeMap<String,String>();
		String vals = headers.get("Set-Cookie");
		if (vals!=null) {
			for (String val: vals.split(",")) {
				String v = val.split(";")[0];
				String[] keyVal = v.toString().split("=");
				if (keyVal.length>1) {
					keyValues.put(keyVal[0],keyVal[1]);
				} else {
					keyValues.put(keyVal[0],"");
				}
			}
		}
		return keyValues;
	}
}
