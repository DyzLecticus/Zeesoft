package nl.zeesoft.zids.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zid.dialog.io.DialogJson;
import nl.zeesoft.zid.dialog.io.DialogQnATsv;
import nl.zeesoft.zspr.Language;

public class Dialogs {
	private List<Dialog> dialogs = new ArrayList<Dialog>();
	
	public String initialize(String installDir) {
		String err = "";
		String fileName = installDir + "dialogs.json";
		File file = new File(fileName);
		if (file.exists()) {
			JsFile json = new JsFile();
			err = json.fromFile(fileName);
			if (err.length()==0 && json.rootElement.children.size()==1) {
				DialogJson io = new DialogJson();
				for (JsElem dElem: json.rootElement.children.get(0).children) {
					JsFile dJson = new JsFile();
					dJson.rootElement = dElem;
					Dialog dialog = io.fromJson(dJson);
					if (dialog!=null && dialog.getName().length()>0) {
						dialogs.add(dialog);
					}
				}
			}
		}
		if (err.length()==0) {
			for (Language language: Language.getLanguages()) {
				err = fromQnATsv(installDir,language.getCode(),"");
				if (err.length()>0) {
					break;
				}
			}
		}
		return err;
	}

	public JsFile getDialogsJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		JsElem dialogsElem = new JsElem("dialogs",true);
		json.rootElement.children.add(dialogsElem);
		DialogJson io = new DialogJson();
		for (Dialog dialog: dialogs) {
			JsFile dj = io.toJson(dialog);
			dj.rootElement.name = null;
			dialogsElem.children.add(dj.rootElement);
		}
		return json;
	}

	public List<Dialog> getDialogs() {
		return dialogs;
	}

	public String fromQnATsv(String installDir,String languageCode,String controllerClassName) {
		String err = "";
		String fileName = installDir + "dialogs-" + languageCode + ".txt";
		File file = new File(fileName);
		if (file.exists()) {
			ZStringBuilder tsv = new ZStringBuilder();
			err = tsv.fromFile(fileName);
			if (err.length()==0) {
				DialogQnATsv io = new DialogQnATsv();
				List<Dialog> addDialogs = io.fromQnATsv(tsv,languageCode,controllerClassName,true);
				if (addDialogs.size()>0) {
					List<Dialog> testDialogs = new ArrayList<Dialog>(dialogs);
					for (Dialog da: addDialogs) {
						for (Dialog dt: testDialogs) {
							if (da.getName().equals(dt.getName()) && dt.getLanguage().getCode().equals(languageCode)) {
								dialogs.remove(dt);
							}
						}
						dialogs.add(da);
					}
				}
			}
		}
		return err;
	}
}
