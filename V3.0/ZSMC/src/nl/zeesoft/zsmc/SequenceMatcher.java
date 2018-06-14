package nl.zeesoft.zsmc;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsmc.sequence.AnalyzerSymbol;
import nl.zeesoft.zsmc.sequence.SequenceAnalyzerSymbolLink;
import nl.zeesoft.zsmc.sequence.SequenceMatcherResult;
import nl.zeesoft.zsmc.sequence.SequenceMatcherSequence;

/**
 * A SequenceMatcher can be used to find a context sensitive match for a certain sequence.
 */
public class SequenceMatcher extends SequenceClassifier {
	private SortedMap<String,List<SequenceMatcherSequence>>		knownSequences	= new TreeMap<String,List<SequenceMatcherSequence>>();
	
	private String												context				= "";

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
		List<SequenceMatcherSequence> list = knownSequences.get(context);
		for (SequenceMatcherSequence seq: list) {
			int conseq = 0;
			double prob = 0D;
			if (match.links.size()==0) {
				// TODO: word for word matching
			} else {
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
						AnalyzerSymbol as = getKnownSymbols().get(link.symbolFrom);
						prob += (getMaxSymbolProb() - as.prob);
						as = getKnownSymbols().get(link.symbolTo);
						prob += (getMaxSymbolProb() - as.prob);
						conseq++;
						prob = (prob * (double)conseq);
					} else {
						conseq = 0;
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
		return r;
	}

	@Override
	protected void handleContextSymbol(ZStringBuilder contextSymbol) {
		super.handleContextSymbol(contextSymbol);
		context = contextSymbol.toString();
	}
	
	@Override
	public void addSymbols(List<String> symbols) {
		super.addSymbols(symbols);
		ZStringSymbolParser sequence = new ZStringSymbolParser();
		sequence.fromSymbols(symbols,true,true);
		addSequence(sequence,context);
		if (context.length()>0) {
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
		for (SequenceMatcherSequence seq: list) {
			if (seq.sequence.equals(sequence)) {
				found = true;
				break;
			}
		}
		if (!found) {
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
		seq.links = parseSequenceLinks(sequence,context);
		return seq;
	}
}
