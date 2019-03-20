package nl.zeesoft.zsmc.kb;

public class KbSymbol {
	public String	symbol	= "";
	public String	context	= "";
	
	public int		count	= 0;
	public double	prob	= 0D;

	public KbSymbol copy() {
		KbSymbol r = new KbSymbol();
		r.symbol = symbol;
		r.context = context;
		r.count = count;
		r.prob = prob;
		return r;
	}

	public String getId() {
		return getId(symbol,context);
	}
	
	public static String getId(String sym,String ctxt) {
		return sym + "|" + ctxt;
	}
}
