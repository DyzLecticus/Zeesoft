ZeeTracker
==========
ZeeTracker is designed to provide a fast and fun music composing work flow.
The interface is based on mod trackers like Scream Tracker and Impulse Tracker.
This means that ZeeTracker is made for regular computer keyboard control.
Part of this key feature is that almost all interface components support playing notes in response to typing certain things on the keyboard.
By default, typing "q", "2" and then "w" will play the corresponding notes C-3, C#3 and then D-3 on the currently selected instrument.
This extends all the way up to C-5 when typing "m".
In order to provide more control an accent velocity can be played by combining these keys with the Shift key.
(Caps Lock is interpreted as a Shift lock).
  
ZeeTracker provides 10 configurable instruments, each with a unique color.
The current working instrument is indicated by the background color of the main tab panel, directly below the menu.
Some of these instruments can use multiple channels in order to combine multiple MIDI instruments into a single ZeeTracker instrument.
ZeeTracker uses the default Java Sequencer and Synthesizer devices and corresponding default sound font.
The MIDI standard instrument set was not designed for modern music styles and the Java default sound font is average at best.
To remedy this, ZeeTracker comes with two high quality internal sound fonts and the option to use a custom sound font.
