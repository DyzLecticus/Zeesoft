package nl.zeesoft.zdsm.database.model;

import nl.zeesoft.zdsm.process.impl.SelfAuthorize;
import nl.zeesoft.zdsm.process.impl.SelfAuthorized;
import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.gui.GuiController;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqObject;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventExceptionSubscriber;

public class ZDSMDataGenerator extends Locker implements EvtEventExceptionSubscriber {
	private ReqAdd 				addDomainRequest				= new ReqAdd(ZDSMModel.DOMAIN_CLASS_FULL_NAME);
	private ReqAdd 				addServiceRequest				= new ReqAdd(ZDSMModel.SERVICE_CLASS_FULL_NAME);
	private ReqAdd 				addProcessRequest				= new ReqAdd(ZDSMModel.PROCESS_CLASS_FULL_NAME);

	private boolean 			done	 						= false;
	
	private boolean				installDemoData					= true;

	public void confirmInstallDemoData() {
		installDemoData = GuiController.getInstance().showConfirmMsg("Do you want to install the demo data?","Install default demo data?", installDemoData);
	}
	
	@Override
	public void handleEventException(EvtEvent evt, Exception ex) {
		setDone(true);
	}
	
	@Override
	public final void handleEvent(EvtEvent evt) {
		if (evt.getType().equals(DbController.DB_INITIALIZED_MODEL) && evt.getValue().toString().equals("true")) {
			generateInitialData();
		} else if (evt.getValue()==addDomainRequest) {
			if (!addDomainRequest.hasError()) {
				addServiceRequest.addSubscriber(this);
				addServiceRequest.getObjects().add(new ReqDataObject(createServiceObject(Service.SELF_INDEX,Domain.SELF,"GET","index.html",
						"","","200","",""
						).toDataObject()));
				addServiceRequest.getObjects().add(new ReqDataObject(createServiceObject(Service.SELF_AUTHORIZER,Domain.SELF,"GET","authorizer.html",
						"","","200","",""
						).toDataObject()));
				addServiceRequest.getObjects().add(new ReqDataObject(createServiceObject(Service.SELF_SESSION_AUTHORIZED,Domain.SELF,"GET","sessionAuthorized.xml",
						"","","200","",""
						).toDataObject()));
				addServiceRequest.getObjects().add(new ReqDataObject(createServiceObject(Service.SELF_AUTHORIZATION_MANAGER,Domain.SELF,"POST","authorizationManager.xml",
						"Content-Type: text/xml", 
						"<authorize><password>admin</password></authorize>\n",
						"200",
						"",
						""
						).toDataObject()));
				DbRequestQueue.getInstance().addRequest(addServiceRequest,this);
			} else {
				Messenger.getInstance().error(this,"Error executing add request: " + addDomainRequest.getErrors().get(0).getMessage());
				setDone(true);
			}
		} else if (evt.getValue()==addServiceRequest) {
			if (!addServiceRequest.hasError()) {
				addProcessRequest.addSubscriber(this);
				addProcessRequest.getObjects().add(new ReqDataObject(createProcessObject(
						Process.SELF_AUTHORIZE,Domain.SELF,SelfAuthorize.class.getName(),
						"Retrieves a session ID using the " + Service.SELF_AUTHORIZER + " service and then authorizes the session using the " + Service.SELF_AUTHORIZATION_MANAGER + " service."
						).toDataObject()));
				addProcessRequest.getObjects().add(new ReqDataObject(createProcessObject(
						Process.SELF_AUTHORIZED,Domain.SELF,SelfAuthorized.class.getName(),
						"Extends the " + Process.SELF_AUTHORIZE + " process. Checks if the session is authorized using the " + Service.SELF_SESSION_AUTHORIZED + " service."
						).toDataObject()));
				DbRequestQueue.getInstance().addRequest(addProcessRequest,this);
				setDone(true);
			} else {
				Messenger.getInstance().error(this,"Error executing add request: " + addServiceRequest.getErrors().get(0).getMessage());
				setDone(true);
			}
		} else if (evt.getValue()==addProcessRequest) {
			if (!addProcessRequest.hasError()) {
				setDone(true);
			} else {
				Messenger.getInstance().error(this,"Error executing add request: " + addProcessRequest.getErrors().get(0).getMessage());
				setDone(true);
			}
		} else if (evt.getValue()!=null && evt.getValue() instanceof ReqObject) {
			handleRequestEvent((ReqObject) evt.getValue(),evt);
		}
	}
	
	protected void handleRequestEvent(ReqObject request,EvtEvent evt) {
		// Override to implement
	}
	
	protected void generateInitialData() {
		if (installDemoData()) {
			Messenger.getInstance().debug(this,"Generating demo data ...");
			setDone(false);
			addDomainRequest.addSubscriber(this);
			addDomainRequest.getObjects().add(new ReqDataObject(createDomainObject(Domain.SELF,"localhost",5454,120,60,7,7,false).toDataObject()));
			DbRequestQueue.getInstance().addRequest(addDomainRequest,this);
			waitTillDone("Generating initial data was interrupted");
			Messenger.getInstance().debug(this,"Generated demo data");
		}
	}
	
	protected boolean installDemoData() {
		return installDemoData;
	}

	protected Domain createDomainObject(String name,String address,int port,int checkProcessSeconds,int checkServiceSeconds,int keepProcessLogDays,int keepResponseDays,boolean addDomainToPath) {
		Domain obj = new Domain();
		obj.setName(name);
		obj.setAddress(address);
		obj.setPort(port);
		obj.setCheckProcessSeconds(checkProcessSeconds);
		obj.setCheckServiceSeconds(checkServiceSeconds);
		obj.setKeepProcessLogDays(keepProcessLogDays);
		obj.setKeepResponseDays(keepResponseDays);
		obj.setAddDomainToPath(addDomainToPath);
		return obj;
	}
	
	protected Service createServiceObject(String name,String domainNameContains,String method,String path,String header, String body, String expectedCode,String expectedHeader, String expectedBody) {
		body = body.replaceAll("<","&lt;");
		body = body.replaceAll(">","&gt;");
		expectedBody = expectedBody.replaceAll("<","&lt;");
		expectedBody = expectedBody.replaceAll(">","&gt;");
		Service obj = new Service();
		obj.setName(name);
		obj.setMethod(method);
		obj.setPath(path);
		obj.setDomainNameContains(domainNameContains);
		obj.setHeader(new StringBuilder(header));
		obj.setBody(new StringBuilder(body));
		obj.setExpectedCode(expectedCode);
		obj.setExpectedHeader(new StringBuilder(expectedHeader));
		obj.setExpectedBody(new StringBuilder(expectedBody));
		return obj;
	}

	protected Process createProcessObject(String name,String domainNameContains,String className, String description) {
		Process obj = new Process();
		obj.setName(name);
		obj.setDomainNameContains(domainNameContains);
		obj.setClassName(className);
		obj.setDescription(new StringBuilder(description));
		return obj;
	}
	
	protected void setDone(boolean done) {
		lockMe(this);
		this.done = done;
		unlockMe(this);
	}

	private boolean isDone() {
		boolean r = false;
		lockMe(this);
		r = done;
		unlockMe(this);
		return r;
	}
	
	protected void waitTillDone(String interruptedMessage) {
		while(!isDone()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Messenger.getInstance().error(this,interruptedMessage);
			}
		}
	}
}
