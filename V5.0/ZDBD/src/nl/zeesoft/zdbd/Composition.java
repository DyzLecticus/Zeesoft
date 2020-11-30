package nl.zeesoft.zdbd;

import nl.zeesoft.zdbd.generate.Generators;
import nl.zeesoft.zdbd.neural.NetworkConfigFactory;
import nl.zeesoft.zdbd.neural.NetworkTrainer;
import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkConfig;
import nl.zeesoft.zdk.thread.RunCode;

public class Composition {
	public String					workDir				= "";
	
	public String 					name					= "";
	public NetworkTrainer			networkTrainer			= new NetworkTrainer();
	public NetworkConfig			networkConfiguration	= NetworkConfigFactory.getNetworkConfig();
	public Network					network					= new Network();
	public Generators				generators				= new Generators();
	
	public RunCode loadNetwork() {
		networkConfiguration.directory = getDirecory();
		return network.getInitializeAndLoadRunCode(networkConfiguration);
	}
	
	public RunCode initializeNetwork() {
		networkConfiguration.directory = getDirecory();
		return network.getConfigureAndInitializeRunCode(networkConfiguration, true);
	}
	
	// TODO: mkdirs before save
	public RunCode saveNetwork() {
		network.setDirectory(getDirecory());
		return network.getInitializeAndLoadRunCode(networkConfiguration);
	}
	
	protected String getDirecory() {
		return FileIO.addSlash(workDir) + FileIO.addSlash(name);
	}
}
