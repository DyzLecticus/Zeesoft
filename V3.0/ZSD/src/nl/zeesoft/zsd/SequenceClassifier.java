package nl.zeesoft.zsd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.sequence.AnalyzerSymbol;
import nl.zeesoft.zsd.sequence.SequenceAnalyzerSymbolLink;
import nl.zeesoft.zsd.sequence.SequenceClassifierResult;

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
		if (symbols.size()==1) {
			List<AnalyzerSymbol> asl = getKnownSymbols(symbols.get(0),caseInsensitive);
			for (AnalyzerSymbol as: asl) {
				if (as.context.length()>0) {
					SequenceClassifierResult res = addOrUpdateResult(r,list,as.context,bandwidth,as.prob,false);
					if (res.prob>highest) {
						highest = res.prob;
					}
				}
			}
		} else {
			for (String symbol: symbols) {
				String to = "";
				if (symbols.size()>(i + 1)) {
					to = symbols.get(i + 1);
					List<SequenceAnalyzerSymbolLink> links = getLinksByFromTo(symbol,to,caseInsensitive);
					for (SequenceAnalyzerSymbolLink link: links) {
						if (link.context.length()>0) {
							SequenceClassifierResult res = addOrUpdateResult(r,list,link.context,bandwidth,link.prob,true);
							if (res.prob>highest) {
								highest = res.prob;
							}
						}
					}
				}
				i++;
			}
		}
		if (r.size()>0) {
			SortedMap<Double,List<SequenceClassifierResult>> sort = new TreeMap<Double,List<SequenceClassifierResult>>();
			for (SequenceClassifierResult res: r) {
				res.probNormalized = res.prob / highest ;
				if (threshold==0D || res.probNormalized>=threshold) {
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
	
	private SequenceClassifierResult addOrUpdateResult(List<SequenceClassifierResult> r,SortedMap<String,SequenceClassifierResult> list,String context,double bandwidth,double prob,boolean link) {
		SequenceClassifierResult res = list.get(context);
		if (res==null) {
			res = new SequenceClassifierResult();
			res.symbol = context;
			list.put(context,res);
			r.add(res);
		}
		res.prob += bandwidth;
		if (link) {
			res.prob += (getLinkContextMaxProbs().get(context) - prob);
		} else {
			res.prob += (getSymbolContextMaxProbs().get(context) - prob);
		}
		return res;
	}
}
