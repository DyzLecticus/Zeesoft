package nl.zeesoft.zsmc.sequence;

public class SequenceAnalyzerSymbolLink {
	public String			symbolFrom	= "";
	public String			symbolTo	= "";
	public AnalyzerSymbol	asFrom		= null;
	public AnalyzerSymbol	asTo		= null;
	public String			context		= "";
	public int				count		= 0;
	public double			prob		= 0D;
	public double			probContext	= 0D;
	
	public SequenceAnalyzerSymbolLink copy() {
		SequenceAnalyzerSymbolLink r = new SequenceAnalyzerSymbolLink();
		r.symbolFrom = symbolFrom;
		r.symbolTo = symbolTo;
		r.asFrom = asFrom.copy();
		r.asTo = asTo.copy();
		r.context = context;
		r.count = count;
		r.prob = prob;
		r.probContext = probContext;
		return r;
	}
}
