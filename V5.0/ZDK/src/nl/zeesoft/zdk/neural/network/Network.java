package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.KeyValueSDR;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.processors.CellGridProcessor;
import nl.zeesoft.zdk.neural.processors.Processor;
import nl.zeesoft.zdk.neural.processors.ProcessorFactory;
import nl.zeesoft.zdk.neural.processors.ProcessorIO;
import nl.zeesoft.zdk.neural.processors.SDRProcessor;
import nl.zeesoft.zdk.thread.Busy;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;
import nl.zeesoft.zdk.thread.Waitable;
import nl.zeesoft.zdk.thread.Waiter;

public class Network implements Waitable {
	private static String			PREVIOUS_IO_FILE_NAME		= "PreviousIO.txt";
	
	private Lock					lock						= new Lock();
	private Busy					busy						= new Busy(this);
	
	// Configuration
	private NetworkConfig			config						= new NetworkConfig();
	
	private Lock					initLocker					= new Lock();
	private List<NetworkProcessor>	processors					= new ArrayList<NetworkProcessor>();
	private CodeRunnerChain			processorChain				= null;
	
	// State
	private NetworkIO				currentIO					= null;
	private NetworkIO				previousIO					= null;
	
	public boolean initializeAndLoad(NetworkConfig config) {
		int timeoutMs = config.initializeTimeoutMs + config.loadTimeoutMs;
		return Waiter.startAndWaitFor(new CodeRunnerList(getInitializeAndLoadRunCode(config)), timeoutMs);
	}
	
	public RunCode getInitializeAndLoadRunCode(NetworkConfig config) {
		return new RunCode() {
			@Override
			protected boolean run() {
				if (config.fileExists()) {
					config.fromFile();
					Str msg = new Str("Configuration;\n");
					msg.sb().append(config.getDescription().sb());
					Logger.dbg(this, msg);
					Str err = configure(config);
					if (err.length()==0) {
						if (!busy.isBusy()) {
							err = initialize(false);
							if (err.length()==0) {
								load();
							}
						}
					}
				} else {
					Logger.dbg(this, new Str("Configuration file not found: " + config.getFileName()));
				}
				return true;
			}
		};
	}
	
	public boolean configureAndInitialize(NetworkConfig config, boolean resetConnections) {
		int timeoutMs = config.initializeTimeoutMs;
		return Waiter.startAndWaitFor(new CodeRunnerList(getConfigureAndInitializeRunCode(config,resetConnections)), timeoutMs);
	}
	
	public RunCode getConfigureAndInitializeRunCode(NetworkConfig config, boolean resetConnections) {
		return new RunCode() {
			@Override
			protected boolean run() {
				Str msg = new Str("Configuration;\n");
				msg.sb().append(config.getDescription().sb());
				Logger.dbg(this, msg);
				Str err = configure(config);
				if (err.length()==0) {
					if (!busy.isBusy()) {
						initialize(resetConnections);
					}
				}
				return true;
			}
		};
	}
	
	public Str configure(NetworkConfig config) {
		Str err = config.testConfiguration();
		if (err.length()>0) {
			Logger.err(this, err);
		} else {
			lock.lock(this);
			if (processors.size()==0) {
				this.config.copyFrom(config);
				processors.clear();
				processorChain = new CodeRunnerChain();
			} else {
				Logger.err(this, new Str("Network has already been initialized"));
			}
			lock.unlock(this);
		}
		return err;
	}
	
	public Str initialize(boolean resetConnections) {
		lock.lock(this);
		Str err = config.testConfiguration();
		if (err.length()==0) {
			err = new Str();
			Logger.dbg(this, new Str("Initializing network ..."));

			busy.setBusy(true);
			CodeRunnerList runnerList = buildInitializeRunnerListNoLock(resetConnections);
			if (!Waiter.startAndWaitFor(runnerList, config.initializeTimeoutMs)) {
				err.sb().append("Network initialization timed out");
			}
			if (err.length()==0 && processors.size() < config.processorConfigs.size()) {
				err.sb().append("Failed to initialize one or more processors");
			}
			if (err.length()==0) {
				buildProcessorChainNoLock(processorChain);
			} else {
				processors.clear();
			}
			busy.setBusy(false);
			
			if (err.length()==0) {
				Logger.dbg(this, new Str("Initialized network"));
			}
		}
		lock.unlock(this);
		if (err.length()>0) {
			Logger.err(this, err);
		}
		return err;
	}

