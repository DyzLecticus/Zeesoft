package nl.zeesoft.zacs.database.model;

import java.util.Date;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbDataObject;

public class Assignment extends Example {
	private String			name					= "";
	private int				maxSymbols				= 128;
	private boolean			stopOnLineEndSymbol		= false;
	private int				thinkWidth				= 50;
	private boolean			thinkFast				= false;
	private boolean			logExtended				= false;
	private boolean			contextDynamic			= false;
	private boolean			correctInput			= false;
	private boolean			correctLineEnd			= false;
	private boolean			correctInputOnly		= false;

	private long			workingModuleId			= 0;
	private Module			workingModule			= null;
	private StringBuilder	workingContext			= new StringBuilder();
	private StringBuilder	workingOutput			= new StringBuilder();
	
	private StringBuilder	log						= new StringBuilder();
	private long			firedSymbolLinks		= 0;
	private long			firedContextLinks		= 0;
	private long			dateTimeFinished		= 0;
	private long			durationMilliseconds	= 0;
	private long			numberOfSymbols			= 0;
	private StringBuilder	inputContext			= new StringBuilder();
	private StringBuilder	outputContext			= new StringBuilder();

	private StringBuilder	correctedInput			= new StringBuilder();
	private StringBuilder	correctedInputSymbols	= new StringBuilder();

	private StringBuilder	prevOutput1				= new StringBuilder();
	private StringBuilder	prevOutput2				= new StringBuilder();
	private StringBuilder	prevOutput3				= new StringBuilder();
	private long			dateTimePrevOutput1		= 0;
	private long			dateTimePrevOutput2		= 0;
	private long			dateTimePrevOutput3		= 0;
	
	public void incrementFiredSymbolLinks(int amount) {
		this.firedSymbolLinks += amount;
	}

