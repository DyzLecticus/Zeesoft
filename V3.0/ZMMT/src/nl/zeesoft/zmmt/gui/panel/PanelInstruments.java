package nl.zeesoft.zmmt.gui.panel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;

import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.gui.FrameMain;
import nl.zeesoft.zmmt.gui.state.CompositionChangePublisher;
import nl.zeesoft.zmmt.gui.state.StateChangeEvent;
import nl.zeesoft.zmmt.gui.state.StateChangeSubscriber;
import nl.zeesoft.zmmt.synthesizer.Drum;
import nl.zeesoft.zmmt.synthesizer.DrumConfiguration;
import nl.zeesoft.zmmt.synthesizer.EchoConfiguration;
import nl.zeesoft.zmmt.synthesizer.Instrument;
import nl.zeesoft.zmmt.synthesizer.InstrumentConfiguration;

public class PanelInstruments extends PanelObject implements ItemListener, ActionListener, ListCellRenderer<Object>, CompositionChangePublisher, StateChangeSubscriber {
	private JComboBox<String>		instrument						= null;
	private String					selectedInstrument				= "";
	
	private JPanel					cardPanel						= null;

	private JSpinner[]				instrumentLayer1MidiNum			= new JSpinner[Instrument.INSTRUMENTS.length];
	private JSpinner[]				instrumentLayer1Volume			= new JSpinner[Instrument.INSTRUMENTS.length];
	private JSpinner[]				instrumentLayer1Filter			= new JSpinner[Instrument.INSTRUMENTS.length];
	private JSpinner[]				instrumentLayer1Chorus			= new JSpinner[Instrument.INSTRUMENTS.length];
	private JSpinner[]				instrumentLayer1Pressure		= new JSpinner[Instrument.INSTRUMENTS.length];
	private JSpinner[]				instrumentLayer1Pan				= new JSpinner[Instrument.INSTRUMENTS.length];
	private JSpinner[]				instrumentLayer1Reverb			= new JSpinner[Instrument.INSTRUMENTS.length];
	private JSpinner[]				instrumentLayer1Modulation		= new JSpinner[Instrument.INSTRUMENTS.length];
	private JSpinner[]				instrumentLayer1BaseOctave		= new JSpinner[Instrument.INSTRUMENTS.length];
	private JSpinner[]				instrumentLayer1BaseVelocity	= new JSpinner[Instrument.INSTRUMENTS.length];
	private JSpinner[]				instrumentLayer1AccentVelocity	= new JSpinner[Instrument.INSTRUMENTS.length];

	private JSpinner[]				instrumentLayer2MidiNum			= new JSpinner[Instrument.INSTRUMENTS.length];
	private JSpinner[]				instrumentLayer2Volume			= new JSpinner[Instrument.INSTRUMENTS.length];
	private JSpinner[]				instrumentLayer2Filter			= new JSpinner[Instrument.INSTRUMENTS.length];
	private JSpinner[]				instrumentLayer2Chorus			= new JSpinner[Instrument.INSTRUMENTS.length];
	private JSpinner[]				instrumentLayer2Pressure		= new JSpinner[Instrument.INSTRUMENTS.length];
	private JSpinner[]				instrumentLayer2Pan				= new JSpinner[Instrument.INSTRUMENTS.length];
	private JSpinner[]				instrumentLayer2Reverb			= new JSpinner[Instrument.INSTRUMENTS.length];
	private JSpinner[]				instrumentLayer2Modulation		= new JSpinner[Instrument.INSTRUMENTS.length];
	private JSpinner[]				instrumentLayer2BaseOctave		= new JSpinner[Instrument.INSTRUMENTS.length];
	private JSpinner[]				instrumentLayer2BaseVelocity	= new JSpinner[Instrument.INSTRUMENTS.length];
	private JSpinner[]				instrumentLayer2AccentVelocity	= new JSpinner[Instrument.INSTRUMENTS.length];
	
	private JSpinner[]				drumLayer1MidiNote				= new JSpinner[Drum.DRUMS.length];
	private JSpinner[]				drumLayer1BaseVelocity			= new JSpinner[Drum.DRUMS.length];
	private JSpinner[]				drumLayer1AccentVelocity		= new JSpinner[Drum.DRUMS.length];

	private JSpinner[]				drumLayer2MidiNote				= new JSpinner[Drum.DRUMS.length];
	private JSpinner[]				drumLayer2BaseVelocity			= new JSpinner[Drum.DRUMS.length];
	private JSpinner[]				drumLayer2AccentVelocity		= new JSpinner[Drum.DRUMS.length];

