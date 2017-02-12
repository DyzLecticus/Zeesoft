package nl.zeesoft.zidm.dialog;

import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zidm.dialog.model.DialogModel;

public class ModelDialog extends Dialog {
	private DialogModel model = null;
	
	public ModelDialog(String name, String languageCode, String controllerClassName,DialogModel model) {
		super(name, languageCode, controllerClassName);
		this.model = model;
	}

	public DialogModel getModel() {
		return model;
	}
}
