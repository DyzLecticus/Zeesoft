package nl.zeesoft.zmmt.gui.panel;

import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class NumberSlider implements ChangeListener,  PropertyChangeListener {
	private JPanel 				panel	= null;
	private JFormattedTextField number	= null; 
	private JSlider 			slider	= null;
	
	public NumberSlider(JFormattedTextField number,JSlider slider) {
		this.number = number;
		this.slider = slider;
		number.addPropertyChangeListener(this);
		slider.addChangeListener(this);
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(number);
		panel.add(slider);
	}
	
	@Override
	public void stateChanged(ChangeEvent evt) {
		number.setValue(slider.getValue());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (number.getValue()!=null) {
			slider.setValue(Integer.parseInt(number.getValue().toString()));
		}
	}
	
	public void setEnabled(boolean enabled) {
		number.setEnabled(enabled);
		slider.setEnabled(enabled);
	}

	public JPanel getPanel() {
		return panel;
	}

	public JFormattedTextField getNumber() {
		return number;
	}

	public JSlider getSlider() {
		return slider;
	}
}
