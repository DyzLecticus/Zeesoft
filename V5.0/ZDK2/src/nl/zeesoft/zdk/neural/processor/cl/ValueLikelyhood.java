package nl.zeesoft.zdk.neural.processor.cl;

public class ValueLikelyhood implements Comparable<ValueLikelyhood>{
	public Object	value		= null;
	public float	likelyhood	= 0F;
	
	public ValueLikelyhood() {
		
	}
	
	public ValueLikelyhood(Object value, float likelyhood) {
		this.value = value;
		this.likelyhood = likelyhood;
	}
	
	@Override
	public int compareTo(ValueLikelyhood o) {
		int r = 0;
		if (o.likelyhood>likelyhood) {
			r = 1;
		} else if (o.likelyhood<likelyhood) {
			r = -1;
		}
		return r;
	}
}
