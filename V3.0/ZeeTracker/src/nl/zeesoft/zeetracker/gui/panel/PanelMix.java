package nl.zeesoft.zeetracker.gui.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import nl.zeesoft.zeetracker.gui.Controller;
import nl.zeesoft.zeetracker.gui.FrameMain;
import nl.zeesoft.zeetracker.gui.state.CompositionChangePublisher;
import nl.zeesoft.zeetracker.gui.state.StateChangeEvent;
import nl.zeesoft.zeetracker.gui.state.StateChangeSubscriber;
import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.sequencer.CompositionToSequenceConvertor;
import nl.zeesoft.zmmt.sequencer.SequencePlayerSubscriber;
import nl.zeesoft.zmmt.synthesizer.Drum;
import nl.zeesoft.zmmt.synthesizer.DrumConfiguration;
import nl.zeesoft.zmmt.synthesizer.Instrument;
import nl.zeesoft.zmmt.synthesizer.InstrumentConfiguration;
import nl.zeesoft.zmmt.synthesizer.SynthesizerConfiguration;

public class PanelMix extends PanelObject implements ItemListener, StateChangeSubscriber, CompositionChangePublisher, MetaEventListener, SequencePlayerSubscriber {
	private	static final String			TOGGLE_MUTE					= "TOGGLE_MUTE";

	private	static final String			TOGGLE_DRUM_MUTE			= "TOGGLE_DRUM_MUTE";
	private	static final String[]		DRUM_SHORTS					= {"KCK","SNR","HHS","CLP","TMS","RD","CMB","FX"};
	
	private	JButton						solo						= null;
	private	JButton						unmute						= null;

	private JLabel						sequence					= null;
	private JLabel						pattern						= null;
	private JLabel						step						= null;
	
	private JButton[]					muteButton					= new JButton[Instrument.INSTRUMENTS.length];
	private JLabel[]					volumeLabel					= new JLabel[Instrument.INSTRUMENTS.length];
	private JSlider[]					volumeSlider				= new JSlider[Instrument.INSTRUMENTS.length];
	private JLabel[]					panLabel					= new JLabel[Instrument.INSTRUMENTS.length];
	private JSlider[]					panSlider					= new JSlider[Instrument.INSTRUMENTS.length];
	private MixerStrip[]				strip						= new MixerStrip[16];

	private JButton[]					muteDrumButton				= new JButton[DRUM_SHORTS.length];
	
	private JComboBox<String>			sideChainSource				= null;
	private JSlider						sideChainAttack				= null;
	private JSlider						sideChainSustain			= null;
	private JSlider						sideChainRelease			= null;

	private JSlider[]					sideChainPercentage			= new JSlider[Instrument.INSTRUMENTS.length];
	
	private SynthesizerConfiguration	synthConfigCopy				= null;
	private String						selectedInstrument			= "";
	
	public PanelMix(Controller controller) {
		super(controller);
		controller.getStateManager().addSubscriber(this);
		controller.addSequencerMetaListener(this);
		controller.addSequencerSubscriber(this);
		selectedInstrument = controller.getStateManager().getSelectedInstrument();
	}

	@Override
	public void initialize() {
		setValidate(false);
		getPanel().addKeyListener(getController().getPlayerKeyListener());
		getPanel().setLayout(new GridBagLayout());
		getPanel().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		int row = 0;
		addComponent(getPanel(), row, 0.01,getPlayingPanel());

		row++;
		addComponent(getPanel(), row, 0.01,getMutePanel());

		row++;
		addComponent(getPanel(), row, 0.01,getMixPanel());

		row++;
		addComponent(getPanel(), row, 0.01,getSideChainPanel(),false);

		row++;
		addFiller(getPanel(),row);
	}

	@Override
	public void requestFocus() {
		solo.requestFocus();
	}

	@Override
	public void handleStateChange(StateChangeEvent evt) {
		setValidate(false);
		if (evt.getType().equals(StateChangeEvent.SELECTED_INSTRUMENT)) {
			selectedInstrument = evt.getSelectedInstrument();
		} else if (evt.getSource()!=this && evt.getType().equals(StateChangeEvent.CHANGED_COMPOSITION)) {
			synthConfigCopy = evt.getComposition().getSynthesizerConfiguration().copy();
			updatedSynthConfig();
		}
		setValidate(true);
	}

