package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import nl.zeesoft.zdk.function.ExecutorTask;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;

public class NetworkIO {	
	protected int								timeoutMs		= 1000;
	protected SortedMap<String,Object>			inputs			= new TreeMap<String,Object>();
	
	protected ConcurrentMap<String,ProcessorIO>	processorIO		= new ConcurrentHashMap<String,ProcessorIO>();
	protected CopyOnWriteArrayList<String>		errors			= new CopyOnWriteArrayList<String>();
	protected NetworkIOStats					stats			= null;
	
	public NetworkIO() {
		
	}
	
	public NetworkIO(String name, Object value) {
		addInput(name, value);
	}
	
	public void addInput(String name, Object value) {
		inputs.put(name, value);
	}
	
	public Object getInput(String name) {
		return inputs.get(name);
	}
	
	public List<String> getProcessorNames() {
		return new ArrayList<String>(processorIO.keySet());
	}
	
	public ProcessorIO getProcessorIO(String name) {
		return processorIO.get(name);
	}
	
	public List<String> getErrors() {
		List<String> r = new ArrayList<String>(errors);
		for (ProcessorIO pio: processorIO.values()) {
			if (pio.error.length()>0) {
				r.add(pio.error);
			}
		}
		return r;
	}
	
	public int getTimeoutMs() {
		return timeoutMs;
	}

	public void setTimeoutMs(int timeoutMs) {
		this.timeoutMs = timeoutMs;
	}
	
	public NetworkIOStats getStats() {
		return stats;
	}
	
	protected void setStats(long start, ExecutorTask task) {
		NetworkIOStats stats = new NetworkIOStats();
		stats.totalNs = System.nanoTime() - start;
		if (task==null) {
			stats.nsPerLayer = new TreeMap<Integer,Long>();
		} else {
			stats.nsPerLayer = task.getNsPerStep();
		}
		this.stats = stats;
	}

	protected void addError(String msg) {
		errors.add(msg);
	}
	
	protected void addProcessorIO(String name, ProcessorIO io) {
		processorIO.put(name, io);
	}
}
