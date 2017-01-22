package nl.zeesoft.zac.module.confabulate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zac.database.model.Module;
import nl.zeesoft.zac.database.model.SymbolLink;
import nl.zeesoft.zac.database.model.SymbolLinkContext;
import nl.zeesoft.zac.database.model.SymbolLinkSequence;
import nl.zeesoft.zac.module.object.ObjSymbolLinkContext;
import nl.zeesoft.zac.module.object.ObjSymbolLinkSequence;

/**
 * This class can confabulate module output using the module context and sequence links.
 */
public class ConModuleInstance {
	private Module								module						= null;
	private ObjSymbolLinkContext				contextLinks				= null;
	private ObjSymbolLinkSequence				sequenceLinks				= null;
	
	private SortedMap<Integer,ConSymbolLevels>	confabulators				= new TreeMap<Integer,ConSymbolLevels>(); 

	private SortedMap<String,Integer>			symbolNumContextLinksTo		= new TreeMap<String,Integer>(); 
	private SortedMap<String,Integer>			symbolNumSequenceLinksTo	= new TreeMap<String,Integer>(); 
	private int									maxContextLinksTo			= 0;
	private int									maxSequenceLinksTo			= 0;
	private int									maxContextLinkCount			= 0;
	private int									maxSequenceLinkCount		= 0;
	private	List<String>						allContextSymbols			= new ArrayList<String>();
	private	List<String>						allSequenceSymbols			= new ArrayList<String>();

	private	List<String>						workingExtension			= null;
	
	public ConModuleInstance(Module module) {
		this.module = module;
		contextLinks = new ObjSymbolLinkContext(module.getId(),null);
		sequenceLinks = new ObjSymbolLinkSequence(module.getId(),null);
	}
	
	// Called by module trainer
	public ConModuleInstance(Module module,List<SymbolLinkContext> cLinks,List<SymbolLinkSequence> sLinks) {
		this.module = module;
		contextLinks = new ObjSymbolLinkContext(module.getId(),null);
		sequenceLinks = new ObjSymbolLinkSequence(module.getId(),null);
		for (SymbolLinkContext cLink: cLinks) {
			if (cLink.getId()>0) {
				contextLinks.addObject(cLink);
			}
		}
		for (SymbolLinkSequence sLink: sLinks) {
			if (sLink.getId()>0) {
				sequenceLinks.addObject(sLink);
			}
		}
		resetDerrivedContextLinkData();
		resetDerrivedSequenceLinkData();
	}

	public void refreshLinks() {
		contextLinks.reinitialize();
		sequenceLinks.reinitialize();
		resetDerrivedContextLinkData();
		resetDerrivedSequenceLinkData();
	}

	public void confabulateContext(ConInputOutput io) {
		resetConfabulators(1);
		io.addLogLine("Confabulate context for sequence: " + io.getInputSequence());
		if (io.getInputSequence().length()>0) {
			List<String> symbols = io.getInputSequenceAsSymbolList();
			for (String symbol: symbols) {
				List<SymbolLink> links = new ArrayList<SymbolLink>(contextLinks.getSymbolLinkContextsBySymbolFrom(symbol,1));
				io.incrementFiredLinks(fireLinksIntoConfabulator(0,links,true));
			}
		}
		List<String> activeSymbols = getConfabulator(0).getSymbolsByLevel(1);
		int added = 0;
		StringBuilder symbolLevels = new StringBuilder();
		for (String symbol: activeSymbols) {
			io.getOutputContextSymbols()[added] = symbol;
			added++;
			symbolLevels.append("\n");
			symbolLevels.append(added);
			symbolLevels.append(": ");
			symbolLevels.append(symbol);
			symbolLevels.append(" (");
			symbolLevels.append(getConfabulatorSymbolLevel(0,symbol));
			symbolLevels.append(")");
			if (added>=8) {
				break;
			}
		}
		if (symbolLevels.length()>0) {
			io.addLogLine("Output context symbols:" + symbolLevels);
		}
		io.addLogLine("Confabulated context");
	}

