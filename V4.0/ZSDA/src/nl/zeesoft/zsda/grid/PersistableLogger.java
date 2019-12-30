package nl.zeesoft.zsda.grid;

import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.grid.ZGrid;
import nl.zeesoft.zdk.htm.grid.ZGridResult;
import nl.zeesoft.zdk.htm.grid.ZGridResultsListener;
import nl.zeesoft.zdk.htm.proc.Classification;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.HistoricalFloats;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.init.Persistable;

public class PersistableLogger extends Locker implements Persistable, ZGridResultsListener, JsClientListener {
	private Config							configuration		= null;
	private HistoricalFloats				history				= new HistoricalFloats();
	private Classification					prediction			= null;
	private SortedMap<Long,Long>			dateTimeLogIdMap	= new TreeMap<Long,Long>();
	
	public PersistableLogger(Messenger msgr,ZGrid grid,Config config) {
		super(msgr);
		grid.addListener(this);
		this.configuration = config;
	}

	public void destroy() {
		configuration = null;
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
		
		// TODO: remove
		if (getMessenger()!=null) {
			ZDate date = new ZDate();
			date.setTime(result.getRequest().dateTime);
			getMessenger().debug(this,"ID: " + result.getRequest().id + " > " + date.getDateTimeString() + ", predicted: " + pred + ", actual: " + val + ", average accuracy: " + history.average);
		}

		prediction = null;
		for (Classification c: result.getClassifications()) {
			if (c.valueKey.equals(DateTimeSDR.VALUE_KEY) && c.steps==1) {
				prediction = c;
				break;
			}
		}
		
		if (configuration!=null) {
			Persistable obj = new PersistableLog(getMessenger(),result);
			configuration.addObject(obj,new ZStringBuilder("ZSDA/Logs/" + obj.getObjectName()),this);
		}
		unlockMe(this);
	}

	@Override
	public void handledRequest(JsClientResponse response) {
		configuration.handledDatabaseRequest(response);
	}
}
