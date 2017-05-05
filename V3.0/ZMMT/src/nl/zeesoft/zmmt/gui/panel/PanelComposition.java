package nl.zeesoft.zmmt.gui.panel;

import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.gui.CompositionUpdater;
import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.gui.FrameMain;

public class PanelComposition extends PanelObject implements CompositionUpdater {
	private JFormattedTextField		composer			= null;
	private JFormattedTextField		name				= null;
	private JFormattedTextField		beatsPerMinute		= null;
	private JFormattedTextField		beatsPerBar			= null;
	private JFormattedTextField		stepsPerBeat		= null;
	
	public PanelComposition(Controller controller) {
		super(controller);
	}

	@Override
	public void initialize() {
		setValidate(false);
		getPanel().addKeyListener(getController().getPlayerKeyListener());
		getPanel().setLayout(new GridBagLayout());
		getPanel().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		int row = 0;

		composer = getNewTextField();
		addLabel(getPanel(),row,"Composer");
		addProperty(getPanel(),row,composer,true);

		row++;
		name = getNewTextField();
		addLabel(getPanel(),row,"Name");
		addProperty(getPanel(),row,name,true);

		row++;
		beatsPerMinute = getNewNumberTextField(3);
		JPanel slider = getNewNumberSlider(beatsPerMinute,1,256,128);
		addLabel(getPanel(),row,"Beats per minute");
		addProperty(getPanel(),row,slider);

		row++;
		beatsPerBar = getNewNumberTextField(2);
		slider = getNewNumberSlider(beatsPerBar,1,16,4);
		addLabel(getPanel(),row,"Beats per bar");
		addProperty(getPanel(),row,slider);

		row++;
		stepsPerBeat = getNewNumberTextField(2);
		slider = getNewNumberSlider(stepsPerBeat,1,16,8);
		addLabel(getPanel(),row,"Steps per beat");
		addProperty(getPanel(),row,slider);
		
		row++;
		addFiller(getPanel(),row);
		setValidate(true);
	}

	@Override
	public String validate() {
		// Nothing to validate
		return "";
	}

	@Override
	public void handleValidChange() {
		getController().changedComposition(FrameMain.COMPOSITION);
	}

	@Override
	public void requestFocus() {
		name.requestFocus();
	}
	
	@Override
	public void updatedComposition(String tab,Composition comp) {
		setValidate(false);
		composer.setValue(comp.getComposer());
		name.setValue(comp.getName());
		beatsPerMinute.setValue(comp.getBeatsPerMinute());
		beatsPerBar.setValue(comp.getBeatsPerBar());
		stepsPerBeat.setValue(comp.getStepsPerBeat());
		setValidate(true);
	}

	@Override
	public void getCompositionUpdate(String tab, Composition comp) {
		comp.setComposer(composer.getValue().toString());
		comp.setName(name.getValue().toString());
		comp.setBeatsPerMinute(Integer.parseInt(beatsPerMinute.getValue().toString()));
		comp.setBeatsPerBar(Integer.parseInt(beatsPerBar.getValue().toString()));
		comp.setStepsPerBeat(Integer.parseInt(stepsPerBeat.getValue().toString()));
	}
}
