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

	public KnowledgeLink getLink(String source,String target,int minCount) {
		KnowledgeLink kl = getLink(source,target);
		if (kl!=null && kl.count<minCount) {
			kl = null;
		}
		return kl;
	}

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
		for (Entry<String,KnowledgeLink> entry: linksByST.entrySet()) {
			KnowledgeLink link = entry.getValue();
			link.prob = ((double)link.count / (double)totalCount);

			double sProb = 1.0D / (double)contextSymbols.size();
			AnalyzerSymbol ss = bases.getKnownSymbols().get(link.source);
			if (ss!=null) {
				sProb = ss.prob;
			}
			link.sourceWeight = (link.prob / sProb) / p0;
			link.sourceWeight = (Math.log(link.sourceWeight) / Math.log(2.0)) + B;

			double tProb = 1.0D / (double)contextSymbols.size();
			AnalyzerSymbol ts = bases.getKnownSymbols().get(link.source);
			if (ts!=null) {
				tProb = ts.prob;
			}
			link.targetWeight = (link.prob / tProb) / p0;
			link.targetWeight = (Math.log(link.targetWeight) / Math.log(2.0)) + B;
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

	public SortedMap<String, KnowledgeLink> getLinksByST() {
		return linksByST;
	}

	public void setLinksByST(SortedMap<String, KnowledgeLink> linksByST) {
		this.linksByST = linksByST;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
