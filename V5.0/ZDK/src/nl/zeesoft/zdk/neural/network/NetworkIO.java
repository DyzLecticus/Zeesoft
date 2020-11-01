package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.processors.ProcessorIO;
import nl.zeesoft.zdk.thread.Lock;

public class NetworkIO {
	private Lock							lock			= new Lock();
	
	private SortedMap<String,Object>		values			= new TreeMap<String,Object>();
	private int								timeoutMs		= 1000;
	private SortedMap<String,ProcessorIO>	processorIO		= new TreeMap<String,ProcessorIO>();
	private List<Str>						errors			= new ArrayList<Str>();
	
	public NetworkIO() {
		
	}
	
	public NetworkIO(NetworkIO io) {
		copyFrom(io);
	}
	
	public void copyFrom(NetworkIO io) {
		io.lock.lock(this);
		lock.lock(this);
		values.clear();
		processorIO.clear();
		errors.clear();
		
		this.values.putAll(io.values);
		this.timeoutMs = io.timeoutMs;
		this.processorIO.putAll(io.processorIO);
		this.errors.addAll(io.errors);
		lock.unlock(this);
		io.lock.unlock(this);
	}
	
	public void setValue(String name, Object value) {
		if (name!=null && name.length()>0 && value!=null) {
			lock.lock(this);
			values.put(name, value);
			lock.unlock(this);
		}
	}
	
	public List<String> getValueNames() {
		lock.lock(this);
		List<String> r = new ArrayList<String>(values.keySet());
		lock.unlock(this);
		return r;
	}
	
	public Object getValue(String name) {
		lock.lock(this);
		Object r = values.get(name);
		lock.unlock(this);
		return r;
	}

	public int getTimeoutMs() {
		lock.lock(this);
		int r = timeoutMs;
		lock.unlock(this);
		return r;
	}

	public void setTimeoutMs(int timeoutMs) {
		lock.lock(this);
		this.timeoutMs = timeoutMs;
		lock.unlock(this);
	}
	
	public List<String> getProcessorNames() {
		lock.lock(this);
		List<String> r = new ArrayList<String>(processorIO.keySet());
		lock.unlock(this);
		return r;
	}
	
	public void setProcessorIO(String name, ProcessorIO io) {
		lock.lock(this);
		processorIO.put(name, io);
		lock.unlock(this);
	}
	
	public ProcessorIO getProcessorIO(String name) {
		lock.lock(this);
		ProcessorIO r = processorIO.get(name);
		lock.unlock(this);
		return r;
	}
	
	public void addError(Str err) {
		lock.lock(this);
		errors.add(err);
		lock.unlock(this);
	}
	
	public boolean hasErrors() {
		lock.lock(this);
		boolean r = errors.size() > 0;
		lock.unlock(this);
		return r;
	}
	
	public List<Str> getErrors() {
		lock.lock(this);
		List<Str> r = new ArrayList<Str>(errors);
		lock.unlock(this);
		return r;
	}
}
