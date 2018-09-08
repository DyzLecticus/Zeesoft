package nl.zeesoft.zevt.trans.entities.english;

import nl.zeesoft.zevt.trans.EntityObject;
import nl.zeesoft.zevt.trans.EntityValueTranslator;

public class EnglishProfanity extends EntityObject {
	private int counter = 1; 
	@Override
	public String getLanguage() {
		return LANG_ENG;
	}
	@Override
	public String getType() {
		return TYPE_PROFANITY;
	}
	@Override
	public void initialize(EntityValueTranslator translator) {
		super.initialize(translator);
		addProfanity("asshole");
		addProfanity("faggot");
		addProfanity("faggy");
		addProfanity("cunt");
		addProfanity("bitch");
		addProfanity("whore");
		addProfanity("slut");
		addProfanity("fuck");
		addProfanity("fucking");
		addProfanity("fucker");
		addProfanity("motherfucker");
		addProfanity("cock");
		addProfanity("dick");
		addProfanity("dickhead");
		addProfanity("bastard");
		addProfanity("turd");
		addProfanity("shite");
		addProfanity("shit");
		addProfanity("shitty");
		addProfanity("nigger");
		addProfanity("arse");
		addProfanity("arsehole");
		addProfanity("blighter");
		addProfanity("munter");
		addProfanity("tosser");
		addProfanity("wanker");
	}
	private void addProfanity(String name) {
		addEntityValue(name,"" + counter,name);
		counter++;
	}
}
