package nl.zeesoft.zsc.confabulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zsc.Generic;
import nl.zeesoft.zsc.confabulator.confabulations.ContextConfabulation;
import nl.zeesoft.zsc.confabulator.confabulations.CorrectionConfabulation;
import nl.zeesoft.zsc.confabulator.confabulations.ExtensionConfabulation;

/**
 * A Confabulator can learn symbol sequences (i.e. sentences), optionally combined with certain context symbols (i.e. subject(s)).
 * When trained, a Confabulator can be used to;
 * - Confabulate one or more context symbols for a certain input sequence.
 * - Confabulate a correction for a certain input sequence, optionally restricted by one or more context symbols.
 * - Confabulate a starter sequence or an extension for an input sequence, optionally restricted by one or more context symbols.
 * 
 * By default, confabulators limit their maximum link distance to 8 and their maximum link count to 1000.
 * Deviations from these defaults can be specified using the Confabulator initialization method.
 * When the link count of one of the links hits the specified count maximum, all Confabulator link counts are divided by 2.
 * Links that have a count of 1 are removed by this division process.
 * When repeatedly confronted with a slowly changing training set, this mechanism allows the Confabulator to slowly forget links that are no longer part of the training set.
 */
public final class Confabulator extends ConfabulatorTrainer {
	private	boolean						log							= false;
	
	private SortedMap<Integer,Module>	modules						= new TreeMap<Integer,Module>();

	// Derived link data
	private SortedMap<String,Integer>	symbolNumContextLinksTo		= new TreeMap<String,Integer>(); 
	private SortedMap<String,Integer>	symbolNumSequenceLinksTo	= new TreeMap<String,Integer>(); 
	private int							maxSequenceLinksTo			= 0;
	private int							maxContextLinksTo			= 0;
	private int							maxCount					= 0;
	private List<String>				allSequenceSymbols			= new ArrayList<String>();
	private List<String>				allContextSymbols			= new ArrayList<String>();
	
	public Confabulator() {
		super(null);
	}
	
	public Confabulator(Messenger msgr) {
		super(msgr);
	}
	
	/**
	 * Uses the confabulator link data to do confabulations.
	 * 
	 * @param confab The confabulation object to use
	 */
	public void confabulate(ConfabulationObject confab) {
		confab.setOutput(new StringBuilder());
		confab.setLog(new StringBuilder());
		lockMe(this);
		if (getDeriveLinkDataNoLock()) {
			deriveLinkDataNoLock();
		}
		if (confab instanceof ContextConfabulation) {
			confabulateContext((ContextConfabulation) confab);
		} else if (confab instanceof ExtensionConfabulation) {
			confabulateExtension((ExtensionConfabulation) confab);
		} else if (confab instanceof CorrectionConfabulation) {
			confabulateCorrection((CorrectionConfabulation) confab);
		} else {
			if (log) {
				confab.addLogLine("ERROR: " + this.getClass().getName() + " does not support " + confab.getClass().getName() + " objects");
			}
		}
		unlockMe(this);
	}
	
	@Override
	public void initialize(int maxLinkDistance, int maxLinkCount, List<Link> links) {
		super.initialize(maxLinkDistance,maxLinkCount,links);
		lockMe(this);
		deriveLinkDataNoLock();
		unlockMe(this);
	}

	/**
	 * Indicates confabulation log statements will be added to the confabulation log.
	 * 
	 * @return True if confabulation log statements will be added to the confabulation log
	 */
	public boolean isLog() {
		boolean r = false;
		lockMe(this);
		r = log;
		unlockMe(this);
		return r;
	}

	/**
	 * Indicates confabulation log statements should be added to the confabulation log.
	 * 
	 * @param log True if confabulation log statements should be added to the confabulation log
	 */
	public void setLog(boolean log) {
		lockMe(this);
		this.log = log;
		unlockMe(this);
	}
	
	/**
	 * Returns a unique list of context symbols contained in the link data.
	 * 
	 * @return a unique list of context symbols contained in the link data
	 */
	public List<String> getAllContextSymbols() {
		List<String> r = null;
		lockMe(this);
		r = new ArrayList<String>(allContextSymbols);
		unlockMe(this);
		return r;
	}
	
