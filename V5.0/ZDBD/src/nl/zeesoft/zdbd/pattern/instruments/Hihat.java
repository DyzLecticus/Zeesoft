package nl.zeesoft.zdbd.pattern.instruments;

public class Hihat extends PatternInstrument {
	public static String	NAME	= "Hihat";
	public static int		GROUP	= 1;
	
	public Hihat(int index) {
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
