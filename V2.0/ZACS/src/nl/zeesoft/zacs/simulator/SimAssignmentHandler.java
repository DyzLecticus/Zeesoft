package nl.zeesoft.zacs.simulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zacs.database.model.Assignment;
import nl.zeesoft.zacs.database.model.ContextLink;
import nl.zeesoft.zacs.database.model.Control;
import nl.zeesoft.zacs.database.model.Module;
import nl.zeesoft.zacs.database.model.ModuleSymbol;
import nl.zeesoft.zacs.database.model.Symbol;
import nl.zeesoft.zacs.database.model.SymbolLink;
import nl.zeesoft.zacs.database.model.ZACSModel;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.model.MdlProperty;
import nl.zeesoft.zodb.database.request.ReqUpdate;

public class SimAssignmentHandler extends Locker {
	private boolean								debug					= false;
	private SimControllerModules				modules					= null;
	private SimControllerSymbols				symbols					= null;
	private int									countSymbolMaximum		= 0;
	private int									countContextMaximum		= 0;
	private int									levelMaximum			= 0;
	private int									levelFireBase			= 0;
	private int									contextSymbolMaximum	= 0;
	private boolean								contextAssociate		= false;
	
	private Assignment							workingAssignment		= null;
	private Module								workingModule			= null;
	private List<Symbol>						workingContext			= new ArrayList<Symbol>();
	private List<String>						workingOutput			= new ArrayList<String>();
	
	private long								duration				= 0;
	
	protected SimAssignmentHandler(boolean dbg) {
		debug = dbg;
		modules = SimController.getInstance().getModules();
		symbols = SimController.getInstance().getSymbols();
	}
	
	protected final void initializeParameters(Control c) {
		levelMaximum = c.getLevelMaximum();
		levelFireBase = c.getLevelFireBase();
		contextSymbolMaximum = c.getContextSymbolMaximum();
		countSymbolMaximum = c.getCountSymbolMaximum();
		countContextMaximum = c.getCountContextMaximum();
		contextAssociate = c.isContextAssociate();
	}

	public final void setWorkingAssignment(Assignment as) {
		lockMe(this);
		workingAssignment = as;
		if (workingAssignment!=null) {
			if (workingAssignment.getWorkingModule()!=null) {
				workingModule = workingAssignment.getWorkingModule();
				resetModules();
				parseWorkingOutputFromAssignment();
				initializeModuleInputs();
				initializeModuleOutputs(workingModule,modules.getModulesAsList(),symbols.getSymbolsFromStrings(workingOutput));
				debugModuleInputOutputSymbols();
				debugWorkingContextSymbols();
				workingAssignment.appendLogLine(debug,"Continuing assignment: " + workingAssignment.getName());
				if (!debug) {
					Messenger.getInstance().debug(this,"Continuing assignment: " + workingAssignment.getName());
				}
			} else {
				initializeWorkingAssignment();
			}
		}
		unlockMe(this);
	}

	public final Assignment getWorkingAssignment() {
		lockMe(this);
		Assignment r = workingAssignment;
		unlockMe(this);
		return r;
	}

	public final void stopWorkingAssignment() {
		lockMe(this);
		if (workingAssignment!=null) {
			Messenger.getInstance().debug(this,"Saving working assignment state ...");
			debugModuleInputOutputSymbols();
			debugWorkingContextSymbols();
			saveAssignment(workingAssignment);
			Messenger.getInstance().debug(this,"Saved working assignment state");
		}
		unlockMe(this);
	}
	
	public final void workOnWorkingAssignment() {
		lockMe(this);
		if (workingAssignment!=null) {
			resetDuration();
			workOnAssignment();
			if (workingAssignment!=null) {
				saveDuration(workingAssignment);
			}
		}
		unlockMe(this);
	}
	
	/**
	 * Determine next output symbol
	 */
	private void workOnAssignment() {
		List<Module> mods = modules.getModulesAsList();

		int firedSymbolLinks = confabulate(workingModule,mods,workingContext,workingAssignment.getThinkWidth(),workingAssignment.isThinkFast(),null);
		
		workingAssignment.incrementFiredSymbolLinks(firedSymbolLinks);
		ModuleSymbol winningSymbol = workingModule.getWinningModuleSymbol();
		
		if (winningSymbol!=null) {
			workingAssignment.appendLogLine(debug,"Module: " + workingModule.getNum() + "; conclusion: " + winningSymbol.getSymbol().getCode() + ", level: " + winningSymbol.getLevel() + ", fired links: " + firedSymbolLinks);
			workingOutput.add(winningSymbol.getSymbol().getCode());
			updateWorkingContextForConfabulatedSymbol(winningSymbol.getSymbol());
			workingModule.setSymbolOutput(winningSymbol.getSymbol());
			setWorkingModule(modules.getNextModuleForModule(workingModule));
			saveWorkingOutputInAssignment();
			if (workingOutput.size()>=workingAssignment.getMaxSymbols() ||
				(workingAssignment.isStopOnLineEndSymbol() && Symbol.isLineEndSymbol(winningSymbol.getSymbol().getCode()))
				) {
				finishWorkingAssignment();
			}
		} else {
			List<ModuleSymbol> topSyms = workingModule.getHighestModuleSymbols(3);
			if (topSyms.size()>0) {
				StringBuilder syms = getModuleSymbolLevelString(topSyms);
				if (topSyms.size()>3) {
					List<ModuleSymbol> activatedSyms = workingModule.getActivatedModuleSymbols();
					int remaining = (activatedSyms.size() - topSyms.size());
					if (remaining>0) {
						syms.append(" ... [");
						syms.append(remaining);
						syms.append("]");
					}
				}
				workingAssignment.appendLogLine(debug,"Module: " + workingModule.getNum() + "; uncertain conclusion: " + syms + ", fired links: " + firedSymbolLinks);
				finishWorkingAssignment();
			} else {
				workingAssignment.appendLogLine(debug,"Module: " + workingModule.getNum() + "; no symbols activated");
				finishWorkingAssignment();
			}
		}
	}

