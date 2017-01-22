package nl.zeesoft.zodb.batch;

import java.util.Date;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.database.query.QryRemove;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.model.MdlObjectRefList;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.impl.BtcLog;
import nl.zeesoft.zodb.model.impl.BtcProgram;
import nl.zeesoft.zodb.model.impl.DbUser;

public final class BtcProgramWorker extends Worker {
	private BtcProgram				program				= null;
	private BtcLog 					log 				= null;
	
	public BtcProgramWorker(BtcProgram p) {
		program = p;
		setSleep(1);
	}
	
	@Override
	public void start() {
		Messenger.getInstance().debug(this, "Starting batch program: " + program.getName().getValue());
		DbUser executeUser = program.getExecuteAsUser(this);

		// Update program
		program.getExecuting().setValue(true);
		
		QryFetch fetch = new QryFetch(BtcLog.class.getName());
		fetch.addCondition(new QryFetchCondition("program",QryFetchCondition.OPERATOR_CONTAINS,new DtIdRef(program.getId().getValue())));
		DbIndex.getInstance().executeFetch(fetch, DbConfig.getInstance().getModel().getAdminUser(this), this);
		int size = fetch.getMainResults().getReferences().size();
		if (size>=program.getMaxLogs().getValue()) {
			Messenger.getInstance().debug(this, "Removing program logs: " + (size - (program.getMaxLogs().getValue() - 1)));
			MdlObjectRefList results = fetch.getMainResults();
			results.sortObjects("started",false);
			QryTransaction t = new QryTransaction(DbConfig.getInstance().getModel().getAdminUser(this));
			for (int i = program.getMaxLogs().getValue() - 1; i < size; i++) {
				t.addQuery(new QryRemove(results.getReferences().get(i).getDataObject()));
			}
			DbIndex.getInstance().executeTransaction(t, this);
		}
		
		// Add log
		log = new BtcLog();
		log.getName().setValue(program.getName().getValue());
		log.getProgram().setValue(program.getId().getValue());
		log.getStarted().setValue(new Date());
		log.setExecutingAsUser(executeUser);
		log.getExecutedAsUser().setValue(executeUser.getId().getValue());

		QryTransaction t = new QryTransaction(DbConfig.getInstance().getModel().getAdminUser(this));
		t.addQuery(new QryUpdate(program));
		t.addQuery(new QryAdd(log));
		DbIndex.getInstance().executeTransaction(t, this);

		super.start();
		
		Messenger.getInstance().debug(this, "Started batch program: " + program.getName().getValue());
    }

	@Override
    public void stop() {
		Messenger.getInstance().debug(this, "Stopping batch program ...");

		super.stop();

		// Update program
		program.getExecuting().setValue(false);

		// Update log
		log.getStopped().setValue(new Date());

		QryTransaction t = new QryTransaction(DbConfig.getInstance().getModel().getAdminUser(this));
		t.addQuery(new QryUpdate(log));
		t.addQuery(new QryUpdate(program));
		DbIndex.getInstance().executeTransaction(t, this);

		Messenger.getInstance().debug(this, "Stopped batch program");
    }
	
	@Override
	public void whileWorking() {
		String err = program.execute(log);
		if (err.length()>0) {
			log.addError(err);
		} else {
			long time = new Date().getTime() - log.getStarted().getValue().getTime();
			log.addLogLine("Executed batch program in " + time + " ms");
		}

		program.reschedule((err.length()>0));
		
		BtcBatch.getInstance().stopProgram(program.getId().getValue());
	}
}
