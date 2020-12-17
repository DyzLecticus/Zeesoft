package nl.zeesoft.zdbd.midi.lfo;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.midi.MidiSequenceUtil;
import nl.zeesoft.zdbd.pattern.Rythm;

public class LFO {
	public static final String	SINE			= "SINE"; 
	public static final String	TRIANGLE		= "LINEAR"; 
		
	public static List<Float> getTickValuesForCycleSteps(Rythm rythm, String type, int cycleSteps) {
		List<Float> r = new ArrayList<Float>();
		int totalTicks = getTotalTicksForCycleSteps(rythm,cycleSteps);
		if (type.equals(SINE)) {
			for (int t = 0; t < totalTicks; t++) {
				double rad = (t / (double)totalTicks) * 360D;
				float val = ((float)Math.sin(Math.toRadians(rad)) + 1F) / 2F;
				r.add(val);
			}
		} else if (type.equals(TRIANGLE)) {
			float val = 0F;
			float incrementPerTick = (1F / (float)totalTicks) * 2F;
			for (int t = 0; t < totalTicks; t++) {
				r.add(val);
				if (t<totalTicks/2) {
					val += incrementPerTick;
				} else {
					val -= incrementPerTick;
				}
				if (val>1F) {
					val = 1F;
				} else if (val<0F) {
					val = 0F;
				}
			}
		}
		return r;
	}
	
	public static int getTotalTicksForCycleSteps(Rythm rythm, int cycleSteps) {
		return MidiSequenceUtil.getTicksPerStep(rythm) * cycleSteps;
	}
}
