package nl.zeesoft.zsd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.sequence.AnalyzerSymbol;
import nl.zeesoft.zsd.sequence.SequenceAnalyzerSymbolLink;
import nl.zeesoft.zsd.sequence.SequenceMatcherResult;
import nl.zeesoft.zsd.sequence.SequenceMatcherSequence;

/**
 * A SequenceMatcher can be used to find a context sensitive match for a certain sequence.
 */
public class SequenceMatcher extends SequenceClassifier {
	private boolean												forceUnique		= false;
	
	private SortedMap<String,List<SequenceMatcherSequence>>		knownSequences	= new TreeMap<String,List<SequenceMatcherSequence>>();

	/**
	 * Indicates sequence will be forced to be unique upon addition (default false)
	 * 
	 * When set to true, a check to see if the sequence already exists is done when adding new sequences.
	 * 
	 * @param forceUnique Indicates sequence will be forced to be unique upon addition
	 */
	public void setForceUnique(boolean forceUnique) {
		this.forceUnique = forceUnique;
	}

	/**
	 * Returns the match or null for a certain input sequence (case sensitive).
	 *  
	 * @param sequence The sequence
	 * @return The match or null
	 */
	public ZStringSymbolParser match(ZStringSymbolParser sequence) {
		return match(sequence,"",false);
	}

	/**
	 * Returns the match or null for a certain input sequence.
	 *  
	 * @param sequence The sequence
	 * @param caseInsensitive Indicates the case differences will be ignored as much as possible
	 * @return The match or null
	 */
	public ZStringSymbolParser match(ZStringSymbolParser sequence,boolean caseInsensitive) {
		return match(sequence,"",caseInsensitive);
	}


	/**
	 * Returns the match or null for a certain input sequence (case sensitive).
	 *  
	 * @param sequence The sequence
	 * @param context The optional context symbol
	 * @return The match or null
	 */
	public ZStringSymbolParser match(ZStringSymbolParser sequence,String context) {
		return match(sequence,context,false);
	}

	/**
	 * Returns the match or null for a certain input sequence.
	 * 
	 * @param sequence The sequence
	 * @param context The optional context symbol
	 * @param caseInsensitive Indicates the case differences will be ignored as much as possible
	 * @return The match or null
	 */
	public ZStringSymbolParser match(ZStringSymbolParser sequence,String context, boolean caseInsensitive) {
		ZStringSymbolParser r = null;
		List<SequenceMatcherResult> list = getMatches(sequence,context,caseInsensitive);
		if (list.size()>0) {
			r = list.get(0).result.sequence;
		}
		return r;
	}

	/**
	 * Returns a list of matching results for a certain input sequence.
	 * 
	 * @param sequence The sequence
	 * @param context The optional context symbol
	 * @param caseInsensitive Indicates the case differences will be ignored as much as possible
	 * @return A list of matching results for a certain input sequence
	 */
	public List<SequenceMatcherResult> getMatches(ZStringSymbolParser sequence,String context, boolean caseInsensitive) {
		return getMatches(sequence,context,caseInsensitive,getLinkContextBandwidth(context),0D);
	}

	/**
	 * Returns a list of matching results for a certain input sequence.
	 * 
	 * @param sequence The sequence
	 * @param context The optional context symbol
	 * @param caseInsensitive Indicates the case differences will be ignored as much as possible
	 * @param threshold If greater than 0D then normalize result probabilities to 1.0D and return everything greater than threshold 
	 * @return A list of matching results for a certain input sequence
	 */
	public List<SequenceMatcherResult> getMatches(ZStringSymbolParser sequence,String context, boolean caseInsensitive, double threshold) {
		return getMatches(sequence,context,caseInsensitive,getLinkContextBandwidth(context),threshold);
	}
	
