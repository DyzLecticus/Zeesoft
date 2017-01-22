package nl.zeesoft.zadf.gui.panel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import nl.zeesoft.zadf.gui.GuiObject;
import nl.zeesoft.zadf.gui.GuiPanel;

public class PnlDetailGrid extends GuiPanel {
	private PnlDetail	detailPanel		= null;
	private PnlGrid		gridPanel		= null;
	
	public PnlDetailGrid(String name,int row,int column, PnlDetail detailPanel, PnlGrid gridPanel) {
		super(name,row,column);
		this.detailPanel = detailPanel;
		this.gridPanel = gridPanel;
		getPanelObjects().add(detailPanel);
		getPanelObjects().add(gridPanel);
	}

	@Override
	public void renderComponent() {
		super.renderComponent();
		if (detailPanel!=null) {
			showDetail();
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (GuiObject guiObj: getPanelObjects().getObjects()) {
			guiObj.setEnabled(enabled);
		}
	}

	public void showDetail() {
		initializePanel(detailPanel.getComponent());
	}
	
	public void showGrid() {
		gridPanel.getGrid().clearData();
		initializePanel(gridPanel.getComponent());
	}
	
	private void initializePanel(Component component) {
		getPanel().removeAll();
		getPanel().setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 0;
		gbc.ipady = 0;
		getPanel().add(component,gbc);
		getPanel().repaint();
		getPanel().revalidate();
	}
}
