package nl.zeesoft.zidm.dialog;

import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zid.dialog.DialogControllerObject;
import nl.zeesoft.zid.dialog.DialogHandler;

public class ModelDialogController extends DialogControllerObject {

	@Override
	public void updatedVariables(DialogHandler handler, Dialog dialog) {
		String cNS = getDialogVariableValueString(handler,dialog.getVariable(ModelDialogFactory.classNameSingle));
		String cNM = getDialogVariableValueString(handler,dialog.getVariable(ModelDialogFactory.classNameMulti));
		String eCNS = getDialogVariableValueString(handler,dialog.getVariable(ModelDialogFactory.extendedClassNameSingle));
		String eCNM = getDialogVariableValueString(handler,dialog.getVariable(ModelDialogFactory.extendedClassNameMulti));
		String pNS = getDialogVariableValueString(handler,dialog.getVariable(ModelDialogFactory.propertyNameSingle));
		String pNM = getDialogVariableValueString(handler,dialog.getVariable(ModelDialogFactory.propertyNameMulti));
		
		if (cNS.length()>0) {
			
		}
	}

}
