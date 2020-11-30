package nl.zeesoft.zdbd;

import nl.zeesoft.zdbd.generate.Generators;
import nl.zeesoft.zdbd.neural.NetworkConfigFactory;
import nl.zeesoft.zdbd.pattern.PatternFactory;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkConfig;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.thread.RunCode;

public class Composition {
	public String					directory				= "";
	
	public String 					name					= "";
	public PatternSequence			trainingSequence		= PatternFactory.getFourOnFloorInstrumentPatternSequence();
	public NetworkConfig			networkConfiguration	= NetworkConfigFactory.getNetworkConfig();
	public Network					network					= new Network();
	public NetworkIO				startIO					= null;
	public Generators				generators				= new Generators();
	
	public RunCode loadNetwork() {
		networkConfiguration.directory = directory;
		return network.getInitializeAndLoadRunCode(networkConfiguration);
	}
	
	public RunCode initializeNetwork() {
		networkConfiguration.directory = directory;
		return network.getConfigureAndInitializeRunCode(networkConfiguration, true);
	}
	
	public RunCode saveNetwork() {
		network.setDirectory(directory);
		return network.getInitializeAndLoadRunCode(networkConfiguration);
	}
}
