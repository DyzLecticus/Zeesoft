package nl.zeesoft.zdbd.pattern.inst;

public class Kick extends PatternInstrument {
	public static String	NAME	= "Kick";
	public static int		GROUP	= 1;
	
	public Kick(int index) {
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
