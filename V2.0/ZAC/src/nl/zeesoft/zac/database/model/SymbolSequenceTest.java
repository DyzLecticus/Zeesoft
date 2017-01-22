package nl.zeesoft.zac.database.model;

import nl.zeesoft.zodb.database.DbDataObject;

public class SymbolSequenceTest extends SymbolSequence {
	public final static String		CONFABULATE_CONTEXT		= "confabulateContext";
	public final static String		CONFABULATE_CORRECT		= "confabulateCorrect";
	public final static String		CONFABULATE_EXTEND		= "confabulateExtend";
	
	private String 					type					= CONFABULATE_CONTEXT;
	private int 					thinkWidth				= 50;
	private int 					maxOutputSymbols		= 32;

	private long 					firedLinks				= 0;
	private StringBuilder 			log						= new StringBuilder();
	private StringBuilder 			output					= new StringBuilder();
	
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("type")) {
			setType(obj.getPropertyValue("type").toString());
		}
		if (obj.hasPropertyValue("thinkWidth")) {
			setThinkWidth(Integer.parseInt(obj.getPropertyValue("thinkWidth").toString()));
		}
		if (obj.hasPropertyValue("maxOutputSymbols")) {
			setMaxOutputSymbols(Integer.parseInt(obj.getPropertyValue("maxOutputSymbols").toString()));
		}
		if (obj.hasPropertyValue("firedLinks")) {
			setFiredLinks(Long.parseLong(obj.getPropertyValue("firedLinks").toString()));
		}
		if (obj.hasPropertyValue("log")) {
			setLog(obj.getPropertyValue("log"));
		}
		if (obj.hasPropertyValue("output")) {
			setOutput(obj.getPropertyValue("output"));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("type",new StringBuilder(getType()));
		r.setPropertyValue("thinkWidth",new StringBuilder("" + getThinkWidth()));
		r.setPropertyValue("maxOutputSymbols",new StringBuilder("" + getMaxOutputSymbols()));
		r.setPropertyValue("firedLinks",new StringBuilder("" + getFiredLinks()));
		r.setPropertyValue("log",new StringBuilder(getLog()));
		r.setPropertyValue("output",new StringBuilder(getOutput()));
		return r;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the thinkWidth
	 */
	public int getThinkWidth() {
		return thinkWidth;
	}

	/**
	 * @param thinkWidth the thinkWidth to set
	 */
	public void setThinkWidth(int thinkWidth) {
		this.thinkWidth = thinkWidth;
	}

	/**
	 * @return the maxOutputSymbols
	 */
	public int getMaxOutputSymbols() {
		return maxOutputSymbols;
	}

	/**
	 * @param maxOutputSymbols the maxOutputSymbols to set
	 */
	public void setMaxOutputSymbols(int maxOutputSymbols) {
		this.maxOutputSymbols = maxOutputSymbols;
	}

	/**
	 * @return the log
	 */
	public StringBuilder getLog() {
		return log;
	}

	/**
	 * @param log the log to set
	 */
	public void setLog(StringBuilder log) {
		this.log = log;
	}

	/**
	 * @return the output
	 */
	public StringBuilder getOutput() {
		return output;
	}

	/**
	 * @param output the output to set
	 */
	public void setOutput(StringBuilder output) {
		this.output = output;
	}

	/**
	 * @return the firedLinks
	 */
	public long getFiredLinks() {
		return firedLinks;
	}

	/**
	 * @param firedLinks the firedLinks to set
	 */
	public void setFiredLinks(long firedLinks) {
		this.firedLinks = firedLinks;
	}	
}
