package nl.zeesoft.zids.database.model;

import java.math.BigDecimal;

import nl.zeesoft.zacs.database.model.ZACSModel;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlLink;
import nl.zeesoft.zodb.database.model.MdlNumber;
import nl.zeesoft.zodb.database.model.MdlPackage;
import nl.zeesoft.zodb.database.model.MdlString;
import nl.zeesoft.zodb.database.model.MdlUniqueConstraint;

public class ZIDSModel extends ZACSModel {
	public final static String	VARIABLE_TYPE_CLASS_NAME					= "VariableType";
	public final static String	DIALOG_CLASS_NAME 							= "Dialog";
	public final static String	DIALOG_EXAMPLE_CLASS_NAME					= "DialogExample";
	public final static String	DIALOG_VARIABLE_CLASS_NAME			 		= "DialogVariable";
	public final static String	DIALOG_VARIABLE_EXAMPLE_CLASS_NAME			= "DialogVariableExample";
	public final static String	SESSION_CLASS_NAME 							= "Session";
	public final static String	SESSION_DIALOG_VARIABLE_CLASS_NAME			= "SessionDialogVariable";
	public final static String	SESSION_VARIABLE_CLASS_NAME					= "SessionVariable";

	public final static String	COGNITIVE_ENTITY_CLASS_NAME					= "CognitiveEntity";
	public final static String	SELF_CLASS_NAME 							= "Self";
	public final static String	HUMAN_CLASS_NAME 							= "Human";

	public final static String	VARIABLE_TYPE_CLASS_FULL_NAME				= ZIDSModel.class.getPackage().getName() + "." + VARIABLE_TYPE_CLASS_NAME;
	public final static String	DIALOG_CLASS_FULL_NAME 						= ZIDSModel.class.getPackage().getName() + "." + DIALOG_CLASS_NAME;
	public final static String	DIALOG_EXAMPLE_CLASS_FULL_NAME				= ZIDSModel.class.getPackage().getName() + "." + DIALOG_EXAMPLE_CLASS_NAME;
	public final static String	DIALOG_VARIABLE_CLASS_FULL_NAME 			= ZIDSModel.class.getPackage().getName() + "." + DIALOG_VARIABLE_CLASS_NAME;
	public final static String	DIALOG_VARIABLE_EXAMPLE_CLASS_FULL_NAME 	= ZIDSModel.class.getPackage().getName() + "." + DIALOG_VARIABLE_EXAMPLE_CLASS_NAME;
	public final static String	SESSION_CLASS_FULL_NAME 					= ZIDSModel.class.getPackage().getName() + "." + SESSION_CLASS_NAME;
	public final static String	SESSION_DIALOG_VARIABLE_CLASS_FULL_NAME 	= ZIDSModel.class.getPackage().getName() + "." + SESSION_DIALOG_VARIABLE_CLASS_NAME;
	public final static String	SESSION_VARIABLE_CLASS_FULL_NAME 			= ZIDSModel.class.getPackage().getName() + "." + SESSION_VARIABLE_CLASS_NAME;

	public final static String	COGNITIVE_ENTITY_CLASS_FULL_NAME			= ZIDSModel.class.getPackage().getName() + "." + COGNITIVE_ENTITY_CLASS_NAME;
	public final static String	SELF_CLASS_FULL_NAME						= ZIDSModel.class.getPackage().getName() + "." + SELF_CLASS_NAME;
	public final static String	HUMAN_CLASS_FULL_NAME						= ZIDSModel.class.getPackage().getName() + "." + HUMAN_CLASS_NAME;
	
	public final static String	SESSION_HANDLING_ASSIGNMENT_NAME			= "Session handler";
	public final static int		MAXIMUM_HUMAN_NAME_SYMBOLS					= 4;
	
