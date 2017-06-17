package nl.zeesoft.zmmt.gui.panel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.ListCellRenderer;

import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.gui.state.CompositionChangePublisher;
import nl.zeesoft.zmmt.gui.state.StateChangeEvent;
import nl.zeesoft.zmmt.gui.state.StateChangeSubscriber;
import nl.zeesoft.zmmt.synthesizer.Drum;
import nl.zeesoft.zmmt.synthesizer.DrumConfiguration;
import nl.zeesoft.zmmt.synthesizer.EchoConfiguration;
import nl.zeesoft.zmmt.synthesizer.Instrument;
import nl.zeesoft.zmmt.synthesizer.InstrumentConfiguration;

public class PanelInstruments extends PanelObject implements ItemListener, ListCellRenderer<Object>, CompositionChangePublisher, StateChangeSubscriber {
	private JComboBox<String>		instrument						= null;
	private String					selectedInstrument				= "";
	
	private JPanel					cardPanel						= null;

	private JSlider[]				instrumentHoldPercentage		= new JSlider[Instrument.INSTRUMENTS.length];
	@SuppressWarnings("unchecked")
	private JComboBox<String>[][]	instrumentLayerMidiNum			= new JComboBox[2][Instrument.INSTRUMENTS.length];
	private JSlider[][]				instrumentLayerPressure			= new JSlider[2][Instrument.INSTRUMENTS.length];
	private JSlider[][]				instrumentLayerModulation		= new JSlider[2][Instrument.INSTRUMENTS.length];
	private JSlider[][]				instrumentLayerReverb			= new JSlider[2][Instrument.INSTRUMENTS.length];
	private JSlider[][]				instrumentLayerChorus			= new JSlider[2][Instrument.INSTRUMENTS.length];
	private JSlider[][]				instrumentLayerFilter			= new JSlider[2][Instrument.INSTRUMENTS.length];
	private JSlider[][]				instrumentLayerResonance		= new JSlider[2][Instrument.INSTRUMENTS.length];
	private JSlider[][]				instrumentLayerAttack			= new JSlider[2][Instrument.INSTRUMENTS.length];
	private JSlider[][]				instrumentLayerDecay			= new JSlider[2][Instrument.INSTRUMENTS.length];
	private JSlider[][]				instrumentLayerRelease			= new JSlider[2][Instrument.INSTRUMENTS.length];
	private JSlider[][]				instrumentLayerVibRate			= new JSlider[2][Instrument.INSTRUMENTS.length];
	private JSlider[][]				instrumentLayerVibDepth			= new JSlider[2][Instrument.INSTRUMENTS.length];
	private JSlider[][]				instrumentLayerVibDelay			= new JSlider[2][Instrument.INSTRUMENTS.length];
	private JSlider[][]				instrumentLayerBaseOctave		= new JSlider[2][Instrument.INSTRUMENTS.length];
	private JSlider[][]				instrumentLayerBaseVelocity		= new JSlider[2][Instrument.INSTRUMENTS.length];
	private JSlider[][]				instrumentLayerAccentVelocity	= new JSlider[2][Instrument.INSTRUMENTS.length];
	
	private JSlider[][]				drumLayerMidiNote				= new JSlider[2][Drum.DRUMS.length];
	private JSlider[][]				drumLayerBaseVelocity			= new JSlider[2][Drum.DRUMS.length];
	private JSlider[][]				drumLayerAccentVelocity			= new JSlider[2][Drum.DRUMS.length];

	private JComboBox<String>		echoInstrument					= null;
	private JSlider					echoLayer						= null;
	private JSlider					echoSteps						= null;
	private JSlider					echoVelocityPercentage1			= null;
	private JSlider					echoVelocityPercentage2			= null;
	private JSlider					echoVelocityPercentage3			= null;
	private JSlider					echoReverb1						= null;
	private JSlider					echoReverb2						= null;
	private JSlider					echoReverb3						= null;
	private JSlider					echoPan1						= null;
	private JSlider					echoPan2						= null;
	private JSlider					echoPan3						= null;
	
	public PanelInstruments(Controller controller) {
		super(controller);
		controller.getStateManager().addSubscriber(this);
		selectedInstrument = controller.getStateManager().getSelectedInstrument();
	}

