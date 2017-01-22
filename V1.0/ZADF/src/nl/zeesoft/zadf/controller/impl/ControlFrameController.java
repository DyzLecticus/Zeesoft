package nl.zeesoft.zadf.controller.impl;

import java.awt.event.ActionEvent;

import nl.zeesoft.zadf.controller.GuiController;
import nl.zeesoft.zadf.controller.GuiWindowController;
import nl.zeesoft.zadf.gui.GuiButton;
import nl.zeesoft.zadf.gui.GuiFrame;
import nl.zeesoft.zadf.gui.GuiLabel;
import nl.zeesoft.zodb.client.ClControlSession;
import nl.zeesoft.zodb.client.ClSessionManager;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventSubscriber;

public class ControlFrameController extends GuiWindowController implements EvtEventSubscriber {
	private boolean waitingServer = false;
	private boolean waitingBatch = false;
	private boolean waitingCache = false;
	
	public ControlFrameController(GuiFrame controlFrame) {
		super(controlFrame);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = "";
		if (e.getActionCommand()!=null) {
			action = e.getActionCommand();
		}

		if (action.equals(ACTION_CLOSE_FRAME)) {
			super.actionPerformed(e);
		} else if (action.equals(ZADFFactory.MENU_MAIN_SHOW_DEBUGGER)) {
			publishEvent(new EvtEvent(ZADFFactory.MENU_MAIN_SHOW_DEBUGGER, e.getSource(), ZADFFactory.MENU_MAIN_SHOW_DEBUGGER));
		} else if (action.equals(ZADFFactory.MENU_CONTROL_SHOW_CONFIGURATION)) {
			publishEvent(new EvtEvent(ZADFFactory.MENU_CONTROL_SHOW_CONFIGURATION, e.getSource(), ZADFFactory.MENU_CONTROL_SHOW_CONFIGURATION));
		} else {
			ClControlSession controlSession = GuiController.getInstance().getControlSession();
			if (controlSession!=null) {
				if (!waitingServer) {
					if (action.equals(ZADFFactory.BUTTON_CONTROL_SERVER_IS_WORKING)) {
						getServerIsWorking(controlSession);
					} else if (action.equals(ZADFFactory.BUTTON_CONTROL_STOP_SERVER)) {
						if (confirmServerStop()) {
							waitingServer = true;
							controlSession.getRequestQueue().addRequest(controlSession.getNewStopServerRequest(), this);
						}
					} else if (action.equals(ZADFFactory.BUTTON_CONTROL_START_SERVER)) {
						waitingServer = true;
						controlSession.getRequestQueue().addRequest(controlSession.getNewStartServerRequest(), this);
					}
				}
				if (!waitingBatch) {
					if (action.equals(ZADFFactory.BUTTON_CONTROL_BATCH_IS_WORKING)) {
						getBatchIsWorking(controlSession);
					} else if (action.equals(ZADFFactory.BUTTON_CONTROL_STOP_BATCH)) {
						waitingBatch = true;
						controlSession.getRequestQueue().addRequest(controlSession.getNewStopBatchRequest(), this);
					} else if (action.equals(ZADFFactory.BUTTON_CONTROL_START_BATCH)) {
						waitingBatch = true;
						controlSession.getRequestQueue().addRequest(controlSession.getNewStartBatchRequest(), this);
					}
				}
				if (!waitingCache) {
					if (action.equals(ZADFFactory.BUTTON_CONTROL_SERVER_CACHE)) {
						getServerCache(controlSession);
					} else if (action.equals(ZADFFactory.BUTTON_CONTROL_CACHE_CLEAR)) {
						waitingCache = true;
						controlSession.getRequestQueue().addRequest(controlSession.getNewClearServerCacheRequest(), this);
					}
				}
			}
		}
	}

	private boolean confirmServerStop() {
		return ((GuiFrame) getGuiObject()).msgConfirmYesNo("This will force all clients to disconnect. Are you sure you want to continue?", "Are you sure?");
	}

	public void initialize() {
		ClControlSession controlSession = GuiController.getInstance().getControlSession();
		if (controlSession!=null) {
			getServerIsWorking(controlSession);
			getBatchIsWorking(controlSession);
			getServerCache(controlSession);
		}
	}

	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getType().equals(ClSessionManager.SERVER_IS_WORKING)) {
			//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
			setServerIsWorking((Boolean)e.getValue());
			waitingServer = false;
		} else if (e.getType().equals(ClSessionManager.BATCH_IS_WORKING)) {
			//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
			setBatchIsWorking((Boolean)e.getValue());
			waitingBatch = false;
		} else if (e.getType().equals(ClSessionManager.SERVER_CACHE)) {
			//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
			setServerCache((String)e.getValue());
			waitingCache = false;
		}
	}
	
	private void getServerIsWorking(ClControlSession controlSession) {
		GuiLabel lbl = (GuiLabel) getGuiObjectByName(ZADFFactory.LABEL_CONTROL_SERVER_IS_WORKING);
		lbl.getLabel().setText("?");
		waitingServer = true;
		controlSession.getRequestQueue().addRequest(controlSession.getNewServerIsWorkingRequest(), this);
	}
		
	private void setServerIsWorking(boolean working) {
		GuiLabel lbl = (GuiLabel) getGuiObjectByName(ZADFFactory.LABEL_CONTROL_SERVER_IS_WORKING);
		GuiButton stop = (GuiButton) getGuiObjectByName(ZADFFactory.BUTTON_CONTROL_STOP_SERVER);
		GuiButton start = (GuiButton) getGuiObjectByName(ZADFFactory.BUTTON_CONTROL_START_SERVER);
		String label = "?";
		if (working) {
			label = "Working";
		} else {
			label = "Stopped";
		}
		lbl.getLabel().setText(label);
		stop.setEnabled(working);
		start.setEnabled(!working);
	}
	
	private void getBatchIsWorking(ClControlSession controlSession) {
		GuiLabel lbl = (GuiLabel) getGuiObjectByName(ZADFFactory.LABEL_CONTROL_BATCH_IS_WORKING);
		lbl.getLabel().setText("?");
		waitingBatch = true;
		controlSession.getRequestQueue().addRequest(controlSession.getNewBatchIsWorkingRequest(), this);
	}

	private void setBatchIsWorking(boolean working) {
		GuiLabel lbl = (GuiLabel) getGuiObjectByName(ZADFFactory.LABEL_CONTROL_BATCH_IS_WORKING);
		GuiButton stop = (GuiButton) getGuiObjectByName(ZADFFactory.BUTTON_CONTROL_STOP_BATCH);
		GuiButton start = (GuiButton) getGuiObjectByName(ZADFFactory.BUTTON_CONTROL_START_BATCH);
		String label = "?";
		if (working) {
			label = "Working";
		} else {
			label = "Stopped";
		}
		lbl.getLabel().setText(label);
		stop.setEnabled(working);
		start.setEnabled(!working);
	}

	private void getServerCache(ClControlSession controlSession) {
		GuiLabel lbl = (GuiLabel) getGuiObjectByName(ZADFFactory.LABEL_CONTROL_SERVER_CACHE);
		lbl.getLabel().setText("?");
		waitingCache = true;
		controlSession.getRequestQueue().addRequest(controlSession.getNewGetServerCacheRequest(), this);
		
	}
	private void setServerCache(String cache) {
		GuiLabel lbl = (GuiLabel) getGuiObjectByName(ZADFFactory.LABEL_CONTROL_SERVER_CACHE);
		lbl.getLabel().setText(cache);
	}

}
