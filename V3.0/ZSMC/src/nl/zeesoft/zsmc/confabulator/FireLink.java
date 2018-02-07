package nl.zeesoft.zsmc.confabulator;

public class FireLink extends KnowledgeLink {
	public Module	module			= null;
	public double	excitation		= 0D;
	public boolean	forward			= true;
	
	public FireLink(KnowledgeLink kl, Module module, boolean forward, double excitation) {
		this.module = module;
		this.excitation = excitation;
		this.forward = forward;
		this.count = kl.count;
		this.prob = kl.prob;
		this.source = kl.source;
		this.sourceWeight = kl.sourceWeight;
		this.target = kl.target;
		this.targetWeight = kl.targetWeight;
	}
}
