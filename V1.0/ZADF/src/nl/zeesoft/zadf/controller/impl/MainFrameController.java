package nl.zeesoft.zadf.controller.impl;

import java.awt.event.ActionEvent;
import java.util.SortedMap;

import nl.zeesoft.zadf.controller.GuiController;
import nl.zeesoft.zadf.controller.GuiDetailController;
import nl.zeesoft.zadf.controller.GuiTreeController;
import nl.zeesoft.zadf.controller.GuiWindowController;
import nl.zeesoft.zadf.gui.GuiFrame;
import nl.zeesoft.zadf.gui.panel.PnlZoomTree;
import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.model.MdlDataObject;

public class MainFrameController extends GuiWindowController {
	public MainFrameController(GuiFrame mainFrame) {
		super(mainFrame);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = "";
		if (e.getActionCommand()!=null) {
			action = e.getActionCommand();
		}
		
		if (action.equals(PnlZoomTree.ZOOM_IN_BUTTON_CLICKED)) {
			GuiTreeController parentController = (GuiTreeController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.TREE_MAIN_PARENT);
			MainFrameGridController parentGridController = (MainFrameGridController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.GRID_MAIN_PARENT);
			GuiDetailController parentDetailController = (GuiDetailController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.PANEL_MAIN_PARENT_DETAIL);
			
			GuiTreeController childController = (GuiTreeController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.TREE_MAIN_CHILD);
			MainFrameGridController childGridController = (MainFrameGridController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.GRID_MAIN_CHILD);
			GuiDetailController childDetailController = (GuiDetailController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.PANEL_MAIN_CHILD_DETAIL);
			
			DbCollection dbCol = null;
			if (childController.getSelectedProperty()!=null) {
				dbCol = childController.getSelectedProperty().getRefColl();
			}
			if ((dbCol==null) && (childController.getSelectedCollection()!=null)) {
				dbCol = childController.getSelectedCollection();
			}
			if ((dbCol!=null) && (childDetailController.getObject()!=null)) {
				DbCollectionProperty filtProp = childGridController.getFilterProperty();
				MdlDataObject parentObject = childGridController.getParentObject();
				MdlDataObject object = childDetailController.getObject();
				SortedMap<Long, String> extendedReferences = childDetailController.getExtendedReferences(); 
				
				parentController.setShowAsSubTree(true);
				parentController.setCollection(dbCol);
				parentDetailController.setObject(object, extendedReferences);
				parentGridController.setFilterProperty(filtProp,true);
				parentGridController.setParentObject(parentObject);
			}
		} else if (action.equals(PnlZoomTree.ZOOM_OUT_BUTTON_CLICKED)) {
			GuiTreeController parentController = (GuiTreeController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.TREE_MAIN_PARENT);
			MainFrameGridController parentGridController = (MainFrameGridController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.GRID_MAIN_PARENT);
			if (parentController.isShowAsSubTree()) {
				parentGridController.setFilterProperty(null,false);
				parentGridController.setParentObject(null);
				parentController.setShowAsSubTree(false);
				parentController.setCollection(null);
			}
		} else if (action.startsWith(ZADFFactory.MENU_MAIN_SHOW_PREFIX)) {
			publishEvent(new EvtEvent(action,e.getSource(),action));
		} else {
			super.actionPerformed(e);
		}
	}
	
}