	/**
	 * Returns a list of matching results for a certain input sequence.
	 * 
	 * @param sequence The sequence
	 * @param context The optional context symbol
	 * @param caseInsensitive Indicates the case differences will be ignored as much as possible
	 * @param bandwidth The bandwidth used as a minimal symbol link probability addition
	 * @param threshold If greater than 0D then normalize result probabilities to 1.0D and return everything greater than threshold 
	 * @return A list of matching results for a certain input sequence
	 */
	public List<SequenceMatcherResult> getMatches(ZStringSymbolParser sequence,String context, boolean caseInsensitive, double bandwidth, double threshold) {
		List<SequenceMatcherResult> r = new ArrayList<SequenceMatcherResult>();
		SequenceMatcherSequence match = getSequenceMatcherSequenceForSequence(sequence,context,caseInsensitive);
		SortedMap<String,AnalyzerSymbol> matchSymbols = new TreeMap<String,AnalyzerSymbol>();
		double highest = 0D;
		for (String symbol: match.symbols) {
			List<AnalyzerSymbol> asl = getKnownSymbols(symbol, context, caseInsensitive);
			for (AnalyzerSymbol as: asl) {
				if (!matchSymbols.containsKey(symbol)) {
					matchSymbols.put(symbol,as);
				}
			}
		}
		if (matchSymbols.size()>0) {
			List<String> unlinkedSymbols = getUnlinkedSymbolsForSequenceSymbols(match.symbols,context,caseInsensitive);
			List<SequenceMatcherSequence> list = knownSequences.get(context);
			for (SequenceMatcherSequence seq: list) {
				int conseq = 1;
				double prob = 0D;
				String pSymbolTo = "";
				for (SequenceAnalyzerSymbolLink link: match.links) {
					boolean found = false;
					boolean foundCase = false;
					for (SequenceAnalyzerSymbolLink comp: seq.links) {
						if (link.context.equals(comp.context) &&
							(link.symbolFrom.equals(comp.symbolFrom) && link.symbolTo.equals(comp.symbolTo)) ||
							(caseInsensitive && link.symbolFrom.equalsIgnoreCase(comp.symbolFrom) && link.symbolTo.equalsIgnoreCase(comp.symbolTo))
							) {
							found = true;
							if (link.symbolFrom.equals(comp.symbolFrom) && link.symbolTo.equals(comp.symbolTo)) {
								foundCase = true;
							}
							break;
						}
					}
					if (found) {
						prob += ((bandwidth + (getLinkContextMaxProbs().get(link.context) - link.prob)) * (double)conseq);
						if (foundCase) {
							if (conseq==1) {
								prob += (getSymbolContextMaxProbs().get(link.context) - link.asFrom.prob);
							}
							prob += (getSymbolContextMaxProbs().get(link.context) - link.asTo.prob);
						}
						if (pSymbolTo.length()==0 || pSymbolTo.equals(link.symbolFrom) || (caseInsensitive && pSymbolTo.equalsIgnoreCase(link.symbolFrom))) {
							conseq++;
						} else {
							conseq = 1;
						}
						pSymbolTo = link.symbolTo;
					} else {
						conseq = 0;
						pSymbolTo = "";
					}
				}
				for (String symbol: unlinkedSymbols) {
					if (!ZStringSymbolParser.isLineEndSymbol(symbol)) {
						for (String comp: seq.symbols) {
							if (symbol.equals(comp) || (caseInsensitive && symbol.equalsIgnoreCase(comp))) {
								AnalyzerSymbol as = matchSymbols.get(symbol);
								if (as!=null) {
									prob += bandwidth;
									prob += (getSymbolContextMaxProbs().get(as.context) - as.prob);
								}
							}
						}
					}
				}
				if (prob>0D) {
					SequenceMatcherResult res = new SequenceMatcherResult();
					res.result = seq.copy();
					res.prob = prob;
					if (res.prob>highest) {
						highest = res.prob;
					}
					r.add(res);
				}
			}
		}
		if (r.size()>0) {
			SortedMap<Double,List<SequenceMatcherResult>> sort = new TreeMap<Double,List<SequenceMatcherResult>>();
			for (SequenceMatcherResult res: r) {
				res.probNormalized = res.prob / highest ;
				if (threshold==0D || res.probNormalized>=threshold) {
					List<SequenceMatcherResult> l = sort.get(res.prob);
					if (l==null) {
						l = new ArrayList<SequenceMatcherResult>();
						sort.put(res.prob,l);
					}
					l.add(0,res);
				}
			}
			r.clear();
			for (Entry<Double,List<SequenceMatcherResult>> entry: sort.entrySet()) {
				for (SequenceMatcherResult res: entry.getValue()) {
					r.add(0,res);
				}
			}
		}
		return r;
	}

