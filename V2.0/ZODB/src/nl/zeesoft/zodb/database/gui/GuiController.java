package nl.zeesoft.zodb.database.gui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.ZODB;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.database.server.SvrController;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventSubscriber;

public class GuiController extends Locker implements EvtEventSubscriber {
	protected static final String		MESSAGE_CAPTION_STARTED		= "Started database";
	protected static final String		MESSAGE_STARTED				= "Click on this icon to access the database controller";
	protected static final String		MESSAGE_CAPTION_STATUS		= "Server status changed";
	protected static final String		STARTED_LABEL				= "Database started";
	protected static final String		SERVER_STATUS_LABEL			= "Server status";
	protected static final String		SERVER_URL_LABEL			= "Server url";
	
	private static GuiController		controller					= null;

	private GuiProgressFrame			progressFrame				= null;
	private GuiMainFrame				mainFrame					= null;
	private GuiDebugFrame				debugFrame					= null;
	private GuiTrayIcon					trayIcon					= null;

	private BufferedImage				iconGreen					= null;	
	private BufferedImage				iconRed						= null;

	private Date						startedDate					= null;
	private boolean						serverIsOpen				= false;
	private BufferedImage				statusIcon					= null;
	private String						statusText					= "Closed";
	private String						urlText						= null;
	private StringBuilder				debugLog					= new StringBuilder();
	private boolean						showDebugger				= DbConfig.getInstance().isDebug();
		
	private boolean						started						= false;
	private boolean						stopping					= false;
	private boolean						messageOnNextStatusChange	= true;
	
	private GuiKeyListener				keyListener					= new GuiKeyListener();
	
	private GuiController() {
		// Singleton
	}

	public static GuiController getInstance() {
		if (controller==null) {
			controller = new GuiController();
			if (DbConfig.getInstance().isShowGUI()) {
				//Try to set the system look and feel
		        try {
		        	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		        	JFrame.setDefaultLookAndFeelDecorated(true);
		        } catch (Exception e) {
		        	Messenger.getInstance().debug(controller, "Unable to set system look and feel: " + e.getMessage());
		        }
		        ToolTipManager.sharedInstance().setDismissDelay(10000);
				DbController.getInstance().insertSubscriber(controller);
				controller.getKeyListener().addSubscriber(controller);
			}
		}
		return controller;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}

	@Override
	public void handleEvent(EvtEvent e) {
		if (!DbConfig.getInstance().isShowGUI()) {
			return;
		}
		if (e.getType().equals(DbController.DB_STARTING)) {
			lockMe(this);
			startedDate = new Date();
			unlockMe(this);
			getMainFrame().refreshPanel();
		} else if (e.getType().equals(DbController.DB_STARTED)) {
			setStarted(true);
			if (getTrayIcon().isAdded()) {
				getTrayIcon().displayInfoMessage(MESSAGE_CAPTION_STARTED,MESSAGE_STARTED);
				messageOnNextStatusChange = !DbConfig.getInstance().isOpenServer();
			}
		} else if (e.getType().equals(DbController.DB_STOPPING)) {
			stoppingZODB();
		} else if (e.getType().equals(DbController.DB_STOPPED)) {
			stoppedZODB();
		} else if (mainFrame!=null && e.getSource()==mainFrame) {
			if (e.getType().equals(GuiMainFrame.STOP_DATABASE_CLICKED)) {
				stopDatabaseClicked();
			} else if (e.getType().equals(GuiMainFrame.SET_STATUS_CLICKED)) {
				setStatusClicked();
			} else if (e.getType().equals(GuiMainFrame.START_BROWSER_CLICKED)) {
				startBrowserClicked();
			} else if (e.getType().equals(GuiMainFrame.SHOW_DEBUGGER_CLICKED)) {
				showDebuggerClicked();
			} else if (e.getType().equals(GuiFrameObject.FRAME_ICONIFIED)) {
				if (getTrayIcon().isAdded()) {
					mainFrame.setVisible(false);
				}
			} else if (e.getType().equals(GuiFrameObject.FRAME_CLOSING)) {
				boolean confirmed = getMainFrame().msgConfirmYesNo("Are you sure you want to close the interface?","Close interface?");
				if (confirmed) {
					closeAllFrames();
				} 
			}
		} else if (debugFrame!=null && e.getSource()==debugFrame) {
			if (e.getType().equals(GuiFrameObject.FRAME_CLOSING)) {
				debugFrame.setVisible(false);
			}
		} else if (progressFrame!=null && e.getSource()==progressFrame) {
			if (e.getType().equals(GuiFrameObject.FRAME_CLOSING)) {
				progressFrame.cornerFrameLocation(false,false);
			}
		} else if (e.getType().equals(GuiKeyListener.CTRL_SHIFT_D_PRESSED)) {
			if (DbConfig.getInstance().isDebug()) {
				boolean confirmed = confirmFocusFrame("Do you want to turn off debugging?","Turn off debugging?");
				if (confirmed) {
					DbConfig.getInstance().setDebug(false);
					DbConfig.getInstance().serialize();
					setShowDebugger(false);
				}
			}
		} else if (e.getType().equals(GuiKeyListener.CTRL_D_PRESSED)) {
			if (!DbConfig.getInstance().isDebug()) {
				boolean confirmed = confirmFocusFrame("Do you want to turn on debugging?","Turn on debugging?");
				if (confirmed) {
					DbConfig.getInstance().setDebug(true);
					DbConfig.getInstance().serialize();
					setShowDebugger(true);
				}
			} else {
				showDebuggerClicked();
			}
		} else if (
			e.getType().equals(Messenger.MSG_DEBUG) ||
			e.getType().equals(Messenger.MSG_WARNING) ||
			e.getType().equals(Messenger.MSG_ERROR)
			) {
			handleMessageEvent(e);
		}
	}

