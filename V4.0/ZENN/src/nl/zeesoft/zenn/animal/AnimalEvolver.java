package nl.zeesoft.zenn.animal;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zenn.network.Evolver;
import nl.zeesoft.zenn.network.NN;
import nl.zeesoft.zenn.network.TestCycleSet;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseResponse;
import nl.zeesoft.zodb.db.init.InitializerDatabaseObject;

public class AnimalEvolver extends Evolver implements InitializerDatabaseObject, JsClientListener {
	private Config		configuration	= null;
	private boolean		herbivore		= true;
	
	private	long		id				= 0;
	
	public AnimalEvolver(Config config,boolean herbivore) {
		super(config.getMessenger(),config.getUnion());
		this.configuration = config;
		this.herbivore = herbivore;
		initialize();
	}
	
	public AnimalEvolver(Messenger msgr,WorkerUnion uni,boolean herbivore) {
		super(msgr,uni);
		this.herbivore = herbivore;
		initialize();
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("herbivore","" + herbivore));
		NN bestSoFar = getBestSoFar();
		if (bestSoFar!=null) {
			JsFile nnJson = bestSoFar.toJson();
			for (JsElem elem: nnJson.rootElement.children) {
				json.rootElement.children.add(elem);
			}
			TestCycleSet bestResults = getBestResults();
			JsFile resJson = bestResults.toJson();
			for (JsElem elem: resJson.rootElement.children) {
				json.rootElement.children.add(elem);
			}
			JsElem code = json.rootElement.getChildByName("code");
			json.rootElement.children.remove(code);
			json.rootElement.children.add(code);
		}
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			herbivore = json.rootElement.getChildBoolean("hebrivore",herbivore);
			if (json.rootElement.children.size()>1) {
				NN bestSoFar = getNewNN();
				bestSoFar.fromJson(json);
				TestCycleSet bestResults = getNewTestCycleSet();
				bestResults.fromJson(json);
				setBest(bestSoFar, bestResults);
			}
		}
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
						id = res.results.get(0).id;
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
		DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_GET);
		request.name = new ZStringBuilder("ZENN/Evolvers/" + getObjectName());
		request.encoding = DatabaseRequest.ENC_KEY;
		configuration.handleDatabaseRequest(request,this,3);
	}
	
	protected void save() {
		DatabaseRequest request = null;
		if (id>0) {
			request = new DatabaseRequest(DatabaseRequest.TYPE_SET);
			request.id = id;
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
	
	@Override
	protected void selectedNN() {
		save();
	}
	
	@Override
	protected String getType() {
		String r = "herbivore";
		if (!herbivore) {
			r = "carnivore";
		}
		return r;
	}
	
	private void initialize() {
		AnimalNN nn = new AnimalNN();
		nn.initialize();
		AnimalTestCycleSet tcs = new AnimalTestCycleSet();
		tcs.initialize(nn, herbivore);
		initialize(nn,tcs,3);
	}
}
