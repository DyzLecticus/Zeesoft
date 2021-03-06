package nl.zeesoft.zeetracker.gui.panel;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public abstract class ValueComponent implements ChangeListener, ItemListener {
	private JPanel 		panel		= null;
	private JLabel		label		= null; 
	private JComponent	value		= null;
	private JLabel		propLabel	= null;
	
	public ValueComponent(JLabel label,JComponent value) {
		this.label = label;
		this.value = value;
	}
	
	protected void initialize() {
		label.setText(getStringValue());
		label.setFocusable(false);
		addChangeListener();
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(label);
		panel.add(Box.createRigidArea(new Dimension(5,0)));
		panel.add(value);
	}
	
	protected abstract void addChangeListener();
	
	protected abstract String getStringValue();
	
	public void setVisible(boolean visible) {
		panel.setVisible(visible);
		if (propLabel!=null) {
			propLabel.setVisible(visible);
		}
	}

	@Override
	public void stateChanged(ChangeEvent evt) {
		if (evt.getSource()==value) {
			label.setText(getStringValue());
		}
	}

	@Override
	public void itemStateChanged(ItemEvent evt) {
		if (evt.getSource()==value) {
			label.setText(getStringValue());
		}
	}
	
	public JPanel getPanel() {
		if (panel==null) {
			initialize();
		}
		return panel;
	}

	public JLabel getLabel() {
		return label;
	}

	public void setLabel(JLabel label) {
		this.label = label;
	}

	public JComponent getValue() {
		return value;
	}

	public void setValue(JComponent value) {
		this.value = value;
	}

	public JLabel getPropLabel() {
		return propLabel;
	}

	public void setPropLabel(JLabel propLabel) {
		this.propLabel = propLabel;
	}
}
