package nl.zeesoft.zodb.database;

import java.util.Date;

import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlProperty;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqError;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqObject;
import nl.zeesoft.zodb.database.request.ReqObjectChange;
import nl.zeesoft.zodb.database.request.ReqObjectGetAndChange;
import nl.zeesoft.zodb.database.request.ReqRemove;
import nl.zeesoft.zodb.database.request.ReqUpdate;

public final class DbRequestWorker extends Worker {
	private ReqObject	req			= null;
	private boolean		initialized = false;
	private int			todo		= 0;
	private int			done		= 0;
	private boolean		finalized 	= false;
	
	private Date		started		= null;

	private Object		isLockedBy	= null;

	private ReqObject	nextReq		= null; // Used to chain update and remove requests to their get requests

	public DbRequestWorker(ReqObject r) {
		setSleep(0);
		req = r;
	}
	
	@Override
	public void whileWorking() {
		lockWorker(this);
		boolean init = initialized;
		unlockWorker(this);
		if (!init) {
			lockWorker(this);
			if (DbConfig.getInstance().isDebugPerformance()) {
				started = new Date();
			}
			todo = DbIndex.getInstance().getTodoForRequest(req, this);
			if (DbConfig.getInstance().isDebugPerformance()) {
				req.addLogLine("Index preprocessing took: " + ((new Date()).getTime() - started.getTime()) + " ms");
				started = new Date();
			} 
			String action = "";
			if (req instanceof ReqGet) {
				action = "Get";
			} else if (req instanceof ReqAdd) {
				action = "Add";
			} else if (req instanceof ReqUpdate) {
				action = "Update";
			} else if (req instanceof ReqRemove) {
				action = "Remove";
			}
			req.addLogLine(action + " " + req.getClassName() + " objects: " + todo);
			initialized = true;
			unlockWorker(this);
		} else if (!isDone()) {
			lockWorker(this);
			int doneSoFar = done;
			unlockWorker(this);
			int newDone = DbIndex.getInstance().getNextDoneForRequest(req, doneSoFar, this);
			lockWorker(this);
			done = newDone;
			unlockWorker(this);
		} else {
			done();
		}
	}
	
	private void done() {
		lockWorker(this);
		stop();
		if (DbConfig.getInstance().isDebugPerformance()) {
			req.addLogLine("Index processing took: " + ((new Date()).getTime() - started.getTime()) + " ms (objects: " + req.getObjects().size() + ")");
			started = new Date();
		}
		if (req instanceof ReqGet) {
			ReqGet r = ((ReqGet) req);
			if (!r.finishedGetIdListRequest()) {
				r.applyRemainingFiltersToObjectList();
				r.applyOrderByToObjectList();
			}
			r.applyStartLimitToObjectList();
			r.applyPropertiesToObjectList();
		}
		if (nextReq!=null && nextReq instanceof ReqObjectGetAndChange) {
			nextReq.prepare();
			req.addLogLine("Prepared next request: " + nextReq.getClass().getName());
		}
		if (DbConfig.getInstance().isDebugPerformance()) {
			req.addLogLine("Post request processing took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		}
		String action = "";
		if (req instanceof ReqGet) {
			action = "Got";
		} else if (req instanceof ReqAdd) {
			action = "Added";
		} else if (req instanceof ReqUpdate) {
			action = "Updated";
		} else if (req instanceof ReqRemove) {
			action = "Removed";
		}
		if (req instanceof ReqGet) {
			req.addLogLine(action + " " + req.getClassName() + " objects: " + req.getObjects().size());
		} else if (req instanceof ReqObjectChange) {
			req.addLogLine(action + " " + req.getClassName() + " objects: " + ((ReqObjectChange)req).getImpactedIds().size());
		}
		
		if (req.getOriginal()!=null) {
			MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(req.getClassName());
			req.getOriginal().setLog(req.getLog());
			req.getOriginal().getObjects().clear();
			for (ReqDataObject object: req.getObjects()) {
				if (!(req instanceof ReqGet)) {
					if (object.getErrors().size()>0) {
						ReqDataObject copyObject = object.copy();
						for (MdlProperty prop: cls.getPropertiesExtended()) {
							if (!prop.getName().equals(MdlProperty.ID)) {
								copyObject.getDataObject().removePropertyValue(prop.getName());
							}
						}
						copyObject.getChildIndexIdList().clear();
						req.getOriginal().getObjects().add(copyObject);
					}
				} else if (req instanceof ReqGet) {
					req.getOriginal().getObjects().add(object.copy());
				}
			}
			req.getOriginal().getErrors().clear();
			for (ReqError error: req.getErrors()) {
				req.getOriginal().getErrors().add(error.copy());
			}
			if (req instanceof ReqGet) {
				((ReqGet)req.getOriginal()).setCount(((ReqGet)req).getCount());
			}
			if (req instanceof ReqObjectChange) {
				ReqObjectChange r = (ReqObjectChange) req;
				ReqObjectChange o = (ReqObjectChange) req.getOriginal();
				o.getImpactedIds().clear();
				for (long id: r.getImpactedIds()) {
					o.getImpactedIds().add(id);
				}
			}
		}
		
		// Allow index to process new request while this thread runs on outside this class
		finalized = true;
		ReqObject publish = req;
		unlockWorker(this);
		
		publish.publishRequestDoneEvent(this);
	}
	
	public boolean isDone() {
		boolean r = false;
		lockWorker(this);
		r = (initialized && done>=todo);
		unlockWorker(this);
		return r;
	}

	public boolean isFinalized() {
		boolean r = false;
		lockWorker(this);
		r = finalized;
		unlockWorker(this);
		return r;
	}

	public void cleanUp() {
		lockWorker(this);
		req = null;
		nextReq = null;
		unlockWorker(this);
	}

	/**
	 * @return the nextReq
	 */
	public ReqObject getNextReq() {
		return nextReq;
	}

	/**
	 * @param nextReq the nextReq to set
	 */
	public void setNextReq(ReqObject nextReq) {
		this.nextReq = nextReq;
	}
	
	/**************************** PRIVATE METHODS **************************/
	private synchronized void lockWorker(Object source) {
		int attempt = 0;
		while (workerIsLocked()) {
		    try {
                wait();
            } catch (InterruptedException e) { 
            	// Ignore
            }
            attempt ++;
			if (attempt>=10000) {
				Messenger.getInstance().warn(this,Locker.getLockFailedMessage(attempt,source));
				attempt = 0;
			}
		}
		isLockedBy = source;
	}

	private synchronized void unlockWorker(Object source) {
		if (isLockedBy==source) {
			isLockedBy = null;
			notifyAll();
		} else {
			Messenger.getInstance().error(this,"Invalid attempt to unlock request worker by source: " + source);
		}
	}
	
	private synchronized boolean workerIsLocked() {
		return isLockedBy!=null;
	}
}	