	public void confabulateCorrect(ConInputOutput io) {
		io.addLogLine("Confabulate correction for sequence: " + io.getInputSequence());
		workingExtension = new ArrayList<String>();
		int i = 0;
		List<String> symbols = io.getInputSequenceAsSymbolList();
		for (String symbol: symbols) {
			String winner = "";
			if (!allSequenceSymbols.contains(symbol)) {
				initializeConfabulatorsForNextSymbol(io);
				int nConf = module.getMaxSequenceDistance() + 1;
				for (int si = i + 1; si < symbols.size(); si++) {
					ConSymbolLevels con = getConfabulator(nConf);
					if (con==null) {
						break;
					}
					String nSymbol = symbols.get(si);
					if (allSequenceSymbols.contains(nSymbol) && 
						(con.getActiveSymbols().size()==0 || con.getActiveSymbols().contains(nSymbol))
						) {
						con.getActiveSymbols().clear();
						con.getActiveSymbols().add(nSymbol);
						con.setSymbolLevel(nSymbol,1);
					}
					nConf++;
				}
				winner = confabulateForward(io);
				winner = confabulateBackward(io);
				if (winner.length()>0) {
					debugConfabulators(io,"Confabulated replacement symbol: " + winner + ", for: " + symbol);
				} else {
					debugConfabulators(io,"Failed to confabulate replacement for symbol: " + symbol);
				}
			}
			if (winner.length()==0) {
				winner = symbol;
			}
			if (winner.length()>0) {
				workingExtension.add(winner);
				io.addSymbolToOutputSequence(winner);
			}
			i++;
		}
		io.addLogLine("Confabulated correction");
	}
	
	public void confabulateExtend(ConInputOutput io) {
		workingExtension = io.getInputSequenceAsSymbolList();
		io.addLogLine("Confabulate extended sequence for sequence: " + io.getInputSequence());

		int added = 0;
		String next = confabulateNextSymbol(io);
		while(next.length()>0) {
			debugConfabulators(io,"Confabulated next symbol: " + next);
			io.addSymbolToOutputSequence(next);
			added++;
			if (added>=io.getMaxOutputSymbols()) {
				break;
			}
			workingExtension.add(next);
			next = confabulateNextSymbol(io);
		}
		debugConfabulators(io,"Confabulated extended sequence");
	}

	private String confabulateNextSymbol(ConInputOutput io) {
		String next = "";
		if (initializeConfabulatorsForNextSymbol(io)) {
			next = confabulateForward(io);
			if (next.length()==0) {
				next = confabulateBackward(io);
			}
		}
		return next;
	}

	private String initializeConfabulatorsForExtension(ConInputOutput io) {
		String symbolFrom = "";
		resetConfabulators(module.getMaxSequenceDistance() * 2);
		String[] conclusions = getConclusions(workingExtension);
		for (int pConf = (module.getMaxSequenceDistance() - 1); pConf >= 0; pConf--) {
			if (conclusions[pConf].length()>0) {
				setConfabulatorSymbolLevel(pConf,conclusions[pConf],1);
				if (pConf == (module.getMaxSequenceDistance() - 1)) {
					symbolFrom = conclusions[pConf];
				}
			}
		}
		io.incrementFiredLinks(initializeConfabulatorContext(io.getInputContextAsSymbolList()));
		return symbolFrom;
	}

