package nl.zeesoft.zeetracker.gui.panel;

import javax.swing.JLabel;
import javax.swing.JSlider;

public class LabelSlider extends ValueComponent {
	private JSlider	slider	= null;

	public LabelSlider(JLabel label, JSlider value) {
		super(label,value);
		slider = value;
	}

	@Override
	protected void addChangeListener() {
		slider.addChangeListener(this);
	}

	@Override
	protected String getStringValue() {
		return "" + String.format("%03d",slider.getValue());
	}
}
