package nl.zeesoft.zadf.controller.impl;

import java.awt.event.ActionEvent;

import nl.zeesoft.zadf.controller.GuiController;
import nl.zeesoft.zadf.controller.GuiWindowController;
import nl.zeesoft.zadf.gui.GuiConfig;
import nl.zeesoft.zadf.gui.GuiDialog;
import nl.zeesoft.zadf.gui.property.PrpCheckBox;
import nl.zeesoft.zadf.gui.property.PrpComboBox;
import nl.zeesoft.zadf.gui.property.PrpInteger;
import nl.zeesoft.zadf.gui.property.PrpString;
import nl.zeesoft.zodb.client.ClConfig;
import nl.zeesoft.zodb.client.ClSession;
import nl.zeesoft.zodb.client.ClSessionInitializationWorker;
import nl.zeesoft.zodb.client.ClSessionManager;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.event.EvtEvent;

public class ClientConfigurationDialogController extends GuiWindowController {
	private boolean handling 	= false;
	private boolean control 	= false;
	
	public ClientConfigurationDialogController(GuiDialog clientConfigurationFrame) {
		super(clientConfigurationFrame);
		control = GuiController.getInstance().isControlGui();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!handling) {
			handling = true;
			String action = "";
			if (e.getActionCommand()!=null) {
				action = e.getActionCommand();
			}
	
			if (action.equals(ACTION_CLOSE_FRAME)) {
				super.actionPerformed(e);
			} else if (action.equals(ZADFFactory.BUTTON_CLIENT_CONFIGURATION_SET)) {
				String hostNameOrIp = getNewHostNameOrIp();
				int port = getNewPort();
				boolean debug = getNewDebug();
				String debugClassPrefix = getNewDebugClassPrefix();
				String xmlCompression = getNewXMLCompression();
				boolean maximizeOnStart = getNewMaximizeOnStart();
				boolean loadGuiModelOnStart = getNewLoadGuiModelOnStart();
				if (
					(!hostNameOrIp.equals(ClConfig.getInstance().getHostNameOrIp())) ||
					(port!=DbConfig.getInstance().getPort()) ||
					(debug!=DbConfig.getInstance().isDebug()) ||
					(!debugClassPrefix.equals(DbConfig.getInstance().getDebugClassPrefix())) ||
					(!xmlCompression.equals(DbConfig.getInstance().getXmlCompression())) ||
					(maximizeOnStart!=GuiConfig.getInstance().isMaximizeOnStart()) ||
					(loadGuiModelOnStart!=GuiConfig.getInstance().isLoadGuiModelOnStart())
					) {
					boolean ok = true;
					if (
						(!hostNameOrIp.equals(ClConfig.getInstance().getHostNameOrIp())) ||
						(port!=DbConfig.getInstance().getPort())
						) {
						if (hostNameOrIp.equals("")) {
							((GuiDialog) getGuiObject()).msgError(ClConfig.LABEL_HOST_NAME_OR_IP + ": Value is mandatory");
							ok = false;
						} else {
							ClSession s = null;
							ClSessionInitializationWorker sw = null;
							if (control) {
								sw = ClSessionManager.getInstance().initializeNewControlSession(hostNameOrIp, port);
							} else {
								sw = ClSessionManager.getInstance().initializeNewSession(hostNameOrIp, port);
							}
							while (sw.isWorking()) {
								try {
									Thread.sleep(10);
								} catch (InterruptedException ex) {
									// Ignore
								}
							}
							s = sw.getSession();
							if ((s==null) || (s.getSessionId()==0)) {
								ok = false;
							}
						}
					}
					if (ok) {
						ClConfig.getInstance().setHostNameOrIp(hostNameOrIp);
						DbConfig.getInstance().setPort(port);
						DbConfig.getInstance().setDebug(debug);
						DbConfig.getInstance().setDebugClassPrefix(debugClassPrefix);
						DbConfig.getInstance().setXmlCompression(xmlCompression);
						GuiConfig.getInstance().setMaximizeOnStart(maximizeOnStart);
						GuiConfig.getInstance().setLoadGuiModelOnStart(loadGuiModelOnStart);
						ClConfig.getInstance().serialize();
						DbConfig.getInstance().serialize();
						GuiConfig.getInstance().serialize();
						publishEvent(new EvtEvent(ACTION_CLOSE_FRAME, this, ACTION_CLOSE_FRAME));
					}
				} else {
					((GuiDialog) getGuiObject()).msgError("No changes to set");
				}
			}
			handling = false;
		}
	}

	public void refresh() {
		setHostNameOrIp(ClConfig.getInstance().getHostNameOrIp());
		setPort(DbConfig.getInstance().getPort());
		setDebug(DbConfig.getInstance().isDebug());
		setDebugClassPrefix(DbConfig.getInstance().getDebugClassPrefix());
		setMaximizeOnStart(GuiConfig.getInstance().isMaximizeOnStart());
		setLoadGuiModelOnStart(GuiConfig.getInstance().isLoadGuiModelOnStart());
		setXmlCompression(DbConfig.getInstance().getXmlCompression());
	}

	private String getNewHostNameOrIp() {
		PrpString guiProp = (PrpString) getGuiObjectByName(ClConfig.PROPERTY_HOST_NAME_OR_IP);
		return guiProp.getNewValueObjectFromComponentValue().getValue();
	}
	
	private int getNewPort() {
		PrpInteger guiProp = (PrpInteger) getGuiObjectByName(DbConfig.PROPERTY_PORT);
		int port = guiProp.getNewValueObjectFromComponentValue().getValue();
		if (control) {
			port--;
		}
		return port;
	}

	private boolean getNewDebug() {
		PrpCheckBox guiProp = (PrpCheckBox) getGuiObjectByName(DbConfig.PROPERTY_DEBUG);
		return guiProp.getNewValueObjectFromComponentValue().getValue();
	}

	private String getNewDebugClassPrefix() {
		PrpString guiProp = (PrpString) getGuiObjectByName(DbConfig.PROPERTY_DEBUG_CLASS_PREFIX);
		return guiProp.getNewValueObjectFromComponentValue().getValue();
	}

	private String getNewXMLCompression() {
		PrpComboBox guiProp = (PrpComboBox) getGuiObjectByName(DbConfig.PROPERTY_XML_COMPRESSION);
		return guiProp.getNewValueObjectFromComponentValue().getValue();
	}

	private boolean getNewMaximizeOnStart() {
		PrpCheckBox guiProp = (PrpCheckBox) getGuiObjectByName(GuiConfig.PROPERTY_MAXIMIZE_ON_START);
		return guiProp.getNewValueObjectFromComponentValue().getValue();
	}

	private boolean getNewLoadGuiModelOnStart() {
		PrpCheckBox guiProp = (PrpCheckBox) getGuiObjectByName(GuiConfig.PROPERTY_LOAD_GUI_MODEL_ON_START);
		return guiProp.getNewValueObjectFromComponentValue().getValue();
	}
	
	private void setHostNameOrIp(String hostNameOrIp) {
		PrpString guiProp = (PrpString) getGuiObjectByName(ClConfig.PROPERTY_HOST_NAME_OR_IP);
		guiProp.getValueObject().setValue(hostNameOrIp);
		guiProp.updateComponentValue();
	}
	
	private void setPort(int port) {
		if (control) {
			port++;
		}
		PrpInteger guiProp = (PrpInteger) getGuiObjectByName(DbConfig.PROPERTY_PORT);
		guiProp.getValueObject().setValue(port);
		guiProp.updateComponentValue();
	}

	private void setDebug(boolean debug) {
		PrpCheckBox guiProp = (PrpCheckBox) getGuiObjectByName(DbConfig.PROPERTY_DEBUG);
		guiProp.getValueObject().setValue(debug);
		guiProp.updateComponentValue();
	}

	private void setDebugClassPrefix(String debugClassPrefix) {
		PrpString guiProp = (PrpString) getGuiObjectByName(DbConfig.PROPERTY_DEBUG_CLASS_PREFIX);
		guiProp.getValueObject().setValue(debugClassPrefix);
		guiProp.updateComponentValue();
	}

	private void setXmlCompression(String xmlCompression) {
		PrpComboBox guiProp = (PrpComboBox) getGuiObjectByName(DbConfig.PROPERTY_XML_COMPRESSION);
		guiProp.getValueObject().setValue(xmlCompression);
		guiProp.updateComponentValue();
	}

	private void setMaximizeOnStart(boolean loadGuiModelOnStart) {
		PrpCheckBox guiProp = (PrpCheckBox) getGuiObjectByName(GuiConfig.PROPERTY_MAXIMIZE_ON_START);
		guiProp.getValueObject().setValue(loadGuiModelOnStart);
		guiProp.updateComponentValue();
	}

	private void setLoadGuiModelOnStart(boolean loadGuiModelOnStart) {
		PrpCheckBox guiProp = (PrpCheckBox) getGuiObjectByName(GuiConfig.PROPERTY_LOAD_GUI_MODEL_ON_START);
		guiProp.getValueObject().setValue(loadGuiModelOnStart);
		guiProp.updateComponentValue();
	}
}
