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
		number.addPropertyChangeListener(this);
		comboBox.addItemListener(this);
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(number);
		panel.add(comboBox);
	}
	
	@Override
	public void itemStateChanged(ItemEvent evt) {
		number.setValue(comboBox.getSelectedIndex() - subtract);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (number.getValue()!=null) {
			comboBox.setSelectedIndex(Integer.parseInt(number.getValue().toString()) + subtract);
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
