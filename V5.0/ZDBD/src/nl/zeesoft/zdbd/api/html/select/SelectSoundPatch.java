package nl.zeesoft.zdbd.api.html.select;

import java.util.List;

import nl.zeesoft.zdbd.api.html.SelectHtml;

public class SelectSoundPatch extends SelectHtml {
	public SelectSoundPatch(List<String> names) {
		super("Select a sound patch to load", "selectSoundPatch");
		onOkClick = "soundpatch.loadSoundPatch();";
		onCancelClick = "modal.hide();";
		for (String name: names) {
			addOption(name);
		}
	}
}
