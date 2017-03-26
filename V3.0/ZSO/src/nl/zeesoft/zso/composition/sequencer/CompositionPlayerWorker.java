package nl.zeesoft.zso.composition.sequencer;

import java.util.Date;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.ConductorConnector;
import nl.zeesoft.zjmo.orchestra.members.WorkClient;
import nl.zeesoft.zjmo.orchestra.protocol.WorkRequest;

public class CompositionPlayerWorker extends Worker {
	private String				positionName	= "";

	private ConductorConnector	connector		= null;
	private WorkClient			client			= null;
	
	private long				playDateTime	= 0;
	private long				startMs			= 0;
	private long				durationMs		= 0;
	
	public CompositionPlayerWorker(Messenger msgr, WorkerUnion union,Sequencer sequencer,String positionName) {
		super(msgr, union);
		setSleep(1);
		this.positionName = positionName;
		connector = new ConductorConnector(msgr,union);
		connector.initialize(sequencer.getOrchestra().getConductors(),false);
	}
	
	
	@Override
	public void start() {
		connector.open();
		super.start();
	}
	
	@Override
	public void stop() {
		super.stop();
		lockMe(this);
		if (client!=null && client.isOpen()) {
			client.sendCloseSessionCommand();
			client.close();
		}
		unlockMe(this);
		connector.close();
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
			if (client==null || !client.isOpen()) {
				client = connector.getWorkClient();
			}
			boolean handled = true;
			if (client!=null && client.isOpen()) {
				ZStringBuilder response = client.sendWorkRequest(wr);
				if (response==null) {
					handled = false;
				} else {
					WorkRequest rwr = new WorkRequest();
					rwr.fromStringBuilder(response);
					if (rwr.getError().length()>0) {
						handled = false;
					}
				}
			} else {
				handled = false;
			}
			if (!handled) {
				getMessenger().error(this,"Failed to play position: " + positionName);
			}
			setPlayDateTime(0,0,0);
		}
	}
}
