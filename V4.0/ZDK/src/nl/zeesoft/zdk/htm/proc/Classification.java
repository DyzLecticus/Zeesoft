package nl.zeesoft.zdk.htm.proc;

import java.util.HashMap;
import java.util.List;

/**
 * A Classification is produced by the classifier.
 * It contains all vote counts for all relevant values and/or labels as well as a short list of most counted values and/or labels.
 */
public class Classification {
	public int						steps				= 0;
	public HashMap<Object,Integer>	valueCounts			= null;
	public HashMap<String,Integer>	labelCounts			= null;
	public List<Object>				mostCountedValues	= null;
	public List<String> 			mostCountedLabels	= null;
}
