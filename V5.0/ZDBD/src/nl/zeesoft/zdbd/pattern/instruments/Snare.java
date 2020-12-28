package nl.zeesoft.zdbd.pattern.instruments;

public class Snare extends PatternInstrument {
	public static String	NAME	= "Snare";
	public static int		GROUP	= 1;
	
	public Snare() {
		
	}
	
	public Snare(int index) {
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
