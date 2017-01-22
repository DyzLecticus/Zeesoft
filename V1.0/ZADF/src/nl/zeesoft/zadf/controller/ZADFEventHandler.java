package nl.zeesoft.zadf.controller;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import nl.zeesoft.zadf.DebugLog;
import nl.zeesoft.zadf.DebugLogItem;
import nl.zeesoft.zadf.controller.impl.BusyWindowController;
import nl.zeesoft.zadf.controller.impl.ClientConfigurationDialogController;
import nl.zeesoft.zadf.controller.impl.ControlConfigurationDialogController;
import nl.zeesoft.zadf.controller.impl.ControlFrameController;
import nl.zeesoft.zadf.controller.impl.DebugFrameController;
import nl.zeesoft.zadf.controller.impl.DetailDialogController;
import nl.zeesoft.zadf.controller.impl.GenericTreeController;
import nl.zeesoft.zadf.controller.impl.InfoDialogController;
import nl.zeesoft.zadf.controller.impl.LoginDialogController;
import nl.zeesoft.zadf.controller.impl.MainFrameController;
import nl.zeesoft.zadf.controller.impl.MainFrameGridController;
import nl.zeesoft.zadf.controller.impl.ModelFrameController;
import nl.zeesoft.zadf.controller.impl.PrpDateTimeDialogController;
import nl.zeesoft.zadf.controller.impl.PrpIdRefDialogController;
import nl.zeesoft.zadf.controller.impl.ReportDialogController;
import nl.zeesoft.zadf.controller.impl.ZADFFactory;
import nl.zeesoft.zadf.gui.GuiConfig;
import nl.zeesoft.zadf.gui.GuiDialog;
import nl.zeesoft.zadf.gui.GuiFrame;
import nl.zeesoft.zadf.gui.GuiGrid;
import nl.zeesoft.zadf.gui.GuiObject;
import nl.zeesoft.zadf.gui.GuiPropertySelectObject;
import nl.zeesoft.zadf.gui.GuiPropertySelectValue;
import nl.zeesoft.zadf.gui.GuiWindow;
import nl.zeesoft.zadf.gui.GuiWindowObject;
import nl.zeesoft.zadf.gui.property.PrpDateTime;
import nl.zeesoft.zadf.gui.property.PrpIdRef;
import nl.zeesoft.zadf.gui.property.PrpIdRefList;
import nl.zeesoft.zadf.gui.property.PrpPassword;
import nl.zeesoft.zadf.gui.property.PrpString;
import nl.zeesoft.zadf.model.DbModel;
import nl.zeesoft.zadf.model.GuiModelContextFilter;
import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.client.ClRequest;
import nl.zeesoft.zodb.client.ClRequestQueue;
import nl.zeesoft.zodb.client.ClSession;
import nl.zeesoft.zodb.client.ClSessionManager;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.query.QryError;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.database.query.QryRemove;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventSubscriber;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.MdlObject;

/**
 * This class is designed to be extended for specific client implementations
 */
public class ZADFEventHandler implements EvtEventSubscriber {
	private static final int				MAX_REQUEST_QUEUE_SIZE	= 3;
	
	private int								connectAttempts 		= 0;
	
	// Single prompt windows for main window
	private List<GuiWindowController> 		promptOrder 			= new ArrayList<GuiWindowController>();
	private List<PrpIdRefDialogController> 	refControllers 			= new ArrayList<PrpIdRefDialogController>();
	
	// Ensures only required grids are refreshed
	private MainFrameGridController			callerGrid				= null;
	
	private boolean							loggingIn				= false;
	private boolean							removing				= false;
	private boolean							detailsFetching			= false;
	private boolean							loadedModel				= false;
	private boolean							waitingConnectResponse	= false;
	private GuiWindowController				waitingForQueue			= null;
	
	private boolean							stopError				= false;
	
