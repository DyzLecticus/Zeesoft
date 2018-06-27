package nl.zeesoft.zsd.sequence;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;

public class SequenceMatcherSequence {
	public String							context		= "";
	public ZStringSymbolParser				sequence	= null;
	public List<String>						symbols		= null;
	public List<SequenceAnalyzerSymbolLink>	links		= null;
	
	public SequenceMatcherSequence copy() {
		SequenceMatcherSequence r = new SequenceMatcherSequence();
		r.context = context;
		r.sequence = new ZStringSymbolParser(sequence);
		r.symbols = new ArrayList<String>(symbols);
		r.links = new ArrayList<SequenceAnalyzerSymbolLink>(links);
		return r;
	}
}
