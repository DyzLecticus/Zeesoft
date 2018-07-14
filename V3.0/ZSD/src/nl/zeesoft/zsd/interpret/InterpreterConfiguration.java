package nl.zeesoft.zsd.interpret;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.SequenceClassifier;
import nl.zeesoft.zsd.initialize.InitializeClass;
import nl.zeesoft.zsd.initialize.Initializer;
import nl.zeesoft.zsd.util.LanguageClassifierJsonGenerator;
import nl.zeesoft.zsd.util.LanguageContextJsonGenerator;
import nl.zeesoft.zsd.util.LanguageMasterContextJsonGenerator;

public class InterpreterConfiguration extends Initializer {
	private BaseConfiguration						base								= new BaseConfiguration();
	
	private SequenceClassifier						languageClassifier					= null;
	private SortedMap<String,SequenceClassifier>	languageMasterContextClassifiers	= new TreeMap<String,SequenceClassifier>();
	private SortedMap<String,SequenceClassifier>	languageContextClassifiers			= new TreeMap<String,SequenceClassifier>();
	private EntityValueTranslator					entityValueTranslator				= null;

	private long									maxMsPerSymbol						= 100;
	private long									maxMsPerSequence					= 2000;

	public InterpreterConfiguration() {

	}

	public InterpreterConfiguration(BaseConfiguration base) {
		this.base = base;
	}

	public void initialize() {
		initialize(null);
	}

	public void initialize(List<String> classNames) {
		for (InitializeClass cls: getInitializeClasses()) {
			if (classNames==null || classNames.size()==0 || classNames.contains(cls.name)) {
				addClass(cls);
			}
		}
		start();
	}
	
	public List<InitializeClass> getInitializeClasses() {
		checkInitializedClasses();
		List<InitializeClass> r = new ArrayList<InitializeClass>();
		InitializeClass c = new InitializeClass();
		c.name = "EntityValueTranslator";
		c.obj = entityValueTranslator;
		r.add(c);
		r.add(getInitializeClassForSequenceClassifier(languageClassifier,LanguageClassifierJsonGenerator.FILE_NAME));
		for (String language: base.getSupportedLanguages()) {
			SequenceClassifier sc =	languageMasterContextClassifiers.get(language);
			r.add(getInitializeClassForSequenceClassifier(sc,LanguageMasterContextJsonGenerator.FILE_NAME_PREFIX + language + ".json"));
			for (String masterContext: base.getSupportedMasterContexts().get(language)) {
				sc = languageContextClassifiers.get(language + masterContext);
				r.add(getInitializeClassForSequenceClassifier(sc,LanguageContextJsonGenerator.FILE_NAME_PREFIX + language + masterContext + ".json"));
			}
		}
		return r;
	}

	public BaseConfiguration getBase() {
		return base;
	}

	public EntityValueTranslator getEntityValueTranslator() {
		return entityValueTranslator;
	}

	public void setEntityValueTranslator(EntityValueTranslator entityValueTranslator) {
		this.entityValueTranslator = entityValueTranslator;
	}

	public SequenceClassifier getLanguageClassifier() {
		return languageClassifier;
	}

	public void setLanguageClassifier(SequenceClassifier languageClassifier) {
		this.languageClassifier = languageClassifier;
	}

	public SortedMap<String, SequenceClassifier> getLanguageMasterContextClassifiers() {
		return languageMasterContextClassifiers;
	}

	public void setLanguageMasterContextClassifiers(SortedMap<String, SequenceClassifier> languageMasterContextClassifiers) {
		this.languageMasterContextClassifiers = languageMasterContextClassifiers;
	}

	public SortedMap<String, SequenceClassifier> getLanguageContextClassifiers() {
		return languageContextClassifiers;
	}

	public void setLanguageContextClassifiers(SortedMap<String, SequenceClassifier> languageContextClassifiers) {
		this.languageContextClassifiers = languageContextClassifiers;
	}

	public long getMaxMsPerSymbol() {
		return maxMsPerSymbol;
	}

	public void setMaxMsPerSymbol(long maxMsPerSymbol) {
		this.maxMsPerSymbol = maxMsPerSymbol;
	}

	public long getMaxMsPerSequence() {
		return maxMsPerSequence;
	}

	public void setMaxMsPerSequence(long maxMsPerSequence) {
		this.maxMsPerSequence = maxMsPerSequence;
	}

	private InitializeClass getInitializeClassForSequenceClassifier(SequenceClassifier sc,String fileName) {
		String dir = base.getOverrideDir();
		String extend = "";
		File file = new File(base.getOverrideDir() + fileName);
		if (!file.exists()) {
			dir = base.getBaseDir();
		}
		InitializeClass r = new InitializeClass();
		r.name = fileName.split("\\.")[0];
		r.obj = sc;
		r.fileNames.add(dir + fileName);
		extend = base.getExtendDir() + fileName;
		file = new File(extend);
		if (file.exists()) {
			r.fileNames.add(extend);
		}
		return r;
	}
	
	private void checkInitializedClasses() {
		if (entityValueTranslator==null) {
			entityValueTranslator = new EntityValueTranslator();
		}
		if (languageClassifier==null) {
			languageClassifier = new SequenceClassifier();
		}
		for (String language: base.getSupportedLanguages()) {
			SequenceClassifier sc =	languageMasterContextClassifiers.get(language);
			if (sc==null) {
				sc = new SequenceClassifier();
				languageMasterContextClassifiers.put(language,sc);
			}
			for (String masterContext: base.getSupportedMasterContexts().get(language)) {
				sc = languageContextClassifiers.get(language + masterContext);
				if (sc==null) {
					sc = new SequenceClassifier();
					languageContextClassifiers.put(language + masterContext,sc);
				}
			}
		}
	}
}