	@Override
	public void addSymbols(List<String> symbols,List<String> contextSymbols) {
		super.addSymbols(symbols,contextSymbols);
		ZStringSymbolParser sequence = new ZStringSymbolParser();
		sequence.fromSymbols(symbols,true,true);
		for (String context: contextSymbols) {
			addSequence(sequence,context);
		}
	}

	public SortedMap<String, List<SequenceMatcherSequence>> getKnownSequences() {
		return knownSequences;
	}

	public void setKnownSequences(SortedMap<String, List<SequenceMatcherSequence>> knownSequences) {
		this.knownSequences = knownSequences;
	}

	private void addSequence(ZStringSymbolParser sequence,String context) {
		List<SequenceMatcherSequence> list = knownSequences.get(context);
		if (list==null) {
			list = new ArrayList<SequenceMatcherSequence>();
			knownSequences.put(context,list);
		}
		boolean found = false;
		if (forceUnique) {
			for (SequenceMatcherSequence seq: list) {
				if (seq.context.equals(context) && seq.sequence.equals(sequence)) {
					found = true;
					break;
				}
			}
		}
		if (!forceUnique || !found) {
			list.add(getSequenceMatcherSequenceForSequence(sequence,context,false));
		}
	}

	private void addSequenceLinks(List<SequenceAnalyzerSymbolLink> list,List<String> symbols,String context,boolean caseInsensitive)  {
		int i = 0;
		int pos = 0;
		for (String symbol: symbols) {
			if (symbols.size()>(i + 1)) {
				String to = symbols.get(i + 1);
				if (!symbol.equals(getIoSeparator()) && !to.equals(getIoSeparator())) {
					int l = 0;
					List<SequenceAnalyzerSymbolLink> links = getLinksByFromTo(symbol,to,context,caseInsensitive);
					for (SequenceAnalyzerSymbolLink link: links) {
						if (l==0 && link.symbolFrom.equals(symbol) && link.symbolTo.equals(to)) {
							list.add(pos,link);
							pos++;
						} else {
							list.add(link);
						}
						l++;
					}
				}
			}
			i++;
		}
	}

	private SequenceMatcherSequence getSequenceMatcherSequenceForSequence(ZStringSymbolParser sequence,String context,boolean caseInsensitive) {
		List<SequenceAnalyzerSymbolLink> addLinks = new ArrayList<SequenceAnalyzerSymbolLink>();
		List<String> syms = sequence.toSymbolsPunctuated();
		addSequenceLinks(addLinks,syms,context,caseInsensitive);
		List<SequenceAnalyzerSymbolLink> links = new ArrayList<SequenceAnalyzerSymbolLink>();
		for (SequenceAnalyzerSymbolLink link: addLinks) {
			links.add(link.copy());
		}
		List<String> symbols = new ArrayList<String>();
		for (String sym: syms) {
			if (!sym.equals(getIoSeparator())) {
				symbols.add(sym);
			}
		}
		SequenceMatcherSequence seq = new SequenceMatcherSequence();
		seq.context = context;
		seq.sequence = sequence;
		seq.symbols = symbols;
		seq.links = links;
		return seq;
	}
}
