package nl.zeesoft.zdbd;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import nl.zeesoft.zdbd.api.ControllerMonitor;
import nl.zeesoft.zdbd.api.ServerConfig;
import nl.zeesoft.zdbd.gui.IconTray;
import nl.zeesoft.zdbd.midi.MidiSys;
import nl.zeesoft.zdbd.theme.ThemeController;
import nl.zeesoft.zdbd.theme.ThemeControllerSettings;
import nl.zeesoft.zdbd.theme.ThemeSequenceSelector;
import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.http.HttpClient;
import nl.zeesoft.zdk.http.HttpServer;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.Waiter;

public class App implements ActionListener {
	public static final String	QUIT		= "QUIT";
	public static final String	OPEN		= "OPEN";
	
	public static final String	APP_URL		= "http://127.0.0.1:1234/";
	
	protected Lock				lock		= new Lock();
	protected ThemeController	controller	= null;
	protected HttpServer		server		= null;
	protected IconTray			iconTray	= new IconTray();
	
	public App() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			JFrame.setDefaultLookAndFeelDecorated(true);
		} catch (Exception e) {
			// Ignore
		}
	}
	
	public boolean start(ThemeControllerSettings settings) {
		boolean r = false;
		
		lock.lock(this);
		// Create the work directory
		if (FileIO.checkDirectory(settings.workDir).length()>0) {
			FileIO.mkDirs(settings.workDir);
		}
		
		// Create the controller
		controller = new ThemeController();
		// Create the controller monitor
		ControllerMonitor monitor = new ControllerMonitor(controller);
		// Create the sequence selector
		ThemeSequenceSelector selector = new ThemeSequenceSelector();
		selector.setController(controller);
		
		// Create the server configuration
		ServerConfig config = new ServerConfig(controller,monitor,selector);
		config.setPort(1234);
		config.setLogger(Logger.logger);
		config.setFilePath(settings.workDir);
		
		// Create the server
		server = new HttpServer(config);
		
		// Open the server
		Str error = server.open();
		if (error.length()>0) {
			Logger.err(this, error);
		} else {
			// Test the server
			HttpClient client = new HttpClient("GET",APP_URL);
			client.sendRequest();
			int responseCode = client.getResponseCode();
			if (responseCode!=200) {
				error = new Str("Unexpected server response code: ");
				error.sb().append(responseCode);
				Logger.err(this, error);
			} else {
				// Initialize the controller
				CodeRunnerChain chain = controller.initialize(settings);
				monitor.startChain(chain);
				r = Waiter.waitFor(chain, 60000);
				if (MidiSys.isInitialized()) {
					MidiSys.sequencer.addListener(selector);
				}
				if (!r) {
					error = new Str("Failed to start app within 60 seconds");
					Logger.err(this, error);
				}
			}
		}
		
		if (r) {
			iconTray.initialize(this);
			App app = this;
			Runtime.getRuntime().addShutdownHook(new Thread() { 
				public void run() { 
					app.stop(); 
				} 
			});
		}
		lock.unlock(this);
		
		if (!r) {
			stop();
		}
		return r;
	}
	
	public void stop() {
		lock.lock(this);
		if (controller!=null) {
			if (controller.isBusy()) {
				Logger.dbg(this, new Str("Waiting for controller to finish ..."));
				Waiter.waitFor(controller, 30000);
			}
			// Destroy the controller
			CodeRunnerChain chain = controller.destroy();
			Waiter.startAndWaitFor(chain,10000);
			controller = null;
		}
		if (server!=null && server.isOpen()) {
			// Close the server
			Str error = server.close();
			if (error.length()>0) {
				Logger.err(this, error);
			}
			// Wait for connections to close if needed
			Waiter.waitForRunners(server.getActiveRunners(),1000);
			server = null;
		}
		iconTray.destroy();
		lock.unlock(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(QUIT)) {
			handleQuitRequest();
		} else if (e.getActionCommand().equals(OPEN)) {
			handleOpenRequest();
		}
	}
	
	protected void handleQuitRequest() {
		int response = JOptionPane.YES_OPTION;
		if (controller.themeHasChanges()) {
	    	response = JOptionPane.showConfirmDialog(
	    		null,
				"The current theme has changes.\nAre you sure you want to quit?",
				"Quit?",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE
			);
		}
        if (response == JOptionPane.YES_OPTION) {
        	stop();
			System.exit(0);
		}
	}
	
	protected void handleOpenRequest() {
		Desktop desktop = Desktop.getDesktop();
		boolean opened = false;
		if (desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				URL url = new URL(APP_URL);
				desktop.browse(url.toURI());
				opened = true;
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!opened) {
	    	JOptionPane.showMessageDialog(
	    		null,
				"Failed to open browser to: " + APP_URL,
				"Browse failed",
				JOptionPane.ERROR_MESSAGE
			);
		}
	}
}
