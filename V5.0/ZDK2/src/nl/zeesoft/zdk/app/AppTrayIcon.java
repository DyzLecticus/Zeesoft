package nl.zeesoft.zdk.app;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.UIManager;

import nl.zeesoft.zdk.TextIcon;

public class AppTrayIcon {
	private SystemTray	tray	= null;
	private TrayIcon	icon	= null;

	public void initialize(ActionListener listener,String appName) {
		if (SystemTray.isSupported()) {
			setSystemLookAndFeel(false);
			createTrayIcon(appName);
			try {
				tray.add(icon);
				initializePopupMenu(listener);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
	}
	
	public PopupMenu getPopupMenu() {
		PopupMenu r = null;
		if (icon!=null) {
			r = icon.getPopupMenu();
		}
		return r;
	}
	
	public void destroy() {
		if (tray!=null && icon!=null) {
			tray.remove(icon);
		}
		tray = null;
		icon = null;
	}
	
	public void displayMessage(String caption, String text, MessageType messageType) {
		if (icon!=null) {
			icon.displayMessage(caption, text, messageType);
		}
	}
	
	public static void setSystemLookAndFeel(boolean mockException) {
		try {
			if (mockException) {
				throw new Exception("Mock exception");
			}
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// Ignore
		}
		JFrame.setDefaultLookAndFeelDecorated(true);
	}

	protected void createTrayIcon(String appName) {
		tray = SystemTray.getSystemTray();
		TextIcon textIcon = new TextIcon();
		textIcon.renderPanel();
		icon = new TrayIcon(textIcon.getBufferedImage());
		icon.setToolTip(appName);
		icon.setImageAutoSize(true);
	}
	
	protected void initializePopupMenu(ActionListener listener) {
		PopupMenu popup = new PopupMenu();
		MenuItem item = new MenuItem("Quit");
		item.setActionCommand(AppActionHandler.QUIT);
		item.addActionListener(listener);
		popup.add(item);
		item = new MenuItem("Open browser");
		item.setActionCommand(AppActionHandler.OPEN);
		item.addActionListener(listener);
		popup.add(item);
		icon.setPopupMenu(popup);
	}
}
