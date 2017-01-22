package nl.zeesoft.zdk.confabulator.confabulations;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.confabulator.ConfabulationObject;

/**
 * Context confabulation.
 */
public final class ContextConfabulation extends ConfabulationObject {
	private boolean		outputAllSymbols	= false;
	private List<Long>	symbolLevels		= new ArrayList<Long>();
	
	public ContextConfabulation() {
		
	}

	public ContextConfabulation(String sequence) {
		super(sequence);
	}

	public ContextConfabulation(String sequence, String context) {
		super(sequence,context);
	}

	/**
	 * Can be used to copy the settings of this confabulation to another.
	 * 
	 * @param copyTo The confabulation to copy the settings to
	 */
	@Override
	public void copyToConfabulationObject(ConfabulationObject copyTo) {
		super.copyToConfabulationObject(copyTo);
		if (copyTo instanceof ContextConfabulation) {
			((ContextConfabulation) copyTo).setOutputAllSymbols(outputAllSymbols);
			for (long level: symbolLevels) {
				((ContextConfabulation) copyTo).getSymbolLevels().add(level);
			}
		}
	}
	
	/**
	 * Indicates all activated symbols should be added to the output.
	 * 
	 * @return true if all activated should be added to the output
	 */
	public boolean isOutputAllSymbols() {
		return outputAllSymbols;
	}

	/**
	 * Indicates all activated symbols should be added to the output.
	 * 
	 * @param outputAllSymbols True if all activated symbols should be added to the output
	 */
	public void setOutputAllSymbols(boolean outputAllSymbols) {
		this.outputAllSymbols = outputAllSymbols;
	}

	/**
	 * Returns the activated symbol levels (ordered by output symbol order).
	 * 
	 * @return The activated symbol levels
	 */
	public List<Long> getSymbolLevels() {
		return symbolLevels;
	}
}
