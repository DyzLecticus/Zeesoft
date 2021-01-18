package nl.zeesoft.zdbd.api.html.form;

import nl.zeesoft.zdbd.neural.Generator;

public class AddGenerator extends GeneratorEditor {
	public AddGenerator() {
		super(new Generator(),"","");
		okLabel = "Add";
		onOkClick = "generators.addDone();";
		onCancelClick = "generators.addCancel();";
		for (FormProperty property: properties) {
			property.onChange = "";
		}
	}
}
