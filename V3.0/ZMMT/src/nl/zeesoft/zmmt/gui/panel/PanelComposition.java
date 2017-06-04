package nl.zeesoft.zmmt.gui.panel;

import java.awt.GridBagLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.gui.state.CompositionChangePublisher;
import nl.zeesoft.zmmt.gui.state.StateChangeEvent;
import nl.zeesoft.zmmt.gui.state.StateChangeSubscriber;

public class PanelComposition extends PanelObject implements CompositionChangePublisher, StateChangeSubscriber {
	private JFormattedTextField		file				= null;
	private JFormattedTextField		composer			= null;
	private JFormattedTextField		name				= null;
	private JSpinner				beatsPerMinute		= null;
	private JSpinner				beatsPerBar			= null;
	private JSpinner				stepsPerBeat		= null;
	private JSpinner				barsPerPattern		= null;
	
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

		addLabel(getPanel(),row,"File");
		file = getNewTextField();
		file.setEnabled(false);
		addProperty(getPanel(),row,file,true);
		
		row++;
		composer = getNewTextField();
		addLabel(getPanel(),row,"Composer");
		addProperty(getPanel(),row,composer,true);

		row++;
		name = getNewTextField();
		addLabel(getPanel(),row,"Name");
		addProperty(getPanel(),row,name,true);

		row++;
		beatsPerMinute = getNewNumberSpinner(3,1,256);
		JPanel slider = getNewNumberSlider(beatsPerMinute,1,256,128);
		addLabel(getPanel(),row,"Beats per minute");
		addProperty(getPanel(),row,slider);

		row++;
		beatsPerBar = getNewNumberSpinner(3,1,16);
		slider = getNewNumberSlider(beatsPerBar,1,16,4);
		addLabel(getPanel(),row,"Beats per bar");
		addProperty(getPanel(),row,slider);

		row++;
		stepsPerBeat = getNewNumberSpinner(3,1,16);
		slider = getNewNumberSlider(stepsPerBeat,1,16,8);
		addLabel(getPanel(),row,"Steps per beat");
		addProperty(getPanel(),row,slider);

		row++;
		barsPerPattern = getNewNumberSpinner(3,1,16);
		slider = getNewNumberSlider(barsPerPattern,1,16,4);
		addLabel(getPanel(),row,"Bars per pattern");
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
	public void handleStateChange(StateChangeEvent evt) {
		setValidate(false);
		if (evt.getType().equals(StateChangeEvent.CHANGED_COMPOSITION)) {
			composer.setValue(evt.getComposition().getComposer());
			name.setValue(evt.getComposition().getName());
			beatsPerMinute.setValue(String.format("%03d",evt.getComposition().getBeatsPerMinute()));
			beatsPerBar.setValue(String.format("%03d",evt.getComposition().getBeatsPerBar()));
			stepsPerBeat.setValue(String.format("%03d",evt.getComposition().getStepsPerBeat()));
			barsPerPattern.setValue(String.format("%03d",evt.getComposition().getBarsPerPattern()));
		} else if (evt.getType().equals(StateChangeEvent.CHANGED_SETTINGS)) {
			file.setValue((new File(evt.getSettings().getWorkingCompositionFileName())).getAbsolutePath());
		}
		setValidate(true);
	}

	@Override
	public void setChangesInComposition(Composition composition) {
		composition.setComposer(composer.getValue().toString());
		composition.setName(name.getValue().toString());
		composition.setBeatsPerMinute(Integer.parseInt(beatsPerMinute.getValue().toString()));
		composition.setBeatsPerBar(Integer.parseInt(beatsPerBar.getValue().toString()));
		composition.setStepsPerBeat(Integer.parseInt(stepsPerBeat.getValue().toString()));
		composition.setBarsPerPattern(Integer.parseInt(barsPerPattern.getValue().toString()));
	}
}
