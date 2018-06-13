package nl.zeesoft.zsmc.sequence;

public class AnalyzerSymbol {
	public String	symbol	= "";
	public int		count	= 0;
	public double	prob	= 0D;
	
	public AnalyzerSymbol copy() {
		AnalyzerSymbol r = new AnalyzerSymbol();
		r.symbol = this.symbol;
		r.count = this.count;
		r.prob = this.prob;
		return r;
	}
}