	public void initializeMainFrame(boolean forceShow) {
		if (!DbConfig.getInstance().isShowGUI()) {
			return;
		}
		if (!isStopping()) {
			getTrayIcon().addTrayIcon();
			if (!getTrayIcon().isAdded() || forceShow) {
				if (forceShow) {
					getMainFrame().normalize();
				} else {
					getMainFrame().setVisible(true);
				}
			}
		}
	}

	public void serverStatusChanged() {
		if (!DbConfig.getInstance().isShowGUI()) {
			return;
		}
		if (SvrController.getInstance().isOpen()) {
			lockMe(this);
			serverIsOpen = true;
			statusIcon = getIconGreen();
			statusText = "Open";
			unlockMe(this);
		} else {
			lockMe(this);
			serverIsOpen = false;
			statusIcon = getIconRed();
			statusText = "Closed";
			unlockMe(this);
		}
		if (mainFrame!=null) {
			mainFrame.refreshPanel();
		}
		if (getTrayIcon().isAdded()) {
			getTrayIcon().refreshTrayIcon();
			if (!isStopping() && messageOnNextStatusChange) {
				getTrayIcon().displayInfoMessage(MESSAGE_CAPTION_STATUS,SERVER_STATUS_LABEL + ": " + statusText);
			}
		}
		messageOnNextStatusChange = true;
	}

	public boolean showConfirmMsg(String msg, String title, boolean defaultValue) {
		boolean r = defaultValue;
		if (DbConfig.getInstance().isShowGUI()) {
			if (progressFrame!=null && progressFrame.isVisible()) {
				progressFrame.cornerFrameLocation(false,false);				
			}
			r = getMainFrame().msgConfirmYesNo(msg,title);
		}
		return r;
	}

	public String showInputMsg(String msg, String title, String defaultValue) {
		String r = defaultValue;
		if (DbConfig.getInstance().isShowGUI()) {
			if (progressFrame!=null && progressFrame.isVisible()) {
				progressFrame.cornerFrameLocation(false,false);				
			}
			r = getMainFrame().msgGetInput(msg,title);
		}
		return r;
	}
	
	public void showProgressFrame() {
		if (!DbConfig.getInstance().isShowGUI()) {
			return;
		}
		if (mainFrame!=null && mainFrame.isVisible()) {
			mainFrame.setEnabled(false);
			getProgressFrame().positionOverFrame(mainFrame);
		} else {
			getProgressFrame().centreFrameLocation();
		}
		getProgressFrame().setVisible(true);
		setMainFrameEnabled();
	}
	
	public void hideProgressFrame() {
		if (!DbConfig.getInstance().isShowGUI()) {
			return;
		}
		getProgressFrame().setVisible(false);
		setMainFrameEnabled();
	}
	
	public void refreshProgressFrame() {
		if (!DbConfig.getInstance().isShowGUI()) {
			return;
		}
		if (getProgressFrame().isVisible()) {
			getProgressFrame().refreshPanel();
		}
	}

	public void setProgressFrameTitle(String title) {
		if (!DbConfig.getInstance().isShowGUI()) {
			return;
		}
		getProgressFrame().setTitle(title);
	}
	
