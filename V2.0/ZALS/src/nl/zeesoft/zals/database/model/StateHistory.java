package nl.zeesoft.zals.database.model;

import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class StateHistory extends HlpObject {
	private long						dateTime				= 0;
	private StringBuilder				objectData				= new StringBuilder();

	private Environment					environment				= null;
	private long						environmentId			= 0;

	public void addEnvironmentObjectToObjectData(EnvironmentObject obj) {
		String color = Environment.COLOR_PLANT;
		int energy = 0;
		if (obj instanceof Organism) {
			energy = ((Organism) obj).getEnergy();
		}
		Animal animal = null;
		if (obj instanceof Animal) {
			animal = (Animal) obj;
			if (obj instanceof Herbivore) {
				color = Environment.COLOR_HERBIVORE;
			} else if (obj instanceof Carnivore) {
				color = Environment.COLOR_CARNIVORE;
			}
		}
		if (objectData.length()>0) {
			objectData.append("\n");
		}
		objectData.append(color);
		objectData.append(";");
		objectData.append(obj.getId());
		objectData.append(";");
		objectData.append(obj.getPosX());
		objectData.append(";");
		objectData.append(obj.getPosY());
		objectData.append(";");
		objectData.append(energy);
		objectData.append(";");
		if (animal!=null) {
			objectData.append(animal.getRotation());
		}
		objectData.append(";");
		if (animal!=null) {
			objectData.append(animal.getScore());
		}
		objectData.append(";");
		if (animal!=null) {
			objectData.append(animal.getTopScore());
		}
		objectData.append(";");
		if (animal!=null) {
			objectData.append(animal.getGeneration());
		}
		objectData.append(";");
		if (animal!=null) {
			objectData.append(animal.getTrainedStates());
		}
		objectData.append(";");
		if (animal!=null && animal.getPlannedActions()!=null) {
			objectData.append(animal.getPlannedActions());
		}
		objectData.append(";");
		if (animal!=null && animal.getConfabulatingModules()!=null) {
			objectData.append(animal.getConfabulatingModules());
		}
		objectData.append(";");
		if (animal!=null && animal.getTrainingModules()!=null) {
			objectData.append(animal.getTrainingModules());
		}
	}

	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("dateTime")) {
			setDateTime(Integer.parseInt(obj.getPropertyValue("dateTime").toString()));
		}
		if (obj.hasPropertyValue("objectData")) {
			setObjectData(obj.getPropertyValue("objectData"));
		}
		if (obj.hasPropertyValue("environment") && obj.getLinkValue("environment").size()>0) {
			setEnvironmentId(obj.getLinkValue("environment").get(0));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("dateTime",new StringBuilder("" + getDateTime()));
		r.setPropertyValue("objectData",new StringBuilder(getObjectData()));
		r.setLinkValue("environment",getEnvironmentId());
		return r;
	}

	/**
	 * @return the environment
	 */
	public Environment getEnvironment() {
		return environment;
	}

	/**
	 * @param environment the environment to set
	 */
	public void setEnvironment(Environment environment) {
		this.environment = environment;
		if (environment!=null) {
			environmentId = environment.getId();
		} else {
			environmentId = 0;
		}
	}

	/**
	 * @return the environmentId
	 */
	public long getEnvironmentId() {
		return environmentId;
	}

	/**
	 * @param environmentId the environmentId to set
	 */
	public void setEnvironmentId(long environmentId) {
		this.environmentId = environmentId;
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
	 * @return the objectData
	 */
	public StringBuilder getObjectData() {
		return objectData;
	}

	/**
	 * @param objectData the objectData to set
	 */
	public void setObjectData(StringBuilder objectData) {
		this.objectData = objectData;
	}
}
