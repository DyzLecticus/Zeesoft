package nl.zeesoft.zmmt.gui.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.gui.FrameMain;
import nl.zeesoft.zmmt.gui.state.CompositionChangePublisher;
import nl.zeesoft.zmmt.gui.state.StateChangeEvent;
import nl.zeesoft.zmmt.gui.state.StateChangeSubscriber;
import nl.zeesoft.zmmt.synthesizer.Instrument;
import nl.zeesoft.zmmt.synthesizer.InstrumentConfiguration;
import nl.zeesoft.zmmt.synthesizer.SynthesizerConfiguration;

public class PanelMix extends PanelObject implements StateChangeSubscriber, CompositionChangePublisher {
	private	JButton						solo						= null;
	private	JButton						unmute						= null;
	
	private JToggleButton[]				muteButton					= new JToggleButton[Instrument.INSTRUMENTS.length];
	private JLabel[]					volumeLabel					= new JLabel[Instrument.INSTRUMENTS.length];
	private JSlider[]					volumeSlider				= new JSlider[Instrument.INSTRUMENTS.length];
	private JLabel[]					panLabel					= new JLabel[Instrument.INSTRUMENTS.length];
	private JSlider[]					panSlider					= new JSlider[Instrument.INSTRUMENTS.length];
	
	private SynthesizerConfiguration	synthConfigCopy				= null;
	private String						selectedInstrument			= "";

	// TODO: Animated LED volume strips
	
	public PanelMix(Controller controller) {
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
		addComponent(getPanel(), row, 0.01,getMutePanel());

		row++;
		addComponent(getPanel(), row, 0.01,getMixPanel());

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
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			InstrumentConfiguration inst = composition.getSynthesizerConfiguration().getInstrument(Instrument.INSTRUMENTS[i]);
			inst.setMuted(muteButton[i].isSelected());
			inst.setVolume(volumeSlider[i].getValue());
			inst.setPan(panSlider[i].getValue());
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getActionCommand().equals(FrameMain.SOLO)) {
			for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
				InstrumentConfiguration inst = synthConfigCopy.getInstrument(Instrument.INSTRUMENTS[i]);
				if (Instrument.INSTRUMENTS[i].equals(selectedInstrument)) {
					inst.setMuted(false);
				} else {
					inst.setMuted(true);
				}
			}
			for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
				if (Instrument.INSTRUMENTS[i].equals(selectedInstrument)) {
					if (muteButton[i].isSelected()) {
						muteButton[i].doClick();
					}
				} else {
					if (!muteButton[i].isSelected()) {
						muteButton[i].doClick();
					}
				}
			}
			getController().getStateManager().addWaitingPublisher(this);
		} else if (evt.getActionCommand().equals(FrameMain.UNMUTE)) {
			for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
				InstrumentConfiguration inst = synthConfigCopy.getInstrument(Instrument.INSTRUMENTS[i]);
				inst.setMuted(false);
			}
			for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
				if (muteButton[i].isSelected()) {
					muteButton[i].doClick();
				}
			}
			getController().getStateManager().addWaitingPublisher(this);
		} else {
			for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
				if (evt.getSource()==muteButton[i]) {
					if (!muteButton[i].isOpaque()==muteButton[i].isSelected()) {
						muteButton[i].setOpaque(muteButton[i].isSelected());
					}
					InstrumentConfiguration inst = synthConfigCopy.getInstrument(Instrument.INSTRUMENTS[i]);
					if (muteButton[i].isSelected()!=inst.isMuted()) {
						inst.setMuted(muteButton[i].isSelected());
						getController().getStateManager().addWaitingPublisher(this);
					}
					break;
				}
			}
		}
	}

	@Override
	public void handleValidChange() {
		updateLabels();
		getController().getStateManager().addWaitingPublisher(this);
	}

	protected JPanel getMutePanel() {
		JPanel r = new JPanel();
		r.setLayout(new BoxLayout(r,BoxLayout.X_AXIS));
		solo = new JButton("Solo");
		solo.setActionCommand(FrameMain.SOLO);
		solo.addActionListener(this);
		solo.addKeyListener(getController().getPlayerKeyListener());
		addControlPageUpDownOverridesToComponent(solo);
		unmute = new JButton("Unmute all");
		unmute.setActionCommand(FrameMain.UNMUTE);
		unmute.addActionListener(this);
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
		JPanel r = new JPanel();
		r.setLayout(new BoxLayout(r,BoxLayout.Y_AXIS));
		
		Color col = Instrument.getColorForInstrument(Instrument.INSTRUMENTS[i]);
		
		JLabel label = new JLabel(Instrument.INSTRUMENT_SHORTS[i]);
		label.setOpaque(true);
		label.setBackground(col);
		label.setBorder(BorderFactory.createLineBorder(col,2,true));
		muteButton[i] = new JToggleButton("M");
		muteButton[i].addActionListener(this);
		muteButton[i].setBackground(Color.RED);
		volumeLabel[i] = new JLabel();
		volumeSlider[i] = new JSlider(JSlider.VERTICAL,0,127,110);
		volumeSlider[i].addChangeListener(this);
		panLabel[i] = new JLabel();
		panSlider[i] = new JSlider(JSlider.HORIZONTAL,0,127,64);
		panSlider[i].setPreferredSize(new Dimension(50,50));
		panSlider[i].setMaximumSize(new Dimension(50,50));
		panSlider[i].addChangeListener(this);

		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		muteButton[i].setAlignmentX(Component.CENTER_ALIGNMENT);
		volumeLabel[i].setAlignmentX(Component.CENTER_ALIGNMENT);
		volumeSlider[i].setAlignmentX(Component.CENTER_ALIGNMENT);
		panLabel[i].setAlignmentX(Component.CENTER_ALIGNMENT);
		panSlider[i].setAlignmentX(Component.CENTER_ALIGNMENT);
		
		muteButton[i].addKeyListener(getController().getPlayerKeyListener());
		volumeLabel[i].addKeyListener(getController().getPlayerKeyListener());
		volumeSlider[i].addKeyListener(getController().getPlayerKeyListener());
		panLabel[i].addKeyListener(getController().getPlayerKeyListener());
		panSlider[i].addKeyListener(getController().getPlayerKeyListener());
		
		addControlPageUpDownOverridesToComponent(muteButton[i]);
		addControlPageUpDownOverridesToComponent(volumeLabel[i]);
		addControlPageUpDownOverridesToComponent(volumeSlider[i]);
		addControlPageUpDownOverridesToComponent(panLabel[i]);
		addControlPageUpDownOverridesToComponent(panSlider[i]);

		r.add(label);
		r.add(muteButton[i]);
		r.add(volumeLabel[i]);
		r.add(volumeSlider[i]);
		r.add(panLabel[i]);
		r.add(panSlider[i]);
		
		return r;
	}
	
	protected void updatedSynthConfig() {
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			InstrumentConfiguration inst = synthConfigCopy.getInstrument(Instrument.INSTRUMENTS[i]);
			if (inst.isMuted()!=muteButton[i].isSelected()) {
				muteButton[i].doClick();
			}
			volumeSlider[i].setValue(inst.getVolume());
			panSlider[i].setValue(inst.getPan());
			boolean enabled = true;
			if (Instrument.INSTRUMENTS[i].equals(Instrument.ECHO) && synthConfigCopy.getEcho().getInstrument().length()>0) {
				enabled = false;
			}
			muteButton[i].setEnabled(enabled);
			volumeSlider[i].setEnabled(enabled);
			panSlider[i].setEnabled(enabled);
		}
		updateLabels();
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
