package nl.zeesoft.zdbd.pattern.instruments;

import nl.zeesoft.zdk.Str;

public abstract class PatternInstrument {
	public static int	OFF				= 0;
	public static int	ON				= 1;
	public static int	ACCENT			= 2;
	
	public int			index			= 0;
	public int[]		stepValues		= new int[64];
	
	public PatternInstrument() {
		
	}
	
	public PatternInstrument(int index) {
		this.index = index;
		for (int s = 0; s < stepValues.length; s++) {
			stepValues[s] = OFF;
		}
	}

	@Override
	public boolean equals(Object obj) {
		boolean r = false;
		if (obj instanceof PatternInstrument) {
			PatternInstrument other = (PatternInstrument) obj;
			if (other.index == this.index) {
				r = true;
				for (int s = 0; s < this.stepValues.length; s++) {
					if (other.stepValues[s]!=this.stepValues[s]) {
						r = false;
						break;
					}
				}
			}
		}
		return r;
	}

	@Override
	public String toString() {
		Str r = new Str(this.getClass().getName());
		r.sb().append("@");
		r.sb().append(index);
		r.sb().append(";");
		for (int s = 0; s < stepValues.length; s++) {
			if (s>0) {
				r.sb().append(",");
			}
			r.sb().append(stepValues[s]);
		}
		return r.toString();
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
