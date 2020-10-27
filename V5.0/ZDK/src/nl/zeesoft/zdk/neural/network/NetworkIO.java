package nl.zeesoft.zdk.neural.network;

import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.neural.processors.ProcessorIO;

public class NetworkIO {
	public SortedMap<String,Object>			values			= new TreeMap<String,Object>();
	public SortedMap<String,ProcessorIO>	processorIO		= new TreeMap<String,ProcessorIO>();
}
