package nl.zeesoft.zsmc;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsmc.sequence.AnalyzerSymbol;
import nl.zeesoft.zsmc.sequence.SequenceAnalyzerSymbolLink;

/**
 * A SequenceClassifier can be used to obtain the context of symbol sequences.
 */
public class SequenceClassifier extends SymbolCorrector {
	/**
	 * Returns the context or an empty string for a certain sequence.
	 * 
	 * @param sequence The sequence
	 * @return The context or an empty string
	 */
	public String classify(ZStringSymbolParser sequence) {
		String r = "";
		double highest = 0D;
		List<AnalyzerSymbol> list = getContexts(sequence);
		for (AnalyzerSymbol s: list) {
			if (s.prob>highest) {
				highest = s.prob;
				r = s.symbol;
			}
		}
		return r;
	}

	/**
	 * Returns a list of all contexts with calculated probabilities for a certain sequence.
	 * 
	 * @param sequence The sequence
	 * @return A list of all contexts with calculated probabilities
	 */
	public List<AnalyzerSymbol> getContexts(ZStringSymbolParser sequence) {
		List<AnalyzerSymbol> r = new ArrayList<AnalyzerSymbol>();
		SortedMap<String,AnalyzerSymbol> list = new TreeMap<String,AnalyzerSymbol>();
		List<String> symbols = sequence.toSymbolsPunctuated(); 
		int i = 0;
		for (String symbol: symbols) {
			String to = "";
			if (symbols.size()>(i + 1)) {
				to = symbols.get(i + 1);
			}
			List<SequenceAnalyzerSymbolLink> links = getLinksBySymbolFrom().get(symbol);
			for (SequenceAnalyzerSymbolLink link: links) {
				if (link.context.length()>0 && (link.symbolTo.equals(to) || symbols.size()==1)) {
					AnalyzerSymbol as = list.get(link.context);
					if (as==null) {
						as = new AnalyzerSymbol();
						as.symbol = link.context;
						list.put(link.context,as);
						r.add(as);
					}
					as.prob += (getMaxLinkProb() - link.prob);
					as.count++;
				}
			}
			i++;
		}
		return r;
	}
}
