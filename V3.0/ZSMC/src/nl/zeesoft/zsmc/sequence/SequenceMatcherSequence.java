package nl.zeesoft.zsmc.sequence;

import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;

public class SequenceMatcherSequence {
	public String							context		= "";
	public ZStringSymbolParser				sequence	= null;
	public List<String>						symbols		= null;
	public List<SequenceAnalyzerSymbolLink>	links		= null;
}
