package nl.zeesoft.zdbd.api.html.select;

import java.util.List;

public class LoadTheme extends SelectTheme {
	public LoadTheme(List<String> names) {
		super("Select a theme to load", names);
		onOkClick = "menu.loadTheme();";
	}
}
