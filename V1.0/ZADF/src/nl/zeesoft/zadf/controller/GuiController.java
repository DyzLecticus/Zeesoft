package nl.zeesoft.zadf.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import nl.zeesoft.zadf.DebugLog;
import nl.zeesoft.zadf.controller.impl.ZADFFactory;
import nl.zeesoft.zadf.gui.GuiConfig;
import nl.zeesoft.zadf.gui.GuiObject;
import nl.zeesoft.zadf.gui.GuiWindow;
import nl.zeesoft.zadf.gui.GuiWindowObject;
import nl.zeesoft.zadf.gui.panel.PnlBusy;
import nl.zeesoft.zadf.model.GuiModel;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.WorkerUnion;
import nl.zeesoft.zodb.client.ClConfig;
import nl.zeesoft.zodb.client.ClControlSession;
import nl.zeesoft.zodb.client.ClSession;
import nl.zeesoft.zodb.client.ClSessionManager;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventPublisher;
import nl.zeesoft.zodb.event.EvtEventSubscriber;

public final class GuiController extends EvtEventPublisher implements EvtEventSubscriber, ActionListener, TreeSelectionListener, ListSelectionListener, PropertyChangeListener, WindowFocusListener {
	protected final static String		STARTED_MAIN_GUI		= "STARTED_MAIN_GUI";
	protected final static String		STARTED_CONTROL_GUI		= "STARTED_CONTROL_GUI";
	protected final static String		STOPPING_GUI			= "STOPPING_GUI";
	protected final static String		CAUGHT_EXCEPTION		= "CAUGHT_EXCEPTION";
	
	private static GuiController		controller				= null;
	
	private	ClSession					session					= null;
	private	ClControlSession			controlSession			= null;
	private List<GuiObjectController>	guiObjectControllers 	= new ArrayList<GuiObjectController>();
	
	private boolean						controlGui				= false;
	private boolean						waitingConnectResponse  = false;
	
	private boolean						stoppingGui				= false;
	
	private GuiController() {
		// Singleton
	}

	public static GuiController getInstance() {
		if (controller==null) {
			controller = new GuiController();
			controller.addSubscriber(GuiConfig.getInstance().getGuiEventHandler());
			
			ClSessionManager.getInstance().addSubscriber(controller);
			ClSessionManager.getInstance().addSubscriber(GuiConfig.getInstance().getGuiEventHandler());
		}
		return controller;
	}

	public void startMainGui() {
		unserializeConfig();
		attachDebugLogger();
		Messenger.getInstance().debug(this,"Starting main GUI ...");
		publishEvent(new EvtEvent(STARTED_MAIN_GUI,this,"Started main GUI"));
	}

	public void startControlGui() {
		controlGui = true;
		unserializeConfig();
		attachDebugLogger();
		Messenger.getInstance().debug(this,"Starting control GUI ...");
		publishEvent(new EvtEvent(STARTED_CONTROL_GUI,this,"Started control GUI"));
	}

	private void attachDebugLogger() {
		if (DbConfig.getInstance().isDebug()) {
			Messenger.getInstance().addSubscriber(DebugLog.getInstance());
			ClSessionManager.getInstance().addSubscriber(DebugLog.getInstance());
			DebugLog.getInstance().addSubscriber(GuiConfig.getInstance().getGuiEventHandler());
			Messenger.getInstance().debug(this,"Attached debug logger");
		}
	}
	
	public void stopGui() {
		if (stoppingGui) {
			return;
		}
		stoppingGui = true;
		Messenger.getInstance().debug(this,"Stopping GUI ...");
		publishEvent(new EvtEvent(STOPPING_GUI,this,"Stopping GUI"));
		for (GuiObjectController objectController: guiObjectControllers) {
			if (objectController.getGuiObject() instanceof GuiWindowObject) {
				GuiWindowObject window = (GuiWindowObject) objectController.getGuiObject();
				window.getWindow().setVisible(false);
			}
		}
		stopWorking();
		if (!isControlGui()) {
			GuiConfig.getInstance().serialize();
			GuiModel.getInstance().serialize();
		}
		Messenger.getInstance().debug(this, "Exiting program ...");
		System.exit(0);
	}

