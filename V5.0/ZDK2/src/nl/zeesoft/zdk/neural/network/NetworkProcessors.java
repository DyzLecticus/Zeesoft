package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.function.Executor;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.function.FunctionList;
import nl.zeesoft.zdk.function.FunctionListList;
import nl.zeesoft.zdk.neural.model.CellStats;
import nl.zeesoft.zdk.neural.model.Cells;
import nl.zeesoft.zdk.neural.network.config.ProcessorConfig;
import nl.zeesoft.zdk.neural.processor.CellsProcessor;
import nl.zeesoft.zdk.neural.processor.LearningProcessor;

public class NetworkProcessors extends AbstractNetworkProcessor {
	protected Executor									executor			= null;
	
	protected SortedMap<String,NetworkProcessor>		processors			= new TreeMap<String,NetworkProcessor>();
	protected SortedMap<Integer,List<NetworkProcessor>>	layerProcessors		= new TreeMap<Integer,List<NetworkProcessor>>();
	
	protected NetworkProcessors(Executor executor) {
		this.executor = executor;
	}
	
	protected boolean initialize(Object caller, List<ProcessorConfig> processorConfigs, int timeoutMs) {
		FunctionListList fll = getInitializeProcessorFunctionListList(processorConfigs);
		List<Object> nps = executor.execute(caller, fll, timeoutMs);
		if (nps!=null) {
			for (Object obj: nps) {
				NetworkProcessor np = (NetworkProcessor) obj;
				processors.put(np.name, np);
				List<NetworkProcessor> lps = layerProcessors.get(np.layer);
				if (lps==null) {
					lps = new ArrayList<NetworkProcessor>();
					layerProcessors.put(np.layer, lps);
				}
				lps.add(np);
			}
		}
		return nps!=null;
	}
	
	protected int getNumberOfLayers() {
		return layerProcessors.size();
	}
	
	protected int getNumberOfProcessors() {
		return processors.size();
	}
	
	protected int getWidth() {
		int r = 0;
		for (List<NetworkProcessor> lps: layerProcessors.values()) {
			if (lps.size()>r) {
				r = lps.size();
			}
		}
		return r;
	}
	
	protected void setLearn(int layer, String name, boolean learn) {
		List<NetworkProcessor> nps = getProcessors(layer, name);
		for (NetworkProcessor np: nps) {
			if (np.processor instanceof LearningProcessor) {
				((LearningProcessor) np.processor).setLearn(learn);
			}
		}
	}
	
	protected CellStats getCellStats(Object caller, int layer, String name) {
		CellStats r = new CellStats();
		List<NetworkProcessor> nps = getProcessors(layer, name);
		for (NetworkProcessor np: nps) {
			if (np.processor instanceof CellsProcessor) {
				Cells cells = ((CellsProcessor) np.processor).getCells();
				r.addModelCells(caller, cells);
			}
		}
		return r;
	}
	
	protected List<String> getProcessorNames() {
		return new ArrayList<String>(processors.keySet());
	}
	
	protected List<NetworkProcessor> getProcessors() {
		return getProcessors(Network.ALL_LAYERS, Network.ALL_PROCESSORS);
	}
	
	protected List<NetworkProcessor> getProcessors(int layer, String name) {
		List<NetworkProcessor> r = new ArrayList<NetworkProcessor>();
		for (NetworkProcessor np: processors.values()) {
			if ((layer<=Network.ALL_LAYERS || np.layer == layer) &&
				(name.equals(Network.ALL_PROCESSORS) || np.name.indexOf(name)>=0)
				) {
				r.add(np);
			}
		}
		return r;
	}
	
	protected NetworkProcessor getProcessor(String name) {
		return processors.get(name);
	}
	
	protected SortedMap<Integer,List<NetworkProcessor>> getLayerProcessors() {
		return new TreeMap<Integer,List<NetworkProcessor>>(layerProcessors);
	}

	protected FunctionListList getInitializeProcessorFunctionListList(List<ProcessorConfig> processorConfigs) {
		FunctionListList r = new FunctionListList();
		FunctionList list = new FunctionList();
		for (ProcessorConfig pc: processorConfigs) {
			list.addFunction(getInitializeProcessorFunction(pc));
		}
		r.addFunctionList(list);
		return r;
	}

	protected Function getInitializeProcessorFunction(ProcessorConfig pc) {
		Function r = new Function() {
			@Override
			protected Object exec() {
				return new NetworkProcessor((ProcessorConfig)param1);
			}
		};
		r.param1 = pc;
		return r;
	}
}
