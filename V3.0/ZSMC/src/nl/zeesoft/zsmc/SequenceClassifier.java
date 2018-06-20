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
	 * Returns the context or an empty string for a certain sequence (case sensitive).
	 * 
	 * @param sequence The sequence
	 * @return The context or an empty string
	 */
	public String classify(ZStringSymbolParser sequence) {
		return classify(sequence,false,getLinkBandwidth(),0D);
	}

	/**
	 * Returns the context or an empty string for a certain sequence.
	 * 
	 * @param sequence The sequence
	 * @param caseInsensitive Indicates the case differences will be ignored as much as possible
	 * @return The context or an empty string
	 */
	public String classify(ZStringSymbolParser sequence, boolean caseInsensitive) {
		String r = "";
		double highest = 0D;
		List<AnalyzerSymbol> list = getContexts(sequence,caseInsensitive,getLinkBandwidth(),0D);
		for (AnalyzerSymbol s: list) {
			if (s.prob>highest) {
				highest = s.prob;
				r = s.symbol;
			}
		}
		return r;
	}

	/**
	 * Returns the context or an empty string for a certain sequence.
	 * 
	 * @param sequence The sequence
	 * @param caseInsensitive Indicates the case differences will be ignored as much as possible
	 * @param bandwidth The bandwidth used as a minimal symbol link probability addition
	 * @param threshold If > 0D then normalize result probabilities to 1.0 and return everything greater than threshold 
	 * @return The context or an empty string
	 */
	public String classify(ZStringSymbolParser sequence, boolean caseInsensitive,double bandwidth, double threshold) {
		String r = "";
		double highest = 0D;
		List<AnalyzerSymbol> list = getContexts(sequence,caseInsensitive,bandwidth,threshold);
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
	 * @param caseInsensitive Indicates the case differences will be ignored as much as possible
	 * @param threshold If > 0D then normalize result probabilities to 1.0 and return everything greater than threshold 
	 * @return A list of all contexts with calculated probabilities
	 */
	public List<AnalyzerSymbol> getContexts(ZStringSymbolParser sequence, boolean caseInsensitive, double threshold) {
		return getContexts(sequence,caseInsensitive,getLinkBandwidth(),threshold);
	}

	/**
	 * Returns a list of all contexts with calculated probabilities for a certain sequence.
	 * 
	 * @param sequence The sequence
	 * @param caseInsensitive Indicates the case differences will be ignored as much as possible
	 * @param bandwidth The bandwidth used as a minimal symbol link probability addition
	 * @param threshold If > 0D then normalize result probabilities to 1.0 and return everything greater than threshold 
	 * @return A list of all contexts with calculated probabilities
	 */
	public List<AnalyzerSymbol> getContexts(ZStringSymbolParser sequence, boolean caseInsensitive,double bandwidth, double threshold) {
		List<AnalyzerSymbol> r = new ArrayList<AnalyzerSymbol>();
		SortedMap<String,AnalyzerSymbol> list = new TreeMap<String,AnalyzerSymbol>();
		List<String> symbols = sequence.toSymbolsPunctuated(); 
		int i = 0;
		double highest = 0D;
		for (String symbol: symbols) {
			String to = "";
			if (symbols.size()>(i + 1)) {
				to = symbols.get(i + 1);
			}
			List<SequenceAnalyzerSymbolLink> links = getLinksBySymbolFrom().get(symbol);
			if (links!=null) {
				links = new ArrayList<SequenceAnalyzerSymbolLink>(links);
			} else {
				links = new ArrayList<SequenceAnalyzerSymbolLink>();
			}
			if (caseInsensitive) {
				String cased = symbol.toLowerCase(); 
				if (!cased.equals(symbol)) {
					List<SequenceAnalyzerSymbolLink> linksAdd = getLinksBySymbolFrom().get(cased);
					if (linksAdd!=null) {
						for (SequenceAnalyzerSymbolLink link: linksAdd) {
							links.add(link);
						}
					}
				}
				cased = symbol.toUpperCase(); 
				if (!cased.equals(symbol)) {
					List<SequenceAnalyzerSymbolLink> linksAdd = getLinksBySymbolFrom().get(cased);
					if (linksAdd!=null) {
						for (SequenceAnalyzerSymbolLink link: linksAdd) {
							links.add(link);
						}
					}
				}
				cased = upperCaseFirst(symbol); 
				if (!cased.equals(symbol)) {
					List<SequenceAnalyzerSymbolLink> linksAdd = getLinksBySymbolFrom().get(cased);
					if (linksAdd!=null) {
						for (SequenceAnalyzerSymbolLink link: linksAdd) {
							links.add(link);
						}
					}
				}
			}
			for (SequenceAnalyzerSymbolLink link: links) {
				if (link.context.length()>0 && (
						symbols.size()==1 ||
						link.symbolTo.equals(to) || 
						(caseInsensitive && link.symbolTo.equalsIgnoreCase(to)) 
					)
					) {
					AnalyzerSymbol as = list.get(link.context);
					if (as==null) {
						as = new AnalyzerSymbol();
						as.symbol = link.context;
						list.put(link.context,as);
						r.add(as);
					}
					as.prob += bandwidth;
					as.prob += (getLinkMaxProb() - link.prob);
					if (as.prob>highest) {
						highest = as.prob;
					}
					as.count++;
				}
			}
			i++;
		}
		if (r.size()>0 && threshold>0D) {
			List<AnalyzerSymbol> test = new ArrayList<AnalyzerSymbol>(r);
			if (threshold>0D) {
				for (AnalyzerSymbol s: test) {
					s.prob = s.prob / highest ;
					if (s.prob<threshold) {
						r.remove(s);
					}
				}
			}
		}
		return r;
	}
	
	public String upperCaseFirst(String str) {
		String r = str.substring(0,1).toUpperCase();
		if (str.length()>1) {
			r += str.substring(1).toLowerCase();
		}
		return r;
	}
}
