package nl.zeesoft.zdk.htm.proc;

import java.util.HashMap;
import java.util.List;

public class Classification {
	public int						steps				= 0;
	public HashMap<Object,Integer>	valueCounts			= null;
	public HashMap<String,Integer>	labelCounts			= null;
	public List<Object>				maxCountedValues	= null;
	public List<String> 			maxCountedLabels	= null;
}
