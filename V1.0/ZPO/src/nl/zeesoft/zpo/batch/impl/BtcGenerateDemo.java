package nl.zeesoft.zpo.batch.impl;

import java.math.BigDecimal;
import java.util.Calendar;

import nl.zeesoft.zodb.batch.BtcProgramObject;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.impl.BtcLog;
import nl.zeesoft.zodb.model.impl.DbUser;
import nl.zeesoft.zpo.model.impl.UsrTask;
import nl.zeesoft.zpo.model.impl.UsrTaskLog;
import nl.zeesoft.zpo.model.impl.UsrTaskRepeat;

public class BtcGenerateDemo extends BtcProgramObject {

	@Override
	public String execute(BtcLog log) {
		String err = "";
		
		QryTransaction t = null;
		
		// Create demo user
		t = new QryTransaction(log.getExecutingAsUser());
		DbUser user = new DbUser();
		user.getName().setValue("demo");
		user.getPassword().setValue(DbConfig.getInstance().encodePassword(new StringBuffer("1demodemo!")));
		t.addQuery(new QryAdd(user));
		DbIndex.getInstance().executeTransaction(t, this);
		
		if (!getTransactionErrors(t, log)) {
			UsrTaskRepeat monday = this.getTaskRepeat(UsrTaskRepeat.MONDAY, log);
			UsrTaskRepeat tuesday = this.getTaskRepeat(UsrTaskRepeat.TUESDAY, log);
			UsrTaskRepeat wednesday = this.getTaskRepeat(UsrTaskRepeat.WEDNESDAY, log);
			UsrTaskRepeat thursday = this.getTaskRepeat(UsrTaskRepeat.THURSDAY, log);
			UsrTaskRepeat friday = this.getTaskRepeat(UsrTaskRepeat.FRIDAY, log);
			
			// Create tasks, task logs and task log report
			t = new QryTransaction(user);
			UsrTask task = new UsrTask();
			task.getName().setValue("Publish task report for 2014 to stakeholders");
			task.getUser().setValue(user);
			task.getDescription().setValue(
				"Publish task report for 2014 to stakeholders.\n"
				);
			t.addQuery(new QryAdd(task));
			UsrTask mondayTask = new UsrTask();
			mondayTask.getName().setValue("Gather leads");
			mondayTask.getUser().setValue(user);
			mondayTask.getRepeat().setValue(monday);
			mondayTask.getDescription().setValue(
				"Contact potential customers.\n"
				);
			t.addQuery(new QryAdd(mondayTask));
			UsrTask tuesdayTask = new UsrTask();
			tuesdayTask.getName().setValue("Follow up on leads");
			tuesdayTask.getUser().setValue(user);
			tuesdayTask.getRepeat().setValue(tuesday);
			tuesdayTask.getDescription().setValue(
				"Create contract proposals and send them to potential customers.\n"
				);
			t.addQuery(new QryAdd(tuesdayTask));
			UsrTask wednesdayTask = new UsrTask();
			wednesdayTask.getName().setValue("Follow up on proposals");
			wednesdayTask.getUser().setValue(user);
			wednesdayTask.getRepeat().setValue(wednesday);
			wednesdayTask.getDescription().setValue(
				"Contact potential customers to confirm they received the contract proposals.\n"
				);
			t.addQuery(new QryAdd(wednesdayTask));
			UsrTask thursdayTask = new UsrTask();
			thursdayTask.getName().setValue("Close deals");
			thursdayTask.getUser().setValue(user);
			thursdayTask.getRepeat().setValue(thursday);
			thursdayTask.getDescription().setValue(
				"Visit new customers to close the deals.\n"
				);
			t.addQuery(new QryAdd(thursdayTask));
			UsrTask fridayTask = new UsrTask();
			fridayTask.getName().setValue("Administration");
			fridayTask.getUser().setValue(user);
			fridayTask.getRepeat().setValue(friday);
			fridayTask.getDescription().setValue(
				"Weekly administration.\n"
				);
			t.addQuery(new QryAdd(fridayTask));
			DbIndex.getInstance().executeTransaction(t, this);
			
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2014);
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			cal.set(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 20);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			int customer = 1;
			t = new QryTransaction(user);
			for (int i = 0; i < 365; i++) {
				if (cal.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY) {
					customer++;
					UsrTaskLog taskLog = new UsrTaskLog();
					taskLog.getName().setValue("Called potential customer " + customer);
					taskLog.getTask().setValue(mondayTask);
					taskLog.getUser().setValue(user);
					taskLog.getDateTime().setValue(cal.getTime());
					taskLog.getDuration().setValue(new BigDecimal("8.00"));
					t.addQuery(new QryAdd(taskLog));
				}
				if (cal.get(Calendar.DAY_OF_WEEK)==Calendar.TUESDAY) {
					UsrTaskLog taskLog = new UsrTaskLog();
					taskLog.getName().setValue("Created contract proposal for potential customer " + customer);
					taskLog.getTask().setValue(tuesdayTask);
					taskLog.getUser().setValue(user);
					taskLog.getDateTime().setValue(cal.getTime());
					taskLog.getDuration().setValue(new BigDecimal("8.00"));
					t.addQuery(new QryAdd(taskLog));
				}
				if (cal.get(Calendar.DAY_OF_WEEK)==Calendar.WEDNESDAY) {
					UsrTaskLog taskLog = new UsrTaskLog();
					taskLog.getName().setValue("Followed up on contract proposal for potential customer " + customer);
					taskLog.getTask().setValue(wednesdayTask);
					taskLog.getUser().setValue(user);
					taskLog.getDateTime().setValue(cal.getTime());
					taskLog.getDuration().setValue(new BigDecimal("8.00"));
					t.addQuery(new QryAdd(taskLog));
				}
				if (cal.get(Calendar.DAY_OF_WEEK)==Calendar.THURSDAY) {
					UsrTaskLog taskLog = new UsrTaskLog();
					taskLog.getName().setValue("Visited customer " + customer + " to sign contract");
					taskLog.getTask().setValue(thursdayTask);
					taskLog.getUser().setValue(user);
					taskLog.getDateTime().setValue(cal.getTime());
					taskLog.getDuration().setValue(new BigDecimal("8.00"));
					t.addQuery(new QryAdd(taskLog));
				}
				if (cal.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY) {
					UsrTaskLog taskLog = new UsrTaskLog();
					taskLog.getTask().setValue(fridayTask);
					taskLog.getUser().setValue(user);
					taskLog.getDateTime().setValue(cal.getTime());
					taskLog.getDuration().setValue(new BigDecimal("8.00"));
					t.addQuery(new QryAdd(taskLog));
				}
				cal.add(Calendar.DATE, 1);
			}
			DbIndex.getInstance().executeTransaction(t, this);
		} else {
			err = "Failed to create demo user";
		}
		
		return err;
	}
	
	private UsrTaskRepeat getTaskRepeat(String name, BtcLog log) {
		UsrTaskRepeat repeat = null;
		QryFetch fetch = new QryFetch(UsrTaskRepeat.class.getName());
		fetch.addCondition(new QryFetchCondition("name",QryFetchCondition.OPERATOR_EQUALS,new DtString(name)));
		DbIndex.getInstance().executeFetch(fetch, log.getExecutingAsUser(), this);
		if (fetch.getMainResults().getReferences().size()>0) {
			repeat = (UsrTaskRepeat) fetch.getMainResults().getReferences().get(0).getDataObject();
		}
		return repeat;
	}
}
