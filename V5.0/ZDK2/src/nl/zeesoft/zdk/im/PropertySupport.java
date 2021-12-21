package nl.zeesoft.zdk.im;

public class PropertySupport {
	public int		index		= 0;
	public Object	value		= null;
	public float	support		= 0F;
	
	public PropertySupport() {
		
	}
	
	public PropertySupport(int index, Object value) {
		this.index = index;
		this.value = value;
	}
}
