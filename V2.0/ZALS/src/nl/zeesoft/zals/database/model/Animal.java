package nl.zeesoft.zals.database.model;

import nl.zeesoft.zodb.database.DbDataObject;

public abstract class Animal extends Organism {
	public final static String			ACTION_TURN_LEFT		= "turnLeft";
	public final static String			ACTION_MOVE_FORWARD		= "moveForward";
	public final static String			ACTION_TURN_RIGHT		= "turnRight";
	public final static String			ACTION_TAKE_BITE		= "takeBite";

	public final static String			FEEDBACK_TURNED_LEFT	= "turnedLeft";
	public final static String			FEEDBACK_MOVED_FORWARD	= "movedForward";
	public final static String			FEEDBACK_TURNED_RIGHT	= "turnedRight";
	public final static String			FEEDBACK_TOOK_BITE		= "tookBite";
	
	private int							rotation				= 0;
	private int							score					= 0;
	private int							topScore				= 0;
	private long						trainedStates			= 0;
	private long						generation				= 0;
	private StringBuilder				trainingModules			= new StringBuilder();
	private StringBuilder				confabulatingModules	= new StringBuilder();
	
	// Not persisted
	private StringBuilder				plannedActions			= null;
	
	public int getForwardPosX() {
		int r = getPosX();
		if (getRotation()==90) {
			r++;
		} else if (getRotation()==270) {
			r--;
		}
		return r;
	}
	
	public int getForwardPosY() {
		int r = getPosY();
		if (getRotation()==0) {
			r--;
		} else if (getRotation()==180) {
			r++;
		}
		return r;
	}

	public void incrementScore() {
		score++;
		if (score>topScore) {
			topScore = score;
		}
	}

	public void incrementGeneration() {
		generation++;
	}

	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("rotation")) {
			setRotation(Integer.parseInt(obj.getPropertyValue("rotation").toString()));
		}
		if (obj.hasPropertyValue("score")) {
			setScore(Integer.parseInt(obj.getPropertyValue("score").toString()));
		}
		if (obj.hasPropertyValue("topScore")) {
			setTopScore(Integer.parseInt(obj.getPropertyValue("topScore").toString()));
		}
		if (obj.hasPropertyValue("trainedStates")) {
			setTrainedStates(Long.parseLong(obj.getPropertyValue("trainedStates").toString()));
		}
		if (obj.hasPropertyValue("generation")) {
			setGeneration(Long.parseLong(obj.getPropertyValue("generation").toString()));
		}
		if (obj.hasPropertyValue("trainingModules")) {
			setTrainingModules(obj.getPropertyValue("trainingModules"));
		}
		if (obj.hasPropertyValue("confabulatingModules")) {
			setConfabulatingModules(obj.getPropertyValue("confabulatingModules"));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("rotation",new StringBuilder("" + getRotation()));
		r.setPropertyValue("score",new StringBuilder("" + getScore()));
		r.setPropertyValue("topScore",new StringBuilder("" + getTopScore()));
		r.setPropertyValue("trainedStates",new StringBuilder("" + getTrainedStates()));
		r.setPropertyValue("generation",new StringBuilder("" + getGeneration()));
		r.setPropertyValue("trainingModules",new StringBuilder(getTrainingModules()));
		r.setPropertyValue("confabulatingModules",new StringBuilder(getConfabulatingModules()));
		return r;
	}

	/**
	 * @return the rotation
	 */
	public int getRotation() {
		return rotation;
	}

	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation(int rotation) {
		rotation = rotation % 360;
		if (rotation % 90 != 0) {
			rotation = rotation - (rotation % 90); 
		}
		this.rotation = rotation;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * @return the topScore
	 */
	public int getTopScore() {
		return topScore;
	}

	/**
	 * @param topScore the topScore to set
	 */
	public void setTopScore(int topScore) {
		this.topScore = topScore;
	}

	/**
	 * @return the trainedStates
	 */
	public long getTrainedStates() {
		return trainedStates;
	}

	/**
	 * @param trainedStates the trainedStates to set
	 */
	public void setTrainedStates(long trainedStates) {
		this.trainedStates = trainedStates;
	}

	/**
	 * @return the generation
	 */
	public long getGeneration() {
		return generation;
	}

	/**
	 * @param generation the generation to set
	 */
	public void setGeneration(long generation) {
		this.generation = generation;
	}

	/**
	 * @return the plannedActions
	 */
	public StringBuilder getPlannedActions() {
		return plannedActions;
	}

	/**
	 * @param plannedActions the plannedActions to set
	 */
	public void setPlannedActions(StringBuilder plannedActions) {
		this.plannedActions = plannedActions;
	}

	/**
	 * @return the trainingModules
	 */
	public StringBuilder getTrainingModules() {
		return trainingModules;
	}

	/**
	 * @param trainingModules the trainingModules to set
	 */
	public void setTrainingModules(StringBuilder trainingModules) {
		this.trainingModules = trainingModules;
	}

	/**
	 * @return the confabulatingModules
	 */
	public StringBuilder getConfabulatingModules() {
		return confabulatingModules;
	}

	/**
	 * @param confabulatingModules the confabulatingModules to set
	 */
	public void setConfabulatingModules(StringBuilder confabulatingModules) {
		this.confabulatingModules = confabulatingModules;
	}
}
