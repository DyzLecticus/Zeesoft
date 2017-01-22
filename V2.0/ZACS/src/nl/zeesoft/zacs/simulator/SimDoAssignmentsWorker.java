package nl.zeesoft.zacs.simulator;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zacs.database.model.Assignment;
import nl.zeesoft.zacs.database.model.Control;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;

public class SimDoAssignmentsWorker extends Worker {
	private static SimDoAssignmentsWorker		doWorker				= null;
	
	private SimAssignmentHandler				assignmentHandler		= null;
	
	private List<Assignment>					assignments				= null;
	private int									assignmentIndex			= 0;
	
	private int									sleep					= 0;
	private int									reloadSeconds			= 0;
	private Date								lastReload				= null;
	
	private SimDoAssignmentsWorker() {
		// Singleton
	}

	public static SimDoAssignmentsWorker getInstance() {
		if (doWorker==null) {
			doWorker = new SimDoAssignmentsWorker();
		}
		return doWorker;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}

	@Override
	public void start() {
		Control c = SimController.getInstance().getControl();
		sleep = c.getAssignmentPauzeMSecs();
		reloadSeconds = c.getAssignmentReloadSecs();
		setSleep(sleep);
		if (assignmentHandler==null) {
			assignmentHandler = SimController.getInstance().getAssignmentHandler();
		} else {
			assignmentHandler.initializeParameters(c);
		}
		lastReload = null;
		checkReloadAssignments();
		super.start();
	}

	@Override
	public void stop() {
		super.stop();
		waitForStop(10,false);
		if (assignmentHandler!=null && assignmentHandler.getWorkingAssignment()!=null) {
			assignmentHandler.stopWorkingAssignment();
		}
	}
	
	@Override
	public void whileWorking() {
		if (assignmentHandler.getWorkingAssignment()==null) {
			assignmentHandler.setWorkingAssignment(getNextAssignment());
		}
		if (assignmentHandler.getWorkingAssignment()!=null) {
			setSleep(0);
			assignmentHandler.workOnWorkingAssignment();
		}
		if (assignmentHandler.getWorkingAssignment()==null) {
			setSleep(sleep);
		}
	}
	
	private Assignment getNextAssignment() {
		Assignment r = null;
		checkReloadAssignments();
		for (Assignment assignment: assignments) {
			if (assignment.getLog().length()==0) {
				r = assignment;
				break;
			}
		}
		if (r==null && assignments.size()>0) {
			if (assignmentIndex>=assignments.size()) {
				assignmentIndex = 0;
			}
			r = assignments.get(assignmentIndex);
			assignmentIndex++;
		}
		return r;
	}

	private void checkReloadAssignments() {
		Date now = null;
		if (reloadSeconds>0 && lastReload!=null) {
			now = new Date();
		}
		if (now==null || (now.getTime() - lastReload.getTime()) > (reloadSeconds * 1000)) {
			if (now!=null && reloadSeconds>0) {
				sleep(100);
			}
			
			SimController.getInstance().getAssignments().reinitialize();
			assignments = SimController.getInstance().getAssignments().getAssignmentsAsList();
			
			if (assignments.size()>0) {
				Assignment continueAssignment = null;
				for (Assignment as: assignments) {
					if (continueAssignment==null && as.getWorkingModule()!=null) {
						continueAssignment = as;
						break;
					}
				}
				if (continueAssignment!=null) {
					assignmentHandler.setWorkingAssignment(continueAssignment);
				}
			} else if (lastReload==null) {
				Messenger.getInstance().debug(this,"No assignments to do");
			}
			
			if (now==null) {
				now = new Date();
			}
			lastReload = now;
		}
	}
}
