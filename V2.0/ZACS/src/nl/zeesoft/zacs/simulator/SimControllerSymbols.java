package nl.zeesoft.zacs.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zacs.database.model.ContextLink;
import nl.zeesoft.zacs.database.model.Example;
import nl.zeesoft.zacs.database.model.Symbol;
import nl.zeesoft.zacs.database.model.SymbolLink;
import nl.zeesoft.zacs.database.model.ZACSModel;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.gui.GuiController;
import nl.zeesoft.zodb.database.model.helpers.HlpControllerObject;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.event.EvtEvent;

public class SimControllerSymbols extends HlpControllerObject {
	private SortedMap<String,Symbol>	symbolsByCode			= new TreeMap<String,Symbol>();
	private SortedMap<Long,Symbol>		symbolsById				= new TreeMap<Long,Symbol>();

	private int							loadedSymbolsLinks		= 0;
	private int							loadedContextLinks		= 0;
	
	private ReqGet						getRequest				= null;
	private ReqAdd						addRequest				= null;
	
	private int							getStart				= 0;

	// Used to lock while learning text
	private SimControllerLocker			locker					= new SimControllerLocker();
	
	// Get added links
	private boolean						getAddedLinks			= false;
	private int							getRequestIndex			= 0;
	private List<ReqGet>				getRequests				= new ArrayList<ReqGet>();
	
	private List<Long>					addedSymbolIdList		= new ArrayList<Long>();

	private boolean						showProgress			= true;
	
	@Override
	protected void initialize() {
		getSymbols();
	}

	protected StringBuilder getContextForTextAndContext(StringBuilder text,StringBuilder context, int convertMaxSymbols, int maxContextSymbols, boolean contextAssociate) {
		text = Example.cleanUpText(text);
		context = Example.cleanUpText(context);
		List<String> symbols = Symbol.parseTextSymbols(text,convertMaxSymbols);
		if (symbols.size()>0) {
			List<String> contextSymbols = new ArrayList<String>();
			if (context.length()>0) {
				List<String> syms = Symbol.parseTextSymbols(context,convertMaxSymbols);
				if (syms.size()>0) {
					for (String sym: syms) {
						if (!Symbol.isStructureSymbol(sym) && !contextSymbols.contains(sym)) {
							contextSymbols.add(sym);
							if (contextSymbols.size()>=maxContextSymbols) {
								break;
							}
						}
					}
				}
			}
			if (contextAssociate) {
				if (contextSymbols.size()<maxContextSymbols) {
					for (String sym: symbols) {
						if (!Symbol.isStructureSymbol(sym) && !contextSymbols.contains(sym) && isContextSymbol(sym)) {
							contextSymbols.add(sym);
							if (contextSymbols.size()>=maxContextSymbols) {
								break;
							}
						}
					}
				}
			}
			context = new StringBuilder();
			for (String sym: contextSymbols) {
				if (context.length()>0) {
					context.append(" ");
				}
				context.append(sym);
			}
		}
		return context;
	}
	
