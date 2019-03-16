package nl.zeesoft.zsmc.confab.confabs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.ZIntegerGenerator;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsmc.confab.Module;
import nl.zeesoft.zsmc.confab.ModuleSymbol;
import nl.zeesoft.zsmc.confab.ModuleWorker;
import nl.zeesoft.zsmc.kb.KbContext;
import nl.zeesoft.zsmc.kb.KbLink;
import nl.zeesoft.zsmc.kb.KnowledgeBase;

public abstract class ConfabulationObject {	
	public ZStringSymbolParser			input			= new ZStringSymbolParser();
	public boolean						caseSensitive	= false;
	public long							maxTime			= 1000;
	public boolean						appendLog		= false;
	public double						noise			= 0D;

	public Messenger					messenger		= null;
	public WorkerUnion					union			= null;
	public KnowledgeBase				kb				= null;
	
	public ZStringBuilder				log				= new ZStringBuilder();
	public Date							started			= null;
	public List<String>					symbols			= null;
	public List<Module>					modules 		= new ArrayList<Module>();
	public List<ModuleWorker>			workers			= new ArrayList<ModuleWorker>();

	private ZIntegerGenerator			generator		= new ZIntegerGenerator(1,100);

	public void initialize(Messenger msgr, WorkerUnion uni,KnowledgeBase kb) {
		messenger = msgr;
		union = uni;
		this.kb = kb;
		started = new Date();
		symbols = input.toSymbolsPunctuated();
	}
	
	public void finalize() {
		modules.clear();
		workers.clear();
	}
	
	public List<Module> copyModules() {
		List<Module> r = new ArrayList<Module>();
		for (Module mod: modules) {
			r.add(mod.copy());
		}
		return r;
	}

	public void confabulate() {
		for (Worker worker: workers) {
			worker.start();
		}
	}

	public boolean isConfabulating() {
		boolean r = false;
		for (ModuleWorker worker: workers) {
			if (!worker.isDone()) {
				r = true;
				break;
			}
		}
		return r;
	}
	
	public void addLogLine(String line) {
		addLogLine(new ZStringBuilder(line));
	}
	
	public void addLogLine(ZStringBuilder line) {
		if (appendLog) {
			log.append((new ZDate()).getTimeString(true));
			log.append(": ");
			log.append(line);
			log.append("\n");
		}
	}

	public void logModuleStateNoLock(String logLine) {
		if (appendLog) {
			ZStringBuilder line = new ZStringBuilder(logLine);
			line.append(";");
			line.append(getDebugModuleSymbolsNoLock());
			addLogLine(line);
		}
	}
	
	protected ZStringBuilder getDebugModuleSymbolsNoLock() {
		ZStringBuilder r = new ZStringBuilder("");
		int m = 0;
		for (Module mod: modules) {
			m++;
			r.append("\n  ");
			r.append(String.format("%02d",m) + ":");
			List<ModuleSymbol> syms = mod.getActiveSymbols();
			int s = 0;
			for (ModuleSymbol modSym: syms) {
				r.append(" ");
				r.append(modSym.symbol);
				r.append(":");
				r.append("" + modSym.prob);
				s++;
				if (s>=4) {
					if (syms.size()>s) {
						r.append(" [");
						r.append("" + (syms.size() - s));
						r.append("]");
					}
					break;
				}
			}
		}
		return r;
	}
	
	protected int initializeModules(String contextSymbol) {
		int fired = 0;
		KbContext context = kb.getContext(contextSymbol);
		for (int m = 0; m < modules.size(); m++) {
			Module mod = modules.get(m);
			if (!mod.isLocked()) {
				fired += getAndFireLinksInModule(m,context);
				mod.normalize(false);
			}
		}
		return fired;
	}
	
	public void limitLinksInModule(Module mod,ModuleSymbol sourceSymbol,KbContext context) {
		List<ModuleSymbol> modSymsComp = mod.getActiveSymbols();
		for (ModuleSymbol modSym: modSymsComp) {
			List<KbLink> lnks = kb.getLinks(sourceSymbol.symbol,1,context.contextSymbol,modSym.symbol,caseSensitive);
			if (lnks.size()==0) {
				mod.supressSymbol(modSym.symbol);
			}
		}
	}
	
	public int getAndFireLinksInModule(int moduleIndex,KbContext context) {
		int fired = 0;
		Module module = modules.get(moduleIndex);
		int start = moduleIndex - kb.getMaxDistance();
		int end = moduleIndex + kb.getMaxDistance();
		if (start<0) {
			start = 0;
		}
		if (end>modules.size()) {
			end = modules.size();
		}
		List<ModuleSymbol> modSyms = module.getActiveSymbols();
		for (int i = start; i<end; i++) {
			if (i!=moduleIndex) {
				int distance = 0;
				if (i<moduleIndex) {
					distance = moduleIndex - i;
				} else {
					distance = i - moduleIndex;
				}
				List<ModuleSymbol> modSymsComp = modules.get(i).getActiveSymbols();
				if (modSyms.size()>0) {
					for (ModuleSymbol modSym: modSyms) {
						fired += getAndFireLinks(module,modSymsComp,(i<moduleIndex),distance,modSym.symbol,context);
					}
				} else {
					fired += getAndFireLinks(module,modSymsComp,(i<moduleIndex),distance,"",context);
				}
			}
		}
		return fired;
	}

	private int getAndFireLinks(Module module,List<ModuleSymbol> sourceSymbols,boolean from,int distance,String targetSymbol,KbContext context) {
		int fired = 0;
		for (ModuleSymbol sourceSymbol: sourceSymbols) {
			List<KbLink> lnks = null;
			if (from) {
				lnks = kb.getLinks(sourceSymbol.symbol,distance,context.contextSymbol,targetSymbol,caseSensitive);
			} else {
				lnks = kb.getLinks(targetSymbol,distance,context.contextSymbol,sourceSymbol.symbol,caseSensitive);
			}
			for (KbLink lnk: lnks) {
				double prob = getFireProb(sourceSymbol,lnk,context);
				if (from) {
					module.exciteSymbol(lnk.symbolTo,prob);
				} else {
					module.exciteSymbol(lnk.symbolFrom,prob);
				}
				fired++;
			}
		}
		return fired;
	}
	
	private double getFireProb(ModuleSymbol sourceSymbol,KbLink lnk,KbContext context) {
		double prob = ((context.linkBandwidth + (context.linkMaxProb - lnk.prob)) * sourceSymbol.probNormalized);
		if (noise>0D) {
			double mult = (noise / 100D) * (double)generator.getNewInteger();
			prob += (prob * mult);
		}
		return prob;
	}
}
