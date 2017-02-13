package nl.zeesoft.zidm.dialog;

import java.util.List;

import nl.zeesoft.zdm.model.ModelClass;
import nl.zeesoft.zdm.model.ModelPackage;
import nl.zeesoft.zdm.model.transformations.impl.AddClass;
import nl.zeesoft.zdm.model.transformations.impl.AddProperty;
import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zid.dialog.DialogControllerObject;
import nl.zeesoft.zid.dialog.DialogHandler;
import nl.zeesoft.zidm.dialog.model.DialogModel;
import nl.zeesoft.zspr.Language;

public class ModelDialogController extends DialogControllerObject {

	@Override
	public void updatedVariables(DialogHandler handler, Dialog dialog) {
		DialogModel model = ((ModelDialog) dialog).getModel();
		String cIA = upperCaseFirst(getDialogVariableValueString(handler,dialog.getVariable(ModelDialogFactory.classInArt)));
		String cNS = upperCaseFirst(getDialogVariableValueString(handler,dialog.getVariable(ModelDialogFactory.classNameSingle)));
		String cNM = upperCaseFirst(getDialogVariableValueString(handler,dialog.getVariable(ModelDialogFactory.classNameMulti)));
		String eCIA = upperCaseFirst(getDialogVariableValueString(handler,dialog.getVariable(ModelDialogFactory.extendedClassInArt)));
		String eCNS = upperCaseFirst(getDialogVariableValueString(handler,dialog.getVariable(ModelDialogFactory.extendedClassNameSingle)));
		String eCNM = upperCaseFirst(getDialogVariableValueString(handler,dialog.getVariable(ModelDialogFactory.extendedClassNameMulti)));
		String pNS = upperCaseFirst(getDialogVariableValueString(handler,dialog.getVariable(ModelDialogFactory.propertyNameSingle)));
		String pNM = upperCaseFirst(getDialogVariableValueString(handler,dialog.getVariable(ModelDialogFactory.propertyNameMulti)));

		ModelPackage pkg = getBasePackage(model);
		
		ModelClass cls = null; 
		ModelClass extendsCls = null;
		//ModelProperty prop = null;
		
		boolean clearClassVariables = true;
		boolean clearExtendedClassVariables = true;
		boolean clearPropertyVariables = true;
		
		String eName = "";
		if (eCNS.length()>0) {
			eName = model.getTranslator().getName(dialog.getLanguage().getCode(),true,eCNS);
		} else if (eCNM.length()>0) {
			eName = model.getTranslator().getName(dialog.getLanguage().getCode(),false,eCNM);
		}
		if (eCNS.length()>0 || eCNM.length()>0) {
			//System.out.println("Looking for class " + eName + " ...");
			if (eName.length()==0) {
				if (dialog.getLanguage().getCode().equals(Language.ENG)) {
					if (eCNS.length()>0) {
						getOutput().append("What is ");
						getOutput().append(eCIA.toLowerCase());
						getOutput().append(" ");
						getOutput().append(eCNS.toLowerCase());
						getOutput().append("?");
					} else {
						getOutput().append("What are ");
						getOutput().append(eCNM.toLowerCase());
						getOutput().append("?");
					}
				}
			} else {
				extendsCls = pkg.getClass(eName);
			}
		}

		if (cNS.length()>0) {
			cls = getClass(dialog,model,pkg,cNS);
			if (cNM.length()==0 && cls==null) {
				if (extendsCls==null) {
					if (dialog.getLanguage().getCode().equals(Language.ENG)) {
						getOutput().append("What is ");
						getOutput().append(cIA.toLowerCase());
						getOutput().append(" ");
						getOutput().append(cNS.toLowerCase());
						getOutput().append("?");
					}
					clearClassVariables = false;
					clearExtendedClassVariables = false;
					clearPropertyVariables = false;
				} else {
					cls = addClass(dialog,model,pkg,cNS,extendsCls);
				}
			}
		}

		if (cNM.length()>0) {
			boolean addedTranslation = false;
			if (cls!=null && extendsCls==null) {
				String name = model.getTranslator().getName(dialog.getLanguage().getCode(),true,cNS);
				if (name.length()>0) {
					model.getTranslator().addTranslation(name,dialog.getLanguage().getCode(),false,cNM);
					addedTranslation = true;
				}
			}
			if (!addedTranslation) {
				String name = model.getTranslator().getName(dialog.getLanguage().getCode(),false,cNM);
				if (name.length()==0) {
					if (dialog.getLanguage().getCode().equals(Language.ENG)) {
						getOutput().append("What are ");
						getOutput().append(cNM.toLowerCase());
						getOutput().append("?");
					}
					clearClassVariables = false;
					clearExtendedClassVariables = false;
					clearPropertyVariables = false;
				}
			}
		}

		if (cls!=null) {
			boolean pList = false;
			if (pNM.length()>0) {
				String name = model.getTranslator().getName(dialog.getLanguage().getCode(),false,pNM);
				if (name.length()>0) {
					pNS = model.getTranslator().getTranslation(name,dialog.getLanguage().getCode(),true);
					if (cNS.length()>0 && cNM.length()==0) {
						pList = true;
					}
				}
			}
			if (pNS.length()>0) {
				String pName = model.getTranslator().getName(dialog.getLanguage().getCode(),true,pNS);
				String pType = "";
				if (pName.length()>0) {
					ModelClass pCls = pkg.getClass(pName);
					if (pCls!=null && pCls.getExtendsClass()!=null) {
						pType = pCls.getExtendsClass().getFullName();
					}
				} else {
					pName = model.getTranslator().addTranslation(pNS,dialog.getLanguage().getCode(),true,pNS);
				}
				pName = pName.toLowerCase();
				AddProperty addProp = new AddProperty(model.getBasePackageName(),cls.getName(),pName);
				if (pType.length()>0) {
					addProp.setType(pType);
				}
				if (pList) {
					addProp.setList("" + pList);
				}
				model.applyTransformation(addProp);
			}
		}

		if (clearClassVariables) {
			setDialogVariable(handler,ModelDialogFactory.classInArt,"");
			setDialogVariable(handler,ModelDialogFactory.classNameSingle,"");
			setDialogVariable(handler,ModelDialogFactory.classNameMulti,"");
		}
		if (clearExtendedClassVariables) {
			setDialogVariable(handler,ModelDialogFactory.extendedClassInArt,"");
			setDialogVariable(handler,ModelDialogFactory.extendedClassNameSingle,"");
			setDialogVariable(handler,ModelDialogFactory.extendedClassNameMulti,"");
		}
		if (clearPropertyVariables) {
			setDialogVariable(handler,ModelDialogFactory.propertyInArt,"");
			setDialogVariable(handler,ModelDialogFactory.propertyNameSingle,"");
			setDialogVariable(handler,ModelDialogFactory.propertyNameMulti,"");
		}
	}

