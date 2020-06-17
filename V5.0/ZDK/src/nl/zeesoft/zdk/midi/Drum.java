package nl.zeesoft.zdk.midi;

import java.util.ArrayList;
import java.util.List;

public class Drum {
	public List<DrumLayer>	layers	= new ArrayList<DrumLayer>();
	
	public Drum copy() {
		Drum r = new Drum();
		for (DrumLayer layer: layers) {
			r.layers.add(layer.copy());
		}
		return r;
	}
}
