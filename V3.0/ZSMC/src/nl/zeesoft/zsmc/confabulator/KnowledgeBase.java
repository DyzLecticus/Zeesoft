package nl.zeesoft.zsmc.confabulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zsmc.sequence.AnalyzerSymbol;

public class KnowledgeBase {
	private SortedMap<String,List<KnowledgeLink>> 	linksBySource 	= new TreeMap<String,List<KnowledgeLink>>();
	private SortedMap<String,List<KnowledgeLink>> 	linksByTarget 	= new TreeMap<String,List<KnowledgeLink>>();
	private int										totalCount		= 0;
	
	public KnowledgeLink learnLink(String source,String target) {
		KnowledgeLink r = null;
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
		for (KnowledgeLink link: listS) {
			if (link.target.equals(target)) {
				r = link;
				r.count++;
				break;
			}
		}
		if (r==null) {
			r = new KnowledgeLink();
			r.source = source;
			r.target = target;
			listS.add(r);
			listT.add(r);
		}
		totalCount++;
		return r;
	}

	public void calculateProb(KnowledgeBases bases, int B, double p0) {
		for (Entry<String,List<KnowledgeLink>> entry: linksBySource.entrySet()) {
			for (KnowledgeLink link: entry.getValue()) {
				double prob = p0;
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
				AnalyzerSymbol s = bases.getKnownSymbols().get(link.source);
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
