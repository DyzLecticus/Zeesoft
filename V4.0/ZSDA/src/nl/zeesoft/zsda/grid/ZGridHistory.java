package nl.zeesoft.zsda.grid;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.grid.ZGrid;
import nl.zeesoft.zdk.htm.grid.ZGridResult;
import nl.zeesoft.zdk.htm.grid.ZGridResultsListener;
import nl.zeesoft.zdk.htm.proc.Anomaly;
import nl.zeesoft.zdk.htm.proc.Classification;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class ZGridHistory extends Locker implements ZGridResultsListener {
	private static final int				KEEP_RESULTS			= 72;
	
	private String							valueKey				= DateTimeSDR.VALUE_KEY;
	private int								steps					= 1;
	
	private int								columnIndex				= -1;
	
	private List<ZGridResult>				results					= new ArrayList<ZGridResult>();
	
	private long							lastUpdate				= 0;
	private JsFile							json					= null;
	
	public ZGridHistory(Messenger msgr,ZGrid grid) {
		super(msgr);
		initialize(grid,valueKey,steps);
	}
	
	public ZGridHistory(Messenger msgr,ZGrid grid,String valueKey) {
		super(msgr);
		initialize(grid,valueKey,steps);
	}
	
	public ZGridHistory(Messenger msgr,ZGrid grid,String valueKey,int steps) {
		super(msgr);
		initialize(grid,valueKey,steps);
	}

	@Override
	public void processedRequest(ZGrid grid, ZGridResult result) {
		lockMe(this);
		if (steps>=1 && columnIndex>=0) {
			results.add(0,result);
			while(results.size()>(KEEP_RESULTS + steps)) {
				results.remove(results.size() - 1);
			}
			long now = System.currentTimeMillis();
			if (now - lastUpdate > 4000) {
				analyzeNoLock();
				lastUpdate = now;
			}
		}
		unlockMe(this);
	}
	
	public JsFile getJson() {
		JsFile r = null;
		lockMe(this);
		r = json;
		unlockMe(this);
		return r;
	}
	
	protected void analyzeNoLock() {
		json = new JsFile();
		json.rootElement = new JsElem();
		if (steps>=1 && columnIndex>=0) {
			List<Object> values = new ArrayList<Object>();
			List<Classification> predictions = new ArrayList<Classification>();
			
			List<Float> relativeActualValues = new ArrayList<Float>();
			List<Float> relativePredictedValues = new ArrayList<Float>();
			List<Float> anomalies = new ArrayList<Float>();
			
			float minVal = Long.MAX_VALUE;
			float maxVal = Long.MIN_VALUE;
			int i = 0;
			int pi = steps;
			for (ZGridResult res: results) {
				if (i==KEEP_RESULTS) {
					break;
				}
				Object val = res.getRequest().inputValues[columnIndex];
				values.add(val);
				if (val!=null) {
					float value = getValueAsFloat(val);
					if (value < minVal) {
						minVal = value;
					}
					if (value > maxVal) {
						maxVal = value;
					}
				}
				
				Anomaly detected = null;
				for (Anomaly anomaly: res.getAnomalies()) {
					if (anomaly.valueKey.equals(valueKey)) {
						detected = anomaly;
						break;
					}
				}
				if (detected!=null) {
					anomalies.add(detected.difference);
				} else {
					anomalies.add(null);
				}
				
				Classification prediction = null;
				if (pi<results.size()) {
					ZGridResult pRes = results.get(pi);
					for (Classification classification: pRes.getClassifications()) {
						if (classification.valueKey.equals(valueKey) && classification.steps==steps) {
							prediction = classification;
							break;
						}
					}
				}
				predictions.add(prediction);
				if (prediction!=null) {
					float pred = Float.MIN_VALUE;
					for (Object pVal: prediction.mostCountedValues) {
						pred = getValueAsFloat(pVal);
						if (pred < minVal) {
							minVal = pred;
						}
						if (pred > maxVal) {
							maxVal = pred;
						}
					}
				}
				
				i++;
				pi++;
			}

			for (i = 0; i < values.size(); i++) {
				float accuracy = 0;
				
				Object val = values.get(i);
				
				if (val!=null) {
					float value = getValueAsFloat(val);
					relativeActualValues.add(getRelativeValue(value,minVal,maxVal));
					
					float pred = Float.MIN_VALUE;
					Classification prediction = predictions.get(i);
					if (prediction!=null) {
						for (Object pVal: prediction.mostCountedValues) {
							pred = getValueAsFloat(pVal);
							if (pVal.equals(val)) {
								accuracy = 1;
								break;
							}
						}
						if (accuracy>0) {
							accuracy = accuracy / (float) prediction.mostCountedValues.size();
						}
					}
					if (pred>Float.MIN_VALUE) {
						relativePredictedValues.add(getRelativeValue(pred,minVal,maxVal));
					} else {
						relativePredictedValues.add(null);
					}
				} else {
					relativeActualValues.add(null);
					relativePredictedValues.add(null);
				}
				
				pi++;
			}
			
			json.rootElement.children.add(new JsElem("valueKey",valueKey,true));
			json.rootElement.children.add(new JsElem("steps","" + steps));
			json.rootElement.children.add(new JsElem("minVal","" + minVal));
			json.rootElement.children.add(new JsElem("maxVal","" + maxVal));
			JsElem resultsElem = new JsElem("results",true);
			json.rootElement.children.add(resultsElem);
			i = 0;
			for (Float relVal: relativeActualValues) {
				JsElem resultElem = new JsElem();
				resultsElem.children.add(resultElem);
				if (relVal!=null) {
					resultElem.children.add(new JsElem("relVal","" + relVal));
				}
				Float relPred = relativePredictedValues.get(i);
				if (relPred!=null) {
					resultElem.children.add(new JsElem("relPred","" + relPred));
				}
				Float anom = anomalies.get(i);
				if (anom!=null) {
					resultElem.children.add(new JsElem("anom","" + anom));
				}
				i++;
			}
		}
	}
	
	protected void initialize(ZGrid grid,String valueKey,int steps) {
		grid.addListener(this);
		this.valueKey = valueKey;
		this.steps = steps;
		columnIndex = grid.getColumnIndex(valueKey);
	}
	
	protected float getValueAsFloat(Object value) {
		float r = 0;
		if (value!=null) {
			if (value instanceof Float) {
				r = (Float) value;
			} else if (value instanceof Integer) {
				r = new Float((Integer) value);
			} else if (value instanceof Long) {
				r = new Float((Long) value);
			}
		}
		return r;
	}
	
	protected float getRelativeValue(float value, float minVal, float maxVal) {
		float r = 0;
		if (value<minVal) {
			r = 0;
		} else if (value>maxVal) {
			r = 1;
		} else {
			if (minVal<0) {
				float diff = minVal * -1;
				minVal += diff;
				maxVal += diff;
				value += diff;
			} else if (minVal>0) {
				float diff = minVal;
				minVal -= diff;
				maxVal -= diff;
				value -= diff;
			}
			if (maxVal>0) {
				r = value / maxVal;
			}
		}
		return r;
	}
}
