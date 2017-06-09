package nl.zeesoft.zmmt;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.gui.Settings;

public class ZeeTracker {
	public static void main(String[] args) {
		Settings settings = new Settings();
		Controller controller = new Controller(settings);
		controller.initialize();
		controller.start(true);
	}

	public static ZStringBuilder getManual() {
		ZStringBuilder r = new ZStringBuilder();
		r.append("ZeeTracker\n");
		r.append("==========\n");
		r.append("ZeeTracker is designed to provide a fast music creating workflow.\n");
		r.append("The interface is based on mod trackers like Scream Tracker and Impulse Tracker.\n");
		r.append("This means that the program is made for keyboard control.\n");
		r.append("For instance, if you explore the menu bar you will see that most menu options have keyboard shortcuts.\n");
		r.append("\n");
		r.append("Menu options\n");
		r.append("------------\n");
		r.append("File\n");
		r.append(" * Load (Ctrl+L)  \n");
		r.append("   Presents a file dialog allowing the user to load a ZeeTracker composition file (*.ztc).\n");
		r.append(" * Save (Ctrl+S)  \n");
		r.append("   When the current composition has not been saved yet, this option will presents a file dialog allowing the user to save the composition file.\n");
		r.append("   Else the current working composition file is overwritten and the undo buffer is cleared.\n");
		r.append(" * Save as (Ctrl+W)  \n");
		r.append("   Presents a file dialog allowing the user to save a composition as a ZeeTracker composition (*.ztc) or a MIDI file (*.mid).\n");
		r.append(" * New (Ctrl+N)  \n");
		r.append("   Creates a new ZeeTracker composition file based on the current settings.\n");
		r.append(" * Demo  \n");
		r.append("   Loads the ZeeTracker demo composition.\n");
		return r;
	}
}
