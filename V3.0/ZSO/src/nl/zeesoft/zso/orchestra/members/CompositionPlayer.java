package nl.zeesoft.zso.orchestra.members;

import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zso.composition.Composition;
import nl.zeesoft.zso.composition.Step;
import nl.zeesoft.zso.orchestra.SampleOrchestra;

public class CompositionPlayer extends Worker {
	private	SampleConductor								conductor	= null;
	private int											bar			= 1;
	private int											step		= 0;
	private SortedMap<String,CompositionPlayerWorker>	workers		= new TreeMap<String,CompositionPlayerWorker>();
	
	public CompositionPlayer(Messenger msgr, WorkerUnion union,SampleConductor conductor) {
		super(msgr, union);
		setSleep(1);
		this.conductor = conductor;
		workers.put(SampleOrchestra.BASEBEAT,new CompositionPlayerWorker(msgr,union,conductor.getNewWorkClient(msgr,union),SampleOrchestra.BASEBEAT));
		workers.put(SampleOrchestra.SNARE,new CompositionPlayerWorker(msgr,union,conductor.getNewWorkClient(msgr,union),SampleOrchestra.SNARE));
		workers.put(SampleOrchestra.HIHAT,new CompositionPlayerWorker(msgr,union,conductor.getNewWorkClient(msgr,union),SampleOrchestra.HIHAT));
	}
	
	@Override
	public void start() {
		bar = 1;
		step = 0;
		for (CompositionPlayerWorker worker: workers.values()) {
			worker.start();
		}
		super.start();
	}

	@Override
	public void stop() {
		super.stop();
		for (CompositionPlayerWorker worker: workers.values()) {
			worker.stop();
		}
		bar = 1;
		step = 0;
	}
	
	@Override
	public void whileWorking() {
		Composition comp = conductor.getComposition();
		Date now = new Date();	
		if (comp!=null) {
			long playDateTime = now.getTime() + (comp.getMsForStep(step) / 2);
			lockMe(this);
			step++;
			if (step>comp.getStepsPerBar()) {
				step = 1;
				bar++;
			}
			if (bar>comp.getBars()) {
				bar = 1;
			}
			List<Step> steps = comp.getSteps(bar, step);
			unlockMe(this);
			setSleep((int) comp.getMsForStep(step));
			for (Step stp: steps) {
				CompositionPlayerWorker worker = workers.get(stp.getPositionName());
				if (worker!=null && worker.isWorking() && worker.getPlayDateTime()==0) {
					worker.setPlayDateTime(playDateTime,stp.getStartMs(),stp.getDurationMs());
				}
			}
		} else {
			bar = 1;
			step = 1;
		}
	}
}
