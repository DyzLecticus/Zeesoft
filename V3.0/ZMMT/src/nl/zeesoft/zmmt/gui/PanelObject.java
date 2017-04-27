package nl.zeesoft.zmmt.gui;

import javax.swing.JPanel;

public abstract class PanelObject {
	private JPanel		panel	= new JPanel();
	
	public abstract void initialize();
	
	public JPanel getPanel() {
		return panel;
	}
}
