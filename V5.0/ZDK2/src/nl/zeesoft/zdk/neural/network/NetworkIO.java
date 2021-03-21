package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Lock;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;

public class NetworkIO {
	protected Lock								lock			= new Lock(this);
	
	protected int								timeoutMs		= 1000;
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
	
	public Object getInput(String name) {
		lock.lock();
		Object r = inputs.get(name);
		lock.unlock();
		return r;
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
	
	public int getTimeoutMs() {
		lock.lock();
		int r = timeoutMs;
		lock.unlock();
		return r;
	}

	public void setTimeoutMs(int timeoutMs) {
		lock.lock();
		this.timeoutMs = timeoutMs;
		lock.unlock();
	}
	
	protected void addError(String msg) {
		lock.lock();
		errors.add(msg);
		lock.unlock();
	}
	
	protected void addProcessorIO(String name, ProcessorIO io) {
		lock.lock();
		processorIO.put(name, io);
		lock.unlock();
	}
}
