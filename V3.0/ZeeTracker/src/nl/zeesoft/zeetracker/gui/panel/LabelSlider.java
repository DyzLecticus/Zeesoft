package nl.zeesoft.zeetracker.gui.panel;

import javax.swing.JLabel;
import javax.swing.JSlider;

public class LabelSlider extends ValueComponent {
	private JSlider	slider	= null;
	private int		divider = 1;

	public LabelSlider(JLabel label, JSlider value,int div) {
		super(label,value);
		slider = value;
		divider = div;
	}

	@Override
	protected void addChangeListener() {
		slider.addChangeListener(this);
	}

	@Override
	protected String getStringValue() {
		String r = "";
		if (divider==1) {
			r =  String.format("%03d",slider.getValue());
		} else {
			r = String.format("%.1f",((float)slider.getValue() / divider));
		}
		return r;
	}
}
