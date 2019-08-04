package nl.zeesoft.zdk.genetic;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.neural.TrainingProgram;

public class EvolverUnit implements Comparable<EvolverUnit> {
	private static final float	TOLERANCE			= 0.1F;
	
	public GeneticNN			geneticNN			= null;
	public TrainingProgram		trainingProgram		= null;
	
	public int compareTo(EvolverUnit o) {
		int r = 0;
		if (geneticNN.neuralNet.size() < o.geneticNN.neuralNet.size()) {
			r = 1;
		} else if (geneticNN.neuralNet.size() > o.geneticNN.neuralNet.size()) {
			r = -1;
		}
		if (r == 0 && !equalsTolerance(trainingProgram.initialResults.averageLoss,o.trainingProgram.initialResults.averageLoss,TOLERANCE)) {
			if (trainingProgram.initialResults.averageLoss < o.trainingProgram.initialResults.averageLoss) {
				r = 1;
			} else if (trainingProgram.initialResults.averageLoss > o.trainingProgram.initialResults.averageLoss) {
				r = -1;
			}
		}
		if (r == 0 && trainingProgram.stopOnSuccess == o.trainingProgram.stopOnSuccess) {
			if (!equalsTolerance(trainingProgram.trainedEpochs,o.trainingProgram.trainedEpochs,TOLERANCE)) {
				if (trainingProgram.trainedEpochs < o.trainingProgram.trainedEpochs) {
					r = 1;
				} else if (trainingProgram.trainedEpochs > o.trainingProgram.trainedEpochs) {
					r = -1;
				}
			} else if (!equalsTolerance(trainingProgram.getLossChangeRate(),o.trainingProgram.getLossChangeRate(),TOLERANCE)) {
				if (trainingProgram.getLossChangeRate() > o.trainingProgram.getLossChangeRate()) {
					r = 1;
				} else if (trainingProgram.getLossChangeRate() < o.trainingProgram.getLossChangeRate()) {
					r = -1;
				}
			}
		}
		return r;
	}
	
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder r = new ZStringBuilder("Code: ");
		r.append(geneticNN.code.getCode().substring(0,16));
		r.append("\n");
		r.append("Size: ");
		r.append("" + geneticNN.neuralNet.size());
		r.append("\n");
		r.append("Initial average loss: ");
		r.append("" + trainingProgram.initialResults.averageLoss);
		r.append("\n");
		r.append("Trained epochs: ");
		r.append("" + trainingProgram.trainedEpochs);
		r.append("\n");
		r.append("Loss rate change: ");
		r.append("" + trainingProgram.getLossChangeRate());
		return r;
	}
	
	public EvolverUnit copy() {
		EvolverUnit r = new EvolverUnit();
		r.geneticNN = geneticNN.copy();
		r.trainingProgram = trainingProgram.copy();
		return r;
	}
	
	private boolean equalsTolerance(float t,float o,float tolerance) {
		boolean r = true;
		float maxDiff = t * tolerance;
		float diff = t - 0;
		if (diff < 0) {
			diff = diff * -1;
		}
		if (diff>maxDiff) {
			r = false;
		}
		return r;
	}
	
	private boolean equalsTolerance(int t,int o,float tolerance) {
		return equalsTolerance((float)t,(float)o,tolerance);
	}
}
