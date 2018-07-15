package nl.zeesoft.zsd.entity.entities.english;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.entity.EntityObject;

public class EnglishProfanity extends EntityObject {
	private int counter = 1; 
	@Override
	public String getLanguage() {
		return BaseConfiguration.LANG_ENG;
	}
	@Override
	public String getType() {
		return BaseConfiguration.TYPE_PROFANITY;
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
