package nl.zeesoft.zidm.dialog.model;

import nl.zeesoft.zspr.Language;

public class DialogModelNameTranslation {
	private Language 	language 	= null;
	private boolean		singular	= true;
	private String		name		= "";
	private String		translation	= "";
	
	protected Language getLanguage() {
		return language;
	}
	
	public void setLanguage(Language language) {
		this.language = language;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTranslation() {
		return translation;
	}
	
	public void setTranslation(String translation) {
		this.translation = translation;
	}

	public boolean isSingular() {
		return singular;
	}

	public void setSingular(boolean singular) {
		this.singular = singular;
	}
}
