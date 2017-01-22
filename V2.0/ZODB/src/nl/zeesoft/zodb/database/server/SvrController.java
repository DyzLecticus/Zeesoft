package nl.zeesoft.zodb.database.server;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.gui.GuiController;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventSubscriber;
import nl.zeesoft.zodb.file.FileIO;

/**
 * This is the main server controller
 * 
 * It delegates most of its actions to the server it instantiates using the server configuration
 * Provides thread safe methods to read and write files in the server data directory
 */
public final class SvrController extends Locker {
	public static final String		URL_PREFIX		= "http://"; 
	public static final String		URL_LOCALHOST	= URL_PREFIX + "localhost"; 
	
	private static SvrController	controller		= null;
	private	boolean					open			= false;
	private List<String>			lockedFiles		= new ArrayList<String>();
	
	// Expose information for server classes
	private	boolean					justInstalled	= false;
	private boolean					debug			= false;

	// Expose information non server classes
	private String					externalUrl		= null;
	
	private SvrController() {
		// Singleton
	}
	
	public static SvrController getInstance() {
		if (controller==null) {
			controller = new SvrController();
			controller.initialize();
		}
		return controller;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}

	public boolean open() {
		boolean error = false;
		boolean opened = false; 
		if (!SvrConfig.getInstance().fileExists()) {
			if (!install()) {
				error = true;
			} else {
				justInstalled = true;
			}
		}
		if (!error) {
			SvrSocketLogger.getInstance().addSubscriber(new EvtEventSubscriber() {
				@Override
				public void handleEvent(EvtEvent e) {
					// Ignore
				}
			});
			SvrSocketLogger.getInstance().start();
			debug = DbConfig.getInstance().isDebug();
			SvrConfig.getInstance().unserialize();
			lockMe(this);
			opened = SvrConfig.getInstance().getServer().open();
			open = opened;
			unlockMe(this);
			if (!opened) {
				SvrSocketLogger.getInstance().stop();
			}
		}
		if (opened) {
			GuiController.getInstance().serverStatusChanged();
		}
		return opened;
	}

	public boolean close() {
		boolean closed = false;
		lockMe(this);
		closed = SvrConfig.getInstance().getServer().close();
		open = !closed;
		unlockMe(this);
		if (closed) {
			GuiController.getInstance().serverStatusChanged();
			SvrSocketLogger.getInstance().stop();
		}
		return closed;
	}
	
	public boolean install() {
		SvrConfig.getInstance().serialize();
		return SvrConfig.getInstance().getServer().install();
	}

	public boolean isOpen() {
		boolean r = false;
		lockMe(this);
		r = open;
		unlockMe(this);
		return r;
	}

	public String getUrl(boolean internal) {
		String url = "";
		url = URL_LOCALHOST + ":" + SvrConfig.getInstance().getPort();
		if (!internal && externalUrl!=null) {
			url = externalUrl;
		}
		return url;
	}

	public boolean startBrowser() {
		return Generic.startBrowserAtUrl(getUrl(true));
	}
	
	public boolean writeImage(String fileName, RenderedImage img) {
		fileName = toDataDirFileName(fileName);
		boolean success = false;
		lockFile(fileName);
		try {
			ImageIO.write(img, "PNG", new File(fileName));
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		unlockFile(fileName);
		return success;
	}

	public boolean writeFile(String fileName, StringBuilder text) {
		fileName = toDataDirFileName(fileName);
		boolean success = false;
		FileIO file = new FileIO();
		lockFile(fileName);
		success = file.writeFile(fileName, text);
		unlockFile(fileName);
		return success;
	}

	public byte[] readImage(String fileName) {
		fileName = toDataDirFileName(fileName);
		byte[] img = null;
		lockFile(fileName);
		img = FileIO.getImageAsByteArray(fileName);
		unlockFile(fileName);
		return img;
	}
	
	public StringBuilder readFile(String fileName) {
		fileName = toDataDirFileName(fileName);
		StringBuilder text = null;
		FileIO file = new FileIO();
		lockFile(fileName);
		text = file.readFile(fileName);
		unlockFile(fileName);
		return text;
	}
	
	protected boolean isJustInstalled() {
		return justInstalled;
	}

	protected boolean isDebug() {
		return debug;
	}
	
	private String toDataDirFileName(String fileName) {
		if (!fileName.startsWith(SvrConfig.getInstance().getDataDir())) {
			fileName = SvrConfig.getInstance().getDataDir() + fileName;
		}
		return fileName;
	}
	
	private synchronized void lockFile(String fileName) {
		int attempt = 0;
		while (fileIsLocked(fileName)) {
		    try {
                wait();
            } catch (InterruptedException e) { 
            	// Ignore
            }
            attempt ++;
			if (attempt>=1000) {
				Messenger.getInstance().warn(this,getLockFailedMessage(attempt,this));
				attempt = 0;
			}
		}
		lockedFiles.add(fileName);
	}

	private synchronized void unlockFile(String fileName) {
		if (fileIsLocked(fileName)) {
			lockedFiles.remove(fileName);
			notifyAll();
		}
	}
	
	private synchronized boolean fileIsLocked(String fileName) {
		return lockedFiles.contains(fileName);
	}

	private void initialize() {
		SvrConfig.getInstance().getServer();
		if (externalUrl==null) {
			try {
				InetAddress localhost = InetAddress.getLocalHost();
				externalUrl = URL_PREFIX + localhost.getCanonicalHostName() + ":" + SvrConfig.getInstance().getPort();
			} catch (UnknownHostException e) {
				Messenger.getInstance().warn(this,"Unable to determine local host IP address: " + e + "\n" + Generic.getCallStackString(e.getStackTrace(),""));
			}
		}
	}
}
