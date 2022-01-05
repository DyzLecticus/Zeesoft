package nl.zeesoft.zdk.test.dai;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Scanner;

import nl.zeesoft.zdk.Console;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.analyze.Analyzer;
import nl.zeesoft.zdk.dai.analyze.History;
import nl.zeesoft.zdk.dai.analyze.PredictionList;
import nl.zeesoft.zdk.dai.cache.Cache;
import nl.zeesoft.zdk.dai.cache.CacheBuilder;

public class TestCachePerformance {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		History history = new History(5000);
		
		ObjMapComparator comparator = new ObjMapComparator();
		
		Analyzer analyzer = new Analyzer();
		
		Console.log("Reading file ...");
		int num = readInputFile(history);
		Console.log("Records: " + num);

		Console.log("");
		PredictionList predictions = generatePredictions(analyzer, history, comparator, true);
		predictions = analyzer.getUntrainedPredictions(history, comparator);
		assert predictions.getPredictions().size() == num;
		
		Console.log("");
		predictions = generatePredictions(analyzer, history, comparator, false);
		
		Cache originalCache = history.cache;

		Console.log("");
		history.cache = buildSuperCache(originalCache, history, comparator, 0.9F); 
		generatePredictions(analyzer, history, comparator, false);
		
		Console.log("");
		history.cache = buildSuperCache(originalCache, history, comparator, 0.8F); 
		generatePredictions(analyzer, history, comparator, false);
	}
	
	public static PredictionList generatePredictions(Analyzer analyzer, History history, ObjMapComparator comparator, boolean untrained) {
		PredictionList r = null;
		long start = System.currentTimeMillis();
		if (untrained) {
			Console.log("Generating untrained predictions ...");
			r = analyzer.getUntrainedPredictions(history, comparator);
			Console.log("Generating untrained predictions took: " + (System.currentTimeMillis() - start) + " ms");
		} else {
			Console.log("Generating predictions ...");
			r = analyzer.getPredictions(history, comparator);
			Console.log("Generating predictions took: " + (System.currentTimeMillis() - start) + " ms");
		}
		Console.log("Accuracy: " + r.getKeyAccuracy("3", false) + ", standard deviation: " + r.getKeyAccuracyStdDev("3", false));
		Console.log("Weighted accuracy: " + r.getKeyAccuracy("3", true) + ", standard deviation: " + r.getKeyAccuracyStdDev("3", true));
		Console.log("Confidence: " + r.getKeyConfidence("3") + ", standard deviation: " + r.getKeyConfidenceStdDev("3"));
		return r;
	}
	
	public static Cache buildSuperCache(Cache originalCache, History history, ObjMapComparator comparator, float mergeSimilarity) {
		Cache r = null;
		long start = System.currentTimeMillis();
		Console.log("Building super cache ...");
		CacheBuilder builder = new CacheBuilder();
		r = builder.buildSuperCache(originalCache, comparator, mergeSimilarity);
		Console.log("Building super cache took: " + (System.currentTimeMillis() - start) + " ms");
		Console.log("Relative super cache size: " + ((float)r.elements.size() / (float)history.cache.elements.size()));
		return r;
	}
	
	public static int readInputFile(ObjMapList history) {
		int num = 0;
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
					num++;
				}
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return num;
	}
}
