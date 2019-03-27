package nl.zeesoft.zsmc.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonHandlerObject;
import nl.zeesoft.zsmc.db.ConfabulatorSet;
import nl.zeesoft.zsmc.db.KnowledgeBaseConfabulator;
import nl.zeesoft.zsmc.kb.KbContext;
import nl.zeesoft.zsmc.mod.ModZSMC;

public class JsonZSMCStateHandler extends JsonHandlerObject {
	public final static String	PATH	= "/state.json"; 
	
	public JsonZSMCStateHandler(Config config, ModObject mod) {
		super(config,mod,PATH);
		setAllowPost(true);
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		ZStringBuilder r = new ZStringBuilder();
		ModObject mod = getConfiguration().getModule(ModZSMC.NAME);
		if (mod==null) {
			r = setResponse(response,405,ModZSMC.NAME + " module not found");
		} else {
			ModZSMC zsc = (ModZSMC) mod;
			if (method.equals("GET")) {
				if (zsc.getConfabulatorSetLoader().isInitializing()) {
					r = setResponse(response,503,"Confabulator set is initializing. Please wait.");
				} else if (!zsc.getConfabulatorSetLoader().isInitialized()) {
					r = setResponse(response,503,"Confabulator set is not loaded yet. Please wait.");
				} else {
					ConfabulatorSet cs = zsc.getConfabulatorManager().getConfabulatorSet();
					if (cs==null) {
						r = setResponse(response,503,"Confabulator set is not loaded yet. Please wait.");
					} else {
						JsFile json = cs.toJson();
						JsElem confsElem = json.rootElement.getChildByName("confabulators");
						if (confsElem!=null) {
							for (JsElem confElem: confsElem.children) {
								String name = confElem.getChildString("name");
								if (name.length()>0) {
									KnowledgeBaseConfabulator conf = cs.getConfabulator(name);
									String state = "Initializing ...";
									if (conf!=null && conf.getKnowledgeBase()!=null) {
										KbContext ctxt = conf.getKnowledgeBase().getContext("");
										if (ctxt!=null) {
											state = "Links: " + ctxt.linkTotalCount;
										}
									}
									confElem.children.add(new JsElem("state",state,true));
								}
							}
						}
						if (getConfiguration().isDebug()) {
							r = json.toStringBuilderReadFormat();
						} else {
							r = json.toStringBuilder();
						}
					}
				}
			} else {
				JsFile json = getPostBodyJson(request, response);
				if (json.rootElement==null) {
					r = setResponse(response,400,"Failed to parse JSON");
				} else {
					String action = json.rootElement.getChildString("action");
					if (action.length()==0) {
						r = setResponse(response,400,"action is mandatory");
					} else {
						if (action.equals("retrain")) {
							if (!zsc.getConfabulatorSetLoader().isInitialized() || !zsc.getConfabulatorManager().isInitialized()) {
								r = setResponse(response,503,"Confabulator set is not initialized yet. Please wait.");
							} else {
								String name = json.rootElement.getChildString("name");
								if (name.length()==0) {
									r = setResponse(response,400,"Confabulator name is mandatory");
								} else if (zsc.getConfabulatorManager().getConfabulator(name)==null) {
									r = setResponse(response,400,"Confabulator not found: " + name);
								} else if (zsc.getConfabulatorManager().retrainConfabulator(name)) {
									r = setResponse(response,200,"Confabulator is retraining");
								} else {
									r = setResponse(response,503,"Confabulator is already retraining. Please wait.");
								}
							}
						} else if (action.equals("reinitialize")) {
							if (!zsc.getConfabulatorSetLoader().isInitialized() || !zsc.getConfabulatorManager().isInitialized()) {
								r = setResponse(response,503,"Confabulator set is not initialized yet. Please wait.");
							} else {
								zsc.getConfabulatorSetLoader().reinitialize();
								r = setResponse(response,200,"Confabulator set is reinitializing");
							}
						} else {
							r = setResponse(response,400,"action is not supported: " + action);
						}
					}
				}
			}
		}
		return r;
	}
}
