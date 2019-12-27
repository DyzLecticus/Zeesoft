package nl.zeesoft.zsda.grid;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zodb.db.init.Persistable;

public class PersistableGridState extends Locker implements Persistable {
	private String			key			= "";
	private ZStringBuilder 	stateData	= new ZStringBuilder();

	public PersistableGridState() {
		super(null);
	}

	public PersistableGridState(Messenger msgr) {
		super(msgr);
	}

	@Override
	public ZStringBuilder getObjectName() {
		lockMe(this);
		ZStringBuilder r = new ZStringBuilder(key);
		unlockMe(this);
		return r;
	}

	public String getKey() {
		lockMe(this);
		String r = key;
		unlockMe(this);
		return r;
	}

	public void setKey(String key) {
		lockMe(this);
		this.key = key;
		unlockMe(this);
	}

	public ZStringBuilder getStateData() {
		ZStringBuilder r = null;
		lockMe(this);
		r = new ZStringBuilder(stateData);
		unlockMe(this);
		return r;
	}

	public void setStateData(ZStringBuilder stateData) {
		lockMe(this);
		this.stateData = stateData;
		unlockMe(this);
	}
	
	@Override
	public JsFile toJson() {
		lockMe(this);
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("key",key,true));
		if (stateData.length()>0) {
			json.rootElement.children.add(new JsElem("stateData",stateData,true));
		} else {
			json.rootElement.children.add(new JsElem("stateData","",true));
		}
		unlockMe(this);
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			lockMe(this);
			key = json.rootElement.getChildString("key",key);
			stateData = json.rootElement.getChildZStringBuilder("stateData",stateData);
			unlockMe(this);
		}
	}
}
