package nl.zeesoft.zsd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.sequence.SequenceClassifierResult;
import nl.zeesoft.zsd.sequence.SequenceAnalyzerSymbolLink;

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
		return classify(sequence,false,getLinkContextBandwidth(""),0D);
	}

	/**
	 * Returns the context or an empty string for a certain sequence.
	 * 
	 * @param sequence The sequence
	 * @param caseInsensitive Indicates the case differences will be ignored as much as possible
	 * @return The context or an empty string
	 */
	public String classify(ZStringSymbolParser sequence, boolean caseInsensitive) {
		return classify(sequence,caseInsensitive,getLinkContextBandwidth(""),0D);
	}

	/**
	 * Returns the context or an empty string for a certain sequence.
	 * 
	 * @param sequence The sequence
	 * @param caseInsensitive Indicates the case differences will be ignored as much as possible
	 * @param bandwidth The bandwidth used as a minimal symbol link probability addition
	 * @param threshold If greater than 0D then normalize result probabilities to 1.0D and return everything greater than threshold 
	 * @return The context or an empty string
	 */
	public String classify(ZStringSymbolParser sequence, boolean caseInsensitive,double bandwidth, double threshold) {
		String r = "";
		List<SequenceClassifierResult> list = getContexts(sequence,caseInsensitive,bandwidth,threshold);
		if (list.size()>0) {
			r = list.get(0).symbol;
		}
		return r;
	}

	/**
	 * Returns a list of all contexts with calculated probabilities for a certain sequence.
	 * 
	 * @param sequence The sequence
	 * @param caseInsensitive Indicates the case differences will be ignored as much as possible
	 * @param threshold If greater than 0D then normalize result probabilities to 1.0D and return everything greater than threshold 
	 * @return A list of all contexts with calculated probabilities
	 */
	public List<SequenceClassifierResult> getContexts(ZStringSymbolParser sequence, boolean caseInsensitive, double threshold) {
		return getContexts(sequence,caseInsensitive,getLinkContextBandwidth(""),threshold);
	}

	/**
	 * Returns a list of all contexts with calculated probabilities for a certain sequence.
	 * 
	 * @param sequence The sequence
	 * @param caseInsensitive Indicates the case differences will be ignored as much as possible
	 * @param bandwidth The bandwidth used as a minimal symbol link probability addition
	 * @param threshold If greater than 0D then normalize result probabilities to 1.0D and return everything greater than threshold 
	 * @return A list of all contexts with calculated probabilities
	 */
	public List<SequenceClassifierResult> getContexts(ZStringSymbolParser sequence, boolean caseInsensitive,double bandwidth, double threshold) {
		List<SequenceClassifierResult> r = new ArrayList<SequenceClassifierResult>();
		SortedMap<String,SequenceClassifierResult> list = new TreeMap<String,SequenceClassifierResult>();
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
					SequenceClassifierResult res = list.get(link.context);
					if (res==null) {
						res = new SequenceClassifierResult();
						res.symbol = link.context;
						list.put(link.context,res);
						r.add(res);
					}
					res.prob += bandwidth;
					res.prob += (getLinkContextMaxProbs().get(link.context) - link.prob);
					if (res.prob>highest) {
						highest = res.prob;
					}
				}
			}
			i++;
		}
		if (r.size()>0) {
			SortedMap<Double,List<SequenceClassifierResult>> sort = new TreeMap<Double,List<SequenceClassifierResult>>();
			for (SequenceClassifierResult res: r) {
				res.probThreshold = res.prob / highest ;
				if (threshold==0D || res.probThreshold>=threshold) {
					List<SequenceClassifierResult> l = sort.get(res.prob);
					if (l==null) {
						l = new ArrayList<SequenceClassifierResult>();
						sort.put(res.prob,l);
					}
					l.add(0,res);
				}
			}
			r.clear();
			for (Entry<Double,List<SequenceClassifierResult>> entry: sort.entrySet()) {
				for (SequenceClassifierResult res: entry.getValue()) {
					r.add(0,res);
				}
			}
		}
		return r;
	}
	
	/**
	 * Returns the string with the first character converted to upper case.
	 * 
	 * @param str The string
	 * @return The string with the first character converted to upper case
	 */
	public String upperCaseFirst(String str) {
		String r = str.substring(0,1).toUpperCase();
		if (str.length()>1) {
			r += str.substring(1).toLowerCase();
		}
		return r;
	}
}
