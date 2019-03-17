package nl.zeesoft.zsmc.kb;

public class KbLink {
	public String	symbolFrom		= "";
	public String	symbolTo		= "";
	public int		distance		= 1;
	public String	context			= "";
	
	public int		count			= 0;
	public double	prob			= 0D;
	
	public KbLink copy() {
		KbLink r = new KbLink();
		r.symbolFrom = symbolFrom;
		r.symbolTo = symbolTo;
		r.distance = distance;
		r.context = context;
		r.count = count;
		r.prob = prob;
		return r;
	}
	
	public String getId() {
		return getId(symbolFrom,distance,context,symbolTo);
	}
	
	public static String getId(String symF,int dist,String ctxt,String symT) {
		return symF + "|" + dist + "|" + ctxt + "|" + symT;
	}
}
