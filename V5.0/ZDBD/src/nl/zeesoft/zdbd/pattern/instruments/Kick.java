package nl.zeesoft.zdbd.pattern.instruments;

public class Kick extends PatternInstrument {
	public static String	NAME	= "Kick";
	public static int		GROUP	= 1;
	
	public Kick() {
		
	}
	
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
