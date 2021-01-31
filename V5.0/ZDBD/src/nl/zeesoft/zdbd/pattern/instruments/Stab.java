package nl.zeesoft.zdbd.pattern.instruments;

public class Stab extends PatternInstrument {
	public static String	NAME	= "Stab";
	public static int		GROUP	= 3;
	
	public Stab() {
		
	}
	
	public Stab(int index) {
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
