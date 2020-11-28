package nl.zeesoft.zdbd.pattern.inst;

public class Ride extends PatternInstrument {
	public static String	NAME	= "Ride";
	public static int		GROUP	= 2;
	
	public Ride(int index) {
		super(index);
	}

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public int group() {
		return GROUP;
	}
}
