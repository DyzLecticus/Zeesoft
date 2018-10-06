package nl.zeesoft.zsc.confab;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsc.confab.confabs.ContextResult;
import nl.zeesoft.zsc.confab.confabs.Correction;

public class ConfabulatorResponse implements JsAble {
	public ConfabulatorRequest	request			= null;
	
	public ZStringBuilder		error			= new ZStringBuilder();
	public ZStringBuilder		log				= new ZStringBuilder();
	
	public ZStringSymbolParser	corrected		= new ZStringSymbolParser();
	public List<Correction>		corrections		= new ArrayList<Correction>();
	
	public List<ContextResult>	contextResults	= new ArrayList<ContextResult>();

	public ZStringSymbolParser	extension		= new ZStringSymbolParser();

	public ConfabulatorResponse() {
		
	}
	
	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("log",log,true));
		if (error.length()>0) {
			json.rootElement.children.add(new JsElem("error",error,true));
		}
		if (contextResults.size()>0) {
			JsElem ctxsElem = new JsElem("contexts",true);
			json.rootElement.children.add(ctxsElem);
			for (ContextResult context: contextResults) {
				JsElem ctxElem = new JsElem();
				ctxsElem.children.add(ctxElem);
				ctxElem.children.add(new JsElem("symbol",context.contextSymbol,true));
				ctxElem.children.add(new JsElem("prob","" + context.prob));
				ctxElem.children.add(new JsElem("probNormalized","" + context.probNormalized));
			}
		} else if (corrected.length()>0) {
			json.rootElement.children.add(new JsElem("corrected",corrected,true));
			JsElem corsElem = new JsElem("corrections",true);
			json.rootElement.children.add(corsElem);
			for (Correction correction: corrections) {
				JsElem corElem = new JsElem();
				corsElem.children.add(corElem);
				corElem.children.add(new JsElem("index","" + correction.index));
				corElem.children.add(new JsElem("symbol",correction.symbol,true));
				corElem.children.add(new JsElem("correction",correction.correction,true));
			}
		} else if (extension.length()>0) {
			json.rootElement.children.add(new JsElem("extension",extension,true));
		}
		return json;
	}
	
	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			contextResults.clear();
			corrections.clear();
			log = json.rootElement.getChildZStringBuilder("log",log);
			error = json.rootElement.getChildZStringBuilder("error",error);
			JsElem ctxsElem = json.rootElement.getChildByName("contexts");
			if (ctxsElem!=null) {
				for (JsElem ctxElem: ctxsElem.children) {
					ContextResult context = new ContextResult();
					context.contextSymbol = ctxElem.getChildString("symbol");
					context.prob = ctxElem.getChildDouble("prob");
					context.probNormalized = ctxElem.getChildDouble("probNormalized");
					contextResults.add(context);
				}
			}
			corrected = json.rootElement.getChildZStringSymbolParser("corrected",corrected);
			JsElem corsElem = json.rootElement.getChildByName("corrections");
			if (corsElem!=null) {
				for (JsElem corElem: corsElem.children) {
					Correction correction = new Correction();
					correction.index = corElem.getChildInt("index");
					correction.symbol = corElem.getChildString("symbol");
					correction.correction = corElem.getChildString("correction");
					corrections.add(correction);
				}
			}
			extension = json.rootElement.getChildZStringSymbolParser("extension",extension);
		}
	}	
}
