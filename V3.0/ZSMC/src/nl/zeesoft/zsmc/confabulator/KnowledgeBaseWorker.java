package nl.zeesoft.zsmc.confabulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class KnowledgeBaseWorker extends Worker {
	private KnowledgeBase		kb	 				= null;
	private Module				sourceModule		= null;
	private Module				targetModule		= null;
	private boolean				forward				= true;
	
	private int					maxMs				= 100;
	
	private	long				stopTime			= 0;
	private boolean				done				= true;
	private int					firedLinks			= 0;

	private List<ModuleSymbol>	activeSourceSymbols	= new ArrayList<ModuleSymbol>();
	private List<ModuleSymbol>	activeTargetSymbols	= new ArrayList<ModuleSymbol>();
	
	public KnowledgeBaseWorker(Messenger msgr, WorkerUnion union, KnowledgeBase kb, Module sourceModule, Module targetModule, boolean forward) {
		super(msgr, union);
		setSleep(0);
		this.kb = kb;
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
	
	public void setMaxMs(int maxMs) {
		this.maxMs = maxMs;
	}
	
	@Override
	public void start() {
		lockMe(this);
		firedLinks = 0;
		unlockMe(this);
		if ((forward && targetModule.isLocked()) ||
			(!forward && sourceModule.isLocked()) ||
			activeSourceSymbols.size() == 0 ||
			activeTargetSymbols.size() == 0
			) {
			System.out.println("Not start worker from: " + sourceModule.getName() + " to: " + targetModule.getName() + " forward: " + forward);
			return;
		}
		lockMe(this);
		stopTime = (new Date()).getTime() + maxMs;
		done = false;
		unlockMe(this);
		super.start();
		System.out.println("Started worker from: " + sourceModule.getName() + " to: " + targetModule.getName() + " forward: " + forward);
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
		boolean stop = true;
		//List<ModuleSymbol> activeSourceSymbols = sourceModule.getActiveSymbols();
		//List<ModuleSymbol> activeTargetSymbols = targetModule.getActiveSymbols();
		List<FireLink> fireLinks = new ArrayList<FireLink>();
		if (forward) {
			for (ModuleSymbol source: activeSourceSymbols) {
				if (activeTargetSymbols.size()>0) {
					for (ModuleSymbol target: activeTargetSymbols) {
						KnowledgeLink kl = kb.getLink(source.symbol,target.symbol);
						if (kl!=null) {
							fireLinks.add(new FireLink(kl,targetModule,true,source.excitation));
						}
					}
				} else if (sourceModule.isContext()) {
					for (KnowledgeLink kl: kb.getLinksBySource().get(source.symbol)) {
						fireLinks.add(new FireLink(kl,targetModule,true,source.excitation));
					}
				}
				if (stopTime <= (new Date()).getTime()) {
					stop = true;
					break;
				}
			}
		} else {
			for (ModuleSymbol target: activeTargetSymbols) {
				if (activeSourceSymbols.size()>0) {
					for (ModuleSymbol source: activeSourceSymbols) {
						KnowledgeLink kl = kb.getLink(source.symbol,target.symbol);
						if (kl!=null) {
							fireLinks.add(new FireLink(kl,sourceModule,false,target.excitation));
						}
					}
				} else {
					for (KnowledgeLink kl: kb.getLinksByTarget().get(target.symbol)) {
						fireLinks.add(new FireLink(kl,sourceModule,false,target.excitation));
					}
				}
				if (stopTime <= (new Date()).getTime()) {
					stop = true;
					break;
				}
			}
		}
		if (fireLinks.size()>0) {
			System.out.println("stopTime <= (new Date()).getTime() : " + stopTime + " <= " + (new Date()).getTime() + " : " + (stopTime <= (new Date()).getTime()));
			int addLinks = 0;
			if (forward) {
				addLinks = targetModule.fireLinks(fireLinks);
			} else if (!forward) {
				addLinks = sourceModule.fireLinks(fireLinks);
			}
			if (addLinks>0) {
				lockMe(this);
				firedLinks += addLinks;
				unlockMe(this);
			}
			//if (forward && sourceModule.isContext()) {
				stop = true;
			//}
		}
		if ((forward && targetModule.isLocked()) ||
			(!forward && sourceModule.isLocked())
			) {
			stop = true;
		}
		if (stop || (stopTime <= (new Date()).getTime())) {
			stop();
			lockMe(this);
			done = true;
			unlockMe(this);
		}
	}
}
