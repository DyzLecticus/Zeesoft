package nl.zeesoft.zmmt.gui.panel;

import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

public class NumberComboBox implements ItemListener, PropertyChangeListener {
	private JPanel 				panel		= null;
	private JFormattedTextField number		= null; 
	private JComboBox<String>	comboBox	= null;
	private int					subtract	= 0;
	
	public NumberComboBox(JFormattedTextField number,JComboBox<String> comboBox,int subtract) {
		this.number = number;
		this.comboBox = comboBox;
		this.subtract = subtract;
		number.addPropertyChangeListener(this);
		comboBox.addItemListener(this);
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(number);
		panel.add(comboBox);
	}
	
	@Override
	public void itemStateChanged(ItemEvent evt) {
		String newValue = "" + (comboBox.getSelectedIndex() - subtract);
		if (number.getValue()==null || !number.getValue().toString().equals(newValue)) {
			number.setValue((comboBox.getSelectedIndex() - subtract));
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
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

	public JFormattedTextField getNumber() {
		return number;
	}

	public JComboBox<String> getComboBox() {
		return comboBox;
	}
}