	public void setLayerLearn(int layer, boolean learn) {
		setLayerProperty(layer, "learn", learn);
	}

	public void setProcessorLearn(String name, boolean learn) {
		setProcessorProperty(name, "learn", learn);
	}
	
	public void setLayerProperty(int layer, String property, Object value) {
		lock.lock(this);
		setLayerPropertyNoLock(layer, property, value);
		lock.unlock(this);
	}
	
	public void setProcessorProperty(String name, String property, Object value) {
		lock.lock(this);
		setProcessorPropertyNoLock(name, property, value);
		lock.unlock(this);
	}

	public RunCode getResetConnectionsRunCode() {
		return new RunCode() {
			@Override
			protected boolean run() {
				resetConnections();
				return true;
			}
		};
	}

	public boolean resetConnections() {
		boolean r = false;
		if (!busy.isBusy()) {
			lock.lock(this);
			busy.setBusy(true);
			if (processors.size()>0) {
				CodeRunnerList runnerList = new CodeRunnerList();
				for (NetworkProcessor processor: processors) {
					processor.resetConnections(runnerList);
				}
				r = Waiter.startAndWaitFor(runnerList, config.initializeTimeoutMs);
			}
			busy.setBusy(false);
			lock.unlock(this);
		}
		return r;
	}
	
	public boolean resetState() {
		boolean r = false;
		lock.lock(this);
		if (processors.size()>0) {
			CodeRunnerList runnerList = new CodeRunnerList();
			for (NetworkProcessor processor: processors) {
				processor.resetState(runnerList);
			}
			r = Waiter.startAndWaitFor(runnerList, config.resetStateTimeoutMs);
			currentIO = null;
			previousIO = null;
		}
		lock.unlock(this);
		return r;
	}
	
	public boolean processIO(NetworkIO networkIO) {
		List<String> valueNames = networkIO.getValueNames();
		boolean initialized = false;
		lock.lock(this);
		if (processors.size()==0) {
			networkIO.addError(new Str("Network has not been initialized"));
		} else {
			initialized = true;
			for (String name: config.inputNames) {
				if (!valueNames.contains(name)) {
					Str err = new Str();
					err.sb().append("Missing value for input name: ");
					err.sb().append(name);
					networkIO.addError(err);
				}
			}
		}
		lock.unlock(this);
		if (initialized && !networkIO.hasErrors()) {
			lock.lock(this);
			currentIO = networkIO;
			if (!Waiter.startAndWaitFor(processorChain, currentIO.getTimeoutMs())) {
				networkIO.addError(new Str("Processing networkIO timed out"));
			}
			previousIO = new NetworkIO(currentIO);
			lock.unlock(this);
		}
		return !networkIO.hasErrors();
	}

	public NetworkIO getLastIO() {
		NetworkIO r = null;
		lock.lock(this);
		if (previousIO!=null) {
			r = new NetworkIO(previousIO);
		}
		lock.unlock(this);
		return r;
	}

	public NetworkIO setDirectory(String directory) {
		NetworkIO r = null;
		lock.lock(this);
		config.directory = directory;
		lock.unlock(this);
		return r;
	}
	
	public Str save() {
		return saveLoad(true);
	}

	public Str load() {
		return saveLoad(false);
	}

	public RunCode getSaveRunCode() {
		return this.getSaveLoadRunCode(true);
	}

	public RunCode getLoadRunCode() {
		return this.getSaveLoadRunCode(false);
	}

	@Override
	public boolean isBusy() {
		return busy.isBusy();
	}
	
