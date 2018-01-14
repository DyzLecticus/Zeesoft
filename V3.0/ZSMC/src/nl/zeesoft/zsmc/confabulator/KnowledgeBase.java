package nl.zeesoft.zsmc.confabulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

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
		for (KnowledgeLink link: listT) {
			if (link.source.equals(source)) {
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

	public void calculateProb() {
		for (Entry<String,List<KnowledgeLink>> entry: linksBySource.entrySet()) {
			for (KnowledgeLink link: entry.getValue()) {
				link.prob = ((double)link.count / (double)totalCount);
			}
		}
		for (Entry<String,List<KnowledgeLink>> entry: linksByTarget.entrySet()) {
			for (KnowledgeLink link: entry.getValue()) {
				link.prob = ((double)link.count / (double)totalCount);
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
