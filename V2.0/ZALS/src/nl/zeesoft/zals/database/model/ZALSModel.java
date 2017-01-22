package nl.zeesoft.zals.database.model;

import java.math.BigDecimal;

import nl.zeesoft.zac.database.model.ZACModel;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlLink;
import nl.zeesoft.zodb.database.model.MdlNumber;
import nl.zeesoft.zodb.database.model.MdlPackage;
import nl.zeesoft.zodb.database.model.MdlString;

public class ZALSModel extends ZACModel {
	public static final String		ENVIRONMENT_CLASS_NAME						= "Environment";
	public static final String		ENVIRONMENT_OBJECT_CLASS_NAME				= "EnvironmentObject";
	public static final String		ORGANISM_CLASS_NAME							= "Organism";
	public static final String		PLANT_CLASS_NAME							= "Plant";
	public static final String		ANIMAL_CLASS_NAME							= "Animal";
	public static final String		HERBIVORE_CLASS_NAME						= "Herbivore";
	public static final String		CARNIVORE_CLASS_NAME						= "Carnivore";

	public static final String		STATE_HISTORY_CLASS_NAME					= "StateHistory";

	public static final String		ENVIRONMENT_CLASS_FULL_NAME					= ZALSModel.class.getPackage().getName() + "." + ENVIRONMENT_CLASS_NAME;
	public static final String		ENVIRONMENT_OBJECT_CLASS_FULL_NAME			= ZALSModel.class.getPackage().getName() + "." + ENVIRONMENT_OBJECT_CLASS_NAME;
	public static final String		ORGANISM_CLASS_FULL_NAME					= ZALSModel.class.getPackage().getName() + "." + ORGANISM_CLASS_NAME;
	public static final String		PLANT_CLASS_FULL_NAME						= ZALSModel.class.getPackage().getName() + "." + PLANT_CLASS_NAME;
	public static final String		ANIMAL_CLASS_FULL_NAME						= ZALSModel.class.getPackage().getName() + "." + ANIMAL_CLASS_NAME;
	public static final String		HERBIVORE_CLASS_FULL_NAME					= ZALSModel.class.getPackage().getName() + "." + HERBIVORE_CLASS_NAME;
	public static final String		CARNIVORE_CLASS_FULL_NAME					= ZALSModel.class.getPackage().getName() + "." + CARNIVORE_CLASS_NAME;

	public static final String		STATE_HISTORY_CLASS_FULL_NAME				= ZALSModel.class.getPackage().getName() + "." + STATE_HISTORY_CLASS_NAME;
	
	@Override
	public void initializeCustomizations() {
		super.initializeCustomizations();

		MdlPackage pkg = getNewPackage();
		pkg.setName(ZALSModel.class.getPackage().getName());
		
		// ENVIRONMENT
		MdlClass cls = pkg.getNewClass();
		cls.setName(ENVIRONMENT_CLASS_NAME);

		MdlString str = cls.getNewString();
		str.setName("classNameHerbivore");
		str.setMaxLength(96);

		str = cls.getNewString();
		str.setName("classNameCarnivore");
		str.setMaxLength(96);
		
		MdlNumber nmbr = cls.getNewNumber();
		nmbr.setName("bufferSize");
		nmbr.setMinValue(new BigDecimal("2"));
		nmbr.setMaxValue(new BigDecimal("16"));

		nmbr = cls.getNewNumber();
		nmbr.setName("herbivores");
		nmbr.setMinValue(new BigDecimal("2"));
		nmbr.setMaxValue(new BigDecimal("16"));

		nmbr = cls.getNewNumber();
		nmbr.setName("carnivores");
		nmbr.setMinValue(new BigDecimal("2"));
		nmbr.setMaxValue(new BigDecimal("16"));
		
		nmbr = cls.getNewNumber();
		nmbr.setName("deathDurationSeconds");
		nmbr.setMinValue(new BigDecimal("1"));
		nmbr.setMaxValue(new BigDecimal("10"));

		nmbr = cls.getNewNumber();
		nmbr.setName("energyInputPerSecond");
		nmbr.setMinValue(new BigDecimal("10"));
		nmbr.setMaxValue(new BigDecimal("99999999"));

		nmbr = cls.getNewNumber();
		nmbr.setName("energyActionLook");
		nmbr.setMinValue(new BigDecimal("10"));
		nmbr.setMaxValue(new BigDecimal("99999"));

		nmbr = cls.getNewNumber();
		nmbr.setName("energyActionMove");
		nmbr.setMinValue(new BigDecimal("10"));
		nmbr.setMaxValue(new BigDecimal("99999"));

		nmbr = cls.getNewNumber();
		nmbr.setName("energyActionTurn");
		nmbr.setMinValue(new BigDecimal("10"));
		nmbr.setMaxValue(new BigDecimal("99999"));

		nmbr = cls.getNewNumber();
		nmbr.setName("energyActionBite");
		nmbr.setMinValue(new BigDecimal("10"));
		nmbr.setMaxValue(new BigDecimal("99999"));

		nmbr = cls.getNewNumber();
		nmbr.setName("maxEnergyPlant");
		nmbr.setMinValue(new BigDecimal("100"));
		nmbr.setMaxValue(new BigDecimal("9999999"));

		nmbr = cls.getNewNumber();
		nmbr.setName("maxEnergyHerbivore");
		nmbr.setMinValue(new BigDecimal("1000"));
		nmbr.setMaxValue(new BigDecimal("99999999"));

		nmbr = cls.getNewNumber();
		nmbr.setName("maxEnergyHerbivoreBite");
		nmbr.setMinValue(new BigDecimal("100"));
		nmbr.setMaxValue(new BigDecimal("99999999"));

		nmbr = cls.getNewNumber();
		nmbr.setName("maxEnergyCarnivore");
		nmbr.setMinValue(new BigDecimal("1000"));
		nmbr.setMaxValue(new BigDecimal("99999999"));

		nmbr = cls.getNewNumber();
		nmbr.setName("maxEnergyCarnivoreBite");
		nmbr.setMinValue(new BigDecimal("100"));
		nmbr.setMaxValue(new BigDecimal("99999999"));

		nmbr = cls.getNewNumber();
		nmbr.setName("statesPerSecond");
		nmbr.setMinValue(new BigDecimal("4"));
		nmbr.setMaxValue(new BigDecimal("50"));

		nmbr = cls.getNewNumber();
		nmbr.setName("keepStateHistorySeconds");
		nmbr.setMinValue(new BigDecimal("6"));
		nmbr.setMaxValue(new BigDecimal("30"));

		// ENVIRONMENT OBJECT
		cls = pkg.getNewClass();
		cls.setName(ENVIRONMENT_OBJECT_CLASS_NAME);
		cls.setAbstr(true);
		
		MdlLink lnk = cls.getNewLink();
		lnk.setName("environment");
		lnk.setClassTo(ENVIRONMENT_CLASS_FULL_NAME);

		nmbr = cls.getNewNumber();
		nmbr.setName("posX");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("" + (Environment.SIZE_X - 1)));

