package nl.zeesoft.zdk.midi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.Synthesizer;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.midi.MidiNote;
import nl.zeesoft.zdk.thread.Lock;

public class Synth {
	public static final int			VOLUME				= 7;
	public static final int			ATTACK				= 73;
	public static final int			DECAY				= 75;
	public static final int			RELEASE				= 72;
	public static final int			PAN					= 10;
	public static final int			MODULATION			= 1;
	public static final int			CHORUS				= 93;
	public static final int			FILTER				= 74;
	public static final int			RESONANCE			= 71;
	public static final int			REVERB				= 91;
	public static final int			VIB_RATE			= 76;
	public static final int			VIB_DEPTH			= 77;
	public static final int			VIB_DELAY			= 78;
	
	private Lock					lock				= new Lock();
	private Synthesizer				synthesizer			= null;
	
	private List<MidiNote>			playingNotes		= new ArrayList<MidiNote>();
	private List<DelayedNotePlayer>	delayedPlayers		= new ArrayList<DelayedNotePlayer>();
	
	protected Synth(Logger logger, Synthesizer synthesizer) {
		this.synthesizer = synthesizer;
		lock.setLogger(this, logger);
	}
	
	protected Synthesizer getSynthesizer() {
		return synthesizer;
	}
	
	protected void setInstrument(int channel,Inst instrument) {
		MidiChannel chan = synthesizer.getChannels()[channel];
		if (chan!=null) {
			if (chan.getProgram()!=instrument.instrument) {
				stopNotes(channel);
				chan.programChange(instrument.instrument);
			}
			chan.controlChange(VOLUME,instrument.volume);
			chan.controlChange(ATTACK,instrument.attack);
			chan.controlChange(DECAY,instrument.decay);
			chan.controlChange(RELEASE,instrument.release);
			chan.controlChange(PAN,instrument.pan);
			chan.controlChange(MODULATION,instrument.modulation);
			chan.controlChange(CHORUS,instrument.chorus);
			chan.controlChange(FILTER,instrument.filter);
			chan.controlChange(RESONANCE,instrument.resonance);
			chan.controlChange(REVERB,instrument.reverb);
			chan.controlChange(VIB_RATE,instrument.vib_rate);
			chan.controlChange(VIB_DEPTH,instrument.vib_depth);
			chan.controlChange(VIB_DELAY,instrument.vib_delay);
			if (chan.getChannelPressure()!=instrument.pressure) {
				chan.setChannelPressure(instrument.pressure);
			}
			if (chan.getMute()!=instrument.mute) {
				chan.setMute(instrument.mute);
			}
			if (chan.getSolo()!=instrument.solo) {
				chan.setSolo(instrument.solo);
			}
		}
	}
	
	protected void startNotes(List<MidiNote> notes) {
		lock.lock(this);
		for (MidiNote note: notes) {
			MidiNote pn = getPlayingNoteNoLock(note);
			if (pn==null) {
				MidiChannel chan = synthesizer.getChannels()[note.channel];
				if (chan!=null) {
					chan.noteOn(note.getMidiNoteNum(),note.velocity);
					playingNotes.add(note);
				}
			}
		}
		lock.unlock(this);
	}
	
	protected void stopNotes(List<MidiNote> notes) {
		lock.lock(this);
		for (MidiNote note: notes) {
			MidiNote pn = getPlayingNoteNoLock(note);
			if (pn!=null) {
				MidiChannel chan = synthesizer.getChannels()[note.channel];
				if (chan!=null) {
					chan.noteOff(note.getMidiNoteNum());
					playingNotes.remove(pn);
				}
			}
		}
		lock.unlock(this);
	}
	
	protected void stopNotes(int channel) {
		lock.lock(this);
		stopNotesNoLock(channel);
		lock.unlock(this);
	}
	
	protected void startGroupNotes(List<MidiNote> notes, int msPerStep) {
		List<MidiNote> nowNotes = new ArrayList<MidiNote>();
		SortedMap<Integer,List<MidiNote>> delayedNotes = new TreeMap<Integer,List<MidiNote>>();
		splitNotes(notes,nowNotes,delayedNotes);
		startNotes(nowNotes);
		handleDelayedNotes(delayedNotes,msPerStep,true);
	}
	
	protected void stopGroupNotes(List<MidiNote> notes, int msPerStep) {
		List<MidiNote> nowNotes = new ArrayList<MidiNote>();
		SortedMap<Integer,List<MidiNote>> delayedNotes = new TreeMap<Integer,List<MidiNote>>();
		splitNotes(notes,nowNotes,delayedNotes);
		stopNotes(nowNotes);
		handleDelayedNotes(delayedNotes,msPerStep,false);
	}
	
	protected void delayedPlayerDone(DelayedNotePlayer player) {
		lock.lock(this);
		delayedPlayers.remove(player);
		lock.unlock(this);
	}
	
	protected List<DelayedNotePlayer> getDelayedPlayers() {
		lock.lock(this);
		List<DelayedNotePlayer> r = new ArrayList<DelayedNotePlayer>(delayedPlayers);
		lock.unlock(this);
		return r;
	}
	
	private void stopNotesNoLock(int channel) {
		List<MidiNote> notes = new ArrayList<MidiNote>(playingNotes);
		for (MidiNote note: notes) {
			if (note.channel==channel) {
				synthesizer.getChannels()[note.channel].noteOff(note.getMidiNoteNum());
				playingNotes.remove(note);
			}
		}
		for (DelayedNotePlayer player: delayedPlayers) {
			if (player.getNotes(channel).size()>0) {
				player.stop();
			}
		}
	}

	private void splitNotes(List<MidiNote> notes, List<MidiNote> nowNotes, SortedMap<Integer,List<MidiNote>> delayedNotes) {
		for (MidiNote note: notes) {
			if (note.delaySteps==0) {
				nowNotes.add(note);
			} else {
				List<MidiNote> delayNotes = delayedNotes.get(note.delaySteps);
				if (delayNotes==null) {
					delayNotes = new ArrayList<MidiNote>();
					delayedNotes.put(note.delaySteps,delayNotes);
				}
				delayNotes.add(note);
			}
		}
	}
	
	private void handleDelayedNotes(SortedMap<Integer,List<MidiNote>> delayedNotes, int msPerStep, boolean actionStart) {
		for (Entry<Integer,List<MidiNote>> entry: delayedNotes.entrySet()) {
			DelayedNoteCode code = new DelayedNoteCode(this);
			code.actionTime = System.currentTimeMillis() + (msPerStep * entry.getKey());
			code.actionStart = actionStart;
			code.notes = entry.getValue();
			DelayedNotePlayer player = new DelayedNotePlayer(code) {
				@Override
				protected void doneCallback() {
					delayedPlayerDone(this);
				}
			};
			lock.lock(this);
			delayedPlayers.add(player);
			lock.unlock(this);
			player.start();
		}
	}
	
	private MidiNote getPlayingNoteNoLock(MidiNote note) {
		MidiNote r = null;
		for (MidiNote pn: playingNotes) {
			if (pn.channel==note.channel && pn.getMidiNoteNum() == note.getMidiNoteNum()) {
				r = pn;
				break;
			}
		}
		return r;
	}
}
