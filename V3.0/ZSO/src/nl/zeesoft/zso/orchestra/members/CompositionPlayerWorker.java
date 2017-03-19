package nl.zeesoft.zso.orchestra.members;

import java.util.Date;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.members.WorkClient;
import nl.zeesoft.zjmo.orchestra.protocol.WorkRequest;

public class CompositionPlayerWorker extends Worker {
	private WorkClient		client			= null;
	private String			positionName	= "";
	
	private long			playDateTime	= 0;
	private long			startMs			= 0;
	private long			durationMs		= 0;
	
	public CompositionPlayerWorker(Messenger msgr, WorkerUnion union,WorkClient client,String positionName) {
		super(msgr, union);
		setSleep(1);
		this.client = client;
		this.positionName = positionName;
	}
	
	
	@Override
	public void start() {
		if (client.open()) {
			super.start();
		}
	}
	
	@Override
	public void stop() {
		super.stop();
		if (client.isOpen()) {
			client.sendCloseSessionCommand();
			client.close();
		}
	}

	public long getPlayDateTime() {
		long r = 0;
		lockMe(this);
		r = playDateTime;
		unlockMe(this);
		return r;
	}
	
	public void setPlayDateTime(long playDateTime,long startMs,long durationMs) {
		lockMe(this);
		this.playDateTime = playDateTime;
		this.startMs = startMs;
		this.durationMs = durationMs;
		unlockMe(this);
	}
	
	@Override
	public void whileWorking() {
		long r = getPlayDateTime();
		if (r>0 && (new Date()).getTime()>=r) {
			JsFile req = new JsFile();
			req.rootElement = new JsElem();
			req.rootElement.children.add(new JsElem("startMs","" + startMs));
			if (durationMs>0) {
				req.rootElement.children.add(new JsElem("durationMs","" + durationMs));
			}
			WorkRequest wr = new WorkRequest();
			wr.setPositionName(positionName);
			wr.setRequest(req);
			client.sendWorkRequest(wr);
			setPlayDateTime(0,0,0);
		}
	}
}
