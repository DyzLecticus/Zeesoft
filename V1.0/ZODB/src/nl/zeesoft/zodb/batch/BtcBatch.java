package nl.zeesoft.zodb.batch;

import java.util.Date;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;
import nl.zeesoft.zodb.model.impl.BtcProgram;

public final class BtcBatch {
	private static BtcBatch						batch			= null;
	
	private SortedMap<Long,BtcProgramWorker> 	programWorkers 	= new TreeMap<Long,BtcProgramWorker>();
	
	private QryFetch							fetch 			= null;

	private BtcBatch() {
		// Singleton
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}

	public static BtcBatch getInstance() {
		if (batch==null) {
			batch = new BtcBatch();
		}
		return batch;
	}

	public void updateAndStartPrograms(Object source) {
		if (fetch==null) {
			fetch = new QryFetch(BtcProgram.class.getName());

			// Date is not included in conditions to ensure cache usage
			QryFetchCondition startNotNull = new QryFetchCondition("start", true, QryFetchCondition.OPERATOR_EQUALS, new DtDateTime());
			QryFetchCondition notRunning = new QryFetchCondition("executing", QryFetchCondition.OPERATOR_EQUALS, new DtBoolean(false));
			QryFetchCondition active = new QryFetchCondition("active", QryFetchCondition.OPERATOR_EQUALS, new DtBoolean(true));
			
			fetch.addCondition(startNotNull);
			fetch.addCondition(notRunning);
			fetch.addCondition(active);
		}

		Date now = new Date();
		DbIndex.getInstance().executeFetch(fetch, DbConfig.getInstance().getModel().getAdminUser(source), source);
		for (MdlObjectRef ref: fetch.getMainResults().getReferences()) {
			BtcProgram program = (BtcProgram) ref.getDataObject();
			if (program.getStart().getValue().getTime()<=now.getTime()) {
				BtcProgramWorker worker = programWorkers.get(program.getId().getValue());
				if (worker==null) {
					worker = new BtcProgramWorker(program);
					programWorkers.put(program.getId().getValue(), worker);
					worker.start();
				}
			}
		}
	}
	
    public void stopProgram(long programId) {
    	BtcProgramWorker p = programWorkers.remove(programId);
    	if (p!=null) {
    		p.stop();
    	}
    }

    protected void stopPrograms() {
    	SortedMap<Long,BtcProgramWorker> progWorkers = new TreeMap<Long,BtcProgramWorker>(programWorkers);
    	for (Entry<Long,BtcProgramWorker> e: progWorkers.entrySet()) {
    		stopProgram((Long) e.getKey());
    	}
    }

}
