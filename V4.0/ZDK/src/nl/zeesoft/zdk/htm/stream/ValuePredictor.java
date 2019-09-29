package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.thread.Locker;

public class ValuePredictor extends Locker implements StreamListener {
	protected PredictionStream				stream			= null;
	
	private	List<ValuePredictorListener>	listeners		= new ArrayList<ValuePredictorListener>();
	
	private List<String>					valueKeys		= new ArrayList<String>();
	
	private HashMap<String,Object>			predictedValues	= new HashMap<String,Object>();
	
	public ValuePredictor(BufferedPredictionStream stream,String valueKey) {
		super(stream.getMessenger());
		valueKeys.add(valueKey);
	}
	
	public void addPredictorListener(ValuePredictorListener listener) {
		lockMe(this);
		listeners.add(listener);
		unlockMe(this);
	}

	public void addValueKey(String valueKey) {
		lockMe(this);
		valueKeys.add(valueKey);
		unlockMe(this);
	}

	@Override
	public void processedResult(Stream stream, StreamResult result) {
		List<ValuePredictorListener> list = null;
		HashMap<String,Object> currentValues = new HashMap<String,Object>();
		HashMap<String,Object> predictedValues = new HashMap<String,Object>();

		SDR predictedSDR = result.outputSDRs.get(3);
		if (predictedSDR instanceof DateTimeSDR) {
			List<String> valKeys = getValueKeys();
			
			currentValues = getCurrentValues(result,valKeys);
			predictedValues = getPredictedValues(result,valKeys);
		
			lockMe(this);
			list = new ArrayList<ValuePredictorListener>(listeners);
			unlockMe(this);
		}
		
		if (predictedValues.size()>0) {
			setPredictedValues(predictedValues);
			for (ValuePredictorListener listener: list) {
				listener.predictedValues(currentValues,predictedValues,result);
			}
		}
	}
	
	protected List<String> getValueKeys() {
		List<String> r = null;
		lockMe(this);
		r = new ArrayList<String>(valueKeys);
		unlockMe(this);
		return r;
	}
	
	protected void setPredictedValues(HashMap<String,Object> predictedValues) {
		lockMe(this);
		this.predictedValues = new HashMap<String,Object>(predictedValues);
		unlockMe(this);
	}
	
	protected HashMap<String,Object> getPredictedValues() {
		HashMap<String,Object> r = null;
		lockMe(this);
		r = new HashMap<String,Object>(predictedValues);
		unlockMe(this);
		return r;
	}

	protected HashMap<String,Object> getCurrentValues(StreamResult result,List<String> valKeys) {
		HashMap<String,Object> r = new HashMap<String,Object>();
		DateTimeSDR curr = (DateTimeSDR) result.inputSDR;
		for (String valueKey: valKeys) {
			r.put(valueKey,curr.keyValues.get(valueKey));
		}
		return r;
	}
	
	protected HashMap<String,Object> getPredictedValues(StreamResult result,List<String> valKeys) {
		HashMap<String,Object> r = new HashMap<String,Object>();
		SDR predictedSDR = result.outputSDRs.get(3);
		if (predictedSDR instanceof DateTimeSDR) {
			DateTimeSDR pred = (DateTimeSDR) predictedSDR;
			for (String valueKey: valKeys) {
				if (pred.keyValues.containsKey(valueKey)) {
					r.put(valueKey,pred.keyValues.get(valueKey));
				}
			}
			if (valKeys.size()==1 && result.outputSDRs.size()>=6) {
				DateTimeSDR lower = (DateTimeSDR) result.outputSDRs.get(4);
				DateTimeSDR upper = (DateTimeSDR) result.outputSDRs.get(5);
				String key = valKeys.get(0);
				r.put(key + "Min",lower.keyValues.get(key));
				r.put(key + "Max",upper.keyValues.get(key));
			}
		}
		return r;
	}
}
