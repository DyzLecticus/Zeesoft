package nl.zeesoft.zdbd.pattern.instruments;

public class Bass extends PatternInstrument {
	public static String	NAME	= "Bass";
	public static int		GROUP	= 2;
	
	public Bass(int index) {
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
