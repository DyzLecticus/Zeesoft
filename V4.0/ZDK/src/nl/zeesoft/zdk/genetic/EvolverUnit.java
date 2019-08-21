package nl.zeesoft.zdk.genetic;

import java.text.DecimalFormat;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.neural.NeuralNet;
import nl.zeesoft.zdk.neural.TrainingProgram;

public class EvolverUnit implements Comparable<EvolverUnit>, JsAble {
	private static final float	TOLERANCE			= 0.1F;
	
	public GeneticCode			code				= null;
	public int					codePropertyStart	= 0;
	public NeuralNet			neuralNet			= null;
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
			if (neuralNet.size() < o.neuralNet.size()) {
				r = 1;
			} else if (neuralNet.size() > o.neuralNet.size()) {
				r = -1;
			}
		}
		if (r == 0 && !equalsTolerance(trainingProgram.initialResults.averageLoss,o.trainingProgram.initialResults.averageLoss,TOLERANCE)) {
			if (trainingProgram.initialResults.averageLoss < o.trainingProgram.initialResults.averageLoss) {
				r = 1;
			} else if (trainingProgram.initialResults.averageLoss > o.trainingProgram.initialResults.averageLoss) {
				r = -1;
			}
		}
		if (trainingProgram.latestResults!=null && o.trainingProgram.latestResults!=null) {
			if (r == 0) {
				if (!equalsTolerance(trainingProgram.trainedEpochs,o.trainingProgram.trainedEpochs,TOLERANCE)) {
					if (trainingProgram.trainedEpochs < o.trainingProgram.trainedEpochs) {
						r = 1;
					} else if (trainingProgram.trainedEpochs > o.trainingProgram.trainedEpochs) {
						r = -1;
					}
				} else if (!equalsTolerance(trainingProgram.totalAverageLoss,o.trainingProgram.totalAverageLoss,TOLERANCE)) {
					if (trainingProgram.totalAverageLoss < o.trainingProgram.totalAverageLoss) {
						r = 1;
					} else if (trainingProgram.totalAverageLoss > o.trainingProgram.totalAverageLoss) {
						r = -1;
					}
				}
			}
		}
		return r;
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("code",code.toCompressedCode(),true));
		json.rootElement.children.add(new JsElem("codePropertyStart","" + codePropertyStart));
		
		JsElem bestNNElem = new JsElem("neuralNet",true);
		json.rootElement.children.add(bestNNElem);
		bestNNElem.children.add(neuralNet.toJson().rootElement);
		
		JsElem bestTPElem = new JsElem("trainingProgram",true);
		json.rootElement.children.add(bestTPElem);
		bestTPElem.children.add(trainingProgram.toJson().rootElement);
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		fromJson(json,null);
	}
	
	public void fromJson(JsFile json,Evolver evolver) {
		if (json.rootElement!=null && evolver!=null) {
			codePropertyStart = json.rootElement.getChildInt("codePropertyStart",codePropertyStart);
			JsElem bestCodeElem = json.rootElement.getChildByName("code");
			JsElem bestNNElem = json.rootElement.getChildByName("neuralNet");
			JsElem bestTPElem = json.rootElement.getChildByName("trainingProgram");
			if (bestCodeElem!=null && bestNNElem!=null && bestTPElem!=null) {
				this.code = new GeneticCode();
				this.code.fromCompressedCode(bestCodeElem.value);
				
				JsFile js = new JsFile();
				js.rootElement = bestNNElem.children.get(0);
				neuralNet = new NeuralNet(js);
				
				js.rootElement = bestTPElem.children.get(0);
				trainingProgram = evolver.getNewTrainingProgram(neuralNet);
				trainingProgram.fromJson(js);
			}
		}
	}
	
	public ZStringBuilder toStringBuilder() {
		DecimalFormat df = new DecimalFormat("0.00000");
		ZStringBuilder r = new ZStringBuilder("- Code: ");
		r.append(code.getCode().substring(0,16));
		r.append("\n");
		r.append("- Size: ");
		r.append("" + neuralNet.size());
		r.append("\n");
		r.append("- Initial average loss: ");
		r.append(df.format(trainingProgram.initialResults.averageLoss));
		if (trainingProgram.latestResults!=null) {
			r.append(" (final: ");
			r.append(df.format(trainingProgram.latestResults.averageLoss));
			r.append(")");
		}
		r.append("\n");
		r.append("- Trained epochs: ");
		r.append("" + trainingProgram.trainedEpochs);
		r.append("\n");
		r.append("- Total average loss: ");
		r.append(df.format(trainingProgram.totalAverageLoss));
		return r;
	}

	public ZStringBuilder toLogLine() {
		DecimalFormat df = new DecimalFormat("0.00000");
		ZStringBuilder r = new ZStringBuilder();
		r.append((new ZDate()).getDateTimeString());
		r.append(" Code: ");
		r.append(code.getCode().substring(0,16));
		r.append(", size: ");
		r.append("" + neuralNet.size());
		r.append(", initial average loss: ");
		r.append(df.format(trainingProgram.initialResults.averageLoss));
		if (trainingProgram.latestResults!=null) {
			r.append(" (final: ");
			r.append(df.format(trainingProgram.latestResults.averageLoss));
			r.append(")");
		}
		r.append(", trained epochs: ");
		r.append("" + trainingProgram.trainedEpochs);
		r.append(", total average loss: ");
		r.append(df.format(trainingProgram.totalAverageLoss));
		return r;
	}

	public EvolverUnit copy() {
		EvolverUnit r = new EvolverUnit();
		r.code = new GeneticCode();
		r.code.setCode(code.getCode());
		r.neuralNet = neuralNet.copy();
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
	
	private boolean equalsTolerance(int t,int o,float tolerance) {
		return equalsTolerance((float)t,(float)o,tolerance);
	}
}
