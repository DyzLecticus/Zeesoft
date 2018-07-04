package nl.zeesoft.zsd.util;

import java.util.List;
import java.util.Map.Entry;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.entity.EntityObject;
import nl.zeesoft.zsd.entity.EntityValue;
import nl.zeesoft.zsd.entity.complex.ComplexObject;
import nl.zeesoft.zsd.entity.complex.ComplexPattern;

/**
 * An EntityToJson instance can be used to translate a set of entity objects into a JSON file.
 */
public class EntityToJson {
	
	/**
	 * Returns the JSON for the specified entities.
	 * 
	 * @param entities The list of entities.
	 * @param context The optional context symbol.
	 * @return The JSON file
	 */
	public JsFile getJsonForEntities(List<EntityObject> entities,String context) {
		return getJsonForEntities(entities,context,false);
	}
	
	/**
	 * Returns the JSON for the specified entities.
	 * 
	 * @param entities The list of entities.
	 * @param context The optional context symbol.
	 * @param languageContext Indicates the entity language is to be used for context.
	 * @return The JSON file
	 */
	public JsFile getJsonForEntities(List<EntityObject> entities,String context,boolean languageContext) {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		String cntxt = context;
		for (EntityObject eo: entities) {
			for (Entry<String,EntityValue> entry: eo.getExternalValues().entrySet()) {
				if (languageContext) {
					cntxt = eo.getLanguage();
				} else if (context.length()==0) {
					cntxt = eo.getType();
				}
				TsvToJson.addSequenceElement(json.rootElement,
					new ZStringBuilder(entry.getValue().externalValue + "."),
					null,
					new ZStringBuilder(cntxt)
					);
			}
			if (eo instanceof ComplexObject) {
				ComplexObject co = (ComplexObject) eo;
				for (ComplexPattern pattern: co.getPatterns()) {
					if (languageContext) {
						cntxt = eo.getLanguage();
					} else if (context.length()==0) {
						if (context.length()==0) {
							if (pattern.context.length()>0) {
								cntxt = pattern.context;
							} else {
								cntxt = eo.getType();
							}
						}
					}
					ZStringSymbolParser ptn = new ZStringSymbolParser(pattern.pattern);
					if (!ptn.containsOneOfCharacters(" ") && !ZStringSymbolParser.endsWithLineEndSymbol(ptn)) {
						ptn.append(".");
					}
					TsvToJson.addSequenceElement(json.rootElement,
						new ZStringBuilder(ptn),
						null,
						new ZStringBuilder(cntxt)
						);
				}
			}
		}
		return json;
	}

}
