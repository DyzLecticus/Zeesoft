ZeeTracker
==========
Welcome to the ZeeTracker landing page.
This page contains the following information;
 * [About ZeeTracker](#about-zeetracker)
 * [Demo composition](#demo-composition)
 * [Screen shots](#screen-shots)
 * [Requirements and download](#requirements-and-download)
 * [Future development](#future-development)
 * [Source code](#source-code)
  
About ZeeTracker
----------------
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
 * [Impulse Tracker](http://www.users.on.net/~jtlim/ImpulseTracker/)  
 * [MIDI](https://en.wikipedia.org/wiki/General_MIDI)  
 * [Polyphone](http://polyphone-soundfonts.com)  
 * [Reason](https://www.propellerheads.se)  
 * [Audacity](http://www.audacityteam.org)  
 * [Gervill](https://docs.oracle.com/javase/8/docs/technotes/guides/sound/enhancements_7.html)
  
Demo composition
----------------
A recording of the ZeeTracker demo composition is available on [SoundCloud](https://soundcloud.com/dyz-lecticus/zeetracker-demo-composition).
  
Screen shots
------------
<img alt="The composition tab" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/ZeeTracker/screenshots/ZeeTracker_Tab_Composition.bmp">
  
<img alt="The instruments tab" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/ZeeTracker/screenshots/ZeeTracker_Tab_Instruments.bmp">
  
<img alt="The patterns tab" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/ZeeTracker/screenshots/ZeeTracker_Tab_Patterns.bmp">
  
<img alt="The pattern sequence tab" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/ZeeTracker/screenshots/ZeeTracker_Tab_Sequence.bmp">
  
<img alt="The mix tab" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/ZeeTracker/screenshots/ZeeTracker_Tab_Mix.bmp">
  
<img alt="The settings tab" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/ZeeTracker/screenshots/ZeeTracker_Tab_Settings.bmp">
  
Requirements and download
-------------------------
The most recent ZeeTracker release version is 1.0A.
Users with Java 1.7 installed can download the executable jar file [here](https://dyz.home.xs4all.nl/ZeeTracker.jar) (+- 8 MB).
Windows users without Java can download a standard executable [here](https://dyz.home.xs4all.nl/ZeeTracker-1.0A.exe) (+- 40 MB).
ZeeTracker requires about 125 MB of working memory.
Using a custom sound font will require more working memory, depending on the size of the sound font.
The windows executable will also require about 150 MB of free disk space.
  
Future development
------------------
ZeeTracker 1.0 is currently in the alpha release stage.
The beta release is planned for mid July 2017 followed by a full 1.0 release somewhere in august 2017.
The following features are on the wish list for the 1.0 beta release;
 * Shuffle  
 * Side chain compression  
  
Source code
-----------
ZeeTracker is open source.
The application consists of the following libraries;
 * [ZeeTracker](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZeeTracker/) (Interface logic)  
 * [Zeesoft MIDI Mod Tracker](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZMMT/) (Application logic) 
 * [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/)  
  