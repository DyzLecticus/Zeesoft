package nl.zeesoft.zdmk.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.Generic;
import nl.zeesoft.zdmk.model.transformations.TransformationObject;

/**
 * A ModelVersion is used to log the initial model state transformations and all transformations for a certain version.
 */
public final class ModelVersion {
	private int							number					= 0;
	private List<TransformationObject> 	transformations 		= new ArrayList<TransformationObject>();
	private List<TransformationObject> 	initialTransformations	= new ArrayList<TransformationObject>();
	private StringBuilder				log						= new StringBuilder();

	protected void appliedTransformation(TransformationObject transformation,boolean appendLog) {
		transformations.add(transformation.getCopy());
		if (appendLog) {
			addLogLine("Applied transformation: " + transformation);
		}
	}

	protected void failedToApplyTransformation(TransformationObject transformation,String error,boolean appendLog) {
		if (appendLog) {
			addLogLine("Failed to apply transformation: " + transformation + ", error: " + error);
		}
	}

	protected void addLogLine(String line) {
		log.append(Generic.getDateTimeString(new Date()));
		log.append(" ");
		log.append(line);
		log.append("\n");
	}

	protected ModelVersion getCopy() {
		ModelVersion r = new ModelVersion();
		r.setNumber(number);
		r.setLog(new StringBuilder(log));
		for (TransformationObject trans: transformations) {
			r.getTransformations().add(trans.getCopy());
		}
		for (TransformationObject trans: initialTransformations) {
			r.getInitialTransformations().add(trans.getCopy());
		}
		return r;
	}
	
	/**
	 * Returns the version number.
	 * 
	 * @return The version number
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * Sets the version number.
	 * 
	 * @param number The version number
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	
	/**
	 * Returns the version transformations.
	 * 
	 * @return The version transformations
	 */
	public List<TransformationObject> getTransformations() {
		return transformations;
	}
	
	/**
	 * Returns the initial model state transformations.
	 * 
	 * @return The initial model state transformations
	 */
	public List<TransformationObject> getInitialTransformations() {
		return initialTransformations;
	}

	/**
	 * Returns the version log.
	 * 
	 * @return The version log
	 */
	public StringBuilder getLog() {
		return log;
	}

	/**
	 * Sets the version log.
	 * 
	 * @param log The version log
	 */
	public void setLog(StringBuilder log) {
		this.log = log;
	}
}
