package nl.zeesoft.zdbd.api.html.form;

public class NewTheme extends FormHtml {
	public NewTheme(String name, int beatsPerMinute, float shufflePercentage) {
		onOkClick = "menu.newTheme();";
		onCancelClick = "modal.hide();";
		addProperty("themeName", "New theme name", name);
		addProperty("themeBPM", "Beats per minute", beatsPerMinute, FormProperty.NUMBER_INPUT);
		addProperty("themeShuffle", "Shuffle", shufflePercentage, FormProperty.ANY_INPUT);
	}
}
