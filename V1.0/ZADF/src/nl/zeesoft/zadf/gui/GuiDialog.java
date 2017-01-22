package nl.zeesoft.zadf.gui;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import nl.zeesoft.zadf.controller.GuiController;
import nl.zeesoft.zadf.controller.GuiWindowController;

public class GuiDialog extends GuiWindowObject {
	public GuiDialog(String name) {
		super(name, new JDialog());
		final JDialog dialog = getDialog();
        final WindowAdapter adapter = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	ActionEvent a = new ActionEvent(dialog,1,GuiWindowController.ACTION_CLOSE_FRAME);
            	GuiController.getInstance().actionPerformed(a);
            }
            public void windowIconified(WindowEvent e) {
            	ActionEvent a = new ActionEvent(dialog,2,GuiWindowController.ACTION_ICONIFY_FRAME);
            	GuiController.getInstance().actionPerformed(a);
            }
		};
		dialog.addWindowListener(adapter);
	}
	
	/**
	 * @return the dialog
	 */
	public JDialog getDialog() {
		return (JDialog) super.getWindow();
	}
}

