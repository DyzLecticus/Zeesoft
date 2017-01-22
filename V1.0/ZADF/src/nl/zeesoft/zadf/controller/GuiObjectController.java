package nl.zeesoft.zadf.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import nl.zeesoft.zadf.gui.GuiObject;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventPublisher;

public abstract class GuiObjectController extends EvtEventPublisher implements ActionListener, TreeSelectionListener, ListSelectionListener, PropertyChangeListener, WindowFocusListener {
	public static final String	GAINED_FOCUS	= "GAINED_FOCUS";
	public static final String	LOST_FOCUS 		= "LOST_FOCUS";

	private GuiObject 			guiObject 		= null;
	
	public GuiObjectController(GuiObject object) {
		this.guiObject = object;
	}
	
	/**
	 * Override to implement
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

	}

	/**
	 * Override to implement
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
	
	}

	/**
	 * Override to implement
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {

	}

	/**
	 * Override to implement
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {

	}

	@Override
	public void windowGainedFocus(WindowEvent e) {
		this.publishEvent(new EvtEvent(GAINED_FOCUS, this, GAINED_FOCUS));
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		this.publishEvent(new EvtEvent(LOST_FOCUS, this, LOST_FOCUS));
	}

	/**
	 * @return the guiObject
	 */
	protected GuiObject getGuiObject() {
		return guiObject;
	}
}
