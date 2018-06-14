package nl.zeesoft.zsmc.sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import nl.zeesoft.zdk.ZStringBuilder;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A SequenceAnalyzer can be used to learn context sensitive sequence symbol pair links.
 */
public class SequenceAnalyzer extends Analyzer {
	private SortedMap<String,SequenceAnalyzerSymbolLink>		knownLinks			= new TreeMap<String,SequenceAnalyzerSymbolLink>();		
	private SortedMap<String,List<SequenceAnalyzerSymbolLink>>	linksBySymbolFrom	= new TreeMap<String,List<SequenceAnalyzerSymbolLink>>();
	private SortedMap<String,List<SequenceAnalyzerSymbolLink>>	linksBySymbolTo		= new TreeMap<String,List<SequenceAnalyzerSymbolLink>>();
	private int													totalLinks			= 0;
	private double												maxLinkProb			= 0.0D;

	private String												context				= "";

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
			entry.getValue().prob = ((double)entry.getValue().count / (double)totalLinks);
			if (entry.getValue().prob>maxLinkProb) {
				maxLinkProb = entry.getValue().prob;
			}
		}
	}
	
	public String getLinkId(String from, String context, String to) {
		return from + "[]" + context + "[]" + to;
	}
	
	private void addOrUpdateLink(String from, String context, String to) {
		String linkId = getLinkId(from,context,to);
		SequenceAnalyzerSymbolLink link = null;
		link = knownLinks.get(linkId);
		if (link==null) {
			link = new SequenceAnalyzerSymbolLink();
			link.context = context;
			link.symbolFrom = from;
			link.symbolTo = to;
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
		totalLinks++;
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

	public int getTotalLinks() {
		return totalLinks;
	}

	public void setTotalLinks(int totalLinks) {
		this.totalLinks = totalLinks;
	}

	public double getMaxLinkProb() {
		return maxLinkProb;
	}

	public void setMaxLinkProb(double maxLinkProb) {
		this.maxLinkProb = maxLinkProb;
	}
}
