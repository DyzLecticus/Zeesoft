package nl.zeesoft.zenn.simulator;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zenn.environment.EnvironmentConfig;
import nl.zeesoft.zenn.environment.EnvironmentState;
import nl.zeesoft.zenn.environment.History;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.idx.IndexConfig;

public class HistoryManager extends Locker implements JsClientListener {
	private Config					configuration		= null;
	private String					namePrefix			= "ZENN/History/";
	
	private EnvironmentConfig		environmentConfig	= null;
	private EnvironmentState		environmentState	= null;
	
	protected HistoryManager(Config config) {
		super(config.getMessenger());
		this.configuration = config;
	}
	
	protected void setEnvironment(EnvironmentConfig environmentConfig,EnvironmentState environmentState) {
		lockMe(this);
		this.environmentConfig = environmentConfig;
		this.environmentState = environmentState;
		unlockMe(this);
	}

	protected void updateHistory() {
		lockMe(this);
		if (environmentConfig!=null && environmentState!=null) {
			removeOldHistories();
			addNewHistory();
		}
		unlockMe(this);
	}

	@Override
	public void handledRequest(JsClientResponse response) {
		if (response.error.length()>0) {
			configuration.error(this,response.error.toString(),response.ex);
		}
	}

	private void removeOldHistories() {
		long timeStamp = System.currentTimeMillis() - environmentConfig.keepStateHistorySeconds * 1000;
		DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_REMOVE);
		request.index = IndexConfig.IDX_NAME;
		request.invert = false;
		request.operator = DatabaseRequest.OP_STARTS_WITH;
		request.value = new ZStringBuilder(namePrefix);
		request.modBefore = timeStamp;
		configuration.handleDatabaseRequest(request,this);
	}

	private void addNewHistory() {
		History hist = new History();
		hist.timeStamp = System.currentTimeMillis();
		hist.addOrganismData(environmentState.organisms);
		ZStringBuilder name = new ZStringBuilder(namePrefix);
		name.append(hist.getObjectName());
		DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_ADD);
		request.name = name;
		request.encoding = DatabaseRequest.ENC_KEY;
		ZStringEncoder encoder = new ZStringEncoder(hist.toJson().toStringBuilder());
		encoder.encodeKey(configuration.getZODBKey(),0);
		request.encoded = encoder;
		configuration.handleDatabaseRequest(request,this);
	}
}
