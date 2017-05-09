package nl.zeesoft.zmmt.gui.panel;

import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.gui.state.CompositionChangePublisher;
import nl.zeesoft.zmmt.gui.state.CompositionChangeSubscriber;

public class PanelComposition extends PanelObject implements CompositionChangePublisher, CompositionChangeSubscriber {
	private JFormattedTextField		composer			= null;
	private JFormattedTextField		name				= null;
	private JFormattedTextField		beatsPerMinute		= null;
	private JFormattedTextField		beatsPerBar			= null;
	private JFormattedTextField		stepsPerBeat		= null;
	
	public PanelComposition(Controller controller) {
		super(controller);
		controller.getStateManager().addSubscriber(this);
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
	public void requestFocus() {
		if (name.getValue()!=null && name.getValue().toString().length()>0) {
			getSliderForNumber(beatsPerMinute).getSlider().requestFocus();
		} else {
			name.requestFocus();
		}
	}

	@Override
	public void handleValidChange() {
		getController().getStateManager().addWaitingPublisher(this);
	}
	
	@Override
	public void changedComposition(Object source, Composition composition) {
		setValidate(false);
		composer.setValue(composition.getComposer());
		name.setValue(composition.getName());
		beatsPerMinute.setValue(composition.getBeatsPerMinute());
		beatsPerBar.setValue(composition.getBeatsPerBar());
		stepsPerBeat.setValue(composition.getStepsPerBeat());
		setValidate(true);
	}

	@Override
	public void setChangesInComposition(Composition composition) {
		composition.setComposer(composer.getValue().toString());
		composition.setName(name.getValue().toString());
		composition.setBeatsPerMinute(Integer.parseInt(beatsPerMinute.getValue().toString()));
		composition.setBeatsPerBar(Integer.parseInt(beatsPerBar.getValue().toString()));
		composition.setStepsPerBeat(Integer.parseInt(stepsPerBeat.getValue().toString()));
	}
}
