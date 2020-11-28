package nl.zeesoft.zdbd.pattern.inst;

public abstract class PatternInstrument {
	public static int	OFF				= 0;
	public static int	ON				= 1;
	public static int	ACCENT			= 2;
	
	public int			index			= 0;
	public int[]		stepValues		= new int[64];
	
	public PatternInstrument(int index) {
		this.index = index;
		for (int s = 0; s < stepValues.length; s++) {
			stepValues[s] = OFF;
		}
	}
	
	public abstract String name();
	
	public abstract int group();
}
