package nl.zeesoft.zdbd.api.html.form;

import nl.zeesoft.zdbd.midi.Arpeggiator;

public class AddArpeggiator extends ArpeggiatorEditor {
	public AddArpeggiator() {
		super(new Arpeggiator(),"","");
		okLabel = "Add";
		onOkClick = "arpeggiators.addDone();";
		onCancelClick = "arpeggiators.addCancel();";
		for (FormProperty property: properties) {
			property.onChange = "";
		}
	}
}
