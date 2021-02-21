package nl.zeesoft.zdbd.gui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionListener;

import nl.zeesoft.zdbd.App;
import nl.zeesoft.zdbd.test.ZDBD;
import nl.zeesoft.zdk.image.ImageIcon;

public class IconTray {
	private Image		image	= null;
	private SystemTray	tray	= null;
	private TrayIcon	icon	= null;
	private PopupMenu	popup	= null;

	public void initialize(ActionListener listener) {
		if (SystemTray.isSupported()) {
			tray = SystemTray.getSystemTray();
			image = ImageIcon.getZeesoftIcon(32).getBufferedImage();
			icon = new TrayIcon(image);
			icon.setToolTip(ZDBD.NAME);
			icon.setImageAutoSize(true);
			try {
				tray.add(icon);
				
				popup = new PopupMenu();
				MenuItem item = new MenuItem("Quit");
				item.setActionCommand(App.QUIT);
				item.addActionListener(listener);
				popup.add(item);
				item = new MenuItem("Open browser");
				item.setActionCommand(App.OPEN);
				item.addActionListener(listener);
				popup.add(item);
				icon.setPopupMenu(popup);
			} catch (AWTException e) {
				e.printStackTrace();
				tray = null;
				image = null;
				icon = null;
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
		image = null;
		icon = null;
	}

	public void displayInfoMessage(String caption, String text) {
		displayMessage(caption, text, TrayIcon.MessageType.INFO);
	}
	
	public void displayMessage(String caption, String text, MessageType messageType) {
		if (icon!=null) {
			icon.displayMessage(caption, text, messageType);
		}
	}
}
