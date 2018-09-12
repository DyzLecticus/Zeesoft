package nl.zeesoft.zevt.trans.entities.english;

import nl.zeesoft.zevt.trans.DatabaseEntityObject;
import nl.zeesoft.zevt.trans.Translator;
import nl.zeesoft.zodb.Languages;

public class EnglishProfanity extends DatabaseEntityObject {
	private int counter = 1; 
	public EnglishProfanity(Translator t) {
		super(t);
	}
	@Override
	public String getLanguage() {
		return Languages.ENG;
	}
	@Override
	public String getType() {
		return TYPE_PROFANITY;
	}
	@Override
	public void initializeEntityValues() {
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
