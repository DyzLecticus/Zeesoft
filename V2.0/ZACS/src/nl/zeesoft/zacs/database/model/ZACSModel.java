package nl.zeesoft.zacs.database.model;

import java.math.BigDecimal;

import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlLink;
import nl.zeesoft.zodb.database.model.MdlModel;
import nl.zeesoft.zodb.database.model.MdlNumber;
import nl.zeesoft.zodb.database.model.MdlPackage;
import nl.zeesoft.zodb.database.model.MdlString;
import nl.zeesoft.zodb.database.model.MdlUniqueConstraint;

public class ZACSModel extends MdlModel {	
	private static final int		NUMBER_OF_MODULES				= 8;

	public static final int			STATE_HISTORY_IMAGE_HEIGHT		= 100;
	public static final int			STATE_HISTORY_IMAGE_WIDTH		= 300;
	
	public static final String		COMMAND_CLASS_NAME				= "Command";
	public static final String		CONTROL_CLASS_NAME				= "Control";
	public static final String		STATE_HISTORY_CLASS_NAME		= "StateHistory";
	public static final String		SYMBOL_CLASS_NAME				= "Symbol";
	public static final String		SYMBOL_LINK_CLASS_NAME			= "SymbolLink";
	public static final String		MODULE_CLASS_NAME				= "Module";
	public static final String		MODULE_SYMBOL_CLASS_NAME		= "ModuleSymbol";
	public static final String		CONTEXT_LINK_CLASS_NAME			= "ContextLink";

	public static final String		EXAMPLE_CLASS_NAME				= "Example";
	public static final String		ASSIGNMENT_CLASS_NAME			= "Assignment";

	public static final String		CRAWLER_CLASS_NAME				= "Crawler";

	public static final String		COMMAND_CLASS_FULL_NAME			= ZACSModel.class.getPackage().getName() + "." + COMMAND_CLASS_NAME;
	public static final String		CONTROL_CLASS_FULL_NAME			= ZACSModel.class.getPackage().getName() + "." + CONTROL_CLASS_NAME;
	public static final String		STATE_HISTORY_CLASS_FULL_NAME	= ZACSModel.class.getPackage().getName() + "." + STATE_HISTORY_CLASS_NAME;
	public static final String		SYMBOL_CLASS_FULL_NAME			= ZACSModel.class.getPackage().getName() + "." + SYMBOL_CLASS_NAME;
	public static final String		SYMBOL_LINK_CLASS_FULL_NAME		= ZACSModel.class.getPackage().getName() + "." + SYMBOL_LINK_CLASS_NAME;
	public static final String		MODULE_CLASS_FULL_NAME			= ZACSModel.class.getPackage().getName() + "." + MODULE_CLASS_NAME;
	public static final String		MODULE_SYMBOL_CLASS_FULL_NAME	= ZACSModel.class.getPackage().getName() + "." + MODULE_SYMBOL_CLASS_NAME;
	public static final String		CONTEXT_LINK_CLASS_FULL_NAME	= ZACSModel.class.getPackage().getName() + "." + CONTEXT_LINK_CLASS_NAME;

	public static final String		EXAMPLE_CLASS_FULL_NAME			= ZACSModel.class.getPackage().getName() + "." + EXAMPLE_CLASS_NAME;
	public static final String		ASSIGNMENT_CLASS_FULL_NAME		= ZACSModel.class.getPackage().getName() + "." + ASSIGNMENT_CLASS_NAME;

	public static final String		CRAWLER_CLASS_FULL_NAME			= ZACSModel.class.getPackage().getName() + "." + CRAWLER_CLASS_NAME;

	public static int getNumberOfModules() {
		return NUMBER_OF_MODULES;
	}
	
