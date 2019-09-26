package nl.zeesoft.zdk.htm.stream;

import java.util.HashMap;

public interface ValuePredictorListener {
	public void predictedValues(HashMap<String,Object> keyValues,Object currentValue);
}
