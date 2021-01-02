package nl.zeesoft.zdbd.api.html.form;

public class NewTheme extends FormHtml {
	public NewTheme(String name, int beatsPerMinute) {
		onOkClick = "menu.newTheme();";
		onCancelClick = "modal.hide();";
		addProperty("themeName", "Name", name);
		addProperty("themeBPM", "Beats per minute", beatsPerMinute, FormProperty.NUMBER_INPUT);
	}
}
