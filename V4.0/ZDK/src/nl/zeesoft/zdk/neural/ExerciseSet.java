package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.matrix.functions.ZRandomize;

public class ExerciseSet {
	public List<Exercise> exercises = new ArrayList<Exercise>();
	
	public ExerciseSet copy() {
		ExerciseSet r = new ExerciseSet();
		for (Exercise ex: exercises) {
			r.exercises.add((Exercise) ex.copy());
		}
		r.randomizeOrder();
		return r;
	}
	
	public void randomizeOrder() {
		ZRandomize rand = new ZRandomize();
		int l = exercises.size();
		rand.min = 0;
		rand.max = l - 1;
		for (int i = 0; i < l; i++) {
			Exercise ex = exercises.remove(i);
			exercises.add((int) rand.getRandomFloat(),ex);
		}
	}
}
