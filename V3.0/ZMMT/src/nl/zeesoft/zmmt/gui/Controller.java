package nl.zeesoft.zmmt.gui;

import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zmmt.composition.Composition;

public class Controller extends Locker {
	private boolean					exitOnClose	= true;
	
	private WorkerUnion				union		= null;

	private Composition				composition	= null;
	
	private ControllerWindowAdapter	adapter		= null;
	
	private FrameMain				mainFrame	= null;
	
	public Controller() {
		super(new Messenger(null));
	}

	public void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			JFrame.setDefaultLookAndFeelDecorated(true);
		} catch (Exception e) {
			// Ignore
		}
		union = new WorkerUnion(getMessenger());

		adapter = new ControllerWindowAdapter(this);

		composition	= new Composition();

		mainFrame = new FrameMain(adapter);
		mainFrame.initialize();
	}
	
	public void setExitOnClose(boolean exitOnClose) {
		this.exitOnClose = exitOnClose;
	}

	public void start() {
		start(false);
	}
	
	public void start(boolean debug) {
		getMessenger().setPrintDebugMessages(debug);
		getMessenger().start();
		mainFrame.getFrame().setVisible(true);
	}

	public void stop(Worker ignoreWorker) {
		mainFrame.getFrame().setVisible(false);
		getMessenger().stop();
		union.stopWorkers(ignoreWorker);
		getMessenger().whileWorking();
	}

	public void windowClosing(WindowEvent e) {
		if (e.getWindow()==mainFrame.getFrame()) {
			stop(null);
			if (exitOnClose) {
				int status = 0;
				if (getMessenger().isError()) {
					status = 1;
				}
				System.exit(status);
			}
		}
	}
	
	public void windowIconified(WindowEvent e) {
		// Ignore
	}
	
	public void windowStateChanged(WindowEvent e) {
		// Ignore
	}

	public WorkerUnion getUnion() {
		return union;
	}

	public Composition getComposition() {
		return composition;
	}

	public void setComposition(Composition composition) {
		this.composition = composition;
	}
}