	private void updateWorkingContextForConfabulatedSymbol(Symbol symbol) {
		if (workingAssignment.isContextDynamic()) {
			StringBuilder currentContext = getSymbolsAsString(workingContext);
			int firedContextLinks = fireContextLinksForSymbol(symbol,null);
			if (firedContextLinks>0) {
				workingAssignment.incrementFiredContextLinks(firedContextLinks);
			}
			List<Symbol> highestSyms = symbols.getHighestSymbols(1);
			if (highestSyms.size()>0) {
				if (highestSyms.get(0).getLevel()>=levelMaximum) {
					symbols.divideSymbolLevelsBelowMaximum(null,levelMaximum);
				}
				
				List<Symbol> topSyms = symbols.getHighestSymbols(contextSymbolMaximum);
				if (topSyms.size()>0) {
					StringBuilder newContext = getSymbolsAsString(topSyms);
					if (!Generic.stringBuilderEquals(currentContext,newContext)) {
						workingAssignment.appendLogLine(debug,"Update working context: " + getSymbolLevelString(topSyms));
						workingContext.clear();
						workingContext.addAll(topSyms);
					}
				}
				resetContextSymbolLevels(workingContext);
			}
		}
	}
	
	private int confabulate(Module confabModule,List<Module> mods,List<Symbol> workingContext,int thinkWidth,boolean thinkFast,SortedMap<Integer,Symbol> nextSymbols) {
		int firedSymbolLinks = 0;
		int fireLevelBase = levelFireBase;

		resetModuleSymbolLevels(mods);
		resetWorkingContextSymbolsEnabled(mods,(contextSymbolMaximum / 2));
		
		firedSymbolLinks += confabulateForwardBackward(confabModule,mods,fireLevelBase,thinkWidth,thinkFast,false,nextSymbols);
		ModuleSymbol winningSymbol = confabModule.getWinningModuleSymbol();
		
		if (winningSymbol==null && workingContext.size()>(contextSymbolMaximum / 2)) {
			resetModuleSymbolLevels(mods);
			resetWorkingContextSymbolsEnabled(mods,contextSymbolMaximum);
			
			firedSymbolLinks += confabulateForwardBackward(confabModule,mods,fireLevelBase,thinkWidth,thinkFast,false,nextSymbols);
			winningSymbol = confabModule.getWinningModuleSymbol();
		}

		if (winningSymbol==null) {
			resetModuleSymbolLevels(mods);
			
			firedSymbolLinks += confabulateForwardBackward(confabModule,mods,fireLevelBase,thinkWidth,thinkFast,true,nextSymbols);
			winningSymbol = confabModule.getWinningModuleSymbol();
		}
		
		return firedSymbolLinks;
	}

	private int confabulateForwardBackward(Module confabModule,List<Module> mods,int fireLevelBase,int thinkWidth,boolean thinkFast,boolean useCount,SortedMap<Integer,Symbol> nextSymbols) {
		// Fire forward
		int firedSymbolLinks = confabulateForward(confabModule,mods,fireLevelBase,thinkWidth,thinkFast,useCount,nextSymbols);
		debugModuleSymbolLevels(confabModule,mods,false,firedSymbolLinks,fireLevelBase,thinkWidth,thinkFast,useCount);
		ModuleSymbol winningSymbol = confabModule.getWinningModuleSymbol();
		
		if (winningSymbol==null) {
			// Fire backward
			firedSymbolLinks += confabulateBackward(confabModule,mods,fireLevelBase,thinkFast,useCount);
			debugModuleSymbolLevels(confabModule,mods,true,firedSymbolLinks,fireLevelBase,thinkWidth,thinkFast,useCount);
		}
		
		return firedSymbolLinks;
	}
		
