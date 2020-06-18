package nl.zeesoft.zdk.midi;

public class State {
	public int		beatsPerMinute			= 120;
	public int		stepsPerBeat			= 4;
	public float[]	stepDelayPercentages	= new float[stepsPerBeat];
	
	public float[] getStepDelayPercentages() {
		if (stepDelayPercentages.length!=stepsPerBeat) {
			float[] r = new float[stepsPerBeat];
			for (int i = 0; i < stepDelayPercentages.length; i++) {
				if (i>=r.length) {
					break;
				} else {
					r[i] = stepDelayPercentages[i];
				}
			}
			stepDelayPercentages = r;
		}
		return stepDelayPercentages;
	}
	
	public State copy() {
		State r = new State();
		r.beatsPerMinute = beatsPerMinute;
		r.stepsPerBeat = stepsPerBeat;
		r.stepDelayPercentages = new float[stepsPerBeat];
		for (int i = 0; i < stepsPerBeat; i++) {
			if (i>=stepDelayPercentages.length) {
				break;
			} else {
				r.stepDelayPercentages[i] = stepDelayPercentages[i];
			}
		}
		return r;
	}
}
