package nl.zeesoft.zdbd.api.html.form;

import nl.zeesoft.zdbd.midi.Arpeggiator;

public class ArpeggiatorEditor extends AbstractEditor {
	public ArpeggiatorEditor(Arpeggiator arpeggiator, String prevName, String nextName) {
		cancelLabel = "Done";
		onCancelClick = "generators.editDone();";
		
		addProperty("name", "Name", arpeggiator.name, FormProperty.TEXT_INPUT);
		addProperty("minDuration", "Minimum duration", arpeggiator.minDuration, FormProperty.NUMBER_INPUT);
		addProperty("maxDuration", "Maximum duration", arpeggiator.maxDuration, FormProperty.NUMBER_INPUT);
		addProperty("density", "Density", arpeggiator.density, FormProperty.ANY_INPUT);
		addProperty("maxOctave", "Maximum octave", arpeggiator.maxOctave, FormProperty.NUMBER_INPUT);
		addProperty("maxInterval", "Maximum interval", arpeggiator.maxInterval, FormProperty.NUMBER_INPUT);
		addProperty("maxSteps", "Maximum steps", arpeggiator.maxSteps, FormProperty.NUMBER_INPUT);
		for (FormProperty property: properties) {
			property.onChange = "arpeggiators.propertyChange(this);";
		}
		
		this.prevName = prevName;
		this.nextName = nextName;
		this.function = "arpeggiators.edit";
	}
}