	protected void learnText(StringBuilder text,StringBuilder context, int convertMaxSymbols, int countSymbolMaximum, int countContextMaximum, boolean skipCountMaxStructSyms, int maxContextSymbols, boolean contextAssociate) {
		text = Example.cleanUpText(text);
		context = Example.cleanUpText(context);
		locker.lockSim(this);
		List<String> symbols = addSymbolsForText(text,convertMaxSymbols);
		if (symbols.size()>1) {
			boolean updateSymbolCount = false;
			boolean updateContextCount = false;
			List<Symbol> contextSymbols = new ArrayList<Symbol>();
			context = getContextForTextAndContext(text,context,convertMaxSymbols,maxContextSymbols,contextAssociate);
			if (context.length()>0) {
				List<String> syms = addSymbolsForText(context,convertMaxSymbols);
				if (syms.size()>0) {
					for (String sym: syms) {
						Symbol conSym = getSymbolByCode(sym);
						contextSymbols.add(conSym);
					}
				}
			}
			List<Symbol> prevSymbols = new ArrayList<Symbol>();
			for (String sym: symbols) {
				Symbol cSym = getSymbolByCode(sym); 
				int distance = 1;
				for (Symbol pSym: prevSymbols) {
					if (pSym!=null && cSym!=null) {
						SymbolLink symLink = pSym.addSymbolLinkIfNotExists(cSym, distance);
						if (symLink.incrementCount()>=countSymbolMaximum) {
							if (skipCountMaxStructSyms && Symbol.isStructureSymbol(cSym.getCode())) {
								symLink.setCount(countSymbolMaximum);
							} else {
								updateSymbolCount = true;
							}
						}
					}
					distance++;
				}
				if (prevSymbols.size()>=ZACSModel.getNumberOfModules()) {
					prevSymbols.remove((prevSymbols.size() - 1));
				}
				prevSymbols.add(0,cSym);
				
				if (contextSymbols.size()>0) {
					for (Symbol conSym: contextSymbols) {
						ContextLink conLink = cSym.addContextLinkIfNotExists(conSym);
						if (conLink.incrementCount()>=countContextMaximum) {
							if (skipCountMaxStructSyms && Symbol.isStructureSymbol(cSym.getCode())) {
								conLink.setCount(countContextMaximum);
							} else {
								updateContextCount = true;
							}
						}
					}
				}
			}
			if (updateSymbolCount) {
				divideSymbolLinkCountsByTwoAndRemoveZeros();
			}
			if (updateContextCount) {
				divideContextLinkCountsByTwoAndRemoveZeros();
			}
		}
		locker.unlockSim(this);
	}

	protected List<Symbol> getSymbolsFromStringBuilders(List<StringBuilder> syms) {
		return getSymbolsFromStringBuilders(syms,0);
	}
	
	protected List<Symbol> getSymbolsFromStringBuilders(List<StringBuilder> syms, int max) {
		List<String> strs = new ArrayList<String>();
		for (StringBuilder sb: syms) {
			strs.add(sb.toString());
		}
		return getSymbolsFromStrings(strs,max);
	}
	
	protected List<Symbol> getSymbolsFromStrings(List<String> syms) {
		return getSymbolsFromStrings(syms,0);
	}

	protected List<Symbol> getSymbolsFromStrings(List<String> syms, int max) {
		List<Symbol> symbols = new ArrayList<Symbol>();
		for (String symbol: syms) {
			Symbol sym = getSymbolByCode(symbol);
			if (sym!=null && sym.getId()>0) {
				symbols.add(sym);
				if (max>0 && symbols.size()>=max) {
					break;
				}
			}
		}
		return symbols;
	}
	
	protected void setEnabled(boolean enabled) {
		lockMe(this);
		List<Symbol> syms = new ArrayList<Symbol>(symbolsById.values());
		unlockMe(this);
		for (Symbol sym: syms) {
			sym.setEnabled(enabled);
		}
	}

	protected void resetLevels(List<Symbol> excludeSymbols) {
		lockMe(this);
		List<Symbol> syms = new ArrayList<Symbol>(symbolsById.values());
		unlockMe(this);
		for (Symbol sym: syms) {
			boolean excluded = false;
			if (excludeSymbols!=null) {
				for (Symbol eSym: excludeSymbols) {
					if (eSym.getId()==sym.getId()) {
						excluded = true;
						break;
					}
				}
			}
			if (!excluded) {
				sym.setLevel(0);
			}
		}
	}
	
	protected Symbol getSymbolById(long id) {
		Symbol r = null;
		lockMe(this);
		r = symbolsById.get(id);
		unlockMe(this);
		return r;
	}

	protected Symbol getSymbolByCode(String code) {
		Symbol r = null;
		lockMe(this);
		r = symbolsByCode.get(code);
		unlockMe(this);
		return r;
	}

	protected Symbol getSymbolLikeCode(String code,boolean tryUpperCaseFirst) {
		Symbol r = null;
		lockMe(this);
		r = symbolsByCode.get(code);
		if (r==null && tryUpperCaseFirst) {
			if (code.length()==1) {
				r = symbolsByCode.get(code.toUpperCase());
			} else if (code.length()>1) {
				r = symbolsByCode.get(code.substring(0,1).toUpperCase() + code.substring(1).toLowerCase());
			}
		}
		if (r==null) {
			r = symbolsByCode.get(code.toLowerCase());
		}
		if (r==null) {
			r = symbolsByCode.get(code.toUpperCase());
		}
		unlockMe(this);
		return r;
	}
	
