package nl.zeesoft.zsmc.confabulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsmc.SpellingChecker;
import nl.zeesoft.zsmc.sequence.AnalyzerSymbol;

public class Confabulator extends Locker {
	private KnowledgeBases				kbs					= null;
	
	private List<Module>				prefix				= new ArrayList<Module>();
	private List<Module>				modules				= new ArrayList<Module>();
	private Module						context				= null;
	
	private List<KnowledgeBaseWorker>	kbws				= new ArrayList<KnowledgeBaseWorker>();
	
	private SpellingChecker				sc					= null;
	private List<Module>				allSequenceModules	= new ArrayList<Module>();
	
	private boolean						confabulating		= false;
	private List<KnowledgeBaseWorker>	activeWorkers		= new ArrayList<KnowledgeBaseWorker>();

	public Confabulator(Messenger msgr, WorkerUnion uni,KnowledgeBases kbs) {
		super(msgr);
		this.kbs = kbs;
		initialize(uni,0);
	}

	public Confabulator(Messenger msgr, WorkerUnion uni,KnowledgeBases kbs, int prefixes) {
		super(msgr);
		this.kbs = kbs;
		initialize(uni,prefixes);
	}
	
	public boolean isConfabulating() {
		boolean r = false;
		lockMe(this);
		r = confabulating;
		unlockMe(this);
		return r;
	}
	
	public synchronized boolean confabulate(Confabulation confab) {
		boolean r = false;
		lockMe(this);
		if (!confabulating) {
			confabulating = true;
			r = true;
		}
		unlockMe(this);
		if (r) {
			if (confab.confMs < 10) {
				confab.confMs = 10;
			}
			confab.startTime = (new Date()).getTime();
			confab.stopTime = confab.startTime + confab.confMs;
	
			initializeConclusions(confab);
	
			List<Module> logMods = new ArrayList<Module>();
			
			if (!context.isLocked()) {
				//System.out.println("===> Confabulate context");
				long stop = confab.startTime + confab.getContextMs();
				int firedLinks = confabulateSymbols(confab,stop,context,true);
				logMods.add(context);
				logModuleStates(confab,"Confabulated context. Fired links; " + firedLinks,logMods);
				logMods.clear();
			}
	
			if ((new Date()).getTime()<confab.stopTime) {
				long stop = confab.startTime + confab.getContextMs() + confab.getPrefixMs();
				int firedLinks = 0;
				List<Module> forwardModules = new ArrayList<Module>();
				List<Module> backwardModules = new ArrayList<Module>();
				StringBuilder names = new StringBuilder();
				for (Module pMod: prefix) {
					if (!pMod.isLocked()) {
						forwardModules.add(pMod);
						backwardModules.add(pMod);
						logMods.add(pMod);
						if (names.length()>0) {
							names.append(", ");
						}
						names.append(pMod.getName());
					}
				}
				if (logMods.size()>0) {
					//System.out.println("===> Confabulate prefixes: " + names);
					firedLinks = confabulateSymbols(confab,stop,forwardModules,backwardModules);
					logModuleStates(confab,"Confabulated prefix(es); " + names + ". Fired links; " + firedLinks,logMods);
					logMods.clear();
				}
			}
			
			// TODO: Modules
			for (int i = 0; i<confab.confSequenceSymbols; i++) {
				// ...
			}
			
			lockMe(this);
			confabulating = false;
			unlockMe(this);
		}
		return r;
	} 

	protected synchronized void workerIsDone(KnowledgeBaseWorker kbw) {
		boolean done = false;
		lockMe(this);
		activeWorkers.remove(kbw);
		done = activeWorkers.size()==0;
		unlockMe(this);
		if (done) {
			notify();
		}
	}

	private synchronized boolean workersAreDone() {
		boolean r = false;
		lockMe(this);
		r = activeWorkers.size()==0;
		unlockMe(this);
		return r;
	}

	private void waitForActiveWorkers() {
		while(!workersAreDone()) {
			try {
				wait();
			} catch (InterruptedException e) {
				getMessenger().error(this,"Waiting for active workers was interrupted",e);
			}
		}
	}

	private List<KnowledgeBaseWorker> activateWorkers(long stopTime,List<Module> forwardModules,List<Module> backwardModules) {
		List<KnowledgeBaseWorker> workers = null;
		lockMe(this);
		activeWorkers.clear();
		for (KnowledgeBaseWorker kbw: kbws) {
			if ((kbw.isForward() && forwardModules.contains(kbw.getTargetModule())) ||
				(!kbw.isForward() && backwardModules.contains(kbw.getSourceModule()))
				) {
				if (kbw.prepare(stopTime)) {
					activeWorkers.add(kbw);
				}
			}
		}
		workers = new ArrayList<KnowledgeBaseWorker>(activeWorkers); 
		unlockMe(this);
		for (KnowledgeBaseWorker kbw: workers) {
			kbw.start();
		}
		return workers;
	}
	
	private int confabulateSymbols(Confabulation confab,long stopTime,Module cMod,boolean backwardOnly) {
		List<Module> forwardModules = new ArrayList<Module>();
		List<Module> backwardModules = new ArrayList<Module>();
		if (!backwardOnly) {
			forwardModules.add(cMod);
		}
		backwardModules.add(cMod);
		return confabulateSymbols(confab,stopTime,forwardModules,backwardModules);
	}

