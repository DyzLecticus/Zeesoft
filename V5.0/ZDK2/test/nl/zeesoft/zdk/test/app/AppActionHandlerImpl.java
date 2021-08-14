package nl.zeesoft.zdk.test.app;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JOptionPane;

import nl.zeesoft.zdk.app.AppActionHandler;

public class AppActionHandlerImpl extends AppActionHandler {
	@Override
	protected boolean confirmQuit() {
		boolean r = false;
		String question = "Are you sure you want to quit?";
    	int response = JOptionPane.showConfirmDialog(null,question,app.getName(),
    		JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE
		);
        if (response == JOptionPane.YES_OPTION) {
        	r = true;
        }
        return r;
	}
	
	@Override
	protected void failedToOpenBrowser() {
    	JOptionPane.showMessageDialog(
    		null,
			"Failed to open browser to: " + app.getSelfUrl(),
			app.getName(),
			JOptionPane.ERROR_MESSAGE
		);
	}
	
	@Override
	protected boolean openDesktopBrowser() {
		boolean r = false;
		Desktop desktop = Desktop.getDesktop();
		if (desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				URL url = new URL(app.getSelfUrl());
				desktop.browse(url.toURI());
				r = true;
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return r;
	}
	
	@Override
	protected boolean openMacBrowser() {
		boolean r = false;
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("mac")>=0) {
			Runtime rt = Runtime.getRuntime();
			try {
				String[] exec = new String[2];
				exec[0] = "open";
				exec[1] = app.getSelfUrl();
				rt.exec(exec);
				r = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return r;
	}
}
