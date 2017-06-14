package nl.zeesoft.zmmt.gui.panel;

import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nl.zeesoft.zmmt.composition.Pattern;
import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.gui.FrameMain;
import nl.zeesoft.zmmt.gui.state.StateChangeEvent;
import nl.zeesoft.zmmt.gui.state.StateChangeSubscriber;
import nl.zeesoft.zmmt.sequencer.CompositionToSequenceConvertor;
import nl.zeesoft.zmmt.sequencer.SequencePlayerSubscriber;
import nl.zeesoft.zmmt.synthesizer.Instrument;

public class PanelMix extends PanelObject implements ActionListener, StateChangeSubscriber, MetaEventListener, SequencePlayerSubscriber {
	private JButton[]				muteButton					= new JButton[Instrument.INSTRUMENTS.length];
	private JLabel[]				volumeLabel					= new JLabel[Instrument.INSTRUMENTS.length];
	private JSlider[]				volumeSlider				= new JSlider[Instrument.INSTRUMENTS.length];
	private JLabel[]				panLabel					= new JLabel[Instrument.INSTRUMENTS.length];
	private JSlider[]				panSlider					= new JSlider[Instrument.INSTRUMENTS.length];

	private JLabel					playingTime					= null;
	private JLabel					playingPattern				= null;
	private JLabel					playingSequence				= null;

	public PanelMix(Controller controller) {
		super(controller);
		controller.getStateManager().addSubscriber(this);
		controller.addSequencerMetaListener(this);
		controller.addSequencerSubscriber(this);
	}

	@Override
	public void initialize() {
		setValidate(false);
		getPanel().addKeyListener(getController().getPlayerKeyListener());
		getPanel().setLayout(new GridBagLayout());
		getPanel().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		//int row = 0;
		//addComponent(getPanel(), row, 0.01,getSequencePanel());
	}

	@Override
	public void requestFocus() {
		//grid.requestFocus();
	}

	@Override
	public void handleStateChange(StateChangeEvent evt) {
		setValidate(false);
		if (evt.getSource()!=this && evt.getType().equals(StateChangeEvent.CHANGED_COMPOSITION)) {
			// ...
		}
		setValidate(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals(F2_PRESSED)) {
			getController().getStateManager().setSelectedTab(this,FrameMain.TAB_INSTRUMENTS);
		} else if (evt.getActionCommand().equals(F3_PRESSED)) {
			getController().getStateManager().setSelectedTab(this,FrameMain.TAB_PATTERNS);
		} else if (evt.getActionCommand().equals(F4_PRESSED)) {
			getController().getStateManager().setSelectedTab(this,FrameMain.TAB_SEQUENCE);
		} else if (evt.getActionCommand().equals(FrameMain.STOP_PLAYING)) {
			getController().stopSequencer();
		}
	}

	@Override
	public void meta(MetaMessage meta) {
		if (meta.getType()==CompositionToSequenceConvertor.MARKER) {
			String txt = new String(meta.getData());
			if (txt.startsWith(CompositionToSequenceConvertor.SEQUENCE_MARKER)) {
				//String[] d = txt.split(":");
				//int index = Integer.parseInt(d[1]);
				// ...
			}
		}
	}

	@Override
	public void started() {
		// Ignore
	}

	@Override
	public void stopped() {
		// ...
	}
}