	public void incrementFiredContextLinks(int amount) {
		this.firedContextLinks += amount;
	}
	
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("name")) {
			setName(obj.getPropertyValue("name").toString());
		}
		if (obj.hasPropertyValue("prevOutput1")) {
			setPrevOutput1(obj.getPropertyValue("prevOutput1"));
		}
		if (obj.hasPropertyValue("prevOutput2")) {
			setPrevOutput2(obj.getPropertyValue("prevOutput2"));
		}
		if (obj.hasPropertyValue("prevOutput3")) {
			setPrevOutput3(obj.getPropertyValue("prevOutput3"));
		}
		if (obj.hasPropertyValue("dateTimePrevOutput1")) {
			setDateTimePrevOutput1(Long.parseLong(obj.getPropertyValue("dateTimePrevOutput1").toString()));
		}
		if (obj.hasPropertyValue("dateTimePrevOutput2")) {
			setDateTimePrevOutput2(Long.parseLong(obj.getPropertyValue("dateTimePrevOutput2").toString()));
		}
		if (obj.hasPropertyValue("dateTimePrevOutput3")) {
			setDateTimePrevOutput3(Long.parseLong(obj.getPropertyValue("dateTimePrevOutput3").toString()));
		}
		if (obj.hasPropertyValue("maxSymbols")) {
			setMaxSymbols(Integer.parseInt(obj.getPropertyValue("maxSymbols").toString()));
		}
		if (obj.hasPropertyValue("stopOnLineEndSymbol")) {
			setStopOnLineEndSymbol(Boolean.parseBoolean(obj.getPropertyValue("stopOnLineEndSymbol").toString()));
		}
		if (obj.hasPropertyValue("thinkWidth")) {
			setThinkWidth(Integer.parseInt(obj.getPropertyValue("thinkWidth").toString()));
		}
		if (obj.hasPropertyValue("thinkFast")) {
			setThinkFast(Boolean.parseBoolean(obj.getPropertyValue("thinkFast").toString()));
		}
		if (obj.hasPropertyValue("logExtended")) {
			setLogExtended(Boolean.parseBoolean(obj.getPropertyValue("logExtended").toString()));
		}
		if (obj.hasPropertyValue("contextDynamic")) {
			setContextDynamic(Boolean.parseBoolean(obj.getPropertyValue("contextDynamic").toString()));
		}
		if (obj.hasPropertyValue("correctInput")) {
			setCorrectInput(Boolean.parseBoolean(obj.getPropertyValue("correctInput").toString()));
		}
		if (obj.hasPropertyValue("correctLineEnd")) {
			setCorrectLineEnd(Boolean.parseBoolean(obj.getPropertyValue("correctLineEnd").toString()));
		}
		if (obj.hasPropertyValue("correctInputOnly")) {
			setCorrectInputOnly(Boolean.parseBoolean(obj.getPropertyValue("correctInputOnly").toString()));
		}
		if (obj.hasPropertyValue("log")) {
			setLog(obj.getPropertyValue("log"));
		}
		if (obj.hasPropertyValue("firedSymbolLinks")) {
			setFiredSymbolLinks(Long.parseLong(obj.getPropertyValue("firedSymbolLinks").toString()));
		}
		if (obj.hasPropertyValue("firedContextLinks")) {
			setFiredContextLinks(Long.parseLong(obj.getPropertyValue("firedContextLinks").toString()));
		}
		if (obj.hasPropertyValue("dateTimeFinished")) {
			setDateTimeFinished(Long.parseLong(obj.getPropertyValue("dateTimeFinished").toString()));
		}
		if (obj.hasPropertyValue("durationMilliseconds")) {
			setDurationMilliseconds(Long.parseLong(obj.getPropertyValue("durationMilliseconds").toString()));
		}
		if (obj.hasPropertyValue("numberOfSymbols")) {
			setNumberOfSymbols(Long.parseLong(obj.getPropertyValue("numberOfSymbols").toString()));
		}
		if (obj.hasPropertyValue("workingModule") && obj.getLinkValue("workingModule")!=null && obj.getLinkValue("workingModule").size()>0) {
			setWorkingModuleId(obj.getLinkValue("workingModule").get(0));
		}
		if (obj.hasPropertyValue("workingContext")) {
			setWorkingContext(obj.getPropertyValue("workingContext"));
		}
		if (obj.hasPropertyValue("workingOutput")) {
			setWorkingOutput(obj.getPropertyValue("workingOutput"));
		}
		if (obj.hasPropertyValue("inputContext")) {
			setInputContext(obj.getPropertyValue("inputContext"));
		}
		if (obj.hasPropertyValue("outputContext")) {
			setOutputContext(obj.getPropertyValue("outputContext"));
		}
		if (obj.hasPropertyValue("correctedInput")) {
			setCorrectedInput(obj.getPropertyValue("correctedInput"));
		}
		if (obj.hasPropertyValue("correctedInputSymbols")) {
			setCorrectedInputSymbols(obj.getPropertyValue("correctedInputSymbols"));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("name",new StringBuilder(getName()));
		r.setPropertyValue("prevOutput1",getPrevOutput1());
		r.setPropertyValue("prevOutput2",getPrevOutput2());
		r.setPropertyValue("prevOutput3",getPrevOutput3());
		r.setPropertyValue("dateTimePrevOutput1",new StringBuilder("" + getDateTimePrevOutput1()));
		r.setPropertyValue("dateTimePrevOutput2",new StringBuilder("" + getDateTimePrevOutput2()));
		r.setPropertyValue("dateTimePrevOutput3",new StringBuilder("" + getDateTimePrevOutput3()));
		r.setPropertyValue("maxSymbols",new StringBuilder("" + getMaxSymbols()));
		r.setPropertyValue("stopOnLineEndSymbol",new StringBuilder("" + isStopOnLineEndSymbol()));
		r.setPropertyValue("thinkWidth",new StringBuilder("" + getThinkWidth()));
		r.setPropertyValue("thinkFast",new StringBuilder("" + isThinkFast()));
		r.setPropertyValue("logExtended",new StringBuilder("" + isLogExtended()));
		r.setPropertyValue("contextDynamic",new StringBuilder("" + isContextDynamic()));
		r.setPropertyValue("correctInput",new StringBuilder("" + isCorrectInput()));
		r.setPropertyValue("correctLineEnd",new StringBuilder("" + isCorrectLineEnd()));
		r.setPropertyValue("correctInputOnly",new StringBuilder("" + isCorrectInputOnly()));
		r.setPropertyValue("log",getLog());
		r.setPropertyValue("firedSymbolLinks",new StringBuilder("" + getFiredSymbolLinks()));
		r.setPropertyValue("firedContextLinks",new StringBuilder("" + getFiredContextLinks()));
		r.setPropertyValue("dateTimeFinished",new StringBuilder("" + getDateTimeFinished()));
		r.setPropertyValue("durationMilliseconds",new StringBuilder("" + getDurationMilliseconds()));
		r.setPropertyValue("numberOfSymbols",new StringBuilder("" + getNumberOfSymbols()));
		r.setLinkValue("workingModule",getWorkingModuleId());
		r.setPropertyValue("workingContext",getWorkingContext());
		r.setPropertyValue("workingOutput",getWorkingOutput());
		r.setPropertyValue("inputContext",getInputContext());
		r.setPropertyValue("outputContext",getOutputContext());
		r.setPropertyValue("correctedInput",getCorrectedInput());
		r.setPropertyValue("correctedInputSymbols",getCorrectedInputSymbols());
		return r;
	}

	public void appendLogLine(boolean debug,String line) {
		if (debug) {
			Messenger.getInstance().debug(this,line);
		}
		Date d = new Date();
		log.append(Generic.getTimeString(d,true));
		log.append(": ");
		log.append(line);
		log.append("\n");
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the maxSymbols
	 */
	public int getMaxSymbols() {
		return maxSymbols;
	}

	/**
	 * @param maxSymbols the maxSymbols to set
	 */
	public void setMaxSymbols(int maxSymbols) {
		this.maxSymbols = maxSymbols;
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
	 * @return the firedSymbolLinks
	 */
	public long getFiredSymbolLinks() {
		return firedSymbolLinks;
	}

	/**
	 * @param firedSymbolLinks the firedSymbolLinks to set
	 */
	public void setFiredSymbolLinks(long firedSymbolLinks) {
		this.firedSymbolLinks = firedSymbolLinks;
	}

	/**
	 * @return the workingModuleId
	 */
	public long getWorkingModuleId() {
		return workingModuleId;
	}

	/**
	 * @param workingModuleId the workingModuleId to set
	 */
	public void setWorkingModuleId(long workingModuleId) {
		this.workingModuleId = workingModuleId;
	}

	/**
	 * @return the workingModule
	 */
	public Module getWorkingModule() {
		return workingModule;
	}

	/**
	 * @param workingModule the workingModule to set
	 */
	public void setWorkingModule(Module workingModule) {
		this.workingModule = workingModule;
		if (workingModule==null) {
			workingModuleId = 0;
		} else {
			workingModuleId = workingModule.getId();
		}
	}

	/**
	 * @return the thinkFast
	 */
	public boolean isThinkFast() {
		return thinkFast;
	}

	/**
	 * @param thinkFast the thinkFast to set
	 */
	public void setThinkFast(boolean thinkFast) {
		this.thinkFast = thinkFast;
	}

	/**
	 * @return the logExtended
	 */
	public boolean isLogExtended() {
		return logExtended;
	}

	/**
	 * @param logExtended the logExtended to set
	 */
	public void setLogExtended(boolean logExtended) {
		this.logExtended = logExtended;
	}

	/**
	 * @return the dateTimeFinished
	 */
	public long getDateTimeFinished() {
		return dateTimeFinished;
	}

	/**
	 * @param dateTimeFinished the dateTimeFinished to set
	 */
	public void setDateTimeFinished(long dateTimeFinished) {
		this.dateTimeFinished = dateTimeFinished;
	}

	/**
	 * @return the durationMilliseconds
	 */
	public long getDurationMilliseconds() {
		return durationMilliseconds;
	}

	/**
	 * @param durationMilliseconds the durationMilliseconds to set
	 */
	public void setDurationMilliseconds(long durationMilliseconds) {
		this.durationMilliseconds = durationMilliseconds;
	}

	/**
	 * @return the numberOfSymbols
	 */
	public long getNumberOfSymbols() {
		return numberOfSymbols;
	}

	/**
	 * @param numberOfSymbols the numberOfSymbols to set
	 */
	public void setNumberOfSymbols(long numberOfSymbols) {
		this.numberOfSymbols = numberOfSymbols;
	}

	/**
	 * @return the prevOutput1
	 */
	public StringBuilder getPrevOutput1() {
		return prevOutput1;
	}

	/**
	 * @param prevOutput1 the prevOutput1 to set
	 */
	public void setPrevOutput1(StringBuilder prevOutput1) {
		this.prevOutput1 = prevOutput1;
	}

	/**
	 * @return the prevOutput2
	 */
	public StringBuilder getPrevOutput2() {
		return prevOutput2;
	}

	/**
	 * @param prevOutput2 the prevOutput2 to set
	 */
	public void setPrevOutput2(StringBuilder prevOutput2) {
		this.prevOutput2 = prevOutput2;
	}

	/**
	 * @return the prevOutput3
	 */
	public StringBuilder getPrevOutput3() {
		return prevOutput3;
	}

	/**
	 * @param prevOutput3 the prevOutput3 to set
	 */
	public void setPrevOutput3(StringBuilder prevOutput3) {
		this.prevOutput3 = prevOutput3;
	}

	/**
	 * @return the dateTimePrevOutput2
	 */
	public long getDateTimePrevOutput2() {
		return dateTimePrevOutput2;
	}

	/**
	 * @param dateTimePrevOutput2 the dateTimePrevOutput2 to set
	 */
	public void setDateTimePrevOutput2(long dateTimePrevOutput2) {
		this.dateTimePrevOutput2 = dateTimePrevOutput2;
	}

	/**
	 * @return the dateTimePrevOutput1
	 */
	public long getDateTimePrevOutput1() {
		return dateTimePrevOutput1;
	}

	/**
	 * @param dateTimePrevOutput1 the dateTimePrevOutput1 to set
	 */
	public void setDateTimePrevOutput1(long dateTimePrevOutput1) {
		this.dateTimePrevOutput1 = dateTimePrevOutput1;
	}

	/**
	 * @return the dateTimePrevOutput3
	 */
	public long getDateTimePrevOutput3() {
		return dateTimePrevOutput3;
	}

	/**
	 * @param dateTimePrevOutput3 the dateTimePrevOutput3 to set
	 */
	public void setDateTimePrevOutput3(long dateTimePrevOutput3) {
		this.dateTimePrevOutput3 = dateTimePrevOutput3;
	}

	/**
	 * @return the contextDynamic
	 */
	public boolean isContextDynamic() {
		return contextDynamic;
	}

	/**
	 * @param contextDynamic the contextDynamic to set
	 */
	public void setContextDynamic(boolean contextDynamic) {
		this.contextDynamic = contextDynamic;
	}

	/**
	 * @return the firedContextLinks
	 */
	public long getFiredContextLinks() {
		return firedContextLinks;
	}

	/**
	 * @param firedContextLinks the firedContextLinks to set
	 */
	public void setFiredContextLinks(long firedContextLinks) {
		this.firedContextLinks = firedContextLinks;
	}

	/**
	 * @return the workingContext
	 */
	public StringBuilder getWorkingContext() {
		return workingContext;
	}

	/**
	 * @param workingContext the workingContext to set
	 */
	public void setWorkingContext(StringBuilder workingContext) {
		this.workingContext = workingContext;
	}

	/**
	 * @return the workingOutput
	 */
	public StringBuilder getWorkingOutput() {
		return workingOutput;
	}

	/**
	 * @param workingOutput the workingOutput to set
	 */
	public void setWorkingOutput(StringBuilder workingOutput) {
		this.workingOutput = workingOutput;
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
	 * @return the outputContext
	 */
	public StringBuilder getOutputContext() {
		return outputContext;
	}

	/**
	 * @param outputContext the outputContext to set
	 */
	public void setOutputContext(StringBuilder outputContext) {
		this.outputContext = outputContext;
	}

	/**
	 * @return the stopOnLineEndSymbol
	 */
	public boolean isStopOnLineEndSymbol() {
		return stopOnLineEndSymbol;
	}

	/**
	 * @param stopOnLineEndSymbol the stopOnLineEndSymbol to set
	 */
	public void setStopOnLineEndSymbol(boolean stopOnLineEndSymbol) {
		this.stopOnLineEndSymbol = stopOnLineEndSymbol;
	}

	/**
	 * @return the inputContext
	 */
	public StringBuilder getInputContext() {
		return inputContext;
	}

	/**
	 * @param inputContext the inputContext to set
	 */
	public void setInputContext(StringBuilder inputContext) {
		this.inputContext = inputContext;
	}

	/**
	 * @return the correctInput
	 */
	public boolean isCorrectInput() {
		return correctInput;
	}

	/**
	 * @param correctInput the correctInput to set
	 */
	public void setCorrectInput(boolean correctInput) {
		this.correctInput = correctInput;
	}

	/**
	 * @return the correctedInput
	 */
	public StringBuilder getCorrectedInput() {
		return correctedInput;
	}

	/**
	 * @param correctedInput the correctedInput to set
	 */
	public void setCorrectedInput(StringBuilder correctedInput) {
		this.correctedInput = correctedInput;
	}

	/**
	 * @return the correctedInputSymbols
	 */
	public StringBuilder getCorrectedInputSymbols() {
		return correctedInputSymbols;
	}

	/**
	 * @param correctedInputSymbols the correctedInputSymbols to set
	 */
	public void setCorrectedInputSymbols(StringBuilder correctedInputSymbols) {
		this.correctedInputSymbols = correctedInputSymbols;
	}

	/**
	 * @return the correctLineEnd
	 */
	public boolean isCorrectLineEnd() {
		return correctLineEnd;
	}

	/**
	 * @param correctLineEnd the correctLineEnd to set
	 */
	public void setCorrectLineEnd(boolean correctLineEnd) {
		this.correctLineEnd = correctLineEnd;
	}

	/**
	 * @return the correctInputOnly
	 */
	public boolean isCorrectInputOnly() {
		return correctInputOnly;
	}

	/**
	 * @param correctInputOnly the correctInputOnly to set
	 */
	public void setCorrectInputOnly(boolean correctInputOnly) {
		this.correctInputOnly = correctInputOnly;
	}
}
