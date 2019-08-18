package nl.zeesoft.zenn.animal;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.genetic.Evolver;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zdk.json.JsFile;
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
		setEvolverSleepMs(20);
	}
	
	public AnimalEvolver(Messenger msgr, WorkerUnion uni,boolean herbivore) {
		super(
			msgr,uni,
			AnimalConstants.MAX_LAYERS,AnimalConstants.MAX_NEURONS,
			100,AnimalTestSet.getNewAnimalTestSet(herbivore),5);
		this.herbivore = herbivore;
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
				if (res.results.size()>0) {
					ZStringEncoder encoder = new ZStringEncoder(res.results.get(0).encoded);
					encoder.decodeKey(configuration.getZODBKey(),0);
					JsFile obj = new JsFile();
					obj.fromStringBuilder(encoder);
					if (obj.rootElement!=null && obj.rootElement.children.size()>0) {
						lockMe(this);
						id = res.results.get(0).id;
						unlockMe(this);
						fromJson(obj);
					}
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
			DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_GET);
			request.name = new ZStringBuilder("ZENN/Evolvers/" + getObjectName());
			request.encoding = DatabaseRequest.ENC_KEY;
			configuration.handleDatabaseRequest(request,this,10);
		}
	}
	
	protected void save() {
		if (configuration!=null) {
			DatabaseRequest request = null;
			if (id>0) {
				request = new DatabaseRequest(DatabaseRequest.TYPE_SET);
				lockMe(this);
				request.id = id;
				unlockMe(this);
			} else {
				request = new DatabaseRequest(DatabaseRequest.TYPE_ADD);
				request.name = new ZStringBuilder("ZENN/Evolvers/" + getObjectName());
			}
			request.encoding = DatabaseRequest.ENC_KEY;
			ZStringEncoder encoder = new ZStringEncoder(toJson().toStringBuilder());
			encoder.encodeKey(configuration.getZODBKey(),0);
			request.encoded = encoder;
			configuration.handleDatabaseRequest(request,this,3);
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
