package nl.zeesoft.zsd.sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;

/**
 * A SequenceAnalyzer can be used to learn context sensitive sequence symbol pair links.
 */
public class SequenceAnalyzer extends Analyzer {
	private SortedMap<String,SequenceAnalyzerSymbolLink>		knownLinks			= new TreeMap<String,SequenceAnalyzerSymbolLink>();		
	private SortedMap<String,List<SequenceAnalyzerSymbolLink>>	linksBySymbolFrom	= new TreeMap<String,List<SequenceAnalyzerSymbolLink>>();
	private SortedMap<String,List<SequenceAnalyzerSymbolLink>>	linksBySymbolTo		= new TreeMap<String,List<SequenceAnalyzerSymbolLink>>();
	private int													linkCount			= 0;
	private SortedMap<String,Integer>							linkContextCounts	= new TreeMap<String,Integer>();
	private double												linkMaxProb			= 0.0D;
	private double												linkMinProb			= 1.0D;
	private SortedMap<String,Double>							linkContextMaxProbs	= new TreeMap<String,Double>();
	private SortedMap<String,Double>							linkContextMinProbs	= new TreeMap<String,Double>();

	private String												context				= "";

	/**
	 * Returns the context used to associate additional sequences.
	 * 
	 * @return The context used to associate additional sequences
	 */
	public String getContext() {
		return context;
	}
	
	/**
	 * Sets the context used to associate additional sequences.
	 * 
	 * @param context The context to set
	 */
	public void setContext(String context) {
		this.context = context;
	}

	@Override
	protected void handleContextSymbol(ZStringBuilder contextSymbol) {
		context = contextSymbol.toString();
	}

	@Override
	public void addSymbols(List<String> symbols) {
		super.addSymbols(symbols);
		int i = 0;
		for (String symbol: symbols) {
			if (symbols.size()>(i + 1)) {
				String to = symbols.get(i + 1);
				addOrUpdateLink(symbol,context,to);
				if (context.length()>0) {
					addOrUpdateLink(symbol,"",to);
				}
			}
			i++;
		}
	}
	
	@Override
	public void calculateProb() {
		super.calculateProb();
		for (Entry<String,SequenceAnalyzerSymbolLink> entry: knownLinks.entrySet()) {
			entry.getValue().prob = ((double)entry.getValue().count / (double)linkCount);
			entry.getValue().probContext = ((double)entry.getValue().count / (double)linkContextCounts.get(entry.getValue().context));
			if (entry.getValue().prob>linkMaxProb) {
				linkMaxProb = entry.getValue().prob;
			}
			if (entry.getValue().prob<linkMinProb) {
				linkMinProb = entry.getValue().prob;
			}
			Double maxProb = linkContextMaxProbs.get(entry.getValue().context);
			Double minProb = linkContextMinProbs.get(entry.getValue().context);
			if (maxProb==null){
				maxProb = new Double(0);
			}
			if (minProb==null){
				minProb = new Double(1);
			}
			if (entry.getValue().probContext>maxProb) {
				maxProb = entry.getValue().probContext;
				linkContextMaxProbs.put(entry.getValue().context,maxProb);
			}
			if (entry.getValue().probContext<minProb) {
				minProb = entry.getValue().probContext;
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
				link.asFrom = getKnownSymbols().get(from);
				link.asTo = getKnownSymbols().get(to);
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
			if (link.context.length()==0) {
				linkCount++;
			}
			Integer count = linkContextCounts.get(context);
			if (count==null){
				count = new Integer(0);
			}
			count++;
			linkContextCounts.put(context,count);
		}
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

	public int getLinkCount() {
		return linkCount;
	}

	public void setLinkCount(int linkCount) {
		this.linkCount = linkCount;
	}

	public double getLinkMaxProb() {
		return linkMaxProb;
	}

	public void setLinkMaxProb(double linkMaxProb) {
		this.linkMaxProb = linkMaxProb;
	}

	public double getLinkMinProb() {
		return linkMinProb;
	}

	public void setLinkMinProb(double linkMinProb) {
		this.linkMinProb = linkMinProb;
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
