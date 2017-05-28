package nl.zeesoft.zmmt.gui.panel;

import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class NumberSlider implements ChangeListener {
	private JPanel 				panel	= null;
	private JSpinner			number	= null; 
	private JSlider 			slider	= null;
	
	public NumberSlider(JSpinner number,JSlider slider) {
		this.number = number;
		this.slider = slider;
		number.addChangeListener(this);
		slider.addChangeListener(this);
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(number);
		panel.add(slider);
	}
	
	@Override
	public void stateChanged(ChangeEvent evt) {
		if (evt.getSource()==slider) {
			String fmtVal = String.format("%03d",slider.getValue());
			if (number.getValue()==null || !number.getValue().toString().equals(fmtVal)) {
				number.setValue(fmtVal);
			}
		} else if (evt.getSource()==number && number.getValue()!=null) {
			int value = Integer.parseInt(number.getValue().toString());
			if (value>slider.getMaximum()) {
				number.setValue(slider.getMaximum());
			} else if (value<slider.getMinimum()) {
				number.setValue(slider.getMinimum());
			} else if (slider.getValue()!=value) {
				slider.setValue(value);
			}
		}
	}
	
	public void setEnabled(boolean enabled) {
		number.setEnabled(enabled);
		slider.setEnabled(enabled);
	}

	public JPanel getPanel() {
		return panel;
	}

	public JSpinner getNumber() {
		return number;
	}

	public JSlider getSlider() {
		return slider;
	}
}
