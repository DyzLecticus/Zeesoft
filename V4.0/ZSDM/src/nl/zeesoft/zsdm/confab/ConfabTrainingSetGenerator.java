package nl.zeesoft.zsdm.confab;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsClient;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zevt.mod.ModZEVT;
import nl.zeesoft.zevt.mod.handler.JsonZEVTEntitiesHandler;
import nl.zeesoft.znlb.context.Language;
import nl.zeesoft.znlb.context.MasterContext;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseResponse;
import nl.zeesoft.zodb.db.DatabaseResult;
import nl.zeesoft.zodb.db.StateListener;
import nl.zeesoft.zsc.confab.TrainingSet;
import nl.zeesoft.zsc.mod.ModZSC;
import nl.zeesoft.zsdm.dialog.Dialog;
import nl.zeesoft.zsdm.dialog.DialogExample;
import nl.zeesoft.zsdm.dialog.DialogVariable;
import nl.zeesoft.zsdm.mod.ModZSDM;

public class ConfabTrainingSetGenerator extends Locker implements JsClientListener {
	private Config							configuration		= null;
	private ConfabConfigurator				configurator		= null;
	
	private List<Dialog>					dialogs				= new ArrayList<Dialog>();
	private SortedMap<String,List<String>>	entityValues		= new TreeMap<String,List<String>>();
	private SortedMap<String,Long>			trainingSetIds		= new TreeMap<String,Long>();
	private int								todo				= 0;

	private List<StateListener>	listeners			= new ArrayList<StateListener>();

	public ConfabTrainingSetGenerator(Config config, ConfabConfigurator confabConfig) {
		super(config.getMessenger());
		configuration = config;
		configurator = confabConfig;
	}

	public void addListener(StateListener listener) {
		lockMe(this);
		listeners.add(listener);
		unlockMe(this);
	}

	public boolean isGenerating() {
		boolean r = false;
		lockMe(this);
		r = (todo > 0);
		unlockMe(this);
		return r;
	}

	public void generateTrainingSets() {
		generateTrainingSets(true);
	}

