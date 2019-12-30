package nl.zeesoft.zsda.grid;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.grid.ZGridRequest;
import nl.zeesoft.zdk.htm.grid.ZGridResult;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zodb.db.init.Persistable;

public class PersistableLog extends Locker implements Persistable {
	private ZGridResult	result = null;

	public PersistableLog(Messenger msgr) {
		super(msgr);
		result = new ZGridResult(msgr,new ZGridRequest(1));
	}
	
	public PersistableLog(Messenger msgr,ZGridResult result) {
		super(msgr);
		this.result = result;
	}

	public void setObjectName(long requestId) {
		result.getRequest().id = requestId;
	}
	
	@Override
	public ZStringBuilder getObjectName() {
		return new ZStringBuilder("" + result.getRequest().id);
	}

	@Override
	public JsFile toJson() {
		return result.toJson();
	}

	@Override
	public void fromJson(JsFile json) {
		result.fromJson(json);
	}

	public ZGridResult getResult() {
		return result;
	}
}
