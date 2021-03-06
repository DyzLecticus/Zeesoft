package nl.zeesoft.zeetracker.gui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JDialog;
import javax.swing.JFrame;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zeetracker.ZeeTracker;
import nl.zeesoft.zeetracker.gui.panel.PanelAbout;

public class DialogAbout implements ActionListener {
	public static final String		GO_TO_WEBSITE	= "GO_TO_WEBSITE";
	
	private Controller				controller		= null;
	private JFrame					owner			= null;
	private JDialog					dialog			= null;
	private PanelAbout				about			= null;
		
	public DialogAbout(Controller controller,JFrame owner) {
		this.controller = controller;
		this.owner = owner;
	}

	public void repositionAndShow() {
		if (dialog==null) {
			initialize();
		}
		if (owner!=null) {
			FrameObject.positionFrameOverFrame(dialog,owner);		
		}
		dialog.setVisible(true);
		about.requestFocus();
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals(GO_TO_WEBSITE)) {
			goToWebsite();
		} else {
			dialog.setVisible(false);
		}
	}
	
	private void initialize() {
		if (dialog==null) {
			ZDKFactory factory = new ZDKFactory();
			dialog = new JDialog(owner,true);
			dialog.setTitle("About ZeeTracker");
			dialog.setIconImage(factory.getZeesoftIcon32().getBufferedImage());
			if (controller!=null) {
				dialog.addKeyListener(controller.getPlayerKeyListener());
				about = new PanelAbout(controller,this);
				about.initialize();
			}
			dialog.add(about.getPanel());
			dialog.pack();
		}
	}
	
	private void goToWebsite() {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(ZeeTracker.WEBSITE_URL));
			} catch (IOException e) {
				if (controller!=null) {
					controller.showErrorMessage(this,"I/O Exception",e);
				}
			} catch (URISyntaxException e) {
				if (controller!=null) {
					controller.showErrorMessage(this,"Invalid URI",e);
				}
			}
			dialog.setVisible(false);
		}
	}
}
