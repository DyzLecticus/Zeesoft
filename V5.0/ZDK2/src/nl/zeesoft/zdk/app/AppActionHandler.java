package nl.zeesoft.zdk.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppActionHandler implements ActionListener {
	public static String	QUIT				= "QUIT";
	public static String	OPEN				= "OPEN";

	protected App			app					= null;
	
	public boolean			confirmQuit			= true;
	public boolean			openDesktopBrowser	= true;
	public boolean			openMacBrowser		= true;

	public void initialize(App app) {
		this.app = app;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(QUIT)) {
			handleQuitRequest();
		} else if (e.getActionCommand().equals(OPEN)) {
			handleOpenRequest();
		}
	}

	public void handleQuitRequest() {
		boolean quit = confirmQuit();
		if (quit) {
			app.stop();
		}
	}
	
	public void handleOpenRequest() {
		boolean opened = openDesktopBrowser();
		if (!opened) {
			opened = openMacBrowser();
		}
		if (!opened) {
			failedToOpenBrowser();
		}
	}
	
	protected boolean confirmQuit() {
		// Override to implement
        return confirmQuit;
	}
	
	protected void failedToOpenBrowser() {
		// Override to implement
	}
	
	protected boolean openDesktopBrowser() {
		// Override to implement
		return openDesktopBrowser;
	}
	
	protected boolean openMacBrowser() {
		// Override to implement
		return openMacBrowser;
	}
}
