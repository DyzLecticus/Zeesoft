package nl.zeesoft.zadf.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import nl.zeesoft.zadf.controller.GuiController;
import nl.zeesoft.zodb.Messenger;

public abstract class GuiWindowObject extends GuiObject {
	private static final String MSG_INFO			= "MSG_INFO";
	private static final String MSG_ERROR			= "MSG_ERROR";
	
	private Window				window 				= null;
	private GuiPanelObjectList	panelObjects		= new GuiPanelObjectList();
	
	public GuiWindowObject(String name,Window w) {
		super(name);
		window = w;
		window.addWindowFocusListener(GuiController.getInstance());
		
		//Try to set the system look and feel
        try {
        	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        	JFrame.setDefaultLookAndFeelDecorated(true);
        } catch (Exception e) {
        	Messenger.getInstance().error(this, "Unable to set default look and feel: " + e.getMessage());
        }
        ToolTipManager.sharedInstance().setDismissDelay(30000);
	}

	@Override
	public void renderComponent() {
		if (getComponent()==null) {
			setComponent(window);
			JPanel panel = panelObjects.renderJPanel();
			if (window instanceof JWindow) {
				((JWindow) window).setContentPane(panel);
			} else if (window instanceof JFrame) {
				((JFrame) window).setContentPane(panel);
				((JFrame) window).setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			} else if (window instanceof JDialog) {
				((JDialog) window).setContentPane(panel);
				((JDialog) window).setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			}
			window.pack();
		}
	}

	@Override
	public GuiObject getGuiObjectForSourceComponent(Component source) {
		GuiObject object = super.getGuiObjectForSourceComponent(source);
		if (object==null) {
			object = panelObjects.getGuiObjectForSourceComponent(source);
		}
		return object;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (GuiObject guiObj: getPanelObjects().getObjects()) {
			guiObj.setEnabled(enabled);
		}
	}

	public GuiObject getGuiObjectByName(String name) {
		GuiObject object = null;
		if (getName().equals(name)) {
			object = this;
		} else {
			object = panelObjects.getGuiObjectByName(name);
		}
		return object;
	}
	
	public void msgInfo(String msg) {
		showMsg(msg,"Information",MSG_INFO);
	}

	public void msgError(String msg) {
		showMsg(msg,"Error",MSG_ERROR);
	}
	
	public void msgInfo(String msg, String title) {
		showMsg(msg,title,MSG_INFO);
	}

	public void msgError(String msg, String title) {
		showMsg(msg,title,MSG_ERROR);
	}

	public boolean msgConfirmYesNo(String msg, String title) {
		return (JOptionPane.showConfirmDialog(window, formatMessage(msg), title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
	}
	
    private void showMsg(String msg, String title,String type) {
    	if (type.equals(MSG_INFO)) {
            JOptionPane.showMessageDialog(window,msg,title,JOptionPane.INFORMATION_MESSAGE);
        } else if (type.equals(MSG_ERROR)) {
    		JOptionPane.showMessageDialog(window,msg,title,JOptionPane.ERROR_MESSAGE);
        }
    }

	public GuiPanel getNewPanel(String name,int row,int column) {
		GuiPanel panel = new GuiPanel(name,row,column);
		panelObjects.add(panel);
		return panel;
	}

	public void centreFrameLocation() {
	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - window.getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - window.getHeight()) / 2);
	    window.setLocation(x, y);
	}

	public void cornerFrameLocation(boolean top, boolean left) {
	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = 0;
	    int y = 0;

	    if (left) {
	    	x = 0;
	    } else {
	    	x = (int) dimension.getWidth() - window.getWidth();
	    }

	    if (top) {
	    	y = 0;
	    } else {
	    	y = (int) dimension.getHeight() - window.getHeight();
	    }
	    window.setLocation(x, y);
	}
	
	public void maximizeFrameSize() {
	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) dimension.getWidth();
	    int y = (int) dimension.getHeight();
	    int locX = 0;
	    int locY = 0;
	    if (x > 400) {
	    	x = x - 100;
	    	locX = 50;
	    }
	    if (y > 300) {
	    	y = y - 100;
	    	locY = 50;
	    }
	    window.setLocation(locX, locY);
	    window.setSize(x,y);
	    window.setPreferredSize(new Dimension(x,y));
	}

	
	/**
	 * @return the window
	 */
	public Window getWindow() {
		return window;
	}

	/**
	 * @return the panelObjects
	 */
	public GuiPanelObjectList getPanelObjects() {
		return panelObjects;
	}
	
	private String formatMessage(String msg) {
		String fMsg = msg;
		if (!msg.startsWith("<html>")) {
			fMsg = "<html>";
			if (msg.contains("\n")) {
				fMsg = fMsg + msg.replaceAll("\n", "<br/>");
			} else if (msg.contains(". ")) {
				fMsg = fMsg + msg.replaceAll("\\. ", ".<br/>");
			} else {
				fMsg = fMsg + msg;
			}
			fMsg = fMsg + "</html>";
		}
		return fMsg;
	}
}
