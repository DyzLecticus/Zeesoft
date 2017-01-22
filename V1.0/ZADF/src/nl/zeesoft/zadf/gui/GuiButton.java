package nl.zeesoft.zadf.gui;

import javax.swing.JButton;

import nl.zeesoft.zadf.controller.GuiController;

public class GuiButton extends GuiPanelObject {
	private JButton	button	= new JButton();
	
	public GuiButton(String name, int row, int column, String text) {
		super(name,row,column);
		button.setText(text);
	}

	@Override
	public void renderComponent() {
		if (getComponent()==null) {
			setComponent(button);
			button.setActionCommand(getName());
			button.addActionListener(GuiController.getInstance());
		}
	}
	
	/**
	 * @return the button
	 */
	public JButton getButton() {
		return button;
	}

}
