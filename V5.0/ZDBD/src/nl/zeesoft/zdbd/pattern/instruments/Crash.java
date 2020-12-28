package nl.zeesoft.zdbd.pattern.instruments;

public class Crash extends PatternInstrument {
	public static String	NAME	= "Crash";
	public static int		GROUP	= 1;
	
	public Crash() {
		
	}
	
	public Crash(int index) {
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
