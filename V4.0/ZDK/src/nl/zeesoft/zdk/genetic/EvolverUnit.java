package nl.zeesoft.zdk.genetic;

import java.text.DecimalFormat;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.neural.TrainingProgram;

public class EvolverUnit implements Comparable<EvolverUnit>, JsAble {
	private static final float	TOLERANCE			= 0.1F;
	
	public GeneticNN			geneticNN			= null;
	public TrainingProgram		trainingProgram		= null;
	
	public int compareTo(EvolverUnit o) {
		int r = 0;
		if (trainingProgram.latestResults!=null && o.trainingProgram.latestResults!=null) {
			if (trainingProgram.latestResults.success && !o.trainingProgram.latestResults.success) {
				r = 1;
			} else if (!trainingProgram.latestResults.success && o.trainingProgram.latestResults.success) {
				r = -1;
			}
		}
		if (r == 0) {
			if (geneticNN.neuralNet.size() < o.geneticNN.neuralNet.size()) {
				r = 1;
			} else if (geneticNN.neuralNet.size() > o.geneticNN.neuralNet.size()) {
				r = -1;
			}
		}
		if (r==0) {
			if (trainingProgram.latestResults==null || o.trainingProgram.latestResults==null) {
				if (!equalsTolerance(trainingProgram.initialResults.averageLoss,o.trainingProgram.initialResults.averageLoss,TOLERANCE)) {
					if (trainingProgram.initialResults.averageLoss < o.trainingProgram.initialResults.averageLoss) {
						r = 1;
					} else if (trainingProgram.initialResults.averageLoss > o.trainingProgram.initialResults.averageLoss) {
						r = -1;
					}
				}
			} else if (!equalsTolerance(trainingProgram.getTrainingResult(),o.trainingProgram.getTrainingResult(),TOLERANCE)) {
				if (trainingProgram.getTrainingResult() < o.trainingProgram.getTrainingResult()) {
					r = 1;
				} else if (trainingProgram.getTrainingResult() > o.trainingProgram.getTrainingResult()) {
					r = -1;
				}
			}
		}
		return r;
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		
		JsElem gnnElem = new JsElem("geneticNN",true);
		json.rootElement.children.add(gnnElem);
		gnnElem.children.add(geneticNN.toJson().rootElement);
				
		JsElem tpElem = new JsElem("trainingProgram",true);
		json.rootElement.children.add(tpElem);
		tpElem.children.add(trainingProgram.toJson().rootElement);
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			JsElem gnnElem = json.rootElement.getChildByName("geneticNN");
			JsElem tpElem = json.rootElement.getChildByName("trainingProgram");
			if (gnnElem!=null && tpElem!=null) {
				JsFile js = new JsFile();
				js.rootElement = gnnElem.children.get(0);
				geneticNN = new GeneticNN();
				geneticNN.fromJson(js);
				
				js.rootElement = tpElem.children.get(0);
				trainingProgram = new TrainingProgram(geneticNN.neuralNet);
				trainingProgram.fromJson(js);
			}
		}
	}
	
	public ZStringBuilder toStringBuilder() {
		DecimalFormat df = new DecimalFormat("0.00000");
		ZStringBuilder r = new ZStringBuilder("- Code: ");
		r.append(geneticNN.code.getCode().substring(0,16));
		r.append("\n");
		r.append("- Size: ");
		r.append("" + geneticNN.neuralNet.size());
		r.append("\n");
		r.append("- Initial average loss: ");
		r.append(df.format(trainingProgram.initialResults.averageLoss));
		if (trainingProgram.latestResults!=null) {
			r.append(" (final: ");
			r.append(df.format(trainingProgram.latestResults.averageLoss));
			r.append(")");
			r.append("\n");
			r.append("- Total average loss: ");
			r.append(df.format(trainingProgram.totalAverageLoss));
			r.append(" (epochs: ");
			r.append("" + trainingProgram.trainedEpochs);
			r.append(")");
			r.append("\n");
			r.append("- Training result: ");
			r.append(df.format(trainingProgram.getTrainingResult()));
		}
		return r;
	}

	public ZStringBuilder toLogLine(String action) {
		DecimalFormat df = new DecimalFormat("0.00000");
		ZStringBuilder r = new ZStringBuilder();
		r.append((new ZDate()).getDateTimeString());
		r.append(" ");
		r.append(action);
		r.append(" code: ");
		r.append(geneticNN.code.getCode().substring(0,16));
		r.append(", size: ");
		r.append("" + geneticNN.neuralNet.size());
		r.append(", initial loss: ");
		r.append(df.format(trainingProgram.initialResults.averageLoss));
		if (trainingProgram.latestResults!=null) {
			r.append(" (final: ");
			r.append(df.format(trainingProgram.latestResults.averageLoss));
			r.append(")");
			r.append(", total loss: ");
			r.append(df.format(trainingProgram.totalAverageLoss));
			r.append(", result: ");
			r.append(df.format(trainingProgram.getTrainingResult()));
			r.append(" (epochs: ");
			r.append("" + trainingProgram.trainedEpochs);
			r.append(")");
		}
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
		float diff = t - o;
		if (diff < 0) {
			diff = diff * -1;
		}
		if (diff>maxDiff) {
			r = false;
		}
		return r;
	}
}
