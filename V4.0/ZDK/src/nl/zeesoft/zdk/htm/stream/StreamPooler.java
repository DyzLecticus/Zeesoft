package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.impl.SpatialPooler;
import nl.zeesoft.zdk.htm.impl.SpatialPoolerConfig;
import nl.zeesoft.zdk.htm.mdl.Model;
import nl.zeesoft.zdk.htm.sdr.SDR;

public class StreamPooler extends SpatialPooler implements StreamProcessable {
	public StreamPooler(Model model, SpatialPoolerConfig config) {
		super(model, config);
	}

	@Override
	public List<SDR> getOutputSDRsForInputSDRs(List<SDR> inputs,boolean learn) {
		List<SDR> r = new ArrayList<SDR>();
		if (inputs.size()>0) {
			r.add(this.getOutputSDRForInputSDR(inputs.get(0),learn));
		}
		return r;
	}
}