	protected CodeRunnerList buildInitializeRunnerListNoLock(boolean resetConnections) {
		CodeRunnerList r = new CodeRunnerList();
		for (NetworkProcessorConfig cfg: config.processorConfigs) {
			RunCode code = new RunCode() {
				@Override
				protected boolean run() {
					initializeNetworkProcessor((NetworkProcessorConfig) params[0], resetConnections);
					return true;
				}
			};
			code.params[0] = cfg;
			r.add(code);
		}
		return r;
	}
	
	protected void initializeNetworkProcessor(NetworkProcessorConfig cfg, boolean resetConnections) {
		SDRProcessor sdrProcessor = ProcessorFactory.getNewSDRProcessor(cfg.processorConfig, true);
		if (sdrProcessor!=null) {
			if (sdrProcessor instanceof CellGridProcessor && resetConnections) {
				CellGridProcessor cgp = (CellGridProcessor) sdrProcessor;
				cgp.resetConnections();
			}
			NetworkProcessor processor = new NetworkProcessor(cfg.name,sdrProcessor,cfg.threads);
			initLocker.lock(this);
			processors.add(processor);
			initLocker.unlock(this);
		}
	}
	
	protected void buildProcessorChainNoLock(CodeRunnerChain chain) {
		SortedMap<Integer,List<Processor>> processorsByLayer = getLayerProcessorsNoLock();
		for (Entry<Integer,List<Processor>> entry: processorsByLayer.entrySet()) {
			CodeRunnerList runnerList = new CodeRunnerList();
			for (Processor processor: entry.getValue()) {
				RunCode code = new RunCode() {
					@Override
					protected boolean run() {
						int layer = (int) params[0];
						Processor processor = (Processor) params[1];
						processIONoLock(layer, processor);
						return true;
					}
				};
				code.params = new Object[2];
				code.params[0] = entry.getKey();
				code.params[1] = processor;
				runnerList.add(code);
			}
			chain.add(runnerList);
		}
	}
	
	protected SortedMap<Integer,List<Processor>> getLayerProcessorsNoLock() {
		SortedMap<Integer,List<Processor>> r = new TreeMap<Integer,List<Processor>>();
		SortedMap<Integer,List<NetworkProcessorConfig>> configs = config.getLayerProcessorConfigs();
		for (Entry<Integer,List<NetworkProcessorConfig>> entry: configs.entrySet()) {
			List<Processor> processors = new ArrayList<Processor>();
			for (NetworkProcessorConfig cfg: entry.getValue()) {
				Processor processor = getProcessorNoLock(cfg.name);
				if (processor!=null) {
					processors.add(processor);
				}
			}
			r.put(entry.getKey(),processors);
		}
		return r;
	}

	protected Processor getProcessorNoLock(String name) {
		Processor r = null;
		for (Processor processor: processors) {
			if (processor.getName().equals(name)) {
				r = processor;
				break;
			}
		}
		return r;
	}

	protected void setLayerPropertyNoLock(int layer, String property, Object value) {
		for (NetworkProcessorConfig cfg: config.processorConfigs) {
			if (cfg.layer==layer) {
				setProcessorPropertyNoLock(cfg.name, property, value);
			}
		}
	}
	
	protected void setProcessorPropertyNoLock(String name, String property, Object value) {
		if (name.equals("*")) {
			for (Processor processor: processors) {
				processor.setProperty(property, value);
			}
		} else {
			Processor processor = getProcessorNoLock(name);
			if (processor!=null) {
				processor.setProperty(property, value);
			}
		}
	}
	
