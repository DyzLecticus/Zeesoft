package nl.zeesoft.zsd.interpret;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.SequenceClassifier;
import nl.zeesoft.zsd.SequencePreprocessor;
import nl.zeesoft.zsd.initialize.InitializeClass;
import nl.zeesoft.zsd.initialize.Initializer;
import nl.zeesoft.zsd.util.LanguageClassifierJsonGenerator;
import nl.zeesoft.zsd.util.LanguageContextJsonGenerator;
import nl.zeesoft.zsd.util.LanguageMasterContextJsonGenerator;
import nl.zeesoft.zsd.util.LanguagePreprocessorJsonGenerator;

public class InterpreterConfiguration extends Initializer {
	private BaseConfiguration						base								= new BaseConfiguration();
	
	private SequencePreprocessor					languagePreprocessor				= null;
	private SequenceClassifier						languageClassifier					= null;
	private SortedMap<String,SequenceClassifier>	languageMasterContextClassifiers	= new TreeMap<String,SequenceClassifier>();
	private SortedMap<String,SequenceClassifier>	languageContextClassifiers			= new TreeMap<String,SequenceClassifier>();
	private EntityValueTranslator					entityValueTranslator				= null;

	public InterpreterConfiguration() {
		
	}

	public InterpreterConfiguration(BaseConfiguration base) {
		this.base = base;
	}

	public InterpreterConfiguration(Messenger msgr, WorkerUnion uni) {
		super(msgr,uni);
	}

	public InterpreterConfiguration(Messenger msgr, WorkerUnion uni, BaseConfiguration base) {
		super(msgr,uni);
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
		r.add(getInitializeClassForSequencePreprocessor(languagePreprocessor,LanguagePreprocessorJsonGenerator.FILE_NAME));
		r.add(getInitializeClassForSequenceClassifier(languageClassifier,LanguageClassifierJsonGenerator.FILE_NAME));
		for (String language: base.getSupportedLanguages()) {
			SequenceClassifier sc =	languageMasterContextClassifiers.get(language);
			r.add(getInitializeClassForSequenceClassifier(sc,LanguageMasterContextJsonGenerator.FILE_NAME_PREFIX + language + ".json"));
			for (String masterContext: base.getSupportedMasterContexts().get(language)) {
				sc = languageContextClassifiers.get(language + masterContext);
				r.add(getInitializeClassForSequenceClassifier(sc,LanguageContextJsonGenerator.FILE_NAME_PREFIX + language + masterContext + ".json"));
			}
		}
		InitializeClass c = new InitializeClass();
		c.name = "EntityValueTranslator";
		c.obj = entityValueTranslator;
		r.add(c);
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

	public SequencePreprocessor getLanguagePreprocessor() {
		return languagePreprocessor;
	}

	public void setLanguagePreprocessor(SequencePreprocessor languagePreprocessor) {
		this.languagePreprocessor = languagePreprocessor;
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

	private InitializeClass getInitializeClassForSequencePreprocessor(SequencePreprocessor sp,String fileName) {
		InitializeClass r = new InitializeClass();
		r.name = fileName.split("\\.")[0];
		r.obj = sp;
		r.fileNames.add(getBaseDirForFileName(fileName) + fileName);
		addExtensionForFileName(r.fileNames,fileName);
		return r;
	}

	private InitializeClass getInitializeClassForSequenceClassifier(SequenceClassifier sc,String fileName) {
		InitializeClass r = new InitializeClass();
		r.name = fileName.split("\\.")[0];
		r.obj = sc;
		r.fileNames.add(getBaseDirForFileName(fileName) + fileName);
		addExtensionForFileName(r.fileNames,fileName);
		return r;
	}
	
	private String getBaseDirForFileName(String fileName) {
		String dir = base.getFullOverrideDir();
		File file = new File(dir + fileName);
		if (!file.exists()) {
			dir = base.getFullBaseDir();
		}
		return dir;
	}

	private void addExtensionForFileName(List<String> fileNames,String fileName) {
		String extend = base.getFullExtendDir() + fileName;
		File file = new File(extend);
		if (file.exists()) {
			fileNames.add(extend);
		}
	}

	private void checkInitializedClasses() {
		if (entityValueTranslator==null) {
			entityValueTranslator = new EntityValueTranslator();
		}
		if (languageClassifier==null) {
			languageClassifier = new SequenceClassifier();
		}
		if (languagePreprocessor==null) {
			languagePreprocessor = new SequencePreprocessor();
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
