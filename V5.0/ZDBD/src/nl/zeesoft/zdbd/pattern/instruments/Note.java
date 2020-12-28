package nl.zeesoft.zdbd.pattern.instruments;

public class Note extends PatternInstrument {
	public static String	NAME	= "Note";
	public static int		GROUP	= 2;
	
	public Note() {
		
	}
	
	public Note(int index) {
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
