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
	private JComboBox<String>		instrument						= null;
	private JPanel					cardPanel						= null;
	private JFormattedTextField[]	instrumentMidiNum				= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private List<JComboBox<String>>	instrumentMidiInstrument		= new ArrayList<JComboBox<String>>();
	private JFormattedTextField[]	instrumentPolyphony				= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	instrumentBaseOctave			= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	instrumentBaseVelocity			= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	instrumentAccentVelocity		= new JFormattedTextField[Instrument.INSTRUMENTS.length];

	private JFormattedTextField[]	instrumentLayerMidiNum			= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private List<JComboBox<String>>	instrumentLayerMidiInstrument	= new ArrayList<JComboBox<String>>();
	private JFormattedTextField[]	instrumentLayerBaseOctave		= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	instrumentLayerBaseVelocity		= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	instrumentLayerAccentVelocity	= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	
	private JFormattedTextField[]	drumNoteNum						= new JFormattedTextField[Drum.DRUMS.length];
	private JFormattedTextField[]	drumBaseVelocity				= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	drumAccentVelocity				= new JFormattedTextField[Instrument.INSTRUMENTS.length];

	private JFormattedTextField[]	drumLayerNoteNum				= new JFormattedTextField[Drum.DRUMS.length];
	private JFormattedTextField[]	drumLayerBaseVelocity			= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	drumLayerAccentVelocity			= new JFormattedTextField[Instrument.INSTRUMENTS.length];

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
			instrumentPolyphony[i].setValue(conf.getPolyphony());
			if (!Instrument.INSTRUMENTS[i].equals(Instrument.DRUMS)) {
				instrumentMidiNum[i].setValue(conf.getLayer1MidiNum());
				instrumentBaseOctave[i].setValue(conf.getLayer1BaseOctave());
				instrumentBaseVelocity[i].setValue(conf.getLayer1BaseVelocity());
				instrumentAccentVelocity[i].setValue(conf.getLayer1AccentVelocity());
			}
			if (Instrument.INSTRUMENTS[i].equals(Instrument.SYNTH1) ||
				Instrument.INSTRUMENTS[i].equals(Instrument.SYNTH_BASS1) ||
				Instrument.INSTRUMENTS[i].equals(Instrument.PIANO) ||
				Instrument.INSTRUMENTS[i].equals(Instrument.STRINGS1)
				) {
				instrumentLayerMidiNum[i].setValue(conf.getLayer2MidiNum());
				instrumentLayerBaseOctave[i].setValue(conf.getLayer2BaseOctave());
				instrumentLayerBaseVelocity[i].setValue(conf.getLayer2BaseVelocity());
				instrumentLayerAccentVelocity[i].setValue(conf.getLayer2AccentVelocity());
			}
		}
		for (int d = 0; d < Drum.DRUMS.length; d++) {
			DrumConfiguration conf = comp.getSynthesizerConfiguration().getDrum(Drum.DRUMS[d]);
			drumNoteNum[d].setValue(conf.getLayer1MidiNote());
			drumBaseVelocity[d].setValue(conf.getLayer1BaseVelocity());
			drumAccentVelocity[d].setValue(conf.getLayer1AccentVelocity());
			drumLayerNoteNum[d].setValue(conf.getLayer2MidiNote());
			drumLayerBaseVelocity[d].setValue(conf.getLayer2BaseVelocity());
			drumLayerAccentVelocity[d].setValue(conf.getLayer2AccentVelocity());
		}
		setValidate(true);
	}

	@Override
	public void getCompositionUpdate(String tab, Composition comp) {
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			InstrumentConfiguration inst = comp.getSynthesizerConfiguration().getInstrument(Instrument.INSTRUMENTS[i]);
			inst.setPolyphony(Integer.parseInt(instrumentPolyphony[i].getValue().toString()));
			if (!Instrument.INSTRUMENTS[i].equals(Instrument.DRUMS)) {
				inst.setLayer1MidiNum(Integer.parseInt(instrumentMidiNum[i].getValue().toString()));
				inst.setLayer1BaseOctave(Integer.parseInt(instrumentBaseOctave[i].getValue().toString()));
				inst.setLayer1BaseVelocity(Integer.parseInt(instrumentBaseVelocity[i].getValue().toString()));
				inst.setLayer1AccentVelocity(Integer.parseInt(instrumentAccentVelocity[i].getValue().toString()));
			}
			if (Instrument.INSTRUMENTS[i].equals(Instrument.SYNTH1) ||
				Instrument.INSTRUMENTS[i].equals(Instrument.SYNTH_BASS1) ||
				Instrument.INSTRUMENTS[i].equals(Instrument.PIANO) ||
				Instrument.INSTRUMENTS[i].equals(Instrument.STRINGS1)
				) {
				inst.setLayer2MidiNum(Integer.parseInt(instrumentLayerMidiNum[i].getValue().toString()));
				inst.setLayer2BaseOctave(Integer.parseInt(instrumentLayerBaseOctave[i].getValue().toString()));
				inst.setLayer2BaseVelocity(Integer.parseInt(instrumentLayerBaseVelocity[i].getValue().toString()));
				inst.setLayer2AccentVelocity(Integer.parseInt(instrumentLayerAccentVelocity[i].getValue().toString()));
			}
		}
		for (int d = 0; d < Drum.DRUMS.length; d++) {
			DrumConfiguration drum = comp.getSynthesizerConfiguration().getDrum(Drum.DRUMS[d]);
			drum.setLayer1MidiNote(Integer.parseInt(drumNoteNum[d].getValue().toString()));
			drum.setLayer1BaseVelocity(Integer.parseInt(drumBaseVelocity[d].getValue().toString()));
			drum.setLayer1AccentVelocity(Integer.parseInt(drumAccentVelocity[d].getValue().toString()));
			drum.setLayer2MidiNote(Integer.parseInt(drumLayerNoteNum[d].getValue().toString()));
			drum.setLayer2BaseVelocity(Integer.parseInt(drumLayerBaseVelocity[d].getValue().toString()));
			drum.setLayer2AccentVelocity(Integer.parseInt(drumLayerAccentVelocity[d].getValue().toString()));
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
		if (instrumentLayerMidiInstrument.size()==4) {
			int num = 0;
			for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
				if (instrumentLayerMidiNum[i]!=null) {
					if (evt.getSource()==instrumentLayerMidiNum[i]) {
						JComboBox<String> midiInstrument = instrumentLayerMidiInstrument.get(num);
						int index = (Integer.parseInt(instrumentLayerMidiNum[i].getValue().toString()) + 1);
						midiInstrument.setSelectedIndex(index);
						break;
					}
					num++;
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
			JComboBox<String> selector = null;
			i = 0;
			for (JComboBox<String> midiInstrument: instrumentLayerMidiInstrument) {
				if (evt.getSource()==midiInstrument) {
					selector = midiInstrument;
					break;
				}
				i++;
			}
			if (selector!=null) {
				if (i==1) {
					i = 3;
				} else if (i==2) {
					i = 7;
				} else if (i==3) {
					i = 10;
				}
				instrumentLayerMidiNum[i].setValue((selector.getSelectedIndex() - 1));
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

		instrumentPolyphony[instrumentNum] = getNewNumberTextField(1);
		JPanel slider = getNewNumberSlider(instrumentPolyphony[instrumentNum],0,16,4);
		addLabel(panel,row,"Polyphony");
		addProperty(panel,row,slider);

		if (!name.equals(Instrument.DRUMS)) {
			row++;
			instrumentMidiNum[instrumentNum] = getNewNumberTextField(3);
			addLabel(panel,row,"Layer 1 MIDI instrument number");
			if (midiInstruments.size()>0) {
				JPanel selector = getMidiInstrumentSelector(name,instrumentMidiNum[instrumentNum],midiInstruments,false);
				addProperty(panel,row,selector);
			} else {
				addProperty(panel,row,instrumentMidiNum[instrumentNum]);
			}
			
			row++;
			instrumentBaseOctave[instrumentNum] = getNewNumberTextField(1);
			slider = getNewNumberSlider(instrumentBaseOctave[instrumentNum],0,9,3);
			addLabel(panel,row,"Layer 1 base octave");
			addProperty(panel,row,slider);

			row++;
			instrumentBaseVelocity[instrumentNum] = getNewNumberTextField(3);
			slider = getNewNumberSlider(instrumentBaseVelocity[instrumentNum],0,127,100);
			addLabel(panel,row,"Layer 1 base velocity");
			addProperty(panel,row,slider);
	
			row++;
			instrumentAccentVelocity[instrumentNum] = getNewNumberTextField(3);
			slider = getNewNumberSlider(instrumentAccentVelocity[instrumentNum],0,127,100);
			addLabel(panel,row,"Layer 1 accent velocity");
			addProperty(panel,row,slider);

			if (name.equals(Instrument.SYNTH1) ||
				name.equals(Instrument.SYNTH_BASS1) ||
				name.equals(Instrument.PIANO) ||
				name.equals(Instrument.STRINGS1)
				) {
				row++;
				instrumentLayerMidiNum[instrumentNum] = getNewNumberTextField(3);
				addLabel(panel,row,"Layer 2 MIDI instrument number");
				if (midiInstruments.size()>0) {
					JPanel selector = getMidiInstrumentSelector(name,instrumentLayerMidiNum[instrumentNum],midiInstruments,true);
					addProperty(panel,row,selector);
				} else {
					addProperty(panel,row,instrumentLayerMidiNum[instrumentNum]);
				}

				row++;
				instrumentLayerBaseOctave[instrumentNum] = getNewNumberTextField(1);
				slider = getNewNumberSlider(instrumentLayerBaseOctave[instrumentNum],0,9,3);
				addLabel(panel,row,"Layer 2 base octave");
				addProperty(panel,row,slider);

				row++;
				instrumentLayerBaseVelocity[instrumentNum] = getNewNumberTextField(3);
				slider = getNewNumberSlider(instrumentLayerBaseVelocity[instrumentNum],0,127,100);
				addLabel(panel,row,"Layer 2 base velocity");
				addProperty(panel,row,slider);
		
				row++;
				instrumentLayerAccentVelocity[instrumentNum] = getNewNumberTextField(3);
				slider = getNewNumberSlider(instrumentLayerAccentVelocity[instrumentNum],0,127,100);
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

				drumNoteNum[d] = getNewNumberTextField(3);
				slider = getNewNumberSlider(drumNoteNum[d],35,81,35);
				addLabel(drumPanel,drow,"Layer 1 MIDI note number");
				addProperty(drumPanel,drow,slider);
				
				drow++;
				drumBaseVelocity[d] = getNewNumberTextField(3);
				slider = getNewNumberSlider(drumBaseVelocity[d],0,127,100);
				addLabel(drumPanel,drow,"Layer 1 Base velocity");
				addProperty(drumPanel,drow,slider);
		
				drow++;
				drumAccentVelocity[d] = getNewNumberTextField(3);
				slider = getNewNumberSlider(drumAccentVelocity[d],0,127,100);
				addLabel(drumPanel,drow,"Layer 1 Accent velocity");
				addProperty(drumPanel,drow,slider);
				
				drow++;
				drumLayerNoteNum[d] = getNewNumberTextField(3);
				slider = getNewNumberSlider(drumLayerNoteNum[d],34,81,35);
				addLabel(drumPanel,drow,"Layer 2 Layer MIDI note number");
				addProperty(drumPanel,drow,slider);
				
				drow++;
				drumLayerBaseVelocity[d] = getNewNumberTextField(3);
				slider = getNewNumberSlider(drumLayerBaseVelocity[d],0,127,100);
				addLabel(drumPanel,drow,"Layer 2 Layer base velocity");
				addProperty(drumPanel,drow,slider);
		
				drow++;
				drumLayerAccentVelocity[d] = getNewNumberTextField(3);
				slider = getNewNumberSlider(drumLayerAccentVelocity[d],0,127,100);
				addLabel(drumPanel,drow,"Layer 2 Layer accent velocity");
				addProperty(drumPanel,drow,slider);

				row++;
				addComponent(panel,row,0.01,drumPanel);
			}
		}
		
		row++;
		addFiller(panel,row);
		
		return panel;
	}
	
	protected JPanel getMidiInstrumentSelector(String name, JFormattedTextField number,List<String> midiInstruments,boolean layer) {
		JPanel selector = new JPanel();
		selector.setLayout(new GridBagLayout());
		
		JComboBox<String> midiInstrument = new JComboBox<String>();
		if (layer) {
			midiInstrument.addItem("");
		}
		for (String inst: midiInstruments) {
			midiInstrument.addItem(inst);
		}
		midiInstrument.addItemListener(this);
		for (int l = 0; l < midiInstrument.getKeyListeners().length; l++) {
			midiInstrument.removeKeyListener(midiInstrument.getKeyListeners()[l]);
		}
		midiInstrument.addKeyListener(getController().getPlayerKeyListener());
		if (name.equals(Instrument.DRUMS)) {
			number.setEnabled(false);
			midiInstrument.setEnabled(false);
		}
		selector.add(number);
		selector.add(midiInstrument);
		if (!layer) {
			instrumentMidiInstrument.add(midiInstrument);
		} else {
			instrumentLayerMidiInstrument.add(midiInstrument);
		}
		return selector;
	}
}