	private boolean initializeConfabulatorsForNextSymbol(ConInputOutput io) {
		boolean confabulate = true;
		String symbolFrom = initializeConfabulatorsForExtension(io);
		if (symbolFrom.length()>0) {
			ConSymbolLevels confabulator = getConfabulator(module.getMaxSequenceDistance());
			List<SymbolLinkSequence> links = sequenceLinks.getSymbolLinkSequencesBySymbolFrom(symbolFrom,1,1);
			if (links.size()>0) {
				List<String> activeSymbols = new ArrayList<String>(confabulator.getActiveSymbols());
				if (activeSymbols.size()>0) {
					for (String symbol: activeSymbols) {
						boolean found = false;
						for (SymbolLinkSequence link: links) {
							if (link.getSymbolTo().equals(symbol)) {
								found = true;
								break;
							}
						}
						if (!found) {
							confabulator.getActiveSymbols().remove(symbol);
						}
					}
				} else {
					for (SymbolLinkSequence link: links) {
						confabulator.getActiveSymbols().add(link.getSymbolTo());
					}
				}
			} else {
				confabulate = false;
			}
		}
		return confabulate;
	}
	
	private String confabulateForward(ConInputOutput io) {
		String winner = "";
		int add = 0;
		for (int conf = module.getMaxSequenceDistance(); conf < (module.getMaxSequenceDistance() * 2); conf++) {
			int distance = 1; 
			for (int pConf = ((module.getMaxSequenceDistance() - 1) + add); pConf >= 0; pConf--) {
				int symbols = 0;
				for (String symbolFrom: getConfabulator(pConf).getSymbolsByLevel(1)) {
					if (symbols<io.getThinkWidth()) {
						List<SymbolLink> links = new ArrayList<SymbolLink>(sequenceLinks.getSymbolLinkSequencesBySymbolFrom(symbolFrom,distance,1));
						io.incrementFiredLinks(fireLinksIntoConfabulator(conf,links,true));
						break;
					} else {
						getConfabulator(pConf).setSymbolLevel(symbolFrom,0);
					}
					symbols++;
				}
				distance++;
			}
			add++;

			if (conf==module.getMaxSequenceDistance()) {
				winner = getConfabulatorWinningSymbol(conf);
				if (winner.length()>0) {
					break;
				}
			}
		}
		return winner;
	}

	private String confabulateBackward(ConInputOutput io) {
		String winner = "";
		int conf = module.getMaxSequenceDistance();
		int distance = 1; 
		for (int nConf = (module.getMaxSequenceDistance() + 1); nConf < (module.getMaxSequenceDistance() * 2); nConf++) {
			int symbols = 0;
			for (String symbolTo: getConfabulator(nConf).getSymbolsByLevel(1)) {
				if (symbols<io.getThinkWidth()) {
					List<SymbolLink> links = new ArrayList<SymbolLink>(sequenceLinks.getSymbolLinkSequencesBySymbolTo(symbolTo,distance,1));
					io.incrementFiredLinks(fireLinksIntoConfabulator(conf,links,false));
					break;
				} else {
					getConfabulator(nConf).setSymbolLevel(symbolTo,0);
				}
				symbols++;
			}
			distance++;

			winner = getConfabulatorWinningSymbol(conf);
			if (winner.length()>0) {
				break;
			}
		}
		return winner;
	}
	
	private void debugConfabulators(ConInputOutput io, String completedAction) {
		StringBuilder line = new StringBuilder();
		line.append(completedAction);
		for (Entry<Integer,ConSymbolLevels> entry: confabulators.entrySet()) {
			List<String> activeSymbols = entry.getValue().getSymbolsByLevel(1);
			if (activeSymbols.size()>0) {
				line.append("\n");
				line.append(entry.getKey() + 1);
				if (entry.getKey()>=9) {
					line.append(": ");
				} else {
					line.append(" : ");
				}
				int added = 0;
				for (String activeSymbol: activeSymbols) {
					if (added>=5) {
						line.append(" ... ");
						line.append("[");
						line.append(activeSymbols.size() - added);
						line.append("]");
						break;
					}
					line.append(" ");
					line.append(activeSymbol);
					line.append(" (");
					line.append(entry.getValue().getSymbolLevel(activeSymbol));
					line.append(")");
					added++;
				}
			}
		}
		io.addLogLine(line.toString());
		
	}
	
