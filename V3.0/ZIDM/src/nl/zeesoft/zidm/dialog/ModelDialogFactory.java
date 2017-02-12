package nl.zeesoft.zidm.dialog;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zspr.Language;
import nl.zeesoft.zspr.pattern.PatternObject;

public class ModelDialogFactory {
	public static final String Modelling				= "Modelling";
	
	public static final String classNameSingle			= "classNameSingle";
	public static final String classNameMulti			= "classNameMulti";
	public static final String extendedClassNameSingle	= "extendedClassNameSingle";
	public static final String extendedClassNameMulti	= "extendedClassNameMulti";
	public static final String propertyNameSingle		= "propertyNameSingle";
	public static final String propertyNameMulti		= "propertyNameMulti";
	
	public static final String defaultAnswer			= "Interesting.";

	public DialogModel getDialogModel() {
		DialogModel model = new DialogModel(getDialogModelNameTranslator());
		model.initializeBaseModel();
		model.getTranslator().addTranslation(model.getBaseClassName(),Language.ENG,true,model.getBaseClassName());
		model.getTranslator().addTranslation(model.getBaseClassName(),Language.ENG,false,model.getBaseClassName() + "s");
		model.getTranslator().addTranslation(model.getBaseClassName(),Language.NLD,true,model.getBaseClassName());
		model.getTranslator().addTranslation(model.getBaseClassName(),Language.NLD,false,model.getBaseClassName() + "en");
		return model;
	}
	
	public DialogModelNameTranslator getDialogModelNameTranslator() {
		DialogModelNameTranslator translator = new DialogModelNameTranslator();
		return translator;
	}
	
	public List<Dialog> getModelDialogs() {
		List<Dialog> dialogs = new ArrayList<Dialog>();
		DialogModel model = getDialogModel();
		dialogs.add(getEnglishModelDialog(model));
		return dialogs;
	}

