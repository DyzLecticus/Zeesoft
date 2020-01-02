package nl.zeesoft.zsda.grid;

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
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.init.Persistable;
import nl.zeesoft.zsda.mod.ModZSDA;

public class PersistableLogger extends Locker implements Persistable, ZGridResultsListener, JsClientListener {
	private Config							configuration		= null;
	
	private int								keepLogsSeconds		= 86400;
	
	private static final int				WINDOW				= 100;
	
	private HistoricalFloats				history				= new HistoricalFloats();
	private Classification					prediction			= null;
	
	public PersistableLogger(Messenger msgr,ZGrid grid,Config config) {
		super(msgr);
		grid.addListener(this);
		this.configuration = config;
		history.window = WINDOW;
	}

	public void destroy() {
		configuration = null;
	}
	
	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		lockMe(this);
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("keepLogsSeconds","" + keepLogsSeconds));
		unlockMe(this);
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			lockMe(this);
			keepLogsSeconds = json.rootElement.getChildInt("keepLogsSeconds",keepLogsSeconds);
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
			
			if (configuration!=null) {
				ZDate date = new ZDate();
				date.setTime(result.getRequest().dateTime);
				configuration.debug(this,"ID: " + result.getRequest().id + " > " + date.getDateTimeString() + ", predicted: " + pred + ", actual: " + val + ", average accuracy: " + history.average);
			}
		}

		prediction = null;
		for (Classification c: result.getClassifications()) {
			if (c.valueKey.equals(DateTimeSDR.VALUE_KEY) && c.steps==1) {
				prediction = c;
				break;
			}
		}
		
		removeOldLogsNoLock();
		addLogNoLock(result);
		unlockMe(this);
	}

	@Override
	public void handledRequest(JsClientResponse response) {
		configuration.handledDatabaseRequest(response);
	}
	
	protected void removeOldLogsNoLock() {
		if (configuration!=null) {
			long dateTime = System.currentTimeMillis() - (keepLogsSeconds * 1000);
			
			DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_REMOVE);
			request.index = ModZSDA.NAME + "/Logs/:dateTime";
			request.operator = DatabaseRequest.OP_GREATER;
			request.invert = true;
			request.value = new ZStringBuilder("" + dateTime); 
			
			configuration.handleDatabaseRequest(request, this);
		}
	}
	
	protected void addLogNoLock(ZGridResult result) {
		if (configuration!=null) {
			Persistable obj = new PersistableLog(getMessenger(),result);
			configuration.addObject(obj,new ZStringBuilder(ModZSDA.NAME + "/Logs/" + obj.getObjectName()),this);
		}
	}
}
