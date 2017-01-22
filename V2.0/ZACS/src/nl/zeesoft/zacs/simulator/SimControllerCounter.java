package nl.zeesoft.zacs.simulator;

import nl.zeesoft.zacs.database.model.ZACSModel;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.model.helpers.HlpControllerObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.event.EvtEvent;

public class SimControllerCounter extends HlpControllerObject {
	private int			symbolCount			= 0;
	private int			symLinkCount		= 0;
	private int			conLinkCount		= 0;
	private int			modSymCount			= 0;
	private int			stateHistoryCount	= 0;
	
	private int			totalCount			= 0;

	private ReqGet		getSymbolRequest	= null;
	private ReqGet		getSymLinkRequest	= null;
	private ReqGet		getConLinkRequest	= null;
	private ReqGet		getModSymRequest	= null;
	private ReqGet		getStatHistRequest	= null;
	
	@Override
	protected void initialize() {
		count();
	}
	
	@Override
	public void handleEvent(EvtEvent e) {
		if (getSymbolRequest!=null && e.getValue()==getSymbolRequest) {
			symbolCount = getSymbolRequest.getCount();
			Messenger.getInstance().debug(this,"Load symbol objects: " + symbolCount);
			DbRequestQueue.getInstance().addRequest(getSymLinkRequest,this);
		} else if (getSymLinkRequest!=null && e.getValue()==getSymLinkRequest) {
			symLinkCount = getSymLinkRequest.getCount();
			Messenger.getInstance().debug(this,"Load symbol link objects: " + symLinkCount);
			DbRequestQueue.getInstance().addRequest(getConLinkRequest,this);
		} else if (getConLinkRequest!=null && e.getValue()==getConLinkRequest) {
			conLinkCount = getConLinkRequest.getCount();
			Messenger.getInstance().debug(this,"Load context link objects: " + conLinkCount);
			DbRequestQueue.getInstance().addRequest(getModSymRequest,this);
		} else if (getModSymRequest!=null && e.getValue()==getModSymRequest) {
			modSymCount = getModSymRequest.getCount();
			Messenger.getInstance().debug(this,"Load module symbol objects: " + modSymCount);
			DbRequestQueue.getInstance().addRequest(getStatHistRequest,this);
		} else if (getStatHistRequest!=null && e.getValue()==getStatHistRequest) {
			stateHistoryCount = getStatHistRequest.getCount();
			Messenger.getInstance().debug(this,"Load state history objects: " + stateHistoryCount);
			totalCount = symbolCount + symLinkCount + conLinkCount + modSymCount + stateHistoryCount; 
			setDone(true);
		}
	}
	
	private void count() {
		setDone(false);
		symbolCount = 0;
		symLinkCount = 0;
		conLinkCount = 0;
		modSymCount = 0;
		stateHistoryCount = 0; 
		totalCount = 0;
		getSymbolRequest = new ReqGet(ZACSModel.SYMBOL_CLASS_FULL_NAME);
		getSymbolRequest.setLimit(1);
		getSymbolRequest.addSubscriber(this);
		getSymLinkRequest = new ReqGet(ZACSModel.SYMBOL_LINK_CLASS_FULL_NAME);
		getSymLinkRequest.setLimit(1);
		getSymLinkRequest.addSubscriber(this);
		getConLinkRequest = new ReqGet(ZACSModel.CONTEXT_LINK_CLASS_FULL_NAME);
		getConLinkRequest.setLimit(1);
		getConLinkRequest.addSubscriber(this);
		getModSymRequest = new ReqGet(ZACSModel.MODULE_SYMBOL_CLASS_FULL_NAME);
		getModSymRequest.setLimit(1);
		getModSymRequest.addSubscriber(this);
		getStatHistRequest = new ReqGet(ZACSModel.STATE_HISTORY_CLASS_FULL_NAME);
		getStatHistRequest.setLimit(1);
		getStatHistRequest.addSubscriber(this);
		DbRequestQueue.getInstance().addRequest(getSymbolRequest,this);
		waitTillDone();
	}

	/**
	 * @return the totalCount
	 */
	public int getTotalCount() {
		return totalCount;
	}
}