	private int confabulateForward(Module confabModule,List<Module> mods,int fireLevelBase,int thinkWidth,boolean thinkFast,boolean useCount,SortedMap<Integer,Symbol> nextSymbols) {
		int firedSymbolLinks = 0;
		Module pMod = modules.getPrevModuleForModule(confabModule);
		for (int i = 0; i < ZACSModel.getNumberOfModules(); i++) {
			SortedMap<Long,List<ModuleSymbol>> pModSyms = getPreviousModuleOutputInputSymbolLists(confabModule,i);
			int distance = 1;
			List<Symbol> allowedSymbols = new ArrayList<Symbol>();
			for (int i2 = 0; i2 < ZACSModel.getNumberOfModules(); i2++) {
				List<ModuleSymbol> pModSymList = pModSyms.get(pMod.getId());
				Symbol nextSymbol = null;
				if (i>0 && nextSymbols!=null) {
					nextSymbol = nextSymbols.get(i);
				}
				if (nextSymbol!=null) {
					ModuleSymbol cModSym = confabModule.getModuleSymbolForSymbolId(nextSymbol.getId());
					cModSym.setLevel(1);
				} else if (pModSymList!=null) {
					ModuleSymbol highest = null;
					for (ModuleSymbol pModSym: pModSymList) {
						List<SymbolLink> linksToWorkingModule = pModSym.getSymbol().getSymbolLinks(distance);
						List<ModuleSymbol> cModSyms = confabModule.getSymbols(linksToWorkingModule);
						int linkNum = 0;
						for (ModuleSymbol cModSym: cModSyms) {
							if ((i==0 || !Symbol.isLineEndSymbol(cModSym.getSymbol().getCode())) && cModSym.getSymbol().isEnabled()) {
								if (i2==0) {
									if (i==0 && nextSymbols!=null) {
										boolean foundAll = true;
										for (Entry<Integer,Symbol> entry: nextSymbols.entrySet()) {
											boolean found = false;
											for (SymbolLink symLink: cModSym.getSymbol().getSymbolLinks(entry.getKey())) {
												if (symLink.getSymbolTo().getId()==entry.getValue().getId()) {
													found = true;
													break;
												}
											}
											if (!found) {
												foundAll = false;
												break;
											}
										}
										if (foundAll) {
											allowedSymbols.add(cModSym.getSymbol());
										}
									} else {
										allowedSymbols.add(cModSym.getSymbol());
									}
								}
								if (allowedSymbols.contains(cModSym.getSymbol())) {
									SymbolLink link = linksToWorkingModule.get(linkNum);
									linkNum++;
									long level = fireLinkInModuleSymbol(link,cModSym,fireLevelBase,useCount);
									if (highest==null || level>=highest.getLevel()) {
										highest = cModSym;
									}
									firedSymbolLinks++;
								}
							} 
						}
					}
					if (highest!=null && highest.getLevel()>=levelMaximum) {
						highest.getModule().divideSymbolLevelsBelowMaximum(null,levelMaximum);
					}
				}
				distance++;
				pMod = modules.getPrevModuleForModule(pMod);
			}
			if ((i==0 || thinkFast) && confabModule.getWinningModuleSymbol()!=null) {
				break;
			}
			List<ModuleSymbol> topSyms = confabModule.getHighestModuleSymbols(thinkWidth);
			if (topSyms.size()==thinkWidth) {
				confabModule.resetSymbolLevelsExcludeModuleSymbols(topSyms);
			}
			pMod = confabModule;
			confabModule = modules.getNextModuleForModule(confabModule);
		}
		return firedSymbolLinks;
	}
	
	private long fireLinkInModuleSymbol(SymbolLink link, ModuleSymbol modSym,int fireLevelBase,boolean useCount) {
		long amount = fireLevelBase;
		long div = link.getSymbolFrom().getTotalSymLinksFrom();
		if (useCount) {
			long perc = (link.getCount() * 100) / countSymbolMaximum;
			perc++;
			amount += (perc * fireLevelBase);
		}
		if (div>0) {
			amount = (amount / div);
		}
		if (amount<=0) {
			amount = 1;
		}
		return modSym.increaseLevel(amount);
	}

	private int confabulateBackward(Module confabModule,List<Module> mods,int fireLevelBase,boolean thinkFast,boolean useCount) {
		int firedSymbolLinks = 0;
		List<ModuleSymbol> confabulatedSymbols = confabModule.getActivatedModuleSymbols(true);
		Module nMod = modules.getNextModuleForModule(confabModule);
		int distance = 1;
		for (int i = 0; i < ZACSModel.getNumberOfModules(); i++) {
			ModuleSymbol highest = null;
			ModuleSymbol nModSym = nMod.getWinningModuleSymbol();
			if (nModSym!=null) {
				for (ModuleSymbol cModSym: confabulatedSymbols) {
					List<SymbolLink> links = cModSym.getSymbol().getSymbolLinks(distance);
					for (SymbolLink link: links) {
						if (link.getSymbolTo().getId()==nModSym.getSymbolId()) {
							long level = fireLinkInModuleSymbol(link,cModSym,fireLevelBase,useCount);
							if (highest==null || level>=highest.getLevel()) {
								highest = cModSym;
							}
							firedSymbolLinks++;
						}
					}
				}
			}
			if (highest!=null && highest.getLevel()>=levelMaximum) {
				highest.getModule().divideSymbolLevelsBelowMaximum(null,levelMaximum);
			}
			if (thinkFast && confabModule.getWinningModuleSymbol()!=null) {
				break;
			}
			nMod = modules.getNextModuleForModule(nMod);
			distance++;
		}
		return firedSymbolLinks;
	}
	
