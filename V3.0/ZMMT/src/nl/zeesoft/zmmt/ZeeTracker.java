package nl.zeesoft.zmmt;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.gui.Settings;

public class ZeeTracker {
	public static final String	VERSION	= "1.0A";
	
	public static void main(String[] args) {
		Settings settings = new Settings();
		Controller controller = new Controller(settings);
		controller.initialize();
		controller.start(true);
	}

	public static ZStringBuilder getDescription() {
		ZStringBuilder r = new ZStringBuilder();
		r.append("ZeeTracker is designed to provide a fast and fun music composing workflow.\n");
		r.append("The interface is based on mod trackers like Scream Tracker and Impulse Tracker.\n");
		r.append("This means that ZeeTracker is made for regular computer keyboard control.\n");
		r.append("Part of this key feature is that almost all interface components support playing notes in response to typing certain things on the keyboard.\n");
		r.append("By default, typing \"q\", \"2\" and then \"w\" will play the corresponding notes C-3, C#3 and then D-3 on the currently selected instrument.\n");
		r.append("This extends all the way up to C-5 when typing \"m\".\n");
		r.append("In order to provide more control an accent velocity can be played by combining these keys with the Shift key.\n");
		r.append("(Caps Lock is interpreted as a Shift lock).\n");
		r.append("  \n");
		r.append("ZeeTracker provides 10 configurable instruments, each with a unique color.\n");
		r.append("The current working instrument is indicated by the background color of the main tab panel, directly below the menu.\n");
		r.append("Some of these instruments can use multiple channels in order to combine multiple MIDI instruments into a single ZeeTracker instrument.\n");
		r.append("ZeeTracker uses the default Java Sequencer and Synthesizer devices and corresponding default sound font.\n");
		r.append("The MIDI standard instrument set was not designed for modern music styles and the Java default sound font is average at best.\n");
		r.append("To remedy this, ZeeTracker comes with two high quality internal sound fonts and the option to use a custom sound font.\n");
		r.append("  \n");
		// TODO: Finish description
		return r;
	}
}
