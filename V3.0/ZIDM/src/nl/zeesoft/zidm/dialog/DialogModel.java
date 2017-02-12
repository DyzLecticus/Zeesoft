package nl.zeesoft.zidm.dialog;

import nl.zeesoft.zdm.model.Model;
import nl.zeesoft.zdm.model.transformations.impl.AddClass;
import nl.zeesoft.zdm.model.transformations.impl.AddPackage;
import nl.zeesoft.zdm.model.transformations.impl.AddProperty;

public class DialogModel extends Model {
	private String						basePackageName		= "nl.zeesoft.zidm";
	private String						baseClassName		= "Object";
	private String						baseIdPropertyName	= "id";
	private DialogModelNameTranslator	translator			= null;
	
	public DialogModel(DialogModelNameTranslator translator) {
		this.translator = translator;
	}

	public DialogModel(String basePackageName,String baseClassName,String baseIdPropertyName,DialogModelNameTranslator translator) {
		this.basePackageName = basePackageName;
		this.baseClassName = baseClassName;
		this.baseIdPropertyName = baseIdPropertyName;
		this.translator = translator;
	}

	public void initializeBaseModel() {
		applyTransformation(new AddPackage(basePackageName));
		applyTransformation(new AddClass(basePackageName,baseClassName));
		applyTransformation(new AddProperty(basePackageName,baseClassName,"id"));
	}
	
	public DialogModelNameTranslator getTranslator() {
		return translator;
	}

	public String getBasePackageName() {
		return basePackageName;
	}

	public String getBaseClassName() {
		return baseClassName;
	}

	public String getBaseIdPropertyName() {
		return baseIdPropertyName;
	}
}
