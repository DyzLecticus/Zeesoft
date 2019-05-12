package nl.zeesoft.zsmc.confab;

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
import nl.zeesoft.zsmc.kb.KbContext;
import nl.zeesoft.zsmc.kb.KbLink;
import nl.zeesoft.zsmc.kb.KbSymbol;
import nl.zeesoft.zsmc.kb.KnowledgeBase;

public abstract class ConfabulationObject {	
	public ZStringSymbolParser			input			= new ZStringSymbolParser();
	public boolean						caseSensitive	= false;
	public long							maxTime			= 1000;
	public boolean						appendLog		= false;
	public double						threshold		= 0.3D;
	public double						noise			= 0D;
	public boolean						strict			= true;
	public String						unknownSymbol	= "[?]";

	protected Messenger					messenger		= null;
	protected WorkerUnion				union			= null;
	protected KnowledgeBase				kb				= null;
	
	public ZStringBuilder				log				= new ZStringBuilder();
	public Date							started			= null;
	public List<String>					symbols			= null;

	protected List<Module>				modules 		= new ArrayList<Module>();
	protected List<ModuleWorker>		workers			= new ArrayList<ModuleWorker>();

	private ZIntegerGenerator			generator		= new ZIntegerGenerator(1,100);

	protected void initialize(Messenger msgr,WorkerUnion uni,KnowledgeBase kb) {
		if (maxTime > 60000) {
			maxTime = 60000;
		}
		if (noise > 1D) {
			noise = 1D;
		}
		if (unknownSymbol.length()==0) {
			unknownSymbol = "[?]";
		}
		messenger = msgr;
		union = uni;
		this.kb = kb;
		started = new Date();
		symbols = input.toSymbolsPunctuated();
	}
	
	protected void finalize() {
		modules.clear();
		workers.clear();
	}

	protected void confabulate() {
		for (Worker worker: workers) {
			worker.start();
		}
	}

	protected boolean isConfabulating() {
		boolean r = false;
		for (ModuleWorker worker: workers) {
			if (!worker.isDone()) {
				r = true;
				break;
			}
		}
		return r;
	}
	
	protected void addLogLine(String line) {
		addLogLine(new ZStringBuilder(line));
	}
	
	protected void addLogLine(ZStringBuilder line) {
		if (appendLog) {
			log.append((new ZDate()).getTimeString(true));
			log.append(": ");
			log.append(line);
			log.append("\n");
		}
	}

	protected void logModuleStateNoLock(String logLine) {
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
	
	protected int initializeModules(KbContext context) {
		int fired = 0;
		for (int m = 0; m < modules.size(); m++) {
			Module mod = modules.get(m);
			if (!mod.isLocked()) {
				fired += getAndFireLinksInModule(m,context);
				mod.normalize(threshold);
			}
		}
		return fired;
	}
	
	protected void limitLinksInModule(Module mod,ModuleSymbol sourceSymbol,int distance,KbContext context) {
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

	protected int getAndFireSymbolsInContextModule(int symbolIndex,Module contextModule,SortedMap<String,KbContext> contexts) {
		int fired = 0;
		List<KbSymbol> syms = kb.getSymbols(symbols.get(symbolIndex), null, caseSensitive);
		for (KbSymbol sym: syms) {
			if (sym.context.length()>0) {
				KbContext context = contexts.get(sym.context);
				double prob = getFireProb(sym,context);
				contextModule.exciteSymbol(sym.context,prob);
				fired++;
			}
		}
		return fired;
	}

	protected int getAndFireLinksInContextModule(int symbolIndex,Module contextModule,SortedMap<String,KbContext> contexts) {
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

	protected int getAndFireLinksInModule(int moduleIndex,KbContext context) {
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

	private double getFireProb(KbSymbol sym,KbContext context) {
		return getFireProb(1D,sym.prob,context,true);
	}

	private double getFireProb(KbLink lnk,KbContext context) {
		return getFireProb(1D,lnk.prob,context,false);
	}

	private double getFireProb(ModuleSymbol sourceSymbol,KbLink lnk,KbContext context) {
		return getFireProb(sourceSymbol.probNormalized,lnk.prob,context,false);
	}

	private double getFireProb(double sourceProb,double kbProb,KbContext context,boolean symbol) {
		double prob = 0D;
		if (symbol) {
			prob = ((context.symbolBandwidth + (context.symbolMaxProb - kbProb)) * sourceProb);
		} else {
			prob = ((context.linkBandwidth + (context.linkMaxProb - kbProb)) * sourceProb);
		}
		if (noise>0D) {
			double mult = (noise / 100D) * (double)generator.getNewInteger();
			prob += (prob * mult);
		}
		return prob;
	}
}