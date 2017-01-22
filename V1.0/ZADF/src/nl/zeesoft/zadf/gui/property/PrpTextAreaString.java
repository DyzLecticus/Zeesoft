package nl.zeesoft.zadf.gui.property;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import nl.zeesoft.zadf.gui.GuiProperty;
import nl.zeesoft.zodb.model.datatypes.DtString;

public class PrpTextAreaString extends GuiProperty {
	private JTextArea				textArea				= null;
	private JScrollPane				scrollPanel				= null;

	public PrpTextAreaString(String name,int row,int column,DtString valueObject) {
		super(name,row,column,valueObject);
	}

	public PrpTextAreaString() {
		super("",0,0,new DtString());
	}

	@Override
	public void renderComponent() {
		if (getComponent()==null) {
			textArea = new JTextArea();
			textArea.setColumns(16);
			textArea.setRows(8);
			textArea.setToolTipText(getToolTipText());
			scrollPanel = new JScrollPane(textArea);
			scrollPanel.getVerticalScrollBar().setUnitIncrement(10);
			scrollPanel.setToolTipText(getToolTipText());
			setComponent(scrollPanel);
			updateComponentValue();
		}
	}

	@Override
	public void updateComponentValue() {
		textArea.setText((String) getValueObject().getValue());
	}

	@Override
	public DtString getNewValueObjectFromComponentValue() {
		DtString valueObj = new DtString();
		valueObj.setValue(textArea.getText());
		return valueObj;
	}

	@Override
	public Object getGridSampleValueObject() {
		return new String();
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (textArea!=null) {
			textArea.setEnabled(enabled);
		}
	}

	@Override
	public boolean isEnabled() {
		boolean e = false;
		if (textArea!=null) {
			e = textArea.isEnabled();
		}
		return e;
	}

	@Override 
	public void setToolTipText(String toolTipText) {
		super.setToolTipText(toolTipText);
		if (getComponent()!=null) {
			((JScrollPane) getComponent()).setToolTipText(toolTipText);
			textArea.setToolTipText(toolTipText);
		}
	}	

	/**
	 * @return the textArea
	 */
	public JTextArea getTextArea() {
		return textArea;
	}

}
