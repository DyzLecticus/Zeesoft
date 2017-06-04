package nl.zeesoft.zmmt.gui.panel;

import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class NumberComboBox implements ItemListener, ChangeListener {
	private JPanel 				panel		= null;
	private JSpinner			number		= null; 
	private JComboBox<String>	comboBox	= null;
	private int					subtract	= 0;
	
	public NumberComboBox(JSpinner number,JComboBox<String> comboBox,int subtract) {
		this.number = number;
		this.comboBox = comboBox;
		this.subtract = subtract;
		int index = Integer.parseInt(number.getValue().toString()) + subtract;
		comboBox.setSelectedIndex(index);
		number.addChangeListener(this);
		comboBox.addItemListener(this);
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(number);
		panel.add(comboBox);
	}
	
	@Override
	public void itemStateChanged(ItemEvent evt) {
		String fmtVal = String.format("%03d",(comboBox.getSelectedIndex() - subtract));
		if (number.getValue()==null || !number.getValue().toString().equals(fmtVal)) {
			number.setValue(fmtVal);
		}
	}

	@Override
	public void stateChanged(ChangeEvent evt) {
		if (number.getValue()!=null) {
			int index = Integer.parseInt(number.getValue().toString()) + subtract;
			if (index>=comboBox.getItemCount()) {
				index = (comboBox.getItemCount() - 1);
				number.setValue((index - subtract));
			} else if (index<0) {
				index = 0;
				number.setValue((index - subtract));
			} else if (comboBox.getSelectedIndex()!=index) {
				comboBox.setSelectedIndex(index);
			}
		}
	}
	
	public void setEnabled(boolean enabled) {
		number.setEnabled(enabled);
		comboBox.setEnabled(enabled);
	}

	public JPanel getPanel() {
		return panel;
	}

	public JSpinner getNumber() {
		return number;
	}

	public JComboBox<String> getComboBox() {
		return comboBox;
	}
}