		nmbr = cls.getNewNumber();
		nmbr.setName("posY");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("" + (Environment.SIZE_Y - 1)));

		// ORGANISM
		cls = pkg.getNewClass();
		cls.setName(ORGANISM_CLASS_NAME);
		cls.getExtendsClasses().add(ENVIRONMENT_OBJECT_CLASS_FULL_NAME);
		cls.setAbstr(true);

		nmbr = cls.getNewNumber();
		nmbr.setName("energy");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("99999999"));

		nmbr = cls.getNewNumber();
		nmbr.setName("dateTimeDied");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal(Long.MAX_VALUE));
		
		// PLANT
		cls = pkg.getNewClass();
		cls.setName(PLANT_CLASS_NAME);
		cls.getExtendsClasses().add(ORGANISM_CLASS_FULL_NAME);

		// ANIMAL
		cls = pkg.getNewClass();
		cls.setName(ANIMAL_CLASS_NAME);
		cls.setAbstr(true);
		cls.getExtendsClasses().add(ORGANISM_CLASS_FULL_NAME);

		nmbr = cls.getNewNumber();
		nmbr.setName("rotation");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("359"));

		nmbr = cls.getNewNumber();
		nmbr.setName("score");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal(Integer.MAX_VALUE));

		nmbr = cls.getNewNumber();
		nmbr.setName("topScore");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal(Integer.MAX_VALUE));

		nmbr = cls.getNewNumber();
		nmbr.setName("trainedStates");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal(Integer.MAX_VALUE));

		nmbr = cls.getNewNumber();
		nmbr.setName("generation");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal(Integer.MAX_VALUE));

		str = cls.getNewString();
		str.setName("trainingModules");
		str.setMaxLength(2048);
		
		str = cls.getNewString();
		str.setName("confabulatingModules");
		str.setMaxLength(2048);
		
		// HERBIVORE
		cls = pkg.getNewClass();
		cls.setName(HERBIVORE_CLASS_NAME);
		cls.getExtendsClasses().add(ANIMAL_CLASS_FULL_NAME);

		// CARNIVORE
		cls = pkg.getNewClass();
		cls.setName(CARNIVORE_CLASS_NAME);
		cls.getExtendsClasses().add(ANIMAL_CLASS_FULL_NAME);

		// STATE HISTORY
		cls = pkg.getNewClass();
		cls.setName(STATE_HISTORY_CLASS_NAME);
		
		lnk = cls.getNewLink();
		lnk.setName("environment");
		lnk.setClassTo(ENVIRONMENT_CLASS_FULL_NAME);

		nmbr = cls.getNewNumber();
		nmbr.setName("dateTime");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal(Long.MAX_VALUE));
		nmbr.setIndex(true);
		
		str = cls.getNewString();
		str.setName("objectData");
		str.setMaxLength(999999);
	}
}
