package nl.zeesoft.zdbd;

import nl.zeesoft.zdbd.generate.Generators;
import nl.zeesoft.zdbd.neural.NetworkConfigFactory;
import nl.zeesoft.zdbd.neural.NetworkTrainer;
import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkConfig;
import nl.zeesoft.zdk.thread.RunCode;

public class Composition {
	protected String			workDir					= "";
	
	protected String 			name					= "";
	protected NetworkTrainer	networkTrainer			= new NetworkTrainer();
	protected NetworkConfig		networkConfiguration	= NetworkConfigFactory.getNetworkConfig();
	protected Network			network					= new Network();
	protected Generators		generators				= new Generators();

	protected RunCode loadNetworkTrainer() {
		return networkTrainer.getFromFileRunCode(getTrainerFileName());
	}

	protected RunCode saveNetworkTrainer() {
		return networkTrainer.getToFileRunCode(getTrainerFileName());
	}
	
	protected RunCode initializeNetwork(boolean load) {
		networkConfiguration.directory = getDirectory();
		return network.getConfigureAndInitializeRunCode(networkConfiguration, true);
	}

	protected RunCode loadNetwork() {
		networkConfiguration.directory = getDirectory();
		return network.getInitializeAndLoadRunCode(networkConfiguration);
	}
	
	protected RunCode saveNetwork() {
		network.setDirectory(getDirectory());
		return network.getSaveRunCode();
	}

	protected RunCode loadGenerators() {
		return generators.getFromFileRunCode(getGeneratorsFileName());
	}

	protected RunCode saveGenerators() {
		return generators.getToFileRunCode(getGeneratorsFileName());
	}
	
	protected String getDirectory() {
		return FileIO.addSlash(workDir) + FileIO.addSlash(name);
	}
	
	protected String getTrainerFileName() {
		return getDirectory() + "NetworkTrainer.txt";
	}
	
	protected String getGeneratorsFileName() {
		return getDirectory() + "Generators.txt";
	}
	
	protected boolean directoryExists() {
		return FileIO.checkDirectory(getDirectory()).length() == 0;
	}
	
	protected RunCode getMkdirsRunCode() {
		RunCode code = new RunCode() {
			@Override
			protected boolean run() {
				FileIO.mkDirs((String)params[0]);
				return true;
			}
		};
		code.params[0] = getDirectory();
		return code;
	}
}
