package nl.zeesoft.zmmt.composition;

public class Control {
	public static final int			MODULATION				= 1;
	public static final int			PAN						= 10;
	public static final int			VOLUME					= 7;
	public static final int			EXPRESSION				= 11;
	public static final int			FILTER					= 74;
	public static final int			REVERB					= 91;
	public static final int			CHORUS					= 93;
	
	public String					instrument				= "";
	public int						control					= 0;		
	public int						step					= 0;
	public int						percentage				= 0;

	public Control copy() {
		Control r = new Control();
		r.instrument = instrument;
		r.control = control;
		r.step = step;
		r.percentage = percentage;
		return r;
	}
	
	@Override
	public String toString() {
		return "" + percentage;
	}
}
