package nl.zeesoft.zmmt.gui.panel;

import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.gui.Settings;
import nl.zeesoft.zmmt.gui.state.StateChangeEvent;
import nl.zeesoft.zmmt.gui.state.StateChangeSubscriber;

public class PanelSettings extends PanelObject implements StateChangeSubscriber {
	private JFormattedTextField		composer			= null;
	private JSpinner				beatsPerMinute		= null;
	private JSpinner				beatsPerBar			= null;
	private JSpinner				stepsPerBeat		= null;
	private JSpinner				barsPerPattern		= null;
	
	private Settings				settingsCopy		= null;

	public PanelSettings(Controller controller) {
		super(controller);
		controller.getStateManager().addSubscriber(this);
		settingsCopy = controller.getStateManager().getSettings().copy();
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
		beatsPerMinute = getNewNumberSpinner(3,1,256);
		JPanel slider = getNewNumberSlider(beatsPerMinute,1,256,128);
		addLabel(getPanel(),row,"Default beats per minute");
		addProperty(getPanel(),row,slider);

		row++;
		beatsPerBar = getNewNumberSpinner(3,1,16);
		slider = getNewNumberSlider(beatsPerBar,1,16,4);
		addLabel(getPanel(),row,"Default beats per bar");
		addProperty(getPanel(),row,slider);

		row++;
		stepsPerBeat = getNewNumberSpinner(3,1,16);
		slider = getNewNumberSlider(stepsPerBeat,1,16,8);
		addLabel(getPanel(),row,"Default steps per beat");
		addProperty(getPanel(),row,slider);

		row++;
		barsPerPattern = getNewNumberSpinner(3,1,16);
		slider = getNewNumberSlider(barsPerPattern,1,16,4);
		addLabel(getPanel(),row,"Default bars per pattern");
		addProperty(getPanel(),row,slider);
		
		updatedSettings();
		
		row++;
		addFiller(getPanel(),row);
		setValidate(true);
	}

	@Override
	public void requestFocus() {
		if (composer.getValue()!=null && composer.getValue().toString().length()>0) {
			getSliderForNumber(beatsPerMinute).getSlider().requestFocus();
		} else {
			composer.requestFocus();
		}
	}

	@Override
	public void handleValidChange() {
		settingsCopy.setComposer(composer.getValue().toString());
		settingsCopy.setDefaultBeatsPerMinute(Integer.parseInt(beatsPerMinute.getValue().toString()));
		settingsCopy.setDefaultBeatsPerBar(Integer.parseInt(beatsPerBar.getValue().toString()));
		settingsCopy.setDefaultStepsPerBeat(Integer.parseInt(stepsPerBeat.getValue().toString()));
		settingsCopy.setDefaultBarsPerPattern(Integer.parseInt(barsPerPattern.getValue().toString()));
		getController().getStateManager().setSettings(this,settingsCopy.copy());
	}
	
	@Override
	public void handleStateChange(StateChangeEvent evt) {
		setValidate(false);
		if (evt.getSource()!=this && evt.getType().equals(StateChangeEvent.CHANGED_SETTINGS)) {
			settingsCopy = settingsCopy.copy();
			updatedSettings();
		}
		setValidate(true);
	}
	
	protected void updatedSettings() {
		composer.setValue(settingsCopy.getComposer());
		beatsPerMinute.setValue(String.format("%03d",settingsCopy.getDefaultBeatsPerMinute()));
		beatsPerBar.setValue(String.format("%03d",settingsCopy.getDefaultBeatsPerBar()));
		stepsPerBeat.setValue(String.format("%03d",settingsCopy.getDefaultStepsPerBeat()));
		barsPerPattern.setValue(String.format("%03d",settingsCopy.getDefaultBarsPerPattern()));
	}
}
