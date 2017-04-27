package nl.zeesoft.zmmt.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelComposition extends PanelObject {
	private JFormattedTextField		composer			= null;
	private JFormattedTextField		name				= null;
	private JFormattedTextField		beatsPerMinute		= null;
	private JFormattedTextField		beatsPerBar			= null;
	private JFormattedTextField		stepsPerBeat		= null;
	
	@Override
	public void initialize() {
		getPanel().setLayout(new GridBagLayout());
		//getPanel().setPreferredSize(new Dimension(300,200));
		getPanel().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		int row = 0;
		NumberFormat fmt = NumberFormat.getIntegerInstance(Locale.US);
		fmt.setGroupingUsed(false);
		fmt.setMaximumIntegerDigits(2);

		composer = new JFormattedTextField();
		addLabel(getPanel(),row,"Composer");
		addProperty(getPanel(),row,composer);

		row++;
		name = new JFormattedTextField();
		addLabel(getPanel(),row,"Name");
		addProperty(getPanel(),row,name);

		row++;
		beatsPerMinute = new JFormattedTextField(fmt);
		addLabel(getPanel(),row,"Beats per minute");
		addProperty(getPanel(),row,beatsPerMinute);

		row++;
		beatsPerBar = new JFormattedTextField(fmt);
		addLabel(getPanel(),row,"Beats per bar");
		addProperty(getPanel(),row,beatsPerBar);

		row++;
		stepsPerBeat = new JFormattedTextField(fmt);
		addLabel(getPanel(),row,"Steps per beat");
		addProperty(getPanel(),row,stepsPerBeat);
		
		row++;
		addFiller(getPanel(),row);
	}

	protected void addLabel(JPanel panel,int row,String text) {
		JLabel lbl = new JLabel(text);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.3;
		gbc.gridx = 0;
		gbc.gridy = row;
		panel.add(lbl,gbc);
	}

	protected void addProperty(JPanel panel,int row,Component c) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.7;
		gbc.gridx = 1;
		gbc.gridy = row;
		panel.add(c,gbc);
	}
	
	protected void addFiller(JPanel panel,int row) {
		JPanel filler = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.PAGE_END;
		gbc.weightx = 1.0;
		gbc.weighty = 0.99;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = row;
		panel.add(filler,gbc);
	}
}
