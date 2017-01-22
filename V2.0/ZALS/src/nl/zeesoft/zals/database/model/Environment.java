package nl.zeesoft.zals.database.model;

import java.util.List;

import nl.zeesoft.zals.simulator.animals.PlanningAnimal;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class Environment extends HlpObject {
	public static final int				SIZE_X					= 40;
	public static final int				SIZE_Y					= 40;
	public static final int				PLANTS					= 4;
	public static final long			MINIMUM_TRAINING_STATES	= 5000;

	public static final String			COLOR_NOTHING			= "black";
	public static final String			COLOR_WALL				= "white";
	public static final String			COLOR_PLANT				= "green";
	public static final String			COLOR_HERBIVORE			= "blue";
	public static final String			COLOR_CARNIVORE			= "red";
	public static final String			COLOR_DEAD_OBJECT		= "purple";

	public static final String			INTENSITY_NOTHING		= "nothing";
	public static final String			INTENSITY_FAINT			= "faint";
	public static final String			INTENSITY_WEAK			= "weak";
	public static final String			INTENSITY_STRONG		= "strong";
	public static final String			INTENSITY_BRIGHT		= "bright";

	public static final String[]		INTENSITIES				= {INTENSITY_BRIGHT,INTENSITY_STRONG,INTENSITY_WEAK,INTENSITY_FAINT,INTENSITY_NOTHING};

	public static final int				MAX_CONTEXT_COUNT		= 50000;
	public static final int				MAX_SEQUENCE_COUNT		= 50000;
	
	private String						classNameHerbivore		= PlanningAnimal.class.getName();
	private String						classNameCarnivore		= PlanningAnimal.class.getName();
	
	private int							bufferSize				= 4;

	private int							herbivores				= 8;
	private int							carnivores				= 2;

	private int							deathDurationSeconds	= 1;

	private int							energyActionLook		= 100;
	private int							energyActionMove		= 1000;
	private int							energyActionTurn		= 300;
	private int							energyActionBite		= 500;
	
	private int							energyInputPerSecond	= 500000;
	private int							maxEnergyPlant			= 500000;
 	private int							maxEnergyHerbivore		= 500000;
 	private int							maxEnergyHerbivoreBite	= 50000;
	private int							maxEnergyCarnivore		= 500000;
 	private int							maxEnergyCarnivoreBite	= 100000;

	private int							statesPerSecond			= 25;
	private int							keepStateHistorySeconds	= 10;
	
	public Herbivore getNewHerbivore(List<EnvironmentObject> objs) {
		Herbivore animal = new Herbivore();
		initializeAnimal(animal,objs);
		return animal;
	}

	public Carnivore getNewCarnivore(List<EnvironmentObject> objs) {
		Carnivore animal = new Carnivore();
		initializeAnimal(animal,objs);
		return animal;
	}

	public void repositionPlant(Plant plant,List<EnvironmentObject> objs) {
		boolean free = false;
		int minX = 0;
		int maxX = 0;
		int minY = 0;
		int maxY = 0;
		if (plant.getId()==1) {
			minX=0;
			maxX=10;
			minY=0;
			maxY=10;
		} else if (plant.getId()==2) {
			minX=(SIZE_X - 11);
			maxX=(SIZE_X - 1);
			minY=0;
			maxY=10;
		} else if (plant.getId()==3) {
			minX=0;
			maxX=10;
			minY=(SIZE_Y - 11);
			maxY=(SIZE_Y - 1);
		} else if (plant.getId()==4) {
			minX=(SIZE_X - 11);
			maxX=(SIZE_X - 1);
			minY=(SIZE_Y - 11);
			maxY=(SIZE_Y - 1);
		}
		int x = Generic.generateRandom(minX,maxX);
		int y = Generic.generateRandom(minY,maxY);
		while (!free) {
			boolean found = false;
			for (EnvironmentObject obj:objs) {
				if (obj.getPosX()==x && obj.getPosY()==y) {
					found = true;
					break;
				}
			}
			if (!found) {
				free = true;
			} else {
				x = Generic.generateRandom(minX,maxX);
				y = Generic.generateRandom(minY,maxY);
			}
		}
		plant.setPosX(x);
		plant.setPosY(y);
	}

	public void initializeAnimal(Animal animal,List<EnvironmentObject> objs) {
		animal.setEnvironment(this);
		animal.setScore(0);
		animal.incrementGeneration();
		if (animal instanceof Herbivore) {
			animal.setEnergy(maxEnergyHerbivore / 2);
		} else if (animal instanceof Carnivore) {
			animal.setEnergy(maxEnergyCarnivore / 2);
		}
		boolean free = false;
		int x = Generic.generateRandom(0,SIZE_X - 1);
		int y = Generic.generateRandom(0,SIZE_X - 1);
		while (!free) {
			boolean found = false;
			for (EnvironmentObject obj:objs) {
				if (obj.getPosX()==x && obj.getPosY()==y) {
					found = true;
					break;
				}
			}
			if (!found) {
				free = true;
			} else {
				x = Generic.generateRandom(0,SIZE_X - 1);
				y = Generic.generateRandom(0,SIZE_X - 1);
			}
		}
		animal.setPosX(x);
		animal.setPosY(y);
		animal.setRotation(Generic.generateRandom(0,359));
	}
	
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("classNameHerbivore")) {
			setClassNameHerbivore(obj.getPropertyValue("classNameHerbivore").toString());
		}
		if (obj.hasPropertyValue("classNameCarnivore")) {
			setClassNameCarnivore(obj.getPropertyValue("classNameCarnivore").toString());
		}
		if (obj.hasPropertyValue("bufferSize")) {
			setBufferSize(Integer.parseInt(obj.getPropertyValue("bufferSize").toString()));
		}
		if (obj.hasPropertyValue("herbivores")) {
			setHerbivores(Integer.parseInt(obj.getPropertyValue("herbivores").toString()));
		}
		if (obj.hasPropertyValue("carnivores")) {
			setCarnivores(Integer.parseInt(obj.getPropertyValue("carnivores").toString()));
		}
		if (obj.hasPropertyValue("energyInputPerSecond")) {
			setEnergyInputPerSecond(Integer.parseInt(obj.getPropertyValue("energyInputPerSecond").toString()));
		}
		if (obj.hasPropertyValue("energyActionLook")) {
			setEnergyActionLook(Integer.parseInt(obj.getPropertyValue("energyActionLook").toString()));
		}
		if (obj.hasPropertyValue("energyActionMove")) {
			setEnergyActionMove(Integer.parseInt(obj.getPropertyValue("energyActionMove").toString()));
		}
		if (obj.hasPropertyValue("energyActionTurn")) {
			setEnergyActionTurn(Integer.parseInt(obj.getPropertyValue("energyActionTurn").toString()));
		}
		if (obj.hasPropertyValue("energyActionBite")) {
			setEnergyActionBite(Integer.parseInt(obj.getPropertyValue("energyActionBite").toString()));
		}
		if (obj.hasPropertyValue("maxEnergyPlant")) {
			setMaxEnergyPlant(Integer.parseInt(obj.getPropertyValue("maxEnergyPlant").toString()));
		}
		if (obj.hasPropertyValue("maxEnergyHerbivore")) {
			setMaxEnergyHerbivore(Integer.parseInt(obj.getPropertyValue("maxEnergyHerbivore").toString()));
		}
		if (obj.hasPropertyValue("maxEnergyHerbivoreBite")) {
			setMaxEnergyHerbivoreBite(Integer.parseInt(obj.getPropertyValue("maxEnergyHerbivoreBite").toString()));
		}
		if (obj.hasPropertyValue("maxEnergyCarnivore")) {
			setMaxEnergyCarnivore(Integer.parseInt(obj.getPropertyValue("maxEnergyCarnivore").toString()));
		}
		if (obj.hasPropertyValue("maxEnergyCarnivoreBite")) {
			setMaxEnergyCarnivoreBite(Integer.parseInt(obj.getPropertyValue("maxEnergyCarnivoreBite").toString()));
		}
		if (obj.hasPropertyValue("statesPerSecond")) {
			setStatesPerSecond(Integer.parseInt(obj.getPropertyValue("statesPerSecond").toString()));
		}
		if (obj.hasPropertyValue("keepStateHistorySeconds")) {
			setKeepStateHistorySeconds(Integer.parseInt(obj.getPropertyValue("keepStateHistorySeconds").toString()));
		}
		if (obj.hasPropertyValue("deathDurationSeconds")) {
			setDeathDurationSeconds(Integer.parseInt(obj.getPropertyValue("deathDurationSeconds").toString()));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("classNameHerbivore",new StringBuilder(getClassNameHerbivore()));
		r.setPropertyValue("classNameCarnivore",new StringBuilder(getClassNameCarnivore()));
		r.setPropertyValue("bufferSize",new StringBuilder("" + getBufferSize()));
		r.setPropertyValue("herbivores",new StringBuilder("" + getHerbivores()));
		r.setPropertyValue("carnivores",new StringBuilder("" + getCarnivores()));
		r.setPropertyValue("energyInputPerSecond",new StringBuilder("" + getEnergyInputPerSecond()));
		r.setPropertyValue("energyActionLook",new StringBuilder("" + getEnergyActionLook()));
		r.setPropertyValue("energyActionMove",new StringBuilder("" + getEnergyActionMove()));
		r.setPropertyValue("energyActionTurn",new StringBuilder("" + getEnergyActionTurn()));
		r.setPropertyValue("energyActionBite",new StringBuilder("" + getEnergyActionBite()));
		r.setPropertyValue("maxEnergyPlant",new StringBuilder("" + getMaxEnergyPlant()));
		r.setPropertyValue("maxEnergyHerbivore",new StringBuilder("" + getMaxEnergyHerbivore()));
		r.setPropertyValue("maxEnergyHerbivoreBite",new StringBuilder("" + getMaxEnergyHerbivoreBite()));
		r.setPropertyValue("maxEnergyCarnivore",new StringBuilder("" + getMaxEnergyCarnivore()));
		r.setPropertyValue("maxEnergyCarnivoreBite",new StringBuilder("" + getMaxEnergyCarnivoreBite()));
		r.setPropertyValue("statesPerSecond",new StringBuilder("" + getStatesPerSecond()));
		r.setPropertyValue("keepStateHistorySeconds",new StringBuilder("" + getKeepStateHistorySeconds()));
		r.setPropertyValue("deathDurationSeconds",new StringBuilder("" + getDeathDurationSeconds()));
		return r;
	}

	/**
	 * @return the statesPerSecond
	 */
	public int getStatesPerSecond() {
		return statesPerSecond;
	}

	/**
	 * @param statesPerSecond the statesPerSecond to set
	 */
	public void setStatesPerSecond(int statesPerSecond) {
		this.statesPerSecond = statesPerSecond;
	}

	/**
	 * @return the keepStateHistorySeconds
	 */
	public int getKeepStateHistorySeconds() {
		return keepStateHistorySeconds;
	}

	/**
	 * @param keepStateHistorySeconds the keepStateHistorySeconds to set
	 */
	public void setKeepStateHistorySeconds(int keepStateHistorySeconds) {
		this.keepStateHistorySeconds = keepStateHistorySeconds;
	}

	/**
	 * @return the herbivores
	 */
	public int getHerbivores() {
		return herbivores;
	}

	/**
	 * @param herbivores the herbivores to set
	 */
	public void setHerbivores(int herbivores) {
		this.herbivores = herbivores;
	}

	/**
	 * @return the carnivores
	 */
	public int getCarnivores() {
		return carnivores;
	}

	/**
	 * @param carnivores the carnivores to set
	 */
	public void setCarnivores(int carnivores) {
		this.carnivores = carnivores;
	}

	/**
	 * @return the energyInputPerSecond
	 */
	public int getEnergyInputPerSecond() {
		return energyInputPerSecond;
	}

	/**
	 * @param energyInputPerSecond the energyInputPerSecond to set
	 */
	public void setEnergyInputPerSecond(int energyInputPerSecond) {
		this.energyInputPerSecond = energyInputPerSecond;
	}

	/**
	 * @return the maxEnergyPlant
	 */
	public int getMaxEnergyPlant() {
		return maxEnergyPlant;
	}

	/**
	 * @param maxEnergyPlant the maxEnergyPlant to set
	 */
	public void setMaxEnergyPlant(int maxEnergyPlant) {
		this.maxEnergyPlant = maxEnergyPlant;
	}

	/**
	 * @return the maxEnergyHerbivore
	 */
	public int getMaxEnergyHerbivore() {
		return maxEnergyHerbivore;
	}

	/**
	 * @param maxEnergyHerbivore the maxEnergyHerbivore to set
	 */
	public void setMaxEnergyHerbivore(int maxEnergyHerbivore) {
		this.maxEnergyHerbivore = maxEnergyHerbivore;
	}

	/**
	 * @return the maxEnergyCarnivore
	 */
	public int getMaxEnergyCarnivore() {
		return maxEnergyCarnivore;
	}

	/**
	 * @param maxEnergyCarnivore the maxEnergyCarnivore to set
	 */
	public void setMaxEnergyCarnivore(int maxEnergyCarnivore) {
		this.maxEnergyCarnivore = maxEnergyCarnivore;
	}

	/**
	 * @return the maxEnergyHerbivoreBite
	 */
	public int getMaxEnergyHerbivoreBite() {
		return maxEnergyHerbivoreBite;
	}

	/**
	 * @param maxEnergyHerbivoreBite the maxEnergyHerbivoreBite to set
	 */
	public void setMaxEnergyHerbivoreBite(int maxEnergyHerbivoreBite) {
		this.maxEnergyHerbivoreBite = maxEnergyHerbivoreBite;
	}

	/**
	 * @return the maxEnergyCarnivoreBite
	 */
	public int getMaxEnergyCarnivoreBite() {
		return maxEnergyCarnivoreBite;
	}

	/**
	 * @param maxEnergyCarnivoreBite the maxEnergyCarnivoreBite to set
	 */
	public void setMaxEnergyCarnivoreBite(int maxEnergyCarnivoreBite) {
		this.maxEnergyCarnivoreBite = maxEnergyCarnivoreBite;
	}

	/**
	 * @return the classNameHerbivore
	 */
	public String getClassNameHerbivore() {
		return classNameHerbivore;
	}

	/**
	 * @param classNameHerbivore the classNameHerbivore to set
	 */
	public void setClassNameHerbivore(String classNameHerbivore) {
		this.classNameHerbivore = classNameHerbivore;
	}

	/**
	 * @return the classNameCarnivore
	 */
	public String getClassNameCarnivore() {
		return classNameCarnivore;
	}

	/**
	 * @param classNameCarnivore the classNameCarnivore to set
	 */
	public void setClassNameCarnivore(String classNameCarnivore) {
		this.classNameCarnivore = classNameCarnivore;
	}

	/**
	 * @return the bufferSize
	 */
	public int getBufferSize() {
		return bufferSize;
	}

	/**
	 * @param bufferSize the bufferSize to set
	 */
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * @return the energyActionLook
	 */
	public int getEnergyActionLook() {
		return energyActionLook;
	}

	/**
	 * @param energyActionLook the energyActionLook to set
	 */
	public void setEnergyActionLook(int energyActionLook) {
		this.energyActionLook = energyActionLook;
	}

	/**
	 * @return the energyActionMove
	 */
	public int getEnergyActionMove() {
		return energyActionMove;
	}

	/**
	 * @param energyActionMove the energyActionMove to set
	 */
	public void setEnergyActionMove(int energyActionMove) {
		this.energyActionMove = energyActionMove;
	}

	/**
	 * @return the energyActionTurn
	 */
	public int getEnergyActionTurn() {
		return energyActionTurn;
	}

	/**
	 * @param energyActionTurn the energyActionTurn to set
	 */
	public void setEnergyActionTurn(int energyActionTurn) {
		this.energyActionTurn = energyActionTurn;
	}

	/**
	 * @return the energyActionBite
	 */
	public int getEnergyActionBite() {
		return energyActionBite;
	}

	/**
	 * @param energyActionBite the energyActionBite to set
	 */
	public void setEnergyActionBite(int energyActionBite) {
		this.energyActionBite = energyActionBite;
	}

	/**
	 * @return the deathDurationSeconds
	 */
	public int getDeathDurationSeconds() {
		return deathDurationSeconds;
	}

	/**
	 * @param deathDurationSeconds the deathDurationSeconds to set
	 */
	public void setDeathDurationSeconds(int deathDurationSeconds) {
		this.deathDurationSeconds = deathDurationSeconds;
	}
}
