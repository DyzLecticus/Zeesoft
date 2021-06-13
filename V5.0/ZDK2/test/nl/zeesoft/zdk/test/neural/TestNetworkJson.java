package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.json.JElem;
import nl.zeesoft.zdk.json.Json;
import nl.zeesoft.zdk.json.JsonConstructor;
import nl.zeesoft.zdk.json.ObjectConstructor;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkJsonConstructor;
import nl.zeesoft.zdk.neural.network.NetworkObjectConstructor;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;
import nl.zeesoft.zdk.neural.network.config.NetworkConfigFactory;
import nl.zeesoft.zdk.str.StrUtil;

public class TestNetworkJson {
	private static TestNetworkJson	self	= new TestNetworkJson();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
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
		
		Network network = new Network();
		assert network.initialize(config);
		
		NetworkJsonConstructor jsc = new NetworkJsonConstructor();
		json = jsc.fromNetwork(self, network, 3000);
		assert json.root.children.size() == 4;
		JElem processors = json.root.get(NetworkJsonConstructor.NETWORK_PROCESSORS);
		assert processors.children.size() == 9;
		
		NetworkObjectConstructor noc = new NetworkObjectConstructor();
		Network network2 = noc.fromJson(self, json, 0, 3000);
		assert network2.getWidth() == network.getWidth();
		assert network2.getNumberOfLayers() == network.getNumberOfLayers();
		assert network2.getNumberOfProcessors() == network.getNumberOfProcessors();
	}
}