	/**
	 * Returns a map of previous modules and activated symbols for those modules
	 */
	private SortedMap<Long,List<ModuleSymbol>> getPreviousModuleOutputInputSymbolLists(Module confabModule,int activated) {
		SortedMap<Long,List<ModuleSymbol>> modSyms = new TreeMap<Long,List<ModuleSymbol>>();
		// Get output symbols
		Module pMod = confabModule;
		for (int i = 0; i < ZACSModel.getNumberOfModules(); i++) {
			pMod = modules.getPrevModuleForModule(pMod);
			List<ModuleSymbol> symbols = null;
			if (i<activated) {
				symbols = pMod.getActivatedModuleSymbols();
			} else if (pMod.getSymbolOutput()!=null) {
				ModuleSymbol sym = pMod.getModuleSymbolForSymbolId(pMod.getSymbolOutput().getId());
				if (sym!=null) {
					symbols = new ArrayList<ModuleSymbol>();
					symbols.add(sym);
				}
			} else if (pMod.getSymbolInput()!=null) {
				ModuleSymbol sym = pMod.getModuleSymbolForSymbolId(pMod.getSymbolInput().getId());
				if (sym!=null) {
					symbols = new ArrayList<ModuleSymbol>();
					symbols.add(sym);
				}
			}
			modSyms.put(pMod.getId(),symbols);
		}

		return modSyms;
	}

	private void debugModuleSymbolLevels(Module confabModule,List<Module> mods,boolean forwardAndBackward,int firedSymbolLinks,int fireLevelBase,int thinkWidth,boolean thinkFast,boolean useCount) {
		if (workingAssignment.isLogExtended()) {
			StringBuilder modSyms = new StringBuilder();
			Module dMod = confabModule;
			for (int i = 0; i < ZACSModel.getNumberOfModules(); i++) {
				List<ModuleSymbol> topSyms = dMod.getHighestModuleSymbols(3);
				if (topSyms.size()==0) {
					break;
				}
				modSyms.append("\n");
				modSyms.append("  - Module: ");
				modSyms.append(dMod.getNum());
				modSyms.append(", top symbols: ");
				modSyms.append(getModuleSymbolLevelString(topSyms));
				
				List<ModuleSymbol> activatedSyms = dMod.getActivatedModuleSymbols();
				int remaining = (activatedSyms.size() - topSyms.size());
				if (remaining>0) {
					modSyms.append(" ... [");
					modSyms.append(remaining);
					modSyms.append("]");
				}
				
				dMod = modules.getNextModuleForModule(dMod);
			}
			StringBuilder options = new StringBuilder();
			options.append(", fire level base: " + fireLevelBase);
			options.append(", think width: " + thinkWidth);
			if (thinkFast) {
				options.append(", think fast");
			} else {
				options.append(", think deep");
			}
			if (useCount) {
				options.append(", use count");
			}
			if (forwardAndBackward) {
				workingAssignment.appendLogLine(debug,"Module: " + confabModule.getNum() + ", fired links (forward & backward" + options + "): " + firedSymbolLinks + modSyms);
			} else {
				workingAssignment.appendLogLine(debug,"Module: " + confabModule.getNum() + ", fired links (forward" + options + "): " + firedSymbolLinks + modSyms);
			}
		}
	}

	private StringBuilder getModuleSymbolLevelString(List<ModuleSymbol> symbols) {
		StringBuilder syms = new StringBuilder();
		if (symbols.size()>0) {
			for (ModuleSymbol modSym: symbols) {
				if (syms.length()>0) {
					syms.append(" ");
				}
				syms.append(modSym.getSymbol().getCode());
				syms.append(" (");
				syms.append(modSym.getLevel());
				syms.append(")");
			}
		}
		return syms;
	}

	private StringBuilder getSymbolLevelString(List<Symbol> symbols) {
		StringBuilder syms = new StringBuilder();
		if (symbols.size()>0) {
			for (Symbol sym: symbols) {
				if (syms.length()>0) {
					syms.append(" ");
				}
				syms.append(sym.getCode());
				syms.append(" (");
				syms.append(sym.getLevel());
				syms.append(")");
			}
		}
		return syms;
	}
	
	private void debugModuleInputOutputSymbols() {
		List<Module> mods = modules.getModulesAsList();
		for (Module mod: mods) {
			String pointer = "   ";
			String in = "";
			String out = "";
			if (mod.getSymbolInput()!=null) {
				in = mod.getSymbolInput().getCode();
			}
			if (mod.getSymbolOutput()!=null) {
				out = mod.getSymbolOutput().getCode();
			}
			if (mod==workingModule) {
				pointer = "-> ";
			}
			Messenger.getInstance().debug(this,pointer + "Module: " + mod.getNum() + " < " + in + " > " + out);
		}
	}

	private void debugWorkingContextSymbols() {
		Messenger.getInstance().debug(this,"Working context symbols: " + getSymbolLevelString(workingContext));
	}
	
