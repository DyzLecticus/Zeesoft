package nl.zeesoft.zodb.mod;

import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseRequestHandler;
import nl.zeesoft.zodb.db.DatabaseResponse;
import nl.zeesoft.zodb.db.StateListener;
import nl.zeesoft.zodb.db.WhiteList;
import nl.zeesoft.zodb.mod.handler.HtmlZODBDataManagerHandler;
import nl.zeesoft.zodb.mod.handler.HtmlZODBIndexHandler;
import nl.zeesoft.zodb.mod.handler.HtmlZODBIndexManagerHandler;
import nl.zeesoft.zodb.mod.handler.JavaScriptZODBDataManagerHandler;
import nl.zeesoft.zodb.mod.handler.JavaScriptZODBHandler;
import nl.zeesoft.zodb.mod.handler.JavaScriptZODBIndexManagerHandler;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;
import nl.zeesoft.zodb.mod.handler.JsonZODBIndexConfigHandler;
import nl.zeesoft.zodb.mod.handler.JsonZODBRequestHandler;

public class ModZODB extends ModObject implements StateListener {
	public static final String	NAME		= "ZODB";
	public static final String	DESC		= "The Zeesoft Object Database provides a simple JSON API to store JSON objects.";
	
	private StringBuilder		key			= null;
	private StringBuilder		newKey		= null;
	private WhiteList			whiteList	= new WhiteList();
	
	private Database			database	= null;
	
	public int					maxLenName	= 128;
	public int					maxLenObj	= 32768;
	
	public ModZODB(Config config) {
		super(config);
		name = NAME;
		ZStringEncoder encoder = new ZStringEncoder();
		key = encoder.generateNewKey(1024);
		whiteList.getList().add("127.0.0.1");
		whiteList.getList().add("0:0:0:0:0:0:0:1");
		desc.append(DESC);
		database = getNewDatabase();
		database.addListener(this);
	}
	
	@Override
	public JsFile toJson() {
		JsFile json = super.toJson();
		json.rootElement.children.add(new JsElem("maxLenName","" + maxLenName));
		json.rootElement.children.add(new JsElem("maxLenObj","" + maxLenObj));
		ZStringEncoder encoder = new ZStringEncoder(key);
		json.rootElement.children.add(new JsElem("key",encoder.compress().toString(),true));
		if (newKey!=null && newKey.length()>0) {
			encoder = new ZStringEncoder(newKey);
			if (newKey.length()>4 || !newKey.toString().equals("true")) {
				encoder.compress();
			}
			json.rootElement.children.add(new JsElem("newKey",encoder,true));
		}
		if (whiteList.getList().size()>0) {
			JsFile wl = whiteList.toJson();
			json.rootElement.children.add(wl.rootElement);
		}
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		super.fromJson(json);
		if (json.rootElement!=null) {
			maxLenName = json.rootElement.getChildInt("maxLenName",maxLenName);
			maxLenObj = json.rootElement.getChildInt("maxLenObj",maxLenObj);
			JsElem k = json.rootElement.getChildByName("key");
			if (k!=null && k.value!=null && k.value.length()>0) {
				ZStringEncoder encoder = new ZStringEncoder(k.value);
				key = encoder.decompress();
			}
			k = json.rootElement.getChildByName("newKey");
			if (k!=null && k.value!=null && k.value.length()>0) {
				ZStringEncoder encoder = new ZStringEncoder(k.value);
				if (k.value.toString().equals("true")) {
					newKey = encoder.generateNewKey(1024);
				} else {
					newKey = encoder.decompress();
				}
			}
			whiteList.fromJson(json);
		}
	}
	
	@Override
	public void install() {
		Database db = getNewDatabase();
		db.install();
	}
	
	@Override
	public void initialize() {
		handlers.add(new HtmlZODBIndexHandler(configuration,this));
		handlers.add(new JavaScriptZODBHandler(configuration,this));
		handlers.add(new JavaScriptZODBIndexManagerHandler(configuration,this));
		handlers.add(new JavaScriptZODBDataManagerHandler(configuration,this));
		handlers.add(new HtmlZODBIndexManagerHandler(configuration,this));
		handlers.add(new HtmlZODBDataManagerHandler(configuration,this));
		handlers.add(new JsonZODBIndexConfigHandler(configuration,this));
		handlers.add(new JsonZODBRequestHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		testers.add(getNewTester());
		database.start();
		super.initialize();
	}
	
	@Override
	public void destroy() {
		super.destroy();
		database.stop();
		database.destroy();
	}

	@Override
	public void stateChanged(Object source,boolean open) {
		if (source instanceof Database && open) {
			startTesters();
		}
	}
	
	public StringBuilder getKey() {
		return key;
	}

	public void setKey(StringBuilder key) {
		this.key = key;
		if (key.equals(newKey)) {
			newKey = null;
		}
	}
	
	public StringBuilder getNewKey() {
		return newKey;
	}

	public void setNewKey(StringBuilder newKey) {
		this.newKey = newKey;
	}

	public WhiteList getWhiteList() {
		return whiteList;
	}

	public Database getDatabase() {
		return database;
	}
	
	public DatabaseResponse handleRequest(DatabaseRequest request) {
		DatabaseRequestHandler handler = getNewDatabaseRequestHandler(database);
		return handler.handleDatabaseRequest(request);
	}
	
	protected Database getNewDatabase() {
		Database r = new Database(configuration);
		if (selfTest) {
			String pfx = NAME + "/Objects/";
			r.getIndexConfig().addIndex(pfx,"testData",false,true);
		}
		return r;
	}
	
	protected DatabaseRequestHandler getNewDatabaseRequestHandler(Database db) {
		return new DatabaseRequestHandler(db,maxLenName,maxLenObj);
	}
	
	protected ZODBTester getNewTester() {
		return new ZODBTester(configuration,configuration.getModuleUrl(NAME) + JsonZODBRequestHandler.PATH);
	}
}
