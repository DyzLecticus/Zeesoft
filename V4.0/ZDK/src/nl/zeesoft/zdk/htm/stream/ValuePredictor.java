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
	
	public void addListener(ValuePredictorListener listener) {
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
		SDR predictedSDR = result.outputSDRs.get(3);
		
		HashMap<String,Object> keyValues = new HashMap<String,Object>();
		Object currentValue = null;
		List<ValuePredictorListener> list = null;
		
		if (predictedSDR instanceof DateTimeSDR) {
			lockMe(this);
			list = new ArrayList<ValuePredictorListener>(listeners);
			if (valueKeys.size()==1 && result.outputSDRs.size()>=6) {
				String key = valueKeys.get(0);
				DateTimeSDR pred = (DateTimeSDR) predictedSDR;
				DateTimeSDR lower = (DateTimeSDR) result.outputSDRs.get(4);
				DateTimeSDR upper = (DateTimeSDR) result.outputSDRs.get(5);
				DateTimeSDR curr = (DateTimeSDR) result.inputSDR;
				Object pVal = pred.keyValues.get(key);
				Object lVal = lower.keyValues.get(key);
				Object uVal = upper.keyValues.get(key);
				currentValue = curr.keyValues.get(key);
				keyValues.put(key,pVal);
				keyValues.put(key + "Min",lVal);
				keyValues.put(key + "Max",uVal);
			} else if (valueKeys.size()>0) {
				for (String valueKey: valueKeys) {
					DateTimeSDR pred = (DateTimeSDR) predictedSDR;
					Object pVal = pred.keyValues.get(valueKey);
					if (currentValue==null) {
						DateTimeSDR curr = (DateTimeSDR) result.inputSDR;
						currentValue = curr.keyValues.get(valueKey);
					}
					if (pVal!=null) {
						keyValues.put(valueKey,pVal);
					}
				}
			}
			unlockMe(this);
		}
		
		if (keyValues.size()>0) {
			setPredictedValues(keyValues);
			for (ValuePredictorListener listener: list) {
				listener.predictedValues(keyValues,currentValue);
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
		r = predictedValues;
		unlockMe(this);
		return r;
	}
}
