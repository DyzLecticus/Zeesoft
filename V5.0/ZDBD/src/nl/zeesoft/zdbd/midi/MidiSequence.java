package nl.zeesoft.zdbd.midi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

public class MidiSequence {
	public Sequence							sequence 		= null;
	public HashMap<Long,Set<MidiEvent>>		eventsPerTick	= new HashMap<Long,Set<MidiEvent>>();
	
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
			for (int i = 0; i < tracks[t].size(); i++) {
				MidiEvent event = tracks[t].get(i);
				Set<MidiEvent> events = eventsPerTick.get(event.getTick());
				if (events==null) {
					events = new HashSet<MidiEvent>();
					eventsPerTick.put(event.getTick(), events);
				}
				events.add(event);
			}
		}
	}
	
	protected Set<MidiEvent> getEventsForTick(long tick) {
		Set<MidiEvent> r = eventsPerTick.get(tick);
		if (r==null) {
			r = new HashSet<MidiEvent>();
		}
		return r;
	}
}
