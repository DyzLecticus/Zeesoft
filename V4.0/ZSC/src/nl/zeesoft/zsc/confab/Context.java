package nl.zeesoft.zsc.confab;

import java.util.ArrayList;
import java.util.List;

public class Context {
	public String			contextSymbol				= "";
		
	public int				symbolTotalCount			= 0;
	public int				totalSymbols				= 0;
	public double			symbolMaxProb				= 0D;
	public double			symbolMinProb				= 0D;
	
	public int				linkTotalCount				= 0;
	public int				totalLinks					= 0;
	public double			linkMaxProb					= 0D;
	public double			linkMinProb					= 0D;
	
	public double			symbolBandwidth				= 0D;
	public double			linkBandwidth				= 0D;
	public double			symbolToLinkBandwidthFactor	= 0D;
	
	public List<String>		knownSymbols				= new ArrayList<String>();
	
	public Context copy() {
		Context r = new Context();
		r.contextSymbol = this.contextSymbol;
		r.symbolTotalCount = this.symbolTotalCount;
		r.totalSymbols = this.totalSymbols;
		r.symbolMaxProb = this.symbolMaxProb;
		r.symbolMinProb = this.symbolMinProb;
		r.linkTotalCount = this.linkTotalCount;
		r.totalLinks = this.totalLinks;
		r.linkMaxProb = this.linkMaxProb;
		r.linkMinProb = this.linkMinProb;
		r.symbolBandwidth = this.symbolBandwidth;
		r.linkBandwidth = this.linkBandwidth;
		r.symbolToLinkBandwidthFactor = this.symbolToLinkBandwidthFactor;
		r.knownSymbols = new ArrayList<String>(this.knownSymbols);
		return r;
	}
}
