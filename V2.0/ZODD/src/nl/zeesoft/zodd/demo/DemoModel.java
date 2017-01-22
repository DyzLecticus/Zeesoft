package nl.zeesoft.zodd.demo;

import java.math.BigDecimal;

import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlLink;
import nl.zeesoft.zodb.database.model.MdlModel;
import nl.zeesoft.zodb.database.model.MdlNumber;
import nl.zeesoft.zodb.database.model.MdlPackage;
import nl.zeesoft.zodb.database.model.MdlString;
import nl.zeesoft.zodb.database.model.MdlUniqueConstraint;


public final class DemoModel extends MdlModel {
	public static final String	DEMO_ABSTRACT_CLASS_NAME	= "DemoAbstract";
	public static final String	DEMO_PARENT_CLASS_NAME		= "DemoParent";
	public static final String	DEMO_CHILD_CLASS_NAME		= "DemoChild";

	public static final String	DEMO_ABSTRACT_FULL_NAME		= DemoModel.class.getPackage().getName() + "." + DEMO_ABSTRACT_CLASS_NAME;
	public static final String	DEMO_PARENT_FULL_NAME		= DemoModel.class.getPackage().getName() + "." + DEMO_PARENT_CLASS_NAME;
	public static final String	DEMO_CHILD_FULL_NAME		= DemoModel.class.getPackage().getName() + "." + DEMO_CHILD_CLASS_NAME;

	@Override
	public void initializeCustomizations() {		
		MdlPackage pkg = getNewPackage();
		pkg.setName(DemoModel.class.getPackage().getName());

		MdlClass cls = pkg.getNewClass();
		cls.setName(DEMO_ABSTRACT_CLASS_NAME);
		cls.setAbstr(true);
		
		MdlString str = cls.getNewString();
		str.setName("demoStringShort");
		str.setIndex(true);
		str.setMaxLength(64);

		cls = pkg.getNewClass();
		cls.setName(DEMO_PARENT_CLASS_NAME);
		cls.getExtendsClasses().add(DEMO_ABSTRACT_FULL_NAME);
		
		str = cls.getNewString();
		str.setName("demoStringLong");
		str.setMaxLength(1024);
		
		MdlNumber nmbr = cls.getNewNumber();
		nmbr.setName("demoNumber");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("9999999"));
		nmbr.setIndex(true);

		MdlUniqueConstraint uc = getNewUniqueConstraint();
		uc.getClasses().add(cls.getFullName());
		uc.getProperties().add("demoStringShort");
		
		cls = pkg.getNewClass();
		cls.setName(DEMO_CHILD_CLASS_NAME);
		cls.getExtendsClasses().add(DEMO_ABSTRACT_FULL_NAME);

		MdlLink lnk = cls.getNewLink();
		lnk.setName("demoLink");
		lnk.setClassTo(DEMO_PARENT_FULL_NAME);
	}
}
