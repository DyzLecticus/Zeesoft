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
	private JFormattedTextField[]	instrumentPolyphony				= new JFormattedTextField[Instrument.INSTRUMENTS.length];

	private JFormattedTextField[]	instrumentLayer1MidiNum			= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private List<JComboBox<String>>	instrumentLayer1MidiInstrument	= new ArrayList<JComboBox<String>>();
	private JFormattedTextField[]	instrumentLayer1BaseOctave		= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	instrumentLayer1BaseVelocity	= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	instrumentLayer1AccentVelocity	= new JFormattedTextField[Instrument.INSTRUMENTS.length];

	private JFormattedTextField[]	instrumentLayer2MidiNum			= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private List<JComboBox<String>>	instrumentLayer2MidiInstrument	= new ArrayList<JComboBox<String>>();
	private JFormattedTextField[]	instrumentLayer2BaseOctave		= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	instrumentLayer2BaseVelocity	= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	instrumentLayer2AccentVelocity	= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	
	private JFormattedTextField[]	drumLayer1MidiNote				= new JFormattedTextField[Drum.DRUMS.length];
	private JFormattedTextField[]	drumLayer1BaseVelocity			= new JFormattedTextField[Drum.DRUMS.length];
	private JFormattedTextField[]	drumLayer1AccentVelocity		= new JFormattedTextField[Drum.DRUMS.length];

	private JFormattedTextField[]	drumLayer2MidiNote				= new JFormattedTextField[Drum.DRUMS.length];
	private JFormattedTextField[]	drumLayer2BaseVelocity			= new JFormattedTextField[Drum.DRUMS.length];
	private JFormattedTextField[]	drumLayer2AccentVelocity		= new JFormattedTextField[Drum.DRUMS.length];

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
			if (instrumentLayer1MidiNum[i]!=null) {
				instrumentLayer1MidiNum[i].setValue(conf.getLayer1MidiNum());
			}
			if (!Instrument.INSTRUMENTS[i].equals(Instrument.DRUMS)) {
				instrumentLayer1BaseOctave[i].setValue(conf.getLayer1BaseOctave());
				instrumentLayer1BaseVelocity[i].setValue(conf.getLayer1BaseVelocity());
				instrumentLayer1AccentVelocity[i].setValue(conf.getLayer1AccentVelocity());
			}
			if (Instrument.INSTRUMENTS[i].equals(Instrument.SYNTH_BASS1) ||
				Instrument.INSTRUMENTS[i].equals(Instrument.SYNTH1) ||
				Instrument.INSTRUMENTS[i].equals(Instrument.PIANO) ||
				Instrument.INSTRUMENTS[i].equals(Instrument.STRINGS)
				) {
				instrumentLayer2MidiNum[i].setValue(conf.getLayer2MidiNum());
				instrumentLayer2BaseOctave[i].setValue(conf.getLayer2BaseOctave());
				instrumentLayer2BaseVelocity[i].setValue(conf.getLayer2BaseVelocity());
				instrumentLayer2AccentVelocity[i].setValue(conf.getLayer2AccentVelocity());

				instrumentLayer2BaseOctave[i].setEnabled(conf.getLayer2MidiNum()>=0);
				instrumentLayer2BaseVelocity[i].setEnabled(conf.getLayer2MidiNum()>=0);
				instrumentLayer2AccentVelocity[i].setEnabled(conf.getLayer2MidiNum()>=0);
				getSliderForNumber(instrumentLayer2BaseOctave[i]).setEnabled(conf.getLayer2MidiNum()>=0);
				getSliderForNumber(instrumentLayer2BaseVelocity[i]).setEnabled(conf.getLayer2MidiNum()>=0);
				getSliderForNumber(instrumentLayer2AccentVelocity[i]).setEnabled(conf.getLayer2MidiNum()>=0);
			}
		}
		for (int d = 0; d < Drum.DRUMS.length; d++) {
			DrumConfiguration conf = comp.getSynthesizerConfiguration().getDrum(Drum.DRUMS[d]);
			drumLayer1MidiNote[d].setValue(conf.getLayer1MidiNote());
			drumLayer1BaseVelocity[d].setValue(conf.getLayer1BaseVelocity());
			drumLayer1AccentVelocity[d].setValue(conf.getLayer1AccentVelocity());
			drumLayer2MidiNote[d].setValue(conf.getLayer2MidiNote());
			drumLayer2BaseVelocity[d].setValue(conf.getLayer2BaseVelocity());
			drumLayer2AccentVelocity[d].setValue(conf.getLayer2AccentVelocity());

			drumLayer2BaseVelocity[d].setEnabled(conf.getLayer2MidiNote()>=35);
			drumLayer2AccentVelocity[d].setEnabled(conf.getLayer2MidiNote()>=35);
			getSliderForNumber(drumLayer2BaseVelocity[d]).setEnabled(conf.getLayer2MidiNote()>=35);
			getSliderForNumber(drumLayer2AccentVelocity[d]).setEnabled(conf.getLayer2MidiNote()>=35);
		}
		setValidate(true);
	}

	@Override
	public void getCompositionUpdate(String tab, Composition comp) {
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			InstrumentConfiguration inst = comp.getSynthesizerConfiguration().getInstrument(Instrument.INSTRUMENTS[i]);
			inst.setPolyphony(Integer.parseInt(instrumentPolyphony[i].getValue().toString()));
			if (!Instrument.INSTRUMENTS[i].equals(Instrument.DRUMS)) {
				inst.setLayer1MidiNum(Integer.parseInt(instrumentLayer1MidiNum[i].getValue().toString()));
				inst.setLayer1BaseOctave(Integer.parseInt(instrumentLayer1BaseOctave[i].getValue().toString()));
				inst.setLayer1BaseVelocity(Integer.parseInt(instrumentLayer1BaseVelocity[i].getValue().toString()));
				inst.setLayer1AccentVelocity(Integer.parseInt(instrumentLayer1AccentVelocity[i].getValue().toString()));
			}
			if (Instrument.INSTRUMENTS[i].equals(Instrument.SYNTH_BASS1) ||
				Instrument.INSTRUMENTS[i].equals(Instrument.SYNTH1) ||
				Instrument.INSTRUMENTS[i].equals(Instrument.PIANO) ||
				Instrument.INSTRUMENTS[i].equals(Instrument.STRINGS)
				) {
				inst.setLayer2MidiNum(Integer.parseInt(instrumentLayer2MidiNum[i].getValue().toString()));
				inst.setLayer2BaseOctave(Integer.parseInt(instrumentLayer2BaseOctave[i].getValue().toString()));
				inst.setLayer2BaseVelocity(Integer.parseInt(instrumentLayer2BaseVelocity[i].getValue().toString()));
				inst.setLayer2AccentVelocity(Integer.parseInt(instrumentLayer2AccentVelocity[i].getValue().toString()));
			}
		}
		for (int d = 0; d < Drum.DRUMS.length; d++) {
			DrumConfiguration drum = comp.getSynthesizerConfiguration().getDrum(Drum.DRUMS[d]);
			drum.setLayer1MidiNote(Integer.parseInt(drumLayer1MidiNote[d].getValue().toString()));
			drum.setLayer1BaseVelocity(Integer.parseInt(drumLayer1BaseVelocity[d].getValue().toString()));
			drum.setLayer1AccentVelocity(Integer.parseInt(drumLayer1AccentVelocity[d].getValue().toString()));
			drum.setLayer2MidiNote(Integer.parseInt(drumLayer2MidiNote[d].getValue().toString()));
			drum.setLayer2BaseVelocity(Integer.parseInt(drumLayer2BaseVelocity[d].getValue().toString()));
			drum.setLayer2AccentVelocity(Integer.parseInt(drumLayer2AccentVelocity[d].getValue().toString()));
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		if (instrumentLayer1MidiInstrument.size()==Instrument.INSTRUMENTS.length) {
			for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
				if (evt.getSource()==instrumentLayer1MidiNum[i]) {
					JComboBox<String> midiInstrument = instrumentLayer1MidiInstrument.get(i);
					midiInstrument.setSelectedIndex(Integer.parseInt(instrumentLayer1MidiNum[i].getValue().toString()));
					break;
				}
			}
		}
		if (instrumentLayer2MidiInstrument.size()==4) {
			int num = 0;
			for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
				if (instrumentLayer2MidiNum[i]!=null) {
					if (evt.getSource()==instrumentLayer2MidiNum[i]) {
						JComboBox<String> midiInstrument = instrumentLayer2MidiInstrument.get(num);
						int index = (Integer.parseInt(instrumentLayer2MidiNum[i].getValue().toString()) + 1);
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
			for (JComboBox<String> midiInstrument: instrumentLayer1MidiInstrument) {
				if (evt.getSource()==midiInstrument) {
					instrumentLayer1MidiNum[i].setValue(midiInstrument.getSelectedIndex());
					break;
				}
				i++;
			}
			JComboBox<String> selector = null;
			i = 0;
			for (JComboBox<String> midiInstrument: instrumentLayer2MidiInstrument) {
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
					i = 6;
				} else if (i==3) {
					i = 7;
				}
				instrumentLayer2MidiNum[i].setValue((selector.getSelectedIndex() - 1));
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
			instrumentLayer1MidiNum[instrumentNum] = getNewNumberTextField(3);
			addLabel(panel,row,"Layer 1 MIDI instrument number");
			if (midiInstruments.size()>0) {
				JPanel selector = getMidiInstrumentSelector(name,instrumentLayer1MidiNum[instrumentNum],midiInstruments,false);
				addProperty(panel,row,selector);
			} else {
				addProperty(panel,row,instrumentLayer1MidiNum[instrumentNum]);
			}

			row++;
			instrumentLayer1BaseOctave[instrumentNum] = getNewNumberTextField(1);
			slider = getNewNumberSlider(instrumentLayer1BaseOctave[instrumentNum],0,9,3);
			addLabel(panel,row,"Layer 1 base octave");
			addProperty(panel,row,slider);

			row++;
			instrumentLayer1BaseVelocity[instrumentNum] = getNewNumberTextField(3);
			slider = getNewNumberSlider(instrumentLayer1BaseVelocity[instrumentNum],0,127,100);
			addLabel(panel,row,"Layer 1 base velocity");
			addProperty(panel,row,slider);
	
			row++;
			instrumentLayer1AccentVelocity[instrumentNum] = getNewNumberTextField(3);
			slider = getNewNumberSlider(instrumentLayer1AccentVelocity[instrumentNum],0,127,100);
			addLabel(panel,row,"Layer 1 accent velocity");
			addProperty(panel,row,slider);

			if (name.equals(Instrument.SYNTH_BASS1) ||
				name.equals(Instrument.SYNTH1) ||
				name.equals(Instrument.PIANO) ||
				name.equals(Instrument.STRINGS)
				) {
				row++;
				instrumentLayer2MidiNum[instrumentNum] = getNewNumberTextField(3);
				addLabel(panel,row,"Layer 2 MIDI instrument number");
				if (midiInstruments.size()>0) {
					JPanel selector = getMidiInstrumentSelector(name,instrumentLayer2MidiNum[instrumentNum],midiInstruments,true);
					addProperty(panel,row,selector);
				} else {
					addProperty(panel,row,instrumentLayer2MidiNum[instrumentNum]);
				}

				row++;
				instrumentLayer2BaseOctave[instrumentNum] = getNewNumberTextField(1);
				slider = getNewNumberSlider(instrumentLayer2BaseOctave[instrumentNum],0,9,3);
				addLabel(panel,row,"Layer 2 base octave");
				addProperty(panel,row,slider);

				row++;
				instrumentLayer2BaseVelocity[instrumentNum] = getNewNumberTextField(3);
				slider = getNewNumberSlider(instrumentLayer2BaseVelocity[instrumentNum],0,127,100);
				addLabel(panel,row,"Layer 2 base velocity");
				addProperty(panel,row,slider);
		
				row++;
				instrumentLayer2AccentVelocity[instrumentNum] = getNewNumberTextField(3);
				slider = getNewNumberSlider(instrumentLayer2AccentVelocity[instrumentNum],0,127,100);
				addLabel(panel,row,"Layer 2 accent velocity");
				addProperty(panel,row,slider);
			}
		}

		if (name.equals(Instrument.DRUMS)) {
			instrumentLayer1MidiInstrument.add(null);
			for (int d = 0; d < Drum.DRUMS.length; d++) {
				int drow = 0;
				JPanel drumPanel = new JPanel();
				drumPanel.setLayout(new GridBagLayout());
				drumPanel.setBorder(BorderFactory.createTitledBorder(Drum.DRUMS[d]));

				drumLayer1MidiNote[d] = getNewNumberTextField(3);
				slider = getNewNumberSlider(drumLayer1MidiNote[d],35,81,35);
				addLabel(drumPanel,drow,"Layer 1 MIDI note number");
				addProperty(drumPanel,drow,slider);
				
				drow++;
				drumLayer1BaseVelocity[d] = getNewNumberTextField(3);
				slider = getNewNumberSlider(drumLayer1BaseVelocity[d],0,127,100);
				addLabel(drumPanel,drow,"Layer 1 Base velocity");
				addProperty(drumPanel,drow,slider);
		
				drow++;
				drumLayer1AccentVelocity[d] = getNewNumberTextField(3);
				slider = getNewNumberSlider(drumLayer1AccentVelocity[d],0,127,100);
				addLabel(drumPanel,drow,"Layer 1 Accent velocity");
				addProperty(drumPanel,drow,slider);
				
				drow++;
				drumLayer2MidiNote[d] = getNewNumberTextField(3);
				slider = getNewNumberSlider(drumLayer2MidiNote[d],34,81,35);
				addLabel(drumPanel,drow,"Layer 2 Layer MIDI note number");
				addProperty(drumPanel,drow,slider);
				
				drow++;
				drumLayer2BaseVelocity[d] = getNewNumberTextField(3);
				slider = getNewNumberSlider(drumLayer2BaseVelocity[d],0,127,100);
				addLabel(drumPanel,drow,"Layer 2 Layer base velocity");
				addProperty(drumPanel,drow,slider);
		
				drow++;
				drumLayer2AccentVelocity[d] = getNewNumberTextField(3);
				slider = getNewNumberSlider(drumLayer2AccentVelocity[d],0,127,100);
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
			instrumentLayer1MidiInstrument.add(midiInstrument);
		} else {
			instrumentLayer2MidiInstrument.add(midiInstrument);
		}
		return selector;
	}
}
