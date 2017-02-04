package nl.zeesoft.zsc.confabulator;

import java.util.Date;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsc.Generic;

/**
 * Abstract confabulation object.
 */
public abstract class ConfabulationObject {
	private int					maxOutputSymbols		= 32;
	private int					accuracy				= 100;
	private boolean				logModuleSymbolLevels	= false;

	private ZStringSymbolParser	sequence				= new ZStringSymbolParser();
	private ZStringSymbolParser	context					= new ZStringSymbolParser();

	private StringBuilder		log						= new StringBuilder();
	private StringBuilder		output					= new StringBuilder();

	public ConfabulationObject() {
		
	}

	public ConfabulationObject(String sequence) {
		this.sequence = new ZStringSymbolParser(sequence);
	}

	public ConfabulationObject(String sequence, String context) {
		this.sequence = new ZStringSymbolParser(sequence);
		this.context = new ZStringSymbolParser(context);
	}

	/**
	 * Can be used to copy the settings of this confabulation to another.
	 * 
	 * @param copyTo The confabulation to copy the settings to
	 */
	public void copyToConfabulationObject(ConfabulationObject copyTo) {
		copyTo.setMaxOutputSymbols(maxOutputSymbols);
		copyTo.setAccuracy(accuracy);
		copyTo.setLogModuleSymbolLevels(logModuleSymbolLevels);
		copyTo.setSequence(sequence);
		copyTo.setContext(context);
	}
	
	/**
	 * Used by the confabulator to add a line to the confabulation log.
	 * 
	 * @param line The line to add a line to the confabulation log
	 */
	protected void addLogLine(String line) {
		log.append(Generic.getDateTimeString(new Date()));
		log.append(":");
		log.append(" ");
		log.append(line);
		log.append("\n");
	}

	/**
	 * Used by the confabulator to add a symbol to the output.
	 * 
	 * @param symbol The symbol to add a symbol to the output
	 */
	protected void addSymbolToOutputSequence(String symbol) {
		if (output.length()>0) {
			output.append(" ");
		}
		output.append(symbol);
	}

	/**
	 * Returns the maximum number of output symbols.
	 * 
	 * @return The maximum number of output symbols
	 */
	public int getMaxOutputSymbols() {
		return maxOutputSymbols;
	}

	/**
	 * Sets the maximum number of output symbols (default 32).
	 * 
	 * @param maxOutputSymbols The maximum number of output symbols
	 */
	public void setMaxOutputSymbols(int maxOutputSymbols) {
		if (maxOutputSymbols<1) {
			maxOutputSymbols = 1;
		} else if (maxOutputSymbols>999) {
			maxOutputSymbols = 999;
		}
		this.maxOutputSymbols = maxOutputSymbols;
	}

	/**
	 * Returns the accuracy.
	 * 
	 * @return The accuracy
	 */
	public int getAccuracy() {
		return accuracy;
	}

	/**
	 * Sets the accuracy to be used for this confabulation (range 10 - 10000, default 100).
	 * 
	 * Higher accuracy means small differences are magnified more.
	 * 
	 * @param accuracy The accuracy to be used for this confabulation
	 */
	public void setAccuracy(int accuracy) {
		if (accuracy<10) {
			accuracy = 10;
		} else if (accuracy>10000) {
			accuracy = 10000;
		}
		this.accuracy = accuracy;
	}

	/**
	 * Indicates module symbol levels should be included in the confabulation log.
	 * 
	 * @return True if module symbol levels should be included in the confabulation log
	 */
	public boolean isLogModuleSymbolLevels() {
		return logModuleSymbolLevels;
	}

	/**
	 * Indicates module symbol levels should be included in the confabulation log (default false).
	 * 
	 * @param logModuleSymbolLevels True if module symbol levels should be included in the confabulation log
	 */
	public void setLogModuleSymbolLevels(boolean logModuleSymbolLevels) {
		this.logModuleSymbolLevels = logModuleSymbolLevels;
	}

	/**
	 * Returns the input symbol sequence.
	 * 
	 * @return The input symbol sequence
	 */
	public ZStringSymbolParser getSequence() {
		return sequence;
	}

	/**
	 * Sets the input symbol sequence.
	 * 
	 * @param sequence The input symbol sequence
	 */
	public void setSequence(ZStringSymbolParser sequence) {
		this.sequence = sequence;
	}

	/**
	 * Returns the context symbols.
	 * 
	 * @return The context symbols
	 */
	public ZStringSymbolParser getContext() {
		return context;
	}

	/**
	 * Sets the context symbols.
	 * 
	 * @param context The context symbols
	 */
	public void setContext(ZStringSymbolParser context) {
		this.context = context;
	}	

	/**
	 * Returns the confabulation log.
	 * 
	 * @return The confabulation log
	 */
	public StringBuilder getLog() {
		return log;
	}

	/**
	 * Sets the confabulation log.
	 * 
	 * @param log The confabulation log
	 */
	public void setLog(StringBuilder log) {
		this.log = log;
	}

	/**
	 * Returns the confabulation output.
	 * 
	 * @return The confabulation output
	 */
	public StringBuilder getOutput() {
		return output;
	}

	/**
	 * Sets the confabulation output.
	 * 
	 * @param output The confabulation output
	 */
	public void setOutput(StringBuilder output) {
		this.output = output;
	}
}
