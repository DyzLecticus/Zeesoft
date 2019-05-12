package nl.zeesoft.zsmc.confab;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsmc.kb.KbContext;
import nl.zeesoft.zsmc.kb.KnowledgeBase;

public class CorrectionConfabulation extends ConfabulationObject {
	public String				contextSymbol	= "";
	public String				alphabet		= "";
	
	public ZStringSymbolParser	corrected		= new ZStringSymbolParser();
	public List<Correction>		corrections		= new ArrayList<Correction>();
	
	private KbContext			context			= null;
	private SymbolCorrector		corrector		= new SymbolCorrector();
	
	@Override
	protected void initialize(Messenger msgr, WorkerUnion uni, KnowledgeBase kb) {		
		super.initialize(msgr,uni,kb);
		context = kb.getContext(contextSymbol);
		if (context==null) {
			context = kb.getContext("");
		}
		for (int m = 0; m < symbols.size(); m++) {
			Module mod = new Module(msgr);
			modules.add(mod);
		}
		for (int m = 0; m < modules.size(); m++) {
			workers.add(new ModuleSequenceWorker(this,m,context));
		}
		int unknown = 0;
		int m = 0;
		for (String symbol: symbols) {
			if (context.knownSymbols.contains(symbol)) {
				Module mod = modules.get(m);
				mod.setActiveSymbol(symbol);
				mod.setLocked(true);
			} else {
				unknown++;
			}
			m++;
		}
		if (unknown > 0) {
			m = 0;
			int u = 0;
			long uMs = (((maxTime / 3) * 2) / unknown);
			for (String symbol: symbols) {
				if (!context.knownSymbols.contains(symbol)) {
					u++;
					long stopAfterMs = uMs * u;
					Module mod = modules.get(m);
					List<String> syms = corrector.getVariations(symbol,context.knownSymbols,started,stopAfterMs,alphabet);
					if (syms.size()==1) {
						mod.setActiveSymbol(syms.get(0));
						mod.setLocked(true);
					} else {
						for (String sym: syms) {
							mod.exciteSymbol(sym,context.linkBandwidth);
						}
					}
				}
				m++;
			}
		}
		initializeModules(context);
		logModuleStateNoLock("Initialized modules");
	}
	
	@Override
	protected void finalize() {
		List<String> syms = new ArrayList<String>();
		int i = 0;
		for (Module mod: modules) {
			String oriSym = symbols.get(i);
			if (mod.isLocked()) {
				List<ModuleSymbol> modSyms = mod.getActiveSymbolsNormalized();
				if (modSyms.size()>0) {
					String corSym = modSyms.get(0).symbol;
					syms.add(corSym);
					if (!context.knownSymbols.contains(oriSym)) {
						Correction cor = new Correction();
						cor.index = i;
						cor.symbol = oriSym;
						cor.correction = corSym;
						corrections.add(cor);
					}
				}
			} else {
				syms.add(oriSym);
			}
			i++;
		}
		corrected.fromSymbols(syms,false,true);
		addLogLine("Finalized; " + corrected);
		super.finalize();
	}
}