	protected List<Symbol> getSymbolsAsList() {
		List<Symbol> r = new ArrayList<Symbol>();
		lockMe(this);
		for (Symbol symbol: symbolsById.values()) {
			r.add(symbol);
		}
		unlockMe(this);
		return r;
	}

	protected int getSize() {
		int r = 0;
		lockMe(this);
		r = symbolsById.size();
		unlockMe(this);
		return r;
	}

	protected long getTotalSymbolLinks() {
		long r = 0;
		lockMe(this);
		for (Symbol symbol: symbolsById.values()) {
			r = r + symbol.getTotalSymbolLinks();
		}
		unlockMe(this);
		return r;
	}

	protected long getAverageSymbolLinkCount() {
		long r = 0;
		long t = 0;
		lockMe(this);
		if (symbolsById.size()>0) {
			for (Symbol symbol: symbolsById.values()) {
				r = r + symbol.getTotalSymbolLinkCount();
				t = t + symbol.getTotalSymbolLinks();
			}
			if (r>0 && t>0) {
				r = (r / t);
			}
		}
		unlockMe(this);
		return r;
	}

	protected long getTotalContextLinks() {
		long r = 0;
		lockMe(this);
		for (Symbol symbol: symbolsById.values()) {
			r = r + symbol.getTotalContextLinks();
		}
		unlockMe(this);
		return r;
	}

	protected long getAverageContextLinkCount() {
		long r = 0;
		long t = 0;
		lockMe(this);
		if (symbolsById.size()>0) {
			for (Symbol symbol: symbolsById.values()) {
				r = r + symbol.getTotalContextLinkCount();
				t = t + symbol.getTotalContextLinks();
			}
			if (r>0 && t>0) {
				r = (r / t);
			}
		}
		unlockMe(this);
		return r;
	}

	protected boolean isContextSymbol(String code) {
		Symbol s = getSymbolLikeCode(code,true);
		return (s!=null && s.getTotalConLinksTo() > 0);
	}
	
	protected void getSymbolLinks() {
		setDone(false);
		loadedSymbolsLinks = 0;
		getAddedLinks = false;
		getStart = 0;
		resetGetSymbolLinkRequest();
		DbRequestQueue.getInstance().addRequest(getRequest,this);
		waitTillDone();
	}

	protected void getContextLinks() {
		setDone(false);
		loadedContextLinks = 0;
		getAddedLinks = false;
		getStart = 0;
		resetGetContextLinkRequest();
		DbRequestQueue.getInstance().addRequest(getRequest,this);
		waitTillDone();
	}

	protected List<Symbol> getSymbolsForContextSymbols(List<Symbol> cSyms) {
		List<Symbol> r = new ArrayList<Symbol>();
		lockMe(this);
		for (Symbol cSym: cSyms) {
			for (Symbol sym: symbolsById.values()) {
				for (ContextLink link: sym.getContextLinks()) {
					if (link.getSymbolToId()==cSym.getId() && !r.contains(sym)) {
						r.add(sym);
					}
				}
			}
		}
		unlockMe(this);
		return r;
	}
	
	protected List<Symbol> getHighestSymbols(int max) {
		SortedMap<Long,List<Symbol>> levelSymListMap = new TreeMap<Long,List<Symbol>>();

		lockMe(this);
		for (Symbol sym: symbolsById.values()) {
			if (sym.getLevel()>0) {
				List<Symbol> symList = levelSymListMap.get(sym.getLevel());
				if (symList==null) {
					symList = new ArrayList<Symbol>();
					levelSymListMap.put(sym.getLevel(),symList);
				}
				symList.add(sym);
			}
		}
		unlockMe(this);
		
		List<Symbol> r = new ArrayList<Symbol>();
		for (Entry<Long,List<Symbol>> entry: levelSymListMap.entrySet()) {
			for (Symbol sym: entry.getValue()) {
				r.add(sym);
			}
		}
		
		if (r.size()>max) {
			int remove = r.size() - max;
			for (int i = 0; i < remove; i++) {
				r.remove(0);
			}
		}
		
		List<Symbol> t = new ArrayList<Symbol>(r);
		r.clear();
		for (Symbol sym: t) {
			r.add(0,sym);
		}
		
		return r;
	}
	
