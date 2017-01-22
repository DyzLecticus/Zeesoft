package nl.zeesoft.zadf.controller.impl;

import java.awt.event.ActionEvent;
import java.util.List;

import nl.zeesoft.zadf.controller.GuiController;
import nl.zeesoft.zadf.controller.GuiWindowController;
import nl.zeesoft.zadf.gui.GuiDialog;
import nl.zeesoft.zadf.gui.GuiProperty;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.client.ClControlSession;
import nl.zeesoft.zodb.client.ClSessionManager;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventSubscriber;
import nl.zeesoft.zodb.model.datatypes.DtObject;
import nl.zeesoft.zodb.protocol.PtcObject;

public class ControlConfigurationDialogController extends GuiWindowController implements EvtEventSubscriber {
	private String[] 	values 	= new String[DbConfig.PROPERTIES.length];
	private boolean 	waiting	= false;
	
	public ControlConfigurationDialogController(GuiDialog controlConfigurationFrame) {
		super(controlConfigurationFrame);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = "";
		if (e.getActionCommand()!=null) {
			action = e.getActionCommand();
		}

		if (action.equals(ACTION_CLOSE_FRAME)) {
			super.actionPerformed(e);
		} else if (action.equals(ZADFFactory.BUTTON_CONTROL_CONFIGURATION_SET)) {
			ClControlSession controlSession = GuiController.getInstance().getControlSession();
			if (controlSession!=null) {
				if (!waiting) {
					boolean confirmClientDisconnectAndReconfigure = false;
					boolean confirmClientReconnect = false;
					String[] vals = new String[DbConfig.PROPERTIES.length];
					boolean changed = false;
					for (int p = 0; p < DbConfig.PROPERTIES.length; p++) {
						GuiProperty guiProp = (GuiProperty) this.getGuiObjectByName(DbConfig.PROPERTIES[p]);
						if (guiProp!=null) {
							DtObject valObj = guiProp.getNewValueObjectFromComponentValue();
							vals[p] = valObj.toString();
							if (!values[p].equals(vals[p])) {
								changed = true;
								if (DbConfig.PROPERTIES[p].equals(DbConfig.PROPERTY_PORT)) {
									confirmClientDisconnectAndReconfigure = true;
								} else if (DbConfig.PROPERTIES[p].equals(DbConfig.PROPERTY_MAX_SESSIONS)) {
									confirmClientReconnect = true;
								} else if (DbConfig.PROPERTIES[p].equals(DbConfig.PROPERTY_ENCRYPT)) {
									confirmClientDisconnectAndReconfigure = true;
								}
							}
						}
					}
					if (changed) {
						boolean ok = true;
						if (confirmClientDisconnectAndReconfigure) {
							ok = confirmClientDisconnectAndReconfigure();
						} else if (confirmClientReconnect) {
							ok = confirmClientReconnect();
						}
						if (ok) {
							waiting = true;
							controlSession.getRequestQueue().addRequest(controlSession.getNewServerSetPropertiesRequest(vals), this);
						}
					} else {
						((GuiDialog) getGuiObject()).msgError("No changes to set");
					}
				}
			}
		}
	}

	private boolean confirmClientDisconnectAndReconfigure() {
		return ((GuiDialog) getGuiObject()).msgConfirmYesNo("Your changes will force all remote clients to disconnect and reconfigure their settings. Are you sure you want to continue?", "Are you sure?");
	}
	
	private boolean confirmClientReconnect() {
		return ((GuiDialog) getGuiObject()).msgConfirmYesNo("Your changes will force all remote clients to reconnect. Are you sure you want to continue?", "Are you sure?");
	}

	public void refresh() {
		ClControlSession controlSession = GuiController.getInstance().getControlSession();
		if (controlSession!=null) {
			waiting = true;
			controlSession.getRequestQueue().addRequest(controlSession.getNewServerGetPropertiesRequest(), this);
		}
	}


	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getType().equals(ClSessionManager.SERVER_PROPERTIES)) {
			List<String> objs = Generic.getObjectsFromString(e.getValue().toString());
			if ((objs.size()>2)) {
				String[] vals = Generic.getValuesFromString(objs.get(2));
				for (int p = 0; p < DbConfig.PROPERTIES.length; p++) {
					values[p] = vals[p];
					GuiProperty guiProp = (GuiProperty) this.getGuiObjectByName(DbConfig.PROPERTIES[p]);
					if (guiProp!=null) {
						DtObject valObj = guiProp.getValueObject();
						valObj.fromString(new StringBuffer(vals[p]));
						guiProp.updateComponentValue();
					}
				}
			} else if (objs.get(0).equals(PtcObject.SET_SERVER_PROPERTIES)) {
				publishEvent(new EvtEvent(ACTION_CLOSE_FRAME, this, ACTION_CLOSE_FRAME));
			}
			waiting = false;
		}
	}
	
}