	public Dialog getEnglishModelDialog(DialogModel model) {
		Dialog dialog = new ModelDialog(Modelling,Language.ENG,ModelDialogController.class.getName(),model);
		dialog.addExample("A {" + classNameSingle + "} is a {" + extendedClassNameSingle + "}.",defaultAnswer);
		dialog.addExample("A {" + classNameSingle + "} is an {" + extendedClassNameSingle + "}.",defaultAnswer);
		dialog.addExample("An {" + classNameSingle + "} is a {" + extendedClassNameSingle + "}.",defaultAnswer);
		dialog.addExample("An {" + classNameSingle + "} is an {" + extendedClassNameSingle + "}.",defaultAnswer);
		dialog.addExample("A {" + classNameSingle + "} is a kind of {" + extendedClassNameSingle + "}.",defaultAnswer);
		dialog.addExample("An {" + classNameSingle + "} is a kind of {" + extendedClassNameSingle + "}.",defaultAnswer);
		dialog.addExample("{" + classNameMulti + "} are {" + extendedClassNameMulti + "}.",defaultAnswer);
		dialog.addExample("{" + classNameMulti + "} are a kind of {" + extendedClassNameMulti + "}.",defaultAnswer);
		
		dialog.addExample("{" + classNameMulti + "} have {" + propertyNameMulti + "}.",defaultAnswer);
		dialog.addExample("{" + classNameMulti + "} have a {" + propertyNameSingle + "}.",defaultAnswer);

		dialog.addExample("{" + classNameMulti + "} is the plural of {" + classNameSingle + "}.",defaultAnswer);
		dialog.addExample("{" + classNameSingle + "} is the singular of {" + classNameMulti + "}.",defaultAnswer);
		dialog.addExample("{" + classNameMulti + "} is the plural form of {" + classNameSingle + "}.",defaultAnswer);
		dialog.addExample("{" + classNameSingle + "} is the singular form of {" + classNameMulti + "}.",defaultAnswer);
		dialog.addExample("{" + classNameMulti + "} is the plural of the word {" + classNameSingle + "}.",defaultAnswer);
		dialog.addExample("{" + classNameSingle + "} is the singular of the word {" + classNameMulti + "}.",defaultAnswer);
		dialog.addExample("{" + classNameMulti + "} is the plural form of the word {" + classNameSingle + "}.",defaultAnswer);
		dialog.addExample("{" + classNameSingle + "} is the singular form of the word {" + classNameMulti + "}.",defaultAnswer);

		for (int i = 0; i < 6; i++) {
			String propertyAttachmentS = " with {" + propertyNameMulti + "}";
			String propertyAttachmentM = " with {" + propertyNameMulti + "}";
			if (i==1) {
				propertyAttachmentS = " with multiple {" + propertyNameMulti + "}";
				propertyAttachmentM = " with multiple {" + propertyNameMulti + "}";
			} else if (i==2) {
				propertyAttachmentS = " with one or more {" + propertyNameMulti + "}";
				propertyAttachmentM = " with one or more {" + propertyNameMulti + "}";
			} else if (i==3) {
				propertyAttachmentS = " that has {" + propertyNameMulti + "}";
				propertyAttachmentM = " that have {" + propertyNameMulti + "}";
			} else if (i==4) {
				propertyAttachmentS = " that has a {" + propertyNameSingle + "}";
				propertyAttachmentM = " that have a {" + propertyNameSingle + "}";
			} else if (i==5) {
				propertyAttachmentS = " that has an {" + propertyNameSingle + "}";
				propertyAttachmentM = " that have an {" + propertyNameSingle + "}";
			} else if (i==4) {
				propertyAttachmentS = " with a {" + propertyNameSingle + "}";
				propertyAttachmentM = " with a {" + propertyNameSingle + "}";
			} else if (i==5) {
				propertyAttachmentS = " with an {" + propertyNameSingle + "}";
				propertyAttachmentM = " with an {" + propertyNameSingle + "}";
			}
			dialog.addExample("A {" + classNameSingle + "} is a {" + extendedClassNameSingle + "}" + propertyAttachmentS + ".",defaultAnswer);
			dialog.addExample("A {" + classNameSingle + "} is an {" + extendedClassNameSingle + "}" + propertyAttachmentS + ".",defaultAnswer);
			dialog.addExample("An {" + classNameSingle + "} is a {" + extendedClassNameSingle + "}" + propertyAttachmentS + ".",defaultAnswer);
			dialog.addExample("An {" + classNameSingle + "} is an {" + extendedClassNameSingle + "}" + propertyAttachmentS + ".",defaultAnswer);
			dialog.addExample("A {" + classNameSingle + "} is a kind of {" + extendedClassNameSingle + "}" + propertyAttachmentS + ".",defaultAnswer);
			dialog.addExample("An {" + classNameSingle + "} is a kind of {" + extendedClassNameSingle + "}" + propertyAttachmentS + ".",defaultAnswer);
			dialog.addExample("{" + classNameMulti + "} are {" + extendedClassNameMulti + "}" + propertyAttachmentM + ".",defaultAnswer);
			dialog.addExample("{" + classNameMulti + "} are a kind of {" + extendedClassNameSingle + "}" + propertyAttachmentM + ".",defaultAnswer);
		}
		
		dialog.addVariable(classNameSingle,PatternObject.TYPE_ALPHABETIC);
		dialog.addVariable(classNameMulti,PatternObject.TYPE_ALPHABETIC);
		dialog.addVariable(extendedClassNameSingle,PatternObject.TYPE_ALPHABETIC);
		dialog.addVariable(extendedClassNameMulti,PatternObject.TYPE_ALPHABETIC);
		dialog.addVariable(propertyNameSingle,PatternObject.TYPE_ALPHABETIC);
		dialog.addVariable(propertyNameMulti,PatternObject.TYPE_ALPHABETIC);

		return dialog;
	}
}
