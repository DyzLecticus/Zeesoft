package nl.zeesoft.zeetracker.gui.panel;

import javax.swing.JComboBox;
import javax.swing.JLabel;

public class LabelComboBox extends ValueComponent {
	private JComboBox<String>	comboBox	= null;
	private int					subtract	= 0;

	public LabelComboBox(JLabel label, JComboBox<String> value,int sub) {
		super(label,value);
		comboBox = value;
		subtract = sub;
	}

	@Override
	protected void addChangeListener() {
		comboBox.addItemListener(this);
	}

	@Override
	protected String getStringValue() {
		return "" + String.format("%03d",(comboBox.getSelectedIndex() - subtract));
	}
}
