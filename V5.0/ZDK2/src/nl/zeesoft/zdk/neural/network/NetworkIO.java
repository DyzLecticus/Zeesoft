package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Lock;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;

public class NetworkIO {
	protected Lock								lock			= new Lock(this);
	
	protected SortedMap<String,Object>			inputs			= new TreeMap<String,Object>();
	protected SortedMap<String,ProcessorIO>		processorIO		= new TreeMap<String,ProcessorIO>();
	protected List<String>						errors			= new ArrayList<String>();
	
	public NetworkIO() {
		
	}
	
	public NetworkIO(String name, Object value) {
		addInput(name, value);
	}
	
	public void addInput(String name, Object value) {
		lock.lock();
		inputs.put(name, value);
		lock.unlock();
	}
	
	public ProcessorIO getProcessorIO(String name) {
		lock.lock();
		ProcessorIO r = processorIO.get(name);
		lock.unlock();
		return r;
	}
	
	public List<String> getErrors() {
		lock.lock();
		List<String> r = new ArrayList<String>(errors);
		for (ProcessorIO pio: processorIO.values()) {
			if (pio.error.length()>0) {
				r.add(pio.error);
			}
		}
		lock.unlock();
		return r;
	}
	
	protected void addProcessorIO(String name, ProcessorIO io) {
		lock.lock();
		processorIO.put(name, io);
		lock.unlock();
	}
}
