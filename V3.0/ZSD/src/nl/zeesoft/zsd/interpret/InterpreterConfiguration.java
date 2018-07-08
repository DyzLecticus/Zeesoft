package nl.zeesoft.zsd.interpret;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.SequenceClassifier;
import nl.zeesoft.zsd.SymbolCorrector;
import nl.zeesoft.zsd.dialog.DialogSet;
import nl.zeesoft.zsd.initialize.InitializeClass;
import nl.zeesoft.zsd.initialize.Initializer;
import nl.zeesoft.zsd.util.LanguageClassifierJsonGenerator;
import nl.zeesoft.zsd.util.LanguageContextJsonGenerator;
import nl.zeesoft.zsd.util.LanguageCorrectorJsonGenerator;
import nl.zeesoft.zsd.util.LanguageMasterContextJsonGenerator;

public class InterpreterConfiguration extends Initializer {
	private DialogSet								dialogSet							= null;
	
	private String									baseDir								= "base/";
	private String									overrideDir							= "override/";
	
	private SequenceClassifier						languageClassifier					= null;
	private SortedMap<String,SymbolCorrector>		languageCorrectors					= new TreeMap<String,SymbolCorrector>();
	private SortedMap<String,SequenceClassifier>	languageMasterContextClassifiers	= new TreeMap<String,SequenceClassifier>();
	private SortedMap<String,SequenceClassifier>	languageContextClassifiers			= new TreeMap<String,SequenceClassifier>();
	private EntityValueTranslator					entityValueTranslator				= null;

	public InterpreterConfiguration(DialogSet dialogSet) {
		this.dialogSet = dialogSet;
	}
	
	public void initialize() {
		for (InitializeClass cls: getInitializeClasses()) {
			addClass(cls);
		}
		start();
	}
	
	public List<InitializeClass> getInitializeClasses() {
		List<InitializeClass> r = new ArrayList<InitializeClass>();
		
		if (entityValueTranslator==null) {
			entityValueTranslator = new EntityValueTranslator();
		}
		InitializeClass c = new InitializeClass();
		c.name = "EntityValueTranslator";
		c.obj = entityValueTranslator;
		r.add(c);
		
		languageClassifier = new SequenceClassifier();
		r.add(getInitializeClassForSequenceClassifier(languageClassifier,LanguageClassifierJsonGenerator.FILE_NAME));
		
		for (String language: dialogSet.getLanguages()) {
			SequenceClassifier sc = new SequenceClassifier();
			languageCorrectors.put(language,sc);
			r.add(getInitializeClassForSequenceClassifier(sc,LanguageCorrectorJsonGenerator.FILE_NAME_PREFIX + language + ".json"));

			sc = new SequenceClassifier();
			languageMasterContextClassifiers.put(language,sc);
			r.add(getInitializeClassForSequenceClassifier(sc,LanguageMasterContextJsonGenerator.FILE_NAME_PREFIX + language + ".json"));
			
			for (String masterContext: dialogSet.getLanguageMasterContexts().get(language)) {
				sc = new SequenceClassifier();
				languageContextClassifiers.put(language + masterContext,sc);
				r.add(getInitializeClassForSequenceClassifier(sc,LanguageContextJsonGenerator.FILE_NAME_PREFIX + language + masterContext + ".json"));
			}
		}
		return r;
	}
	
	public EntityValueTranslator getEntityValueTranslator() {
		return entityValueTranslator;
	}

	public void setEntityValueTranslator(EntityValueTranslator entityValueTranslator) {
		this.entityValueTranslator = entityValueTranslator;
	}

	public DialogSet getDialogSet() {
		return dialogSet;
	}

	public void setDialogSet(DialogSet dialogSet) {
		this.dialogSet = dialogSet;
	}

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public String getOverrideDir() {
		return overrideDir;
	}

	public void setOverrideDir(String overrideDir) {
		this.overrideDir = overrideDir;
	}

	public SequenceClassifier getLanguageClassifier() {
		return languageClassifier;
	}

	public void setLanguageClassifier(SequenceClassifier languageClassifier) {
		this.languageClassifier = languageClassifier;
	}

	public SortedMap<String, SymbolCorrector> getLanguageCorrectors() {
		return languageCorrectors;
	}

	public void setLanguageCorrectors(SortedMap<String, SymbolCorrector> languageCorrectors) {
		this.languageCorrectors = languageCorrectors;
	}

	public SortedMap<String, SequenceClassifier> getLanguageMasterContextClassifiers() {
		return languageMasterContextClassifiers;
	}

	public void setLanguageMasterContextClassifiers(
			SortedMap<String, SequenceClassifier> languageMasterContextClassifiers) {
		this.languageMasterContextClassifiers = languageMasterContextClassifiers;
	}

	public SortedMap<String, SequenceClassifier> getLanguageContextClassifiers() {
		return languageContextClassifiers;
	}

	public void setLanguageContextClassifiers(SortedMap<String, SequenceClassifier> languageContextClassifiers) {
		this.languageContextClassifiers = languageContextClassifiers;
	}

	private InitializeClass getInitializeClassForSequenceClassifier(SequenceClassifier sc,String fileName) {
		String dir = overrideDir;
		File file = new File(overrideDir + fileName);
		if (!file.exists()) {
			dir = baseDir;
		}
		InitializeClass r = new InitializeClass();
		r.name = fileName.split("\\.")[0];
		r.obj = sc;
		r.fileName = dir + fileName;
		return r;
	}
}
