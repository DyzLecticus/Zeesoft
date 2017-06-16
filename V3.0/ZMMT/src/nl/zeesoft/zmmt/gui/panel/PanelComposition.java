package nl.zeesoft.zmmt.gui.panel;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
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
	private JCheckBox				useDrumKit			= null;
	private JCheckBox				useSynthesizers		= null;
	
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

		file = addLabelTextFieldToPanel(getPanel(),row,"File");
		file.setEnabled(false);
		
		row++;
		composer = addLabelTextFieldToPanel(getPanel(),row,"Composer");
		row++;
		name = addLabelTextFieldToPanel(getPanel(),row,"Name");
		row++;
		beatsPerMinute = addLabelNumberToPanel(getPanel(),row,"Beats per minute",1,256,128);
		row++;
		beatsPerBar = addLabelNumberToPanel(getPanel(),row,"Beats per bar",1,16,4);
		row++;
		stepsPerBeat = addLabelNumberToPanel(getPanel(),row,"Steps per beat",1,16,8);
		row++;
		barsPerPattern = addLabelNumberToPanel(getPanel(),row,"Bars per pattern",1,16,4);
		
		row++;
		useDrumKit = addLabelCheckBoxToPanel(getPanel(),row,"Use internal drum kit");
		useDrumKit.addActionListener(this);
		row++;
		useSynthesizers = addLabelCheckBoxToPanel(getPanel(),row,"Use internal drum synthesizers");
		useSynthesizers.addActionListener(this);
		
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
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getSource()==useDrumKit) {
			handleValidChange();
		} else if (evt.getSource()==useSynthesizers) {
			handleValidChange();
		}
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
			useDrumKit.setSelected(evt.getComposition().getSynthesizerConfiguration().isUseInternalDrumKit());
			useSynthesizers.setSelected(evt.getComposition().getSynthesizerConfiguration().isUseInternalSynthesizers());
		} else if (evt.getType().equals(StateChangeEvent.CHANGED_SETTINGS)) {
			String fileName = evt.getSettings().getWorkingCompositionFileName();
			if (fileName.length()>0) {
				file.setValue((new File(fileName)).getAbsolutePath());
			} else {
				file.setValue("");
			}
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
		composition.getSynthesizerConfiguration().setUseInternalDrumKit(useDrumKit.isSelected());
		composition.getSynthesizerConfiguration().setUseInternalSynthesizers(useSynthesizers.isSelected());
	}
}