	/**
	 * Returns a unique list of sequence symbols contained in the link data.
	 * 
	 * @return a unique list of sequence symbols contained in the link data
	 */
	public List<String> getAllSequenceSymbols() {
		List<String> r = null;
		lockMe(this);
		r = new ArrayList<String>(allSequenceSymbols);
		unlockMe(this);
		return r;
	}
	
	private void confabulateContext(ContextConfabulation confab) {
		resetModules(1);
		if (confab.getSequence().length()>0) {
			List<String> sequenceSymbols = confab.getSequence().toSymbols();
			int i = 0;
			for (String symbol: sequenceSymbols) {
				String symbolTo = null;
				for (int distance = 1; distance<=getMaxLinkDistanceNoLock(); distance++) {
					int index = i + distance;
					if (index>=sequenceSymbols.size()) {
						break;
					} else {
						symbolTo = sequenceSymbols.get(index);
					}
					List<Link> lnks = getLinksBySymbolFromDistanceToNoLock(symbol,distance,symbolTo);
					fireLinksIntoModule(confab,0,lnks,true,true);
				}
			}
		}
		String winner = "";
		if (!confab.isOutputAllSymbols()) {
			winner = modules.get(0).getWinningModuleSymbol();
		}
		if (winner.length()>0) {
			confab.addSymbolToOutputSequence(winner);
			confab.getSymbolLevels().add(modules.get(0).getSymbolLevel(winner));
			if (log) {
				debugModules(confab,"Confabulated winning context symbol: " + winner);
			}
		} else {
			StringBuilder sStr = new StringBuilder();
			for (String symbol: modules.get(0).getSymbolsByLevel(1)) {
				confab.addSymbolToOutputSequence(symbol);
				confab.getSymbolLevels().add(modules.get(0).getSymbolLevel(symbol));
				if (sStr.length()>0) {
					sStr.append(", ");
				}
				sStr.append(symbol);
			}
			if (log) {
				if (confab.getOutput().length()>0) {
					debugModules(confab,"Confabulated context symbols: " + sStr);
				} else {
					debugModules(confab,"Failed to confabulate context");
				}
			}
		}
	}
	
	private void confabulateCorrection(CorrectionConfabulation confab) {
		confab.getCorrectionKeys().clear();
		confab.getCorrectionValues().clear();
		List<String> workingExtension = new ArrayList<String>();
		int i = 0;
		List<String> sequenceSymbols = confab.getSequence().toSymbols();
		for (String symbol: sequenceSymbols) {
			String winner = "";
			if (!allSequenceSymbols.contains(symbol)) {
				List<String> futureConclusions = new ArrayList<String>();
				for (int f = (i + 1); f < sequenceSymbols.size(); f++) {
					String conclusion = sequenceSymbols.get(f);
					if (!allSequenceSymbols.contains(conclusion)) {
						conclusion = "";
					}
					futureConclusions.add(conclusion);
				}
				winner = confabulateForwardAndBackward(confab, workingExtension, futureConclusions);
				if (log) {
					if (winner.length()>0) {
						confab.addCorrection(symbol,winner);
						debugModules(confab,"Confabulated replacement symbol: " + winner + ", for: " + symbol);
					} else {
						debugModules(confab,"Failed to confabulate replacement for symbol: " + symbol);
					}
				}
			}
			if (winner.length()==0) {
				winner = symbol;
			}
			if (winner.length()>0) {
				workingExtension.add(winner);
				confab.addSymbolToOutputSequence(winner);
			}
			i++;
		}
	}
	
	private void confabulateExtension(ExtensionConfabulation confab) {
		List<String> workingExtension = confab.getSequence().toSymbols();
		int added = 0;
		String next = "";
		if (workingExtension.size()==0) {
			next = confabulateFirstSymbol(confab);
			if (log) {
				if (next.length()>0) {
					debugModules(confab,"Confabulated first symbol: " + next);
				} else {
					debugModules(confab,"Failed to confabulate first symbol");
				}
			}
		} else {
			next = confabulateForwardAndBackward(confab, workingExtension);
			if (log) {
				debugModules(confab,"Confabulated next symbol: " + next);
			} else {
				debugModules(confab,"Failed to confabulate next symbol");
			}
		}
		while(next.length()>0) {
			confab.addSymbolToOutputSequence(next);
			added++;
			if (added>=confab.getMaxOutputSymbols()) {
				break;
			}
			workingExtension.add(next);
			next = confabulateForwardAndBackward(confab, workingExtension);
			if (log) {
				if (next.length()>0) {
					debugModules(confab,"Confabulated next symbol: " + next);
				} else {
					debugModules(confab,"Failed to confabulate next symbol");
				}
			}
		}
	}

