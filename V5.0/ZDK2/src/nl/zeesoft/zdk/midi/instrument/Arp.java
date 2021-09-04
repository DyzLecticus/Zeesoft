package nl.zeesoft.zdk.midi.instrument;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.midi.synth.SynthConfig;

public class Arp extends Instrument {
	public Arp() {
		super(ARP);
		sounds.add(new InstrumentChannelSound(SynthConfig.ARP_CHANNEL_1, ARP, 3, 0, 0.75F, 80, 1.75F, 80));
		sounds.add(new InstrumentChannelSound(SynthConfig.ARP_CHANNEL_2, ARP, 3, 0, 0.75F, 80, 1.75F, 80));
	}

	@Override
	public List<Integer> getChannels() {
		List<Integer> r = new ArrayList<Integer>();
		r.add(SynthConfig.ARP_CHANNEL_1);
		r.add(SynthConfig.ARP_CHANNEL_2);
		return r;
	}
}
