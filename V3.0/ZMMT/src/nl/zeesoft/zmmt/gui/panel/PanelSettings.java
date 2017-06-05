package nl.zeesoft.zmmt.gui.panel;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class PanelSettings extends PanelObject implements StateChangeSubscriber, ActionListener {
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

		addLabel(getPanel(),row,"File");
		JFormattedTextField file = getNewTextField();
		file.setValue((new File(settingsCopy.getFileName())).getAbsolutePath());
		file.setEnabled(false);
		addProperty(getPanel(),row,file,true);

		row++;
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

		row++;
		addLabel(getPanel(),row,"Instrument defaults");
		addProperty(getPanel(),row,getInstrumentPanel());

		row++;
		customSoundFont = getNewTextField();
		addLabel(getPanel(),row,"Custom sound font file");
		addProperty(getPanel(),row,customSoundFont,true);

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
		if (evt.getActionCommand().equals(SAVE_INSTRUMENTS)) {
			boolean confirmed = getController().showConfirmMessage("Are you sure you want to save the current instruments defaults");
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
		restoreInstruments = new JButton("Restore");
		restoreInstruments.setActionCommand(RESTORE_INSTRUMENTS);
		restoreInstruments.addActionListener(this);
		panel.add(saveInstruments,BorderLayout.LINE_START);
		panel.add(restoreInstruments,BorderLayout.CENTER);
		return panel;
	}
}