	protected void divideSymbolLevelsBelowMaximum(Symbol trigger,long maxLevel) {
		List<Symbol> highestSyms = getHighestSymbols(1);
		lockMe(this);
		if (symbolsById.size()>0) {
			Symbol highest = trigger;
			if (trigger==null && highestSyms.size()>0) {
				highest = highestSyms.get(0);
			}
			if (highest!=null) {
				while (highest.getLevel()>=maxLevel) {
					int div = 2;
					if (highest.getLevel()>(maxLevel*100)) {
						div = 100;
					} else if (highest.getLevel()>(maxLevel*10)) {
						div = 10;
					} else if (highest.getLevel()>(maxLevel*4)) {
						div = 4;
					}
					for (Symbol sym: symbolsById.values()) {
						if (sym.getLevel()>1) {
							sym.divideSymbolLevelBy(div);
						} else {
							sym.setLevel(0);
						}
					}
				}
				if (trigger!=null) {
					trigger.increaseLevel(1);
				}
			}
		}
		unlockMe(this);
	}

	protected void setShowProgress(boolean showProgress) {
		this.showProgress = showProgress;
	}
	
	protected void getAddedSymbolLinks(List<Long> addedIdList) {
		for (Symbol symbol: symbolsById.values()) {
			List<SymbolLink> links = symbol.getSymbolLinks();
			for (SymbolLink link: links) {
				if (link.getId()==0) {
					symbol.removeSymbolLink(link);
				}
			}
		}
		getAddedLinks(ZACSModel.SYMBOL_LINK_CLASS_FULL_NAME,addedIdList);
	}

	protected void getAddedContextLinks(List<Long> addedIdList) {
		for (Symbol symbol: symbolsById.values()) {
			List<ContextLink> links = symbol.getContextLinks();
			for (ContextLink link: links) {
				if (link.getId()==0) {
					symbol.removeContextLink(link);
				}
			}
		}
		getAddedLinks(ZACSModel.CONTEXT_LINK_CLASS_FULL_NAME,addedIdList);
	}
	
