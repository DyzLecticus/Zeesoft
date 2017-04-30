package nl.zeesoft.zmmt.gui;

import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.syntesizer.Drum;
import nl.zeesoft.zmmt.syntesizer.DrumConfiguration;
import nl.zeesoft.zmmt.syntesizer.Instrument;
import nl.zeesoft.zmmt.syntesizer.InstrumentConfiguration;

public class PanelInstruments extends PanelObject implements ItemListener, CompositionUpdater {
	private JComboBox<String>		instrument					= null;
	private JPanel					cardPanel					= null;
	private JFormattedTextField[]	instrumentMidiNum			= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private List<JComboBox<String>>	instrumentMidiInstrument	= new ArrayList<JComboBox<String>>();
	private JFormattedTextField[]	instrumentPolyphony			= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	instrumentBaseOctave		= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	instrumentBaseVelocity		= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	instrumentAccentVelocity	= new JFormattedTextField[Instrument.INSTRUMENTS.length];

	private JFormattedTextField[]	drumNoteNum					= new JFormattedTextField[Drum.DRUMS.length];
	private JFormattedTextField[]	drumBaseVelocity			= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	drumAccentVelocity			= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	
	public PanelInstruments(Controller controller) {
		super(controller);
	}

	@Override
	public void initialize() {
		setValidate(false);
		getPanel().addKeyListener(getController().getPlayerKeyListener());
		getPanel().setLayout(new GridBagLayout());
		getPanel().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
				
		int row = 0;

		instrument = getController().getNewInstrumentSelector();
		instrument.addItemListener(this);
		
		addComponent(getPanel(),row,0.01,instrument,false);
		
		row++;
		cardPanel = getCardPanel(getController().getPlayerKeyListener());
		addComponent(getPanel(),row,0.01,cardPanel);
		
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
		getController().changedComposition(FrameMain.INSTRUMENTS);
	}

	@Override
	public void requestFocus() {
		instrument.requestFocus();
	}