	private void initializeWorkingAssignment() {
		workingAssignment.setInputContext(new StringBuilder());
		workingAssignment.setOutput(new StringBuilder());
		workingAssignment.setOutputContext(new StringBuilder());
		workingAssignment.setWorkingOutput(new StringBuilder());
		workingAssignment.setWorkingContext(new StringBuilder());
		workingAssignment.setLog(new StringBuilder());
		workingAssignment.setFiredSymbolLinks(0);
		workingAssignment.setFiredContextLinks(0);
		workingAssignment.setDateTimeFinished(0);
		workingAssignment.setDurationMilliseconds(0);
		workingAssignment.setNumberOfSymbols(0);
		workingAssignment.setCorrectedInput(new StringBuilder());
		workingAssignment.setCorrectedInputSymbols(new StringBuilder());
		setWorkingModule(null);
		
		StringBuilder msg = new StringBuilder();
		msg.append("Initializing assignment: ");
		msg.append(workingAssignment.getName());
		msg.append(" (");
		if (workingAssignment.isThinkFast()) {
			msg.append("Think fast");
		} else {
			msg.append("Think deep");
		}
		if (workingAssignment.isContextDynamic()) {
			msg.append(", context dynamic");
		}
		if (workingAssignment.isCorrectInput()) {
			msg.append(", correct input");
		}
		if (workingAssignment.isCorrectLineEnd()) {
			msg.append(", correct line end");
		}
		if (workingAssignment.isCorrectInputOnly()) {
			msg.append(", correct input only");
		}
		msg.append(")");
		workingAssignment.appendLogLine(debug,msg.toString());
		
		List<String> allSymbols = workingAssignment.getSymbolList(false);
		List<Symbol> workingSymbols = symbols.getSymbolsFromStrings(allSymbols);
		if (workingSymbols.size()==0 && !workingAssignment.isCorrectInput()) {
			workingAssignment.appendLogLine(debug,"No symbols recognized in input: " + workingAssignment.getInput());
			resetWorkingAssignment();
		} else {
			List<Module> mods = modules.getModulesAsList();
			workingAssignment.appendLogLine(debug,"Recognized symbols: " + getSymbolsAsString(workingSymbols));

			setWorkingModule(mods.get(0));
			initializeWorkingContext(workingSymbols);
			
			if (workingAssignment.isCorrectInput()) {
				correctInput(mods,allSymbols);
				//debugModuleInputOutputSymbols();
				if (!Generic.stringBuilderEquals(workingAssignment.getInput(),workingAssignment.getCorrectedInput())) {
					workingSymbols = symbols.getSymbolsFromStrings(Symbol.parseTextSymbols(workingAssignment.getCorrectedInput(),0));
					initializeWorkingContext(workingSymbols);
				}
				if (workingAssignment.isCorrectInputOnly()) {
					finishWorkingAssignment();
				}
			} else {
				initializeModuleInputs(mods,workingSymbols);
				//debugModuleInputOutputSymbols();
			}
		}
	}