	private JComboBox<String>		echoInstrument					= null;
	private JSpinner				echoLayer						= null;
	private JSpinner				echoSteps						= null;
	private JSpinner				echoVelocityPercentage1			= null;
	private JSpinner				echoVelocityPercentage2			= null;
	private JSpinner				echoVelocityPercentage3			= null;
	private JSpinner				echoReverb1						= null;
	private JSpinner				echoReverb2						= null;
	private JSpinner				echoReverb3						= null;
	private JSpinner				echoPan1						= null;
	private JSpinner				echoPan2						= null;
	private JSpinner				echoPan3						= null;
	
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
			for (int i = 0; i < (Instrument.INSTRUMENTS.length - 1); i++) {
				InstrumentConfiguration conf = evt.getComposition().getSynthesizerConfiguration().getInstrument(Instrument.INSTRUMENTS[i]);
				if (instrumentLayer1MidiNum[i]!=null) {
					instrumentLayer1MidiNum[i].setValue(String.format("%03d",conf.getLayer1().getMidiNum()));
				}
				instrumentLayer1Volume[i].setValue(String.format("%03d",conf.getLayer1().getVolume()));
				instrumentLayer1Filter[i].setValue(String.format("%03d",conf.getLayer1().getFilter()));
				instrumentLayer1Chorus[i].setValue(String.format("%03d",conf.getLayer1().getChorus()));
				instrumentLayer1Pressure[i].setValue(String.format("%03d",conf.getLayer1().getPressure()));
				instrumentLayer1Pan[i].setValue(String.format("%03d",conf.getLayer1().getPan()));
				instrumentLayer1Reverb[i].setValue(String.format("%03d",conf.getLayer1().getReverb()));
				instrumentLayer1Modulation[i].setValue(String.format("%03d",conf.getLayer1().getModulation()));
				if (!Instrument.INSTRUMENTS[i].equals(Instrument.DRUMS)) {
					instrumentLayer1BaseOctave[i].setValue(String.format("%03d",conf.getLayer1().getBaseOctave()));
					instrumentLayer1BaseVelocity[i].setValue(String.format("%03d",conf.getLayer1().getBaseVelocity()));
					instrumentLayer1AccentVelocity[i].setValue(String.format("%03d",conf.getLayer1().getAccentVelocity()));
				}
				if (Instrument.INSTRUMENTS[i].equals(Instrument.SYNTH_BASS1) ||
					Instrument.INSTRUMENTS[i].equals(Instrument.SYNTH1) ||
					Instrument.INSTRUMENTS[i].equals(Instrument.LEAD) ||
					Instrument.INSTRUMENTS[i].equals(Instrument.STRINGS)
					) {
					instrumentLayer2MidiNum[i].setValue(String.format("%03d",conf.getLayer2().getMidiNum()));
					instrumentLayer2Volume[i].setValue(String.format("%03d",conf.getLayer2().getVolume()));
					instrumentLayer2Filter[i].setValue(String.format("%03d",conf.getLayer2().getFilter()));
					instrumentLayer2Chorus[i].setValue(String.format("%03d",conf.getLayer2().getChorus()));
					instrumentLayer2Pressure[i].setValue(String.format("%03d",conf.getLayer2().getPressure()));
					instrumentLayer2Pan[i].setValue(String.format("%03d",conf.getLayer2().getPan()));
					instrumentLayer2Reverb[i].setValue(String.format("%03d",conf.getLayer2().getReverb()));
					instrumentLayer2Modulation[i].setValue(String.format("%03d",conf.getLayer2().getModulation()));
					instrumentLayer2BaseOctave[i].setValue(String.format("%03d",conf.getLayer2().getBaseOctave()));
					instrumentLayer2BaseVelocity[i].setValue(String.format("%03d",conf.getLayer2().getBaseVelocity()));
					instrumentLayer2AccentVelocity[i].setValue(String.format("%03d",conf.getLayer2().getAccentVelocity()));
	
					getSliderForNumber(instrumentLayer2Volume[i]).setEnabled(conf.getLayer2().getMidiNum()>=0);
					getSliderForNumber(instrumentLayer2Filter[i]).setEnabled(conf.getLayer2().getMidiNum()>=0);
					getSliderForNumber(instrumentLayer2Chorus[i]).setEnabled(conf.getLayer2().getMidiNum()>=0);
					getSliderForNumber(instrumentLayer2Pressure[i]).setEnabled(conf.getLayer2().getMidiNum()>=0);
					getSliderForNumber(instrumentLayer2Reverb[i]).setEnabled(conf.getLayer2().getMidiNum()>=0);
					getSliderForNumber(instrumentLayer2Pan[i]).setEnabled(conf.getLayer2().getMidiNum()>=0);
					getSliderForNumber(instrumentLayer2Modulation[i]).setEnabled(conf.getLayer2().getMidiNum()>=0);
					getSliderForNumber(instrumentLayer2BaseOctave[i]).setEnabled(conf.getLayer2().getMidiNum()>=0);
					getSliderForNumber(instrumentLayer2BaseVelocity[i]).setEnabled(conf.getLayer2().getMidiNum()>=0);
					getSliderForNumber(instrumentLayer2AccentVelocity[i]).setEnabled(conf.getLayer2().getMidiNum()>=0);
				}
			}
			for (int d = 0; d < Drum.DRUMS.length; d++) {
				DrumConfiguration conf = evt.getComposition().getSynthesizerConfiguration().getDrum(Drum.DRUMS[d]);
				drumLayer1MidiNote[d].setValue(String.format("%03d",conf.getLayer1MidiNote()));
				drumLayer1BaseVelocity[d].setValue(String.format("%03d",conf.getLayer1BaseVelocity()));
				drumLayer1AccentVelocity[d].setValue(String.format("%03d",conf.getLayer1AccentVelocity()));
				drumLayer2MidiNote[d].setValue(String.format("%03d",conf.getLayer2MidiNote()));
				drumLayer2BaseVelocity[d].setValue(String.format("%03d",conf.getLayer2BaseVelocity()));
				drumLayer2AccentVelocity[d].setValue(String.format("%03d",conf.getLayer2AccentVelocity()));
	
				getSliderForNumber(drumLayer2BaseVelocity[d]).setEnabled(conf.getLayer2MidiNote()>=35);
				getSliderForNumber(drumLayer2AccentVelocity[d]).setEnabled(conf.getLayer2MidiNote()>=35);
			}
			EchoConfiguration echo = evt.getComposition().getSynthesizerConfiguration().getEcho();
			if (echo.getInstrument().length()==0) {
				echoInstrument.setSelectedIndex(0);
				getSliderForNumber(echoLayer).setEnabled(false);
				getSliderForNumber(echoSteps).setEnabled(false);
				getSliderForNumber(echoVelocityPercentage1).setEnabled(false);
				getSliderForNumber(echoVelocityPercentage2).setEnabled(false);
				getSliderForNumber(echoVelocityPercentage3).setEnabled(false);
				getSliderForNumber(echoReverb1).setEnabled(false);
				getSliderForNumber(echoReverb2).setEnabled(false);
				getSliderForNumber(echoReverb3).setEnabled(false);
				getSliderForNumber(echoPan1).setEnabled(false);
				getSliderForNumber(echoPan2).setEnabled(false);
				getSliderForNumber(echoPan3).setEnabled(false);
			} else {
				if (echo.getInstrument().equals(Instrument.SYNTH_BASS1) ||
					echo.getInstrument().equals(Instrument.SYNTH1) ||
					echo.getInstrument().equals(Instrument.LEAD) ||
					echo.getInstrument().equals(Instrument.STRINGS)
					) {
					getSliderForNumber(echoLayer).setEnabled(true);
				} else {
					getSliderForNumber(echoLayer).setEnabled(false);
				}
				getSliderForNumber(echoSteps).setEnabled(true);
				getSliderForNumber(echoVelocityPercentage1).setEnabled(true);
				getSliderForNumber(echoVelocityPercentage2).setEnabled(true);
				getSliderForNumber(echoVelocityPercentage3).setEnabled(true);
				getSliderForNumber(echoReverb1).setEnabled(true);
				getSliderForNumber(echoReverb2).setEnabled(true);
				getSliderForNumber(echoReverb3).setEnabled(true);
				getSliderForNumber(echoPan1).setEnabled(true);
				getSliderForNumber(echoPan2).setEnabled(true);
				getSliderForNumber(echoPan3).setEnabled(true);
				for (int i = 0; i < (Instrument.INSTRUMENTS.length - 1); i++) {
					if (echo.getInstrument().equals(Instrument.INSTRUMENTS[i])) {
						echoInstrument.setSelectedIndex((i + 1));
						break;
					}
				}
				echoLayer.setValue(String.format("%03d",echo.getLayer()));
				echoSteps.setValue(String.format("%03d",echo.getSteps()));
				echoVelocityPercentage1.setValue(String.format("%03d",echo.getVelocityPercentage1()));
				echoVelocityPercentage2.setValue(String.format("%03d",echo.getVelocityPercentage2()));
				echoVelocityPercentage3.setValue(String.format("%03d",echo.getVelocityPercentage3()));
				echoReverb1.setValue(String.format("%03d",echo.getReverb1()));
				echoReverb2.setValue(String.format("%03d",echo.getReverb2()));
				echoReverb3.setValue(String.format("%03d",echo.getReverb3()));
				echoPan1.setValue(String.format("%03d",echo.getPan1()));
				echoPan2.setValue(String.format("%03d",echo.getPan2()));
				echoPan3.setValue(String.format("%03d",echo.getPan3()));
			}
		}
		setValidate(true);
	}

	@Override
	public void setChangesInComposition(Composition composition) {
		for (int i = 0; i < (Instrument.INSTRUMENTS.length - 1); i++) {
			InstrumentConfiguration inst = composition.getSynthesizerConfiguration().getInstrument(Instrument.INSTRUMENTS[i]);
			inst.getLayer1().setMidiNum(Integer.parseInt(instrumentLayer1MidiNum[i].getValue().toString()));
			inst.getLayer1().setVolume(Integer.parseInt(instrumentLayer1Volume[i].getValue().toString()));
			inst.getLayer1().setFilter(Integer.parseInt(instrumentLayer1Filter[i].getValue().toString()));
			inst.getLayer1().setChorus(Integer.parseInt(instrumentLayer1Chorus[i].getValue().toString()));
			inst.getLayer1().setPressure(Integer.parseInt(instrumentLayer1Pressure[i].getValue().toString()));
			inst.getLayer1().setPan(Integer.parseInt(instrumentLayer1Pan[i].getValue().toString()));
			inst.getLayer1().setReverb(Integer.parseInt(instrumentLayer1Reverb[i].getValue().toString()));
			inst.getLayer1().setModulation(Integer.parseInt(instrumentLayer1Modulation[i].getValue().toString()));
			if (!Instrument.INSTRUMENTS[i].equals(Instrument.DRUMS)) {
				inst.getLayer1().setBaseOctave(Integer.parseInt(instrumentLayer1BaseOctave[i].getValue().toString()));
				inst.getLayer1().setBaseVelocity(Integer.parseInt(instrumentLayer1BaseVelocity[i].getValue().toString()));
				inst.getLayer1().setAccentVelocity(Integer.parseInt(instrumentLayer1AccentVelocity[i].getValue().toString()));
			}
			if (Instrument.INSTRUMENTS[i].equals(Instrument.SYNTH_BASS1) ||
				Instrument.INSTRUMENTS[i].equals(Instrument.SYNTH1) ||
				Instrument.INSTRUMENTS[i].equals(Instrument.LEAD) ||
				Instrument.INSTRUMENTS[i].equals(Instrument.STRINGS)
				) {
				inst.getLayer2().setMidiNum(Integer.parseInt(instrumentLayer2MidiNum[i].getValue().toString()));
				inst.getLayer2().setVolume(Integer.parseInt(instrumentLayer2Volume[i].getValue().toString()));
				inst.getLayer2().setFilter(Integer.parseInt(instrumentLayer2Filter[i].getValue().toString()));
				inst.getLayer2().setChorus(Integer.parseInt(instrumentLayer2Chorus[i].getValue().toString()));
				inst.getLayer2().setPressure(Integer.parseInt(instrumentLayer2Pressure[i].getValue().toString()));
				inst.getLayer2().setPan(Integer.parseInt(instrumentLayer2Pan[i].getValue().toString()));
				inst.getLayer2().setReverb(Integer.parseInt(instrumentLayer2Reverb[i].getValue().toString()));
				inst.getLayer2().setModulation(Integer.parseInt(instrumentLayer2Modulation[i].getValue().toString()));
				inst.getLayer2().setBaseOctave(Integer.parseInt(instrumentLayer2BaseOctave[i].getValue().toString()));
				inst.getLayer2().setBaseVelocity(Integer.parseInt(instrumentLayer2BaseVelocity[i].getValue().toString()));
				inst.getLayer2().setAccentVelocity(Integer.parseInt(instrumentLayer2AccentVelocity[i].getValue().toString()));
			}
		}
		for (int d = 0; d < Drum.DRUMS.length; d++) {
			DrumConfiguration drum = composition.getSynthesizerConfiguration().getDrum(Drum.DRUMS[d]);
			drum.setLayer1MidiNote(Integer.parseInt(drumLayer1MidiNote[d].getValue().toString()));
			drum.setLayer1BaseVelocity(Integer.parseInt(drumLayer1BaseVelocity[d].getValue().toString()));
			drum.setLayer1AccentVelocity(Integer.parseInt(drumLayer1AccentVelocity[d].getValue().toString()));
			drum.setLayer2MidiNote(Integer.parseInt(drumLayer2MidiNote[d].getValue().toString()));
			drum.setLayer2BaseVelocity(Integer.parseInt(drumLayer2BaseVelocity[d].getValue().toString()));
			drum.setLayer2AccentVelocity(Integer.parseInt(drumLayer2AccentVelocity[d].getValue().toString()));
		}
		EchoConfiguration echo = composition.getSynthesizerConfiguration().getEcho();
		echo.setInstrument(echoInstrument.getSelectedItem().toString());
		if (echo.getInstrument().equals(Instrument.SYNTH_BASS1) ||
			echo.getInstrument().equals(Instrument.SYNTH1) ||
			echo.getInstrument().equals(Instrument.LEAD) ||
			echo.getInstrument().equals(Instrument.STRINGS)
			) {
			echo.setLayer(Integer.parseInt(echoLayer.getValue().toString()));
		} else {
			echo.setLayer(1);
		}
		echo.setSteps(Integer.parseInt(echoSteps.getValue().toString()));
		echo.setVelocityPercentage1(Integer.parseInt(echoVelocityPercentage1.getValue().toString()));
		echo.setVelocityPercentage2(Integer.parseInt(echoVelocityPercentage2.getValue().toString()));
		echo.setVelocityPercentage3(Integer.parseInt(echoVelocityPercentage3.getValue().toString()));
		echo.setReverb1(Integer.parseInt(echoReverb1.getValue().toString()));
		echo.setReverb2(Integer.parseInt(echoReverb2.getValue().toString()));
		echo.setReverb3(Integer.parseInt(echoReverb3.getValue().toString()));
		echo.setPan1(Integer.parseInt(echoPan1.getValue().toString()));
		echo.setPan2(Integer.parseInt(echoPan2.getValue().toString()));
		echo.setPan3(Integer.parseInt(echoPan3.getValue().toString()));
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
		if (evt.getActionCommand().equals(F4_PRESSED)) {
			getController().getStateManager().setSelectedTab(this,FrameMain.TAB_SEQUENCE);
		}
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
				midiInstruments.add(instruments[i].getName());
				if (midiInstruments.size()>=128) {
					break;
				}
			}
		}
		
		for (int i = 0; i < (Instrument.INSTRUMENTS.length -1); i++) {
			panel.add(getInstrumentPanel(i,midiInstruments),Instrument.INSTRUMENTS[i]);
		}
		panel.add(getEchoPanel(),Instrument.ECHO);
		
		layout.show(panel,(String) instrument.getSelectedItem());
		return panel;
	}
	
	protected JPanel getInstrumentPanel(int instrumentNum,List<String> midiInstruments) {
		String name = Instrument.INSTRUMENTS[instrumentNum];

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		int row = 0;

		instrumentLayer1MidiNum[instrumentNum] = getNewNumberSpinner(3,0,127);
		addLabel(panel,row,"Layer 1 MIDI instrument number");
		if (midiInstruments.size()>0) {
			JPanel selector = getMidiInstrumentSelector(instrumentLayer1MidiNum[instrumentNum],midiInstruments,false);
			addProperty(panel,row,selector);
		} else {
			addProperty(panel,row,instrumentLayer1MidiNum[instrumentNum]);
		}
		if (name.equals(Instrument.DRUMS)) {
			getComboBoxForNumber(instrumentLayer1MidiNum[instrumentNum]).setEnabled(false);
		}
		
		row++;
		instrumentLayer1Volume[instrumentNum] = getNewNumberSpinner(3,0,127);
		JPanel slider = getNewNumberSlider(instrumentLayer1Volume[instrumentNum],0,127,110);
		addLabel(panel,row,"Layer 1 volume");
		addProperty(panel,row,slider);

		row++;
		instrumentLayer1Pan[instrumentNum] = getNewNumberSpinner(3,0,127);
		slider = getNewNumberSlider(instrumentLayer1Pan[instrumentNum],0,127,64);
		addLabel(panel,row,"Layer 1 pan");
		addProperty(panel,row,slider);

		row++;
		instrumentLayer1Pressure[instrumentNum] = getNewNumberSpinner(3,0,127);
		slider = getNewNumberSlider(instrumentLayer1Pressure[instrumentNum],0,127,0);
		addLabel(panel,row,"Layer 1 pressure");
		addProperty(panel,row,slider);

		row++;
		instrumentLayer1Chorus[instrumentNum] = getNewNumberSpinner(3,0,127);
		slider = getNewNumberSlider(instrumentLayer1Chorus[instrumentNum],0,127,0);
		addLabel(panel,row,"Layer 1 chorus");
		addProperty(panel,row,slider);

		row++;
		instrumentLayer1Modulation[instrumentNum] = getNewNumberSpinner(3,0,127);
		slider = getNewNumberSlider(instrumentLayer1Modulation[instrumentNum],0,127,0);
		addLabel(panel,row,"Layer 1 modulation");
		addProperty(panel,row,slider);

		row++;
		instrumentLayer1Filter[instrumentNum] = getNewNumberSpinner(3,0,127);
		slider = getNewNumberSlider(instrumentLayer1Filter[instrumentNum],0,127,127);
		addLabel(panel,row,"Layer 1 filter");
		addProperty(panel,row,slider);

		row++;
		instrumentLayer1Reverb[instrumentNum] = getNewNumberSpinner(3,0,127);
		slider = getNewNumberSlider(instrumentLayer1Reverb[instrumentNum],0,127,64);
		addLabel(panel,row,"Layer 1 reverb");
		addProperty(panel,row,slider);

		if (!name.equals(Instrument.DRUMS)) {
			row++;
			instrumentLayer1BaseOctave[instrumentNum] = getNewNumberSpinner(3,0,9);
			slider = getNewNumberSlider(instrumentLayer1BaseOctave[instrumentNum],0,9,3);
			addLabel(panel,row,"Layer 1 base octave");
			addProperty(panel,row,slider);

			row++;
			instrumentLayer1BaseVelocity[instrumentNum] = getNewNumberSpinner(3,0,127);
			slider = getNewNumberSlider(instrumentLayer1BaseVelocity[instrumentNum],0,127,100);
			addLabel(panel,row,"Layer 1 base velocity");
			addProperty(panel,row,slider);
	
			row++;
			instrumentLayer1AccentVelocity[instrumentNum] = getNewNumberSpinner(3,0,127);
			slider = getNewNumberSlider(instrumentLayer1AccentVelocity[instrumentNum],0,127,100);
			addLabel(panel,row,"Layer 1 accent velocity");
			addProperty(panel,row,slider);

			if (name.equals(Instrument.SYNTH_BASS1) ||
				name.equals(Instrument.SYNTH1) ||
				name.equals(Instrument.LEAD) ||
				name.equals(Instrument.STRINGS)
				) {
				row++;
				instrumentLayer2MidiNum[instrumentNum] = getNewNumberSpinner(3,-1,127);
				addLabel(panel,row,"Layer 2 MIDI instrument number");
				if (midiInstruments.size()>0) {
					JPanel selector = getMidiInstrumentSelector(instrumentLayer2MidiNum[instrumentNum],midiInstruments,true);
					addProperty(panel,row,selector);
				} else {
					addProperty(panel,row,instrumentLayer2MidiNum[instrumentNum]);
				}

				row++;
				instrumentLayer2Volume[instrumentNum] = getNewNumberSpinner(3,0,127);
				slider = getNewNumberSlider(instrumentLayer2Volume[instrumentNum],0,127,110);
				addLabel(panel,row,"Layer 2 volume");
				addProperty(panel,row,slider);

				row++;
				instrumentLayer2Pan[instrumentNum] = getNewNumberSpinner(3,0,127);
				slider = getNewNumberSlider(instrumentLayer2Pan[instrumentNum],0,127,64);
				addLabel(panel,row,"Layer 2 pan");
				addProperty(panel,row,slider);

				row++;
				instrumentLayer2Pressure[instrumentNum] = getNewNumberSpinner(3,0,127);
				slider = getNewNumberSlider(instrumentLayer2Pressure[instrumentNum],0,127,0);
				addLabel(panel,row,"Layer 2 pressure");
				addProperty(panel,row,slider);

				row++;
				instrumentLayer2Chorus[instrumentNum] = getNewNumberSpinner(3,0,127);
				slider = getNewNumberSlider(instrumentLayer2Chorus[instrumentNum],0,127,0);
				addLabel(panel,row,"Layer 2 chorus");
				addProperty(panel,row,slider);

				row++;
				instrumentLayer2Modulation[instrumentNum] = getNewNumberSpinner(3,0,127);
				slider = getNewNumberSlider(instrumentLayer2Modulation[instrumentNum],0,127,0);
				addLabel(panel,row,"Layer 2 modulation");
				addProperty(panel,row,slider);

				row++;
				instrumentLayer2Filter[instrumentNum] = getNewNumberSpinner(3,0,127);
				slider = getNewNumberSlider(instrumentLayer2Filter[instrumentNum],0,127,127);
				addLabel(panel,row,"Layer 2 filter");
				addProperty(panel,row,slider);

				row++;
				instrumentLayer2Reverb[instrumentNum] = getNewNumberSpinner(3,0,127);
				slider = getNewNumberSlider(instrumentLayer2Reverb[instrumentNum],0,127,64);
				addLabel(panel,row,"Layer 2 reverb");
				addProperty(panel,row,slider);

				row++;
				instrumentLayer2BaseOctave[instrumentNum] = getNewNumberSpinner(3,0,9);
				slider = getNewNumberSlider(instrumentLayer2BaseOctave[instrumentNum],0,9,3);
				addLabel(panel,row,"Layer 2 base octave");
				addProperty(panel,row,slider);

				row++;
				instrumentLayer2BaseVelocity[instrumentNum] = getNewNumberSpinner(3,0,127);
				slider = getNewNumberSlider(instrumentLayer2BaseVelocity[instrumentNum],0,127,100);
				addLabel(panel,row,"Layer 2 base velocity");
				addProperty(panel,row,slider);
		
				row++;
				instrumentLayer2AccentVelocity[instrumentNum] = getNewNumberSpinner(3,0,127);
				slider = getNewNumberSlider(instrumentLayer2AccentVelocity[instrumentNum],0,127,100);
				addLabel(panel,row,"Layer 2 accent velocity");
				addProperty(panel,row,slider);
			}
		}

		if (name.equals(Instrument.DRUMS)) {
			for (int d = 0; d < Drum.DRUMS.length; d++) {
				int drow = 0;
				JPanel drumPanel = new JPanel();
				drumPanel.setLayout(new GridBagLayout());
				drumPanel.setBorder(BorderFactory.createTitledBorder(Drum.DRUMS[d]));

				drumLayer1MidiNote[d] = getNewNumberSpinner(3,35,81);
				slider = getNewNumberSlider(drumLayer1MidiNote[d],35,81,35);
				addLabel(drumPanel,drow,"Layer 1 MIDI note number");
				addProperty(drumPanel,drow,slider);
				
				drow++;
				drumLayer1BaseVelocity[d] = getNewNumberSpinner(3,0,127);
				slider = getNewNumberSlider(drumLayer1BaseVelocity[d],0,127,100);
				addLabel(drumPanel,drow,"Layer 1 Base velocity");
				addProperty(drumPanel,drow,slider);
		
				drow++;
				drumLayer1AccentVelocity[d] = getNewNumberSpinner(3,0,127);
				slider = getNewNumberSlider(drumLayer1AccentVelocity[d],0,127,100);
				addLabel(drumPanel,drow,"Layer 1 Accent velocity");
				addProperty(drumPanel,drow,slider);
				
				drow++;
				drumLayer2MidiNote[d] = getNewNumberSpinner(3,34,81);
				slider = getNewNumberSlider(drumLayer2MidiNote[d],34,81,35);
				addLabel(drumPanel,drow,"Layer 2 MIDI note number");
				addProperty(drumPanel,drow,slider);
				
				drow++;
				drumLayer2BaseVelocity[d] = getNewNumberSpinner(3,0,127);
				slider = getNewNumberSlider(drumLayer2BaseVelocity[d],0,127,100);
				addLabel(drumPanel,drow,"Layer 2 base velocity");
				addProperty(drumPanel,drow,slider);
		
				drow++;
				drumLayer2AccentVelocity[d] = getNewNumberSpinner(3,0,127);
				slider = getNewNumberSlider(drumLayer2AccentVelocity[d],0,127,100);
				addLabel(drumPanel,drow,"Layer 2 accent velocity");
				addProperty(drumPanel,drow,slider);

				row++;
				addComponent(panel,row,0.01,drumPanel);
			}
		}
		
		row++;
		addFiller(panel,row);
		
		return panel;
	}
	
	protected JPanel getEchoPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		int row = 0;
		
		echoInstrument = new JComboBox<String>();
		echoInstrument.addItem("");
		echoInstrument.addItemListener(this);
		for (int l = 0; l < echoInstrument.getKeyListeners().length; l++) {
			echoInstrument.removeKeyListener(echoInstrument.getKeyListeners()[l]);
		}
		echoInstrument.addKeyListener(getController().getPlayerKeyListener());
		for (int i = 0; i < (Instrument.INSTRUMENTS.length - 2); i++) {
			if (!Instrument.INSTRUMENTS[i].equals(Instrument.DRUMS)) {
				echoInstrument.addItem(Instrument.INSTRUMENTS[i]);
			}
		}
		addLabel(panel,row,"Instrument");
		addProperty(panel,row,echoInstrument);

		row++;
		echoLayer = getNewNumberSpinner(3,1,2);
		JPanel slider = getNewNumberSlider(echoLayer,1,2,1);
		addLabel(panel,row,"Layer");
		addProperty(panel,row,slider);

		row++;
		echoSteps = getNewNumberSpinner(3,1,24);
		slider = getNewNumberSlider(echoSteps,1,24,6);
		addLabel(panel,row,"Steps");
		addProperty(panel,row,slider);

		row++;
		echoVelocityPercentage1 = getNewNumberSpinner(3,1,99);
		slider = getNewNumberSlider(echoVelocityPercentage1,1,99,60);
		addLabel(panel,row,"Echo 1 velocity percentage");
		addProperty(panel,row,slider);

		row++;
		echoVelocityPercentage2 = getNewNumberSpinner(3,1,99);
		slider = getNewNumberSlider(echoVelocityPercentage2,1,99,40);
		addLabel(panel,row,"Echo 2 velocity percentage");
		addProperty(panel,row,slider);

		row++;
		echoVelocityPercentage3 = getNewNumberSpinner(3,1,99);
		slider = getNewNumberSlider(echoVelocityPercentage3,1,99,20);
		addLabel(panel,row,"Echo 3 velocity percentage");
		addProperty(panel,row,slider);

		row++;
		echoPan1 = getNewNumberSpinner(3,0,127);
		slider = getNewNumberSlider(echoPan1,0,127,24);
		addLabel(panel,row,"Echo 1 pan");
		addProperty(panel,row,slider);

		row++;
		echoPan2 = getNewNumberSpinner(3,0,127);
		slider = getNewNumberSlider(echoPan2,0,127,104);
		addLabel(panel,row,"Echo 2 pan");
		addProperty(panel,row,slider);

		row++;
		echoPan3 = getNewNumberSpinner(3,0,127);
		slider = getNewNumberSlider(echoPan3,0,127,48);
		addLabel(panel,row,"Echo 3 pan");
		addProperty(panel,row,slider);

		row++;
		echoReverb1 = getNewNumberSpinner(3,0,127);
		slider = getNewNumberSlider(echoReverb1,0,127,103);
		addLabel(panel,row,"Echo 1 reverb");
		addProperty(panel,row,slider);

		row++;
		echoReverb2 = getNewNumberSpinner(3,0,127);
		slider = getNewNumberSlider(echoReverb2,0,127,115);
		addLabel(panel,row,"Echo 2 reverb");
		addProperty(panel,row,slider);

		row++;
		echoReverb3 = getNewNumberSpinner(3,0,127);
		slider = getNewNumberSlider(echoReverb3,0,127,127);
		addLabel(panel,row,"Echo 2 reverb");
		addProperty(panel,row,slider);

		row++;
		addFiller(panel,row);
		
		return panel;
	}
	
	protected JPanel getMidiInstrumentSelector(JSpinner number,List<String> midiInstruments,boolean layer) {
		List<String> options = new ArrayList<String>(midiInstruments);
		int subtract = 0;
		if (layer) {
			options.add(0,"");
			subtract = 1;
		}
		return getNewNumberComboBox(number,options,this,subtract);
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
		
		// F4 Override
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_F4,0,false);
		r.registerKeyboardAction(this,F4_PRESSED,stroke,JComponent.WHEN_FOCUSED);
		
		for (int l = 0; l < r.getKeyListeners().length; l++) {
			r.removeKeyListener(r.getKeyListeners()[l]);
		}
		r.addKeyListener(getController().getPlayerKeyListener());
		return r;
	}
}
