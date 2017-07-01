Zeesoft MIDI Mod Tracker
========================
Zeesoft MIDI Mod Tracker (ZMMT) is a library that contains all ZeeTracker application logic.
This library depends on the [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/).  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZMMT/releases/zmmt-0.9.3.zip) to download the latest ZMMT release (version 0.9.3).  
All ZMMT releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZMMT/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZMMT](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZMMT/src/nl/zeesoft/zmmt/test/ZMMT.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.zmmt.test.TestSynthesizerConfiguration
-------------------------------------------------
This test shows how to create a *SynthesizerConfiguration* instance and convert it to and from JSON.

**Example implementation**  
~~~~
// Create configuration
SynthesizerConfiguration conf = new SynthesizerConfiguration();
// Convert to JSON
JsFile json = conf.toJson();
// Convert from JSON
conf.fromJson(json);
~~~~

Class references;  
 * [TestSynthesizerConfiguration](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZMMT/src/nl/zeesoft/zmmt/test/TestSynthesizerConfiguration.java)
 * [SynthesizerConfiguration](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZMMT/src/nl/zeesoft/zmmt/synthesizer/SynthesizerConfiguration.java)

**Test output**  
The output of this test shows the JSON structure of the *SynthesizerConfiguration*.  
~~~~
{
  "useInternalDrumKit": true,
  "useInternalSynthesizers": true,
  "sideChainSource": "",
  "sideChainAttack": 0.5,
  "sideChainSustain": 0.5,
  "sideChainRelease": 0.8,
  "instrument": {
    "name": "Bass 1",
    "muted": false,
    "volume": 123,
    "pan": 123,
    "holdPercentage": 90,
    "sideChainPercentage": 40,
    "l1MidiNum": 123,
    "l1Pressure": 123,
    "l1Modulation": 123,
    "l1Reverb": 123,
    "l1Chorus": 123,
    "l1Filter": 123,
    "l1Resonance": 123,
    "l1Attack": 123,
    "l1Decay": 123,
    "l1Release": 123,
    "l1VibRate": 123,
    "l1VibDepth": 123,
    "l1VibDelay": 123,
    "l1ControlModulation": true,
    "l1ControlFilter": false,
    "l1ModToChorus": false,
    "l1ModToResonance": false,
    "l1ModToVibDepth": false,
    "l1BaseOctave": 5,
    "l1BaseVelocity": 123,
    "l1AccentVelocity": 123,
    "l2MidiNum": 111,
    "l2Pressure": 111,
    "l2Modulation": 111,
    "l2Reverb": 111,
    "l2Chorus": 111,
    "l2Filter": 111,
    "l2Resonance": 111,
    "l2Attack": 111,
    "l2Decay": 111,
    "l2Release": 111,
    "l2VibRate": 111,
    "l2VibDepth": 111,
    "l2VibDelay": 111,
    "l2ControlModulation": true,
    "l2ControlFilter": true,
    "l2ModToChorus": false,
    "l2ModToResonance": false,
    "l2ModToVibDepth": false,
    "l2BaseOctave": 5,
    "l2BaseVelocity": 111,
    "l2AccentVelocity": 111
  },
  "instrument": {
    "name": "Bass 2",
    "muted": false,
    "volume": 100,
    "pan": 64,
    "holdPercentage": 80,
    "sideChainPercentage": 20,
    "l1MidiNum": 92,
    "l1Pressure": 0,
    "l1Modulation": 0,
    "l1Reverb": 32,
    "l1Chorus": 100,
    "l1Filter": 48,
    "l1Resonance": 80,
    "l1Attack": 64,
    "l1Decay": 24,
    "l1Release": 52,
    "l1VibRate": 64,
    "l1VibDepth": 64,
    "l1VibDelay": 64,
    "l1ControlModulation": true,
    "l1ControlFilter": true,
    "l1ModToChorus": false,
    "l1ModToResonance": false,
    "l1ModToVibDepth": true,
    "l1BaseOctave": 3,
    "l1BaseVelocity": 48,
    "l1AccentVelocity": 56
  },
  "instrument": {
    "name": "Bass 3",
    "muted": false,
    "volume": 100,
    "pan": 64,
    "holdPercentage": 100,
    "sideChainPercentage": 0,
    "l1MidiNum": 84,
    "l1Pressure": 0,
    "l1Modulation": 0,
    "l1Reverb": 24,
    "l1Chorus": 0,
    "l1Filter": 64,
    "l1Resonance": 64,
    "l1Attack": 64,
    "l1Decay": 64,
    "l1Release": 64,
    "l1VibRate": 64,
    "l1VibDepth": 64,
    "l1VibDelay": 64,
    "l1ControlModulation": true,
    "l1ControlFilter": true,
    "l1ModToChorus": false,
    "l1ModToResonance": false,
    "l1ModToVibDepth": true,
    "l1BaseOctave": 2,
    "l1BaseVelocity": 52,
    "l1AccentVelocity": 60
  },
  "instrument": {
    "name": "Synth 1",
    "muted": false,
    "volume": 100,
    "pan": 64,
    "holdPercentage": 75,
    "sideChainPercentage": 0,
    "l1MidiNum": 83,
    "l1Pressure": 0,
    "l1Modulation": 0,
    "l1Reverb": 72,
    "l1Chorus": 0,
    "l1Filter": 48,
    "l1Resonance": 80,
    "l1Attack": 64,
    "l1Decay": 64,
    "l1Release": 64,
    "l1VibRate": 64,
    "l1VibDepth": 64,
    "l1VibDelay": 64,
    "l1ControlModulation": true,
    "l1ControlFilter": true,
    "l1ModToChorus": false,
    "l1ModToResonance": false,
    "l1ModToVibDepth": true,
    "l1BaseOctave": 4,
    "l1BaseVelocity": 72,
    "l1AccentVelocity": 72,
    "l2MidiNum": 82,
    "l2Pressure": 0,
    "l2Modulation": 0,
    "l2Reverb": 88,
    "l2Chorus": 0,
    "l2Filter": 52,
    "l2Resonance": 72,
    "l2Attack": 64,
    "l2Decay": 64,
    "l2Release": 64,
    "l2VibRate": 64,
    "l2VibDepth": 64,
    "l2VibDelay": 64,
    "l2ControlModulation": true,
    "l2ControlFilter": true,
    "l2ModToChorus": false,
    "l2ModToResonance": false,
    "l2ModToVibDepth": true,
    "l2BaseOctave": 5,
    "l2BaseVelocity": 0,
    "l2AccentVelocity": 80
  },
  "instrument": {
    "name": "Synth 2",
    "muted": false,
    "volume": 100,
    "pan": 64,
    "holdPercentage": 80,
    "sideChainPercentage": 0,
    "l1MidiNum": 81,
    "l1Pressure": 0,
    "l1Modulation": 0,
    "l1Reverb": 64,
    "l1Chorus": 0,
    "l1Filter": 56,
    "l1Resonance": 64,
    "l1Attack": 64,
    "l1Decay": 64,
    "l1Release": 64,
    "l1VibRate": 64,
    "l1VibDepth": 64,
    "l1VibDelay": 64,
    "l1ControlModulation": true,
    "l1ControlFilter": true,
    "l1ModToChorus": false,
    "l1ModToResonance": false,
    "l1ModToVibDepth": true,
    "l1BaseOctave": 5,
    "l1BaseVelocity": 56,
    "l1AccentVelocity": 60
  },
  "instrument": {
    "name": "Synth 3",
    "muted": false,
    "volume": 100,
    "pan": 64,
    "holdPercentage": 10,
    "sideChainPercentage": 0,
    "l1MidiNum": 86,
    "l1Pressure": 0,
    "l1Modulation": 0,
    "l1Reverb": 0,
    "l1Chorus": 64,
    "l1Filter": 64,
    "l1Resonance": 92,
    "l1Attack": 64,
    "l1Decay": 64,
    "l1Release": 64,
    "l1VibRate": 64,
    "l1VibDepth": 64,
    "l1VibDelay": 64,
    "l1ControlModulation": true,
    "l1ControlFilter": true,
    "l1ModToChorus": false,
    "l1ModToResonance": false,
    "l1ModToVibDepth": true,
    "l1BaseOctave": 3,
    "l1BaseVelocity": 56,
    "l1AccentVelocity": 64
  },
  "instrument": {
    "name": "Lead",
    "muted": false,
    "volume": 100,
    "pan": 64,
    "holdPercentage": 75,
    "sideChainPercentage": 0,
    "l1MidiNum": 80,
    "l1Pressure": 0,
    "l1Modulation": 0,
    "l1Reverb": 72,
    "l1Chorus": 0,
    "l1Filter": 52,
    "l1Resonance": 64,
    "l1Attack": 64,
    "l1Decay": 64,
    "l1Release": 64,
    "l1VibRate": 64,
    "l1VibDepth": 64,
    "l1VibDelay": 64,
    "l1ControlModulation": true,
    "l1ControlFilter": true,
    "l1ModToChorus": false,
    "l1ModToResonance": false,
    "l1ModToVibDepth": true,
    "l1BaseOctave": 5,
    "l1BaseVelocity": 60,
    "l1AccentVelocity": 60,
    "l2MidiNum": 84,
    "l2Pressure": 0,
    "l2Modulation": 0,
    "l2Reverb": 127,
    "l2Chorus": 0,
    "l2Filter": 52,
    "l2Resonance": 64,
    "l2Attack": 64,
    "l2Decay": 64,
    "l2Release": 64,
    "l2VibRate": 64,
    "l2VibDepth": 64,
    "l2VibDelay": 64,
    "l2ControlModulation": true,
    "l2ControlFilter": true,
    "l2ModToChorus": false,
    "l2ModToResonance": false,
    "l2ModToVibDepth": true,
    "l2BaseOctave": 6,
    "l2BaseVelocity": 0,
    "l2AccentVelocity": 56
  },
  "instrument": {
    "name": "Drums",
    "muted": false,
    "volume": 100,
    "pan": 64,
    "holdPercentage": 80,
    "l1MidiNum": 118,
    "l1Pressure": 0,
    "l1Modulation": 0,
    "l1Reverb": 20,
    "l1Chorus": 0,
    "l1Filter": 64,
    "l1Resonance": 64,
    "l1Attack": 64,
    "l1Decay": 64,
    "l1Release": 64,
    "l1VibRate": 64,
    "l1VibDepth": 64,
    "l1VibDelay": 64,
    "l1ControlModulation": true,
    "l1ControlFilter": true,
    "l1ModToChorus": false,
    "l1ModToResonance": false,
    "l1ModToVibDepth": true
  },
  "instrument": {
    "name": "Strings",
    "muted": false,
    "volume": 100,
    "pan": 64,
    "holdPercentage": 20,
    "sideChainPercentage": 20,
    "l1MidiNum": 89,
    "l1Pressure": 0,
    "l1Modulation": 0,
    "l1Reverb": 127,
    "l1Chorus": 0,
    "l1Filter": 64,
    "l1Resonance": 64,
    "l1Attack": 64,
    "l1Decay": 64,
    "l1Release": 64,
    "l1VibRate": 64,
    "l1VibDepth": 64,
    "l1VibDelay": 64,
    "l1ControlModulation": true,
    "l1ControlFilter": true,
    "l1ModToChorus": false,
    "l1ModToResonance": false,
    "l1ModToVibDepth": true,
    "l1BaseOctave": 4,
    "l1BaseVelocity": 60,
    "l1AccentVelocity": 80,
    "l2MidiNum": 89,
    "l2Pressure": 0,
    "l2Modulation": 0,
    "l2Reverb": 127,
    "l2Chorus": 48,
    "l2Filter": 64,
    "l2Resonance": 64,
    "l2Attack": 80,
    "l2Decay": 64,
    "l2Release": 64,
    "l2VibRate": 20,
    "l2VibDepth": 65,
    "l2VibDelay": 64,
    "l2ControlModulation": true,
    "l2ControlFilter": true,
    "l2ModToChorus": false,
    "l2ModToResonance": false,
    "l2ModToVibDepth": true,
    "l2BaseOctave": 5,
    "l2BaseVelocity": 60,
    "l2AccentVelocity": 80
  },
  "instrument": {
    "name": "Echo",
    "muted": false,
    "volume": 100,
    "pan": 64,
    "holdPercentage": 90,
    "sideChainPercentage": 0,
    "l1MidiNum": 0,
    "l1Pressure": 0,
    "l1Modulation": 0,
    "l1Reverb": 24,
    "l1Chorus": 0,
    "l1Filter": 64,
    "l1Resonance": 64,
    "l1Attack": 64,
    "l1Decay": 64,
    "l1Release": 64,
    "l1VibRate": 64,
    "l1VibDepth": 64,
    "l1VibDelay": 64,
    "l1ControlModulation": true,
    "l1ControlFilter": true,
    "l1ModToChorus": false,
    "l1ModToResonance": false,
    "l1ModToVibDepth": true,
    "l1BaseOctave": 3,
    "l1BaseVelocity": 100,
    "l1AccentVelocity": 110
  },
  "drum": {
    "name": "Kick",
    "muted": false,
    "l1MidiNote": 35,
    "l1BaseVelocity": 96,
    "l1AccentVelocity": 104,
    "l2MidiNote": 40,
    "l2BaseVelocity": 90,
    "l2AccentVelocity": 116
  },
  "drum": {
    "name": "Snare",
    "muted": false,
    "l1MidiNote": 50,
    "l1BaseVelocity": 78,
    "l1AccentVelocity": 88,
    "l2MidiNote": 54,
    "l2BaseVelocity": 48,
    "l2AccentVelocity": 58
  },
  "drum": {
    "name": "Hihat 1",
    "muted": false,
    "l1MidiNote": 44,
    "l1BaseVelocity": 64,
    "l1AccentVelocity": 72,
    "l2MidiNote": 34,
    "l2BaseVelocity": 100,
    "l2AccentVelocity": 110
  },
  "drum": {
    "name": "Hihat 2",
    "muted": false,
    "l1MidiNote": 45,
    "l1BaseVelocity": 64,
    "l1AccentVelocity": 72,
    "l2MidiNote": 34,
    "l2BaseVelocity": 100,
    "l2AccentVelocity": 110
  },
  "drum": {
    "name": "Clap",
    "muted": false,
    "l1MidiNote": 74,
    "l1BaseVelocity": 60,
    "l1AccentVelocity": 70,
    "l2MidiNote": 34,
    "l2BaseVelocity": 100,
    "l2AccentVelocity": 110
  },
  "drum": {
    "name": "Tom 1",
    "muted": false,
    "l1MidiNote": 55,
    "l1BaseVelocity": 88,
    "l1AccentVelocity": 98,
    "l2MidiNote": 34,
    "l2BaseVelocity": 100,
    "l2AccentVelocity": 110
  },
  "drum": {
    "name": "Tom 2",
    "muted": false,
    "l1MidiNote": 59,
    "l1BaseVelocity": 88,
    "l1AccentVelocity": 98,
    "l2MidiNote": 34,
    "l2BaseVelocity": 100,
    "l2AccentVelocity": 110
  },
  "drum": {
    "name": "Ride",
    "muted": false,
    "l1MidiNote": 69,
    "l1BaseVelocity": 76,
    "l1AccentVelocity": 82,
    "l2MidiNote": 34,
    "l2BaseVelocity": 100,
    "l2AccentVelocity": 110
  },
  "drum": {
    "name": "Cymbal",
    "muted": false,
    "l1MidiNote": 71,
    "l1BaseVelocity": 44,
    "l1AccentVelocity": 48,
    "l2MidiNote": 34,
    "l2BaseVelocity": 100,
    "l2AccentVelocity": 110
  },
  "drum": {
    "name": "FX 1",
    "muted": false,
    "l1MidiNote": 80,
    "l1BaseVelocity": 58,
    "l1AccentVelocity": 68,
    "l2MidiNote": 34,
    "l2BaseVelocity": 100,
    "l2AccentVelocity": 110
  },
  "drum": {
    "name": "FX 2",
    "muted": false,
    "l1MidiNote": 81,
    "l1BaseVelocity": 58,
    "l1AccentVelocity": 68,
    "l2MidiNote": 34,
    "l2BaseVelocity": 100,
    "l2AccentVelocity": 110
  },
  "drum": {
    "name": "FX 3",
    "muted": false,
    "l1MidiNote": 76,
    "l1BaseVelocity": 58,
    "l1AccentVelocity": 68,
    "l2MidiNote": 34,
    "l2BaseVelocity": 100,
    "l2AccentVelocity": 110
  },
  "echo": {
    "instrument": "Lead",
    "layer": 1,
    "steps": 6,
    "velocityPercentage1": 70,
    "velocityPercentage2": 50,
    "velocityPercentage3": 30,
    "reverb1": 80,
    "reverb2": 104,
    "reverb3": 127,
    "pan1": 0,
    "pan2": 127,
    "pan3": 64
  }
}
~~~~

nl.zeesoft.zmmt.test.TestComposition
------------------------------------
This test shows how to create a *Composition* instance and convert it to and from JSON.

**Example implementation**  
~~~~
// Create composition
Composition comp = new Composition();
// Convert to JSON
JsFile json = comp.toJson();
// Convert from JSON
comp.fromJson(json);
~~~~

Class references;  
 * [TestComposition](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZMMT/src/nl/zeesoft/zmmt/test/TestComposition.java)
 * [Composition](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZMMT/src/nl/zeesoft/zmmt/composition/Composition.java)

**Test output**  
The output of this test shows the JSON structure of the *Composition*.  
~~~~
{
  "composer": "Dyz Lecticus",
  "name": "ZeeTracker mock composition",
  "beatsPerMinute": 128,
  "beatsPerBar": 4,
  "stepsPerBeat": 8,
  "barsPerPattern": 4,
  "instruments": {
    "useInternalDrumKit": true,
    "useInternalSynthesizers": true,
    "sideChainSource": "Kick",
    "sideChainAttack": 0.5,
    "sideChainSustain": 0.5,
    "sideChainRelease": 0.8,
    "instrument": {
      "name": "Bass 1",
      "muted": false,
      "volume": 123,
      "pan": 123,
      "holdPercentage": 90,
      "sideChainPercentage": 10,
      "l1MidiNum": 123,
      "l1Pressure": 123,
      "l1Modulation": 123,
      "l1Reverb": 123,
      "l1Chorus": 123,
      "l1Filter": 123,
      "l1Resonance": 64,
      "l1Attack": 28,
      "l1Decay": 64,
      "l1Release": 64,
      "l1VibRate": 64,
      "l1VibDepth": 64,
      "l1VibDelay": 64,
      "l1ControlModulation": true,
      "l1ControlFilter": false,
      "l1ModToChorus": false,
      "l1ModToResonance": false,
      "l1ModToVibDepth": false,
      "l1BaseOctave": 5,
      "l1BaseVelocity": 123,
      "l1AccentVelocity": 123,
      "l2MidiNum": 85,
      "l2Pressure": 0,
      "l2Modulation": 0,
      "l2Reverb": 0,
      "l2Chorus": 64,
      "l2Filter": 64,
      "l2Resonance": 80,
      "l2Attack": 64,
      "l2Decay": 64,
      "l2Release": 64,
      "l2VibRate": 64,
      "l2VibDepth": 64,
      "l2VibDelay": 64,
      "l2ControlModulation": true,
      "l2ControlFilter": true,
      "l2ModToChorus": false,
      "l2ModToResonance": false,
      "l2ModToVibDepth": false,
      "l2BaseOctave": 2,
      "l2BaseVelocity": 56,
      "l2AccentVelocity": 60
    },
    "instrument": {
      "name": "Bass 2",
      "muted": false,
      "volume": 100,
      "pan": 64,
      "holdPercentage": 80,
      "sideChainPercentage": 20,
      "l1MidiNum": 92,
      "l1Pressure": 0,
      "l1Modulation": 0,
      "l1Reverb": 32,
      "l1Chorus": 100,
      "l1Filter": 48,
      "l1Resonance": 80,
      "l1Attack": 64,
      "l1Decay": 24,
      "l1Release": 52,
      "l1VibRate": 64,
      "l1VibDepth": 64,
      "l1VibDelay": 64,
      "l1ControlModulation": true,
      "l1ControlFilter": true,
      "l1ModToChorus": false,
      "l1ModToResonance": false,
      "l1ModToVibDepth": true,
      "l1BaseOctave": 3,
      "l1BaseVelocity": 48,
      "l1AccentVelocity": 56
    },
    "instrument": {
      "name": "Bass 3",
      "muted": false,
      "volume": 100,
      "pan": 64,
      "holdPercentage": 100,
      "sideChainPercentage": 0,
      "l1MidiNum": 84,
      "l1Pressure": 0,
      "l1Modulation": 0,
      "l1Reverb": 24,
      "l1Chorus": 0,
      "l1Filter": 64,
      "l1Resonance": 64,
      "l1Attack": 64,
      "l1Decay": 64,
      "l1Release": 64,
      "l1VibRate": 64,
      "l1VibDepth": 64,
      "l1VibDelay": 64,
      "l1ControlModulation": true,
      "l1ControlFilter": true,
      "l1ModToChorus": false,
      "l1ModToResonance": false,
      "l1ModToVibDepth": true,
      "l1BaseOctave": 2,
      "l1BaseVelocity": 52,
      "l1AccentVelocity": 60
    },
    "instrument": {
      "name": "Synth 1",
      "muted": false,
      "volume": 100,
      "pan": 64,
      "holdPercentage": 75,
      "sideChainPercentage": 0,
      "l1MidiNum": 83,
      "l1Pressure": 0,
      "l1Modulation": 0,
      "l1Reverb": 72,
      "l1Chorus": 0,
      "l1Filter": 48,
      "l1Resonance": 80,
      "l1Attack": 64,
      "l1Decay": 64,
      "l1Release": 64,
      "l1VibRate": 64,
      "l1VibDepth": 64,
      "l1VibDelay": 64,
      "l1ControlModulation": true,
      "l1ControlFilter": true,
      "l1ModToChorus": false,
      "l1ModToResonance": false,
      "l1ModToVibDepth": true,
      "l1BaseOctave": 4,
      "l1BaseVelocity": 72,
      "l1AccentVelocity": 72,
      "l2MidiNum": 82,
      "l2Pressure": 0,
      "l2Modulation": 0,
      "l2Reverb": 88,
      "l2Chorus": 0,
      "l2Filter": 52,
      "l2Resonance": 72,
      "l2Attack": 64,
      "l2Decay": 64,
      "l2Release": 64,
      "l2VibRate": 64,
      "l2VibDepth": 64,
      "l2VibDelay": 64,
      "l2ControlModulation": true,
      "l2ControlFilter": true,
      "l2ModToChorus": false,
      "l2ModToResonance": false,
      "l2ModToVibDepth": true,
      "l2BaseOctave": 5,
      "l2BaseVelocity": 0,
      "l2AccentVelocity": 80
    },
    "instrument": {
      "name": "Synth 2",
      "muted": false,
      "volume": 100,
      "pan": 64,
      "holdPercentage": 80,
      "sideChainPercentage": 0,
      "l1MidiNum": 81,
      "l1Pressure": 0,
      "l1Modulation": 0,
      "l1Reverb": 64,
      "l1Chorus": 0,
      "l1Filter": 56,
      "l1Resonance": 64,
      "l1Attack": 64,
      "l1Decay": 64,
      "l1Release": 64,
      "l1VibRate": 64,
      "l1VibDepth": 64,
      "l1VibDelay": 64,
      "l1ControlModulation": true,
      "l1ControlFilter": true,
      "l1ModToChorus": false,
      "l1ModToResonance": false,
      "l1ModToVibDepth": true,
      "l1BaseOctave": 5,
      "l1BaseVelocity": 56,
      "l1AccentVelocity": 60
    },
    "instrument": {
      "name": "Synth 3",
      "muted": false,
      "volume": 100,
      "pan": 64,
      "holdPercentage": 10,
      "sideChainPercentage": 0,
      "l1MidiNum": 86,
      "l1Pressure": 0,
      "l1Modulation": 0,
      "l1Reverb": 0,
      "l1Chorus": 64,
      "l1Filter": 64,
      "l1Resonance": 92,
      "l1Attack": 64,
      "l1Decay": 64,
      "l1Release": 64,
      "l1VibRate": 64,
      "l1VibDepth": 64,
      "l1VibDelay": 64,
      "l1ControlModulation": true,
      "l1ControlFilter": true,
      "l1ModToChorus": false,
      "l1ModToResonance": false,
      "l1ModToVibDepth": true,
      "l1BaseOctave": 3,
      "l1BaseVelocity": 56,
      "l1AccentVelocity": 64
    },
    "instrument": {
      "name": "Lead",
      "muted": false,
      "volume": 100,
      "pan": 64,
      "holdPercentage": 75,
      "sideChainPercentage": 0,
      "l1MidiNum": 80,
      "l1Pressure": 0,
      "l1Modulation": 0,
      "l1Reverb": 72,
      "l1Chorus": 0,
      "l1Filter": 52,
      "l1Resonance": 64,
      "l1Attack": 64,
      "l1Decay": 64,
      "l1Release": 64,
      "l1VibRate": 64,
      "l1VibDepth": 64,
      "l1VibDelay": 64,
      "l1ControlModulation": true,
      "l1ControlFilter": true,
      "l1ModToChorus": false,
      "l1ModToResonance": false,
      "l1ModToVibDepth": true,
      "l1BaseOctave": 5,
      "l1BaseVelocity": 60,
      "l1AccentVelocity": 60,
      "l2MidiNum": 84,
      "l2Pressure": 0,
      "l2Modulation": 0,
      "l2Reverb": 127,
      "l2Chorus": 0,
      "l2Filter": 52,
      "l2Resonance": 64,
      "l2Attack": 64,
      "l2Decay": 64,
      "l2Release": 64,
      "l2VibRate": 64,
      "l2VibDepth": 64,
      "l2VibDelay": 64,
      "l2ControlModulation": true,
      "l2ControlFilter": true,
      "l2ModToChorus": false,
      "l2ModToResonance": false,
      "l2ModToVibDepth": true,
      "l2BaseOctave": 6,
      "l2BaseVelocity": 0,
      "l2AccentVelocity": 56
    },
    "instrument": {
      "name": "Drums",
      "muted": false,
      "volume": 100,
      "pan": 64,
      "holdPercentage": 80,
      "l1MidiNum": 118,
      "l1Pressure": 0,
      "l1Modulation": 0,
      "l1Reverb": 20,
      "l1Chorus": 0,
      "l1Filter": 64,
      "l1Resonance": 64,
      "l1Attack": 64,
      "l1Decay": 64,
      "l1Release": 64,
      "l1VibRate": 64,
      "l1VibDepth": 64,
      "l1VibDelay": 64,
      "l1ControlModulation": true,
      "l1ControlFilter": true,
      "l1ModToChorus": false,
      "l1ModToResonance": false,
      "l1ModToVibDepth": true
    },
    "instrument": {
      "name": "Strings",
      "muted": false,
      "volume": 100,
      "pan": 64,
      "holdPercentage": 20,
      "sideChainPercentage": 20,
      "l1MidiNum": 89,
      "l1Pressure": 0,
      "l1Modulation": 0,
      "l1Reverb": 127,
      "l1Chorus": 0,
      "l1Filter": 64,
      "l1Resonance": 64,
      "l1Attack": 64,
      "l1Decay": 64,
      "l1Release": 64,
      "l1VibRate": 64,
      "l1VibDepth": 64,
      "l1VibDelay": 64,
      "l1ControlModulation": true,
      "l1ControlFilter": true,
      "l1ModToChorus": false,
      "l1ModToResonance": false,
      "l1ModToVibDepth": true,
      "l1BaseOctave": 4,
      "l1BaseVelocity": 60,
      "l1AccentVelocity": 80,
      "l2MidiNum": 89,
      "l2Pressure": 0,
      "l2Modulation": 0,
      "l2Reverb": 127,
      "l2Chorus": 48,
      "l2Filter": 64,
      "l2Resonance": 64,
      "l2Attack": 80,
      "l2Decay": 64,
      "l2Release": 64,
      "l2VibRate": 20,
      "l2VibDepth": 65,
      "l2VibDelay": 64,
      "l2ControlModulation": true,
      "l2ControlFilter": true,
      "l2ModToChorus": false,
      "l2ModToResonance": false,
      "l2ModToVibDepth": true,
      "l2BaseOctave": 5,
      "l2BaseVelocity": 60,
      "l2AccentVelocity": 80
    },
    "instrument": {
      "name": "Echo",
      "muted": false,
      "volume": 100,
      "pan": 64,
      "holdPercentage": 90,
      "sideChainPercentage": 0,
      "l1MidiNum": 0,
      "l1Pressure": 0,
      "l1Modulation": 0,
      "l1Reverb": 24,
      "l1Chorus": 0,
      "l1Filter": 64,
      "l1Resonance": 64,
      "l1Attack": 64,
      "l1Decay": 64,
      "l1Release": 64,
      "l1VibRate": 64,
      "l1VibDepth": 64,
      "l1VibDelay": 64,
      "l1ControlModulation": true,
      "l1ControlFilter": true,
      "l1ModToChorus": false,
      "l1ModToResonance": false,
      "l1ModToVibDepth": true,
      "l1BaseOctave": 3,
      "l1BaseVelocity": 100,
      "l1AccentVelocity": 110
    },
    "drum": {
      "name": "Kick",
      "muted": false,
      "l1MidiNote": 35,
      "l1BaseVelocity": 96,
      "l1AccentVelocity": 104,
      "l2MidiNote": 40,
      "l2BaseVelocity": 90,
      "l2AccentVelocity": 116
    },
    "drum": {
      "name": "Snare",
      "muted": false,
      "l1MidiNote": 50,
      "l1BaseVelocity": 78,
      "l1AccentVelocity": 88,
      "l2MidiNote": 54,
      "l2BaseVelocity": 48,
      "l2AccentVelocity": 58
    },
    "drum": {
      "name": "Hihat 1",
      "muted": false,
      "l1MidiNote": 44,
      "l1BaseVelocity": 64,
      "l1AccentVelocity": 72,
      "l2MidiNote": 34,
      "l2BaseVelocity": 100,
      "l2AccentVelocity": 110
    },
    "drum": {
      "name": "Hihat 2",
      "muted": false,
      "l1MidiNote": 45,
      "l1BaseVelocity": 64,
      "l1AccentVelocity": 72,
      "l2MidiNote": 34,
      "l2BaseVelocity": 100,
      "l2AccentVelocity": 110
    },
    "drum": {
      "name": "Clap",
      "muted": false,
      "l1MidiNote": 74,
      "l1BaseVelocity": 60,
      "l1AccentVelocity": 70,
      "l2MidiNote": 34,
      "l2BaseVelocity": 100,
      "l2AccentVelocity": 110
    },
    "drum": {
      "name": "Tom 1",
      "muted": false,
      "l1MidiNote": 55,
      "l1BaseVelocity": 88,
      "l1AccentVelocity": 98,
      "l2MidiNote": 34,
      "l2BaseVelocity": 100,
      "l2AccentVelocity": 110
    },
    "drum": {
      "name": "Tom 2",
      "muted": false,
      "l1MidiNote": 59,
      "l1BaseVelocity": 88,
      "l1AccentVelocity": 98,
      "l2MidiNote": 34,
      "l2BaseVelocity": 100,
      "l2AccentVelocity": 110
    },
    "drum": {
      "name": "Ride",
      "muted": false,
      "l1MidiNote": 69,
      "l1BaseVelocity": 76,
      "l1AccentVelocity": 82,
      "l2MidiNote": 34,
      "l2BaseVelocity": 100,
      "l2AccentVelocity": 110
    },
    "drum": {
      "name": "Cymbal",
      "muted": false,
      "l1MidiNote": 71,
      "l1BaseVelocity": 44,
      "l1AccentVelocity": 48,
      "l2MidiNote": 34,
      "l2BaseVelocity": 100,
      "l2AccentVelocity": 110
    },
    "drum": {
      "name": "FX 1",
      "muted": false,
      "l1MidiNote": 80,
      "l1BaseVelocity": 58,
      "l1AccentVelocity": 68,
      "l2MidiNote": 34,
      "l2BaseVelocity": 100,
      "l2AccentVelocity": 110
    },
    "drum": {
      "name": "FX 2",
      "muted": false,
      "l1MidiNote": 81,
      "l1BaseVelocity": 58,
      "l1AccentVelocity": 68,
      "l2MidiNote": 34,
      "l2BaseVelocity": 100,
      "l2AccentVelocity": 110
    },
    "drum": {
      "name": "FX 3",
      "muted": false,
      "l1MidiNote": 76,
      "l1BaseVelocity": 58,
      "l1AccentVelocity": 68,
      "l2MidiNote": 34,
      "l2BaseVelocity": 100,
      "l2AccentVelocity": 110
    },
    "echo": {
      "instrument": "Lead",
      "layer": 1,
      "steps": 6,
      "velocityPercentage1": 70,
      "velocityPercentage2": 50,
      "velocityPercentage3": 30,
      "reverb1": 80,
      "reverb2": 104,
      "reverb3": 127,
      "pan1": 0,
      "pan2": 127,
      "pan3": 64
    }
  },
  "patterns": [
    {
      "number": 0,
      "notes": [
        {
          "i": "Drums",
          "t": 1,
          "s": 1,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 1,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 5,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 5,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 9,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 9,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 13,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 13,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 17,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 17,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 21,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 21,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 25,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 25,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 29,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 29,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 33,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 33,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 37,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 37,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 41,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 41,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 45,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 45,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 49,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 49,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 53,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 53,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 57,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 57,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 61,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 61,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 65,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 65,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 69,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 69,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 73,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 73,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 77,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 77,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 81,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 81,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 85,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 85,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 89,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 89,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 93,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 93,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 97,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 97,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 101,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 101,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 105,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 105,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 109,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 109,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 113,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 113,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 117,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 117,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 121,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 121,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 125,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 125,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        }
      ],
      "controls": [
        {
          "i": "Drums",
          "c": 11,
          "s": 1,
          "p": 0
        },
        {
          "i": "Drums",
          "c": 11,
          "s": 64,
          "p": 100
        },
        {
          "i": "Drums",
          "c": 11,
          "s": 128,
          "p": 0
        },
        {
          "i": "Bass 1",
          "c": 11,
          "s": 1,
          "p": 50
        },
        {
          "i": "Bass 1",
          "c": 11,
          "s": 64,
          "p": 100
        },
        {
          "i": "Bass 1",
          "c": 11,
          "s": 128,
          "p": 0
        }
      ]
    },
    {
      "number": 1,
      "notes": [
        {
          "i": "Drums",
          "t": 1,
          "s": 1,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 1,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 5,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 5,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 9,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 9,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 13,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 13,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 17,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 17,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 21,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 21,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 25,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 25,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 29,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 29,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 33,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 33,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 37,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 37,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 41,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 41,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 45,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 45,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 49,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 49,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 53,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 53,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 57,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 57,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 61,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 61,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 65,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 65,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 69,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 69,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 73,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 73,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 77,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 77,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 81,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 81,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 85,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 85,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 89,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 89,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 93,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 93,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 97,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 97,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 101,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 101,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 105,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 105,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 109,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 109,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 113,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 113,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 117,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 117,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 1,
          "s": 121,
          "n": 36,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 2,
          "s": 121,
          "n": 39,
          "d": 1,
          "a": true,
          "v": 100
        },
        {
          "i": "Drums",
          "t": 3,
          "s": 125,
          "n": 40,
          "d": 1,
          "a": false,
          "v": 100
        },
        {
          "i": "Bass 1",
          "t": 4,
          "s": 125,
          "n": 36,
          "d": 4,
          "a": false,
          "v": 100
        }
      ],
      "controls": [
        {
          "i": "Drums",
          "c": 11,
          "s": 1,
          "p": 0
        },
        {
          "i": "Drums",
          "c": 11,
          "s": 64,
          "p": 100
        },
        {
          "i": "Drums",
          "c": 11,
          "s": 128,
          "p": 0
        }
      ]
    }
  ],
  "sequence": {
    "0": 1,
    "1": 0
  }
}
~~~~

nl.zeesoft.zmmt.test.TestCompositionToSequenceConvertor
-------------------------------------------------------
This test shows how to convert a *Composition* to a *Sequence*.

**Example implementation**  
~~~~
// Create composition
Composition comp = new Composition();
// Create convertor
CompositionToSequenceConvertor convertor = new CompositionToSequenceConvertor(comp);
// Convert to Sequence
convertor.convertSequence(false,false);
// Get sequence from convertor
Sequence seq = convertor.getSequence();
~~~~

Class references;  
 * [TestCompositionToSequenceConvertor](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZMMT/src/nl/zeesoft/zmmt/test/TestCompositionToSequenceConvertor.java)
 * [CompositionToSequenceConvertor](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZMMT/src/nl/zeesoft/zmmt/sequencer/CompositionToSequenceConvertor.java)

**Test output**  
The output of this test shows the time it takes to convert a simple composition, with and without side chain compression.  
~~~~
Composition to sequence conversion with side chain compression took 228 ms
Composition to sequence conversion without side chain compression took 10 ms
~~~~

Test results
------------
All 3 tests have been executed successfully (10 assertions).  
Total test duration: 1090 ms (total sleep duration: 0 ms).  

Memory usage per test;  
 * nl.zeesoft.zmmt.test.TestSynthesizerConfiguration: 326 Kb / 0 Mb
 * nl.zeesoft.zmmt.test.TestComposition: 334 Kb / 0 Mb
 * nl.zeesoft.zmmt.test.TestCompositionToSequenceConvertor: 348 Kb / 0 Mb
