package nl.zeesoft.zacs.database.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class Module extends HlpObject {
	private int 				num				= 0;
	private long 				level			= 0;

	private long				symbolInputId	= 0;
	private long				symbolOutputId	= 0;
	private Symbol				symbolInput		= null;
	private Symbol				symbolOutput	= null;
	
	private List<ModuleSymbol>	symbols			= new ArrayList<ModuleSymbol>();
	
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("num")) {
			setNum(Integer.parseInt(obj.getPropertyValue("num").toString()));
		}
		if (obj.hasPropertyValue("level")) {
			setLevel(Long.parseLong(obj.getPropertyValue("level").toString()));
		}
		if (obj.hasPropertyValue("symbolInput") && obj.getLinkValue("symbolInput").size()>0) {
			setSymbolInputId(obj.getLinkValue("symbolInput").get(0));
		}
		if (obj.hasPropertyValue("symbolOutput") && obj.getLinkValue("symbolOutput").size()>0) {
			setSymbolOutputId(obj.getLinkValue("symbolOutput").get(0));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("num",new StringBuilder("" + getNum()));
		r.setPropertyValue("level",new StringBuilder("" + getLevel()));
		r.setLinkValue("symbolInput",getSymbolInputId());
		r.setLinkValue("symbolOutput",getSymbolOutputId());
		return r;
	}

	/**
	 * @return the symbols
	 */
	public List<ModuleSymbol> getSymbols() {
		lockMe(this);
		List<ModuleSymbol> r = new ArrayList<ModuleSymbol>(symbols);
		unlockMe(this);
		return r;
	}

	/**
	 * @return the symbols for a list of links
	 */
	public List<ModuleSymbol> getSymbols(List<SymbolLink> links) {
		List<ModuleSymbol> r = new ArrayList<ModuleSymbol>();
		lockMe(this);
		for (SymbolLink link: links) {
			for (ModuleSymbol modSym: symbols) {
				if (modSym.getSymbolId()==link.getSymbolToId()) {
					r.add(modSym);
					break;
				}
			}
		}
		unlockMe(this);
		return r;
	}

	public List<ModuleSymbol> getActivatedModuleSymbols() {
		return getActivatedModuleSymbols(false);
	}

	public List<ModuleSymbol> getActivatedModuleSymbols(boolean ignoreStructSyms) {
		List<ModuleSymbol> r = new ArrayList<ModuleSymbol>();
		lockMe(this);
		for (ModuleSymbol modSym: symbols) {
			if (modSym.getLevel()>0 && (!ignoreStructSyms || !Symbol.isStructureSymbol(modSym.getSymbol().getCode()))) {
				r.add(modSym);
			}
		}
		unlockMe(this);
		return r;
	}

	public List<ModuleSymbol> getHighestModuleSymbols(int max) {
		return getHighestModuleSymbols(max,false);
	}

	public List<ModuleSymbol> getHighestModuleSymbols(int max,boolean ignoreStructSyms) {
		SortedMap<Long,List<ModuleSymbol>> levelModSymListMap = new TreeMap<Long,List<ModuleSymbol>>();

		lockMe(this);
		for (ModuleSymbol modSym: symbols) {
			if (modSym.getLevel()>0 && (!ignoreStructSyms || !Symbol.isStructureSymbol(modSym.getSymbol().getCode()))) {
				List<ModuleSymbol> modSymList = levelModSymListMap.get(modSym.getLevel());
				if (modSymList==null) {
					modSymList = new ArrayList<ModuleSymbol>();
					levelModSymListMap.put(modSym.getLevel(),modSymList);
				}
				modSymList.add(modSym);
			}
		}
		unlockMe(this);
		
		List<ModuleSymbol> r = new ArrayList<ModuleSymbol>();
		for (Entry<Long,List<ModuleSymbol>> entry: levelModSymListMap.entrySet()) {
			for (ModuleSymbol modSym: entry.getValue()) {
				r.add(modSym);
			}
		}
		
		if (r.size()>max) {
			int remove = r.size() - max;
			for (int i = 0; i < remove; i++) {
				r.remove(0);
			}
		}
		
		List<ModuleSymbol> t = new ArrayList<ModuleSymbol>(r);
		r.clear();
		for (ModuleSymbol sym: t) {
			r.add(0,sym);
		}
		return r;
	}

	public ModuleSymbol getWinningModuleSymbol() {
		return getWinningModuleSymbol(false);
	}
	
	public ModuleSymbol getWinningModuleSymbol(boolean ignoreStructSyms) {
		ModuleSymbol winner = null;
		List<ModuleSymbol> modSyms = getHighestModuleSymbols(2,ignoreStructSyms);
		if (modSyms.size()>0) {
			if (modSyms.size()==1) {
				winner = modSyms.get(0);
			} else if (modSyms.get(0).getLevel()>modSyms.get(1).getLevel()) {
				winner = modSyms.get(0);
			}
		}
		return winner;
	}

	public ModuleSymbol getModuleSymbolForSymbolId(long symbolId) {
		ModuleSymbol r = null;
		lockMe(this);
		for (ModuleSymbol modSym: symbols) {
			if (modSym.getSymbolId()==symbolId) {
				r = modSym;
				break;
			}
		}
		unlockMe(this);
		return r;
	}
	
	public void addSymbol(ModuleSymbol symbol) {
		lockMe(this);
		symbols.add(symbol);
		unlockMe(this);
	}

	public void resetSymbolLevelsExcludeSymbols(List<Symbol> excludeSymbols) {
		List<Long> idList = null;
		if (excludeSymbols!=null) {
			idList = new ArrayList<Long>();
			for (Symbol eSym: excludeSymbols) {
				idList.add(eSym.getId());
			}
		}
		resetSymbolLevelsExcludeSymbolIdList(idList);
	}

	public void resetSymbolLevelsExcludeModuleSymbols(List<ModuleSymbol> excludeSymbols) {
		List<Long> idList = null;
		if (excludeSymbols!=null) {
			idList = new ArrayList<Long>();
			for (ModuleSymbol eModSym: excludeSymbols) {
				idList.add(eModSym.getSymbolId());
			}
		}
		resetSymbolLevelsExcludeSymbolIdList(idList);
	}

	public long getTotalSymbolLevel() {
		long r = 0;
		lockMe(this);
		if (symbols.size()>0) {
			for (ModuleSymbol modSym: symbols) {
				r = r + modSym.getLevel();
			}
		}
		unlockMe(this);
		return r;
	}
	
	public void divideSymbolLevelsBelowMaximum(ModuleSymbol trigger,long maxLevel) {
		ModuleSymbol highest = trigger;
		if (trigger==null) {
			List<ModuleSymbol> highestSyms = getHighestModuleSymbols(1);
			trigger = getWinningModuleSymbol();
			if (highestSyms.size()>0) {
				highest = highestSyms.get(0);
			}
		}
		lockMe(this);
		if (symbols.size()>0 && highest!=null && highest.getLevel()>=maxLevel) {
			while (highest.getLevel()>=maxLevel) {
				int div = 2;
				if (highest.getLevel()>(maxLevel*100)) {
					div = 100;
				} else if (highest.getLevel()>(maxLevel*10)) {
					div = 10;
				} else if (highest.getLevel()>(maxLevel*4)) {
					div = 4;
				}
				for (ModuleSymbol modSym: symbols) {
					if (modSym.getLevel()>1) {
						modSym.divideSymbolLevelBy(div);
					} else {
						modSym.setLevel(0);
					}
				}
			}
			if (trigger!=null) {
				trigger.increaseLevel(1);
			}
		}
		unlockMe(this);
	}
	
	/**
	 * @return the num
	 */
	public int getNum() {
		return num;
	}

	/**
	 * @param num the num to set
	 */
	public void setNum(int num) {
		this.num = num;
	}

	/**
	 * @return the level
	 */
	public long getLevel() {
		long r = 0;
		lockMe(this);
		r = level;
		unlockMe(this);
		return r;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(long level) {
		lockMe(this);
		this.level = level;
		unlockMe(this);
	}

	/**
	 * @return the symbolInputId
	 */
	public long getSymbolInputId() {
		long r = 0;
		lockMe(this);
		r = symbolInputId;
		unlockMe(this);
		return r;
	}

	/**
	 * @param symbolInputId the symbolInputId to set
	 */
	public void setSymbolInputId(long symbolInputId) {
		lockMe(this);
		this.symbolInputId = symbolInputId;
		unlockMe(this);
	}

	/**
	 * @return the symbolOutputId
	 */
	public long getSymbolOutputId() {
		long r = 0;
		lockMe(this);
		r = symbolOutputId;
		unlockMe(this);
		return r;
	}

	/**
	 * @param symbolOutputId the symbolOutputId to set
	 */
	public void setSymbolOutputId(long symbolOutputId) {
		lockMe(this);
		this.symbolOutputId = symbolOutputId;
		unlockMe(this);
	}

	/**
	 * @return the symbolInput
	 */
	public Symbol getSymbolInput() {
		Symbol r = null;
		lockMe(this);
		r = symbolInput;
		unlockMe(this);
		return r;
	}

	/**
	 * @param symbolInput the symbolInput to set
	 */
	public void setSymbolInput(Symbol symbolInput) {
		lockMe(this);
		this.symbolInput = symbolInput;
		if (symbolInput!=null) {
			symbolInputId = symbolInput.getId();
		} else {
			symbolInputId = 0;
		}
		unlockMe(this);
	}

	/**
	 * @return the symbolOutput
	 */
	public Symbol getSymbolOutput() {
		Symbol r = null;
		lockMe(this);
		r = symbolOutput;
		unlockMe(this);
		return r;
	}

	/**
	 * @param symbolOutput the symbolOutput to set
	 */
	public void setSymbolOutput(Symbol symbolOutput) {
		lockMe(this);
		this.symbolOutput = symbolOutput;
		if (symbolOutput!=null) {
			symbolOutputId = symbolOutput.getId();
		} else {
			symbolOutputId = 0;
		}
		unlockMe(this);
	}

	private void resetSymbolLevelsExcludeSymbolIdList(List<Long> excludeSymbolIdList) {
		lockMe(this);
		List<ModuleSymbol> syms = new ArrayList<ModuleSymbol>(symbols);
		unlockMe(this);
		for (ModuleSymbol modSym: syms) {
			if (excludeSymbolIdList==null || !excludeSymbolIdList.contains(modSym.getSymbolId())) {
				modSym.setLevel(0);
			}
		}
	}
}
