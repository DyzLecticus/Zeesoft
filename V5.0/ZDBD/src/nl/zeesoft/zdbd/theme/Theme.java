package nl.zeesoft.zdbd.theme;

import nl.zeesoft.zdbd.midi.SoundPatch;
import nl.zeesoft.zdbd.neural.Generators;
import nl.zeesoft.zdbd.neural.NetworkConfigFactory;
import nl.zeesoft.zdbd.neural.NetworkTrainer;
import nl.zeesoft.zdbd.pattern.Rythm;
import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkConfig;
import nl.zeesoft.zdk.thread.RunCode;

public class Theme {
	protected String			themeDir				= "";
	
	protected String 			name					= "";
	protected Rythm				rythm					= new Rythm();
	protected NetworkTrainer	networkTrainer			= new NetworkTrainer();
	protected NetworkConfig		networkConfiguration	= NetworkConfigFactory.getNetworkConfig();
	protected Network			network					= new Network();
	protected Generators		generators				= new Generators();
	protected SoundPatch		soundPatch				= new SoundPatch();

	protected RunCode loadRythm() {
		return rythm.getFromFileRunCode(getRythmFileName());
	}

	protected RunCode saveRythm() {
		return rythm.getToFileRunCode(getRythmFileName());
	}
	
	protected RunCode loadNetworkTrainer() {
		return networkTrainer.getFromFileRunCode(getTrainerFileName());
	}

	protected RunCode saveNetworkTrainer() {
		return networkTrainer.getToFileRunCode(getTrainerFileName());
	}
	
	protected RunCode initializeNetwork() {
		networkConfiguration.directory = getDirectory();
		return network.getInitializeRunCode(networkConfiguration, true);
	}

	protected RunCode loadNetwork() {
		networkConfiguration.directory = getDirectory();
		return network.getInitializeAndLoadRunCode(networkConfiguration,false);
	}
	
	public RunCode trainNetwork() {
		return networkTrainer.getTrainNetworkRunCode(network);
	}
	
	protected RunCode saveNetwork() {
		network.setDirectory(getDirectory());
		return network.getSaveRunCode(true);
	}

	protected RunCode loadGenerators() {
		return generators.getFromFileRunCode(getGeneratorsFileName());
	}

	protected RunCode saveGenerators() {
		return generators.getToFileRunCode(getGeneratorsFileName());
	}

	protected RunCode generateSequence(String name) {
		return generators.getGenerateSequenceRunCode(network, networkTrainer.getLastIO(), networkTrainer.getSequence(), name);
	}
	
	protected RunCode loadSoundPatch() {
		return soundPatch.getFromFileRunCode(getSoundPatchFileName());
	}

	protected RunCode saveSoundPatch() {
		return soundPatch.getToFileRunCode(getSoundPatchFileName());
	}
	
	protected String getDirectory() {
		return FileIO.addSlash(themeDir) + FileIO.addSlash(name);
	}
	
	protected String getRythmFileName() {
		return getDirectory() + "Rythm.txt";
	}
	
	protected String getTrainerFileName() {
		return getDirectory() + "NetworkTrainer.txt";
	}
	
	protected String getGeneratorsFileName() {
		return getDirectory() + "Generators.txt";
	}
	
	protected String getSoundPatchFileName() {
		return getDirectory() + "SoundPatch.txt";
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
	
	protected RunCode getDeleteDirRunCode() {
		RunCode code = new RunCode() {
			@Override
			protected boolean run() {
				FileIO.deleteDir((String)params[0], true);
				return true;
			}
		};
		code.params[0] = getDirectory();
		return code;
	}
}
