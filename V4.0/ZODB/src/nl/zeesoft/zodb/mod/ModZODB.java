package nl.zeesoft.zodb.mod;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseRequestHandler;
import nl.zeesoft.zodb.db.DatabaseResponse;
import nl.zeesoft.zodb.db.StateListener;
import nl.zeesoft.zodb.mod.handler.HtmlZODBDataManagerHandler;
import nl.zeesoft.zodb.mod.handler.HtmlZODBIndexHandler;
import nl.zeesoft.zodb.mod.handler.JavaScriptZODBDataManagerHandler;
import nl.zeesoft.zodb.mod.handler.JavaScriptZODBHandler;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;
import nl.zeesoft.zodb.mod.handler.JsonZODBRequestHandler;

public class ModZODB extends ModObject implements StateListener {
	public static final String	NAME		= "ZODB";
	public static final String	DESC		= "The Zeesoft Object Database provides a simple JSON API to store JSON objects.";
	
	private StringBuilder		key			= null;
	private StringBuilder		newKey		= null;
	private List<String>		whiteList	= new ArrayList<String>();
	
	private Database			database	= null;
	
	public ModZODB(Config config) {
		super(config);
		name = NAME;
		ZStringEncoder encoder = new ZStringEncoder();
		key = encoder.generateNewKey(1024);
		whiteList.add("127.0.0.1");
		whiteList.add("0:0:0:0:0:0:0:1");
		desc.append(DESC);
		database = getNewDatabase();
		database.addListener(this);
	}
	
	@Override
	public JsFile toJson() {
		JsFile json = super.toJson();
		ZStringEncoder encoder = new ZStringEncoder(key);
		json.rootElement.children.add(new JsElem("key",encoder.compress().toString(),true));
		if (newKey!=null && newKey.length()>0) {
			encoder = new ZStringEncoder(newKey);
			json.rootElement.children.add(new JsElem("newKey",encoder.compress().toString(),true));
		}
		JsElem wElem = new JsElem("whiteList",true);
		json.rootElement.children.add(wElem);
		for (String w: whiteList) {
			wElem.children.add(new JsElem(null,w,true));
		}
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		super.fromJson(json);
		if (json.rootElement!=null) {
			JsElem k = json.rootElement.getChildByName("key");
			if (k!=null && k.value!=null && k.value.length()>0) {
				ZStringEncoder encoder = new ZStringEncoder(k.value);
				key = encoder.decompress();
			}
			k = json.rootElement.getChildByName("newKey");
			if (k!=null && k.value!=null && k.value.length()>0) {
				ZStringEncoder encoder = new ZStringEncoder(k.value);
				newKey = encoder.decompress();
			}
			JsElem wElem = json.rootElement.getChildByName("whiteList");
			if (wElem!=null && wElem.array) {
				whiteList.clear();
				for (JsElem w: wElem.children) {
					if (w.value!=null && w.value.length()>0) {
						whiteList.add(w.value.toString());
					}
				}
			}
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
		handlers.add(new JavaScriptZODBDataManagerHandler(configuration,this));
		handlers.add(new HtmlZODBDataManagerHandler(configuration,this));
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

	public List<String> getWhiteList() {
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
		return new Database(configuration);
	}
	
	protected DatabaseRequestHandler getNewDatabaseRequestHandler(Database db) {
		return new DatabaseRequestHandler(db);
	}
	
	protected ZODBTester getNewTester() {
		return new ZODBTester(configuration,configuration.getModuleUrl(NAME) + JsonZODBRequestHandler.PATH);
	}
}