	public void setProgressFrameTodo(int todo) {
		if (!DbConfig.getInstance().isShowGUI()) {
			return;
		}
		getProgressFrame().setTodo(todo);
	}

	public void incrementProgressFrameDone() {
		if (!DbConfig.getInstance().isShowGUI()) {
			return;
		}
		if (getProgressFrame().isVisible()) {
			getProgressFrame().incrementDone();
		}
	}

	public void incrementProgressFrameDone(int increment) {
		if (!DbConfig.getInstance().isShowGUI()) {
			return;
		}
		if (getProgressFrame().isVisible()) {
			getProgressFrame().incrementDone(increment);
		}
	}
	
	/**
	 * @return the startedDate
	 */
	protected Date getStartedDate() {
		Date r = null;
		lockMe(this);
		r = startedDate;
		unlockMe(this);
		return r;
	}

	/**
	 * @return the serverIsOpen
	 */
	protected boolean serverIsOpen() {
		boolean r = false;
		lockMe(this);
		r = serverIsOpen;
		unlockMe(this);
		return r;
	}

	/**
	 * @return the statusIcon
	 */
	protected BufferedImage getStatusIcon() {
		BufferedImage r = null;
		lockMe(this);
		if (statusIcon==null) {
			statusIcon = getIconRed();
		}
		r = statusIcon;
		unlockMe(this);
		return r;
	}

	/**
	 * @return the statusText
	 */
	protected String getStatusText() {
		String r = null;
		lockMe(this);
		r = statusText;
		unlockMe(this);
		return r;
	}

	/**
	 * @return the urlText
	 */
	protected String getUrlText() {
		String r = null;
		lockMe(this);
		if (urlText==null) {
			urlText = SvrController.getInstance().getUrl(false);
		}
		r = urlText;
		unlockMe(this);
		return r;
	}
	
	/**
	 * @return the debugLog
	 */
	protected StringBuilder getDebugLog() {
		StringBuilder r = null;
		lockMe(this);
		r = new StringBuilder(debugLog);
		unlockMe(this);
		return r;
	}

	/**
	 * @return the showDebugger
	 */
	protected boolean isShowDebugger() {
		boolean r = showDebugger;
		lockMe(this);
		r = showDebugger;
		unlockMe(this);
		return r;
	}

	protected void setShowDebugger(boolean showDebugger) {
		lockMe(this);
		this.showDebugger = showDebugger;
		unlockMe(this);
		if (!showDebugger && debugFrame!=null) {
			debugFrame.setVisible(false);
		}
		if (mainFrame!=null) {
			mainFrame.refreshPanel();
		}
	}

	/**
	 * @return the keyListener
	 */
	protected GuiKeyListener getKeyListener() {
		return keyListener;
	}
	
	protected static JButton getNewButton(String text) {
		JButton button = new JButton(text);
		button.addKeyListener(GuiController.getInstance().getKeyListener());
		return button;
	}

	protected static JTextArea getNewTextArea(boolean disabled) {
		JTextArea textarea = new JTextArea();
		if (disabled) {
			textarea.setEditable(false);
			textarea.setDisabledTextColor(Color.BLACK);
		}
		textarea.setLineWrap(false);
		return textarea;
	}

	private BufferedImage getIconGreen() {
		if (iconGreen==null) {
			iconGreen = ZODB.getIconImage(Color.GREEN).getBufferedImage();
		}
		return iconGreen;
	}

	private BufferedImage getIconRed() {
		if (iconRed==null) {
			iconRed = ZODB.getIconImage(Color.RED).getBufferedImage();
		}
		return iconRed;
	}
	
	private GuiProgressFrame getProgressFrame() {
		if (progressFrame==null) {
			progressFrame = new GuiProgressFrame(300,20);
			progressFrame.addSubscriber(this);
		}
		return progressFrame;
	}

	private GuiMainFrame getMainFrame() {
		if (mainFrame==null) {
			mainFrame = new GuiMainFrame();
			mainFrame.addSubscriber(this);
		}
		return mainFrame;
	}

	private GuiDebugFrame getDebugFrame() {
		if (debugFrame==null) {
			debugFrame = new GuiDebugFrame();
			debugFrame.addSubscriber(this);
		}
		return debugFrame;
	}

	private GuiTrayIcon getTrayIcon() {
		if (trayIcon==null) {
			trayIcon = new GuiTrayIcon();
		}
		return trayIcon;
	}

