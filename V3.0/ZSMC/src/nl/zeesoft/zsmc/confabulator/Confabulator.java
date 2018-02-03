package nl.zeesoft.zsmc.confabulator;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsmc.SpellingChecker;

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
		
	}
	
	private void initializeConclusions(Confabulation confab) {
		List<String> symbols = confab.contextSymbols.toSymbolsPunctuated();
		List<ModuleSymbol> conclusions = new ArrayList<ModuleSymbol>();
		for (String symbol: symbols) {
			ModuleSymbol sym = new ModuleSymbol();
			sym.symbol = symbol;
			sym.excitation = 1.0D;
			conclusions.add(sym);
		}
		context.setConclusions(conclusions);
		
		symbols = confab.inputSymbols.toSymbolsPunctuated();
		conclusions = new ArrayList<ModuleSymbol>();
		/*
		for (String symbol: symbols) {
			ModuleSymbol sym = new ModuleSymbol();
			sym.symbol = symbol;
			sym.excitation = 1.0D;
			conclusions.add(sym);
		}
		context.setConclusions(conclusions);
		*/
	}
}
