package nl.zeesoft.zdsm.monitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import nl.zeesoft.zdsm.database.model.Domain;
import nl.zeesoft.zdsm.database.model.Service;
import nl.zeesoft.zdsm.process.PrHandler;
import nl.zeesoft.zdsm.process.PrWorker;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;

public class MonMonitor {
	private MonDomainLoader 			domains						= new MonDomainLoader();
	private MonServiceLoader 			services					= new MonServiceLoader();
	private MonProcessLoader 			processes					= new MonProcessLoader();
	
	private SortedMap<String,Long> 		domainMonitoredProcessMap	= new TreeMap<String,Long>();
	private SortedMap<String,Long> 		domainMonitoredServiceMap	= new TreeMap<String,Long>();
	private List<Worker>				activeWorkers				= new ArrayList<Worker>();
	
	private SortedMap<String,Service>	servicesByName				= new TreeMap<String,Service>();
	
	protected void work() {
		reinitializeLoaders();
		
		// Response workers
		List<Worker> removeWorkers = new ArrayList<Worker>();
		for (Worker worker: activeWorkers) {
			if (!worker.isWorking()) {
				removeWorkers.add(worker);
			}
		}
		activeWorkers.removeAll(removeWorkers);
		activeWorkers.addAll(getAddWorkers());
	}

	protected void stopWorking() {
		for (Worker worker: activeWorkers) {
			if (worker.isWorking()) {
				worker.stop();
			}
		}
		int tries = 0;
		for (Worker worker: activeWorkers) {
			while (worker.isWorking()) {
				tries++;
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					Messenger.getInstance().error(this,"Stopping workers was interrupted");
					break;
				}
				if (tries>1000) {
					break;
				}
			}
			if (tries>1000) {
				Messenger.getInstance().error(this,"Failed to stop workers");
				break;
			}
		}
	}
	
	private List<Worker> getAddWorkers() {
		List<Worker> workers = new ArrayList<Worker>();
		Date now = new Date();
		for (Domain domain: domains.getDomainsAsList()) {
			long dateTimeProcess = domainMonitoredProcessMap.get(domain.getName());
			if ((now.getTime() - (domain.getCheckProcessSeconds() * 1000L)) > dateTimeProcess) {
				for (nl.zeesoft.zdsm.database.model.Process process: processes.getProcessesAsList()) {
					if (process.isActive() && !process.isRemoveMe() && domain.isActive() && !domain.isRemoveMe() && process.getClassName().length()>0 && 
						(process.getDomainNameContains().length()==0 || domain.getName().contains(process.getDomainNameContains()))
						) {
						PrHandler handler = (PrHandler) Generic.testInstanceForName(process.getClassName());
						if (handler==null) {
							Messenger.getInstance().error(this,"Unable to instantiate class: " + process.getClassName());
						} else {
							SortedMap<String,Service> sByName = new TreeMap<String,Service>();
							for (Service ori: servicesByName.values()) {
								Service copy = new Service();
								copy.fromDataObject(ori.toDataObject());
								copy.setExpectedHeader(new StringBuilder());
								copy.setExpectedBody(new StringBuilder());
								sByName.put(ori.getName(),copy);
							}
							handler.setDomain(domain);
							handler.setProcess(process);
							handler.setServicesByName(sByName);
							PrWorker worker = new PrWorker(handler);
							workers.add(worker);
							worker.start();
						}
					}
				}
				domainMonitoredProcessMap.put(domain.getName(),now.getTime());
			}
			long dateTimeService = domainMonitoredServiceMap.get(domain.getName());
			if ((now.getTime() - (domain.getCheckServiceSeconds() * 1000L)) > dateTimeService) {
				for (Service service: services.getServicesAsList()) {
					if (service.isActive() && !service.isRemoveMe() && domain.isActive() && !domain.isRemoveMe() &&
						(service.getDomainNameContains().length()==0 || domain.getName().contains(service.getDomainNameContains()))
						) {
						MonResponse response = new MonResponse(domain,service);
						MonResponseWorker worker = new MonResponseWorker(response);
						workers.add(worker);
						worker.start();
					}
				}
				domainMonitoredServiceMap.put(domain.getName(),now.getTime());
			}
		}
		return workers;
	}
	
	private void reinitializeLoaders() {
		domains.reinitialize();
		services.reinitialize();
		processes.reinitialize();
		servicesByName.clear();
		for (Service ori: services.getServicesAsList()) {
			Service copy = new Service();
			copy.fromDataObject(ori.toDataObject());
			copy.setId(0);
			servicesByName.put(ori.getName(),copy);
		}
		Set<String> domainNames = new TreeSet<String>(domainMonitoredProcessMap.keySet());
		for (String domainName: domainNames) {
			boolean found = false;
			for (Domain domain: domains.getDomainsAsList()) {
				if (domain.isActive() && !domain.isRemoveMe() && domain.getName().equals(domainName)) {
					found = true;
					break;
				}
			}
			if (!found) {
				domainMonitoredProcessMap.remove(domainName);
				domainMonitoredServiceMap.remove(domainName);
			}
		}
		for (Domain domain: domains.getDomainsAsList()) {
			if (!domainMonitoredProcessMap.containsKey(domain.getName())) {
				domainMonitoredProcessMap.put(domain.getName(),0L);
			}
			if (!domainMonitoredServiceMap.containsKey(domain.getName())) {
				domainMonitoredServiceMap.put(domain.getName(),0L);
			}
		}
	}
}
