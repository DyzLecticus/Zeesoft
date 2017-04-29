package nl.zeesoft.zmmt.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.KeyListener;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class PanelObject {
	private JPanel		panel	= new JPanel();
	
	public abstract void initialize(Controller controller,KeyListener keyListener);
	
	public JPanel getPanel() {
		return panel;
	}

	protected void addLabel(JPanel panel,int row,String text) {
		JLabel lbl = new JLabel(text + " ");
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.01;
		gbc.gridx = 0;
		gbc.gridy = row;
		panel.add(lbl,gbc);
	}

	protected void addProperty(JPanel panel,int row,Component c) {
		addProperty(panel,row,c,false);
	}
	
	protected void addProperty(JPanel panel,int row,Component c,boolean fill) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		if (fill) {
			gbc.fill = GridBagConstraints.HORIZONTAL;
		}
		gbc.weightx = 0.99;
		gbc.gridx = 1;
		gbc.gridy = row;
		panel.add(c,gbc);
	}

	protected void addFiller(JPanel panel,int row) {
		addComponent(panel,row,0.99,new JPanel());
	}

	protected void addComponent(JPanel panel,int row,double weighty,Component c) {
		addComponent(panel,row,weighty,c,true);
	}

	protected void addComponent(JPanel panel,int row,double weighty,Component c,boolean fill) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		if (fill) {
			gbc.fill = GridBagConstraints.HORIZONTAL;
		}
		gbc.weightx = 1.0;
		gbc.weighty = weighty;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = row;
		panel.add(c,gbc);
	}
	
	protected JFormattedTextField getNewNumberTextField(int digits) {
		NumberFormat fmt = NumberFormat.getIntegerInstance(Locale.US);
		fmt.setGroupingUsed(false);
		fmt.setMaximumIntegerDigits(digits);
		JFormattedTextField r = new JFormattedTextField(fmt);
		r.setPreferredSize(new Dimension(100,20));
		return r;
	}

	protected JPanel getNewNumberSlider(JFormattedTextField number,int min,int max) {
		JPanel r = new JPanel();
		
		r.add(number);
		return r;
	}
}
