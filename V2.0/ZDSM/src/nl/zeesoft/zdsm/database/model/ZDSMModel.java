package nl.zeesoft.zdsm.database.model;

import java.math.BigDecimal;

import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlLink;
import nl.zeesoft.zodb.database.model.MdlModel;
import nl.zeesoft.zodb.database.model.MdlNumber;
import nl.zeesoft.zodb.database.model.MdlPackage;
import nl.zeesoft.zodb.database.model.MdlString;
import nl.zeesoft.zodb.database.model.MdlUniqueConstraint;

public class ZDSMModel extends MdlModel {	
	public static final String		DOMAIN_CLASS_NAME					= "Domain";
	public static final String		TASK_CLASS_NAME						= "Task";
	public static final String		SERVICE_CLASS_NAME					= "Service";
	public static final String		LOG_CLASS_NAME						= "Log";
	public static final String		RESPONSE_CLASS_NAME					= "Response";
	public static final String		PROCESS_CLASS_NAME					= "Process";
	public static final String		PROCESS_LOG_CLASS_NAME				= "ProcessLog";

	public static final String		DOMAIN_CLASS_FULL_NAME				= ZDSMModel.class.getPackage().getName() + "." + DOMAIN_CLASS_NAME;
	public static final String		TASK_CLASS_FULL_NAME				= ZDSMModel.class.getPackage().getName() + "." + TASK_CLASS_NAME;
	public static final String		SERVICE_CLASS_FULL_NAME				= ZDSMModel.class.getPackage().getName() + "." + SERVICE_CLASS_NAME;
	public static final String		LOG_CLASS_FULL_NAME					= ZDSMModel.class.getPackage().getName() + "." + LOG_CLASS_NAME;
	public static final String		RESPONSE_CLASS_FULL_NAME			= ZDSMModel.class.getPackage().getName() + "." + RESPONSE_CLASS_NAME;
	public static final String		PROCESS_CLASS_FULL_NAME				= ZDSMModel.class.getPackage().getName() + "." + PROCESS_CLASS_NAME;
	public static final String		PROCESS_LOG_CLASS_FULL_NAME			= ZDSMModel.class.getPackage().getName() + "." + PROCESS_LOG_CLASS_NAME;
	
	@Override
	public void initializeCustomizations() {
		MdlPackage pkg = getNewPackage();
		pkg.setName(ZDSMModel.class.getPackage().getName());
		
		// DOMAIN
		MdlClass cls = pkg.getNewClass();
		cls.setName(DOMAIN_CLASS_NAME);

		MdlString str = cls.getNewString();
		str.setName("name");
		str.setMaxLength(64);
		str.setIndex(true);
		
		str = cls.getNewString();
		str.setName("address");
		str.setMaxLength(96);
		
		MdlNumber nmbr = cls.getNewNumber();
		nmbr.setName("port");
		nmbr.setMinValue(new BigDecimal("1"));
		nmbr.setMaxValue(new BigDecimal("99999"));

		nmbr = cls.getNewNumber();
		nmbr.setName("checkProcessSeconds");
		nmbr.setMinValue(new BigDecimal("10"));
		nmbr.setMaxValue(new BigDecimal("3600"));

		nmbr = cls.getNewNumber();
		nmbr.setName("checkServiceSeconds");
		nmbr.setMinValue(new BigDecimal("10"));
		nmbr.setMaxValue(new BigDecimal("3600"));

		nmbr = cls.getNewNumber();
		nmbr.setName("keepProcessLogDays");
		nmbr.setMinValue(new BigDecimal("1"));
		nmbr.setMaxValue(new BigDecimal("365"));

		nmbr = cls.getNewNumber();
		nmbr.setName("keepResponseDays");
		nmbr.setMinValue(new BigDecimal("1"));
		nmbr.setMaxValue(new BigDecimal("365"));

		str = cls.getNewString();
		str.setName("https");
		str.setMaxLength(5);

		str = cls.getNewString();
		str.setName("addDomainToPath");
		str.setMaxLength(5);

		str = cls.getNewString();
		str.setName("addHeaderHost");
		str.setMaxLength(5);

		str = cls.getNewString();
		str.setName("addHeaderHostPort");
		str.setMaxLength(5);

		str = cls.getNewString();
		str.setName("addHeaderAuthBasic");
		str.setMaxLength(5);

		str = cls.getNewString();
		str.setName("userName");
		str.setMaxLength(64);

		str = cls.getNewString();
		str.setName("userPassword");
		str.setMaxLength(64);
		str.setEncode(true);
		
		str = cls.getNewString();
		str.setName("active");
		str.setMaxLength(5);

		str = cls.getNewString();
		str.setName("removeMe");
		str.setMaxLength(5);


		// TASK
		cls = pkg.getNewClass();
		cls.setName(TASK_CLASS_NAME);
		cls.setAbstr(true);

		str = cls.getNewString();
		str.setName("name");
		str.setMaxLength(64);
		str.setIndex(true);
		
		str = cls.getNewString();
		str.setName("domainNameContains");
		str.setMaxLength(64);

		str = cls.getNewString();
		str.setName("active");
		str.setMaxLength(5);

		str = cls.getNewString();
		str.setName("removeMe");
		str.setMaxLength(5);

		// SERVICE
		cls = pkg.getNewClass();
		cls.setName(SERVICE_CLASS_NAME);
		cls.getExtendsClasses().add(TASK_CLASS_FULL_NAME);

		str = cls.getNewString();
		str.setName("method");
		str.setMaxLength(64);
		
		str = cls.getNewString();
		str.setName("path");
		str.setMaxLength(96);

		str = cls.getNewString();
		str.setName("query");
		str.setMaxLength(2048);

		str = cls.getNewString();
		str.setName("header");
		str.setMaxLength(2048);
		
		str = cls.getNewString();
		str.setName("body");
		str.setMaxLength(999999);

		str = cls.getNewString();
		str.setName("timeOut");
		str.setMaxLength(5);

		nmbr = cls.getNewNumber();
		nmbr.setName("timeOutSeconds");
		nmbr.setMinValue(new BigDecimal("1"));
		nmbr.setMaxValue(new BigDecimal("60"));

		str = cls.getNewString();
		str.setName("expectedCode");
		str.setMaxLength(4);

		str = cls.getNewString();
		str.setName("expectedHeader");
		str.setMaxLength(2048);
		
		str = cls.getNewString();
		str.setName("expectedBody");
		str.setMaxLength(999999);
		
		// LOG
		cls = pkg.getNewClass();
		cls.setName(LOG_CLASS_NAME);
		cls.setAbstr(true);
		
		nmbr = cls.getNewNumber();
		nmbr.setName("dateTime");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("" + Long.MAX_VALUE));
		nmbr.setIndex(true);

