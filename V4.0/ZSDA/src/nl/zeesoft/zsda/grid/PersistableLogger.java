package nl.zeesoft.zsda.grid;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.grid.ZGrid;
import nl.zeesoft.zdk.htm.grid.ZGridResult;
import nl.zeesoft.zdk.htm.grid.ZGridResultsListener;
import nl.zeesoft.zdk.htm.proc.Anomaly;
import nl.zeesoft.zdk.htm.proc.Classification;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.HistoricalFloats;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zodb.db.init.Persistable;

public class PersistableLogger extends Locker implements Persistable, ZGridResultsListener {
	private HistoricalFloats				history			= new HistoricalFloats();
	private Classification					prediction		= null;
	private SortedMap<Long,ResultLog>		logs			= new TreeMap<Long,ResultLog>();
	
	public PersistableLogger(Messenger msgr,ZGrid grid) {
		super(msgr);
		grid.addListener(this);
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		lockMe(this);
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("history",history.toStringBuilder(),true));
		if (prediction!=null) {
			JsElem predElem = new JsElem("prediction",true);
			json.rootElement.children.add(predElem);
			predElem.children.add(prediction.toJson().rootElement);
		}
		unlockMe(this);
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			lockMe(this);
			ZStringBuilder str = json.rootElement.getChildZStringBuilder("history");
			if (str!=null && str.length()>0) {
				history.fromStringBuilder(str);
			}
			JsElem predElem = json.rootElement.getChildByName("prediction");
			if (predElem!=null && predElem.children.size()>0) {
				JsFile js = new JsFile();
				js.rootElement = predElem.children.get(0);
				prediction = new Classification();
				prediction.fromJson(js);
			}
			unlockMe(this);
		}
	}

	@Override
	public ZStringBuilder getObjectName() {
		return new ZStringBuilder("Logger");
	}

	@Override
	public void processedRequest(ZGrid grid, ZGridResult result) {
		int val = (int) result.getRequest().inputValues[1];
		int pred = Integer.MIN_VALUE;
		
		lockMe(this);
		
		float accuracy = 0;
		if (prediction!=null) {
			for (Object pVal: prediction.mostCountedValues) {
				if (pVal instanceof Integer) {
					pred = (Integer)pVal;
					accuracy = 1;
					if (pred==val) {
						break;
					}
				}
			}
			if (accuracy > 0) {
				accuracy = accuracy / (float) prediction.mostCountedValues.size();
			}
			history.addFloat(accuracy);
		}
		
		if (getMessenger()!=null) {
			ZDate date = new ZDate();
			date.setTime(result.getRequest().dateTime);
			getMessenger().debug(this,"ID: " + result.getRequest().id + " > " + date.getDateTimeString() + ", predicted: " + pred + ", actual: " + val + ", average accuracy: " + history.average);
		}

		List<Anomaly> anomalies = result.getAnomalies();

		ResultLog log = new ResultLog();
		log.dateTime = result.getRequest().dateTime;
		log.predictedValue = pred;
		log.actualValue = val;
		log.accuracy = accuracy;
		log.averageAccuracy = history.average;
		if (anomalies.size()>0) {
			log.detectedAnomaly = anomalies.get(0);
		}
		logs.put(log.dateTime,log);

		prediction = null;
		for (Classification c: result.getClassifications()) {
			if (c.valueKey.equals(DateTimeSDR.VALUE_KEY) && c.steps==1) {
				prediction = c;
				break;
			}
		}
		
		unlockMe(this);
	}
}
