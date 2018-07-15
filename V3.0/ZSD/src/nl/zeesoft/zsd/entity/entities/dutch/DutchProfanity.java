package nl.zeesoft.zsd.entity.entities.dutch;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.entity.EntityObject;

public class DutchProfanity extends EntityObject {
	private int counter = 1; 
	@Override
	public String getLanguage() {
		return BaseConfiguration.LANG_NLD;
	}
	@Override
	public String getType() {
		return BaseConfiguration.TYPE_PROFANITY;
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