	@Override
	public void initializeCustomizations() {
		MdlPackage pkg = getNewPackage();
		pkg.setName(ZACSModel.class.getPackage().getName());
		
		// COMMAND
		MdlClass cls = pkg.getNewClass();
		cls.setName(COMMAND_CLASS_NAME);

		MdlString str = cls.getNewString();
		str.setName("code");
		str.setMaxLength(24);
		str.setIndex(true);
		
		// CONTROL
		cls = pkg.getNewClass();
		cls.setName(CONTROL_CLASS_NAME);

		MdlLink lnk = cls.getNewLink();
		lnk.setName("command");
		lnk.setClassTo(COMMAND_CLASS_FULL_NAME);
		
		str = cls.getNewString();
		str.setName("status");
		str.setMaxLength(64);

		str = cls.getNewString();
		str.setName("autoActivate");
		str.setMaxLength(5);

		str = cls.getNewString();
		str.setName("autoReactivate");
		str.setMaxLength(5);

		MdlNumber nmbr = cls.getNewNumber();
		nmbr.setName("reactivateMinutes");
		nmbr.setMinValue(new BigDecimal("10"));
		nmbr.setMaxValue(new BigDecimal("720"));

		str = cls.getNewString();
		str.setName("saveModuleSymbolState");
		str.setMaxLength(5);
		
		nmbr = cls.getNewNumber();
		nmbr.setName("updateControlSeconds");
		nmbr.setMinValue(new BigDecimal("1"));
		nmbr.setMaxValue(new BigDecimal("600"));
		
		str = cls.getNewString();
		str.setName("learnExamples");
		str.setMaxLength(5);

		nmbr = cls.getNewNumber();
		nmbr.setName("examplePauzeMSecs");
		nmbr.setMinValue(new BigDecimal("1"));
		nmbr.setMaxValue(new BigDecimal("9999"));

		nmbr = cls.getNewNumber();
		nmbr.setName("countSymbolMaximum");
		nmbr.setMinValue(new BigDecimal("10"));
		nmbr.setMaxValue(new BigDecimal("9999"));

		nmbr = cls.getNewNumber();
		nmbr.setName("countContextMaximum");
		nmbr.setMinValue(new BigDecimal("10"));
		nmbr.setMaxValue(new BigDecimal("99999"));

		str = cls.getNewString();
		str.setName("skipCountMaxStructSyms");
		str.setMaxLength(5);

		str = cls.getNewString();
		str.setName("contextAssociate");
		str.setMaxLength(5);
		
		str = cls.getNewString();
		str.setName("doAssignments");
		str.setMaxLength(5);

		nmbr = cls.getNewNumber();
		nmbr.setName("assignmentPauzeMSecs");
		nmbr.setMinValue(new BigDecimal("1"));
		nmbr.setMaxValue(new BigDecimal("9999"));

		nmbr = cls.getNewNumber();
		nmbr.setName("assignmentReloadSecs");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("9999"));

		nmbr = cls.getNewNumber();
		nmbr.setName("levelMaximum");
		nmbr.setMinValue(new BigDecimal("10"));
		nmbr.setMaxValue(new BigDecimal("999999"));

		nmbr = cls.getNewNumber();
		nmbr.setName("levelFireBase");
		nmbr.setMinValue(new BigDecimal("10"));
		nmbr.setMaxValue(new BigDecimal("9999"));

		nmbr = cls.getNewNumber();
		nmbr.setName("contextSymbolMaximum");
		nmbr.setMinValue(new BigDecimal("2"));
		nmbr.setMaxValue(new BigDecimal("99"));
		
		nmbr = cls.getNewNumber();
		nmbr.setName("stateHistoryMaximum");
		nmbr.setMinValue(new BigDecimal("10"));
		nmbr.setMaxValue(new BigDecimal("10000"));

		nmbr = cls.getNewNumber();
		nmbr.setName("addHistorySeconds");
		nmbr.setMinValue(new BigDecimal("1"));
		nmbr.setMaxValue(new BigDecimal("600"));
		
		// STATE HISTORY
		cls = pkg.getNewClass();
		cls.setName(STATE_HISTORY_CLASS_NAME);

		lnk = cls.getNewLink();
		lnk.setName("control");
		lnk.setClassTo(CONTROL_CLASS_FULL_NAME);

		nmbr = cls.getNewNumber();
		nmbr.setName("dateTime");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setIndex(true);

		str = cls.getNewString();
		str.setName("status");
		str.setMaxLength(64);