	public void stopWorking() {
		for (GuiObjectController objectController: guiObjectControllers) {
			if (objectController instanceof GuiGridController) {
				GuiGridController gc = (GuiGridController) objectController;
				gc.stopRefreshDataWorker();
			} else if (objectController.getGuiObject() instanceof GuiWindow) {
				GuiWindow window = (GuiWindow) objectController.getGuiObject();
				GuiObject obj = window.getPanelObjects().getGuiObjectByName(ZADFFactory.PANEL_BUSY);
				if ((obj!=null) && (obj instanceof PnlBusy)) {
					// Stop busy panel worker
					PnlBusy busyPanel = (PnlBusy) obj;
					busyPanel.stop();
				}
			}
		}
    	ClSessionManager.getInstance().stopSessions();
    	WorkerUnion.getInstance().stopWorkers();
	}
	
	public void addGuiObjectController(GuiObjectController goc) {
		if (getGuiObjectControllerByObjectName(goc.getGuiObject().getName())==null) { 
			guiObjectControllers.add(goc);
			goc.addSubscriber(GuiConfig.getInstance().getGuiEventHandler());
		} else {
			Messenger.getInstance().error(this, "Object controller for GUI object: " + goc.getGuiObject().getName() + " already registered");
		}
	}
	
	public GuiObjectController getGuiObjectControllerByObjectName(String name) {
		GuiObjectController c = null;
		
		List<GuiObjectController> objectControllers	= new ArrayList<GuiObjectController>(guiObjectControllers);
		for (GuiObjectController objectController: objectControllers) {
			if (objectController.getGuiObject().getName().equals(name)) {
				c = objectController;
				break;
			}
		}
		return c;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Component source = null;
		if ((e.getSource()!=null) && (e.getSource() instanceof Component)) {
			source = (Component) e.getSource();
		}
		if (source!=null) {
			GuiObjectController objectController = getGuiControllerForSourceComponent(source);
			if (objectController!=null) {
				//Messenger.getInstance().debug(this, "Forwarding action: " + e.getActionCommand() + ", to source GUI object: " + objectController.getGuiObject().getName() + ", source: " + e.getSource());
				try {
					objectController.actionPerformed(e);
				} catch (Exception exception) {
					publishEvent(new EvtEvent(CAUGHT_EXCEPTION,this,exception));
				}
			} else {
				Messenger.getInstance().error(this, "Source GUI object not found for action: " + e.getActionCommand() + ", source: " + e.getSource());
			}
		}
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		Component source = null;
		if ((e.getSource()!=null) && (e.getSource() instanceof Component)) {
			source = (Component) e.getSource();
		}
		if (source!=null) {
			GuiObjectController objectController = getGuiControllerForSourceComponent(source);
			if (objectController!=null) {
				//Messenger.getInstance().debug(this, "Forwarding tree selection to source GUI object: " + objectController.getGuiObject().getName() + ", source: " + e.getSource());
				try {
					objectController.valueChanged(e);
				} catch (Exception exception) {
					publishEvent(new EvtEvent(CAUGHT_EXCEPTION,this,exception));
				}
			} else {
				Messenger.getInstance().error(this, "Source GUI object not found for tree selection, source: " + e.getSource());
			}
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		Component source = null;
		if ((e.getSource()!=null) && (e.getSource() instanceof Component)) {
			source = (Component) e.getSource();
		}
		if (source!=null) {
			GuiObjectController objectController = getGuiControllerForSourceComponent(source);
			if (objectController!=null) {
				//Messenger.getInstance().debug(this, "Forwarding property change: " + e.getPropertyName() + ", to source GUI object: " + objectController.getGuiObject().getName() + ", source: " + e.getSource());
				try {
					objectController.propertyChange(e);
				} catch (Exception exception) {
					publishEvent(new EvtEvent(CAUGHT_EXCEPTION,this,exception));
				}
			} else {
				Messenger.getInstance().error(this, "Source GUI object not found for property change: " + e.getPropertyName() + ", source: " + e.getSource());
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		Component source = null;
		if (e.getSource()!=null) {
			if (e.getSource() instanceof Component) {
				source = (Component) e.getSource();
			}
		}
		if (source!=null) {
			GuiObjectController objectController = getGuiControllerForSourceComponent(source);
			if (objectController!=null) {
				//Messenger.getInstance().debug(this, "Forwarding list selection to source GUI object: " + objectController.getGuiObject().getName() + ", source: " + e.getSource());
				try {
					objectController.valueChanged(e);
				} catch (Exception exception) {
					publishEvent(new EvtEvent(CAUGHT_EXCEPTION,this,exception));
				}
			} else {
				Messenger.getInstance().error(this, "Source GUI object not found for list selection, source: " + e.getSource());
			}
		}
	}


	@Override
	public void windowGainedFocus(WindowEvent e) {
		Component source = null;
		if (e.getSource()!=null) {
			if (e.getSource() instanceof Component) {
				source = (Component) e.getSource();
			}
		}
		if (source!=null) {
			GuiObjectController objectController = getGuiControllerForSourceComponent(source);
			if (objectController!=null) {
				//Messenger.getInstance().debug(this, "Forwarding window gained focus to source GUI object: " + objectController.getGuiObject().getName() + ", source: " + e.getSource());
				try {
					objectController.windowGainedFocus(e);
				} catch (Exception exception) {
					publishEvent(new EvtEvent(CAUGHT_EXCEPTION,this,exception));
				}
			} else {
				Messenger.getInstance().error(this, "Source GUI object not found for window gained focus, source: " + e.getSource());
			}
		}
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		Component source = null;
		if (e.getSource()!=null) {
			if (e.getSource() instanceof Component) {
				source = (Component) e.getSource();
			}
		}
		if (source!=null) {
			GuiObjectController objectController = getGuiControllerForSourceComponent(source);
			if (objectController!=null) {
				//Messenger.getInstance().debug(this, "Forwarding window lost focus to source GUI object: " + objectController.getGuiObject().getName() + ", source: " + e.getSource());
				try {
					objectController.windowLostFocus(e);
				} catch (Exception exception) {
					publishEvent(new EvtEvent(CAUGHT_EXCEPTION,this,exception));
				}
			} else {
				Messenger.getInstance().error(this, "Source GUI object not found for window lost focus, source: " + e.getSource());
			}
		}
	}

	private GuiObjectController getGuiControllerForSourceComponent(Component source) {
		GuiObjectController c = null;
		for (GuiObjectController controller: guiObjectControllers) {
			if (controller.getGuiObject().getGuiObjectForSourceComponent(source)!=null) {
				c = controller;
				break;
			}
		}
		return c;
	}

	public ClSession getSession() {
		if (!waitingConnectResponse) {
			waitingConnectResponse = true;
			if (session==null) {
				if (controlGui) {
					ClSessionManager.getInstance().initializeNewControlSession();
				} else {
					ClSessionManager.getInstance().initializeNewSession();
				}
			}
		}
		return session;
	}

	public ClControlSession getControlSession() {
		if (controlSession==null) {
			ClSessionManager.getInstance().initializeNewControlSession();
		}
		return controlSession;
	}
	
	private void unserializeConfig() {
		Messenger.getInstance().debug(this,"Unserializing configuration ...");
		DbConfig.getInstance().unserialize();
		ClConfig.getInstance().unserialize();
		GuiConfig.getInstance().unserialize();
	}

	/**
	 * @return the controlGui
	 */
	public boolean isControlGui() {
		return controlGui;
	}

	/**
	 * @return the guiObjectControllers
	 */
	public List<GuiObjectController> getGuiObjectControllers() {
		return new ArrayList<GuiObjectController>(guiObjectControllers);
	}

	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getType().equals(ClSessionManager.OBTAINED_SESSION)) {
			if (e.getValue()!=null) {
				if (e.getValue() instanceof ClControlSession) {
					controlSession = (ClControlSession) e.getValue();
					session = controlSession;
				} else {
					session = (ClSession) e.getValue();
				}
			}
			waitingConnectResponse = false;
		} else if (e.getType().equals(ClSessionManager.FAILED_TO_CONNECT_TO_SERVER)) {
			waitingConnectResponse = false;
		} else if (e.getType().equals(ClSessionManager.FAILED_TO_OBTAIN_SESSION)) {
			waitingConnectResponse = false;
		}
	}
}
