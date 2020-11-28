package nl.zeesoft.zdbd.pattern.inst;

public class Percussion1 extends PatternInstrument {
	public static String	NAME	= "Percussion1";
	public static int		GROUP	= 2;
	
	public Percussion1(int index) {
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
