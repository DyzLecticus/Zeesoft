package nl.zeesoft.zso.composition.sequencer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.protocol.WorkRequest;

public class CompositionMidiPlayerWorker extends CompositionPlayerWorker {
	private String				instrument		= "";

	private List<Integer>		notes			= new ArrayList<Integer>();
	private int					velocity		= 0;
	private long				durationMs		= 0;
	
	public CompositionMidiPlayerWorker(Messenger msgr, WorkerUnion uni,Sequencer sequencer,String positionName,String instrument) {
		super(msgr,uni,sequencer,positionName);
		this.instrument = instrument;
	}
	
	public void setPlayDateTime(long playDateTime,List<Integer> notes,int velocity,long durationMs) {
		getMessenger().warn(this,"Play " + instrument + ", notes: " + notes.size() + ", velocity: " + velocity + ", duration: " + durationMs + " ...");
		super.setPlayDateTime(playDateTime);
		lockMe(this);
		this.notes = notes;
		this.velocity = velocity;
		this.durationMs = durationMs;
		unlockMe(this);
	}
	
	@Override
	public void whileWorking() {
		long r = getPlayDateTime();
		if (r>0 && (new Date()).getTime()>=r && notes.size()>0) {
			JsFile req = new JsFile();
			req.rootElement = new JsElem();
			req.rootElement.children.add(new JsElem("instrument",instrument,true));
			JsElem notesElem = new JsElem("notes");
			req.rootElement.children.add(notesElem);
			int i = 0;
			for (Integer note: notes) {
				notesElem.children.add(new JsElem("" + i,"" + note));
				i++;
			}
			req.rootElement.children.add(new JsElem("velocity","" + velocity));
			req.rootElement.children.add(new JsElem("durationMs","" + durationMs));
			WorkRequest wr = new WorkRequest();
			wr.setRequest(req);
			sendWorkRequest(wr);
			setPlayDateTime(0);
		}
	}
	
	
}