	@Override
	public void updatedComposition(String tab,Composition comp) {
		setValidate(false);
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			InstrumentConfiguration conf = comp.getSynthesizerConfiguration().getInstrument(Instrument.INSTRUMENTS[i]);
			instrumentMidiNum[i].setValue(conf.getMidiNum());
			instrumentPolyphony[i].setValue(conf.getPolyphony());
			if (!Instrument.INSTRUMENTS[i].equals(Instrument.DRUMS)) {
				instrumentBaseOctave[i].setValue(conf.getBaseOctave());
				instrumentBaseVelocity[i].setValue(conf.getBaseVelocity());
				instrumentAccentVelocity[i].setValue(conf.getAccentVelocity());
			}
		}
		for (int d = 0; d < Drum.DRUMS.length; d++) {
			DrumConfiguration conf = comp.getSynthesizerConfiguration().getDrum(Drum.DRUMS[d]);
			drumNoteNum[d].setValue(conf.getNoteNum());
			drumBaseVelocity[d].setValue(conf.getBaseVelocity());
			drumAccentVelocity[d].setValue(conf.getAccentVelocity());
		}
		setValidate(true);
	}

	@Override
	public void getCompositionUpdate(String tab, Composition comp) {
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			InstrumentConfiguration inst = comp.getSynthesizerConfiguration().getInstrument(Instrument.INSTRUMENTS[i]);
			inst.setMidiNum(Integer.parseInt(instrumentMidiNum[i].getValue().toString()));
			inst.setPolyphony(Integer.parseInt(instrumentPolyphony[i].getValue().toString()));
			if (!Instrument.INSTRUMENTS[i].equals(Instrument.DRUMS)) {
				inst.setBaseOctave(Integer.parseInt(instrumentBaseOctave[i].getValue().toString()));
				inst.setBaseVelocity(Integer.parseInt(instrumentBaseVelocity[i].getValue().toString()));
				inst.setAccentVelocity(Integer.parseInt(instrumentAccentVelocity[i].getValue().toString()));
			}
		}
		for (int d = 0; d < Drum.DRUMS.length; d++) {
			DrumConfiguration drum = comp.getSynthesizerConfiguration().getDrum(Drum.DRUMS[d]);
			drum.setNoteNum(Integer.parseInt(drumNoteNum[d].getValue().toString()));
			drum.setBaseVelocity(Integer.parseInt(drumBaseVelocity[d].getValue().toString()));
			drum.setAccentVelocity(Integer.parseInt(drumAccentVelocity[d].getValue().toString()));
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		if (instrumentMidiInstrument.size()==Instrument.INSTRUMENTS.length) {
			for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
				if (evt.getSource()==instrumentMidiNum[i]) {
					JComboBox<String> midiInstrument = instrumentMidiInstrument.get(i);
					midiInstrument.setSelectedIndex(Integer.parseInt(instrumentMidiNum[i].getValue().toString()));
				}
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent evt) {
		if (evt.getSource()==instrument) {
			CardLayout cl = (CardLayout)(cardPanel.getLayout());
			cl.show(cardPanel,(String) evt.getItem());
		} else {
			int i = 0;
			for (JComboBox<String> midiInstrument: instrumentMidiInstrument) {
				if (evt.getSource()==midiInstrument) {
					instrumentMidiNum[i].setValue(midiInstrument.getSelectedIndex());
					break;
				}
				i++;
			}
		}
	}

	protected JPanel getCardPanel(KeyListener keyListener) {
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
		
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			panel.add(getInstrumentPanel(i,keyListener,midiInstruments),Instrument.INSTRUMENTS[i]);
		}
		layout.show(panel,(String) instrument.getSelectedItem());
		return panel;
	}
	
	protected JPanel getInstrumentPanel(int instrumentNum,KeyListener keyListener,List<String> midiInstruments) {
		String name = Instrument.INSTRUMENTS[instrumentNum];

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		int row = 0;

		instrumentMidiNum[instrumentNum] = getNewNumberTextField(3);
		addLabel(panel,row,"MIDI instrument number");
		if (midiInstruments.size()>0) {
			JPanel selector = new JPanel();
			selector.setLayout(new GridBagLayout());
			
			JComboBox<String> midiInstrument = new JComboBox<String>();
			for (String inst: midiInstruments) {
				midiInstrument.addItem(inst);
			}
			midiInstrument.addItemListener(this);
			for (int l = 0; l < midiInstrument.getKeyListeners().length; l++) {
				midiInstrument.removeKeyListener(midiInstrument.getKeyListeners()[l]);
			}
			midiInstrument.addKeyListener(getController().getPlayerKeyListener());
			selector.add(instrumentMidiNum[instrumentNum]);
			selector.add(midiInstrument);
			instrumentMidiInstrument.add(midiInstrument);
			addProperty(panel,row,selector);
		} else {
			addProperty(panel,row,instrumentMidiNum[instrumentNum]);
		}
		
		row++;
		instrumentPolyphony[instrumentNum] = getNewNumberTextField(1);
		JPanel slider = getNewNumberSlider(instrumentPolyphony[instrumentNum],0,16,4);
		addLabel(panel,row,"Polyphony");
		addProperty(panel,row,slider);

		if (!name.equals(Instrument.DRUMS)) {
			row++;
			instrumentBaseOctave[instrumentNum] = getNewNumberTextField(1);
			slider = getNewNumberSlider(instrumentBaseOctave[instrumentNum],0,9,3);
			addLabel(panel,row,"Base octave");
			addProperty(panel,row,slider);

			row++;
			instrumentBaseVelocity[instrumentNum] = getNewNumberTextField(3);
			slider = getNewNumberSlider(instrumentBaseVelocity[instrumentNum],0,127,100);
			addLabel(panel,row,"Base velocity");
			addProperty(panel,row,slider);
	
			row++;
			instrumentAccentVelocity[instrumentNum] = getNewNumberTextField(3);
			slider = getNewNumberSlider(instrumentAccentVelocity[instrumentNum],0,127,100);
			addLabel(panel,row,"Accent velocity");
			addProperty(panel,row,slider);
		}

		if (name.equals(Instrument.DRUMS)) {
			for (int d = 0; d < Drum.DRUMS.length; d++) {
				int drow = 0;
				JPanel drumPanel = new JPanel();
				drumPanel.setLayout(new GridBagLayout());
				drumPanel.setBorder(BorderFactory.createTitledBorder(Drum.DRUMS[d]));

				drumNoteNum[d] = getNewNumberTextField(3);
				addLabel(drumPanel,drow,"Midi note number");
				addProperty(drumPanel,drow,drumNoteNum[d]);
				
				drow++;
				drumBaseVelocity[d] = getNewNumberTextField(3);
				slider = getNewNumberSlider(drumBaseVelocity[d],0,127,100);
				addLabel(drumPanel,drow,"Base velocity");
				addProperty(drumPanel,drow,slider);
		
				drow++;
				drumAccentVelocity[d] = getNewNumberTextField(3);
				slider = getNewNumberSlider(drumAccentVelocity[d],0,127,100);
				addLabel(drumPanel,drow,"Accent velocity");
				addProperty(drumPanel,drow,slider);
				
				row++;
				addComponent(panel,row,0.01,drumPanel);
			}
		}
		
		row++;
		addFiller(panel,row);
		
		return panel;
	}
}
