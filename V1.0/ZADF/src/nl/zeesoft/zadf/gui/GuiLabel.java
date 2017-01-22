package nl.zeesoft.zadf.gui;

import javax.swing.JLabel;

public class GuiLabel extends GuiPanelObject {
	private JLabel 	label	= new JLabel();
	
	public GuiLabel(String name, int row, int column, String text) {
		super(name,row,column);
		String lbl = text;
		if ((text.contains("\n")) || (text.contains("\t"))) {
			lbl = "<html>";
			lbl = lbl + text.replace("\n", "<br />").replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
			lbl = lbl + "</html>";
		}
		label.setText(lbl);
	}

	@Override
	public void renderComponent() {
		if (getComponent()==null) {
			setComponent(label);
		}
	}
	
	/**
	 * @return the label
	 */
	public JLabel getLabel() {
		return label;
	}

}
