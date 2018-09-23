package nl.zeesoft.zsc.confab;

public class Link {
	public String	symbolFrom		= "";
	public String	symbolTo		= "";
	public int		distance		= 1;
	public String	context			= "";
	
	public int		count			= 0;
	public double	prob			= 0D;
	
	public String getId() {
		return getId(symbolFrom,distance,context,symbolTo);
	}
	
	public static String getId(String symF,int dist,String ctxt,String symT) {
		return symF + "|" + dist + "|" + ctxt + "|" + symT;
	}
}