	private void setStarted(boolean started) {
		lockMe(this);
		this.started = started;
		unlockMe(this);
		setMainFrameEnabled();
	}

	private void setStopping(boolean stopping) {
		lockMe(this);
		this.stopping = stopping;
		unlockMe(this);
		setMainFrameEnabled();
	}

	private boolean isStopping() {
		boolean r = false;
		lockMe(this);
		r = stopping;
		unlockMe(this);
		return r;
	}
	
	public void setMainFrameEnabled() {
		if (mainFrame!=null) {
			boolean r = false;
			lockMe(this);
			r = (started && !stopping && (progressFrame==null || !progressFrame.isVisible()));
			unlockMe(this);
			mainFrame.setEnabled(r);
		}
	}

	private void stopZODB() {
		stoppingZODB();
		DbController.getInstance().stop();
	}

	private void stoppingZODB() {
		setStopping(true);
		if (mainFrame!=null) {
			mainFrame.setVisible(false);
		}
	}

	private void stoppedZODB() {
		closeAllFrames();
	}

	private void closeAllFrames() {
		if (mainFrame!=null) {
			mainFrame.setVisible(false);
		}
		if (debugFrame!=null) {
			debugFrame.setVisible(false);
		}
		if (progressFrame!=null) {
			progressFrame.setVisible(false);
		}
		if (getTrayIcon().isAdded()) {
			trayIcon.removeTrayIcon();
		}
	}

	private void stopDatabaseClicked() {
		boolean confirmed = getMainFrame().msgConfirmYesNo("Are you sure you want to stop the database?","Stop databse?");
		if (confirmed) {
			stopZODB();
		} else {
			getMainFrame().refreshPanel();
		}
	}

	private void setStatusClicked() {
		if (SvrController.getInstance().isOpen()) {
			boolean confirmed = getMainFrame().msgConfirmYesNo("Are you sure you want to close the server?","Close server?");
			if (confirmed) {
				messageOnNextStatusChange = false;
				SvrController.getInstance().close();
			} else {
				getMainFrame().refreshPanel();
			}
		} else {
			messageOnNextStatusChange = false;
			SvrController.getInstance().open();
		}
	}

	private void startBrowserClicked() {
		if (SvrController.getInstance().startBrowser()) {
			getMainFrame().iconify();
		}
		getMainFrame().refreshPanel();
	}
	
	private void showDebuggerClicked() {
		if (!isStopping()) {
			getDebugFrame().normalize();
		}
		if (mainFrame!=null) {
			mainFrame.refreshPanel();
		}
	}

	private void handleMessageEvent(EvtEvent e) {
		boolean refresh = false;
		if (e.getType().equals(Messenger.MSG_DEBUG)) {
			if (isShowDebugger()) {
				addDebugLogLine(Messenger.getMessageEventString(e));
				refresh = true;
			}
		} else if (
			e.getType().equals(Messenger.MSG_WARNING) ||
			e.getType().equals(Messenger.MSG_ERROR)
			) {
			addDebugLogLine(Messenger.getMessageEventString(e));
			if (!isShowDebugger()) {
				setShowDebugger(true);
			}
			showDebuggerClicked();
			refresh = true;
		}
		if (refresh && debugFrame!=null && debugFrame.isVisible()) {
			debugFrame.refreshPanel();
		}
	}
	
	private void addDebugLogLine(String line) {
		lockMe(this);
		debugLog.append(line);
		debugLog.append("\n");
		unlockMe(this);
	}
	
	private GuiFrameObject getFocusFrame() {
		GuiFrameObject focusFrame = null;
		if (progressFrame!=null && progressFrame.isVisible()) {
			focusFrame = progressFrame;
		} else if (mainFrame!=null && mainFrame.isVisible()) {
			focusFrame = mainFrame;
		} else if (debugFrame!=null && debugFrame.isVisible()) {
			focusFrame = debugFrame;
		}
		return focusFrame;
	}

	private boolean confirmFocusFrame(String msg, String title) {
		boolean confirmed = false;
		GuiFrameObject focusFrame = getFocusFrame();
		if (focusFrame!=null) {
			if (mainFrame!=null && progressFrame!=null && focusFrame==progressFrame) {
				progressFrame.cornerFrameLocation(false,false);
				focusFrame = mainFrame;
			}
			confirmed = focusFrame.msgConfirmYesNo(msg,title);
		}
		return confirmed;
	}
}
