package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.KeyValueSDR;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.ScalarEncoder;
import nl.zeesoft.zdk.neural.processors.Classification;
import nl.zeesoft.zdk.neural.processors.Classifier;
import nl.zeesoft.zdk.neural.processors.ClassifierConfig;
import nl.zeesoft.zdk.neural.processors.ProcessorFactory;
import nl.zeesoft.zdk.neural.processors.ProcessorIO;
import nl.zeesoft.zdk.neural.processors.Processor;
import nl.zeesoft.zdk.neural.processors.SpatialPooler;
import nl.zeesoft.zdk.neural.processors.SpatialPoolerConfig;
import nl.zeesoft.zdk.neural.processors.TemporalMemory;
import nl.zeesoft.zdk.neural.processors.TemporalMemoryConfig;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;

public class TestProcessorFactory extends TestObject {
	public TestProcessorFactory(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestProcessorFactory(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *ProcessorFactory* to create a basic chain of default *Processor* instances that can make predictions.");
		System.out.println("*Processor* instances are thread safe wrappers around *SDRProcessor* instances.");
		System.out.println("*ProcessorIO* instances are used to specify *Processor* input/output and more.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the processors");
		System.out.println("Processor sp = ProcessorFactory.getNewProcessor(\"SP\", new SpatialPoolerConfig(), true);");
		System.out.println("Processor tm = ProcessorFactory.getNewProcessor(\"TM\", new TemporalMemoryConfig());");
		System.out.println("Processor cl = ProcessorFactory.getNewProcessor(\"CL\", new ClassifierConfig());");

		System.out.println("// Use the spatial pooler");
		System.out.println("ProcessorIO io1 = new ProcessorIO();");
		System.out.println("io1.inputs.add(new SDR());");
		System.out.println("sp.processIO(io1);");

		System.out.println("// Use the temporal memory");
		System.out.println("ProcessorIO io2 = new ProcessorIO();");
		System.out.println("io2.inputs.add(io1.outputs.get(SpatialPooler.ACTIVE_COLUMNS_OUTPUT);");
		System.out.println("tm.processIO(io2);");

		System.out.println("// Use the classifier");
		System.out.println("KeyValueSDR kvSdr = new KeyValueSDR(io2.outputs.get(TemporalMemory.ACTIVE_CELLS_OUTPUT));");
		System.out.println("kvSdr.put(Classifier.DEFAULT_VALUE_KEY, i % 4);");
		System.out.println("ProcessorIO io3 = new ProcessorIO();");
		System.out.println("io3.inputs.add(kvSdr);");
		System.out.println("cl.processIO(io3);");
		
		System.out.println("// Get the classification");
		System.out.println("KeyValueSDR kvSdr = (KeyValueSDR) outputList.get(outputList.size() - 1);");
		System.out.println("Classification cls = (Classification) kvSdr.get(Classifier.CLASSIFICATION_VALUE_KEY + \":1\");");
		
		System.out.println("// Save the processor data to files");
		System.out.println("sp.save(\"data/sp.txt\");");
		System.out.println("tm.save(\"data/tm.txt\");");
		System.out.println("cl.save(\"data/cl.txt\");");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestProcessorFactory.class));
		System.out.println(" * " + getTester().getLinkForClass(ProcessorFactory.class));
		System.out.println(" * " + getTester().getLinkForClass(Processor.class));
		System.out.println(" * " + getTester().getLinkForClass(ProcessorIO.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows an example SDR processing chain and a subsample of some of its temporal memory outputs.  ");
	}

	@Override
	protected void test(String[] args) {
		Logger.setLoggerDebug(true);
		
		Processor sp = ProcessorFactory.getNewProcessor("SP", new SpatialPoolerConfig(), true);
		Processor tm = ProcessorFactory.getNewProcessor("TM", new TemporalMemoryConfig());
		Processor cl = ProcessorFactory.getNewProcessor("CL", new ClassifierConfig());
		
		List<SDR> inputList = getInputSDRList(1000);
		List<SDR> outputList = new ArrayList<SDR>();
		
		long started = System.currentTimeMillis();
		
		System.out.println();
		System.out.println("Processing ...");
		int i = 0;
		for (SDR input: inputList) {
			ProcessorIO io1 = new ProcessorIO();
			io1.inputs.add(input);
			sp.processIO(io1);
			SDR output = io1.outputs.get(SpatialPooler.ACTIVE_COLUMNS_OUTPUT);
			
			ProcessorIO io2 = new ProcessorIO();
			if (i >= 40) {
				io2.inputs.add(io1.outputs.get(SpatialPooler.ACTIVE_COLUMNS_OUTPUT));
				tm.processIO(io2);
				output = io2.outputs.get(TemporalMemory.ACTIVE_CELLS_OUTPUT);
			}
			
			if (i >= 940) {
				KeyValueSDR kvSdr = new KeyValueSDR(io2.outputs.get(TemporalMemory.ACTIVE_CELLS_OUTPUT));
				kvSdr.setValue(i%4);
				ProcessorIO io3 = new ProcessorIO();
				io3.inputs.add(kvSdr);
				cl.processIO(io3);
				output = io3.outputs.get(Classifier.CLASSIFICATION_OUTPUT);
			}
			
			output.sort();
			outputList.add(output);
			if (i > 0 && i % 100 == 0) {
				Logger.dbg(this, output.toStr());
			}
			
			i++;
		}
		
		long duration = System.currentTimeMillis() - started;
		System.out.println("Processing " + inputList.size() + " SDRs took: " + duration + " ms (" + (duration / inputList.size()) + " ms/SDR)");
		
		KeyValueSDR kvSdr = (KeyValueSDR) outputList.get(outputList.size() - 1);
		Classification cls = (Classification) kvSdr.get(Classifier.CLASSIFICATION_VALUE_KEY + ":1");
		
		assertEqual((Integer)cls.getMostCountedValues().size(),1,"Predicted values size does not match expectation");
		assertEqual((Integer)cls.getMostCountedValues().get(0),0,"Predicted value does not match expectation");
	}
	
	private List<SDR> getInputSDRList(int num) {
		ScalarEncoder encoder = new ScalarEncoder();
		
		Str err = encoder.testMinimalOverlap();
		assertEqual(err, new Str(), "Error message does not match expectation");
		
		List<SDR> r = new ArrayList<SDR>();
		for (int i = 0; i < num; i++) {
			r.add(encoder.getEncodedValue(i % 4));
		}
		return r;
	}
}
