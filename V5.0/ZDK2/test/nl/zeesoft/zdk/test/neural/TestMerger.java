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
		
		/*
		mr.bits = null;
		mr.processIO(io);
		assert io.error.length() > 0;
		assert io.error.equals("Classifier is not initialized");

		mr.initialize(config);

		io = new ProcessorIO(new Sdr(400));
		mr.processIO(io);
		assert io.outputs.size() == 1;
		assert io.outputs.get(Classifier.ASSOCIATED_SDR_OUTPUT).length == 400;
		assert mr.activationHistory.sdrs.size() == 1;
		assert mr.bits.bits.size() == 0;
		
		Sdr sdr1 = new Sdr(400);
		sdr1.setBit(0, true);
		sdr1.setBit(1, true);
		sdr1.setBit(2, true);
		sdr1.setBit(3, true);
		
		Sdr sdr2 = new Sdr(400);
		sdr2.setBit(3, true);
		sdr2.setBit(4, true);
		sdr2.setBit(5, true);
		sdr2.setBit(6, true);
		
		ProcessorIO io1 = new ProcessorIO(sdr1);
		io1.inputValue = 1;
		ProcessorIO io2 = new ProcessorIO(sdr2);
		io2.inputValue = 2;
		
		mr.processIO(io1);
		assert mr.activationHistory.sdrs.size() == 2;
		assert mr.bits.bits.size() == 0;
		assert io1.outputValue instanceof Classification;
		Classification classification = ((Classification)io1.outputValue);
		assert classification.name.equals(config.valueName);
		assert classification.step == config.predictStep;
		assert classification.valueCounts.size() == 0;
		
		mr.processIO(io2);
		assert mr.activationHistory.sdrs.size() == 2;
		assert mr.bits.bits.size() == 4;
		assert io2.outputValue instanceof Classification;
		classification = ((Classification)io1.outputValue);
		assert classification.name.equals(config.valueName);
		assert classification.step == config.predictStep;
		assert classification.valueCounts.size() == 0;
		
		io1.outputs.clear();
		mr.processIO(io1);
		assert mr.activationHistory.sdrs.size() == 2;
		assert mr.bits.bits.size() == 7;
		classification = ((Classification)io1.outputValue);
		assert classification.valueCounts.size() == 2;
		assert classification.valueCounts.get(1) == 5;
		assert classification.getMostCountedValues().size() == 1;
		assert (int)classification.getMostCountedValues().get(0) == 2;
		assert classification.getStandardDeviation() == 10.606602F;

		io2.outputs.clear();
		mr.processIO(io2);

		io1.outputs.clear();
		mr.processIO(io1);
		assert mr.activationHistory.sdrs.size() == 2;
		assert mr.bits.bits.size() == 7;
		classification = ((Classification)io1.outputValue);
		assert classification.valueCounts.size() == 2;
		assert classification.valueCounts.get(1) == 3;
		assert classification.getMostCountedValues().size() == 1;
		assert (int)classification.getMostCountedValues().get(0) == 2;
		assert classification.getStandardDeviation() == 6.363961F;
		
		mr.config.learn = false;
		io1.outputs.clear();
		mr.processIO(io1);
		classification = ((Classification)io1.outputValue);
		assert classification.getStandardDeviation() == 6.363961F;

		mr.config.learn = true;
		for (int i = 0; i < 10; i++) {
			io1.outputs.clear();
			mr.processIO(io1);
		}
		assert mr.bits.bits.size() == 4;
		classification = ((Classification)io1.outputValue);
		assert classification.valueCounts.size() == 1;
		
		mr.reset();
		assert mr.activationHistory.sdrs.size() == 0;
		assert mr.bits.bits.size() == 0;
		
		io1.inputs.clear();
		io1.inputs.add(new Sdr(400));
		mr.processIO(io1);
		assert mr.activationHistory.sdrs.size() == 1;
		assert mr.bits.bits.size() == 0;
		classification = ((Classification)io1.outputValue);
		assert classification.valueCounts.size() == 0;
		*/
	}
}
