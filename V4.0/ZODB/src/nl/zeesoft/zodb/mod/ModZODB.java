package nl.zeesoft.zodb.mod;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.WhiteList;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseRequestHandler;
import nl.zeesoft.zodb.db.DatabaseResponse;
import nl.zeesoft.zodb.db.DatabaseStateListener;
import nl.zeesoft.zodb.mod.handler.HtmlZODBDataManagerHandler;
import nl.zeesoft.zodb.mod.handler.HtmlZODBIndexHandler;
import nl.zeesoft.zodb.mod.handler.HtmlZODBIndexManagerHandler;
import nl.zeesoft.zodb.mod.handler.HtmlZODBStateManagerHandler;
import nl.zeesoft.zodb.mod.handler.JavaScriptZODBDataManagerHandler;
import nl.zeesoft.zodb.mod.handler.JavaScriptZODBHandler;
import nl.zeesoft.zodb.mod.handler.JavaScriptZODBIndexManagerHandler;
import nl.zeesoft.zodb.mod.handler.JavaScriptZODBStateManagerHandler;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;
import nl.zeesoft.zodb.mod.handler.JsonZODBIndexConfigHandler;
import nl.zeesoft.zodb.mod.handler.JsonZODBRequestHandler;
import nl.zeesoft.zodb.mod.handler.JsonZODBStateHandler;

public class ModZODB extends ModObject implements DatabaseStateListener {
	public static final String	NAME				= "ZODB";
	public static final String	DESC				= "The Zeesoft Object Database provides a simple JSON API to store JSON objects.";
	
	private StringBuilder		key					= null;
	private StringBuilder		newKey				= null;
	private WhiteList			whiteList			= new WhiteList();
	
	private Database			database			= null;
	
	public int					maxLenName			= 128;
	public int					maxLenObj			= 32768;
	public int					indexBlockSize		= 1000;
	public int					dataBlockSize		= 10;
	
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
		json.rootElement.children.add(new JsElem("indexBlockSize","" + indexBlockSize));
		json.rootElement.children.add(new JsElem("dataBlockSize","" + dataBlockSize));
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
		json.rootElement.children.add(new JsElem("maxLenName","" + maxLenName));
		json.rootElement.children.add(new JsElem("maxLenObj","" + maxLenObj));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		super.fromJson(json);
		if (json.rootElement!=null) {
			indexBlockSize = json.rootElement.getChildInt("indexBlockSize",indexBlockSize);
			if (indexBlockSize<10) {
				indexBlockSize = 10;
			}
			dataBlockSize = json.rootElement.getChildInt("dataBlockSize",dataBlockSize);
			if (dataBlockSize<1) {
				dataBlockSize = 1;
			}
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
			if (key.length()<64 || !ZStringEncoder.isNumber(key,false)) {
				if (newKey.length()<64 || !ZStringEncoder.isNumber(newKey,false)) {
					ZStringEncoder encoder = new ZStringEncoder();
					newKey = encoder.generateNewKey(1024);
				}
			}
			whiteList.fromJson(json);
			maxLenName = json.rootElement.getChildInt("maxLenName",maxLenName);
			if (maxLenName<8) {
				maxLenName = 8;
			}
			maxLenObj = json.rootElement.getChildInt("maxLenObj",maxLenObj);
			if (maxLenObj<24) {
				maxLenObj = 24;
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
		handlers.add(new JavaScriptZODBIndexManagerHandler(configuration,this));
		handlers.add(new JavaScriptZODBDataManagerHandler(configuration,this));
		handlers.add(new JavaScriptZODBStateManagerHandler(configuration,this));
		handlers.add(new HtmlZODBIndexManagerHandler(configuration,this));
		handlers.add(new HtmlZODBDataManagerHandler(configuration,this));
		handlers.add(new HtmlZODBStateManagerHandler(configuration,this));
		handlers.add(new JsonZODBIndexConfigHandler(configuration,this));
		handlers.add(new JsonZODBRequestHandler(configuration,this));
		handlers.add(new JsonZODBStateHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		testers.add(getNewTester());
		database.getConfiguration().debug = configuration.isDebug();
		database.getConfiguration().dataDir = configuration.getFullDataDir();
		database.getConfiguration().key = key;
		database.getConfiguration().newKey = newKey;
		database.getConfiguration().indexBlockSize = dataBlockSize;
		database.getConfiguration().dataBlockSize = dataBlockSize;
		if (selfTest) {
			String pfx = NAME + "/Objects/";
			database.getConfiguration().indexConfig.addIndex(pfx,"testData",false,true);
		}
		database.initialize();
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

	@Override
	public void keyChanged(StringBuilder newKey) {
		setKey(newKey);
		ZStringBuilder err = configuration.rewriteConfig();
		if (err.length()==0) {
			configuration.debug(this,"Changed database key");
		} else {
			configuration.error(this,"An error occured while changing the database key");
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
		return getNewDatabaseRequestHandler(database).handleDatabaseRequest(request);
	}
	
	protected Database getNewDatabase() {
		return new Database(configuration.getMessenger(),configuration.getUnion());
	}
	
	protected DatabaseRequestHandler getNewDatabaseRequestHandler(Database db) {
		return new DatabaseRequestHandler(db,maxLenName,maxLenObj);
	}
	
	protected ZODBTester getNewTester() {
		return new ZODBTester(configuration,configuration.getModuleUrl(NAME) + JsonZODBRequestHandler.PATH);
	}
}
