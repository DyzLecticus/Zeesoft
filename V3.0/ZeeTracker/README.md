ZeeTracker
==========
ZeeTracker is a music composing application designed to provide a fast and fun work flow.
The interface is based on mod trackers like Scream Tracker and Impulse Tracker.
This means that ZeeTracker is made for regular computer keyboard control.
Part of this feature is that almost all interface components support playing notes in response to typing certain things on the keyboard.
By default, typing "q", "2" and then "w" will play the corresponding notes C-3, C#3 and then D-3 on the currently selected instrument.
This extends all the way up to B-4 when typing "m".
In order to provide more control an accent velocity can be played by combining these keys with the Shift key.
(Caps Lock is interpreted as a Shift lock).
  
ZeeTracker is based on the Java MIDI API.
It uses the default Java Sequencer and Synthesizer devices and corresponding default sound font.
The MIDI standard instrument set was not designed for modern music styles and the Java default sound font is average at best.
In order to work around these limitations, ZeeTracker MIDI configuration is mostly limited to instrument configuration.
Note and controller events are entered in a ZeeTracker specific format and then translated to MIDI using the instrument configuration.
ZeeTracker provides 10 configurable instruments, each with a unique color.
Some of these instruments can use multiple MIDI channels in order to combine multiple MIDI instruments into a single ZeeTracker instrument.
ZeeTracker also comes with two high quality internal sound fonts and the option to use a custom sound font.
The internal sound fonts were created with the free sound font editing software called 'Polyphone'.
The sound font samples were created with the professional audio production software called 'Reason'.
The Java default Synthesizer is a slightly limited implementation implementation of the Gervill synthesizer created by Karl Helgason.
One of those limitations is that the audio output cannot be directly recorded to disk.
Zeesoft recommends using the free audio recording and editing software called 'Audacity' to do this.
  