	private String confabulateFirstSymbol(ExtensionConfabulation confab) {
		String winner = "";
		String[] conclusions = getConclusions(new ArrayList<String>(),null);
		applyConclusionsToModules(conclusions);
		List<Link> lnks = getLinksByStartNoLock(confab.getContext().toSymbols());		
		fireLinksIntoModule(confab,getMaxLinkDistanceNoLock(),lnks,false,false);
		winner = modules.get(getMaxLinkDistanceNoLock()).getWinningModuleSymbol();
		return winner;
	}
	
	private void fireLinksIntoModule(ConfabulationObject confab, int num, List<Link> lnks,boolean forward,boolean context) {
		for (Link lnk: lnks) {
			if (context) {
				long level = modules.get(num).getSymbolLevel(lnk.getSymbolContext());
				 modules.get(num).setSymbolLevel(lnk.getSymbolContext(),level + getAddLevelForLink(confab,lnk,true));
			} else if (forward) {
				long level =  modules.get(num).getSymbolLevel(lnk.getSymbolTo());
				modules.get(num).setSymbolLevel(lnk.getSymbolTo(),level + getAddLevelForLink(confab,lnk,false));
			} else {
				long level =  modules.get(num).getSymbolLevel(lnk.getSymbolFrom());
				modules.get(num).setSymbolLevel(lnk.getSymbolFrom(),level + getAddLevelForLink(confab,lnk,false));
			}
		}
	}
	
	private String[] getConclusions(List<String> workingExtension,List<String> futureConclusions) {
		String[] conclusions = new String[(getMaxLinkDistanceNoLock() * 2) + 1];
		int get = workingExtension.size() - 1;
		for (int i = (getMaxLinkDistanceNoLock() - 1); i >= 0; i--) {
			if (get>=0) {
				conclusions[i] = workingExtension.get(get);
			} else {
				conclusions[i] = "";
			}
			get--;
		}
		conclusions[getMaxLinkDistanceNoLock()] = "";
		get = 0;
		for (int i = (getMaxLinkDistanceNoLock() + 1); i < conclusions.length; i++) {
			if (futureConclusions!=null && futureConclusions.size()>0 && get<futureConclusions.size()) {
				conclusions[i] = futureConclusions.get(get);
			} else {
				conclusions[i] = "";
			}
			get++;
		}
		return conclusions;
	}

	private void applyConclusionsToModules(String[] conclusions) {
		resetModules(conclusions.length);
		for (int c = 0; c < conclusions.length; c++) {
			if (conclusions[c].length()>0 && c!=getMaxLinkDistanceNoLock()) {
				modules.get(c).setConclusion(conclusions[c]);
			}
		}
	}

	private String confabulateForwardAndBackward(ConfabulationSequenceObject confab,List<String> workingExtension) {
		return confabulateForwardAndBackward(confab,workingExtension,new ArrayList<String>());
	}

	private String confabulateForwardAndBackward(ConfabulationSequenceObject confab,List<String> workingExtension,List<String> futureConclusions) {
		String winner = "";
		boolean confabulate = true;
		List<String> newFutureConclusions = new ArrayList<String>();
		int filledConclusions = 0;
		for (String conclusion: futureConclusions) {
			if (conclusion.length()>0) {
				filledConclusions++;
			}
		}
		while (confabulate) {
			String[] conclusions = getConclusions(workingExtension,futureConclusions);
			applyConclusionsToModules(conclusions);
			winner = confabulateForward(confab,conclusions);
			if (winner.length()==0) {
				winner = confabulateBackward(confab,conclusions);
			}
			if (winner.length()==0) {
				newFutureConclusions = new ArrayList<String>();
				int newFilledConclusions = 0;
				for (int nM = (getMaxLinkDistanceNoLock() + 1); nM<conclusions.length; nM++) {
					String nWinner = modules.get(nM).getWinningModuleSymbol();
					newFutureConclusions.add(nWinner);
					if (nWinner.length()>0) {
						newFilledConclusions++;
					}
				}
				if (filledConclusions==newFilledConclusions) {
					confabulate = false;
				} else {
					filledConclusions = newFilledConclusions;
					futureConclusions = newFutureConclusions;
				}
			} else {
				confabulate = false;
			}
		}
		return winner;
	}
	
