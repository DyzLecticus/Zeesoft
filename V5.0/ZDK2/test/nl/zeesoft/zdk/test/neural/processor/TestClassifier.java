package nl.zeesoft.zdk.test.neural.processor;

import java.util.Collections;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;
import nl.zeesoft.zdk.neural.processor.cl.ClConfig;
import nl.zeesoft.zdk.neural.processor.cl.Classification;
import nl.zeesoft.zdk.neural.processor.cl.Classifier;
import nl.zeesoft.zdk.neural.processor.cl.ValueLikelyhood;

public class TestClassifier {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		ClConfig config = new ClConfig();
		config.size = new Size(10,10,4);
				
		InputOutputConfig ioConfig = config.getInputOutputConfig();
		assert ioConfig.inputs.size() == 1;
		assert ioConfig.inputs.get(Classifier.ASSOCIATE_SDR_INPUT).name.equals("AssociateSDR");
		assert ioConfig.outputs.size() == 1;
		assert ioConfig.outputs.get(Classifier.ASSOCIATED_SDR_OUTPUT).name.equals("AssociatedSDR");
		assert ioConfig.toString().length() == 42;

		Classification classification = new Classification();
		assert classification.getMostLikelyValues(2).size() == 0;
		classification.valueLikelyhoods.add(new ValueLikelyhood("A",0.2F));
		assert classification.getMostLikelyValues(2).size() == 1;
		classification.valueLikelyhoods.add(new ValueLikelyhood("B",0.3F));
		assert classification.getMostLikelyValues(2).size() == 2;
		classification.valueLikelyhoods.add(new ValueLikelyhood("C",0.4F));
		assert classification.getMostLikelyValues(2).size() == 2;
		assert classification.getMostLikelyValues(2).get(0).value.equals("A");
		Collections.sort(classification.valueLikelyhoods);
		assert classification.getMostLikelyValues(2).get(0).value.equals("C");
		
		Classifier cl = new Classifier();
		assert cl.getInputOutputConfig()!=null;
		cl.reset();
		
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
		classification = ((Classification)io1.outputValue);
		assert classification.step == config.predictStep;
		assert classification.valueLikelyhoods.size() == 0;
		assert classification.value.equals(1);
		
		cl.processIO(io2);
		assert cl.activationHistory.sdrs.size() == 2;
		assert cl.bits.bits.size() == 4;
		assert io2.outputValue instanceof Classification;
		classification = ((Classification)io2.outputValue);
		assert classification.step == config.predictStep;
		assert classification.valueLikelyhoods.size() == 1;
		assert classification.value.equals(2);
		
		io1.outputs.clear();
		cl.processIO(io1);
		assert cl.activationHistory.sdrs.size() == 2;
		assert cl.bits.bits.size() == 7;
		classification = ((Classification)io1.outputValue);
		assert classification.valueLikelyhoods.size() == 2;
		assert classification.valueLikelyhoods.get(0).likelyhood == 0.79983985F;
		assert classification.valueLikelyhoods.get(1).likelyhood == 0.20016013F;
		assert classification.getStandardDeviation() == 0.42403758F;
		assert classification.value.equals(1);

		io2.outputs.clear();
		cl.processIO(io2);

		io1.outputs.clear();
		cl.processIO(io1);
		assert cl.activationHistory.sdrs.size() == 2;
		assert cl.bits.bits.size() == 7;
		classification = ((Classification)io1.outputValue);
		assert classification.valueLikelyhoods.size() == 2;
		assert classification.valueLikelyhoods.get(0).likelyhood == 0.79983985F;
		assert classification.valueLikelyhoods.get(1).likelyhood == 0.20016013F;
		assert classification.getStandardDeviation() == 0.42403758F;
		assert classification.value.equals(1);
		assert classification.prediction.likelyhood == 0.79983985F;
		assert classification.averagePrediction.likelyhood == 1F;

		cl.setLearn(false);
		cl.config.alpha = 0.05F;
		io1.outputs.clear();
		cl.processIO(io1);
		classification = ((Classification)io1.outputValue);
		assert classification.getStandardDeviation() == 0.41183138F;

		cl.setLearn(true);
		for (int i = 0; i < 10; i++) {
			io1.outputs.clear();
			cl.processIO(io1);
		}
		assert cl.bits.bits.size() == 7;
		classification = ((Classification)io1.outputValue);
		assert classification.valueLikelyhoods.size() == 2;
		
		cl.reset();
		assert cl.activationHistory.sdrs.size() == 0;
		assert cl.bits.bits.size() == 0;
		
		io1.inputs.clear();
		io1.inputs.add(new Sdr(400));
		cl.processIO(io1);
		assert cl.activationHistory.sdrs.size() == 1;
		assert cl.bits.bits.size() == 0;
		classification = ((Classification)io1.outputValue);
		assert classification.valueLikelyhoods.size() == 0;
	}
}
