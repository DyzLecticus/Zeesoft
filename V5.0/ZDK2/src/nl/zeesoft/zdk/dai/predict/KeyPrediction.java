package nl.zeesoft.zdk.dai.predict;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.dai.recognize.ListPatternRecognizer;

public class KeyPrediction {
	public String						key					= "";
	public Object						predictedValue		= null;
	public List<ListPatternRecognizer>	patternRecognizers	= new ArrayList<ListPatternRecognizer>();
	public float						support				= 0F;
	public float						confidence			= 0F;
	
	public KeyPrediction() {
		
	}
	
	public KeyPrediction(String key, Object predictedValue) {
		this.key = key;
		this.predictedValue = predictedValue;
	}
}
