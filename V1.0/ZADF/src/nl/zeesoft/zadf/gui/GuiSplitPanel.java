package nl.zeesoft.zadf.gui;

import java.awt.Component;

import javax.swing.JSplitPane;

public class GuiSplitPanel extends GuiPanelAbstract {
	private JSplitPane 			panel 			= null;
	
	public GuiSplitPanel(String name,int row,int column,int split) {
		super(name,row,column);
		panel = new JSplitPane(split);
	}

	@Override
	public void renderComponent() {
		if (getComponent()==null) {
			Component comp1 = null;
			Component comp2 = null;
			
			
			getPanelObjects().calculateWeights();
			if (getPanelObjects().getRows()==2) {
				getPanelObjects().getRowWeights().clear();
				getPanelObjects().getRowWeights().add(1.0D);
				getPanelObjects().getRowWeights().add(1.0D);
			} else if (getPanelObjects().getColumns()==2) {
				getPanelObjects().getColumnWeights().clear();
				getPanelObjects().getColumnWeights().add(1.0D);
				getPanelObjects().getColumnWeights().add(1.0D);
			}
			
			getPanelObjects().renderJPanel();
			if (getPanelObjects().getObjects().size()>=1) {
				comp1 = getPanelObjects().getObjects().get(0).getComponent();
			}
			if (getPanelObjects().getObjects().size()>=2) {
				comp2 = getPanelObjects().getObjects().get(1).getComponent();
			}
			if (panel.getOrientation()==JSplitPane.HORIZONTAL_SPLIT) {
				if (comp1!=null) {
					panel.setLeftComponent(comp1);
				}
				if (comp2!=null) {
					panel.setRightComponent(comp2);
				}
			} else if (panel.getOrientation()==JSplitPane.VERTICAL_SPLIT) {
				if (comp1!=null) {
					panel.setTopComponent(comp1);
				}
				if (comp2!=null) {
					panel.setBottomComponent(comp2);
				}
			}
			setComponent(panel);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (GuiObject guiObj: getPanelObjects().getObjects()) {
			guiObj.setEnabled(enabled);
		}
	}
	
	/**
	 * @return the panel
	 */
	public JSplitPane getPanel() {
		return panel;
	}
}
