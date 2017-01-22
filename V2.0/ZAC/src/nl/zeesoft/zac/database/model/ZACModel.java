package nl.zeesoft.zac.database.model;

import java.math.BigDecimal;

import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlLink;
import nl.zeesoft.zodb.database.model.MdlModel;
import nl.zeesoft.zodb.database.model.MdlNumber;
import nl.zeesoft.zodb.database.model.MdlPackage;
import nl.zeesoft.zodb.database.model.MdlString;
import nl.zeesoft.zodb.database.model.MdlUniqueConstraint;

public class ZACModel extends MdlModel {
	public static final String		MODULE_CLASS_NAME							= "Module";
	public static final String		SYMBOL_SEQUENCE_CLASS_NAME					= "SymbolSequence";
	public static final String		SYMBOL_SEQUENCE_TRAINING_CLASS_NAME			= "SymbolSequenceTraining";
	public static final String		SYMBOL_SEQUENCE_TEST_CLASS_NAME				= "SymbolSequenceTest";
	public static final String		SYMBOL_LINK_CLASS_NAME						= "SymbolLink";
	public static final String		SYMBOL_LINK_CONTEXT_CLASS_NAME				= "SymbolLinkContext";
	public static final String		SYMBOL_LINK_SEQUENCE_CLASS_NAME				= "SymbolLinkSequence";

	public static final String		MODULE_CLASS_FULL_NAME						= ZACModel.class.getPackage().getName() + "." + MODULE_CLASS_NAME;
	public static final String		SYMBOL_SEQUENCE_CLASS_FULL_NAME				= ZACModel.class.getPackage().getName() + "." + SYMBOL_SEQUENCE_CLASS_NAME;
	public static final String		SYMBOL_SEQUENCE_TRAINING_CLASS_FULL_NAME	= ZACModel.class.getPackage().getName() + "." + SYMBOL_SEQUENCE_TRAINING_CLASS_NAME;
	public static final String		SYMBOL_SEQUENCE_TEST_CLASS_FULL_NAME		= ZACModel.class.getPackage().getName() + "." + SYMBOL_SEQUENCE_TEST_CLASS_NAME;
	public static final String		SYMBOL_LINK_CLASS_FULL_NAME					= ZACModel.class.getPackage().getName() + "." + SYMBOL_LINK_CLASS_NAME;
	public static final String		SYMBOL_LINK_CONTEXT_CLASS_FULL_NAME			= ZACModel.class.getPackage().getName() + "." + SYMBOL_LINK_CONTEXT_CLASS_NAME;
	public static final String		SYMBOL_LINK_SEQUENCE_CLASS_FULL_NAME		= ZACModel.class.getPackage().getName() + "." + SYMBOL_LINK_SEQUENCE_CLASS_NAME;
	