	private void correctInput(List<Module> mods,List<String> allSymbols) {
		StringBuilder correctedInput = new StringBuilder();
		StringBuilder correctedInputSymbols = new StringBuilder();
		
		int firedSymbolLinks = 0;
		Module firstMod = mods.get(0);
		Module lastMod = modules.getPrevModuleForModule(firstMod);
		int i = 0;
		String lastSymbol = "";
		for (String symbol: allSymbols) {
			lastSymbol = symbol;
			Symbol sym = symbols.getSymbolLikeCode(symbol,(i==0));
			if (sym!=null) {
				correctedInput = insertInputSymbolInLastModule(mods,sym,correctedInput);
			} else {
				StringBuilder unknowns = new StringBuilder(symbol);
				SortedMap<Integer,Symbol> nextSymbols = new TreeMap<Integer,Symbol>();
				int nDistance = 0;
				for (int ni = (i+1); ni < allSymbols.size(); ni++) {
					String nSymbol = allSymbols.get(ni);
					Symbol nSym = symbols.getSymbolLikeCode(nSymbol,false);
					nDistance++;
					if (nSym==null) {
						unknowns.append(" ");
						unknowns.append(nSymbol);
					} else  {
						nextSymbols.put(nDistance,nSym);
					}
					if (nDistance==ZACSModel.getNumberOfModules()) {
						break;
					}
				}
				
				// Append line end if needed
				if (workingAssignment.isCorrectLineEnd() && nextSymbols.size()==0 && (i + nDistance + 1)==allSymbols.size()) {
					Symbol nSym = null;
					if (nDistance==0) {
						firedSymbolLinks += confabulate(firstMod,mods,workingContext,workingAssignment.getThinkWidth(),workingAssignment.isThinkFast(),nextSymbols);
						List<ModuleSymbol> highestSymbols = firstMod.getHighestModuleSymbols(workingAssignment.getThinkWidth());
						for (ModuleSymbol modSym: highestSymbols) {
							if (Symbol.isLineEndSymbol(modSym.getSymbol().getCode())) {
								nSym = modSym.getSymbol();
								nextSymbols.put((nDistance + 1),nSym);
								break;
							}
						}
					}
					if (nSym==null) {
						nSym = symbols.getSymbolByCode(".");
						if (nSym!=null) {
							nextSymbols.put((nDistance + 1),nSym);
						}
					}
				}

				firedSymbolLinks += confabulate(firstMod,mods,workingContext,workingAssignment.getThinkWidth(),workingAssignment.isThinkFast(),nextSymbols);
				List<ModuleSymbol> highestSymbols = firstMod.getHighestModuleSymbols(workingAssignment.getThinkWidth());
				boolean corrected = false;
				if (nextSymbols.size()>0) {
					for (ModuleSymbol modSym: highestSymbols) {
						correctedInput = insertInputSymbolInLastModule(mods,modSym.getSymbol(),correctedInput);
						if (correctedInputSymbols.length()>0) {
							correctedInputSymbols.append(Generic.SEP_OBJ);
						}
						correctedInputSymbols.append(symbol);
						correctedInputSymbols.append(Generic.SEP_STR);
						correctedInputSymbols.append(modSym.getSymbol().getCode());
						corrected = true;
						break;
					}
					if (!corrected && nextSymbols.get(1)!=null) {
						for (ModuleSymbol modSym: highestSymbols) {
							if (modSym.getSymbol().getId()==nextSymbols.get(1).getId()) {
								workingAssignment.appendLogLine(debug,"Discarded input symbol: " + symbol);
								corrected = true;
								break;
							}
						}
					}
				}
				if (!corrected) {
					pushModuleInputSymbols(mods);
					lastMod.setSymbolInput(null);
				}
			}
			i++;
		}
		// Append line end if needed
		if (workingAssignment.isCorrectLineEnd() && !Symbol.isLineEndSymbol(lastSymbol)) {
			firedSymbolLinks += confabulate(firstMod,mods,workingContext,workingAssignment.getThinkWidth(),workingAssignment.isThinkFast(),null);
			List<ModuleSymbol> highestSymbols = firstMod.getHighestModuleSymbols(workingAssignment.getThinkWidth());
			for (ModuleSymbol modSym: highestSymbols) {
				if (Symbol.isLineEndSymbol(modSym.getSymbol().getCode())) {
					correctedInput = insertInputSymbolInLastModule(mods,modSym.getSymbol(),correctedInput);
					break;
				}
			}
		}
		workingAssignment.incrementFiredSymbolLinks(firedSymbolLinks);
		workingAssignment.setCorrectedInput(correctedInput);
		workingAssignment.setCorrectedInputSymbols(correctedInputSymbols);
		workingAssignment.appendLogLine(debug,"Corrected input: " + correctedInput);
	}
	
	private StringBuilder insertInputSymbolInLastModule(List<Module> mods, Symbol sym, StringBuilder correctedInput) {
		if (sym!=null) {
			if (correctedInput.length()>0 && !Symbol.isLineEndSymbol(sym.getCode())) {
				correctedInput.append(" ");
			}
			correctedInput.append(sym.getCode());
		}
		Module lastMod = pushModuleInputSymbols(mods);
		lastMod.setSymbolInput(sym);
		return correctedInput;
	}

	private Module pushModuleInputSymbols(List<Module> mods) {
		Module lastMod = null;
		for (Module mod: mods) {
			Module nMod = modules.getNextModuleForModule(mod);
			mod.setSymbolInput(nMod.getSymbolInput());
			lastMod = mod;
		}
		return lastMod;
	}
	
	private void initializeWorkingContext(List<Symbol> inputSymbols) {
		if (workingAssignment.getContext().length()>0 || workingAssignment.isContextDynamic()) {
			workingContext.clear();
			List<Symbol> contextSymbols = initializeContextForTextAndContext(contextSymbolMaximum,inputSymbols);
			if (contextSymbols.size()>0) {
				workingAssignment.appendLogLine(debug,"Initial working context: " + getSymbolLevelString(contextSymbols));
				workingContext.addAll(contextSymbols);
				workingAssignment.setInputContext(getSymbolsAsString(workingContext));
			} else {
				workingAssignment.appendLogLine(debug,"Unable to determine initial working context. Fired context links: " + workingAssignment.getFiredContextLinks());
			}
		}
	}
	
