package nl.zeesoft.zadf.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

public class GuiPanel extends GuiPanelAbstract {
	private JPanel 				panel 				= new JPanel();
	private GridBagConstraints	gridBagConstraints	= new GridBagConstraints(); 
	
	public GuiPanel(String name,int row,int column) {
		super(name,row,column);
	}

	@Override
	public void renderComponent() {
		if (getComponent()==null) {
			setComponent(panel);
			if (panel.getLayout() instanceof GridBagLayout) {
				panel.add(getPanelObjects().renderJPanel(),gridBagConstraints);
			} else {
				panel.add(getPanelObjects().renderJPanel());
			}
		}
	}

	/**
	 * @return the panel
	 */
	public JPanel getPanel() {
		return panel;
	}

	/**
	 * @return the gridBagConstraints
	 */
	public GridBagConstraints getGridBagConstraints() {
		return gridBagConstraints;
	}

}
