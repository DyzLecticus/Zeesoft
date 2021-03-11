package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.neural.ProcessorIO;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.sp.SpConfig;
import nl.zeesoft.zdk.neural.sp.SpatialPooler;

public class TestSpatialPoolerOverlap {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		SpConfig config = new SpConfig();
		SpatialPooler sp = new SpatialPooler();
		
		sp.initialize(config);
		sp.resetConnections();
		
		List<Sdr> inputs = getInputSdrs();
		List<Sdr> outputs = new ArrayList<Sdr>();
		
		int iterations = 250;
		for (int i = 0; i < iterations; i++) {
			for (Sdr sdr: inputs) {
				ProcessorIO io = new ProcessorIO();
				io.inputs.add(sdr);
				sp.processIO(io);
				assert io.error.length()==0;
				assert io.outputs.size()==1;
				outputs.add(io.outputs.get(0));
			}
		}
		
		assert outputs.size()==1000;
	}
	
	public static List<Sdr> getInputSdrs() {
		List<Sdr> r = new ArrayList<Sdr>();
		int s = 0;
		for (int i = 0; i < 4; i++) {
			Sdr sdr = new Sdr(16);
			for (int b = s; b < 4; b++) {
				sdr.setBit(b, true);
			}
			r.add(sdr);
			s += 4;
		}
		return r;
	}
}
