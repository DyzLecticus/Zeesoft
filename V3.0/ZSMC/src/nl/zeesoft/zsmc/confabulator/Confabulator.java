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
	
	public Confabulator(Messenger msgr, WorkerUnion uni,KnowledgeBases kbs) {
		super(msgr);
		this.kbs = kbs;
		sc = new SpellingChecker();
		sc.setKnownSymbols(kbs.getKnownSymbols());
		sc.setTotalSymbols(kbs.getTotalSymbols());
		intitializeModules();
		intitializeWorkers(uni);
	}

	public void confabulate(Confabulation confab) {
		if (confab.confMs < 10) {
			confab.confMs = 10;
		}
		confab.startTime = (new Date()).getTime();
		confab.stopTime = confab.startTime + confab.confMs;

		initializeConclusions(confab);

		if (!context.isLocked()) {
			System.out.println("===> Confabulate context");
			long stop = confab.startTime + confab.getContextMs();
			int firedLinks = confabulateSymbols(confab,context,stop);
			logModuleStates(confab,"Confabulated context. Fired links; " + firedLinks);
		}

		if ((new Date()).getTime()<confab.stopTime) {
			long stop = confab.startTime + confab.getContextMs() + confab.getPrefixMs();
			int firedLinks = 0;
			boolean log = false;
			for (Module pMod: prefix) {
				if (!pMod.isLocked()) {
					System.out.println("===> Confabulate prefix: " + pMod.getName());
					firedLinks = confabulateSymbols(confab,pMod,stop);
					log = true;
				}
			}
			if (log) {
				logModuleStates(confab,"Confabulated prefix. Fired links; " + firedLinks);
			}
		}
		
		// TODO: Modules
	}

	private int confabulateSymbols(Confabulation confab,Module cMod,long stopTime) {
		int firedLinks = 0;

		List<KnowledgeBaseWorker> workers = new ArrayList<KnowledgeBaseWorker>();
		
		// Prepare
		for (KnowledgeBaseWorker kbw: kbws) {
			if ((kbw.isForward() && kbw.getTargetModule()==cMod) ||
				(!kbw.isForward() && kbw.getSourceModule()==cMod)
				) {
				workers.add(kbw);
				kbw.setActiveSymbols();
				kbw.setStopTime(stopTime);
			}
		}

		// Start
		for (KnowledgeBaseWorker kbw: workers) {
			kbw.start();
		}

		// Wait
		for (KnowledgeBaseWorker kbw: workers) {
			while (!kbw.isDone()) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					getMessenger().error(this,"Knowledge base worker was interrupted",e);
				}
			}
		}
		
		// Contract
		cMod.contract(true);
		
		// Get results
		for (KnowledgeBaseWorker kbw: workers) {
			firedLinks += kbw.getFiredLinks();
		}
		
		return firedLinks;
	}
	
	/*
	public void confabulate(Confabulation confab) {
		initializeConclusions(confab);
		if (confab.contextSymbols.length()==0 && confab.inputSymbols.length()>0) {
			confabulateContext(confab);
		}
		
		Module cMod = null; 
		for (Module pm: prefix) {
			if (!pm.isLocked()) {
				cMod = pm;
				break;
			}
		}
		if (cMod!=null) {
			confabulateSequenceModule(confab,cMod);
			int index = (allSequenceModules.indexOf(cMod) + 1);
			while(!cMod.isLocked()) {
				if (index==allSequenceModules.size()) {
					break;
				}
				Module mod = allSequenceModules.get(index);
				if (!mod.isLocked()) {
					confabulateSequenceModule(confab,mod);
					confabulateSequenceModule(confab,cMod);
					//break;
				}
				index++;
			}
		}
	}
	
	/*
	private void confabulateSequenceModule(Confabulation confab,Module mod) {
		List<FireLink> contextLinks = getContextFireLinksForModule(mod);
		List<FireLink> fireLinks = new ArrayList<FireLink>();
		addSequentialFireLinks(mod,fireLinks,contextLinks);
		fireLinksAndContractModules(confab,fireLinks);
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
	
	/*
	private List<FireLink> confabulateContext(Confabulation confab) {
		List<FireLink> fireLinks = new ArrayList<FireLink>();
		List<ModuleSymbol> activeSymbols = null;
		for (Module mod: prefix) {
			activeSymbols = mod.getActiveSymbols();
			if (activeSymbols.size()>0) {
				for (ModuleSymbol ms: activeSymbols) {
					List<KnowledgeLink> kls = kbs.getContext().getLinksByTarget().get(ms.symbol);
					for (KnowledgeLink kl: kls) {
						fireLinks.add(new FireLink(kl, context, false, ms.excitation));
					}
				}
			}
		}
		fireLinksAndContractModules(confab,fireLinks);
		return fireLinks;
	}

	private void fireLinksAndContractModules(Confabulation confab, List<FireLink> fireLinks) {
		List<Module> mods = fireLinks(fireLinks);
		logModuleStates(confab,"Fired links; " + fireLinks.size(),mods);
		contractModules(mods);
		logModuleStates(confab,"Contracted modules; " + mods.size(),mods);
	}
	
	private List<FireLink> getContextFireLinksForModule(Module mod) {
		List<FireLink> fireLinks = new ArrayList<FireLink>();
		List<ModuleSymbol> activeSymbols = context.getActiveSymbols();
		List<ModuleSymbol> confabulatedSymbols = mod.getActiveSymbols();
		if (activeSymbols.size()>0) {
			boolean add = false;
			for (ModuleSymbol ms: activeSymbols) {
				List<KnowledgeLink> kls = kbs.getContext().getLinksBySource().get(ms.symbol);
				for (KnowledgeLink kl: kls) {
					add = true;
					if (confabulatedSymbols.size()>0) {
						add = false;
						for (ModuleSymbol cms: confabulatedSymbols) {
							if (cms.symbol.equals(kl.target)) {
								add = true;
								break;
							}
						}
					}
					if (add) {
						fireLinks.add(new FireLink(kl, mod, true, ms.excitation));
					}
				}
			}
		}
		return fireLinks;
	}

	private void addSequentialFireLinks(Module cMod, List<FireLink> fireLinks, List<FireLink> contextLinks) {
		boolean forward = true;
		int distance = 0;
		List<ModuleSymbol> confabulatedSymbols = cMod.getActiveSymbols();
		for (Module mod: allSequenceModules) {
			if (mod!=cMod) {
				if (forward) {
					distance = allSequenceModules.indexOf(cMod) - allSequenceModules.indexOf(mod);
				} else {
					distance = allSequenceModules.indexOf(mod) - allSequenceModules.indexOf(cMod);
				}
				if (distance>0 && distance<kbs.getModules()) {
					List<ModuleSymbol> activeSymbols = mod.getActiveSymbols();
					if (activeSymbols.size()>0) {
						KnowledgeBase kb = kbs.getKnowledgeBases().get(distance - 1);
						List<KnowledgeLink> kls = null;
						String symbol = "";
						boolean add = true;
						for (ModuleSymbol ms: activeSymbols) {
							if (forward) {
								kls = kb.getLinksBySource().get(ms.symbol);
							} else {
								kls = kb.getLinksByTarget().get(ms.symbol);
							}
							if (kls!=null) {
								for (KnowledgeLink kl: kls) {
									if (forward) {
										symbol = kl.target;
									} else {
										symbol = kl.source;
									}
									add = true;
									if (confabulatedSymbols.size()>0) {
										add = false;
										for (ModuleSymbol cms: confabulatedSymbols) {
											if (cms.symbol.equals(symbol)) {
												add = true;
												break;
											}
										}
									}
									if (add && contextLinks.size()>0) {
										add = false;
										for (FireLink ckl: contextLinks) {
											if (ckl.target.equals(symbol)) {
												add = true;
												break;
											}
										}
									}
									if (add) {
										fireLinks.add(new FireLink(kl, cMod, forward, ms.excitation));
									}
								}
							}
						}
					}
				}
			} else {
				forward = false;
			}
		}
	}

	private List<Module> fireLinks(List<FireLink> links) {
		List<Module> mods = new ArrayList<Module>();
		for (FireLink fl: links) {
			fl.module.fireLink(fl);
			if (!mods.contains(fl.module)) {
				mods.add(fl.module);
			}
		}
		return mods;
	}

	private void contractModules(List<Module> mods) {
		List<ModuleSymbol> activeSymbols = null;
		for (Module mod: mods) {
			activeSymbols = mod.getActiveSymbols();
			if (activeSymbols.size()>1) {
				mod.normalize(activeSymbols,kbs.getB(),kbs.getP0());
			}
			if (activeSymbols.size()==1) {
				mod.setLocked(true);
			}
		}
	}
	*/

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

	private void intitializeModules() {
		allSequenceModules.clear();
		prefix.clear();
		for (int i = 0; i<(kbs.getModules() - 1); i++) {
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
		int s = 0;
		for (Module sMod: allSequenceModules) {
			kbws.add(new KnowledgeBaseWorker(getMessenger(),uni,kbs.getContext(),context,sMod,true));
			kbws.add(new KnowledgeBaseWorker(getMessenger(),uni,kbs.getContext(),context,sMod,false));
			int m = s + (kbs.getModules() - 1);
			int kb = 0;
			for (int i = (s + 1); i < m; i++) {
				if (i<allSequenceModules.size()) {
					kbws.add(new KnowledgeBaseWorker(getMessenger(),uni,kbs.getKnowledgeBases().get(kb),sMod,allSequenceModules.get(i),true));
					kbws.add(new KnowledgeBaseWorker(getMessenger(),uni,kbs.getKnowledgeBases().get(kb),sMod,allSequenceModules.get(i),false));
				} else {
					break;
				}
			}
			s++;
		}
	}
}
