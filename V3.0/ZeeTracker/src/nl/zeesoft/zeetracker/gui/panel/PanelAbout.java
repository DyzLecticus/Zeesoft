package nl.zeesoft.zeetracker.gui.panel;

import java.awt.Desktop;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nl.zeesoft.zeetracker.ZeeTracker;
import nl.zeesoft.zeetracker.gui.Controller;
import nl.zeesoft.zeetracker.gui.DialogAbout;

public class PanelAbout extends PanelObject {
	private ActionListener	listener	= null;
	private JButton			close		= null;
	
	public PanelAbout(Controller controller,ActionListener listener) {
		super(controller);
		this.listener = listener;
	}

	@Override
	public void requestFocus() {
		close.requestFocus();
	}

	@Override
	public void initialize() {
		setValidate(false);
		getPanel().addKeyListener(getController().getPlayerKeyListener());
		getPanel().setLayout(new GridBagLayout());
		getPanel().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		int row = 0;

		JLabel version = new JLabel(ZeeTracker.VERSION);
		version.setFocusable(false);
		addLabelProperty(getPanel(), row,"Version ",version);
		
		row++;
		String e = Character.toString((char)233);
		JLabel createdBy = new JLabel("Andr" + e + " van der Zee, Leiden, The Netherlands");
		createdBy.setFocusable(false);
		addLabelProperty(getPanel(), row,"Created by ",createdBy);

		row++;
		JLabel license = new JLabel(DialogAbout.LICENSE_URL);
		license.setFocusable(false);
		addLabelProperty(getPanel(), row,"License ",license);
		
		row++;
		JLabel source = new JLabel(DialogAbout.WEBSITE_URL);
		source.setFocusable(false);
		addLabelProperty(getPanel(), row,"Website ",source);

		row++;
		JPanel buttons = new JPanel();
		buttons.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
		buttons.setLayout(new BoxLayout(buttons,BoxLayout.X_AXIS));
		close = new JButton("close");
		close.addActionListener(listener);
		close.addFocusListener(this);
		close.addKeyListener(getController().getPlayerKeyListener());
		buttons.add(close);
		if (Desktop.isDesktopSupported()) {
			JButton goToSite = new JButton("Go to website");
			goToSite.addActionListener(listener);
			goToSite.setActionCommand(DialogAbout.WEBSITE_URL);
			goToSite.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			goToSite.addFocusListener(this);
			goToSite.addKeyListener(getController().getPlayerKeyListener());
			buttons.add(goToSite);
		}
		addProperty(getPanel(),row,buttons);
		
		row++;
		addFiller(getPanel(), row);
	}
}