	@Override
	public void handleEvent(EvtEvent e) {
		try {
			if (e.getType().equals(GuiController.STARTED_MAIN_GUI)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				startedGUI();
			} else if (e.getType().equals(GuiController.STARTED_CONTROL_GUI)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				startedGUI();
			} else if (e.getType().equals(GuiController.STOPPING_GUI)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				stoppingGUI();
			} else if (e.getType().equals(GuiController.CAUGHT_EXCEPTION)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				handleException((Exception) e.getValue());
			} else if (e.getType().equals(ClSessionManager.FAILED_TO_CONNECT_TO_SERVER)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				waitingConnectResponse = false;
				failedToConnectToServerOrObtainSessionOrDecodeInput(e);
			} else if (e.getType().equals(ClSessionManager.FAILED_TO_OBTAIN_SESSION)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				waitingConnectResponse = false;
				failedToConnectToServerOrObtainSessionOrDecodeInput(e);
			} else if (e.getType().equals(ClSessionManager.OBTAINED_SESSION)) {
				if (waitingConnectResponse) {
					loginButtonClicked(e);
				}
				waitingConnectResponse = false;
				String title = GuiConfig.getInstance().getGuiFactory().getApplicationName() +" Login*";
				((GuiDialog) getOrCreateLoginDialogController().getGuiObject()).getDialog().setTitle(title);
			} else if (e.getType().equals(ClSessionManager.CRC_MISMATCH)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				crcMismatch(e);
			} else if (e.getType().equals(LoginDialogController.LOGIN_BUTTON_CLICKED)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				if (!waitingConnectResponse) {
					loginButtonClicked(e);
				}
			} else if (e.getType().equals(ClSessionManager.AUTHORIZED_SESSION)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				authorizedSession(e);
			} else if (e.getType().equals(ClSessionManager.FAILED_TO_AUTHORIZE_SESSION)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				failedToAuthorizeSession(e);
			} else if (e.getType().equals(ClSessionManager.SERVER_STOPPED_SESSION)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				stoppedSessionOrLostConnection(e);
			} else if (e.getType().equals(ClSessionManager.LOST_CONNECTION_TO_SERVER)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				stoppedSessionOrLostConnection(e);
			} else if (e.getType().equals(ClSessionManager.UNABLE_TO_DECODE_SERVER_RESPONSE)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				failedToConnectToServerOrObtainSessionOrDecodeInput(e);
			} else if (e.getType().equals(ClSessionManager.REQUEST_RESPONSE_ERROR)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				requestResponseError(e);
				requestQueueUpdated(e);
			} else if (e.getType().equals(ClSessionManager.REQUEST_RESPONSE_PROCESS_ERROR)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				handleException((Exception) e.getValue());
				requestQueueUpdated(e);
			} else if (e.getType().equals(DbModel.LOADED_MODEL)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				getOrCreateBusyWindowController().hide();
				setMainFrameEnabled(true);
				loadedModel = true;
			} else if (e.getType().equals(GuiWindowController.ACTION_CLOSE_FRAME)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				closeFrame(e);
			} else if (e.getType().equals(GuiPropertySelectValue.SELECT_VALUE_CLICKED)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				selectValueClicked(e);
			} else if (e.getType().equals(DebugLog.ADDED_LOG_ITEM)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				DebugLogItem item = (DebugLogItem) e.getValue();
				if (item.isError()) {
					DebugFrameController dfc = getOrCreateDebugFrameController();
					if (dfc.isShowOnError()) {
						GuiFrame frame = (GuiFrame) dfc.getGuiObject();
						if (!frame.getFrame().isVisible()) {
							dfc.refresh();
							setDebugFrameVisible(true);
						}
					}
				}
			} else if (e.getType().equals(ZADFFactory.MENU_MAIN_SHOW_REPORTER)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				getOrCreateReportDialogController().refreshReportModel();
				setReportDialogVisible(true);
				setMainFrameEnabled(false);
			} else if (e.getType().equals(ZADFFactory.MENU_MAIN_SHOW_DEBUGGER)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				getOrCreateDebugFrameController().refresh();
				setDebugFrameVisible(true);
			} else if (e.getType().equals(ZADFFactory.MENU_MAIN_SHOW_MODEL)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				getOrCreateModelFrameController().initialize();
				setModelFrameVisible(true);
			} else if (e.getType().equals(ZADFFactory.MENU_CONTROL_SHOW_CONFIGURATION)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				getOrCreateControlConfigurationDialogController().refresh();
				setControlConfigurationDialogVisible(true);
				setControlFrameEnabled(false);
			} else if (e.getType().equals(ZADFFactory.MENU_MAIN_SHOW_CONFIGURATION)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				getOrCreateClientConfigurationDialogController().refresh();
				setClientConfigurationDialogVisible(true);
				setMainFrameEnabled(false);
			} else if (e.getType().equals(GuiGridController.ADD_BUTTON_CLICKED)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				showDetailDialog(e);
			} else if ( 
				(e.getType().equals(GuiGridController.UPDATE_BUTTON_CLICKED)) ||
				(
					(e.getType().equals(GuiGrid.GRID_DOUBLE_CLICKED)) &&
					(e.getSource() instanceof MainFrameGridController)
				)
				) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				showDetailDialog(new EvtEvent(GuiGridController.UPDATE_BUTTON_CLICKED,e.getSource(),e.getValue()));
			} else if (e.getType().equals(GuiGridController.REMOVE_BUTTON_CLICKED)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				removeSelectedObjects(e);
			} else if (e.getType().equals(DetailDialogController.FETCHING_REFERENCES)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				detailsFetching = true;
				setDetailDialogEnabled(false);
			} else if (e.getType().equals(DetailDialogController.FETCHED_REFERENCES)) {
				//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
				detailsFetching = false;
				setDetailDialogEnabled(true);
			} else if (e.getType().equals(ClSessionManager.ADDED_REQUEST)) {
				if (loadedModel) {
					ClRequestQueue queue = (ClRequestQueue) e.getSource();
					GuiWindow busyWindow = (GuiWindow) getOrCreateBusyWindowController().getGuiObject();
					if ((!busyWindow.getWindow().isVisible()) && (waitingForQueue==null)) {
						if (queue.getQueueSize(this)>=MAX_REQUEST_QUEUE_SIZE) {
							waitingForQueue = promptOrder.get(0);
							((GuiWindowObject) waitingForQueue.getGuiObject()).getWindow().setEnabled(false);
							waitingForQueue.getGuiObject().setEnabled(false);
							getOrCreateBusyWindowController().positionOverWindowController(waitingForQueue);
							getOrCreateBusyWindowController().show();
						}
					}
				}
			} else if (e.getType().equals(ClSessionManager.SENT_REQUEST)) {
				requestQueueUpdated(e);
			} else if (e.getType().equals(ClRequest.RECEIVED_REQUEST_RESPONSE)) {
				boolean removeRequestResponse = false;
				ClRequest request = (ClRequest) e.getValue();
				for (QryObject qry: request.getQueryRequest().getQueries()) {
					if (qry instanceof QryRemove) {
						removeRequestResponse = true;
						break;
					}
				}
				if (removeRequestResponse) {
					//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
					removedSelectedObjects(e);
				}
				requestQueueUpdated(e);
			}
		} catch (Exception exception) {
			handleException(exception);
		}
	}
	
	protected void requestQueueUpdated(EvtEvent e) {
		if (loadedModel) {
			ClRequestQueue queue = null;
			if (e.getSource() instanceof ClRequestQueue) {
				queue = (ClRequestQueue) e.getSource();
			} else if (e.getSource() instanceof ClSession) {
				queue = ((ClSession) e.getSource()).getRequestQueue();
			}
			if ((waitingForQueue!=null) && (queue!=null) && (queue.getQueueSize(this)<=1)) {
				getOrCreateBusyWindowController().hide();
				waitingForQueue.getGuiObject().setEnabled(true);
				((GuiWindowObject) waitingForQueue.getGuiObject()).getWindow().setEnabled(true);
				waitingForQueue = null;
			}
		}
	}
	
	protected void startedGUI() {
		setLoginDialogVisible(true);
		GuiController.getInstance().getSession();
	}

	/**
	 * Override to implement
	 */
	protected void stoppingGUI() {
		
	}
	
	protected void failedToConnectToServerOrObtainSessionOrDecodeInput(EvtEvent e) {
		if (stopError) {
			return;
		}
		boolean error = true;
		if (e.getType().equals(ClSessionManager.FAILED_TO_CONNECT_TO_SERVER)) {
			//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
			connectAttempts++;
			// First connection attempt is silent
			if (connectAttempts<=1) {
				error = false;
			}
		}
		if (error) {
			setLoginDialogEnabled(false);
			if (e.getType().equals(ClSessionManager.FAILED_TO_CONNECT_TO_SERVER)) {
				GuiDialog loginDialog = (GuiDialog) this.getOrCreateLoginDialogController().getGuiObject();
				boolean config = loginDialog.msgConfirmYesNo("Failed to connect to server: " + e.getValue() + ". Would you like to change the configuration?", "Change configuration?"); 
				if (config) {
					// Show configuration
					if (!getOrCreateClientConfigurationDialogController().getGuiObject().getComponent().isVisible()) {
						getOrCreateClientConfigurationDialogController().refresh();
					}
					setClientConfigurationDialogVisible(true);
				} else {
					stopError = true;
					GuiController.getInstance().stopGui();
				}
			} else if (e.getType().equals(ClSessionManager.FAILED_TO_OBTAIN_SESSION)) {
				stopError = true;
				errorMessage("Failed to obtain a session ID");
				GuiController.getInstance().stopGui();
			} else if (e.getType().equals(ClSessionManager.UNABLE_TO_DECODE_SERVER_RESPONSE)) {
				stopError = true;
				errorMessage((String) e.getValue());
				GuiController.getInstance().stopGui();
			}
		}
	}

	protected void crcMismatch(EvtEvent e) {
		errorMessage((String) e.getValue());
		GuiController.getInstance().stopGui();
	}

	protected void loginButtonClicked(EvtEvent e) {
		if (!loggingIn) {
			loggingIn = true;
			waitingConnectResponse = true;
			ClSession session = GuiController.getInstance().getSession();
			if (session!=null) {
				waitingConnectResponse = false;
				PrpString uName = (PrpString) getOrCreateLoginDialogController().getGuiObjectByName(ZADFFactory.PROPERTY_LOGIN_NAME);
				PrpPassword uPassword = (PrpPassword) getOrCreateLoginDialogController().getGuiObjectByName(ZADFFactory.PROPERTY_LOGIN_PASSWORD);
				String userName = uName.getNewValueObjectFromComponentValue().getValue(); 
				StringBuffer userPassword = uPassword.getNewValueObjectFromComponentValue().getValue();
				if (userName.equals("")) {
					errorMessage("Name: value is mandatory");
					loggingIn = false;
				} else if (userName.equals("password")) {
					errorMessage("Password: value is mandatory");
					loggingIn = false;
				} else {
					ClRequest auth = session.getNewAuthorizationRequest(userName, userPassword);
					session.getRequestQueue().addRequest(auth, this);
				}
			} else {
				loggingIn = false;
			}
		}
	}

	protected void authorizedSession(EvtEvent e) {
		setLoginDialogVisible(false);
		if (GuiController.getInstance().isControlGui()) {
			getOrCreateControlFrameController().initialize(); 
			setControlFrameVisible(true);
		} else {
			setMainFrameEnabled(false);
			if (GuiConfig.getInstance().isMaximizeOnStart()) {
				JFrame frame = ((GuiFrame) getOrCreateMainFrameController().getGuiObject()).getFrame();
				frame.setExtendedState(frame.getExtendedState() | Frame.MAXIMIZED_BOTH);
			}
			setMainFrameVisible(true);
			getOrCreateBusyWindowController().positionOverWindowController(getOrCreateMainFrameController());
			getOrCreateBusyWindowController().show();
			DbModel.getInstance().loadEntities();
		}
		loggingIn = false;
	}

	protected void failedToAuthorizeSession(EvtEvent e) {
		errorMessage((String) e.getValue());
		loggingIn = false;
	}

	protected void stoppedSessionOrLostConnection(EvtEvent e) {
		if (stopError) {
			return;
		}
		stopError = true;
		errorMessage((String) e.getValue());
		GuiController.getInstance().stopGui();
	}

	protected void requestResponseError(EvtEvent e) {
		ClRequest r = (ClRequest) e.getValue();
		errorMessage(r.getActionResponse());
	}

	protected void closeFrame(EvtEvent e) {
		if (
			(e.getSource() instanceof LoginDialogController) && 
			(e.getSource() == getOrCreateLoginDialogController())
			) {
			GuiController.getInstance().stopGui();
		} else if (
			(e.getSource() instanceof MainFrameController) && 
			(e.getSource() == getOrCreateMainFrameController())
			) {
			GuiController.getInstance().stopGui();
		} else if (
			(e.getSource() instanceof ControlFrameController) && 
			(e.getSource() == getOrCreateControlFrameController())
			) {
			GuiController.getInstance().stopGui();
		} else if (
			(e.getSource() instanceof ControlConfigurationDialogController) && 
			(e.getSource() == getOrCreateControlConfigurationDialogController())
			) {
			enablePreviousPrompt();
			setControlConfigurationDialogVisible(false);
		} else if (
			(e.getSource() instanceof ClientConfigurationDialogController) && 
			(e.getSource() == getOrCreateClientConfigurationDialogController())
			) {
			enablePreviousPrompt();
			setClientConfigurationDialogVisible(false);
		} else if (
			(e.getSource() instanceof DetailDialogController) && 
			(e.getSource() == getOrCreateDetailDialogController())
			) {
			enablePreviousPrompt();
			setDetailDialogVisible(false);
		} else if (
			(e.getSource() instanceof InfoDialogController) && 
			(e.getSource() == getOrCreateInfoDialogController())
			) {
			enablePreviousPrompt();
			setInfoDialogVisible(getOrCreateInfoDialogController(),null,false,"");
		} else if (
			(e.getSource() instanceof PrpDateTimeDialogController) && 
			(e.getSource() == getOrCreatePrpDateTimeDialogController())
			) {
			enablePreviousPrompt();
			setPrpDateTimeDialogVisible(false);
		} else if (
			(e.getSource() instanceof PrpIdRefDialogController)
			) {
			enablePreviousPrompt();
			PrpIdRefDialogController pdc = (PrpIdRefDialogController) e.getSource();
			setPrpIdRefDialogVisible(refControllers.indexOf(pdc),false);
			// Might be enabled by enablePreviousPrompt (in order to catch the focus) 
			if (detailsFetching) {
				setDetailDialogEnabled(false);
			}
		} else if (
			(e.getSource() instanceof ReportDialogController) && 
			(e.getSource() == getOrCreateReportDialogController())
			) {
			enablePreviousPrompt();
			setReportDialogVisible(false);
		} else if (
			(e.getSource() instanceof DebugFrameController) && 
			(e.getSource() == getOrCreateDebugFrameController())
			) {
			setDebugFrameVisible(false);
		} else if (
			(e.getSource() instanceof ModelFrameController) && 
			(e.getSource() == getOrCreateModelFrameController())
			) {
			setModelFrameVisible(false);
		} else {
			Messenger.getInstance().debug(this, "Unknown source for close frame event handling: " + e.getSource());
		}
	}

	protected void selectValueClicked(EvtEvent e) {
		if (e.getSource() instanceof GuiObject) {
			if ((e.getValue() instanceof PrpIdRef) || (e.getValue() instanceof PrpIdRefList)) {
				if (
					(((GuiObject) e.getSource()).getName().equals(ZADFFactory.GRID_MAIN_PARENT)) ||
					(((GuiObject) e.getSource()).getName().equals(ZADFFactory.GRID_MAIN_CHILD)) 
					) {
					setMainFrameEnabled(false);
					PrpIdRefDialogController pdc = getOrCreatePrpIdRefDialogController(0);
					pdc.setProperty((GuiPropertySelectObject) e.getValue());
					getOrCreatePrpIdRefDialogController(0).positionOverWindowController(getOrCreateMainFrameController());
					setPrpIdRefDialogVisible(0,true);
				} else if (
					(((GuiObject) e.getSource()).getName().equals(ZADFFactory.DIALOG_DETAIL)) 
					) {
					setDetailDialogEnabled(false);
					PrpIdRefDialogController pdc = getOrCreatePrpIdRefDialogController(0);
					pdc.setProperty((GuiPropertySelectObject) e.getValue());
					getOrCreatePrpIdRefDialogController(0).positionOverWindowController(getOrCreateDetailDialogController());
					setPrpIdRefDialogVisible(0,true);
				} else if (
					(((GuiObject) e.getSource()).getName().equals(ZADFFactory.DIALOG_REPORT)) 
					) {
					setReportDialogEnabled(false);
					PrpIdRefDialogController pdc = getOrCreatePrpIdRefDialogController(0);
					pdc.setProperty((GuiPropertySelectObject) e.getValue());
					getOrCreatePrpIdRefDialogController(0).positionOverWindowController(getOrCreateReportDialogController());
					setPrpIdRefDialogVisible(0,true);
				} else if (((GuiObject) e.getSource()).getName().startsWith(ZADFFactory.GRID_ID_REF_COLLECTION)) {
					GuiWindowController gwc = promptOrder.get(0);
					if (gwc instanceof PrpIdRefDialogController) {
						PrpIdRefDialogController caller = (PrpIdRefDialogController) gwc;
						int index = refControllers.indexOf(caller) + 1;
						PrpIdRefDialogController pdc = getOrCreatePrpIdRefDialogController(index);
						pdc.setProperty((GuiPropertySelectObject) e.getValue());
						getOrCreatePrpIdRefDialogController(index).positionOverWindowController(getOrCreatePrpIdRefDialogController((index - 1)));
						setPrpIdRefDialogEnabled((index - 1),false);
						setPrpIdRefDialogVisible(index,true);
					}
				} else {
					Messenger.getInstance().warn(this, "No handler implemented for value selection source: " + e.getSource() + ", value: " + e.getValue());
				}
			} else if (e.getValue() instanceof PrpDateTime) {
				if (
					(((GuiObject) e.getSource()).getName().equals(ZADFFactory.GRID_MAIN_PARENT)) ||
					(((GuiObject) e.getSource()).getName().equals(ZADFFactory.GRID_MAIN_CHILD)) 
					) {
					setMainFrameEnabled(false);
					PrpDateTimeDialogController pdc = getOrCreatePrpDateTimeDialogController();
					pdc.setProperty((PrpDateTime) e.getValue());
					getOrCreatePrpDateTimeDialogController().positionOverWindowController(getOrCreateMainFrameController());
					setPrpDateTimeDialogVisible(true);
				} else if (
					(((GuiObject) e.getSource()).getName().equals(ZADFFactory.DIALOG_DETAIL)) 
					) {
					setDetailDialogEnabled(false);
					PrpDateTimeDialogController pdc = getOrCreatePrpDateTimeDialogController();
					pdc.setProperty((PrpDateTime) e.getValue());
					getOrCreatePrpDateTimeDialogController().positionOverWindowController(getOrCreateDetailDialogController());
					setPrpDateTimeDialogVisible(true);
				} else if (
					(((GuiObject) e.getSource()).getName().equals(ZADFFactory.DIALOG_REPORT)) 
					) {
					setReportDialogEnabled(false);
					PrpDateTimeDialogController pdc = getOrCreatePrpDateTimeDialogController();
					pdc.setProperty((PrpDateTime) e.getValue());
					getOrCreatePrpDateTimeDialogController().positionOverWindowController(getOrCreateReportDialogController());
					setPrpDateTimeDialogVisible(true);
				} else if (((GuiObject) e.getSource()).getName().startsWith(ZADFFactory.GRID_ID_REF_COLLECTION)) {
					GuiWindowController gwc = promptOrder.get(0);
					if (gwc instanceof PrpIdRefDialogController) {
						PrpIdRefDialogController caller = (PrpIdRefDialogController) gwc;
						int index = refControllers.indexOf(caller);
						PrpDateTimeDialogController pdc = getOrCreatePrpDateTimeDialogController();
						pdc.setProperty((PrpDateTime) e.getValue());
						getOrCreatePrpDateTimeDialogController().positionOverWindowController(getOrCreatePrpIdRefDialogController(index));
						setPrpIdRefDialogEnabled(index,false);
						setPrpDateTimeDialogVisible(true);
					}
				} else {
					Messenger.getInstance().warn(this, "No handler implemented for value selection source: " + e.getSource() + ", value: " + e.getValue());
				}
			} else {
				Messenger.getInstance().warn(this, "No handler implemented for value selection source: " + e.getSource() + ", value: " + e.getValue());
			}
		} else {
			Messenger.getInstance().warn(this, "No handler implemented for value selection source: " + e.getSource());
		}
	}

	protected void showDetailDialog(EvtEvent e) {
		callerGrid = (MainFrameGridController) e.getSource();
		DbCollection collection = callerGrid.getCollection();
		int userLevel = GuiController.getInstance().getSession().getUserLevel();
		if (e.getType().equals(GuiGridController.ADD_BUTTON_CLICKED)) {
			if (userLevel > collection.getUserLevelAdd().getValue()) {
				return;
			}
		} else if (e.getType().equals(GuiGridController.UPDATE_BUTTON_CLICKED)) {
			if (userLevel > collection.getUserLevelUpdate().getValue()) {
				return;
			}
		}
		
		MdlDataObject object = null;
		SortedMap<Long, String> extendedReferences = null;

		if (e.getType().equals(GuiGridController.UPDATE_BUTTON_CLICKED)) {
			if (callerGrid.getSelectedIdList().size()>0) {
				long id = callerGrid.getSelectedIdList().get(0);
				object = callerGrid.getResults().getMdlObjectRefById(id).getDataObject();
				extendedReferences = callerGrid.getExtendedReferences();
			} else {
				((GuiFrame) getOrCreateMainFrameController().getGuiObject()).msgError("Select an object");
				return;
			}
		}
		
		getOrCreateDetailDialogController().getFilters().clear();
		for (GuiModelContextFilter filter: callerGrid.getFilters()) {
			if (
				(!filter.getProperty().equals(MdlObject.PROPERTY_ID)) &&
				(!filter.getProperty().equals(MdlObject.PROPERTY_CLASSNAME)) &&
				(!filter.getProperty().equals(MdlObject.PROPERTY_CREATEDBY)) &&
				(!filter.getProperty().equals(MdlObject.PROPERTY_CREATEDON)) &&
				(!filter.getProperty().equals(MdlObject.PROPERTY_CHANGEDBY)) &&
				(!filter.getProperty().equals(MdlObject.PROPERTY_CHANGEDON))
				) {
				getOrCreateDetailDialogController().getFilters().add(filter);
			}
		}
		for (GuiModelContextFilter filter: callerGrid.getControlController().getFilters()) {
			if (
				(filter.isActive()) && (!filter.isInvert()) &&
				(!filter.getProperty().equals(MdlObject.PROPERTY_ID)) &&
				(!filter.getProperty().equals(MdlObject.PROPERTY_CLASSNAME)) &&
				(!filter.getProperty().equals(MdlObject.PROPERTY_CREATEDBY)) &&
				(!filter.getProperty().equals(MdlObject.PROPERTY_CREATEDON)) &&
				(!filter.getProperty().equals(MdlObject.PROPERTY_CHANGEDBY)) &&
				(!filter.getProperty().equals(MdlObject.PROPERTY_CHANGEDON))
				) {
				getOrCreateDetailDialogController().getFilters().add(filter);
			}
		}
		getOrCreateDetailDialogController().setCollection(collection);
		getOrCreateDetailDialogController().setObject(object, extendedReferences);
		getOrCreateDetailDialogController().setCallerGrid(callerGrid);
		getOrCreateDetailDialogController().positionOverWindowController(getOrCreateMainFrameController());
		
		setMainFrameEnabled(false);
		setDetailDialogVisible(true);
	}
	
	protected void removeSelectedObjects(EvtEvent e) {
		if (!removing) {
			removing = true;
			callerGrid = (MainFrameGridController) e.getSource();
			if (callerGrid.getSelectedIdList().size()>0) {
				int userLevel = GuiController.getInstance().getSession().getUserLevel();
				if (userLevel > callerGrid.getCollection().getUserLevelRemove().getValue()) {
					return;
				}
				String objects = "objects";
				if (callerGrid.getSelectedIdList().size()==1) {
					objects = "object";
				}
				boolean remove = ((GuiFrame) getOrCreateMainFrameController().getGuiObject()).msgConfirmYesNo("Are you sure you want to remove the selected " + objects + "?", "Are you sure?");
				if (remove) {
					QryTransaction transaction = new QryTransaction(null);
					for (long id: callerGrid.getSelectedIdList()) {
						transaction.addQuery(new QryRemove(callerGrid.getResults().getMdlObjectRefById(id).getDataObject()));
					}
					ClSession session = GuiController.getInstance().getSession();
					ClRequest request = session.getRequestQueue().getNewRequest(this);
					request.setQueryRequest(transaction);
					request.addSubscriber(this);
					session.getRequestQueue().addRequest(request, this);
				} else {
					removing = false;
				}
			} else {
				((GuiFrame) getOrCreateMainFrameController().getGuiObject()).msgError("Select one or more objects");
				removing = false;
			}
		}
	}
	
	protected void removedSelectedObjects(EvtEvent e) {
		if (e.getType().equals(ClRequest.RECEIVED_REQUEST_RESPONSE)) {
			boolean refresh = false;
			ClRequest request = (ClRequest) e.getValue();
			List<String> errors = new ArrayList<String>();
			for (QryObject qry: request.getQueryRequest().getQueries()) {
				if (qry instanceof QryRemove) {
					QryRemove remove = (QryRemove) qry;
					if (remove.getErrors().size()>0) {
						for (QryError error: remove.getErrors()) {
							errors.add(DbModel.getQueryErrorMessageForCollection(error, remove.getFetch().getClassName()));
						}
					}
					if (
						(((QryTransaction) request.getQueryRequest()).getRemovedIdList().size()>0) ||
						(((QryTransaction) request.getQueryRequest()).getUpdatedIdList().size()>0)
						) {
						refresh = true;
					}
				}
			}
			if (errors.size()>0) {
				((GuiFrame) this.getOrCreateMainFrameController().getGuiObject()).msgError(errors.get(0));
				for (String err: errors) {
					Messenger.getInstance().debug(this, "Remove response error: " + err);
				}
			}
			if (refresh) {
				callerGrid.delayedRefreshData();
			}
			removing = false;
		}
	}

	protected void errorMessage(String message) {
		for (GuiWindowController gwc: promptOrder) {
			if (gwc.getGuiObject().getComponent().isVisible()) {
				((GuiWindowObject) gwc.getGuiObject()).msgError(message);
				break;
			}
		}
	}

	protected void setLoginDialogVisible(boolean visible) {
		LoginDialogController ldc = getOrCreateLoginDialogController();
		((GuiWindowObject) ldc.getGuiObject()).getWindow().setVisible(visible);
		updatePromptOrder(ldc,visible);
	}

	protected void setMainFrameVisible(boolean visible) {
		MainFrameController mfc = getOrCreateMainFrameController();
		((GuiWindowObject) mfc.getGuiObject()).getWindow().setVisible(visible);
		updatePromptOrder(mfc,visible);
	}

	protected void setControlFrameVisible(boolean visible) {
		ControlFrameController cfc = getOrCreateControlFrameController(); 
		((GuiWindowObject) cfc.getGuiObject()).getWindow().setVisible(visible);
		updatePromptOrder(cfc,visible);
	}
	
	protected void setControlConfigurationDialogVisible(boolean visible) {
		final ControlConfigurationDialogController ccdc = getOrCreateControlConfigurationDialogController(); 
		final boolean value = visible;
	    SwingUtilities.invokeLater(new Runnable() {
	    	public void run() {
	    		((GuiWindowObject) ccdc.getGuiObject()).getWindow().setVisible(value);
	    		if (value) {
	    			ccdc.positionOverWindowController(promptOrder.get(0));
	    		}
	    		updatePromptOrder(ccdc,value);
	      	}
	    });	
	}

	protected void setClientConfigurationDialogVisible(boolean visible) {
		final ClientConfigurationDialogController ccdc = getOrCreateClientConfigurationDialogController(); 
		final boolean value = visible;
	    SwingUtilities.invokeLater(new Runnable() {
	    	public void run() {
	    		((GuiWindowObject) ccdc.getGuiObject()).getWindow().setVisible(value);
	    		if (value) {
	    			ccdc.positionOverWindowController(promptOrder.get(0));
	    		}
	    		updatePromptOrder(ccdc,value);
	      	}
	    });	
	}

	protected void setReportDialogVisible(boolean visible) {
		final boolean value = visible;
		final ReportDialogController rdc = getOrCreateReportDialogController(); 
	    SwingUtilities.invokeLater(new Runnable() {
	    	public void run() {
	    		if (value) {
	    			rdc.positionOverWindowController(getOrCreateMainFrameController());
	    		}
	    		((GuiWindowObject) rdc.getGuiObject()).getWindow().setVisible(value);
	    		updatePromptOrder(rdc,value);
	      	}
	    });	
	}

	protected void setDebugFrameVisible(boolean visible) {
		final boolean value = visible;
		final DebugFrameController dfc = getOrCreateDebugFrameController();
	    SwingUtilities.invokeLater(new Runnable() {
	    	public void run() {
	    		GuiWindowObject window = ((GuiWindowObject) dfc.getGuiObject());
	    		if (value) {
		    		if (!window.getWindow().isVisible()) {
			    		window.getWindow().setVisible(value);
		    		} else {
		    			window.getWindow().toFront();
		    		}
	    		} else {
	    			window.getWindow().setVisible(value);
	    		}
	      	}
	    });	
	}

	protected void setModelFrameVisible(boolean visible) {
		final boolean value = visible;
		final ModelFrameController mfc = getOrCreateModelFrameController();
	    SwingUtilities.invokeLater(new Runnable() {
	    	public void run() {
	    		GuiWindowObject window = ((GuiWindowObject) mfc.getGuiObject());
	    		if (value) {
		    		if (!window.getWindow().isVisible()) {
		    			mfc.positionOverWindowController(getOrCreateMainFrameController());
			    		window.getWindow().setVisible(value);
		    		} else {
		    			window.getWindow().toFront();
		    		}
	    		} else {
	    			window.getWindow().setVisible(value);
	    		}
	      	}
	    });	
	}

	protected void setDetailDialogVisible(boolean visible) {
		final boolean value = visible;
		final DetailDialogController ddc = getOrCreateDetailDialogController(); 
	    SwingUtilities.invokeLater(new Runnable() {
	    	public void run() {
	    		if (value) {
	    			ddc.positionOverWindowController(getOrCreateMainFrameController());
	    		}
	    		((GuiWindowObject) ddc.getGuiObject()).getWindow().setVisible(value);
	    		updatePromptOrder(ddc,value);
	      	}
	    });	
	}

	protected void setInfoDialogVisible(InfoDialogController infoDialogController, GuiWindowController parentController, boolean visible, String info) {
		final boolean value = visible;
		final String inf = info;
		final InfoDialogController idc = infoDialogController;
		final GuiWindowController parent = parentController;
	    SwingUtilities.invokeLater(new Runnable() {
	    	public void run() {
	    		if (value) {
	    			idc.positionOverWindowController(parent);
	    		}
	    		idc.setInfo(inf);
	    		((GuiWindowObject) idc.getGuiObject()).getWindow().setVisible(value);
	    		if (value) {
		    		((GuiWindowObject) idc.getGuiObject()).getWindow().toFront();
		    		((GuiWindowObject) idc.getGuiObject()).getWindow().repaint();
		    		((GuiWindowObject) idc.getGuiObject()).getWindow().revalidate();
	    		}
	    		updatePromptOrder(idc,value);
	      	}
	    });	
	}

	protected void setPrpIdRefDialogVisible(int index, boolean visible) {
		final boolean value = visible;
		if (!visible) {
			setPrpDateTimeDialogVisible(false);
		} else {
			setPrpIdRefDialogEnabled(index,true);
		}
		final PrpIdRefDialogController pdc = getOrCreatePrpIdRefDialogController(index);
	    SwingUtilities.invokeLater(new Runnable() {
	    	public void run() {
	    		GuiDialog dialog = ((GuiDialog) pdc.getGuiObject());
	    		dialog.getDialog().setVisible(value);
	    		if (value) {
	    			dialog.getDialog().repaint();
	    		}
	    		updatePromptOrder(pdc,value);
	      	}
	    });	
	}

	protected void setPrpDateTimeDialogVisible(boolean visible) {
		final boolean value = visible;
		final PrpDateTimeDialogController pdc = getOrCreatePrpDateTimeDialogController(); 
	    SwingUtilities.invokeLater(new Runnable() {
	    	public void run() {
	    		GuiDialog dialog = ((GuiDialog) pdc.getGuiObject());
	    		dialog.getDialog().setVisible(value);
	    		if (value) {
	    			dialog.getDialog().repaint();
	    		}
	    		updatePromptOrder(pdc,value);
	      	}
	    });	
	}
	
	protected void setLoginDialogEnabled(boolean enabled) {
		LoginDialogController fc = getOrCreateLoginDialogController();
		fc.getGuiObject().setEnabled(enabled);
	}

	protected void setMainFrameEnabled(boolean enabled) {
		MainFrameController fc = getOrCreateMainFrameController();
		fc.getGuiObject().setEnabled(enabled);
	}

	protected void setControlFrameEnabled(boolean enabled) {
		ControlFrameController fc = getOrCreateControlFrameController();
		fc.getGuiObject().setEnabled(enabled);
	}

	protected void setPrpIdRefDialogEnabled(int index, boolean enabled) {
		PrpIdRefDialogController dc = getOrCreatePrpIdRefDialogController(index);
		dc.getGuiObject().setEnabled(enabled);
	}

	protected void setDetailDialogEnabled(boolean enabled) {
		DetailDialogController dc = getOrCreateDetailDialogController();
		dc.getGuiObject().setEnabled(enabled);
	}

	protected void setReportDialogEnabled(boolean enabled) {
		ReportDialogController dc = getOrCreateReportDialogController();
		dc.getGuiObject().setEnabled(enabled);
	}

	protected void updatePromptOrder(GuiWindowController gwc, boolean visible) {
		if (gwc!=null) {
			if (visible) {
				if (!promptOrder.contains(gwc)) {
					promptOrder.add(0,gwc);
				}
			} else {
				promptOrder.remove(gwc);
			}
		}
	}
	
	protected void enablePreviousPrompt() {
	    SwingUtilities.invokeLater(new Runnable() {
	    	public void run() {
				GuiWindowController gwc = promptOrder.get(1);
				if (gwc instanceof MainFrameController) {
					setMainFrameEnabled(true);
				} else if (gwc instanceof ControlFrameController) {
					setControlFrameEnabled(true);
				} else if (gwc instanceof DetailDialogController) {
					setDetailDialogEnabled(true);
				} else if (gwc instanceof PrpIdRefDialogController) {
					setPrpIdRefDialogEnabled(refControllers.indexOf(gwc),true);
				} else { 
					gwc.getGuiObject().setEnabled(true);
				}
	    	}
	    });
	}
	
	protected void resetPromptOrder() {
		for (GuiObjectController objectController: GuiController.getInstance().getGuiObjectControllers()) {
			if (objectController.getGuiObject() instanceof GuiWindowObject) {
				boolean visible = false;
				if (objectController instanceof MainFrameController) {
					if (objectController == getOrCreateMainFrameController()) {
						visible = true;
					}
				} else if (objectController instanceof DebugFrameController) {
					if (objectController == getOrCreateDebugFrameController()) {
						visible = true;
					}
				} else if (objectController instanceof ControlFrameController) {
					if (objectController == getOrCreateControlFrameController()) {
						visible = true;
					}
				} else if (objectController instanceof BusyWindowController) {
					if (objectController == getOrCreateBusyWindowController()) {
						getOrCreateBusyWindowController().hide();
					}
				} else if (!(objectController instanceof LoginDialogController)) {
					promptOrder.remove(objectController);
				}
				GuiWindowObject window = (GuiWindowObject) objectController.getGuiObject();
				if (!visible) {
					window.getWindow().setVisible(false);
				} else {
					window.setEnabled(true);
				}
			}
		}
	}
	
	protected void handleException(Exception exception) {
		exception.printStackTrace();
		String error = exception.toString();
		if (!error.equals("")) {
			error = error + "\n";
		}
		error = error + Generic.getCallStackString(exception.getStackTrace(),"");
		Messenger.getInstance().error(this, error);
		boolean stop = true;
		if (DbConfig.getInstance().isDebug()) {
			DebugFrameController dfc = getOrCreateDebugFrameController();
			if (dfc.isShowOnError()) {
				stop = false;
				resetPromptOrder();
			}
		}
		if (stop) {
			errorMessage("An exception has been thrown");
			GuiController.getInstance().stopGui();
		}
	}
	
	private MainFrameController getOrCreateMainFrameController() {
		MainFrameController fc = (MainFrameController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.FRAME_MAIN);
		if (fc==null) {
			fc = GuiConfig.getInstance().getGuiFactory().getNewMainFrameController();
			DbModel.getInstance().addSubscriber(getMainFrameTreeParentController());
			DbModel.getInstance().addSubscriber(this);
		}
		return fc;
	}

	private ControlFrameController getOrCreateControlFrameController() {
		ControlFrameController fc = (ControlFrameController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.FRAME_CONTROL);
		if (fc==null) {
			fc = GuiConfig.getInstance().getGuiFactory().getNewControlFrameController();
			ClSessionManager.getInstance().addSubscriber(fc);
		}
		return fc;
	}

	private ControlConfigurationDialogController getOrCreateControlConfigurationDialogController() {
		ControlConfigurationDialogController dc = (ControlConfigurationDialogController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.DIALOG_CONTROL_CONFIGURATION);
		if (dc==null) {
			dc = GuiConfig.getInstance().getGuiFactory().getNewControlConfigurationDialogController();
			ClSessionManager.getInstance().addSubscriber(dc);
		}
		return dc;
	}

	private ClientConfigurationDialogController getOrCreateClientConfigurationDialogController() {
		ClientConfigurationDialogController dc = (ClientConfigurationDialogController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.DIALOG_CLIENT_CONFIGURATION);
		if (dc==null) {
			dc = GuiConfig.getInstance().getGuiFactory().getNewClientConfigurationDialogController();
		}
		return dc;
	}

	private ReportDialogController getOrCreateReportDialogController() {
		ReportDialogController rc = (ReportDialogController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.DIALOG_REPORT);
		if (rc==null) {
			rc = GuiConfig.getInstance().getGuiFactory().getNewReportDialogController();
		}
		return rc;
	}
	
	private DebugFrameController getOrCreateDebugFrameController() {
		DebugFrameController fc = (DebugFrameController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.FRAME_DEBUG);
		if (fc==null) {
			fc = GuiConfig.getInstance().getGuiFactory().getNewDebugFrameController();
			DebugLog.getInstance().addSubscriber(fc);
		}
		return fc;
	}

	private ModelFrameController getOrCreateModelFrameController() {
		ModelFrameController fc = (ModelFrameController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.FRAME_MODEL);
		if (fc==null) {
			fc = GuiConfig.getInstance().getGuiFactory().getNewModelFrameController();
		}
		return fc;
	}

	private DetailDialogController getOrCreateDetailDialogController() {
		DetailDialogController fc = (DetailDialogController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.DIALOG_DETAIL);
		if (fc==null) {
			fc = GuiConfig.getInstance().getGuiFactory().getNewDetailDialogController();
			GuiGridController gcp = (GuiGridController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.GRID_MAIN_PARENT);
			GuiGridController gcc = (GuiGridController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.GRID_MAIN_CHILD);
			fc.addSubscriber(gcp);
			fc.addSubscriber(gcc);
		}
		return fc;
	}

	private InfoDialogController getOrCreateInfoDialogController() {
		InfoDialogController fc = (InfoDialogController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.DIALOG_INFO);
		if (fc==null) {
			fc = GuiConfig.getInstance().getGuiFactory().getNewInfoDialogController();
		}
		return fc;
	}

	private LoginDialogController getOrCreateLoginDialogController() {
		LoginDialogController dc = (LoginDialogController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.DIALOG_LOGIN);
		if (dc==null) {
			dc = GuiConfig.getInstance().getGuiFactory().getNewLoginDialogController();
		}
		return dc;
	}
	
	private BusyWindowController getOrCreateBusyWindowController() {
		BusyWindowController wc = (BusyWindowController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.WINDOW_BUSY);
		if (wc==null) {
			wc = GuiConfig.getInstance().getGuiFactory().getNewBusyWindowController();
		}
		return wc;
	}

	private PrpIdRefDialogController getOrCreatePrpIdRefDialogController(int index) {
		String suffix = "_" + index;
		PrpIdRefDialogController dc = (PrpIdRefDialogController) GuiController.getInstance().getGuiObjectControllerByObjectName((ZADFFactory.DIALOG_ID_REF + suffix));
		if (dc==null) {
			dc = GuiConfig.getInstance().getGuiFactory().getNewPrpIdRefDialogController(suffix);
		}
		if (!refControllers.contains(dc)) {
			refControllers.add(dc);
		}
		return dc;
	}
	
	private PrpDateTimeDialogController getOrCreatePrpDateTimeDialogController() {
		PrpDateTimeDialogController dc = (PrpDateTimeDialogController) GuiController.getInstance().getGuiObjectControllerByObjectName((ZADFFactory.DIALOG_DATETIME));
		if (dc==null) {
			dc = GuiConfig.getInstance().getGuiFactory().getNewPrpDateTimeDialogController();
		}
		return dc;
	}

	private GenericTreeController getMainFrameTreeParentController() {
		return (GenericTreeController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.TREE_MAIN_PARENT);
	}
}
