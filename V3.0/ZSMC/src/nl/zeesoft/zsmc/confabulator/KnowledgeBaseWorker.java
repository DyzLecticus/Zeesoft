package nl.zeesoft.zsmc.confabulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class KnowledgeBaseWorker extends Worker {
	private Confabulator		confabulator		= null;
	
	private KnowledgeBase		kb	 				= null;
	private int					minCount			= 0;
	private Module				sourceModule		= null;
	private Module				targetModule		= null;
	private boolean				forward				= true;
	
	private	long				stopTime			= 0;
	private int					firedLinks			= 0;

	private List<ModuleSymbol>	activeSourceSymbols	= new ArrayList<ModuleSymbol>();
	private List<ModuleSymbol>	activeTargetSymbols	= new ArrayList<ModuleSymbol>();
	
	public KnowledgeBaseWorker(Messenger msgr, WorkerUnion union, Confabulator conf, KnowledgeBase kb, int minCount, Module sourceModule, Module targetModule, boolean forward) {
		super(msgr, union);
		setSleep(0);
		this.confabulator = conf;
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
	
	public boolean prepare(long stopTime) {
		boolean start = true;
		this.stopTime = stopTime;
		activeSourceSymbols	= sourceModule.getActiveSymbols();
		activeTargetSymbols = targetModule.getActiveSymbols();
		if ((forward && targetModule.isLocked()) ||
			(!forward && sourceModule.isLocked()) ||
			(activeSourceSymbols.size() == 0 && !sourceModule.isContext()) ||
			(activeTargetSymbols.size() == 0 && !sourceModule.isContext())
			) {
			//System.out.println("Not start worker source: " + sourceModule.getName() + " target: " + targetModule.getName() + " forward: " + forward);
			start = false;
		}
		return start;
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
		int fLinks = 0;
		boolean stop = false;
		if (forward) {
			for (ModuleSymbol source: activeSourceSymbols) {
				if (activeTargetSymbols.size()>0) {
					for (ModuleSymbol target: activeTargetSymbols) {
						KnowledgeLink kl = kb.getLink(source.symbol,target.symbol,minCount);
						if (kl!=null) {
							targetModule.fireLink(new FireLink(kl,targetModule,true,source.excitation));
							fLinks++;
						}
						if (stopTime <= (new Date()).getTime()) {
							stop = true;
							break;
						}
					}
				} else if (sourceModule.isContext()) {
					for (KnowledgeLink kl: kb.getLinksBySource().get(source.symbol)) {
						if (kl.count>=minCount) {
							targetModule.fireLink(new FireLink(kl,targetModule,true,source.excitation));
							fLinks++;
							if (stopTime <= (new Date()).getTime()) {
								stop = true;
								break;
							}
						}
					}
				}
				if (stop) {
					break;
				}
			}
		} else {
			for (ModuleSymbol target: activeTargetSymbols) {
				if (activeSourceSymbols.size()>0) {
					for (ModuleSymbol source: activeSourceSymbols) {
						KnowledgeLink kl = kb.getLink(source.symbol,target.symbol,minCount);
						if (kl!=null) {
							sourceModule.fireLink(new FireLink(kl,sourceModule,false,target.excitation));
							fLinks++;
							if (stopTime <= (new Date()).getTime()) {
								stop = true;
								break;
							}
						}
					}
				} else {
					for (KnowledgeLink kl: kb.getLinksByTarget().get(target.symbol)) {
						if (kl.count>=minCount) {
							sourceModule.fireLink(new FireLink(kl,sourceModule,false,target.excitation));
							fLinks++;
							if (stopTime <= (new Date()).getTime()) {
								stop = true;
								break;
							}
						}
					}
				}
				if (stop) {
					break;
				}
			}
		}
		stop();
		lockMe(this);
		firedLinks += fLinks;
		unlockMe(this);
		confabulator.workerIsDone(this);
	}
}
