package nl.zeesoft.zsc.confab;

public class Symbol {
	public String	symbol	= "";
	public String	context	= "";
	
	public int		count	= 0;
	public double	prob	= 0D;
	
	public String getId() {
		return getId(symbol,context);
	}
	
	public static String getId(String sym,String ctxt) {
		return sym + "|" + ctxt;
	}
}
