package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.neural.processor.ProcessorIO;

public class NetworkIO {
	public SortedMap<String,Object>				inputs			= new TreeMap<String,Object>();
	public SortedMap<String,List<ProcessorIO>>	processorIO		= new TreeMap<String,List<ProcessorIO>>();
	public List<String>							errors			= new ArrayList<String>();
	
	public NetworkIO() {
		
	}
	
	public NetworkIO(String name, Object value) {
		inputs.put(name, value);
	}
}
