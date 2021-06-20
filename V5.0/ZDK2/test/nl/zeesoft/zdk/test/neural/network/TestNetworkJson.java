package nl.zeesoft.zdk.test.neural.network;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.json.JElem;
import nl.zeesoft.zdk.json.Json;
import nl.zeesoft.zdk.json.JsonConstructor;
import nl.zeesoft.zdk.json.ObjectConstructor;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.network.NetworkJsonConstructor;
import nl.zeesoft.zdk.neural.network.NetworkObjectConstructor;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;
import nl.zeesoft.zdk.neural.network.config.NetworkConfigFactory;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;
import nl.zeesoft.zdk.neural.processor.cl.Classification;
import nl.zeesoft.zdk.str.StrUtil;

public class TestNetworkJson {
	private static TestNetworkJson	self	= new TestNetworkJson();
	
	private static int				WORKERS	= 4;
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		Classification cl = new Classification();
		cl.name = "Pizza";
		cl.step = 3;
		cl.value = 0.0F;
		cl.valueCounts.put(0.3F, 5);
		cl.valueCounts.put(0.5F, 7);
				
		ProcessorIO pio = new ProcessorIO();
		pio.inputValue = 1.0F;
		Sdr sdr1 = new Sdr(10);
		sdr1.setBit(1, true);
		sdr1.setBit(4, true);
		pio.inputs.add(sdr1);
		Sdr sdr2 = new Sdr(10);
		sdr2.setBit(2, true);
		sdr2.setBit(5, true);
		pio.inputs.add(sdr2);
		pio.outputValue = cl;
		pio.error = "ERROR";
		
		ProcessorIO pio2 = new ProcessorIO();
		Sdr sdr3 = new Sdr();
		sdr3.setBit(3, true);
		sdr3.setBit(4, true);
		pio2.inputs.add(sdr3);
		
		MockNetworkIO nio = new MockNetworkIO();
		nio.setTimeoutMs(123);
		nio.addInput("Input1", 2.0F);
		nio.addInput("Input2", "Low");
		nio.addProcessorIO("MockName1", pio);
		nio.addProcessorIO("MockName2", pio2);
		nio.addError("ERROR_1");
		nio.addError("ERROR_2");
		
		Json nioJs = JsonConstructor.fromObject(nio);
		NetworkIO nio2 = (NetworkIO) ObjectConstructor.fromJson(nioJs);
		Json nio2Js = JsonConstructor.fromObject(nio2);
		assert StrUtil.equals(nioJs.toStringBuilder(), nio2Js.toStringBuilder());
		
		NetworkConfigFactory factory = new NetworkConfigFactory();
		factory.setSmallScale();
		
		NetworkConfig config = factory.getSimpleConfig("Input1", "Input2");
		factory.addTemporalMemoryMerger(config, "TemporalMemory");

		Json json = JsonConstructor.fromObjectUseConvertors(config);
		StringBuilder str = json.toStringBuilder();
		Json json2 = new Json(str);
		config = (NetworkConfig) ObjectConstructor.fromJson(json2);
		json2 = JsonConstructor.fromObjectUseConvertors(config);
		assert StrUtil.equals(json2.toStringBuilder(), str);
		
		Rand.reset(0);
		
		Network network = new Network();
		assert network.initialize(config);
		assert network.reset();
		
		for (int i = 0; i < 1; i++) {
			NetworkIO io = new NetworkIO();
			io.addInput("Input1", i % 2);
			io.addInput("Input2", i % 3);
			network.processIO(io);
			assert io.getErrors().size() == 0;
		}
		
		Logger.debug(self, "Converting network to JSON ...");
		long start = System.currentTimeMillis();
		network.setNumberOfWorkers(WORKERS);
		
		NetworkJsonConstructor jsc = new NetworkJsonConstructor();
		json = jsc.fromNetwork(self, network, 10000);
		assert json.root.children.size() == 4;
		JElem processors = json.root.get(NetworkJsonConstructor.NETWORK_PROCESSORS);
		assert processors.children.size() == 9;
		
		network.setNumberOfWorkers(0);
		Logger.debug(self, "Converting network to JSON took " + (System.currentTimeMillis() - start) + " ms");
		Logger.debug(self, "JSON length: " + json.toStringBuilder().length());
	
		Logger.debug(self, "Converting JSON to network ...");
		start = System.currentTimeMillis();
		
		NetworkObjectConstructor noc = new NetworkObjectConstructor();
		Network network2 = noc.fromJson(self, json, WORKERS, 10000);
		assert network2.getWidth() == network.getWidth();
		assert network2.getNumberOfLayers() == network.getNumberOfLayers();
		assert network2.getNumberOfProcessors() == network.getNumberOfProcessors();

		network2.setNumberOfWorkers(0);
		Logger.debug(self, "Converting JSON to network took " + (System.currentTimeMillis() - start) + " ms");
	}
}
