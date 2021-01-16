package nl.zeesoft.zodb.database.gui;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import nl.zeesoft.zodb.Messenger;

public class GuiTrayIcon {
	private	Image		image	= null;
	private SystemTray	tray	= null;
	private TrayIcon	icon	= null;
	private boolean		added	= false;

	public TrayIcon(Image image) {
	}

	protected void addTrayIcon() {
		if (added) {
			return;
		}
		if (tray==null && SystemTray.isSupported()) {
			tray = SystemTray.getSystemTray();
		}
		if (tray!=null) {
			if (icon==null) {
				initialize();
			}
			try {
				refreshTrayIcon();
				tray.add(icon);
				added = true;
			} catch (AWTException e) {
				Messenger.getInstance().debug(this,"Unable to add icon to system tray: " + e.getMessage());
			}
		}
	}
	
	protected void refreshTrayIcon() {
		icon.setImage(GuiController.getInstance().getStatusIcon());
		icon.setToolTip(GuiController.SERVER_STATUS_LABEL + ": " + GuiController.getInstance().getStatusText());
	}

	protected void removeTrayIcon() {
		if (!added || icon==null) {
			return;
		}
		if (tray!=null) {
			tray.remove(icon);
			added = false;
		}
	}

	protected void displayInfoMessage(String caption, String text) {
		displayMessage(caption, text, TrayIcon.MessageType.INFO);
	}
	
	protected void displayMessage(String caption, String text, MessageType messageType) {
		if (icon!=null) {
			icon.displayMessage(caption, text, messageType);
		}
	}

	/**
	 * @return the added
	 */
	protected boolean isAdded() {
		return added;
	}
	
	private void initialize() {
		icon = new TrayIcon(GuiController.getInstance().getStatusIcon());
		icon.setImageAutoSize(true);
		icon.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				GuiController.getInstance().initializeMainFrame(true);
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});
	}
}
