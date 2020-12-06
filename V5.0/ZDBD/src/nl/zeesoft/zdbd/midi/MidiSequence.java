package nl.zeesoft.zdbd.midi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

public class MidiSequence {
	public Sequence							sequence 		= null;
	public HashMap<Long,Set<MidiEvent>>		eventsPerTick	= new HashMap<Long,Set<MidiEvent>>();
	
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
	
	protected List<MidiEvent> getMidiEventsForTicks(long start, long end) {
		List<MidiEvent> r = new ArrayList<MidiEvent>();
		for (long tick = start; tick < end; tick++) {
			Set<MidiEvent> events = eventsPerTick.get(tick);
			if (events!=null) {
				r.addAll(events);
			}
		}
		return r;
	}
}
