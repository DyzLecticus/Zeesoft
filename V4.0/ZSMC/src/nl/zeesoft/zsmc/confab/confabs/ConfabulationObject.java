package nl.zeesoft.zsmc.confab.confabs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsmc.confab.Module;
import nl.zeesoft.zsmc.kb.KnowledgeBase;

public abstract class ConfabulationObject {	
	public ZStringSymbolParser			input			= new ZStringSymbolParser();
	public boolean						caseSensitive	= false;
	public long							maxTime			= 1000;
	public boolean						appendLog		= false;
	public double						noise			= 0D;

	public Messenger					messenger		= null;
	public WorkerUnion					union			= null;
	public KnowledgeBase				kb				= null;
	
	public ZStringBuilder				log				= new ZStringBuilder();
	public Date							started			= null;
	public List<String>					symbols			= null;
	public List<Module>					modules 		= new ArrayList<Module>();
	public List<Worker>					workers			= new ArrayList<Worker>();
	
	public void initialize(Messenger msgr, WorkerUnion uni,KnowledgeBase kb) {
		messenger = msgr;
		union = uni;
		this.kb = kb;
		started = new Date();
		symbols = input.toSymbolsPunctuated();
	}
	
	public List<Module> copyModules() {
		List<Module> r = new ArrayList<Module>();
		for (Module mod: modules) {
			r.add(mod.copy());
		}
		return r;
	}
	
	public void addLogLine(String line) {
		addLogLine(new ZStringBuilder(line));
	}
	
	public void addLogLine(ZStringBuilder line) {
		if (appendLog) {
			log.append((new ZDate()).getTimeString(true));
			log.append(": ");
			log.append(line);
		}
	}
}
