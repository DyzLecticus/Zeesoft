package nl.zeesoft.zdbd.pattern.instruments;

public class Octave extends PatternInstrument {
	public static String	NAME	= "Octave";
	public static int		GROUP	= 2;
	
	public Octave(int index) {
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
