package nl.zeesoft.zadf.gui;

import java.awt.Component;

public abstract class GuiPanelAbstract extends GuiPanelObject {
	private GuiPanelObjectList	panelObjects = new GuiPanelObjectList();
	
	public GuiPanelAbstract(String name,int row,int column) {
		super(name,row,column);
	}

	@Override
	public GuiObject getGuiObjectForSourceComponent(Component source) {
		GuiObject object = super.getGuiObjectForSourceComponent(source);
		if (object==null) {
			object = panelObjects.getGuiObjectForSourceComponent(source);
		}
		return object;
	}

	public GuiObject getGuiObjectByName(String name) {
		GuiObject object = null;
		if (getName().equals(name)) {
			object = this;
		} else {
			object = panelObjects.getGuiObjectByName(name);
		}
		return object;
	}

	/**
	 * @return the panelObjects
	 */
	public GuiPanelObjectList getPanelObjects() {
		return panelObjects;
	}

}
