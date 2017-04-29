package nl.zeesoft.zmmt.gui;

import java.awt.GridBagLayout;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;

public class PanelComposition extends PanelObject {
	private JFormattedTextField		composer			= null;
	private JFormattedTextField		name				= null;
	private JFormattedTextField		beatsPerMinute		= null;
	private JFormattedTextField		beatsPerBar			= null;
	private JFormattedTextField		stepsPerBeat		= null;
	
	@Override
	public void initialize(Controller controller,KeyListener keyListener) {
		getPanel().addKeyListener(keyListener);
		getPanel().setLayout(new GridBagLayout());
		getPanel().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		int row = 0;

		composer = new JFormattedTextField();
		addLabel(getPanel(),row,"Composer");
		addProperty(getPanel(),row,composer,true);

		row++;
		name = new JFormattedTextField();
		addLabel(getPanel(),row,"Name");
		addProperty(getPanel(),row,name,true);

		row++;
		beatsPerMinute = getNewNumberTextField(3);
		addLabel(getPanel(),row,"Beats per minute");
		addProperty(getPanel(),row,beatsPerMinute);

		row++;
		beatsPerBar = getNewNumberTextField(2);
		addLabel(getPanel(),row,"Beats per bar");
		addProperty(getPanel(),row,beatsPerBar);

		row++;
		stepsPerBeat = getNewNumberTextField(2);
		addLabel(getPanel(),row,"Steps per beat");
		addProperty(getPanel(),row,stepsPerBeat);
		
		row++;
		addFiller(getPanel(),row);
	}
}
