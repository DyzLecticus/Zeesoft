package nl.zeesoft.zadf.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuItem;

import nl.zeesoft.zadf.controller.GuiController;
import nl.zeesoft.zadf.controller.GuiWindowController;

public class GuiFrame extends GuiWindowObject {
	private List<JMenuItem> menuItems = new ArrayList<JMenuItem>();

	@Override
	public GuiObject getGuiObjectForSourceComponent(Component source) {
		GuiObject object = super.getGuiObjectForSourceComponent(source);
		if (object==null) {
			for (JMenuItem item: menuItems) {
				if (source==item) {
					object = this;
					break;
				}
			}
		}
		return object;
	}

	public GuiFrame(String name) {
		super(name, new JFrame());
		final JFrame frame = getFrame();
        final WindowAdapter adapter = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	ActionEvent a = new ActionEvent(frame,1,GuiWindowController.ACTION_CLOSE_FRAME);
            	GuiController.getInstance().actionPerformed(a);
            }
            public void windowIconified(WindowEvent e) {
            	ActionEvent a = new ActionEvent(frame,2,GuiWindowController.ACTION_ICONIFY_FRAME);
            	GuiController.getInstance().actionPerformed(a);
            }
		};
		frame.addWindowListener(adapter);
	}
	
	/**
	 * @return the frame
	 */
	public JFrame getFrame() {
		return (JFrame) super.getWindow();
	}
	
	public void addMenuItem(JMenuItem item) {
		menuItems.add(item);
		item.addActionListener(GuiController.getInstance());
	}
}
