package nl.zeesoft.zadf.controller.impl;

import java.awt.event.ActionEvent;

import nl.zeesoft.zadf.controller.GuiController;
import nl.zeesoft.zadf.controller.GuiTreeController;
import nl.zeesoft.zadf.controller.GuiWindowController;
import nl.zeesoft.zadf.gui.GuiFrame;

public class ModelFrameController extends GuiWindowController {
	public ModelFrameController(GuiFrame mainFrame) {
		super(mainFrame);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = "";
		if (e.getActionCommand()!=null) {
			action = e.getActionCommand();
		}
		
		if (action.equals(ACTION_CLOSE_FRAME)) {
			super.actionPerformed(e);
		} else if (action.equals(ZADFFactory.MENU_MODEL_SHOW_ENTITY_MODEL)) {
			getGuiTreeController().setShowEntityModel(!getGuiTreeController().isShowEntityModel());
		}
	}
	
	public void initialize() {
		getGuiTreeController().setMaximumDepth(10);
	}
	
	private GuiTreeController getGuiTreeController() {
		return (GuiTreeController) GuiController.getInstance().getGuiObjectControllerByObjectName(ZADFFactory.TREE_MODEL);
	}
	
}
