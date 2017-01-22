package nl.zeesoft.zodb.test;

import java.math.BigDecimal;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlLink;
import nl.zeesoft.zodb.database.model.MdlModel;
import nl.zeesoft.zodb.database.model.MdlNumber;
import nl.zeesoft.zodb.database.model.MdlPackage;
import nl.zeesoft.zodb.database.model.MdlString;
import nl.zeesoft.zodb.database.model.MdlUniqueConstraint;


public final class TestModel extends MdlModel {
	public static final String	TEST_CLASS_1_CLASS_NAME	= "TestClass1";
	public static final String	TEST_CLASS_2_CLASS_NAME	= "TestClass2";

	public static final String	TEST_CLASS_1_FULL_NAME	= TestModel.class.getPackage().getName() + "." + TEST_CLASS_1_CLASS_NAME;
	public static final String	TEST_CLASS_2_FULL_NAME	= TestModel.class.getPackage().getName() + "." + TEST_CLASS_2_CLASS_NAME;
	
	@Override
	public void initializeCustomizations() {		
		MdlPackage pkg = getNewPackage();
		pkg.setName(TestModel.class.getPackage().getName());

		MdlClass cls = pkg.getNewClass();
		cls.setName(TEST_CLASS_1_CLASS_NAME);

		MdlString str = cls.getNewString();
		str.setName("testStringProperty");
		str.setMaxLength(1024);

		MdlNumber nmbr = cls.getNewNumber();
		nmbr.setName("testNumberProperty");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("9999"));
		nmbr.setIndex(true);

		MdlUniqueConstraint uc = getNewUniqueConstraint();
		uc.getClasses().add(cls.getFullName());
		uc.getProperties().add(str.getName());
		
		cls = pkg.getNewClass();
		cls.setName(TEST_CLASS_2_CLASS_NAME);

		str = cls.getNewString();
		str.setName("testStringProperty");
		str.setMaxLength(24);
		str.setIndex(true);

		MdlLink lnk = cls.getNewLink();
		lnk.setName("testLinkProperty");
		lnk.setClassTo(TEST_CLASS_1_FULL_NAME);
	}
	
	public static MdlClass getTestClass1() {
		return DbConfig.getInstance().getModel().getClassByFullName(TEST_CLASS_1_FULL_NAME);
	}

	public static MdlClass getTestClass2() {
		return DbConfig.getInstance().getModel().getClassByFullName(TEST_CLASS_2_FULL_NAME);
	}
}
