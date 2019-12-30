package nl.zeesoft.zdk.htm.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.htm.proc.Anomaly;
import nl.zeesoft.zdk.htm.proc.Classification;
import nl.zeesoft.zdk.htm.proc.Classifier;
import nl.zeesoft.zdk.htm.proc.Detector;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

/**
 * A ZGridResult is sent to ZGridResultListener implementations to communicate ZGridRequest processing results.
 */
public class ZGridResult extends Locker implements JsAble {
	private ZGridRequest					request			= null;
	private SortedMap<String,List<SDR>>		columnOutputs	= new TreeMap<String,List<SDR>>();
	private List<Classification>			classifications	= new ArrayList<Classification>();
	private List<Anomaly>					anomalies		= new ArrayList<Anomaly>();
	
	public ZGridResult(Messenger msgr) {
		super(msgr);
	}
	
	public ZGridResult(Messenger msgr,ZGridRequest request) {
		super(msgr);
		this.request = request;
	}
	
	/**
	 * Returns the request that led to this result.
	 * 
	 * @return The request
	 */
	public ZGridRequest getRequest() {
		return request;
	}
	
	/**
	 * Returns a list of all the column ids in this result.
	 * 
	 * @return A list of columns ids
	 */
	public List<String> getColumnIds() {
		lockMe(this);
		List<String> r = new ArrayList<String>(columnOutputs.keySet());
		unlockMe(this);
		return r;
	}
	
	/**
	 * Returns the specified column output SDR for a certain column.
	 * 
	 * @param columnId The id of the column
	 * @param index The index of the column output SDR
	 * @return The column output SDR
	 */
	public SDR getColumnOutput(String columnId,int index) {
		lockMe(this);
		SDR r = null;
		List<SDR> outputs = columnOutputs.get(columnId);
		if (outputs!=null && outputs.size()>index) {
			r = outputs.get(index);
		}
		unlockMe(this);
		return r;
	}

	/**
	 * Returns the first column output SDR for a certain column.
	 * 
	 * @param columnId The id of the column
	 * @return The column output SDR
	 */
	public List<SDR> getColumnOutput(String columnId) {
		lockMe(this);
		List<SDR> r = new ArrayList<>(columnOutputs.get(columnId));
		unlockMe(this);
		return r;
	}
	
	/**
	 * Returns all Classification objects of all column output SDRs in this result.
	 *  
	 * @return All Classification objects in this result
	 */
	public List<Classification> getClassifications() {
		lockMe(this);
		List<Classification> r = new ArrayList<Classification>(classifications);
		unlockMe(this);
		return r;
	}
	
	/**
	 * Returns all Anomaly objects of all column output SDRs in this result.
	 *  
	 * @return All Anomaly objects in this result
	 */
	public List<Anomaly> getAnomalies() {
		lockMe(this);
		List<Anomaly> r = new ArrayList<Anomaly>(anomalies);
		unlockMe(this);
		return r;
	}
	
	protected void setColumnOutput(String columnId,List<SDR> outputs) {
		lockMe(this);
		if (outputs==null) {
			columnOutputs.remove(columnId);
		} else {
			columnOutputs.put(columnId,outputs);
			for (SDR outputSDR: outputs) {
				if (outputSDR instanceof DateTimeSDR) {
					DateTimeSDR dateTimeSDR = (DateTimeSDR) outputSDR;
					if (dateTimeSDR.keyValues.containsKey(Classifier.CLASSIFICATION_KEY)) {
						Object obj = dateTimeSDR.keyValues.get(Classifier.CLASSIFICATION_KEY);
						if (obj instanceof Classification) {
							classifications.add((Classification) obj);
						}
					} else if (dateTimeSDR.keyValues.containsKey(Detector.ANOMALY_KEY)) {
						Object obj = dateTimeSDR.keyValues.get(Detector.ANOMALY_KEY);
						if (obj instanceof Anomaly) {
							anomalies.add((Anomaly) obj);
						}
					}
				}
			}
		}
		unlockMe(this);
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		lockMe(this);
		json.rootElement = new JsElem();
		JsElem reqElem = new JsElem("request",true);
		json.rootElement.children.add(reqElem);
		reqElem.children.add(request.toJson().rootElement);
		JsElem colsElem = new JsElem("columnOutputs",true);
		json.rootElement.children.add(colsElem);
		for (Entry<String,List<SDR>> entry: columnOutputs.entrySet()) {
			JsElem colElem = new JsElem();
			colsElem.children.add(colElem);
			colElem.children.add(new JsElem("columnId",entry.getKey(),true));
			JsElem sdrsElem = new JsElem("sdrs",true);
			colElem.children.add(sdrsElem);
			for (SDR sdr: entry.getValue()) {
				sdrsElem.children.add(new JsElem(null,sdr.toStringBuilder(),true));
			}
		}
		if (classifications.size()>0) {
			JsElem clasElem = new JsElem("classifications",true);
			json.rootElement.children.add(clasElem);
			for (Classification cla: classifications) {
				clasElem.children.add(cla.toJson().rootElement);
			}
		}
		if (anomalies.size()>0) {
			JsElem anomElem = new JsElem("anomalies",true);
			json.rootElement.children.add(anomElem);
			for (Anomaly ano: anomalies) {
				anomElem.children.add(ano.toJson().rootElement);
			}
		}
		unlockMe(this);
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			lockMe(this);
			JsElem reqElem = json.rootElement.getChildByName("request");
			if (reqElem!=null && reqElem.children.size()>0) {
				request = new ZGridRequest(1);
				JsFile js = new JsFile();
				js.rootElement = reqElem.children.get(0);
				request.fromJson(js);
			}
			JsElem colsElem = json.rootElement.getChildByName("columnOutputs");
			if (colsElem!=null && colsElem.children.size()>0) {
				for (JsElem colElem: colsElem.children) {
					String columnId = colElem.getChildString("columnId");
					List<SDR> sdrs = new ArrayList<SDR>();
					JsElem sdrsElem = colElem.getChildByName("sdrs");
					if (sdrsElem!=null) {
						for (JsElem sdrElem: sdrsElem.children) {
							SDR sdr = new SDR(100);
							sdr.fromStringBuilder(sdrElem.value);
							sdrs.add(sdr);
						}
					}
					if (columnId.length()>0 && sdrs.size()>0) {
						columnOutputs.put(columnId,sdrs);
					}
				}
			}
			JsElem clasElem = json.rootElement.getChildByName("classifications");
			if (clasElem!=null && clasElem.children.size()>0) {
				for (JsElem claElem: clasElem.children) {
					JsFile js = new JsFile();
					js.rootElement = claElem;
					Classification cla = new Classification();
					cla.fromJson(js);
					classifications.add(cla);
				}
			}
			JsElem anomElem = json.rootElement.getChildByName("anomalies");
			if (anomElem!=null && anomElem.children.size()>0) {
				for (JsElem anoElem: anomElem.children) {
					JsFile js = new JsFile();
					js.rootElement = anoElem;
					Anomaly ano = new Anomaly();
					ano.fromJson(js);
					anomalies.add(ano);
				}
			}
			unlockMe(this);
		}
	}
}
