package nl.zeesoft.zac.module.confabulate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zac.ZAC;
import nl.zeesoft.zodb.Generic;

public class ConInputOutput {
	private int								thinkWidth				= 50;
	private int								maxOutputSymbols		= 32;
	private StringBuilder 					inputSequence			= new StringBuilder();		
	private String[]						inputContextSymbols		= new String[8];		
	
	private long							firedLinks				= 0;
	private StringBuilder 					log						= new StringBuilder();		
	private StringBuilder 					outputSequence			= new StringBuilder();		
	private String[]						outputContextSymbols	= new String[8];
	
	private List<String>					inputSequenceSymbols	= null;
	
	public ConInputOutput(StringBuilder inputSequence) {
		this.inputSequence = inputSequence;
		initializeContext("","","","","","","","");
	}

	public ConInputOutput(StringBuilder inputSequence,String inputContextSymbol1,String inputContextSymbol2,String inputContextSymbol3,String inputContextSymbol4) {
		this.inputSequence = inputSequence;
		initializeContext(inputContextSymbol1,inputContextSymbol2,inputContextSymbol3,inputContextSymbol4,"","","","");
	}

	public ConInputOutput(StringBuilder inputSequence,String inputContextSymbol1,String inputContextSymbol2,String inputContextSymbol3,String inputContextSymbol4,String inputContextSymbol5,String inputContextSymbol6,String inputContextSymbol7,String inputContextSymbol8) {
		this.inputSequence = inputSequence;
		initializeContext(inputContextSymbol1,inputContextSymbol2,inputContextSymbol3,inputContextSymbol4,inputContextSymbol5,inputContextSymbol6,inputContextSymbol7,inputContextSymbol8);
	}

	public void addLogLine(String line) {
		log.append(Generic.getDateTimeString(new Date()));
		log.append(": ");
		log.append(line);
		log.append("\n");
	}

	public void addSymbolToOutputSequence(String symbol) {
		if (getOutputSequence().length()>0) {
			getOutputSequence().append(" ");
		}
		getOutputSequence().append(symbol);
	}

	public void incrementFiredLinks(long firedLinks) {
		this.firedLinks+=firedLinks;
	}
	
	public List<String> getInputSequenceAsSymbolList() {
		if (inputSequenceSymbols==null) {
			inputSequenceSymbols = new ArrayList<String>();
			for (StringBuilder sym: ZAC.parseSymbolsFromText(inputSequence)) {
				inputSequenceSymbols.add(sym.toString());
			}
		}
		return inputSequenceSymbols;
	}
	
	public List<String> getInputContextAsSymbolList() {
		return getContextAsSymbolList(getInputContextSymbols());
	}

	public List<String> getOutputContextAsSymbolList() {
		return getContextAsSymbolList(getOutputContextSymbols());
	}
	
	/**
	 * @return the log
	 */
	public StringBuilder getLog() {
		return log;
	}

	/**
	 * @return the inputSequence
	 */
	public StringBuilder getInputSequence() {
		return inputSequence;
	}

	/**
	 * @param inputSequence the inputSequence to set
	 */
	public void setInputSequence(StringBuilder inputSequence) {
		this.inputSequence = inputSequence;
	}

	/**
	 * @return the inputContextSymbols
	 */
	public String[] getInputContextSymbols() {
		return inputContextSymbols;
	}

	/**
	 * @param inputContextSymbols the inputContextSymbols to set
	 */
	public void setInputContextSymbols(String[] inputContextSymbols) {
		this.inputContextSymbols = inputContextSymbols;
	}

	/**
	 * @return the outputSequence
	 */
	public StringBuilder getOutputSequence() {
		return outputSequence;
	}

	/**
	 * @param outputSequence the outputSequence to set
	 */
	public void setOutputSequence(StringBuilder outputSequence) {
		this.outputSequence = outputSequence;
	}

	/**
	 * @return the outputContextSymbols
	 */
	public String[] getOutputContextSymbols() {
		return outputContextSymbols;
	}

	/**
	 * @param outputContextSymbols the outputContextSymbols to set
	 */
	public void setOutputContextSymbols(String[] outputContextSymbols) {
		this.outputContextSymbols = outputContextSymbols;
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
	 * @return the firedLinks
	 */
	public long getFiredLinks() {
		return firedLinks;
	}

	private void initializeContext(String inputContextSymbol1,String inputContextSymbol2,String inputContextSymbol3,String inputContextSymbol4,String inputContextSymbol5,String inputContextSymbol6,String inputContextSymbol7,String inputContextSymbol8) {
		for (int i = 0; i < inputContextSymbols.length; i++) {
			if (i==0) {
				inputContextSymbols[i] = inputContextSymbol1;
			} else if (i==1) {
				inputContextSymbols[i] = inputContextSymbol2;
			} else if (i==2) {
				inputContextSymbols[i] = inputContextSymbol3;
			} else if (i==3) {
				inputContextSymbols[i] = inputContextSymbol4;
			} else if (i==4) {
				inputContextSymbols[i] = inputContextSymbol5;
			} else if (i==5) {
				inputContextSymbols[i] = inputContextSymbol6;
			} else if (i==6) {
				inputContextSymbols[i] = inputContextSymbol7;
			} else if (i==7) {
				inputContextSymbols[i] = inputContextSymbol8;
			}
			outputContextSymbols[i] = "";
		}
	}	

	private List<String> getContextAsSymbolList(String[] contextSymbols) {
		List<String> symbols = new ArrayList<String>();
		for (int i = 0; i < contextSymbols.length; i++) {
			if (contextSymbols[i].length()>0) {
				symbols.add(contextSymbols[i]);
			}
		}
		return symbols;
	}
}
