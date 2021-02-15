package nl.zeesoft.zdbd.api.html.form;

import java.util.List;

import nl.zeesoft.zdbd.midi.EchoConfig;

public class EchoEditor extends AbstractEditor {
	public EchoEditor(String name, List<EchoConfig> echos, String prevName, String nextName) {
		cancelLabel = "Done";
		onCancelClick = "soundpatch.editDone();";
		
		addProperty("instrumentName", "Instrument", name, FormProperty.TEXT);
		
		int i = 0;
		for (EchoConfig echo: echos) {
			addEchoProperties(echo,i);
			i++;
		}
		
		for (FormProperty property: properties) {
			property.onChange = "soundpatch.propertyChange(this);";
		}
		
		this.prevName = prevName;
		this.nextName = nextName;
		this.function = "soundpatch.edit";
	}
	
	protected void addEchoProperties(EchoConfig echo, int index) {
		String suffix = "-" + index;
		addProperty("echo-number" + suffix, "Number", "" + (index + 1), FormProperty.TEXT);
		addProperty("echo-active" + suffix, "Active", echo.active, FormProperty.CHECKBOX_INPUT);
		addProperty("echo-sourceChannel" + suffix, "Instrument/layer", LfoEditor.getLfoChannels(), FormProperty.SELECT, LfoEditor.getNameForChannel(echo.sourceChannel));
		addProperty("echo-delay" + suffix, "Delay", echo.delay, FormProperty.NUMBER_INPUT);
		addProperty("echo-pan" + suffix, "Pan", echo.pan, FormProperty.NUMBER_INPUT);
		addProperty("echo-velocity" + suffix, "Velocity", echo.velocity, FormProperty.ANY_INPUT);
		addProperty("echo-filter" + suffix, "Filter", echo.filter, FormProperty.ANY_INPUT);
		addProperty("echo-reverb" + suffix, "Reverb", echo.reverb, FormProperty.ANY_INPUT);
		addProperty("echo-chorus" + suffix, "Chorus", echo.chorus, FormProperty.ANY_INPUT);
	}
}
