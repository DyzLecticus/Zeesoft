package nl.zeesoft.zsc.confab;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;

public abstract class ConfabulationObject {
	public ZStringSymbolParser	input			= new ZStringSymbolParser();
	public long					maxTime			= 1000;
	public boolean				appendLog		= false;

	public ZStringBuilder		log				= new ZStringBuilder();
	public Date					started			= null;
	public List<String>			symbols			= null;
	public List<Module>			modules 		= new ArrayList<Module>();
	
	public void initialize() {
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
	
	public void addLogLine(ZStringBuilder line) {
		if (appendLog) {
			log.append((new ZDate()).getTimeString(true));
			log.append(": ");
			log.append(line);
		}
	}
}
