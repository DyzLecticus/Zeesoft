package nl.zeesoft.zsmc.confabulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zsmc.sequence.AnalyzerSymbol;

public class KnowledgeBase {
	private SortedMap<String,List<KnowledgeLink>> 	linksBySource	= new TreeMap<String,List<KnowledgeLink>>();
	private SortedMap<String,List<KnowledgeLink>> 	linksByTarget	= new TreeMap<String,List<KnowledgeLink>>();
	private SortedMap<String,KnowledgeLink> 		linksByST		= new TreeMap<String,KnowledgeLink>();
	private int										totalCount		= 0;

	public KnowledgeLink getLink(String source,String target) {
		return linksByST.get(source + "[]" + target);
	}
	
	public KnowledgeLink learnLink(String source,String target) {
		KnowledgeLink r = getLink(source,target);
		if (r==null) {
			r = new KnowledgeLink();
			r.source = source;
			r.target = target;
			
			List<KnowledgeLink> listS = linksBySource.get(source);
			if (listS==null) {
				listS = new ArrayList<KnowledgeLink>();
				linksBySource.put(source,listS);
			}
			List<KnowledgeLink> listT = linksByTarget.get(target);
			if (listT==null) {
				listT = new ArrayList<KnowledgeLink>();
				linksByTarget.put(target,listT);
			}
			listS.add(r);
			listT.add(r);
			linksByST.put(source + "[]" + target,r);
		} else {
			r.count++;
		}
		totalCount++;
		return r;
	}

	public void calculateProb(KnowledgeBases bases, List<String> contextSymbols, int B, double p0) {
		for (Entry<String,List<KnowledgeLink>> entry: linksBySource.entrySet()) {
			List<KnowledgeLink> test = new ArrayList<KnowledgeLink>(entry.getValue());
			for (KnowledgeLink link: test) {
				if (link.count<=3) {
					entry.getValue().remove(link);
					totalCount = totalCount - link.count;
				}
			}
		}
		for (Entry<String,List<KnowledgeLink>> entry: linksByTarget.entrySet()) {
			List<KnowledgeLink> test = new ArrayList<KnowledgeLink>(entry.getValue());
			for (KnowledgeLink link: test) {
				if (link.count<=3) {
					entry.getValue().remove(link);
				}
			}
		}
		for (Entry<String,List<KnowledgeLink>> entry: linksBySource.entrySet()) {
			for (KnowledgeLink link: entry.getValue()) {
				double prob = 1.0D / (double)contextSymbols.size();
				AnalyzerSymbol s = bases.getKnownSymbols().get(link.source);
				if (s!=null) {
					prob = s.prob;
				}
				link.prob = ((double)link.count / (double)totalCount);
				link.sourceWeight = (link.prob / prob) / p0;
				link.sourceWeight = (Math.log(link.sourceWeight) / Math.log(2.0)) + B;
			}
		}
		for (Entry<String,List<KnowledgeLink>> entry: linksByTarget.entrySet()) {
			for (KnowledgeLink link: entry.getValue()) {
				double prob = p0;
				AnalyzerSymbol s = bases.getKnownSymbols().get(link.target);
				if (s!=null) {
					prob = s.prob;
				}
				link.prob = ((double)link.count / (double)totalCount);
				link.targetWeight = (link.prob / prob) / p0;
				link.targetWeight = (Math.log(link.targetWeight) / Math.log(2.0)) + B;
			}
		}
	}

	public SortedMap<String, List<KnowledgeLink>> getLinksBySource() {
		return linksBySource;
	}

	public void setLinksBySource(SortedMap<String, List<KnowledgeLink>> linksBySource) {
		this.linksBySource = linksBySource;
	}

	public SortedMap<String, List<KnowledgeLink>> getLinksByTarget() {
		return linksByTarget;
	}

	public void setLinksByTarget(SortedMap<String, List<KnowledgeLink>> linksByTarget) {
		this.linksByTarget = linksByTarget;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
