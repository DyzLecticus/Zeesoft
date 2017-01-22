package nl.zeesoft.zadf.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.model.datatypes.DtObject;

public abstract class GuiProperty extends GuiPanelObject implements ActionListener, PropertyChangeListener {
	public final static String		ACTION_EVENT	= "ACTION_EVENT";
	public final static String		CHANGE_EVENT	= "CHANGE_EVENT";
	
	private DtObject				valueObject 	= null;
	private String					toolTipText		= null;

	public GuiProperty(String name,int row,int column,DtObject valueObject) {
		super(name,row,column);
		this.valueObject = valueObject;
	}

	public GuiProperty() {
		super("",0,0);
	}

	public abstract void updateComponentValue();
	public abstract DtObject getNewValueObjectFromComponentValue();

	public abstract Object getGridSampleValueObject();
	
	@SuppressWarnings("unchecked")
	public void attachListeners() {
		if (getComponent() instanceof JTextField) {
			((JTextField) getComponent()).addActionListener(this);
			((JTextField) getComponent()).addPropertyChangeListener(this);
		} else if (getComponent() instanceof JToggleButton) {
			((JToggleButton) getComponent()).addActionListener(this);
			((JToggleButton) getComponent()).addPropertyChangeListener(this);
		} else if (getComponent() instanceof JComboBox) {
			((JComboBox<String>) getComponent()).addActionListener(this);
			((JComboBox<String>) getComponent()).addPropertyChangeListener(this);
		}
	}

	/**
	 * @return the valueObject
	 */
	public DtObject getValueObject() {
		return valueObject;
	}

	/**
	 * @param valueObject the valueObject to set
	 */
	public void setValueObject(DtObject valueObject) {
		this.valueObject = valueObject;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		publishEvent(new EvtEvent(CHANGE_EVENT,this,event));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		publishEvent(new EvtEvent(ACTION_EVENT,this,event));
	}

	/**
	 * @return the toolTipText
	 */
	protected String getToolTipText() {
		return toolTipText;
	}

	/**
	 * @param toolTipText the toolTipText to set
	 */
	public void setToolTipText(String toolTipText) {
		this.toolTipText = toolTipText;
	}	
}

