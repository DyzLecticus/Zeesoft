package nl.zeesoft.zacs.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zacs.database.model.Module;
import nl.zeesoft.zacs.database.model.ModuleSymbol;
import nl.zeesoft.zacs.database.model.Symbol;
import nl.zeesoft.zacs.database.model.ZACSModel;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.gui.GuiController;
import nl.zeesoft.zodb.database.model.helpers.HlpControllerObject;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.event.EvtEvent;

public class SimControllerModules extends HlpControllerObject {
	private SortedMap<Integer,Module>		modulesByNum		= new TreeMap<Integer,Module>();
	private SortedMap<Long,Module>			modulesById			= new TreeMap<Long,Module>();
	private SortedMap<Long,ModuleSymbol>	moduleSymbolsById	= new TreeMap<Long,ModuleSymbol>();

	private ReqGet							getRequest			= null;
	private ReqAdd							addRequest			= null;
	
	private int								getStart			= 0;

	private boolean							showProgress		= true;
	
	@Override
	protected void initialize() {
		getModules();
	}

	protected Module getModuleById(long id) {
		return modulesById.get(id);
	}

	protected Module getModuleByNum(int num) {
		return modulesByNum.get(num);
	}

	protected ModuleSymbol getModuleSymbolById(long id) {
		ModuleSymbol r = null;
		lockMe(this);
		r = moduleSymbolsById.get(id);
		unlockMe(this);
		return r;
	}

	protected List<Module> getModulesAsList() {
		List<Module> r = new ArrayList<Module>();
		for (Module module: modulesByNum.values()) {
			r.add(module);
		}
		return r;
	}

	protected long getTotalSymbolLevel() {
		long r = 0;
		if (modulesById.size()>0) {
			for (Module module: modulesById.values()) {
				r = r + module.getTotalSymbolLevel();
			}
		}
		return r;
	}

	protected Module getPrevModuleForModule(Module mod) {
		int getNum = mod.getNum() - 1;
		if (getNum == 0) {
			getNum = ZACSModel.getNumberOfModules();
		}
		return modulesByNum.get(getNum);
	}

	protected Module getNextModuleForModule(Module mod) {
		int getNum = mod.getNum() + 1;
		if (getNum > ZACSModel.getNumberOfModules()) {
			getNum = 1;
		}
		return modulesByNum.get(getNum);
	}

	protected void setShowProgress(boolean showProgress) {
		this.showProgress = showProgress;
	}
	
	private void getModules() {
		setDone(false);
		addRequest = null;
		getRequest = new ReqGet(ZACSModel.MODULE_CLASS_FULL_NAME);
		getRequest.getProperties().add(ReqGet.ALL_PROPERTIES);
		getRequest.addSubscriber(this);
		DbRequestQueue.getInstance().addRequest(getRequest,this);
		waitTillDone();
	}
	
	protected void getModuleSymbols() {
		setDone(false);
		getStart = 0;
		resetGetModuleSymbolRequest();
		DbRequestQueue.getInstance().addRequest(getRequest,this);
		waitTillDone();
	}

	protected void addModuleSymbolsForSymbolIdList(List<Long> symbolIdList) {
		if (symbolIdList.size()>0 && modulesById.size()>0) {
			setDone(false);
			getRequest = null;
			addRequest = new ReqAdd(ZACSModel.MODULE_SYMBOL_CLASS_FULL_NAME);
			addRequest.addSubscriber(this);
			for (Module mod: modulesById.values()) {
				for (long id: symbolIdList) {
					ModuleSymbol modSym = new ModuleSymbol();
					modSym.setModuleId(mod.getId());
					modSym.setSymbolId(id);
					addRequest.getObjects().add(new ReqDataObject(modSym.toDataObject()));
				}
			}
			DbRequestQueue.getInstance().addRequest(addRequest,this);
			waitTillDone();
			getModuleSymbols();
		}
	}
	
	@Override
	public void handleEvent(EvtEvent e) {
		resetTimeOut();
		if (getRequest!=null && e.getValue()==getRequest) {
			if (getRequest.getClassName().equals(ZACSModel.MODULE_CLASS_FULL_NAME)) {
				if (getRequest.getObjects().size()>0) {
					for (ReqDataObject object: getRequest.getObjects()) {
						Module mod = new Module();
						mod.fromDataObject(object.getDataObject());
						if (!modulesByNum.containsKey(mod.getNum())) {
							modulesByNum.put(mod.getNum(),mod);
							modulesById.put(mod.getId(),mod);
							if (mod.getSymbolInputId()>0) {
								mod.setSymbolInput(SimController.getInstance().getSymbols().getSymbolById(mod.getSymbolInputId()));
							}
							if (mod.getSymbolOutputId()>0) {
								mod.setSymbolOutput(SimController.getInstance().getSymbols().getSymbolById(mod.getSymbolOutputId()));
							}
						}
					}
				}
				setDone(true);
			} else if (getRequest.getClassName().equals(ZACSModel.MODULE_SYMBOL_CLASS_FULL_NAME)) {
				if (getRequest.getObjects().size()>0) {
					for (ReqDataObject object: getRequest.getObjects()) {
						ModuleSymbol modSym = new ModuleSymbol();
						modSym.fromDataObject(object.getDataObject());
						lockMe(this);
						if (!moduleSymbolsById.containsKey(modSym.getId())) {
							Module mod = modulesById.get(modSym.getModuleId());
							Symbol sym = SimController.getInstance().getSymbols().getSymbolById(modSym.getSymbolId());
							if (mod!=null && sym!=null) {
								modSym.setModule(mod);
								modSym.setSymbol(sym);
								mod.addSymbol(modSym);
								moduleSymbolsById.put(modSym.getId(),modSym);
							}
						}
						unlockMe(this);
					}
					if (showProgress) {
						GuiController.getInstance().incrementProgressFrameDone(getRequest.getObjects().size());
					}
					if (getRequest.getObjects().size()==getRequest.getLimit()) {
						getStart += getRequest.getLimit();
						resetGetModuleSymbolRequest();
						DbRequestQueue.getInstance().addRequest(getRequest,this);
					} else {
						if (showProgress) {
							Messenger.getInstance().debug(this,"Loaded module symbol objects: " + moduleSymbolsById.size());
						}
						setDone(true);
					}
				} else {
					if (getRequest.hasError()) {
						Messenger.getInstance().error(this,"Empty request results, error: " + getRequest.getErrors().get(0).getMessage());
					} else if (showProgress) {
						Messenger.getInstance().debug(this,"Loaded module symbol objects: " + moduleSymbolsById.size());
					}
					setDone(true);
				}
			}
		} else if (addRequest!=null && e.getValue()==addRequest) {
			setDone(true);
		}
	}
	
	@Override
	protected void whileWaiting() {
		GuiController.getInstance().refreshProgressFrame();
	}
	
	private void resetGetModuleSymbolRequest() {
		resetGetRequest(ZACSModel.MODULE_SYMBOL_CLASS_FULL_NAME);
	}

	private void resetGetRequest(String className) {
		addRequest = null;
		getRequest = new ReqGet(className);
		getRequest.getProperties().add(ReqGet.ALL_PROPERTIES);
		getRequest.addSubscriber(this);
		getRequest.setStart(getStart);
		getRequest.setLimit(100);
	}
}
