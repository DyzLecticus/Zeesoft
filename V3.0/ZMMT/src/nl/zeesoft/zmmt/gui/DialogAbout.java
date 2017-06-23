package nl.zeesoft.zmmt.gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JDialog;
import javax.swing.JFrame;

import nl.zeesoft.zdk.image.ImageIcon;
import nl.zeesoft.zmmt.gui.panel.PanelAbout;

public class DialogAbout implements ActionListener {
	public static final String		WEBSITE_URL		= "https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZMMT";
	public static final String		LICENSE_URL		= "http://www.dbad-license.org/";
	
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
		if (evt.getActionCommand().equals(WEBSITE_URL)) {
			goToWebsite();
		} else {
			dialog.setVisible(false);
		}
	}
	
	private void initialize() {
		if (dialog==null) {
			dialog = new JDialog(owner,true);
			dialog.setTitle("About");
			dialog.setIconImage(new ImageIcon("z",32,Color.WHITE).getBufferedImage());
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
				Desktop.getDesktop().browse(new URI(WEBSITE_URL));
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
