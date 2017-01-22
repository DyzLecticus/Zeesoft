package nl.zeesoft.zodb.database.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import nl.zeesoft.zodb.ZODB;

public class GuiDebugFrame extends GuiFrameObject {
	private JPanel					debugPanel				= null;

	private JScrollPane				scrollPanel				= null;
	private JTextArea				debugLog				= null;

	@Override
	protected void render() {
		setTitle("ZODB - Debugger");
		setMinimumSize(new Dimension(400,200));
		setPreferredSize(new Dimension(800,400));
		setLayout(new GridBagLayout());
		setIconImage(ZODB.getIconImage().getBufferedImage());

		GridBagConstraints gbc = null;

		debugPanel = new JPanel();
		debugPanel.setLayout(new GridBagLayout());

		debugLog = GuiController.getNewTextArea(true);
		
		scrollPanel = new JScrollPane(debugLog);
		
		gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.fill = GridBagConstraints.BOTH;
		debugPanel.add(scrollPanel,gbc);
		
		gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(10,10,10,10);
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.fill = GridBagConstraints.BOTH;
		getContentPane().setLayout(new GridBagLayout());
		getContentPane().add(debugPanel,gbc);
		
		super.render();
	}

	@Override
	protected void refreshPanel() {
		if (debugLog!=null) {
			String text = GuiController.getInstance().getDebugLog().toString();
			lockMe(this);
			debugLog.setText(text);
			unlockMe(this);
		}
		super.refreshPanel();
	}
}
