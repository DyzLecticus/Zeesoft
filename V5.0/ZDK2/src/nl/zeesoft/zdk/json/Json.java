package nl.zeesoft.zdk.json;

import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Json {
	public JsonExceptionHandler		exceptionHandler	= null;
	public JElem					root				= null;
	
	public StringBuilder toStr() {
		StringBuilder r = new StringBuilder();
		if (root!=null) {
			r = root.toStr();
		}
		return r;
	}

	public void fromStr(StringBuilder str) {
		Map<String,Object> elems = getElements(str);
        root = new JElem();
        root.fromElements(elems);
	}
	
	protected Map<String,Object> getElements(StringBuilder str) {
		Map<String,Object> r = null;
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByName("javascript");
        str.insert(0, "Java.asJSONCompatible(");
        str.append(")");
        Object result;
		try {
			result = engine.eval(str.toString());
	        @SuppressWarnings("unchecked")
			Map<String,Object> elems = (Map<String,Object>) result;
	        r = elems;
		} catch (ScriptException ex) {
			if (exceptionHandler!=null) {
				exceptionHandler.handleException(this, ex);
			} else {
				JsonExceptionHandler.handleExceptionDefault(this, ex);
			}
		}
		return r; 
	}
}