	@Override
	public void initialize() {
		setValidate(false);
		getPanel().addKeyListener(getController().getPlayerKeyListener());
		getPanel().setLayout(new GridBagLayout());
		getPanel().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
				
		int row = 0;

		instrument = getInstrumentSelector();
		instrument.addItemListener(this);
		
		addComponent(getPanel(),row,0.01,instrument,false);
		
		row++;
		cardPanel = getCardPanel();
		addComponent(getPanel(),row,0.01,cardPanel);
		
		row++;
		addFiller(getPanel(),row);
		setValidate(true);
	}

	@Override
	public void requestFocus() {
		getScroller().getVerticalScrollBar().setValue(getScroller().getVerticalScrollBar().getMinimum());
		instrument.requestFocus();
	}

	@Override
	public void handleValidChange() {
		getController().getStateManager().addWaitingPublisher(this);
	}

	@Override
	public void handleStateChange(StateChangeEvent evt) {
		setValidate(false);
		if (!evt.getSource().equals(this) && !selectedInstrument.equals(evt.getSelectedInstrument())) {
			selectedInstrument = evt.getSelectedInstrument();
			instrument.setSelectedIndex(Instrument.getIndexForInstrument(evt.getSelectedInstrument()));
			instrument.setBackground(Instrument.getColorForInstrument(evt.getSelectedInstrument()));
		} else if (evt.getType().equals(StateChangeEvent.CHANGED_COMPOSITION)) {
			EchoConfiguration echo = evt.getComposition().getSynthesizerConfiguration().getEcho();
			for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
				InstrumentConfiguration conf = evt.getComposition().getSynthesizerConfiguration().getInstrument(Instrument.INSTRUMENTS[i]);
				instrumentHoldPercentage[i].setValue(conf.getHoldPercentage());

				int l = 0;
				instrumentLayerMidiNum[l][i].setSelectedIndex(conf.getLayer(l).getMidiNum());
				instrumentLayerPressure[l][i].setValue(conf.getLayer(l).getPressure());
				instrumentLayerModulation[l][i].setValue(conf.getLayer(l).getModulation());
				instrumentLayerReverb[l][i].setValue(conf.getLayer(l).getReverb());
				instrumentLayerChorus[l][i].setValue(conf.getLayer(l).getChorus());
				
				instrumentLayerFilter[l][i].setValue(conf.getLayer(l).getFilter());
				instrumentLayerResonance[l][i].setValue(conf.getLayer(l).getResonance());
				instrumentLayerAttack[l][i].setValue(conf.getLayer(l).getAttack());
				instrumentLayerDecay[l][i].setValue(conf.getLayer(l).getDecay());
				instrumentLayerRelease[l][i].setValue(conf.getLayer(l).getRelease());
				instrumentLayerVibRate[l][i].setValue(conf.getLayer(l).getVibRate());
				instrumentLayerVibDepth[l][i].setValue(conf.getLayer(l).getVibDepth());
				instrumentLayerVibDelay[l][i].setValue(conf.getLayer(l).getVibDelay());
				
				if (!Instrument.INSTRUMENTS[i].equals(Instrument.DRUMS)) {
					instrumentLayerBaseOctave[l][i].setValue(conf.getLayer(l).getBaseOctave());
					instrumentLayerBaseVelocity[l][i].setValue(conf.getLayer(l).getBaseVelocity());
					instrumentLayerAccentVelocity[l][i].setValue(conf.getLayer(l).getAccentVelocity());
				}
				if (Instrument.INSTRUMENTS[i].equals(Instrument.SYNTH_BASS1) ||
					Instrument.INSTRUMENTS[i].equals(Instrument.SYNTH1) ||
					Instrument.INSTRUMENTS[i].equals(Instrument.LEAD) ||
					Instrument.INSTRUMENTS[i].equals(Instrument.STRINGS)
					) {
					l = 1;
					instrumentLayerMidiNum[l][i].setSelectedIndex((conf.getLayer(l).getMidiNum() + 1));
					instrumentLayerPressure[l][i].setValue(conf.getLayer(l).getPressure());
					instrumentLayerModulation[l][i].setValue(conf.getLayer(l).getModulation());
					instrumentLayerReverb[l][i].setValue(conf.getLayer(l).getReverb());
					instrumentLayerChorus[l][i].setValue(conf.getLayer(l).getChorus());
					
					instrumentLayerFilter[l][i].setValue(conf.getLayer(l).getFilter());
					instrumentLayerResonance[l][i].setValue(conf.getLayer(l).getResonance());
					instrumentLayerAttack[l][i].setValue(conf.getLayer(l).getAttack());
					instrumentLayerDecay[l][i].setValue(conf.getLayer(l).getDecay());
					instrumentLayerRelease[l][i].setValue(conf.getLayer(l).getRelease());
					instrumentLayerVibRate[l][i].setValue(conf.getLayer(l).getVibRate());
					instrumentLayerVibDepth[l][i].setValue(conf.getLayer(l).getVibDepth());
					instrumentLayerVibDelay[l][i].setValue(conf.getLayer(l).getVibDelay());
					
					instrumentLayerBaseOctave[l][i].setValue(conf.getLayer(l).getBaseOctave());
					instrumentLayerBaseVelocity[l][i].setValue(conf.getLayer(l).getBaseVelocity());
					instrumentLayerAccentVelocity[l][i].setValue(conf.getLayer(l).getAccentVelocity());
	
					instrumentLayerPressure[l][i].setEnabled(conf.getLayer2().getMidiNum()>=0);
					instrumentLayerModulation[l][i].setEnabled(conf.getLayer2().getMidiNum()>=0);
					instrumentLayerReverb[l][i].setEnabled(conf.getLayer2().getMidiNum()>=0);
					instrumentLayerChorus[l][i].setEnabled(conf.getLayer2().getMidiNum()>=0);

					instrumentLayerFilter[l][i].setEnabled(conf.getLayer2().getMidiNum()>=0);
					instrumentLayerResonance[l][i].setEnabled(conf.getLayer2().getMidiNum()>=0);
					instrumentLayerAttack[l][i].setEnabled(conf.getLayer2().getMidiNum()>=0);
					instrumentLayerDecay[l][i].setEnabled(conf.getLayer2().getMidiNum()>=0);
					instrumentLayerRelease[l][i].setEnabled(conf.getLayer2().getMidiNum()>=0);
					instrumentLayerVibRate[l][i].setEnabled(conf.getLayer2().getMidiNum()>=0);
					instrumentLayerVibDepth[l][i].setEnabled(conf.getLayer2().getMidiNum()>=0);
					instrumentLayerVibDelay[l][i].setEnabled(conf.getLayer2().getMidiNum()>=0);
					
					instrumentLayerBaseOctave[l][i].setEnabled(conf.getLayer2().getMidiNum()>=0);
					instrumentLayerBaseVelocity[l][i].setEnabled(conf.getLayer2().getMidiNum()>=0);
					instrumentLayerAccentVelocity[l][i].setEnabled(conf.getLayer2().getMidiNum()>=0);
				}
				if (Instrument.INSTRUMENTS[i].equals(Instrument.ECHO)) {
					l = 0;
					instrumentHoldPercentage[i].setEnabled(echo.getInstrument().length()==0);
					instrumentLayerMidiNum[l][i].setEnabled(echo.getInstrument().length()==0);
					instrumentLayerPressure[l][i].setEnabled(echo.getInstrument().length()==0);
					instrumentLayerModulation[l][i].setEnabled(echo.getInstrument().length()==0);
					instrumentLayerReverb[l][i].setEnabled(echo.getInstrument().length()==0);
					instrumentLayerChorus[l][i].setEnabled(echo.getInstrument().length()==0);
					
					instrumentLayerFilter[l][i].setEnabled(echo.getInstrument().length()==0);
					instrumentLayerResonance[l][i].setEnabled(echo.getInstrument().length()==0);
					instrumentLayerAttack[l][i].setEnabled(echo.getInstrument().length()==0);
					instrumentLayerDecay[l][i].setEnabled(echo.getInstrument().length()==0);
					instrumentLayerRelease[l][i].setEnabled(echo.getInstrument().length()==0);
					instrumentLayerVibRate[l][i].setEnabled(echo.getInstrument().length()==0);
					instrumentLayerVibDepth[l][i].setEnabled(echo.getInstrument().length()==0);
					instrumentLayerVibDelay[l][i].setEnabled(echo.getInstrument().length()==0);

					instrumentLayerBaseOctave[l][i].setEnabled(echo.getInstrument().length()==0);
					instrumentLayerBaseVelocity[l][i].setEnabled(echo.getInstrument().length()==0);
					instrumentLayerAccentVelocity[l][i].setEnabled(echo.getInstrument().length()==0);
				}
			}
			for (int d = 0; d < Drum.DRUMS.length; d++) {
				DrumConfiguration conf = evt.getComposition().getSynthesizerConfiguration().getDrum(Drum.DRUMS[d]);
				int l = 0;
				drumLayerMidiNote[l][d].setValue(conf.getLayer1MidiNote());
				drumLayerBaseVelocity[l][d].setValue(conf.getLayer1BaseVelocity());
				drumLayerAccentVelocity[l][d].setValue(conf.getLayer1AccentVelocity());
				l = 1;
				drumLayerMidiNote[l][d].setValue(conf.getLayer2MidiNote());
				drumLayerBaseVelocity[l][d].setValue(conf.getLayer2BaseVelocity());
				drumLayerAccentVelocity[l][d].setValue(conf.getLayer2AccentVelocity());
				drumLayerBaseVelocity[l][d].setEnabled(conf.getLayer2MidiNote()>=35);
				drumLayerAccentVelocity[l][d].setEnabled(conf.getLayer2MidiNote()>=35);
			}
			if (echo.getInstrument().length()==0) {
				echoInstrument.setSelectedIndex(0);
				echoLayer.setEnabled(false);
				echoVelocityPercentage3.setEnabled(false);
				echoReverb3.setEnabled(false);
				echoPan3.setEnabled(false);
				echoLayer.setValue(1);
			} else {
				if (echo.getInstrument().equals(Instrument.SYNTH_BASS1) ||
					echo.getInstrument().equals(Instrument.SYNTH1) ||
					echo.getInstrument().equals(Instrument.LEAD) ||
					echo.getInstrument().equals(Instrument.STRINGS)
					) {
					echoLayer.setEnabled(true);
				} else {
					echoLayer.setEnabled(false);
				}
				echoSteps.setEnabled(true);
				echoVelocityPercentage3.setEnabled(true);
				echoReverb3.setEnabled(true);
				echoPan3.setEnabled(true);
				for (int i = 0; i < (Instrument.INSTRUMENTS.length - 1); i++) {
					if (echo.getInstrument().equals(Instrument.INSTRUMENTS[i])) {
						echoInstrument.setSelectedIndex((i + 1));
						break;
					}
				}
				echoLayer.setValue(echo.getLayer());
				echoSteps.setValue(echo.getSteps());
				echoVelocityPercentage1.setValue(echo.getVelocityPercentage1());
				echoVelocityPercentage2.setValue(echo.getVelocityPercentage2());
				echoVelocityPercentage3.setValue(echo.getVelocityPercentage3());
				echoReverb1.setValue(echo.getReverb1());
				echoReverb2.setValue(echo.getReverb2());
				echoReverb3.setValue(echo.getReverb3());
				echoPan1.setValue(echo.getPan1());
				echoPan2.setValue(echo.getPan2());
				echoPan3.setValue(echo.getPan3());
			}
		}
		setValidate(true);
	}

	@Override
	public void setChangesInComposition(Composition composition) {
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			InstrumentConfiguration inst = composition.getSynthesizerConfiguration().getInstrument(Instrument.INSTRUMENTS[i]);
			inst.setHoldPercentage(instrumentHoldPercentage[i].getValue());

			int l = 0;
			inst.getLayer(l).setMidiNum(instrumentLayerMidiNum[l][i].getSelectedIndex());
			inst.getLayer(l).setPressure(instrumentLayerPressure[l][i].getValue());
			inst.getLayer(l).setModulation(instrumentLayerModulation[l][i].getValue());
			inst.getLayer(l).setReverb(instrumentLayerReverb[l][i].getValue());
			inst.getLayer(l).setChorus(instrumentLayerChorus[l][i].getValue());
			
			inst.getLayer(l).setFilter(instrumentLayerFilter[l][i].getValue());
			inst.getLayer(l).setResonance(instrumentLayerResonance[l][i].getValue());
			inst.getLayer(l).setAttack(instrumentLayerAttack[l][i].getValue());
			inst.getLayer(l).setDecay(instrumentLayerDecay[l][i].getValue());
			inst.getLayer(l).setRelease(instrumentLayerRelease[l][i].getValue());
			inst.getLayer(l).setVibRate(instrumentLayerVibRate[l][i].getValue());
			inst.getLayer(l).setVibDepth(instrumentLayerVibDepth[l][i].getValue());
			inst.getLayer(l).setVibDelay(instrumentLayerVibDelay[l][i].getValue());
			
			if (!Instrument.INSTRUMENTS[i].equals(Instrument.DRUMS)) {
				inst.getLayer(l).setBaseOctave(instrumentLayerBaseOctave[l][i].getValue());
				inst.getLayer(l).setBaseVelocity(instrumentLayerBaseVelocity[l][i].getValue());
				inst.getLayer(l).setAccentVelocity(instrumentLayerAccentVelocity[l][i].getValue());
			}
			if (Instrument.INSTRUMENTS[i].equals(Instrument.SYNTH_BASS1) ||
				Instrument.INSTRUMENTS[i].equals(Instrument.SYNTH1) ||
				Instrument.INSTRUMENTS[i].equals(Instrument.LEAD) ||
				Instrument.INSTRUMENTS[i].equals(Instrument.STRINGS)
				) {

				l = 1;
				inst.getLayer(l).setMidiNum((instrumentLayerMidiNum[l][i].getSelectedIndex() - 1));
				inst.getLayer(l).setPressure(instrumentLayerPressure[l][i].getValue());
				inst.getLayer(l).setModulation(instrumentLayerModulation[l][i].getValue());
				inst.getLayer(l).setReverb(instrumentLayerReverb[l][i].getValue());
				inst.getLayer(l).setChorus(instrumentLayerChorus[l][i].getValue());
				
				inst.getLayer(l).setFilter(instrumentLayerFilter[l][i].getValue());
				inst.getLayer(l).setResonance(instrumentLayerResonance[l][i].getValue());
				inst.getLayer(l).setAttack(instrumentLayerAttack[l][i].getValue());
				inst.getLayer(l).setDecay(instrumentLayerDecay[l][i].getValue());
				inst.getLayer(l).setRelease(instrumentLayerRelease[l][i].getValue());
				inst.getLayer(l).setVibRate(instrumentLayerVibRate[l][i].getValue());
				inst.getLayer(l).setVibDepth(instrumentLayerVibDepth[l][i].getValue());
				inst.getLayer(l).setVibDelay(instrumentLayerVibDelay[l][i].getValue());
				
				inst.getLayer(l).setBaseOctave(instrumentLayerBaseOctave[l][i].getValue());
				inst.getLayer(l).setBaseVelocity(instrumentLayerBaseVelocity[l][i].getValue());
				inst.getLayer(l).setAccentVelocity(instrumentLayerAccentVelocity[l][i].getValue());
			}
		}
		for (int d = 0; d < Drum.DRUMS.length; d++) {
			DrumConfiguration drum = composition.getSynthesizerConfiguration().getDrum(Drum.DRUMS[d]);
			int l = 0;
			drum.setLayer1MidiNote(drumLayerMidiNote[l][d].getValue());
			drum.setLayer1BaseVelocity(drumLayerBaseVelocity[l][d].getValue());
			drum.setLayer1AccentVelocity(drumLayerAccentVelocity[l][d].getValue());
			l = 1;
			drum.setLayer2MidiNote(drumLayerMidiNote[l][d].getValue());
			drum.setLayer2BaseVelocity(drumLayerBaseVelocity[l][d].getValue());
			drum.setLayer2AccentVelocity(drumLayerAccentVelocity[l][d].getValue());
		}
		EchoConfiguration echo = composition.getSynthesizerConfiguration().getEcho();
		String instrument = echoInstrument.getSelectedItem().toString();
		if (echoInstrument.getSelectedIndex()==0) {
			instrument = "";
		}
		echo.setInstrument(instrument);
		if (echo.getInstrument().equals(Instrument.SYNTH_BASS1) ||
			echo.getInstrument().equals(Instrument.SYNTH1) ||
			echo.getInstrument().equals(Instrument.LEAD) ||
			echo.getInstrument().equals(Instrument.STRINGS)
			) {
			echo.setLayer(echoLayer.getValue());
		} else {
			echo.setLayer(1);
		}
		echo.setSteps(echoSteps.getValue());
		echo.setVelocityPercentage1(echoVelocityPercentage1.getValue());
		echo.setVelocityPercentage2(echoVelocityPercentage2.getValue());
		echo.setVelocityPercentage3(echoVelocityPercentage3.getValue());
		echo.setReverb1(echoReverb1.getValue());
		echo.setReverb2(echoReverb2.getValue());
		echo.setReverb3(echoReverb3.getValue());
		echo.setPan1(echoPan1.getValue());
		echo.setPan2(echoPan2.getValue());
		echo.setPan3(echoPan3.getValue());
	}

	@Override
	public void itemStateChanged(ItemEvent evt) {
		if (evt.getSource()==instrument) {
			getScroller().getVerticalScrollBar().setValue(getScroller().getVerticalScrollBar().getMinimum());
			CardLayout cl = (CardLayout)(cardPanel.getLayout());
			cl.show(cardPanel,(String) evt.getItem());
		} else if (evt.getSource()==echoInstrument) {
			handlePropertyChanged(evt.getSource());
		}
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (!selectedInstrument.equals(instrument.getSelectedItem().toString())) {
			selectedInstrument = instrument.getSelectedItem().toString();
			instrument.setBackground(Instrument.getColorForInstrument(selectedInstrument));
			getController().getStateManager().setSelectedInstrument(this,selectedInstrument);
		}
	}

	@Override
	public Component getListCellRendererComponent(
		@SuppressWarnings("rawtypes") final JList list,final Object value,int index,boolean isSelected,boolean hasFocus) {
		String instrument = "" + value;
		JLabel label = new JLabel(instrument);
		label.setFocusable(false);
		Color color = Instrument.getColorForInstrument(instrument);
		label.setOpaque(true);
		if (isSelected) {
			label.setBackground(list.getSelectionBackground());
			label.setForeground(list.getSelectionForeground());
			label.setBorder(BorderFactory.createDashedBorder(Color.BLACK));
		} else {
			label.setBackground(color);
			if (hasFocus) {
				label.setBorder(BorderFactory.createDashedBorder(Color.BLACK));
			} else {
				label.setBorder(BorderFactory.createLineBorder(color));
			}
		}
		return label;
	}

	protected JPanel getCardPanel() {
		JPanel panel = new JPanel();
		CardLayout layout = new CardLayout();
		panel.setLayout(layout);
		
		List<String> midiInstruments = new ArrayList<String>();
		Synthesizer synth = null;
		try {
			synth = MidiSystem.getSynthesizer();
		} catch (MidiUnavailableException e) {
			// Ignore
		}
		if (synth!=null) {
			javax.sound.midi.Instrument[] instruments = synth.getAvailableInstruments();
			for (int i = 0; i < instruments.length; i++) {
				midiInstruments.add(String.format("%03d",i) + " " + instruments[i].getName());
				if (midiInstruments.size()>=128) {
					break;
				}
			}
		} else {
			for (int i = 0; i < 128; i++) {
				midiInstruments.add(String.format("%03d",i));
			}
		}
		
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			panel.add(getInstrumentPanel(i,midiInstruments),Instrument.INSTRUMENTS[i]);
		}
		
		layout.show(panel,(String) instrument.getSelectedItem());
		return panel;
	}
	
	protected JPanel getInstrumentPanel(int instrumentNum,List<String> midiInstruments) {
		String name = Instrument.INSTRUMENTS[instrumentNum];

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		int row = 0;

		instrumentHoldPercentage[instrumentNum] = addLabelSliderToPanel(panel,row,"Hold percentage",0,100,75);

		int l = 0;
		row++;
		String label = "MIDI instrument number";
		if (name.equals(Instrument.SYNTH_BASS1) ||
			name.equals(Instrument.SYNTH1) ||
			name.equals(Instrument.LEAD) ||
			name.equals(Instrument.STRINGS)
			) {
			label = label + " 1";
		}
		instrumentLayerMidiNum[l][instrumentNum] = addLabelComboBox(panel,row,label,midiInstruments,this,0); 
		if (name.equals(Instrument.DRUMS)) {
			instrumentLayerMidiNum[l][instrumentNum].setEnabled(false);
		}

		row++;
		instrumentLayerPressure[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Pressure",0,127,0);
		row++;
		instrumentLayerModulation[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Modulation",0,127,0);
		row++;
		instrumentLayerReverb[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Reverb",0,127,24);
		row++;
		instrumentLayerChorus[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Chorus",0,127,0);

		row++;
		instrumentLayerFilter[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Filter",0,127,127);
		row++;
		instrumentLayerResonance[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Resonance",0,127,127);
		row++;
		instrumentLayerAttack[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Attack",0,127,127);
		row++;
		instrumentLayerDecay[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Decay",0,127,127);
		row++;
		instrumentLayerRelease[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Release",0,127,127);
		row++;
		instrumentLayerVibRate[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Vibrato rate",0,127,127);
		row++;
		instrumentLayerVibDepth[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Vibrato depth",0,127,127);
		row++;
		instrumentLayerVibDelay[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Vibrato delay",0,127,127);

		if (!name.equals(Instrument.DRUMS)) {
			row++;
			instrumentLayerBaseOctave[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Base octave",0,9,3);
			row++;
			instrumentLayerBaseVelocity[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Base velocity",0,127,100);
			row++;
			instrumentLayerAccentVelocity[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Accent velocity",0,127,110);
		}
		
		if (name.equals(Instrument.SYNTH_BASS1) ||
			name.equals(Instrument.SYNTH1) ||
			name.equals(Instrument.LEAD) ||
			name.equals(Instrument.STRINGS)
			) {
			l = 1;
			row++;
			label = "MIDI instrument number";
			if (name.equals(Instrument.SYNTH_BASS1) ||
				name.equals(Instrument.SYNTH1) ||
				name.equals(Instrument.LEAD) ||
				name.equals(Instrument.STRINGS)
				) {
				label = label + " 2";
			}
			List<String> midiInst = new ArrayList<String>(midiInstruments);
			midiInst.add(0,"");
			instrumentLayerMidiNum[l][instrumentNum] = addLabelComboBox(panel,row,label,midiInst,this,1); 
			if (name.equals(Instrument.DRUMS)) {
				instrumentLayerMidiNum[l][instrumentNum].setEnabled(false);
			}

			row++;
			instrumentLayerPressure[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Pressure",0,127,0);
			row++;
			instrumentLayerModulation[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Modulation",0,127,0);
			row++;
			instrumentLayerReverb[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Reverb",0,127,24);
			row++;
			instrumentLayerChorus[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Chorus",0,127,0);

			row++;
			instrumentLayerFilter[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Filter",0,127,127);
			row++;
			instrumentLayerResonance[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Resonance",0,127,127);
			row++;
			instrumentLayerAttack[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Attack",0,127,127);
			row++;
			instrumentLayerDecay[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Decay",0,127,127);
			row++;
			instrumentLayerRelease[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Release",0,127,127);
			row++;
			instrumentLayerVibRate[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Vibrato rate",0,127,127);
			row++;
			instrumentLayerVibDepth[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Vibrato depth",0,127,127);
			row++;
			instrumentLayerVibDelay[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Vibrato delay",0,127,127);

			row++;
			instrumentLayerBaseOctave[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Base octave",0,9,3);
			row++;
			instrumentLayerBaseVelocity[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Base velocity",0,127,100);
			row++;
			instrumentLayerAccentVelocity[l][instrumentNum] = addLabelSliderToPanel(panel,row,"Accent velocity",0,127,110);
		}

		if (name.equals(Instrument.DRUMS)) {
			for (int d = 0; d < Drum.DRUMS.length; d++) {
				int drow = 0;
				JPanel drumPanel = new JPanel();
				drumPanel.setLayout(new GridBagLayout());
				drumPanel.setBorder(BorderFactory.createTitledBorder(Drum.DRUMS[d]));

				drumLayerMidiNote[0][d] = addLabelSliderToPanel(drumPanel,drow,"MIDI note number 1",35,81,35);
				drow++;
				drumLayerBaseVelocity[0][d] = addLabelSliderToPanel(drumPanel,drow,"Base velocity",0,127,100);
				drow++;
				drumLayerAccentVelocity[0][d] = addLabelSliderToPanel(drumPanel,drow,"Accent velocity",0,127,110);

				drow++;
				drumLayerMidiNote[1][d] = addLabelSliderToPanel(drumPanel,drow,"MIDI note number 2",34,81,34);
				drow++;
				drumLayerBaseVelocity[1][d] = addLabelSliderToPanel(drumPanel,drow,"Base velocity",0,127,100);
				drow++;
				drumLayerAccentVelocity[1][d] = addLabelSliderToPanel(drumPanel,drow,"Accent velocity",0,127,110);

				row++;
				addComponent(panel,row,0.01,drumPanel,false);
			}
		}
		
		if (name.equals(Instrument.ECHO)) {
			JPanel echoPanel = new JPanel();
			echoPanel.setLayout(new GridBagLayout());
			echoPanel.setBorder(BorderFactory.createTitledBorder("Echo"));
			
			int erow = 0;
			echoInstrument = new JComboBox<String>();
			echoInstrument.addItem("[Self]");
			echoInstrument.addItemListener(this);
			for (l = 0; l < echoInstrument.getKeyListeners().length; l++) {
				echoInstrument.removeKeyListener(echoInstrument.getKeyListeners()[l]);
			}
			echoInstrument.addFocusListener(this);
			echoInstrument.addKeyListener(getController().getPlayerKeyListener());
			addFunctionKeyOverridesToComponent(echoInstrument);
			for (int i = 0; i < (Instrument.INSTRUMENTS.length - 2); i++) {
				if (!Instrument.INSTRUMENTS[i].equals(Instrument.DRUMS)) {
					echoInstrument.addItem(Instrument.INSTRUMENTS[i]);
				}
			}
			addLabelProperty(echoPanel,erow,"Source instrument",echoInstrument);
			
			erow++;
			echoLayer = addLabelSliderToPanel(echoPanel,erow,"Source layer",1,2,1);
			erow++;
			echoSteps = addLabelSliderToPanel(echoPanel,erow,"Steps",1,24,6);

			erow++;
			echoPan1 = addLabelSliderToPanel(echoPanel,erow,"Pan 1",0,127,24);
			erow++;
			echoPan2 = addLabelSliderToPanel(echoPanel,erow,"Pan 2",0,127,104);
			erow++;
			echoPan3 = addLabelSliderToPanel(echoPanel,erow,"Pan 3",0,127,48);

			erow++;
			echoVelocityPercentage1 = addLabelSliderToPanel(echoPanel,erow,"Velocity percentage 1",1,99,80);
			erow++;
			echoVelocityPercentage2 = addLabelSliderToPanel(echoPanel,erow,"Velocity percentage 2",1,99,60);
			erow++;
			echoVelocityPercentage3 = addLabelSliderToPanel(echoPanel,erow,"Velocity percentage 3",1,99,40);
			
			erow++;
			echoReverb1 = addLabelSliderToPanel(echoPanel,erow,"Reverb 1",0,127,80);
			erow++;
			echoReverb2 = addLabelSliderToPanel(echoPanel,erow,"Reverb 2",0,127,104);
			erow++;
			echoReverb3 = addLabelSliderToPanel(echoPanel,erow,"Reverb",0,127,127);

			row++;
			addComponent(panel,row,0.01,echoPanel,false);
		}
		
		row++;
		addFiller(panel,row);
		
		return panel;
	}

	protected JComboBox<String> getInstrumentSelector() {
		JComboBox<String> r = new JComboBox<String>();
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			r.addItem(Instrument.INSTRUMENTS[i]);
		}
		r.setSelectedIndex(Instrument.getIndexForInstrument(selectedInstrument));
		r.addActionListener(this);
		r.setOpaque(true);
		r.setBackground(Instrument.getColorForInstrument(selectedInstrument));
		r.setRenderer(this);
		
		addFunctionKeyOverridesToComponent(r);
		addControlPageUpDownOverridesToComponent(r);

		for (int l = 0; l < r.getKeyListeners().length; l++) {
			r.removeKeyListener(r.getKeyListeners()[l]);
		}
		r.addKeyListener(getController().getPlayerKeyListener());
		return r;
	}
}
