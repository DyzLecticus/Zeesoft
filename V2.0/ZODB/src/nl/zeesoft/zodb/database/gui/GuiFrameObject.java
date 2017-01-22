package nl.zeesoft.zodb.database.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventPublisher;

public abstract class GuiFrameObject extends EvtEventPublisher {
	public final static String 		FRAME_CLOSING 		= "FRAME_CLOSING";
	public final static String 		FRAME_ICONIFIED 	= "FRAME_ICONIFIED";
	public final static String 		FRAME_STATE_CHANGED = "FRAME_STATE_CHANGED";

	private Object					lockedBy			= null;

	private Window					window				= null; 
	private	JDialog					dialog				= null;
	private	JFrame					frame				= null;
	private JPanel					panel				= null;

	private boolean					rendered			= false;
	
	protected GuiFrameObject() {
		final GuiFrameObject self = this;
		final WindowAdapter adapter = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	self.publishEvent(new EvtEvent(FRAME_CLOSING,self,e));
            }
            public void windowIconified(WindowEvent e) {
            	self.publishEvent(new EvtEvent(FRAME_ICONIFIED,self,e));
            }
			public void windowStateChanged(WindowEvent e) {
            	self.publishEvent(new EvtEvent(FRAME_STATE_CHANGED,self,e));
			}
		};

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(adapter);
		frame.addKeyListener(GuiController.getInstance().getKeyListener());

		dialog = new JDialog();
		//dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.addWindowListener(adapter);
		dialog.addKeyListener(GuiController.getInstance().getKeyListener());
		
		window = frame;
	}

	/**
	 * To be called first in constructor if dialog rendering is required
	 * @param d true if dialog rendering is required
	 */
	protected void setRenderDialog(boolean d) {
		if (!rendered) {
			if (d) {
				window = dialog;
			} else {
				window = frame;
			}
		}
	}

	public void setVisible(boolean visible) {
		if (visible && !rendered) {
			render();
		}
		window.setVisible(visible);
		refreshPanel();
	}
	
	protected void render() {
		if (!rendered) {
			if (panel!=null) {
				panel.addKeyListener(GuiController.getInstance().getKeyListener());
				if (frame==window) {
					frame.setContentPane(panel);
				} else {
					dialog.setContentPane(panel);
				}
			}
			if (frame==window) {
				frame.pack();
			} else {
				dialog.pack();
			}
			centreFrameLocation();			
			rendered = true;
		}
	}
	
	protected boolean isVisible() {
		return window.isVisible();
	}

	protected void setLayout(LayoutManager mgr) {
		window.setLayout(mgr);
	}

	protected void setMinimumSize(Dimension minimumSize) {
		window.setMinimumSize(minimumSize);
	}

	protected void setPreferredSize(Dimension minimumSize) {
		window.setPreferredSize(minimumSize);
	}

	protected Container getContentPane() {
		Container c = null;
		if (window==frame) {
			c = frame.getContentPane();
		} else {
			c = dialog.getContentPane();
		}
		return c;
	}
	
	protected void setUndecorated(boolean undecorated) {
		if (window==frame) {
			frame.setUndecorated(undecorated);
		} else {
			dialog.setUndecorated(undecorated);
		}
	}

	protected void setAlwaysOnTop(boolean alwaysOnTop) {
		window.setAlwaysOnTop(alwaysOnTop);
	}

	protected void setEnabled(boolean enabled) {
		window.setEnabled(enabled);
	}

	protected void iconify() {
		if (window==frame) {
			frame.setExtendedState(JFrame.ICONIFIED);
		} else {
			dialog.setVisible(false);
		}
	}

	protected void setIconImage(Image image) {
		window.setIconImage(image);
	}
	
	protected void setResizable(boolean resizable) {
		if (window==frame) {
			frame.setResizable(resizable);
		} else {
			dialog.setResizable(resizable);
		}
	}

	protected void normalize() {
		if (!isVisible()) {
			centreFrameLocation();
			checkFrameSize(null);
			setVisible(true);
		}
		if (window==frame) {
			frame.setExtendedState(JFrame.NORMAL);
		}
		window.toFront();
	}

	protected void setTitle(String title) {
		if (window==frame) {
			frame.setTitle(title);
		} else {
			dialog.setTitle(title);
		}
	}

	protected String getTitle() {
		String t = "";
		if (window==frame) {
			t = frame.getTitle();
		} else {
			t = dialog.getTitle();
		}
		return t;
	}

	protected void centreFrameLocation() {
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		checkFrameSize(screenSize);
	    int x = (int) ((screenSize.getWidth() - window.getWidth()) / 2);
	    int y = (int) ((screenSize.getHeight() - window.getHeight()) / 2);
	    window.setLocation(x, y);
	}

	protected void cornerFrameLocation(boolean left,boolean top) {
		int x = 0;
		int y = 0;
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if (!left) {
			x = (int) (screenSize.getWidth() - window.getWidth());
		}
		if (!top) {
			y = (int) (screenSize.getHeight() - window.getHeight());
		}
	    window.setLocation(x, y);
	}

	protected void positionOverFrame(GuiFrameObject bottom) {
		int x = (bottom.getX() + (bottom.getWidth() / 2)) - (window.getWidth() / 2);
		int y = (bottom.getY() + (bottom.getHeight() / 2)) - (window.getHeight() / 2);
		window.setLocation(x, y);
	}

	protected void maximizeFrameSize(int margin) {
		maximizeFrameSize(null,margin);
	}
	
	protected void refreshPanel() {
		if (rendered && panel!=null) {
			panel.repaint();
			panel.revalidate();
		}
	}
	
	protected boolean msgConfirmYesNo(String msg, String title) {
		return (JOptionPane.showConfirmDialog(frame,msg,title,JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
	}

	protected String msgGetInput(String msg, String title) {
		return (JOptionPane.showInputDialog(frame,msg,title,JOptionPane.OK_CANCEL_OPTION));
	}
	
	protected int getWidth() {
		return window.getWidth();
	}

	protected int getHeight() {
		return window.getHeight();
	}

	protected int getX() {
		return window.getX();
	}

	protected int getY() {
		return window.getY();
	}
	
	protected final synchronized void lockMe(Object source) {
		int attempt = 0;
		while (isLocked()) {
		    try {
                wait();
            } catch (InterruptedException e) { 
            	// Ignore
            }
            attempt ++;
			if (attempt>=1000) {
				Messenger.getInstance().warn(this,Locker.getLockFailedMessage(attempt,source));
				attempt = 0;
			}
		}
		lockedBy = source;
	}

	protected final synchronized void unlockMe(Object source) {
		if (lockedBy==source) {
			lockedBy=null;
			notifyAll();
		}
	}
	
	protected final synchronized boolean isLocked() {
		return (lockedBy!=null);
	}

	protected JPanel getJPanel() {
		return panel;
	}

	protected void setJPanel(JPanel panel) {
		this.panel = panel;
	}

	private void maximizeFrameSize(Dimension screenSize,int margin) {
		if (screenSize==null) {
			screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		}
		window.setPreferredSize(new Dimension(screenSize.width - margin,screenSize.height - margin));
	}

	private void checkFrameSize(Dimension screenSize) {
		if (screenSize==null) {
			screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		}
		boolean change = false;
		int width = window.getSize().width; 
		int height = window.getSize().height; 
	    if (window.getSize().width>screenSize.width) {
	    	width = screenSize.width;
	    	change = true;
	    }
	    if (frame.getSize().height>screenSize.height) {
	    	height = screenSize.height;
	    	change = true;
	    }
	    if (change) {
	    	window.setSize(width, height);
	    }
	}
}