	private String confabulateForward(ConfabulationSequenceObject confab,String[] conclusions) {
		String winner = "";
		for (int c = getMaxLinkDistanceNoLock(); c < conclusions.length; c++) {
			boolean foundLinks = false;
			if (conclusions[c].length()==0) {
				int distance = 0;
				for (int pM = (c - 1); pM>=0; pM--) {
					distance++;
					int s = 0;
					for (String symbolFrom: modules.get(pM).getSymbolsByLevel(1)) {
						if (s>=confab.getMaxWidth()) {
							modules.get(pM).setSymbolLevel(symbolFrom,0);
						} else {
							List<Link> lnks = getLinksBySymbolFromDistanceNoLock(symbolFrom,distance,confab.getContext().toSymbols());
							if (lnks.size()>0) {
								foundLinks = true;
							}
							if (confab.isStrict() && distance==1) {
								for (Link lnk: lnks) {
									if (!modules.get(c).getActiveSymbols().contains(lnk.getSymbolTo())) {
										modules.get(c).getActiveSymbols().add(lnk.getSymbolTo());
									}
								}
							}
							fireLinksIntoModule(confab,c,lnks,true,false);
						}
						s++;
					}
					if (!foundLinks && confab.isStrict() && c==getMaxLinkDistanceNoLock() && distance==1) {
						break;
					}
					if (distance>=getMaxLinkDistanceNoLock()) {
						break;
					}
				}
			}
			if (c==getMaxLinkDistanceNoLock()) {
				if (!foundLinks && confab.isStrict()) {
					break;
				}
				if (!confab.isForceMaxDepth()) {
					winner = modules.get(getMaxLinkDistanceNoLock()).getWinningModuleSymbol();
					if (winner.length()>0) {
						break;
					}
				}
			}
		}
		return winner;
	}

	private String confabulateBackward(ConfabulationSequenceObject confab,String[] conclusions) {
		String winner = "";
		int distance = 0;
		for (int c = (getMaxLinkDistanceNoLock() + 1); c < conclusions.length; c++) {
			distance++;
			String symbolTo = modules.get(c).getWinningModuleSymbol();
			if (symbolTo.length()>0) {
				List<Link> lnks = getLinksBySymbolToDistanceNoLock(symbolTo,distance,confab.getContext().toSymbols());
				fireLinksIntoModule(confab,getMaxLinkDistanceNoLock(),lnks,false,false);
			}
			if (!confab.isForceMaxDepth()) {
				winner = modules.get(getMaxLinkDistanceNoLock()).getWinningModuleSymbol();
				if (winner.length()>0) {
					break;
				}
			}
		}
		if (confab.isForceMaxDepth()) {
			winner = modules.get(getMaxLinkDistanceNoLock()).getWinningModuleSymbol();
		}
		return winner;
	}
	
	private void resetModules(int num) {
		modules.clear();
		for (int i = 0; i < num; i++) {
			modules.put(i,new Module());
		}
	}
	
