package nl.zeesoft.zadf.gui.property;

import java.util.Arrays;

import javax.swing.JPasswordField;

import nl.zeesoft.zadf.gui.GuiProperty;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;

public class PrpPassword extends GuiProperty {

	public PrpPassword(String name,int row,int column,DtStringBuffer valueObject) {
		super(name,row,column,valueObject);
	}

	public PrpPassword() {
		super("",0,0,new DtStringBuffer());
	}

	@Override
	public void renderComponent() {
		if (getComponent()==null) {
			JPasswordField comp = new JPasswordField();
			comp.setColumns(16);
			comp.setToolTipText(getToolTipText());
			setComponent(comp);
			updateComponentValue();
		}
	}

	@Override
	public void updateComponentValue() {
		JPasswordField comp = (JPasswordField) getComponent();
		comp.setText(DbConfig.getInstance().decodePassword(new StringBuffer((StringBuffer) getValueObject().getValue())).toString());
	}

	@Override
	public DtStringBuffer getNewValueObjectFromComponentValue() {
		DtStringBuffer valueObj = new DtStringBuffer();
		JPasswordField comp = (JPasswordField) getComponent();
		if (comp!=null) {
			char[] pwd = comp.getPassword();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < pwd.length; i++) {
				sb.append(pwd[i]);
			}
			Arrays.fill(pwd,'0');
			valueObj.setValue(DbConfig.getInstance().encodePassword(sb));
		}
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
			((JPasswordField) getComponent()).setToolTipText(toolTipText);
		}
	}	
}
