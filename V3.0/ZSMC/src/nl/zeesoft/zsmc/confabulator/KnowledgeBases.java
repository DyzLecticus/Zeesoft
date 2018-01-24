package nl.zeesoft.zsmc.confabulator;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zsmc.sequence.Analyzer;

public class KnowledgeBases extends Analyzer {
	private int 				modules			= 8;
	private List<KnowledgeBase>	knowledgeBases	= new ArrayList<KnowledgeBase>();
	
	public KnowledgeBases() {
		// modules is optional
		initializeModules();
	}

	public KnowledgeBases(int modules) {
		if (modules<=0) {
			modules = 2;
		}
		this.modules = modules;
		initializeModules();
	}
	
	public void initializeModules() {
		for (int i = 1; i<modules; i++) {
			knowledgeBases.add(new KnowledgeBase());
		}
	}
	/*
	@Override
	public void addSequence(ZStringSymbolParser sequence) {
		System.out.println("Adding sequence: " + sequence);
		super.addSequence(sequence);
	}
	*/
	
	@Override
	public void addSymbols(List<String> symbols) {
		super.addSymbols(symbols);
		int s = 0;
		for (String symbol: symbols) {
			for (int i = 1; i<modules; i++) {
				if (s+i<symbols.size()) {
					String nextSymbol = symbols.get(s+i); 
					knowledgeBases.get(i - 1).learnLink(symbol,nextSymbol);
				} else {
					break;
				}
			}
			s++;
		}
	}

	@Override
	public void calculateProb() {
		super.calculateProb();
		for (int i = 1; i<modules; i++) {
			knowledgeBases.get(i - 1).calculateProb();
		}
	}

	public int getModules() {
		return modules;
	}

	public List<KnowledgeBase> getKnowledgeBases() {
		return knowledgeBases;
	}

	public void setKnowledgeBases(List<KnowledgeBase> knowledgeBases) {
		this.knowledgeBases = knowledgeBases;
	}
}