	private void debugModules(ConfabulationObject confab, String completedAction) {
		StringBuilder line = new StringBuilder();
		line.append(completedAction);
		if (confab.isLogModuleSymbolLevels()) {
			for (Entry<Integer,Module> entry: modules.entrySet()) {
				List<String> activeSymbols = entry.getValue().getSymbolsByLevel(1);
				if (activeSymbols.size()>0) {
					line.append("\n");
					line.append("Module ");
					line.append(Generic.minStrInt(entry.getKey() + 1,2));
					line.append(": ");
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
		}
		confab.addLogLine(line.toString());
	}
	
	private List<Link> getLinksBySymbolFromDistanceToNoLock(String symbolFrom, int distance, String symbolTo) {
		List<Link> r = new ArrayList<Link>();
		for (Link lnk: getLinksNoLock()) {
			if (lnk.getSymbolFrom().equals(symbolFrom) && lnk.getDistance()==distance && lnk.getSymbolTo().equals(symbolTo) && lnk.getCount()>1 && lnk.getSymbolContext().length()>0) {
				r.add(lnk);
			}
		}
		return r;
	}

	private List<Link> getLinksByStartNoLock(List<String> context) {
		List<Link> r = new ArrayList<Link>();
		for (String symbolFrom: allSequenceSymbols) {
			Integer num = symbolNumSequenceLinksTo.get(symbolFrom);
			if (num==null || ((int) num == 0)) {
				for (Link lnk: getLinksNoLock()) {
					if (lnk.getSymbolFrom().equals(symbolFrom) && (context==null || context.size()==0 || context.contains(lnk.getSymbolContext())) && lnk.getCount()>1) {
						r.add(lnk);
					}
				}
			}
		}
		return r;
	}

	private List<Link> getLinksBySymbolFromDistanceNoLock(String symbolFrom, int distance, List<String> context) {
		List<Link> r = new ArrayList<Link>();
		for (Link lnk: getLinksNoLock()) {
			if (lnk.getSymbolFrom().equals(symbolFrom) && lnk.getDistance()==distance && (context==null || context.size()==0 || context.contains(lnk.getSymbolContext())) && lnk.getCount()>1) {
				r.add(lnk);
			}
		}
		return r;
	}

	private List<Link> getLinksBySymbolToDistanceNoLock(String symbolTo, int distance, List<String> context) {
		List<Link> r = new ArrayList<Link>();
		for (Link lnk: getLinksNoLock()) {
			if (lnk.getSymbolTo().equals(symbolTo) && lnk.getDistance()==distance && (context==null || context.size()==0 || context.contains(lnk.getSymbolContext())) && lnk.getCount()>1) {
				r.add(lnk);
			}
		}
		return r;
	}
	
	private void deriveLinkDataNoLock() {
		symbolNumContextLinksTo.clear();
		symbolNumSequenceLinksTo.clear();
		maxContextLinksTo = 0;
		maxSequenceLinksTo = 0;
		maxCount = 1;
		allContextSymbols.clear();
		allSequenceSymbols.clear();
		for (Link lnk: getLinksNoLock()) {
			if (lnk.getSymbolContext().length()>0) {
				if (!allContextSymbols.contains(lnk.getSymbolContext())) {
					allContextSymbols.add(lnk.getSymbolContext());
				}
				Integer num = symbolNumContextLinksTo.get(lnk.getSymbolContext());
				
				if (num==null) {
					num = 1;
				} else {
					num++;
				}
				symbolNumContextLinksTo.put(lnk.getSymbolContext(),num);
				if (num>maxContextLinksTo) {
					maxContextLinksTo = num;
				}
			}
			if (!allSequenceSymbols.contains(lnk.getSymbolFrom())) {
				allSequenceSymbols.add(lnk.getSymbolFrom());
			}
			if (!allSequenceSymbols.contains(lnk.getSymbolTo())) {
				allSequenceSymbols.add(lnk.getSymbolTo());
			}
			Integer num = symbolNumSequenceLinksTo.get(lnk.getSymbolTo());
			if (num==null) {
				num = 1;
			} else {
				num++;
			}
			symbolNumSequenceLinksTo.put(lnk.getSymbolTo(),num);
			if (num>maxSequenceLinksTo) {
				maxSequenceLinksTo = num;
			}
			if (lnk.getCount()>maxCount) {
				maxCount = lnk.getCount();
			}
		}
		setDeriveLinkDataNoLock(false);
	}
	
	private long getAddLevelForLink(ConfabulationObject confab, Link lnk,boolean context) {
		long add = 0;
		int numTo = 0;
		int maxTo = 0;
		if (context) {
			numTo = symbolNumContextLinksTo.get(lnk.getSymbolContext());
			maxTo = maxContextLinksTo;
		} else {
			numTo = symbolNumSequenceLinksTo.get(lnk.getSymbolTo());
			maxTo = maxSequenceLinksTo;
		}
		if (numTo==0) {
			numTo = 1;
		}
		if (maxTo==0) {
			maxTo = 1;
		}
		add = (confab.getAccuracy() + 1) - ((numTo * confab.getAccuracy()) / maxTo);
		add += 1 + ((lnk.getCount() * confab.getAccuracy()) / maxCount);
		return add;
	}
}