	@Override
	public void handleEvent(EvtEvent e) {
		resetTimeOut();
		if (getRequest!=null && e.getValue()==getRequest) {
			if (getRequest.getClassName().equals(ZACSModel.SYMBOL_CLASS_FULL_NAME)) {
				if (getRequest.getObjects().size()>0) {
					for (ReqDataObject object: getRequest.getObjects()) {
						Symbol sym = new Symbol();
						sym.fromDataObject(object.getDataObject());
						lockMe(this);
						if (!symbolsByCode.containsKey(sym.getCode())) {
							symbolsByCode.put(sym.getCode(),sym);
							symbolsById.put(sym.getId(),sym);
						}
						unlockMe(this);
					}
				}
				if (showProgress) {
					GuiController.getInstance().incrementProgressFrameDone(getRequest.getObjects().size());
					Messenger.getInstance().debug(this,"Loaded symbol objects: " + symbolsById.size());
				}
				setDone(true);
			} else if (getRequest.getClassName().equals(ZACSModel.SYMBOL_LINK_CLASS_FULL_NAME)) {
				if (getRequest.getObjects().size()>0) {
					for (ReqDataObject object: getRequest.getObjects()) {
						SymbolLink symLink = new SymbolLink();
						symLink.fromDataObject(object.getDataObject());
						lockMe(this);
						Symbol symFrom = symbolsById.get(symLink.getSymbolFromId());
						Symbol symTo = symbolsById.get(symLink.getSymbolToId());
						unlockMe(this);
						if (symFrom!=null && symTo!=null) {
							symLink.setSymbolFrom(symFrom);
							symLink.setSymbolTo(symTo);
							symFrom.addSymbolLink(symLink);
							loadedSymbolsLinks++;
						} else {
							Messenger.getInstance().error(this,"Symbol link symbol from id: " + symLink.getSymbolFromId() + ", found: " + (symFrom!=null)  + ", to: " + symLink.getSymbolToId() + ", found: " + (symTo!=null));
						}
					}
					if (getAddedLinks) {
						getRequestIndex++;
						if (getRequestIndex<getRequests.size()) {
							getRequest = getRequests.get(getRequestIndex);
							DbRequestQueue.getInstance().addRequest(getRequest,this);
						} else {
							getAddedLinks = false;
							getRequests.clear();
							setDone(true);
						}
					} else {
						if (showProgress) {
							GuiController.getInstance().incrementProgressFrameDone(getRequest.getObjects().size());
						}
						if (getRequest.getObjects().size()==getRequest.getLimit()) {
							getStart += getRequest.getLimit();
							resetGetSymbolLinkRequest();
							DbRequestQueue.getInstance().addRequest(getRequest,this);
						} else {
							if (showProgress) {
								Messenger.getInstance().debug(this,"Loaded symbol link objects: " + loadedSymbolsLinks);
							}
							setDone(true);
						}
					}
				} else {
					if (showProgress) {
						Messenger.getInstance().debug(this,"Loaded symbol link objects: " + loadedContextLinks);
					}
					setDone(true);
				}
			} else if (getRequest.getClassName().equals(ZACSModel.CONTEXT_LINK_CLASS_FULL_NAME)) {
				if (getRequest.getObjects().size()>0) {
					for (ReqDataObject object: getRequest.getObjects()) {
						ContextLink conLink = new ContextLink();
						conLink.fromDataObject(object.getDataObject());
						lockMe(this);
						Symbol symFrom = symbolsById.get(conLink.getSymbolFromId());
						Symbol symTo = symbolsById.get(conLink.getSymbolToId());
						unlockMe(this);
						if (symFrom!=null && symTo!=null) {
							conLink.setSymbolFrom(symFrom);
							conLink.setSymbolTo(symTo);
							symFrom.addContextLink(conLink);
							loadedContextLinks++;
						} else {
							Messenger.getInstance().error(this,"Context link symbol from id: " + conLink.getSymbolFromId() + ", found: " + (symFrom!=null)  + ", to: " + conLink.getSymbolToId() + ", found: " + (symTo!=null));
						}
					}
					if (getAddedLinks) {
						getRequestIndex++;
						if (getRequestIndex<getRequests.size()) {
							getRequest = getRequests.get(getRequestIndex);
							DbRequestQueue.getInstance().addRequest(getRequest,this);
						} else {
							getAddedLinks = false;
							getRequests.clear();
							setDone(true);
						}
					} else {
						if (showProgress) {
							GuiController.getInstance().incrementProgressFrameDone(getRequest.getObjects().size());
						}
						if (getRequest.getObjects().size()==getRequest.getLimit()) {
							getStart += getRequest.getLimit();
							resetGetContextLinkRequest();
							DbRequestQueue.getInstance().addRequest(getRequest,this);
						} else {
							if (showProgress) {
								Messenger.getInstance().debug(this,"Loaded context link objects: " + loadedContextLinks);
							}
							setDone(true);
						}
					}
				} else {
					if (showProgress) {
						Messenger.getInstance().debug(this,"Loaded context link objects: " + loadedContextLinks);
					}
					setDone(true);
				}
			}
		} else if (addRequest!=null && e.getValue()==addRequest) {
			if (addRequest.hasError()) {
				Messenger.getInstance().debug(this,"Error while adding symbols: " + addRequest.getErrors().get(0).getMessage());
			}
			addedSymbolIdList = new ArrayList<Long>(addRequest.getImpactedIds());
			setDone(true);
		}
	}

	@Override
	protected void whileWaiting() {
		GuiController.getInstance().refreshProgressFrame();
	}