	private String[] getConclusions(List<String> workingExtension) {
		String[] conclusions = new String[module.getMaxSequenceDistance()];
		int get = workingExtension.size() - 1;
		for (int i = (module.getMaxSequenceDistance() - 1); i >= 0; i--) {
			if (get>=0) {
				conclusions[i] = workingExtension.get(get);
			} else {
				conclusions[i] = "";
			}
			get--;
		}
		return conclusions;
	}
	
	private long fireLinksIntoConfabulator(int num,List<SymbolLink> links,boolean forward) {
		long firedLinks = 0;
		for (SymbolLink link: links) {
			if (forward) {
				fireLinkIntoConfabulator(num,link,link.getSymbolTo());
			} else {
				fireLinkIntoConfabulator(num,link,link.getSymbolFrom());
			}
			firedLinks++;
		}
		return firedLinks; 
	}

	private void fireLinkIntoConfabulator(int num,SymbolLink link,String symbol) {
		int numTo = 0;
		int maxTo = 0;
		int maxCount = 0;
		if (link instanceof SymbolLinkContext) {
			numTo = symbolNumContextLinksTo.get(link.getSymbolTo());
			maxTo = maxContextLinksTo;
			maxCount = maxContextLinkCount;
		} else if (link instanceof SymbolLinkSequence) {
			numTo = symbolNumSequenceLinksTo.get(link.getSymbolTo());
			maxTo = maxSequenceLinksTo;
			maxCount = maxSequenceLinkCount;
		}
		if (numTo==0) {
			numTo = 1;
		}
		if (maxTo==0) {
			maxTo = 1;
		}
		if (maxCount==0) {
			maxCount = 1;
		}
		long add = 11 - ((numTo * 10) / maxTo);
		long addCount = 1 + ((link.getCount() * 10) / maxCount);
		long level = getConfabulatorSymbolLevel(num,symbol);
		setConfabulatorSymbolLevel(num,symbol,(level + add + addCount));
	}
	
	private void resetConfabulators(int num) {
		confabulators = new TreeMap<Integer,ConSymbolLevels>();
		for (int i = 0; i < num; i++) {
			confabulators.put(i,new ConSymbolLevels());
		}
	}

	private long initializeConfabulatorContext(List<String> contextSymbols) {
		long firedLinks = 0 ;
		List<String> symbols = new ArrayList<String>();
		List<SymbolLink> cLinks = new ArrayList<SymbolLink>();
		for (String contextSymbol: contextSymbols) {
			for (SymbolLinkContext add: contextLinks.getSymbolLinkContextsBySymbolTo(contextSymbol,1)) {
				cLinks.add(add);
				if (!symbols.contains(add.getSymbolFrom())) {
					symbols.add(add.getSymbolFrom());
				}
			}
		}
		
		// Remove symbols for which there is no forward sequence link conclusion in previous modules
		List<SymbolLinkSequence> sLinks = new ArrayList<SymbolLinkSequence>();
		for (Entry<Integer,ConSymbolLevels> entry: confabulators.entrySet()) {
			if (entry.getKey()<module.getMaxSequenceDistance()) {
				List<String> conclusions = entry.getValue().getSymbolsByLevel(1);
				String conclusion = "";
				if (conclusions.size()==1) {
					conclusion = conclusions.get(0);
				}
				if (conclusion.length()>0) {
					int distance = module.getMaxSequenceDistance() - entry.getKey();
					sLinks.addAll(sequenceLinks.getSymbolLinkSequencesBySymbolFrom(conclusion,distance,1));
				}
			}
		}
		List<String> allowedNextSymbols = new ArrayList<String>(symbols);
		if (symbols.size()>0 && sLinks.size()>0) {
			List<String> testSymbols = new ArrayList<String>(allowedNextSymbols);
			for (String symbol: testSymbols) {
				boolean found = false;
				for (SymbolLinkSequence sLink: sLinks) {
					if (sLink.getSymbolTo().equals(symbol)) {
						found = true;
						break;
					}
				}
				if (!found) {
					allowedNextSymbols.remove(symbol);
				}
			}
		}
		
		for (Entry<Integer,ConSymbolLevels> entry: confabulators.entrySet()) {
			if (entry.getKey()>=module.getMaxSequenceDistance()) {
				if (entry.getKey()==module.getMaxSequenceDistance()) {
					entry.getValue().getActiveSymbols().addAll(allowedNextSymbols);
				} else {
					entry.getValue().getActiveSymbols().addAll(symbols);
				}
				firedLinks = fireLinksIntoConfabulator(entry.getKey(),cLinks,false);
			}
		}
		return firedLinks;
	}
	
