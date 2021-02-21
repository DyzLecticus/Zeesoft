package nl.zeesoft.zdbd.api.html.form;

import nl.zeesoft.zdbd.api.html.FormHtml;

public class SaveThemeAs extends FormHtml {
	public SaveThemeAs(String value) {
		onOkClick = "menu.saveThemeAs();";
		onCancelClick = "modal.hide();";
		addProperty("saveThemeAs", "Save theme as", value);
	}
}
