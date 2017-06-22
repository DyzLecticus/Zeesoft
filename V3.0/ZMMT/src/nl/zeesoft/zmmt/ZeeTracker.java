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
		r.append("This means that ZeeTracker is made for keyboard control.\n");
		r.append("Mouse control is supported for most operations but not needed to create music with ZeeTracker.  \n");
		r.append("  \n");
		r.append("Menu options\n");
		r.append("------------\n");
		r.append("**File**  \n");
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
		r.append(" * Quit  \n");
		r.append("   Stops the program.\n");
		r.append("**Show**  \n");
		r.append(" * Composition (F1)  \n");
		r.append("   Switches to the composition tab.\n");
		r.append(" * Instruments (F2)  \n");
		r.append("   Switches to the instruments tab.\n");
		r.append(" * Patterns (F3)  \n");
		r.append("   Switches to the patterns tab.\n");
		r.append(" * Sequence (F4)  \n");
		r.append("   Switches to the pattern sequence tab.\n");
		r.append(" * Mix (F9)  \n");
		r.append("   Switches to the mix tab.\n");
		r.append(" * Settings (F12)  \n");
		r.append("   Switches to the settings tab.\n");
		r.append("**Instrument**  \n");
		r.append("This menu provides options to select a specific working instrument.\n");
		r.append("The currently selected instrument is reflected in the background color of the tab selectors.\n");
		r.append(" * Synth bass 1 (Ctrl+1)  \n");
		r.append(" * Synth bass 2 (Ctrl+2)  \n");
		r.append(" * Synth bass 3 (Ctrl+3)  \n");
		r.append(" * Synth 1 (Ctrl+4)  \n");
		r.append(" * Synth 2 (Ctrl+5)  \n");
		r.append(" * Synth 3 (Ctrl+6)  \n");
		r.append(" * Lead (Ctrl+7)  \n");
		r.append(" * Drums (Ctrl+8)  \n");
		r.append(" * Strings (Ctrl+9)  \n");
		r.append(" * Echo (Ctrl+0)  \n");
		r.append("Edit\n");
		r.append(" * Undo (Ctrl+Z)  \n");
		r.append("   Undoes the latest composition change.\n");
		r.append(" * Redo (Ctrl+Y)  \n");
		r.append(" * Pattern  \n");
		r.append("   * Select pattern (Ctrl+P)  \n");
		r.append("   * Next pattern (Ctrl+PageDown)  \n");
		r.append("   * Previous pattern (Ctrl+PageUp)  \n");
		r.append("   * Toggle insert (Ctrl+I)  \n");
		r.append("   * Edit pattern (Ctrl+E)  \n");
		r.append("   * Copy notes (Ctrl+C)  \n");
		r.append("   * Paste notes (Ctrl+V)  \n");
		r.append("Sequencer\n");
		r.append(" * Play pattern (F5)  \n");
		r.append(" * Play sequence (F6)  \n");
		r.append(" * Continue (F7)  \n");
		r.append(" * Pause/Stop (F8)  \n");
		// TODO: Finish manual
		// TODO: Include manual
		return r;
	}
}
