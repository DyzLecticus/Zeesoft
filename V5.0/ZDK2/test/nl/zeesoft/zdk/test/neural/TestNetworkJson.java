package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.json.Json;
import nl.zeesoft.zdk.json.JsonConstructor;
import nl.zeesoft.zdk.json.ObjectConstructor;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;
import nl.zeesoft.zdk.neural.network.config.NetworkConfigFactory;
import nl.zeesoft.zdk.str.StrUtil;

public class TestNetworkJson {
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
	}
}
