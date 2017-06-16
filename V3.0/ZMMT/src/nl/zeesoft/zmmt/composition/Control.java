package nl.zeesoft.zmmt.composition;

public class Control {
	public static final int			VOLUME					= 7;
	public static final int			PAN						= 10;
	public static final int			EXPRESSION				= 11;
	public static final int			MODULATION				= 1;
	public static final int			REVERB					= 91;
	public static final int			CHORUS					= 93;
	public static final int			FILTER					= 74;
	public static final int			RESONANCE				= 71;
	public static final int			ATTACK					= 73;
	public static final int			DECAY					= 75;
	public static final int			RELEASE					= 72;
	public static final int			VIB_RATE				= 76;
	public static final int			VIB_DEPTH				= 77;
	public static final int			VIB_DELAY				= 78;

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
