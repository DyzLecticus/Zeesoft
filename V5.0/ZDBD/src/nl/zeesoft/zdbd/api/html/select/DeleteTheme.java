package nl.zeesoft.zdbd.api.html.select;

import java.util.List;

public class DeleteTheme extends SelectTheme {
	public DeleteTheme(List<String> names) {
		super("Select a theme to delete", names);
		onOkClick = "menu.deleteTheme();";
	}
}
