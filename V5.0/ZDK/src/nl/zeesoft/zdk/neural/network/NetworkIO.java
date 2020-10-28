package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.neural.processors.ProcessorIO;
import nl.zeesoft.zdk.thread.Lock;

public class NetworkIO {
	private Lock							lock			= new Lock();
	private SortedMap<String,Object>		values			= new TreeMap<String,Object>();
	private SortedMap<String,ProcessorIO>	processorIO		= new TreeMap<String,ProcessorIO>();
	
	public void setValue(String name, Object value) {
		lock.lock(this);
		values.put(name, value);
		lock.unlock(this);
	}
	
	public void setProcessorIO(String name, ProcessorIO io) {
		lock.lock(this);
		processorIO.put(name, io);
		lock.unlock(this);
	}
	
	public List<String> getValueNames() {
		lock.lock(this);
		List<String> r = new ArrayList<String>(values.keySet());
		lock.unlock(this);
		return r;
	}
	
	public List<String> getProcessorNames() {
		lock.lock(this);
		List<String> r = new ArrayList<String>(processorIO.keySet());
		lock.unlock(this);
		return r;
	}
}
