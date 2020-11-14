package nl.zeesoft.zdbd.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import nl.zeesoft.zdbd.pattern.DrumAndBassPattern;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkIO;

public class NetworkTrainer {
	public int			startTrainTemporalMemory	= 8;
	public int			startTrainClassifiers		= 16;
	public int			maxTrainCycles				= 32;
	public float		minimumAverageAccuracy		= 0.950F;
	public float		minimumClassifierAccuracy	= 0.990F;
	
	public List<NetworkIO> trainNetwork(Network network, PatternSequence sequence) {
		if (maxTrainCycles < 4) {
			maxTrainCycles = 4;
		}
		if (startTrainClassifiers >= maxTrainCycles) {
			startTrainClassifiers = (maxTrainCycles - 1);
		}
		if (startTrainTemporalMemory >= startTrainClassifiers) {
			startTrainTemporalMemory = (startTrainClassifiers - 1);
		}
		
		List<NetworkIO> r = new ArrayList<NetworkIO>();
		
		List<NetworkIO> trainingSet = getNetworkIOForPatternSequence(sequence);
		
		network.setProcessorLearn("*", false);
		network.setLayerLearn(NetworkConfigFactory.POOLER_LAYER, true);
		network.setLayerProperty(NetworkConfigFactory.CLASSIFIER_LAYER, "logPredictionAccuracy", true);
		
		long start = System.currentTimeMillis();
		
		NetworkIO lastIO = null;
		Str err = new Str();
		for (int i = 1; i <= maxTrainCycles; i++) {
			Str msg = new Str("Training cycle ");
			msg.sb().append(i);
			msg.sb().append("/");
			msg.sb().append(maxTrainCycles);
			msg.sb().append(" ...");
			Logger.dbg(this, msg);
			if (i == startTrainTemporalMemory) {
				Logger.dbg(this, new Str("(Training memory)"));
				network.setLayerLearn(NetworkConfigFactory.MEMORY_LAYER, true);
			}
			if (i == startTrainClassifiers) {
				Logger.dbg(this, new Str("(Training classifiers)"));
				network.setLayerLearn(NetworkConfigFactory.CLASSIFIER_LAYER, true);
			}
			for (NetworkIO io: trainingSet) {
				NetworkIO result = new NetworkIO(io);
				network.processIO(result);
				r.add(result);
				if (result.hasErrors()) {
					err = result.getErrors().get(0);
					Logger.err(this, err);
					break;
				}
			}
			if (err.length()>0) {
				break;
			}
			
			lastIO = r.get(r.size() - 1);
			if (lastIO.isAccurate(false, minimumAverageAccuracy) && lastIO.isAccurate(true, minimumClassifierAccuracy)) {
				break;
			}
			SortedMap<String,Float> accuracies = lastIO.getClassifierAccuracies(false);
			if (accuracies.size()>0) {
				SortedMap<String,Float> accuracyTrends = lastIO.getClassifierAccuracies(true);
				msg = new Str();
				msg.sb().append("Accuracies (average) / trends: "); 
				msg.sb().append(accuracies.values()); 
				msg.sb().append(" (");
				msg.sb().append(lastIO.getAverageClassifierAccuracy(false));
				msg.sb().append(") / "); 
				msg.sb().append(accuracyTrends.values()); 
				Logger.dbg(this, msg);
			}
		}
		
		if (lastIO!=null) {
			SortedMap<String,Float> accuracies = lastIO.getClassifierAccuracies(false);
			if (accuracies.size()>0) {
				Str msg = new Str();
				msg.sb().append("Training network took; ");
				msg.sb().append((System.currentTimeMillis() - start));
				msg.sb().append(" ms, accuracies;\n");
				for (Entry<String,Float> entry: accuracies.entrySet()) {
					msg.sb().append("- "); 
					msg.sb().append(entry.getKey()); 
					msg.sb().append(": "); 
					msg.sb().append(entry.getValue()); 
					msg.sb().append("\n"); 
				}
				msg.sb().append("- Average: ");
				msg.sb().append(lastIO.getAverageClassifierAccuracy(false));
				Logger.dbg(this, msg);
			}
		}
		
		network.setProcessorLearn("*", false);
		network.setLayerProperty(NetworkConfigFactory.CLASSIFIER_LAYER, "logPredictionAccuracy", false);
		
		return r;
	}
	
	public static List<NetworkIO> getNetworkIOForPatternSequence(PatternSequence sequence) {
		List<NetworkIO> r = new ArrayList<NetworkIO>();
		
		List<DrumAndBassPattern> patterns = sequence.getSequencedPatterns();
		
		int totalSteps = 0;
		for (DrumAndBassPattern pattern: patterns) {
			totalSteps += pattern.rythm.getStepsPerPattern();
		}
		
		for (int i = 0; i < totalSteps; i++) {
			r.add(new NetworkIO());
		}
		
		int start = 0;
		for (DrumAndBassPattern pattern: patterns) {
			List<SDR> rythmSDRs = pattern.rythm.getSDRsForPattern(pattern.num);
			List<SDR> patternSDRs = pattern.getSDRsForPattern();
			int ps = 0;
			for (int s = start; s < start + pattern.rythm.getStepsPerPattern(); s++) {
				NetworkIO io = r.get(s);
				io.setValue(NetworkConfigFactory.CONTEXT_INPUT, rythmSDRs.get(ps));
				io.setValue(NetworkConfigFactory.PATTERN_INPUT, patternSDRs.get(ps));
				ps++;
			}
			start += pattern.rythm.getStepsPerPattern();
		}
		
		return r;
	}
}
