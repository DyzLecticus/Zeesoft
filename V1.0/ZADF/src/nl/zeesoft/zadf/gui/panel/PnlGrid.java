package nl.zeesoft.zadf.gui.panel;

import java.awt.GridBagConstraints;

import javax.swing.JSplitPane;

import nl.zeesoft.zadf.gui.GuiGrid;
import nl.zeesoft.zadf.gui.GuiSplitPanel;

public class PnlGrid extends GuiSplitPanel {
	private PnlGridControl				controlPanel	= null;		
	private GuiGrid 					grid			= null;
	
	public PnlGrid(String name,int row,int column,boolean showResults) {
		super(name,row,column,JSplitPane.VERTICAL_SPLIT);
		setAnchor(GridBagConstraints.FIRST_LINE_START);
		setFill(GridBagConstraints.BOTH);
		
		controlPanel = new PnlGridControl("controlPanel",0,0);
		controlPanel.setAnchor(GridBagConstraints.FIRST_LINE_START);
		controlPanel.setFill(GridBagConstraints.BOTH);
		
		grid = new GuiGrid("grid",1,0);
		grid.setAnchor(GridBagConstraints.FIRST_LINE_START);
		grid.setFill(GridBagConstraints.BOTH);
	}
	
	@Override
	public void renderComponent() {
		if (getComponent()==null) {
			this.getPanelObjects().add(controlPanel);
			this.getPanelObjects().add(grid);
			
			super.renderComponent();
		}
	}

	/**
	 * @return the controlPanel
	 */
	public PnlGridControl getControlPanel() {
		return controlPanel;
	}

	/**
	 * @return the grid
	 */
	public GuiGrid getGrid() {
		return grid;
	}
}
