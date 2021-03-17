package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;
import nl.zeesoft.zdk.neural.processor.cl.ClConfig;
import nl.zeesoft.zdk.neural.processor.cl.Classification;
import nl.zeesoft.zdk.neural.processor.cl.Classifier;

public class TestClassifier {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		ClConfig config = new ClConfig();
		config.size = new Size(10,10,4);
		config.maxCount = 6;
		
		Classifier cl = new Classifier();
		assert cl.getInputNames().size() == 1;
		assert cl.getInputNames().get(0).equals("AssociateSDR");
		assert cl.getOutputNames().size() == 1;
		assert cl.getOutputNames().get(0).equals("AssociatedSDR");
		assert cl.toString().length() == 58;
		
		ProcessorIO io = new ProcessorIO();
		cl.processIO(io);
		assert io.error.length() > 0;
		assert io.error.equals("Classifier is not initialized");

		cl.initialize(config);
		assert cl.activationHistory.length == 400;
		assert cl.activationHistory.capacity == config.predictStep + 1;
		assert cl.toString().length() == 53;
		
		cl.bits = null;
		cl.processIO(io);
		assert io.error.length() > 0;
		assert io.error.equals("Classifier is not initialized");

		cl.initialize(config);

		io = new ProcessorIO(new Sdr(400));
		cl.processIO(io);
		assert io.outputs.size() == 1;
		assert io.outputs.get(Classifier.ASSOCIATED_SDR_OUTPUT).length == 400;
		assert cl.activationHistory.sdrs.size() == 1;
		assert cl.bits.bits.size() == 0;
		
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
		
		cl.processIO(io1);
		assert cl.activationHistory.sdrs.size() == 2;
		assert cl.bits.bits.size() == 0;
		assert io1.outputValue instanceof Classification;
		Classification classification = ((Classification)io1.outputValue);
		assert classification.name.equals(config.valueName);
		assert classification.step == config.predictStep;
		assert classification.valueCounts.size() == 0;
		
		cl.processIO(io2);
		assert cl.activationHistory.sdrs.size() == 2;
		assert cl.bits.bits.size() == 4;
		assert io2.outputValue instanceof Classification;
		classification = ((Classification)io1.outputValue);
		assert classification.name.equals(config.valueName);
		assert classification.step == config.predictStep;
		assert classification.valueCounts.size() == 0;
		
		io1.outputs.clear();
		cl.processIO(io1);
		assert cl.activationHistory.sdrs.size() == 2;
		assert cl.bits.bits.size() == 7;
		classification = ((Classification)io1.outputValue);
		assert classification.valueCounts.size() == 2;
		assert classification.valueCounts.get(1) == 5;
		assert classification.getMostCountedValues().size() == 1;
		assert (int)classification.getMostCountedValues().get(0) == 2;
		assert classification.getStandardDeviation() == 10.606602F;

		io2.outputs.clear();
		cl.processIO(io2);

		io1.outputs.clear();
		cl.processIO(io1);
		assert cl.activationHistory.sdrs.size() == 2;
		assert cl.bits.bits.size() == 7;
		classification = ((Classification)io1.outputValue);
		assert classification.valueCounts.size() == 2;
		assert classification.valueCounts.get(1) == 3;
		assert classification.getMostCountedValues().size() == 1;
		assert (int)classification.getMostCountedValues().get(0) == 2;
		assert classification.getStandardDeviation() == 6.363961F;
		
		cl.config.learn = false;
		io1.outputs.clear();
		cl.processIO(io1);
		classification = ((Classification)io1.outputValue);
		assert classification.getStandardDeviation() == 6.363961F;

		cl.config.learn = true;
		for (int i = 0; i < 10; i++) {
			io1.outputs.clear();
			cl.processIO(io1);
		}
		assert cl.bits.bits.size() == 4;
		classification = ((Classification)io1.outputValue);
		assert classification.valueCounts.size() == 1;
		
		cl.reset();
		assert cl.activationHistory.sdrs.size() == 0;
		assert cl.bits.bits.size() == 0;
		
		io1.inputs.clear();
		io1.inputs.add(new Sdr(400));
		cl.processIO(io1);
		assert cl.activationHistory.sdrs.size() == 1;
		assert cl.bits.bits.size() == 0;
		classification = ((Classification)io1.outputValue);
		assert classification.valueCounts.size() == 0;
		
	}
}
