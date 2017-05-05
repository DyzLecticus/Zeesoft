package nl.zeesoft.zmmt.gui.panel;

import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
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
import nl.zeesoft.zmmt.gui.CompositionUpdater;
import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.gui.FrameMain;
import nl.zeesoft.zmmt.syntesizer.Drum;
import nl.zeesoft.zmmt.syntesizer.DrumConfiguration;
import nl.zeesoft.zmmt.syntesizer.Instrument;
import nl.zeesoft.zmmt.syntesizer.InstrumentConfiguration;

public class PanelInstruments extends PanelObject implements ItemListener, CompositionUpdater {
	private JComboBox<String>		instrument						= null;
	private JPanel					cardPanel						= null;
	private JFormattedTextField[]	instrumentPolyphony				= new JFormattedTextField[Instrument.INSTRUMENTS.length];

	private JFormattedTextField[]	instrumentLayer1MidiNum			= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	instrumentLayer1Pressure		= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	instrumentLayer1Reverb			= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	instrumentLayer1BaseOctave		= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	instrumentLayer1BaseVelocity	= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	instrumentLayer1AccentVelocity	= new JFormattedTextField[Instrument.INSTRUMENTS.length];

	private JFormattedTextField[]	instrumentLayer2MidiNum			= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	instrumentLayer2Pressure		= new JFormattedTextField[Instrument.INSTRUMENTS.length];
	private JFormattedTextField[]	instrumentLayer2Reverb			= new JFormattedTextField[Instrument.INSTRUMENTS.length];
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
		for (int i = 0; i < (Instrument.INSTRUMENTS.length - 1); i++) {
			InstrumentConfiguration conf = comp.getSynthesizerConfiguration().getInstrument(Instrument.INSTRUMENTS[i]);
			instrumentPolyphony[i].setValue(conf.getPolyphony());
			if (instrumentLayer1MidiNum[i]!=null) {
				instrumentLayer1MidiNum[i].setValue(conf.getLayer1MidiNum());
			}
			instrumentLayer1Pressure[i].setValue(conf.getLayer1Pressure());
			instrumentLayer1Reverb[i].setValue(conf.getLayer1Reverb());
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
				instrumentLayer2Pressure[i].setValue(conf.getLayer2Pressure());
				instrumentLayer2Reverb[i].setValue(conf.getLayer2Reverb());
				instrumentLayer2BaseOctave[i].setValue(conf.getLayer2BaseOctave());
				instrumentLayer2BaseVelocity[i].setValue(conf.getLayer2BaseVelocity());
				instrumentLayer2AccentVelocity[i].setValue(conf.getLayer2AccentVelocity());

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

			getSliderForNumber(drumLayer2BaseVelocity[d]).setEnabled(conf.getLayer2MidiNote()>=35);
			getSliderForNumber(drumLayer2AccentVelocity[d]).setEnabled(conf.getLayer2MidiNote()>=35);
		}
		setValidate(true);
	}

	@Override
	public void getCompositionUpdate(String tab, Composition comp) {
		for (int i = 0; i < (Instrument.INSTRUMENTS.length - 1); i++) {
			InstrumentConfiguration inst = comp.getSynthesizerConfiguration().getInstrument(Instrument.INSTRUMENTS[i]);
			inst.setPolyphony(Integer.parseInt(instrumentPolyphony[i].getValue().toString()));
			inst.setLayer1MidiNum(Integer.parseInt(instrumentLayer1MidiNum[i].getValue().toString()));
			inst.setLayer1Pressure(Integer.parseInt(instrumentLayer1Pressure[i].getValue().toString()));
			inst.setLayer1Reverb(Integer.parseInt(instrumentLayer1Reverb[i].getValue().toString()));
			if (!Instrument.INSTRUMENTS[i].equals(Instrument.DRUMS)) {
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
				inst.setLayer2Pressure(Integer.parseInt(instrumentLayer2Pressure[i].getValue().toString()));
				inst.setLayer2Reverb(Integer.parseInt(instrumentLayer2Reverb[i].getValue().toString()));
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
	public void itemStateChanged(ItemEvent evt) {
		if (evt.getSource()==instrument) {
			CardLayout cl = (CardLayout)(cardPanel.getLayout());
			cl.show(cardPanel,(String) evt.getItem());
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

		row++;
		instrumentLayer1MidiNum[instrumentNum] = getNewNumberTextField(3);
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
		instrumentLayer1Pressure[instrumentNum] = getNewNumberTextField(3);
		slider = getNewNumberSlider(instrumentLayer1Pressure[instrumentNum],0,127,0);
		addLabel(panel,row,"Layer 1 pressure");
		addProperty(panel,row,slider);

		row++;
		instrumentLayer1Reverb[instrumentNum] = getNewNumberTextField(3);
		slider = getNewNumberSlider(instrumentLayer1Reverb[instrumentNum],0,127,64);
		addLabel(panel,row,"Layer 1 reverb");
		addProperty(panel,row,slider);

		if (!name.equals(Instrument.DRUMS)) {
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
					JPanel selector = getMidiInstrumentSelector(instrumentLayer2MidiNum[instrumentNum],midiInstruments,true);
					addProperty(panel,row,selector);
				} else {
					addProperty(panel,row,instrumentLayer2MidiNum[instrumentNum]);
				}

				row++;
				instrumentLayer2Pressure[instrumentNum] = getNewNumberTextField(3);
				slider = getNewNumberSlider(instrumentLayer2Pressure[instrumentNum],0,127,0);
				addLabel(panel,row,"Layer 2 pressure");
				addProperty(panel,row,slider);

				row++;
				instrumentLayer2Reverb[instrumentNum] = getNewNumberTextField(3);
				slider = getNewNumberSlider(instrumentLayer2Reverb[instrumentNum],0,127,64);
				addLabel(panel,row,"Layer 2 reverb");
				addProperty(panel,row,slider);

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
	
	protected JPanel getMidiInstrumentSelector(JFormattedTextField number,List<String> midiInstruments,boolean layer) {
		List<String> options = new ArrayList<String>(midiInstruments);
		int subtract = 0;
		if (layer) {
			options.add(0,"");
			subtract = -1;
		}
		return getNewNumberComboBox(number,options,subtract);
	}
}
