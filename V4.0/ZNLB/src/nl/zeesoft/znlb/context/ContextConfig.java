package nl.zeesoft.znlb.context;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.znlb.ZNLBConfig;
import nl.zeesoft.znlb.lang.Languages;
import nl.zeesoft.znlb.mod.ModZNLB;
import nl.zeesoft.zodb.db.InitializerDatabaseObject;
import nl.zeesoft.zodb.db.InitializerObject;

public class ContextConfig extends InitializerObject {
	public static final String	MASTER_CONTEXT_GENERIC				= "Generic";
	public static final String	MASTER_CONTEXT_GENERIC_DESC			= "Generic topics.";

	public static final String	CONTEXT_GENERIC_SUPPORT				= "Support";
	public static final String	CONTEXT_GENERIC_SUPPORT_DESC		= "Support the user when the bot is unable to help.";
	public static final String	CONTEXT_GENERIC_PROFANITY			= "Profanity";
	public static final String	CONTEXT_GENERIC_PROFANITY_DESC		= "Profanity, foul language and cursing.";

	private ZNLBConfig			configuration						= null;
	
	public ContextConfig(ZNLBConfig config) {
		super(config,ModZNLB.NAME + "/Contexts/");
		configuration = config;
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
		for (String code: configuration.getLanguages().getCodes()) {
			if (!code.equals(Languages.UNI)) {
				Language lang = new Language();
				lang.name = configuration.getLanguages().getNameForCode(code);
				lang.code = code;
				addObjectNoLock(lang);
				
				MasterContext mc = lang.addMasterContext(MASTER_CONTEXT_GENERIC,MASTER_CONTEXT_GENERIC_DESC);
				mc.addContext(CONTEXT_GENERIC_SUPPORT,CONTEXT_GENERIC_SUPPORT_DESC);
				mc.addContext(CONTEXT_GENERIC_PROFANITY,CONTEXT_GENERIC_PROFANITY_DESC);
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
