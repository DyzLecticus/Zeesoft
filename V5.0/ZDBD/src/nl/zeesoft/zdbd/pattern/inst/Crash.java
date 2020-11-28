package nl.zeesoft.zdbd.pattern.inst;

public class Crash extends PatternInstrument {
	public static String	NAME	= "Crash";
	public static int		GROUP	= 2;
	
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
