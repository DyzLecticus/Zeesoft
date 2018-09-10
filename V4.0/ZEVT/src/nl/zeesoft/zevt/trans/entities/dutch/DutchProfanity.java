package nl.zeesoft.zevt.trans.entities.dutch;

import nl.zeesoft.zevt.trans.DatabaseEntityObject;
import nl.zeesoft.zevt.trans.Translator;

public class DutchProfanity extends DatabaseEntityObject {
	private int counter = 1; 
	public DutchProfanity(Translator t) {
		super(t);
	}
	@Override
	public String getLanguage() {
		return LANG_NLD;
	}
	@Override
	public String getType() {
		return TYPE_PROFANITY;
	}
	@Override
	public void initializeEntityValues() {
		addProfanity("eikel");
		addProfanity("klootzak");
		addProfanity("flikker");
		addProfanity("kut");
		addProfanity("kutwijf");
		addProfanity("teef");
		addProfanity("hoer");
		addProfanity("slet");
		addProfanity("fuck");
		addProfanity("fucking");
		addProfanity("fucker");
		addProfanity("fokking");
		addProfanity("fakking");
		addProfanity("neuk");
		addProfanity("moederneuker");
		addProfanity("lul");
		addProfanity("pisvlek");
		addProfanity("klere");
		addProfanity("klerelijer");
		addProfanity("kolere");
		addProfanity("mongool");
		addProfanity("tering");
		addProfanity("tyfus");
		addProfanity("drol");
		addProfanity("flapdrol");
		addProfanity("schijt");
		addProfanity("schijterige");
		addProfanity("varkensneuker");
		addProfanity("geitenneuker");
		addProfanity("nikker");
	}
	private void addProfanity(String name) {
		addEntityValue(name,"" + counter,name);
		counter++;
	}
}
