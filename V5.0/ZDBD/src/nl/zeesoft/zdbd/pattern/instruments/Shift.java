package nl.zeesoft.zdbd.pattern.instruments;

public class Shift extends PatternInstrument {
	public static String	NAME	= "Shift";
	public static int		GROUP	= 3;
	
	public Shift() {
		
	}
	
	public Shift(int index) {
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
