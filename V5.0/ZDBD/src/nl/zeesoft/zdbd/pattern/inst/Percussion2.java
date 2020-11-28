package nl.zeesoft.zdbd.pattern.inst;

public class Percussion2 extends PatternInstrument {
	public static String	NAME	= "Percussion2";
	public static int		GROUP	= 2;
	
	public Percussion2(int index) {
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
