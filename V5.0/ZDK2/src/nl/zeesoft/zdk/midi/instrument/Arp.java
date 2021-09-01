package nl.zeesoft.zdk.midi.instrument;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.midi.synth.SynthConfig;

public class Arp extends Instrument {
	public Arp() {
		super(ARP);
	}

	@Override
	public List<Integer> getChannels() {
		List<Integer> r = new ArrayList<Integer>();
		r.add(SynthConfig.ARP_CHANNEL_1);
		r.add(SynthConfig.ARP_CHANNEL_2);
		return r;
	}
}
