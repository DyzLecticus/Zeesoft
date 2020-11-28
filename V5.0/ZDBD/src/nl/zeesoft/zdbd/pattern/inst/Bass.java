package nl.zeesoft.zdbd.pattern.inst;

public class Bass extends PatternInstrument {
	public static String	NAME	= "Bass";
	public static int		GROUP	= 1;
	
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
