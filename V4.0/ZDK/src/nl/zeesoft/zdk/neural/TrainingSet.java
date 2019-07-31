package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.matrix.functions.ZRandomize;

public class TrainingSet {
	public List<Training> 	trainings	 	= new ArrayList<Training>();
	
	public float			averageError	= 0;
	public boolean			success			= true;
	
	public void finalize() {
		averageError = 0;
		success = true;
		float total = 0;
		for (Training t: trainings) {
			if (!t.success) {
				success = false;
			}
			for (int i = 0; i<t.errors.length; i++) {
				if (t.errors[i]>0) {
					averageError += t.errors[i];
				} else if (t.errors[i]<0) {
					averageError += t.errors[i] * -1F;
				}
				total++;
			}
		}
		if (total>0) {
			averageError = averageError / total;
		}
	}
	
	public TrainingSet copy() {
		TrainingSet r = new TrainingSet();
		for (Training ex: trainings) {
			r.trainings.add((Training) ex.copy());
		}
		return r;
	}
	
	public void randomizeOrder() {
		ZRandomize rand = new ZRandomize();
		int l = trainings.size();
		if (l>1) {
			for (int i = 0; i < l; i++) {
				Training t = trainings.remove(i);
				trainings.add(rand.getRandomInt(0,l - 1),t);
			}
		}
	}
}
