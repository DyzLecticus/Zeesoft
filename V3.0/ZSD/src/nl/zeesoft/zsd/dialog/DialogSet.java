package nl.zeesoft.zsd.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericHandshake;
import nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericQnA;
import nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericHandshake;
import nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericQnA;
import nl.zeesoft.zsd.initialize.Initializable;

public class DialogSet implements Initializable {
	private DialogIdentity							identity				= new DialogIdentity();
	private List<String>							languages				= new ArrayList<String>();
	private SortedMap<String,List<DialogObject>>	languageDialogs			= new TreeMap<String,List<DialogObject>>();
	private SortedMap<String,List<String>>			languageMasterContexts	= new TreeMap<String,List<String>>();

	public DialogSet() {
		for (DialogObject dialog: getDefaultDialogs()) {
			addDialog(dialog);
		}
	}
	
	public void setIdentity(DialogIdentity identity) {
		this.identity = identity;
	}

	@Override
	public void initialize(ZStringBuilder data) {
		// TODO: Read identity and dialogs from JSON?
		initialize();
	}
	
	public void initialize() {
		for (DialogObject dialog: getDialogs()) {
			dialog.setIdentity(identity);
			dialog.initialize();
		}
	}
	
	public void addDialog(DialogObject dialog) {
		List<DialogObject> dialogs = languageDialogs.get(dialog.getLanguage());
		if (dialogs==null) {
			dialogs = new ArrayList<DialogObject>();
			languageDialogs.put(dialog.getLanguage(),dialogs);
		}
		dialogs.add(dialog);
		if (!languages.contains(dialog.getLanguage())) {
			languages.add(dialog.getLanguage());
		}
		List<String> masterContexts = languageMasterContexts.get(dialog.getLanguage());  
		if (masterContexts==null) {
			masterContexts = new ArrayList<String>();
			languageMasterContexts.put(dialog.getLanguage(),masterContexts);
		}
		if (!masterContexts.contains(dialog.getMasterContext())) {
			masterContexts.add(dialog.getMasterContext());
		}
	}

	public List<DialogObject> getDialogs() {
		List<DialogObject> r = new ArrayList<DialogObject>();
		for (Entry<String,List<DialogObject>> entry: languageDialogs.entrySet()) {
			for (DialogObject dialog: entry.getValue()) {
				r.add(dialog);
			}
		}
		return r;
	}

	public List<DialogObject> getDialogs(String language) {
		return getDialogs(language,"","");
	}

	public List<DialogObject> getDialogs(String language,String masterContext) {
		return getDialogs(language,masterContext,"");
	}

	public List<DialogObject> getDialogs(String language,String masterContext,String context) {
		List<DialogObject> r = new ArrayList<DialogObject>();
		List<DialogObject> dialogs = languageDialogs.get(language);
		if (dialogs!=null) {
			if (masterContext.length()>0 || context.length()>0) {
				for (DialogObject dialog: dialogs) {
					if (
						(masterContext.length()==0 || dialog.getMasterContext().equals(masterContext)) &&
						(context.length()==0 || dialog.getContext().equals(context))
						) {
						r.add(dialog);
					}
				}
			} else {
				r = new ArrayList<DialogObject>(dialogs);
			}
		}
		return r;
	}

	public DialogIdentity getIdentity() {
		return identity;
	}

	public List<String> getLanguages() {
		return languages;
	}

	public SortedMap<String, List<String>> getLanguageMasterContexts() {
		return languageMasterContexts;
	}
	
	protected List<DialogObject> getDefaultDialogs() {
		List<DialogObject> r = new ArrayList<DialogObject>();
		r.add(new EnglishGenericHandshake());
		r.add(new EnglishGenericQnA());
		r.add(new DutchGenericHandshake());
		r.add(new DutchGenericQnA());
		return r;
	}
}