	private void setConfabulatorSymbolLevel(int num,String symbol,long level) {
		ConSymbolLevels con = getConfabulator(num);
		if (con!=null) {
			con.setSymbolLevel(symbol,level);
		}
	}

	private long getConfabulatorSymbolLevel(int num,String symbol) {
		long r = 0;
		ConSymbolLevels con = getConfabulator(num);
		if (con!=null) {
			r = con.getSymbolLevel(symbol);
		}
		return r;
	}

	private String getConfabulatorWinningSymbol(int num) {
		String winner = "";
		ConSymbolLevels con = getConfabulator(num);
		if (con!=null) {
			List<String> symbols = con.getSymbolsByLevel(1);
			if (symbols.size()==1) {
				winner = symbols.get(0);
			} else if (symbols.size()>1) {
				long level1 = getConfabulatorSymbolLevel(num,symbols.get(0));
				long level2 = getConfabulatorSymbolLevel(num,symbols.get(1));
				if (level1>level2) {
					winner = symbols.get(0);
				}
			}
		}
		return winner;
	}
	
	private ConSymbolLevels getConfabulator(int num) {
		return confabulators.get(num);
	}
	
	private void resetDerrivedContextLinkData() {
		symbolNumContextLinksTo.clear();
		maxContextLinksTo = 0;
		maxContextLinkCount = 0;
		allContextSymbols.clear();
		for (SymbolLinkContext cLink: contextLinks.getSymbolLinkContextsAsList()) {
			Integer num = symbolNumContextLinksTo.get(cLink.getSymbolTo());
			if (num==null) {
				num = 1;
			} else {
				num++;
			}
			symbolNumContextLinksTo.put(cLink.getSymbolTo(),num);
			if (num>maxContextLinksTo) {
				maxContextLinksTo = num;
			}
			if (cLink.getCount()>maxContextLinkCount) {
				maxContextLinkCount = (int) cLink.getCount();
			}
			if (!allContextSymbols.contains(cLink.getSymbolTo())) {
				allContextSymbols.add(cLink.getSymbolTo());
			}
		}
	}

	private void resetDerrivedSequenceLinkData() {
		allSequenceSymbols.clear();
		symbolNumSequenceLinksTo.clear();
		maxSequenceLinksTo = 0;
		maxSequenceLinkCount = 0;
		allSequenceSymbols.clear();
		for (SymbolLinkSequence sLink: sequenceLinks.getSymbolLinkSequencesAsList()) {
			Integer num = symbolNumSequenceLinksTo.get(sLink.getSymbolTo());
			if (num==null) {
				num = 1;
			} else {
				num++;
			}
			symbolNumSequenceLinksTo.put(sLink.getSymbolTo(),num);
			if (num>maxSequenceLinksTo) {
				maxSequenceLinksTo = num;
			}
			if (sLink.getCount()>maxSequenceLinkCount) {
				maxSequenceLinkCount = (int) sLink.getCount();
			}
			if (!allSequenceSymbols.contains(sLink.getSymbolFrom())) {
				allSequenceSymbols.add(sLink.getSymbolFrom());
			}
			if (!allSequenceSymbols.contains(sLink.getSymbolTo())) {
				allSequenceSymbols.add(sLink.getSymbolTo());
			}
		}
	}
}
