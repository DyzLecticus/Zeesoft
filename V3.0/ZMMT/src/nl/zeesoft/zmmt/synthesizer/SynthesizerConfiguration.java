package nl.zeesoft.zmmt.synthesizer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sound.midi.Synthesizer;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zmmt.composition.Control;

public class SynthesizerConfiguration {
	public static final String				SOURCE_KICK					= "Kick";
	public static final String				SOURCE_MIDI					= "MIDI beat";

	private int								masterVolume				= 120;
	
	private List<InstrumentConfiguration>	instruments					= new ArrayList<InstrumentConfiguration>();
	private EchoConfiguration				echo						= new EchoConfiguration();
	private List<DrumConfiguration>			drums 						= new ArrayList<DrumConfiguration>();
	private boolean							useInternalDrumKit			= true;
	private boolean							useInternalSynthesizers		= true;
	
	private	String							sideChainSource				= "";
	private double							sideChainAttack				= 0.2D;
	private double							sideChainSustain			= 0.7D;
	private double							sideChainRelease			= 0.5D;
	
	public SynthesizerConfiguration() {
		initialize();
	}
	
	public SynthesizerConfiguration copy() {
		SynthesizerConfiguration copy = new SynthesizerConfiguration();
		copy.setMasterVolume(masterVolume);
		copy.setUseInternalDrumKit(useInternalDrumKit);
		copy.setUseInternalSynthesizers(useInternalSynthesizers);
		copy.setSideChainSource(sideChainSource);
		copy.setSideChainAttack(sideChainAttack);
		copy.setSideChainSustain(sideChainSustain);
		copy.setSideChainRelease(sideChainRelease);
		copy.getInstruments().clear();
		for (InstrumentConfiguration inst: instruments) {
			copy.getInstruments().add(inst.copy());
		}
		copy.setEcho(echo.copy());
		copy.getDrums().clear();
		for (DrumConfiguration drum: drums) {
			copy.getDrums().add(drum.copy());
		}
		return copy;
	}
	
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("masterVolume","" + masterVolume));
		json.rootElement.children.add(new JsElem("useInternalDrumKit","" + useInternalDrumKit));
		json.rootElement.children.add(new JsElem("useInternalSynthesizers","" + useInternalSynthesizers));
		json.rootElement.children.add(new JsElem("sideChainSource",sideChainSource,true));
		json.rootElement.children.add(new JsElem("sideChainAttack","" + sideChainAttack));
		json.rootElement.children.add(new JsElem("sideChainSustain","" + sideChainSustain));
		json.rootElement.children.add(new JsElem("sideChainRelease","" + sideChainRelease));
		for (InstrumentConfiguration inst: instruments) {
			JsElem instElem = new JsElem("instrument");
			json.rootElement.children.add(instElem);
			instElem.children.add(new JsElem("name",inst.getName(),true));
			instElem.children.add(new JsElem("muted","" + inst.isMuted()));
			instElem.children.add(new JsElem("volume","" + inst.getVolume()));
			instElem.children.add(new JsElem("pan","" + inst.getPan()));
			instElem.children.add(new JsElem("holdPercentage","" + inst.getHoldPercentage()));
			if (!inst.getName().equals(Instrument.DRUMS)) {
				instElem.children.add(new JsElem("sideChainPercentage","" + inst.getSideChainPercentage()));
			}
			instElem.children.add(new JsElem("l1MidiNum","" + inst.getLayer1().getMidiNum()));
			instElem.children.add(new JsElem("l1Pressure","" + inst.getLayer1().getPressure()));
			instElem.children.add(new JsElem("l1Modulation","" + inst.getLayer1().getModulation()));
			instElem.children.add(new JsElem("l1Reverb","" + inst.getLayer1().getReverb()));
			instElem.children.add(new JsElem("l1Chorus","" + inst.getLayer1().getChorus()));
			instElem.children.add(new JsElem("l1Filter","" + inst.getLayer1().getFilter()));
			instElem.children.add(new JsElem("l1Resonance","" + inst.getLayer1().getResonance()));
			instElem.children.add(new JsElem("l1Attack","" + inst.getLayer1().getAttack()));
			instElem.children.add(new JsElem("l1Decay","" + inst.getLayer1().getDecay()));
			instElem.children.add(new JsElem("l1Release","" + inst.getLayer1().getRelease()));
			instElem.children.add(new JsElem("l1VibRate","" + inst.getLayer1().getVibRate()));
			instElem.children.add(new JsElem("l1VibDepth","" + inst.getLayer1().getVibDepth()));
			instElem.children.add(new JsElem("l1VibDelay","" + inst.getLayer1().getVibDelay()));
			instElem.children.add(new JsElem("l1ControlModulation","" + inst.getLayer1().isControlModulation()));
			instElem.children.add(new JsElem("l1ControlFilter","" + inst.getLayer1().isControlFilter()));
			instElem.children.add(new JsElem("l1ModToChorus","" + inst.getLayer1().isModToChorus()));
			instElem.children.add(new JsElem("l1ModToResonance","" + inst.getLayer1().isModToResonance()));
			instElem.children.add(new JsElem("l1ModToVibDepth","" + inst.getLayer1().isModToVibDepth()));
			if (!inst.getName().equals(Instrument.DRUMS)) {
				instElem.children.add(new JsElem("l1BaseOctave","" + inst.getLayer1().getBaseOctave()));
				instElem.children.add(new JsElem("l1BaseVelocity","" + inst.getLayer1().getBaseVelocity()));
				instElem.children.add(new JsElem("l1AccentVelocity","" + inst.getLayer1().getAccentVelocity()));
			}
			if (inst.getName().equals(Instrument.BASS1) ||
				inst.getName().equals(Instrument.SYNTH1) || 
				inst.getName().equals(Instrument.LEAD) ||
				inst.getName().equals(Instrument.STRINGS)
				) {
				instElem.children.add(new JsElem("l2MidiNum","" + inst.getLayer2().getMidiNum()));
				instElem.children.add(new JsElem("l2Pressure","" + inst.getLayer2().getPressure()));
				instElem.children.add(new JsElem("l2Modulation","" + inst.getLayer2().getModulation()));
				instElem.children.add(new JsElem("l2Reverb","" + inst.getLayer2().getReverb()));
				instElem.children.add(new JsElem("l2Chorus","" + inst.getLayer2().getChorus()));
				instElem.children.add(new JsElem("l2Filter","" + inst.getLayer2().getFilter()));
				instElem.children.add(new JsElem("l2Resonance","" + inst.getLayer2().getResonance()));
				instElem.children.add(new JsElem("l2Attack","" + inst.getLayer2().getAttack()));
				instElem.children.add(new JsElem("l2Decay","" + inst.getLayer2().getDecay()));
				instElem.children.add(new JsElem("l2Release","" + inst.getLayer2().getRelease()));
				instElem.children.add(new JsElem("l2VibRate","" + inst.getLayer2().getVibRate()));
				instElem.children.add(new JsElem("l2VibDepth","" + inst.getLayer2().getVibDepth()));
				instElem.children.add(new JsElem("l2VibDelay","" + inst.getLayer2().getVibDelay()));
				instElem.children.add(new JsElem("l2ControlModulation","" + inst.getLayer2().isControlModulation()));
				instElem.children.add(new JsElem("l2ControlFilter","" + inst.getLayer2().isControlFilter()));
				instElem.children.add(new JsElem("l2ModToChorus","" + inst.getLayer2().isModToChorus()));
				instElem.children.add(new JsElem("l2ModToResonance","" + inst.getLayer2().isModToResonance()));
				instElem.children.add(new JsElem("l2ModToVibDepth","" + inst.getLayer2().isModToVibDepth()));
				instElem.children.add(new JsElem("l2BaseOctave","" + inst.getLayer2().getBaseOctave()));
				instElem.children.add(new JsElem("l2BaseVelocity","" + inst.getLayer2().getBaseVelocity()));
				instElem.children.add(new JsElem("l2AccentVelocity","" + inst.getLayer2().getAccentVelocity()));
			}
		}
		for (DrumConfiguration drum: drums) {
			JsElem drumElem = new JsElem("drum");
			json.rootElement.children.add(drumElem);
			drumElem.children.add(new JsElem("name",drum.getName(),true));
			drumElem.children.add(new JsElem("muted","" + drum.isMuted()));
			drumElem.children.add(new JsElem("l1MidiNote","" + drum.getLayer1MidiNote()));
			drumElem.children.add(new JsElem("l1BaseVelocity","" + drum.getLayer1BaseVelocity()));
			drumElem.children.add(new JsElem("l1AccentVelocity","" + drum.getLayer1AccentVelocity()));
			drumElem.children.add(new JsElem("l2MidiNote","" + drum.getLayer2MidiNote()));
			drumElem.children.add(new JsElem("l2BaseVelocity","" + drum.getLayer2BaseVelocity()));
			drumElem.children.add(new JsElem("l2AccentVelocity","" + drum.getLayer2AccentVelocity()));
		}
		JsElem echoElem = new JsElem("echo");
		json.rootElement.children.add(echoElem);
		echoElem.children.add(new JsElem("instrument",echo.getInstrument(),true));
		echoElem.children.add(new JsElem("layer","" + echo.getLayer()));
		echoElem.children.add(new JsElem("steps","" + echo.getSteps()));
		echoElem.children.add(new JsElem("velocityPercentage1","" + echo.getVelocityPercentage1()));
		echoElem.children.add(new JsElem("velocityPercentage2","" + echo.getVelocityPercentage2()));
		echoElem.children.add(new JsElem("velocityPercentage3","" + echo.getVelocityPercentage3()));
		echoElem.children.add(new JsElem("reverb1","" + echo.getReverb1()));
		echoElem.children.add(new JsElem("reverb2","" + echo.getReverb2()));
		echoElem.children.add(new JsElem("reverb3","" + echo.getReverb3()));
		echoElem.children.add(new JsElem("pan1","" + echo.getPan1()));
		echoElem.children.add(new JsElem("pan2","" + echo.getPan2()));
		echoElem.children.add(new JsElem("pan3","" + echo.getPan3()));
		return json;
	}
	
	public void fromJson(JsFile json) {
		initialize();
		if (json.rootElement!=null) {
			for (JsElem elem: json.rootElement.children) {
				if (elem.name.equals("masterVolume")) {
					masterVolume = Integer.parseInt(elem.value.toString());
				} else if (elem.name.equals("useInternalDrumKit")) {
					useInternalDrumKit = Boolean.parseBoolean(elem.value.toString());
				} else if (elem.name.equals("useInternalSynthesizers")) {
					useInternalSynthesizers = Boolean.parseBoolean(elem.value.toString());
				} else if (elem.name.equals("sideChainSource")) {
					sideChainSource = elem.value.toString();
				} else if (elem.name.equals("sideChainAttack")) {
					sideChainAttack = Double.parseDouble(elem.value.toString());
				} else if (elem.name.equals("sideChainSustain")) {
					sideChainSustain = Double.parseDouble(elem.value.toString());
				} else if (elem.name.equals("sideChainRelease")) {
					sideChainRelease = Double.parseDouble(elem.value.toString());
				} else if (elem.name.equals("instrument")) {
					InstrumentConfiguration inst = new InstrumentConfiguration();
					for (JsElem val: elem.children) {
						if (val.name.equals("name")) {
							inst.setName(val.value.toString());
						} else if (val.name.equals("muted")) {
							inst.setMuted(Boolean.parseBoolean(val.value.toString()));
						} else if (val.name.equals("volume")) {
							inst.setVolume(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("pan")) {
							inst.setPan(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("holdPercentage")) {
							inst.setHoldPercentage(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("sideChainPercentage")) {
							inst.setSideChainPercentage(Integer.parseInt(val.value.toString()));
						// Layer 1
						} else if (val.name.equals("l1MidiNum")) {
							inst.getLayer1().setMidiNum(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l1Pressure")) {
							inst.getLayer1().setPressure(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l1Modulation")) {
							inst.getLayer1().setModulation(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l1Reverb")) {
							inst.getLayer1().setReverb(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l1Chorus")) {
							inst.getLayer1().setChorus(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l1Filter")) {
							inst.getLayer1().setFilter(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l1Resonance")) {
							inst.getLayer1().setResonance(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l1Attack")) {
							inst.getLayer1().setAttack(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l1Decay")) {
							inst.getLayer1().setDecay(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l1Release")) {
							inst.getLayer1().setRelease(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l1VibRate")) {
							inst.getLayer1().setVibRate(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l1VibDepth")) {
							inst.getLayer1().setVibDepth(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l1VibDelay")) {
							inst.getLayer1().setVibDelay(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l1BaseOctave")) {
							inst.getLayer1().setBaseOctave(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l1BaseVelocity")) {
							inst.getLayer1().setBaseVelocity(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l1AccentVelocity")) {
							inst.getLayer1().setAccentVelocity(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l1ControlModulation")) {
							inst.getLayer1().setControlModulation(Boolean.parseBoolean(val.value.toString()));
						} else if (val.name.equals("l1ControlFilter")) {
							inst.getLayer1().setControlFilter(Boolean.parseBoolean(val.value.toString()));
						} else if (val.name.equals("l1ModToChorus")) {
							inst.getLayer1().setModToChorus(Boolean.parseBoolean(val.value.toString()));
						} else if (val.name.equals("l1ModToResonance")) {
							inst.getLayer1().setModToResonance(Boolean.parseBoolean(val.value.toString()));
						} else if (val.name.equals("l1ModToVibDepth")) {
							inst.getLayer1().setModToVibDepth(Boolean.parseBoolean(val.value.toString()));
						// Layer 2
						} else if (val.name.equals("l2MidiNum")) {
							inst.getLayer2().setMidiNum(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l2Pressure")) {
							inst.getLayer2().setPressure(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l2Modulation")) {
							inst.getLayer2().setModulation(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l2Reverb")) {
							inst.getLayer2().setReverb(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l2Chorus")) {
							inst.getLayer2().setChorus(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l2Filter")) {
							inst.getLayer2().setFilter(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l2Resonance")) {
							inst.getLayer2().setResonance(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l2Attack")) {
							inst.getLayer2().setAttack(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l2Decay")) {
							inst.getLayer2().setDecay(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l2Release")) {
							inst.getLayer2().setRelease(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l2VibRate")) {
							inst.getLayer2().setVibRate(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l2VibDepth")) {
							inst.getLayer2().setVibDepth(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l2VibDelay")) {
							inst.getLayer2().setVibDelay(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l2BaseOctave")) {
							inst.getLayer2().setBaseOctave(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l2BaseVelocity")) {
							inst.getLayer2().setBaseVelocity(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l2AccentVelocity")) {
							inst.getLayer2().setAccentVelocity(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l2ControlModulation")) {
							inst.getLayer2().setControlModulation(Boolean.parseBoolean(val.value.toString()));
						} else if (val.name.equals("l2ControlFilter")) {
							inst.getLayer2().setControlFilter(Boolean.parseBoolean(val.value.toString()));
						} else if (val.name.equals("l2ModToChorus")) {
							inst.getLayer2().setModToChorus(Boolean.parseBoolean(val.value.toString()));
						} else if (val.name.equals("l2ModToResonance")) {
							inst.getLayer2().setModToResonance(Boolean.parseBoolean(val.value.toString()));
						} else if (val.name.equals("l2ModToVibDepth")) {
							inst.getLayer2().setModToVibDepth(Boolean.parseBoolean(val.value.toString()));
						}
					}
					if (inst.getName().length()>0) {
						InstrumentConfiguration current = getInstrument(inst.getName());
						if (current!=null) {
							int index = instruments.indexOf(current);
							instruments.remove(index);
							instruments.add(index,inst);
						}
					}
				} else if (elem.name.equals("drum")) {
					DrumConfiguration drum = new DrumConfiguration();
					for (JsElem val: elem.children) {
						if (val.name.equals("name")) {
							drum.setName(val.value.toString());
						} else if (val.name.equals("muted")) {
							drum.setMuted(Boolean.parseBoolean(val.value.toString()));
						} else if (val.name.equals("l1MidiNote")) {
							drum.setLayer1MidiNote(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l1BaseVelocity")) {
							drum.setLayer1BaseVelocity(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l1AccentVelocity")) {
							drum.setLayer1AccentVelocity(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l2MidiNote")) {
							drum.setLayer2MidiNote(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l2BaseVelocity")) {
							drum.setLayer2BaseVelocity(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("l2AccentVelocity")) {
							drum.setLayer2AccentVelocity(Integer.parseInt(val.value.toString()));
						}
					}
					if (drum.getName().length()>0) {
						DrumConfiguration current = getDrum(drum.getName());
						if (current!=null) {
							int index = drums.indexOf(current);
							drums.remove(index);
							drums.add(index,drum);
						}
					}
				} else if (elem.name.equals("echo")) {
					for (JsElem val: elem.children) {
						if (val.name.equals("instrument")) {
							echo.setInstrument(val.value.toString());
						} else if (val.name.equals("layer")) {
							echo.setLayer(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("steps")) {
							echo.setSteps(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("velocityPercentage1")) {
							echo.setVelocityPercentage1(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("velocityPercentage2")) {
							echo.setVelocityPercentage2(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("velocityPercentage3")) {
							echo.setVelocityPercentage3(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("reverb1")) {
							echo.setReverb1(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("reverb2")) {
							echo.setReverb2(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("reverb3")) {
							echo.setReverb3(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("pan1")) {
							echo.setPan1(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("pan2")) {
							echo.setPan2(Integer.parseInt(val.value.toString()));
						} else if (val.name.equals("pan3")) {
							echo.setPan3(Integer.parseInt(val.value.toString()));
						}
					}
				}
			}
		}
	}

	public int getMasterVolume() {
		return masterVolume;
	}

	public void setMasterVolume(int masterVolume) {
		this.masterVolume = masterVolume;
	}

	public List<InstrumentConfiguration> getInstruments() {
		return instruments;
	}

	public EchoConfiguration getEcho() {
		return echo;
	}

	public void setEcho(EchoConfiguration echo) {
		this.echo = echo;
	}

	public List<DrumConfiguration> getDrums() {
		return drums;
	}

	public boolean isUseInternalDrumKit() {
		return useInternalDrumKit;
	}

	public void setUseInternalDrumKit(boolean useInternalDrumKit) {
		this.useInternalDrumKit = useInternalDrumKit;
	}

	public boolean isUseInternalSynthesizers() {
		return useInternalSynthesizers;
	}

	public void setUseInternalSynthesizers(boolean useInternalSynthesizers) {
		this.useInternalSynthesizers = useInternalSynthesizers;
	}

	public String getSideChainSource() {
		return sideChainSource;
	}

	public void setSideChainSource(String sideChainSource) {
		this.sideChainSource = sideChainSource;
	}

	public double getSideChainAttack() {
		return sideChainAttack;
	}

	public void setSideChainAttack(double sideChainAttack) {
		this.sideChainAttack = sideChainAttack;
	}

	public double getSideChainSustain() {
		return sideChainSustain;
	}

	public void setSideChainSustain(double sideChainSustain) {
		this.sideChainSustain = sideChainSustain;
	}

	public double getSideChainRelease() {
		return sideChainRelease;
	}

	public void setSideChainRelease(double sideChainRelease) {
		this.sideChainRelease = sideChainRelease;
	}

	public InstrumentConfiguration getInstrument(String instrument) {
		InstrumentConfiguration r = null;
		for (InstrumentConfiguration inst: instruments) {
			if (inst.getName().equals(instrument)) {
				r = inst;
			}
		}
		return r;
	}

	public DrumConfiguration getDrum(String drum) {
		DrumConfiguration r = null;
		for (DrumConfiguration inst: drums) {
			if (inst.getName().equals(drum)) {
				r = inst;
			}
		}
		return r;
	}

	public void configureMidiSynthesizer(Synthesizer synth,boolean configureControls) {
		int channels = 3;
		InstrumentConfiguration echoInst = getInstrument(echo.getInstrument());
		if (echoInst==null) {
			echoInst = getInstrument(Instrument.ECHO);
			channels = 2;
		}
		int volume = (echoInst.getVolume() * masterVolume) / 127;
		int layerMidiNum = echoInst.getLayer((echo.getLayer() - 1)).getMidiNum();
		int layerPressure = echoInst.getLayer((echo.getLayer() - 1)).getPressure();
		int layerModulation = echoInst.getLayer((echo.getLayer() - 1)).getModulation();
		int layerChorus = echoInst.getLayer((echo.getLayer() - 1)).getChorus();
		int layerFilter = echoInst.getLayer((echo.getLayer() - 1)).getFilter();
		int layerResonance = echoInst.getLayer((echo.getLayer() - 1)).getResonance();
		int layerAttack = echoInst.getLayer((echo.getLayer() - 1)).getAttack();
		int layerDecay = echoInst.getLayer((echo.getLayer() - 1)).getDecay();
		int layerRelease = echoInst.getLayer((echo.getLayer() - 1)).getRelease();
		int layerVibRate = echoInst.getLayer((echo.getLayer() - 1)).getVibRate();
		int layerVibDepth = echoInst.getLayer((echo.getLayer() - 1)).getVibDepth();
		int layerVibDelay = echoInst.getLayer((echo.getLayer() - 1)).getVibDelay();
		if (layerMidiNum>=0) {
			for (int e = 0; e < channels; e++) {
				int channel = Instrument.getMidiChannelForInstrument(Instrument.ECHO,e);
				int val = synth.getChannels()[channel].getProgram();
				if (val!=layerMidiNum) {
					synth.getChannels()[channel].programChange(layerMidiNum);
				}
				val = synth.getChannels()[channel].getChannelPressure();
				if (val!=layerPressure) {
					synth.getChannels()[channel].setChannelPressure(layerPressure);
				}
				synth.getChannels()[channel].controlChange(Control.VOLUME,volume);
				if (configureControls) {
					synth.getChannels()[channel].controlChange(Control.MODULATION,layerModulation);
					synth.getChannels()[channel].controlChange(Control.CHORUS,layerChorus);
					synth.getChannels()[channel].controlChange(Control.FILTER,layerFilter);
					synth.getChannels()[channel].controlChange(Control.RESONANCE,layerResonance);
					synth.getChannels()[channel].controlChange(Control.ATTACK,layerAttack);
					synth.getChannels()[channel].controlChange(Control.DECAY,layerDecay);
					synth.getChannels()[channel].controlChange(Control.RELEASE,layerRelease);
					synth.getChannels()[channel].controlChange(Control.VIB_RATE,layerVibRate);
					synth.getChannels()[channel].controlChange(Control.VIB_DEPTH,layerVibDepth);
					synth.getChannels()[channel].controlChange(Control.VIB_DELAY,layerVibDelay);
				}
				if (e==0) {
					synth.getChannels()[channel].controlChange(Control.PAN,echo.getPan1());
					synth.getChannels()[channel].controlChange(Control.REVERB,echo.getReverb1());
				} else if (e==1) {
					synth.getChannels()[channel].controlChange(Control.PAN,echo.getPan2());
					synth.getChannels()[channel].controlChange(Control.REVERB,echo.getReverb2());
				} else if (e==2) {
					synth.getChannels()[channel].controlChange(Control.PAN,echo.getPan3());
					synth.getChannels()[channel].controlChange(Control.REVERB,echo.getReverb3());
				}
			}
		}
		for (InstrumentConfiguration inst: instruments) {
			if (!inst.getName().equals(Instrument.ECHO) || echo.getInstrument().length()==0) {
				int layer = 0;
				if (inst.getName().equals(Instrument.ECHO)) {
					layer = 2;
				}
				int channel = Instrument.getMidiChannelForInstrument(inst.getName(),layer);
				int val = synth.getChannels()[channel].getProgram();
				if (val!=inst.getLayer1().getMidiNum()) {
					synth.getChannels()[channel].programChange(inst.getLayer1().getMidiNum());
				}
				val = synth.getChannels()[channel].getChannelPressure();
				if (val!=inst.getLayer1().getPressure()) {
					synth.getChannels()[channel].setChannelPressure(inst.getLayer1().getPressure());
				}
				synth.getChannels()[channel].controlChange(Control.VOLUME,((inst.getVolume() * masterVolume) / 127));
				synth.getChannels()[channel].controlChange(Control.PAN,inst.getPan());
				synth.getChannels()[channel].controlChange(Control.REVERB,inst.getLayer1().getReverb());
				if (configureControls) {
					synth.getChannels()[channel].controlChange(Control.MODULATION,inst.getLayer(layer % 2).getModulation());
					synth.getChannels()[channel].controlChange(Control.CHORUS,inst.getLayer(layer % 2).getChorus());
					synth.getChannels()[channel].controlChange(Control.FILTER,inst.getLayer(layer % 2).getFilter());
					synth.getChannels()[channel].controlChange(Control.RESONANCE,inst.getLayer(layer % 2).getResonance());
					synth.getChannels()[channel].controlChange(Control.ATTACK,inst.getLayer(layer % 2).getAttack());
					synth.getChannels()[channel].controlChange(Control.DECAY,inst.getLayer(layer % 2).getDecay());
					synth.getChannels()[channel].controlChange(Control.RELEASE,inst.getLayer(layer % 2).getRelease());
					synth.getChannels()[channel].controlChange(Control.VIB_RATE,inst.getLayer(layer % 2).getVibRate());
					synth.getChannels()[channel].controlChange(Control.VIB_DEPTH,inst.getLayer(layer % 2).getVibDepth());
					synth.getChannels()[channel].controlChange(Control.VIB_DELAY,inst.getLayer(layer % 2).getVibDelay());
				}
				if (inst.getLayer2().getMidiNum()>=0) {
					channel = Instrument.getMidiChannelForInstrument(inst.getName(),1);
					if (channel>=0) {
						layer = 1;
						val = synth.getChannels()[channel].getProgram();
						if (val!=inst.getLayer2().getMidiNum()) {
							synth.getChannels()[channel].programChange(inst.getLayer2().getMidiNum());
						}
						val = synth.getChannels()[channel].getChannelPressure();
						if (val!=inst.getLayer2().getPressure()) {
							synth.getChannels()[channel].setChannelPressure(inst.getLayer2().getPressure());
						}
						synth.getChannels()[channel].controlChange(Control.VOLUME,((inst.getVolume() * masterVolume) / 127));
						synth.getChannels()[channel].controlChange(Control.PAN,inst.getPan());
						synth.getChannels()[channel].controlChange(Control.REVERB,inst.getLayer2().getReverb());
						if (configureControls) {
							synth.getChannels()[channel].controlChange(Control.MODULATION,inst.getLayer(layer % 2).getModulation());
							synth.getChannels()[channel].controlChange(Control.CHORUS,inst.getLayer(layer % 2).getChorus());
							synth.getChannels()[channel].controlChange(Control.FILTER,inst.getLayer(layer % 2).getFilter());
							synth.getChannels()[channel].controlChange(Control.RESONANCE,inst.getLayer(layer % 2).getResonance());
							synth.getChannels()[channel].controlChange(Control.ATTACK,inst.getLayer(layer % 2).getAttack());
							synth.getChannels()[channel].controlChange(Control.DECAY,inst.getLayer(layer % 2).getDecay());
							synth.getChannels()[channel].controlChange(Control.RELEASE,inst.getLayer(layer % 2).getRelease());
							synth.getChannels()[channel].controlChange(Control.VIB_RATE,inst.getLayer(layer % 2).getVibRate());
							synth.getChannels()[channel].controlChange(Control.VIB_DEPTH,inst.getLayer(layer % 2).getVibDepth());
							synth.getChannels()[channel].controlChange(Control.VIB_DELAY,inst.getLayer(layer % 2).getVibDelay());
						}
					}
				}
			}
		}
	}

	public List<MidiNote> getMidiNotesForNote(String instrument, int note,boolean accent,long msPerStep) {
		List<MidiNote> notes = new ArrayList<MidiNote>();
		if (instrument.equals(Instrument.ECHO) && echo.getInstrument().length()>0) {
			instrument = echo.getInstrument();
		}
		if (instrument.length()>0) {
			int playNote = getMidiNoteNumberForNote(instrument,note,false);
			if (playNote>=0) {
				int velocity = getVelocityForNote(instrument,note,accent,false);
				if (velocity>=0) {
					int channels = 3;
					int layer = 0;
					if (instrument.equals(Instrument.ECHO)) {
						layer = 2;
						channels = 2;
					}
					MidiNote mn = new MidiNote();
					mn.instrument = instrument;
					mn.note = note;
					mn.channel = Instrument.getMidiChannelForInstrument(instrument,layer);
					mn.midiNote = playNote;
					mn.velocity = velocity;
					notes.add(mn);
					int layerNote = getMidiNoteNumberForNote(instrument,note,true);
					if (layerNote>=0) {
						velocity = getVelocityForNote(instrument,note,accent,true);
						mn = new MidiNote();
						mn.instrument = instrument;
						mn.note = note;
						mn.channel = Instrument.getMidiChannelForInstrument(instrument,1);
						mn.midiNote = layerNote;
						mn.velocity = velocity;
						notes.add(mn);
					}
					if (msPerStep>0 && notes.size()>=echo.getLayer() &&
						(instrument.equals(echo.getInstrument()) || (echo.getInstrument().length()==0 && instrument.equals(Instrument.ECHO))) 
						) {
						long now = (new Date()).getTime();
						MidiNote base = notes.get((echo.getLayer()-1));
						for (int e = 0; e < channels; e++) {
							int perc = 0;
							if (e==0) {
								perc = echo.getVelocityPercentage1();
							} else if (e==1) {
								perc = echo.getVelocityPercentage2();
							} else if (e==2) {
								perc = echo.getVelocityPercentage3();
							}
							if (base.velocity>0) {
								velocity = (base.velocity * perc) / 100;
							} else {
								velocity = 0;
							}
							MidiNoteDelayed mnd = new MidiNoteDelayed();
							mnd.instrument = Instrument.ECHO;
							mnd.note = base.note;
							mnd.channel = Instrument.getMidiChannelForInstrument(Instrument.ECHO,e);
							mnd.midiNote = base.midiNote;
							mnd.velocity = velocity;
							mnd.delaySteps = ((e + 1) * echo.getSteps());
							mnd.playDateTime = now + (mnd.delaySteps * msPerStep);
							notes.add(mnd);
						}
					}
				}
			}
		}
		return notes;
	}

	protected int getMidiNoteNumberForNote(String instrument,int note,boolean layer) {
		int r = -1;
		InstrumentConfiguration inst = getInstrument(instrument);
		if (instrument.equals(Instrument.DRUMS)) {
			DrumConfiguration drum = null;
			String name = Drum.getDrumNameForNote(note);
			if (name.length()>0) {
				drum = getDrum(name);
			}
			if (drum!=null) {
				if (layer) { 
					if (drum.getLayer2MidiNote()>=35 && drum.getLayer2MidiNote()!=drum.getLayer1MidiNote()) {
						r = drum.getLayer2MidiNote();
					}
				} else {
					r = drum.getLayer1MidiNote();
				}
			}
		} else {
			if (layer) { 
				if (inst.getLayer2().getMidiNum()>=0) {
					r = (inst.getLayer2().getBaseOctave() * 12) + (note - 36);
				}
			} else {
				r = (inst.getLayer1().getBaseOctave() * 12) + (note - 36);
			}
		}
		if (r>127) {
			r = -1;
		}
		return r;
	}

	protected int getVelocityForNote(String instrument,int note,boolean accent,boolean layer) {
		int r = -1;
		InstrumentConfiguration inst = getInstrument(instrument);
		if (instrument.equals(Instrument.DRUMS)) {
			DrumConfiguration drum = null;
			String name = Drum.getDrumNameForNote(note);
			if (name.length()>0) {
				drum = getDrum(name);
			}
			if (drum!=null) {
				if (layer) {
					if (drum.getLayer2MidiNote()>=35 && drum.getLayer2MidiNote()!=drum.getLayer1MidiNote()) {
						if (accent && drum.getLayer2MidiNote()>=0) {
							r = drum.getLayer2AccentVelocity();
						} else {
							r = drum.getLayer2BaseVelocity();
						}
					}
				} else {
					if (accent) {
						r = drum.getLayer1AccentVelocity();
					} else {
						r = drum.getLayer1BaseVelocity();
					}
				}
			}
		} else {
			if (layer) {
				if (inst.getLayer2().getMidiNum()>=0) {
					if (accent) {
						r = inst.getLayer2().getAccentVelocity();
					} else {
						r = inst.getLayer2().getBaseVelocity();
					}
				}
			} else {
				if (accent) {
					r = inst.getLayer1().getAccentVelocity();
				} else {
					r = inst.getLayer1().getBaseVelocity();
				}
			}
		}
		return r;
	}

	protected void initialize() {
		instruments.clear();
		drums.clear();
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			InstrumentConfiguration inst = new InstrumentConfiguration();
			inst.setName(Instrument.INSTRUMENTS[i]);
			initializeInstrument(inst);
			instruments.add(inst);
		}
		for (int i = 0; i < Drum.DRUMS.length; i++) {
			DrumConfiguration drum = new DrumConfiguration();
			drum.setName(Drum.DRUMS[i]);
			initializeDrum(drum);
			drums.add(drum);
		}
	}

	protected void initializeInstrument(InstrumentConfiguration instrument) {
		if (instrument.getName().equals(Instrument.BASS1)) {
			instrument.setHoldPercentage(90);
			instrument.setSideChainPercentage(40);
			
			instrument.getLayer1().setMidiNum(81);
			instrument.getLayer1().setPressure(0);
			instrument.getLayer1().setModulation(0);
			instrument.getLayer1().setReverb(0);
			instrument.getLayer1().setChorus(0);
			instrument.getLayer1().setFilter(12);
			instrument.getLayer1().setResonance(64);
			instrument.getLayer1().setAttack(28);
			instrument.getLayer1().setDecay(64);
			instrument.getLayer1().setRelease(64);
			instrument.getLayer1().setVibRate(64);
			instrument.getLayer1().setVibDepth(64);
			instrument.getLayer1().setVibDelay(64);
			instrument.getLayer1().setControlModulation(true);
			instrument.getLayer1().setControlFilter(false);
			instrument.getLayer1().setModToChorus(false);
			instrument.getLayer1().setModToResonance(false);
			instrument.getLayer1().setModToVibDepth(false);
			instrument.getLayer1().setBaseOctave(2);
			instrument.getLayer1().setBaseVelocity(120);
			instrument.getLayer1().setAccentVelocity(124);
			
			instrument.getLayer2().setMidiNum(85);
			instrument.getLayer2().setPressure(0);
			instrument.getLayer2().setModulation(0);
			instrument.getLayer2().setReverb(0);
			instrument.getLayer2().setChorus(64);
			instrument.getLayer2().setFilter(64);
			instrument.getLayer2().setResonance(80);
			instrument.getLayer2().setAttack(64);
			instrument.getLayer2().setDecay(64);
			instrument.getLayer2().setRelease(64);
			instrument.getLayer2().setVibRate(64);
			instrument.getLayer2().setVibDepth(64);
			instrument.getLayer2().setVibDelay(64);
			instrument.getLayer2().setControlModulation(true);
			instrument.getLayer2().setControlFilter(true);
			instrument.getLayer2().setModToChorus(false);
			instrument.getLayer2().setModToResonance(false);
			instrument.getLayer2().setModToVibDepth(false);
			instrument.getLayer2().setBaseOctave(2);
			instrument.getLayer2().setBaseVelocity(56);
			instrument.getLayer2().setAccentVelocity(60);
		} else if (instrument.getName().equals(Instrument.BASS2)) {
			instrument.setHoldPercentage(80);
			instrument.setSideChainPercentage(20);
			
			instrument.getLayer1().setMidiNum(92);
			instrument.getLayer1().setPressure(0);
			instrument.getLayer1().setModulation(0);
			instrument.getLayer1().setReverb(32);
			instrument.getLayer1().setChorus(100);
			instrument.getLayer1().setFilter(48);
			instrument.getLayer1().setResonance(80);
			instrument.getLayer1().setAttack(64);
			instrument.getLayer1().setDecay(24);
			instrument.getLayer1().setRelease(52);
			instrument.getLayer1().setVibRate(64);
			instrument.getLayer1().setVibDepth(64);
			instrument.getLayer1().setVibDelay(64);
			instrument.getLayer1().setControlModulation(true);
			instrument.getLayer1().setControlFilter(true);
			instrument.getLayer1().setModToChorus(false);
			instrument.getLayer1().setModToResonance(false);
			instrument.getLayer1().setModToVibDepth(true);
			instrument.getLayer1().setBaseOctave(3);
			instrument.getLayer1().setBaseVelocity(48);
			instrument.getLayer1().setAccentVelocity(56);
		} else if (instrument.getName().equals(Instrument.BASS3)) {
			instrument.setHoldPercentage(100);
			
			instrument.getLayer1().setMidiNum(84);
			instrument.getLayer1().setPressure(0);
			instrument.getLayer1().setModulation(0);
			instrument.getLayer1().setReverb(24);
			instrument.getLayer1().setChorus(0);
			instrument.getLayer1().setFilter(64);
			instrument.getLayer1().setResonance(64);
			instrument.getLayer1().setAttack(64);
			instrument.getLayer1().setDecay(64);
			instrument.getLayer1().setRelease(64);
			instrument.getLayer1().setVibRate(64);
			instrument.getLayer1().setVibDepth(64);
			instrument.getLayer1().setVibDelay(64);
			instrument.getLayer1().setControlModulation(true);
			instrument.getLayer1().setControlFilter(true);
			instrument.getLayer1().setModToChorus(false);
			instrument.getLayer1().setModToResonance(false);
			instrument.getLayer1().setModToVibDepth(true);
			instrument.getLayer1().setBaseOctave(2);
			instrument.getLayer1().setBaseVelocity(52);
			instrument.getLayer1().setAccentVelocity(60);
		} else if (instrument.getName().equals(Instrument.SYNTH1)) {
			instrument.setHoldPercentage(75);
			
			instrument.getLayer1().setMidiNum(83);
			instrument.getLayer1().setPressure(0);
			instrument.getLayer1().setModulation(0);
			instrument.getLayer1().setReverb(72);
			instrument.getLayer1().setChorus(0);
			instrument.getLayer1().setFilter(48);
			instrument.getLayer1().setResonance(80);
			instrument.getLayer1().setAttack(64);
			instrument.getLayer1().setDecay(64);
			instrument.getLayer1().setRelease(64);
			instrument.getLayer1().setVibRate(64);
			instrument.getLayer1().setVibDepth(64);
			instrument.getLayer1().setVibDelay(64);
			instrument.getLayer1().setControlModulation(true);
			instrument.getLayer1().setControlFilter(true);
			instrument.getLayer1().setModToChorus(false);
			instrument.getLayer1().setModToResonance(false);
			instrument.getLayer1().setModToVibDepth(true);
			instrument.getLayer1().setBaseOctave(4);
			instrument.getLayer1().setBaseVelocity(72);
			instrument.getLayer1().setAccentVelocity(72);
			
			instrument.getLayer2().setMidiNum(82);
			instrument.getLayer2().setPressure(0);
			instrument.getLayer2().setModulation(0);
			instrument.getLayer2().setReverb(88);
			instrument.getLayer2().setChorus(0);
			instrument.getLayer2().setFilter(52);
			instrument.getLayer2().setResonance(72);
			instrument.getLayer2().setAttack(64);
			instrument.getLayer2().setDecay(64);
			instrument.getLayer2().setRelease(64);
			instrument.getLayer2().setVibRate(64);
			instrument.getLayer2().setVibDepth(64);
			instrument.getLayer2().setVibDelay(64);
			instrument.getLayer2().setControlModulation(true);
			instrument.getLayer2().setControlFilter(true);
			instrument.getLayer2().setModToChorus(false);
			instrument.getLayer2().setModToResonance(false);
			instrument.getLayer2().setModToVibDepth(true);
			instrument.getLayer2().setBaseOctave(5);
			instrument.getLayer2().setBaseVelocity(0);
			instrument.getLayer2().setAccentVelocity(80);
		} else if (instrument.getName().equals(Instrument.SYNTH2)) {
			instrument.setHoldPercentage(80);
			
			instrument.getLayer1().setMidiNum(81);
			instrument.getLayer1().setPressure(0);
			instrument.getLayer1().setModulation(0);
			instrument.getLayer1().setReverb(64);
			instrument.getLayer1().setChorus(0);
			instrument.getLayer1().setFilter(56);
			instrument.getLayer1().setResonance(64);
			instrument.getLayer1().setAttack(64);
			instrument.getLayer1().setDecay(64);
			instrument.getLayer1().setRelease(64);
			instrument.getLayer1().setVibRate(64);
			instrument.getLayer1().setVibDepth(64);
			instrument.getLayer1().setVibDelay(64);
			instrument.getLayer1().setControlModulation(true);
			instrument.getLayer1().setControlFilter(true);
			instrument.getLayer1().setModToChorus(false);
			instrument.getLayer1().setModToResonance(false);
			instrument.getLayer1().setModToVibDepth(true);
			instrument.getLayer1().setBaseOctave(5);
			instrument.getLayer1().setBaseVelocity(56);
			instrument.getLayer1().setAccentVelocity(60);
		} else if (instrument.getName().equals(Instrument.SYNTH3)) {
			instrument.setHoldPercentage(10);
			
			instrument.getLayer1().setMidiNum(86);
			instrument.getLayer1().setPressure(0);
			instrument.getLayer1().setModulation(0);
			instrument.getLayer1().setReverb(0);
			instrument.getLayer1().setChorus(64);
			instrument.getLayer1().setFilter(64);
			instrument.getLayer1().setResonance(92);
			instrument.getLayer1().setAttack(64);
			instrument.getLayer1().setDecay(64);
			instrument.getLayer1().setRelease(64);
			instrument.getLayer1().setVibRate(64);
			instrument.getLayer1().setVibDepth(64);
			instrument.getLayer1().setVibDelay(64);
			instrument.getLayer1().setControlModulation(true);
			instrument.getLayer1().setControlFilter(true);
			instrument.getLayer1().setModToChorus(false);
			instrument.getLayer1().setModToResonance(false);
			instrument.getLayer1().setModToVibDepth(true);
			instrument.getLayer1().setBaseOctave(3);
			instrument.getLayer1().setBaseVelocity(56);
			instrument.getLayer1().setAccentVelocity(64);
		} else if (instrument.getName().equals(Instrument.LEAD)) {
			instrument.setHoldPercentage(75);
			
			instrument.getLayer1().setMidiNum(80);
			instrument.getLayer1().setPressure(0);
			instrument.getLayer1().setModulation(0);
			instrument.getLayer1().setReverb(72);
			instrument.getLayer1().setChorus(0);
			instrument.getLayer1().setFilter(52);
			instrument.getLayer1().setResonance(64);
			instrument.getLayer1().setAttack(64);
			instrument.getLayer1().setDecay(64);
			instrument.getLayer1().setRelease(64);
			instrument.getLayer1().setVibRate(64);
			instrument.getLayer1().setVibDepth(64);
			instrument.getLayer1().setVibDelay(64);
			instrument.getLayer1().setControlModulation(true);
			instrument.getLayer1().setControlFilter(true);
			instrument.getLayer1().setModToChorus(false);
			instrument.getLayer1().setModToResonance(false);
			instrument.getLayer1().setModToVibDepth(true);
			instrument.getLayer1().setBaseOctave(5);
			instrument.getLayer1().setBaseVelocity(60);
			instrument.getLayer1().setAccentVelocity(60);
			
			instrument.getLayer2().setMidiNum(84);
			instrument.getLayer2().setPressure(0);
			instrument.getLayer2().setModulation(0);
			instrument.getLayer2().setReverb(127);
			instrument.getLayer2().setChorus(0);
			instrument.getLayer2().setFilter(52);
			instrument.getLayer2().setResonance(64);
			instrument.getLayer2().setAttack(64);
			instrument.getLayer2().setDecay(64);
			instrument.getLayer2().setRelease(64);
			instrument.getLayer2().setVibRate(64);
			instrument.getLayer2().setVibDepth(64);
			instrument.getLayer2().setVibDelay(64);
			instrument.getLayer2().setControlModulation(true);
			instrument.getLayer2().setControlFilter(true);
			instrument.getLayer2().setModToChorus(false);
			instrument.getLayer2().setModToResonance(false);
			instrument.getLayer2().setModToVibDepth(true);
			instrument.getLayer2().setBaseOctave(6);
			instrument.getLayer2().setBaseVelocity(0);
			instrument.getLayer2().setAccentVelocity(56);
		} else if (instrument.getName().equals(Instrument.DRUMS)) {
			instrument.setHoldPercentage(80);
			
			instrument.getLayer1().setMidiNum(118);
			instrument.getLayer1().setPressure(0);
			instrument.getLayer1().setModulation(0);
			instrument.getLayer1().setReverb(20);
			instrument.getLayer1().setChorus(0);
			instrument.getLayer1().setFilter(64);
			instrument.getLayer1().setResonance(64);
			instrument.getLayer1().setAttack(64);
			instrument.getLayer1().setDecay(64);
			instrument.getLayer1().setRelease(64);
			instrument.getLayer1().setVibRate(64);
			instrument.getLayer1().setVibDepth(64);
			instrument.getLayer1().setVibDelay(64);
			instrument.getLayer1().setControlModulation(true);
			instrument.getLayer1().setControlFilter(true);
			instrument.getLayer1().setModToChorus(false);
			instrument.getLayer1().setModToResonance(false);
			instrument.getLayer1().setModToVibDepth(true);
		} else if (instrument.getName().equals(Instrument.STRINGS)) {
			instrument.setHoldPercentage(20);
			instrument.setSideChainPercentage(20);
			
			instrument.getLayer1().setMidiNum(89);
			instrument.getLayer1().setPressure(0);
			instrument.getLayer1().setModulation(0);
			instrument.getLayer1().setReverb(127);
			instrument.getLayer1().setChorus(0);
			instrument.getLayer1().setFilter(64);
			instrument.getLayer1().setResonance(64);
			instrument.getLayer1().setAttack(64);
			instrument.getLayer1().setDecay(64);
			instrument.getLayer1().setRelease(64);
			instrument.getLayer1().setVibRate(64);
			instrument.getLayer1().setVibDepth(64);
			instrument.getLayer1().setVibDelay(64);
			instrument.getLayer1().setControlModulation(true);
			instrument.getLayer1().setControlFilter(true);
			instrument.getLayer1().setModToChorus(false);
			instrument.getLayer1().setModToResonance(false);
			instrument.getLayer1().setModToVibDepth(true);
			instrument.getLayer1().setBaseOctave(4);
			instrument.getLayer1().setBaseVelocity(60);
			instrument.getLayer1().setAccentVelocity(80);
			
			instrument.getLayer2().setMidiNum(89);
			instrument.getLayer2().setPressure(0);
			instrument.getLayer2().setModulation(0);
			instrument.getLayer2().setReverb(127);
			instrument.getLayer2().setChorus(48);
			instrument.getLayer2().setFilter(64);
			instrument.getLayer2().setResonance(64);
			instrument.getLayer2().setAttack(80);
			instrument.getLayer2().setDecay(64);
			instrument.getLayer2().setRelease(64);
			instrument.getLayer2().setVibRate(20);
			instrument.getLayer2().setVibDepth(65);
			instrument.getLayer2().setVibDelay(64);
			instrument.getLayer2().setControlModulation(true);
			instrument.getLayer2().setControlFilter(true);
			instrument.getLayer2().setModToChorus(false);
			instrument.getLayer2().setModToResonance(false);
			instrument.getLayer2().setModToVibDepth(true);
			instrument.getLayer2().setBaseOctave(5);
			instrument.getLayer2().setBaseVelocity(60);
			instrument.getLayer2().setAccentVelocity(80);
		}
	}

	protected void initializeDrum(DrumConfiguration drum) {
		if (drum.getName().equals(Drum.KICK)) {
			drum.setLayer1MidiNote(35);
			drum.setLayer1BaseVelocity(96);
			drum.setLayer1AccentVelocity(104);
			drum.setLayer2MidiNote(40);
			drum.setLayer2BaseVelocity(90);
			drum.setLayer2AccentVelocity(116);
		} else if (drum.getName().equals(Drum.SNARE)) {
			drum.setLayer1MidiNote(50);
			drum.setLayer1BaseVelocity(78);
			drum.setLayer1AccentVelocity(88);
			drum.setLayer2MidiNote(54);
			drum.setLayer2BaseVelocity(48);
			drum.setLayer2AccentVelocity(58);
		} else if (drum.getName().equals(Drum.HIHAT1)) {
			drum.setLayer1MidiNote(44);
			drum.setLayer1BaseVelocity(64);
			drum.setLayer1AccentVelocity(72);
		} else if (drum.getName().equals(Drum.HIHAT2)) {
			drum.setLayer1MidiNote(45);
			drum.setLayer1BaseVelocity(64);
			drum.setLayer1AccentVelocity(72);
		} else if (drum.getName().equals(Drum.CLAP)) {
			drum.setLayer1MidiNote(74);
			drum.setLayer1BaseVelocity(60);
			drum.setLayer1AccentVelocity(70);
		} else if (drum.getName().equals(Drum.TOM1)) {
			drum.setLayer1MidiNote(55);
			drum.setLayer1BaseVelocity(88);
			drum.setLayer1AccentVelocity(98);
		} else if (drum.getName().equals(Drum.TOM2)) {
			drum.setLayer1MidiNote(59);
			drum.setLayer1BaseVelocity(88);
			drum.setLayer1AccentVelocity(98);
		} else if (drum.getName().equals(Drum.RIDE)) {
			drum.setLayer1MidiNote(69);
			drum.setLayer1BaseVelocity(76);
			drum.setLayer1AccentVelocity(82);
		} else if (drum.getName().equals(Drum.CYMBAL)) {
			drum.setLayer1MidiNote(70);
			drum.setLayer1BaseVelocity(72);
			drum.setLayer1AccentVelocity(76);
			drum.setLayer1MidiNote(71);
			drum.setLayer1BaseVelocity(44);
			drum.setLayer1AccentVelocity(48);
		} else if (drum.getName().equals(Drum.FX1)) {
			drum.setLayer1MidiNote(80);
			drum.setLayer1BaseVelocity(58);
			drum.setLayer1AccentVelocity(68);
		} else if (drum.getName().equals(Drum.FX2)) {
			drum.setLayer1MidiNote(81);
			drum.setLayer1BaseVelocity(58);
			drum.setLayer1AccentVelocity(68);
		} else if (drum.getName().equals(Drum.FX3)) {
			drum.setLayer1MidiNote(76);
			drum.setLayer1BaseVelocity(58);
			drum.setLayer1AccentVelocity(68);
		}
	}
}
