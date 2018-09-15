package nl.zeesoft.znlb.prepro;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.znlb.ZNLBConfig;
import nl.zeesoft.znlb.mod.ModZNLB;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseResponse;
import nl.zeesoft.zodb.db.DatabaseResult;

public class Preprocessor extends Locker implements JsClientListener {
	private Config							configuration	= null;
	private List<PreprocessorStateListener>	listeners		= new ArrayList<PreprocessorStateListener>();
	
	private List<LanguagePreprocessor>		processors		= new ArrayList<LanguagePreprocessor>();
	
	private boolean							initializing	= false;
	private boolean							initialized		= false;
	private int								todo			= 0;
	
	public Preprocessor(Config config) {
		super(config.getMessenger());
		configuration = config;
	}
	
	public void addListener(PreprocessorStateListener listener) {
		lockMe(this);
		listeners.add(listener);
		unlockMe(this);
	}
	
	public void install() {
		lockMe(this);
		processors.add(new EnglishPreprocessor());
		processors.add(new DutchPreprocessor());
		for (LanguagePreprocessor pp: processors) {
			pp.initializeReplacements();
		}
		addPreprocessorsToDatabaseNoLock();
		unlockMe(this);
	}
	
	public void initialize() {
		lockMe(this);
		if (!initializing && !initialized) {
			initializing = true;
			listPreprocessorsInDatabaseNoLock();
		}
		unlockMe(this);
	}
	
	public void destroy() {
		stateChanged(false);
		lockMe(this);
		processors.clear();
		unlockMe(this);
	}
	
	public boolean isInitialized() {
		boolean r = false;
		lockMe(this);
		r = initialized;
		unlockMe(this);
		return r;
	}

	public ZStringSymbolParser process(ZStringSymbolParser sequence) {
		return process(sequence,null);
	}
	
	public ZStringSymbolParser process(ZStringSymbolParser sequence,List<String> languages) {
		ZStringSymbolParser r = sequence;
		r.trim();
		if (r.length()>0) {
			List<String> symbols = r.toSymbolsPunctuated();
			String end = symbols.get(symbols.size() - 1);
			if (!ZStringSymbolParser.isLineEndSymbol(end)) {
				symbols.add(".");
			}
			r.fromSymbols(symbols,true,true);
			lockMe(this);
			for (LanguagePreprocessor pp: processors) {
				if (languages==null || languages.size()==0 || languages.contains(pp.getLanguage())) {
					r = pp.process(r);
				}
			}
			unlockMe(this);
		}
		return r;
	}

	@Override
	public void handledRequest(JsClientResponse response) {
		DatabaseResponse res = configuration.handledDatabaseRequest(response);
		if (res!=null) {
			if (response.error.length()>0) {
				configuration.error(this,response.error.toString(),response.ex);
				if (res.request.type.equals(DatabaseRequest.TYPE_GET)) {
					lockMe(this);
					todo--;
					unlockMe(this);
				}
			} else {
				if (res.request.type.equals(DatabaseRequest.TYPE_LIST)) {
					if (res.results.size()==0) {
						install();
						stateChanged(true);
					} else {
						lockMe(this);
						todo = 0;
						for (DatabaseResult result: res.results) {
							String[] elem = result.name.split("/");
							String language = ((ZNLBConfig)configuration).getLanguages().getCodeForName(elem[(elem.length - 1)]);
							LanguagePreprocessor pp = new LanguagePreprocessor();
							pp.setLanguage(language);
							processors.add(pp);
							todo++;
						}
						getPreprocessorsFromDatabaseNoLock();
						unlockMe(this);
					}
				} else if (res.request.type.equals(DatabaseRequest.TYPE_GET)) {
					lockMe(this);
					todo--;
					LanguagePreprocessor lpp = null;
					for (LanguagePreprocessor pp: processors) {
						if (getLanguagePreprocessorName(pp).equals(res.request.name)) {
							lpp = pp;
							break;
						}
					}
					if (lpp!=null && res.results.size()>0 && res.results.get(0).obj!=null) {
						lpp.fromJson(res.results.get(0).obj);
						configuration.debug(this,"Loaded " + getLanguagePreprocessorName(lpp) + ", replacements: " + lpp.getReplacements().size());
					}
					unlockMe(this);
				}
			}
			if (res.request.type.equals(DatabaseRequest.TYPE_GET)) {
				boolean open = false;
				lockMe(this);
				if (todo==0) {
					open = true;
				}
				unlockMe(this);
				if (open) {
					stateChanged(true);
				}
			}
		}
	}
	
	private void addPreprocessorsToDatabaseNoLock() {
		for (LanguagePreprocessor pp: processors) {
			DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_ADD);
			request.name = getLanguagePreprocessorName(pp);
			request.obj = pp.toJson();
			configuration.handleDatabaseRequest(request,this);
		}
	}

	private void listPreprocessorsInDatabaseNoLock() {
		DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_LIST);
		request.startsWith = ModZNLB.NAME + "/Preprocessors/";
		request.max = 100;
		configuration.handleDatabaseRequest(request,this);
	}

	private void getPreprocessorsFromDatabaseNoLock() {
		for (LanguagePreprocessor pp: processors) {
			DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_GET);
			request.name = getLanguagePreprocessorName(pp);
			configuration.handleDatabaseRequest(request,this);
		}
	}
	
	private String getLanguagePreprocessorName(LanguagePreprocessor pp) {
		return ModZNLB.NAME + "/Preprocessors/" + ((ZNLBConfig)configuration).getLanguages().getNameForCode(pp.getLanguage());
	}
	
	private void stateChanged(boolean open) {
		lockMe(this);
		List<PreprocessorStateListener> lst = new ArrayList<PreprocessorStateListener>(listeners);
		if (open) {
			initializing = false;
			initialized = true;
		} else {
			initialized = false;
		}
		unlockMe(this);
		for (PreprocessorStateListener listener: lst) {
			listener.preprocessorStateChanged(open);
		}
	}
}
