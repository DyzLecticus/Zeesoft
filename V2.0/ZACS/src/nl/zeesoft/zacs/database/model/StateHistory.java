package nl.zeesoft.zacs.database.model;

import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class StateHistory extends HlpObject {
	private long				controlId				= 0;
	private Control				control					= null;

	private long				dateTime				= 0;
	private String				status					= "";

	private long				statTotalSymbols		= 0;
	private long				statTotalSymLinks		= 0;
	private long				statAvgSymLinkCount		= 0;
	private long				statTotalSymbolLevel	= 0;
	private long				statTotalConLinks		= 0;
	private long				statAvgConLinkCount		= 0;
	
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("control")) {
			setControlId(obj.getLinkValue("control").get(0));
		}
		if (obj.hasPropertyValue("dateTime")) {
			setDateTime(Long.parseLong(obj.getPropertyValue("dateTime").toString()));
		}
		if (obj.hasPropertyValue("status")) {
			setStatus(obj.getPropertyValue("status").toString());
		}
		if (obj.hasPropertyValue("statTotalSymbols")) {
			setStatTotalSymbols(Long.parseLong(obj.getPropertyValue("statTotalSymbols").toString()));
		}
		if (obj.hasPropertyValue("statTotalSymLinks")) {
			setStatTotalSymLinks(Long.parseLong(obj.getPropertyValue("statTotalSymLinks").toString()));
		}
		if (obj.hasPropertyValue("statAvgSymLinkCount")) {
			setStatAvgSymLinkCount(Long.parseLong(obj.getPropertyValue("statAvgSymLinkCount").toString()));
		}
		if (obj.hasPropertyValue("statTotalSymbolLevel")) {
			setStatTotalSymbolLevel(Long.parseLong(obj.getPropertyValue("statTotalSymbolLevel").toString()));
		}
		if (obj.hasPropertyValue("statTotalConLinks")) {
			setStatTotalConLinks(Long.parseLong(obj.getPropertyValue("statTotalConLinks").toString()));
		}
		if (obj.hasPropertyValue("statAvgConLinkCount")) {
			setStatAvgConLinkCount(Long.parseLong(obj.getPropertyValue("statAvgConLinkCount").toString()));
		}
	}

	@Override	
	
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setLinkValue("control",getControlId());
		r.setPropertyValue("dateTime",new StringBuilder("" + getDateTime()));
		r.setPropertyValue("status",new StringBuilder(getStatus()));
		r.setPropertyValue("statTotalSymbols",new StringBuilder("" + getStatTotalSymbols()));
		r.setPropertyValue("statTotalSymLinks",new StringBuilder("" + getStatTotalSymLinks()));
		r.setPropertyValue("statAvgSymLinkCount",new StringBuilder("" + getStatAvgSymLinkCount()));
		r.setPropertyValue("statTotalSymbolLevel",new StringBuilder("" + getStatTotalSymbolLevel()));
		r.setPropertyValue("statTotalConLinks",new StringBuilder("" + getStatTotalConLinks()));
		r.setPropertyValue("statAvgConLinkCount",new StringBuilder("" + getStatAvgConLinkCount()));
		return r;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the controlId
	 */
	public long getControlId() {
		return controlId;
	}

	/**
	 * @param controlId the controlId to set
	 */
	public void setControlId(long controlId) {
		this.controlId = controlId;
	}

	/**
	 * @return the control
	 */
	public Control getControl() {
		return control;
	}

	/**
	 * @param control the control to set
	 */
	public void setControl(Control control) {
		this.control = control;
	}

	/**
	 * @return the dateTime
	 */
	public long getDateTime() {
		return dateTime;
	}

	/**
	 * @param dateTime the dateTime to set
	 */
	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * @return the statTotalSymbols
	 */
	public long getStatTotalSymbols() {
		return statTotalSymbols;
	}

	/**
	 * @param statTotalSymbols the statTotalSymbols to set
	 */
	public void setStatTotalSymbols(long statTotalSymbols) {
		this.statTotalSymbols = statTotalSymbols;
	}

	/**
	 * @return the statTotalSymLinks
	 */
	public long getStatTotalSymLinks() {
		return statTotalSymLinks;
	}

	/**
	 * @param statTotalSymLinks the statTotalSymLinks to set
	 */
	public void setStatTotalSymLinks(long statTotalSymLinks) {
		this.statTotalSymLinks = statTotalSymLinks;
	}

	/**
	 * @return the statAvgSymLinkCount
	 */
	public long getStatAvgSymLinkCount() {
		return statAvgSymLinkCount;
	}

	/**
	 * @param statAvgSymLinkCount the statAvgSymLinkCount to set
	 */
	public void setStatAvgSymLinkCount(long statAvgSymLinkCount) {
		this.statAvgSymLinkCount = statAvgSymLinkCount;
	}

	/**
	 * @return the statTotalSymbolLevel
	 */
	public long getStatTotalSymbolLevel() {
		return statTotalSymbolLevel;
	}

	/**
	 * @param statTotalSymbolLevel the statTotalSymbolLevel to set
	 */
	public void setStatTotalSymbolLevel(long statTotalSymbolLevel) {
		this.statTotalSymbolLevel = statTotalSymbolLevel;
	}

	/**
	 * @return the statTotalConLinks
	 */
	public long getStatTotalConLinks() {
		return statTotalConLinks;
	}

	/**
	 * @param statTotalConLinks the statTotalConLinks to set
	 */
	public void setStatTotalConLinks(long statTotalConLinks) {
		this.statTotalConLinks = statTotalConLinks;
	}

	/**
	 * @return the statAvgConLinkCount
	 */
	public long getStatAvgConLinkCount() {
		return statAvgConLinkCount;
	}

	/**
	 * @param statAvgConLinkCount the statAvgConLinkCount to set
	 */
	public void setStatAvgConLinkCount(long statAvgConLinkCount) {
		this.statAvgConLinkCount = statAvgConLinkCount;
	}

}