	private List<Symbol> initializeContextForTextAndContext(int maxContextSymbols,List<Symbol> inputSymbols) {
		resetContextSymbolLevels(null);
		StringBuilder input = getSymbolsAsString(inputSymbols);
		StringBuilder context = symbols.getContextForTextAndContext(input,workingAssignment.getContext(),0,maxContextSymbols,contextAssociate);
		List<StringBuilder> cSBs = Generic.stringBuilderSplit(context," ");		
		List<Symbol> contextSymbols = symbols.getSymbolsFromStringBuilders(cSBs,maxContextSymbols);
		long highestLevel = 1;
		if (workingAssignment.isContextDynamic() && contextSymbols.size()<maxContextSymbols) {
			int firedContextLinks = 0;
			List<StringBuilder> tSBs = Generic.stringBuilderSplit(input," ");
			List<Symbol> textSymbols = symbols.getSymbolsFromStringBuilders(tSBs,0);
			for (Symbol tSym: textSymbols) {
				firedContextLinks += fireContextLinksForSymbol(tSym,contextSymbols);
			}
			if (firedContextLinks>0) {
				workingAssignment.incrementFiredContextLinks(firedContextLinks);
			}
			List<Symbol> highestSyms = symbols.getHighestSymbols(1);
			if (highestSyms.size()>0) {
				Symbol highest = highestSyms.get(0);
				if (highestSyms.get(0).getLevel()>=levelMaximum) {
					symbols.divideSymbolLevelsBelowMaximum(null,levelMaximum);
					highestLevel = (highest.getLevel() + 1);
				}

				List<Symbol> topSyms = symbols.getHighestSymbols((maxContextSymbols - contextSymbols.size()));
				contextSymbols.addAll(topSyms);
				if (topSyms.size()>0 && workingAssignment.isLogExtended()) {
					workingAssignment.appendLogLine(debug,"Add context symbols: " + getSymbolLevelString(topSyms));
				}
				resetContextSymbolLevels(workingContext);
			}
		}
		for (Symbol cSym: contextSymbols) {
			if (cSym.getLevel()==0) {
				cSym.setLevel(highestLevel);
			}
		}
		return contextSymbols;
	}
	
	private int fireContextLinksForSymbol(Symbol symbol,List<Symbol> excludeContextSymbols) {
		int firedContextLinks = 0;
		if (!Symbol.isStructureSymbol(symbol.getCode())) {
			for (ContextLink link: symbol.getContextLinks()) {
				boolean found = false;
				if (excludeContextSymbols!=null) {
					for (Symbol cSym: excludeContextSymbols) {
						if (link.getSymbolTo().getCode().equals(cSym.getCode())) {
							found = true;
							break;
						}
					}
				}
				if (!found) {
					long amount = levelFireBase;
					long div = link.getSymbolFrom().getTotalConLinksFrom();
					long perc = (link.getCount() * 10) / countContextMaximum;
					perc++;
					amount += (perc * levelFireBase);
					if (div>0) {
						amount = (amount / div);
					}
					if (amount<=0) {
						amount = 1;
					}
					link.getSymbolTo().increaseLevel(amount);
					firedContextLinks++;
				}
			}
		}
		return firedContextLinks;
	}
	
	private void finishWorkingAssignment() {
		Assignment saveAssignment = workingAssignment;
		saveWorkingOutputInAssignment();
		workingAssignment.setOutput(workingAssignment.getWorkingOutput());
		workingAssignment.setOutputContext(getSymbolsAsString(workingContext));
		workingAssignment.setDateTimeFinished((new Date()).getTime());
		if (
			workingAssignment.getOutput().length()>0 &&
			!Generic.stringBuilderEquals(workingAssignment.getOutput(),workingAssignment.getPrevOutput1())
			) {
			workingAssignment.setPrevOutput3(workingAssignment.getPrevOutput2());
			workingAssignment.setPrevOutput2(workingAssignment.getPrevOutput1());
			workingAssignment.setPrevOutput1(workingAssignment.getOutput());
			workingAssignment.setDateTimePrevOutput3(workingAssignment.getDateTimePrevOutput2());
			workingAssignment.setDateTimePrevOutput2(workingAssignment.getDateTimePrevOutput1());
			workingAssignment.setDateTimePrevOutput1(workingAssignment.getDateTimeFinished());
		}
		workingAssignment.appendLogLine(debug,"Finished assignment: " + workingAssignment.getName());
		resetWorkingAssignment();
		saveAssignment(saveAssignment);
	}

	private void saveAssignment(Assignment saveAssignment) {
		ReqUpdate updateRequest = saveAssignment.getNewUpdateRequest(null);
		for (MdlProperty prop: DbConfig.getInstance().getModel().getClassByFullName(ZACSModel.ASSIGNMENT_CLASS_FULL_NAME).getPropertiesExtended()) {
			if (prop.getName().equals("name") ||
				prop.getName().equals("maxSymbols") ||
				prop.getName().startsWith("think") ||
				prop.getName().equals("logExtended") ||
				prop.getName().startsWith("context") ||
				prop.getName().equals("input")
				) {
				updateRequest.getUpdateObject().removePropertyValue(prop.getName());
			}
		}
		DbRequestQueue.getInstance().addRequest(updateRequest,this);
	}
	
	private void resetWorkingAssignment() {
		setWorkingModule(null);
		if (workingAssignment!=null) {
			workingAssignment.setWorkingOutput(new StringBuilder());
			workingAssignment.setWorkingContext(new StringBuilder());
		}
		workingAssignment = null;
		workingContext.clear();
		workingOutput.clear();
		resetModules();
	}