	protected void processIONoLock(int layer, Processor processor) {
		List<NetworkLink> linksTo = config.getNetworkLinksTo(processor.getName());
		if (linksTo.size()>0) {
			SDR[] inputs = new SDR[linksTo.size()];
			for (NetworkLink link: linksTo) {
				if (config.getInputNames().contains(link.fromName)) {
					Object value = currentIO.getValue(link.fromName);
					if (value instanceof SDR) {
						inputs[link.toIndex] = (SDR) value;
					} else {
						KeyValueSDR sdr = new KeyValueSDR();
						sdr.setValue(value);
						inputs[link.toIndex] = sdr;
					}
				} else {
					NetworkProcessorConfig cfg = config.getNetworkProcessorConfig(link.fromName);
					NetworkIO sourceIO = currentIO;
					if (cfg.layer>=layer) {
						sourceIO = previousIO;
					}
					if (sourceIO!=null) {
						ProcessorIO sourcePIO = sourceIO.getProcessorIO(link.fromName);
						if (sourcePIO!=null && sourcePIO.outputs!=null) {
							if (sourcePIO.outputs.size()>link.fromIndex) {
								inputs[link.toIndex] = sourcePIO.outputs.get(link.fromIndex);
							} else {
								Str err = new Str("Missing source processor output: ");
								err.sb().append(link.fromName);
								err.sb().append("/");
								err.sb().append(link.fromIndex);
								err.sb().append(" for processor: ");
								err.sb().append(link.toName);
								currentIO.addError(err);
							}
						}
					}
				}
			}
			if (inputs[0]!=null) {
				ProcessorIO io = new ProcessorIO();
				for (int i = 0; i < inputs.length; i++) {
					if (inputs[i]!=null) {
						io.inputs.add(inputs[i]);
					}
				}
				processor.processIO(io);
				currentIO.setProcessorIO(processor.getName(),io);
			}
		}
	}

	protected RunCode getSaveLoadRunCode(boolean save) {
		return new RunCode() {
			@Override
			protected boolean run() {
				saveLoad(save);
				return true;
			}
		};
	}
	
	protected Str saveLoad(boolean save) {
		Str err = new Str();
		if (!busy.isBusy()) {
			if (save) {
				Logger.dbg(this, new Str("Saving network ..."));
			} else {
				Logger.dbg(this, new Str("Loading network ..."));
			}
			lock.lock(this);
			busy.setBusy(true);
			int timeoutMs = config.saveTimeoutMs;
			if (!save) {
				timeoutMs = config.loadTimeoutMs;
			}
			CodeRunnerList runnerList = buildSaveLoadRunnerList(save,config.directory);
			if (!Waiter.startAndWaitFor(runnerList, timeoutMs)) {
				if (save) {
					err.sb().append("Saving network timed out");
				} else {
					err.sb().append("Loading network timed out");
				}
			}
			busy.setBusy(false);
			lock.unlock(this);
			if (err.length()==0) {
				if (save) {
					Logger.dbg(this, new Str("Saved network"));
				} else {
					Logger.dbg(this, new Str("Loaded network"));
				}
			} else {
				Logger.err(this, err);
			}
		}
		return err;
	}
	
	protected CodeRunnerList buildSaveLoadRunnerList(boolean save, String directory) {
		CodeRunnerList r = new CodeRunnerList();
		if (save) {
			RunCode code = new RunCode() {
				@Override
				protected boolean run() {
					Logger.dbg(this, new Str("Writing " + config.getFileName() + " ..."));
					config.toFile();
					return true;
				}
			};
			r.add(code);
		}
		for (Processor processor: processors) {
			RunCode code = new RunCode() {
				@Override
				protected boolean run() {
					Processor processor = (Processor) params[0];
					String path = FileIO.addSlash(directory) + processor.getName() + ".txt";
					if (save) {
						Logger.dbg(this, new Str("Writing " + path + " ..."));
						processor.save(path);
					} else {
						Logger.dbg(this, new Str("Reading " + path + " ..."));
						processor.load(path);
					}
					return true;
				}
			};
			code.params[0] = processor;
			r.add(code);
		}
		RunCode code = new RunCode() {
			@Override
			protected boolean run() {
				Str data = new Str(); 
				String path = FileIO.addSlash(directory) + PREVIOUS_IO_FILE_NAME;
				if (save) {
					if (previousIO!=null) {
						data = previousIO.toStr();
					}
					Logger.dbg(this, new Str("Writing " + path + " ..."));
					data.toFile(path);
				} else {
					Logger.dbg(this, new Str("Reading " + path + " ..."));
					Str err = data.fromFile(path);
					if (err.length()==0) {
						previousIO = new NetworkIO();
						previousIO.fromStr(data);
					}
				}
				return true;
			}
		};
		r.add(code);
		return r;
	}
}
