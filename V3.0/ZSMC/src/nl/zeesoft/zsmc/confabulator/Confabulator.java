package nl.zeesoft.zsmc.confabulator;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsmc.SpellingChecker;
import nl.zeesoft.zsmc.sequence.AnalyzerSymbol;

public class Confabulator extends Locker {
	private KnowledgeBases		kbs			= null;
	
	private List<Module>		prefix		= new ArrayList<Module>();
	private List<Module>		modules		= new ArrayList<Module>();
	private Module				context		= null;
	
	private SpellingChecker		sc			= null;
	
	public Confabulator(Messenger msgr, WorkerUnion uni,KnowledgeBases kbs) {
		super(msgr);
		this.kbs = kbs;
		sc = new SpellingChecker();
		sc.setKnownSymbols(kbs.getKnownSymbols());
		sc.setTotalSymbols(kbs.getTotalSymbols());
	}
	
	public void intitializeModules() {
		prefix.clear();
		for (int i = 0; i<(kbs.getModules() - 1); i++) {
			prefix.add(new Module(kbs,false));
		}
		modules.clear();
		for (int i = 0; i<kbs.getModules(); i++) {
			modules.add(new Module(kbs,false));
		}
		context = new Module(kbs,true);
	}
	
	public void confabulate(Confabulation confab) {
		initializeConclusions(confab);
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
		}
		
		// Clear modules
		for (Module mod: modules) {
			mod.setConclusion("");
		}
		
		logModuleStates(confab,"Initial module states;");
	}
	
	private void logModuleStates(Confabulation confab,String message) {
		ZStringBuilder append = new ZStringBuilder(message);
		append.append("\n");
		append.append(logModuleState(confab,context,"Context"));
		for (int i = 0; i<prefix.size(); i++) {
			append.append(logModuleState(confab,prefix.get(i),"Prefix " + (i + 1)));
		}
		for (int i = 0; i<modules.size(); i++) {
			append.append(logModuleState(confab,modules.get(i),"Module " + (i + 1)));
		}
		confab.appendLog(append);
	}
	
	private ZStringBuilder logModuleState(Confabulation confab, Module mod, String moduleName) {
		ZStringBuilder append = new ZStringBuilder();
		append.append(moduleName);
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
}
