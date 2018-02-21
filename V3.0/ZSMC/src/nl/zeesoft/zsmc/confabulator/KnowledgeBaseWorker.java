package nl.zeesoft.zsmc.confabulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class KnowledgeBaseWorker extends Worker {
	private KnowledgeBase		kb	 				= null;
	private int					minCount			= 0;
	private Module				sourceModule		= null;
	private Module				targetModule		= null;
	private boolean				forward				= true;
	
	private	long				stopTime			= 0;
	private boolean				done				= true;
	private int					firedLinks			= 0;

	private List<ModuleSymbol>	activeSourceSymbols	= new ArrayList<ModuleSymbol>();
	private List<ModuleSymbol>	activeTargetSymbols	= new ArrayList<ModuleSymbol>();
	
	public KnowledgeBaseWorker(Messenger msgr, WorkerUnion union, KnowledgeBase kb, int minCount, Module sourceModule, Module targetModule, boolean forward) {
		super(msgr, union);
		setSleep(0);
		this.kb = kb;
		this.minCount = minCount;
		this.sourceModule = sourceModule;
		this.targetModule = targetModule;
		this.forward = forward;
	}

	public Module getSourceModule() {
		return sourceModule;
	}

	public Module getTargetModule() {
		return targetModule;
	}

	public boolean isForward() {
		return forward;
	}
	
	public void setActiveSymbols() {
		activeSourceSymbols	= sourceModule.getActiveSymbols();
		activeTargetSymbols = targetModule.getActiveSymbols();
	}
	
	public void setStopTime(long stopTime) {
		this.stopTime = stopTime;
	}
	
	@Override
	public void start() {
		lockMe(this);
		firedLinks = 0;
		unlockMe(this);
		if ((forward && targetModule.isLocked()) ||
			(!forward && sourceModule.isLocked()) ||
			(activeSourceSymbols.size() == 0 && !sourceModule.isContext()) ||
			activeTargetSymbols.size() == 0
			) {
			//System.out.println("Not start worker source: " + sourceModule.getName() + " target: " + targetModule.getName() + " forward: " + forward);
			return;
		}
		lockMe(this);
		done = false;
		unlockMe(this);
		super.start();
		//System.out.println("Started worker source: " + sourceModule.getName() + " target: " + targetModule.getName() + " forward: " + forward);
	}

	public boolean isDone() {
		boolean r = false;
		lockMe(this);
		r = done;
		unlockMe(this);
		return r;
	}

	public int getFiredLinks() {
		int r = 0;
		lockMe(this);
		r = firedLinks;
		unlockMe(this);
		return r;
	}
	
	@Override
	public void whileWorking() {
		List<FireLink> fireLinks = new ArrayList<FireLink>();
		if (forward) {
			for (ModuleSymbol source: activeSourceSymbols) {
				if (activeTargetSymbols.size()>0) {
					for (ModuleSymbol target: activeTargetSymbols) {
						KnowledgeLink kl = kb.getLink(source.symbol,target.symbol,minCount);
						if (kl!=null) {
							fireLinks.add(new FireLink(kl,targetModule,true,source.excitation));
						}
					}
				} else if (sourceModule.isContext()) {
					for (KnowledgeLink kl: kb.getLinksBySource().get(source.symbol)) {
						if (kl.count>=minCount) {
							fireLinks.add(new FireLink(kl,targetModule,true,source.excitation));
						}
					}
				}
				if (stopTime <= (new Date()).getTime()) {
					break;
				}
			}
		} else {
			for (ModuleSymbol target: activeTargetSymbols) {
				if (activeSourceSymbols.size()>0) {
					for (ModuleSymbol source: activeSourceSymbols) {
						KnowledgeLink kl = kb.getLink(source.symbol,target.symbol,minCount);
						if (kl!=null) {
							fireLinks.add(new FireLink(kl,sourceModule,false,target.excitation));
						}
					}
				} else {
					for (KnowledgeLink kl: kb.getLinksByTarget().get(target.symbol)) {
						if (kl.count>=minCount) {
							fireLinks.add(new FireLink(kl,sourceModule,false,target.excitation));
						}
					}
				}
				if ((new Date()).getTime() >= stopTime) {
					break;
				}
			}
		}
		if (fireLinks.size()>0) {
			//long now = (new Date()).getTime();
			//System.out.println("now >= stopTime : " + now + " >= " + stopTime + " : " + (stopTime >= now));
			int addLinks = 0;
			for (FireLink fireLink: fireLinks) {
				fireLink.module.fireLink(fireLink);
				addLinks++;
				if ((new Date()).getTime() >= stopTime) {
					break;
				}
			}
			if (addLinks>0) {
				lockMe(this);
				firedLinks += addLinks;
				unlockMe(this);
			}
		}
		stop();
		lockMe(this);
		done = true;
		unlockMe(this);
	}
}
