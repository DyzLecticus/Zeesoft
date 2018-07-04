package nl.zeesoft.zsd.dialog.english;

import nl.zeesoft.zsd.entity.EntityObject;

public class EnglishGenericHandshake extends GenericHandshake {
	public static final String					MASTER_CONTEXT_GENERIC		= "Generic";
	public static final String					CONTEXT_GENERIC_HANDSHAKE	= "Handshake";
	public static final String					CONTEXT_GENERIC_QNA			= "QuestionAndAnswer";

	public static final String					VARIABLE_FULLNAME			= "fullName";
	public static final String					VARIABLE_FIRSTNAME			= "firstName";
	public static final String					VARIABLE_PREPOSITION		= "preposition";
	public static final String					VARIABLE_LASTNAME			= "lastName";
	
	@Override
	public void initialize() {
		setLanguage(EntityObject.LANG_ENG);
		setMasterContext(MASTER_CONTEXT_GENERIC);
		setContext(CONTEXT_GENERIC_HANDSHAKE);
		
		
		// TODO Auto-generated method stub
		
	}
	
}
