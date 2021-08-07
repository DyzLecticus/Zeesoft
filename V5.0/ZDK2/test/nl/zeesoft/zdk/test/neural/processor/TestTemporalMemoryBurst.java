package nl.zeesoft.zdk.test.neural.processor;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;
import nl.zeesoft.zdk.neural.processor.tm.TemporalMemory;
import nl.zeesoft.zdk.neural.processor.tm.TmConfig;

public class TestTemporalMemoryBurst {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		Rand.reset(0);
		
		testBurst(50);
	}
	
	public static List<Sdr> testBurst(int iterations) {
		TmConfig config = new TmConfig();
		config.size = new Size(10,10,4);
		config.activationThreshold = 4;
		config.matchingThreshold = 2;
		TemporalMemory tm = new TemporalMemory();

		tm.initialize(config);
		
		tm.setNumberOfWorkers(10);
		
		List<Sdr> inputs = getInputSdrs();
		List<Sdr> outputs = new ArrayList<Sdr>();
		
		for (int i = 0; i < iterations; i++) {
			for (Sdr sdr: inputs) {
				ProcessorIO io = new ProcessorIO(sdr);
				tm.processIO(io);
				assert io.error.length() == 0;
				assert io.outputs.size() == 4;
				outputs.add(io.outputs.get(TemporalMemory.BURSTING_COLUMNS_OUTPUT));
			}
		}
		assert outputs.size() == iterations * 4;
		
		tm.setNumberOfWorkers(0);
		
		int i = 0;
		float avg = 0;
		int total = 0;
		for (Sdr output: outputs) {
			total += output.onBits.size();
			if (i > 0 && i % iterations == 0) {
				avg = 0;
				if (total>0) {
					avg = (float)total / (float)iterations;
				}
				if (i == iterations) {
					assert avg > 0F;
				}
				total = 0;
			}
			i++;
		}
		assert avg == 0F;
		
		ProcessorIO empty = new ProcessorIO(new Sdr(100));
		tm.processIO(empty);
		assert empty.outputs.get(TemporalMemory.ACTIVE_CELLS_OUTPUT).onBits.size() == 0;
		assert empty.outputs.get(TemporalMemory.BURSTING_COLUMNS_OUTPUT).onBits.size() == 0;
		assert empty.outputs.get(TemporalMemory.PREDICTIVE_CELLS_OUTPUT).onBits.size() == 0;
		assert empty.outputs.get(TemporalMemory.WINNER_CELLS_OUTPUT).onBits.size() == 0;
		
		return outputs;
	}
	
	public static List<Sdr> getInputSdrs() {
		List<Sdr> r = new ArrayList<Sdr>();
		int s = 0;
		for (int i = 0; i < 4; i++) {
			Sdr sdr = new Sdr(100);
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
