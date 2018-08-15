package nl.zeesoft.zsd.util;

import java.util.List;
import java.util.Map.Entry;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.BaseConfiguration;
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
	 * @param entities The list of entities
	 * @param context The optional context symbol
	 * @return The JSON file
	 */
	public JsFile getJsonForEntities(List<EntityObject> entities,String context) {
		return getJsonForEntities(entities,context,false);
	}
	
	/**
	 * Returns the JSON for the specified entities.
	 * 
	 * @param entities The list of entities
	 * @param context The optional context symbol
	 * @param languageContext Indicates the entity language is to be used for context
	 * @return The JSON file
	 */
	public JsFile getJsonForEntities(List<EntityObject> entities,String context,boolean languageContext) {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		JsElem seqsElem = new JsElem("sequences",true);
		json.rootElement.children.add(seqsElem);
		addJsonForEntities(seqsElem,entities,context,languageContext);
		return json;
	}
	
	/**
	 * Adds the JSON for the specified entities.
	 * 
	 * @param parent The parent element to add JSON to
	 * @param entities The list of entities
	 * @param context The optional context symbol
	 * @param languageContext Indicates the entity language is to be used for context
	 */
	public void addJsonForEntities(JsElem parent,List<EntityObject> entities,String context,boolean languageContext) {
		String cntxt = context;
		for (EntityObject eo: entities) {
			if (!eo.getLanguage().equals(BaseConfiguration.LANG_UNI)) {
				for (String extra: eo.getToJsonExtras()) {
					if (languageContext) {
						cntxt = eo.getLanguage();
					} else if (context.length()==0) {
						cntxt = eo.getType();
					}
					ZStringBuilder input = new ZStringBuilder(upperCaseFirst(extra));
					input.append(".");
					TsvToJson.addSequenceElement(parent,input,null,new ZStringBuilder(cntxt));
				}
				for (Entry<String,EntityValue> entry: eo.getExternalValues().entrySet()) {
					if (languageContext) {
						cntxt = eo.getLanguage();
					} else if (context.length()==0) {
						cntxt = eo.getType();
					}
					ZStringBuilder input = null;
					if (eo.getToJsonPrefixes().size()==0 && eo.getToJsonSuffixes().size()==0) {
						input = new ZStringBuilder(upperCaseFirst(entry.getValue().externalValue));
						input.append(".");
						TsvToJson.addSequenceElement(parent,input,null,new ZStringBuilder(cntxt));
					}
					for (String prefix: eo.getToJsonPrefixes()) {
						input = new ZStringBuilder(upperCaseFirst(prefix));
						input.append(" ");
						input.append(entry.getValue().externalValue);
						input.append(".");
						TsvToJson.addSequenceElement(parent,input,null,new ZStringBuilder(cntxt));
					}
					for (String suffix: eo.getToJsonSuffixes()) {
						input = new ZStringBuilder(upperCaseFirst(entry.getValue().externalValue));
						input.append(" ");
						input.append(suffix);
						input.append(".");
						TsvToJson.addSequenceElement(parent,input,null,new ZStringBuilder(cntxt));
					}
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
						TsvToJson.checkAddSequenceElement(parent,
							ptn,
							null,
							new ZStringBuilder(cntxt)
							);
					}
				}
			}
		}
	}
	
	private String upperCaseFirst(String str) {
		String r = str.substring(0,1).toUpperCase();
		if (str.length()>1) {
			r += str.substring(1).toLowerCase();
		}
		return r;
	}
}
