package nl.zeesoft.zdbd.theme;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.midi.Arpeggiators;
import nl.zeesoft.zdbd.midi.SoundPatch;
import nl.zeesoft.zdbd.neural.Generators;
import nl.zeesoft.zdbd.neural.NetworkConfigFactory;
import nl.zeesoft.zdbd.neural.NetworkTrainer;
import nl.zeesoft.zdbd.pattern.Rythm;
import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkConfig;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.RunCode;

public class Theme {
	protected String			themeDir				= "";
	
	protected String 			name					= "";
	protected Rythm				rythm					= new Rythm();
	protected NetworkTrainer	networkTrainer			= new NetworkTrainer();
	protected NetworkConfig		networkConfiguration	= NetworkConfigFactory.getNetworkConfig();
	protected Network			network					= new Network();
	protected Generators		generators				= new Generators();
	protected Arpeggiators		arpeggiators			= new Arpeggiators();
	protected SoundPatch		soundPatch				= new SoundPatch();
	
	protected void setShuffle(float percentage) {
		rythm.setShuffle(percentage);
		Rythm publish = new Rythm();
		publish.copyFrom(rythm);
		networkTrainer.setShuffle(publish.stepDelays);
		generators.setShuffle(publish.stepDelays);
	}
	
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
	
	protected CodeRunnerChain trainNetwork() {
		return networkTrainer.getTrainNetworkChain(network);
	}
	
	protected List<RunCode> resetNetwork() {
		network = new Network();
		List<RunCode> r = new ArrayList<RunCode>();
		r.add(network.getInitializeRunCode(networkConfiguration, true));
		r.add(getResetNetworkTrainerRunCode());
		return r;
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

	protected RunCode loadArpeggiators() {
		return arpeggiators.getFromFileRunCode(getArpeggiatorsFileName());
	}

	protected RunCode saveArpeggiators() {
		return arpeggiators.getToFileRunCode(getArpeggiatorsFileName());
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
	
	protected String getArpeggiatorsFileName() {
		return getDirectory() + "Arpeggiators.txt";
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
	
	protected RunCode getResetNetworkTrainerRunCode() {
		RunCode code = new RunCode() {
			@Override
			protected boolean run() {
				networkTrainer.reset();
				return true;
			}
		};
		return code;
	}
}
