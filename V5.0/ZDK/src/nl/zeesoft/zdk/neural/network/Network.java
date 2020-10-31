package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.KeyValueSDR;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.processors.Processor;
import nl.zeesoft.zdk.neural.processors.ProcessorFactory;
import nl.zeesoft.zdk.neural.processors.ProcessorIO;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;
import nl.zeesoft.zdk.thread.Waiter;

public class Network {
	private Lock				lock					= new Lock();
	
	// Configuration
	private NetworkConfig		config					= new NetworkConfig();
	
	private List<Processor>		processors				= new ArrayList<Processor>();
	private List<String>		learningProcessorNames	= new ArrayList<String>();
	private CodeRunnerChain		processorChain			= null;
	
	// State
	private NetworkIO			currentIO				= null;
	private NetworkIO			previousIO				= null;
	
	// TODO: Multi thread initialize and state reset
	// TODO: Add multi threaded save and load
	
	public Str configure(NetworkConfig config) {
		Str err = config.testConfiguration();
		if (err.length()>0) {
			Logger.err(this, err);
		} else {
			lock.lock(this);
			this.config.copyFrom(config);
			processors.clear();
			processorChain = new CodeRunnerChain();
			learningProcessorNames.clear();
			lock.unlock(this);
		}
		return err;
	}
	
	public Str initialize(boolean resetConnections) {
		Str err = config.testConfiguration();
		if (err.length()>0) {
			Logger.err(this, err);
		} else {
			err = new Str();
			Logger.dbg(this, new Str("Initializing network ..."));
			lock.lock(this);
			for (NetworkProcessorConfig cfg: config.processorConfigs) {
				Processor processor = ProcessorFactory.getNewProcessor(cfg.name, cfg.processorConfig, cfg.threads, resetConnections);
				if (processor!=null) {
					processors.add(
						ProcessorFactory.getNewProcessor(
							cfg.name, cfg.processorConfig, cfg.threads, resetConnections
						)
					);
					learningProcessorNames.add(cfg.name);
				} else {
					if (err.length()>0) {
						err.sb().append("\n");
					}
					err.sb().append("Failed to initialize processor: ");
					err.sb().append(cfg.name);
				}
			}
			if (err.length()==0) {
				buildProcessorChainNoLock(processorChain);
			} else {
				processors.clear();
				learningProcessorNames.clear();
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
	
	public void setProcessorLearning(String name, boolean learning) {
		lock.lock(this);
		if (learning) {
			if (!learningProcessorNames.contains(name)) {
				learningProcessorNames.add(name);
			}
		} else {
			learningProcessorNames.remove(name);
		}
		lock.unlock(this);
	}
	
	public boolean resetState() {
		return resetState(1000);
	}
	
	public boolean resetState(int timeoutMs) {
		lock.lock(this);
		CodeRunnerList runnerList = new CodeRunnerList();
		for (Processor processor: processors) {
			processor.resetState(runnerList);
		}
		boolean r = Waiter.startAndWaitFor(runnerList, timeoutMs);
		currentIO = null;
		previousIO = null;
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
		for (NetworkProcessorConfig cfg: config.processorConfigs) {
			Processor processor = getProcessorNoLock(cfg.name);
			if (processor!=null) {
				List<Processor> names = r.get(cfg.layer);
				if (names==null) {
					names = new ArrayList<Processor>();
					r.put(cfg.layer, names);
				}
				names.add(getProcessorNoLock(cfg.name));
			}
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
	
	protected void processIONoLock(int layer, Processor processor) {
		List<NetworkLink> linksTo = config.getNetworkLinksTo(processor.getName());
		if (linksTo.size()>0) {
			SDR[] inputs = new SDR[linksTo.size()];
			for (NetworkLink link: linksTo) {
				if (config.inputNames.contains(link.fromName)) {
					Object value = currentIO.getValue(link.fromName);
					KeyValueSDR sdr = new KeyValueSDR();
					sdr.setValue(value);
					inputs[link.toIndex] = sdr;
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
				io.learn = learningProcessorNames.contains(processor.getName());
				for (int i = 0; i < inputs.length; i++) {
					io.inputs.add(inputs[i]);
				}
				processor.processIO(io);
				currentIO.setProcessorIO(processor.getName(),io);
			}
		}
	}
}
