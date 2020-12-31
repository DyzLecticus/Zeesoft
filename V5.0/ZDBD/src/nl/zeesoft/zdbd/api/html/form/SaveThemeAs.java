package nl.zeesoft.zdbd.api.html.form;

public class SaveThemeAs extends FormHtml{
	public SaveThemeAs(String value) {
		onOkClick = "menu.saveThemeAs();";
		onCancelClick = "modal.hide();";
		addProperty("saveThemeAs", "Save theme as", value);
	}
}
