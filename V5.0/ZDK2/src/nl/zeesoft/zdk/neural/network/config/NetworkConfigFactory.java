package nl.zeesoft.zdk.neural.network.config;

import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.network.config.type.ClassifierConfig;
import nl.zeesoft.zdk.neural.network.config.type.MergerConfig;
import nl.zeesoft.zdk.neural.network.config.type.ScalarEncoderConfig;
import nl.zeesoft.zdk.neural.network.config.type.SpatialPoolerConfig;
import nl.zeesoft.zdk.neural.network.config.type.TemporalMemoryConfig;
import nl.zeesoft.zdk.neural.processor.se.ScConfig;

public class NetworkConfigFactory {
	public ScConfig		encoder				= new ScConfig();
	public Size			size				= new Size(48,48,16);
	public int			onBits				= 46;
	public int			activationThreshold	= 13;
	public int			matchingThreshold	= 10;
	
	public NetworkConfig getSimpleConfig(String ...inputNames) {
		NetworkConfig r = new NetworkConfig();
		for (int i = 0; i < inputNames.length; i++) {
			addInputColumn(r, i + 1, inputNames[i]);
		}
		return r;
	}
	
	public NetworkConfig getApicalFeedbackConfig(String ...inputNames) {
		NetworkConfig r = getSimpleConfig(inputNames);
		addTemporalMemoryMerger(r, "TemporalMemory");
		return r;
	}
	
	public void addInputColumn(NetworkConfig config, int column, String name) {
		config.addInput(name);
		
		String seName = "ScalarEncoder" + column;
		String spName = "SpatialPooler" + column;
		String tmName = "TemporalMemory" + column;
		String clName = "Classifier" + column;
		
		addInputColumnProcessors(config, seName, spName, tmName, clName);
		addInputColumnProcessorLinks(config, name, seName, spName, tmName, clName);
	}
	
	public void addTemporalMemoryMerger(NetworkConfig config, String tmNameContains) { 
		MergerConfig mrc = config.addMerger("Merger");
		mrc.config.size = size.copy();
		for (ProcessorConfig tmc: config.getProcessorConfigs(tmNameContains)) {
			if (tmc instanceof TemporalMemoryConfig) {
				config.addLink(tmc.name, "Merger");
				config.addLink("Merger", 0, tmc.name, 1);
			}
		}
	}
	
	protected void addInputColumnProcessors(
		NetworkConfig config, String seName, String spName, String tmName, String clName
		) {
		ScalarEncoderConfig sec = config.addScalarEncoder(0, seName);
		sec.encoder = encoder.copy();
		
		SpatialPoolerConfig spc = config.addSpatialPooler(1, spName);
		spc.config.inputSize = new Size(encoder.encodeLength);
		spc.config.outputSize = new Size(size.x, size.y);
		spc.config.outputOnBits = onBits;
		
		TemporalMemoryConfig tmc = config.addTemporalMemory(2, tmName);
		tmc.config.size = size.copy();
		tmc.config.activationThreshold = activationThreshold;
		tmc.config.matchingThreshold = matchingThreshold;
		
		ClassifierConfig clc = config.addClassifier(3, clName);
		clc.config.size = size.copy();
	}
	
	protected void addInputColumnProcessorLinks(
		NetworkConfig config, String name, String seName, String spName, String tmName, String clName
		) {
		config.addLink(name, seName);
		config.addLink(seName, spName);
		config.addLink(spName, tmName);
		config.addLink(tmName, clName);
		config.addLink(name, 0, clName, 1);
	}
}
