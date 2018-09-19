package nl.zeesoft.znlb.context;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.znlb.lang.Languages;
import nl.zeesoft.znlb.mod.ModZNLB;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.InitializerDatabaseObject;
import nl.zeesoft.zodb.db.InitializerObject;

public class ContextConfig extends InitializerObject {
	public static final String	MASTER_CONTEXT_GENERIC				= "Generic";
	public static final String	MASTER_CONTEXT_GENERIC_DESC			= "Generic topics.";

	public static final String	CONTEXT_GENERIC_CANCEL				= "Cancel";
	public static final String	CONTEXT_GENERIC_CLASSIFICATION		= "Classification";
	public static final String	CONTEXT_GENERIC_GOODBYE				= "Goodbye";
	public static final String	CONTEXT_GENERIC_HANDSHAKE			= "Handshake";
	public static final String	CONTEXT_GENERIC_LANGUAGE			= "Language";
	public static final String	CONTEXT_GENERIC_MATH				= "Math";
	public static final String	CONTEXT_GENERIC_PROFANITY			= "Profanity";
	public static final String	CONTEXT_GENERIC_QNA					= "QuestionAndAnswer";
	public static final String	CONTEXT_GENERIC_SUPPORT				= "Support";
	public static final String	CONTEXT_GENERIC_THANKS				= "Thanks";

	public static final String	CONTEXT_GENERIC_CANCEL_DESC			= "Allows the user to cancel the current dialog.";
	public static final String	CONTEXT_GENERIC_CLASSIFICATION_DESC	= "Generates output when classification fails.";
	public static final String	CONTEXT_GENERIC_HANDSHAKE_DESC		= "Greeting and name exchange.";
	public static final String	CONTEXT_GENERIC_GOODBYE_DESC		= "Goodbye handling.";
	public static final String	CONTEXT_GENERIC_LANGUAGE_DESC		= "Handles questions about languages.";
	public static final String	CONTEXT_GENERIC_MATH_DESC			= "Handles basic mathematical calculation.";
	public static final String	CONTEXT_GENERIC_PROFANITY_DESC		= "Handles profanity like foul language and cursing.";
	public static final String	CONTEXT_GENERIC_QNA_DESC			= "Handles general questions.";
	public static final String	CONTEXT_GENERIC_SUPPORT_DESC		= "Allows the user to request support.";
	public static final String	CONTEXT_GENERIC_THANKS_DESC			= "Handles compliments and feedback.";
	
	public ContextConfig(Config config) {
		super(config,ModZNLB.NAME + "/Contexts/");
	}
	
	public List<Language> getLanguages() {
		List<Language> r = null;
		lockMe(this);
		r = new ArrayList<Language>(getLanguagesNoLock());
		unlockMe(this);
		return r;
	}

	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		JsElem langsElem = new JsElem("languages",true);
		json.rootElement.children.add(langsElem);
		for (Language lang: getLanguages()) {
			JsElem langElem = new JsElem();
			langsElem.children.add(langElem);
			langElem.children = lang.toJson().rootElement.children;
		}
		return json;
	}
	
	@Override
	protected void initializeDatabaseObjectsNoLock() {
		Languages languages = ((ModZNLB) getConfiguration().getModule(ModZNLB.NAME)).getLanguages(); 
		for (String code: languages.getCodes()) {
			if (!code.equals(Languages.UNI)) {
				Language lang = new Language();
				lang.name = languages.getNameForCode(code);
				lang.code = code;
				addObjectNoLock(lang);
				
				MasterContext mc = lang.addMasterContext(MASTER_CONTEXT_GENERIC,MASTER_CONTEXT_GENERIC_DESC);
				mc.addContext(CONTEXT_GENERIC_CANCEL,CONTEXT_GENERIC_CANCEL_DESC);
				mc.addContext(CONTEXT_GENERIC_CLASSIFICATION,CONTEXT_GENERIC_CLASSIFICATION_DESC);
				mc.addContext(CONTEXT_GENERIC_GOODBYE,CONTEXT_GENERIC_GOODBYE_DESC);
				mc.addContext(CONTEXT_GENERIC_HANDSHAKE,CONTEXT_GENERIC_HANDSHAKE_DESC);
				mc.addContext(CONTEXT_GENERIC_LANGUAGE,CONTEXT_GENERIC_LANGUAGE_DESC);
				mc.addContext(CONTEXT_GENERIC_MATH,CONTEXT_GENERIC_MATH_DESC);
				mc.addContext(CONTEXT_GENERIC_PROFANITY,CONTEXT_GENERIC_PROFANITY_DESC);
				mc.addContext(CONTEXT_GENERIC_QNA,CONTEXT_GENERIC_QNA_DESC);
				mc.addContext(CONTEXT_GENERIC_SUPPORT,CONTEXT_GENERIC_SUPPORT_DESC);
				mc.addContext(CONTEXT_GENERIC_THANKS,CONTEXT_GENERIC_THANKS_DESC);
			}
		}
	}

	@Override
	protected InitializerDatabaseObject getNewObjectNoLock(String name) {
		Language lang = new Language();
		lang.name = name;
		return lang;
	}

	private List<Language> getLanguagesNoLock() {
		List<Language> r = new ArrayList<Language>();
		for (InitializerDatabaseObject object: getObjectsNoLock()) {
			r .add((Language)object);
		}
		return r;
	}
}
