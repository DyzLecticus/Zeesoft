package nl.zeesoft.zsd.dialog.dialogs;

public abstract class GenericHandshake extends Generic {
	public static final String	CONTEXT_GENERIC_HANDSHAKE	= "Handshake";

	public static final String	VARIABLE_FIRSTNAME			= "firstName";
	public static final String	VARIABLE_LASTNAME			= "lastName";
	public static final String	VARIABLE_PREPOSITION		= "preposition";

	public static final String	VARIABLE_FULLNAME			= "fullName";

	//private int					counter						= 0;
	
	public GenericHandshake() {
		setContext(CONTEXT_GENERIC_HANDSHAKE);
		setHandlerClassName(GenericHandshakeHandler.class.getName());
	}

	/*
	@Override
	protected void addComplexPattern(EntityValueTranslator t,ComplexPattern pattern) {
		if (pattern.pattern.contains("{preposition}")) {
			EntityObject prep = t.getEntityObject(getLanguage(),BaseConfiguration.TYPE_PREPOSITION);
			if (counter >= prep.getExternalValues().size()) {
				counter = 0;
			}
			int i = 0;
			for (String pre: prep.getExternalValues().keySet()) {
				if (i==counter) {
					ZStringSymbolParser ptn = new ZStringSymbolParser(pattern.pattern);
					ptn.replace("{preposition}",pre);
					addExample(ptn,"");
					break;
				}
				i++;
			}
			counter++;
		} else {
			addExample(pattern.pattern,"");
		}
	}
	*/
}
