package nl.zeesoft.zdbd.api.html.form;

import java.util.List;

import nl.zeesoft.zdbd.midi.SynthChannelConfig;
import nl.zeesoft.zdbd.midi.convertors.BassConvertor;
import nl.zeesoft.zdbd.midi.convertors.DrumConvertor;
import nl.zeesoft.zdbd.midi.convertors.DrumSampleConvertor;
import nl.zeesoft.zdbd.midi.convertors.InstrumentConvertor;
import nl.zeesoft.zdbd.midi.convertors.SynthLayerConvertor;

public class InstrumentEditor extends AbstractEditor {
	public InstrumentEditor(String name, SynthChannelConfig layer1, SynthChannelConfig layer2,
		List<InstrumentConvertor> convertors, String prevName, String nextName
		) {
		cancelLabel = "Done";
		onCancelClick = "soundpatch.editDone();";
		
		SynthLayerConvertor layer1Conv = null;
		SynthLayerConvertor layer2Conv = null;
		BassConvertor bc = null;
		if (convertors.size()==1) {
			InstrumentConvertor conv = convertors.get(0);
			if (conv instanceof BassConvertor) {
				bc = (BassConvertor) conv;
				layer1Conv = bc.layers.get(0);
				if (layer2!=null && bc.layers.size()>1) {
					layer2Conv = bc.layers.get(1);
				}
			}
		}
		
		addProperty("instrumentName", "Instrument", name, FormProperty.TEXT);
		if (bc!=null) {
			addProperty("conv-hold", "Hold", bc.hold, FormProperty.ANY_INPUT);
		}
		addSynthChannelConfigProperties(layer1,layer1Conv,0);
		if (layer2!=null) {
			addSynthChannelConfigProperties(layer2,layer2Conv,1);
		}
		if (convertors.size()>1) {
			for (InstrumentConvertor conv: convertors) {
				if (conv instanceof DrumConvertor) {
					addDrumConvertorProperties((DrumConvertor) conv);
				}
			}
		}
		
		for (FormProperty property: properties) {
			property.onChange = "soundpatch.propertyChange(this);";
		}
		
		this.prevName = prevName;
		this.nextName = nextName;
		this.function = "soundpatch.edit";
	}
	
	protected void addSynthChannelConfigProperties(SynthChannelConfig layer, SynthLayerConvertor layerConv, int num) {
		String suffix = "-" + num;
		addProperty("layer" + suffix, "Layer", "" + (num + 1), FormProperty.TEXT);
		if (layerConv!=null) {
			addProperty("conv-baseOctave" + suffix, "Base octave", layerConv.baseOctave, FormProperty.NUMBER_INPUT);
			addProperty("conv-velocity" + suffix, "Velocity", layerConv.velocity, FormProperty.NUMBER_INPUT);
			addProperty("conv-accentVelocity" + suffix, "Accent velocity", layerConv.accentVelocity, FormProperty.NUMBER_INPUT);
		}
		addProperty("instrument" + suffix, "Instrument", layer.instrument, FormProperty.NUMBER_INPUT);
		addProperty("volume" + suffix, "Volume", layer.volume, FormProperty.NUMBER_INPUT);
		addProperty("attack" + suffix, "Attack", layer.attack, FormProperty.NUMBER_INPUT);
		addProperty("decay" + suffix, "Decay", layer.decay, FormProperty.NUMBER_INPUT);
		addProperty("pan" + suffix, "Pan", layer.pan, FormProperty.NUMBER_INPUT);
		addProperty("modulation" + suffix, "Modulation", layer.modulation, FormProperty.NUMBER_INPUT);
		addProperty("chorus" + suffix, "Chorus", layer.chorus, FormProperty.NUMBER_INPUT);
		addProperty("filter" + suffix, "Filter", layer.filter, FormProperty.NUMBER_INPUT);
		addProperty("resonance" + suffix, "Resonance", layer.resonance, FormProperty.NUMBER_INPUT);
		addProperty("reverb" + suffix, "Reverb", layer.reverb, FormProperty.NUMBER_INPUT);
		addProperty("vib_rate" + suffix, "Vibrato rate", layer.vib_rate, FormProperty.NUMBER_INPUT);
		addProperty("vib_depth" + suffix, "Vibrato depth", layer.vib_depth, FormProperty.NUMBER_INPUT);
		addProperty("vib_delay" + suffix, "Vibrato delay", layer.vib_delay, FormProperty.NUMBER_INPUT);
	}
	
	protected void addDrumConvertorProperties(DrumConvertor conv) {
		int i = 0;
		String name = conv.name;
		if (name.endsWith("Hihat")) {
			if (name.startsWith("Closed")) {
				name = "Closed hihat";
			} else {
				name = "Open hihat";
			}
		}
		addProperty("convDrumName" + conv.name, "Drum", conv.name, FormProperty.TEXT);
		for (DrumSampleConvertor sample: conv.samples) {
			String suffix = "-" + conv.name + "-" + i;
			addProperty("conv-midiNote" + suffix, "Midi note", sample.midiNote, FormProperty.NUMBER_INPUT);
			addProperty("conv-velocity" + suffix, "Velocity", sample.velocity, FormProperty.NUMBER_INPUT);
			addProperty("conv-hold" + suffix, "Hold", sample.hold, FormProperty.ANY_INPUT);
			addProperty("conv-accentVelocity" + suffix, "Accent velocity", sample.accentVelocity, FormProperty.NUMBER_INPUT);
			addProperty("conv-accentHold" + suffix, "Accent hold", sample.accentHold, FormProperty.ANY_INPUT);
			i++;
		}
	}
}