	public void generateTrainingSets(boolean includeLanguages) {
		if (configurator.getLanguages().size()>0) {
			boolean doIt = false;
			lockMe(this);
			if (todo==0) {
				doIt = true;
				dialogs.clear();
				entityValues.clear();
				trainingSetIds.clear();
				if (includeLanguages) {
					todo = 1;
				}
				for (Language lang: configurator.getLanguages()) {
					todo += (lang.masterContexts.size() + 1);
				}
			}
			unlockMe(this);
			if (doIt) {
				stateChanged();
	
				DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_GET);
				request.startsWith = ModZSDM.NAME + "/Dialogs/";
				configuration.handleDatabaseRequest(request,this);
				
				request = new DatabaseRequest(DatabaseRequest.TYPE_LIST);
				request.startsWith = ModZSC.NAME + "/TrainingSets/";
				request.max = 1000;
				configuration.handleDatabaseRequest(request,this);
	
				if (includeLanguages) {
					JsClient client = new JsClient(configuration.getMessenger(),configuration.getUnion());
					client.addJsClientListener(this);
					client.handleRequest(configuration.getModuleUrl(ModZEVT.NAME) + JsonZEVTEntitiesHandler.PATH);
				}
			}
		}
	}
	
	public void retrainConfabulators() {
		retrainConfabulators(true);
	}

	public void retrainConfabulators(boolean includeLanguages) {
		if (configurator.getLanguages().size()>0) {
			// TODO: Implement
		}
	}

	@Override
	public void handledRequest(JsClientResponse response) {
		boolean done = false;
		if (response.error.length()>0) {
			configuration.error(this,response.error.toString(),response.ex);
		}
		DatabaseResponse res = configuration.handledDatabaseRequest(response);
		if (res!=null) {
			boolean checkGenerateContext = false;
			if (res.request.type.equals(DatabaseRequest.TYPE_GET)) {
				lockMe(this);
				for (DatabaseResult result: res.results) {
					Dialog dialog = new Dialog();
					dialog.fromJson(result.obj);
					dialogs.add(dialog);
				}
				checkGenerateContext = true;
				unlockMe(this);
			} else if (res.request.type.equals(DatabaseRequest.TYPE_LIST)) {
				lockMe(this);
				for (DatabaseResult result: res.results) {
					String name = result.name.substring(res.request.startsWith.length());
					trainingSetIds.put(name,result.id);
				}
				checkGenerateContext = true;
				unlockMe(this);
			} else if (res.request.type.equals(DatabaseRequest.TYPE_SET)) {
				lockMe(this);
				todo--;
				if (todo==0) {
					done = true;
				}
				unlockMe(this);
			}
			if (checkGenerateContext) {
				lockMe(this);
				List<Dialog> copyDialogs = new ArrayList<Dialog>(dialogs);
				int tsSize = trainingSetIds.size();
				unlockMe(this);
				if (copyDialogs.size()>0 && tsSize>0) {
					generateContextTrainingSets(copyDialogs);
				}
			}
		} else {
			if (response.response!=null && response.response.rootElement!=null) {
				lockMe(this);
				JsFile resJs = response.response;
				JsElem entsElem = resJs.rootElement.getChildByName("entities");
				if (entsElem!=null) {
					for (JsElem entElem: entsElem.children) {
						String language = entElem.getChildString("language");
						List<String> evs = entityValues.get(language);
						if (evs==null) {
							evs = new ArrayList<String>();
							entityValues.put(language,evs);
						}
						JsElem valsElem = entElem.getChildByName("entityValues");
						if (valsElem!=null) {
							for (JsElem valElem: valsElem.children) {
								if (valElem.value!=null) {
									evs.add(valElem.value.toString());
								}
							}
						}
					}
				}
				List<Dialog> copyDialogs = new ArrayList<Dialog>(dialogs);
				SortedMap<String,List<String>> copyEntityValues = new TreeMap<String,List<String>>(entityValues);
				unlockMe(this);
				if (copyDialogs.size()>0 && copyEntityValues.size()>0) {
					generateLanguageTrainingSet(copyDialogs,copyEntityValues);
				}
			}
		}
		if (done) {
			configuration.debug(this,"Updated training sets");
			stateChanged();
		}
	}

	protected void generateLanguageTrainingSet(List<Dialog> dialogs,SortedMap<String,List<String>> entityValues) {
		TrainingSet ts = new TrainingSet();
		ts.setName(configurator.getLanguageConfabulatorName());
		for (Dialog dialog: dialogs) {
			for (DialogExample example: dialog.getExamples()) {
				if (example.toLanguageClassifier) {
					ts.addSequence(example.input,new ZStringSymbolParser(dialog.getLanguage()));
				}
			}
			for (DialogVariable variable: dialog.getVariables()) {
				for (ZStringSymbolParser prompt: variable.prompts) {
					ts.addSequence(prompt,new ZStringSymbolParser(dialog.getLanguage()));
				}
			}
		}
		for (Entry<String,List<String>> entry: entityValues.entrySet()) {
			for (String ev: entry.getValue()) {
				ZStringSymbolParser sequence = new ZStringSymbolParser(ev);
				sequence.append(".");
				ts.addSequence(sequence,new ZStringSymbolParser(entry.getKey()));
				ZStringSymbolParser ucfSequence = new ZStringSymbolParser(ev);
				ucfSequence.upperCaseFirst();
				ucfSequence.append(".");
				ts.addSequence(ucfSequence,new ZStringSymbolParser(entry.getKey()));
			}
		}
		updateTrainingSet(ts,60);
	}

	protected void generateContextTrainingSets(List<Dialog> dialogs) {
		TrainingSet ts = null;
		for (Language lang: configurator.getLanguages()) {
			ts = getMasterContextTrainingSet(lang,dialogs);
			updateTrainingSet(ts);
			for (MasterContext mc: lang.masterContexts) {
				ts = getContextTrainingSet(lang,mc,dialogs);
				updateTrainingSet(ts);
			}
		}
	}
	
	protected void updateTrainingSet(TrainingSet ts) {
		updateTrainingSet(ts,0);
	}
	
	protected void updateTrainingSet(TrainingSet ts,int timeoutSeconds) {
		lockMe(this);
		if (trainingSetIds.containsKey(ts.getName())) {
			configuration.debug(this,"Updating training set: " + ts.getName() + ", sequences: " + ts.getSequences().size());
			long id = trainingSetIds.get(ts.getName());
			DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_SET);
			request.id = id;
			request.obj = ts.toJson();
			configuration.handleDatabaseRequest(request,this,timeoutSeconds);
		}
		unlockMe(this);
	}
	
	protected TrainingSet getMasterContextTrainingSet(Language lang,List<Dialog> dialogs) {
		TrainingSet r = new TrainingSet();
		r.setName(configurator.getMasterContextConfabulatorName(lang));
		for (Dialog dialog: dialogs) {
			if (dialog.getLanguage().equals(lang.code)) {
				for (DialogExample example: dialog.getExamples()) {
					if (example.toMasterClassifier) {
						ZStringSymbolParser sequence = dialog.getTypedSequence(example.input);
						r.addSequence(sequence,new ZStringSymbolParser(dialog.getMasterContext()));
					}
				}
				for (DialogVariable variable: dialog.getVariables()) {
					for (ZStringSymbolParser prompt: variable.prompts) {
						r.addSequence(prompt,new ZStringSymbolParser(dialog.getMasterContext()));
					}
				}
			}
		}
		return r;
	}
	
	protected TrainingSet getContextTrainingSet(Language lang,MasterContext mc,List<Dialog> dialogs) {
		TrainingSet r = new TrainingSet();
		r.setName(configurator.getContextConfabulatorName(lang,mc));
		for (Dialog dialog: dialogs) {
			if (dialog.getLanguage().equals(lang.code) &&
				dialog.getMasterContext().equals(mc.name)
				) {
				for (DialogExample example: dialog.getExamples()) {
					if (example.toContextClassifier) {
						ZStringSymbolParser sequence = dialog.getTypedSequence(example.input);
						r.addSequence(sequence,new ZStringSymbolParser(dialog.getContext()));
					}
				}
				for (DialogVariable variable: dialog.getVariables()) {
					for (ZStringSymbolParser prompt: variable.prompts) {
						r.addSequence(prompt,new ZStringSymbolParser(dialog.getContext()));
					}
				}
			}
		}
		return r;
	}
	
	private void stateChanged() {
		lockMe(this);
		boolean open = (todo==0);
		List<StateListener> list = new ArrayList<StateListener>(listeners);
		unlockMe(this);
		for (StateListener listener: list) {
			listener.stateChanged(this,open);
		}
	}
}
