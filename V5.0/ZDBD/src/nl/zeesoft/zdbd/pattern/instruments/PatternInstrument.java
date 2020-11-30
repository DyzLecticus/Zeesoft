package nl.zeesoft.zdbd.pattern.instruments;

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
	
	public void copyFrom(PatternInstrument inst) {
		this.index = inst.index;
		for (int s = 0; s < this.stepValues.length; s++) {
			this.stepValues[s] = inst.stepValues[s];
		}
	}
	
	public abstract String name();
	
	public abstract int group();
}
