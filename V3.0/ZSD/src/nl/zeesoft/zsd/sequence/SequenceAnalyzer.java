package nl.zeesoft.zsd.sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A SequenceAnalyzer can be used to learn context sensitive sequence symbol pair links.
 */
public class SequenceAnalyzer extends Analyzer {
	private SortedMap<String,SequenceAnalyzerSymbolLink>		knownLinks			= new TreeMap<String,SequenceAnalyzerSymbolLink>();		
	private SortedMap<String,List<SequenceAnalyzerSymbolLink>>	linksBySymbolFrom	= new TreeMap<String,List<SequenceAnalyzerSymbolLink>>();
	private SortedMap<String,List<SequenceAnalyzerSymbolLink>>	linksBySymbolTo		= new TreeMap<String,List<SequenceAnalyzerSymbolLink>>();
	private SortedMap<String,Integer>							linkContextCounts	= new TreeMap<String,Integer>();
	private SortedMap<String,Double>							linkContextMaxProbs	= new TreeMap<String,Double>();
	private SortedMap<String,Double>							linkContextMinProbs	= new TreeMap<String,Double>();

	@Override
	public void addSymbols(List<String> symbols,List<String> contextSymbols) {
		super.addSymbols(symbols,contextSymbols);
		for (String context: contextSymbols) {
			int i = 0;
			for (String symbol: symbols) {
				if (symbols.size()>(i + 1)) {
					String to = symbols.get(i + 1);
					addOrUpdateLink(symbol,context,to);
				}
				i++;
			}
		}
	}
	
	@Override
	public void calculateProb() {
		super.calculateProb();
		linkContextMaxProbs.clear();
		linkContextMinProbs.clear();
		for (Entry<String,SequenceAnalyzerSymbolLink> entry: knownLinks.entrySet()) {
			entry.getValue().prob = ((double)entry.getValue().count / (double)linkContextCounts.get(entry.getValue().context));
			Double maxProb = linkContextMaxProbs.get(entry.getValue().context);
			Double minProb = linkContextMinProbs.get(entry.getValue().context);
			if (maxProb==null){
				maxProb = new Double(0);
			}
			if (minProb==null){
				minProb = new Double(1);
			}
			if (entry.getValue().prob>maxProb) {
				maxProb = entry.getValue().prob;
				linkContextMaxProbs.put(entry.getValue().context,maxProb);
			}
			if (entry.getValue().prob<minProb) {
				minProb = entry.getValue().prob;
				linkContextMinProbs.put(entry.getValue().context,minProb);
			}
		}
	}
	
	public String getLinkId(String from, String context, String to) {
		return from + "[]" + context + "[]" + to;
	}
	
	private void addOrUpdateLink(String from, String context, String to) {
		if (!from.equals(getIoSeparator()) && !to.equals(getIoSeparator())) {
			String linkId = getLinkId(from,context,to);
			SequenceAnalyzerSymbolLink link = null;
			link = knownLinks.get(linkId);
			if (link==null) {
				link = new SequenceAnalyzerSymbolLink();
				link.context = context;
				link.symbolFrom = from;
				link.symbolTo = to;
				link.asFrom = getKnownSymbol(from,context);
				link.asTo = getKnownSymbol(to,context);
				knownLinks.put(linkId,link);
				
				List<SequenceAnalyzerSymbolLink> list = null; 
	
				list = linksBySymbolFrom.get(from);
				if (list==null) {
					list = new ArrayList<SequenceAnalyzerSymbolLink>();
					linksBySymbolFrom.put(from,list);
				}
				list.add(link);
				
				list = linksBySymbolTo.get(to);
				if (list==null) {
					list = new ArrayList<SequenceAnalyzerSymbolLink>();
					linksBySymbolTo.put(to,list);
				}
				list.add(link);
			}
			link.count++;
			Integer count = linkContextCounts.get(context);
			if (count==null){
				count = new Integer(0);
			}
			count++;
			linkContextCounts.put(context,count);
		}
	}
	
