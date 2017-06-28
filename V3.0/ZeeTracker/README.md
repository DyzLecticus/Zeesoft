ZeeTracker
==========
ZeeTracker is a music composing application designed to provide a fast and fun work flow.
The interface is based on mod trackers like Scream Tracker and Impulse Tracker.
This means that ZeeTracker is made for regular computer keyboard control.
Part of this feature is that almost all interface components support playing notes in response to typing certain things on the keyboard.
By default, typing "q", "2" and then "w" will play the corresponding notes C-3, C#3 and then D-3 on the currently selected instrument.
This extends all the way up to C-5 when typing "m".
In order to provide more control an accent velocity can be played by combining these keys with the Shift key.
(Caps Lock is interpreted as a Shift lock).
  
ZeeTracker is based on the Java MIDI API.
It uses the default Java Sequencer and Synthesizer devices and corresponding default sound font.
The MIDI standard instrument set was not designed for modern music styles and the Java default sound font is average at best.
In order to work around these limitations, ZeeTracker MIDI configuration is limited to instrument configuration.
Note and controller events are entered in a ZeeTracker specific format and then translated to MIDI using the instrument configuration.
ZeeTracker provides 10 configurable instruments, each with a unique color.
Some of these instruments can use multiple MIDI channels in order to combine multiple MIDI instruments into a single ZeeTracker instrument.
ZeeTracker also comes with two high quality internal sound fonts and the option to use a custom sound font.
The internal sound fonts were created with the free sound font editing software called 'Polyphone'.
The sound font samples were created with the professional audio production software called 'Reason'.
Another limitation of working with the Java default Synthesizer is that audio output cannot be directly recorded to disk.
Zeesoft recommends using the free audio recording and editing software called 'Audacity' to do this.
  
