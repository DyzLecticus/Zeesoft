package nl.zeesoft.zdk.dai.predict;

import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.recognize.ListPatternRecognizer;

public class ObjMapPrediction {
	public ListPatternRecognizer	patternRecognizer	= null;
	public ObjMap					predictedMap		= null;
	
	public int						votes				= 0;
	public float					confidence			= 0F;
	
	public ObjMapPrediction() {
		
	}
	
	public ObjMapPrediction(ListPatternRecognizer lpr, ObjMap pm) {
		this.patternRecognizer = lpr;
		this.predictedMap = pm;
	}
	
	@Override
	public String toString() {
		return predictedMap + ", votes: " + votes + ", confidence: " + confidence;
	}
}