	public List<SequenceAnalyzerSymbolLink> getLinksByFromTo(String from,String to,String context,boolean caseInsensitive) {
		List<SequenceAnalyzerSymbolLink> r = new ArrayList<SequenceAnalyzerSymbolLink>();
		if (caseInsensitive) {
			List<SequenceAnalyzerSymbolLink> links = getLinksByFromTo(from,to,caseInsensitive);
			for (SequenceAnalyzerSymbolLink link: links) {
				if (link.context.equals(context)) {
					r.add(link);
				}
			}
		} else {
			SequenceAnalyzerSymbolLink link = getKnownLinks().get(getLinkId(from,context,to));
			if (link!=null) {
				r.add(link);
			}
		}
		return r;
	}
	
	public List<SequenceAnalyzerSymbolLink> getLinksByFromTo(String from,String to,boolean caseInsensitive) {
		List<SequenceAnalyzerSymbolLink> r = new ArrayList<SequenceAnalyzerSymbolLink>();
		if (caseInsensitive) {
			for (String cased: getCaseVariations(from)) {
				List<SequenceAnalyzerSymbolLink> add = getLinksBySymbolFrom().get(cased);
				if (add!=null) {
					for (SequenceAnalyzerSymbolLink link: add) {
						if (to.length()==0 || link.symbolTo.equalsIgnoreCase(to)) {
							r.add(link);
						}
					}
				}
			}
		} else {
			List<SequenceAnalyzerSymbolLink> add = getLinksBySymbolFrom().get(from);
			if (add!=null) {
				for (SequenceAnalyzerSymbolLink link: add) {
					if (to.length()==0 || link.symbolTo.equals(to)) {
						r.add(link);
					}
				}
			}
		}
		return r;
	}

	public List<String> getUnlinkedSymbolsForSequenceSymbols(List<String> symbols,String context,boolean caseInsensitive) {
		List<String> r = new ArrayList<String>();
		int i = 0;
		String from = "";
		for (String symbol: symbols) {
			if (from.length()>0 && symbols.size()>(i + 1)) {
				String to = symbols.get(i + 1);
				List<SequenceAnalyzerSymbolLink> linksFrom = null;
				List<SequenceAnalyzerSymbolLink> linksTo = null;
				if (context==null) {
					linksFrom = getLinksByFromTo(from,symbol,caseInsensitive);
					linksTo = getLinksByFromTo(symbol,to,caseInsensitive);
				} else {
					linksFrom = getLinksByFromTo(from,symbol,context,caseInsensitive);
					linksTo = getLinksByFromTo(symbol,to,context,caseInsensitive);
				}
				if (linksFrom.size()==0 && linksTo.size()==0) {
					r.add(symbol);
				}
			}
			i++;
			from = symbol;
		}
		if (symbols.size()==1) {
			r.add(symbols.get(0));
		}
		return r;
	}

	public SortedMap<String, SequenceAnalyzerSymbolLink> getKnownLinks() {
		return knownLinks;
	}

	public void setKnownLinks(SortedMap<String, SequenceAnalyzerSymbolLink> knownLinks) {
		this.knownLinks = knownLinks;
	}

	public SortedMap<String, List<SequenceAnalyzerSymbolLink>> getLinksBySymbolFrom() {
		return linksBySymbolFrom;
	}

	public void setLinksBySymbolFrom(SortedMap<String, List<SequenceAnalyzerSymbolLink>> linksBySymbolFrom) {
		this.linksBySymbolFrom = linksBySymbolFrom;
	}

	public SortedMap<String, List<SequenceAnalyzerSymbolLink>> getLinksBySymbolTo() {
		return linksBySymbolTo;
	}

	public void setLinksBySymbolTo(SortedMap<String, List<SequenceAnalyzerSymbolLink>> linksBySymbolTo) {
		this.linksBySymbolTo = linksBySymbolTo;
	}

	public SortedMap<String, Integer> getLinkContextCounts() {
		return linkContextCounts;
	}

	public void setLinkContextCounts(SortedMap<String, Integer> contextCounts) {
		this.linkContextCounts = contextCounts;
	}

	public SortedMap<String, Double> getLinkContextMaxProbs() {
		return linkContextMaxProbs;
	}

	public void setLinkContextMaxProbs(SortedMap<String, Double> contextMaxProbs) {
		this.linkContextMaxProbs = contextMaxProbs;
	}

	public SortedMap<String, Double> getLinkContextMinProbs() {
		return linkContextMinProbs;
	}

	public void setLinkContextMinProbs(SortedMap<String, Double> contextMinProbs) {
		this.linkContextMinProbs = contextMinProbs;
	}
}