	private void divideSymbolLinkCountsByTwoAndRemoveZeros() {
		for (Symbol symbol : SimController.getInstance().getSymbols().getSymbolsAsList()) {
			List<SymbolLink> removeLinks = symbol.divideSymbolLinkCountsByTwo();
			for (SymbolLink link: removeLinks) {
				symbol.removeSymbolLink(link);
				SimController.getInstance().removedSymbolLink(link.getId());
			}
		}
	}

	private void divideContextLinkCountsByTwoAndRemoveZeros() {
		for (Symbol symbol : SimController.getInstance().getSymbols().getSymbolsAsList()) {
			List<ContextLink> removeLinks = symbol.divideContextLinkCountsByTwo();
			for (ContextLink link: removeLinks) {
				symbol.removeContextLink(link);
				SimController.getInstance().removedContextLink(link.getId());
			}
		}
	}

	private List<String> addSymbolsForText(StringBuilder text, int convertMaxSymbols) {
		List<String> symbols = Symbol.parseTextSymbols(text,convertMaxSymbols);
		List<String> addSymbolCodes = new ArrayList<String>();
		List<Symbol> addSymbols = new ArrayList<Symbol>();
		for (String sym: symbols) {
			if (!addSymbolCodes.contains(sym) && getSymbolByCode(sym)==null) {
				Symbol symbol = new Symbol();
				symbol.setCode(sym);
				addSymbols.add(symbol);
				addSymbolCodes.add(sym);
			}
		}
		if (addSymbols.size()>0) {
			List<Long> addedIdList = addSymbols(addSymbols);
			SimController.getInstance().getModules().addModuleSymbolsForSymbolIdList(addedIdList);
		}
		return symbols;
	}

	private void getSymbols() {
		setDone(false);
		getAddedLinks = false;
		addRequest = null;
		getRequest = new ReqGet(ZACSModel.SYMBOL_CLASS_FULL_NAME);
		getRequest.getProperties().add(ReqGet.ALL_PROPERTIES);
		getRequest.addSubscriber(this);
		DbRequestQueue.getInstance().addRequest(getRequest,this);
		waitTillDone();
	}
	
	private void resetGetSymbolLinkRequest() {
		getRequest = new ReqGet(ZACSModel.SYMBOL_LINK_CLASS_FULL_NAME);
		getRequest.getProperties().add(ReqGet.ALL_PROPERTIES);
		getRequest.addSubscriber(this);
		getRequest.setStart(getStart);
		getRequest.setLimit(100);
	}

	private void resetGetContextLinkRequest() {
		getRequest = new ReqGet(ZACSModel.CONTEXT_LINK_CLASS_FULL_NAME);
		getRequest.getProperties().add(ReqGet.ALL_PROPERTIES);
		getRequest.addSubscriber(this);
		getRequest.setStart(getStart);
		getRequest.setLimit(100);
	}
	
	private List<Long> addSymbols(List<Symbol> addSymbols) {
		addedSymbolIdList.clear();
		if (addSymbols.size()>0) {
			setDone(false);
			getAddedLinks = false;
			getRequest = null;
			addRequest = new ReqAdd(ZACSModel.SYMBOL_CLASS_FULL_NAME);
			addRequest.addSubscriber(this);
			for (Symbol symbol: addSymbols) {
				addRequest.getObjects().add(new ReqDataObject(symbol.toDataObject()));
			}
			DbRequestQueue.getInstance().addRequest(addRequest,this);
			waitTillDone();
			getSymbols();
		}
		return addedSymbolIdList;
	}

	private void getAddedLinks(String className, List<Long> addedIdList) {
		if (addedIdList.size()>0) {
			setDone(false);
			getAddedLinks = true;
			getRequestIndex = 0;
			getRequests.clear();
			for (long id: addedIdList) {
				getRequest = new ReqGet(className,id);
				getRequest.getProperties().add(ReqGet.ALL_PROPERTIES);
				getRequest.addSubscriber(this);
				getRequests.add(getRequest);
			}
			getRequest = getRequests.get(getRequestIndex);
			DbRequestQueue.getInstance().addRequest(getRequest,this);
			waitTillDone();
		}
	}
}
