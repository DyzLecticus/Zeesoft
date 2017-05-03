package nl.zeesoft.zmmt.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.sound.midi.Synthesizer;
import javax.swing.JComboBox;

import nl.zeesoft.zmmt.syntesizer.Instrument;
import nl.zeesoft.zmmt.syntesizer.MidiNote;

public class InstrumentPlayer implements ActionListener {
	public static final String				SELECTED_INSTRUMENT		= "SELECTED_INSTRUMENT";
	public static List<JComboBox<String>>	selectors				= new ArrayList<JComboBox<String>>();
	
	private String							selectedInstrument		= Instrument.PIANO;
	private Synthesizer						synthesizer				= null;
	
	private SortedMap<String,MidiNote>		playingNotes			= new TreeMap<String,MidiNote>();		
	
	public void setSynthesizer(Synthesizer synth) {
		this.synthesizer = synth;
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

	public void playInstrumentNotes(List<MidiNote> notes) {
		if (synthesizer!=null) {
			for (MidiNote note: notes) {
				if (!playingNotes.containsKey(note.getId())) {
					playingNotes.put(note.getId(),note);
					synthesizer.getChannels()[note.channel].noteOn(note.midiNote,note.velocity);
				}
			}
		}
	}

	public void stopInstrumentNotes(List<MidiNote> notes) {
		if (synthesizer!=null) {
			for (MidiNote note: notes) {
				synthesizer.getChannels()[note.channel].noteOff(note.midiNote);
				playingNotes.remove(note.getId());
			}
		}
	}
	
	public void stopInstrumentNotes(String name) {
		if (synthesizer!=null) {
			SortedMap<String,MidiNote> playing = new TreeMap<String,MidiNote>(playingNotes);
			for (String id: playing.keySet()) {
				MidiNote note = playingNotes.get(id);
				if (note.instrument.equals(name)) {
					synthesizer.getChannels()[note.channel].noteOff(note.midiNote);
					playingNotes.remove(note.getId());
				}
			}
			int channel = Instrument.getMidiChannelForInstrument(name,0);
			synthesizer.getChannels()[channel].allNotesOff();
			int layerChannel = Instrument.getMidiChannelForInstrument(name,1);
			if (layerChannel!=channel && layerChannel>=0) {
				synthesizer.getChannels()[channel].allNotesOff();
			}
		}
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
		if (!this.selectedInstrument.equals(selectedInstrument)) {
			stopInstrumentNotes();
			this.selectedInstrument = selectedInstrument;
			for (JComboBox<String> sel: selectors) {
				if (sel!=source) {
					sel.setSelectedIndex(getSelectedInstrumentIndex());
				}
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
}
