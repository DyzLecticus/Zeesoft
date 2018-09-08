package nl.zeesoft.zevt.trans.entities.dutch;

import nl.zeesoft.zevt.trans.EntityObject;
import nl.zeesoft.zevt.trans.EntityValueTranslator;

public class DutchProfanity extends EntityObject {
	private int counter = 1; 
	@Override
	public String getLanguage() {
		return LANG_NLD;
	}
	@Override
	public String getType() {
		return TYPE_PROFANITY;
	}
	@Override
	public void initialize(EntityValueTranslator translator) {
		super.initialize(translator);
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
