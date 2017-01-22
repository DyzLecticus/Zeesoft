package nl.zeesoft.zacs.database.model;

import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class ModuleSymbol extends HlpObject {

	private long 	moduleId		= 0;
	private long 	symbolId		= 0;
	private long 	level 			= 0;

	private Module	module			= null;
	private Symbol	symbol			= null;
	
	public long increaseLevel(long amount) {
		long r = 0;
		lockMe(this);
		level = level + amount;
		r = level;
		unlockMe(this);
		return r;
	}
	
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("module")) {
			setModuleId(obj.getLinkValue("module").get(0));
		}
		if (obj.hasPropertyValue("symbol")) {
			setSymbolId(obj.getLinkValue("symbol").get(0));
		}
		if (obj.hasPropertyValue("level")) {
			setLevel(Long.parseLong(obj.getPropertyValue("level").toString()));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setLinkValue("module",getModuleId());
		r.setLinkValue("symbol",getSymbolId());
		r.setPropertyValue("level",new StringBuilder("" + getLevel()));
		return r;
	}

	/**
	 * @return the level
	 */
	public long divideSymbolLevelBy(int div) {
		lockMe(this);
		level = (level / div);
		long r = level;
		unlockMe(this);
		return r;
	}

	/**
	 * @return the level
	 */
	public long getLevel() {
		lockMe(this);
		long r = level;
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
	 * @return the moduleId
	 */
	public long getModuleId() {
		return moduleId;
	}

	/**
	 * @param moduleId the moduleId to set
	 */
	public void setModuleId(long moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * @return the symbolId
	 */
	public long getSymbolId() {
		return symbolId;
	}

	/**
	 * @param symbolId the symbolId to set
	 */
	public void setSymbolId(long symbolId) {
		this.symbolId = symbolId;
	}

	/**
	 * @return the module
	 */
	public Module getModule() {
		return module;
	}

	/**
	 * @param module the module to set
	 */
	public void setModule(Module module) {
		this.module = module;
	}

	/**
	 * @return the symbol
	 */
	public Symbol getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}
}
