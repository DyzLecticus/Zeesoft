package nl.zeesoft.zsmc.confabulator;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class Confabulator {
	public static final int		B			= 30;
	public static final double	p0			= 0.0001D;
	
	private Messenger			messenger 	= null;
	private WorkerUnion			union		= null;
	private List<Module>		modules		= new ArrayList<Module>();
	private KnowledgeBases		kbs			= null;
	
	public Confabulator(Messenger msgr, WorkerUnion uni,KnowledgeBases kbs) {
		this.messenger = msgr;
		this.union = uni;
		this.kbs = kbs;
	}

	public void intitializeModules() {
		intitializeModules(B,p0);
	}
	
	public void intitializeModules(int B, double p0) {
		modules.clear();
		for (int i = 0; i<kbs.getModules(); i++) {
			modules.add(new Module(messenger,union,kbs,modules,B,p0));
		}
	}

	public void setConclusions(ZStringSymbolParser conclusions) {
		conclusions.toCase(true);
		String[] conc = new String[modules.size()];
		List<String> list = conclusions.toSymbolsPunctuated();
		int start = 0;
		if (list.size()>(modules.size()/2)) {
			start = list.size() - (modules.size()/2);
		}
		for (int i = start; i<list.size(); i++) {
			conc[i - start] = list.get(i);
		}
		for (int i = 0; i<conc.length; i++) {
			if (conc[i]==null) {
				conc[i]="";
			}
			System.out.println("Conclusion " + i + ": " + conc[i]);
		}
		setConclusions(conc);
	}

	public void setConclusions(String[] conclusions) {
		if (conclusions.length>=modules.size()) {
			for(int i = 0; i<conclusions.length; i++) {
				modules.get(i).setConclusion(conclusions[i]);
				String conclusion = modules.get(i).getConclusion();
				if (conclusion!=null && conclusion.length()>0) {
					modules.get(i).setLocked(true);
				}
			}
		}
	}
	
	public void startConfabulation(int confMs) {
		int[] confMss = new int[modules.size()];
		for (int i = 0; i<modules.size(); i++) {
			confMss[i] = confMs;
		}
		startConfabulation(confMss);
	}

	public void startConfabulation(int[] confMss) {
		for (int i = 0; i<modules.size(); i++) {
			modules.get(i).startConfabulation(confMss[i]);
		}
	}

	public List<String> getFinalConclusions() {
		List<String> r = null;
		boolean done = true;
		for (Module module: modules) {
			if (!module.isDone()) {
				done = false;
				break;
			}
		}
		if (done) {
			r = getConclusions();
		}
		return r;
	}
	
	public List<String> getConclusions() {
		List<String> r = new ArrayList<String>();
		for (Module mod: modules) {
			String v = mod.getConclusion();
			if (v!=null) {
				r.add(v);
			} else {
				r.add("");
			}
		}
		return r;
	}
	
	public ZStringBuilder getActiveSymbolsList() {
		ZStringBuilder r = new ZStringBuilder();
		int m = 0;
		for (Module mod: modules) {
			m++;
			if (r.length()>0) {
				r.append("\n");
			}
			r.append("Module ");
			r.append("" + m);
			r.append(": ");
			r.append(mod.getActiveSymbolsList());
		}
		return r;
	}
	
}
