package nl.zeesoft.zsda.grid;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zodb.db.init.Persistable;

public class PersistableProcessorState extends Locker implements Persistable {
	private String			columnId	= "";
	private ZStringBuilder 	stateData	= new ZStringBuilder();

	public PersistableProcessorState() {
		super(null);
	}

	public PersistableProcessorState(Messenger msgr) {
		super(msgr);
	}

	@Override
	public ZStringBuilder getObjectName() {
		lockMe(this);
		ZStringBuilder r = new ZStringBuilder(columnId);
		unlockMe(this);
		return r;
	}

	public String getColumnId() {
		lockMe(this);
		String r = columnId;
		unlockMe(this);
		return r;
	}

	public void setColumnId(String columnId) {
		lockMe(this);
		this.columnId = columnId;
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
		json.rootElement.children.add(new JsElem("columnId",columnId,true));
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
			columnId = json.rootElement.getChildString("columnId",columnId);
			stateData = json.rootElement.getChildZStringBuilder("stateData",stateData);
			unlockMe(this);
		}
	}
}
