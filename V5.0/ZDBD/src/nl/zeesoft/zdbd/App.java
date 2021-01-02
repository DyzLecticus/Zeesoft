package nl.zeesoft.zdbd;

import nl.zeesoft.zdbd.api.ControllerMonitor;
import nl.zeesoft.zdbd.api.ServerConfig;
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

public class App {
	protected Lock				lock		= new Lock();
	protected ThemeController	controller	= null;
	protected HttpServer		server		= null;
	
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
			HttpClient client = new HttpClient("GET","http://127.0.0.1:8080/");
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
		lock.unlock(this);
	}
}
