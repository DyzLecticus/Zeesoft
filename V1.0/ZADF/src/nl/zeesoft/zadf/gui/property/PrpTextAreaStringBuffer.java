package nl.zeesoft.zadf.gui.property;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import nl.zeesoft.zadf.gui.GuiProperty;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;

public class PrpTextAreaStringBuffer extends GuiProperty {
	private JTextArea				textArea				= null;
	private JScrollPane				scrollPanel				= null;

	public PrpTextAreaStringBuffer(String name,int row,int column,DtStringBuffer valueObject) {
		super(name,row,column,valueObject);
	}

	public PrpTextAreaStringBuffer() {
		super("",0,0,new DtStringBuffer());
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
		textArea.setText(getValueObject().getValue().toString());
	}

	@Override
	public DtStringBuffer getNewValueObjectFromComponentValue() {
		DtStringBuffer valueObj = new DtStringBuffer();
		valueObj.setValue(new StringBuffer(textArea.getText()));
		return valueObj;
	}

	@Override
	public Object getGridSampleValueObject() {
		return new String();
	}

	@Override 
	public void setToolTipText(String toolTipText) {
		super.setToolTipText(toolTipText);
		if (getComponent()!=null) {
			((JScrollPane) getComponent()).setToolTipText(toolTipText);
			textArea.setToolTipText(toolTipText);
		}
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

	/**
	 * @return the textArea
	 */
	public JTextArea getTextArea() {
		return textArea;
	}

}
