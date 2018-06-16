package nl.zeesoft.zsmc;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zsmc.pattern.EntityObject;
import nl.zeesoft.zsmc.pattern.UniversalAlphabetic;
import nl.zeesoft.zsmc.pattern.UniversalNumeric;
import nl.zeesoft.zsmc.pattern.UniversalTime;

public class EntityValueTranslator {
	private List<EntityObject>					entities			= new ArrayList<EntityObject>();

	public EntityValueTranslator() {
		addDefaultEntities();
	}
	
	public String getValueConcatenator() {
		return ":";
	}

	public String getOrConcatenator() {
		return "|";
	}

	public List<EntityObject> getEntities() {
		return entities;
	}
	
	public void initialize() {
		for (EntityObject eo: entities) {
			eo.initialize(getValueConcatenator());
		}
	}

	public void addDefaultEntities() {
		entities.add(new UniversalTime());
		entities.add(new UniversalNumeric());
		entities.add(new UniversalAlphabetic());
	}
}
