package nl.zeesoft.zdbd.midi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import nl.zeesoft.zdbd.midi.convertors.PatternSequenceConvertor;
import nl.zeesoft.zdbd.pattern.instruments.Crash;
import nl.zeesoft.zdbd.pattern.instruments.Hihat;
import nl.zeesoft.zdbd.pattern.instruments.Kick;
import nl.zeesoft.zdbd.pattern.instruments.Percussion1;
import nl.zeesoft.zdbd.pattern.instruments.Percussion2;
import nl.zeesoft.zdbd.pattern.instruments.Ride;
import nl.zeesoft.zdbd.pattern.instruments.Snare;

public class MidiSequence {
	public Sequence							sequence 				= null;
	public HashMap<Long,Set<MidiEvent>>		eventsPerTick			= new HashMap<Long,Set<MidiEvent>>();
	public HashMap<Long,Set<MidiEvent>>		kickEventsPerTick		= new HashMap<Long,Set<MidiEvent>>();
	public HashMap<Long,Set<MidiEvent>>		snareEventsPerTick		= new HashMap<Long,Set<MidiEvent>>();
	public HashMap<Long,Set<MidiEvent>>		hihatEventsPerTick		= new HashMap<Long,Set<MidiEvent>>();
	public HashMap<Long,Set<MidiEvent>>		rideEventsPerTick		= new HashMap<Long,Set<MidiEvent>>();
	public HashMap<Long,Set<MidiEvent>>		crashEventsPerTick		= new HashMap<Long,Set<MidiEvent>>();
	public HashMap<Long,Set<MidiEvent>>		perc1EventsPerTick		= new HashMap<Long,Set<MidiEvent>>();
	public HashMap<Long,Set<MidiEvent>>		perc2EventsPerTick		= new HashMap<Long,Set<MidiEvent>>();
	
	protected MidiSequence() {
		
	}
	
	protected MidiSequence(Sequence sequence) {
		this.sequence = sequence;
		setEventsPerTick();
	}
	
	protected long getTickLength() {
		return sequence.getTickLength();
	}
	
	protected void setEventsPerTick() {
		Track[] tracks = sequence.getTracks();
		for (int t = 0; t < tracks.length; t++) {
			HashMap<Long,Set<MidiEvent>> trackSource = null;
			List<String> names = PatternSequenceConvertor.getTrackNames();
			if (names.get(t).equals(Kick.NAME)) {
				trackSource = kickEventsPerTick;
			} else if (names.get(t).equals(Snare.NAME)) {
				trackSource = snareEventsPerTick;
			} else if (names.get(t).equals(Hihat.NAME)) {
				trackSource = hihatEventsPerTick;
			} else if (names.get(t).equals(Ride.NAME)) {
				trackSource = rideEventsPerTick;
			} else if (names.get(t).equals(Crash.NAME)) {
				trackSource = crashEventsPerTick;
			} else if (names.get(t).equals(Percussion1.NAME)) {
				trackSource = perc1EventsPerTick;
			} else if (names.get(t).equals(Percussion2.NAME)) {
				trackSource = perc2EventsPerTick;
			}
			for (int i = 0; i < tracks[t].size(); i++) {
				MidiEvent event = tracks[t].get(i);
				Set<MidiEvent> events = eventsPerTick.get(event.getTick());
				if (events==null) {
					events = new HashSet<MidiEvent>();
					eventsPerTick.put(event.getTick(), events);
				}
				events.add(event);
				if (trackSource!=null) {
					boolean add = true;
					if (event.getMessage() instanceof ShortMessage) {
						ShortMessage msg = (ShortMessage) event.getMessage();
						if (msg.getCommand() == ShortMessage.NOTE_OFF) {
							add = false;
						}
					}
					if (add) {
						events = trackSource.get(event.getTick());
						if (events==null) {
							events = new HashSet<MidiEvent>();
							trackSource.put(event.getTick(), events);
						}
						events.add(event);
					}
				}
			}
		}
	}
	
	protected Set<MidiEvent> getEventsForTick(long tick) {
		return getEventsForTick(tick,null);
	}
	
	protected Set<MidiEvent> getEventsForTick(long tick, MixState state) {
		Set<MidiEvent> r = eventsPerTick.get(tick);
		if (r==null) {
			r = new HashSet<MidiEvent>();
		}
		if (state!=null) {
			Set<MidiEvent> test = new HashSet<MidiEvent>(r);
			for (MidiEvent event: test) {
				if (event.getMessage() instanceof ShortMessage) {
					ShortMessage msg = (ShortMessage) event.getMessage();
					if (state.muteChannels[msg.getChannel()] && msg.getCommand() != ShortMessage.NOTE_OFF) {
						r.remove(event);
					}
				}
			}
			if (!state.muteChannels[SynthConfig.DRUM_CHANNEL]) {
				List<String> names = PatternSequenceConvertor.getTrackNames();
				for (int t = 0; t < state.muteDrums.length; t++) {
					if (state.muteDrums[t]) {
						HashMap<Long,Set<MidiEvent>> trackSource = null;
						if (names.get(t).equals(Kick.NAME)) {
							trackSource = kickEventsPerTick;
						} else if (names.get(t).equals(Snare.NAME)) {
							trackSource = snareEventsPerTick;
						} else if (names.get(t).equals(Hihat.NAME)) {
							trackSource = hihatEventsPerTick;
						} else if (names.get(t).equals(Ride.NAME)) {
							trackSource = rideEventsPerTick;
						} else if (names.get(t).equals(Crash.NAME)) {
							trackSource = crashEventsPerTick;
						} else if (names.get(t).equals(Percussion1.NAME)) {
							trackSource = perc1EventsPerTick;
						} else if (names.get(t).equals(Percussion2.NAME)) {
							trackSource = perc2EventsPerTick;
						}
						if (trackSource!=null) {
							Set<MidiEvent> remove = trackSource.get(tick);
							if (remove!=null) {
								for (MidiEvent event: remove) {
									r.remove(event);
								}
							}
						}
					}
				}
			}
		}
		return r;
	}
}
