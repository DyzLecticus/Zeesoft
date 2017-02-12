package nl.zeesoft.zidm.dialog;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zidm.dialog.model.DialogModel;
import nl.zeesoft.zidm.dialog.model.DialogModelNameTranslator;
import nl.zeesoft.zidm.dialog.pattern.IndefiniteArticleEnglish;
import nl.zeesoft.zidm.dialog.pattern.ModelPatternManager;
import nl.zeesoft.zspr.Language;
import nl.zeesoft.zspr.pattern.PatternObject;

public class ModelDialogFactory {
	public static final String Modelling				= "Modelling";

	public static final String classInArt				= "classNameInArt";
	public static final String classNameSingle			= "classNameSingle";
	public static final String classNameMulti			= "classNameMulti";
	
	public static final String extendedClassInArt		= "extendedClassInArt";
	public static final String extendedClassNameSingle	= "extendedClassNameSingle";
	public static final String extendedClassNameMulti	= "extendedClassNameMulti";
	
	public static final String propertyInArt			= "propertyInArt";
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
		return new DialogModelNameTranslator();
	}
	
	public List<Dialog> getModelDialogs(DialogModel model) {
		List<Dialog> dialogs = new ArrayList<Dialog>();
		dialogs.add(getEnglishModelDialog(model));
		return dialogs;
	}

	public ModelPatternManager getModelPatternManager() {
		return new ModelPatternManager();
	}

	public Dialog getEnglishModelDialog(DialogModel model) {
		Dialog dialog = new ModelDialog(Modelling,Language.ENG,ModelDialogController.class.getName(),model);
		dialog.addExample("{" + classInArt + "} {" + classNameSingle + "} is {" + extendedClassInArt + "} {" + extendedClassNameSingle + "}.",defaultAnswer);
		dialog.addExample("{" + classInArt + "} {" + classNameSingle + "} is {" + extendedClassInArt + "} kind of {" + extendedClassNameSingle + "}.",defaultAnswer);
		dialog.addExample("{" + classNameMulti + "} are {" + extendedClassNameMulti + "}.",defaultAnswer);
		dialog.addExample("{" + classNameMulti + "} are {" + extendedClassInArt + "} kind of {" + extendedClassNameMulti + "}.",defaultAnswer);
		dialog.addExample("{" + classInArt + "} {" + classNameSingle + "} has {" + propertyInArt + "} {" + propertyNameSingle + "}.",defaultAnswer);
		dialog.addExample("{" + classNameMulti + "} have {" + propertyNameMulti + "}.",defaultAnswer);
		dialog.addExample("{" + classNameMulti + "} have {" + propertyInArt + "} {" + propertyNameSingle + "}.",defaultAnswer);

		dialog.addExample("{" + classNameMulti + "} is the plural for {" + classNameSingle + "}.",defaultAnswer);
		dialog.addExample("{" + classNameSingle + "} is the singular for {" + classNameMulti + "}.",defaultAnswer);
		dialog.addExample("{" + classNameMulti + "} is plural for {" + classNameSingle + "}.",defaultAnswer);
		dialog.addExample("{" + classNameSingle + "} is singular for {" + classNameMulti + "}.",defaultAnswer);
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
				propertyAttachmentS = " that has {" + propertyInArt + "} {" + propertyNameSingle + "}";
				propertyAttachmentM = " that have {" + propertyInArt + "} {" + propertyNameSingle + "}";
			} else if (i==5) {
				propertyAttachmentS = " that has {" + propertyInArt + "} {" + propertyNameSingle + "}";
				propertyAttachmentM = " that have {" + propertyInArt + "} {" + propertyNameSingle + "}";
			} else if (i==4) {
				propertyAttachmentS = " with a {" + propertyNameSingle + "}";
				propertyAttachmentM = " with a {" + propertyNameSingle + "}";
			} else if (i==5) {
				propertyAttachmentS = " with an {" + propertyNameSingle + "}";
				propertyAttachmentM = " with an {" + propertyNameSingle + "}";
			}
			dialog.addExample("{" + classInArt + "} {" + classNameSingle + "} is {" + extendedClassInArt + "} {" + extendedClassNameSingle + "}" + propertyAttachmentS + ".",defaultAnswer);
			dialog.addExample("{" + classInArt + "} {" + classNameSingle + "} is {" + extendedClassInArt + "} kind of {" + extendedClassNameSingle + "}" + propertyAttachmentS + ".",defaultAnswer);
			dialog.addExample("{" + classNameMulti + "} are {" + extendedClassNameMulti + "}" + propertyAttachmentM + ".",defaultAnswer);
			dialog.addExample("{" + classNameMulti + "} are {" + extendedClassInArt + "} kind of {" + extendedClassNameSingle + "}" + propertyAttachmentM + ".",defaultAnswer);
			dialog.addExample("{" + classInArt + "} {" + classNameSingle + "} has {" + propertyInArt + "} {" + propertyNameSingle + "}.",defaultAnswer);
			dialog.addExample("{" + classNameMulti + "} have {" + propertyNameMulti + "}.",defaultAnswer);
			dialog.addExample("{" + classNameMulti + "} have {" + propertyInArt + "} {" + propertyNameSingle + "}.",defaultAnswer);
		}

		dialog.addVariable(classInArt,IndefiniteArticleEnglish.TYPE);
		dialog.addVariable(classNameSingle,PatternObject.TYPE_ALPHABETIC);
		dialog.addVariable(classNameMulti,PatternObject.TYPE_ALPHABETIC);
		
		dialog.addVariable(extendedClassInArt,IndefiniteArticleEnglish.TYPE);
		dialog.addVariable(extendedClassNameSingle,PatternObject.TYPE_ALPHABETIC);
		dialog.addVariable(extendedClassNameMulti,PatternObject.TYPE_ALPHABETIC);
		
		dialog.addVariable(propertyInArt,IndefiniteArticleEnglish.TYPE);
		dialog.addVariable(propertyNameSingle,PatternObject.TYPE_ALPHABETIC);
		dialog.addVariable(propertyNameMulti,PatternObject.TYPE_ALPHABETIC);

		return dialog;
	}
}
