package nl.zeesoft.zmmt.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Synthesizer;
import javax.swing.JComboBox;

import nl.zeesoft.zmmt.syntesizer.Instrument;

public class InstrumentPlayer implements ActionListener {
	public static final String				SELECTED_INSTRUMENT		= "SELECTED_INSTRUMENT";
	public static List<JComboBox<String>>	selectors				= new ArrayList<JComboBox<String>>();
	
	private String							selectedInstrument		= Instrument.PIANO;
	private Synthesizer						synth					= null;
	
	private List<String>					playingNotes			= new ArrayList<String>();
	
	public void setSynthesizer(Synthesizer synth) {
		this.synth = synth;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals(SELECTED_INSTRUMENT)) {
			JComboBox<String> selector = getSelectorForSource(evt.getSource());
			if (selector!=null) {
				setSelectedInstrument((String) selector.getSelectedItem(),evt.getSource());
			}
		}
	}

	public void playInstrumentNote(String name, int noteNumber, int velocity) {
		if (synth!=null) {
			String id = getNoteId(name,noteNumber);
			if (!playingNotes.contains(id)) {
				playingNotes.add(id);
				int channel = Instrument.getMidiChannelForInstrument(name);
				synth.getChannels()[channel].noteOn(noteNumber, velocity);
			}
		}
	}

	public void stopInstrumentNote(String name, int noteNumber) {
		if (synth!=null) {
			int channel = Instrument.getMidiChannelForInstrument(name);
			synth.getChannels()[channel].noteOff(noteNumber);
			String id = getNoteId(name,noteNumber);
			if (playingNotes.contains(id)) {
				playingNotes.remove(id);
			}
		}
	}

	public void stopInstrumentNotes(String name) {
		if (synth!=null) {
			int channel = Instrument.getMidiChannelForInstrument(name);
			synth.getChannels()[channel].allNotesOff();
			String pfx = name + ":";
			List<String> playing = new ArrayList<String>(playingNotes);
			for (String id: playing) {
				if (id.startsWith(pfx)) {
					playingNotes.remove(id);
				}
			}
		}
	}

	public void playInstrumentNote(int noteNumber, int velocity) {
		playInstrumentNote(selectedInstrument,noteNumber,velocity);
	}

	public void stopInstrumentNote(int noteNumber) {
		stopInstrumentNote(selectedInstrument,noteNumber);
	}

	public void stopInstrumentNotes() {
		stopInstrumentNotes(selectedInstrument);
	}
	
	public JComboBox<String> getSelectorForSource(Object source) {
		JComboBox<String> r = null;
		for (JComboBox<String> selector: selectors) {
			if (selector==source) {
				r = selector;
				break;
			}
		}
		return r;
	}

	public JComboBox<String> getNewSelector() {
		JComboBox<String> r = new JComboBox<String>();
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			r.addItem(Instrument.INSTRUMENTS[i]);
		}
		r.setSelectedIndex(getSelectedInstrumentIndex());
		r.addActionListener(this);
		r.setActionCommand(SELECTED_INSTRUMENT);
		for (int l = 0; l < r.getKeyListeners().length; l++) {
			r.removeKeyListener(r.getKeyListeners()[l]);
		}
		selectors.add(r);
		return r;
	}
	
	public String getSelectedInstrument() {
		return selectedInstrument;
	}

	public void setSelectedInstrument(String selectedInstrument,Object source) {
		stopInstrumentNotes();
		this.selectedInstrument = selectedInstrument;
		for (JComboBox<String> sel: selectors) {
			if (sel!=source) {
				sel.setSelectedIndex(getSelectedInstrumentIndex());
			}
		}
	}
	
	private int getSelectedInstrumentIndex() {
		int r = 0;
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			if (Instrument.INSTRUMENTS[i].equals(selectedInstrument)) {
				r = i;
				break;
			}
		}
		return r;
	}
	
	private String getNoteId(String instrument,int noteNumber) {
		return instrument + ":" + noteNumber;
	}
}
