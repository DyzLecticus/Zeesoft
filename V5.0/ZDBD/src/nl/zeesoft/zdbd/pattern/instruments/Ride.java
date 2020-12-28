package nl.zeesoft.zdbd.pattern.instruments;

public class Ride extends PatternInstrument {
	public static String	NAME	= "Ride";
	public static int		GROUP	= 1;
	
	public Ride() {
		
	}
	
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
