package nl.zeesoft.zdbd.midi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sound.midi.MidiEvent;

public class EchoBuffer {
	public List<Set<MidiEvent>>	tickEvents = new ArrayList<Set<MidiEvent>>();
	
	public EchoBuffer copy() {
		EchoBuffer r = new EchoBuffer();
		for (Set<MidiEvent> events: tickEvents) {
			Set<MidiEvent> evts = new HashSet<MidiEvent>();
			for (MidiEvent event: events) {
				evts.add(event);
			}
			r.tickEvents.add(evts);
		}
		return r;
	}
	
	public Set<MidiEvent> getTickEvents(int ticksPerStep, int delay) {
		Set<MidiEvent> r = new HashSet<MidiEvent>();
		int max = ticksPerStep * delay;
		if (tickEvents.size()>max) {
			r = tickEvents.remove(0);
		}
		return r;
	}
	
	public boolean hasTickEvents(int ticksPerStep, int delay) {
		boolean r = false;
		int max = ticksPerStep * delay;
		if (tickEvents.size()>max) {
			r = true;
		}
		return r;
	}
}
