package nl.zeesoft.zidm.dialog;

import nl.zeesoft.zdm.model.Model;
import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zspr.Language;

public class ModelDialogFactory {
	public static final String Modelling				= "Modelling";
	
	public static final String classNameSingle			= "classNameSingle";
	public static final String classNameMulti			= "classNameMulti";
	public static final String extendedClassNameSingle	= "extendedClassNameSingle";
	public static final String extendedClassNameMulti	= "extendedClassNameMulti";
	public static final String propertyNameSingle		= "propertyNameSingle";
	public static final String propertyNameMulti		= "propertyNameMulti";
	
	public static final String defaultAnswer			= "Interesting.";
	
	public Dialog getEnglishModelDialog(Model model) {
		Dialog dialog = new ModelDialog(Modelling,Language.ENG,ModelDialogController.class.getName(),model);
		dialog.addExample("A {" + classNameSingle + "} is a {" + extendedClassNameSingle + "}.",defaultAnswer);
		dialog.addExample("A {" + classNameSingle + "} is an {" + extendedClassNameSingle + "}.",defaultAnswer);
		dialog.addExample("An {" + classNameSingle + "} is a {" + extendedClassNameSingle + "}.",defaultAnswer);
		dialog.addExample("An {" + classNameSingle + "} is an {" + extendedClassNameSingle + "}.",defaultAnswer);
		dialog.addExample("A {" + classNameSingle + "} is a kind of {" + extendedClassNameSingle + "}.",defaultAnswer);
		dialog.addExample("An {" + classNameSingle + "} is a kind of {" + extendedClassNameSingle + "}.",defaultAnswer);
		dialog.addExample("{" + classNameMulti + "} are {" + extendedClassNameMulti + "}.",defaultAnswer);
		dialog.addExample("{" + classNameMulti + "} are a kind of {" + extendedClassNameMulti + "}.",defaultAnswer);

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
		
		return dialog;
	}
}