References;  
 * [Mod trackers](https://en.wikipedia.org/wiki/Music_tracker)  
 * [Impulse Tracker](http://www.users.on.net/~jtlim/ImpulseTracker)  
 * [MIDI](https://en.wikipedia.org/wiki/General_MIDI)  
 * [Polyphone](http://polyphone-soundfonts.com)  
 * [Reason](https://www.propellerheads.se)  
 * [Gervill](https://docs.oracle.com/javase/8/docs/technotes/guides/sound/enhancements_7.html)  
 * [Audacity](http://www.audacityteam.org)  

**Internal sound fonts**  
The two internal ZeeTracker sound fonts provide replacements for the following MIDI instruments;  
 * [ZeeTrackerDrumKit.sf2](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZeeTracker/resources/ZeeTrackerDrumKit.sf2)  
   * 118 Synth Drum  
 * [ZeeTrackerSynthesizers.sf2](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZeeTracker/resources/ZeeTrackerSynthesizers.sf2)  
   * 080 Square Wave  
   * 081 Saw Wave  
   * 082 Syn.Calliope  
   * 083 Chiffer Lead  
   * 084 Charang  
   * 085 Solo Vox  
   * 086 5th Saw Wave  
   * 087 Bass & Lead  
   * 088 Fantasia  
   * 089 Warm Pad  
   * 090 Polysynth  
   * 091 Space Voice  
   * 092 Bowed Glass  
   * 093 Metal Pad  
   * 094 Halo Pad  
   * 095 Sweep Pad  
   * 096 Ice Rain  
   * 097 Soundtrack  
  
**Please note** that these internal sound fonts are not General MIDI compliant.

**Requirements and download**  
The most recent ZeeTracker version is 1.1.
Users with Java 1.8 installed can download the executable jar file [here](https://dyz.home.xs4all.nl/ZeeTracker.jar) (+- 8 MB).
Windows users without Java can download a standard executable [here](https://dyz.home.xs4all.nl/ZeeTracker-1.1.exe) (+- 50 MB).
ZeeTracker requires about 200 MB of working memory.
Using a custom sound font will require more working memory, depending on the size of the sound font.
The windows executable will require about 150 MB of free disk space.

**Dependencies**  
This library depends on the following libraries;  
 * [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/)  
 * [Zeesoft MIDI Mod Tracker](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZMMT/)  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZT](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZeeTracker/src/nl/zeesoft/zeetracker/test/ZT.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.zeetracker.test.TestSettings
---------------------------------------
This test shows how to create a *Settings* instance and convert it to and from JSON.

**Example implementation**  
~~~~
// Create settings
Settings settings = new Settings();
// Convert to JSON
JsFile json = settings.toJson();
// Convert from JSON
settings.fromJson(json);
~~~~

Class references;  
 * [TestSettings](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZeeTracker/src/nl/zeesoft/zeetracker/test/TestSettings.java)
 * [Settings](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZeeTracker/src/nl/zeesoft/zeetracker/gui/Settings.java)

**Test output**  
The output of this test shows the JSON structure of the *Settings*.  
~~~~
{
  "composer": "",
  "defaultBeatsPerMinute": 128,
  "defaultBeatsPerBar": 4,
  "defaultStepsPerBeat": 8,
  "defaultBarsPerPattern": 4,
  "workingTab": "",
  "workingInstrument": "",
  "workingShowInstrumentFX": false,
  "workingPatternEditMode": "",
  "workingCompositionFileName": "",
  "workingCompositionPattern": 0,
  "customSoundFontFileName": "",
  "customFontName": "Courier New",
  "customFontSize": 14,
  "customRowHeight": 18,
  "keyCodeNoteNumbers": {
    "0": 51,
    "2": 37,
    "3": 39,
    "5": 42,
    "6": 44,
    "7": 46,
    "9": 49,
    "B": 55,
    "C": 52,
    "D": 51,
    "E": 40,
    "G": 54,
    "H": 56,
    "I": 48,
    "J": 58,
    "M": 59,
    "N": 57,
    "O": 50,
    "P": 52,
    "Q": 36,
    "R": 41,
    "S": 49,
    "T": 43,
    "U": 47,
    "V": 53,
    "W": 38,
    "X": 50,
    "Y": 45,
    "Z": 48
  },
  "instruments": {
    "masterVolume": 120,
    "useInternalDrumKit": true,
    "useInternalSynthesizers": true,
    "sideChainSource": "",
    "sideChainAttack": 0.2,
    "sideChainSustain": 0.7,
    "sideChainRelease": 0.5,
    "instrument": {
      "name": "Bass 1",
      "muted": false,
      "volume": 100,
      "pan": 64,
      "holdPercentage": 90,
      "sideChainPercentage": 40,
      "l1MidiNum": 87,
      "l1Pressure": 0,
      "l1Modulation": 0,
      "l1Reverb": 0,
      "l1Chorus": 0,
      "l1Filter": 16,
      "l1Resonance": 76,
      "l1Attack": 28,
      "l1Decay": 48,
      "l1Release": 64,
      "l1VibRate": 64,
      "l1VibDepth": 64,
      "l1VibDelay": 64,
      "l1ControlModulation": true,
      "l1ControlFilter": false,
      "l1ModToChorus": false,
      "l1ModToResonance": false,
      "l1ModToVibDepth": false,
      "l1BaseOctave": 2,
      "l1BaseVelocity": 116,
      "l1AccentVelocity": 124,
      "l2MidiNum": 85,
      "l2Pressure": 0,
      "l2Modulation": 0,
      "l2Reverb": 0,
      "l2Chorus": 40,
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
      "l2BaseVelocity": 48,
      "l2AccentVelocity": 56
    },
    "instrument": {
      "name": "Bass 2",
      "muted": false,
      "volume": 100,
      "pan": 64,
      "holdPercentage": 80,
      "sideChainPercentage": 20,
      "l1MidiNum": 93,
      "l1Pressure": 0,
      "l1Modulation": 0,
      "l1Reverb": 8,
      "l1Chorus": 40,
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
      "l1BaseVelocity": 60,
      "l1AccentVelocity": 68
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
      "l1Chorus": 8,
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
      "l1Reverb": 48,
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
      "l2Reverb": 40,
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
      "l1Release": 66,
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
      "l2Reverb": 100,
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
      "l1Reverb": 8,
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
      "l1MidiNum": 90,
      "l1Pressure": 0,
      "l1Modulation": 0,
      "l1Reverb": 72,
      "l1Chorus": 0,
      "l1Filter": 64,
      "l1Resonance": 64,
      "l1Attack": 64,
      "l1Decay": 52,
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
      "l1BaseVelocity": 56,
      "l1AccentVelocity": 64,
      "l2MidiNum": 90,
      "l2Pressure": 0,
      "l2Modulation": 0,
      "l2Reverb": 72,
      "l2Chorus": 52,
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
      "l2BaseVelocity": 56,
      "l2AccentVelocity": 64
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
      "l1MidiNote": 70,
      "l1BaseVelocity": 72,
      "l1AccentVelocity": 76,
      "l2MidiNote": 71,
      "l2BaseVelocity": 44,
      "l2AccentVelocity": 48
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
}
~~~~

Test results
------------
All 1 tests have been executed successfully (1 assertions).  
Total test duration: 110 ms (total sleep duration: 0 ms).  

Memory usage per test;  
 * nl.zeesoft.zeetracker.test.TestSettings: 566 Kb / 0 Mb
