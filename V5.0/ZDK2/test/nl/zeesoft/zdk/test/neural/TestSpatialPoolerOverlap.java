package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.ProcessorIO;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.sp.SpConfig;
import nl.zeesoft.zdk.neural.sp.SpatialPooler;

public class TestSpatialPoolerOverlap {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		testOverlap(1000);
	}
	
	public static List<Sdr> testOverlap(int iterations) {
		SpConfig config = new SpConfig();
		config.inputSize = new Size(4,4);
		config.outputSize = new Size(10,10);
		config.outputOnBits = 2;
		SpatialPooler sp = new SpatialPooler();

		sp.initialize(config);
		sp.reset();
		
		List<Sdr> inputs = getInputSdrs();
		List<Sdr> outputs = new ArrayList<Sdr>();
		
		for (int i = 0; i < iterations; i++) {
			for (Sdr sdr: inputs) {
				ProcessorIO io = new ProcessorIO(sdr);
				sp.processIO(io);
				assert io.error.length() == 0;
				assert io.outputs.size() == 1;
				outputs.add(io.outputs.get(SpatialPooler.ACTIVE_COLUMNS_OUTPUT));
			}
		}
		assert outputs.size() == iterations * 4;
		
		float overlap = 0;
		float similarOverlap = 0;
		int lastIndex = (iterations * 4 - 1);
		int startIndex = (iterations * 3 - 1);
		Sdr lastOutput = outputs.get((iterations*4 - 1));
		int i = 0;
		for (Sdr output: outputs) {
			if (i>=startIndex && i<lastIndex) {
				if (i % 4 == 3) {
					similarOverlap += output.getOverlap(lastOutput); 
				} else {
					overlap += output.getOverlap(lastOutput);
				}
			}
			i++;
		}
		float factor = Float.MAX_VALUE;
		if (overlap > 0) {
			overlap = overlap / 3;
			factor = (similarOverlap / overlap);
		}
		assert factor > 1F;
		
		return outputs;
	}
	
	public static List<Sdr> getInputSdrs() {
		List<Sdr> r = new ArrayList<Sdr>();
		int s = 0;
		for (int i = 0; i < 4; i++) {
			Sdr sdr = new Sdr(16);
			for (int b = s; b < s + 4; b++) {
				sdr.setBit(b, true);
			}
			assert sdr.onBits.size() == 4;
			r.add(sdr);
			s += 4;
		}
		return r;
	}
}
