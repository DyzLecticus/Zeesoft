package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Console;
import nl.zeesoft.zdk.test.neural.network.TestNetwork;
import nl.zeesoft.zdk.test.neural.network.TestNetworkConfig;
import nl.zeesoft.zdk.test.neural.network.TestNetworkConfigFactory;
import nl.zeesoft.zdk.test.neural.network.TestNetworkJson;
import nl.zeesoft.zdk.test.neural.network.TestNetworkTrainer;
import nl.zeesoft.zdk.test.neural.processor.TestClassifier;
import nl.zeesoft.zdk.test.neural.processor.TestClassifierJson;
import nl.zeesoft.zdk.test.neural.processor.TestDateTimeEncoder;
import nl.zeesoft.zdk.test.neural.processor.TestMerger;
import nl.zeesoft.zdk.test.neural.processor.TestScalarEncoder;
import nl.zeesoft.zdk.test.neural.processor.TestSpatialPooler;
import nl.zeesoft.zdk.test.neural.processor.TestSpatialPoolerJson;
import nl.zeesoft.zdk.test.neural.processor.TestSpatialPoolerOverlap;
import nl.zeesoft.zdk.test.neural.processor.TestTemporalMemory;
import nl.zeesoft.zdk.test.neural.processor.TestTemporalMemoryBurst;
import nl.zeesoft.zdk.test.neural.processor.TestTemporalMemoryJson;

public class ZdkNeuralTests {
	public static void main(String[] args) {
		Console.log("Test Sdr ...");
		TestSdr.main(args);
		Console.log("Test SdrHistory ...");
		TestSdrHistory.main(args);
		TestSdrStringConvertor.main(args);
		Console.log("Test ScalarEncoder ...");
		TestScalarEncoder.main(args);
		Console.log("Test DateTimeEncoder ...");
		TestDateTimeEncoder.main(args);
		Console.log("Test Cells ...");
		TestSegment.main(args);
		TestCell.main(args);
		TestCells.main(args);
		TestCellsStringConvertor.main(args);
		Console.log("Test SpatialPooler ...");
		TestSpatialPooler.main(args);
		TestSpatialPoolerOverlap.main(args);
		TestSpatialPoolerJson.main(args);
		Console.log("Test TemporalMemory ...");
		TestTemporalMemory.main(args);
		TestTemporalMemoryBurst.main(args);
		TestTemporalMemoryJson.main(args);
		Console.log("Test Classifier ...");
		TestClassifier.main(args);
		TestClassifierJson.main(args);
		Console.log("Test Merger ...");
		TestMerger.main(args);
		Console.log("Test Network ...");
		TestNetworkConfig.main(args);
		TestNetworkConfigFactory.main(args);
		TestNetwork.main(args);
		TestNetworkTrainer.main(args);
		TestNetworkJson.main(args);
	}
}
