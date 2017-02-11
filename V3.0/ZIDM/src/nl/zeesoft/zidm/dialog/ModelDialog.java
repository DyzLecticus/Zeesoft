package nl.zeesoft.zidm.dialog;

import nl.zeesoft.zdm.model.Model;
import nl.zeesoft.zid.dialog.Dialog;

public class ModelDialog extends Dialog {
	private Model model = null;
	
	public ModelDialog(String name, String languageCode, String controllerClassName,Model model) {
		super(name, languageCode, controllerClassName);
		this.model = model;
	}

	public Model getModel() {
		return model;
	}
}
