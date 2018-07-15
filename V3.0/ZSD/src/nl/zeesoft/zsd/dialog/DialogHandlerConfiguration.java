package nl.zeesoft.zsd.dialog;

import java.io.File;
import java.util.List;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.initialize.InitializeClass;
import nl.zeesoft.zsd.interpret.InterpreterConfiguration;
import nl.zeesoft.zsd.util.LanguageDialogSetJsonGenerator;

public class DialogHandlerConfiguration extends InterpreterConfiguration {
	private DialogSet					dialogSet					= null;
	
	public DialogHandlerConfiguration() {
		
	}
	
	public DialogHandlerConfiguration(BaseConfiguration base) {
		super(base);
	}
	
	@Override
	public List<InitializeClass> getInitializeClasses() {
		List<InitializeClass> r = super.getInitializeClasses();
		
		if (dialogSet==null) {
			dialogSet = new DialogSet();
		}
		InitializeClass c = new InitializeClass();
		c.name = "DialogSet";
		c.obj = dialogSet;
		for (String language: getBase().getSupportedLanguages()) {
			String fileName = LanguageDialogSetJsonGenerator.FILE_NAME_PREFIX + language + ".json";
			String override = getBase().getOverrideDir() + fileName;
			File test = new File(override);
			if (test.exists()) {
				c.fileNames.add(override);
			} else {
				c.fileNames.add(getBase().getBaseDir() + fileName);
			}
			String extend = getBase().getExtendDir() + fileName;
			test = new File(extend);
			if (test.exists()) {
				c.fileNames.add(extend);
			}
		}
		r.add(c);
		
		return r;
	}


	public DialogSet getDialogSet() {
		return dialogSet;
	}

	public void setDialogSet(DialogSet dialogSet) {
		this.dialogSet = dialogSet;
	}
}
