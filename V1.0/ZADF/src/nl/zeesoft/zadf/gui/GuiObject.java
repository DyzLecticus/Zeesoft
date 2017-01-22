package nl.zeesoft.zadf.gui;

import java.awt.Component;

import nl.zeesoft.zodb.event.EvtEventPublisher;

public abstract class GuiObject extends EvtEventPublisher {
	private String		name		= "";
	private Component 	component 	= null;
	
	public GuiObject(String name) {
		this.name = name;
	}
	
	public abstract void renderComponent();
	
	public GuiObject getGuiObjectForSourceComponent(Component source) {
		GuiObject r = null;
		if (getComponent()==source) {
			r = this;
		}
		return r;
	}

	public void setEnabled(boolean enabled) {
		if (component!=null) {
			component.setEnabled(enabled);
		}
	}

	public boolean isEnabled() {
		boolean e = false;
		if (component!=null) {
			e = component.isEnabled();
		}
		return e;
	}

	/**
	 * @return the component
	 */
	public Component getComponent() {
		return component;
	}

	/**
	 * @param component the component to set
	 */
	protected void setComponent(Component component) {
		this.component = component;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
