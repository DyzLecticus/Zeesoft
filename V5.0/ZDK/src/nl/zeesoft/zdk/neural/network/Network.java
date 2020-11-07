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
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;
import nl.zeesoft.zdk.thread.Waiter;

public class Network {
	private Lock					lock						= new Lock();
	
	// Configuration
	private NetworkConfig			config						= new NetworkConfig();
	
	private Lock					initLocker					= new Lock();
	private List<NetworkProcessor>	processors					= new ArrayList<NetworkProcessor>();
	private CodeRunnerChain			processorChain				= null;
	
	// State
	private NetworkIO				currentIO					= null;
	private NetworkIO				previousIO					= null;
	
	public Str configure(NetworkConfig config) {
		Str err = config.testConfiguration();
		if (err.length()>0) {
			Logger.err(this, err);
		} else {
			lock.lock(this);
			this.config.copyFrom(config);
			processors.clear();
			processorChain = new CodeRunnerChain();
			lock.unlock(this);
		}
		return err;
	}
	
	public Str initialize(boolean resetConnections) {
		lock.lock(this);
		Str err = config.testConfiguration();
		lock.unlock(this);
		if (err.length()>0) {
			Logger.err(this, err);
		} else {
			err = new Str();
			Logger.dbg(this, new Str("Initializing network ..."));

			lock.lock(this);
			CodeRunnerList runnerList = buildInitializeRunnerListNoLock(resetConnections);
			if (!Waiter.startAndWaitFor(runnerList, config.initializeTimeoutMs)) {
				err.sb().append("Network initialization timed out");
			}
			if (processors.size() < config.processorConfigs.size()) {
				err.sb().append("Failed to initialize one or more processors");
			}
			if (err.length()==0) {
				buildProcessorChainNoLock(processorChain);
			} else {
				processors.clear();
			}
			lock.unlock(this);
			
			if (err.length()==0) {
				Logger.dbg(this, new Str("Initialized network"));
			} else {
				Logger.err(this, err);
			}
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

	public boolean resetConnections() {
		boolean r = false;
		lock.lock(this);
		if (processors.size()>0) {
			CodeRunnerList runnerList = new CodeRunnerList();
			for (NetworkProcessor processor: processors) {
				processor.resetConnections(runnerList);
			}
			r = Waiter.startAndWaitFor(runnerList, config.resetStateTimeoutMs);
		}
		lock.unlock(this);
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
		lock.lock(this);
		if (processors.size()==0) {
			networkIO.addError(new Str("Network has not been initialized"));
		} else {
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
		if (!networkIO.hasErrors()) {
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

	public Str save(String directory) {
		return saveLoad(true, directory);
	}

	public Str load(String directory) {
		return saveLoad(false, directory);
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
				if (config.inputNames.contains(link.fromName)) {
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
					ProcessorIO sourcePIO = sourceIO.getProcessorIO(link.fromName);
					if (sourcePIO.outputs.size()>link.fromIndex) {
						inputs[link.toIndex] = sourcePIO.outputs.get(link.fromIndex);
					} else {
						Str err = new Str("Missing source processor output: ");
						err.sb().append(link.fromName);
						err.sb().append("/");
						err.sb().append(link.fromIndex);
						currentIO.addError(err);
					}
				}
			}
			if (inputs[0]!=null) {
				ProcessorIO io = new ProcessorIO();
				for (int i = 0; i < inputs.length; i++) {
					io.inputs.add(inputs[i]);
				}
				processor.processIO(io);
				currentIO.setProcessorIO(processor.getName(),io);
			}
		}
	}
	
	protected Str saveLoad(boolean save, String directory) {
		Str err = new Str();
		if (save) {
			Logger.dbg(this, new Str("Saving network ..."));
		} else {
			Logger.dbg(this, new Str("Loading network ..."));
		}
		lock.lock(this);
		int timeoutMs = config.saveTimeoutMs;
		if (!save) {
			timeoutMs = config.loadTimeoutMs;
		}
		CodeRunnerList runnerList = buildSaveLoadRunnerList(save,directory);
		if (!Waiter.startAndWaitFor(runnerList, timeoutMs)) {
			if (save) {
				err.sb().append("Saving network timed out");
			} else {
				err.sb().append("Loading network timed out");
			}
		}
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
		return err;
	}
	
	protected CodeRunnerList buildSaveLoadRunnerList(boolean save, String directory) {
		CodeRunnerList r = new CodeRunnerList();
		for (Processor processor: processors) {
			RunCode code = new RunCode() {
				@Override
				protected boolean run() {
					Processor processor = (Processor) params[0];
					String path = FileIO.addSlash(directory) + processor.getName();
					if (save) {
						processor.save(path);
					} else {
						processor.load(path);
					}
					return true;
				}
			};
			code.params[0] = processor;
			r.add(code);
		}
		return r;
	}
}
