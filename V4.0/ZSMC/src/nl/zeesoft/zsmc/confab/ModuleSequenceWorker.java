package nl.zeesoft.zsmc.confab;

import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsmc.kb.KbContext;
import nl.zeesoft.zsmc.kb.KbLink;
import nl.zeesoft.zsmc.kb.KnowledgeBase;

public class ModuleSequenceWorker extends Worker {
	private KnowledgeBase	kb				= null;
	private Module			from			= null;
	private Module			to				= null;
	private int				distance		= 0;
	private String			contextSymbol	= "";
	private boolean			caseSensitive	= true;
	private KbContext		context			= null;
	
	public ModuleSequenceWorker(Messenger msgr, WorkerUnion union,KnowledgeBase kb,Module from,Module to,int distance,String contextSymbol,boolean caseSensitive) {
		super(msgr, union);
		this.kb = kb;
		this.from = from;
		this.to = to;
		this.distance = distance;
		this.contextSymbol = contextSymbol;
		this.caseSensitive = caseSensitive;
		context = kb.getContext(contextSymbol);
		setSleep(0);
	}

	@Override
	public void whileWorking() {
		if (!from.isLocked() || !to.isLocked()) {
			List<ModuleSymbol> modSymsFrom = from.getActiveSymbols();
			List<ModuleSymbol> modSymsTo = to.getActiveSymbols();
			if (modSymsFrom.size()>0 && modSymsTo.size()>0) {
				for (ModuleSymbol modSymFrom: modSymsFrom) {
					for (ModuleSymbol modSymTo: modSymsTo) {
						List<KbLink> lnks = kb.getLinks(modSymFrom.symbol,distance,contextSymbol,modSymTo.symbol,caseSensitive);
						for (KbLink lnk: lnks) {
							from.exciteSymbol(lnk.symbolFrom,((context.linkMaxProb - lnk.prob) * modSymTo.probNormalized));
							to.exciteSymbol(lnk.symbolTo,((context.linkMaxProb - lnk.prob) * modSymFrom.probNormalized));
						}
					}
				}
			} else if (modSymsFrom.size()>0 && !to.isLocked()) {
				for (ModuleSymbol modSymFrom: modSymsFrom) {
					List<KbLink> lnks = kb.getLinks(modSymFrom.symbol,distance,contextSymbol,"",caseSensitive);
					for (KbLink lnk: lnks) {
						to.exciteSymbol(lnk.symbolTo,((context.linkMaxProb - lnk.prob) * modSymFrom.probNormalized));
					}
				}
			} else if (modSymsTo.size()>0 && !from.isLocked()) {
				for (ModuleSymbol modSymTo: modSymsTo) {
					List<KbLink> lnks = kb.getLinks("",distance,contextSymbol,modSymTo.symbol,caseSensitive);
					for (KbLink lnk: lnks) {
						from.exciteSymbol(lnk.symbolFrom,((context.linkMaxProb - lnk.prob) * modSymTo.probNormalized));
					}
				}
			}
		} else {
			stop();
		}
	}
	
}
