package nl.zeesoft.zdbd.api.html.select;

import java.util.List;

import nl.zeesoft.zdbd.api.html.SelectHtml;

public class SelectTheme extends SelectHtml {
	public SelectTheme(String title, List<String> names) {
		super(title, "selectTheme");
		onOkClick = "menu.selectTheme();";
		onCancelClick = "modal.hide();";
		for (String name: names) {
			addOption(name);
		}
	}
}
