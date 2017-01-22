package nl.zeesoft.zdsm.database.model;

import nl.zeesoft.zodb.database.DbDataObject;

public class ProcessLog extends Log  {
	private StringBuilder	log				= new StringBuilder();
	private long 			processId		= 0;
	private Process 		process			= null;

	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("log")) {
			setLog(obj.getPropertyValue("log"));
		}
		if (obj.hasPropertyValue("process") && obj.getLinkValue("process").size()>0) {
			setProcessId(obj.getLinkValue("process").get(0));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("log",new StringBuilder(getLog()));
		r.setLinkValue("process",getProcessId());
		return r;
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
	 * @return the processId
	 */
	public long getProcessId() {
		return processId;
	}
	
	/**
	 * @param processId the processId to set
	 */
	public void setProcessId(long processId) {
		this.processId = processId;
	}
	
	/**
	 * @return the process
	 */
	public Process getProcess() {
		return process;
	}
	
	/**
	 * @param process the process to set
	 */
	public void setProcess(Process process) {
		this.process = process;
		if (process!=null) {
			processId = process.getId();
		} else {
			processId = 0;
		}
	}
}