	@Override
	public void initializeCustomizations() {
		MdlPackage pkg = getNewPackage();
		pkg.setName(ZACModel.class.getPackage().getName());
		
		// MODULE
		MdlClass cls = pkg.getNewClass();
		cls.setName(MODULE_CLASS_NAME);

		MdlString str = cls.getNewString();
		str.setName("name");
		str.setMaxLength(64);
		str.setIndex(true);
		
		MdlNumber nmbr = cls.getNewNumber();
		nmbr.setName("maxSequenceCount");
		nmbr.setMinValue(new BigDecimal("100"));
		nmbr.setMaxValue(new BigDecimal("999999"));

		nmbr = cls.getNewNumber();
		nmbr.setName("maxSequenceDistance");
		nmbr.setMinValue(new BigDecimal("2"));
		nmbr.setMaxValue(new BigDecimal("99"));

		nmbr = cls.getNewNumber();
		nmbr.setName("maxContextCount");
		nmbr.setMinValue(new BigDecimal("100"));
		nmbr.setMaxValue(new BigDecimal("999999"));

		str = cls.getNewString();
		str.setName("active");
		str.setMaxLength(5);

		str = cls.getNewString();
		str.setName("removeMe");
		str.setMaxLength(5);

		// SYMBOL SEQUENCE
		cls = pkg.getNewClass();
		cls.setName(SYMBOL_SEQUENCE_CLASS_NAME);
		cls.setAbstr(true);

		MdlLink link = cls.getNewLink();
		link.setName("module");
		link.setClassTo(MODULE_CLASS_FULL_NAME);

		str = cls.getNewString();
		str.setName("sequence");
		str.setMaxLength(4096);

		str = cls.getNewString();
		str.setName("contextSymbol1");
		str.setMaxLength(64);
		str = cls.getNewString();
		str.setName("contextSymbol2");
		str.setMaxLength(64);
		str = cls.getNewString();
		str.setName("contextSymbol3");
		str.setMaxLength(64);
		str = cls.getNewString();
		str.setName("contextSymbol4");
		str.setMaxLength(64);
		str = cls.getNewString();
		str.setName("contextSymbol5");
		str.setMaxLength(64);
		str = cls.getNewString();
		str.setName("contextSymbol6");
		str.setMaxLength(64);
		str = cls.getNewString();
		str.setName("contextSymbol7");
		str.setMaxLength(64);
		str = cls.getNewString();
		str.setName("contextSymbol8");
		str.setMaxLength(64);

		// SYMBOL SEQUENCE TRAINING
		cls = pkg.getNewClass();
		cls.setName(SYMBOL_SEQUENCE_TRAINING_CLASS_NAME);
		cls.getExtendsClasses().add(SYMBOL_SEQUENCE_CLASS_FULL_NAME);
		
		// SYMBOL SEQUENCE TEST
		cls = pkg.getNewClass();
		cls.setName(SYMBOL_SEQUENCE_TEST_CLASS_NAME);
		cls.getExtendsClasses().add(SYMBOL_SEQUENCE_CLASS_FULL_NAME);

		str = cls.getNewString();
		str.setName("type");
		str.setMaxLength(32);

		nmbr = cls.getNewNumber();
		nmbr.setName("thinkWidth");
		nmbr.setMinValue(new BigDecimal("10"));
		nmbr.setMaxValue(new BigDecimal("9999"));

		nmbr = cls.getNewNumber();
		nmbr.setName("maxOutputSymbols");
		nmbr.setMinValue(new BigDecimal("1"));
		nmbr.setMaxValue(new BigDecimal("999"));

		nmbr = cls.getNewNumber();
		nmbr.setName("firedLinks");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal(Long.MAX_VALUE));
		
		str = cls.getNewString();
		str.setName("log");
		str.setMaxLength(999999);

		str = cls.getNewString();
		str.setName("output");
		str.setMaxLength(4096);
		
		// SYMBOL LINK
		cls = pkg.getNewClass();
		cls.setAbstr(true);
		cls.setName(SYMBOL_LINK_CLASS_NAME);

		link = cls.getNewLink();
		link.setName("module");
		link.setClassTo(MODULE_CLASS_FULL_NAME);

		str = cls.getNewString();
		str.setName("symbolFrom");
		str.setMaxLength(64);
		str.setIndex(true);

		str = cls.getNewString();
		str.setName("symbolTo");
		str.setMaxLength(64);
		str.setIndex(true);
		
		nmbr = cls.getNewNumber();
		nmbr.setName("count");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal(Long.MAX_VALUE));

		// SYMBOL LINK CONTEXT
		cls = pkg.getNewClass();
		cls.setName(SYMBOL_LINK_CONTEXT_CLASS_NAME);
		cls.getExtendsClasses().add(SYMBOL_LINK_CLASS_FULL_NAME);
		
		// SYMBOL LINK SEQUENCE
		cls = pkg.getNewClass();
		cls.setName(SYMBOL_LINK_SEQUENCE_CLASS_NAME);
		cls.getExtendsClasses().add(SYMBOL_LINK_CLASS_FULL_NAME);
		
		nmbr = cls.getNewNumber();
		nmbr.setName("distance");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal(Long.MAX_VALUE));
		
		// Unique constraints
		MdlUniqueConstraint uc = getNewUniqueConstraint();
		uc.getClasses().add(MODULE_CLASS_FULL_NAME);
		uc.getProperties().add("name");

		uc = getNewUniqueConstraint();
		uc.getClasses().add(SYMBOL_SEQUENCE_TRAINING_CLASS_FULL_NAME);
		uc.getProperties().add("module");
		uc.getProperties().add("sequence");
		
		uc = getNewUniqueConstraint();
		uc.getClasses().add(SYMBOL_LINK_CONTEXT_CLASS_FULL_NAME);
		uc.getProperties().add("module");
		uc.getProperties().add("symbolFrom");
		uc.getProperties().add("symbolTo");

		uc = getNewUniqueConstraint();
		uc.getClasses().add(SYMBOL_LINK_SEQUENCE_CLASS_FULL_NAME);
		uc.getProperties().add("module");
		uc.getProperties().add("symbolFrom");
		uc.getProperties().add("symbolTo");
		uc.getProperties().add("distance");
	}
}
