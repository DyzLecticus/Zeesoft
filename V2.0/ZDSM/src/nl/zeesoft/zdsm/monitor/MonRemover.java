package nl.zeesoft.zdsm.monitor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdsm.database.model.Domain;
import nl.zeesoft.zdsm.database.model.Service;
import nl.zeesoft.zdsm.database.model.ZDSMModel;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.model.helpers.HlpControllerObject;
import nl.zeesoft.zodb.database.request.ReqGetFilter;
import nl.zeesoft.zodb.database.request.ReqRemove;
import nl.zeesoft.zodb.event.EvtEvent;

public class MonRemover extends HlpControllerObject {
	private MonDomainLoader 		domains				= new MonDomainLoader();
	private MonServiceLoader 		services			= new MonServiceLoader();
	private MonProcessLoader 		processes			= new MonProcessLoader();

	private ReqRemove				removeRequest		= null;
	private List<ReqRemove>			removeRequestList	= new ArrayList<ReqRemove>();
	
	public MonRemover() {
		setTimeOutSeconds(60);
	}
	
	@Override
	protected void initialize() {
		// Not implemented
	}

	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getValue()!=null && e.getValue()==removeRequest) {
			resetTimeOut();
			if (removeRequest.hasError()) {
				Messenger.getInstance().error(this,"Remove request error: " + removeRequest.getErrors().get(0).getMessage());
			} else if (removeRequestList.size()>0) {
				if (!this.isDone()) {
					removeRequest = removeRequestList.remove(0);
					DbRequestQueue.getInstance().addRequest(removeRequest,this);
				}
			} else {
				setDone(true);
			}
		}
	}

	protected void stopWorking() {
		setDone(true);
	}
	
	protected void work() {
		domains.reinitialize();
		services.reinitialize();
		processes.reinitialize();
		
		removeRequestList.clear();
		ReqRemove request = null;
		
		for (Domain domain: domains.getDomainsAsList()) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE,(domain.getKeepResponseDays() * -1));
			Date d = cal.getTime();
			request = new ReqRemove(ZDSMModel.RESPONSE_CLASS_FULL_NAME);
			request.addSubscriber(this);
			request.getGet().addFilter("domain",ReqGetFilter.CONTAINS,"" + domain.getId());
			request.getGet().addFilter("dateTime",ReqGetFilter.LESS,"" + d.getTime());
			removeRequestList.add(request);

			cal = Calendar.getInstance();
			cal.add(Calendar.DATE,(domain.getKeepProcessLogDays() * -1));
			d = cal.getTime();
			request = new ReqRemove(ZDSMModel.PROCESS_LOG_CLASS_FULL_NAME);
			request.addSubscriber(this);
			request.getGet().addFilter("domain",ReqGetFilter.CONTAINS,"" + domain.getId());
			request.getGet().addFilter("dateTime",ReqGetFilter.LESS,"" + d.getTime());
			removeRequestList.add(request);
			
			if (domain.isRemoveMe()) {
				request = new ReqRemove(ZDSMModel.RESPONSE_CLASS_FULL_NAME);
				request.addSubscriber(this);
				request.getGet().addFilter("domain",ReqGetFilter.CONTAINS,"" + domain.getId());
				removeRequestList.add(request);

				request = new ReqRemove(ZDSMModel.PROCESS_LOG_CLASS_FULL_NAME);
				request.addSubscriber(this);
				request.getGet().addFilter("domain",ReqGetFilter.CONTAINS,"" + domain.getId());
				removeRequestList.add(request);

				request = new ReqRemove(ZDSMModel.DOMAIN_CLASS_FULL_NAME,domain.getId());
				request.addSubscriber(this);
				removeRequestList.add(request);
			}
		}
		for (Service service: services.getServicesAsList()) {
			if (service.isRemoveMe()) {
				request = new ReqRemove(ZDSMModel.RESPONSE_CLASS_FULL_NAME);
				request.addSubscriber(this);
				request.getGet().addFilter("service",ReqGetFilter.CONTAINS,"" + service.getId());
				removeRequestList.add(request);

				request = new ReqRemove(ZDSMModel.SERVICE_CLASS_FULL_NAME,service.getId());
				request.addSubscriber(this);
				removeRequestList.add(request);
			}
		}
		for (nl.zeesoft.zdsm.database.model.Process process: processes.getProcessesAsList()) {
			if (process.isRemoveMe()) {
				request = new ReqRemove(ZDSMModel.PROCESS_LOG_CLASS_FULL_NAME);
				request.addSubscriber(this);
				request.getGet().addFilter("process",ReqGetFilter.CONTAINS,"" + process.getId());
				removeRequestList.add(request);

				request = new ReqRemove(ZDSMModel.PROCESS_CLASS_FULL_NAME,process.getId());
				request.addSubscriber(this);
				removeRequestList.add(request);
			}
		}
		
		if (removeRequestList.size()>0) {
			setDone(false);
			removeRequest = removeRequestList.remove(0);
			DbRequestQueue.getInstance().addRequest(removeRequest,this);
			waitTillDone();
		}
	}
}