	private void resetWorkingContextSymbolsEnabled(List<Module> mods, int max) {
		if (workingContext.size()==0) {
			symbols.setEnabled(true);
		} else {
			symbols.setEnabled(false);
			
			List<Symbol> contextSymbols = null;
			if (max>0 && max<workingContext.size()) {
				contextSymbols = new ArrayList<Symbol>();
				int added = 0;
				for (Symbol cSym: workingContext) {
					contextSymbols.add(cSym);
					added++;
					if (added>=max) {
						break;
					}
				}
			} else {
				contextSymbols = new ArrayList<Symbol>(workingContext);
			}
			
			List<Symbol> tSyms = symbols.getSymbolsForContextSymbols(contextSymbols);
			for (Symbol tSym: tSyms) {
				tSym.setEnabled(true);
			}
		}
	}
	
	private void initializeModuleInputs() {
		List<Symbol> workingSymbols = symbols.getSymbolsFromStrings(workingAssignment.getSymbolList(false));
		List<Module> mods = modules.getModulesAsList();
		initializeModuleInputs(mods,workingSymbols);
	}

	private void initializeModuleInputs(List<Module> mods,List<Symbol> workingSymbols) {
		int start = 0;
		if (workingSymbols.size()>mods.size()) {
			start = workingSymbols.size() - mods.size();
		}
		int modNum = (mods.size() - workingSymbols.size());
		if (modNum<0) {
			modNum = 0;
		}
		Module mod = mods.get(modNum);
		int i = 0;
		for (Symbol symbol: workingSymbols) {
			if (i>=start) {
				mod.setSymbolInput(symbol);
				mod = modules.getNextModuleForModule(mod);
			}
			i++;
		}
	}
	
	private void initializeModuleOutputs(Module workingModule,List<Module> mods,List<Symbol> workingSymbols) {
		//Messenger.getInstance().debug(this,"Working symbols: " + workingSymbols.size());
		List<Symbol> syms = new ArrayList<Symbol>();
		if (workingSymbols.size()>mods.size()) {
			int end = (workingSymbols.size() - mods.size());
			for (int i = (workingSymbols.size() - 1); i >= end; i--) {
				syms.add(0,workingSymbols.get(i));
			}
		} else {
			for (Symbol sym: workingSymbols) {
				syms.add(sym);
			}
			while(syms.size()<mods.size()) {
				syms.add(0,null);
			}
		}
		Module mod = workingModule;
		for (Symbol symbol: syms) {
			mod.setSymbolOutput(symbol);
			mod = modules.getNextModuleForModule(mod);
		}
	}
	
	private void parseWorkingOutputFromAssignment() {
		workingContext.clear();
		for (StringBuilder sb: Generic.stringBuilderSplit(workingAssignment.getWorkingContext()," ")) {
			String[] codeLevel = sb.toString().split(":");
			Symbol sym = symbols.getSymbolByCode(codeLevel[0]);
			if (sym!=null && codeLevel.length>1) {
				sym.setLevel(Long.parseLong(codeLevel[1]));
			}
			workingContext.add(sym);
		}

		workingOutput.clear();
		for (StringBuilder sb: Generic.stringBuilderSplit(workingAssignment.getWorkingOutput()," ")) {
			workingOutput.add(sb.toString());
		} 
	}
	
	private void saveWorkingOutputInAssignment() {
		workingAssignment.setNumberOfSymbols(workingOutput.size());

		workingAssignment.setWorkingContext(new StringBuilder());
		boolean first = true;
		for (Symbol symbol: workingContext) {
			if (!first) {
				workingAssignment.getWorkingContext().append(" ");
			}
			workingAssignment.getWorkingContext().append(symbol.getCode());
			workingAssignment.getWorkingContext().append(":");
			workingAssignment.getWorkingContext().append(symbol.getLevel());
			first = false;
		}

		workingAssignment.setWorkingOutput(new StringBuilder());
		first = true;
		for (String symbol: workingOutput) {
			if (!first) {
				workingAssignment.getWorkingOutput().append(" ");
			}
			workingAssignment.getWorkingOutput().append(symbol);
			first = false;
		}
	}

	private StringBuilder getSymbolsAsString(List<Symbol> symbols) {
		StringBuilder syms = new StringBuilder();
		for (Symbol sym: symbols) {
			if (syms.length()>0) {
				syms.append(" ");
			}
			syms.append(sym.getCode());
		}
		return syms;
	}

	private void resetModules() {
		List<Module> mods = modules.getModulesAsList();
		for (Module mod: mods) {
			mod.setSymbolInput(null);
			mod.setSymbolOutput(null);
		}
		resetModuleSymbolLevels(mods);
		resetContextSymbolLevels(null);
	}

	private void resetModuleSymbolLevels(List<Module> mods) {
		for (Module mod: mods) {
			mod.resetSymbolLevelsExcludeModuleSymbols(null);
		}
	}
	
	private void resetContextSymbolLevels(List<Symbol> excludeSymbols) {
		symbols.resetLevels(excludeSymbols);
	}

	private void setWorkingModule(Module module) {
		workingModule = module;
		if (workingAssignment!=null) {
			workingAssignment.setWorkingModule(module);
		}
	}
	
	private void resetDuration() {
		duration = (new Date()).getTime();
	}

	private long getDuration() {
		return (new Date()).getTime() - duration;
	}

	private void saveDuration(Assignment assignment) {
		assignment.setDurationMilliseconds(assignment.getDurationMilliseconds() + getDuration());
	}
}