	private ModelClass getClass(Dialog dialog,DialogModel model,ModelPackage pkg,String classNameSingle) {
		ModelClass r = null;
		String name = model.getTranslator().getName(dialog.getLanguage().getCode(),true,classNameSingle);
		r = pkg.getClass(name);
		return r;
	}
	
	private ModelClass addClass(Dialog dialog,DialogModel model,ModelPackage pkg,String classNameSingle,ModelClass extendsCls) {
		ModelClass r = null;
		String name = model.getTranslator().addTranslation(classNameSingle,dialog.getLanguage().getCode(),true,classNameSingle);
		AddClass addCls = new AddClass(model.getBasePackageName(),name);
		if (extendsCls!=null) {
			addCls.setExtendsPackageName(extendsCls.getPack().getName());
			addCls.setExtendsClassName(extendsCls.getName());
		}
		model.applyTransformation(addCls);
		pkg = getBasePackage(model);
		r = pkg.getClass(name);
		return r;
	}
	
	private ModelPackage getBasePackage(DialogModel model) {
		ModelPackage r = null;
		List<ModelPackage> packages = model.getPackagesCopy();
		for (ModelPackage pack: packages) {
			if (pack.getName().equals(model.getBasePackageName())) {
				r = pack;
				break;
			}
		}
		return r;
	}
	
	private String upperCaseFirst(String str) {
		String r = "";
		if (str.length()>0) {
			if (str.length()>1) {
				r = str.substring(0,1).toUpperCase() + str.substring(1);
			} else {
				r = str.toUpperCase();
			}
		}
		return r;
	}
}
