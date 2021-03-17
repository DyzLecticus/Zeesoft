package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;
import nl.zeesoft.zdk.neural.processor.mr.Merger;
import nl.zeesoft.zdk.neural.processor.mr.MrConfig;

public class TestMerger {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		MrConfig config = new MrConfig();
		config.size = new Size(4,4,4);
		
		Merger mr = new Merger();
		assert mr.getInputNames().size() == 8;
		assert mr.getInputNames().get(0).equals("SDR1");
		assert mr.getInputNames().get(1).equals("SDR2");
		assert mr.getOutputNames().size() == 1;
		assert mr.getOutputNames().get(0).equals("MergedSDR");
		assert mr.toString().length() == 110;
		
		ProcessorIO io = new ProcessorIO();
		mr.processIO(io);
		assert io.error.length() > 0;
		assert io.error.equals("Merger is not initialized");

		mr.initialize(config);
		assert mr.toString().length() == 119;
		
		Sdr sdr1 = new Sdr(32);
		sdr1.setBit(0, true);
		Sdr sdr2 = new Sdr(32);
		sdr2.setBit(16, true);
		Sdr sdr3 = new Sdr(32);
		sdr3.setBit(8, true);
		
		io = new ProcessorIO(sdr1);
		mr.processIO(io);
		Sdr output = io.outputs.get(Merger.MERGED_SDR_OUTPUT);
		assert output.length == mr.config.size.volume();
		assert output.onBits.size() == 1;
		
		io.outputs.clear();
		io = new ProcessorIO(sdr1, sdr2, sdr3);
		mr.processIO(io);
		output = io.outputs.get(Merger.MERGED_SDR_OUTPUT);
		assert output.length == mr.config.size.volume();
		assert output.onBits.size() == 3;
		
		io.outputs.clear();
		mr.config.concatenate = true;
		mr.processIO(io);
		output = io.outputs.get(Merger.MERGED_SDR_OUTPUT);
		assert output.length == mr.config.size.volume();
		assert output.onBits.size() == 2;
	}
}