	@Override
	public void setChangesInComposition(Composition composition) {
		composition.getSynthesizerConfiguration().setSideChainSource(sideChainSource.getSelectedItem().toString());
		composition.getSynthesizerConfiguration().setSideChainAttack(((double) sideChainAttack.getValue()) / 10);
		composition.getSynthesizerConfiguration().setSideChainSustain(((double) sideChainSustain.getValue()) / 10);
		composition.getSynthesizerConfiguration().setSideChainRelease(((double) sideChainRelease.getValue()) / 10);
		for (int d = 0; d < Drum.DRUMS.length; d++) {
			DrumConfiguration drum = composition.getSynthesizerConfiguration().getDrum(Drum.DRUMS[d]);
			DrumConfiguration copy = synthConfigCopy.getDrum(Drum.DRUMS[d]);
			drum.setMuted(copy.isMuted());
		}
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			InstrumentConfiguration inst = composition.getSynthesizerConfiguration().getInstrument(Instrument.INSTRUMENTS[i]);
			InstrumentConfiguration copy = synthConfigCopy.getInstrument(Instrument.INSTRUMENTS[i]);
			inst.setMuted(copy.isMuted());
			inst.setVolume(volumeSlider[i].getValue());
			inst.setPan(panSlider[i].getValue());
			if (!inst.getName().equals(Instrument.DRUMS)) {
				inst.setSideChainPercentage(sideChainPercentage[i].getValue());
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getActionCommand().equals(FrameMain.SOLO)) {
			boolean changed = false;
			for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
				InstrumentConfiguration inst = synthConfigCopy.getInstrument(Instrument.INSTRUMENTS[i]);
				if (Instrument.INSTRUMENTS[i].equals(selectedInstrument)) {
					if (inst.isMuted()) {
						inst.setMuted(false);
						changed = true;
					}
				} else {
					if (!inst.isMuted()) {
						inst.setMuted(true);
						changed = true;
					}
				}
			}
			if (changed) {
				getController().getStateManager().addWaitingPublisher(this);
				updatedMuteState();
			}
		} else if (evt.getActionCommand().equals(FrameMain.UNMUTE)) {
			boolean changed = false;
			for (int d = 0; d < Drum.DRUMS.length; d++) {
				DrumConfiguration drum = synthConfigCopy.getDrum(Drum.DRUMS[d]);
				if (drum.isMuted()) {
					drum.setMuted(false);
					changed = true;
				}
			}
			for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
				InstrumentConfiguration inst = synthConfigCopy.getInstrument(Instrument.INSTRUMENTS[i]);
				if (inst.isMuted()) {
					inst.setMuted(false);
					changed = true;
				}
			}
			if (changed) {
				getController().getStateManager().addWaitingPublisher(this);
				updatedDrumMuteState();
				updatedMuteState();
			}
		} else if (evt.getActionCommand().equals(TOGGLE_MUTE)) {
			for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
				if (evt.getSource()==muteButton[i]) {
					InstrumentConfiguration inst = synthConfigCopy.getInstrument(Instrument.INSTRUMENTS[i]);
					inst.setMuted(!inst.isMuted());
					break;
				}
			}
			getController().getStateManager().addWaitingPublisher(this);
			updatedMuteState();
		} else if (evt.getActionCommand().equals(TOGGLE_DRUM_MUTE)) {
			for (int d = 0; d < DRUM_SHORTS.length; d++) {
				if (evt.getSource()==muteDrumButton[d]) {
					List<DrumConfiguration> drums = new ArrayList<DrumConfiguration>();
					if (d==0) {
						drums.add(synthConfigCopy.getDrum(Drum.KICK));
					} else if (d==1) {
						drums.add(synthConfigCopy.getDrum(Drum.SNARE));
					} else if (d==2) {
						drums.add(synthConfigCopy.getDrum(Drum.HIHAT1));
						drums.add(synthConfigCopy.getDrum(Drum.HIHAT2));
					} else if (d==3) {
						drums.add(synthConfigCopy.getDrum(Drum.CLAP));
					} else if (d==4) {
						drums.add(synthConfigCopy.getDrum(Drum.TOM1));
						drums.add(synthConfigCopy.getDrum(Drum.TOM2));
					} else if (d==5) {
						drums.add(synthConfigCopy.getDrum(Drum.RIDE));
					} else if (d==6) {
						drums.add(synthConfigCopy.getDrum(Drum.CYMBAL));
					} else if (d==7) {
						drums.add(synthConfigCopy.getDrum(Drum.FX1));
						drums.add(synthConfigCopy.getDrum(Drum.FX2));
						drums.add(synthConfigCopy.getDrum(Drum.FX3));
					}
					for (DrumConfiguration drum: drums) {
						drum.setMuted(!drum.isMuted());
					}
					break;
				}
			}
			getController().getStateManager().addWaitingPublisher(this);
			updatedDrumMuteState();
		}
	}

	@Override
	public void handleValidChange() {
		updateLabels();
		getController().getStateManager().addWaitingPublisher(this);
	}


	@Override
	public void started() {
		// Ignore
	}

	@Override
	public void stopped() {
		sequence.setText("---");
		pattern.setText("--");
		step.setText("---");
		for (int c = 0; c < 16; c++) {
			strip[c].setValue(0);
		}
	}

	@Override
	public void meta(MetaMessage meta) {
		if (meta.getType()==CompositionToSequenceConvertor.MARKER) {
			String txt = new String(meta.getData());
			String[] d = txt.split(":");
			if (txt.startsWith(CompositionToSequenceConvertor.VELOCITY_MARKER)) {
				for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
					int layers = 2;
					if (Instrument.INSTRUMENTS[i].equals(Instrument.DRUMS)) {
						layers = 1;
					} else if (Instrument.INSTRUMENTS[i].equals(Instrument.ECHO)) {
						layers = 3;
					}
					for (int l = 0; l < layers; l++) {
						int c = Instrument.getMidiChannelForInstrument(Instrument.INSTRUMENTS[i],l);
						if (c>=0) {
							int value = Integer.parseInt(d[c + 1]);
							strip[c].setValue(value);
						}
					}
				}
			} else if (txt.startsWith(CompositionToSequenceConvertor.SEQUENCE_MARKER)) {
				int index = Integer.parseInt(d[1]);
				if (index<0) {
					sequence.setText("---");
				} else {
					sequence.setText(String.format("%03d",index));
				}
			} else if (txt.startsWith(CompositionToSequenceConvertor.PATTERN_STEP_MARKER)) {
				int p = Integer.parseInt(d[1]);
				int s = Integer.parseInt(d[2]);
				if (p<0) {
					pattern.setText("--");
				} else {
					pattern.setText(String.format("%02d",p));
				}
				if (s<0) {
					step.setText("---");
				} else {
					step.setText(String.format("%03d",s));
				}
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent evt) {
		if (evt.getSource()==sideChainSource) {
			handlePropertyChanged(evt.getSource());
		}
	}

	protected JPanel getPlayingPanel() {
		JPanel r = new JPanel();
		r.setLayout(new BoxLayout(r,BoxLayout.X_AXIS));
		r.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
		
		JLabel playing = new JLabel("Playing");
		sequence = new JLabel("---");
		JLabel sep1 = new JLabel("/");
		pattern = new JLabel("--");
		JLabel sep2 = new JLabel("/");
		step = new JLabel("---");
		
		playing.setFocusable(false);
		sequence.setFocusable(false);
		sep1.setFocusable(false);
		pattern.setFocusable(false);
		sep2.setFocusable(false);
		step.setFocusable(false);

		r.add(playing);
		r.add(Box.createRigidArea(new Dimension(10,0)));
		r.add(sequence);
		r.add(Box.createRigidArea(new Dimension(5,0)));
		r.add(sep1);
		r.add(Box.createRigidArea(new Dimension(5,0)));
		r.add(pattern);
		r.add(Box.createRigidArea(new Dimension(5,0)));
		r.add(sep2);
		r.add(Box.createRigidArea(new Dimension(5,0)));
		r.add(step);
		
		return r;
	}

	protected JPanel getMutePanel() {
		JPanel r = new JPanel();
		r.setLayout(new BoxLayout(r,BoxLayout.X_AXIS));
		solo = new JButton("Solo");
		solo.setActionCommand(FrameMain.SOLO);
		solo.addActionListener(this);
		solo.addFocusListener(this);
		solo.addKeyListener(getController().getPlayerKeyListener());
		addControlPageUpDownOverridesToComponent(solo);
		unmute = new JButton("Unmute all");
		unmute.setActionCommand(FrameMain.UNMUTE);
		unmute.addActionListener(this);
		unmute.addFocusListener(this);
		unmute.addKeyListener(getController().getPlayerKeyListener());
		addControlPageUpDownOverridesToComponent(unmute);
		r.add(solo);
		r.add(unmute);
		return r;
	}
	
	protected JPanel getMixPanel() {
		JPanel r = new JPanel();
		r.setLayout(new BoxLayout(r,BoxLayout.X_AXIS));
		r.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			r.add(getColumnForInstrument(i));
		}
		return r;
	}
	
	protected JPanel getColumnForInstrument(int i) {
		Color col = Instrument.getColorForInstrument(Instrument.INSTRUMENTS[i]);

		JPanel r = new JPanel();
		r.setLayout(new BoxLayout(r,BoxLayout.Y_AXIS));
		
		JPanel strips = new JPanel();
		strips.setLayout(new BoxLayout(strips,BoxLayout.X_AXIS));
		
		if (Instrument.INSTRUMENTS[i].equals(Instrument.DRUMS)) {
			strips.add(getDrumMutes());
			strips.add(Box.createRigidArea(new Dimension(5,0)));
		}
		
		int layers = 2;
		if (Instrument.INSTRUMENTS[i].equals(Instrument.DRUMS)) {
			layers = 1;
		} else if (Instrument.INSTRUMENTS[i].equals(Instrument.ECHO)) {
			layers = 3;
		}
		for (int l = 0; l < layers; l++) {
			int c = Instrument.getMidiChannelForInstrument(Instrument.INSTRUMENTS[i],l);
			if (c>=0) {
				if (l>0) {
					strips.add(Box.createRigidArea(new Dimension(5,0)));
				}
				strip[c] = new MixerStrip(col);
				strip[c].setBorder(BorderFactory.createEmptyBorder());
				strips.add(strip[c]);
			}
		}
		
		JLabel label = new JLabel(Instrument.INSTRUMENT_SHORTS[i]);
		label.setOpaque(true);
		label.setBackground(col);
		label.setBorder(BorderFactory.createLineBorder(col,2,true));
		label.setFocusable(false);
		muteButton[i] = new JButton(" M ");
		muteButton[i].setOpaque(true);
		muteButton[i].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,2));
		volumeLabel[i] = new JLabel();
		volumeLabel[i].setFocusable(false);
		volumeSlider[i] = new JSlider(JSlider.VERTICAL,0,127,110);
		panLabel[i] = new JLabel();
		panLabel[i].setFocusable(false);
		panSlider[i] = new JSlider(JSlider.HORIZONTAL,0,127,64);
		panSlider[i].setPreferredSize(new Dimension(60,30));
		panSlider[i].setMaximumSize(new Dimension(60,30));

		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		muteButton[i].setAlignmentX(Component.CENTER_ALIGNMENT);
		volumeLabel[i].setAlignmentX(Component.CENTER_ALIGNMENT);
		volumeSlider[i].setAlignmentX(Component.CENTER_ALIGNMENT);
		panLabel[i].setAlignmentX(Component.CENTER_ALIGNMENT);
		panSlider[i].setAlignmentX(Component.CENTER_ALIGNMENT);

		muteButton[i].addFocusListener(this);
		volumeSlider[i].addFocusListener(this);
		panSlider[i].addFocusListener(this);
		
		muteButton[i].addKeyListener(getController().getPlayerKeyListener());
		volumeSlider[i].addKeyListener(getController().getPlayerKeyListener());
		panSlider[i].addKeyListener(getController().getPlayerKeyListener());
		
		addControlPageUpDownOverridesToComponent(muteButton[i]);
		addControlPageUpDownOverridesToComponent(volumeLabel[i]);
		addControlPageUpDownOverridesToComponent(volumeSlider[i]);
		addControlPageUpDownOverridesToComponent(panLabel[i]);
		addControlPageUpDownOverridesToComponent(panSlider[i]);
		
		muteButton[i].setActionCommand(TOGGLE_MUTE);
		muteButton[i].addActionListener(this);
		volumeSlider[i].addChangeListener(this);
		panSlider[i].addChangeListener(this);
		
		strips.add(Box.createRigidArea(new Dimension(5,0)));
		strips.add(volumeSlider[i]);
		
		r.add(label);
		r.add(Box.createRigidArea(new Dimension(0,5)));
		r.add(muteButton[i]);
		r.add(Box.createRigidArea(new Dimension(0,5)));
		r.add(volumeLabel[i]);
		r.add(Box.createRigidArea(new Dimension(0,5)));
		r.add(strips);
		r.add(Box.createRigidArea(new Dimension(0,5)));
		r.add(panLabel[i]);
		r.add(Box.createRigidArea(new Dimension(0,5)));
		r.add(panSlider[i]);
		
		return r;
	}

	protected JPanel getSideChainPanel() {
		JPanel r = new JPanel();
		r.setLayout(new BoxLayout(r,BoxLayout.X_AXIS));
		r.setBorder(BorderFactory.createTitledBorder("Side chain compression"));
		
		JPanel source = new JPanel();
		source.setLayout(new GridBagLayout());
		int row = 0;
		sideChainSource = new JComboBox<String>();
		sideChainSource.addItem("");
		sideChainSource.addItem(SynthesizerConfiguration.SOURCE_KICK);
		sideChainSource.addItem(SynthesizerConfiguration.SOURCE_MIDI);
		sideChainSource.addItemListener(this);
		for (int l = 0; l < sideChainSource.getKeyListeners().length; l++) {
			sideChainSource.removeKeyListener(sideChainSource.getKeyListeners()[l]);
		}
		sideChainSource.addFocusListener(this);
		sideChainSource.addKeyListener(getController().getPlayerKeyListener());
		addFunctionKeyOverridesToComponent(sideChainSource);
		addLabelProperty(source,row,"Source",sideChainSource);

		row++;
		sideChainAttack = addLabelSliderToPanel(source,row,new JLabel("Attack steps"),1,40,5,10);
		row++;
		sideChainSustain = addLabelSliderToPanel(source,row,new JLabel("Sustain steps"),1,40,15,10);
		row++;
		sideChainRelease = addLabelSliderToPanel(source,row,new JLabel("Release steps"),1,80,20,10);
		row++;
		addFiller(source,row);
		
		JPanel destination = new JPanel();
		destination.setLayout(new GridBagLayout());
		row = 0;
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			if (!Instrument.INSTRUMENTS[i].equals(Instrument.DRUMS)) {
				Color col = Instrument.getColorForInstrument(Instrument.INSTRUMENTS[i]);
				JLabel label = new JLabel(Instrument.INSTRUMENT_SHORTS[i]);
				label.setOpaque(true);
				label.setBackground(col);
				label.setBorder(BorderFactory.createLineBorder(col,2,true));
				label.setFocusable(false);
				row++;
				sideChainPercentage[i] = addLabelSliderToPanel(destination,row,label,0,100,0);
			}
		}
		
		source.setAlignmentY(Component.TOP_ALIGNMENT);
		destination.setAlignmentY(Component.TOP_ALIGNMENT);

		r.add(source);
		r.add(Box.createRigidArea(new Dimension(10,0)));
		r.add(destination);
		
		return r;
	}

	protected JPanel getDrumMutes() {
		JPanel r = new JPanel();
		r.setLayout(new BoxLayout(r,BoxLayout.Y_AXIS));
		
		Color col = Instrument.getColorForInstrument(Instrument.DRUMS);
		
		for (int d = 0; d < DRUM_SHORTS.length; d++) {
			JLabel label = new JLabel(DRUM_SHORTS[d]);
			label.setOpaque(true);
			label.setBackground(col);
			label.setBorder(BorderFactory.createLineBorder(col,2,true));
			label.setFocusable(false);
			
			muteDrumButton[d] = new JButton(" M ");
			muteDrumButton[d].setOpaque(true);
			muteDrumButton[d].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,2));
			muteDrumButton[d].addFocusListener(this);
			muteDrumButton[d].addKeyListener(getController().getPlayerKeyListener());
			muteDrumButton[d].setActionCommand(TOGGLE_DRUM_MUTE);
			muteDrumButton[d].addActionListener(this);

			label.setAlignmentX(Component.RIGHT_ALIGNMENT);
			muteDrumButton[d].setAlignmentX(Component.RIGHT_ALIGNMENT);

			JPanel row = new JPanel();
			row.setLayout(new BoxLayout(row,BoxLayout.X_AXIS));
			row.setAlignmentX(Component.RIGHT_ALIGNMENT);
			
			row.add(label);
			row.add(Box.createRigidArea(new Dimension(5,0)));
			row.add(muteDrumButton[d]);
			
			if (d>0) {
				r.add(Box.createRigidArea(new Dimension(0,5)));
			}
			r.add(row);
		}
		
		return r;
	}
	
	protected void updatedSynthConfig() {
		if (synthConfigCopy.getSideChainSource().length()==0) {
			sideChainSource.setSelectedIndex(0);
		} else if (synthConfigCopy.getSideChainSource().equals(SynthesizerConfiguration.SOURCE_KICK)) {
			sideChainSource.setSelectedIndex(1);
		} else if (synthConfigCopy.getSideChainSource().equals(SynthesizerConfiguration.SOURCE_MIDI)) {
			sideChainSource.setSelectedIndex(2);
		}
		sideChainAttack.setValue((int) (synthConfigCopy.getSideChainAttack() * 10));
		sideChainSustain.setValue((int) (synthConfigCopy.getSideChainSustain() * 10));
		sideChainRelease.setValue((int) (synthConfigCopy.getSideChainRelease() * 10));
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			InstrumentConfiguration inst = synthConfigCopy.getInstrument(Instrument.INSTRUMENTS[i]);
			volumeSlider[i].setValue(inst.getVolume());
			panSlider[i].setValue(inst.getPan());
			if (!Instrument.INSTRUMENTS[i].equals(Instrument.DRUMS)) {
				sideChainPercentage[i].setValue(inst.getSideChainPercentage());
			}
			boolean enabled = true;
			if (Instrument.INSTRUMENTS[i].equals(Instrument.ECHO) && synthConfigCopy.getEcho().getInstrument().length()>0) {
				enabled = false;
			}
			muteButton[i].setEnabled(enabled);
			volumeSlider[i].setEnabled(enabled);
			panSlider[i].setEnabled(enabled);
			if (!Instrument.INSTRUMENTS[i].equals(Instrument.DRUMS)) {
				sideChainPercentage[i].setEnabled(enabled);
			}
		}
		updatedMuteState();
		updateLabels();
		updatedDrumMuteState();
	}
	
	protected void updatedMuteState() {
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			InstrumentConfiguration inst = synthConfigCopy.getInstrument(Instrument.INSTRUMENTS[i]);
			if (inst.isMuted()) {
				muteButton[i].setBorder(BorderFactory.createLineBorder(Color.RED,2));
			} else {
				muteButton[i].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,2));
			}
		}
	}

	protected void updatedDrumMuteState() {
		for (int d = 0; d < DRUM_SHORTS.length; d++) {
			List<DrumConfiguration> drums = new ArrayList<DrumConfiguration>();
			if (d==0) {
				drums.add(synthConfigCopy.getDrum(Drum.KICK));
			} else if (d==1) {
				drums.add(synthConfigCopy.getDrum(Drum.SNARE));
			} else if (d==2) {
				drums.add(synthConfigCopy.getDrum(Drum.HIHAT1));
				drums.add(synthConfigCopy.getDrum(Drum.HIHAT2));
			} else if (d==3) {
				drums.add(synthConfigCopy.getDrum(Drum.CLAP));
			} else if (d==4) {
				drums.add(synthConfigCopy.getDrum(Drum.TOM1));
				drums.add(synthConfigCopy.getDrum(Drum.TOM2));
			} else if (d==5) {
				drums.add(synthConfigCopy.getDrum(Drum.RIDE));
			} else if (d==6) {
				drums.add(synthConfigCopy.getDrum(Drum.CYMBAL));
			} else if (d==7) {
				drums.add(synthConfigCopy.getDrum(Drum.FX1));
				drums.add(synthConfigCopy.getDrum(Drum.FX2));
				drums.add(synthConfigCopy.getDrum(Drum.FX3));
			}
			for (DrumConfiguration drum: drums) {
				if (drum.isMuted()) {
					muteDrumButton[d].setBorder(BorderFactory.createLineBorder(Color.RED,2));
					break;
				} else {
					muteDrumButton[d].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,2));
				}
			}
		}
	}
	
	protected void updateLabels() {
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			volumeLabel[i].setText("" + volumeSlider[i].getValue());
			panLabel[i].setText("" + panSlider[i].getValue());
			boolean enabled = true;
			if (Instrument.INSTRUMENTS[i].equals(Instrument.ECHO) && synthConfigCopy.getEcho().getInstrument().length()>0) {
				enabled = false;
			}
			volumeLabel[i].setEnabled(enabled);
			panLabel[i].setEnabled(enabled);
		}
	}
}
