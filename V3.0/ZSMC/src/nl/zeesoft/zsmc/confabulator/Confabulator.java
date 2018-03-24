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
	
			
			
			/*
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
				List<Module> contractModules = new ArrayList<Module>();
				StringBuilder names = new StringBuilder();
				addUnlockedPrefixModules(names,logMods,forwardModules,backwardModules);
				addUnlockedPrefixModules(contractModules);
				addUnlockedSequenceModules(names,logMods,forwardModules,backwardModules);
				if (logMods.size()>0) {
					//System.out.println("===> Confabulate prefixes: " + names);
					firedLinks = confabulateSymbols(confab,stop,forwardModules,backwardModules,contractModules,false);
					logModuleStates(confab,"Confabulated prefix(es); " + names + ". Fired links; " + firedLinks,logMods);
					logMods.clear();
				}
			}
			
			if ((new Date()).getTime()<confab.stopTime) {
				for (int i = 0; i<confab.confSequenceSymbols; i++) {
					long stop = confab.startTime + confab.getContextMs() + confab.getPrefixMs() + (confab.getSymbolMs() * (i + 1));
					while(!modules.get(0).isLocked()) {
						int firedLinks = 0;
						StringBuilder names = new StringBuilder();
						List<Module> forwardModules = new ArrayList<Module>();
						List<Module> backwardModules = new ArrayList<Module>();
						List<Module> contractModules = new ArrayList<Module>();
						addUnlockedSequenceModules(names,logMods,forwardModules,backwardModules,true);
						addUnlockedSequenceModules(contractModules);
						if (logMods.size()>0) {
							firedLinks = confabulateSymbols(confab,stop,forwardModules,backwardModules,contractModules,true);
							logModuleStates(confab,"Confabulated modules; " + names + ". Fired links; " + firedLinks,logMods);
							logMods.clear();
						}
						if ((new Date()).getTime()>=stop) {
							break;
						}
					}
					
					// TODO: remove break;
					break;
				}
			}
			*/
			long stopContext = confab.startTime + confab.getContextMs();
			long stopPrefix = confab.startTime + confab.getContextMs() + confab.getPrefixMs();
			while ((new Date()).getTime()<confab.stopTime) {
				List<Module> logMods = null;
				if (!context.isLocked() && ((new Date()).getTime()<stopContext)) {
					//System.out.println("===> Confabulate context");
					logMods = runConfabulationCycle(confab,stopContext,true,false,false);
				} else {
					if ((new Date()).getTime()<stopPrefix) {
						for (Module mod: prefix) {
							if (!mod.isLocked()) {
								logMods = runConfabulationCycle(confab,stopPrefix,true,true,false);
								break;
							}
						}
					}
					if (logMods==null || logMods.size()==0) {
						for (Module mod: modules) {
							if (!mod.isLocked()) {
								logMods = runConfabulationCycle(confab,confab.stopTime,true,true,true);
							}
						}
					}
				}
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

	/*
	private List<KnowledgeBaseWorker> activateWorkers(long stopTime,List<Module> forwardModules,List<Module> backwardModules,boolean skipContext) {
		List<KnowledgeBaseWorker> workers = null;
		lockMe(this);
		activeWorkers.clear();
		for (KnowledgeBaseWorker kbw: kbws) {
			if (
				(
					(kbw.isForward() && forwardModules.contains(kbw.getTargetModule())) ||
					(!kbw.isForward() && backwardModules.contains(kbw.getSourceModule()))
				) && (
					(!skipContext || !kbw.getSourceModule().isContext())
				)
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
	*/

	private List<Module> runConfabulationCycle(Confabulation confab,long stopTime,boolean doContext,boolean doPrefix,boolean doSequence) {
		int firedLinks = 0;
		List<Module> logMods = new ArrayList<Module>();
		
		List<KnowledgeBaseWorker> workers = new ArrayList<KnowledgeBaseWorker>();
		if (doContext) {
			addContextWorkersToConfabulationCycle(workers);
		}
		if (doPrefix) {
			addPrefixWorkersToConfabulationCycle(workers);
		}
		if (doSequence) {
			addModuleWorkersToConfabulationCycle(workers);
		}
		
		workers = activateWorkers(workers,stopTime,logMods);
		waitForActiveWorkers();
		
		if (logMods.size()>0) {
			// Contract
			for (Module mod: logMods) {
				if (mod.isContext()) {
					mod.contract(confab.confContextSymbols);
				} else {
					mod.contract(kbs.getB());
				}
			}
			
			// Get results
			for (KnowledgeBaseWorker kbw: workers) {
				firedLinks += kbw.getFiredLinks();
			}
			logModuleStates(confab,"Confabulated modules. Fired links; " + firedLinks,logMods);
		}

		return logMods;
	}

	private List<KnowledgeBaseWorker> activateWorkers(List<KnowledgeBaseWorker> workers,long stopTime,List<Module> logMods) {
		lockMe(this);
		activeWorkers.clear();
		for (KnowledgeBaseWorker kbw: workers) {
			if (kbw.prepare(stopTime)) {
				activeWorkers.add(kbw);
			}
		}
		workers = new ArrayList<KnowledgeBaseWorker>(activeWorkers); 
		unlockMe(this);
		for (KnowledgeBaseWorker kbw: workers) {
			kbw.start();
			if (kbw.isForward()) {
				if (!logMods.contains(kbw.getTargetModule())) {
					logMods.add(kbw.getTargetModule());
				}
			} else {
				if (!logMods.contains(kbw.getSourceModule())) {
					logMods.add(kbw.getSourceModule());
				}
			}
		}
		return workers;
	}
	
	private void addContextWorkersToConfabulationCycle(List<KnowledgeBaseWorker> workers) {
		if (!context.isLocked()) {
			addWorkers(workers,getToContextWorkers());
		}
	}
	
	private void addPrefixWorkersToConfabulationCycle(List<KnowledgeBaseWorker> workers) {
		if (context.getActiveSymbolsSize()>0) {
			for (Module mod: prefix) {
				if (!mod.isLocked()) {
					if (context.getActiveSymbolsSize()>0) {
						addWorkers(workers,getFromContextWorkers(mod));
					}
					addWorkers(workers,getWorkersByTarget(mod,true,null));
					addWorkers(workers,getWorkersBySource(mod,false,null));
				}
			}
		}
	}

	private void addModuleWorkersToConfabulationCycle(List<KnowledgeBaseWorker> workers) {
		if (context.getActiveSymbolsSize()>0) {
			for (Module mod: modules) {
				if (!mod.isLocked()) {
					if (context.getActiveSymbolsSize()>0 && mod.getActiveSymbolsSize()==0) {
						addWorkers(workers,getFromContextWorkers(mod));
					}
					addWorkers(workers,getWorkersByTarget(mod,true,null));
					//addWorkers(workers,getWorkersBySource(mod,false,null));
				}
			}
		}
	}
	
	private void addWorkers(List<KnowledgeBaseWorker> workers,List<KnowledgeBaseWorker> addWorkers) {
		for (KnowledgeBaseWorker worker: addWorkers) {
			if (!workers.contains(worker)) {
				workers.add(worker);
			}
		}
	}
	
	private List<KnowledgeBaseWorker> getToContextWorkers() {
		List<KnowledgeBaseWorker> workers = new ArrayList<KnowledgeBaseWorker>();
		workers = getWorkersBySource(context,false,null);
		return workers;
	}
	
	private List<KnowledgeBaseWorker> getFromContextWorkers() {
		List<KnowledgeBaseWorker> workers = new ArrayList<KnowledgeBaseWorker>();
		workers = getWorkersBySource(context,true,null);
		return workers;
	}
	
	private List<KnowledgeBaseWorker> getFromContextWorkers(Module target) {
		return getWorkersBySource(context,target,true);
	}

	private List<KnowledgeBaseWorker> getWorkersByTarget(Module mod,boolean forward,List<Module> sources) {
		List<KnowledgeBaseWorker> workers = new ArrayList<KnowledgeBaseWorker>();
		for (KnowledgeBaseWorker worker: kbws) {
			if (worker.isForward()==forward && worker.getTargetModule()==mod) {
				if (sources == null || sources.size() == 0 || sources.contains(worker.getSourceModule())) {
					workers.add(worker);
				}
			}
		}
		return workers;
	}

	private List<KnowledgeBaseWorker> getWorkersBySource(Module mod,Module target,boolean forward) {
		List<KnowledgeBaseWorker> workers = new ArrayList<KnowledgeBaseWorker>();
		List<Module> targets = null;
		if (target!=null) {
			targets = new ArrayList<Module>();
			targets.add(target);
		}
		workers = getWorkersBySource(mod,forward,targets);
		return workers;
	}

	private List<KnowledgeBaseWorker> getWorkersBySource(Module mod,boolean forward,List<Module> targets) {
		List<KnowledgeBaseWorker> workers = new ArrayList<KnowledgeBaseWorker>();
		for (KnowledgeBaseWorker worker: kbws) {
			if (worker.isForward()==forward && worker.getSourceModule()==mod) {
				if (targets == null || targets.size() == 0 || targets.contains(worker.getTargetModule())) {
					workers.add(worker);
				}
			}
		}
		return workers;
	}
	
	/*
	private void addUnlockedPrefixModules(StringBuilder names, List<Module> logMods,List<Module> forwardModules,List<Module> backwardModules) {
		for (Module mod: prefix) {
			if (!mod.isLocked()) {
				forwardModules.add(mod);
				backwardModules.add(mod);
				logMods.add(mod);
				if (names.length()>0) {
					names.append(", ");
				}
				names.append(mod.getName());
			}
		}
	}

	private void addUnlockedPrefixModules(List<Module> contractModules) {
		for (Module mod: prefix) {
			if (!mod.isLocked() && !contractModules.contains(mod)) {
				contractModules.add(mod);
			}
		}
	}

	private void addUnlockedSequenceModules(StringBuilder names, List<Module> logMods,List<Module> forwardModules,List<Module> backwardModules) {
		addUnlockedSequenceModules(names,logMods,forwardModules,backwardModules,false);
	}

	private void addUnlockedSequenceModules(StringBuilder names, List<Module> logMods,List<Module> forwardModules,List<Module> backwardModules,boolean forwardOnly) {
		for (Module mod: modules) {
			if (!mod.isLocked()) {
				forwardModules.add(mod);
				if (!forwardOnly) {
					backwardModules.add(mod);
				}
				logMods.add(mod);
				if (names.length()>0) {
					names.append(", ");
				}
				names.append(mod.getName());
			}
		}
	}

	private void addUnlockedSequenceModules(List<Module> contractModules) {
		for (Module mod: modules) {
			if (!mod.isLocked()) {
				contractModules.add(mod);
			}
		}
	}

	private int confabulateSymbols(Confabulation confab,long stopTime,Module cMod,boolean backwardOnly) {
		List<Module> forwardModules = new ArrayList<Module>();
		List<Module> backwardModules = new ArrayList<Module>();
		List<Module> contractModules = new ArrayList<Module>();
		if (!backwardOnly) {
			forwardModules.add(cMod);
		}
		backwardModules.add(cMod);
		contractModules.add(cMod);
		return confabulateSymbols(confab,stopTime,forwardModules,backwardModules,contractModules,false);
	}

	private int confabulateSymbols(Confabulation confab,long stopTime,List<Module> forwardModules,List<Module> backwardModules,List<Module> contractModules, boolean skipContext) {
		int firedLinks = 0;
		
		List<KnowledgeBaseWorker> workers = activateWorkers(stopTime,forwardModules,backwardModules,skipContext);
		waitForActiveWorkers();
		
		// Contract
		for (Module mod: contractModules) {
			if (mod.isContext()) {
				mod.contract(confab.confContextSymbols);
			} else {
				mod.contract(kbs.getB());
			}
		}
		
		// Get results
		for (KnowledgeBaseWorker kbw: workers) {
			firedLinks += kbw.getFiredLinks();
		}
		
		return firedLinks;
	}
	*/
	
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
		if (conclusions.size()>0 && conclusions.size()<=confab.confContextSymbols) {
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
