package nl.zeesoft.zdbd.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import nl.zeesoft.zdbd.ThemeController;
import nl.zeesoft.zdbd.ThemeSequenceSelector;
import nl.zeesoft.zdbd.midi.MidiSys;

public class SequencerPanel implements ActionListener {
	public static String				PLAY_SEQUENCE	= "PLAY_SEQUENCE";
	public static String				PLAY_THEME		= "PLAY_THEME";
	public static String				STOP			= "STOP";
	
	protected ThemeController			controller		= null;
	protected ThemeSequenceSelector		selector		= null;
	protected JPanel					pane			= null;
	protected JComboBox<String>			sequence		= null;
	
	public JPanel getPanel() {
		return pane;
	}
	
	public void initialize(ThemeController controller, ThemeSequenceSelector selector) {
		this.controller = controller;
		this.selector = selector;
		pane = new JPanel();
		pane.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		sequence = new JComboBox<String>();
		pane.add(sequence,c);
		
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 1;
		c.gridy = 0;
		JButton button = new JButton();
		button.setText("Play sequence");
		button.setActionCommand(PLAY_SEQUENCE);
		button.addActionListener(this);
		pane.add(button,c);
		
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 2;
		c.gridy = 0;
		button = new JButton();
		button.setText("Play theme");
		button.setActionCommand(PLAY_THEME);
		button.addActionListener(this);
		pane.add(button,c);
		
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 3;
		c.gridy = 0;
		button = new JButton();
		button.setText("Stop");
		button.setActionCommand(STOP);
		button.addActionListener(this);
		
		pane.add(button,c);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(PLAY_SEQUENCE)) {
			String name = (String) sequence.getSelectedItem();
			if (name!=null && name.length()>0) {
				SwingWorker<String, Object> sw = new SwingWorker<String, Object>() {
					@Override
					public String doInBackground() {
						selector.startSequence(name);
						return "";
			       }
				};
				sw.execute();
			}
		} else if (e.getActionCommand().equals(PLAY_THEME)) {
			String name = (String) sequence.getSelectedItem();
			if (name!=null && name.length()>0) {
				SwingWorker<String, Object> sw = new SwingWorker<String, Object>() {
					@Override
					public String doInBackground() {
						selector.startTheme(name);
						return "";
			       }
				};
				sw.execute();
			}
		} else if (e.getActionCommand().equals(STOP)) {
			SwingWorker<String, Object> sw = new SwingWorker<String, Object>() {
				@Override
				public String doInBackground() {
					if (MidiSys.sequencer.isRunning()) {
						MidiSys.sequencer.stop();
					}
					return "";
		       }
			};
			sw.execute();
		}
	}
	
	public void refresh() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Object selectedItem = sequence.getSelectedItem();
				sequence.removeAllItems();
				List<String> sequences = controller.getSequenceNames();
				for (String name: sequences) {
					sequence.addItem(name);
				}
				if (sequences.size()>0) {
					if (selectedItem!=null && sequences.contains(selectedItem)) {
						sequence.setSelectedItem(selectedItem);
					} else {
						sequence.setSelectedItem(sequences.get(0));
					}
				}
			}
        });
	}
}
