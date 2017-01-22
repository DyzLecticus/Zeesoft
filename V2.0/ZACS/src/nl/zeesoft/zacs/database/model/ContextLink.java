package nl.zeesoft.zacs.database.model;

import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class ContextLink extends HlpObject {
	private long 				count 			= 1;
	
	private long 				symbolFromId	= 0;
	private long 				symbolToId		= 0;

	private Symbol				symbolFrom		= null;
	private Symbol				symbolTo		= null;
		
	public long incrementCount() {
		long r = 0;
		lockMe(this);
		count = count + 1;
		r = count;
		unlockMe(this);
		return r;
	}
	
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("count")) {
			setCount(Long.parseLong(obj.getPropertyValue("count").toString()));
		}
		if (obj.hasPropertyValue("symbolFrom")) {
			setSymbolFromId(obj.getLinkValue("symbolFrom").get(0));
		}
		if (obj.hasPropertyValue("symbolTo")) {
			setSymbolToId(obj.getLinkValue("symbolTo").get(0));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("count",new StringBuilder("" + getCount()));
		r.setLinkValue("symbolFrom",getSymbolFromId());
		r.setLinkValue("symbolTo",getSymbolToId());
		return r;
	}

	/**
	 * @return the count
	 */
	public long getCount() {
		lockMe(this);
		long r = count;
		unlockMe(this);
		return r;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(long count) {
		lockMe(this);
		this.count = count;
		unlockMe(this);
	}

	public long divideCountByTwo() {
		lockMe(this);
		count = (count / 2);
		long r = count;
		unlockMe(this);
		return r;
	}


	/**
	 * @return the symbolFromId
	 */
	public long getSymbolFromId() {
		return symbolFromId;
	}

	/**
	 * @param symbolFromId the symbolFromId to set
	 */
	public void setSymbolFromId(long symbolFromId) {
		this.symbolFromId = symbolFromId;
	}

	/**
	 * @return the symbolToId
	 */
	public long getSymbolToId() {
		return symbolToId;
	}

	/**
	 * @param symbolToId the symbolToId to set
	 */
	public void setSymbolToId(long symbolToId) {
		this.symbolToId = symbolToId;
	}


	/**
	 * @return the symbolFrom
	 */
	public Symbol getSymbolFrom() {
		return symbolFrom;
	}

	/**
	 * @param symbolFrom the symbolFrom to set
	 */
	public void setSymbolFrom(Symbol symbolFrom) {
		this.symbolFrom = symbolFrom;
		this.symbolFromId = symbolFrom.getId();
	}

	/**
	 * @return the symbolTo
	 */
	public Symbol getSymbolTo() {
		return symbolTo;
	}

	/**
	 * @param symbolTo the symbolTo to set
	 */
	public void setSymbolTo(Symbol symbolTo) {
		this.symbolTo = symbolTo;
		this.symbolToId = symbolTo.getId();
	}
}