		nmbr = cls.getNewNumber();
		nmbr.setName("statAvgSymLinkCount");
		nmbr.setMinValue(new BigDecimal("0"));

		nmbr = cls.getNewNumber();
		nmbr.setName("statAvgConLinkCount");
		nmbr.setMinValue(new BigDecimal("0"));
		
		nmbr = cls.getNewNumber();
		nmbr.setName("statTotalSymbolLevel");
		nmbr.setMinValue(new BigDecimal("0"));
		
		nmbr = cls.getNewNumber();
		nmbr.setName("statTotalSymbols");
		nmbr.setMinValue(new BigDecimal("0"));

		nmbr = cls.getNewNumber();
		nmbr.setName("statTotalSymLinks");
		nmbr.setMinValue(new BigDecimal("0"));

		nmbr = cls.getNewNumber();
		nmbr.setName("statTotalConLinks");
		nmbr.setMinValue(new BigDecimal("0"));
		
		// SYMBOL
		cls = pkg.getNewClass();
		cls.setName(SYMBOL_CLASS_NAME);

		str = cls.getNewString();
		str.setName("code");
		str.setMaxLength(64);

		str = cls.getNewString();
		str.setName("enabled");
		str.setMaxLength(5);
		
		nmbr = cls.getNewNumber();
		nmbr.setName("level");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("" + Long.MAX_VALUE));

		nmbr = cls.getNewNumber();
		nmbr.setName("totalSymLinksFrom");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("" + Long.MAX_VALUE));

		nmbr = cls.getNewNumber();
		nmbr.setName("totalSymLinksTo");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("" + Long.MAX_VALUE));

		nmbr = cls.getNewNumber();
		nmbr.setName("totalConLinksFrom");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("" + Long.MAX_VALUE));

		nmbr = cls.getNewNumber();
		nmbr.setName("totalConLinksTo");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("" + Long.MAX_VALUE));

		// SYMBOL LINK
		cls = pkg.getNewClass();
		cls.setName(SYMBOL_LINK_CLASS_NAME);

		lnk = cls.getNewLink();
		lnk.setName("symbolFrom");
		lnk.setClassTo(SYMBOL_CLASS_FULL_NAME);
		
		lnk = cls.getNewLink();
		lnk.setName("symbolTo");
		lnk.setClassTo(SYMBOL_CLASS_FULL_NAME);

		nmbr = cls.getNewNumber();
		nmbr.setName("distance");
		nmbr.setMinValue(new BigDecimal("1"));
		nmbr.setMaxValue(new BigDecimal("99"));
		
		nmbr = cls.getNewNumber();
		nmbr.setName("count");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("" + Long.MAX_VALUE));

		// MODULE
		cls = pkg.getNewClass();
		cls.setName(MODULE_CLASS_NAME);

		nmbr = cls.getNewNumber();
		nmbr.setName("num");
		nmbr.setMinValue(new BigDecimal("1"));
		nmbr.setMaxValue(new BigDecimal("99"));
		nmbr.setIndex(true);

		lnk = cls.getNewLink();
		lnk.setName("symbolInput");
		lnk.setClassTo(SYMBOL_CLASS_FULL_NAME);

		lnk = cls.getNewLink();
		lnk.setName("symbolOutput");
		lnk.setClassTo(SYMBOL_CLASS_FULL_NAME);

		// MODULE_SYMBOL
		cls = pkg.getNewClass();
		cls.setName(MODULE_SYMBOL_CLASS_NAME);

		lnk = cls.getNewLink();
		lnk.setName("module");
		lnk.setClassTo(MODULE_CLASS_FULL_NAME);

		lnk = cls.getNewLink();
		lnk.setName("symbol");
		lnk.setClassTo(SYMBOL_CLASS_FULL_NAME);
		
		nmbr = cls.getNewNumber();
		nmbr.setName("level");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("" + Long.MAX_VALUE));

		// CONTEXT LINK
		cls = pkg.getNewClass();
		cls.setName(CONTEXT_LINK_CLASS_NAME);

		lnk = cls.getNewLink();
		lnk.setName("symbolFrom");
		lnk.setClassTo(SYMBOL_CLASS_FULL_NAME);
		
		lnk = cls.getNewLink();
		lnk.setName("symbolTo");
		lnk.setClassTo(SYMBOL_CLASS_FULL_NAME);

		nmbr = cls.getNewNumber();
		nmbr.setName("count");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("" + Long.MAX_VALUE));
		
		// EXAMPLE
		cls = pkg.getNewClass();
		cls.setName(EXAMPLE_CLASS_NAME);

		str = cls.getNewString();
		str.setName("context");
		str.setMaxLength(1024);
		
		str = cls.getNewString();
		str.setName("input");
		str.setMaxLength(1024);
		
		str = cls.getNewString();
		str.setName("output");
		str.setMaxLength(4096);
		
		// ASSIGNMENT
		cls = pkg.getNewClass();
		cls.setName(ASSIGNMENT_CLASS_NAME);
		cls.getExtendsClasses().add(EXAMPLE_CLASS_FULL_NAME);

		str = cls.getNewString();
		str.setName("name");
		str.setMaxLength(32);
		
		nmbr = cls.getNewNumber();
		nmbr.setName("maxSymbols");
		nmbr.setMinValue(new BigDecimal("10"));
		nmbr.setMaxValue(new BigDecimal("256"));

		str = cls.getNewString();
		str.setName("stopOnLineEndSymbol");
		str.setMaxLength(5);

		nmbr = cls.getNewNumber();
		nmbr.setName("thinkWidth");
		nmbr.setMinValue(new BigDecimal("10"));
		nmbr.setMaxValue(new BigDecimal("9999"));
		
		str = cls.getNewString();
		str.setName("thinkFast");
		str.setMaxLength(5);

		str = cls.getNewString();
		str.setName("logExtended");
		str.setMaxLength(5);

		str = cls.getNewString();
		str.setName("contextDynamic");
		str.setMaxLength(5);

		str = cls.getNewString();
		str.setName("correctInput");
		str.setMaxLength(5);
		
		str = cls.getNewString();
		str.setName("correctLineEnd");
		str.setMaxLength(5);
		
		str = cls.getNewString();
		str.setName("correctInputOnly");
		str.setMaxLength(5);
		
		lnk = cls.getNewLink();
		lnk.setName("workingModule");
		lnk.setClassTo(MODULE_CLASS_FULL_NAME);

		str = cls.getNewString();
		str.setName("workingContext");
		str.setMaxLength(1024);

		str = cls.getNewString();
		str.setName("workingOutput");
		str.setMaxLength(4096);
		
		str = cls.getNewString();
		str.setName("log");
		str.setMaxLength(999999);

		nmbr = cls.getNewNumber();
		nmbr.setName("firedSymbolLinks");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("" + Long.MAX_VALUE));

		nmbr = cls.getNewNumber();
		nmbr.setName("firedContextLinks");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("" + Long.MAX_VALUE));

		nmbr = cls.getNewNumber();
		nmbr.setName("dateTimeFinished");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("" + Long.MAX_VALUE));

		nmbr = cls.getNewNumber();
		nmbr.setName("durationMilliseconds");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("" + Long.MAX_VALUE));

		nmbr = cls.getNewNumber();
		nmbr.setName("numberOfSymbols");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("" + Long.MAX_VALUE));

		str = cls.getNewString();
		str.setName("inputContext");
		str.setMaxLength(1024);

		str = cls.getNewString();
		str.setName("outputContext");
		str.setMaxLength(1024);

		str = cls.getNewString();
		str.setName("correctedInput");
		str.setMaxLength(1024);

		str = cls.getNewString();
		str.setName("correctedInputSymbols");
		str.setMaxLength(1024);

		nmbr = cls.getNewNumber();
		nmbr.setName("dateTimePrevOutput1");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("" + Long.MAX_VALUE));

		str = cls.getNewString();
		str.setName("prevOutput1");
		str.setMaxLength(4096);
		
		nmbr = cls.getNewNumber();
		nmbr.setName("dateTimePrevOutput2");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("" + Long.MAX_VALUE));

		str = cls.getNewString();
		str.setName("prevOutput2");
		str.setMaxLength(4096);

		nmbr = cls.getNewNumber();
		nmbr.setName("dateTimePrevOutput3");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("" + Long.MAX_VALUE));
		
		str = cls.getNewString();
		str.setName("prevOutput3");
		str.setMaxLength(4096);

		// CRAWLER
		cls = pkg.getNewClass();
		cls.setName(CRAWLER_CLASS_NAME);

		str = cls.getNewString();
		str.setName("crawl");
		str.setMaxLength(5);

		str = cls.getNewString();
		str.setName("crawlUrl");
		str.setMaxLength(100);

		str = cls.getNewString();
		str.setName("respectRobotSpec");
		str.setMaxLength(5);

		nmbr = cls.getNewNumber();
		nmbr.setName("maxCrawlUrls");
		nmbr.setMinValue(new BigDecimal("10"));
		nmbr.setMaxValue(new BigDecimal("9999"));
		
		nmbr = cls.getNewNumber();
		nmbr.setName("minTextLength");
		nmbr.setMinValue(new BigDecimal("1"));
		nmbr.setMaxValue(new BigDecimal("9999"));

		str = cls.getNewString();
		str.setName("parseLists");
		str.setMaxLength(5);
		
		str = cls.getNewString();
		str.setName("convertTextToExamples");
		str.setMaxLength(5);
		
		nmbr = cls.getNewNumber();
		nmbr.setName("convertMaxExamples");
		nmbr.setMinValue(new BigDecimal("10"));
		nmbr.setMaxValue(new BigDecimal("99999"));

		nmbr = cls.getNewNumber();
		nmbr.setName("convertSentenceMinLength");
		nmbr.setMinValue(new BigDecimal("1"));
		nmbr.setMaxValue(new BigDecimal("999"));
		
		str = cls.getNewString();
		str.setName("convertContext");
		str.setMaxLength(1024);
		
		str = cls.getNewString();
		str.setName("convertContextAssociate");
		str.setMaxLength(5);
		
		str = cls.getNewString();
		str.setName("crawledUrls");
		str.setMaxLength(999999);
		
		str = cls.getNewString();
		str.setName("scrapedText");
		str.setMaxLength(9999999);

		nmbr = cls.getNewNumber();
		nmbr.setName("scrapedTextLength");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("9999999"));
		
		// Unique constraints
		MdlUniqueConstraint uc = getNewUniqueConstraint();
		uc.getClasses().add(SYMBOL_CLASS_FULL_NAME);
		uc.getProperties().add("code");
		uc.setCaseSensitive(true);

		uc = getNewUniqueConstraint();
		uc.getClasses().add(SYMBOL_LINK_CLASS_FULL_NAME);
		uc.getProperties().add("symbolFrom");
		uc.getProperties().add("symbolTo");
		uc.getProperties().add("distance");

		uc = getNewUniqueConstraint();
		uc.getClasses().add(MODULE_CLASS_FULL_NAME);
		uc.getProperties().add("num");

		uc = getNewUniqueConstraint();
		uc.getClasses().add(MODULE_SYMBOL_CLASS_FULL_NAME);
		uc.getProperties().add("module");
		uc.getProperties().add("symbol");

		uc = getNewUniqueConstraint();
		uc.getClasses().add(CONTEXT_LINK_CLASS_FULL_NAME);
		uc.getProperties().add("symbolFrom");
		uc.getProperties().add("symbolTo");
		
		uc = getNewUniqueConstraint();
		uc.getClasses().add(EXAMPLE_CLASS_FULL_NAME);
		uc.getProperties().add("input");
		uc.getProperties().add("output");

		uc = getNewUniqueConstraint();
		uc.getClasses().add(ASSIGNMENT_CLASS_FULL_NAME);
		uc.getProperties().add("name");

		uc = getNewUniqueConstraint();
		uc.getClasses().add(CRAWLER_CLASS_FULL_NAME);
		uc.getProperties().add("crawlUrl");
	}
}