	@Override
	public void initializeCustomizations() {
		super.initializeCustomizations();
		
		MdlPackage pkg = getNewPackage();
		pkg.setName(ZIDSModel.class.getPackage().getName());

		// VARIABLE TYPE
		MdlClass cls = pkg.getNewClass();
		cls.setName(VARIABLE_TYPE_CLASS_NAME);

		MdlString str = cls.getNewString();
		str.setName("name");
		str.setMaxLength(64);
		str.setIndex(true);

		// DIALOG
		cls = pkg.getNewClass();
		cls.setName(DIALOG_CLASS_NAME);

		str = cls.getNewString();
		str.setName("name");
		str.setMaxLength(64);
		str.setIndex(true);

		str = cls.getNewString();
		str.setName("contextSymbol");
		str.setMaxLength(96);

		// DIALOG EXAMPLE
 		cls = pkg.getNewClass();
		cls.setName(DIALOG_EXAMPLE_CLASS_NAME);

		MdlLink lnk = cls.getNewLink();
		lnk.setClassTo(DIALOG_CLASS_FULL_NAME);
		lnk.setName("dialog");
		lnk.setMaxSize(1);

		str = cls.getNewString();
		str.setName("input");
		str.setMaxLength(1024);

		str = cls.getNewString();
		str.setName("output");
		str.setMaxLength(4096);

		// DIALOG VARIABLE
 		cls = pkg.getNewClass();
		cls.setName(DIALOG_VARIABLE_CLASS_NAME);

		lnk = cls.getNewLink();
		lnk.setClassTo(DIALOG_CLASS_FULL_NAME);
		lnk.setName("dialog");
		lnk.setMaxSize(1);

		str = cls.getNewString();
		str.setName("code");
		str.setMaxLength(64);

		lnk = cls.getNewLink();
		lnk.setClassTo(VARIABLE_TYPE_CLASS_FULL_NAME);
		lnk.setName("type");
		lnk.setMaxSize(1);
		
		str = cls.getNewString();
		str.setName("contextSymbol");
		str.setMaxLength(96);
		
		str = cls.getNewString();
		str.setName("prompt");
		str.setMaxLength(2048);

		// DIALOG VARIABLE EXAMPLE
 		cls = pkg.getNewClass();
		cls.setName(DIALOG_VARIABLE_EXAMPLE_CLASS_NAME);

		lnk = cls.getNewLink();
		lnk.setClassTo(DIALOG_VARIABLE_CLASS_FULL_NAME);
		lnk.setName("variable");
		lnk.setMaxSize(1);

		str = cls.getNewString();
		str.setName("input");
		str.setMaxLength(1024);

		str = cls.getNewString();
		str.setName("output");
		str.setMaxLength(4096);

		// SESSION
 		cls = pkg.getNewClass();
		cls.setName(SESSION_CLASS_NAME);

		str = cls.getNewString();
		str.setName("sessionId");
		str.setMaxLength(64);

		MdlNumber num = cls.getNewNumber();
		num.setName("dateTimeStart");
		num.setMinValue(new BigDecimal("0"));
		num.setMaxValue(new BigDecimal("" + Long.MAX_VALUE));

		num = cls.getNewNumber();
		num.setName("dateTimeLastActivity");
		num.setMinValue(new BigDecimal("0"));
		num.setMaxValue(new BigDecimal("" + Long.MAX_VALUE));
		
		num = cls.getNewNumber();
		num.setName("dateTimeEnd");
		num.setMinValue(new BigDecimal("0"));
		num.setMaxValue(new BigDecimal("" + Long.MAX_VALUE));

		str = cls.getNewString();
		str.setName("context");
		str.setMaxLength(1024);
		
		lnk = cls.getNewLink();
		lnk.setClassTo(DIALOG_CLASS_FULL_NAME);
		lnk.setName("dialog");
		lnk.setMaxSize(1);

		str = cls.getNewString();
		str.setName("log");
		str.setMaxLength(999999);

		str = cls.getNewString();
		str.setName("logIncludingThoughts");
		str.setMaxLength(999999);

		str = cls.getNewString();
		str.setName("logAssignments");
		str.setMaxLength(9999999);
		
		str = cls.getNewString();
		str.setName("output");
		str.setMaxLength(4096);

		// SESSION DIALOG VARIABLE
 		cls = pkg.getNewClass();
		cls.setName(SESSION_DIALOG_VARIABLE_CLASS_NAME);
		
		lnk = cls.getNewLink();
		lnk.setClassTo(SESSION_CLASS_FULL_NAME);
		lnk.setName("session");
		lnk.setMaxSize(1);

		str = cls.getNewString();
		str.setName("code");
		str.setMaxLength(64);
		
		str = cls.getNewString();
		str.setName("value");
		str.setMaxLength(1024);

		// SESSION VARIABLE
 		cls = pkg.getNewClass();
		cls.setName(SESSION_VARIABLE_CLASS_NAME);
		cls.getExtendsClasses().add(SESSION_DIALOG_VARIABLE_CLASS_FULL_NAME);
				
		// COGNITIVE ENTITY
 		cls = pkg.getNewClass();
		cls.setName(COGNITIVE_ENTITY_CLASS_NAME);
		cls.setAbstr(true);

		str = cls.getNewString();
		str.setName("name");
		str.setMaxLength(96);
		str.setIndex(true);
		
		// SELF
 		cls = pkg.getNewClass();
		cls.setName(SELF_CLASS_NAME);
		cls.getExtendsClasses().add(COGNITIVE_ENTITY_CLASS_FULL_NAME);

		// HUMAN
 		cls = pkg.getNewClass();
		cls.setName(HUMAN_CLASS_NAME);
		cls.getExtendsClasses().add(COGNITIVE_ENTITY_CLASS_FULL_NAME);

		str = cls.getNewString();
		str.setName("emailAddress");
		str.setMaxLength(96);
		str.setIndex(true);
		
		// Unique constraints
		MdlUniqueConstraint uc = getNewUniqueConstraint();
		uc.getClasses().add(VARIABLE_TYPE_CLASS_FULL_NAME);
		uc.getProperties().add("name");

		uc = getNewUniqueConstraint();
		uc.getClasses().add(DIALOG_CLASS_FULL_NAME);
		uc.getProperties().add("name");

		uc = getNewUniqueConstraint();
		uc.getClasses().add(DIALOG_CLASS_FULL_NAME);
		uc.getProperties().add("contextSymbol");
		
		uc = getNewUniqueConstraint();
		uc.getClasses().add(DIALOG_EXAMPLE_CLASS_FULL_NAME);
		uc.getProperties().add("dialog");
		uc.getProperties().add("input");
		uc.getProperties().add("output");

		uc = getNewUniqueConstraint();
		uc.getClasses().add(DIALOG_VARIABLE_CLASS_FULL_NAME);
		uc.getProperties().add("dialog");
		uc.getProperties().add("code");

		uc = getNewUniqueConstraint();
		uc.getClasses().add(DIALOG_VARIABLE_CLASS_FULL_NAME);
		uc.getProperties().add("contextSymbol");

		uc = getNewUniqueConstraint();
		uc.getClasses().add(DIALOG_VARIABLE_EXAMPLE_CLASS_FULL_NAME);
		uc.getProperties().add("variable");
		uc.getProperties().add("input");
		uc.getProperties().add("output");
		
		uc = getNewUniqueConstraint();
		uc.getClasses().add(SESSION_CLASS_FULL_NAME);
		uc.getProperties().add("sessionId");

		uc = getNewUniqueConstraint();
		uc.getClasses().add(SESSION_DIALOG_VARIABLE_CLASS_FULL_NAME);
		uc.getProperties().add("session");
		uc.getProperties().add("code");

		uc = getNewUniqueConstraint();
		uc.getClasses().add(SESSION_VARIABLE_CLASS_FULL_NAME);
		uc.getProperties().add("session");
		uc.getProperties().add("code");

		uc = getNewUniqueConstraint();
		uc.getClasses().add(SELF_CLASS_FULL_NAME);
		uc.getClasses().add(HUMAN_CLASS_FULL_NAME);
		uc.getProperties().add("name");
	}
	
	public String getSelfFirstName() {
		return "Dyz";
	}

	public String getSelfLastName() {
		return "Lecticus";
	}

	// TODO: Create dialog for this
	public String getSelfIntroduction(String typeSpecifier) {
		String r = "I am an artificial cognitive entity. I was created by Andre van der Zee. I live in a database.";
		if (typeSpecifier.equals("NED")) {
			r = "Ik ben een kunstmatige cognitieve entitiet. I ben geschapen door Andre van der Zee. Ik leef in een databank.";
		}
		return r;
	}
}
