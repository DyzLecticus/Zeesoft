package nl.zeesoft.zidm.dialog;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zspr.Language;

public class DialogModelNameTranslator extends Locker {
	private long									uniqueId			= 0;
	private List<DialogModelNameTranslation>		translations		= new ArrayList<DialogModelNameTranslation>();
	
	public DialogModelNameTranslator() {
		super(null);
	}

	public DialogModelNameTranslator(List<DialogModelNameTranslation>	translations) {
		super(null);
		this.translations = translations;
	}
	
	public DialogModelNameTranslator(Messenger msgr,List<DialogModelNameTranslation> translations) {
		super(msgr);
		this.translations = translations;
	}

	public List<DialogModelNameTranslation> getTranslations() {
		List<DialogModelNameTranslation> transList = null;
		lockMe(this);
		transList = new ArrayList<DialogModelNameTranslation>(translations);
		unlockMe(this);
		return transList;
	}
	
	//public String addTranslation(String languageCode, boolean singular, String translation) {
	//	return addTranslation("",languageCode,singular,translation);
	//}
	
	public String addTranslation(String name,String languageCode, boolean singular, String translation) {
		lockMe(this);
		boolean exists = false;
		if (name.length()>0) {
			exists = (getTranslationNoLock(name, languageCode, singular)).length()>0;
			if (exists) {
				System.err.println("Translation already exists: " + name + " " + languageCode + " " + singular);
			}
		} 
		if (!exists) {
			exists = (getNameNoLock(languageCode,translation)).length()>0;
			if (exists) {
				System.err.println("Translation already exists: " + languageCode + " " + translation);
			}
		}
		if (!exists) {
			if (name.length()==0) {
				name = getNewUniqueNameNoLock();
			}
			DialogModelNameTranslation trans = new DialogModelNameTranslation();
			trans.setName(name);
			trans.setLanguage(Language.getLanguage(languageCode));
			trans.setSingular(singular);
			trans.setTranslation(translation);
			translations.add(trans);
		}
		unlockMe(this);
		return name;
	}

	public void removeTranslations(String name) {
		lockMe(this);
		List<DialogModelNameTranslation> testTrans = new ArrayList<DialogModelNameTranslation>(translations);
		for (DialogModelNameTranslation trans: testTrans) {
			if (trans.getName().equals(name)) {
				translations.remove(trans);
			}
		}
		unlockMe(this);
	}

	public String getTranslation(String name, String languageCode, boolean singular) {
		String translation = "";
		lockMe(this);
		translation = getTranslationNoLock(name,languageCode,singular);
		unlockMe(this);
		return translation;
	}

	public String getName(String languageCode,String translation) {
		String name = "";
		lockMe(this);
		name = getNameNoLock(languageCode,translation);
		unlockMe(this);
		return name;
	}
	
	private String getTranslationNoLock(String name, String languageCode, boolean singular) {
		String translation = "";
		for (DialogModelNameTranslation trans: translations) {
			if (trans.getName().equals(name) &&
				trans.getLanguage().getCode().equals(languageCode) &&
				trans.isSingular() == singular
				) {
				translation = trans.getTranslation();
				break;
			}
		}
		return translation;
	}

	private String getNameNoLock(String languageCode,String translation) {
		String name = "";
		for (DialogModelNameTranslation trans: translations) {
			if (trans.getLanguage().getCode().equals(languageCode) &&
				trans.getTranslation().equals(translation)
				) {
				name = trans.getName();
				break;
			}
		}
		return name;
	}

	private String getNewUniqueNameNoLock() {
		String name = "";
		uniqueId++;
		name = "" + uniqueId;
		return name;
	}
}
