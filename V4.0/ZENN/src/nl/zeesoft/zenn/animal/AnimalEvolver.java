package nl.zeesoft.zenn.animal;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.genetic.Evolver;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseResponse;
import nl.zeesoft.zodb.db.init.Persistable;

public class AnimalEvolver extends Evolver implements Persistable, JsClientListener {
	private Config		configuration	= null;
	private boolean		herbivore		= true;
	
	private	long		id				= 0;
	
	public AnimalEvolver(Config config,boolean herbivore) {
		super(
			config.getMessenger(),config.getUnion(),
			AnimalConstants.MAX_LAYERS,AnimalConstants.MAX_NEURONS,
			100,AnimalTestSet.getNewAnimalTestSet(herbivore),5);
		this.configuration = config;
		this.herbivore = herbivore;
		setSleepMs(20);
		setSleepMsFoundBest(200);
	}
	
	public AnimalEvolver(Messenger msgr, WorkerUnion uni,boolean herbivore) {
		super(
			msgr,uni,
			AnimalConstants.MAX_LAYERS,AnimalConstants.MAX_NEURONS,
			100,AnimalTestSet.getNewAnimalTestSet(herbivore),5);
		this.herbivore = herbivore;
		setSleepMs(20);
		setSleepMsFoundBest(200);
	}

	@Override
	public ZStringBuilder getObjectName() {
		return new ZStringBuilder(ZStringBuilder.upperCaseFirst(getType()));
	}
	
	@Override
	public void handledRequest(JsClientResponse response) {
		DatabaseResponse res = configuration.handledDatabaseRequest(response);
		if (response.error.length()>0) {
			configuration.error(this,response.error.toString(),response.ex);
		}
		if (res!=null) {
			if (res.request.type.equals(DatabaseRequest.TYPE_GET)) {
				if (res.results.size()>0 && res.results.get(0).object!=null) {
					lockMe(this);
					id = res.results.get(0).id;
					unlockMe(this);
					fromJson(res.results.get(0).object);
				}
				if (!isWorking()) {
					start();
				}
			} else if (res.request.type.equals(DatabaseRequest.TYPE_ADD)) {
				load();
			}
		}
	}
	
	public void load() {
		if (configuration!=null) {
			configuration.getObject(new ZStringBuilder("ZENN/Evolvers/" + getObjectName()),this,10);
		}
	}
	
	protected void save() {
		if (configuration!=null) {
			long objId = 0;
			lockMe(this);
			objId = id;
			unlockMe(this);
			if (objId>0) {
				configuration.setObject(objId,this,this);
			} else {
				configuration.addObject(this,new ZStringBuilder("ZENN/Evolvers/" + getObjectName()),this);
			}
		}
	}
	
	@Override
	protected void selectedBest() {
		save();
	}
	
	protected String getType() {
		String r = "herbivore";
		if (!herbivore) {
			r = "carnivore";
		}
		return r;
	}
}
