package nl.zeesoft.zdk.test.dai;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import nl.zeesoft.zdk.Console;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.optimize.Optimizer;
import nl.zeesoft.zdk.dai.predict.KeyPrediction;
import nl.zeesoft.zdk.dai.predict.ObjMapPrediction;
import nl.zeesoft.zdk.dai.predict.Prediction;
import nl.zeesoft.zdk.dai.predict.PredictionList;
import nl.zeesoft.zdk.dai.predict.Predictor;
import nl.zeesoft.zdk.dai.recognize.ListPatternRecognizer;
import nl.zeesoft.zdk.dai.recognize.PatternRecognizer;

public class TestOptimizer {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		assert new Prediction() != null;
		assert new ObjMapPrediction() != null;
		assert new KeyPrediction() != null;
		
		ObjMapList history = new ObjMapList();
		history.maxSize = 10000;
		
		ObjMapComparator comparator = new ObjMapComparator();
		
		PatternRecognizer patternRecognizer = new PatternRecognizer();
		patternRecognizer.generateDefaultPatternRecognizers();
		
		Predictor predictor = new Predictor();
		
		Optimizer optimizer = new Optimizer();
		
		File f = new File("resources/rec-center-hourly.csv");
		try {
			Scanner myReader = new Scanner(f);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				if (data.contains(" ") && data.contains(",")) {
					String[] vals = data.split(",");
					String[] dt = vals[0].split(" ");
					String[] d = dt[0].split("/");
					String[] t = dt[1].split(":");
					
					Float val = Util.parseFloat(vals[1]);
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.YEAR, Util.parseInt(d[2]));
					cal.set(Calendar.MONTH, Util.parseInt(d[0]));
					cal.set(Calendar.DATE, Util.parseInt(d[1]));
					cal.set(Calendar.HOUR, Util.parseInt(t[0]));
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					history.add(new ObjMap(cal.get(Calendar.DAY_OF_WEEK), cal.get(Calendar.HOUR_OF_DAY), val));
				}
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Console.log("History: " + history.list.size());
		
		Console.log("Generating predictions ...");
		PredictionList predictions = predictor.getPredictions(history, patternRecognizer, comparator, 500);
		Console.log(optimizer.getKeyAccuracy(history, predictions, comparator, "3") + " " + predictions.getKeyConfidence("3"));
		Console.log(predictions.list.get(0));

		Console.log("Optimizing ...");
		optimizer.calculatePatternRecognizerWeights(patternRecognizer, history, predictions, comparator);
		for (ListPatternRecognizer lpr: patternRecognizer.patternRecognizers) {
			Console.log(lpr.indexes + " = " + lpr.accuracy + " > " + lpr.weight);
		}
		
		Console.log("Generating predictions ...");
		predictions = predictor.getPredictions(history, patternRecognizer, comparator, 500);
		Console.log(optimizer.getKeyAccuracy(history, predictions, comparator, "3") + " " + predictions.getKeyConfidence("3"));
		Console.log(predictions.list.get(0));
		
		Console.log("Optimizing ...");
		optimizer.calculatePatternRecognizerWeights(patternRecognizer, history, predictions, comparator);
		for (ListPatternRecognizer lpr: patternRecognizer.patternRecognizers) {
			Console.log(lpr.indexes + " = " + lpr.accuracy + " > " + lpr.weight);
		}
		
		Console.log("Generating predictions ...");
		predictions = predictor.getPredictions(history, patternRecognizer, comparator, 500);
		Console.log(optimizer.getKeyAccuracy(history, predictions, comparator, "3") + " " + predictions.getKeyConfidence("3"));
		Console.log(predictions.list.get(0));
		
		/*
		TestPatternRecognizer.feedPattern(history);
		TestPatternRecognizer.feedPattern(history);
		TestPatternRecognizer.feedPattern(history);
		TestPatternRecognizer.feedPattern(history);
		feedPattern(history);
		feedPattern(history);
		feedPattern(history);
		feedPattern(history);

		List<Prediction> predictions = predictor.getPredictions(history, patternRecognizer, comparator);
		
		//Console.log(optimizer.getErrors(history, predictions, comparator));
		//SortedMap<String, Float> accuracy = optimizer.getKeyAccuracy(history, predictions, comparator);
		//for (Entry<String, Float> entry: accuracy.entrySet()) {
		//	Console.log(entry.getKey() + " = " + entry.getValue());
		//}
		Console.log(optimizer.getAccuracy(history, predictions, comparator));
		
		for (int i = 0; i < 4; i++) {
			optimizer.calculatePatternRecognizerWeights(patternRecognizer, history, predictions, comparator);
			predictions = predictor.getPredictions(history, patternRecognizer, comparator);
			Console.log(optimizer.getAccuracy(history, predictions, comparator));
		}
		
		for (ListPatternRecognizer lpr: patternRecognizer.patternRecognizers) {
			Console.log(lpr.indexes + " = " + lpr.accuracy + " > " + lpr.weight);
		}
		
		feedPattern(history);
		feedPattern(history);
		feedPattern(history);
		feedPattern(history);
		TestPatternRecognizer.feedPattern(history);
		TestPatternRecognizer.feedPattern(history);
		TestPatternRecognizer.feedPattern(history);
		TestPatternRecognizer.feedPattern(history);
		feedPattern(history);
		feedPattern(history);
		feedPattern(history);
		feedPattern(history);

		predictions = predictor.getPredictions(history, patternRecognizer, comparator);
		Console.log(optimizer.getAccuracy(history, predictions, comparator));

		for (int i = 0; i < 10; i++) {
			optimizer.calculatePatternRecognizerWeights(patternRecognizer, history, predictions, comparator);
			predictions = predictor.getPredictions(history, patternRecognizer, comparator);
			Console.log(optimizer.getAccuracy(history, predictions, comparator));
		}

		for (ListPatternRecognizer lpr: patternRecognizer.patternRecognizers) {
			Console.log(lpr.indexes + " = " + lpr.accuracy + " > " + lpr.weight);
		}
		*/
	}

	public static List<ObjMap> getPattern() {
		List<ObjMap> r = new ArrayList<ObjMap>();
		r.add(new ObjMap(2F, 2F, 2F));
		r.add(new ObjMap(0F, 0.5F, 0F));
		r.add(new ObjMap(0F, 2F, 0F));
		r.add(new ObjMap(2F, 1F, 0F));
		r.add(new ObjMap(0F, 0.5F, 0F));
		r.add(new ObjMap(0F, 2F, 2F));
		r.add(new ObjMap(2F, 1F, 0F));
		r.add(new ObjMap(0F, 0.5F, 0F));
		r.add(new ObjMap(0F, 2F, 0F));
		r.add(new ObjMap(2F, 1F, 2F));
		r.add(new ObjMap(0F, 0.5F, 0F));
		r.add(new ObjMap(1F, 2F, 0F));
		return r;
	}
	
	public static void feedPattern(ObjMapList history) {
		history.addAll(getPattern());
	}
}