		nmbr = cls.getNewNumber();
		nmbr.setName("duration");
		nmbr.setMinValue(new BigDecimal("0"));
		nmbr.setMaxValue(new BigDecimal("" + Long.MAX_VALUE));
		nmbr.setIndex(true);

		str = cls.getNewString();
		str.setName("success");
		str.setMaxLength(5);
		str.setIndex(true);
		
		MdlLink lnk = cls.getNewLink();
		lnk.setName("domain");
		lnk.setClassTo(DOMAIN_CLASS_FULL_NAME);
		
		// RESPONSE
		cls = pkg.getNewClass();
		cls.setName(RESPONSE_CLASS_NAME);
		cls.getExtendsClasses().add(LOG_CLASS_FULL_NAME);

		str = cls.getNewString();
		str.setName("fullUrl");
		str.setMaxLength(2048);
		
		str = cls.getNewString();
		str.setName("method");
		str.setMaxLength(4);
		
		str = cls.getNewString();
		str.setName("requestHeader");
		str.setMaxLength(2048);
		
		str = cls.getNewString();
		str.setName("requestBody");
		str.setMaxLength(999999);

		str = cls.getNewString();
		str.setName("code");
		str.setMaxLength(4);

		str = cls.getNewString();
		str.setName("header");
		str.setMaxLength(2048);
		
		str = cls.getNewString();
		str.setName("body");
		str.setMaxLength(999999);

		lnk = cls.getNewLink();
		lnk.setName("service");
		lnk.setClassTo(SERVICE_CLASS_FULL_NAME);

		// PROCESS
		cls = pkg.getNewClass();
		cls.setName(PROCESS_CLASS_NAME);
		cls.getExtendsClasses().add(TASK_CLASS_FULL_NAME);

		str = cls.getNewString();
		str.setName("className");
		str.setMaxLength(96);
		str.setIndex(true);
		
		str = cls.getNewString();
		str.setName("description");
		str.setMaxLength(2048);
		str.setIndex(true);
		
		// PROCESS LOG
		cls = pkg.getNewClass();
		cls.setName(PROCESS_LOG_CLASS_NAME);
		cls.getExtendsClasses().add(LOG_CLASS_FULL_NAME);

		str = cls.getNewString();
		str.setName("log");
		str.setMaxLength(999999);
		
		lnk = cls.getNewLink();
		lnk.setName("process");
		lnk.setClassTo(PROCESS_CLASS_FULL_NAME);
		
		// Unique constraints
		MdlUniqueConstraint uc = getNewUniqueConstraint();
		uc.getClasses().add(DOMAIN_CLASS_FULL_NAME);
		uc.getProperties().add("name");

		uc = getNewUniqueConstraint();
		uc.getClasses().add(SERVICE_CLASS_FULL_NAME);
		uc.getProperties().add("name");

		uc = getNewUniqueConstraint();
		uc.getClasses().add(PROCESS_CLASS_FULL_NAME);
		uc.getProperties().add("name");
	}
}