	private int confabulateSymbols(Confabulation confab,long stopTime,List<Module> forwardModules,List<Module> backwardModules) {
		int firedLinks = 0;
		
		List<KnowledgeBaseWorker> workers = activateWorkers(stopTime,forwardModules,backwardModules);
		waitForActiveWorkers();
		
		// Contract
		for (Module mod: forwardModules) {
			if (mod.isContext()) {
				mod.contract(confab.confContextSymbols);
			} else {
				mod.contract(1);
			}
		}
		for (Module mod: backwardModules) {
			if (!forwardModules.contains(mod)) {
				if (mod.isContext()) {
					mod.contract(confab.confContextSymbols);
				} else {
					mod.contract(1);
				}
			}
		}
		
		// Get results
		for (KnowledgeBaseWorker kbw: workers) {
			firedLinks += kbw.getFiredLinks();
		}
		
		return firedLinks;
	}
	
	private void initializeConclusions(Confabulation confab) {
		// Set context
		List<String> symbols = confab.contextSymbols.toSymbolsPunctuated();
		List<ModuleSymbol> conclusions = new ArrayList<ModuleSymbol>();
		for (String symbol: symbols) {
			ModuleSymbol sym = new ModuleSymbol();
			sym.symbol = symbol;
			sym.excitation = 1.0D;
			conclusions.add(sym);
		}
		context.setConclusions(conclusions);
		if (conclusions.size()==1) {
			context.setLocked(true);
		}
		
		// Set prefix
		symbols = confab.inputSymbols.toSymbolsPunctuated();
		int si = (symbols.size() - 1);
		for (int i = (prefix.size() - 1); i >=0; i--) {
			conclusions.clear();
			if (si>=0) {
				String symbol = "";
				symbol = symbols.get(si);
				for (AnalyzerSymbol as: sc.getCorrections(symbol)) {
					ModuleSymbol sym = new ModuleSymbol();
					sym.symbol = as.symbol;
					sym.excitation = as.prob;
					conclusions.add(sym);
				}
			}
			si--;
			prefix.get(i).setConclusions(conclusions);
			if (conclusions.size()<=1) {
				prefix.get(i).setLocked(true);
			}
		}
		
		// Clear modules
		for (Module mod: modules) {
			mod.setConclusion("");
		}
		
		logModuleStates(confab,"Initial module states;");
	}
	
	private void logModuleStates(Confabulation confab,String message) {
		logModuleStates(confab,message,null);
	}

	private void logModuleStates(Confabulation confab,String message, List<Module> mods) {
		ZStringBuilder append = new ZStringBuilder(message);
		append.append("\n");
		if (mods==null || mods.contains(context)) {
			append.append(logModuleState(confab,context,context.getName()));
		}
		for (int i = 0; i<prefix.size(); i++) {
			if (mods==null || mods.contains(prefix.get(i))) {
				append.append(logModuleState(confab,prefix.get(i),prefix.get(i).getName()));
			}
		}
		for (int i = 0; i<modules.size(); i++) {
			if (mods==null || mods.contains(modules.get(i))) {
				append.append(logModuleState(confab,modules.get(i),modules.get(i).getName()));
			}
		}
		confab.appendLog(append);
	}
	
	private ZStringBuilder logModuleState(Confabulation confab, Module mod, String moduleName) {
		ZStringBuilder append = new ZStringBuilder();
		append.append(moduleName);
		if (mod.isLocked()) {
			append.append(" L");
		} else {
			append.append(" U");
		}
		append.append(": ");
		int added = 0;
		List<ModuleSymbol> activeSymbols = mod.getActiveSymbols();
		for (ModuleSymbol ms: activeSymbols) {
			if (added < 3) {
				if (added>0) {
					append.append(" ");
				}
				append.append(ms.symbol);
				append.append("(");
				append.append("" + ms.excitation);
				append.append(")");
			} else if (added==3) {
				append.append(" [ ... ");
				append.append("" + (activeSymbols.size() - added));
				append.append("]");
			}
			added++;
		}
		append.append("\n");
		return append;
	}

	private void intitializeModules(int prefixes) {
		allSequenceModules.clear();
		prefix.clear();
		for (int i = 0; i<prefixes; i++) {
			Module add = new Module(getMessenger(),kbs,"Prefix " + (i+1),false);
			prefix.add(add);
			allSequenceModules.add(add);
		}
		modules.clear();
		for (int i = 0; i<kbs.getModules(); i++) {
			Module add = new Module(getMessenger(),kbs,"Module " + (i+1),false);
			modules.add(add);
			allSequenceModules.add(add);
		}
		context = new Module(getMessenger(),kbs,"Context",true);
	}

	private void intitializeWorkers(WorkerUnion uni) {
		for (Module sMod: allSequenceModules) {
			kbws.add(new KnowledgeBaseWorker(getMessenger(),uni,this,kbs.getContext(),kbs.getMinCount(),context,sMod,true));
			kbws.add(new KnowledgeBaseWorker(getMessenger(),uni,this,kbs.getContext(),kbs.getMinCount(),context,sMod,false));
		}
		int s = 0;
		for (Module sMod: allSequenceModules) {
			int m = s + (kbs.getModules() - 1);
			int kb = 0;
			for (int i = (s + 1); i < m; i++) {
				if (i<allSequenceModules.size()) {
					kbws.add(new KnowledgeBaseWorker(getMessenger(),uni,this,kbs.getKnowledgeBases().get(kb),kbs.getMinCount(),sMod,allSequenceModules.get(i),true));
					kbws.add(new KnowledgeBaseWorker(getMessenger(),uni,this,kbs.getKnowledgeBases().get(kb),kbs.getMinCount(),sMod,allSequenceModules.get(i),false));
				} else {
					break;
				}
			}
			s++;
		}
	}

	private void initialize(WorkerUnion uni, int prefixes) {
		if (prefixes<=0) {
			prefixes = (kbs.getModules() - 1);
		}
		sc = new SpellingChecker();
		sc.setKnownSymbols(kbs.getKnownSymbols());
		sc.setTotalSymbols(kbs.getTotalSymbols());
		intitializeModules(prefixes);
		intitializeWorkers(uni);
	}
}
