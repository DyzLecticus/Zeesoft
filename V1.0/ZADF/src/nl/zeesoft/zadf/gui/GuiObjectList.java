package nl.zeesoft.zadf.gui;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiObjectList {
	private List<GuiObject> objects = new ArrayList<GuiObject>();

	public void add(GuiObject object) {
		objects.add(object);
	}
	
	public GuiObject get(String name) {
		GuiObject obj = null;
		for (GuiObject object: objects) {
			if (object.getName().equals(name)) {
				obj = object;
				break;
			}
		}
		return obj;
	}

	public void clear() {
		objects.clear();
	}
	
	public List<GuiObject> getObjects() {
		return new ArrayList<GuiObject>(objects);
	}
}
