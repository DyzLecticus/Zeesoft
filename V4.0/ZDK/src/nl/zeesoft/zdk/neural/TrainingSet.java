package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.matrix.functions.ZRandomize;

public class TrainingSet {
	public List<Training> 	trainings	 	= new ArrayList<Training>();
	public float			errorTolerance	= 0.1F;
	
	public float			averageError	= 0;
	public boolean			success			= true;
	
	public void finalize() {
		averageError = 0;
		success = true;
		float total = 0;
		for (Training t: trainings) {
			if (!trainingIsSuccess(t)) {
				success = false;
			}
			for (int i = 0; i<t.errors.length; i++) {
				averageError += t.errors[i];
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
		r.errorTolerance = errorTolerance;
		r.averageError = averageError;
		r.success = success;
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
	
	protected boolean trainingIsSuccess(Training t) {
		boolean r = true;
		for (int i = 0; i < t.errors.length; i++) {
			t.errors[i] = t.expectations[i] - t.outputs[i];
			if (t.errors[i]!=0.0) {
				float diff = t.errors[i];
				if (diff<0) {
					diff = diff * -1;
				}
				if (diff>errorTolerance) {
					r = false;
				}
			}
		}
		return r;
	}
}
