package nl.zeesoft.zdk.confabulator.confabulations;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.confabulator.ConfabulationObject;
import nl.zeesoft.zdk.confabulator.ConfabulationSequenceObject;

/**
 * Correction confabulation.
 */
public final class CorrectionConfabulation extends ConfabulationSequenceObject {
	private List<String>				correctionKeys		= new ArrayList<String>();
	private List<String>				correctionValues	= new ArrayList<String>();

	public CorrectionConfabulation() {
		
	}

	public CorrectionConfabulation(String sequence) {
		super(sequence);
	}

	public CorrectionConfabulation(String sequence, String context) {
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
		if (copyTo instanceof CorrectionConfabulation) {
			((CorrectionConfabulation) copyTo).getCorrectionKeys().clear();
			((CorrectionConfabulation) copyTo).getCorrectionValues().clear();
			for (String key: correctionKeys) {
				((CorrectionConfabulation) copyTo).getCorrectionKeys().add(key);
			}
			for (String value: correctionValues) {
				((CorrectionConfabulation) copyTo).getCorrectionValues().add(value);
			}
		}
	}
	
	/**
	 * Used by the confabulator to add a correction key/value combination to the correction key and value arrays.
	 * 
	 * @param key The correction key
	 * @param value The correction value
	 */
	public void addCorrection(String key, String value) {
		correctionKeys.add(key);
		correctionValues.add(value);
	}

	/**
	 * Returns a list of correction keys ordered by correction order.
	 * 
	 * Correction keys are symbols that have been corrected in the input sequence.
	 * 
	 * @return An ordered list of correction keys
	 */
	public List<String> getCorrectionKeys() {
		return correctionKeys;
	}

	/**
	 * Returns a list of correction values ordered by correction order.
	 * 
	 * Correction values are symbols that have been used to correct the input sequence.
	 * 
	 * @return An ordered list of correction values
	 */
	public List<String> getCorrectionValues() {
		return correctionValues;
	}
}
