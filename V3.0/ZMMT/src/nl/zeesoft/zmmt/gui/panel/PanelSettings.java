package nl.zeesoft.zmmt.gui.panel;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.gui.Settings;
import nl.zeesoft.zmmt.gui.state.StateChangeEvent;
import nl.zeesoft.zmmt.gui.state.StateChangeSubscriber;
import nl.zeesoft.zmmt.synthesizer.SynthesizerConfiguration;

public class PanelSettings extends PanelObject implements StateChangeSubscriber {
	private static final String			SAVE_INSTRUMENTS			= "SAVE_INSTRUMENTS";
	private static final String			RESTORE_INSTRUMENTS			= "RESTORE_INSTRUMENTS";
	
	private JFormattedTextField			composer					= null;
	private JSpinner					beatsPerMinute				= null;
	private JSpinner					beatsPerBar					= null;
	private JSpinner					stepsPerBeat				= null;
	private JSpinner					barsPerPattern				= null;
	
	private JButton						saveInstruments				= null;
	private JButton						restoreInstruments			= null;

	private JFormattedTextField			customSoundFont				= null;

	private Settings					settingsCopy				= null;
	private	SynthesizerConfiguration	synthCopy					= null;

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

		JFormattedTextField file = addLabelTextFieldToPanel(getPanel(),row,"File");
		file.setValue((new File(settingsCopy.getFileName())).getAbsolutePath());
		file.setEnabled(false);

		row++;
		composer = addLabelTextFieldToPanel(getPanel(),row,"Composer");
		row++;
		beatsPerMinute = addLabelNumberToPanel(getPanel(),row,"Beats per minute",1,256,128);
		row++;
		beatsPerBar = addLabelNumberToPanel(getPanel(),row,"Beats per bar",1,16,4);
		row++;
		stepsPerBeat = addLabelNumberToPanel(getPanel(),row,"Steps per beat",1,16,8);
		row++;
		barsPerPattern = addLabelNumberToPanel(getPanel(),row,"Bars per pattern",1,16,4);
		
		row++;
		addLabelProperty(getPanel(),row,"Instrument defaults",getInstrumentPanel());

		row++;
		customSoundFont = addLabelTextFieldToPanel(getPanel(),row,"Custom sound font file");

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
		settingsCopy.setCustomSoundFontFileName(customSoundFont.getValue().toString());
		getController().getStateManager().setSettings(this,settingsCopy.copy());
	}
	
	@Override
	public void handleStateChange(StateChangeEvent evt) {
		setValidate(false);
		if (evt.getSource()!=this && evt.getType().equals(StateChangeEvent.CHANGED_COMPOSITION)) {
			synthCopy = evt.getComposition().getSynthesizerConfiguration().copy();
		} else if (evt.getSource()!=this && evt.getType().equals(StateChangeEvent.CHANGED_SETTINGS)) {
			settingsCopy = evt.getSettings().copy();
			synthCopy = settingsCopy.getSynthesizerConfiguration();
			updatedSettings();
		}
		setValidate(true);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getActionCommand().equals(SAVE_INSTRUMENTS)) {
			boolean confirmed = getController().showConfirmMessage("Are you sure you want to save the current instruments as defaults");
			if (confirmed) {
				settingsCopy.setSynthesizerConfiguration(synthCopy);
				getController().getStateManager().setSettings(this,settingsCopy.copy());
			}
		} else if (evt.getActionCommand().equals(RESTORE_INSTRUMENTS)) {
			boolean confirmed = getController().showConfirmMessage("Are you sure you want to restore the default instruments");
			if (confirmed) {
				settingsCopy.setSynthesizerConfiguration(new SynthesizerConfiguration());
				getController().getStateManager().setSettings(this,settingsCopy.copy());
			}
		}
	}
	
	protected void updatedSettings() {
		composer.setValue(settingsCopy.getComposer());
		beatsPerMinute.setValue(String.format("%03d",settingsCopy.getDefaultBeatsPerMinute()));
		beatsPerBar.setValue(String.format("%03d",settingsCopy.getDefaultBeatsPerBar()));
		stepsPerBeat.setValue(String.format("%03d",settingsCopy.getDefaultStepsPerBeat()));
		barsPerPattern.setValue(String.format("%03d",settingsCopy.getDefaultBarsPerPattern()));
		customSoundFont.setValue(settingsCopy.getCustomSoundFontFileName());
	}
	
	protected JPanel getInstrumentPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		saveInstruments = new JButton("Save");
		saveInstruments.setActionCommand(SAVE_INSTRUMENTS);
		saveInstruments.addActionListener(this);
		saveInstruments.addKeyListener(getController().getPlayerKeyListener());
		restoreInstruments = new JButton("Restore");
		restoreInstruments.setActionCommand(RESTORE_INSTRUMENTS);
		restoreInstruments.addActionListener(this);
		restoreInstruments.addKeyListener(getController().getPlayerKeyListener());
		panel.add(saveInstruments,BorderLayout.LINE_START);
		panel.add(restoreInstruments,BorderLayout.CENTER);
		return panel;
	}
}
