package nl.zeesoft.zid.dialog.io;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zid.dialog.DialogExample;
import nl.zeesoft.zid.dialog.DialogVariable;
import nl.zeesoft.zid.dialog.DialogVariableExample;

/**
 * DialogJson provides methods to convert Dialog objects to and from JSON.
 */
public class DialogJson {
	
	public JsFile toJson(Dialog dialog) {
		JsFile file = new JsFile();
		JsElem dElem = new JsElem("dialog");
		file.rootElement = dElem;
		dElem.children.add(new JsElem("name",dialog.getName(),true));
		dElem.children.add(new JsElem("languageCode",dialog.getLanguage().getCode(),true));
		dElem.children.add(new JsElem("controllerClassName",dialog.getControllerClassName(),true));

		JsElem exElem = new JsElem("examples",true);
		dElem.children.add(exElem);
		for (DialogExample example: dialog.getExamples()) {
			JsElem oElem = new JsElem("example");
			exElem.children.add(oElem);
			oElem.children.add(new JsElem("input",example.getInput(),true));
			oElem.children.add(new JsElem("output",example.getInput(),true));
		}
		
		JsElem vElem = new JsElem("variables",true);
		dElem.children.add(vElem);
		for (DialogVariable variable: dialog.getVariables()) {
			JsElem oElem = new JsElem("variable");
			vElem.children.add(oElem);
			oElem.children.add(new JsElem("name",variable.getName(),true));
			oElem.children.add(new JsElem("type",variable.getType(),true));
			
			JsElem vexElem = new JsElem("examples",true);
			oElem.children.add(vexElem);
			for (DialogVariableExample example: variable.getExamples()) {
				JsElem obElem = new JsElem("example");
				vexElem.children.add(obElem);
				obElem.children.add(new JsElem("question",example.getQuestion(),true));
				obElem.children.add(new JsElem("answer",example.getAnswer(),true));
			}
		}
		return file;
	}
	
	public Dialog fromJson(JsFile file) {
		Dialog dialog = null;
		if (file.rootElement!=null) {
			String name = "";
			String languageCode = "";
			String controllerClassName = "";
			JsElem exElem = null;
			JsElem vElem = null;
			for (JsElem cElem: file.rootElement.children) {
				if (cElem.name.equals("name")) {
					name = cElem.value.toString();
				} else if (cElem.name.equals("languageCode")) {
					languageCode = cElem.value.toString();
				} else if (cElem.name.equals("controllerClassName")) {
					controllerClassName = cElem.value.toString();
				} else if (cElem.name.equals("examples")) {
					exElem = cElem;
				} else if (cElem.name.equals("variables")) {
					vElem = cElem;
				}
			}
			if (name.length()>0 && languageCode.length()>0 && controllerClassName.length()>0) {
				dialog = new Dialog(name,languageCode,controllerClassName);
				if (exElem!=null) {
					for (JsElem oElem: exElem.children) {		
						String input = oElem.getChildByName("input").value.toString();
						String output = oElem.getChildByName("output").value.toString();
						dialog.addExample(input, output);
					}
				}
				if (vElem!=null) {
					for (JsElem oElem: vElem.children) {		
						String n = oElem.getChildByName("name").value.toString();
						String type = oElem.getChildByName("type").value.toString();
						dialog.addVariable(n,type);
						JsElem vexElem = oElem.getChildByName("examples");
						for (JsElem obElem: vexElem.children) {
							String question = obElem.getChildByName("question").value.toString();
							String answer = obElem.getChildByName("answer").value.toString();
							dialog.addVariableExample(n, question, answer);
						}
					}
				}
			}
		}
		return dialog;
	}
	
}
