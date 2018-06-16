package nl.zeesoft.zsmc;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsmc.sequence.AnalyzerSymbol;
import nl.zeesoft.zsmc.sequence.SequenceAnalyzerSymbolLink;
import nl.zeesoft.zsmc.sequence.SequenceMatcherResult;
import nl.zeesoft.zsmc.sequence.SequenceMatcherSequence;

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
	 * Returns the match or null for a certain input sequence.
	 *  
	 * @param sequence The sequence
	 * @return The match or null
	 */
	public ZStringSymbolParser match(ZStringSymbolParser sequence) {
		return match(sequence,"");
	}

	/**
	 * Returns the match or null for a certain input sequence.
	 * 
	 * @param sequence The sequence
	 * @param context The optional context symbol
	 * @return The match or null
	 */
	public ZStringSymbolParser match(ZStringSymbolParser sequence,String context) {
		ZStringSymbolParser r = null;
		double highest = 0D;
		List<SequenceMatcherResult> list = getMatches(sequence,context);
		for (SequenceMatcherResult s: list) {
			if (s.prob>highest) {
				highest = s.prob;
				r = s.result.sequence;
			}
		}
		return r;
	}
	
	/**
	 * Returns a list of matching results for a certain input sequence.
	 * 
	 * @param sequence The sequence
	 * @param context The optional context symbol
	 * @return A list of matching results for a certain input sequence
	 */
	public List<SequenceMatcherResult> getMatches(ZStringSymbolParser sequence,String context) {
		List<SequenceMatcherResult> r = new ArrayList<SequenceMatcherResult>();
		SequenceMatcherSequence match = getSequenceMatcherSequenceForSequence(sequence,context);
		SortedMap<String,AnalyzerSymbol> matchSymbols = new TreeMap<String,AnalyzerSymbol>();
		for (String symbol: match.symbols) {
			if (!matchSymbols.containsKey(symbol)) {
				AnalyzerSymbol as = getKnownSymbols().get(symbol);
				if (as!=null) {
					matchSymbols.put(symbol,as);
				}
			}
		}
		if (matchSymbols.size()>0) {
			List<String> unlinkedSymbols = new ArrayList<String>();
			int i = 0;
			String from = "";
			for (String symbol: match.symbols) {
				if (from.length()>0 && match.symbols.size()>(i + 1)) {
					String to = match.symbols.get(i + 1);
					SequenceAnalyzerSymbolLink linkFrom = getKnownLinks().get(getLinkId(from,context,symbol));
					SequenceAnalyzerSymbolLink linkTo = getKnownLinks().get(getLinkId(symbol,context,to));
					if (linkFrom==null && linkTo==null) {
						unlinkedSymbols.add(symbol);
					}
				}
				i++;
				from = symbol;
			}
			if (match.symbols.size()==1) {
				unlinkedSymbols.add(match.symbols.get(0));
			}
			List<SequenceMatcherSequence> list = knownSequences.get(context);
			for (SequenceMatcherSequence seq: list) {
				int conseq = 0;
				double prob = 0D;
				for (SequenceAnalyzerSymbolLink link: match.links) {
					boolean found = false;
					for (SequenceAnalyzerSymbolLink comp: seq.links) {
						if (link.symbolFrom.equals(comp.symbolFrom) &&
							link.context.equals(comp.context) &&
							link.symbolTo.equals(comp.symbolTo)
							) {
							found = true;
							break;
						}
					}
					if (found) {
						prob += (getMaxLinkProb() - link.prob);
						prob += (getMaxSymbolProb() - link.asFrom.prob);
						prob += (getMaxSymbolProb() - link.asTo.prob);
						conseq++;
						prob = (prob * (double)conseq);
					} else {
						conseq = 0;
					}
				}
				for (String symbol: unlinkedSymbols) {
					for (String comp: seq.symbols) {
						if (symbol.equals(comp)) {
							AnalyzerSymbol as = matchSymbols.get(symbol);
							prob += (getMaxSymbolProb() - as.prob);
						}
					}
				}
				if (prob>0D) {
					SequenceMatcherResult res = new SequenceMatcherResult();
					res.result = seq;
					res.prob = prob;
					r.add(res);
				}
			}
		}
		return r;
	}

	@Override
	public void addSymbols(List<String> symbols) {
		super.addSymbols(symbols);
		ZStringSymbolParser sequence = new ZStringSymbolParser();
		sequence.fromSymbols(symbols,true,true);
		addSequence(sequence,getContext());
		if (getContext().length()>0) {
			addSequence(sequence,"");
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
			list.add(getSequenceMatcherSequenceForSequence(sequence,context));
		}
	}
	
	private List<SequenceAnalyzerSymbolLink> parseSequenceLinks(ZStringSymbolParser sequence,String context)  {
		List<SequenceAnalyzerSymbolLink> r = new ArrayList<SequenceAnalyzerSymbolLink>();
		List<String> symbols = sequence.toSymbolsPunctuated();
		int i = 0;
		for (String symbol: symbols) {
			if (symbols.size()>(i + 1)) {
				String to = symbols.get(i + 1);
				SequenceAnalyzerSymbolLink link = getKnownLinks().get(getLinkId(symbol,context,to));
				if (link!=null) {
					r.add(link);
				}
			}
			i++;
		}
		return r;
	}

	private SequenceMatcherSequence getSequenceMatcherSequenceForSequence(ZStringSymbolParser sequence,String context) {
		SequenceMatcherSequence seq = new SequenceMatcherSequence();
		seq.context = context;
		seq.sequence = sequence;
		seq.symbols = sequence.toSymbolsPunctuated();
		seq.links = parseSequenceLinks(sequence,context);
		return seq;
	}
}
