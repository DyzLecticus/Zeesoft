package nl.zeesoft.zadf.gui.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JScrollPane;

import nl.zeesoft.zadf.gui.GuiButton;
import nl.zeesoft.zadf.gui.GuiObject;
import nl.zeesoft.zadf.gui.GuiPanel;
import nl.zeesoft.zadf.gui.GuiTree;

public class PnlZoomTree extends GuiPanel {
	public static final String	ZOOM_IN_BUTTON_CLICKED 	= "ZOOM_IN_BUTTON_CLICKED";
	public static final String	ZOOM_OUT_BUTTON_CLICKED	= "ZOOM_OUT_BUTTON_CLICKED";
	
	private JScrollPane			scrollPanel				= null;

	private GuiButton			btnZoomIn				= null; 
	private GuiButton			btnZoomOut				= null; 
	
	private GuiTree				tree					= null;
	
	public PnlZoomTree(String name,int row,int column, GuiTree tree) {
		super(name,row,column);
		this.tree = tree;
		scrollPanel = new JScrollPane(getPanel());
		scrollPanel.getVerticalScrollBar().setUnitIncrement(10);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		btnZoomIn.setEnabled(enabled);
		btnZoomOut.setEnabled(enabled);
		for (GuiObject guiObj: getPanelObjects().getObjects()) {
			guiObj.setEnabled(enabled);
		}
	}

	@Override
	public void renderComponent() {
		if (getComponent()==null) {
			getPanel().setLayout(new GridBagLayout());
			getGridBagConstraints().fill = GridBagConstraints.BOTH;
			getGridBagConstraints().anchor = GridBagConstraints.FIRST_LINE_START;
			getGridBagConstraints().weightx = 1;
			getGridBagConstraints().weighty = 1;
			getGridBagConstraints().gridheight = 1;
			getGridBagConstraints().gridwidth = 1;
			getGridBagConstraints().ipadx = 0;
			getGridBagConstraints().ipady = 0;
			
			btnZoomIn = new GuiButton(ZOOM_IN_BUTTON_CLICKED,0,0,"Zoom in");
			btnZoomIn.setAnchor(GridBagConstraints.FIRST_LINE_START);
			btnZoomIn.setFill(GridBagConstraints.NONE);
			btnZoomOut = new GuiButton(ZOOM_OUT_BUTTON_CLICKED,0,1,"Zoom out");
			btnZoomOut.setAnchor(GridBagConstraints.FIRST_LINE_START);
			btnZoomOut.setFill(GridBagConstraints.NONE);

			GuiPanel zoomPanel = new GuiPanel("zoomPanel",0,0) {
				@Override
				public void renderComponent() {
					getPanelObjects().calculateWeights();
					getPanelObjects().getColumnWeights().clear();
					getPanelObjects().getColumnWeights().add(0.001D);
					getPanelObjects().getColumnWeights().add(0.999D);
					super.renderComponent();
				}
				
			};
			zoomPanel.setAnchor(GridBagConstraints.FIRST_LINE_START);
			zoomPanel.setFill(GridBagConstraints.HORIZONTAL);
			zoomPanel.getPanel().setLayout(new GridBagLayout());
			zoomPanel.getGridBagConstraints().fill = GridBagConstraints.HORIZONTAL;
			zoomPanel.getGridBagConstraints().anchor = GridBagConstraints.FIRST_LINE_START;
			zoomPanel.getGridBagConstraints().weightx = 1;
			zoomPanel.getGridBagConstraints().weighty = 1;
			zoomPanel.getGridBagConstraints().gridheight = 1;
			zoomPanel.getGridBagConstraints().gridwidth = 1;
			zoomPanel.getGridBagConstraints().ipadx = 0;
			zoomPanel.getGridBagConstraints().ipady = 0;
			
			zoomPanel.getPanelObjects().add(btnZoomIn);
			zoomPanel.getPanelObjects().add(btnZoomOut);

			tree.setRow(1);
			tree.setColumn(0);
			
			getPanelObjects().add(zoomPanel);
			getPanelObjects().add(tree);
			
			getPanelObjects().calculateWeights();
			getPanelObjects().getRowWeights().clear();
			getPanelObjects().getRowWeights().add(0.001D);
			getPanelObjects().getRowWeights().add(0.999D);
			
			super.renderComponent();
			
			setComponent(scrollPanel);
		}
	}

	/**
	 * @return the scrollPanel
	 */
	public JScrollPane getScrollPanel() {
		return scrollPanel;
	}
}
