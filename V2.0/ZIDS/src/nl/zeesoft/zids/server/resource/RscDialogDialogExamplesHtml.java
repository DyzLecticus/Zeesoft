package nl.zeesoft.zids.server.resource;


public class RscDialogDialogExamplesHtml extends RscDialogDialogsHtml {
	public RscDialogDialogExamplesHtml(String title, String backgroundColor) {
		super(title, backgroundColor);
	}

	@Override
	protected boolean showExamples() {
		return true;
	}

	@Override
	protected String getLinkHtml() {
		return "<a href=\"../dialogs/dialogs.html\">Hide examples</a>";
	}
}
