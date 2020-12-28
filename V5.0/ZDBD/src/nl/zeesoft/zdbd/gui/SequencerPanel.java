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
import nl.zeesoft.zdk.thread.Lock;

public class SequencerPanel implements ActionListener {
	public static String					PLAY_SEQUENCE	= "PLAY_SEQUENCE";
	public static String					PLAY_THEME		= "PLAY_THEME";
	public static String					STOP			= "STOP";
	
	protected Lock							lock			= new Lock();
	protected ThemeController				controller		= null;
	protected ThemeSequenceSelector			selector		= null;
	protected JPanel						pane			= null;
	protected JComboBox<String>				sequence		= null;
	protected long							lastAction		= 0;
	
	public JPanel getPanel() {
		if (pane==null) {
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
		return pane;
	}
	
	public void initialize(ThemeController controller, ThemeSequenceSelector selector) {
		this.controller = controller;
		this.selector = selector;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		lock.lock(this);
		long last = lastAction;
		lock.unlock(this);
		
		if (System.currentTimeMillis() - last>100) {
			lock.lock(this);
			lastAction = System.currentTimeMillis();
			lock.unlock(this);
			
			if (e.getActionCommand().equals(PLAY_SEQUENCE)) {
				String name = (String) sequence.getSelectedItem();
				if (name!=null && name.length()>0) {
					SwingWorker<Object,Object> sw = new SwingWorker<Object,Object>() {
						@Override
						protected Object doInBackground() throws Exception {
							selector.startSequence(name);
							return null;
						}
					};
					sw.execute();
				}
			} else if (e.getActionCommand().equals(PLAY_THEME)) {
				String name = (String) sequence.getSelectedItem();
				if (name!=null && name.length()>0) {
					SwingWorker<Object,Object> sw = new SwingWorker<Object,Object>() {
						@Override
						protected Object doInBackground() throws Exception {
							selector.startTheme(name);
							return null;
						}
					};
					sw.execute();
				}
			} else if (e.getActionCommand().equals(STOP)) {
				SwingWorker<Object,Object> sw = new SwingWorker<Object,Object>() {
					@Override
					protected Object doInBackground() throws Exception {
						if (MidiSys.sequencer.isRunning()) {
							MidiSys.sequencer.stop();
						}
						return null;
					}
				};
				sw.execute();
			}
		}
	}
	
	public void refresh() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				List<String> sequences = controller.getSequenceNames();
				Object selectedItem = sequence.getSelectedItem();
				sequence.removeAllItems();
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
