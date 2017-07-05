package nl.zeesoft.zeetracker;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zeetracker.gui.Controller;
import nl.zeesoft.zeetracker.gui.Settings;

public class ZeeTracker {
	public static final String	VERSION			= "1.0B";
	public static final String	LICENSE_URL		= "http://www.dbad-license.org";
	public static final String	WEBSITE_URL		= "https://github.com/DyzLecticus/Zeesoft/tree/master/ZeeTracker#zeetracker";
	
	public static void main(String[] args) {
		Settings settings = new Settings();
		Controller controller = new Controller(settings);
		controller.initialize();
		controller.start(true);
	}

	public static ZStringBuilder getDescription() {
		ZStringBuilder r = new ZStringBuilder();
		r.append("ZeeTracker is a music composing application designed to provide a fast and fun work flow.\n");
		r.append("The interface is based on mod trackers like Scream Tracker and Impulse Tracker.\n");
		r.append("This means that ZeeTracker is made for regular computer keyboard control.\n");
		r.append("Part of this feature is that almost all interface components support playing notes in response to typing certain things on the keyboard.\n");
		r.append("By default, typing \"q\", \"2\" and then \"w\" will play the corresponding notes C-3, C#3 and then D-3 on the currently selected instrument.\n");
		r.append("This extends all the way up to B-4 when typing \"m\".\n");
		r.append("In order to provide more control an accent velocity can be played by combining these keys with the Shift key.\n");
		r.append("(Caps Lock is interpreted as a Shift lock).\n");
		r.append("  \n");
		r.append("ZeeTracker is based on the Java MIDI API.\n");
		r.append("It uses the default Java Sequencer and Synthesizer devices and corresponding default sound font.\n");
		r.append("The MIDI standard instrument set was not designed for modern music styles and the Java default sound font is average at best.\n");
		r.append("In order to work around these limitations, ZeeTracker MIDI configuration is mostly limited to instrument configuration.\n");
		r.append("Note and controller events are entered in a ZeeTracker specific format and then translated to MIDI using the instrument configuration.\n");
		r.append("ZeeTracker provides 10 configurable instruments, each with a unique color.\n");
		r.append("Some of these instruments can use multiple MIDI channels in order to combine multiple MIDI instruments into a single ZeeTracker instrument.\n");
		r.append("ZeeTracker also comes with two high quality internal sound fonts and the option to use a custom sound font.\n");
		r.append("The internal sound fonts were created with the free sound font editing software called 'Polyphone'.\n");
		r.append("The sound font samples were created with the professional audio production software called 'Reason'.\n");
		r.append("The Java default Synthesizer is a slightly limited implementation implementation of the Gervill synthesizer created by Karl Helgason.\n");
		r.append("One of those limitations is that the audio output cannot be directly recorded to disk.\n");
		r.append("Zeesoft recommends using the free audio recording and editing software called 'Audacity' to do this.\n");
		r.append("  \n");
		r.append("References;  \n");
		r.append(" * [Mod trackers](https://en.wikipedia.org/wiki/Music_tracker)  \n");
		r.append(" * [Impulse Tracker](http://www.users.on.net/~jtlim/ImpulseTracker)  \n");
		r.append(" * [MIDI](https://en.wikipedia.org/wiki/General_MIDI)  \n");
		r.append(" * [Polyphone](http://polyphone-soundfonts.com)  \n");
		r.append(" * [Reason](https://www.propellerheads.se)  \n");
		r.append(" * [Gervill](https://docs.oracle.com/javase/8/docs/technotes/guides/sound/enhancements_7.html)  \n");
		r.append(" * [Audacity](http://www.audacityteam.org)  \n");
		return r;
	}

	public static ZStringBuilder getRequirementsAndDownload() {
		ZStringBuilder r = new ZStringBuilder();
		r.append("The most recent ZeeTracker version is " + VERSION + ".\n");
		r.append("Users with Java 1.7 installed can download the executable jar file [here](https://dyz.home.xs4all.nl/ZeeTracker.jar) (+- 8 MB).\n");
		r.append("Windows users without Java can download a standard executable [here](https://dyz.home.xs4all.nl/ZeeTracker-" + VERSION + ".exe) (+- 40 MB).\n");
		r.append("ZeeTracker requires about 125 MB of working memory.\n");
		r.append("Using a custom sound font will require more working memory, depending on the size of the sound font.\n");
		r.append("The windows executable will require about 150 MB of free disk space.\n");
		return r;
	}

	public static ZStringBuilder getSoundFonts() {
		ZStringBuilder r = new ZStringBuilder();
		r.append("The two internal ZeeTracker sound fonts provide replacements for the following MIDI instruments;  \n");
		r.append(" * [ZeeTrackerDrumKit.sf2](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZeeTracker/resources/ZeeTrackerDrumKit.sf2)  \n");
		r.append("   * 118 Synth Drum  \n");
		r.append(" * [ZeeTrackerSynthesizers.sf2](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZeeTracker/resources/ZeeTrackerSynthesizers.sf2)  \n");
		r.append("   * 080 Square Wave  \n");
		r.append("   * 081 Saw Wave  \n");
		r.append("   * 082 Syn.Calliope  \n");
		r.append("   * 083 Chiffer Lead  \n");
		r.append("   * 084 Charang  \n");
		r.append("   * 085 Solo Vox  \n");
		r.append("   * 086 5th Saw Wave  \n");
		r.append("   * 087 Bass & Lead  \n");
		r.append("   * 088 Fantasia  \n");
		r.append("   * 089 Warm Pad  \n");
		r.append("   * 090 Polysynth  \n");
		r.append("   * 091 Space Voice  \n");
		r.append("   * 092 Bowed Glass  \n");
		r.append("   * 093 Metal Pad  \n");
		r.append("   * 094 Halo Pad  \n");
		r.append("   * 095 Sweep Pad  \n");
		r.append("   * 096 Ice Rain  \n");
		r.append("   * 097 Soundtrack  \n");
		r.append("  \n");
		r.append("**Please note** that these internal sound fonts are not General MIDI compliant.\n");
		return r;
	}
}
