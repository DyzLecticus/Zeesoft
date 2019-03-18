package nl.zeesoft.zsmc.confab.confabs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;

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
	public boolean						strict			= true;
	public String						unknownSymbol	= "[?]";

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
				mod.normalize();
			}
		}
		return fired;
	}
	
	public void limitLinksInModule(Module mod,ModuleSymbol sourceSymbol,int distance,KbContext context) {
		List<ModuleSymbol> modSymsComp = mod.getActiveSymbolsNormalized();
		List<String> exceptions = new ArrayList<String>();
		for (ModuleSymbol modSym: modSymsComp) {
			List<KbLink> lnks = kb.getLinks(sourceSymbol.symbol,distance,context.contextSymbol,modSym.symbol,caseSensitive);
			if (lnks.size()>0) {
				exceptions.add(modSym.symbol);
			}
		}
		mod.supressSymbolsExcept(exceptions);
	}

	public int getAndFireLinksInContextModule(int symbolIndex,Module contextModule,SortedMap<String,KbContext> contexts) {
		int fired = 0;
		String symbolFrom = symbols.get(symbolIndex);
		for (int d = 1; d <= kb.getMaxDistance(); d++) {
			int i = symbolIndex + d;
			if (i < symbols.size()) {
				String symbolTo = symbols.get(i);
				List<KbLink> lnks = kb.getLinks(symbolFrom,d,null,symbolTo,caseSensitive);
				for (KbLink lnk: lnks) {
					if (lnk.context.length()>0) {
						KbContext context = contexts.get(lnk.context);
						double prob = getFireProb(lnk,context);
						contextModule.exciteSymbol(lnk.context,prob);
						fired++;
					}
				}
			} else {
				break;
			}
		}
		return fired;
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
		List<ModuleSymbol> modSyms = module.getActiveSymbolsNormalized();
		for (int i = start; i<end; i++) {
			if (i!=moduleIndex) {
				int distance = 0;
				if (i<moduleIndex) {
					distance = moduleIndex - i;
				} else {
					distance = i - moduleIndex;
				}
				List<ModuleSymbol> modSymsComp = modules.get(i).getActiveSymbolsNormalized();
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

	private double getFireProb(KbLink lnk,KbContext context) {
		return getFireProb(1D,lnk,context);
	}
	
	private double getFireProb(ModuleSymbol sourceSymbol,KbLink lnk,KbContext context) {
		return getFireProb(sourceSymbol.probNormalized,lnk,context);
	}

	private double getFireProb(double sourceProb,KbLink lnk,KbContext context) {
		double prob = ((context.linkBandwidth + (context.linkMaxProb - lnk.prob)) * sourceProb);
		if (noise>0D) {
			double mult = (noise / 100D) * (double)generator.getNewInteger();
			prob += (prob * mult);
		}
		return prob;
	}
}
