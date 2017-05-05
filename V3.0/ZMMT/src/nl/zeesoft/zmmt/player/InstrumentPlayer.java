package nl.zeesoft.zmmt.player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.sound.midi.Synthesizer;
import javax.swing.JComboBox;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zmmt.syntesizer.Instrument;
import nl.zeesoft.zmmt.syntesizer.MidiNote;
import nl.zeesoft.zmmt.syntesizer.MidiNoteDelayed;

public class InstrumentPlayer extends Locker implements ActionListener {
	public static final String				SELECTED_INSTRUMENT		= "SELECTED_INSTRUMENT";
	public List<JComboBox<String>>			selectors				= new ArrayList<JComboBox<String>>();
	
	private InstrumentPlayerEchoWorker		echoWorker				= null;
	
	private String							selectedInstrument		= Instrument.LEAD;
	private Synthesizer						synthesizer				= null;
	
	private SortedMap<String,MidiNote>		playingNotes			= new TreeMap<String,MidiNote>();		
	
	public InstrumentPlayer(Messenger msgr, WorkerUnion uni) {
		super(msgr);
		echoWorker = new InstrumentPlayerEchoWorker(msgr,uni,this);
	}

	public void start() {
		echoWorker.start();
	}

	public void stop() {
		echoWorker.stop();
	}

	public void setSynthesizer(Synthesizer synth) {
		lockMe(this);
		this.synthesizer = synth;
		unlockMe(this);
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
		List<MidiNoteDelayed> delayedNotes = new ArrayList<MidiNoteDelayed>();
		boolean delay = false;
		lockMe(this);
		if (synthesizer!=null) {
			for (MidiNote note: notes) {
				if (note instanceof MidiNoteDelayed) {
					delayedNotes.add((MidiNoteDelayed) note);
				} else {
					if (!playingNotes.containsKey(note.getId())) {
						delay = true;
						playingNotes.put(note.getId(),note);
						synthesizer.getChannels()[note.channel].noteOn(note.midiNote,note.velocity);
					}
				}
			}
		}
		unlockMe(this);
		if (delay && delayedNotes.size()>0) {
			echoWorker.addNotes(true,delayedNotes);
		}
	}

	public void stopInstrumentNotes(List<MidiNote> notes) {
		List<MidiNoteDelayed> delayedNotes = new ArrayList<MidiNoteDelayed>();
		lockMe(this);
		if (synthesizer!=null) {
			for (MidiNote note: notes) {
				if (note instanceof MidiNoteDelayed) {
					delayedNotes.add((MidiNoteDelayed) note);
				} else {
					synthesizer.getChannels()[note.channel].noteOff(note.midiNote);
					playingNotes.remove(note.getId());
				}
			}
		}
		unlockMe(this);
		if (delayedNotes.size()>0) {
			echoWorker.addNotes(false,delayedNotes);
		}
	}
	
	public void stopInstrumentNotes(String name) {
		lockMe(this);
		if (synthesizer!=null) {
			SortedMap<String,MidiNote> playing = new TreeMap<String,MidiNote>(playingNotes);
			for (String id: playing.keySet()) {
				MidiNote note = playingNotes.get(id);
				if (note.instrument.equals(name)) {
					synthesizer.getChannels()[note.channel].noteOff(note.midiNote);
					playingNotes.remove(note.getId());
				}
			}
			int channel1 = Instrument.getMidiChannelForInstrument(name,0);
			synthesizer.getChannels()[channel1].allNotesOff();
			int channel2 = Instrument.getMidiChannelForInstrument(name,1);
			if (channel2>=0 && channel2!=channel1) {
				synthesizer.getChannels()[channel2].allNotesOff();
			}
			int channel3 = Instrument.getMidiChannelForInstrument(name,2);
			if (channel3>=0 && channel3!=channel2 && channel3!=channel1) {
				synthesizer.getChannels()[channel3].allNotesOff();
			}
		}
		unlockMe(this);
		if (name.equals(Instrument.ECHO)) {
			echoWorker.clearNotes();
		}
	}

	public void stopInstrumentNotes() {
		stopInstrumentNotes(getSelectedInstrument());
	}
	
	public JComboBox<String> getSelectorForSource(Object source) {
		lockMe(this);
		JComboBox<String> r = null;
		for (JComboBox<String> selector: selectors) {
			if (selector==source) {
				r = selector;
				break;
			}
		}
		unlockMe(this);
		return r;
	}

	public JComboBox<String> getNewSelector() {
		lockMe(this);
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
		unlockMe(this);
		return r;
	}
	
	public String getSelectedInstrument() {
		String r = "";
		lockMe(this);
		r = selectedInstrument;
		unlockMe(this);
		return r;
	}

	public void setSelectedInstrument(String selectedInstrument,Object source) {
		if (!getSelectedInstrument().equals(selectedInstrument)) {
			List<JComboBox<String>> updateSelectors = new ArrayList<JComboBox<String>>();
			int updateIndex = 0;
			stopInstrumentNotes();
			lockMe(this);
			this.selectedInstrument = selectedInstrument;
			updateIndex = getSelectedInstrumentIndex();
			for (JComboBox<String> sel: selectors) {
				if (sel!=source) {
					updateSelectors.add(sel);
				}
			}
			unlockMe(this);
			if (updateSelectors.size()>0) {
				for (JComboBox<String> sel: updateSelectors) {
					sel.setSelectedIndex(updateIndex);
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