References;  
 * [MIDI](https://en.wikipedia.org/wiki/General_MIDI)  
 * [Polyphone](http://polyphone-soundfonts.com)  
 * [Reason](https://www.propellerheads.se)  
 * [Audacity](http://www.audacityteam.org)  

**Requirements and download**
The most recent ZeeTracker version is 1.0A.
Users with Java 1.7 installed can download the executable jar file [here](https://dyz.home.xs4all.nl/ZeeTracker.jar) (+- 8 MB).
Windows users without Java can download a standard executable [here](https://dyz.home.xs4all.nl/ZeeTracker-1.0A.exe) (+- 40 MB).
ZeeTracker requires about 125 MB of working memory.
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
    "useInternalDrumKit": true,
    "useInternalSynthesizers": true,
    "instrument": {
      "name": "Synth bass 1",
      "muted": false,
      "volume": 100,
      "pan": 64,
      "holdPercentage": 90,
      "l1MidiNum": 34,
      "l1Pressure": 0,
      "l1Modulation": 0,
      "l1Reverb": 32,
      "l1Chorus": 72,
      "l1Filter": 16,
      "l1Resonance": 80,
      "l1Attack": 64,
      "l1Decay": 64,
      "l1Release": 64,
      "l1VibRate": 64,
      "l1VibDepth": 64,
      "l1VibDelay": 64,
      "l1ControlModulation": true,
      "l1ControlFilter": false,
      "l1ModToChorus": false,
      "l1ModToResonance": true,
      "l1ModToVibDepth": false,
      "l1BaseOctave": 2,
      "l1BaseVelocity": 60,
      "l1AccentVelocity": 70,
      "l2MidiNum": 38,
      "l2Pressure": 0,
      "l2Modulation": 0,
      "l2Reverb": 24,
      "l2Chorus": 0,
      "l2Filter": 64,
      "l2Resonance": 64,
      "l2Attack": 64,
      "l2Decay": 64,
      "l2Release": 64,
      "l2VibRate": 64,
      "l2VibDepth": 64,
      "l2VibDelay": 64,
      "l2BaseOctave": 2,
      "l2BaseVelocity": 100,
      "l2AccentVelocity": 110,
      "l2ControlModulation": true,
      "l2ControlFilter": true,
      "l2ModToChorus": false,
      "l2ModToResonance": false,
      "l2ModToVibDepth": true
    },
    "instrument": {
      "name": "Synth bass 2",
      "muted": false,
      "volume": 100,
      "pan": 48,
      "holdPercentage": 90,
      "l1MidiNum": 7,
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
    "instrument": {
      "name": "Synth bass 3",
      "muted": false,
      "volume": 100,
      "pan": 80,
      "holdPercentage": 90,
      "l1MidiNum": 25,
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
    "instrument": {
      "name": "Synth 1",
      "muted": false,
      "volume": 100,
      "pan": 64,
      "holdPercentage": 90,
      "l1MidiNum": 85,
      "l1Pressure": 0,
      "l1Modulation": 0,
      "l1Reverb": 16,
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
      "l1BaseOctave": 5,
      "l1BaseVelocity": 60,
      "l1AccentVelocity": 70,
      "l2MidiNum": 80,
      "l2Pressure": 0,
      "l2Modulation": 0,
      "l2Reverb": 127,
      "l2Chorus": 0,
      "l2Filter": 64,
      "l2Resonance": 64,
      "l2Attack": 64,
      "l2Decay": 64,
      "l2Release": 64,
      "l2VibRate": 64,
      "l2VibDepth": 64,
      "l2VibDelay": 64,
      "l2BaseOctave": 6,
      "l2BaseVelocity": 50,
      "l2AccentVelocity": 64,
      "l2ControlModulation": true,
      "l2ControlFilter": true,
      "l2ModToChorus": false,
      "l2ModToResonance": false,
      "l2ModToVibDepth": true
    },
    "instrument": {
      "name": "Synth 2",
      "muted": false,
      "volume": 100,
      "pan": 48,
      "holdPercentage": 90,
      "l1MidiNum": 81,
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
      "l1AccentVelocity": 70
    },
    "instrument": {
      "name": "Synth 3",
      "muted": false,
      "volume": 100,
      "pan": 80,
      "holdPercentage": 90,
      "l1MidiNum": 62,
      "l1Pressure": 0,
      "l1Modulation": 0,
      "l1Reverb": 48,
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
      "l1BaseVelocity": 84,
      "l1AccentVelocity": 96
    },
    "instrument": {
      "name": "Lead",
      "muted": false,
      "volume": 100,
      "pan": 64,
      "holdPercentage": 90,
      "l1MidiNum": 87,
      "l1Pressure": 0,
      "l1Modulation": 0,
      "l1Reverb": 64,
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
      "l1BaseOctave": 6,
      "l1BaseVelocity": 70,
      "l1AccentVelocity": 80,
      "l2MidiNum": 102,
      "l2Pressure": 0,
      "l2Modulation": 0,
      "l2Reverb": 127,
      "l2Chorus": 0,
      "l2Filter": 64,
      "l2Resonance": 64,
      "l2Attack": 64,
      "l2Decay": 64,
      "l2Release": 64,
      "l2VibRate": 64,
      "l2VibDepth": 64,
      "l2VibDelay": 64,
      "l2BaseOctave": 6,
      "l2BaseVelocity": 60,
      "l2AccentVelocity": 70,
      "l2ControlModulation": true,
      "l2ControlFilter": true,
      "l2ModToChorus": false,
      "l2ModToResonance": false,
      "l2ModToVibDepth": true
    },
    "instrument": {
      "name": "Drums",
      "muted": false,
      "volume": 100,
      "pan": 64,
      "holdPercentage": 90,
      "l1MidiNum": 118,
      "l1Pressure": 0,
      "l1Modulation": 0,
      "l1Reverb": 0,
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
      "holdPercentage": 90,
      "l1MidiNum": 50,
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
      "l1BaseVelocity": 40,
      "l1AccentVelocity": 50,
      "l2MidiNum": 49,
      "l2Pressure": 0,
      "l2Modulation": 0,
      "l2Reverb": 127,
      "l2Chorus": 0,
      "l2Filter": 64,
      "l2Resonance": 64,
      "l2Attack": 64,
      "l2Decay": 64,
      "l2Release": 64,
      "l2VibRate": 64,
      "l2VibDepth": 64,
      "l2VibDelay": 64,
      "l2BaseOctave": 5,
      "l2BaseVelocity": 36,
      "l2AccentVelocity": 44,
      "l2ControlModulation": true,
      "l2ControlFilter": true,
      "l2ModToChorus": false,
      "l2ModToResonance": false,
      "l2ModToVibDepth": true
    },
    "instrument": {
      "name": "Echo",
      "muted": false,
      "volume": 100,
      "pan": 64,
      "holdPercentage": 90,
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
      "l1MidiNote": 40,
      "l1BaseVelocity": 90,
      "l1AccentVelocity": 110,
      "l2MidiNote": 34,
      "l2BaseVelocity": 100,
      "l2AccentVelocity": 110
    },
    "drum": {
      "name": "Snare",
      "muted": false,
      "l1MidiNote": 54,
      "l1BaseVelocity": 60,
      "l1AccentVelocity": 70,
      "l2MidiNote": 34,
      "l2BaseVelocity": 100,
      "l2AccentVelocity": 110
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
      "l1BaseVelocity": 100,
      "l1AccentVelocity": 110,
      "l2MidiNote": 34,
      "l2BaseVelocity": 100,
      "l2AccentVelocity": 110
    },
    "drum": {
      "name": "Tom 2",
      "muted": false,
      "l1MidiNote": 59,
      "l1BaseVelocity": 100,
      "l1AccentVelocity": 110,
      "l2MidiNote": 34,
      "l2BaseVelocity": 100,
      "l2AccentVelocity": 110
    },
    "drum": {
      "name": "Ride",
      "muted": false,
      "l1MidiNote": 69,
      "l1BaseVelocity": 50,
      "l1AccentVelocity": 60,
      "l2MidiNote": 34,
      "l2BaseVelocity": 100,
      "l2AccentVelocity": 110
    },
    "drum": {
      "name": "Cymbal",
      "muted": false,
      "l1MidiNote": 71,
      "l1BaseVelocity": 40,
      "l1AccentVelocity": 50,
      "l2MidiNote": 34,
      "l2BaseVelocity": 100,
      "l2AccentVelocity": 110
    },
    "drum": {
      "name": "FX 1",
      "muted": false,
      "l1MidiNote": 80,
      "l1BaseVelocity": 70,
      "l1AccentVelocity": 80,
      "l2MidiNote": 34,
      "l2BaseVelocity": 100,
      "l2AccentVelocity": 110
    },
    "drum": {
      "name": "FX 2",
      "muted": false,
      "l1MidiNote": 81,
      "l1BaseVelocity": 70,
      "l1AccentVelocity": 80,
      "l2MidiNote": 34,
      "l2BaseVelocity": 100,
      "l2AccentVelocity": 110
    },
    "drum": {
      "name": "FX 3",
      "muted": false,
      "l1MidiNote": 76,
      "l1BaseVelocity": 70,
      "l1AccentVelocity": 80,
      "l2MidiNote": 34,
      "l2BaseVelocity": 100,
      "l2AccentVelocity": 110
    },
    "echo": {
      "instrument": "",
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
Total test duration: 95 ms (total sleep duration: 0 ms).  

Memory usage per test;  
 * nl.zeesoft.zeetracker.test.TestSettings: 309 Kb / 0 Mb
