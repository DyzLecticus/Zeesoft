package nl.zeesoft.zdbd;

import nl.zeesoft.zdbd.neural.NetworkConfigFactory;
import nl.zeesoft.zdbd.pattern.PatternFactory;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkConfig;
import nl.zeesoft.zdk.thread.RunCode;

public class Composition {
	public String 			name					= "";
	public NetworkConfig	networkConfiguration	= NetworkConfigFactory.getNetworkConfig();
	public Network			network					= new Network();
	public PatternSequence	trainingSequence		= PatternFactory.getFourOnFloorInstrumentPatternSequence();
	
	public RunCode loadNetwork(String directory) {
		networkConfiguration.directory = directory;
		return network.getInitializeAndLoadRunCode(networkConfiguration);
	}
	
	public Str initializeNetwork(String directory) {
		networkConfiguration.directory = directory;
		Str err = network.configure(networkConfiguration);
		if (err.length()==0) {
			err = network.initialize(true);
		}
		return err;
	}
	
	public RunCode saveNetwork(String directory) {
		network.setDirectory(directory);
		return network.getInitializeAndLoadRunCode(networkConfiguration);
	}
}
